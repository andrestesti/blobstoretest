/**
 * @fileoverview
 * Provides methods for the Blobstoretest UI and interaction with the
 * Blobstore UI API.
 *
 * @author Andrés Testi
 */

/** blobstoretest namespace. */
var blobstoretest = blobstoretest || {};

/**
 * Client ID of the application (from the APIs Console).
 * 
 * @type {string}
 */
blobstoretest.CLIENT_ID = '370378822481-70j2omuds3g3gfqhualkourlmkha1jun.apps.googleusercontent.com';

/**
 * Scopes used by the application.
 * 
 * @type {string}
 */
blobstoretest.SCOPES = 'https://www.googleapis.com/auth/userinfo.email';

/**
 * Whether or not the user is signed in.
 * 
 * @type {boolean}
 */
blobstoretest.signedIn = false;

/**
 * Loads the application UI after the user has completed auth.
 */
blobstoretest.userAuthed = function() {
  var request = gapi.client.oauth2.userinfo.get().execute(function(resp) {
    if (!resp.code) {
      blobstoretest.signedIn = true;
      $('#signinButton').text('Sign out');
      $('#addFile').prop('disabled', false);
      $('#listFiles').prop('disabled', false);
      blobstoretest.listFiles();
    } else {
      blobstoretest.signedIn = false;
      $('#addFile').prop('disabled', true);
      $('#listFiles').prop('disabled', true);
    }
  });
};

/**
 * Handles the auth flow, with the given value for immediate mode.
 * 
 * @param {boolean}
 *          mode Whether or not to use immediate mode.
 * @param {Function}
 *          callback Callback to call on completion.
 */
blobstoretest.signin = function(mode, callback) {
  gapi.auth.authorize({
    client_id : blobstoretest.CLIENT_ID,
    scope : blobstoretest.SCOPES,
    immediate : mode
  }, callback);
};

/**
 * Presents the user with the authorization popup.
 */
blobstoretest.auth = function() {
  if (!blobstoretest.signedIn) {
    blobstoretest.signin(false, blobstoretest.userAuthed);
  } else {
    blobstoretest.signedIn = false;
    $('#signinButton').text('Sign in');
  }
};

/**
 * Prints a fileData to the greeting log. param {Object} greeting Greeting to
 * print.
 */
blobstoretest.print = function(fileData) {
  $('#outputLog').append(
      '<div class="row"><div class="span12"><a target="_blank" href="/download?blobKey='
          + fileData.blobKey.keyString + '">' + fileData.filename + '</a></div></div>');
};

/**
 * Lists files via the API.
 */
blobstoretest.listFiles = function() {
  gapi.client.blobstoretest.listFiles().execute(function(resp) {
    if (!resp.code) {
      resp.items = resp.items || [];
      $('#outputLog').empty();
      for (var i = 0; i < resp.items.length; i++) {
        blobstoretest.print(resp.items[i]);
      }
    }
  });
};

blobstoretest.disableUpload = function() {
  $('#addFile').prop('disabled', false);
  $('#uploadForm').hide();
}

blobstoretest.enableUpload = function() {
  $('#addFile').prop('disabled', true);
  $('#uploadForm').show();
}

/**
 * Enables the button callbacks in the UI.
 */
blobstoretest.enableButtons = function() {

  var uploadUrl = '';

  $('#addFile').click(function() {

    gapi.client.blobstoretest.createUploadUrl().execute(function(resp) {
      if (!resp.code) {
        uploadUrl = resp.value;
        blobstoretest.enableUpload();
      }
    });
  });

  $('#uploadButton').click(function() {
    var formData = new FormData($('#uploadForm')[0]);
    $.ajax({
      url : uploadUrl, 
      type : 'POST',
      success : function() {
        blobstoretest.disableUpload();
        blobstoretest.listFiles();
      },
      error : function() {
        window.alert('Uploading failure');
        blobstoretest.disableUpload();
      },
      data : formData,
      cache : false,
      contentType : false,
      processData : false
    });
  });

  $('#listFiles').click(function() {
    blobstoretest.listFiles();
  });

  $('#signinButton').click(function() {
    blobstoretest.auth();
  });

  $('#uploadForm').submit(function(event) {
    var uploadButton = $('uploadButton');
    event.preventDefault();

    // Update button text.
    uploadButton.innerHTML = 'Uploading...';

    // The rest of the code will go here...
  });
};

/**
 * Initializes the application.
 * 
 * @param {string}
 *          apiRoot Root of the API's path.
 */
blobstoretest.init = function() {

  var apiRoot = '//' + window.location.host + '/_ah/api';
  // Loads the OAuth and blobstoretest APIs asynchronously, and triggers login
  // when they have completed.
  var apisToLoad;
  var callback = function() {
    if (--apisToLoad == 0) {
      blobstoretest.enableButtons();
      blobstoretest.signin(true, blobstoretest.userAuthed);
    }
  }

  apisToLoad = 2; // must match number of calls to gapi.client.load()
  gapi.client.load('blobstoretest', 'v1', callback, apiRoot);
  gapi.client.load('oauth2', 'v2', callback);
}
