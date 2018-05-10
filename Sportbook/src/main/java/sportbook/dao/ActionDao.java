/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.dao;

import sportbook.domain.Action;
import sportbook.domain.Activity;
import sportbook.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class is responsible for storing and accessing data in the Action table of the database.
 * 
 * @author mshroom
 */
public class ActionDao {

    private SimpleDateFormat simpleDate;
    private SimpleDateFormat monthDate;
    private Database database;
    private ActivityDao activityDao;
    private UserDao userDao;

    public ActionDao(Database database, ActivityDao activityDao, UserDao userDao) {
        this.simpleDate = new SimpleDateFormat("dd/MM/yyyy");
        this.monthDate = new SimpleDateFormat("MM/YYYY");
        this.database = database;
        this.activityDao = activityDao;
        this.userDao = userDao;
    }

    /**
     * Method inserts a new action to the database.
     * 
     * @param user User object for whom the action is saved
     * @param activity Activity object for the activity of this action
     * @param units Amount of units (defined by activity) in this action
     * @param setAsGoal True if the action is saved as a goal
     * @param accomplished True if the workout is already accomplished
     * @param date Date object corresponding the day of the accomplished or planned workout
     * 
     * @throws SQLException 
     */
    public void create(User user, Activity activity, int units, Boolean setAsGoal, Boolean accomplished, Date date) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Action (user_id, activity_id, time, units, accomplished, setAsGoal) VALUES (?, ?, ?, ?, ?, ?)");
        stmt.setInt(1, user.getId());
        stmt.setInt(2, activity.getId());
        stmt.setDate(3, new java.sql.Date(date.getTime()));
        stmt.setDouble(4, units);
        stmt.setBoolean(5, accomplished);
        stmt.setBoolean(6, setAsGoal);

        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    /**
     * Method finds all actions saved for the given activity.
     * 
     * @param activity Activity object to be searched
     * 
     * @return Action objects in a list
     * 
     * @throws SQLException 
     */
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

    /**
     * Method finds all actions of the given user.
     * 
     * @param user User object to be searched
     * 
     * @return Action objects in a list
     * 
     * @throws SQLException 
     */
    public ArrayList<Action> findAllByUser(User user) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Action WHERE user_id = ?");
        stmt.setInt(1, user.getId());

        ArrayList<Action> actions = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            Activity activity = activityDao.findOne(rs.getInt("activity_id"));
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

    /**
     * Method finds all uncompleted goals of a given user.
     * 
     * @param user User object to be searched
     * 
     * @return Action objects in a list
     * 
     * @throws SQLException 
     */
    public List<Action> findAllUncompletedGoalsByUser(User user) throws SQLException {
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

    /**
     * Method finds all uncompleted goals of the given user matching the given activity.
     * 
     * @param user User object to be searched
     * @param activity Activity object to be searched
     * 
     * @return Action objects in a list
     * 
     * @throws SQLException 
     */
    public List<Action> findAllUncompletedGoalsByUserAndActivity(User user, Activity activity) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Action WHERE user_id = ? AND activity_id = ? AND setAsGoal = ? AND accomplished = ?");
        stmt.setInt(1, user.getId());
        stmt.setInt(2, activity.getId());
        stmt.setBoolean(3, true);
        stmt.setBoolean(4, false);

        ArrayList<Action> usersActions = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            int units = rs.getInt("units");
            Date date = rs.getDate("time");

            usersActions.add(new Action(id, user, activity, units, true, false, date));
        }
        rs.close();
        stmt.close();
        connection.close();
        return usersActions;
    }

    /**
     * Method finds all workouts and accomplished goals of the given user.
     * 
     * @param user User object to be searched
     * 
     * @return Action objects in a list
     * 
     * @throws SQLException 
     */
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

    /**
     * Method finds all workouts and accomplished goals of the given user matching the given activity.
     * 
     * @param user User object to be searched
     * @param activity Activity object to be searched
     * 
     * @return Action objects in a list
     * 
     * @throws SQLException 
     */
    public List<Action> findAllWorkoutsByUserAndActivity(User user, Activity activity) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Action WHERE user_id = ? AND activity_id = ? AND accomplished = ?");
        stmt.setInt(1, user.getId());
        stmt.setInt(2, activity.getId());
        stmt.setBoolean(3, true);

        ArrayList<Action> usersActions = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
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

    /**
     * Method finds all completed goals of the given user matching the given activity.
     * 
     * @param user User object to be searched
     * @param activity Activity object to be searched
     * 
     * @return Action objects in a list
     * 
     * @throws SQLException 
     */
    public List<Action> findAllCompletedGoalsByUserAndActivity(User user, Activity activity) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Action WHERE user_id = ? AND activity_id = ? AND setAsGoal = ? AND accomplished = ?");
        stmt.setInt(1, user.getId());
        stmt.setInt(2, activity.getId());
        stmt.setBoolean(3, true);
        stmt.setBoolean(4, true);

        ArrayList<Action> usersActions = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            int units = rs.getInt("units");
            Date date = rs.getDate("time");

            usersActions.add(new Action(id, user, activity, units, true, true, date));
        }
        rs.close();
        stmt.close();
        connection.close();
        return usersActions;
    }

    /**
     * Method finds all actions of the given user matching the month of the given date.
     * 
     * @param user User object to be searched
     * @param date Date object corresponding the month to be searched
     * 
     * @return Action objects in a list
     * 
     * @throws SQLException 
     */
    public List<Action> findAllByUserAndMonth(User user, Date date) throws SQLException {
        List<Action> actions = this.findAllByUser(user);
        List<Action> monthsActions = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++) {
            Action a = actions.get(i);
            if (monthDate.format(a.getDate()).equals(monthDate.format(date))) {
                monthsActions.add(a);
            }
        }
        return monthsActions;
    }

    /**
     * Method finds all workouts and accomplished goals of the given user 
     * on the day matching the given date.
     * 
     * @param user User object to be searched
     * @param date Date object corresponding the day to be searched
     * 
     * @return Action objects in a list
     * 
     * @throws SQLException 
     */
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

    /**
     * Method counts how many units the given user has accomplished in the given activity 
     * on the given day.
     * 
     * @param user User object to be searched
     * @param activity Activity object to be searched
     * @param date Date object corresponding the day in question
     * 
     * @return The sum of the accomplished units
     * 
     * @throws SQLException 
     */
    public Integer countAllWorkoutsByUserAndActivityAndMonth(User user, Activity activity, Date date) throws SQLException {
        List<Action> usersWorkouts = this.findAllWorkoutsByUserAndActivity(user, activity);
        int sum = 0;
        for (int i = 0; i < usersWorkouts.size(); i++) {
            Action a = usersWorkouts.get(i);
            if (monthDate.format(a.getDate()).equals(monthDate.format(date))) {
                sum += a.getUnits();
            }
        }
        return sum;
    }

    /**
     * Method counts the amount of completed goals of the given user in the given activity
     * on the given month.
     * 
     * @param user User object to be searched
     * @param activity Activity object to be searched
     * @param date Date object corresponding the month in question
     * 
     * @return The amount of completed goals
     * 
     * @throws SQLException 
     */
    public int countCompletedGoalsByUserAndActivityAndMonth(User user, Activity activity, Date date) throws SQLException {
        List<Action> usersGoals = this.findAllCompletedGoalsByUserAndActivity(user, activity);
        List<Action> monthsGoals = new ArrayList<>();
        for (int i = 0; i < usersGoals.size(); i++) {
            Action a = usersGoals.get(i);
            if (monthDate.format(a.getDate()).equals(monthDate.format(date))) {
                monthsGoals.add(a);
            }
        }
        return monthsGoals.size();
    }

    /**
     * Method counts the amount of uncompleted goals of the given user in the given activity
     * on the given month.
     * 
     * @param user User object to be searched
     * @param activity Activity object to be searched
     * @param date Date object corresponding the month in question
     * 
     * @return The amount of uncompleted goals
     * 
     * @throws SQLException 
     */
    public int countUncompletedGoalsByUserAndMonth(User user, Activity activity, Date date) throws SQLException {
        List<Action> usersGoals = this.findAllUncompletedGoalsByUserAndActivity(user, activity);
        List<Action> monthsGoals = new ArrayList<>();
        for (int i = 0; i < usersGoals.size(); i++) {
            Action a = usersGoals.get(i);
            if (monthDate.format(a.getDate()).equals(monthDate.format(date))) {
                monthsGoals.add(a);
            }
        }
        return monthsGoals.size();
    }

    /**
     * Method finds all uncompleted goals of the given user on the given day.
     * 
     * @param user User object to be searched
     * @param date Date object corresponding the day in question
     * 
     * @return Action objects in a list
     * 
     * @throws SQLException 
     */
    public List<Action> findAllUncompletedGoalsByUserAndDay(User user, Date date) throws SQLException {
        List<Action> usersGoals = this.findAllUncompletedGoalsByUser(user);
        List<Action> daysGoals = new ArrayList<>();
        for (int i = 0; i < usersGoals.size(); i++) {
            Action a = usersGoals.get(i);
            if (simpleDate.format(a.getDate()).equals(simpleDate.format(date))) {
                daysGoals.add(a);
            }
        }
        return daysGoals;
    }

    /**
     * Method deletes an action from the database.
     * If a workout or an uncomplete goal is deleted, it is permanently removed from the database.
     * If a completed goal is deleted, the goal will be marked as uncomplete but not removed.
     * 
     * @param action Action to be deleted
     * 
     * @throws SQLException 
     */
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

    /**
     * Method changes the given action to accomplished.
     * A goal is completed using this method.
     * 
     * @param action Action to be updated
     * 
     * @throws SQLException 
     */
    public void complete(Action action) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("UPDATE Action SET accomplished = ? WHERE id = ?");
        stmt.setBoolean(1, true);
        stmt.setInt(2, action.getId());
        stmt.executeUpdate();
        stmt.close();

        connection.close();
    }
}
