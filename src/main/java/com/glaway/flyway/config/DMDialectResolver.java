package com.glaway.flyway.config;

import org.springframework.data.jdbc.repository.config.DialectResolver;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;

public class DMDialectResolver implements DialectResolver.JdbcDialectProvider {

    @Override
    public Optional<Dialect> getDialect(JdbcOperations jdbcOperations) {
        return Optional.ofNullable(
                jdbcOperations.execute((ConnectionCallback<Dialect>) DMDialectResolver::getDialect)
        );
    }

    private static Dialect getDialect(Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        String name = metaData.getDatabaseProductName().toLowerCase(Locale.ROOT);
        if (name.contains("dm dbms")) {
            return DMDialect.INSTANCE;
        }
        return null;
    }
}
