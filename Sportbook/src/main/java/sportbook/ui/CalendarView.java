/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.ui;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sportbook.dao.ActionDao;
import sportbook.dao.ActivityDao;
import sportbook.dao.UserDao;
import sportbook.domain.Action;
/**
 *
 * @author minna
 */
public class CalendarView {
    
    private ActionDao actionDao;
    private Calendar calendar;
    private SimpleDateFormat simpleDate;
    private VBox workoutNodes;
    private VBox goalNodes;
    private UserDao userDao;
    private ActivityDao activityDao;
    private Label error;

    public CalendarView(ActionDao actionDao, UserDao userDao, ActivityDao activityDao, Calendar calendar) {
        this.actionDao = actionDao;
        this.userDao = userDao;
        this.activityDao = activityDao;
        this.calendar = calendar;
        this.simpleDate = new SimpleDateFormat("dd/MM/yyyy");
    }

    public Parent getView() throws SQLException {

        Button previousButton = new Button("Previous day");
        Label dateLabel = new Label(simpleDate.format(calendar.getTime()));
        Button nextButton = new Button("Next day");
        Button addWorkout = new Button("Add a workout");
        Button addGoal = new Button("Add a goal");
        Label workoutsLabel = new Label("Completed workouts");
        Label goalsLabel = new Label("Goals to complete");
        ComboBox comboBox = new ComboBox(activityDao.listActivities());
        comboBox.setVisible(false);
        comboBox.setPromptText("Choose an activity");
        TextField numberOfUnits = new TextField();
        numberOfUnits.setVisible(false);
        numberOfUnits.setPromptText("How many units?");
        Button save = new Button("Save");
        save.setVisible(false);
        error = new Label("");

        GridPane calendarGridPane = new GridPane();

        calendarGridPane.add(previousButton, 0, 0);
        calendarGridPane.add(dateLabel, 1, 0);
        calendarGridPane.add(nextButton, 2, 0);
        calendarGridPane.add(addWorkout, 0, 1);
        calendarGridPane.add(addGoal, 2, 1);
        calendarGridPane.add(comboBox, 0, 2);
        calendarGridPane.add(numberOfUnits, 1, 2);
        calendarGridPane.add(save, 2, 2);
        calendarGridPane.add(workoutsLabel, 0, 4);
        calendarGridPane.add(goalsLabel, 2, 4);
        calendarGridPane.add(error, 0, 3);

        calendarGridPane.setPrefSize(600, 380);
        calendarGridPane.setAlignment(Pos.TOP_CENTER);
        calendarGridPane.setVgap(10);
        calendarGridPane.setHgap(10);
        calendarGridPane.setPadding(new Insets(20, 20, 20, 20));

        nextButton.setOnAction((event) -> {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            dateLabel.setText(simpleDate.format(calendar.getTime()));
            drawWorkoutList();
            drawGoalList();
            comboBox.setVisible(false);
            numberOfUnits.setVisible(false);
            save.setVisible(false);
            numberOfUnits.clear();
            comboBox.setValue(comboBox.getPromptText());
        });

        previousButton.setOnAction((event) -> {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            dateLabel.setText(simpleDate.format(calendar.getTime()));
            drawWorkoutList();
            drawGoalList();
            comboBox.setVisible(false);
            numberOfUnits.setVisible(false);
            save.setVisible(false);
            numberOfUnits.clear();
            comboBox.setValue(comboBox.getPromptText());
        });

        addWorkout.setOnAction((event) -> {
            if (comboBox.isVisible()) {
                comboBox.setVisible(false);
                numberOfUnits.setVisible(false);
                save.setVisible(false);
                return;
            }
            comboBox.setVisible(true);
            numberOfUnits.setVisible(true);
            save.setText("Save workout");
            save.setVisible(true);
            save.setOnAction((event2) -> {
                try {
                    actionDao.create(userDao.getCurrentUser(), activityDao.findByToString(comboBox.getValue().toString()), Integer.parseInt(numberOfUnits.getText()), false, true, calendar.getTime());
                } catch (SQLException ex) {
                    error.setText("Problem occurred while accessing the database");
                    System.out.println(ex);
                }
                drawWorkoutList();
                drawGoalList();
                comboBox.setVisible(false);
                numberOfUnits.setVisible(false);
                save.setVisible(false);
                numberOfUnits.clear();
                comboBox.setValue(comboBox.getPromptText());
            });
        });

        addGoal.setOnAction((event) -> {
            if (comboBox.isVisible()) {
                comboBox.setVisible(false);
                numberOfUnits.setVisible(false);
                save.setVisible(false);
                return;
            }
            comboBox.setVisible(true);
            numberOfUnits.setVisible(true);
            save.setText("Save goal");
            save.setVisible(true);
            save.setOnAction((event2) -> {
                try {
                    actionDao.create(userDao.getCurrentUser(), activityDao.findByToString(comboBox.getValue().toString()), Integer.parseInt(numberOfUnits.getText()), true, false, calendar.getTime());
                } catch (SQLException ex) {
                    error.setText("Problem occurred while accessing the database");
                    System.out.println(ex);
                }
                drawWorkoutList();
                drawGoalList();
                comboBox.setVisible(false);
                numberOfUnits.setVisible(false);
                save.setVisible(false);
                numberOfUnits.clear();
                comboBox.setValue(comboBox.getPromptText());
            });
        });

        ScrollPane workoutScrollBar = new ScrollPane();
        ScrollPane goalScrollBar = new ScrollPane();

        workoutNodes = new VBox();
        drawWorkoutList();

        goalNodes = new VBox();
        drawGoalList();

        workoutScrollBar.setContent(workoutNodes);
        goalScrollBar.setContent(goalNodes);

        calendarGridPane.add(workoutScrollBar, 0, 5);
        calendarGridPane.add(goalScrollBar, 2, 5);

        return calendarGridPane;
    }

    private Node createActionNode(Action action) {
        GridPane node = new GridPane();
        Label label = new Label(action.getActivity().getName() + " " + action.getUnits() + " " + action.getActivity().getUnit());
        Button deleteButton = new Button("Delete");
        Label completedLabel = new Label("Goal completed!");
        Button completeButton = new Button("Mark as complete");

        node.add(label, 0, 0);
        node.add(deleteButton, 1, 0);

        if (action.getSetAsGoal() == true) {
            if (action.getAccomplished() == false) {
                node.add(completeButton, 0, 1);
            } else {
                node.add(completedLabel, 0, 1);
            }
        }

        deleteButton.setOnAction((event) -> {
            try {
                actionDao.delete(action);
                drawWorkoutList();
                drawGoalList();
            } catch (SQLException ex) {
                error.setText("A problem occurred while accessing the database");
            }
        });

        completeButton.setOnAction((event) -> {
            try {
                actionDao.complete(action);
                drawWorkoutList();
                drawGoalList();
            } catch (SQLException ex) {
                error.setText("A problem occurred while accessing the database");
            }
        });
        return node;
    }

    private void drawWorkoutList() {
        workoutNodes.getChildren().clear();
        try {
            List<Action> workouts = actionDao.findAllWorkoutsByUserAndDay(userDao.getCurrentUser(), calendar.getTime());
            workouts.forEach(workout -> {
            workoutNodes.getChildren().add(createActionNode(workout));
        });
        } catch (SQLException ex) {
            error.setText("A problem occurred while accessing the database");
        }
        
    }

    private void drawGoalList() {
        goalNodes.getChildren().clear();
        try {
            List<Action> goals = actionDao.findAllGoalsByUserAndDay(userDao.getCurrentUser(), calendar.getTime());
            goals.forEach(goal -> {
            goalNodes.getChildren().add(createActionNode(goal));
        });
        } catch (SQLException ex) {
            error.setText("A problem occurred while accessing the database");
        }
        
    }
}
