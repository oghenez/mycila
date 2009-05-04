package com.mycila.testing.plugin.db.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Inject a {@link javax.sql.DataSource}
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
public @interface DbDataSource {

    /**
     * @return JDBC driver
     */
    Class<? extends java.sql.Driver> driver();

    /**
     * @return JDBC connection url
     */
    String url();

    /**
     * @return JDBC username if any. Default to 'sa'
     */
    String username() default "sa";

    /**
     * @return JDBC password if needed. Default to blank.
     */
    String password() default "";

    /**
     * @return JDBC driver properties if needed.
     */
    DbProp[] properties() default {};

    /**
     * @return Default transaction isolation level to use when cretaing connections. Default will keep the driver one.
     */
    Isolation defaultIsolation() default Isolation.DEFAULT;
}
