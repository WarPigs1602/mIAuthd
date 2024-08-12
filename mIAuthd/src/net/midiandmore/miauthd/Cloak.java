/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.midiandmore.miauthd;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
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
        var buf = new StringBuilder();
        try {
            var add = InetAddress.getByName(host).getHostAddress();
            if (add.equals(host)) {
                if (add.contains(".")) {
                    var tokens = add.split("\\.");
                    for (var elem : tokens) {
                        sb.append(parse(elem));
                        sb.append(".");
                    }
                } else if (add.contains(":")) {
                    var tokens = add.split(":");
                    for (var elem : tokens) {
                        sb.append(parse(elem));
                        sb.append(".");
                    }
                }
                sb.append("ip");
            } else {
                String[] tokens;
                if (add.contains(".")) {
                    tokens = add.split("\\.");
                    for (var elem : tokens) {
                        if (elem.length() == 1) {
                            buf.append("0");
                        }
                        buf.append(Integer.toHexString(Integer.parseInt(elem)));
                    }
                } else if (add.contains(":")) {
                    tokens = add.split(":");
                    for (var elem : tokens) {
                        buf.append(elem);
                    }
                }
                var i = 1;
                if (add.contains(".")) {
                    tokens = host.split("\\.");
                    for (var elem : tokens) {
                        var hex = buf.toString();
                        if (elem.contains(hex) && !hex.isBlank()) {
                            if (!sb.toString().startsWith("cloak-")) {
                                sb.insert(0, "cloak-");
                            }
                            sb.append(parseHex(hex));
                            sb.append(".");
                            buf.delete(0, buf.length());
                        } else if (elem.contains("-")) {
                            var parts = elem.split("-");
                            var j = 1;
                            for (var part : parts) {
                                if (part.matches("\\d*")) {
                                    sb.append(parse(part));
                                } else if (part.contains("dyn") || part.contains("ip") || part.contains("dsl") || part.contains("nat")) {
                                    if (!sb.toString().startsWith("cloak-")) {
                                        sb.insert(0, "cloak-");
                                    } else {
                                        sb.append(part);
                                    }
                                } else {
                                    sb.append(part);
                                    if (j < parts.length) {
                                        sb.append("-");
                                    }
                                }
                                j++;
                            }
                            sb.append(".");
                        } else if (elem.matches("\\d*")) {
                            sb.append(parse(elem));
                        } else if (elem.contains("dyn") || elem.contains("ip") || elem.contains("dsl") || elem.contains("nat")) {
                            if (!sb.toString().startsWith("cloak-")) {
                                sb.insert(0, "cloak-");
                            } else {
                                sb.append(elem);
                            }
                        } else {
                            sb.append(elem);
                            if (i < tokens.length) {
                                sb.append(".");
                            }
                        }
                        i++;
                    }
                }
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(Cloak.class.getName()).log(Level.SEVERE, null, ex);
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
