package com.brunogoncalves.phonebook.backend.storage.mysql;

import com.brunogoncalves.phonebook.backend.domain.Contact;
import com.brunogoncalves.phonebook.backend.domain.ContactData;
import com.brunogoncalves.phonebook.backend.storage.exception.ContactStorageException;
import com.brunogoncalves.phonebook.backend.storage.exception.CouldNotGetContactException;
import com.brunogoncalves.phonebook.backend.storage.exception.CouldNotInsertContactException;
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

import static com.brunogoncalves.phonebook.backend.storage.mysql.ContactStorageMysqlExpectations.*;
import static com.brunogoncalves.phonebook.backend.storage.mysql.ContactStorageMysqlVerifications.*;
import static org.junit.Assert.assertEquals;

public class ContactStorageMysqlContactGetTest {

    @Mocked
    private BasicDataSource basicDataSource;

    @Mocked
    private Connection connection;

    @Mocked
    private PreparedStatement preparedStatement;

    @Mocked
    private ResultSet resultSet;

    private ContactStorageMysql storageMysql;

    private static String GET_CONTACT_STATEMENT = "" +
            "SELECT id, phone_number, first_name, last_name " +
            "FROM CONTACT " +
            "WHERE id = ? ";

    @Before
    public void initMysqlStorage() throws ContactStorageException {
        storageMysql = new ContactStorageMysql(basicDataSource);
    }

    @Test
    public void testContactStorageGetShouldSuccessfullyReturnContact()
            throws ContactStorageException, SQLException, CouldNotGetContactException {
        final int contactId = 1;
        final Contact expectedContact = new Contact(contactId, "first", "last", "phone");

        expectStorageToGetADbConnection(basicDataSource, connection);
        expectStorageToPrepareStatement(connection, GET_CONTACT_STATEMENT, preparedStatement);
        expectStorageToExecuteQuery(preparedStatement, resultSet);
        expectResultSetToReturnContact(expectedContact);

        final Contact contact = storageMysql.get(contactId);

        assertEquals(expectedContact, contact);
        verifySetOfArgumentsOfPreparedStatementForGetById(contactId);
        verifyResultSetIsClosed(resultSet);
        verifyPreparedStatementIsClosed(preparedStatement);
        verifyConnectionIsClosed(connection);
    }

    @Test(expected = ContactStorageException.class)
    public void testContactStorageCreateThrowContactStorageExceptionInCaseOfExceptionGettingConnection()
            throws ContactStorageException, SQLException, CouldNotGetContactException {
        expectStorageToGetADbConnectionButThrowException(basicDataSource);

        storageMysql.get(1);
    }


    @Test(expected = ContactStorageException.class)
    public void testContactStorageCreateThrowContactStorageExceptionInCaseOfExceptionCreatingPreparedStatement()
            throws ContactStorageException, SQLException, CouldNotGetContactException {
        expectStorageToGetADbConnection(basicDataSource, connection);
        expectStorageToPrepareStatementButThrowException(connection, GET_CONTACT_STATEMENT);

        storageMysql.get(1);

        verifyConnectionIsClosed(connection);
    }

    private void verifySetOfArgumentsOfPreparedStatementForGetById(final int contactId) throws SQLException {
        new Verifications() {{
            preparedStatement.setInt(1, withEqual(contactId));
        }};
    }

    private void expectResultSetToReturnContact(final Contact contact) throws SQLException {

        new Expectations() {{
            resultSet.getInt("id");
            result = contact.getId();
            times = 1;

            resultSet.getString("first_name");
            result = contact.getContactData().getFirstName();
            times = 1;

            resultSet.getString("last_name");
            result = contact.getContactData().getLastName();
            times = 1;

            resultSet.getString("phone_number");
            result = contact.getContactData().getPhoneNumber();
            times = 1;

            resultSet.next();
            result = new Delegate() {
                int timesCalled = 0;

                boolean delegate() {
                    timesCalled++;
                    return timesCalled <= 1;
                }
            };
        }};
    }

}
