package com.brunogoncalves.phonebook.backend.storage.mysql;

import com.brunogoncalves.phonebook.backend.storage.ContactStorageException;
import com.brunogoncalves.phonebook.backend.domain.Contact;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

//TODO: Tests to the Update are missing
public class ContactStorageMysqlTest {

    @Mocked
    private BasicDataSource basicDataSource;

    @Mocked
    private Connection connection;

    @Mocked
    private Statement statement;

    @Mocked
    private PreparedStatement preparedStatement;

    @Mocked
    private ResultSet resultSet;

    private ContactStorageMysql storageMysql;

    @Test
    public void testContactStorageConstructorShouldCreateContactTable() throws ContactStorageException, SQLException {
        expectStorageToGetADbConnection();
        expectStorageToCreateStatement();

        storageMysql = new ContactStorageMysql(basicDataSource);

        verifyExecutionOfCreateTableStatement();
    }

    @Test
    public void testContactStorageShouldSafelyHandleExceptionWhenContactTableIsAlreadyCreated() throws ContactStorageException, SQLException {
        expectStorageToGetADbConnection();
        expectStorageToCreateStatement();
        expectStorageToExecuteCreateStatementButReturnException(1050);

        storageMysql = new ContactStorageMysql(basicDataSource);
    }

    @Test(expected = ContactStorageException.class)
    public void testContactStorageShouldThrowContactStorageExceptionInCaseOfExceptionGettingConnection() throws ContactStorageException, SQLException {
        expectStorageToGetADbConnectionButThrowException();

        storageMysql = new ContactStorageMysql(basicDataSource);
    }

    @Test(expected = ContactStorageException.class)
    public void testContactStorageShouldThrowContactStorageExceptionInCaseOfExceptionCreatingStatement() throws ContactStorageException, SQLException {
        expectStorageToGetADbConnection();
        expectStorageToCreateStatementButThrowException();

        storageMysql = new ContactStorageMysql(basicDataSource);
    }

    @Test(expected = ContactStorageException.class)
    public void testContactStorageShouldThrowContactStorageExceptionInCaseOfExceptionCreatingTable() throws ContactStorageException, SQLException {
        expectStorageToGetADbConnection();
        expectStorageToCreateStatement();
        expectStorageToExecuteCreateStatementButReturnException(1000);

        storageMysql = new ContactStorageMysql(basicDataSource);
    }

    @Test
    public void testContactStorageCreateShouldSuccessfullyCreateContact() throws ContactStorageException, SQLException {
        final Contact contact = new Contact("first", "last", "number");
        storageMysql = new ContactStorageMysql(basicDataSource);

        expectStorageToGetADbConnection();
        expectStorageToCreatePreparedStatement("INSERT INTO CONTACT(phone_number, first_name, last_name) " +
                                               "VALUES(?, ?, ?) ");

        storageMysql.create(contact);

        verifyArgumentSettingAndExecutionOfPreparedStatementForCreate(contact);
    }

    @Test(expected = ContactStorageException.class)
    public void testContactStorageCreateThrowContactStorageExceptionInCaseOfExceptionGettingConnection() throws ContactStorageException, SQLException {
        final Contact contact = new Contact("first", "last", "number");
        storageMysql = new ContactStorageMysql(basicDataSource);

        expectStorageToGetADbConnectionButThrowException();

        storageMysql.create(contact);
    }


    @Test(expected = ContactStorageException.class)
    public void testContactStorageCreateThrowContactStorageExceptionInCaseOfExceptionCreatingPreparedStatement() throws ContactStorageException, SQLException {
        final Contact contact = new Contact("first", "last", "number");
        storageMysql = new ContactStorageMysql(basicDataSource);

        expectStorageToGetADbConnection();
        expectStorageToCreatePreparedStatementButThrowException();

        storageMysql.create(contact);
    }

    @Test
    public void testContactStorageSearchShouldSuccessfullySearchContact() throws ContactStorageException, SQLException {
        final String token = "search_token";
        final List<Map<String, String>> queryResults = ImmutableList.of(
                ImmutableMap.of(
                        "first_name", "first1",
                        "last_name", "last1",
                        "phone_number", "number1"
                ),
                ImmutableMap.of(
                        "first_name", "first2",
                        "last_name", "last2",
                        "phone_number", "number2"
                )
        );
        final List<Contact> expectedContacts = ImmutableList.of(
                new Contact("first1", "last1", "number1"),
                new Contact("first2", "last2", "number2")
        );

        storageMysql = new ContactStorageMysql(basicDataSource);

        expectStorageToGetADbConnection();
        expectStorageToCreatePreparedStatement("SELECT phone_number, first_name, last_name " +
                                               "FROM CONTACT " +
                                               "WHERE phone_number LIKE ? " +
                                               "OR first_name LIKE ? " +
                                               "OR last_name LIKE ? ");
        expectStorageToExecuteSearchPreparedStatement();
        expectResultSetToReturn(queryResults);

        final List<Contact> contactList = storageMysql.searchByToken(token);

        verifyArgumentSettingOfPreparedStatementForSearch(token);
        assertEquals(expectedContacts, contactList);
    }

    @Test(expected = ContactStorageException.class)
    public void testContactStorageSearchThrowContactStorageExceptionInCaseOfExceptionGettingConnection() throws ContactStorageException, SQLException {
        final String token = "search_token";
        storageMysql = new ContactStorageMysql(basicDataSource);

        expectStorageToGetADbConnectionButThrowException();

        storageMysql.searchByToken(token);
    }

    @Test(expected = ContactStorageException.class)
    public void testContactStorageSearchThrowContactStorageExceptionInCaseOfExceptionCreatingPreparedStatement() throws ContactStorageException, SQLException {
        final String token = "search_token";
        storageMysql = new ContactStorageMysql(basicDataSource);

        expectStorageToGetADbConnection();
        expectStorageToCreatePreparedStatementButThrowException();

        storageMysql.searchByToken(token);
    }

    private void expectResultSetToReturn(final List<Map<String, String>> queryResults) throws SQLException {

        new Expectations() {{
            resultSet.getString(anyString);
            result = new Delegate() {
                int timesCalled = 0;

                String delegate(String fieldName) {
                    final String result = queryResults.get(timesCalled / 3).get(fieldName);
                    timesCalled++;
                    return result;
                }
            };

            resultSet.next();
            result = new Delegate() {
                int timesCalled = 0;

                boolean delegate() {
                    timesCalled++;
                    return timesCalled <= queryResults.size();
                }
            };
        }};

    }

    private void expectStorageToExecuteSearchPreparedStatement() throws SQLException {
        new Expectations() {{
            preparedStatement.executeQuery();
            result = resultSet;
        }};
    }

    private void expectStorageToCreatePreparedStatement(final String statement) throws SQLException {
        new Expectations() {{
            connection.prepareStatement(withEqual(statement));
            result = preparedStatement;
        }};
    }

    private void expectStorageToCreatePreparedStatementButThrowException() throws SQLException {
        new Expectations() {{
            connection.prepareStatement(withEqual(anyString));
            result = new SQLException();
        }};
    }

    private void expectStorageToGetADbConnection() throws SQLException {
        new Expectations() {{
            basicDataSource.getConnection();
            result = connection;
        }};
    }

    private void expectStorageToGetADbConnectionButThrowException() throws SQLException {
        new Expectations() {{
            basicDataSource.getConnection();
            result = new SQLException();
        }};
    }

    private void expectStorageToCreateStatement() throws SQLException {
        new Expectations() {{
            connection.createStatement();
            result = statement;
        }};
    }

    private void expectStorageToCreateStatementButThrowException() throws SQLException {
        new Expectations() {{
            connection.createStatement();
            result = new SQLException();
        }};
    }

    private void expectStorageToExecuteCreateStatementButReturnException(int sqlVendorCode) throws SQLException {
        new Expectations() {{
            statement.execute(anyString);
            result = new SQLException("Table already exists", null, sqlVendorCode);
        }};
    }

    private void verifyExecutionOfCreateTableStatement() throws SQLException {
        new Verifications() {{
            statement.execute(withEqual("CREATE TABLE CONTACT " +
                                        "(phone_number VARCHAR(255) not NULL, " +
                                        " first_name   VARCHAR(255) not NULL, " +
                                        " last_name    VARCHAR(255) not NULL, " +
                                        " PRIMARY KEY (phone_number)) "));
        }};
    }

    private void verifyArgumentSettingAndExecutionOfPreparedStatementForCreate(final Contact contact) throws SQLException {
        new Verifications() {{
            preparedStatement.setString(withEqual(1), withEqual(contact.getPhoneNumber()));
            preparedStatement.setString(withEqual(2), withEqual(contact.getFirstName()));
            preparedStatement.setString(withEqual(3), withEqual(contact.getLastName()));
            preparedStatement.execute();
        }};
    }

    private void verifyArgumentSettingOfPreparedStatementForSearch(final String token) throws SQLException {
        new Verifications() {{
            preparedStatement.setString(withEqual(1), withEqual("%" + token + "%"));
            preparedStatement.setString(withEqual(2), withEqual("%" + token + "%"));
            preparedStatement.setString(withEqual(3), withEqual("%" + token + "%"));
        }};
    }


}
