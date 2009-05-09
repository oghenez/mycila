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

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import java.util.UUID;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class SqlResultsTest extends MycilaTestNGTest {

    static {
        MycilaTesting.debug();
    }

    @DbDataSource(driver = Driver.class, url = "jdbc:h2:file:./target/db")
    Db db;

    @DbDataSource(driver = Driver.class, url = "jdbc:h2:file:./target/types2")
    Db db2;

    @DbDataSource(driver = Driver.class, url = "jdbc:h2:file:./target/types3")
    Db db3;

    @BeforeClass
    public void setup() throws Exception {
        db.runScript("/create.sql").runScript("/insert.sql");
        System.out.println(db.prepare("SELECT ID, NAME, SEX as S FROM PERSON").query());

        db2.runScript("/types.sql").runScript("/types-insert.sql");
        SqlResults results2 = db2.prepare("SELECT * FROM TESTTYPES").query();
        System.out.println(results2);
        System.out.println(results2.column("column21"));

        db3.runScript("/types.sql");
        System.out.println(db3.prepare("SELECT * FROM TESTTYPES").query());
    }

    @Test
    public void test_insert_complex() throws Exception {
        db3.prepare("INSERT INTO TESTTYPES(c13) VALUES(?)").setBytes(1, "hello".getBytes()).commit();                    // byte arrays
        db3.prepare("INSERT INTO TESTTYPES(c14) VALUES(?)").setObject(1, UUID.randomUUID()).commit();                    // java object seralized
        db3.prepare("INSERT INTO TESTTYPES(c18) VALUES(?)").setBlob(1, new SerialBlob("hello".getBytes())).commit();     // blob
        db3.prepare("INSERT INTO TESTTYPES(c19) VALUES(?)").setClob(1, new SerialClob("hello".toCharArray())).commit();  // clob
        db3.prepare("INSERT INTO TESTTYPES(column21) VALUES(?)").setObjects(1, new Object[]{1, "h", 'R'}).commit();           // array
        System.out.println(db3.prepare("SELECT * FROM TESTTYPES").query());
    }

    //@Test
    public void test_select() throws Exception {
        SqlResults results2 = db2.prepare("SELECT * FROM TESTTYPES").query();
        System.out.println("All results:\n" + results2);
        System.out.println("columns: " + results2.columnCount());
        System.out.println("rows: " + results2.columnCount());
        System.out.println("9th element of row 5: " + results2.row(4).column(8));
        System.out.println("name of column 4: " + results2.columnHeader(3).name());
    }
}
