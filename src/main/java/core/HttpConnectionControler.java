package core;

import core.io.WebRootHandler;
import core.io.WebRootException;
import core.io.ReadFileException;
import http.HttpParser;
import http.HttpRequest;
import http.HttpParsingException;

import java.io.*;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpConnectionControler extends Thread {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionControler.class);
    private Socket socket;
    private WebRootHandler webRootHandler;

    public HttpConnectionControler(Socket socket, WebRootHandler webRootHandler) {
        this.socket = socket;
        this.webRootHandler = webRootHandler;
    }

    @Override
    public void run() {
        try (
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
        ) {
            HttpParser parser = new HttpParser();
            HttpRequest request;
            try {
                request = parser.parserHttpRequest(input);
            } catch (HttpParsingException ex) {
                LOGGER.error("Failed to parse HTTP request", ex);
                sendResponse(output, "400 Bad Request", "text/plain", "Bad Request".getBytes());
                return;
            }
            
            String target = request.getTarget();
            LOGGER.info("HTTP Request Target: {}", target);

            try {
                byte[] fileContent = webRootHandler.getFileByteArrayData(target);
                String mimeType = webRootHandler.getFileMimeType(target);
                sendResponse(output, "200 OK", mimeType, fileContent);
            } catch (FileNotFoundException e) {
                LOGGER.error("File not found: {}", target);
                sendResponse(output, "404 Not Found", "text/plain", "404 Not Found".getBytes());
            } catch (ReadFileException e) {
                LOGGER.error("Error reading file: {}", target, e);
                sendResponse(output, "500 Internal Server Error", "text/plain", "Internal Server Error".getBytes());
            }

        } catch (IOException e) {
            LOGGER.error("Problem with communication", e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // Log or ignore closing exceptions.
                }
            }
        }
    }

    /**
     * Helper method to send HTTP responses.
     */
    private void sendResponse(OutputStream output, String status, String contentType, byte[] content) throws IOException {
        final String CRLF = "\r\n";
        String headers = "HTTP/1.1 " + status + CRLF
                + "Content-Type: " + contentType + CRLF
                + "Content-Length: " + content.length + CRLF
                + "Connection: close" + CRLF
                + CRLF;

        output.write(headers.getBytes());
        output.write(content);
        output.flush();
    }
}
