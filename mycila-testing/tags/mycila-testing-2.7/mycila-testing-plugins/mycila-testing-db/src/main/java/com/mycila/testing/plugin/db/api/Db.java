/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
