package com.arlabs.myfm.utils;

import com.arlabs.myfm.constants.DatabaseConstants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author AR-LABS
 */
public class Database {
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;
    
    static {
        NativeLogger.setClassName(Database.class);
        
        config.setJdbcUrl(DatabaseConstants.DATABASE_URL);
        config.setUsername(DatabaseConstants.USERNAME);
        config.setPassword(DatabaseConstants.PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
        
        try {
            initializeDatabase();
        }
        catch(SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Failed to initialize database, " + ex.getMessage());
        }
    }
    
    private static void initializeDatabase() throws SQLException {        
        String projectTableStatement = "CREATE TABLE IF NOT EXISTS "
                + "projects (project_id SERIAL PRIMARY KEY,"
                + "project_name VARCHAR(100) NOT NULL,"
                + "creation_date DATE DEFAULT CURRENT_DATE);";
        
        String flagTableStatement = "CREATE TABLE IF NOT EXISTS "
                + "flags (flag_id SERIAL PRIMARY KEY,"
                + "flag_name VARCHAR(100) NOT NULL,"
                + "flag_value VARCHAR(255) NOT NULL,"
                + "project_id INTEGER REFERENCES projects(project_id) ON DELETE CASCADE,"
                + "creation_date DATE DEFAULT CURRENT_DATE);";
        
        String webhookTableStatement = "CREATE TABLE IF NOT EXISTS "
                + "webhooks (webhook_id SERIAL PRIMARY KEY,"
                + "webhook_name VARCHAR(100) NOT NULL,"
                + "webhook_value VARCHAR(255) NOT NULL,"
                + "project_id INTEGER REFERENCES projects(project_id) ON DELETE CASCADE,"
                + "creation_date DATE DEFAULT CURRENT_DATE);";
        
        String bindingTableStatement = "CREATE TABLE IF NOT EXISTS "
                + "bindings (binding_id SERIAL PRIMARY KEY,"
                + "project_id INTEGER REFERENCES projects(project_id) ON DELETE CASCADE,"
                + "webhook_id INTEGER REFERENCES webhooks(webhook_id) ON DELETE CASCADE,"
                + "flag_id INTEGER REFERENCES flags(flag_id) ON DELETE CASCADE,"
                + "creation_date DATE DEFAULT CURRENT_DATE);";
        
        try(Statement stmt = ds.getConnection().createStatement()) {
            stmt.executeUpdate(projectTableStatement);
            stmt.executeUpdate(flagTableStatement);
            stmt.executeUpdate(webhookTableStatement);
            stmt.executeUpdate(bindingTableStatement);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    
    public static void disconnect() {
        if(ds.isRunning()) {
            ds.close();
            NativeLogger.logInfo("[JDBC]: Database connection closed");
        }
    }
}
