package blobstoretest;

public class UploadUrl {
  
  public String value;
  
  public UploadUrl(String value) {
    this.value = value;
  }
  
  public UploadUrl() {
    this("");   
  }
  
  public String getValue() {
    return value;
  }
  
  public void setValue(String value) {
    this.value = value;
  }
}
