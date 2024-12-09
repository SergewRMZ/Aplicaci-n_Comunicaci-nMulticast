
package dto;
public class ResponseDto {
    private boolean error;
    private String message;
    private Object data;
    
    public ResponseDto(boolean error, String message, Object data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }
    
    public boolean getError() {
        return this.error;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public Object getObject() {
      return this.data;
    }
}
