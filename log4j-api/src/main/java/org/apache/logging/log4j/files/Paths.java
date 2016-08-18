package org.apache.logging.log4j.files;

import java.io.File;
import java.net.URI;

/**
 * Created by Yo on 17/08/2016.
 */
public class Paths {
    public static Path get(String replace) {
        return new Path(new File(replace));
    }

    public static Path get(URI replace) {
        return new Path(new File(replace));
    }
}
