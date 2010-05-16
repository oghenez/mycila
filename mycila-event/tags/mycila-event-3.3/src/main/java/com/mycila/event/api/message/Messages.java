/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.event.api.message;

import com.mycila.event.api.DispatcherException;
import com.mycila.event.api.SubscriberExecutionException;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Messages {

    private Messages() {
    }

    public static <R> MessageRequest<R> createRequest() {
        return new Message<R>();
    }

    public static <R> MessageRequest<R> createRequest(Object parameter) {
        return new Message<R>(parameter);
    }

    public static <R> MessageRequest<R> createRequest(Object... parameters) {
        return new Message<R>(parameters);
    }

    private static final class Message<R> implements MessageRequest<R>, MessageResponse<R> {

        private final Collection<MessageListener<R>> listeners = new CopyOnWriteArrayList<MessageListener<R>>();
        private final CountDownLatch answered = new CountDownLatch(1);
        private final AtomicBoolean replied = new AtomicBoolean(false);
        private final Object[] parameter;
        private volatile R reply;
        private volatile SubscriberExecutionException error;

        private Message(Object... parameter) {
            this.parameter = parameter;
        }

        public Object[] getParameters() {
            return parameter;
        }

        public MessageRequest<R> addListener(MessageListener<R> listener) {
            listeners.add(listener);
            return this;
        }

        public R getResponse() throws SubscriberExecutionException, InterruptedException {
            answered.await();
            return result();
        }

        public R getResponse(long timeout, TimeUnit unit) throws SubscriberExecutionException, TimeoutException, InterruptedException {
            if (answered.await(timeout, unit))
                return result();
            throw new TimeoutException("No response returned within " + timeout + " " + unit);
        }

        public void reply(R reply) {
            if (replied.compareAndSet(false, true)) {
                this.reply = reply;
                answered.countDown();
                for (MessageListener<R> listener : listeners)
                    listener.onResponse(reply);
            } else
                throw new DispatcherException("Request has already been replied");
        }

        public void replyError(Throwable error) {
            if (replied.compareAndSet(false, true)) {
                this.error = SubscriberExecutionException.wrap(error);
                answered.countDown();
                for (MessageListener<R> listener : listeners)
                    listener.onError(this.error.getCause());
            } else
                throw new DispatcherException("Request has already been replied");
        }

        private R result() throws SubscriberExecutionException {
            if (error != null) throw error;
            return reply;
        }

        @Override
        public String toString() {
            return "req(" + Arrays.deepToString(getParameters()) + ") => reply(" + (replied.get() ? reply : "<waiting>") + ")";
        }
    }

}
