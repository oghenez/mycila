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

    @DbDataSource(driver = Driver.class, url = "jdbc:h2:tcp://localhost/./target/db")
    DataSource dataSource;

    @DbDataSource(driver = Driver.class, url = "jdbc:h2:tcp://localhost/./target/db")
    Db db;

    @BeforeClass
    public void setup() throws Exception {
        assertNotNull(dataSource);
        db.runScript("/create.sql");
    }

    @Test
    public void test_insert() throws Exception {
        db
                .prepare("INSERT INTO PERSON VALUES('1', 'math', 'M')").pushAndCommit()
                .prepare("INSERT INTO PERSON VALUES('2', 'cassie', 'F')").pushAndCommit();
        System.out.println(db.prepare("select * from person;").query());
    }

    @Test
    public void test_complex_requests() throws Exception {
        db.prepare("INSERT INTO PERSON VALUES(?, ?, ?)")
                .setInt(1, 10).setString(2, "complex1").setString(3, "F").push()
                .setInt(1, 11).setString(2, "complex2").setString(3, "M").push()
                .commit()
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
