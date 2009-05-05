package com.mycila.testing.plugin.db;

import com.mycila.testing.core.api.TestContext;
import static com.mycila.testing.core.introspect.Filters.*;
import com.mycila.testing.core.plugin.DefaultTestPlugin;
import com.mycila.testing.plugin.db.api.Db;
import com.mycila.testing.plugin.db.api.DbDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DatabasePlugin extends DefaultTestPlugin {

    private Map<TestContext, List<Db>> dbs = new IdentityHashMap<TestContext, List<Db>>();

    @Override
    public List<String> getAfter() {
        return Arrays.asList("spring", "guice1", "guice2");
    }

    @Override
    public void prepareTestInstance(TestContext context) throws Exception {
        for (Field field : context.introspector().selectFields(and(fieldsAnnotatedBy(DbDataSource.class), fieldsAccepting(DataSource.class)))) {
            context.introspector().set(field, DriverDataSourceImpl.from(field.getAnnotation(DbDataSource.class)));
        }
        for (Field field : context.introspector().selectFields(and(fieldsAnnotatedBy(DbDataSource.class), fieldsAccepting(Db.class)))) {
            context.introspector().set(field, registerForClose(context, DbImpl.using(DriverDataSourceImpl.from(field.getAnnotation(DbDataSource.class)))));
        }
    }

    @Override
    public void afterClass(TestContext context) throws Exception {
        List<Db> dbs = this.dbs.get(context);
        if (dbs != null) {
            for (Db db : dbs) {
                db.close();
            }
        }
    }

    private Db registerForClose(TestContext context, Db db) {
        List<Db> dbs = this.dbs.get(context);
        if (dbs == null) {
            this.dbs.put(context, dbs = new LinkedList<Db>());
        }
        dbs.add(db);
        return db;
    }

}
