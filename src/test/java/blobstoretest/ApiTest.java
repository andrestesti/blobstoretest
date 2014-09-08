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

import static org.junit.Assert.assertEquals;

import blobstoretest.shared.FileData;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.collect.ImmutableList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Tests for the Blobstoretest API.
 * 
 * @author Andrés Testi
 */
public class ApiTest {

  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
      new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void testlistFiles() {

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();

    Blobstoretest blobstoretest = new Blobstoretest(datastore, blobstore);

    FileData fd1 = new FileData("file1.txt", new BlobKey("file1"));
    putEntity(datastore, fd1);

    FileData fd2 = new FileData("file2.txt", new BlobKey("file2"));
    putEntity(datastore, fd2);

    List<FileData> expected = ImmutableList.of(fd1, fd2);

    List<FileData> actual = blobstoretest.listFiles(null, 0, 5);

    assertEquals(expected, actual);
  }

  private Entity putEntity(DatastoreService datastore, FileData fileData) {
    Entity entity = FileDataMapper.toEntity(fileData);
    datastore.put(entity);
    return entity;
  }
}
