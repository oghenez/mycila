package com.mycila.junit.matchers;

import groovy.lang.Closure;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.Callable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ExceptionMatchers {

    private ExceptionMatchers() {
    }

    public static Expression expression(final Closure<?> c) {
        return new Expression() {
            @Override
            public void run() throws Throwable {
                c.call();
            }
        };
    }

    public static Expression expression(final Callable<?> c) {
        return new Expression() {
            @Override
            public void run() throws Throwable {
                c.call();
            }
        };
    }

    public static Matcher<Throwable> message(final String message) {
        return new TypeSafeMatcher<Throwable>() {
            @Override
            protected boolean matchesSafely(Throwable item) {
                return message == null && item.getMessage() == null || message != null && message.equals(item.getMessage());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("message ").appendValue(message);
            }
        };
    }

    public static Thrown thrown(Class<? extends Throwable> exceptionClass) {
        return new Thrown(exceptionClass);
    }

    public static final class Thrown extends TypeSafeMatcher<Expression> {
        private final Class<? extends Throwable> exceptionClass;
        private String message;

        private Thrown(Class<? extends Throwable> exceptionClass) {
            this.exceptionClass = exceptionClass;
        }

        public Matcher<Expression> withMessage(String msg) {
            this.message = msg;
            return this;
        }

        @Override
        protected boolean matchesSafely(Expression item) {
            try {
                item.run();
                return false;
            } catch (Throwable e) {
                return exceptionClass.isInstance(e) && (message == null || message.equals(e.getMessage()));
            }
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("expression throwing ").appendValue(exceptionClass.getName());
            if (message != null) {
                description.appendText(" with message ").appendValue(message);
            }
        }
    }

    public static abstract class Expression {
        abstract void run() throws Throwable;

        @Override
        public String toString() {
            return "working expression";
        }
    }
}
