/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.dao;

import sportbook.domain.Activity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * Class is responsible for storing and accessing data in the Activity table of the database.
 * 
 * @author mshroom
 */
public class ActivityDao {

    private Database database;
    
    public ActivityDao(Database database) {
        this.database = database;
    }
    
    /**
     * Method inserts a new activity to the database.
     * 
     * @param name Name of the activity, eg. "running"
     * @param unit Name of the units to be used, eg. "meters"
     * 
     * @return The created Activity object
     * 
     * @throws SQLException 
     */
    public Activity create(String name, String unit) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Activity (name, unit) VALUES (?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, unit);
        
        stmt.executeUpdate();
        stmt.close();
        connection.close();
        return this.findByNameAndUnit(name, unit);
    }
 
    /**
     * Method searches for the given activity id in the database.
     * 
     * @param id Activity id
     * 
     * @return Activity object with corresponding id or null if the activity id does not exist
     * 
     * @throws SQLException 
     */
    public Activity findOne(int id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Activity WHERE id = ?");
        stmt.setInt(1, id);
        
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        
        String name = rs.getString("name");
        String unit = rs.getString("unit");
        rs.close();
        stmt.close();
        connection.close();
        
        Activity a = new Activity(id, name, unit);
        
        return a;
    }
    
    /**
     * Method searches for an activity with the given name and unit in the database
     * 
     * @param name Name of the activity
     * @param unit Name of the unit
     * 
     * @return Activity object with corresponding name and unit or null if the activity does not exist
     * 
     * @throws SQLException 
     */
    public Activity findByNameAndUnit(String name, String unit) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Activity WHERE name = ? AND unit = ?");
        stmt.setString(1, name);
        stmt.setString(2, unit);
        
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        int id = rs.getInt("id");
        
        rs.close();
        stmt.close();
        connection.close();
        
        Activity a = new Activity(id, name, unit);
        
        return a;
    }
    
    /**
     * Method finds all saved activities in the database.
     * 
     * @return activities in a list, sorted alphabetically by name
     * 
     * @throws SQLException 
     */
    public List<Activity> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Activity ORDER BY name");
        
        ResultSet rs = stmt.executeQuery();
        List<Activity> activities = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String unit = rs.getString("unit");
            
            activities.add(new Activity(id, name, unit));
        }
        rs.close();
        stmt.close();
        connection.close();
        
        return activities;
    }
    
    /**
     * Method deleted an activity from the database.
     * 
     * @param toBeDeleted Activity object to be deleted, activity id must be correct
     * 
     * @throws SQLException 
     */
    public void delete(Activity toBeDeleted) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Activity WHERE id = ?");
        stmt.setInt(1, toBeDeleted.getId());
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }
}
