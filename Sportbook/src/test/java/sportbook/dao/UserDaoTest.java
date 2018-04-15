/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.dao;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import sportbook.domain.User;

/**
 *
 * @author minna
 */
public class UserDaoTest {
   
    
    File testDatabase;
    UserDao userdao;
    
    @Before
    public void setUp() throws Exception {
        testDatabase = new File("sportbookdatatest.db");
        Database database = new Database("jdbc:sqlite:sportbookdatatest.db");
        database.init();
        this.userdao = new UserDao(database);
        userdao.create("Testuser", "Testpassword");
    }
    
    @Test
    public void userIsAddedToDatabase() throws SQLException {
        List<User> users = userdao.findAll();
        assertEquals(1, users.size());
    }
    
    @Test
    public void userCanBeFoundById() throws SQLException {
        assertEquals(new User(1, "Testuser", "Testpassword"), userdao.findOne(1));
    }
    
    @Test
    public void userCanBeFoundByUsername() throws SQLException {
        assertEquals(new User(1, "Testuser", "Testpassword"), userdao.findByUsername("Testuser"));
    }
    
    @Test
    public void userCanBeDeleted() throws SQLException {
        User user = userdao.findByUsername("Testuser");
        userdao.delete(user);
        List<User> users = userdao.findAll();
        assertEquals(0, users.size());
    }
    
    @Test
    public void userCanChangeUsername() throws SQLException {
        User user = userdao.findByUsername("Testuser");
        int id = user.getId();
        userdao.changeUsername(user, "Testuser2");
        User user2 = userdao.findOne(id);
        assertEquals("Testuser2", user2.getUsername());
    }
    
    @Test
    public void userCanChangePassword() throws SQLException {
        User user = userdao.findByUsername("Testuser");
        int id = user.getId();
        userdao.changePassword(user, "NewPassword");
        User user2 = userdao.findOne(id);
        assertEquals("NewPassword", user2.getPassword());
    }
    
    @After
    public void tearDown() {
        testDatabase.delete();
    }
    
}
