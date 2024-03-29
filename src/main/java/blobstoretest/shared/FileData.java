/*
 * Copyright (C) 2014 Andrés Testi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package blobstoretest.shared;

import com.google.appengine.api.blobstore.BlobKey;

import java.util.Objects;

/**
 * Shareable file information.
 * 
 * @author Andrés Testi
 */
public class FileData {

  private String filename;
  private BlobKey blobKey;

  public FileData(String filename, BlobKey blobKey) {
    this.filename = filename;
    this.blobKey = blobKey;
  }
  
  public FileData() {
    this(null, null);
  }
  
  public String getFilename() {
    return filename;
  }
  
  public void setFilename(String filename) {
    this.filename = filename;
  }

  public BlobKey getBlobKey() {
    return blobKey;
  }

  public void setBlobKey(BlobKey blobKey) {
    this.blobKey = blobKey;
  }
  
  @Override public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final FileData other = (FileData) obj;

    return Objects.equals(this.blobKey, other.blobKey)
        && Objects.equals(this.filename, other.filename);
  }
  
  @Override public int hashCode() {
    return Objects.hash(blobKey, filename);
  }
}
