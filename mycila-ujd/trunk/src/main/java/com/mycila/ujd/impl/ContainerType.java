package com.mycila.ujd.impl;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public enum ContainerType {
    DIR, JAR_LOCAL, JAR_REMOTE, UNKNOWN;

    public static ContainerType from(URL url) {
        try {
            if (new File(url.toURI()).isDirectory()) return DIR;
        } catch (Exception ignored) {
        }
        if ("file".equals(url.getProtocol()))
            return JAR_LOCAL;
        try {
            if (new URL("jar", "", url + "!/").openConnection() instanceof JarURLConnection)
                return JAR_REMOTE;
        } catch (IOException ignored) {
        }
        return UNKNOWN;
    }
}
