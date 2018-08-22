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
package org.apache.sling.commons.jcr.file.internal;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.commons.jcr.file.JcrFileSupportService;
import org.jetbrains.annotations.NotNull;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    property = {
        Constants.SERVICE_DESCRIPTION + "=Apache Sling Commons JCR File Support Service",
        Constants.SERVICE_VENDOR + "=The Apache Software Foundation"
    }
)
@Designate(
    ocd = DefaultJcrFileSupportServiceConfiguration.class
)
public class DefaultJcrFileSupportService implements JcrFileSupportService {

    private DefaultJcrFileSupportServiceConfiguration configuration;

    private final Logger logger = LoggerFactory.getLogger(DefaultJcrFileSupportService.class);

    public DefaultJcrFileSupportService() {
    }

    @Activate
    public void activate(final DefaultJcrFileSupportServiceConfiguration configuration) {
        this.configuration = configuration;
    }

    @Modified
    public void modified(final DefaultJcrFileSupportServiceConfiguration configuration) {
        this.configuration = configuration;
    }

    @Deactivate
    public void deactivate() {
        this.configuration = null;
    }

    public boolean isFile(@NotNull final Node node) throws RepositoryException {
        for (final String type : configuration.file_node_types()) {
            if (node.isNodeType(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDirectory(@NotNull final Node node) throws RepositoryException {
        for (final String type : configuration.directory_node_types()) {
            if (node.isNodeType(type)) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    public JcrFileAttributes fromPath(@NotNull final Path path) throws IOException {
        try {
            final JcrFileSystem fileSystem = (JcrFileSystem) path.getFileSystem();
            final Session session = fileSystem.getSession();
            final Node node = session.getNode(path.toString());
            final FileTime lastModifiedTime = timeFromProperty(node, "jcr:lastModified");
            final FileTime lastAccessTime = FileTime.fromMillis(0L);
            final FileTime creationTime = timeFromProperty(node, "jcr:created");
            final boolean isRegularFile = isFile(node);
            final boolean isDirectory = isDirectory(node);
            final boolean isSymbolicLink = false;
            final boolean isOther = !isRegularFile && !isDirectory;
            final long size;
            if (isRegularFile) {
                size = lengthOfFileContent(node);
            } else {
                size = 0;
            }
            final JcrFileAttributes jcrFileAttributes = new JcrFileAttributes(lastModifiedTime, lastAccessTime, creationTime, isRegularFile, isDirectory, isSymbolicLink, isOther, size);
            logger.info("from path {}: ", jcrFileAttributes);
            return jcrFileAttributes;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IOException(e);
        }
    }

    private static FileTime timeFromProperty(final Node node, final String name) {
        try {
            final Property property = node.getProperty(name);
            final Calendar date = property.getDate();
            return FileTime.fromMillis(date.getTimeInMillis());
        } catch (Exception e) {
            return FileTime.fromMillis(0L);
        }
    }

    private long lengthOfFileContent(final Node node) {
        try {
            return node.getNode("jcr:content").getProperty("jcr:data").getLength();
        } catch (Exception e) {
            return -1L;
        }
    }

}
