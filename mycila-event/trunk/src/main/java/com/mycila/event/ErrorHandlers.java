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

package com.mycila.event;

import com.mycila.event.dispatch.DispatcherException;
import com.mycila.event.util.Provider;
import com.mycila.event.util.Providers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ErrorHandlers {

    private ErrorHandlers() {
    }

    public static Provider<ErrorHandler> compose(final Provider<? extends ErrorHandler>... providers) {
        return new Provider<ErrorHandler>() {
            @Override
            public ErrorHandler get() {
                final ErrorHandler[] errorHandlers = new ErrorHandler[providers.length];
                for (int i = 0; i < errorHandlers.length; i++)
                    errorHandlers[i] = providers[i].get();
                return new ErrorHandler() {
                    @Override
                    public void onPublishingStarting() {
                        for (ErrorHandler errorHandler : errorHandlers)
                            errorHandler.onPublishingStarting();
                    }

                    @Override
                    public void onPublishingFinished() {
                        for (ErrorHandler errorHandler : errorHandlers)
                            errorHandler.onPublishingFinished();
                    }

                    @Override
                    public void onError(Subscription subscription, Event event, Exception e) {
                        for (ErrorHandler errorHandler : errorHandlers)
                            errorHandler.onError(subscription, event, e);
                    }

                    @Override
                    public boolean hasFailed() {
                        for (ErrorHandler errorHandler : errorHandlers)
                            if (errorHandler.hasFailed())
                                return true;
                        return false;
                    }

                    @Override
                    public List<Exception> errors() {
                        List<Exception> exceptions = new LinkedList<Exception>();
                        for (ErrorHandler errorHandler : errorHandlers)
                            exceptions.addAll(errorHandler.errors());
                        return exceptions;
                    }
                };
            }
        };
    }

    public static Provider<ErrorHandler> ignoreErrors() {
        return Providers.cache(SILENT);
    }

    private static final ErrorHandler SILENT = new ErrorHandler() {
        @Override
        public void onError(Subscription subscription, Event event, Exception e) {
        }

        @Override
        public List<Exception> errors() {
            return Collections.emptyList();
        }

        @Override
        public boolean hasFailed() {
            return false;
        }

        @Override
        public void onPublishingFinished() {
        }

        @Override
        public void onPublishingStarting() {
        }
    };

    public static Provider<ErrorHandler> rethrowErrorsAfterPublish() {
        return new Provider<ErrorHandler>() {
            @Override
            public ErrorHandler get() {
                return new ErrorHandler() {
                    private final CopyOnWriteArrayList<Exception> exceptions = new CopyOnWriteArrayList<Exception>();

                    @Override
                    public List<Exception> errors() {
                        return Collections.unmodifiableList(new ArrayList<Exception>(exceptions));
                    }

                    @Override
                    public boolean hasFailed() {
                        return !exceptions.isEmpty();
                    }

                    @Override
                    public void onError(Subscription subscription, Event event, Exception e) {
                        exceptions.add(e);
                    }

                    @Override
                    public void onPublishingFinished() {
                        if (hasFailed()) {
                            List<Exception> exceptions = errors();
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

                    @Override
                    public void onPublishingStarting() {
                        exceptions.clear();
                    }
                };
            }
        };
    }

    public static Provider<ErrorHandler> rethrowErrorsImmediately() {
        return new Provider<ErrorHandler>() {
            @Override
            public ErrorHandler get() {
                return new ErrorHandler() {
                    private final CopyOnWriteArrayList<Exception> exceptions = new CopyOnWriteArrayList<Exception>();

                    @Override
                    public List<Exception> errors() {
                        return Collections.unmodifiableList(new ArrayList<Exception>(exceptions));
                    }

                    @Override
                    public boolean hasFailed() {
                        return !exceptions.isEmpty();
                    }

                    @Override
                    public void onError(Subscription subscription, Event event, Exception e) {
                        exceptions.add(e);
                        if (e instanceof RuntimeException)
                            throw (RuntimeException) e;
                        DispatcherException other = new DispatcherException(e.getMessage(), e);
                        other.setStackTrace(e.getStackTrace());
                        throw other;
                    }

                    @Override
                    public void onPublishingFinished() {
                    }

                    @Override
                    public void onPublishingStarting() {
                        exceptions.clear();
                    }
                };
            }
        };
    }

}
