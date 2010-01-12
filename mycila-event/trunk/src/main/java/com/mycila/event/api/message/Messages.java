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

import java.lang.reflect.InvocationTargetException;
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
        return createRequest(null);
    }

    public static <P, R> MessageRequest<R> createRequest(P parameter) {
        return new Message<P, R>(parameter);
    }

    private static class Message<P, R> implements MessageRequest<R>, MessageResponse<P, R> {

        private final CountDownLatch answered = new CountDownLatch(1);
        private final AtomicBoolean replied = new AtomicBoolean(false);
        private final P parameter;
        private R reply;
        private RuntimeException error;

        private Message(P parameter) {
            this.parameter = parameter;
        }

        public P getParameter() {
            return parameter;
        }

        public R getResponse() throws InterruptedException {
            answered.await();
            return result();
        }

        public R getResponse(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
            if(answered.await(timeout, unit))
                return result();
            throw new TimeoutException("No response returned within " + timeout + " " + unit);
        }

        public void reply(R reply) {
            if (!replied.getAndSet(true)) {
                this.reply = reply;
                answered.countDown();
            } else throw new DispatcherException("Request has already been replied");
        }

        public void replyError(Exception error) {
            if (!replied.getAndSet(true)) {
                Throwable t = error;
                if (t instanceof InvocationTargetException)
                    t = ((InvocationTargetException) error).getTargetException();
                if (t instanceof Error) throw (Error) t;
                if (t instanceof RuntimeException) this.error = (RuntimeException) t;
                else {
                    this.error = DispatcherException.wrap(t);
                }
                answered.countDown();
            } else throw new DispatcherException("Request has already been replied");
        }

        private R result() {
            if (error != null) throw error;
            return reply;
        }
    }

}