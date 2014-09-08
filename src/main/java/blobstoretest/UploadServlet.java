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

import blobstoretest.shared.ErrorMessage;
import blobstoretest.shared.ErrorResponse;
import blobstoretest.shared.FileData;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet upload files to Blobstore.
 * 
 * @author Andrés Testi
 */
@Singleton
@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {
  
  private final DatastoreService datastore;
  private final BlobstoreService blobstore;
 
  @Inject
  UploadServlet(DatastoreService datastore, BlobstoreService blobstore) {
    this.datastore = datastore;
    this.blobstore = blobstore;
  }

  /**
   * Registers the uploaded files in the Datastore.
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
      throws ServletException, IOException {
    resp.setContentType("application/json");

    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();

    Map<String, List<BlobInfo>> blobs;
    try {
      blobs = blobstore.getBlobInfos(req);
    } catch (IllegalStateException e) {

      ErrorResponse error = new ErrorResponse();
      error.setCode(HttpServletResponse.SC_BAD_REQUEST);
      ErrorMessage message = new ErrorMessage();
      message.setMessage("Blobstore service illegal state");

      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().print(gson.toJson(error));
      return;
    }

    List<BlobInfo> infos = blobs.get("files");
    ArrayList<FileData> files = new ArrayList<>();

    for (BlobInfo i : infos) {
      FileData fileData = new FileData(i.getFilename(), i.getBlobKey());
      Entity entity = FileDataMapper.toEntity(fileData);
      files.add(fileData);
      datastore.put(entity);
    }

    resp.setStatus(HttpServletResponse.SC_OK);
    resp.getWriter().print(gson.toJson(files));
  }
}
