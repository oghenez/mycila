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

import com.mycila.testing.core.MycilaTesting;
import com.mycila.testing.plugin.db.api.Db;
import com.mycila.testing.plugin.db.api.DbDataSource;
import com.mycila.testing.plugin.db.api.SqlResults;
import com.mycila.testing.testng.MycilaTestNGTest;
import org.h2.Driver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.sql.DataSource;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DatabasePluginUsage extends MycilaTestNGTest {

    static {
        MycilaTesting.debug();
    }

    @DbDataSource(driver = Driver.class, url = "jdbc:h2:file:./target/db")
    DataSource dataSource;

    @DbDataSource(driver = Driver.class, url = "jdbc:h2:file:./target/db")
    Db db;

    @BeforeClass
    public void setup() throws Exception {
        assertNotNull(dataSource);
        db.runScript("/create.sql");
    }

    @Test
    public void test_insert() throws Exception {
        db
                .prepare("INSERT INTO PERSON VALUES('1', 'math', 'M')").commit()
                .prepare("INSERT INTO PERSON VALUES('2', 'cassie', 'F')").commit();
        System.out.println(db.prepare("select * from person;").query());
    }

    @Test
    public void test_complex_requests() throws Exception {
        db.prepare("INSERT INTO PERSON VALUES(?, ?, ?)")
                .setInt(1, 10).setString(2, "complex1").setString(3, "F").push()
                .setInt(1, 11).setString(2, "complex2").setString(3, "M").commit()
                .prepare("INSERT INTO PERSON VALUES(?, ?, ?)")
                .setInt(1, 13).setString(2, "complex3").setString(3, "F").push()
                .rollback();
        System.out.println(db.prepare("select * from person;").query());
    }

    @Test
    public void test_batch() throws Exception {
        db.newBatch()
                .add("INSERT INTO PERSON VALUES('5', 'jello', 'M')")
                .add("INSERT INTO PERSON VALUES('6', 'jelly', 'F')")
                .addScript("/insert.sql")
                .commit()
                .newBatch()
                .add("INSERT INTO PERSON VALUES('7', 'jella', 'F')")
                .rollback();
        System.out.println(db.prepare("select * from person;").query());
    }

    @Test
    public void test_select() throws Exception {
        SqlResults sqlResults = db.prepare("SELECT ID, NAME, SEX as S FROM PERSON").query();
        System.out.println(sqlResults);
    }

}
