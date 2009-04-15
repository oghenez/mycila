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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Collection;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ExtendedAssert {

    private static final SoftHashMap<URL, byte[]> cache = new SoftHashMap<URL, byte[]>();
    private static final byte[] NULL = new byte[0];

    private ExtendedAssert() {
    }

    public void assertSameXml(String actual, String expected) {
        assertSameXml(null, actual, expected);
    }

    public void assertSameXml(String message, String actual, String expected) {
        if (actual == null && expected == null) {
            return;
        }
        if (expected == null || actual == null) {
            fail(message, actual, expected);
        }
        //noinspection ConstantConditions
        if (!actual.replaceAll("\\r|\\n", "").replaceAll(">\\s*<", "><").equals(expected.replaceAll("\\r|\\n", "").replaceAll(">\\s*<", "><"))) {
            fail(message, actual, expected);
        }
    }

    public static void assertNotEquals(Object actual, Object expected) {
        assertNotEquals(null, actual, expected);
    }

    public static void assertNotEquals(String message, Object actual, Object expected) {
        if (expected == null && actual == null || expected != null && expected.equals(actual)) {
            fail(message, actual, "Objects not equals");
        }
    }

    public static void assertEmpty(String actual) {
        assertEmpty(null, actual);
    }

    public static void assertEmpty(String message, String actual) {
        if (actual != null && actual.length() > 0) {
            fail(message, actual, "Empty string");
        }
    }

    public static void assertEmpty(Collection<?> actual) {
        assertEmpty(null, actual);
    }

    public static void assertEmpty(String message, Collection<?> actual) {
        if (actual != null && !actual.isEmpty()) {
            fail(message, actual, "Empty collection");
        }
    }

    public static void assertEmpty(Object[] actual) {
        assertEmpty(null, actual);
    }

    public static void assertEmpty(String message, Object[] actual) {
        if (actual != null && actual.length > 0) {
            fail(message, actual, "Empty array");
        }
    }

    public static void assertBlank(String actual) {
        assertBlank(null, actual);
    }

    public static void assertBlank(String message, String actual) {
        if (actual != null && actual.trim().length() > 0) {
            fail(message, actual, "Blank string");
        }
    }

    public static URL resource(String classPath) {
        URL u = Thread.currentThread().getContextClassLoader().getResource(classPath);
        if (u == null) {
            throw new IllegalArgumentException("Resource not found in classpath: " + classPath);
        }
        return u;
    }

    public static String readString(String classPath) {
        return readString(classPath, "UTF-8");
    }

    public static String readString(String classPath, String encoding) {
        try {
            return new String(readByte(classPath), encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static byte[] readByte(String classPath) {
        return readByte(resource(classPath));
    }

    public static byte[] readByte(URL url) {
        byte[] data = cache.get(url);
        if (data == null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedInputStream bis = new BufferedInputStream(url.openStream());
                data = new byte[8192];
                int count;
                while ((count = bis.read(data)) != -1) {
                    baos.write(data, 0, count);
                }
                bis.close();
                data = baos.toByteArray();
                cache.put(url, data);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return data == NULL ? null : data;
    }

    public static AssertException assertThrow(final Class<? extends Throwable> exceptionClass) {
        return new AssertionExceptionImpl(exceptionClass);
    }

    private static String asString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    private static String format(String message, Object actual, Object expected) {
        String formatted = "";
        if (message != null && !message.equals(""))
            formatted = message + " ";
        String expectedString = String.valueOf(expected);
        String actualString = String.valueOf(actual);
        if (expectedString.equals(actualString))
            return formatted + "expected: "
                    + formatClassAndValue(expected, expectedString)
                    + " but was: " + formatClassAndValue(actual, actualString);
        else
            return formatted + "expected:<" + expectedString + "> but was:<"
                    + actualString + ">";
    }

    private static String formatClassAndValue(Object value, String valueString) {
        String className = value == null ? "null" : value.getClass().getName();
        return className + "<" + valueString + ">";
    }

    private static void fail(String message, Object actual, Object expected) {
        fail(format(message, actual, expected));
    }

    private static void fail(String message) {
        throw new AssertionError(message == null ? "" : message);
    }

    public static interface AssertException {
        AssertException withMessage(String message);

        AssertException containingMessage(String message);

        void whenRunning(Code code);
    }

    private static class AssertionExceptionImpl implements AssertException {

        private static enum MsgCheck {
            NONE, EQ, IN
        }

        private final Class<? extends Throwable> exceptionClass;

        private String message;
        private MsgCheck msgCheck = MsgCheck.NONE;

        public AssertionExceptionImpl(Class<? extends Throwable> exceptionClass) {
            this.exceptionClass = exceptionClass;
        }

        public AssertException withMessage(String message) {
            this.message = message;
            msgCheck = MsgCheck.EQ;
            return this;
        }

        public AssertException containingMessage(String message) {
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
                    } else if(message != null && msgThrown != null) {
                        switch (msgCheck) {
                            case EQ:
                                if(!message.equals(msgThrown)) {
                                    fail("Received bad exception message. Exception is:\n" + asString(throwable), msgThrown, message);
                                } else {
                                    break;
                                }
                            case IN:
                                if(!msgThrown.contains(message)) {
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
    }

}
