/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.midiandmore.miauthd;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

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
            var i = 0;
            if (host.contains(".")) {
                String[] elements = host.split("\\.");
                for (String data : elements) {
                    var isIP = false;
                    if (data.contains("-")) {
                        String[] tokens = data.split("-");
                        var j = 1;
                        for (String token : tokens) {
                            if (token.matches("\\d*")) {
                                sb.append(Hex.encodeHex(token.substring(0, token.length() / 2).getBytes("UTF-8")));
                                i++;
                            } else if (token.equals("ip")) {
                                isIP = true;
                            } else {
                                sb.append(token);
                            }
                            if (j < tokens.length && !isIP) {
                                sb.append("-");
                            }
                            j++;
                        }
                    } else if (data.equals("ip")) {
                        isIP = true;
                    } else if (data.matches("\\d*")) {
                        sb.append(Hex.encodeHex(data.substring(0, data.length() / 2).getBytes("UTF-8")));

                    } else {
                        sb.append(data);
                    }
                    if (isIP) {
                        sb.insert(0, "cloak-");
                    }
                    sb.append(".");
                    i++;
                }
                if (i == 4 && elements.length == 4) {
                    sb.append("ip");
                }
            }
            if (host.contains(":")) {
                String[] elements = host.split(":");
                for (String data : elements) {
                    var elem = data.toCharArray();
                    var part = 0;
                    var buf = new StringBuilder();
                    for (char ip : elem) {
                        if (part < (elem.length / 2)) {
                            buf.append(ip);
                            part++;
                        } else {
                            sb.append(buf);
                            break;
                        }
                    }
                }
                sb.append(".ip");
            }
        } catch (Exception ex) {
            Logger.getLogger(Cloak.class.getName()).log(Level.SEVERE, null, ex);
        }
        var s = sb.toString();
        return s.endsWith(".") ? s.substring(0, s.length() - 1) : s;
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
