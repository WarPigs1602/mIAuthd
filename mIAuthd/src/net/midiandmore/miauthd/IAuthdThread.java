/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.midiandmore.miauthd;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Starts a new Thread
 * 
 * @author Andreas Pschorn
 */
public class IAuthdThread implements Runnable, Info {

    private Thread thread;
    private MIAuthd mi;
    
    public IAuthdThread(MIAuthd mi) {
        setMi(mi);
        (thread = new Thread(this)).start();
    }

    @Override
    public void run() {
        try {
            getMi().getIauthd().sendText("VERSION %s", VERSION);
            getMi().getIauthd().sendText("V :miauthd");
            getMi().getIauthd().sendText("O AU");
            InputStream is = System.in;
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            var content = "";
            while((content = br.readLine()) != null) {
                getMi().getIauthd().parseLine(content);
            }            
        } catch (IOException ex) {
            Logger.getLogger(IAuthdThread.class.getName()).log(Level.SEVERE, null, ex);
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
