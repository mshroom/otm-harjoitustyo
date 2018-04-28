/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author minna
 */
public class Database {
    private String databaseAddress;
    
    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }
    
    public void init() {
        List<String> statements = sqlStatements();
        
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();
            for (String s : statements) {
                System.out.println("Running command >> " + s);
                st.executeUpdate(s);
            }
        } catch (Throwable t) {
            System.out.println("Error >> " + t.getMessage());        
        }        
    }

    private List<String> sqlStatements() {
        ArrayList<String> list = new ArrayList<>();
        list.add("CREATE TABLE User (id integer PRIMARY KEY, username varchar(50), password varchar(50), admin boolean);");
        list.add("CREATE TABLE Activity (id integer PRIMARY KEY, name varchar(50), unit varchar(50));");
        list.add("CREATE TABLE Action(id integer PRIMARY KEY, user_id integer, activity_id integer, time date, units integer, accomplished boolean, setAsGoal boolean, FOREIGN KEY (user_id) REFERENCES User(id), FOREIGN KEY (activity_id) REFERENCES Activity(id));");
        return list;
    }
    
}
