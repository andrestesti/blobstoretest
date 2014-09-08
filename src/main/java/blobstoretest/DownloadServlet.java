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

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet downloads files from Blobstore.
 * 
 * @author Andrés Testi
 */
@Singleton
@SuppressWarnings("serial")
public class DownloadServlet extends HttpServlet {

  private final BlobstoreService blobstore;

  @Inject
  DownloadServlet(BlobstoreService blobstore) {
    this.blobstore = blobstore;
  }

  /**
   * Serves a file by means a blobKey.
   */
  @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
      throws ServletException, IOException {
    String blobKey = req.getParameter("blobKey");
    if(blobKey == null) {
      
      ErrorResponse error = new ErrorResponse();
      error.setCode(HttpServletResponse.SC_BAD_REQUEST);
      ErrorMessage message = new ErrorMessage();
      message.setMessage("The Blobkey parameter is required");
            
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.setContentType("application/json");
      
      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();      
      resp.getWriter().print(gson.toJson(error));
      
    } else {
      blobstore.serve(new BlobKey(blobKey), resp);
    }
  }
}
