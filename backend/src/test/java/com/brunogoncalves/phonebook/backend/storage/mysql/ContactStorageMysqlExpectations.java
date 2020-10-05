package com.brunogoncalves.phonebook.backend.storage.mysql;

import mockit.Expectations;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Contains a set of helper static methods for the {@link ContactStorageMysql} tests.
 */
public class ContactStorageMysqlExpectations {

    static void expectStorageToGetADbConnection(final DataSource dataSource, final Connection connection) throws SQLException {
        new Expectations() {{
            times = 1;
            dataSource.getConnection();
            result = connection;
            times = 1;
        }};
    }

    static void expectStorageToGetADbConnectionButThrowException(final DataSource dataSource) throws SQLException {
        new Expectations() {{
            dataSource.getConnection();
            result = new SQLException();
            times = 1;
        }};
    }

    static void expectStorageToCreateStatement(final Connection connection, final Statement statement) throws SQLException {
        new Expectations() {{
            connection.createStatement();
            result = statement;
            times = 1;
        }};
    }

    static void expectStorageToCreateStatementButThrowException(final Connection connection) throws SQLException {
        new Expectations() {{
            connection.createStatement();
            result = new SQLException();
            times = 1;
        }};
    }

    static void expectStorageToPrepareStatement(final Connection connection, final String sql, final PreparedStatement preparedStatement) throws SQLException {
        new Expectations() {{
            connection.prepareStatement(withEqual(sql));
            result = preparedStatement;
            times = 1;
        }};
    }

    static void expectStorageToPrepareStatementButThrowException(final Connection connection, final String sql) throws SQLException {
        new Expectations() {{
            connection.prepareStatement(withEqual(sql));
            result = new SQLException();
            times = 1;
        }};
    }

    static void expectStorageToPrepareStatementWithGeneratedKeys(final Connection connection, final String sql, final PreparedStatement preparedStatement) throws SQLException {
        new Expectations() {{
            connection.prepareStatement(withEqual(sql), Statement.RETURN_GENERATED_KEYS);
            result = preparedStatement;
            times = 1;
        }};
    }

    static void expectStorageToPrepareStatementWithGeneratedKeysButThrowException(final Connection connection, final String sql, final PreparedStatement preparedStatement) throws SQLException {
        new Expectations() {{
            connection.prepareStatement(withEqual(sql), Statement.RETURN_GENERATED_KEYS);
            result = new SQLException();
            times = 1;
        }};
    }

    static void expectStorageToExecuteQuery(final PreparedStatement preparedStatement, final ResultSet resultSet) throws SQLException {
        new Expectations() {{
            preparedStatement.executeQuery();
            result = resultSet;
            times = 1;
        }};
    }

    static void expectStorageToExecuteUpdate(final PreparedStatement preparedStatement, final int rowsAffected) throws SQLException {
        new Expectations() {{
            preparedStatement.executeUpdate();
            result = rowsAffected;
            times = 1;
        }};
    }

    static void expectStorageToExecuteStatementButReturnException(final Statement statement, final String sql, final SQLException exception) throws SQLException {
        new Expectations() {{
            statement.execute(withEqual(sql));
            result = exception;
            times = 1;
        }};
    }

    static void expectStorageToGetGeneratedKeys(final PreparedStatement preparedStatement, final ResultSet resultSet) throws SQLException {
        new Expectations() {{
            preparedStatement.getGeneratedKeys();
            result = resultSet;
            times = 1;
        }};
    }
}
