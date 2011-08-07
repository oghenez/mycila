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
package com.mycila.testing.plugin.spring;

import org.h2.Driver;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Configuration
public class MyJavaConfig {

    @org.springframework.context.annotation.Bean
    public Dao dao() {
        return new DaoImpl();
    }

    @org.springframework.context.annotation.Bean
    public PlatformTransactionManager myTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @org.springframework.context.annotation.Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                "jdbc:h2:mem:",
                "sa",
                "");
        dataSource.setDriverClassName(Driver.class.getName());
        return dataSource;
    }

}
