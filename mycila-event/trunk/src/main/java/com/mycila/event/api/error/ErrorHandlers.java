package com.mycila.event.api.error;

import com.mycila.event.api.EventServiceException;
import com.mycila.event.api.event.Event;
import static com.mycila.event.api.util.Ensure.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ErrorHandlers {

    private ErrorHandlers() {
    }

    public static ErrorHandlerProvider singleton(final ErrorHandler handler) {
        notNull(handler, "ErrorHandler");
        return new ErrorHandlerProvider() {
            @Override
            public ErrorHandler get() {
                return handler;
            }
        };
    }

    public static ErrorHandlerProvider singleton(final ErrorHandlerProvider errorHandlerProvider) {
        notNull(errorHandlerProvider, "ErrorHandlerProvider");
        return new ErrorHandlerProvider() {
            final ErrorHandler errorHandler = errorHandlerProvider.get();

            @Override
            public ErrorHandler get() {
                return errorHandler;
            }
        };
    }

    public static ErrorHandlerProvider ignoreErrors() {
        return singleton(SILENT);
    }

    private static final ErrorHandler SILENT = new ErrorHandler() {
        @Override
        public void onError(Event<?> event, Exception exception) {
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

    public static ErrorHandlerProvider rethrowErrorsWhenFinished() {
        return new ErrorHandlerProvider() {
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
                    public void onError(Event<?> event, Exception exception) {
                        exceptions.add(exception);
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
                            throw new EventServiceException(sw.toString(), null);
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

    public static ErrorHandlerProvider rethrowErrorsImmediately() {
        return new ErrorHandlerProvider() {
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
                    public void onError(Event<?> event, Exception exception) {
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
        };
    }

}
