package com.mycila.testing.ea;

import static com.mycila.testing.ea.ExtendedAssert.*;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class AssertionExceptionImpl implements ExtendedAssert.AssertException {

    private static enum MsgCheck {
        NONE, EQ, IN
    }

    private final Class<? extends Throwable> exceptionClass;

    private String message;
    private MsgCheck msgCheck = MsgCheck.NONE;

    public AssertionExceptionImpl(Class<? extends Throwable> exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public ExtendedAssert.AssertException withMessage(String message) {
        this.message = message;
        msgCheck = MsgCheck.EQ;
        return this;
    }

    public ExtendedAssert.AssertException containingMessage(String message) {
        this.message = message;
        msgCheck = MsgCheck.IN;
        return this;
    }

    public void whenRunning(Code code) {
        boolean failed = true;
        try {
            code.run();
            failed = false;
        } catch (Throwable throwable) {
            if (!exceptionClass.isAssignableFrom(throwable.getClass())) {
                fail("Received bad exception class. Exception is:\n" + asString(throwable), throwable.getClass().getName(), exceptionClass.getName());
            }
            if (msgCheck != MsgCheck.NONE) {
                String msgThrown = throwable.getMessage();
                if (message == null && msgThrown != null) {
                    fail("Received bad exception message. Exception is:\n" + asString(throwable), msgThrown, "no message (null)");
                } else if (message != null && msgThrown == null) {
                    switch (msgCheck) {
                        case EQ:
                            fail("Received bad exception message. Exception is:\n" + asString(throwable), "no message (null)", message);
                        case IN:
                            fail("Received bad exception message. Exception is:\n" + asString(throwable), "no message (null)", "message containing: " + message);
                    }
                } else if (message != null && msgThrown != null) {
                    switch (msgCheck) {
                        case EQ:
                            if (!message.equals(msgThrown)) {
                                fail("Received bad exception message. Exception is:\n" + asString(throwable), msgThrown, message);
                            } else {
                                break;
                            }
                        case IN:
                            if (!msgThrown.contains(message)) {
                                fail("Received bad exception message. Exception is:\n" + asString(throwable), msgThrown, "message containing: " + message);
                            } else {
                                break;
                            }
                    }
                }
            }
        }
        if (!failed) {
            switch (msgCheck) {
                case NONE:
                    fail(String.format("Should have thrown Exception class '%s'", exceptionClass.getName()));
                case EQ:
                    fail(String.format("Should have thrown Exception class '%s' with message '%s'", exceptionClass.getName(), message));
                case IN:
                    fail(String.format("Should have thrown Exception class '%s' containing message '%s'", exceptionClass.getName(), message));

            }
        }
    }

    private static String asString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

}