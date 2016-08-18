package org.apache.logging.log4j.files;

import java.io.IOException;

/**
 * Created by Yo on 17/08/2016.
 */
public interface FileVisitor<T> {

    FileVisitResult preVisitDirectory(T dir, BasicFileAttributes attrs)
            throws IOException;

    FileVisitResult visitFile(T file, BasicFileAttributes attrs)
            throws IOException;

    FileVisitResult visitFileFailed(T file, IOException exc)
            throws IOException;

    FileVisitResult postVisitDirectory(T dir, IOException exc)
            throws IOException;
}