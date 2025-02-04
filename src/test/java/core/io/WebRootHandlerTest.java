
package core.io;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.TestInstance;

/**
 *
 * @author LAPTOP SPIRIT
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebRootHandlerTest {
    
    private WebRootHandler webRootHandler;
    private Method checkIfEndsWithSlashMethod;
    private Method checkIfRelativePathExistsMethod;
    
    @BeforeAll
    public void beforeClass() throws WebRootException, NoSuchMethodException {
        webRootHandler= new WebRootHandler("WebRoot");
        Class <WebRootHandler> cls = WebRootHandler.class;
        checkIfEndsWithSlashMethod = cls.getDeclaredMethod("checksIfEndsWithSlash", String.class);
        checkIfEndsWithSlashMethod.setAccessible(true);
        
        checkIfRelativePathExistsMethod = cls.getDeclaredMethod("checkIfRelativePathExists", String.class);
        checkIfRelativePathExistsMethod.setAccessible(true);
    }
    
    @Test
    void constructorGoodPath(){
        try {
            WebRootHandler webRootHandler = new WebRootHandler("C:\\Users\\LAPTOP SPIRIT\\Documents\\Learning\\Java\\Httpserver\\WebRoot");
        } catch (WebRootException e) {
            fail(e);
        }
    }
    
    @Test
    void constructorBadPath(){
        try {
            WebRootHandler webRootHandler = new WebRootHandler("C:\\Users\\LAPTOP SPIRIT\\Documents\\Learning\\Java\\Httpserver\\WebRoot2");
            fail();
        } catch (WebRootException e) {
            
        }
    }
    
    @Test
    void constructorGoodPath2(){
        try {
            WebRootHandler webRootHandler = new WebRootHandler("WebRoot");
        } catch (WebRootException e) {
            fail(e);
        }
    }
    
    @Test
    void checkIfEndsWithSlashMethodGood(){
        try {
            boolean result = (Boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler,"index.html");
            assertFalse(result);
        } catch (IllegalAccessException ex) {
            fail(ex);
        } catch (InvocationTargetException ex) {
            fail(ex);
        }
    }
    
    @Test
    void checkIfEndsWithSlashMethodFalse2() {
        try {
            boolean result = (Boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler,"/index.html");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }
    
    @Test
    void checkIfEndsWithSlashMethodFalse3() {
        try {
            boolean result = (Boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler,"/private/index.html");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }
    
    @Test
    void checkIfEndsWithSlashMethodTrue() {
        try {
            boolean result = (Boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler,"/");
            assertTrue(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }
    
    @Test
    void checkIfEndsWithSlashMethodTrue2() {
        try {
            boolean result = (Boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler,"/private/");
            assertTrue(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootFilePathExists() {
        try {
            boolean result = (boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "/index.html");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootFilePathExistsBadRelative() {
        try {
            boolean result = (boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "/./././index.html");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootFilePathExistsDoesNotExist() {
        try {
            boolean result = (boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "/indexNotHere.html");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootFilePathExistsInvalid() {
        try {
            boolean result = (boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "/../LICENSE");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }
    
    @Test
    void testGetFileMimeTypeText() {
        try {
            String mimeType = webRootHandler.getFileMimeType("/");
            assertEquals("text/html", mimeType);
        } catch (FileNotFoundException e) {
            fail(e);
        }
    }
    
    @Test
    void testGetFileMimeTypePng() {
        try {
            String mimeType = webRootHandler.getFileMimeType("/Mazda.jpg");
            assertEquals("image/jpeg", mimeType);
        } catch (FileNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileMimeTypeDefault() {
        try {
            String mimeType = webRootHandler.getFileMimeType("/favicon.ico");
            assertEquals("application/octet-stream", mimeType);
        } catch (FileNotFoundException e) {
            fail(e);
        }
    }
    
    @Test
    void testGetFileByteArrayData() {
        try {
            assertTrue(webRootHandler.getFileByteArrayData("/").length > 0);
        } catch (FileNotFoundException e) {
            fail(e);
        } catch (ReadFileException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileByteArrayDataFileNotThere() {
        try {
            webRootHandler.getFileByteArrayData("/test.html");
            fail();
        } catch (FileNotFoundException e) {
            // pass
        } catch (ReadFileException e) {
            fail(e);
        }
    }
}
