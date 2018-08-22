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

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class JcrFileAttributes implements BasicFileAttributes {

    private final FileTime lastModifiedTime;

    private final FileTime lastAccessTime;

    private final FileTime creationTime;

    private final boolean isRegularFile;

    private final boolean isDirectory;

    private final boolean isSymbolicLink;

    private final boolean isOther;

    private final long size;

    JcrFileAttributes(final FileTime lastModifiedTime, final FileTime lastAccessTime, final FileTime creationTime, final boolean isRegularFile, final boolean isDirectory, final boolean isSymbolicLink, final boolean isOther, final long size) {
        this.lastModifiedTime = lastModifiedTime;
        this.lastAccessTime = lastAccessTime;
        this.creationTime = creationTime;
        this.isRegularFile = isRegularFile;
        this.isDirectory = isDirectory;
        this.isSymbolicLink = isSymbolicLink;
        this.isOther = isOther;
        this.size = size;
    }

    @Override
    public FileTime lastModifiedTime() {
        return lastModifiedTime;
    }

    @Override
    public FileTime lastAccessTime() {
        return lastAccessTime;
    }

    @Override
    public FileTime creationTime() {
        return creationTime;
    }

    @Override
    public boolean isRegularFile() {
        return isRegularFile;
    }

    @Override
    public boolean isDirectory() {
        return isDirectory;
    }

    @Override
    public boolean isSymbolicLink() {
        return isSymbolicLink;
    }

    @Override
    public boolean isOther() {
        return isOther;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public Object fileKey() {
        return null;
    }

}
