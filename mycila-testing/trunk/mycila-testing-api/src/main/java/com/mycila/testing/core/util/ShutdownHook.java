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