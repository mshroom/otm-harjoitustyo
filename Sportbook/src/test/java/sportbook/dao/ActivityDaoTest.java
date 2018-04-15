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
import sportbook.domain.Activity;

/**
 *
 * @author minna
 */
public class ActivityDaoTest {
    File testDatabase;
    ActivityDao activitydao;
    
    @Before
    public void setUp() throws Exception {
        testDatabase = new File("sportbookdatatest.db");
        Database database = new Database("jdbc:sqlite:sportbookdatatest.db");
        database.init();
        this.activitydao = new ActivityDao(database);
        activitydao.create("running", "meters");
    }
    
    @Test
    public void activityIsAddedToDatabase() throws SQLException {
        List<Activity> activities = activitydao.findAll();
        assertEquals(1, activities.size());
    }
    
    @Test
    public void activityCanBeFoundById() throws SQLException {
        assertEquals(new Activity(1, "running", "meters"), activitydao.findOne(1));
    }
    
    @Test
    public void activityCanBeFoundByNameAndUnit() throws SQLException {
        assertEquals(new Activity(1, "running", "meters"), activitydao.findByNameAndUnit("running", "meters"));
    }
    
    @Test
    public void activityCanBeFoundByToString() throws SQLException {
        Activity a = new Activity(1, "running", "meters");
        assertEquals(a, activitydao.findByToString(a.toString()));
    }
    
    @Test
    public void activityCanBeDeleted() throws SQLException {
        Activity a = activitydao.findOne(1);
        activitydao.delete(a);
        List<Activity> activities = activitydao.findAll();
        assertEquals(0, activities.size());
    }
    
    @After
    public void tearDown() {
        testDatabase.delete();
    }

    
}
