package com.mycila.testing.plugin.db;

import static com.mycila.testing.ea.ExtendedAssert.*;
import com.mycila.testing.testng.MycilaTestNGTest;
import org.h2.Driver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DatabasePluginUsage extends MycilaTestNGTest {

    @InjectDataSource(driver = Driver.class, url = "jdbc:h2:tcp://localhost/./target/db")
    DataSource dataSource;

    @BeforeClass
    public void setup() throws Exception {
        assertNotNull(dataSource);
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(asString("/create.sql"));
        statement.close();
        connection.close();
    }

    @Test
    public void test_insert() throws Exception {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.addBatch("INSERT INTO PERSON VALUES('1', 'math', 'M')");
        statement.addBatch("INSERT INTO PERSON VALUES('2', 'cassie', 'F')");
        statement.executeBatch();
        statement.close();
        connection.close();
    }

    @Test
    public void test_select() throws Exception {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        //TODO
        statement.close();
        connection.close();
    }

    @Test
    public void test_tx_rollback() throws Exception {
        Connection connection = dataSource.getConnection();
        connection.setReadOnly(false);
        if (connection.getAutoCommit()) {
            connection.setAutoCommit(false);
        }
        connection.setTransactionIsolation(Isolation.READ_UNCOMMITTED.value());

        Statement statement = connection.createStatement();
        statement.addBatch("INSERT INTO PERSON VALUES('3', 'raph', 'M')");
        statement.addBatch("INSERT INTO PERSON VALUES('4', 'cha', 'F')");
        statement.executeBatch();
        statement.close();

        connection.rollback();
        connection.close();
    }

    @Test
    public void test_tx_commit() throws Exception {
        Connection connection = dataSource.getConnection();
        connection.setReadOnly(false);
        if (connection.getAutoCommit()) {
            connection.setAutoCommit(false);
        }
        connection.setTransactionIsolation(Isolation.READ_UNCOMMITTED.value());

        Statement statement = connection.createStatement();
        statement.addBatch("INSERT INTO PERSON VALUES('3', 'raph', 'M')");
        statement.addBatch("INSERT INTO PERSON VALUES('4', 'cha', 'F')");
        statement.executeBatch();
        statement.close();

        connection.commit();
        connection.close();
    }

}
