//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.flywaydb.core.internal.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.flywaydb.core.api.FlywayException;

public enum DatabaseType {
    COCKROACHDB("CockroachDB", 0, false),
    DB2("DB2", 12, true),
    DERBY("Derby", 12, true),
    FIREBIRD("Firebird", 0, true),
    H2("H2", 12, true),
    HSQLDB("HSQLDB", 12, true),
    INFORMIX("Informix", 12, true),
    MARIADB("MariaDB", 12, true),
    MYSQL("MySQL", 12, true),
    ORACLE("Oracle", 12, true),
    POSTGRESQL("PostgreSQL", 0, true),
    REDSHIFT("Redshift", 12, true),
    SQLITE("SQLite", 12, false),
    SQLSERVER("SQL Server", 12, true),
    SYBASEASE_JTDS("Sybase ASE", 0, true),
    SYBASEASE_JCONNECT("Sybase ASE", 12, true),
    SAPHANA("SAP HANA", 12, true),
    SNOWFLAKE("Snowflake", 12, false),
    DM("DM", 12, false);


    private final String name;
    private final int nullType;
    private final boolean supportsReadOnlyTransactions;

    private DatabaseType(String name, int nullType, boolean supportsReadOnlyTransactions) {
        this.name = name;
        this.nullType = nullType;
        this.supportsReadOnlyTransactions = supportsReadOnlyTransactions;
    }

    public static DatabaseType fromJdbcConnection(Connection connection) {
        DatabaseMetaData databaseMetaData = JdbcUtils.getDatabaseMetaData(connection);
        String databaseProductName = JdbcUtils.getDatabaseProductName(databaseMetaData);
        String databaseProductVersion = JdbcUtils.getDatabaseProductVersion(databaseMetaData);
        return fromDatabaseProductNameAndVersion(databaseProductName, databaseProductVersion, connection);
    }

    private static DatabaseType fromDatabaseProductNameAndVersion(String databaseProductName, String databaseProductVersion, Connection connection) {
        if (databaseProductName.startsWith("Apache Derby")) {
            return DERBY;
        } else if (databaseProductName.startsWith("SQLite")) {
            return SQLITE;
        } else if (databaseProductName.startsWith("H2")) {
            return H2;
        } else if (databaseProductName.contains("HSQL Database Engine")) {
            return HSQLDB;
        } else if (databaseProductName.startsWith("Microsoft SQL Server")) {
            return SQLSERVER;
        } else if (!databaseProductName.startsWith("MariaDB") && (!databaseProductName.contains("MySQL") || !databaseProductVersion.contains("MariaDB")) && (!databaseProductName.contains("MySQL") || !getSelectVersionOutput(connection).contains("MariaDB"))) {
            if (databaseProductName.contains("MySQL")) {
                return MYSQL;
            } else if (databaseProductName.startsWith("Oracle")) {
                return ORACLE;
            } else if (databaseProductName.startsWith("PostgreSQL")) {
                String selectVersionQueryOutput = getSelectVersionOutput(connection);
                if (databaseProductName.startsWith("PostgreSQL 8") && selectVersionQueryOutput.contains("Redshift")) {
                    return REDSHIFT;
                } else {
                    return selectVersionQueryOutput.contains("CockroachDB") ? COCKROACHDB : POSTGRESQL;
                }
            } else if (databaseProductName.startsWith("DB2")) {
                return DB2;
            } else if (databaseProductName.startsWith("ASE")) {
                return SYBASEASE_JTDS;
            } else if (databaseProductName.startsWith("Adaptive Server Enterprise")) {
                return SYBASEASE_JCONNECT;
            } else if (databaseProductName.startsWith("HDB")) {
                return SAPHANA;
            } else if (databaseProductName.startsWith("Informix")) {
                return INFORMIX;
            } else if (databaseProductName.startsWith("Firebird")) {
                return FIREBIRD;
            } else if (databaseProductName.startsWith("Snowflake")) {
                return SNOWFLAKE;
            } else if (databaseProductName.startsWith("DM")) {
                return DM;
            } else {
                throw new FlywayException("Unsupported Database: " + databaseProductName);
            }
        } else {
            return MARIADB;
        }
    }

    public static String getSelectVersionOutput(Connection connection) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String var5;
        try {
            statement = connection.prepareStatement("SELECT version()");
            resultSet = statement.executeQuery();
            String result = null;
            if (resultSet.next()) {
                result = resultSet.getString(1);
            }

            return result;
        } catch (SQLException var9) {
            var5 = "";
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
        }

        return var5;
    }

    public String getName() {
        return this.name;
    }

    public int getNullType() {
        return this.nullType;
    }

    public String toString() {
        return this.name;
    }
}
