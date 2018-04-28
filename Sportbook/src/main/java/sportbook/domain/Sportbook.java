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
 *
 * @author minna
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

    public boolean deleteAccount() {
        try {
            userDao.delete(this.loggedIn);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }
    
    public boolean deleteAccount(User user) {
        try {
            userDao.delete(user);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }

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

    public boolean deleteAction(Action action) {
        try {
            actionDao.delete(action);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }

    public boolean completeAction(Action action) {
        try {
            actionDao.complete(action);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }

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

    public List<User> getUsers() {
        try {
            return userDao.findAll();
        } catch (SQLException ex) {
            return null;
        }
    }

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

    public boolean setAdmin(String username) {
        try {
            User admin = userDao.findByUsername(username);
            userDao.setAdmin(admin);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

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
