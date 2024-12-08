
package dto;
public class ResponseDto {
    private boolean error;
    private String message;
    
    public ResponseDto(boolean error, String message) {
        this.error = error;
        this.message = message;
    }
    
    public boolean getError() {
        return this.error;
    }
    
    public String getMessage() {
        return this.message;
    }
}
