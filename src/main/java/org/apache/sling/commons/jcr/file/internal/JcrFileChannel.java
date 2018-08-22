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
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https://wiki.apache.org/jackrabbit/JCR%20Binary%20Usecase
 */
public class JcrFileChannel extends FileChannel {

    private long position;

    private final Node node;

    private final Logger logger = LoggerFactory.getLogger(JcrFileChannel.class);

    JcrFileChannel(final Node node) throws RepositoryException {
        logger.info("JcrFileChannel: {}", node.getPath());
        this.node = node;
    }

    private Binary binary() throws RepositoryException {
        return node.getNode("jcr:content").getProperty("jcr:data").getBinary();
    }

    // TODO
    @Override
    public int read(ByteBuffer dst) throws IOException {
        logger.info("read");
        /*
        try {
            int read = binary().read(dst.array(), position);
            position = position + read;
            logger.info("read {} bytes into array of length {}, new position is {}", read, dst.array().length, position);
            return read;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IOException(e);
        }
        */
        return 0;
    }

    // TODO
    @Override
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        logger.info("read {} {}", offset, length);
        return 0;
    }

    // TODO
    @Override
    public int write(ByteBuffer src) throws IOException {
        logger.info("write");
        return 0;
    }

    // TODO
    @Override
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        logger.info("write {} {}", offset, length);
        return 0;
    }

    @Override
    public long position() throws IOException {
        logger.info("position: {}", position);
        return position;
    }

    // TODO
    @Override
    public FileChannel position(long newPosition) throws IOException {
        logger.info("setting position ({}) to new position {}", position, newPosition);
        this.position = newPosition;
        return this;
    }

    // TODO
    @Override
    public long size() throws IOException {
        logger.info("size");
        try {
            return binary().getSize();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IOException(e);
        }
    }

    // TODO
    @Override
    public FileChannel truncate(long size) throws IOException {
        logger.info("truncate", size);
        return this;
    }

    // TODO
    @Override
    public void force(boolean metaData) throws IOException {
        logger.info("force {}", metaData);
    }

    // TODO
    @Override
    public long transferTo(long position, long count, WritableByteChannel target) throws IOException {
        logger.info("transferTo {} {}", position, count);
        return 0;
    }

    // TODO
    @Override
    public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException {
        logger.info("transferFrom {} {}", position, count);
        return 0;
    }

    // TODO
    @Override
    public int read(ByteBuffer dst, long position) throws IOException {
        logger.info("read {}", position);
        return 0;
    }

    // TODO
    @Override
    public int write(ByteBuffer src, long position) throws IOException {
        logger.info("write {}", position);
        return 0;
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

    // TODO
    @Override
    protected void implCloseChannel() throws IOException {
        logger.info("implCloseChannel");
    }

}
