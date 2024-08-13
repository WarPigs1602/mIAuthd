/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.midiandmore.miauthd;
import jakarta.json.Json;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads the iAuthd config
 * 
 * @author Andreas Pschorn
 */
public class Config {
    
    private MIAuthd mi;
    private Properties configFile;
    
    /**
     * Initiales the class
     * 
     * @param mi The MIAuthd class
     */
    protected Config(MIAuthd mi, String configFile) {
        setMi(mi);
        loadConfig(configFile);
    }    

    /**
     * Loads the config files in the Properties
     */
    private void loadConfig(String configFile) {
        setConfigFile(loadDataFromJSONasProperties(configFile, "name", "value"));
    }
    
    /**
     * Loads the config data from a JSON file
     *
     * @param file The file
     * @param obj First element
     * @param obj2 Second element
     * @return The properties
     */
    protected Properties loadDataFromJSONasProperties(String file, String obj, String obj2) {
        var ar = new Properties();
        try {
            InputStream is = new FileInputStream(file);
            var rdr = Json.createReader(is);
            var results = rdr.readArray();
            var i = 0;
            for (var jsonValue : results) {
                var jobj = results.getJsonObject(i);
                ar.put(jobj.getString(obj), jobj.getString(obj2));
                i++;
            }
        } catch (Exception fne) {
            fne.printStackTrace();
        }
        return ar;
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
     * @return the configFile
     */
    public Properties getConfigFile() {
        return configFile;
    }

    /**
     * @param configFile the configFile to set
     */
    public void setConfigFile(Properties configFile) {
        this.configFile = configFile;
    }
}
