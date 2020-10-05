package com.brunogoncalves.phonebook.backend.storage.mysql;

import com.brunogoncalves.phonebook.backend.storage.exception.ContactStorageException;
import mockit.Mocked;
import mockit.Verifications;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

import java.sql.*;

import static com.brunogoncalves.phonebook.backend.storage.mysql.ContactStorageMysqlExpectations.*;

//TODO: Tests to the Update are missing
public class ContactStorageMysqlInitializationTest {

    @Mocked
    private BasicDataSource basicDataSource;

    @Mocked
    private Connection connection;

    @Mocked
    private Statement statement;

    @Mocked
    private ResultSet resultSet;

    private static String CREATE_STATEMENT = "" +
            "CREATE TABLE CONTACT " +
            "(id           int          not NULL AUTO_INCREMENT, " +
            " phone_number VARCHAR(255) not NULL, " +
            " first_name   VARCHAR(255) not NULL, " +
            " last_name    VARCHAR(255) not NULL, " +
            " PRIMARY KEY (id)) ";

    @Test
    public void testContactStorageConstructorShouldCreateContactTable() throws ContactStorageException, SQLException {
        expectStorageToGetADbConnection(basicDataSource, connection);
        expectStorageToCreateStatement(connection, statement);

        new ContactStorageMysql(basicDataSource);

        verifyExecutionOfCreateTableStatement();
    }

    @Test
    public void testContactStorageShouldSafelyHandleExceptionWhenContactTableIsAlreadyCreated() throws ContactStorageException, SQLException {
        expectStorageToGetADbConnection(basicDataSource, connection);
        expectStorageToCreateStatement(connection, statement);
        expectStorageToExecuteStatementButReturnException(statement, CREATE_STATEMENT, new SQLException("Table Already Exists", null, 1050));

        new ContactStorageMysql(basicDataSource);
    }

    @Test(expected = ContactStorageException.class)
    public void testContactStorageShouldThrowContactStorageExceptionInCaseOfExceptionGettingConnection() throws ContactStorageException, SQLException {
        expectStorageToGetADbConnectionButThrowException(basicDataSource);

        new ContactStorageMysql(basicDataSource);
    }

    @Test(expected = ContactStorageException.class)
    public void testContactStorageShouldThrowContactStorageExceptionInCaseOfExceptionCreatingStatement() throws ContactStorageException, SQLException {
        expectStorageToGetADbConnection(basicDataSource, connection);
        expectStorageToCreateStatementButThrowException(connection);

        new ContactStorageMysql(basicDataSource);
    }

    @Test(expected = ContactStorageException.class)
    public void testContactStorageShouldThrowContactStorageExceptionInCaseOfExceptionCreatingTable() throws ContactStorageException, SQLException {
        expectStorageToGetADbConnection(basicDataSource, connection);
        expectStorageToCreateStatement(connection, statement);
        expectStorageToExecuteStatementButReturnException(statement, CREATE_STATEMENT, new SQLException("Error", null, 1000));

        new ContactStorageMysql(basicDataSource);
    }

    private void verifyExecutionOfCreateTableStatement() throws SQLException {
        new Verifications() {{
            statement.execute(withEqual(CREATE_STATEMENT));
        }};
    }
}
