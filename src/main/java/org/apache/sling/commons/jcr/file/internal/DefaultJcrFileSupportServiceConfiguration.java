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

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
    name = "Apache Sling Commons JCR File Support Service",
    description = "Provides support for JCR File tasks"
)
@interface DefaultJcrFileSupportServiceConfiguration {

    @AttributeDefinition(
        name = "service ranking",
        description = "service ranking"
    )
    int service_ranking() default 0;

    @AttributeDefinition(
        name = "file node type",
        description = "node type used for new files"
    )
    String file_node_type() default "nt:file";

    @AttributeDefinition(
        name = "directory node typ",
        description = "node type used for new directories"
    )
    String directory_node_type() default "nt:folder";

    @AttributeDefinition(
        name = "file node types",
        description = "file node types"
    )
    String[] file_node_types() default {"nt:file"};

    @AttributeDefinition(
        name = "directory node types",
        description = "directory node types"
    )
    String[] directory_node_types() default {"rep:root", "nt:folder", "sling:Folder"};

}
