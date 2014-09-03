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

import com.google.common.collect.ImmutableList;

import java.util.List;

import javax.inject.Singleton;

@Singleton
public class GreetingsRepository {

  private final List<HelloGreeting> greetings;

  GreetingsRepository() {
    greetings =
        ImmutableList.of(new HelloGreeting("hello world!"), new HelloGreeting("goodbye world!"));
  }

  public HelloGreeting getGreeting(Integer index) {
    return greetings.get(index);
  }

  public List<HelloGreeting> list() {
    return greetings;
  }
}
