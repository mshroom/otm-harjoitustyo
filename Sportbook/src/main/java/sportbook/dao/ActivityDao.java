/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sportbook.domain.Activity;

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
        
    public Activity findByToString(String toString) throws SQLException {
        List<Activity> activities = this.findAll();
        for (int i = 0; i < activities.size(); i++) {
            Activity a = activities.get(i);
            if (a.toString().equals(toString)) {
                return a;
            }
        }
        return null;
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
    
    public ObservableList<String> listActivities() throws SQLException {            
        ObservableList<String> list = FXCollections.observableArrayList();
        List<Activity> activities = this.findAll();
        activities.forEach(activity -> {
            list.add(activity.toString());            
        });
        return list;
    }
}
