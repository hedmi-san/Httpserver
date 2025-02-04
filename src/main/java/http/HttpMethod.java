
package http;

public enum HttpMethod {
    GET,HEAD;
    public static final int MAX_LENGTH;
    static{
        int tempMAxLength = -1;
        for(HttpMethod method : values()){
            if(method.name().length() > tempMAxLength){
                tempMAxLength = method.name().length();
            }
        }
        MAX_LENGTH = tempMAxLength;
    }
}
