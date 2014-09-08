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

import blobstoretest.Auth;

import com.google.api.server.spi.response.UnauthorizedException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * This interceptor applies validation for {@link Auth} annotation.
 * 
 * @author Andrés Testi
 */
public class AuthInterceptor implements MethodInterceptor {

  public static int authIndex(Class<?>[] paramTypes) {
    for (int i = 0; i < paramTypes.length; i++) {
      if (paramTypes[i].getAnnotation(Auth.class) != null) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {

    int authIndex = authIndex(invocation.getMethod().getParameterTypes());
    assert authIndex > -1;

    Object o = invocation.getArguments()[authIndex];
    if (o == null) {
      throw new UnauthorizedException("This service requires authentication");
    }

    return invocation.proceed();
  }
}
