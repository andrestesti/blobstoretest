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

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Defines v1 of a blobstoretest API.
 */
@Api(
    name = "blobstoretest",
    description = "This is an API to test Blobstore",
    version = "v1",
    scopes = {Constants.EMAIL_SCOPE},
 clientIds = {
        Constants.WEB_CLIENT_ID,
        Constants.COMMAND_LINE_CLIENT_ID, 
        Constants.API_EXPLORER_CLIENT_ID}
)
public class Blobstoretest {

  private final DatastoreService datastoreService;
  private final BlobstoreService blobstoreService;

  @Inject
  Blobstoretest(DatastoreService datastoreService, BlobstoreService blobstoreService) {
    this.datastoreService = datastoreService;
    this.blobstoreService = blobstoreService;
  }

  @ApiMethod(name = "createUploadUrl", path = "url", httpMethod = HttpMethod.POST)
  public UploadUrl createUploadUrl() {
    String url = blobstoreService.createUploadUrl("/_ah/api/blobstoretest/v1/upload");
    return new UploadUrl(url);
  }
    
  @ApiMethod(name = "upload", path = "upload", httpMethod = HttpMethod.POST)
  public List<BlobKey> upload(HttpServletRequest req, User user) {
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);

    List<BlobKey> blobKeys = blobs.get("files");

    for (BlobKey bk : blobKeys) {
      Entity file = new Entity("File");
      file.setProperty("user", user);
      file.setProperty("blobKey", bk);
      datastoreService.put(file);
    }

    return blobKeys;
  }
  
  @ApiMethod(name = "getUser", path = "user", httpMethod = HttpMethod.GET)
  public User getUser(User user) {
    return user;
  }
  
  @ApiMethod(name = "listFiles", path = "files", httpMethod = HttpMethod.GET)
  public List<BlobKey> listFiles(User user) {

    Filter filter = new FilterPredicate("user", FilterOperator.EQUAL, user);
    Query q = new Query("File").setFilter(filter);
    PreparedQuery pq = datastoreService.prepare(q);

    List<BlobKey> files = new ArrayList<>();

    for (Entity f : pq.asIterable()) {
      files.add((BlobKey) f.getProperty("blobKey"));
    }

    return files;
  }
}
