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

import static com.mycila.log.Level.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class AbstractLogger implements Logger {
    public boolean canTrace() {
        return canLog(TRACE);
    }

    @Override
    public final void trace(Object message, Object... args) {
        if(canLog(TRACE)) doLog(TRACE, null, message, args);
    }

    @Override
    public final void trace(Throwable throwable, Object message, Object... args) {
        if(canLog(TRACE)) doLog(TRACE, throwable, message, args);
    }

    @Override
    public final boolean canDebug() {
        return canLog(DEBUG);
    }

    @Override
    public final void debug(Object message, Object... args) {
        if(canLog(DEBUG)) doLog(DEBUG, null, message, args);
    }

    @Override
    public final void debug(Throwable throwable, Object message, Object... args) {
        if(canLog(DEBUG)) doLog(DEBUG, throwable, message, args);
    }

    @Override
    public final boolean canInfo() {
        return canLog(INFO);
    }

    @Override
    public final void info(Object message, Object... args) {
        if(canLog(INFO)) doLog(INFO, null, message, args);
    }

    @Override
    public final void info(Throwable throwable, Object message, Object... args) {
        if(canLog(INFO)) doLog(INFO, throwable, message, args);
    }

    @Override
    public final boolean canWarn() {
        return canLog(WARN);
    }

    @Override
    public final void warn(Object message, Object... args) {
        if(canLog(WARN)) doLog(WARN, null, message, args);
    }

    @Override
    public final void warn(Throwable throwable, Object message, Object... args) {
        if(canLog(WARN)) doLog(WARN, throwable, message, args);
    }

    @Override
    public final boolean canError() {
        return canLog(ERROR);
    }

    @Override
    public final void error(Object message, Object... args) {
        if(canLog(ERROR)) doLog(ERROR, null, message, args);
    }

    @Override
    public final void error(Throwable throwable, Object message, Object... args) {
        if(canLog(ERROR)) doLog(ERROR, throwable, message, args);
    }

    @Override
    public final void log(Level level, Object message, Object... args) {
        if(canLog(level)) doLog(level, null, message, args);
    }

    @Override
    public final void log(Level level, Throwable throwable, Object message, Object... args) {
        if (canLog(level)) doLog(level, throwable, message, args);
    }

    @Override
    public void debug(Object message) {
        if(canLog(DEBUG)) doLog(DEBUG, null, message);
    }

    @Override
    public void debug(Throwable throwable, Object message) {
        if(canLog(DEBUG)) doLog(DEBUG, throwable, message);
    }

    @Override
    public void error(Object message) {
        if(canLog(ERROR)) doLog(ERROR, null, message);
    }

    @Override
    public void error(Throwable throwable, Object message) {
        if(canLog(ERROR)) doLog(ERROR, throwable, message);
    }

    @Override
    public void info(Object message) {
        if(canLog(INFO)) doLog(INFO, null, message);
    }

    @Override
    public void info(Throwable throwable, Object message) {
        if(canLog(INFO)) doLog(INFO, throwable, message);
    }

    @Override
    public void log(Level level, Object message) {
        if(canLog(level)) doLog(level, null, message);
    }

    @Override
    public void log(Level level, Throwable throwable, Object message) {
        if(canLog(level)) doLog(level, throwable, message);
    }

    @Override
    public void trace(Object message) {
        if(canLog(TRACE)) doLog(TRACE, null, message);
    }

    @Override
    public void trace(Throwable throwable, Object message) {
        if(canLog(TRACE)) doLog(TRACE, throwable, message);
    }

    @Override
    public void warn(Object message) {
        if(canLog(WARN)) doLog(WARN, null, message);
    }

    @Override
    public void warn(Throwable throwable, Object message) {
        if(canLog(WARN)) doLog(WARN, throwable, message);
    }

    protected abstract void doLog(Level level, Throwable throwable, Object message, Object... args);

}
