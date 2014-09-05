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

import blobstoretest.Blobstoretest;
import blobstoretest.Serve;

import com.google.api.server.spi.guice.GuiceSystemServiceServletModule;

import java.util.Arrays;

/**
 * @author Andrés Testi
 */
public class BlobstoretestServletModule extends GuiceSystemServiceServletModule {

  @Override protected void configureServlets() {
    super.configureServlets();

    serve("/serve").with(Serve.class);
    serveGuiceSystemServiceServlet("/_ah/spi/*", Arrays.asList(Blobstoretest.class));
  }
}
