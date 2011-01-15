/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
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

package com.mycila.jdbc.tx.sql;

import com.mycila.jdbc.UnitOfWork;

import javax.inject.Singleton;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public final class JdbcUnitOfWork implements UnitOfWork {

    private static final Logger LOGGER = Logger.getLogger(JdbcUnitOfWork.class.getName());

    @Override
    public void begin() {
        // be sure to start with a brand new thread local map
        ConnectionHolder.clean();
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("Beginning Unit of Work");
    }

    @Override
    public void end() {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("Ending Unit of Work");
        for (ConnectionHolder holder : ConnectionHolder.listAll())
            holder.close();
        // be sure to clean the thread local map
        ConnectionHolder.clean();
    }
}
