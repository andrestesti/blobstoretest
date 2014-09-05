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

import com.google.api.server.spi.response.BadRequestException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.validation.ConstraintViolationException;

/**
 * This interceptor translates {@link ConstraintViolationException}s to {@link BadRequestException}s.
 * 
 * @author Andrés Testi
 */
public class ValidationTranslationInterceptor implements MethodInterceptor {

  @Override public Object invoke(MethodInvocation invocation) throws Throwable {
    try {
      return invocation.proceed();
    } catch (ConstraintViolationException ve) {
      throw new BadRequestException("Validation constraint failed", ve);
    } catch (Throwable t) {
      throw t;
    }
  }
}
