import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Closure<V> implements Runnable, Callable<V> {

    private final Class<?>[] ptypes;
    private final Object[] curried;
    private final Object delegate;
    private final Method method;
    private final Method def;
    private Closure<V> next;

    // --- ctors ---

    private Closure(Object delegate, Method method, Method def) {
        this.delegate = delegate;
        this.method = method;
        this.def = def;
        this.ptypes = method.getParameterTypes();
        this.curried = new Object[0];
        this.next = null;
    }

    private Closure(Closure<?> me, Closure<V> next) {
        this.ptypes = me.ptypes;
        this.curried = me.curried;
        this.delegate = me.delegate;
        this.method = me.method;
        this.def = me.def;
        this.next = next;
    }

    private Closure(Closure<V> me, Object[] curried) {
        this.ptypes = new Class[me.ptypes.length - curried.length];
        System.arraycopy(me.ptypes, curried.length, this.ptypes, 0, this.ptypes.length);
        this.curried = new Object[me.curried.length + curried.length];
        System.arraycopy(me.curried, 0, this.curried, 0, me.curried.length);
        System.arraycopy(curried, 0, this.curried, me.curried.length, curried.length);
        this.delegate = me.delegate;
        this.method = me.method;
        this.def = me.def;
        this.next = me.next;
    }

    // --- properties ---

    public final Class<?> getReturnType() {
        return next == null ? method.getReturnType() : next.getReturnType();
    }

    public final Object getDelegate() {
        return delegate;
    }

    public final Class<?>[] getParameterTypes() {
        return ptypes;
    }

    public final int getParameterCount() {
        return ptypes.length;
    }

    @Override
    public final String toString() {
        return toString(null).toString();
    }

    private CharSequence toString(CharSequence inner) {
        StringBuilder sb = new StringBuilder();
        if (next == null) {
            sb.append(getReturnType().getName()).append(" ");
        }
        sb.append(def.getDeclaringClass().getName()).append(".").append(method.getName()).append("(");
        for (Object c : curried) {
            sb.append(c).append(", ");
        }
        if (inner != null) {
            sb.append(inner);
        } else {
            for (Class<?> ptype : ptypes) {
                sb.append(ptype.getName()).append(", ");
            }
            if (sb.charAt(sb.length() - 1) != '(') {
                sb.delete(sb.length() - 2, sb.length());
            }
        }
        sb.append(")");
        return next == null ? sb : next.toString(sb);
    }

    // --- execution ---

    public final void run() throws ExecutionException, InvocationException {
        call();
    }

    public final V call() throws ExecutionException, InvocationException {
        return call(new Object[0]);
    }

    public final V call(Object... args) throws ExecutionException, InvocationException {
        if (args.length != getParameterCount()) {
            throw new InvocationException("Bad parameter count: " + args.length + ". Requires " + getParameterCount() + " for calling closure " + this);
        }
        V v;
        try {
            //noinspection unchecked
            v = (V) method.invoke(delegate, merge(args));
        } catch (IllegalAccessException e) {
            // should not occur
            throw new InvocationException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new ExecutionException(e.getTargetException().getMessage(), e.getTargetException());
        }
        return next == null ? v : next.call(v);
    }

    // --- transforms ---

    public <V> Closure<V> then(Closure<V> then) {
        Objects.requireNonNull(then, "Closure required");
        if (then.getParameterCount() == 1 && then.getParameterTypes()[0].isAssignableFrom(getReturnType())) {
            return new Closure<>(this, then);
        }
        throw new IllegalArgumentException("Next closure must have only one argument accepting the return type of this closure " + this);
    }

    public Closure<V> curry(Object... args) {
        if (args.length > getParameterCount()) {
            throw new IllegalArgumentException("Too many arguments for this closure: " + args.length + ". Closure: " + this);
        }
        return args.length == 0 ? this : new Closure<>(this, args);
    }

    // --- privates ---

    private Object[] merge(Object... args) {
        Object[] o = new Object[curried.length + args.length];
        System.arraycopy(curried, 0, o, 0, curried.length);
        System.arraycopy(args, 0, o, curried.length, args.length);
        return o;
    }

    // --- factory ---

    public static <V> Closure<V> of(Object closure) throws IllegalArgumentException {
        Objects.requireNonNull(closure, "Closure required");
        Method[] methods = closure.getClass().getDeclaredMethods();
        if (methods.length == 1) {
            for (Class<?> ifc : closure.getClass().getInterfaces()) {
                try {
                    Method def = ifc.getDeclaredMethod(methods[0].getName(), methods[0].getParameterTypes());
                    if (!methods[0].isAccessible()) {
                        methods[0].setAccessible(true);
                    }
                    return new Closure<>(closure, methods[0], def);
                } catch (NoSuchMethodException ignored) {
                    // test other interfaces
                }
            }
        }
        throw new IllegalArgumentException("Object must implement a single-method interface");
    }

    // --- exceptions ---

    public static class Exception extends RuntimeException {
        private Exception(String message) {
            super(message);
        }

        private Exception(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static final class IllegalArgumentException extends Exception {
        private IllegalArgumentException(String message) {
            super(message);
        }
    }

    public static final class ExecutionException extends Exception {
        private ExecutionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static final class InvocationException extends Exception {
        private InvocationException(String message, Throwable cause) {
            super(message, cause);
        }

        private InvocationException(String message) {
            super(message);
        }
    }


    /*

    public <R> Closure<R> curry(Object o, Object... args) {
        Objects.requireNonNull(o, "Closure required");
        if (args == null) {
            args = new Object[0];
        }
        Method[] methods = o.getClass().getDeclaredMethods();
        switch (methods.length) {
            case 0: {
                throw new IllegalArgumentException("Object must implement a single-method interface");

            }
        }

        *//*if (!type.isInterface() || type.getMethods().length != 1) {
            throw new IllegalArgumentException("");
        }
        Method m = type.getMethods()[0];*//*
        return null;
    }

    *//**
     * Support for Closure currying.
     * <p/>
     * Typical usage:
     * <pre class="groovyTestCase">
     * def multiply = { a, b -> a * b }
     * def doubler = multiply.curry(2)
     * assert doubler(4) == 8
     * </pre>
     * Note: special treatment is given to Closure vararg-style capability.
     * If you curry a vararg parameter, you don't consume the entire vararg array
     * but instead the first parameter of the vararg array as the following example shows:
     * <pre class="groovyTestCase">
     * def a = { one, two, Object[] others -> one + two + others.sum() }
     * assert a.parameterTypes.name == ['java.lang.Object', 'java.lang.Object', '[Ljava.lang.Object;']
     * assert a(1,2,3,4) == 10
     * def b = a.curry(1)
     * assert b.parameterTypes.name == ['java.lang.Object', '[Ljava.lang.Object;']
     * assert b(2,3,4) == 10
     * def c = b.curry(2)
     * assert c.parameterTypes.name == ['[Ljava.lang.Object;']
     * assert c(3,4) == 10
     * def d = c.curry(3)
     * assert d.parameterTypes.name == ['[Ljava.lang.Object;']
     * assert d(4) == 10
     * def e = d.curry(4)
     * assert e.parameterTypes.name == ['[Ljava.lang.Object;']
     * assert e() == 10
     * assert e(5) == 15
     * </pre>
     *
     * @param arguments the arguments to bind
     * @return the new closure with its arguments bound
     *//*
    public Closure<V> curry(final Object... arguments) {
        return new CurriedClosure<V>(this, arguments);
    }

    *//**
     * Support for Closure "right" currying.
     * Parameters are supplied on the right rather than left as per the normal curry() method.
     * Typical usage:
     * <pre>
     * def divide = { a, b -> a / b }
     * def halver = divide.rcurry(2)
     * assert halver(8) == 4
     * </pre>
     *
     * @param arguments the arguments to bind
     * @return the new closure with its arguments bound
     * @see #curry(Object...)
     *//*
    public Closure<V> rcurry(final Object... arguments) {
        return new CurriedClosure<V>(-arguments.length, this, arguments);
    }



*/
}
