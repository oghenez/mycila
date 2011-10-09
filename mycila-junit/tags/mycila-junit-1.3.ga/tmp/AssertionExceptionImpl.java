/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
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
package com.mycila.testing.ea;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.mycila.testing.ea.ExtendedAssert.*;

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