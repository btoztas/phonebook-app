package com.brunogoncalves.phonebook.persistence.mysql;

import com.brunogoncalves.phonebook.domain.Contact;
import com.brunogoncalves.phonebook.persistence.ContactPersistence;
import com.brunogoncalves.phonebook.persistence.ContactPersistenceException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class ContactPersistenceMysql implements ContactPersistence {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactPersistenceMysql.class);

    private static final String INSERT_PREPARED_STATEMENT = "INSERT INTO CONTACT(phone_number, first_name, last_name) VALUES(?, ?, ?)";

    private static final String UPDATE_PREPARED_STATEMENT = "UPDATE CONTACT (phone_number, first_name, last_name) VALUES(?, ?, ?)";

    private static final String DELETE_PREPARED_STATEMENT = "DELETE FROM CONTACT WHERE phone_number = ?";

    private static final String CREATE_TABLE_STATEMENT =    "CREATE TABLE CONTACT " +
                                                            "(phone_number INTEGER not NULL, " +
                                                            " first_name VARCHAR(255), " +
                                                            " last_name VARCHAR(255), " +
                                                            " PRIMARY KEY (phone_number))";
    private final BasicDataSource dataSource;


    //TODO: Somewhere here we will need a DB Conn Pool
    public ContactPersistenceMysql(final String dbUser, final String dbPass, final String jdbcString) throws ContactPersistenceException {
        this(initializeDataSource(dbUser, dbPass, jdbcString));
    }

    public ContactPersistenceMysql(final BasicDataSource dataSource) throws ContactPersistenceException {
        this.dataSource = dataSource;
        assureContactTableIsCreated();
    }

    @Override
    public Contact create(final Contact contact) {
        LOGGER.info("Creating {}", contact);
        return contact;
    }

    @Override
    public Optional<Contact> getByNumber(final String number) {
        LOGGER.info("Getting {}", number);
        return Optional.empty();
    }

    @Override
    public List<Contact> getByName(final String name) {
        return null;
    }

    @Override
    public Contact update(final Contact contact) {
        return contact;
    }

    @Override
    public void delete(final String number) {

    }

    private Connection getDbConnection(final String errorMessage) throws ContactPersistenceException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.error(errorMessage, e);
            throw new ContactPersistenceException(e);
        }
    }

    private static BasicDataSource initializeDataSource(final String dbUser, final String dbPass, final String jdbcString) {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(JDBC_DRIVER);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPass);
        dataSource.setUrl(jdbcString);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setPoolPreparedStatements(true);
        return dataSource;
    }

    private void assureContactTableIsCreated() throws ContactPersistenceException {
        final Connection connection = getDbConnection("Could not get a DB Connection to assure Contact Table is created");
        createContactTable(connection);
    }

    private void createContactTable(final Connection connection) {
        try {
            final Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE_STATEMENT);
            LOGGER.info("Contact table successfully created");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
