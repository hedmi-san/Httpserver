package core;

import core.io.WebRootHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerListenerThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);
    private int port;
    private String webroot;
    private ServerSocket server;
    private WebRootHandler webRootHandler;

    public ServerListenerThread(int port, String webroot) throws IOException {
        this.port = port;
        this.webroot = webroot;
        this.server = new ServerSocket(this.port);
        try {
            this.webRootHandler = new WebRootHandler(webroot);
        } catch (Exception e) {
            LOGGER.error("Failed to initialize WebRootHandler with webroot: " + webroot, e);
            throw new IOException("Invalid webroot: " + webroot, e);
        }
    }

    @Override
    public void run() {
        try {
            while (server.isBound() && !server.isClosed()) {
                Socket socket = server.accept();
                LOGGER.info("Connection accepted from " + socket.getInetAddress());
                HttpConnectionControler workerThread = new HttpConnectionControler(socket, webRootHandler);
                workerThread.start();
            }
        } catch (IOException ex) {
            LOGGER.error("Problem with setting up or handling socket connections", ex);
        } finally {
            if (server != null && !server.isClosed()) {
                try {
                    server.close();
                    LOGGER.info("Server socket closed.");
                } catch (IOException ex) {
                    LOGGER.error("Error closing the server socket", ex);
                }
            }
        }
    }
}
