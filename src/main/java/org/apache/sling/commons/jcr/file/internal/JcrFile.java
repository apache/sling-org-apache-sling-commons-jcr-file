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
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JcrFile extends File {

    private final String path;

    private final JcrFileSystem fileSystem;

    private final Logger logger = LoggerFactory.getLogger(JcrFile.class);

    JcrFile(@NotNull final JcrFileSystem fileSystem, @NotNull String pathname) {
        super(pathname);
        this.fileSystem = fileSystem;
        this.path = PathUtil.normalize(pathname); // TODO
    }

    @Override
    @NotNull
    public String getName() {
        logger.info("getting name for {}", path);
        return PathUtil.getName(path);
    }

    @Override
    public String getParent() {
        logger.info("getting parent for {}", path);
        return PathUtil.getParent(path);
    }

    @Override
    public File getParentFile() {
        logger.info("getting parent file for {}", path);
        final String path = PathUtil.getParent(this.path);
        if (path != null) {
            return new JcrFile(fileSystem, path);
        } else {
            return null;
        }
    }

    @Override
    @NotNull
    public String getPath() {
        logger.info("getting path {}", path);
        return path;
    }

    @Override
    public boolean isAbsolute() {
        logger.info("is absolute {}", path);
        return PathUtil.isAbsolute(path);
    }

    // TODO
    @Override
    @NotNull
    public String getAbsolutePath() {
        logger.info("getting absolute path {}", path);
        throw new UnsupportedOperationException("getAbsolutePath");
    }

    @Override
    @NotNull
    public File getAbsoluteFile() {
        logger.info("getting absolute file for {}", path);
        return new JcrFile(fileSystem, getAbsolutePath());
    }

    // TODO
    @Override
    @NotNull
    public String getCanonicalPath() throws IOException {
        logger.info("getCanonicalPath");
        throw new UnsupportedOperationException("getCanonicalPath");
    }

    // TODO
    @Override
    @NotNull
    public File getCanonicalFile() throws IOException {
        logger.info("getCanonicalFile");
        throw new UnsupportedOperationException("getCanonicalFile");
    }

    // TODO
    @Override
    public URL toURL() throws MalformedURLException {
        logger.info("toURL");
        throw new UnsupportedOperationException("toURL");
    }

    // TODO
    @Override
    @NotNull
    public URI toURI() {
        logger.info("toURI");
        throw new UnsupportedOperationException("toURI");
    }

    // TODO
    @Override
    public boolean canRead() {
        logger.info("canRead");
        return true;
    }

    // TODO
    @Override
    public boolean canWrite() {
        logger.info("canWrite");
        return true;
    }

    // TODO
    @Override
    public boolean exists() {
        logger.info("exists");
        return true;
    }

    @Override
    public boolean isDirectory() {
        logger.info("is directory {}", path);
        try {
            return fileSystem.provider().isDirectory(getNode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isFile() {
        logger.info("is file {}", path);
        try {
            return fileSystem.provider().isFile(getNode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // TODO
    @Override
    public boolean isHidden() {
        logger.info("isHidden");
        return false;
    }

    // TODO
    @Override
    public long lastModified() {
        logger.info("lastModified");
        throw new UnsupportedOperationException("lastModified");
    }

    // TODO
    @Override
    public long length() {
        logger.info("length");
        throw new UnsupportedOperationException("length");
    }

    // TODO
    @Override
    public boolean createNewFile() throws IOException {
        logger.info("createNewFile");
        throw new UnsupportedOperationException("createNewFile");
    }

    // TODO
    @Override
    public boolean delete() {
        logger.info("delete");
        throw new UnsupportedOperationException("delete");
    }

    // TODO
    @Override
    public void deleteOnExit() {
        logger.info("deleteOnExit");
        throw new UnsupportedOperationException("deleteOnExit");
    }

    // TODO
    @Override
    public String[] list() {
        logger.info("list");
        throw new UnsupportedOperationException("list");
    }

    // TODO
    @Override
    public String[] list(FilenameFilter filter) {
        logger.info("list");
        throw new UnsupportedOperationException("list");
    }

    // TODO
    @Override
    public File[] listFiles() {
        logger.info("listFiles");
        throw new UnsupportedOperationException("listFiles");
    }

    // TODO
    @Override
    public File[] listFiles(FilenameFilter filter) {
        logger.info("listFiles");
        throw new UnsupportedOperationException("listFiles");
    }

    // TODO
    @Override
    public File[] listFiles(FileFilter filter) {
        logger.info("listFiles");
        throw new UnsupportedOperationException("listFiles");
    }

    // TODO
    @Override
    public boolean mkdir() {
        logger.info("mkdir");
        throw new UnsupportedOperationException("mkdir");
    }

    // TODO
    @Override
    public boolean mkdirs() {
        logger.info("mkdirs");
        throw new UnsupportedOperationException("mkdirs");
    }

    // TODO
    @Override
    public boolean renameTo(File dest) {
        logger.info("renameTo");
        throw new UnsupportedOperationException("renameTo");
    }

    // TODO
    @Override
    public boolean setLastModified(long time) {
        logger.info("setLastModified");
        throw new UnsupportedOperationException("setLastModified");
    }

    // TODO
    @Override
    public boolean setReadOnly() {
        logger.info("setReadOnly");
        throw new UnsupportedOperationException("setReadOnly");
    }

    // TODO
    @Override
    public boolean setWritable(boolean writable, boolean ownerOnly) {
        logger.info("setWritable");
        throw new UnsupportedOperationException("setWritable");
    }

    // TODO
    @Override
    public boolean setWritable(boolean writable) {
        logger.info("setWritable");
        throw new UnsupportedOperationException("setWritable");
    }

    // TODO
    @Override
    public boolean setReadable(boolean readable, boolean ownerOnly) {
        logger.info("setReadable");
        throw new UnsupportedOperationException("setReadable");
    }

    // TODO
    @Override
    public boolean setReadable(boolean readable) {
        logger.info("setReadable");
        throw new UnsupportedOperationException("setReadable");
    }

    // TODO
    @Override
    public boolean setExecutable(boolean executable, boolean ownerOnly) {
        logger.info("setExecutable");
        throw new UnsupportedOperationException("setExecutable");
    }

    // TODO
    @Override
    public boolean setExecutable(boolean executable) {
        logger.info("setExecutable");
        throw new UnsupportedOperationException("setExecutable");
    }

    // TODO
    @Override
    public boolean canExecute() {
        logger.info("canExecute");
        return false;
    }

    @Override
    public long getTotalSpace() {
        logger.info("getting total space ({})", path);
        return Long.MAX_VALUE;
    }

    @Override
    public long getFreeSpace() {
        logger.info("getting free space ({})", path);
        return Long.MAX_VALUE;
    }

    @Override
    public long getUsableSpace() {
        logger.info("getting usable space ({})", path);
        return Long.MAX_VALUE;
    }

    // TODO
    @Override
    public int compareTo(final File pathname) {
        logger.info("compareTo");
        return path.compareTo(pathname.getPath());
    }

    // TODO
    @Override
    public boolean equals(final Object obj) {
        logger.info("equals");
        if ((obj != null) && (obj instanceof JcrFile)) {
            return compareTo((JcrFile) obj) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        logger.info("hash code ({})", path);
        return path.hashCode() ^ 1234321;
    }

    @Override
    public String toString() {
        logger.info("to string ({})", path);
        return path;
    }

    @Override
    @NotNull
    public Path toPath() {
        logger.info("to path ({})", path);
        return new JcrPath(fileSystem, path);
    }

    Node getNode() throws RepositoryException {
        return fileSystem.getSession().getNode(path);
    }

}
