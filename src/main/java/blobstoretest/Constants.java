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

/**
 * Contains the client IDs and scopes for allowed clients consuming the blobstoretest API.
 */
public class Constants {
  
  public static final String WEB_CLIENT_ID =
      "370378822481-70j2omuds3g3gfqhualkourlmkha1jun.apps.googleusercontent.com";
  
  public static final String COMMAND_LINE_CLIENT_ID =
      "370378822481-ejhjqvgkekgkv4u02bku5fg27ddb623t.apps.googleusercontent.com";
  
  public static final String API_EXPLORER_CLIENT_ID =
      com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID;

  public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";

  public static final String UPLOAD_REDIRECT_URL = "/ah/api/blobstoretest/v1/upload";
}
