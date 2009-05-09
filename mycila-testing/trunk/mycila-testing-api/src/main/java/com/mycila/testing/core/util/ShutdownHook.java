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
package com.mycila.testing.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ShutdownHook {

    private static final ShutdownHook INSTANCE = new ShutdownHook();

    private final List<Closeable> clients = new ArrayList<Closeable>();
    private volatile boolean shutdownInProgress;

    private ShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                shutdownInProgress = true;
                for (Closeable closeable : clients) {
                    close(closeable);
                }
            }
        }, getClass().getName()));
    }

    public void add(Closeable closeable) {
        if (shutdownInProgress) {
            close(closeable);
        } else {
            clients.add(closeable);
        }
    }

    private void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception ignored) {
        }
    }

    public static ShutdownHook get() {
        return INSTANCE;
    }
}