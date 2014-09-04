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
package blobstoretest.di;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * AppEngine services.
 * 
 * @author Andrés Testi
 */
public class GaeServiceModule extends AbstractModule {

  @Override
  protected void configure() {}

  @Provides
  BlobstoreService getBlobstoreService() {
    return BlobstoreServiceFactory.getBlobstoreService();
  }
  
  @Provides
  DatastoreService getDatastoreService() {
    return DatastoreServiceFactory.getDatastoreService();
  }

}
