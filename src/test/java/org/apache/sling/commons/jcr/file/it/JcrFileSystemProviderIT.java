/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.commons.jcr.file.it;

import java.net.URI;
import java.nio.file.FileSystem;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.fail;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class JcrFileSystemProviderIT extends JcrFileTestSupport {

    @Test
    public void testFileSystemProvider() {
        assertThat(fileSystemProvider, notNullValue());
    }

    @Test
    public void testGetScheme() {
        assertThat("jcr", equalTo(fileSystemProvider.getScheme()));
    }

    @Test
    public void testNewFileSystem() throws Exception {
        final URI uri = new URI("jcr", null, "/", null);
        final Map<String, Object> env = new HashMap<>();
        final Session session = userSession("admin");
        env.put(Session.class.getName(), session);
        final FileSystem fileSystem = fileSystemProvider.newFileSystem(uri, env);
        assertThat(fileSystem, notNullValue());
        fileSystem.close();
        assertThat(session.isLive(), is(false));
    }

    @Test
    public void testNewFileSystem_InvalidScheme() throws Exception {
        final URI uri = new URI("/");
        final Map<String, Object> env = new HashMap<>();
        final Session session = userSession("admin");
        env.put(Session.class.getName(), session);
        try {
            fileSystemProvider.newFileSystem(uri, env);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("URI scheme is not " + fileSystemProvider.getScheme()));
        }
    }

    @Test
    public void testNewFileSystem_EnvIsNull() throws Exception {
        final URI uri = new URI("jcr", null, "/", null);
        try {
            fileSystemProvider.newFileSystem(uri, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("env is null"));
        }
    }

    @Test
    public void testNewFileSystem_NoSession() throws Exception {
        final URI uri = new URI("jcr", null, "/", null);
        final Map<String, Object> env = new HashMap<>();
        try {
            fileSystemProvider.newFileSystem(uri, env);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("no session in env"));
        }
    }

    @Test
    public void testNewFileSystem_SessionIsNull() throws Exception {
        final URI uri = new URI("jcr", null, "/", null);
        final Map<String, Object> env = new HashMap<>();
        env.put(Session.class.getName(), null);
        try {
            fileSystemProvider.newFileSystem(uri, env);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("session in env is null"));
        }
    }

    @Test
    public void testNewFileSystem_InvalidSession() throws Exception {
        final URI uri = new URI("jcr", null, "/", null);
        final Map<String, Object> env = new HashMap<>();
        env.put(Session.class.getName(), new Object());
        try {
            fileSystemProvider.newFileSystem(uri, env);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("session in env is not a " + Session.class.getName()));
        }
    }

    @Test
    public void testNewFileSystem_SessionInUse() throws Exception {
        final URI uri = new URI("jcr", null, "/", null);
        final Map<String, Object> env = new HashMap<>();
        final Session session = userSession("admin");
        env.put(Session.class.getName(), session);
        final FileSystem fileSystem = fileSystemProvider.newFileSystem(uri, env);
        assertThat(fileSystem, notNullValue());
        try {
            fileSystemProvider.newFileSystem(uri, env);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("session is already in use"));
        } finally {
            fileSystem.close();
        }
    }

}
