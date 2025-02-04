
package core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;

public class WebRootHandler {
    private File webRoot;

    public WebRootHandler(String webRootPath) throws WebRootException {
        webRoot = new File(webRootPath);
        if (!webRoot.exists() || !webRoot.isDirectory()) {
            throw new WebRootException("WebRoot Not found");
        }
    }
    
    private boolean checksIfEndsWithSlash(String relativePath){
        return relativePath.endsWith("/");
    }
    
    private boolean checkIfRelativePathExists(String relativePath){
        File file = new File(relativePath);
        if (!file.exists()) {
            return false;
        }
        try {
            if(file.getCanonicalPath().startsWith(webRoot.getCanonicalPath())){  
            }
        } catch (IOException ex) {
            return false;
        }
        return false;
    }
    
    public String getFileMimeType(String relativePath) throws FileNotFoundException{
        if (checksIfEndsWithSlash(relativePath)) {
            relativePath += "index.html";
        }
        
        if (!new File(webRoot, relativePath).exists()) {
            throw new FileNotFoundException("File does not exist: " + relativePath);
        }

        
        File file = new File(webRoot,relativePath);
        String mimeType = URLConnection.getFileNameMap().getContentTypeFor(file.getName());
        if (mimeType == null) {
            return "application/octet-stream";
        }
        
        return mimeType;
    }
    
    public byte[] getFileByteArrayData(String relativePath) throws FileNotFoundException, ReadFileException{
        if (checksIfEndsWithSlash(relativePath)) {
            relativePath += "index.html";
        }
        
        if (!new File(webRoot, relativePath).exists()) {
            throw new FileNotFoundException("File does not exist: " + relativePath);
        }
        
        File file = new File(webRoot,relativePath);
        FileInputStream inputFiletStream = new FileInputStream(file);
        byte[] fileByte = new byte[(int)file.length()];
        try {
            inputFiletStream.read(fileByte);
            inputFiletStream.close();
        } catch (IOException ex) {
            throw new ReadFileException(ex);
        }
        return fileByte;
    }
}
