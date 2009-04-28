package com.mycila.testing.plugin.db;

import com.mycila.testing.core.api.TestContext;
import com.mycila.testing.core.api.TestExecution;
import static com.mycila.testing.core.introspect.Filters.*;
import com.mycila.testing.core.plugin.DefaultTestPlugin;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DatabasePlugin extends DefaultTestPlugin {

    @Override
    public List<String> getAfter() {
        return Arrays.asList("spring", "guice1", "guice2");
    }

    @Override
    public void prepareTestInstance(TestContext context) throws Exception {
        for (Field field : context.introspector().selectFields(and(fieldsAnnotatedBy(InjectDataSource.class), fieldsAccepting(DataSource.class)))) {
            context.introspector().set(field, DriverDataSourceImpl.from(field.getAnnotation(InjectDataSource.class)));
        }
    }

    @Override
    public void beforeTest(TestExecution testExecution) throws Exception {
        // @Query
        // @Tx: ro, isolation, rollback, ...
        // 
        /*Connection c;
        c.createStatement().executeQuery().
                // for tx
                        dataSource.getConnection().setAutoCommit();
        dataSource.getConnection().setReadOnly();
        dataSource.getConnection().setTransactionIsolation();*/
    }

    @Override
    public void afterTest(TestExecution testExecution) throws Exception {
        // for tx
        /*dataSource.getConnection().setAutoCommit();
        dataSource.getConnection().setReadOnly();
        dataSource.getConnection().setTransactionIsolation();*/
    }

    @Override
    public void afterClass(TestContext context) throws Exception {

    }

}
