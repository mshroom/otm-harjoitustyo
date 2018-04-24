/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.domain;

import java.io.File;
import java.util.Calendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sportbook.dao.ActionDao;
import sportbook.dao.ActivityDao;
import sportbook.dao.Database;
import sportbook.dao.UserDao;

/**
 *
 * @author minna
 */
public class SportbookTest {
    File testDatabase;
    Sportbook sportbook;
    Calendar calendar;
    
    @Before
    public void setUp() throws Exception {
        testDatabase = new File("sportbookdatatest.db");
        Database database = new Database("jdbc:sqlite:sportbookdatatest.db");
        database.init();
        ActivityDao activitydao = new ActivityDao(database);
        UserDao userdao = new UserDao(database);
        ActionDao actiondao = new ActionDao(database, activitydao, userdao);
        this.sportbook = new Sportbook(userdao, activitydao,  actiondao);
        this.calendar = Calendar.getInstance();
        userdao.create("Testuser", "Testpassword");
        activitydao.create("running", "meters");
        activitydao.create("swimming", "minutes");
        actiondao.create(userdao.findOne(1), activitydao.findOne(1), 100, true, false, calendar.getTime());
        actiondao.create(userdao.findOne(1), activitydao.findOne(2), 60, false, true, calendar.getTime());
        sportbook.login("Testuser", "Testpassword");
    }
    
    @Test
    public void loginFailsIfUsernameIsIncorrect() {
        assertEquals(1, sportbook.login("wrongUsername", "password"));        
    }
    
    @Test
    public void loginFailsIfPAsswordIsIncorrect() {
        assertEquals(2, sportbook.login("Testuser", "WrongPassword"));
    }
    
    @Test
    public void userCanLogin() {
        assertEquals(3, sportbook.login("Testuser", "Testpassword"));
        assertEquals(new User(1, "Testuser", "Testpassword"), sportbook.getLoggedIn());
    }
    
    @Test
    public void registerFailsIfUsernameIsAlreadyInUse() {
        assertEquals(1, sportbook.register("Testuser", "Newpassword"));
    }
    
    @Test
    public void userCanRegister() {
        assertEquals(2, sportbook.register("Newuser", "Testpassword"));
        assertEquals(new User(2, "Newuser", "Testpassword"), sportbook.getLoggedIn());
        assertEquals(3, sportbook.login("Newuser", "Testpassword"));
    }
    
    @Test
    public void userCanChangeUsername() {
        assertEquals(2, sportbook.changeUsername("Newusername"));
        assertEquals(3, sportbook.login("Newusername", "Testpassword"));
    }
    
    @Test
    public void userCantChangeUsernameToOneThatIsAlreadyInUse() {
        sportbook.register("Newuser", "Newpassword");
        assertEquals(1, sportbook.changeUsername("Testuser"));
    }
    
    @Test
    public void userCanChangePassword() {
        assertEquals(true, sportbook.changePassword("Newpassword"));
        assertEquals(3, sportbook.login("Testuser", "Newpassword"));
    }
    
    @Test
    public void userAccountCanBeDeleted() {
        sportbook.register("Newuser", "Newpassword");
        assertEquals(true, sportbook.deleteAccount());
        assertEquals(1, sportbook.login("Newuser", "Newpassword"));
    }
    
    @Test
    public void activityCanBeCreated() {
        assertEquals(2, sportbook.createActivity("running", "minutes"));
        assertEquals(3, sportbook.listActivities().size());
    }
    
    @Test
    public void activityThatExistsWillNotBeCreated() {
        assertEquals(1, sportbook.createActivity("running", "meters"));
        assertEquals(2, sportbook.listActivities().size());
    }
    
    @Test
    public void activityCanBeDeleted() {
        sportbook.createActivity("running", "minutes");
        assertEquals(1, sportbook.deleteActivity("running, minutes"));
        assertEquals(2, sportbook.listActivities().size());
    }
    
    @Test
    public void activityThatIsInUseCannotBeDeleted() {
        assertEquals(2, sportbook.deleteActivity("running, meters"));
        assertEquals(2, sportbook.listActivities().size());
    }
    
    @Test
    public void workoutCanBeSaved() {
        assertEquals(1, sportbook.getDailyWorkouts(calendar.getTime()).size());
        assertEquals(true, sportbook.saveAction("running, meters", 5000, false, calendar.getTime()));
        assertEquals(2, sportbook.getDailyWorkouts(calendar.getTime()).size());
    }
    
    @Test
    public void goalCanBeSaved() {
        assertEquals(1, sportbook.getDailyGoals(calendar.getTime()).size());
        assertEquals(true, sportbook.saveAction("swimming, minutes", 60, true, calendar.getTime()));
        assertEquals(2, sportbook.getDailyGoals(calendar.getTime()).size());
    }
    
    @Test
    public void actionCanBeDeleted() {
        assertTrue(sportbook.deleteAction(new Action(1, new User(1, "Testuser", "Testpassword"), new Activity(1, "running", "meters"), 100, true, false, calendar.getTime())));
    }
    
    @Test
    public void actionCanBeCompleted() {
        assertTrue(sportbook.completeAction(new Action(1, new User(1, "Testuser", "Testpassword"), new Activity(1, "running", "meters"), 100, true, false, calendar.getTime())));
    }
    
    @Test
    public void dailyWorkoutsContainWorkoutsAndCompletedGoals() {
        assertEquals(1, sportbook.getDailyWorkouts(calendar.getTime()).size());
        sportbook.completeAction(new Action(1, new User(1, "Testuser", "Testpassword"), new Activity(1, "running", "meters"), 100, true, false, calendar.getTime()));
        assertEquals(2, sportbook.getDailyWorkouts(calendar.getTime()).size());
    }
    
    @Test
    public void dailyGoalsContainOnlyUncompletedGoals() {
        assertEquals(1, sportbook.getDailyGoals(calendar.getTime()).size());
        sportbook.completeAction(new Action(1, new User(1, "Testuser", "Testpassword"), new Activity(1, "running", "meters"), 100, true, false, calendar.getTime()));
        assertEquals(0, sportbook.getDailyGoals(calendar.getTime()).size());        
    }
    
    @After
    public void tearDown() {
        testDatabase.delete();
    }

}
