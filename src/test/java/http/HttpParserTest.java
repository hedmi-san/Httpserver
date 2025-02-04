
package http;


import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(Lifecycle.PER_CLASS)
public class HttpParserTest {
    private HttpParser httpParser;
    
    public HttpParserTest() {
    }
    
    @BeforeAll
    public void beforeClass(){
        httpParser = new HttpParser();
    }
    /**
     * Test of parserHttpRequest method, of class HttpParser.
     */
    @Test 
    public void parserHttpRequest() {
        HttpRequest request = null;
        try {
            request = httpParser.parserHttpRequest(generateValidGETTestCase());
        } catch (HttpParsingException ex) {
            fail(ex);
        }
        assertNotNull(request);
        assertEquals(request.getMethod(),HttpMethod.GET);
        assertEquals(request.getTarget(),"/");
    }
    @Test 
    public void parserBadHttpRequest1() {
        try {
            HttpRequest request =  httpParser.parserHttpRequest(generateBadCase1());
            fail();
        } catch (HttpParsingException ex) {
            assertEquals(ex.getErrorCode(),HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }
    
    @Test 
    public void parserBadHttpRequest2() {
        try {
            HttpRequest request =  httpParser.parserHttpRequest(generateBadCase2());
            fail();
        } catch (HttpParsingException ex) {
            assertEquals(ex.getErrorCode(),HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }
    
    @Test 
    public void parserBadHttpRequestInvalidNumberOfItems() {
        try {
            HttpRequest request =  httpParser.parserHttpRequest(generateBadCase3());
            fail();
        } catch (HttpParsingException ex) {
            assertEquals(ex.getErrorCode(),HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }
    
    @Test 
    public void parserBadHttpRequestEmptyRequestLine() {
        try {
            HttpRequest request =  httpParser.parserHttpRequest(generateBadCase4());
            fail();
        } catch (HttpParsingException ex) {
            assertEquals(ex.getErrorCode(),HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }
    @Test 
    public void parserBadHttpRequestIncorrectCRLFnoLF() {
        try {
            HttpRequest request =  httpParser.parserHttpRequest(generateBadCase5());
            fail();
        } catch (HttpParsingException ex) {
            assertEquals(ex.getErrorCode(),HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }
    
    private InputStream generateValidGETTestCase(){
        
        String rawData = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;  
    }
    
    private InputStream generateBadCase1(){
        
        String rawData = "GeT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;  
    }
    
    private InputStream generateBadCase2(){
        
        String rawData = "GETTTT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;  
    }
    
    private InputStream generateBadCase3(){
        
        String rawData = "GET / AAAA HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;  
    }
    
    private InputStream generateBadCase4(){
        
        String rawData = "\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;  
    }
    
    private InputStream generateBadCase5(){
        
        String rawData = "GET / HTTP/1.1\r" + //<===== no LF \n
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;  
    }
    
    
}
