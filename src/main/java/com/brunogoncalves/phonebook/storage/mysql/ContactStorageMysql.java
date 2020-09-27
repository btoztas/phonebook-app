package com.brunogoncalves.phonebook.storage.mysql;

import com.brunogoncalves.phonebook.domain.Contact;
import com.brunogoncalves.phonebook.storage.ContactStorage;
import com.brunogoncalves.phonebook.storage.ContactStorageException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactStorageMysql implements ContactStorage {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactStorageMysql.class);

    private static final String CREATE_TABLE_STATEMENT = "CREATE TABLE CONTACT " +
                                                         "(phone_number VARCHAR(255) not NULL, " +
                                                         " first_name   VARCHAR(255) not NULL, " +
                                                         " last_name    VARCHAR(255) not NULL, " +
                                                         " PRIMARY KEY (phone_number)) ";

    private static final String INSERT_STATEMENT = "INSERT INTO CONTACT(phone_number, first_name, last_name) " +
                                                   "            VALUES(?, ?, ?) ";

    private static final String SEARCH_STATEMENT = "SELECT phone_number, first_name, last_name" +
                                                   "FROM CONTACT " +
                                                   "WHERE phone_number = ? " +
                                                   "   OR fist_name LIKE ?" +
                                                   "   OR last_name LIKE ? ";

    private static final String UPDATE_STATEMENT = "UPDATE CONTACT " +
                                                   "SET first_name = ?, " +
                                                   "    last_name = ? " +
                                                   "WHERE phone_number = '?' ";

    private static final String DELETE_STATEMENT = "DELETE FROM CONTACT " +
                                                   "WHERE phone_number = ? ";

    private static final int TABLE_ALREADY_EXISTS_ERROR_CODE = 1050;

    private final BasicDataSource dataSource;


    public ContactStorageMysql(final String dbUser, final String dbPass, final String jdbcString) throws ContactStorageException {
        this(initializeDataSource(dbUser, dbPass, jdbcString));
    }

    public ContactStorageMysql(final BasicDataSource dataSource) throws ContactStorageException {
        this.dataSource = dataSource;
        ensureContactTableIsCreated();
    }

    @Override
    public void create(final Contact contact) throws ContactStorageException {
        LOGGER.info("Creating {}", contact);
        final Connection connection = getDbConnection(String.format("insert %s", contact.toString()));
        doContactInsert(connection, contact);
    }

    @Override
    public List<Contact> searchByToken(final String token) throws ContactStorageException {
        LOGGER.info("Searching with token {}", token);
        final Connection connection = getDbConnection(String.format("search by token %s", token));
        return doContactSearch(connection, token);
    }

    @Override
    public void update(final Contact contact) throws ContactStorageException {
        LOGGER.info("Updating {}", contact);
        final Connection connection = getDbConnection(String.format("update %s", contact.toString()));
        doContactUpdate(connection, contact);
    }

    @Override
    public void delete(final String phoneNumber) throws ContactStorageException {
        LOGGER.info("Deleting contact with phone number {}", phoneNumber);
        final Connection connection = getDbConnection(String.format("delete contact with number %s", phoneNumber));
        doContactDelete(connection, phoneNumber);
    }

    private void doContactInsert(final Connection connection, final Contact contact) throws ContactStorageException {
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STATEMENT);
            preparedStatement.setString(1, contact.getFirstName());
            preparedStatement.setString(2, contact.getLastName());
            preparedStatement.setString(3, contact.getPhoneNumber());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Could not perform update for record {}", contact, e);
            throw new ContactStorageException(e);
        }
    }

    private List<Contact> doContactSearch(final Connection connection, final String token) throws ContactStorageException {
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_STATEMENT);
            preparedStatement.setString(1, token);
            preparedStatement.setString(2, '%' + token + '%');
            preparedStatement.setString(3, '%' + token + '%');
            final ResultSet resultSet = preparedStatement.executeQuery();

            final List<Contact> contacts = new LinkedList<>();
            while (resultSet.next()) {
                contacts.add(new Contact(resultSet.getString("first_name"), resultSet.getString("last_name"),
                                         resultSet.getString("phone_number")));
            }
            resultSet.close();
            preparedStatement.close();
            return contacts;
        } catch (SQLException e) {
            LOGGER.error("Could not search with token {}", token, e);
            throw new ContactStorageException(e);
        }
    }

    private void doContactUpdate(final Connection connection, final Contact contact) throws ContactStorageException {
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STATEMENT);
            preparedStatement.setString(1, contact.getPhoneNumber());
            preparedStatement.setString(2, contact.getFirstName());
            preparedStatement.setString(3, contact.getLastName());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Could not update record {}", contact, e);
            throw new ContactStorageException(e);
        }
    }

    private void doContactDelete(final Connection connection, final String phoneNumber) throws ContactStorageException {
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STATEMENT);
            preparedStatement.setString(1, phoneNumber);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Could not delete record with phone number {}", phoneNumber, e);
            throw new ContactStorageException(e);
        }
    }

    private Connection getDbConnection(final String operationDescription) throws ContactStorageException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.error("Could not get a DB Connection to {}", operationDescription, e);
            throw new ContactStorageException(e);
        }
    }

    private void ensureContactTableIsCreated() throws ContactStorageException {
        final Connection connection = getDbConnection("Contact Table creation");
        createContactTable(connection);
    }


    private void createContactTable(final Connection connection) throws ContactStorageException {
        try {
            final Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE_STATEMENT);
            LOGGER.info("Contact table successfully created");
        } catch (final SQLException e) {
            if (e.getErrorCode() == TABLE_ALREADY_EXISTS_ERROR_CODE)
                LOGGER.info("Contact table already exists");
            else {
                LOGGER.error("Could not create contact table", e);
                throw new ContactStorageException(e);
            }
        }
    }

    private static BasicDataSource initializeDataSource(final String dbUser, final String dbPass, final String jdbcString) {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(JDBC_DRIVER);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPass);
        dataSource.setUrl(jdbcString);
        dataSource.setPoolPreparedStatements(true);
        return dataSource;
    }
}
