package com.mycila.testing.plugin.db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a field of type {@link javax.sql.DataSource} to be injected with the created datasource
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
public @interface InjectDataSource {
    Class<? extends java.sql.Driver> driver();

    String url();

    String username() default "sa";

    String password() default "";

    Property[] properties() default {};
}
