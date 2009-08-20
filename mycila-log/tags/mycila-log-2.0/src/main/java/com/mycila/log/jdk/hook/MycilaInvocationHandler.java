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
package com.mycila.log.jdk.hook;

import java.io.UnsupportedEncodingException;
import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MycilaInvocationHandler<T extends Handler> implements InvocationHandler<T> {
    public void publish(T handler, LogRecord record) {
        handler.publish(record);
    }

    public void flush(T handler) {
        handler.flush();
    }

    public void close(T handler) throws SecurityException {
        handler.close();
    }

    public void setFormatter(T handler, Formatter newFormatter) throws SecurityException {
        handler.setFormatter(newFormatter);
    }

    public Formatter getFormatter(T handler) {
        return handler.getFormatter();
    }

    public void setEncoding(T handler, String encoding) throws SecurityException, UnsupportedEncodingException {
        handler.setEncoding(encoding);
    }

    public String getEncoding(T handler) {
        return handler.getEncoding();
    }

    public void setFilter(T handler, Filter newFilter) throws SecurityException {
        handler.setFilter(newFilter);
    }

    public Filter getFilter(T handler) {
        return handler.getFilter();
    }

    public void setErrorManager(T handler, ErrorManager em) {
        handler.setErrorManager(em);
    }

    public ErrorManager getErrorManager(T handler) {
        return handler.getErrorManager();
    }

    public void setLevel(T handler, Level newLevel) throws SecurityException {
        handler.setLevel(newLevel);
    }

    public Level getLevel(T handler) {
        return handler.getLevel();
    }

    public boolean isLoggable(T handler, LogRecord record) {
        return handler.isLoggable(record);
    }
}
