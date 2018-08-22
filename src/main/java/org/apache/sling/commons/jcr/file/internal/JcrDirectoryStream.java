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
import java.nio.file.DirectoryStream;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JcrDirectoryStream implements DirectoryStream<Path> {

    private final JcrPath directory;

    private final JcrFileSystem fileSystem;

    private final DirectoryStream.Filter<? super Path> filter; // TODO

    private final Node node;

    private final Logger logger = LoggerFactory.getLogger(JcrDirectoryStream.class);

    // TODO take filter into account
    JcrDirectoryStream(@NotNull final JcrPath directory, final Filter<? super Path> filter) throws NotDirectoryException {
        this.directory = directory;
        this.filter = filter;
        final String path = directory.toString();
        try {
            fileSystem = (JcrFileSystem) directory.getFileSystem();
            node = fileSystem.getSession().getNode(path);
            logger.info("node: {} {}", node.getPath(), node.getPrimaryNodeType().getName());
            if (!fileSystem.provider().isDirectory(node)) {
                logger.error("{} is not a directory", path);
                throw new NotDirectoryException(path);
            }
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error reading from path " + path);
        }
    }

    @Override
    @NotNull
    public Iterator<Path> iterator() {
        logger.info("iterator for {}", directory);

        final Set<String> paths = new HashSet<>();

        try {
            final NodeIterator nodes = node.getNodes();
            while (nodes.hasNext()) {
                final Node node = nodes.nextNode();
                if (fileSystem.provider().isDirectory(node) || fileSystem.provider().isFile(node)) {
                    final String path = node.getPath();
                    paths.add(path);
                    logger.info("node {} added", path);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return new Iterator<Path>() {

            final Iterator<String> iterator = paths.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Path next() {
                return new JcrPath(fileSystem, iterator.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };

    }

    @Override
    public void close() throws IOException {
    }

}
