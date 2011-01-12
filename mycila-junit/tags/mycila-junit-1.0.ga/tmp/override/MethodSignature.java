package com.mycila.sandbox.override;

import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MethodSignature {
    private final int hash;
    private final String desc;

    private MethodSignature(Method method) {
        int h = method.getName().hashCode();
        h = h * 31 + method.getParameterTypes().length;
        Class[] params = method.getParameterTypes();
        for (Class parameterType : params)
            h = h * 31 + parameterType.hashCode();
        this.hash = h;
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName()).append("(");
        for (int j = 0, max = params.length - 1; j <= max; j++) {
            sb.append(getTypeName(params[j]));
            if (j < max)
                sb.append(",");
        }
        sb.append(")");
        this.desc = sb.toString();
    }

    @Override
    public int hashCode() {
        return this.hash;
    }

    @Override
    public String toString() {
        return desc;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MethodSignature && desc.equals(((MethodSignature) o).desc);
    }

    public static MethodSignature of(Method m) {
        return new MethodSignature(m);
    }

    private static String getTypeName(Class type) {
        StringBuilder sb = new StringBuilder();
        while (type.isArray()) {
            sb.append("[]");
            type = type.getComponentType();
        }
        sb.insert(0, type.getName());
        return sb.toString();
    }

}
