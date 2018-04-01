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
import sportbook.domain.User;

/**
 *
 * @author minna
 */
public class UserDao {
    
    private User currentUser;
    private Database database;

    public UserDao(Database database) {
        this.currentUser = new User(-1, "Hello", "World");
        this.database = database;
    }

    public User create(String username, String password) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO User (username, password) VALUES (?, ?)");
        stmt.setString(1, username);
        stmt.setString(2, password);

        stmt.executeUpdate();
        stmt.close();
        connection.close();
        return this.findByUsername(username);
    }
    
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

        rs.close();
        stmt.close();
        connection.close();

        User u = new User(id, username, password);
        return u;
    }

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

        rs.close();
        stmt.close();
        connection.close();

        User u = new User(id, username, password);
        return u;
    }

    public List<User> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM User ORDER BY username");

        ResultSet rs = stmt.executeQuery();
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String username = rs.getString("username");
            String password = rs.getString("password");

            users.add(new User(id, username, password));
        }
        rs.close();
        stmt.close();
        connection.close();
        return users;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void delete(User userToBeDeleted) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM User WHERE id = ?");
        stmt.setInt(1, userToBeDeleted.getId());
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    public void changeUsername(User user, String username) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("UPDATE User SET username = ? WHERE id = ?");
        stmt.setString(1, username);
        stmt.setInt(2, user.getId());
        stmt.executeUpdate();
        stmt.close();
        connection.close();
        this.currentUser.setUsername(username);
    }

    public void changePassword(User user, String password) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("UPDATE User SET password = ? WHERE id = ?");
        stmt.setString(1, password);
        stmt.setInt(2, user.getId());
        stmt.executeUpdate();
        stmt.close();
        connection.close();
        this.currentUser.setPassword(password);
    }
}
