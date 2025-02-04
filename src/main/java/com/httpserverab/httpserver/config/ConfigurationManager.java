
package com.httpserverab.httpserver.config;
import com.fasterxml.jackson.databind.JsonNode;
import com.httpserverab.httpserver.util.Json;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
public class ConfigurationManager {
    
    private static ConfigurationManager myConfigurationManager;
    private static Configuration myCurrentCOnfiguration;
    
    private ConfigurationManager(){}
    
    public static ConfigurationManager getInstance(){
        if (myConfigurationManager == null)
            myConfigurationManager = new ConfigurationManager();
        return myConfigurationManager;
    }
    /**
     * Used to load a configuration file by the path provided 
     */
    public void loadConfiguration(String filePath) {
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException ex) {
            throw new HttpConfigurationException(ex);
        }
        StringBuffer sb = new StringBuffer();
        int i;
        try {
            while(( i = fileReader.read())!= -1){
                sb.append((char)i);
            }
        } catch (IOException ex) {
            throw new HttpConfigurationException(ex);
        }
        JsonNode conf = null;
        try {
            conf = Json.parse(sb.toString());
        } catch (IOException ex) {
            throw new HttpConfigurationException("Erroe parsing the configuration File",ex);
        }
        try {
            myCurrentCOnfiguration = Json.fromJson(conf, Configuration.class);
        } catch (IOException ex) {
            throw new HttpConfigurationException("Erroe parsing the configuration file, internal",ex);
        }
    }
    
    /**
     * Return the current loaded configurations 
     */
    public Configuration getCurrentConfiguration(){
        if(myCurrentCOnfiguration == null){
            throw new HttpConfigurationException("No current Configuration Set");
        }
        return myCurrentCOnfiguration;
    }
}

