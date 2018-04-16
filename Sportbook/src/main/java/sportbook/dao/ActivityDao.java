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
 *
 * @author minna
 */
public class ActivityDao {

    private Database database;
    
    public ActivityDao(Database database) {
        this.database = database;
    }
    
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
    
    public void delete(Activity toBeDeleted) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Activity WHERE id = ?");
        stmt.setInt(1, toBeDeleted.getId());
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }
}
