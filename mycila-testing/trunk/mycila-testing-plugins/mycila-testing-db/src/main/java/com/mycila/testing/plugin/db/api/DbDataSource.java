/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
