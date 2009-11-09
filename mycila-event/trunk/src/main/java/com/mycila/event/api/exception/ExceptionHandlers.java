package com.mycila.event.api.exception;

import com.mycila.event.api.EventServiceException;
import com.mycila.event.api.event.Event;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ExceptionHandlers {

    private ExceptionHandlers() {
    }

    public static ExceptionHandlerProvider singleton(final ExceptionHandler handler) {
        return new ExceptionHandlerProvider() {
            @Override
            public ExceptionHandler get() {
                return handler;
            }
        };
    }

    public static ExceptionHandlerProvider ignoreExceptionsProvider() {
        return singleton(ignoreExceptions());
    }

    public static ExceptionHandler ignoreExceptions() {
        return SILENT;
    }

    private static final ExceptionHandler SILENT = new ExceptionHandler() {
        @Override
        public void onException(Event<?> event, Exception exception) {
        }

        @Override
        public List<Exception> exceptions() {
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

    public static ExceptionHandlerProvider rethrowExceptionsWhenFinishedProvider() {
        return new ExceptionHandlerProvider() {
            @Override
            public ExceptionHandler get() {
                return rethrowExceptionsWhenFinished();
            }
        };
    }

    public static ExceptionHandler rethrowExceptionsWhenFinished() {
        return new ExceptionHandler() {
            private final CopyOnWriteArrayList<Exception> exceptions = new CopyOnWriteArrayList<Exception>();

            @Override
            public List<Exception> exceptions() {
                return Collections.unmodifiableList(new ArrayList<Exception>(exceptions));
            }

            @Override
            public boolean hasFailed() {
                return !exceptions.isEmpty();
            }

            @Override
            public void onException(Event<?> event, Exception exception) {
                exceptions.add(exception);
            }

            @Override
            public void onPublishingFinished() {
                if (hasFailed()) {
                    List<Exception> exceptions = exceptions();
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
                    throw new EventServiceException(sw.toString(), null);
                }
            }

            @Override
            public void onPublishingStarting() {
                exceptions.clear();
            }
        };
    }

    public static ExceptionHandlerProvider rethrowExceptionsImmediatelyProvider() {
        return new ExceptionHandlerProvider() {
            @Override
            public ExceptionHandler get() {
                return rethrowExceptionsImmediately();
            }
        };
    }

    public static ExceptionHandler rethrowExceptionsImmediately() {
        return new ExceptionHandler() {
            private final CopyOnWriteArrayList<Exception> exceptions = new CopyOnWriteArrayList<Exception>();

            @Override
            public List<Exception> exceptions() {
                return Collections.unmodifiableList(new ArrayList<Exception>(exceptions));
            }

            @Override
            public boolean hasFailed() {
                return !exceptions.isEmpty();
            }

            @Override
            public void onException(Event<?> event, Exception exception) {
                exceptions.add(exception);
                if (exception instanceof RuntimeException)
                    throw (RuntimeException) exception;
                EventServiceException e = new EventServiceException(exception.getMessage(), exception);
                e.setStackTrace(exception.getStackTrace());
                throw e;
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

}
