package com.mycila.testing.plugin.db.api;

import java.io.File;
import java.net.URL;
import java.sql.Connection;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Db {

    /**
     * @return The connection for this DB
     */
    Connection getConnection();

    /**
     * Close the connection. This is not mandatory: the plugin will do it for you.
     * But you can close the connection if you need.
     *
     * @return this
     */
    Db close();

    /**
     * Run a query
     *
     * @param query SQL query to run
     * @return results as a table representation
     */
    Statement prepare(String query);

    /**
     * Run a script in the classpath
     *
     * @param scriptPathInClasspath script path
     * @return this
     */
    Db runScript(String scriptPathInClasspath);

    /**
     * Run a script from any location
     *
     * @param script script location
     * @return this
     */
    Db runScript(URL script);

    /**
     * Run a script from a file
     *
     * @param script script file
     * @return this
     */
    Db runScript(File script);

    /**
     * Start a new batch
     *
     * @return a Batch instance
     */
    Batch newBatch();

}
