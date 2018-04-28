/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.domain;

import sportbook.dao.UserDao;
import sportbook.dao.ActivityDao;
import sportbook.dao.ActionDao;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class contains the essential software logic.
 * 
 * @author mshroom
 */
public class Sportbook {
    private UserDao userDao;
    private ActivityDao activityDao;
    private ActionDao actionDao;
    private User loggedIn;

    public Sportbook(UserDao userDao, ActivityDao activityDao, ActionDao actionDao) {
        this.userDao = userDao;
        this.activityDao = activityDao;
        this.actionDao = actionDao;
        this.loggedIn = new User(-1, "Hello", "World", false);
    }

    public User getLoggedIn() {
        return this.loggedIn;
    }

    /**
     * Method is used to login a registered user.
     * 
     * @param username User's username
     * @param password User's password
     * 
     * @return 1 if username does not exist, 
     * 2 if password is incorrect, 
     * 3 if login is successful and 
     * 4 if there is a failure in accessing saved data.
     */
    public int login(String username, String password) {
        try {
            User user = userDao.findByUsername(username);
            if (user == null) {
                return 1;
            } else if (!password.equals(user.getPassword())) {
                return 2;
            }
            this.loggedIn = user;
            return 3;
        } catch (SQLException ex) {
            System.out.println(ex);
            return 4;
        }
    }

    /**
     * Method is used to register a new user.
     * 
     * @param username User's username
     * @param password User's password
     * 
     * @return 1 if the username is already in use and registration fails,
     * 2 if the registration is successful and
     * 3 if there is a failure in accessing saved data.
     */
    public int register(String username, String password) {
        try {
            User user = userDao.findByUsername(username);
            if (user != null) {
                return 1;
            }
            userDao.create(username, password);
            this.loggedIn = userDao.findByUsername(username);
            return 2;
        } catch (SQLException ex) {
            System.out.println(ex);
            return 3;
        }
    }
    
    /**
     * Method is used to change the username of the current user.
     * 
     * @param username New username
     * 
     * @return 1 if the username is already in use,
     * 2 if the change is successful and
     * 3 if there is a failure in accessing saved data.
     */
    public int changeUsername(String username) {
        try {
            User user = userDao.findByUsername(username);
            if (user != null) {
                return 1;
            } else {
                userDao.changeUsername(this.loggedIn, username);
                this.loggedIn = userDao.findByUsername(username);
                return 2;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return 3;
        }
    }

    /**
     * Method is used to change the password of the current user.
     * 
     * @param password New password
     * 
     * @return true if the change is successful,
     * false if there is a failure in saving data.
     */
    public boolean changePassword(String password) {
        try {
            userDao.changePassword(this.loggedIn, password);
            this.loggedIn = userDao.findByUsername(this.loggedIn.getUsername());
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }

    /**
     * Method is used to delete the account of the current user.
     * 
     * @return true if the deletion is successful,
     * false if there is a failure in saving data.
     */
    public boolean deleteAccount() {
        try {
            userDao.delete(this.loggedIn);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }
    
    /**
     * Method is used to delete the account of the given user.
     * 
     * @param user User to be deleted
     * 
     * @return true if the deletion is successful,
     * false if there is a failure in saving data.
     */
    public boolean deleteAccount(User user) {
        try {
            userDao.delete(user);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }

    /**
     * Method is used to create a new activity.
     * 
     * @param activity Name of the activity
     * @param units Name of the units to be used with the activity
     * 
     * @return 1 if the activity with the same data already exists, 
     * 2 if the creation is successful and
     * 3 if there is a failure in saving data.
     */
    public int createActivity(String activity, String units) {
        try {
            Activity newActivity = activityDao.findByNameAndUnit(activity, units);
            if (newActivity != null) {
                return 1;
            } else {
                activityDao.create(activity, units);
                return 2;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return 3;
        }
    }

    /**
     * Method is used to delete an activity from the database.
     * The activity will not be deleted if there are saved workouts
     * or goals that use the activity.
     * 
     * @param activity The toString() representation of the activity
     * 
     * @return 1 if the deletion is successful,
     * 2 if the activity is in use and cannot be deleted and
     * 3 if there is a failure in saving data.
     */
    public int deleteActivity(String activity) {
        try {
            Activity a = this.findByToString(activity);
            if (actionDao.findAllByActivity(a).isEmpty()) {
                activityDao.delete(a);
                return 1;
            } else {
                return 2;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return 3;
        }
    }

    /**
     * Method is used to create a list of all saved activities.
     * 
     * @return list of the String representations of the activities
     */
    public ObservableList<String> listActivities() {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            List<Activity> activities = activityDao.findAll();
            activities.forEach(activity -> {
                list.add(activity.toString());
            });
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return list;
    }

    /**
     * Method is used to save a workout or a goal for the current user.
     * 
     * @param activity The string representation of the activity
     * @param numberOfUnits The number of units to be saved
     * @param goal True if the action is a goal
     * @param date Date of the action
     * 
     * @return true if the creation is successful,
     * false if there is a failure in saving data.
     */
    public boolean saveAction(String activity, int numberOfUnits, boolean goal, Date date) {
        try {
            if (goal) {
                actionDao.create(this.loggedIn, this.findByToString(activity), numberOfUnits, true, false, date);
            } else {
                actionDao.create(this.loggedIn, this.findByToString(activity), numberOfUnits, false, true, date);
            }
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }

    /**
     * Method is used to delete a saved workout or goal. 
     * If the action is a completed goal, the goal will be marked as uncomplete but not deleted.
     * 
     * @param action Action to be deleted
     * 
     * @return true if the deletion is successful
     * false if there is a failure in saving data.
     */
    public boolean deleteAction(Action action) {
        try {
            actionDao.delete(action);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }

    /**
     * Method is used to mark a goal as complete.
     * 
     * @param action Goal to be completed
     * 
     * @return true if the update is successful,
     * false if there is a failure in saving data.
     */
    public boolean completeAction(Action action) {
        try {
            actionDao.complete(action);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }

    /**
     * Method finds all workouts and completed goals of the current user on the given day.
     * 
     * @param date The date of the day observed.
     * 
     * @return list of all workouts or null if there is a failure in accessing saved data.
     */
    public List<Action> getDailyWorkouts(Date date) {
        List<Action> workouts = new ArrayList<>();
        try {
            workouts = actionDao.findAllWorkoutsByUserAndDay(this.loggedIn, date);
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
        Collections.sort(workouts);
        return workouts;
    }

    /**
     * Method finds all uncompleted goals of the current user on the given day.
     * 
     * @param date The date of the day observed.
     * 
     * @return list of all uncompleted goals or null if there is a failure in accessing saved data.
     */
    public List<Action> getDailyGoals(Date date) {
        List<Action> goals = new ArrayList<>();
        try {
            goals = actionDao.findAllUncompletedGoalsByUserAndDay(this.loggedIn, date);
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
        Collections.sort(goals);
        return goals;
    }

    /**
     * Method finds all activities that the user has used on the given month.
     * 
     * @param date The date of the day observed
     * 
     * @return a list of used activities in alphabetical order
     */
    public List<Activity> getMonthlyActivities(Date date) {
        List<Activity> activities = new ArrayList<>();
        List<Action> actions;
        try {
            actions = actionDao.findAllByUserAndMonth(this.loggedIn, date);
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
        for (int i = 0; i < actions.size(); i++) {
            Activity a = actions.get(i).getActivity();
            if (!activities.contains(a)) {
                activities.add(a);
            }
        }
        Collections.sort(activities);
        return activities;
    }

    /**
     * Method finds an activity based on it's String representation.
     * The method is useful in interpreting texts in the user interface.
     * 
     * @param activity
     * @return 
     */
    private Activity findByToString(String activity) {
        try {
            List<Activity> activities = activityDao.findAll();
            for (int i = 0; i < activities.size(); i++) {
                Activity a = activities.get(i);
                if (a.toString().equals(activity)) {
                    return a;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    /**
     * Method creates monthly statistics for the current user on the given month.
     * Statistics include the amount of workouts and the number of completed and uncompleted goals 
     * for each activity that the user has used on the month in question.
     * 
     * @param date The date of the month in question
     * 
     * @return Statistics in a list of StatisticNode objects
     */
    public List<StatisticsNode> createStatistics(Date date) {
        List<StatisticsNode> statistics = new ArrayList<>();
        List<Activity> activities = this.getMonthlyActivities(date);
        for (int i = 0; i < activities.size(); i++) {
            try {
                Activity a = activities.get(i);
                String activity = a.toString();
                double workouts = actionDao.countAllWorkoutsByUserAndActivityAndMonth(loggedIn, a, date);
                int completed = actionDao.countCompletedGoalsByUserAndActivityAndMonth(loggedIn, a, date);
                int uncompleted = actionDao.countUncompletedGoalsByUserAndMonth(loggedIn, a, date);
                StatisticsNode node = new StatisticsNode(activity, workouts, completed, uncompleted);
                statistics.add(node);
            } catch (SQLException ex) {
                System.out.println(ex);
                return null;
            }            
        }
        return statistics;
    }

    /**
     * Method creates a list of all registered users.
     * 
     * @return  a list or null if there is a failure in fetching saved data.
     */
    public List<User> getUsers() {
        try {
            return userDao.findAll();
        } catch (SQLException ex) {
            return null;
        }
    }

    /**
     * Method tells if there are registered users.
     * 
     * @return true if there is at least one user or admin, 
     * false if there are no users or there is a failure in fetching saved data.
     */
    public boolean hasUsers() {
        try {
            if (userDao.findAll().size() > 0) {
                return true;
            }
        } catch (SQLException ex) {
            return false;
        }
        return false;
    }

    /**
     * Method is used to define a username for the admin account.
     * 
     * @param username The username of the user to be made an admin
     * 
     * @return true if the update is successful, otherwise false
     */
    public boolean setAdmin(String username) {
        try {
            User admin = userDao.findByUsername(username);
            userDao.setAdmin(admin);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    /**
     * Class is used to create an object containing monthly statistics data for a given activity.
     * Statistics include the amount of workouts and the number of completed and uncompleted goals.
     */
    public class StatisticsNode {

        private String activity;
        private double workouts;
        private int completed;
        private int uncompleted;

        public StatisticsNode(String activity, double workouts, int completed, int uncompleted) {
            this.activity = activity;
            this.workouts = workouts;
            this.completed = completed;
            this.uncompleted = uncompleted;
        }
        
        public String getActivity() {
            return this.activity;
        }
        
        public String getWorkouts() {
            return "" + this.workouts;
        }
        
        public String getCompleted() {
            return "" + this.completed;
        }
        
        public String getUncompleted() {
            return "" + this.uncompleted;
        }
    }
}
