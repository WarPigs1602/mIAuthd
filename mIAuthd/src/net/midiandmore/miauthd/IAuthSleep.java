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
                getMi().getIauthd().addStats("Version: %s", VERSION);
                getMi().getIauthd().addStats("Started: %s seconds ago", (System.currentTimeMillis() / 1000) - getMi().getStartTime());
                thread.sleep(15000);
                getMi().getIauthd().clearStats();
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

