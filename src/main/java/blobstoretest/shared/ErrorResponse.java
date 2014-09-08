package blobstoretest.shared;

public class ErrorResponse {
  
  private ErrorMessage error;
  private int code;
  
  public ErrorMessage getError() {
    return error;
  }
  
  public void setError(ErrorMessage error) {
    this.error = error;
  }
  
  public int getCode() {
    return code;
  }
  
  public void setCode(int code) {
    this.code = code;
  }
}
