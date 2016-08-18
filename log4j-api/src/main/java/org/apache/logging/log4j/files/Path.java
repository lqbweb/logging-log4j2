package org.apache.logging.log4j.files;

import java.io.File;

/**
 * Created by Yo on 17/08/2016.
 */
public class Path implements Comparable<Path> {
    private final File file;
    private final File parent;

    public Path(File parent, File child) {
        this.file = child;
        this.parent = parent;
    }

    public Path(File file) {
        this.file = file; this.parent=file.getParentFile();
    }

    public File toFile() {
        return this.file;
    }

    @Override
    public int compareTo(Path other) {
        return this.toFile().compareTo(other.toFile());
    }
}
