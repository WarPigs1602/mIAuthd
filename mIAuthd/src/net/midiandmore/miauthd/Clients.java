/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.midiandmore.miauthd;

/**
 * Adds a client
 * 
 * @author Andreas Pschorn
 */
public class Clients {
    
    private MIAuthd mi;
    private String id;
    private String password;
    private String username;
    private String hostname;
    private String ip;
    private String remoteip;
    private String remoteport;
    private String uniqueId;
    private boolean webirc;
                    
    public Clients(MIAuthd mi, String id) {
        setMi(mi);
        setId(id);
        setWebirc(false);
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
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the remoteip
     */
    public String getRemoteip() {
        return remoteip;
    }

    /**
     * @param remoteip the remoteip to set
     */
    public void setRemoteip(String remoteip) {
        this.remoteip = remoteip;
    }

    /**
     * @return the remoteport
     */
    public String getRemoteport() {
        return remoteport;
    }

    /**
     * @param remoteport the remoteport to set
     */
    public void setRemoteport(String remoteport) {
        this.remoteport = remoteport;
    }

    /**
     * @return the uniqueId
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * @param uniqueId the uniqueId to set
     */
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * @return the webirc
     */
    public boolean isWebirc() {
        return webirc;
    }

    /**
     * @param webirc the webirc to set
     */
    public void setWebirc(boolean webirc) {
        this.webirc = webirc;
    }
}
