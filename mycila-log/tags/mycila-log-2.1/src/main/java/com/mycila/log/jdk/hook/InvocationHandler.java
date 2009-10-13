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
public interface InvocationHandler<T extends Handler> {

    void publish(T handler, LogRecord record);

    void flush(T handler);

    void close(T handler) throws SecurityException;

    void setFormatter(T handler, Formatter newFormatter) throws SecurityException;

    Formatter getFormatter(T handler);

    void setEncoding(T handler, String encoding) throws SecurityException, UnsupportedEncodingException;

    String getEncoding(T handler);

    void setFilter(T handler, Filter newFilter) throws SecurityException;

    Filter getFilter(T handler);

    void setErrorManager(T handler, ErrorManager em);

    ErrorManager getErrorManager(T handler);

    void setLevel(T handler, Level newLevel) throws SecurityException;

    Level getLevel(T handler);

    boolean isLoggable(T handler, LogRecord record);
}
