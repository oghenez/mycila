package com.mycila.event.api.exception;

import com.mycila.event.api.event.Event;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

    public static ExceptionHandlerProvider silentProvider() {
        return singleton(SILENT);
    }

    public static ExceptionHandler silent() {
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
        public void onEnd() {
        }

        @Override
        public void onStart() {
        }
    };

    public static ExceptionHandlerProvider cumulativeProvider() {
        return new ExceptionHandlerProvider() {
            @Override
            public ExceptionHandler get() {
                return new CumulativeExceptionHandler();
            }
        };
    }

    public static ExceptionHandler cumulative() {
        return new CumulativeExceptionHandler();
    }

    public static final class CumulativeExceptionHandler implements ExceptionHandler {

        private final List<Exception> exceptions = Collections.synchronizedList(new LinkedList<Exception>());

        public List<Exception> exceptions() {
            return Collections.unmodifiableList(new ArrayList<Exception>(exceptions));
        }

        public boolean hasFailed() {
            return !exceptions.isEmpty();
        }

        @Override
        public void onException(Event<?> event, Exception exception) {
            exceptions.add(exception);
        }

        @Override
        public void onEnd() {
            if (hasFailed()) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                pw.println(exceptions.size() + " errors occured:");
                pw.println();
                for (Exception exception : exceptions) {
                    exception.printStackTrace(pw);
                    pw.println();
                }
                throw new RuntimeException(pw.toString());
            }
        }

        @Override
        public void onStart() {
        }
    }
}
