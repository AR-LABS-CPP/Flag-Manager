package com.arlabs.myfm.dbcontrollers;

import com.arlabs.myfm.utils.Database;
import com.arlabs.myfm.utils.NativeLogger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AR-LABS
 */
public class ProjectSelectionDBController {
    private Connection dbConnection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String sqlStatement;
    
    public ProjectSelectionDBController() {
        try {
            dbConnection = Database.getConnection();
            NativeLogger.setClassName(this.getClass());
            
            NativeLogger.logInfo("[JDBC]: Connected with the database");
        } catch (SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Error connecting with the database");
        }
    }
    
    public int addNewProject(String projectName) {
        int projectId = -1;
        
        try {
            sqlStatement = "INSERT INTO projects (project_name) VALUES (?)";
            preparedStatement = dbConnection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, projectName);
            preparedStatement.executeUpdate();
            
            resultSet = preparedStatement.getGeneratedKeys();
            
            if(resultSet.next()) {
                System.out.println(resultSet.getInt(1));
                projectId = resultSet.getInt(1);
            }
        }
        catch(SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Failed to add new project, "
                    + ex.getMessage());
            
        }
        
        return projectId;
    }
    
    public boolean deleteProject(String projectId) {
        try {
            sqlStatement = "DELETE FROM projects WHERE project_id = ?";
            preparedStatement = dbConnection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, Integer.parseInt(projectId));
            preparedStatement.executeUpdate();
        }
        catch(SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Failed to delete the project, " + ex.getMessage());
            
            return false;
        }
        
        return true;
    }
        
    public List<List<Object>> getProjectsWithFlagAndWebhookCount() {
        List<List<Object>> projects = new ArrayList<>();
        
        try {
            sqlStatement = "SELECT p.project_id, p.project_name, "
                    + "p.creation_date, COUNT(DISTINCT f.flag_id) as flag_count, "
                    + "COUNT(DISTINCT w.webhook_id) as webhook_count "
                    + "FROM projects p LEFT JOIN flags f ON p.project_id = "
                    + "f.project_id LEFT JOIN webhooks w ON p.project_id = "
                    + "w.project_id GROUP BY p.project_id, p.project_name,"
                    + "p.creation_date ORDER BY p.project_id ASC";
            preparedStatement = dbConnection.prepareStatement(sqlStatement);
            resultSet = preparedStatement.executeQuery();
                        
            while(resultSet.next()) {
                projects.add(List.of(
                    resultSet.getObject(1),
                    resultSet.getObject(2),
                    resultSet.getObject(3),
                    resultSet.getObject(4),
                    resultSet.getObject(5)
                ));
            }
        }
        catch(SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Failed to get list of project, "
                    + ex.getMessage());
        }
        
        return projects;
    }
    
    public String getProjectName(String projectId) {
        try {
            String projectName = "";
            
            sqlStatement = "SELECT project_name FROM projects WHERE "
                    + "project_id = ?";
            preparedStatement = dbConnection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, Integer.parseInt(projectId));
            resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()) {
                projectName = resultSet.getString("project_name");
            }
            
            return projectName;
        }
        catch(SQLException ex) {
            NativeLogger.logInfo("[JDBC]: Failed to get list of project, "
                    + ex.getMessage());
        }
        
        return null;
    }
    
    public void closeDbConnection() {
        Database.disconnect();
    }
}
