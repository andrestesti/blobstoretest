package blobstoretest;

import blobstoretest.shared.UploadUrl;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WorkflowTest {
  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
      new LocalDatastoreServiceTestConfig());
  
  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }
  
  @Test
  public void testWorkflow() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
    Blobstoretest api = new Blobstoretest(datastore, blobstore);
    UploadUrl uploadUrl = api.createUploadUrl(null);
    
    HttpServletRequest req = mock(HttpServletRequest.class);
    HttpServletResponse res = mock(HttpServletResponse.class);
    
    UploadServlet uploadServlet = new UploadServlet(datastore, blobstore);
    
  }
}
