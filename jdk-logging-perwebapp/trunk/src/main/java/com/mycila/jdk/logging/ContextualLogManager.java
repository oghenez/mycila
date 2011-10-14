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
    private static final Field Logger_manager;
    private static final Field LogManager_rootLogger;
    private static final Field LogManager_manager;
    private static final Method LogManager_initializeGlobalHandlers;
    private static final Method LogManager_readPrimordialConfiguration;

    static {
        try {
            LogManager_readPrimordialConfiguration = LogManager.class.getDeclaredMethod("readPrimordialConfiguration");
            LogManager_readPrimordialConfiguration.setAccessible(true);

            LogManager_initializeGlobalHandlers = LogManager.class.getDeclaredMethod("initializeGlobalHandlers");
            LogManager_initializeGlobalHandlers.setAccessible(true);

            LogManager_rootLogger = LogManager.class.getDeclaredField("rootLogger");
            LogManager_rootLogger.setAccessible(true);

            LogManager_manager = LogManager.class.getDeclaredField("manager");
            LogManager_manager.setAccessible(true);

            Logger_manager = Logger.class.getDeclaredField("manager");
            Logger_manager.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public ContextualLogManager() {
        if (!INSTANCE.compareAndSet(null, this)) {
            throw new IllegalStateException("A ContextualLogManager has already been initialized !");
        }
        LogManager.getLogManager().getLogger("").info("=== Initializing " + ContextualLogManager.class.getSimpleName() + " ===");
    }

    public static boolean isAvailable() {
        return INSTANCE.get() != null;
    }

    public static ContextualLogManager get() {
        ContextualLogManager manager = INSTANCE.get();
        if (manager == null)
            throw new IllegalStateException("ContextualLogManager has not been initialized by JDK Logging system");
        return manager;
    }

    public static ContextualLogManager replaceLogManager() {
        if (!isAvailable()) {
            final ContextualLogManager globalManager = new ContextualLogManager();
            try {
                LogManager_manager.set(null, globalManager);
                Logger root = new Logger("", null) {
                    {
                        try {
                            Logger_manager.set(this, globalManager);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e.getMessage(), e);
                        }
                        setLevel(Level.INFO);
                    }

                    @Override
                    public String toString() {
                        return "RootLogger for " + globalManager;
                    }
                };
                LogManager_rootLogger.set(globalManager, root);
                globalManager.addLogger(root);
                LogManager_readPrimordialConfiguration.invoke(globalManager);
                LogManager_initializeGlobalHandlers.invoke(globalManager);
            } catch (Throwable e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return get();
    }

    public LogManager getContextualLogManager() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        return logManagers.get(contextClassLoader);
    }

    public void unregisterClassLoader(ClassLoader contextClassLoader) {
        LogManager manager = logManagers.remove(contextClassLoader);
        if (manager != null) {
            manager.reset();
        }
    }

    public void registerClassLoader(ClassLoader contextClassLoader) {
        final String name = contextClassLoader.toString();
        final LogManager perLoader = new LogManager() {
            @Override
            public String toString() {
                return "LogManager for classloader " + name;
            }
        };
        LogManager.getLogManager().getLogger("").info("=== Creating " + perLoader + " ===");
        try {
            Logger root = new Logger("", null) {
                {
                    try {
                        Logger_manager.set(this, perLoader);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                    setLevel(Level.INFO);
                }

                @Override
                public String toString() {
                    return "RootLogger for " + perLoader;
                }
            };
            LogManager_rootLogger.set(perLoader, root);
            perLoader.addLogger(root);
            LogManager_readPrimordialConfiguration.invoke(perLoader);
            LogManager_initializeGlobalHandlers.invoke(perLoader);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        logManagers.put(contextClassLoader, perLoader);
    }

    // DELEGATE METHODS

    @Override
    public boolean addLogger(Logger logger) {
        LogManager logManager = getContextualLogManager();
        if (logManager == null) {
            return super.addLogger(logger);
        } else {
            try {
                Logger_manager.set(logger, logManager);
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
