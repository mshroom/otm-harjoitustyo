/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author minna
 */
public class ActivityTest {
    Activity activity;
    
    @Before
    public void setUp() {
        this.activity = new Activity(1, "running", "meters");
    }
    
    @Test
    public void activitiesWithSimilarNameAndUnitsEqual() {
        Activity other = new Activity(2, "running", "meters");
        assertTrue(activity.equals(other));
    }
    
    @Test
    public void activitiesWithDifferentUnitsDoNotEqual() {
        Activity other = new Activity(1, "running", "miles");
        assertFalse(activity.equals(other));
    }
    
    @Test
    public void activitiesWithDifferentNameDoNotEqual() {
        Activity other = new Activity(1, "jogging", "meters");
        assertFalse(activity.equals(other));
    }
    
    @Test
    public void activityAndObjectOfDifferentTypeDoNotEqual() {
        Object o = new Object();
        assertFalse(activity.equals(o));
    }
    
    @Test
    public void toStringIsWorking() {
        assertEquals("running, meters", activity.toString());
    }   
    
}
