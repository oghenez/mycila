package com.mycila.testing.plugin.db.api;

import java.io.File;
import java.net.URL;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Batch {
    /**
     * Add some sql commands in the batch
     *
     * @param sqlLines script path
     * @return this
     */
    Batch add(String sqlLines);

    /**
     * Add a script to the batch
     *
     * @param scriptPathInClasspath script location in classpath
     * @return this
     */
    Batch addScript(String scriptPathInClasspath);

    /**
     * Add a script to the batch
     *
     * @param script script location
     * @return this
     */
    Batch addScript(URL script);

    /**
     * Add a script to the batch
     *
     * @param script script file
     * @return this
     */
    Batch addScript(File script);

    /**
     * Run the batch
     *
     * @return this
     */
    Db commit();

    Db rollback();
}
