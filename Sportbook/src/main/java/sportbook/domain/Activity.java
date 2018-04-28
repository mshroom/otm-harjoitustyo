/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.domain;

import java.util.Objects;

/**
 * Class is used to create and control Activity objects
 * 
 * @author mshroom
 */
public class Activity implements Comparable<Activity> {
    
    private int id;
    private String name;
    private String unit;
    
    public Activity(int id, String name, String unit) {
        this.name = name;
        this.unit = unit;
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.unit);
        return hash;
    }

    /**
     * Method compares if two activities equal. Activities equal if names and units equal.
     * 
     * @param obj activity to be compared
     * 
     * @return true if activities equal, otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Activity other = (Activity) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.unit, other.unit)) {
            return false;
        }
        return true;
    }
    
    /**
     * Method creates a String that contains the name and the unit of this activity.
     */
    @Override
    public String toString() {
        return this.name + ", " + this.unit;
    }

    /**
     * Method sorts the two activities based on their names.
     */
    @Override
    public int compareTo(Activity a) {
        return this.name.compareTo(a.getName());
    }    
}
