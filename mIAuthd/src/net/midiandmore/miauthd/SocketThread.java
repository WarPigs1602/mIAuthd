/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.midiandmore.miauthd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Starts a new Thread
 * 
 * @author Andreas Pschorn
 */
public class SocketThread implements Runnable {

    private Thread thread;
    private MIAuthd mi;
    private Socket socket;
    private PrintWriter pw;
    private BufferedReader br;
    private boolean runs;
    
    public SocketThread(MIAuthd mi) {
        setMi(mi);
        (thread = new Thread(this)).start();
    }

    @Override
    public void run() {
        setRuns(true);
        var host = getMi().getConfig().getConfigFile().getProperty("TRUST_HOST");
        var port = Integer.valueOf(getMi().getConfig().getConfigFile().getProperty("TRUST_PORT"));
        try {        
            setSocket(new Socket(host, port));
            setPw(new PrintWriter(getSocket().getOutputStream()));
            setBr(new BufferedReader(new InputStreamReader(getSocket().getInputStream())));
            var content = "";
            getMi().getHandler().setConnected(System.currentTimeMillis() / 1000);
            while(!getSocket().isClosed() && (content = getBr().readLine()) != null && isRuns()) {
                getMi().getHandler().parseLine(content);
            }        
            setRuns(false);
        } catch (Exception ex) {
            Logger.getLogger(SocketThread.class.getName()).log(Level.SEVERE, null, ex);
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

    /**
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @param socket the socket to set
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
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

    /**
     * @return the runs
     */
    public boolean isRuns() {
        return runs;
    }

    /**
     * @param runs the runs to set
     */
    public void setRuns(boolean runs) {
        this.runs = runs;
    }
}

