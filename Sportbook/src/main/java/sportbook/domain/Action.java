/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.domain;

import java.util.Date;

/**
 *
 * @author minna
 */
public class Action implements Comparable<Action> {
    private int id;
    private int units;
    private Boolean accomplished;
    private Boolean setAsGoal;
    private User user;
    private Activity activity;
    private Date date;
    
    public Action(int id, User user, Activity activity, Integer units, Boolean setAsGoal, Boolean accomplished, Date date) {
        this.id = id;
        this.user = user;
        this.activity = activity;
        this.units = units;
        this.setAsGoal = setAsGoal;
        this.accomplished = accomplished;
        this.date = date;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public Boolean getAccomplished() {
        return accomplished;
    }

    public void setAccomplished(Boolean accomplished) {
        this.accomplished = accomplished;
    }

    public Boolean getSetAsGoal() {
        return setAsGoal;
    }

    public void setSetAsGoal(Boolean setAsGoal) {
        this.setAsGoal = setAsGoal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    
    public Date getDate() {
        return this.date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(Action a) {
        return this.activity.compareTo(a.activity);
    }
}
