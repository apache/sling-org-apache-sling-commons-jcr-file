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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.commons.jcr.file.JcrFileSupportService;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = FileSystemProvider.class,
    property = {
        Constants.SERVICE_DESCRIPTION + "=Apache Sling Commons JCR File System Provider",
        Constants.SERVICE_VENDOR + "=The Apache Software Foundation",
        "type=jcr"
    }
)
public class JcrFileSystemProvider extends FileSystemProvider {

    private final Map<Session, JcrFileSystem> cache = new HashMap<>();

    private final JcrFileStore jcrFileStore = new JcrFileStore();

    @Reference
    private volatile JcrFileSupportService jcrFileSupportService;

    private final Object lock = new Object();

    static final String SCHEME = "jcr";

    private final Logger logger = LoggerFactory.getLogger(JcrFileSystemProvider.class);

    public JcrFileSystemProvider() {
    }

    @Override
    public String getScheme() {
        logger.info("getting scheme");
        return SCHEME;
    }

    @Override
    public FileSystem newFileSystem(final URI uri, final Map<String, ?> env) throws IOException {
        logger.info("new file system: {}, {}", uri, env);

        if (!getScheme().equalsIgnoreCase(uri.getScheme())) {
            throw new IllegalArgumentException("URI scheme is not " + getScheme());
        }

        if (env == null) {
            throw new IllegalArgumentException("env is null");
        }

        if (!env.containsKey(Session.class.getName())) {
            throw new IllegalArgumentException("no session in env");
        }

        final Object object = env.get(Session.class.getName());
        if (object == null) {
            throw new IllegalArgumentException("session in env is null");
        }

        if (!(object instanceof Session)) {
            throw new IllegalArgumentException("session in env is not a " + Session.class.getName());
        }

        final Session session = (Session) object;
        if (!session.isLive()) {
            throw new IllegalArgumentException("session in env is not live");
        }

        synchronized (lock) {
            if (isInCache(session)) {
                throw new IllegalArgumentException("session is already in use");
            }
            final JcrFileSystem fileSystem = new JcrFileSystem(this, uri, session);
            putIntoCache(fileSystem);
            return fileSystem;
        }
    }

    @Override
    public FileSystem getFileSystem(final URI uri) {
        final String message = String.format("getting file system by URI is not supported: %s", uri.toString());
        logger.error(message);
        throw new UnsupportedOperationException(message);
    }

    @Override
    public Path getPath(final URI uri) {
        final String message = String.format("getting path by URI is not supported: %s", uri.toString());
        logger.error(message);
        throw new UnsupportedOperationException(message);
    }

    // TODO
    public FileChannel newFileChannel(final Path path, final Set<? extends OpenOption> options, final FileAttribute<?>... attrs) throws IOException {
        logger.info("newFileChannel");
        try {
            final JcrPath jcrPath = (JcrPath) path;
            final Node node;
            if (jcrPath.toFile().exists()) {
                node = PathUtil.toNode(path);
            } else {
                node = jcrFileSupportService.newFile(path);
            }
            return new JcrFileChannel(node);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IOException(e);
        }
    }

    // TODO
    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        logger.info("newByteChannel");
        try {
            final Node node = PathUtil.toNode(path);
            return new JcrFileChannel(node);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IOException(e);
        }
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(final Path dir, final DirectoryStream.Filter<? super Path> filter) throws IOException {
        logger.info("new directory stream for {}", dir.toString());
        if (dir instanceof JcrPath) {
            return new JcrDirectoryStream((JcrPath) dir, filter);
        } else {
            throw new IllegalArgumentException("directory is not a JCR path");
        }
    }

    // TODO
    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
        logger.info("createDirectory");
    }

    // TODO
    @Override
    public void delete(Path path) throws IOException {
        logger.info("delete: {}", path);
        final File file = path.toFile();
        if (!file.exists()) {
            throw new NoSuchFileException(path.toString());
        } else {
            // TODO delete
        }
    }

    // TODO
    @Override
    public void copy(Path source, Path target, CopyOption... options) throws IOException {
        logger.info("copy");
    }

    // TODO
    @Override
    public void move(Path source, Path target, CopyOption... options) throws IOException {
        logger.info("move");
    }

    // TODO
    @Override
    public boolean isSameFile(Path path, Path path2) throws IOException {
        logger.info("isSameFile");
        return false;
    }

    // TODO
    @Override
    public boolean isHidden(Path path) throws IOException {
        logger.info("isHidden");
        return false;
    }

    @Override
    public FileStore getFileStore(final Path path) throws IOException {
        logger.info("getting file store for {}", path.toString());
        return jcrFileStore;
    }

    // TODO
    @Override
    public void checkAccess(final Path path, final AccessMode... modes) throws IOException {
        logger.info("checking access: {}", path.toString());
    }

    // TODO
    @Override
    public <V extends FileAttributeView> V getFileAttributeView(final Path path, final Class<V> type, final LinkOption... options) {
        logger.info("getting file attribute view");
        return null;
    }

    // TODO
    @Override
    public <A extends BasicFileAttributes> A readAttributes(final Path path, final Class<A> type, final LinkOption... options) throws IOException {
        logger.info("reading attributes: {}, {}, {}", path, type, options);
        if (type == BasicFileAttributes.class) {
            final BasicFileAttributes basicFileAttributes = jcrFileSupportService.fromPath(path);
            logger.info("basic file attributes: {}", basicFileAttributes);
            return (A) basicFileAttributes;
        } else {
            throw new IOException("Unsupported file attributes type: " + type);
        }
    }

    // TODO
    @Override
    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
        logger.info("readAttributes: {}, {}, {}", path, attributes, options);
        return Collections.emptyMap();
    }

    // TODO
    @Override
    public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
        logger.info("setAttribute");
    }

    boolean isFile(final Node node) throws RepositoryException {
        return jcrFileSupportService.isFile(node);
    }

    boolean isDirectory(final Node node) throws RepositoryException {
        return jcrFileSupportService.isDirectory(node);
    }

    boolean isInCache(final Session session) {
        return cache.containsKey(session);
    }

    void putIntoCache(final JcrFileSystem fileSystem) {
        cache.put(fileSystem.getSession(), fileSystem);
    }

    void removeFromCache(final JcrFileSystem fileSystem) {
        cache.remove(fileSystem.getSession());
    }

}
