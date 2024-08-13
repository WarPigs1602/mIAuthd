/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.midiandmore.miauthd;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Adds a cloak feature in mIAuthd
 *
 * @author Andreas Pschorn
 */
public class Cloak {
    
    private MIAuthd mi;
    
    public Cloak(MIAuthd mi) {
        setMi(mi);
    }
    
    protected String parseCloak(String host) {
        var sb = new StringBuilder();
        try {
            if (host.contains(":")) {
                var tokens = host.split(":");
                for (var elem : tokens) {
                    if (elem.isBlank()) {
                        continue;
                    }
                    sb.append(parse(elem));
                    sb.append(".");
                }
                sb.append("ip");
                
            } else {
                var add = InetAddress.getByName(host).getHostAddress();
                if (add.contains(".")) {
                    var tokens = add.split("\\.");
                    for (var elem : tokens) {
                        sb.append(parse(elem));
                        sb.append(".");
                    }
                    sb.append("ip");
                } else if (add.contains(":")) {
                    var tokens = add.split(":");
                    for (var elem : tokens) {
                        if (elem.isBlank() || elem.equals("0")) {
                            continue;
                        }                        
                        sb.append(parse(elem));
                        sb.append(".");
                    }
                    sb.append("ip");
                }                
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(Cloak.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (sb.isEmpty()) {
            sb.append(host);
        }
        return sb.toString();
    }
    
    private String parseHex(String text) {
        var buf = DigestUtils.md5Hex(text).toCharArray();
        var sb = new StringBuilder();
        int i = 0;
        for (var chr : buf) {
            sb.append(chr);
            if (i >= 7) {
                break;
            }
            i++;
        }
        return sb.toString();
    }
    
    private String parse(String text) {
        var buf = DigestUtils.md5Hex(text).toCharArray();
        var sb = new StringBuilder();
        int i = 0;
        for (var chr : buf) {
            sb.append(chr);
            if (i >= 3) {
                break;
            }
            i++;
        }
        return sb.toString();
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
