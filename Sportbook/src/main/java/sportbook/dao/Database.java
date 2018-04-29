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
 * Class is responsible for initializing and connecting to the database.
 * 
 * @author mshroom
 */
public class Database {
    private String databaseAddress;
    
    /**
     * The address of the database is defined in the constructor.
     * The address "jdbc:sqlite:databasename.db" refers to a file "databasename.db"
     * that is located in the application's root directory.
     * 
     * @param databaseAddress The address of the database as described above
     * @throws ClassNotFoundException 
     */
    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }
    
    /**
     * Method is used to connect to the database.
     * 
     * @return Connection to the database via DriverManager
     * @throws SQLException 
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }
    
    /**
     * Method initializes the database by executing necessary CREATE TABLE statements.
     * If the database already exists, no tables will be created.
     * If changes are made to the database structure and the CREATE TABLE statements are changed,
     * the method will create a new database only if the existing database is deleted.
     */
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

    /**
     * Method defines the CREATE TABLE statements for the database.
     * 
     * @return statements in a list
     */
    private List<String> sqlStatements() {
        ArrayList<String> list = new ArrayList<>();
        list.add("CREATE TABLE User (id integer PRIMARY KEY, username varchar(50), password varchar(50), admin boolean);");
        list.add("CREATE TABLE Activity (id integer PRIMARY KEY, name varchar(50), unit varchar(50));");
        list.add("CREATE TABLE Action(id integer PRIMARY KEY, user_id integer, activity_id integer, time date, units float, accomplished boolean, setAsGoal boolean, FOREIGN KEY (user_id) REFERENCES User(id), FOREIGN KEY (activity_id) REFERENCES Activity(id));");
        return list;
    }
    
}
