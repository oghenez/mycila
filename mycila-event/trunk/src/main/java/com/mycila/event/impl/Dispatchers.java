package com.mycila.event.impl;

import com.mycila.event.api.Dispatcher;
import com.mycila.event.api.ErrorHandlerProvider;
import com.mycila.event.api.ErrorHandlers;
import com.mycila.event.api.Topic;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Dispatchers {

    private Dispatchers() {
    }

    public static Dispatcher synchronous(boolean blocking) {
        return synchronous(blocking, ErrorHandlers.rethrowErrorsWhenFinished());
    }

    public static Dispatcher synchronous(boolean blocking, ErrorHandlerProvider errorHandlerProvider) {
        final SynchronousNonBlockingDispatcher dispatcher = new SynchronousNonBlockingDispatcher(notNull(errorHandlerProvider, "ErrorHandlerProvider"));
        return !blocking ? dispatcher : new DispatcherDelegate(dispatcher) {
            final Lock publishing = new ReentrantLock();

            @Override
            public <E> void publish(Topic topic, E source) {
                publishing.lock();
                try {
                    delegate.publish(topic, source);
                } finally {
                    publishing.unlock();
                }
            }
        };
    }

}
