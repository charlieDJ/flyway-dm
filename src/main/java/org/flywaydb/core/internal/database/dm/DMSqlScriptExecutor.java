package org.flywaydb.core.internal.database.dm;

import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.flywaydb.core.internal.sqlscript.DefaultSqlScriptExecutor;

public class DMSqlScriptExecutor extends DefaultSqlScriptExecutor {


    public DMSqlScriptExecutor(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }


}
