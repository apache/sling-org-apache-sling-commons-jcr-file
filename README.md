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
