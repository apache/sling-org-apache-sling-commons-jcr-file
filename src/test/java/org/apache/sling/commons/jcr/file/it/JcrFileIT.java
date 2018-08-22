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

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public final class JcrFileIT extends JcrFileTestSupport {

    private FileSystem fileSystem;

    // /content/starter
    private File starter;

    // /content/starter/sling-logo.png
    private File sling_logo_png;

    @Before
    public void setUp() throws Exception {
        fileSystem = fileSystem("admin", "/");
        starter = fileSystem.getPath("/content/starter").toFile();
        sling_logo_png = fileSystem.getPath("/content/starter/sling-logo.png").toFile();
    }

    @After
    public void tearDown() throws IOException {
        fileSystem.close();
    }

    @Test
    public void test_sling_logo_png_getName() {
        assertThat(sling_logo_png.getName(), is("sling-logo.png"));
    }

    @Test
    public void test_sling_logo_png_isFile() {
        assertThat(sling_logo_png.isFile(), is(true));
    }

    @Test
    public void test_sling_logo_png_isDirecory() {
        assertThat(sling_logo_png.isDirectory(), is(false));
    }

    @Test
    public void test_sling_logo_png_getParentFile() {
        assertThat(sling_logo_png.getParentFile(), is(starter));
    }

}
