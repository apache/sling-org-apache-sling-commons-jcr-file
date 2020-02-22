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
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;
import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.sling.resource.presence.ResourcePresence;
import org.apache.sling.testing.paxexam.TestSupport;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.util.Filter;

import static org.apache.sling.testing.paxexam.SlingOptions.slingQuickstartOakTar;
import static org.apache.sling.testing.paxexam.SlingOptions.slingResourcePresence;
import static org.apache.sling.testing.paxexam.SlingVersionResolver.SLING_GROUP_ID;
import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.cm.ConfigurationAdminOptions.factoryConfiguration;

public abstract class JcrFileTestSupport extends TestSupport {

    @Inject
    protected Repository repository;

    @Inject
    @Filter(value = "(type=jcr)")
    protected FileSystemProvider fileSystemProvider;

    @Inject
    @Filter(value = "(path=/content/starter/sling-logo.png)")
    private ResourcePresence resourcePresence;

    protected static final Credentials ADMIN_CREDENTIALS = new SimpleCredentials("admin", "admin".toCharArray());

    @Configuration
    public Option[] configuration() {
        return options(
            baseConfiguration(),
            quickstart(),
            // Sling Commons JCR File
            testBundle("bundle.filename"),
            // testing
            slingResourcePresence(),
            factoryConfiguration("org.apache.sling.resource.presence.internal.ResourcePresenter")
                .put("path", "/content/starter/sling-logo.png")
                .asOption(),
            mavenBundle().groupId(SLING_GROUP_ID).artifactId("org.apache.sling.starter.content").version("1.0.0"),
            mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.hamcrest").versionAsInProject(),
            junitBundles()
        );
    }

    protected Option quickstart() {
        final int httpPort = findFreePort();
        final String workingDirectory = workingDirectory();
        return composite(
            slingQuickstartOakTar(workingDirectory, httpPort)
        );
    }

    protected Session userSession(final String username) throws RepositoryException {
        final Session session = repository.login(ADMIN_CREDENTIALS);
        final Credentials credentials = new SimpleCredentials(username, "".toCharArray());
        return session.impersonate(credentials);
    }

    protected FileSystem fileSystem(final String username, final String path) throws Exception {
        final URI uri = new URI("jcr", null, path, null);
        final Session session = userSession(username);
        final Map<String, Object> env = Collections.singletonMap(Session.class.getName(), session);
        return fileSystemProvider.newFileSystem(uri, env);
    }

}
