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
import blobstoretest.shared.UploadUrl;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.DefaultValue;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.User;

import org.apache.bval.guice.Validate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Defines v1 of the blobstoretest API.
 */
@Api(
    name = "blobstoretest",
    description = "This is an API to test Blobstore",
    version = Constants.VERSION,
    scopes = Constants.EMAIL_SCOPE,
 clientIds = {
        Constants.WEB_CLIENT_ID,
        Constants.COMMAND_LINE_CLIENT_ID, 
        Constants.API_EXPLORER_CLIENT_ID}
)
public class Blobstoretest {
  
  private final DatastoreService datastore;
  private final BlobstoreService blobstore;

  @Inject
  Blobstoretest(DatastoreService datastore, BlobstoreService blobstore) {
    this.datastore = datastore;
    this.blobstore = blobstore;
  }

  /**
   * Returns an upload URL for Blobstore. This is a POST method to respect the RESTful principles,
   * because this is not an idempotent action.
   * Authentication required.
   * 
   * @param user the authenticated user.
   * @return a wrapped upload URL.
   */
  @ApiMethod(name = "createUploadUrl", path = "url", httpMethod = HttpMethod.POST)
  public UploadUrl createUploadUrl(@Auth User user) {
    String url = blobstore.createUploadUrl(Constants.UPLOAD_URL);
    return new UploadUrl(url);
  }

  /**
   * Returns the paginated list of uploaded files.
   * 
   * @param user the authenticated user.
   * @param offset the file list offset.
   * @param limit the file list limit.
   * @return the paginated list of uploaded files.
   */
  @ApiMethod(name = "listFiles", path = "files", httpMethod = HttpMethod.GET)
  @Validate
  public List<FileData> listFiles(
      @Auth User user, 
      @Named("offset") @DefaultValue("0") @Min(0) int offset, 
      @Named("limit") @DefaultValue("100") @Min(0) @Max(100) int limit) {
    
    Filter filter = new FilterPredicate("user", FilterOperator.EQUAL, user);
    Query q = new Query(Constants.ENTITY_NAME).setFilter(filter);
    PreparedQuery pq = datastore.prepare(q);

    Iterable<Entity> entities = pq.asIterable(FetchOptions.Builder.withOffset(offset).limit(limit));
    List<FileData> files = new ArrayList<>();

    for (Entity e : entities) {
      String filename = (String) e.getProperty("filename");
      BlobKey blobKey = (BlobKey) e.getProperty("blobKey");
      files.add(new FileData(filename, blobKey));
    }

    return files;
  }
}
