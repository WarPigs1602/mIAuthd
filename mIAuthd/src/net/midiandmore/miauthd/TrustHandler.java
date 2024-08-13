/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.midiandmore.miauthd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.apache.commons.codec.digest.HmacAlgorithms.HMAC_MD5;
import org.apache.commons.codec.digest.HmacUtils;

/**
 * This is the truts handler
 *
 * @author Andreas Pschorn
 */
public class TrustHandler {

    private MIAuthd mi;
    private boolean authed;
    private long connected;
    private PrintWriter pw;
    private BufferedReader br;

    protected TrustHandler(MIAuthd mi) {
        setMi(mi);
        setAuthed(false);
        setConnected(-1);
    }

    protected void parseLine(String line) {
        var tokens = line.split(" ", 2);
        String message = null;
        if (tokens[0].equals("KILL") || tokens[0].equals("PASS") || tokens[0].equals("UNTHROTTLE")) {
            if (tokens.length < 2) {
                return;
            }
            var arguments = tokens[1].split(" ", 2);
            var verdict = tokens[0];
            var id = arguments[0];
            if (arguments.length > 1) {
                message = arguments[1];
            }
            getMi().getIauthd().sendVerdict(id, verdict, message);
        } else if (tokens[0].equals("AUTH")) {
            if (tokens.length < 2) {
                return;
            }
            getMi().getIauthd().sendSnotice("Received authentication request from trusts backend.");
            var nonce = tokens[1];
            var hmac = new HmacUtils(HMAC_MD5, (String) getMi().getConfig().getConfigFile().get("TRUST_PASS")).hmacHex(nonce);
            sendText("AUTH %s %s", getMi().getConfig().getConfigFile().get("TRUST_NAME"),
                     hmac);
        } else if (tokens[0].equals("AUTHOK")) {
            getMi().getIauthd().sendSnotice("Successfully authenticated with trusts backend.");
            setAuthed(true);
            setConnected(System.currentTimeMillis() / 1000);
        } else if (tokens[0].equals("QUIT")) {
            if (tokens.length < 2) {
                message = "No reason specified.";
            } else {
                message = tokens[1];
            }
            getMi().getIauthd().sendSnotice("Trusts backend closed connection: %s", message);
            try {
                getMi().getSocketThread().getSocket().close();
                getMi().setSocketThread(null);
            } catch (IOException ex) {
                Logger.getLogger(TrustHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    protected void sendText(String text, Object... args) {
        getMi().getSocketThread().getPw().println(text.formatted(args));
        getMi().getSocketThread().getPw().flush();
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

    /**
     * @return the authed
     */
    public boolean isAuthed() {
        return authed;
    }

    /**
     * @param authed the authed to set
     */
    public void setAuthed(boolean authed) {
        this.authed = authed;
    }

    /**
     * @return the connected
     */
    public long getConnected() {
        return connected;
    }

    /**
     * @param connected the connected to set
     */
    public void setConnected(long connected) {
        this.connected = connected;
    }

    /**
     * @return the pw
     */
    public PrintWriter getPw() {
        return pw;
    }

    /**
     * @param pw the pw to set
     */
    public void setPw(PrintWriter pw) {
        this.pw = pw;
    }

    /**
     * @return the br
     */
    public BufferedReader getBr() {
        return br;
    }

    /**
     * @param br the br to set
     */
    public void setBr(BufferedReader br) {
        this.br = br;
    }
}
