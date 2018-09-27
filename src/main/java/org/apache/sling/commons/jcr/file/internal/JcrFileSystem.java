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
import java.net.URI;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.Collections;
import java.util.Set;

import javax.jcr.Session;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JcrFileSystem extends FileSystem {

    private final JcrFileSystemProvider provider;

    private final URI uri;

    private final Session session;

    private final Iterable<Path> rootDirectories;

    static final String SEPARATOR = "/";

    private final Logger logger = LoggerFactory.getLogger(JcrFileSystem.class);

    JcrFileSystem(final JcrFileSystemProvider provider, final URI uri, final Session session) {
        this.provider = provider;
        this.uri = uri;
        this.session = session;
        rootDirectories = Collections.singleton(new JcrPath(this, "/"));
    }

    @Override
    public JcrFileSystemProvider provider() {
        logger.trace("provider");
        return provider;
    }

    @Override
    public void close() throws IOException {
        logger.info("close");
        provider.removeFromCache(this);
        try {
            session.save();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IOException(e);
        }
        session.logout();
    }

    @Override
    public boolean isOpen() {
        logger.info("isOpen");
        return session.isLive();
    }

    @Override
    public boolean isReadOnly() {
        logger.info("isReadOnly");
        return false;
    }

    @Override
    public String getSeparator() {
        logger.info("getSeparator");
        return SEPARATOR;
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        logger.info("getRootDirectories");
        return rootDirectories;
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        logger.info("getFileStores");
        return null;
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        logger.info("supportedFileAttributeViews");
        return Collections.singleton("basic");
    }

    @Override
    @NotNull
    public Path getPath(final String first, final String... more) {
        logger.info("getPath: {}, {}", first, more);
        return new JcrPath(this, first, more);
    }

    @Override
    public PathMatcher getPathMatcher(String syntaxAndPattern) {
        logger.info("getPathMatcher");
        throw new UnsupportedOperationException(); // TODO implement?
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        logger.info("getUserPrincipalLookupService");
        throw new UnsupportedOperationException(); // TODO implement?
    }

    @Override
    public WatchService newWatchService() throws IOException {
        logger.info("newWatchService");
        throw new UnsupportedOperationException(); // TODO implement?
    }

    Session getSession() {
        return session;
    }

}
