package com.brunogoncalves.phonebook.backend.storage.mysql;

import com.brunogoncalves.phonebook.backend.domain.Contact;
import com.brunogoncalves.phonebook.backend.storage.exception.ContactStorageException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.brunogoncalves.phonebook.backend.storage.mysql.ContactStorageMysqlExpectations.*;
import static com.brunogoncalves.phonebook.backend.storage.mysql.ContactStorageMysqlVerifications.*;
import static org.junit.Assert.assertEquals;

public class ContactStorageMysqlContactSearchTest {

    @Mocked
    private BasicDataSource basicDataSource;

    @Mocked
    private Connection connection;

    @Mocked
    private PreparedStatement preparedStatement;

    @Mocked
    private ResultSet resultSet;

    private ContactStorageMysql storageMysql;

    private static String SEARCH_STATEMENT = "" +
            "SELECT id, phone_number, first_name, last_name " +
            "FROM CONTACT " +
            "WHERE phone_number LIKE ? " +
            "OR first_name LIKE ? " +
            "OR last_name LIKE ? ";

    @Before
    public void initMysqlStorage() throws ContactStorageException {
        storageMysql = new ContactStorageMysql(basicDataSource);
    }

    @Test
    public void testContactStorageSearchShouldSuccessfullySearchContact() throws ContactStorageException, SQLException {
        final String token = "search_token";
        final List<Map<String, Object>> queryResults = ImmutableList.of(
                ImmutableMap.of(
                        "id", 1,
                        "first_name", "first1",
                        "last_name", "last1",
                        "phone_number", "number1"
                ),
                ImmutableMap.of(
                        "id", 2,
                        "first_name", "first2",
                        "last_name", "last2",
                        "phone_number", "number2"
                )
        );
        final List<Contact> expectedContacts = ImmutableList.of(
                new Contact(1, "first1", "last1", "number1"),
                new Contact(2, "first2", "last2", "number2")
        );

        expectStorageToGetADbConnection(basicDataSource, connection);
        expectStorageToPrepareStatement(connection, SEARCH_STATEMENT, preparedStatement);
        expectStorageToExecuteQuery(preparedStatement, resultSet);
        expectResultSetToReturn(queryResults);

        final List<Contact> contactList = storageMysql.searchByToken(token);

        verifyArgumentSettingOfPreparedStatementForSearch(token);
        verifyResultSetIsClosed(resultSet);
        verifyPreparedStatementIsClosed(preparedStatement);
        verifyConnectionIsClosed(connection);
        assertEquals(expectedContacts, contactList);
    }

    @Test(expected = ContactStorageException.class)
    public void testContactStorageSearchThrowContactStorageExceptionInCaseOfExceptionGettingConnection() throws ContactStorageException, SQLException {
        final String token = "search_token";

        expectStorageToGetADbConnectionButThrowException(basicDataSource);

        storageMysql.searchByToken(token);
    }

    @Test(expected = ContactStorageException.class)
    public void testContactStorageSearchThrowContactStorageExceptionInCaseOfExceptionCreatingPreparedStatement() throws ContactStorageException, SQLException {
        final String token = "search_token";

        expectStorageToGetADbConnection(basicDataSource, connection);
        expectStorageToPrepareStatementButThrowException(connection, SEARCH_STATEMENT);

        storageMysql.searchByToken(token);
    }


    private void verifyArgumentSettingOfPreparedStatementForSearch(final String token) throws SQLException {
        new Verifications() {{
            preparedStatement.setString(withEqual(1), withEqual("%" + token + "%"));
            preparedStatement.setString(withEqual(2), withEqual("%" + token + "%"));
            preparedStatement.setString(withEqual(3), withEqual("%" + token + "%"));
        }};
    }

    private void expectResultSetToReturn(final List<Map<String, Object>> queryResults) throws SQLException {

        new Expectations() {{
            resultSet.getInt("id");
            result = new Delegate() {
                int timesCalled = 0;

                int delegate(final String fieldName) {
                    final int result = (int) queryResults.get(timesCalled).get(fieldName);
                    timesCalled++;
                    return result;
                }
            };

            resultSet.getString(anyString);
            result = new Delegate() {
                int timesCalled = 0;

                String delegate(final String fieldName) {
                    final String result = (String) queryResults.get(timesCalled/3).get(fieldName);
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
}
