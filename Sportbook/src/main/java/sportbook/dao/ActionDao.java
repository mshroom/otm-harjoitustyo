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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sportbook.domain.Action;
import sportbook.domain.Activity;
import sportbook.domain.User;

/**
 *
 * @author minna
 */
public class ActionDao {

    private SimpleDateFormat simpleDate;
    private Database database;
    private ActivityDao activityDao;
    private UserDao userDao;

    public ActionDao(Database database, ActivityDao activityDao, UserDao userDao) {
        this.simpleDate = new SimpleDateFormat("dd/MM/yyyy");
        this.database = database;
        this.activityDao = activityDao;
        this.userDao = userDao;
    }

    public void create(User user, Activity activity, Integer units, Boolean setAsGoal, Boolean accomplished, Date date) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Action (user_id, activity_id, time, units, accomplished, setAsGoal) VALUES (?, ?, ?, ?, ?, ?)");
        stmt.setInt(1, user.getId());
        stmt.setInt(2, activity.getId());
        stmt.setDate(3, new java.sql.Date(date.getTime()));
        stmt.setInt(4, units);
        stmt.setBoolean(5, accomplished);
        stmt.setBoolean(6, setAsGoal);

        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    public ArrayList<Action> findAllByActivity(Activity activity) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Action WHERE activity_id = ?");
        stmt.setInt(1, activity.getId());

        ArrayList<Action> actions = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            User user = userDao.findOne(rs.getInt("user_id"));
            int units = rs.getInt("units");
            boolean setAsGoal = rs.getBoolean("setAsGoal");
            boolean accomplished = rs.getBoolean("accomplished");
            Date date = rs.getDate("time");

            actions.add(new Action(id, user, activity, units, setAsGoal, accomplished, date));
        }
        rs.close();
        stmt.close();
        connection.close();
        return actions;
    }

    public List<Action> findAllGoalsByUser(User user) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Action WHERE user_id = ? AND setAsGoal = ? AND accomplished = ?");
        stmt.setInt(1, user.getId());
        stmt.setBoolean(2, true);
        stmt.setBoolean(3, false);

        ArrayList<Action> usersActions = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            Activity activity = activityDao.findOne(rs.getInt("activity_id"));
            int units = rs.getInt("units");
            Date date = rs.getDate("time");

            usersActions.add(new Action(id, user, activity, units, true, false, date));
        }
        rs.close();
        stmt.close();
        connection.close();
        return usersActions;
    }

    public List<Action> findAllWorkoutsByUser(User user) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Action WHERE user_id = ? AND accomplished = ?");
        stmt.setInt(1, user.getId());
        stmt.setBoolean(2, true);

        ArrayList<Action> usersActions = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            Activity activity = activityDao.findOne(rs.getInt("activity_id"));
            int units = rs.getInt("units");
            boolean setAsGoal = rs.getBoolean("setAsGoal");
            Date date = rs.getDate("time");

            usersActions.add(new Action(id, user, activity, units, setAsGoal, true, date));
        }
        rs.close();
        stmt.close();
        connection.close();
        return usersActions;
    }

    public List<Action> findAllWorkoutsByUserAndDay(User user, Date date) throws SQLException {
        List<Action> usersWorkouts = this.findAllWorkoutsByUser(user);
        List<Action> daysWorkouts = new ArrayList<>();
        for (int i = 0; i < usersWorkouts.size(); i++) {
            Action a = usersWorkouts.get(i);
            if (simpleDate.format(a.getDate()).equals(simpleDate.format(date))) {               
                    daysWorkouts.add(a);                
            }
        }
        return daysWorkouts;
    }

    public List<Action> findAllGoalsByUserAndDay(User user, Date date) throws SQLException {
        List<Action> usersGoals = this.findAllGoalsByUser(user);
        List<Action> daysGoals = new ArrayList<>();
        for (int i = 0; i < usersGoals.size(); i++) {
            Action a = usersGoals.get(i);
            if (simpleDate.format(a.getDate()).equals(simpleDate.format(date))) {               
                    daysGoals.add(a);                
            }
        }
        return daysGoals;
    }

    public void delete(Action action) throws SQLException {
        Connection connection = database.getConnection();
                
        if (action.getSetAsGoal() && action.getAccomplished()) {
            PreparedStatement stmt = connection.prepareStatement("UPDATE Action SET accomplished = ? WHERE id = ?");
            stmt.setBoolean(1, false);
            stmt.setInt(2, action.getId());
            stmt.executeUpdate();
            stmt.close();
        } else {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM Action WHERE id = ?");
            stmt.setInt(1, action.getId());
            stmt.executeUpdate();
            stmt.close();
        }
        connection.close();
    }

    public void complete(Action action) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("UPDATE Action SET accomplished = ? WHERE id = ?");
        stmt.setBoolean(1, true);
        stmt.setInt(2, action.getId());
        stmt.executeUpdate();
        stmt.close();
        
        connection.close();
    }

    private Action unite(Action a, Action b) {
        a.setUnits(a.getUnits() + b.getUnits());
        return a;
    }
}
