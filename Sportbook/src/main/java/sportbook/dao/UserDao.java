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
 *
 * @author minna
 */
public class UserDao {

    private Database database;

    public UserDao(Database database) {
        this.database = database;
    }

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

    public void changeUsername(User user, String username) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("UPDATE User SET username = ? WHERE id = ?");
        stmt.setString(1, username);
        stmt.setInt(2, user.getId());
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    public void changePassword(User user, String password) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("UPDATE User SET password = ? WHERE id = ?");
        stmt.setString(1, password);
        stmt.setInt(2, user.getId());
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    public void setAdmin(User admin) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("UPDATE User SET admin = ? WHERE id = ?");
        stmt.setBoolean(1, true);
        stmt.setInt(2, admin.getId());
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }
}
