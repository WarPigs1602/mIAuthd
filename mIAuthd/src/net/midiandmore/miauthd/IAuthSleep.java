/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.midiandmore.miauthd;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Starts a new Thread
 *
 * @author Andreas Pschorn
 */
public class IAuthSleep implements Runnable, Info {

    private Thread thread;
    private MIAuthd mi;

    public IAuthSleep(MIAuthd mi) {
        setMi(mi);
        (thread = new Thread(this)).start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                var trust = getMi().getConfig().getConfigFile().get("TRUST_ENABLED").equals("true");
                if (trust) {
                    if (getMi().getSocketThread() == null) {
                        getMi().setSocketThread(new SocketThread(getMi()));
                    } else if (getMi().getSocketThread().getSocket() == null) {
                        getMi().getSocketThread().setRuns(false);
                        getMi().setSocketThread(null);
                    } else if (getMi().getSocketThread().isRuns()
                            && getMi().getSocketThread().getSocket().isClosed()) {
                        getMi().getSocketThread().setRuns(false);
                        getMi().setSocketThread(null);
                    }
                }
                getMi().getIauthd().addStats("Version: %s", VERSION);
                getMi().getIauthd().addStats("Started: %s seconds ago", (System.currentTimeMillis() / 1000) - getMi().getStartTime());
                if (getMi().getHandler().getConnected() != -1 && getMi().getHandler().isAuthed()) {
                    getMi().getIauthd().addStats("Connected to trusts backend for %s seconds.",
                            (System.currentTimeMillis() / 1000) - getMi().getHandler().getConnected());
                } else {
                    getMi().getIauthd().addStats("Not connected to trusts backend.");
                }
                getMi().getIauthd().addStats("Accepted connections: %d", getMi().getIauthd().getStatsPasssed());
                getMi().getIauthd().addStats("Rejected connections: %d", getMi().getIauthd().getStatsKilled());
                getMi().getIauthd().addStats("Unthrottled connections: %d", getMi().getIauthd().getStatsUnthrottled());
                getMi().getIauthd().addStats("Pending connections: %d", getMi().getUser().size());
                System.gc();
                var rt = Runtime.getRuntime();
                getMi().getIauthd().addStats("Memory usage: %d KB", ((rt.totalMemory() - rt.freeMemory()) / 1024));
                getMi().getIauthd().addConfig("Connected webirc to: %s", getMi().getConfig().getConfigFile().get("WEBIRC_HOST"));
                getMi().getIauthd().addConfig("Trust enabled: %s", getMi().getConfig().getConfigFile().get("TRUST_ENABLED"));
                thread.sleep(15000);
                getMi().getIauthd().clearStats();
                getMi().getIauthd().clearConfig();
            } catch (InterruptedException ex) {
                Logger.getLogger(IAuthSleep.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }
        }
    }

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
}
