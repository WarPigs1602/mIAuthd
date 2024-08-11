/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.midiandmore.miauthd;

import java.util.Arrays;
import java.util.regex.PatternSyntaxException;

/**
 * Parses the IAuthd
 *
 * @author Andreas Pschorn
 */
public class IAuthd {

    private MIAuthd mi;
    private long statsPasssed;
    private long statsUnthrottled;
    private long statsKilled;
    private long uniqueId;

    /**
     * Parses the IAuthd
     *
     * @param mi
     */
    protected IAuthd(MIAuthd mi) {
        setMi(mi);
        setUniqueId(0);
        setStatsKilled(0);
        setStatsUnthrottled(0);
        setStatsPasssed(0);
    }

    /**
     * Sends a text
     *
     * @param text The text
     * @param args The args like %d %s
     */
    protected void sendText(String text, Object... args) {
        System.out.println(text.formatted(args));
        System.out.flush();
    }

    /**
     * Sends a error message
     *
     * @param text The text
     * @param args The args like %d %s
     */
    protected void sendError(String text, Object... args) {
        System.err.println(text.formatted(args));
        System.err.flush();
    }

    protected void sendSnotice(String message, Object... args) {
        sendText("> :iauthd: %s", message.formatted(args));
    }

    protected void handleClose() {
        System.exit(0);
    }

    /**
     * Clears stats
     */
    protected void clearStats() {
        sendText("s");
    }

    /**
     * Adds a stats message
     *
     * @param message The message
     * @param args The args like %d %s
     */
    protected void addStats(String message, Object... args) {
        sendText("S midiandmore-iauthd :%s", message.formatted(args));
    }

    /**
     * Clears configs
     */
    protected void clearConfig() {
        sendText("a");
    }
    
    /**
     * Adds a configuration message
     *
     * @param message The message
     * @param args The args like %d %s
     */
    protected void addConfig(String message, Object... args) {
        sendText("A * midiandmore-iauthd :%s", message.formatted(args));
    }    

    /**
     * Parses the incoming line
     *
     * @param line The text
     */
    protected void parseLine(String line) {
        try {
            String[] tokens;
            if (line.isEmpty()) {
                return;
            }
            if (line.contains(" :")) {
                String[] trailing = line.split(" \\:", 2);
                tokens = trailing[0].split(" ", 3);
            } else {
                tokens = line.split(" ", 3);
            }
            if (tokens.length < 3) {
                return;
            }
            var id = tokens[0];
            if (id.equals("-1"))
                return;
            var command = tokens[1];
            var params = tokens[2].split(" ", 4);
            if (command.equals("W")) {
                if (params.length < 4) //too few parameters
                {
                    return;
                }
                getMi().getUser().get(id).setPassword(params[0]);
                getMi().getUser().get(id).setUsername(params[1]);
                var cloak = getMi().getConfig().getConfigFile().getOrDefault("CLOAK", "false").equals("true") ? 
                        getMi().getCloak().parseCloak(params[2]) : params[2];
                getMi().getUser().get(id).setHostname(cloak);
                getMi().getUser().get(id).setIp(params[3]);
                if (getMi().getUser().get(id).getPassword().equals(getMi().getConfig().getConfigFile().get("WEBIRC_PASS"))
                        && getMi().getUser().get(id).getUsername().equals(getMi().getConfig().getConfigFile().get("WEBIRC_USERNAME"))) {
                    sendText("N %s %s %s %s", id, getMi().getUser().get(id).getRemoteip(),
                            getMi().getUser().get(id).getRemoteport(), getMi().getUser().get(id).getHostname());
                    getMi().getUser().get(id).setWebirc(true);
                }
            } else if (command.equals("C")) {//new client
                if (params.length < 2) // too few parameters
                {
                    return;
                }
                getMi().getUser().put(id, new Clients(getMi(), id));
                getMi().getUser().get(id).setRemoteip(params[0]);
                getMi().getUser().get(id).setRemoteport(params[1]);
                getMi().getUser().get(id).setUniqueId(String.valueOf(getUniqueId()));
                setUniqueId(getUniqueId() + 1);
            } else if (command.equals("H")) {
                // hurry state (ircd has finished DNS
                // ident check
                var trust = getMi().getConfig().getConfigFile().get("TRUST_ENABLED").equals("true");
                if (trust && getMi().getSocketThread() != null && getMi().getSocketThread().getSocket() != null 
                        && getMi().getHandler().isAuthed()) {
                    getMi().getHandler().sendText("CHECK %s-%s %s %s",
                            id, getMi().getUser().get(id).getUniqueId(), getMi().getUser().get(id).getUsername(),
                            getMi().getUser().get(id).getRemoteip());
                } else {
                    sendVerdict("%s-%s".formatted(id, getMi().getUser().get(id).getUniqueId()), "PASS", "moo");
                }
            } else if (command.equalsIgnoreCase("u")) {  // trusted/untrusted username
                var username = params[0];
                var webirc = getMi().getUser().get(id).isWebirc();
                if (webirc) {
                    getMi().getUser().get(id).setUsername(username.startsWith("~") ? username.substring(1) : username);
                    sendText("U %s %s %s %s", id, getMi().getUser().get(id).getRemoteip(), getMi().getUser().get(id).getRemoteport(), getMi().getUser().get(id).getUsername());
                    return;
                }
                // untrusted username (i.e.non - working identd
                if (command.equals("U")) {
                    username = '~' + username;
                }
                if (getMi().getUser().get(id).getUsername() == null || !username.equals("~")) {
                    getMi().getUser().get(id).setUsername(username);
                }
            } else if (command.equals("D")) { // client disconnected
                if (getMi().getUser().containsKey(id)) {
                    getMi().getUser().remove(id);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace(System.err);
        }
    }

    protected void sendVerdict(String combinedId, String verdict, String message) {
        var tokens = combinedId.split("-", 2);
        if (tokens.length < 2) {
            return;
        }
        var id = tokens[0];
        var unique = tokens[1];
        if (!getMi().getUser().containsKey(id) || !getMi().getUser().get(id).getUniqueId().equals(unique)) {
            return;
        }
        if (verdict.equals("PASS")) {
            // Every 10000th accepted connection gets a free cow.
            if (getStatsPasssed() % 10000 == 0) {
                String[] cow = {"(__)", " oo\\\\\\~", "  !!!!"};

                for (String line : cow) {
                    sendText("C %s %s %s :%s",
                            id, getMi().getUser().get(id).getRemoteip(), getMi().getUser().get(id).getRemoteport(), line);
                }

            }
            if (message != null) {
                sendText("C %s %s %s :%s",
                        id, getMi().getUser().get(id).getRemoteip(), getMi().getUser().get(id).getRemoteport(), message);
            }

            sendText("D %s %s %s",
                    id, getMi().getUser().get(id).getRemoteip(), getMi().getUser().get(id).getRemoteport());

            setStatsPasssed(getStatsPasssed() + 1);
        } else if (verdict.equals("UNTHROTTLE")) {
            sendText("T %s %s %s",
                    id, getMi().getUser().get(id).getRemoteip(), getMi().getUser().get(id).getRemoteport());
            setStatsUnthrottled(getStatsUnthrottled() + 1);
            return; // not a final verdict, just an addition
        } else if (message == null) {
            message = "Connections from your host cannot be accepted at this time.";
            sendText("k %s %s %s :%s",
                    id, getMi().getUser().get(id).getRemoteip(), getMi().getUser().get(id).getRemoteport(), message);
            setStatsKilled(getStatsKilled() + 1);
            getMi().getUser().remove(id);
        }
    }

    /**
     * @return the mi
     */
    /**
     * @return the mi
     */
    public MIAuthd getMi() {
        return mi;
    }

    /**
     * @param mi the mi to set
     */
    public void setMi(MIAuthd mi) {
        this.mi = mi;
    }

    /**
     * @return the statsPasssed
     */
    public long getStatsPasssed() {
        return statsPasssed;
    }

    /**
     * @param statsPasssed the statsPasssed to set
     */
    public void setStatsPasssed(long statsPasssed) {
        this.statsPasssed = statsPasssed;
    }

    /**
     * @return the statsUnthrottled
     */
    public long getStatsUnthrottled() {
        return statsUnthrottled;
    }

    /**
     * @param statsUnthrottled the statsUnthrottled to set
     */
    public void setStatsUnthrottled(long statsUnthrottled) {
        this.statsUnthrottled = statsUnthrottled;
    }

    /**
     * @return the statsKilled
     */
    public long getStatsKilled() {
        return statsKilled;
    }

    /**
     * @param statsKilled the statsKilled to set
     */
    public void setStatsKilled(long statsKilled) {
        this.statsKilled = statsKilled;
    }

    /**
     * @return the uniqueId
     */
    public long getUniqueId() {
        return uniqueId;
    }

    /**
     * @param uniqueId the uniqueId to set
     */
    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }
}
