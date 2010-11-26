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
