package com.mycila.jmx.export;

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MethodOperation implements JmxOperation {

    private final Signature signature;
    private final Method operation;
    private final ModelMBeanOperationInfo operationInfo;

    public MethodOperation(Method operation, String exportName, String description, MBeanParameterInfo... parameters) {
        this.operation = operation;
        this.signature = new Signature(operation);
        this.operationInfo = new ModelMBeanOperationInfo(
                exportName,
                description,
                parameters,
                operation.getReturnType().getName(),
                MBeanOperationInfo.UNKNOWN);
    }

    @Override
    public Signature getSignature() {
        return signature;
    }

    @Override
    public ModelMBeanOperationInfo getMetadata() {
        return operationInfo;
    }

    @Override
    public Object invoke(Object managedResource, Object... params) throws ReflectionException {
        try {
            return operation.invoke(managedResource, params);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e, e.getMessage());
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof Exception)
                throw new ReflectionException((Exception) e.getTargetException(), e.getTargetException().getMessage());
            throw new ReflectionException(new Exception(e.getTargetException()), e.getTargetException().getMessage());
        }
    }

    @Override
    public String toString() {
        return getMetadata().getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodOperation that = (MethodOperation) o;
        return signature.equals(that.signature);
    }

    @Override
    public int hashCode() {
        return signature.hashCode();
    }
}
