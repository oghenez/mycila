/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
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

package com.mycila.jdbc.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mycila.jdbc.ConnectionFactory;
import com.mycila.jdbc.UnitOfWork;
import com.mycila.jdbc.query.Sql;
import com.mycila.jdbc.tx.*;
import com.mycila.jdbc.tx.sql.JdbcConnectionFactory;
import com.mycila.jdbc.tx.sql.JdbcTransactionManager;
import com.mycila.jdbc.tx.sql.JdbcUnitOfWork;

import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

public final class SqlModule extends AbstractModule {
    @Override
    protected void configure() {
        // Datasource
        requireBinding(DataSource.class);

        // Manage datasource connections per thread
        bind(ConnectionFactory.class).to(JdbcConnectionFactory.class);
        bind(UnitOfWork.class).to(JdbcUnitOfWork.class);

        // Manage transactions
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        requestInjection(transactionInterceptor);
        bindInterceptor(any(), annotatedWith(Transactional.class), transactionInterceptor);
        bind(TransactionManager.class).to(JdbcTransactionManager.class).in(Singleton.class);
        bind(TransactionDefinitionBuilder.class).to(AnnotatedTransactionDefinitionBuilder.class);
    }

    @Provides
    Connection connection(ConnectionFactory factory) throws SQLException {
        return factory.getCurrentConnection();
    }

    @Provides
    Sql sql(ConnectionFactory factory) throws SQLException {
        return new Sql(factory.getCurrentConnection());
    }
}
