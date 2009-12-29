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
package com.mycila.log.jdk.handler;

import static com.mycila.log.jdk.handler.Utils.*;
import com.mycila.log.jdk.hook.InvocationHandler;

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
public class MycilaHandler<T extends Handler> extends Handler {

    private T handler;
    private Level maxLevel;
    private InvocationHandler<T> hook;

    public MycilaHandler() {
        setHook(Utils.<T>hook(getClass()));
    }

    public MycilaHandler(T handler) {
        setHook(Utils.<T>hook(getClass()));
        setHandler(handler);
    }

    public Level getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(Level maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setHook(InvocationHandler<T> hook) {
        this.hook = hook;
    }

    public Level getThresold() {
        return maxLevel;
    }

    public InvocationHandler<T> getHook() {
        return hook;
    }

    public void setHandler(T handler) {
        this.handler = handler;
        setLevel(level(getClass(), Level.INFO));
        setMaxLevel(maxLevel(getClass(), Level.SEVERE));
        setFilter(filter(getClass()));
        setFormatter(formatter(getClass()));
        try {
            setEncoding(encoding(getClass()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public T getHandler() {
        return handler;
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        int level = record.getLevel().intValue();
        return level == Level.ALL.intValue() || getHook().isLoggable(getHandler(), record) && level <= getThresold().intValue();
    }

    public void publish(LogRecord record) {
        if(isLoggable(record)) {
            getHook().publish(getHandler(), record);
        }
    }

    public void flush() {
        getHook().flush(getHandler());
    }

    public void close() throws SecurityException {
        getHook().close(getHandler());
    }

    @Override
    public void setFormatter(Formatter newFormatter) throws SecurityException {
        getHook().setFormatter(getHandler(), newFormatter);
    }

    @Override
    public Formatter getFormatter() {
        return getHook().getFormatter(getHandler());
    }

    @Override
    public String getEncoding() {
        return getHook().getEncoding(getHandler());
    }

    @Override
    public void setFilter(Filter newFilter) throws SecurityException {
        getHook().setFilter(getHandler(), newFilter);
    }

    @Override
    public Filter getFilter() {
        return getHook().getFilter(getHandler());
    }

    @Override
    public void setErrorManager(ErrorManager em) {
        getHook().setErrorManager(getHandler(), em);
    }

    @Override
    public ErrorManager getErrorManager() {
        return getHook().getErrorManager(getHandler());
    }

    @Override
    public void setLevel(Level newLevel) throws SecurityException {
        getHook().setLevel(getHandler(), newLevel);
    }

    @Override
    public Level getLevel() {
        return getHook().getLevel(getHandler());
    }

}
