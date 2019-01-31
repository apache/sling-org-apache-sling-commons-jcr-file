[<img src="https://sling.apache.org/res/logos/sling.png"/>](https://sling.apache.org)

 [![Build Status](https://builds.apache.org/buildStatus/icon?job=Sling/sling-org-apache-sling-commons-jcr-file/master)](https://builds.apache.org/job/Sling/job/sling-org-apache-sling-commons-jcr-file/job/master) [![Test Status](https://img.shields.io/jenkins/t/https/builds.apache.org/job/Sling/job/sling-org-apache-sling-commons-jcr-file/job/master.svg)](https://builds.apache.org/job/Sling/job/sling-org-apache-sling-commons-jcr-file/job/master/test_results_analyzer/) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

# Apache Sling Commons JCR File

This module is part of the [Apache Sling](https://sling.apache.org) project.

This module provides a basic NIO.2 file system implementation for JCR.

## JCR File System Properties

| Property Name (of Type `java.lang.String`) | Allowed Values | Property Description |
| ---- | ---- | ---- |
| `javax.jcr.Session` | a valid (_live_) `Session` | Session which is used to access (read/write) the JCR |

## Limitations

* Getting an existing `FileSystem` or `Path` via `URI` (`FileSystemProvider#getFileSystem(URI):FileSystem`) is not supported due to underlying JCR `Session`s from environments (`env`).
* Getting total space, free space and usable space always returns `Long.MAX_VALUE`

## Notes

* Most classes in this module log errors **and** throw exceptions afterwards due to the sparingly used logging in MINA's SFTP subsystem.
