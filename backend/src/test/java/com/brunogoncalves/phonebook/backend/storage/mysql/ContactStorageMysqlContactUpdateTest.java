package com.brunogoncalves.phonebook.backend.storage.mysql;

import com.brunogoncalves.phonebook.backend.domain.ContactData;
import com.brunogoncalves.phonebook.backend.storage.exception.ContactStorageException;
import com.brunogoncalves.phonebook.backend.storage.exception.CouldNotUpdateContactException;
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

public class ContactStorageMysqlContactUpdateTest {
    @Mocked
    private BasicDataSource basicDataSource;

    @Mocked
    private Connection connection;

    @Mocked
    private PreparedStatement preparedStatement;

    @Mocked
    private ResultSet resultSet;

    private ContactStorageMysql storageMysql;

    private static String UPDATE_STATEMENT = "" +
            "UPDATE CONTACT " +
            "SET phone_number = ?, first_name = ?,  last_name = ? " +
            "WHERE id = ?";

    @Before
    public void initMysqlStorage() throws ContactStorageException {
        storageMysql = new ContactStorageMysql(basicDataSource);
    }

    @Test
    public void testContactStorageUpdateShouldSuccessfullyCreateContact() throws ContactStorageException, SQLException, CouldNotUpdateContactException {
        final ContactData contactData = new ContactData("first", "last", "number");
        final int contactId = 1;

        expectStorageToGetADbConnection(basicDataSource, connection);
        expectStorageToPrepareStatement(connection, UPDATE_STATEMENT, preparedStatement);
        expectStorageToExecuteUpdate(preparedStatement, 1);

        storageMysql.update(contactData, contactId);

        verifySetOfArgumentsOfPreparedStatementForUpdate(contactId, contactData);
        verifyPreparedStatementIsClosed(preparedStatement);
        verifyConnectionIsClosed(connection);
    }

    @Test(expected = ContactStorageException.class)
    public void testContactStorageCreateThrowContactStorageExceptionInCaseOfExceptionGettingConnection() throws ContactStorageException, SQLException, CouldNotUpdateContactException {
        final ContactData contactData = new ContactData("first", "last", "number");
        final int contactId = 1;

        expectStorageToGetADbConnectionButThrowException(basicDataSource, 6);

        storageMysql.update(contactData, contactId);
    }


    @Test(expected = ContactStorageException.class)
    public void testContactStorageCreateThrowContactStorageExceptionInCaseOfExceptionCreatingPreparedStatement() throws ContactStorageException, SQLException, CouldNotUpdateContactException {
        final ContactData contactData = new ContactData("first", "last", "number");
        final int contactId = 1;

        expectStorageToGetADbConnection(basicDataSource, connection);
        expectStorageToPrepareStatementButThrowException(connection, UPDATE_STATEMENT);

        storageMysql.update(contactData, contactId);

        verifyConnectionIsClosed(connection);
    }

    @Test(expected = CouldNotUpdateContactException.class)
    public void testContactStorageCreateThrowsCouldNotUpdateContactExceptionIfNumberRowsAffectedIs0() throws ContactStorageException, SQLException, CouldNotUpdateContactException {
        final ContactData contactData = new ContactData("first", "last", "number");
        final int contactId = 1;

        expectStorageToGetADbConnection(basicDataSource, connection);
        expectStorageToPrepareStatement(connection, UPDATE_STATEMENT, preparedStatement);
        expectStorageToExecuteUpdate(preparedStatement, 0);

        storageMysql.update(contactData, contactId);

        verifySetOfArgumentsOfPreparedStatementForUpdate(contactId, contactData);
        verifyResultSetIsClosed(resultSet);
        verifyPreparedStatementIsClosed(preparedStatement);
        verifyConnectionIsClosed(connection);
    }

    private void verifySetOfArgumentsOfPreparedStatementForUpdate(final int contactId, final ContactData contactData) throws SQLException {
        new Verifications() {{
            preparedStatement.setString(withEqual(1), withEqual(contactData.getPhoneNumber()));
            preparedStatement.setString(withEqual(2), withEqual(contactData.getFirstName()));
            preparedStatement.setString(withEqual(3), withEqual(contactData.getLastName()));
            preparedStatement.setInt(withEqual(4), withEqual(contactId));
        }};
    }
}
