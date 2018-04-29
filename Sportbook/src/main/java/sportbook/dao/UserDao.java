/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.dao;

import sportbook.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class is responsible for storing and accessing data in the User table of the database.
 * 
 * @author mshroom
 */
public class UserDao {

    private Database database;

    public UserDao(Database database) {
        this.database = database;
    }

    /**
     * Method inserts a new user to the database. 
     * Users are created as normal users, admin rights can be added later.
     * 
     * @param username User's username
     * @param password User's password
     * 
     * @return The created User object
     * 
     * @throws SQLException 
     */
    public User create(String username, String password) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO User (username, password, admin) VALUES (?, ?, ?)");
        stmt.setString(1, username);
        stmt.setString(2, password);
        stmt.setBoolean(3, false);

        stmt.executeUpdate();
        stmt.close();
        connection.close();
        return this.findByUsername(username);
    }

    /**
     * Method searches for the given user id in the database.
     * 
     * @param id User's id
     * 
     * @return User object with corresponding id or null if the user id does not exist
     * 
     * @throws SQLException 
     */
    public User findOne(int id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM User WHERE id = ?");
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        String username = rs.getString("username");
        String password = rs.getString("password");
        Boolean admin = rs.getBoolean("admin");

        rs.close();
        stmt.close();
        connection.close();

        User u = new User(id, username, password, admin);
        return u;
    }

    /**
     * Method searches for the given username in the database.
     * 
     * @param username User's username
     * 
     * @return User object with corresponding username or null if the username does not exist
     * 
     * @throws SQLException 
     */
    public User findByUsername(String username) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM User WHERE username = ?");
        stmt.setString(1, username);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        int id = rs.getInt("id");
        String password = rs.getString("password");
        Boolean admin = rs.getBoolean("admin");

        rs.close();
        stmt.close();
        connection.close();

        User u = new User(id, username, password, admin);
        return u;
    }

    /**
     * Method finds all users in the database.
     * 
     * @return users in a list, sorted alphabetically by username
     * 
     * @throws SQLException 
     */
    public List<User> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM User ORDER BY username");

        ResultSet rs = stmt.executeQuery();
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String username = rs.getString("username");
            String password = rs.getString("password");
            Boolean admin = rs.getBoolean("admin");

            users.add(new User(id, username, password, admin));
        }
        rs.close();
        stmt.close();
        connection.close();
        return users;
    }

    /**
     * Method deletes a user and all user's saved data from the database.
     * 
     * @param userToBeDeleted User object to be deleted, user id must be correct
     * 
     * @throws SQLException 
     */
    public void delete(User userToBeDeleted) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM User WHERE id = ?");
        stmt.setInt(1, userToBeDeleted.getId());
        stmt.executeUpdate();
        stmt.close();
        PreparedStatement stmt2 = connection.prepareStatement("DELETE FROM Action WHERE user_id = ?");
        stmt2.setInt(1, userToBeDeleted.getId());
        stmt2.executeUpdate();
        stmt2.close();
        connection.close();
    }

    /**
     * Method changes the username of the given user.
     * 
     * @param user User object to be updated, user id must be correct
     * @param username New username
     * 
     * @throws SQLException 
     */
    public void changeUsername(User user, String username) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("UPDATE User SET username = ? WHERE id = ?");
        stmt.setString(1, username);
        stmt.setInt(2, user.getId());
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    /**
     * Method changes the password of the given user.
     * 
     * @param user User object to be updated, user id must be correct
     * @param password
     * 
     * @throws SQLException 
     */
    public void changePassword(User user, String password) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("UPDATE User SET password = ? WHERE id = ?");
        stmt.setString(1, password);
        stmt.setInt(2, user.getId());
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    /**
     * Method changes the role of the given user to admin.
     * 
     * @param user User to be updated, user id must be correct
     * 
     * @throws SQLException 
     */
    public void setAdmin(User user) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("UPDATE User SET admin = ? WHERE id = ?");
        stmt.setBoolean(1, true);
        stmt.setInt(2, user.getId());
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }
}
