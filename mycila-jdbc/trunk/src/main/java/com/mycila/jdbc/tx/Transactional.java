package com.mycila.jdbc.tx;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Transactional {

    /**
     * The transaction propagation type.
     * Defaults to {@link Propagation#REQUIRED}.
     */
    Propagation propagation() default Propagation.REQUIRED;

    /**
     * The transaction isolation level.
     */
    Isolation isolation() default Isolation.DEFAULT;

    /**
     * <code>true</code> if the transaction is read-only.
     * Defaults to <code>false</code>.
     * <p>This just serves as a hint for the actual transaction subsystem;
     * it will <i>not necessarily</i> cause failure of write access attempts.
     * A transaction manager which cannot interpret the read-only hint will
     * <i>not</i> throw an exception when asked for a read-only transaction.
     */
    boolean readOnly() default false;

    /**
     * Defines zero (0) or more exception {@link Class classes}, which must be a
     * subclass of {@link Throwable}, indicating which exception types must cause
     * a transaction rollback.
     * <p>This is the preferred way to construct a rollback rule, matching the
     * exception class and subclasses.
     */
    Class<? extends Throwable>[] rollbackOn() default {Throwable.class};
}
