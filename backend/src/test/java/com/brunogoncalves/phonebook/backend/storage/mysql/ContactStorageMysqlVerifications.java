package com.brunogoncalves.phonebook.backend.storage.mysql;

import mockit.Verifications;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactStorageMysqlVerifications {

    static void verifyResultSetIsClosed(final ResultSet resultSet) throws SQLException {
        new Verifications() {{
            resultSet.close();
        }};
    }

    static void verifyPreparedStatementIsClosed(final PreparedStatement preparedStatement) throws SQLException {
        new Verifications() {{
            preparedStatement.close();
        }};
    }

    static void verifyConnectionIsClosed(final Connection connection) throws SQLException {
        new Verifications() {{
            connection.close();
        }};
    }
}
