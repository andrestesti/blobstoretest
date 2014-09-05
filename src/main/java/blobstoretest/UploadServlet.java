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
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;

import java.io.IOException;
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
public class UploadServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  
  private final DatastoreService datastore;
  private final BlobstoreService blobstore;

  @Inject
  UploadServlet(DatastoreService datastore, BlobstoreService blobstore) {
    this.datastore = datastore;
    this.blobstore = blobstore;
  }

  /**
   * Registers the uploaded files in the Datastore, and returns a list of uploaded files.
   */
  @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
      throws ServletException, IOException {
    Map<String, List<BlobInfo>> blobs;
    try {
      blobs = blobstore.getBlobInfos(req);
    } catch (IllegalStateException e) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.setContentType("application/json");
      resp.getWriter().format(
          "{\"error\": {\"message\": \"Blobstore service illegal state\"}, \"code\": %d}",
          HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    List<BlobInfo> infos = blobs.get("files");

    for (BlobInfo i : infos) {

      FileData data = new FileData(i.getFilename(), i.getBlobKey());

      Entity entity = new Entity(Constants.ENTITY_NAME);
      entity.setProperty("filename", data.getFilename());
      entity.setProperty("blobKey", data.getBlobKey());

      datastore.put(entity);
    }

    resp.setStatus(HttpServletResponse.SC_OK);
    resp.setContentType("application/json");
    resp.getWriter().write("{}");
    resp.getOutputStream().flush();
  }
}
