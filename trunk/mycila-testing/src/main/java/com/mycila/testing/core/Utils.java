package com.mycila.testing.core;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Utils {

    static Properties load(URL url) throws IOException {
        InputStream is = null;
        try {
            is = new BufferedInputStream(url.openStream());
            Properties p = new Properties();
            p.load(is);
            return p;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
