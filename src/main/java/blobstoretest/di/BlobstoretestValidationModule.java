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

import static com.google.inject.matcher.Matchers.annotatedWith;

import blobstoretest.Auth;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.response.BadRequestException;
import com.google.appengine.api.users.User;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;

import org.apache.bval.guice.Validate;
import org.apache.bval.guice.ValidationModule;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.validation.ConstraintViolationException;

/**
 * This module applies validations for Bean Validators and {@link Auth} annotation.
 * Also translates {@link ConstraintViolationException} to {@link BadRequestException}.
 * 
 * @author Andrés Testi
 */
public class BlobstoretestValidationModule extends AbstractModule {

  private static final Matcher<Method> AUTH_MATCHER = new AbstractMatcher<Method>() {
    public boolean matches(Method m) {
      int authIndex = AuthInterceptor.authIndex(m.getParameters());
      if (authIndex > -1) {
        Parameter p = m.getParameters()[authIndex];
        return User.class.isAssignableFrom(p.getType());
      }
      return false;
    };
  };

  @Override protected void configure() {
    bindInterceptor(annotatedWith(Api.class), AUTH_MATCHER, new AuthInterceptor());
    
    // We must respect this interceptor order.
    bindInterceptor(annotatedWith(Api.class), annotatedWith(Validate.class),
        new ValidationTranslationInterceptor());
    install(new ValidationModule());
  }
}
