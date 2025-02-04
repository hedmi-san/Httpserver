package http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpParser {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);
    
    private static final int SP = 0x20 ; //32
    private static final int CR = 0x0D ; //13
    private static final int LF = 0x0A ; //10
    
    public HttpRequest parserHttpRequest(InputStream input) throws HttpParsingException{
        InputStreamReader reader = new InputStreamReader(input, StandardCharsets.US_ASCII);
        
        HttpRequest request = new HttpRequest();
        
        try {
            parseRequestLine(reader,request);
        } catch (IOException ex) {
            
        }
        parseHeaders(reader,request);
        parseBody(reader,request);
        
        return request;
    }
    
    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException,HttpParsingException {
        StringBuilder processingDataBuffer = new StringBuilder();
        
        boolean methodParse = false;
        boolean targetParse = false;
        
        int _byte;
        while((_byte = reader.read())>= 0){
            if (_byte == CR) {
                _byte = reader.read();
                if (_byte == LF) {
                    LOGGER.debug("Request Line Version to Process :{}",processingDataBuffer.toString());
                    
                    if (!methodParse || !targetParse) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    
                    try {
                        request.setHttpVersion(processingDataBuffer.toString());
                    } catch (BadHttpVersionException ex) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    return ;
                }else{
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }
            if (_byte == SP) {
                //Process previous data
                if (!methodParse) {
                    LOGGER.debug("Request Line Method to Process :{}",processingDataBuffer.toString());
                    request.setMethod(processingDataBuffer.toString());
                    methodParse = true;
                }else if (!targetParse) {
                    LOGGER.debug("Request Line Target to Process :{}",processingDataBuffer.toString());
                    request.setTarget(processingDataBuffer.toString());
                    targetParse = true;
                }else{
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                processingDataBuffer.delete(0, processingDataBuffer.length());
            }else {
                processingDataBuffer.append((char)_byte);
                if (!methodParse) {
                    if (processingDataBuffer.length() > HttpMethod.MAX_LENGTH) {
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }
    }

    private void parseHeaders(InputStreamReader reader, HttpRequest request) {
        
    }


    private void parseBody(InputStreamReader reader, HttpRequest request) {
        
    }
}
