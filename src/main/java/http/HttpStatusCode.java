
package http;

public enum HttpStatusCode {
    
    /*---  Clients Errors ---*/
    CLIENT_ERROR_400_BAD_REQUEST(400,"Bad Request"),
    CLIENT_ERROR_405_METHOD_NOT_ALLOWED(405,"Method Not Allowed"),
    CLIENT_ERROR_414_BAD_REQUEST(414,"URI Too Long"),
    /*---  Clients Errors ---*/
    SERVER_ERROR_500_ITERNAL_SERVER_ERROR(500,"Iternal Server Error"),
    SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED(505,"Http version Not Supported"),
    SERVER_ERROR_501_NOT_IMPLEMENTED(501,"Method Not Implemented");
    
    public final int STATUS_CODE;
    public final String MESSAGE;

    private HttpStatusCode(int STATUS_CODE, String MESSAGE) {
        this.STATUS_CODE = STATUS_CODE;
        this.MESSAGE = MESSAGE;
    }
    
    
}
