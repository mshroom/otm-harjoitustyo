/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.dao;

import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import sportbook.domain.Action;
import sportbook.domain.Activity;
import sportbook.domain.User;

/**
 *
 * @author minna
 */
public class ActionDaoTest {
    File testDatabase;
    ActionDao actiondao;
    ActivityDao activitydao;
    UserDao userdao;
    Calendar calendar;
    
    @Before
    public void setUp() throws Exception {
        testDatabase = new File("sportbookdatatest.db");
        Database database = new Database("jdbc:sqlite:sportbookdatatest.db");
        database.init();
        this.activitydao = new ActivityDao(database);
        this.userdao = new UserDao(database);
        this.actiondao = new ActionDao(database, activitydao, userdao);
        userdao.create("Testuser", "Testpassword");
        activitydao.create("running", "meters");
        activitydao.create("swimming", "minutes");
        this.calendar = Calendar.getInstance();
        actiondao.create(userdao.findOne(1), activitydao.findOne(1), 100, true, false, calendar.getTime());
        actiondao.create(userdao.findOne(1), activitydao.findOne(2), 60, false, true, calendar.getTime());
    }
    
    @Test
    public void actionIsAddedToDatabase() throws SQLException {
        Activity a = activitydao.findOne(1);
        List<Action> actions = actiondao.findAllByActivity(a);
        assertEquals(1, actions.size());
    }
    
    @Test
    public void usersGoalsCanBeFound() throws SQLException {
        User u = userdao.findOne(1);
        List<Action> goals = actiondao.findAllUncompletedGoalsByUser(u);
        assertEquals(1, goals.size());
    }
    
    @Test
    public void usersWorkoutsCanBeFound() throws SQLException {
        User u = userdao.findOne(1);
        List<Action> workouts = actiondao.findAllWorkoutsByUser(u);
        assertEquals(1, workouts.size());
    }
    
    @Test
    public void usersGoalsCanBeFoundByDay() throws SQLException {
        User u = userdao.findOne(1);
        List<Action> goals = actiondao.findAllUncompletedGoalsByUserAndDay(u, calendar.getTime());
        assertEquals(1, goals.size());
    }
    
    @Test
    public void usersWorkoutsCanBeFoundByDay() throws SQLException {
        User u = userdao.findOne(1);
        List<Action> workouts = actiondao.findAllWorkoutsByUserAndDay(u, calendar.getTime());
        assertEquals(1, workouts.size());
    }
    
    @Test
    public void completedGoalIsListedAsWorkout() throws SQLException {
        User u = userdao.findOne(1);
        List<Action> goals = actiondao.findAllUncompletedGoalsByUser(u);
        Action a = goals.get(0);
        assertFalse(a.getAccomplished());
        actiondao.complete(a);
        List<Action> goals2 = actiondao.findAllUncompletedGoalsByUser(u);
        assertEquals(0, goals2.size());
        List<Action> workouts = actiondao.findAllWorkoutsByUser(u);
        assertEquals(2, workouts.size());
    }
    
    @Test
    public void deletingCompletedGoalMakesItUncompleted() throws SQLException {
        User u = userdao.findOne(1);
        Activity act = activitydao.findOne(1);
        List<Action> running1 = actiondao.findAllByActivity(act);
        Action r1 = running1.get(0);
        assertFalse(r1.getAccomplished());
        actiondao.complete(r1);        
        List<Action> running2 = actiondao.findAllByActivity(act);
        Action r2 = running2.get(0);
        actiondao.delete(r2);
        List<Action> goals2 = actiondao.findAllUncompletedGoalsByUser(u);
        Action a = goals2.get(0);
        assertFalse(a.getAccomplished());
    }
    
    @Test
    public void uncompletedGoalCanBeDeleted() throws SQLException {
        User u = userdao.findOne(1);
        Action a = actiondao.findAllUncompletedGoalsByUser(u).get(0);
        actiondao.delete(a);
        List<Action> goals = actiondao.findAllUncompletedGoalsByUser(u);
        assertEquals(0, goals.size());
    }
    
    @Test
    public void workoutCanBeDeleted() throws SQLException {
        User u = userdao.findOne(1);
        Action a = actiondao.findAllWorkoutsByUser(u).get(0);
        actiondao.delete(a);
        List<Action> workouts = actiondao.findAllWorkoutsByUser(u);
        assertEquals(0, workouts.size());
    }
    
    @After
    public void tearDown() {
        testDatabase.delete();
    }

    
}
