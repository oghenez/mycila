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

    @DbDataSource(driver = Driver.class, url = "jdbc:h2:tcp://localhost/./target/db")
    Db db;

    @DbDataSource(driver = Driver.class, url = "jdbc:h2:tcp://localhost/./target/types2")
    Db db2;

    @DbDataSource(driver = Driver.class, url = "jdbc:h2:tcp://localhost/./target/types3")
    Db db3;

    @BeforeClass
    public void setup() throws Exception {
        db.runScript("/create.sql").runScript("/insert.sql");
        System.out.println(db.prepare("SELECT ID, NAME, SEX as S FROM PERSON").query());

        db2.runScript("/types.sql").runScript("/types-insert.sql");
        SqlResults results2 = db2.prepare("SELECT * FROM TESTTYPES").query();
        System.out.println(results2);
        System.out.println(results2.column("C21"));

        db3.runScript("/types.sql");
        System.out.println(db3.prepare("SELECT * FROM TESTTYPES").query());
    }

    @Test
    public void test_insert_complex() throws Exception {
        db3.prepare("INSERT INTO TESTTYPES(c13) VALUES(?)").setBytes(1, "hello".getBytes()).pushAndCommit();                    // byte arrays
        db3.prepare("INSERT INTO TESTTYPES(c14) VALUES(?)").setObject(1, UUID.randomUUID()).pushAndCommit();                    // java object seralized
        db3.prepare("INSERT INTO TESTTYPES(c18) VALUES(?)").setBlob(1, new SerialBlob("hello".getBytes())).pushAndCommit();     // blob
        db3.prepare("INSERT INTO TESTTYPES(c19) VALUES(?)").setClob(1, new SerialClob("hello".toCharArray())).pushAndCommit();  // clob
        db3.prepare("INSERT INTO TESTTYPES(c21) VALUES(?)").setObjects(1, new Object[]{1, "h", 'R'}).pushAndCommit();          // array
        System.out.println(db3.prepare("SELECT * FROM TESTTYPES").query());
    }

    @Test
    public void test_conversion() throws Exception {

    }

}