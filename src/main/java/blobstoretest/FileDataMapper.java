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
package blobstoretest;

import blobstoretest.shared.FileData;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Entity;

/**
 * Utility class for {@link Entity} mappings.
 * 
 * @author Andrés Testi
 */
public class FileDataMapper {

  public static FileData fromEntity(Entity entity) {
    FileData fileData = new FileData();
    fileData.setBlobKey((BlobKey) entity.getProperty("blobKey"));
    fileData.setFilename((String) entity.getProperty("filename"));
    return fileData;
  }

  public static Entity toEntity(FileData fileData) {
    Entity entity = new Entity(Constants.ENTITY_NAME);
    entity.setProperty("filename", fileData.getFilename());
    entity.setProperty("blobKey", fileData.getBlobKey());
    return entity;
  }
}
