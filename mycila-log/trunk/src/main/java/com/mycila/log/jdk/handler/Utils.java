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

import com.mycila.log.jdk.hook.InvocationHandler;
import com.mycila.log.jdk.hook.MycilaInvocationHandler;

import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.SimpleFormatter;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Utils {
    private Utils() {
    }

    static Level maxLevel(Class c, Level def) {
        return Level.parse(property(c, "max", def.getName()));
    }

    static Level level(Class c, Level def) {
        return Level.parse(property(c, "level", def.getName()));
    }

    @SuppressWarnings({"unchecked"})
    static <T extends Handler> InvocationHandler<T> hook(Class c) {
        return clazz(c, "hook", MycilaInvocationHandler.class);
    }

    static Formatter formatter(Class c) {
        return clazz(c, "formatter", SimpleFormatter.class);
    }

    static Filter filter(Class c) {
        return clazz(c, "filter", null);
    }

    static String encoding(Class c) {
        return property(c, "encoding", null);
    }

    @SuppressWarnings({"unchecked"})
    static <T> T clazz(Class c, String prop, Class<T> def) {
        try {
            String clazz = property(c, prop, def == null ? null : def.getName());
            return clazz == null ? null : (T) Thread.currentThread().getContextClassLoader().loadClass(clazz).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    static String property(Class c, String prop, String def) {
        String val = LogManager.getLogManager().getProperty(c.getName() + "." + prop);
        return val == null ? def : val;
    }

}
