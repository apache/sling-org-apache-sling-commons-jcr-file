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
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.DELETE_ON_CLOSE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * https://wiki.apache.org/jackrabbit/JCR%20Binary%20Usecase
 */
public class JcrFileChannel extends FileChannel {

    private final Node node;

    private final Path file = Files.createTempFile(null, null);

    private final FileChannel fileChannel;

    private final Logger logger = LoggerFactory.getLogger(JcrFileChannel.class);

    JcrFileChannel(final Node node) throws Exception {
        logger.info("JcrFileChannel: {}", node.getPath());
        this.node = node;
        fileChannel = init();
    }

    private Binary getBinary() throws RepositoryException {
        return node.getNode("jcr:content").getProperty("jcr:data").getBinary();
    }

    private void setBinary(final Binary binary) throws RepositoryException {
        node.getNode("jcr:content").setProperty("jcr:data", binary);
    }

    private FileChannel init() throws IOException {
        Binary binary = null;
        try {
            binary = getBinary();
        } catch (Exception e) {
            logger.error("reading binary failed: {}", e.getMessage(), e);
        }
        if (binary != null) {
            try (final InputStream inputStream = binary.getStream()) {
                Files.copy(inputStream, file, REPLACE_EXISTING);
            } catch (Exception e) {
                logger.error("copying binary to file failed: {}", e.getMessage(), e);
            }
        }
        return FileChannel.open(file, READ, WRITE);
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        logger.info("read");
        return fileChannel.read(dst);
    }

    @Override
    public long read(final ByteBuffer[] dsts, final int offset, final int length) throws IOException {
        logger.info("read {} {}", offset, length);
        return fileChannel.read(dsts, offset, length);
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        logger.info("write");
        return fileChannel.write(src);
    }

    @Override
    public long write(final ByteBuffer[] srcs, final int offset, final int length) throws IOException {
        logger.info("write {} {}", offset, length);
        return fileChannel.write(srcs, offset, length);
    }

    @Override
    public long position() throws IOException {
        logger.info("position");
        return fileChannel.position();
    }

    @Override
    public FileChannel position(final long newPosition) throws IOException {
        logger.info("setting position ({}) to new position {}", fileChannel.position(), newPosition);
        fileChannel.position(newPosition);
        return this;
    }

    // TODO
    @Override
    public long size() throws IOException {
        logger.info("size");
        try {
            return getBinary().getSize();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IOException(e);
        }
    }

    @Override
    public FileChannel truncate(final long size) throws IOException {
        logger.info("truncate", size);
        fileChannel.truncate(size);
        return this;
    }

    // TODO
    @Override
    public void force(final boolean metaData) throws IOException {
        logger.info("force {}", metaData);
    }

    @Override
    public long transferTo(final long position, final long count, final WritableByteChannel target) throws IOException {
        logger.info("transferTo {} {}", position, count);
        return fileChannel.transferTo(position, count, target);
    }

    @Override
    public long transferFrom(final ReadableByteChannel src, final long position, final long count) throws IOException {
        logger.info("transferFrom {} {}", position, count);
        return fileChannel.transferFrom(src, position, count);
    }

    @Override
    public int read(final ByteBuffer dst, final long position) throws IOException {
        logger.info("read {}", position);
        return fileChannel.read(dst, position);
    }

    @Override
    public int write(final ByteBuffer src, final long position) throws IOException {
        logger.info("write {}", position);
        return fileChannel.write(src, position);
    }

    // TODO
    @Override
    public MappedByteBuffer map(MapMode mode, long position, long size) throws IOException {
        logger.info("map {} {}", position, size);
        return null;
    }

    // TODO
    @Override
    public FileLock lock(long position, long size, boolean shared) throws IOException {
        logger.info("lock {} {} {}", position, size, shared);
        return null;
    }

    // TODO
    @Override
    public FileLock tryLock(long position, long size, boolean shared) throws IOException {
        logger.info("tryLock {} {} {}", position, size, shared);
        return null;
    }

    @Override
    protected void implCloseChannel() throws IOException {
        logger.info("implCloseChannel");
        fileChannel.close();
        try (final InputStream inputStream = Files.newInputStream(file, READ, DELETE_ON_CLOSE)) {
            final Session session = node.getSession();
            final ValueFactory valueFactory = session.getValueFactory();
            final Binary binary = valueFactory.createBinary(inputStream);
            setBinary(binary);
            session.save();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IOException(e);
        }
    }

}
