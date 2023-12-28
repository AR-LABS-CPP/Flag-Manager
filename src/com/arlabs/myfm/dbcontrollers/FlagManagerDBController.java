package com.arlabs.myfm.dbcontrollers;

import com.arlabs.myfm.utils.Database;
import com.arlabs.myfm.utils.NativeLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author AR-LABS
 */
public class FlagManagerDBController {

    private Connection dbConnection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String sqlStatement;

    public FlagManagerDBController() {
        try {
            dbConnection = Database.getConnection();
            NativeLogger.setClassName(this.getClass());

            NativeLogger.logInfo("[JDBC]: Connected with the database");
        } catch (SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Error connecting with the database");
        }
    }

    public Map<Integer, List<Object>> getAllFlags(String projectId) {
        try {
            Map<Integer, List<Object>> allFlags = new HashMap<>();

            sqlStatement = "SELECT * FROM flags WHERE project_id = ? "
                    + "ORDER BY flag_id ASC";
            preparedStatement = dbConnection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, Integer.parseInt(projectId));
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                allFlags.put(Integer.valueOf(resultSet.getObject(1).toString()),
                        List.of(
                                resultSet.getObject(2),
                                resultSet.getObject(3),
                                resultSet.getObject(4),
                                resultSet.getObject(5)
                        ));
            }
            
            NativeLogger.logInfo("[JDBC]: Retrieved all flags successfully");

            return allFlags;
        } catch (SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Failed to get all flags, "
                    + ex.getMessage());
        }

        return null;
    }

    public Map<Integer, List<Object>> getAllWebhooks(String projectId) {
        try {
            Map<Integer, List<Object>> allWebhooks = new HashMap<>();

            sqlStatement = "SELECT * FROM webhooks WHERE project_id = ? "
                    + "ORDER BY webhook_id ASC";
            preparedStatement = dbConnection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, Integer.parseInt(projectId));
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                allWebhooks.put(Integer.valueOf(resultSet.getObject(1).toString()),
                        List.of(
                                resultSet.getObject(2),
                                resultSet.getObject(3),
                                resultSet.getObject(4),
                                resultSet.getObject(5)
                        ));
            }
            
            NativeLogger.logInfo("[JDBC]: Retrieved all webhooks successfully");

            return allWebhooks;
        } catch (SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Failed to fetch webhooks, "
                    + ex.getMessage());
        }

        return null;
    }

    public int addNewFlag(String projectId, String flagName, String flagValue) {
        int flagId = -1;
        
        try {
            sqlStatement = "INSERT INTO flags (flag_name, flag_value, "
                    + "project_id) VALUES (?, ?, ?)";
            preparedStatement = dbConnection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, flagName);
            preparedStatement.setString(2, flagValue);
            preparedStatement.setInt(3, Integer.parseInt(projectId));
            preparedStatement.executeUpdate();
            
            resultSet = preparedStatement.getGeneratedKeys();
            
            while(resultSet.next()) {
                flagId = resultSet.getInt(1);
            }
            
            NativeLogger.logInfo("[JDBC]: New flag added successfully");
        } catch (SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Failed to add new flag, "
                    + ex.getMessage());
        }
        
        return flagId;
    }

    public boolean editFlag(String flagId, String flagName, String flagValue) {
        try {
            sqlStatement = "UPDATE flags SET flag_name = ?, flag_value "
                    + "= ? WHERE flag_id = ?";
            preparedStatement = dbConnection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, flagName);
            preparedStatement.setString(2, flagValue);
            preparedStatement.setInt(3, Integer.parseInt(flagId));
            preparedStatement.executeUpdate();
            
            NativeLogger.logInfo("[JDBC]: Flag edited successfully");

            return true;
        } catch (SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Failed to edit the flag, "
                    + ex.getMessage());

            return false;
        }
    }

    public boolean deleteFlag(String flagId) {
        try {
            sqlStatement = "DELETE FROM flags WHERE flag_id = ?";
            preparedStatement = dbConnection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, Integer.parseInt(flagId));
            preparedStatement.executeUpdate();
            
            NativeLogger.logInfo("[JDBC]: Flag deleted successfully");

            return true;
        } catch (SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Failed to delete the flag, "
                    + ex.getMessage());

            return false;
        }
    }

    public int addWebhook(String projectId, String webhookName, String webhookValue) {
        int webhookId = -1;
        
        try {
            sqlStatement = "INSERT INTO webhooks (project_id, webhook_name, "
                    + "webhook_value) VALUES (?, ?, ?)";
            preparedStatement = dbConnection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, Integer.parseInt(projectId));
            preparedStatement.setString(2, webhookName);
            preparedStatement.setString(3, webhookValue);
            preparedStatement.executeUpdate();
            
            resultSet = preparedStatement.getGeneratedKeys();
            
            while(resultSet.next()) {
                webhookId = resultSet.getInt(1);
            }
            
            NativeLogger.logInfo("[JDBC]: New webhook added successfully");
        } catch (SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Failed to add new webhook, "
                    + ex.getMessage());
        }
        
        return webhookId;
    }

    public boolean editWebhook(String webhookId, String webhookName, String webhookValue) {
        try {
            sqlStatement = "UPDATE webhooks SET webhook_name = ?, "
                    + "webhook_value = ? WHERE webhook_id = ?";
            preparedStatement = dbConnection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, webhookName);
            preparedStatement.setString(2, webhookValue);
            preparedStatement.setInt(3, Integer.parseInt(webhookId));
            preparedStatement.executeUpdate();
            
            NativeLogger.logInfo("[JDBC]: Webhook edited successfully");

            return true;
        } catch (SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Failed to edit webhook, "
                    + ex.getMessage());

            return false;
        }
    }

    public boolean deleteWebhook(String webhookId) {
        try {
            sqlStatement = "DELETE FROM webhooks WHERE webhook_id = ?";
            preparedStatement = dbConnection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, Integer.parseInt(webhookId));
            preparedStatement.executeUpdate();
            
            NativeLogger.logInfo("[JDBC]: Webhook deleted successfully");

            return true;
        } catch (SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Failed to delete the webhook, "
                    + ex.getMessage());

            return false;
        }
    }

    public void closeDbConnection() {
        Database.disconnect();
    }
}
