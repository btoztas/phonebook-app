package com.brunogoncalves.phonebook.backend.storage.mysql;

import com.brunogoncalves.phonebook.backend.domain.Contact;
import com.brunogoncalves.phonebook.backend.domain.ContactData;
import com.brunogoncalves.phonebook.backend.storage.exception.ContactStorageException;
import com.brunogoncalves.phonebook.backend.storage.exception.CouldNotInsertContactException;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Mocked;
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

public class ContactStorageMysqlContactCreateTest {

    @Mocked
    private BasicDataSource basicDataSource;

    @Mocked
    private Connection connection;

    @Mocked
    private PreparedStatement preparedStatement;

    @Mocked
    private ResultSet resultSet;

    private ContactStorageMysql storageMysql;

    private static String INSERT_STATEMENT = "" +
            "INSERT INTO CONTACT(phone_number, first_name, last_name) " +
            "VALUES(?, ?, ?) ";

    @Before
    public void initMysqlStorage() throws ContactStorageException {
        storageMysql = new ContactStorageMysql(basicDataSource);
    }

    @Test
    public void testContactStorageCreateShouldSuccessfullyCreateContact() throws ContactStorageException, SQLException, CouldNotInsertContactException {
        final ContactData contactData = new ContactData("first", "last", "number");
        final Integer contactId = 1;
        final Contact expectedContact = new Contact(contactId, contactData);

        expectStorageToGetADbConnection(basicDataSource, connection);
        expectStorageToPrepareStatement(connection, INSERT_STATEMENT, preparedStatement);
        expectStorageToExecuteUpdate(preparedStatement, 1);
        expectStorageToGetGeneratedKeys(preparedStatement, resultSet);
        expectResultSetToReturnContactId(contactId);

        final Contact contact = storageMysql.create(contactData);

        assertEquals(expectedContact, contact);
        verifySetOfArgumentsOfPreparedStatementForCreate(contactData);
        verifyResultSetIsClosed(resultSet);
        verifyPreparedStatementIsClosed(preparedStatement);
        verifyConnectionIsClosed(connection);
    }

    @Test(expected = ContactStorageException.class)
    public void testContactStorageCreateThrowContactStorageExceptionInCaseOfExceptionGettingConnection() throws ContactStorageException, SQLException, CouldNotInsertContactException {
        final ContactData contactData = new ContactData("first", "last", "number");

        expectStorageToGetADbConnectionButThrowException(basicDataSource);

        storageMysql.create(contactData);
    }


    @Test(expected = ContactStorageException.class)
    public void testContactStorageCreateThrowContactStorageExceptionInCaseOfExceptionCreatingPreparedStatement() throws ContactStorageException, SQLException, CouldNotInsertContactException {
        final ContactData contactData = new ContactData("first", "last", "number");

        expectStorageToGetADbConnection(basicDataSource, connection);
        expectStorageToPrepareStatementWithGeneratedKeysButThrowException(connection, INSERT_STATEMENT, preparedStatement);

        storageMysql.create(contactData);

        verifyConnectionIsClosed(connection);
    }

    @Test(expected = CouldNotInsertContactException.class)
    public void testContactStorageCreateThrowsCouldNotInsertContactExceptionIfNumberRowsAffectedIs0() throws ContactStorageException, SQLException, CouldNotInsertContactException {
        final ContactData contactData = new ContactData("first", "last", "number");

        expectStorageToGetADbConnection(basicDataSource, connection);
        expectStorageToPrepareStatementWithGeneratedKeys(connection, INSERT_STATEMENT, preparedStatement);
        expectStorageToExecuteUpdate(preparedStatement, 0);

        storageMysql.create(contactData);

        verifySetOfArgumentsOfPreparedStatementForCreate(contactData);
        verifyResultSetIsClosed(resultSet);
        verifyPreparedStatementIsClosed(preparedStatement);
        verifyConnectionIsClosed(connection);
    }

    private void verifySetOfArgumentsOfPreparedStatementForCreate(final ContactData contact) throws SQLException {
        new Expectations() {{
            preparedStatement.setString(withEqual(1), withEqual(contact.getPhoneNumber()));
            preparedStatement.setString(withEqual(2), withEqual(contact.getFirstName()));
            preparedStatement.setString(withEqual(3), withEqual(contact.getLastName()));
        }};
    }

    private void expectResultSetToReturnContactId(final Integer contactId) throws SQLException {

        new Expectations() {{
            resultSet.getInt(1);
            result = contactId;

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
