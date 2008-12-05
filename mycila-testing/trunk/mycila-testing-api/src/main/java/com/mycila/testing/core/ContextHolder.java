package com.mycila.testing.core;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class ContextHolder {

    private static final ThreadLocal<Context> ctx = new InheritableThreadLocal<Context>();

    static void set(Context c) {
        ctx.set(c);
    }

    static void unset() {
        ctx.remove();
    }

    public static Context get() {
        Context c = ctx.get();
        if (c == null) {
            throw new IllegalStateException("There is not Context bound to local thread !");
        }
        return c;
    }
}
