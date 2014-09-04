package blobstoretest;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class Serve extends HttpServlet {

  private static final long serialVersionUID = 1L;
  
  private final BlobstoreService blobstoreService;

  @Inject
  Serve(BlobstoreService blobstoreService) {
    this.blobstoreService = blobstoreService;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws 
      ServletException, IOException {
    BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
    blobstoreService.serve(blobKey, resp);            
  }
}
