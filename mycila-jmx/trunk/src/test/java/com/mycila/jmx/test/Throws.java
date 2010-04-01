package com.mycila.jmx.test;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Throws extends TypeSafeMatcher<Code> {

    private final Class<? extends Throwable> exceptionClass;
    private final String message;
    private final boolean checkMessage;

    public Throws(Class<? extends Throwable> exceptionClass, String message, boolean checkMessage) {
        super(Code.class);
        this.exceptionClass = exceptionClass;
        this.message = message;
        this.checkMessage = checkMessage;
    }

    @Override
    public boolean matchesSafely(Code item) {
        try {
            item.run();
            throw new AssertionError("Code must have thrown an exception");
        } catch (Throwable throwable) {
            if (!exceptionClass.isInstance(throwable)) {
                throwable.printStackTrace();
                throw new AssertionError("Code thrown bad exception: " + throwable.getClass().getName());
            }
            if (checkMessage
                    && (throwable.getMessage() == null && message != null
                    || throwable.getMessage() != null && !throwable.getMessage().equals(message))) {
                throwable.printStackTrace();
                throw new AssertionError("Code thrown bad message: " + throwable.getMessage());
            }
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("code throws exception [")
                .appendText(exceptionClass.getName())
                .appendText("]");
        if (checkMessage)
            description.appendText(" with message [")
                    .appendText(message)
                    .appendText("]");
    }

    @Override
    public String toString() {
        return "sfsf";
    }

    public static Throws fire(Class<? extends Throwable> exceptionClass) {
        return new Throws(exceptionClass, null, false);
    }

    public static Throws fire(Class<? extends Throwable> exceptionClass, String message) {
        return new Throws(exceptionClass, message, true);
    }
}
