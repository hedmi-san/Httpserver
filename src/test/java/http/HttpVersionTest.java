
package http;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
public class HttpVersionTest {

    /**
     * Test of getBestCompatibleVersion method, of class HttpVersion.
     */
    @Test
    public void testGetBestCompatibleVersion() {
        HttpVersion version = null;
        try {
            version = HttpVersion.getBestCompatibleVersion("HTTP/1.1");
        } catch (BadHttpVersionException ex) {
            fail();
        }
        assertNotNull(version);
        assertEquals(version,HttpVersion.HTTP_1_1);
    }
    
    @Test
    public void testGetBestCompatibleVersionBadFormat() {
        HttpVersion version = null;
        try {
            version = HttpVersion.getBestCompatibleVersion("http/1.1");
            fail();
        } catch (BadHttpVersionException ex) {
        }
    }
    
    @Test
    public void testGetBestCompatibleVersionHigherVersiont() {
        HttpVersion version = null;
        
        try {
            version = HttpVersion.getBestCompatibleVersion("HTTP/1.2");
            assertNotNull(version);
            assertEquals(version,HttpVersion.HTTP_1_1);
        } catch (BadHttpVersionException ex) {
            fail();
        }
    }
    
}
