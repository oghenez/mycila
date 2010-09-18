package com.mycila.event.internal;

import com.mycila.event.EventRequest;
import com.mycila.event.FutureListener;
import com.mycila.event.SubscriberExecutionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import static com.mycila.event.internal.Message.State.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Message<R> implements Future<R>, EventRequest<R> {

    static enum State {
        WAITING, DONE, ERROR, CANCELLED
    }

    private final CountDownLatch answerLatch = new CountDownLatch(1);
    private final List<Object> parameters = new ArrayList<Object>();
    private final AtomicReference<State> state = new AtomicReference<State>(WAITING);
    private final Collection<FutureListener<R>> listeners;

    private volatile R reply;
    private volatile SubscriberExecutionException error;

    public Message(Collection<FutureListener<R>> listeners, List<?> parameters) {
        this.parameters.addAll(parameters);
        this.listeners = listeners;
    }

    @Override
    public boolean isCancelled() {
        return state.get() == CANCELLED;
    }

    @Override
    public boolean isDone() {
        State s = state.get();
        return s == DONE || s == ERROR;
    }

    @Override
    public List<?> getParameters() {
        return parameters;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (state.compareAndSet(WAITING, CANCELLED)) {
            // unlock all waiting threads
            answerLatch.countDown();
            return true;
        }
        return false;
    }

    @Override
    public R get() throws SubscriberExecutionException, InterruptedException {
        answerLatch.await();
        return result();
    }

    @Override
    public R get(long timeout, TimeUnit unit) throws SubscriberExecutionException, TimeoutException, InterruptedException {
        if (answerLatch.await(timeout, unit))
            return result();
        throw new TimeoutException("No response returned within " + timeout + " " + unit);
    }

    @Override
    public void reply(R reply) {
        if (state.compareAndSet(WAITING, DONE)) {
            this.reply = reply;
            answerLatch.countDown();
            for (FutureListener<R> listener : listeners)
                listener.onResponse(reply);
        } else
            throw new IllegalStateException("Request has already been replied");
    }

    @Override
    public void replyError(Throwable error) {
        if (state.compareAndSet(WAITING, ERROR)) {
            this.error = SubscriberExecutionException.wrap(error);
            answerLatch.countDown();
            for (FutureListener<R> listener : listeners)
                listener.onError(this.error.getCause());
        } else
            throw new IllegalStateException("Request has already been replied");
    }

    private R result() throws SubscriberExecutionException {
        switch (state.get()) {
            case DONE:
                return reply;
            case ERROR:
                throw error;
            default:
                throw new IllegalStateException("Result not available yet !");
        }
    }

    @Override
    public String toString() {
        return "req(" + getParameters() + ") => " + state.get();
    }
}
