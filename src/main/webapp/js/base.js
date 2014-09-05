
/**
 * @fileoverview
 * Provides methods for the Hello Endpoints sample UI and interaction with the
 * Hello Endpoints API.
 *
 * @author Andrés Testi
 */

/** blobstoretest namespace. */
var blobstoretest = blobstoretest || {};

/**
 * Client ID of the application (from the APIs Console).
 * @type {string}
 */
blobstoretest.CLIENT_ID =
    '370378822481-70j2omuds3g3gfqhualkourlmkha1jun.apps.googleusercontent.com';

/**
 * Scopes used by the application.
 * @type {string}
 */
blobstoretest.SCOPES =
    'https://www.googleapis.com/auth/userinfo.email';

/**
 * Whether or not the user is signed in.
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
      document.getElementById('signinButton').innerHTML = 'Sign out';
      document.getElementById('authedGreeting').disabled = false;
    }
  });
};

/**
 * Handles the auth flow, with the given value for immediate mode.
 * @param {boolean} mode Whether or not to use immediate mode.
 * @param {Function} callback Callback to call on completion.
 */
blobstoretest.signin = function(mode, callback) {
  gapi.auth.authorize({client_id: blobstoretest.CLIENT_ID,
      scope: blobstoretest.SCOPES, immediate: mode},
      callback);
};

/**
 * Presents the user with the authorization popup.
 */
blobstoretest.auth = function() {
  if (!blobstoretest.signedIn) {
    blobstoretest.signin(false,
        blobstoretest.userAuthed);
  } else {
    blobstoretest.signedIn = false;
    document.getElementById('signinButton').innerHTML = 'Sign in';
    document.getElementById('authedGreeting').disabled = true;
  }
};

/**
 * Prints a fileData to the greeting log.
 * param {Object} greeting Greeting to print.
 */
blobstoretest.print = function(fileData) {
  var element = document.createElement('div');
  element.classList.add('row');
  element.innerHTML = fileData.filename;
  document.getElementById('outputLog').appendChild(element);
};

/**
 * Gets a numbered greeting via the API.
 * @param {string} id ID of the greeting.
 */
blobstoretest.getGreeting = function(id) {
  gapi.client.blobstoretest.blobstoretest.getGreeting({'id': id}).execute(
      function(resp) {
        if (!resp.code) {
          google.devrel.samples.hello.print(resp);
        } else {
          window.alert(resp.message);
        }
      });
};

/**
 * Lists greetings via the API.
 */
blobstoretest.listGreeting = function() {
  gapi.client.blobstoretest.blobstoretest.listGreeting().execute(
      function(resp) {
        if (!resp.code) {
          resp.items = resp.items || [];
          for (var i = 0; i < resp.items.length; i++) {
            google.devrel.samples.hello.print(resp.items[i]);
          }
        }
      });
};

/**
 * Gets a greeting a specified number of times.
 * @param {string} greeting Greeting to repeat.
 * @param {string} count Number of times to repeat it.
 */
blobstoretest.multiplyGreeting = function(
    greeting, times) {
  gapi.client.blobstoretest.blobstoretest.multiply({
      'message': greeting,
      'times': times
    }).execute(function(resp) {
      if (!resp.code) {
        google.devrel.samples.hello.print(resp);
      }
    });
};

/**
 * Greets the current user via the API.
 */
blobstoretest.authedGreeting = function(id) {
	blobstoretest.authed().execute(
      function(resp) {
    	  blobstoretest.print(resp);
      });
};

/**
 * Enables the button callbacks in the UI.
 */
blobstoretest.enableButtons = function() {
  document.getElementById('getGreeting').onclick = function() {
	  blobstoretest.getGreeting(
        document.getElementById('id').value);
  }

  document.getElementById('listGreeting').onclick = function() {
	  blobstoretest.listGreeting();
  }

  document.getElementById('multiplyGreetings').onclick = function() {
	  blobstoretest.multiplyGreeting(
        document.getElementById('greeting').value,
        document.getElementById('count').value);
  }

  document.getElementById('authedGreeting').onclick = function() {
	  blobstoretest.authedGreeting();
  }
  
  document.getElementById('signinButton').onclick = function() {
	  blobstoretest.auth();
  }
};

/**
 * Initializes the application.
 * @param {string} apiRoot Root of the API's path.
 */
blobstoretest.init = function(apiRoot) {
  // Loads the OAuth and helloworld APIs asynchronously, and triggers login
  // when they have completed.
  var apisToLoad;
  var callback = function() {
    if (--apisToLoad == 0) {
    	blobstoretest.enableButtons();
    	blobstoretest.signin(true,
    			blobstoretest.userAuthed);
    }
  }

  apisToLoad = 2; // must match number of calls to gapi.client.load()
  gapi.client.load('blobstoretest', 'v1', callback, apiRoot);
  gapi.client.load('oauth2', 'v2', callback);
};
