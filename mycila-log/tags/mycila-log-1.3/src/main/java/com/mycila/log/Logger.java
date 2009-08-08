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
package com.mycila.log;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Logger {

    boolean canTrace();

    void trace(String message, Object... args);

    void trace(Throwable throwable, String message, Object... args);

    boolean canDebug();

    void debug(String message, Object... args);

    void debug(Throwable throwable, String message, Object... args);

    boolean canInfo();

    void info(String message, Object... args);

    void info(Throwable throwable, String message, Object... args);

    boolean canWarn();

    void warn(String message, Object... args);

    void warn(Throwable throwable, String message, Object... args);

    boolean canError();

    void error(String message, Object... args);

    void error(Throwable throwable, String message, Object... args);

    boolean canLog(Level level);

    void log(Level level, String message, Object... args);

    void log(Level level, Throwable throwable, String message, Object... args);
}
