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

import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.oauth.OAuthService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter validates authenticated users.
 * 
 * @author Andrés Testi
 */
@Singleton
public class OAuthFilter implements Filter {

  private final OAuthService oauth;

  @Inject
  OAuthFilter(OAuthService oauth) {
    this.oauth = oauth;
  }

  @Override
  public void init(FilterConfig conf) throws ServletException {}

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    try {
      oauth.getCurrentUser();
      chain.doFilter(req, res);
    } catch (OAuthRequestException e) {

      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();      
      
      HttpServletResponse httpRes = (HttpServletResponse) res;
      httpRes.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      httpRes.setContentType("application/json");
      
      ErrorResponse error = new ErrorResponse();
      error.setCode(HttpServletResponse.SC_UNAUTHORIZED);
      ErrorMessage message = new ErrorMessage();
      message.setMessage("This service requires authentication");
      
      httpRes.getWriter().print(gson.toJson(error));      
    }
  }

  @Override
  public void destroy() {}
}
