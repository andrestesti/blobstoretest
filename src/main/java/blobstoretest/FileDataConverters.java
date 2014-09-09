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

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Entity;
import com.google.common.base.Function;

/**
 * Utility class for {@link FileData} conversions.
 * 
 * @author Andrés Testi
 */
public class FileDataConverters {

  private static final Function<Entity, FileData> ENTITY_TO_FILE_DATA =
      new Function<Entity, FileData>() {
        @Override
        public FileData apply(Entity entity) {
          return entityToFileData(entity);
        }
      };

  private static final Function<FileData, Entity> FILE_DATA_TO_ENTITY =
      new Function<FileData, Entity>() {
        public Entity apply(FileData fileData) {
          return fileDataToEntity(fileData);
        }
      };

  private static final Function<BlobInfo, FileData> BLOB_INFO_TO_FILE_DATA =
      new Function<BlobInfo, FileData>() {
        @Override
        public FileData apply(BlobInfo blobInfo) {
          return blobInfoToFileData(blobInfo);
        }
      };

  public static Function<Entity, FileData> entityToFileData() {
    return ENTITY_TO_FILE_DATA;
  }

  public static Function<FileData, Entity> fileDataToEntity() {
    return FILE_DATA_TO_ENTITY;
  }

  public static Function<BlobInfo, FileData> blobInfoToEntity() {
    return BLOB_INFO_TO_FILE_DATA;
  }

  public static FileData entityToFileData(Entity entity) {
    FileData fileData = new FileData();
    fileData.setBlobKey((BlobKey) entity.getProperty("blobKey"));
    fileData.setFilename((String) entity.getProperty("filename"));
    return fileData;
  }

  public static Entity fileDataToEntity(FileData fileData) {
    Entity entity = new Entity(Constants.ENTITY_NAME);
    entity.setProperty("filename", fileData.getFilename());
    entity.setProperty("blobKey", fileData.getBlobKey());
    return entity;
  }

  public static FileData blobInfoToFileData(BlobInfo blobInfo) {
    return new FileData(blobInfo.getFilename(), blobInfo.getBlobKey());
  }
}
