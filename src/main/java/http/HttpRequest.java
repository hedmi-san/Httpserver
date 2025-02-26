
package http;

public class HttpRequest extends HttpMessage{
    private HttpMethod method;
    private String target;
    private String originalHttpVersion;
    private HttpVersion bestCompatibleVersion;
    HttpRequest() {
    }

    public HttpMethod getMethod() {
        return method;
    }

    void setMethod(String methodName)throws HttpParsingException{
        for(HttpMethod method : HttpMethod.values()){
            if (methodName.equals(method.name())) {
                this.method = method;
                return;
            }
        }
        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
    }

    void setTarget(String target)throws HttpParsingException {
        if (target == null || target.length() == 0) {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
            this.target = target;
    }
    
    public String getTarget() {
        return target;
    }

    void setHttpVersion(String originalHttpVersion) throws BadHttpVersionException, HttpParsingException {
        this.originalHttpVersion = originalHttpVersion;
        this.bestCompatibleVersion = HttpVersion.getBestCompatibleVersion(originalHttpVersion);
        if(this.bestCompatibleVersion == null){
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    public HttpVersion getBestCompatibleVersion() {
        return bestCompatibleVersion;
    }
    
    
    
    
}
