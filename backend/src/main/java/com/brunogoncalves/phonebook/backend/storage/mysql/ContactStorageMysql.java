package com.brunogoncalves.phonebook.backend.storage.mysql;

import com.brunogoncalves.phonebook.backend.domain.Contact;
import com.brunogoncalves.phonebook.backend.domain.ContactData;
import com.brunogoncalves.phonebook.backend.storage.ContactStorage;
import com.brunogoncalves.phonebook.backend.storage.exception.*;

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

/**
 * An implementation of {@link ContactStorage} for MySQL.
 */
public class ContactStorageMysql implements ContactStorage {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactStorageMysql.class);

    private static final String CREATE_TABLE_STATEMENT = "" +
            "CREATE TABLE CONTACT " +
            "(id           int          not NULL AUTO_INCREMENT, " +
            " phone_number VARCHAR(255) not NULL, " +
            " first_name   VARCHAR(255) not NULL, " +
            " last_name    VARCHAR(255) not NULL, " +
            " PRIMARY KEY (id)) ";

    private static final String INSERT_STATEMENT = "" +
            "INSERT INTO CONTACT(phone_number, first_name, last_name) " +
            "VALUES(?, ?, ?) ";

    private static final String SEARCH_STATEMENT = "" +
            "SELECT id, phone_number, first_name, last_name " +
            "FROM CONTACT " +
            "WHERE phone_number LIKE ? " +
            "OR first_name LIKE ? " +
            "OR last_name LIKE ? ";

    private static final String GET_BY_ID_STATEMENT = "" +
            "SELECT id, phone_number, first_name, last_name " +
            "FROM CONTACT " +
            "WHERE id = ? ";

    private static final String UPDATE_STATEMENT = "" +
            "UPDATE CONTACT " +
            "SET phone_number = ?, first_name = ?,  last_name = ? " +
            "WHERE id = ?";

    private static final int TABLE_ALREADY_EXISTS_ERROR_CODE = 1050;

    private static final int RETRIES_ON_DB_CONNECTION = 5;

    private final BasicDataSource dataSource;

    public ContactStorageMysql(final String dbUser, final String dbPass, final String jdbcString) throws ContactStorageException {
        this(initializeDataSource(dbUser, dbPass, jdbcString));
    }

    /**
     * A constructor for this class that allows to pass an instance of {@link BasicDataSource}. This is useful for unit
     * tests.
     *
     * @param dataSource the {@link BasicDataSource} to use.
     * @throws ContactStorageException in case of issues creating the contacts table.
     */
    public ContactStorageMysql(final BasicDataSource dataSource) throws ContactStorageException {
        this.dataSource = dataSource;
        ensureContactTableIsCreated();
    }

    @Override
    public Contact create(final ContactData contactData) throws ContactStorageException, CouldNotInsertContactException {
        LOGGER.info("Creating {}", contactData);
        try (final Connection connection = getDbConnection(String.format("insert %s", contactData.toString()));
             final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {
            bindPreparedStatementsForInsert(preparedStatement, contactData);
            return executeInsertAndReturnContact(preparedStatement, contactData);
        } catch (SQLException e) {
            LOGGER.error("Could not perform insert for record {}", contactData, e);
            throw new ContactStorageException(e);
        }
    }

    private void bindPreparedStatementsForInsert(final PreparedStatement preparedStatement, final ContactData contactData) throws SQLException {
        preparedStatement.setString(1, contactData.getPhoneNumber());
        preparedStatement.setString(2, contactData.getFirstName());
        preparedStatement.setString(3, contactData.getLastName());
    }

    private Contact executeInsertAndReturnContact(final PreparedStatement preparedStatement, final ContactData contactData) throws SQLException, CouldNotInsertContactException {
        final int rowsAffected = preparedStatement.executeUpdate();
        try (final ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
            if (rowsAffected == 1 && resultSet.next())
                return new Contact(resultSet.getInt(1), contactData);

            throw new CouldNotInsertContactException();
        }
    }

    @Override
    public Contact get(final int contactId) throws ContactStorageException, CouldNotGetContactException {
        LOGGER.info("Getting contact with Id {}", contactId);
        try (final Connection connection = getDbConnection(String.format("getting by id %s", contactId));
             final PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID_STATEMENT)) {
            bindPreparedStatementsForGetById(preparedStatement, contactId);
            return executeGetByIdAndReturnContact(preparedStatement);
        } catch (SQLException e) {
            LOGGER.error("Could not perform get by id {}", contactId, e);
            throw new ContactStorageException(e);
        }
    }

    private Contact executeGetByIdAndReturnContact(final PreparedStatement preparedStatement) throws SQLException, CouldNotGetContactException {
        try (final ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next())
                return new Contact(resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("phone_number"));

            throw new CouldNotGetContactException();
        }
    }

    private void bindPreparedStatementsForGetById(final PreparedStatement preparedStatement, final int contactId) throws SQLException {
        preparedStatement.setInt(1, contactId);
    }

    @Override
    public List<Contact> searchByToken(final String token) throws ContactStorageException {
        LOGGER.info("Searching with token {}", token);
        try (final Connection connection = getDbConnection(String.format("search by token %s", token));
             final PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_STATEMENT)) {
            bindPreparedStatementsForSearchWithToken(preparedStatement, token);
            return executeSearchAndReturnContacts(preparedStatement);
        } catch (SQLException e) {
            LOGGER.error("Could not perform search for token {}", token, e);
            throw new ContactStorageException(e);
        }
    }

    private List<Contact> executeSearchAndReturnContacts(PreparedStatement preparedStatement) throws SQLException {
        try (final ResultSet resultSet = preparedStatement.executeQuery()) {
            final List<Contact> contacts = new LinkedList<>();
            while (resultSet.next()) {
                contacts.add(
                        new Contact(resultSet.getInt("id"),
                                resultSet.getString("first_name"),
                                resultSet.getString("last_name"),
                                resultSet.getString("phone_number")));
            }
            return contacts;
        }
    }

    private void bindPreparedStatementsForSearchWithToken(final PreparedStatement preparedStatement, final String token) throws SQLException {
        preparedStatement.setString(1, '%' + token + '%');
        preparedStatement.setString(2, '%' + token + '%');
        preparedStatement.setString(3, '%' + token + '%');
    }

    @Override
    public void update(final ContactData contactData, final int contactId) throws ContactStorageException, CouldNotUpdateContactException {
        LOGGER.info("Updating contactId {} with contactData {}", contactId, contactData);
        try (final Connection connection = getDbConnection(String.format("update %s with %s", contactId, contactData.toString()));
             final PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STATEMENT)) {
            bindPreparedStatementsForUpdate(preparedStatement, contactId, contactData);
            executeUpdate(preparedStatement);
        } catch (SQLException e) {
            LOGGER.error("Could not perform update of contactId {} with data {}", contactId, contactData, e);
            throw new ContactStorageException(e);
        }
    }

    private void bindPreparedStatementsForUpdate(final PreparedStatement preparedStatement, final int contactId, final ContactData contactData) throws SQLException {
        preparedStatement.setString(1, contactData.getPhoneNumber());
        preparedStatement.setString(2, contactData.getFirstName());
        preparedStatement.setString(3, contactData.getLastName());
        preparedStatement.setInt(4, contactId);
    }

    private void executeUpdate(final PreparedStatement preparedStatement) throws SQLException, CouldNotUpdateContactException {
        final int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected == 0)
            throw new CouldNotUpdateContactException();
    }

    private Connection getDbConnection(final String operationDescription) throws ContactStorageException {
        int retries = 0;
        while (true) {
            try {
                return dataSource.getConnection();
            } catch (final SQLException exception) {
                retries++;
                if (retries <= RETRIES_ON_DB_CONNECTION) {
                    LOGGER.warn("Failed getting a connection from the pool for {}. This was attempt #{}. Retrying...",
                            operationDescription, retries);
                    try {
                        Thread.sleep(750);
                    } catch (InterruptedException e) {
                        LOGGER.warn("Got InterruptedException while waiting for a DB Connection for operation {}",
                                operationDescription, e);
                        Thread.currentThread().interrupt();
                    }
                }
                else
                    throw new ContactStorageException(exception);
            }
        }
    }

    private void ensureContactTableIsCreated() throws ContactStorageException {
        try (final Connection connection = getDbConnection("Contact Table creation")) {
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
