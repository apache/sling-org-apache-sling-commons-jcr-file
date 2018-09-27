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
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JcrPath implements Path {

    private final JcrFileSystem fileSystem;

    private final String path;

    private final List<String> names;

    private final Logger logger = LoggerFactory.getLogger(JcrPath.class);

    JcrPath(final JcrFileSystem fileSystem, final String path) {
        this.fileSystem = fileSystem;
        this.path = PathUtil.normalize(path);
        this.names = names(this.path);
        logger.info("new path: {}", path);
    }

    JcrPath(final JcrFileSystem fileSystem, final String first, final String... more) {
        this.fileSystem = fileSystem;
        this.path = PathUtil.normalize(first + JcrFileSystem.SEPARATOR + String.join(JcrFileSystem.SEPARATOR, more));
        this.names = names(this.path);
        logger.info("new path: {}", path);
    }

    private List<String> names(final String path) {
        final List<String> names = new ArrayList<>();
        if (path != null) {
            final String[] strings = path.split("/");
            for (final String string : strings) {
                if (!string.isEmpty()) {
                    names.add(string);
                }
            }
        }
        return names;
    }

    @Override
    public FileSystem getFileSystem() {
        logger.info("getting file system for {}", path);
        return fileSystem;
    }

    @Override
    public boolean isAbsolute() {
        logger.info("isAbsolute: {}", path);
        return path.startsWith("/");
    }

    @Override
    public Path getRoot() {
        logger.info("getting root for {}", path);
        return new JcrPath(fileSystem, "/");
    }

    @Override
    public Path getFileName() {
        logger.info("getting file name for {}", path);
        if (names.size() == 0) {
            return null;
        } else {
            final String name = names.get(names.size() - 1);
            logger.info("file name: {}", name);
            return new JcrPath(fileSystem, name);
        }
    }

    @Override
    public Path getParent() {
        logger.info("getting parent for {}", path);
        final String parent = PathUtil.getParent(path);
        if (parent == null) {
            return null;
        }
        return new JcrPath(fileSystem, parent);
    }

    @Override
    public int getNameCount() {
        logger.info("getting name count: {}", path);
        return names.size();
    }

    @Override
    public Path getName(int index) {
        logger.info("getting name: {}", path);
        if (names.size() == 0) {
            throw new IllegalArgumentException("TODO");
        }
        if (index < 0) {
            throw new IllegalArgumentException("TODO");
        }
        if (index >= names.size()) {
            throw new IllegalArgumentException("TODO");
        }
        return new JcrPath(fileSystem, names.get(index));
    }

    // TODO
    @Override
    public Path subpath(int beginIndex, int endIndex) {
        logger.info("subpath: {}", path);
        return null;
    }

    // TODO
    @Override
    public boolean startsWith(Path other) {
        logger.info("startsWith: {}", path);
        return false;
    }

    // TODO
    @Override
    public boolean startsWith(String other) {
        logger.info("startsWith: {}", path);
        return false;
    }

    // TODO
    @Override
    public boolean endsWith(Path other) {
        logger.info("endsWith: {}", path);
        return false;
    }

    // TODO
    @Override
    public boolean endsWith(String other) {
        logger.info("endsWith: {}", path);
        return false;
    }

    @Override
    public Path normalize() {
        logger.info("normalizing path {}", path);
        return new JcrPath(fileSystem, PathUtil.normalize(path));
    }

    // TODO
    @Override
    public Path resolve(Path other) {
        logger.info("resolving given path {} against this path {}", other, this);
        if (other.isAbsolute()) {
            return other;
        }
        if (this.path.endsWith("/")) {
            final String path = this.path.concat(other.toString());
            return new JcrPath(fileSystem, path);
        } else {
            final String path = String.format("%s/%s", this.path, other.toString());
            return new JcrPath(fileSystem, path);
        }
    }

    // TODO
    @Override
    public Path resolve(String other) {
        logger.info("resolving given path {} against this path {}", other, this);
        final Path path = new JcrPath(fileSystem, other); // TODO InvalidPathException
        return resolve(path);
    }

    // TODO
    @Override
    public Path resolveSibling(Path other) {
        logger.info("resolveSibling: {}", other);
        return null;
    }

    // TODO
    @Override
    public Path resolveSibling(String other) {
        logger.info("resolveSibling: {}", other);
        return null;
    }

    // TODO
    @Override
    public Path relativize(Path other) {
        logger.info("relativize: {}", other);
        return null;
    }

    // TODO
    @Override
    public URI toUri() {
        logger.info("toUri: {}", path);
        return null;
    }

    @Override
    public Path toAbsolutePath() {
        logger.info("toAbsolutePath: {}", path);
        if (isAbsolute()) {
            return this;
        } else {
            return new JcrPath(fileSystem, "/".concat(path));
        }
    }

    // TODO
    @Override
    public Path toRealPath(LinkOption... options) throws IOException {
        logger.info("toRealPath: {}", path);
        return null;
    }

    @Override
    public JcrFile toFile() {
        logger.info("to file: {}", path);
        return new JcrFile(fileSystem, path);
    }

    // TODO
    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
        logger.info("register1");
        return null;
    }

    // TODO
    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>... events) throws IOException {
        logger.info("register2");
        return null;
    }

    // TODO
    @Override
    public Iterator<Path> iterator() {
        logger.info("iterator");
        return null;
    }

    // TODO
    @Override
    public int compareTo(Path other) {
        logger.info("compareTo");
        return 0;
    }

    @Override
    public String toString() {
        return path;
    }

}
