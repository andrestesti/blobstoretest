package blobstoretest.di;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

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
