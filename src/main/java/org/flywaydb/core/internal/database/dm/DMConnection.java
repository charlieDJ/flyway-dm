package org.flywaydb.core.internal.database.dm;

import java.sql.SQLException;

import org.flywaydb.core.internal.database.base.Connection;
import org.flywaydb.core.internal.database.base.Schema;



public class DMConnection extends Connection<DMDatabase> {
    DMConnection(DMDatabase database, java.sql.Connection connection) {
        super(database, connection);
    }

    @Override
    protected String getCurrentSchemaNameOrSearchPath() throws SQLException {
        //获取当前连接db的用户名
        return jdbcTemplate.queryForString("SELECT SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA') FROM DUAL");
    }

    @Override
    public void doChangeCurrentSchemaOrSearchPathTo(String schema) throws SQLException {
        jdbcTemplate.execute("ALTER SESSION SET CURRENT_SCHEMA=" + database.quote(schema));
    }

    @Override
    public Schema getSchema(String name) {
        return new DMSchema(jdbcTemplate, database, name);
    }
}
