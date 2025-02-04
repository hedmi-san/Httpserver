package com.httpserverab.httpserver;

import com.httpserverab.httpserver.config.Configuration;
import com.httpserverab.httpserver.config.ConfigurationManager;
import core.ServerListenerThread;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Httpserver {
    private final static Logger LOGGER = LoggerFactory.getLogger(Httpserver.class);

    public static void main(String[] args)  {
        LOGGER.info("Server running.....!");
        ConfigurationManager.getInstance().loadConfiguration("src/main/java/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();
        LOGGER.info("Using port :"+ conf.getPort());
        LOGGER.info("Using webroot :"+ conf.getWebroot());
        try {
            ServerListenerThread serverThread = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            serverThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
} 