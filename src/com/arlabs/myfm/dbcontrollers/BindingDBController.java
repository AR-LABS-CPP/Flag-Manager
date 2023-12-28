package com.arlabs.myfm.dbcontrollers;

import com.arlabs.myfm.utils.Database;
import com.arlabs.myfm.utils.NativeLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author AR-LABS
 */
public class BindingDBController {
    private Connection dbConnection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String sqlStatement;
    
    public BindingDBController() {
        try {
            dbConnection = Database.getConnection();
            NativeLogger.setClassName(this.getClass());
            
            NativeLogger.logInfo("[JDBC]: Connected with the database");
        } catch (SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Error connecting with the database");
        }
    }
    
    public Map<Object, List<Object>> getAllBindings(String projectId) {
        try {
            Map<Object, List<Object>> bindingsGroupedByWebook = new HashMap<>();
            
            sqlStatement = "SELECT * FROM bindings WHERE project_id = ? "
                    + "ORDER BY binding_id ASC";
            preparedStatement = dbConnection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, Integer.parseInt(projectId));
            resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()) {
                Object webHook = resultSet.getObject("webhook_id");
                
                if(!bindingsGroupedByWebook.containsKey(webHook)) {
                    bindingsGroupedByWebook.put(webHook, new ArrayList<>());
                }
                
                bindingsGroupedByWebook.get(webHook).add(resultSet.getObject("flag_id"));
            }
            
            NativeLogger.logInfo("[JDBC]: Retrieved all bindings successfully");
                        
            return bindingsGroupedByWebook;
        }
        catch(SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Failed to get bindings, "
                    + ex.getMessage());
            
            return null;
        }
    }
    
    public boolean addSingleBinding(int webhookId, int flagId) {
        return false;
    }
    
    public boolean addMultipleBindings(int projectId, int webhookId, List<Integer> flagIds) {
        try {
            sqlStatement = "INSERT INTO bindings (project_id, webhook_id, flag_id) "
                    + "VALUES (?, ?, ?)";
            preparedStatement = dbConnection.prepareStatement(sqlStatement);
            
            List<Object[]> data = new ArrayList<>();
            
            for(int flagId : flagIds) {
                data.add(new Object[] { projectId, webhookId, flagId });
            }
            
            dbConnection.setAutoCommit(false);
            
            for(Object[] row : data) {
                for(int idx = 0; idx < row.length; idx++) {
                    preparedStatement.setObject(idx + 1, row[idx]);
                }
                
                preparedStatement.addBatch();
            }
            
            int[] updateCounts = preparedStatement.executeBatch();
            
            dbConnection.commit();
            
            NativeLogger.logInfo("[JDBC]: Multiple bindings added successfully");
            
            return updateCounts.length == flagIds.size();
        }
        catch(SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Failed to add bindings, "
                    + ex.getMessage());
            
            try {
                dbConnection.rollback();
            } catch (SQLException ex1) {
                NativeLogger.logInfo("[JDBC]: Failed to rollback transaction, "
                    + ex.getMessage());
            }
            
            return false;
        }
    }
    
    public boolean deleteMultipleBindings(int projectId, int webhookId, List<Integer> flagIds) {
        try {
            sqlStatement = "DELETE FROM bindings WHERE project_id = ? AND webhook_id = ? AND flag_id = ?";
            preparedStatement = dbConnection.prepareStatement(sqlStatement);
            
            List<Object[]> data = new ArrayList<>();
            
            for(int flagId : flagIds) {
                data.add(new Object[] { projectId, webhookId, flagId });
            }
            
            dbConnection.setAutoCommit(false);
            
            for(Object[] row : data) {
                for(int idx = 0; idx < row.length; idx++) {
                    preparedStatement.setObject(idx + 1, row[idx]);
                }
                
                preparedStatement.addBatch();
            }
            
            int[] updateCounts = preparedStatement.executeBatch();
            
            dbConnection.commit();
            
            NativeLogger.logInfo("[JDBC]: Multiple bindings deleted successfully");
            
            return updateCounts.length == flagIds.size();
        }
        catch(SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Failed to delete bindings, "
                    + ex.getMessage());
            
            try {
                dbConnection.rollback();
            } catch (SQLException ex1) {
                NativeLogger.logInfo("[JDBC]: Failed to rollback transaction, "
                    + ex.getMessage());
            }
            
            return false;
        }
    }
    
    public void closeDbConnection() {
        Database.disconnect();
    }
}
