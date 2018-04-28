/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.domain;


import java.util.Calendar;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author minna
 */
public class ActionTest {
    
    Action goal;
    Action workout;
    Calendar calendar;
    
    @Before
    public void setUp() {
        User user = new User(1, "hello", "world", false);
        Activity activity = new Activity(1, "running", "meters");
        this.calendar = Calendar.getInstance();
        this.goal = new Action(1, user, activity, 100, true, false, calendar.getTime());
        this.workout = new Action(2, user, activity, 100, false, true, calendar.getTime());
    }
    
    @Test
    public void actionIsCreatedCorrectly() {
        assertEquals(1, goal.getId());
        assertEquals(new User(1, "hello", "world", false), goal.getUser());
        assertEquals(new Activity(1, "running", "meters"), goal.getActivity());
        assertEquals(calendar.getTime(), goal.getDate());
    }    
    
    @Test
    public void goalIsSetAsGoalAndNotAccomplished() {
        assertTrue(goal.getSetAsGoal());
        assertFalse(goal.getAccomplished());
    }
    
    @Test
    public void workoutIsSetAccomplishedAndNotSetAsGoal() {
        assertTrue(workout.getAccomplished());
        assertFalse(workout.getSetAsGoal());
    }
}
