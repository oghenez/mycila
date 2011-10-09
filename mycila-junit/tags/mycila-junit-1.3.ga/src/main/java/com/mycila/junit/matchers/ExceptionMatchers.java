/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
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

package com.mycila.junit.matchers;

import groovy.lang.Closure;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.Callable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ExceptionMatchers {

    private ExceptionMatchers() {
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

    public static Thrown thrown(Class<? extends Throwable> exceptionClass) {
        return thrown(Matchers.<Throwable>instanceOf(exceptionClass));
    }

    public static Thrown thrown(Matcher<Throwable> typeMatcher) {
        return new Thrown(typeMatcher);
    }

    public static final class Thrown extends TypeSafeMatcher<Expression> {
        private final Matcher<Throwable> typeMatcher;
        private Matcher<String> message;

        private Thrown(Matcher<Throwable> typeMatcher) {
            this.typeMatcher = typeMatcher;
        }

        public Thrown withMessage(String msg) {
            return withMessage(Matchers.equalTo(msg));
        }

        public Thrown withMessage(Matcher<String> message) {
            this.message = message;
            return this;
        }

        @Override
        protected boolean matchesSafely(Expression item) {
            try {
                item.run();
                return false;
            } catch (Throwable e) {

                return typeMatcher.matches(e) && (message == null || message.matches(e.getMessage()));
            }
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("expression throwing ").appendDescriptionOf(typeMatcher);
            if (message != null) {
                description.appendText(" with message ").appendDescriptionOf(message);
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
