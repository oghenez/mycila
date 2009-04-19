package com.mycila.testing.plugin.annotation;

import com.mycila.testing.junit.MycilaJunit3Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotationTestPluginJunit3Test extends MycilaJunit3Test {

    @Skip
    public void test_must_be_skipped_by_plugin() throws Exception {
        fail("must_be_skipped_by_plugin");
    }

    @Skip
    @ExpectException(type = IllegalStateException.class, message = "bla bla bla")
    public void test_must_throw_an_exception_1() throws Exception {
        fail("must_be_skipped_by_plugin");
        throw new IllegalStateException("exception 1");
    }

    @ExpectException(type = IllegalStateException.class)
    public void test_must_throw_an_exception_2() throws Exception {
        throw new IllegalStateException("exception 2");
    }

    @ExpectException(type = IllegalStateException.class, message = "exception 3")
    public void test_must_throw_an_exception_3() throws Exception {
        throw new IllegalStateException("exception 3");
    }

    @ExpectException(type = IllegalStateException.class, containing = "4")
    public void test_must_throw_an_exception_4() throws Exception {
        throw new IllegalStateException("exception 4");
    }

    @ExpectException(type = RuntimeException.class, message = "exception 5")
    public void test_must_throw_an_exception_5() throws Exception {
        throw new IllegalStateException("exception 5");
    }

    @Skip
    // because fails the test when working
    @ExpectException(type = IllegalArgumentException.class)
    public void test_must_throw_an_exception_6() throws Exception {
        throw new IllegalStateException("exception 6");
    }

    @Skip
    // because fails the test when working
    @ExpectException(type = IllegalStateException.class, message = "bla bla")
    public void test_must_throw_an_exception_7() throws Exception {
        throw new IllegalStateException("exception 7");
    }

    @Skip
    // because fails the test when working
    @ExpectException(type = IllegalStateException.class, containing = "bla bla")
    public void test_must_throw_an_exception_8() throws Exception {
        throw new IllegalStateException("exception 8");
    }
}