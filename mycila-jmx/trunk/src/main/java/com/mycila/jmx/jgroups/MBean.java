package com.mycila.jmx.jgroups;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Optional annotation that exposes all public methods in the class
 * hierarchy (excluding Object) as MBean operations. All methods
 * are exposed if and only if exposeAll attribute is true.
 * <p/>
 * <p/>
 * If a more fine grained MBean attribute and operation exposure is needed
 * do not use @MBean annotation but annotate fields and public methods directly
 * using @ManagedOperation and @ManagedAttribute annotations.
 *
 * @author Chris Mills
 * @version $Id: MBean.java,v 1.6 2008/04/28 13:43:10 vlada Exp $
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface MBean {
    public abstract String objectName() default "";

    public abstract boolean exposeAll() default false;

    public abstract String description() default "";
}