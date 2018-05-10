/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.domain;

import java.io.File;
import java.util.Calendar;
import java.util.List;
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
import sportbook.domain.Sportbook.StatisticsNode;

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
        assertEquals(new User(1, "Testuser", "Testpassword", false), sportbook.getLoggedIn());
    }
    
    @Test
    public void registerFailsIfUsernameIsAlreadyInUse() {
        assertEquals(1, sportbook.register("Testuser", "Newpassword"));
    }
    
    @Test
    public void userCanRegister() {
        assertEquals(2, sportbook.register("Newuser", "Testpassword"));
        assertEquals(new User(2, "Newuser", "Testpassword", false), sportbook.getLoggedIn());
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
        assertEquals(2, sportbook.getUsers().size());
        assertEquals(true, sportbook.deleteAccount());
        assertEquals(1, sportbook.getUsers().size());
        assertEquals(1, sportbook.login("Newuser", "Newpassword"));
    }
    
    @Test
    public void userAccountCanBeDeleted2() {
        User other = new User(2, "User2", "password", false);
        sportbook.register("User2", "password");
        assertEquals(2, sportbook.getUsers().size());
        assertEquals(true, sportbook.deleteAccount(other));
        assertEquals(1, sportbook.login("User2", "password"));
        assertEquals(1, sportbook.getUsers().size());
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
        assertTrue(sportbook.deleteAction(new Action(1, new User(1, "Testuser", "Testpassword", false), new Activity(1, "running", "meters"), 100, true, false, calendar.getTime())));
    }
    
    @Test
    public void actionCanBeCompleted() {
        assertTrue(sportbook.completeAction(new Action(1, new User(1, "Testuser", "Testpassword", false), new Activity(1, "running", "meters"), 100, true, false, calendar.getTime())));
    }
    
    @Test
    public void dailyWorkoutsContainWorkoutsAndCompletedGoals() {
        assertEquals(1, sportbook.getDailyWorkouts(calendar.getTime()).size());
        sportbook.completeAction(new Action(1, new User(1, "Testuser", "Testpassword", false), new Activity(1, "running", "meters"), 100, true, false, calendar.getTime()));
        assertEquals(2, sportbook.getDailyWorkouts(calendar.getTime()).size());
    }
    
    @Test
    public void dailyGoalsContainOnlyUncompletedGoals() {
        assertEquals(1, sportbook.getDailyGoals(calendar.getTime()).size());
        sportbook.completeAction(new Action(1, new User(1, "Testuser", "Testpassword", false), new Activity(1, "running", "meters"), 100, true, false, calendar.getTime()));
        assertEquals(0, sportbook.getDailyGoals(calendar.getTime()).size());        
    }
    
    @Test
    public void monthlyActivitiesCanBeFound() {
        assertEquals(2, sportbook.getMonthlyActivities(calendar.getTime()).size());
        calendar.add(Calendar.MONTH, 1);
        assertEquals(0, sportbook.getMonthlyActivities(calendar.getTime()).size());
    }
    
    @Test
    public void usersAreFound() {
        assertTrue(sportbook.hasUsers());
        assertEquals(1, sportbook.getUsers().size());
        sportbook.deleteAccount(new User(1, "Testuser", "Testpassword", false));
        assertFalse(sportbook.hasUsers());
        assertEquals(0, sportbook.getUsers().size());
    }
    
    @Test
    public void userCanBeSetAdmin() {
        assertTrue(sportbook.setAdmin("Testuser"));        
    }
    
    @Test
    public void statisticsAreCreatedCorrectly() {
        sportbook.saveAction("swimming, minutes", 100, false, calendar.getTime());
        sportbook.saveAction("swimming, minutes", 40, false, calendar.getTime());
        List<StatisticsNode> statistics = sportbook.createStatistics(calendar.getTime());
        assertEquals(2, statistics.size());
        if (statistics.size() == 2) {
            StatisticsNode running = statistics.get(0);
            StatisticsNode swimming = statistics.get(1);
            assertEquals("0.0", running.getWorkouts());
            assertEquals("1", running.getUncompleted());
            assertEquals("0", running.getCompleted());
            assertEquals("2.0", swimming.getWorkouts());
            assertEquals("0", swimming.getCompleted());
            assertEquals("0", swimming.getUncompleted());
        }
    }
    
    @After
    public void tearDown() {
        testDatabase.delete();
    }

}
