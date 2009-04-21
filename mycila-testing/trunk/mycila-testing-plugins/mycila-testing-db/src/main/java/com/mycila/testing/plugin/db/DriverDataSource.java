package com.mycila.testing.plugin.db;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public @interface DriverDataSource {
    Class<? extends java.sql.Driver> driver();

    String url();

    String username() default "sa";

    String password() default "";

    DriverProperty[] properties() default {};
}
