/**
 * Copyright (C) 2011 Mycila <mathieu.carbou@gmail.com>
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

package com.mycila.jdk.logging;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Use: -Djava.util.logging.manager=com.mycila.jdk.logging.ContextualLogManager
 *
 * @author Mathieu Carbou
 */
public final class ContextualLogManager extends LogManager {

    private final Map<ClassLoader, LogManager> logManagers = new IdentityHashMap<ClassLoader, LogManager>();

    private static AtomicReference<ContextualLogManager> INSTANCE = new AtomicReference<ContextualLogManager>();
    private static final Field loggerManagerField;
    private static final Method initializeGlobalHandlers;
    private static final Method readPrimordialConfiguration;

    static {
        try {
            readPrimordialConfiguration = LogManager.class.getDeclaredMethod("readPrimordialConfiguration");
            readPrimordialConfiguration.setAccessible(true);
            initializeGlobalHandlers = LogManager.class.getDeclaredMethod("initializeGlobalHandlers");
            initializeGlobalHandlers.setAccessible(true);
            loggerManagerField = Logger.class.getDeclaredField("manager");
            loggerManagerField.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public ContextualLogManager() {
        if (!INSTANCE.compareAndSet(null, this)) {
            throw new IllegalStateException("A ContextualLogManager has already been initialized !");
        }
        System.out.println("=== Initializing " + ContextualLogManager.class.getSimpleName() + " ===");
        /*try {
            readConfiguration();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }*/
    }

    public static ContextualLogManager get() {
        if (INSTANCE == null)
            new ContextualLogManager();
        return INSTANCE.get();
    }

    public LogManager getContextualLogManager() {
        ClassLoader webappClassLoader = Thread.currentThread().getContextClassLoader();
        return logManagers.get(webappClassLoader);
    }

    public void unregisterWebapp(ClassLoader webappClassLoader) {
        LogManager manager = logManagers.remove(webappClassLoader);
        if (manager != null) {
            manager.reset();
        }
    }

    public void registerWebapp(ClassLoader webappClassLoader) {
        final String name = webappClassLoader.toString();
        final LogManager perWebapp = new LogManager() {
            @Override
            public String toString() {
                return "LogManager for Webapp with classloader " + name;
            }
        };
        System.out.println("=== Creating " + perWebapp + " ===");
        try {
            Logger root = new Logger("", null) {
                {
                    try {
                        loggerManagerField.set(this, perWebapp);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                    initializeGlobalHandlers.invoke(perWebapp);
                    setLevel(Level.INFO);
                }

                @Override
                public String toString() {
                    return "RootLogger for " + perWebapp;
                }
            };
            Field rootLoggerField = LogManager.class.getDeclaredField("rootLogger");
            rootLoggerField.setAccessible(true);
            rootLoggerField.set(perWebapp, root);
            perWebapp.addLogger(root);
            readPrimordialConfiguration.invoke(perWebapp);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        logManagers.put(webappClassLoader, perWebapp);
    }

    // DELEGATE METHODS

    @Override
    public boolean addLogger(Logger logger) {
        LogManager logManager = getContextualLogManager();
        if (logManager == null) {
            return super.addLogger(logger);
        } else {
            try {
                loggerManagerField.set(logger, logManager);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            return logManager.addLogger(logger);
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) throws SecurityException {
        LogManager logManager = getContextualLogManager();
        if (logManager == null) {
            super.addPropertyChangeListener(l);
        } else {
            logManager.addPropertyChangeListener(l);
        }
    }

    @Override
    public void checkAccess() throws SecurityException {
        LogManager logManager = getContextualLogManager();
        if (logManager == null) {
            super.checkAccess();
        } else {
            logManager.checkAccess();
        }
    }

    @Override
    public Logger getLogger(String name) {
        LogManager logManager = getContextualLogManager();
        if (logManager == null) {
            return super.getLogger(name);
        } else {
            return logManager.getLogger(name);
        }
    }

    @Override
    public Enumeration<String> getLoggerNames() {
        LogManager logManager = getContextualLogManager();
        if (logManager == null) {
            return super.getLoggerNames();
        } else {
            return logManager.getLoggerNames();
        }
    }

    @Override
    public String getProperty(String name) {
        LogManager logManager = getContextualLogManager();
        if (logManager == null) {
            return super.getProperty(name);
        } else {
            return logManager.getProperty(name);
        }
    }

    @Override
    public void readConfiguration() throws IOException, SecurityException {
        LogManager logManager = getContextualLogManager();
        if (logManager == null) {
            super.readConfiguration();
        } else {
            logManager.readConfiguration();
        }
    }

    @Override
    public void readConfiguration(InputStream ins) throws IOException, SecurityException {
        LogManager logManager = getContextualLogManager();
        if (logManager == null) {
            super.readConfiguration(ins);
        } else {
            logManager.readConfiguration(ins);
        }
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) throws SecurityException {
        LogManager logManager = getContextualLogManager();
        if (logManager == null) {
            super.removePropertyChangeListener(l);
        } else {
            logManager.removePropertyChangeListener(l);
        }
    }

    @Override
    public void reset() throws SecurityException {
        LogManager logManager = getContextualLogManager();
        if (logManager == null) {
            super.reset();
        } else {
            logManager.reset();
        }
    }

}
