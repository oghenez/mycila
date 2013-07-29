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

package com.mycila.event.spi;

import com.mycila.event.api.DispatcherException;
import com.mycila.event.api.Event;
import com.mycila.event.api.Subscriber;
import com.mycila.event.api.Subscription;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ErrorHandlers {

    private ErrorHandlers() {
    }

    public static ErrorHandler compose(final ErrorHandler... errorHandlers) {
        return new ErrorHandler() {
            @Override
            public <E> void onPublishingStarting(Event<E> event) {
                for (ErrorHandler errorHandler : errorHandlers)
                    errorHandler.onPublishingStarting(event);
            }

            @Override
            public <E> void onPublishingFinished(Event<E> event) {
                for (ErrorHandler errorHandler : errorHandlers)
                    errorHandler.onPublishingFinished(event);
            }

            @Override
            public <E> void onError(Subscription<E, Subscriber<E>> subscription, Event<E> event, Exception e) {
                for (ErrorHandler errorHandler : errorHandlers)
                    errorHandler.onError(subscription, event, e);
            }
        };
    }

    public static ErrorHandler ignoreErrors() {
        return SILENT;
    }

    private static final ErrorHandler SILENT = new ErrorHandlerAdapter();

    public static ErrorHandler rethrowErrorsAfterPublish() {
        return new ErrorHandler() {
            private final ThreadLocal<CopyOnWriteArrayList<Exception>> exceptions
                    = new InheritableThreadLocal<CopyOnWriteArrayList<Exception>>() {
                @Override
                protected CopyOnWriteArrayList<Exception> initialValue() {
                    return new CopyOnWriteArrayList<Exception>();
                }
            };

            @Override
            public <E> void onError(Subscription<E, Subscriber<E>> subscription, Event<E> event, Exception e) {
                exceptions.get().add(e);
            }

            @Override
            public <E> void onPublishingStarting(Event<E> event) {
                exceptions.get().clear();
            }

            @Override
            public <E> void onPublishingFinished(Event<E> event) {
                List<Exception> exceptions = this.exceptions.get();
                this.exceptions.remove();
                if (!exceptions.isEmpty()) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    pw.println("ExceptionHandler caught " + exceptions.size() + " error(s):");
                    pw.println();
                    int count = 1;
                    for (Exception exception : exceptions) {
                        pw.print(count++ + ") ");
                        exception.printStackTrace(pw);
                        pw.println();
                    }
                    throw new DispatcherException(sw.toString(), null);
                }
            }
        };
    }

    public static ErrorHandler rethrowErrorsImmediately() {
        return new ErrorHandlerAdapter() {
            @Override
            public <E> void onError(Subscription<E, Subscriber<E>> subscription, Event<E> event, Exception e) {
                throw ExceptionUtils.toRuntime(e);
            }
        };
    }

}
