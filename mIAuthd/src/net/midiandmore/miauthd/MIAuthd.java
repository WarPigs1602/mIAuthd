/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package net.midiandmore.miauthd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The mIAuthd class
 *
 * @author Andreas Pschorn
 */
public class MIAuthd {

    private Config config;
    private IAuthd iauthd;
    private IAuthdThread thread;
    private IAuthSleep sleep;
    private SocketThread socketThread;
    private TrustHandler handler;
    private long lastTime;
    private long startTime;
    private HashMap<String, Clients> user;
    
    /**
     * Runs the IAuthd
     *
     * @param args The arguments
     */
    public static void main(String[] args) {
        try {
            new MIAuthd(args);
        } catch (Exception e) {
        }
    }

    protected MIAuthd(String[] args) {
        init(args);
    }
    /**
     * The init of the IAuthd
     *
     * @throws Exception
     */
    private void init(String[] args) {
        setUser(new HashMap<String, Clients>());
        setStartTime(System.currentTimeMillis() / 1000);
        setLastTime(System.currentTimeMillis() / 1000);
        var configFile = "config.json";
        if (args.length == 1) {
            configFile = args[0];
        }
        setConfig(new Config(this, configFile));
        setHandler(new TrustHandler(this));
        setIauthd(new IAuthd(this));
        setThread(new IAuthdThread(this));
        setSleep(new IAuthSleep(this));
    }

    /**
     * @return the config
     */
    public Config getConfig() {
        return config;
    }

    /**
     * @param config the config to set
     */
    public void setConfig(Config config) {
        this.config = config;
    }

    /**
     * @return the iauthd
     */
    public IAuthd getIauthd() {
        return iauthd;
    }

    /**
     * @param iauthd the iauthd to set
     */
    public void setIauthd(IAuthd iauthd) {
        this.iauthd = iauthd;
    }

    /**
     * @return the thread
     */
    public IAuthdThread getThread() {
        return thread;
    }

    /**
     * @param thread the thread to set
     */
    public void setThread(IAuthdThread thread) {
        this.thread = thread;
    }

    /**
     * @return the startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the lastTime
     */
    public long getLastTime() {
        return lastTime;
    }

    /**
     * @param lastTime the lastTime to set
     */
    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * @return the sleep
     */
    public IAuthSleep getSleep() {
        return sleep;
    }

    /**
     * @param sleep the sleep to set
     */
    public void setSleep(IAuthSleep sleep) {
        this.sleep = sleep;
    }

    /**
     * @return the user
     */
    public HashMap<String, Clients> getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(HashMap user) {
        this.user = user;
    }

    /**
     * @return the handler
     */
    public TrustHandler getHandler() {
        return handler;
    }

    /**
     * @param handler the handler to set
     */
    public void setHandler(TrustHandler handler) {
        this.handler = handler;
    }

    /**
     * @return the socketThread
     */
    public SocketThread getSocketThread() {
        return socketThread;
    }

    /**
     * @param socketThread the socketThread to set
     */
    public void setSocketThread(SocketThread socketThread) {
        this.socketThread = socketThread;
    }
}
