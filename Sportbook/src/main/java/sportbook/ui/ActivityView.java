/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.ui;

import sportbook.domain.Sportbook;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


/**
 *
 * @author minna
 */
public class ActivityView {

    private Sportbook sportbook;
    private ComboBox comboBox;

    public ActivityView(Sportbook sportbook) {
        this.sportbook = sportbook;
    }

    public Parent getView() {

        Label nameLabel = new Label("Create a new activity");
        TextField nameField = new TextField();
        nameField.setPromptText("Name of the activity");
        Label unitsLabel = new Label("Which units are to be used with this activity?");
        TextField unitsField = new TextField();
        unitsField.setPromptText("Name of units");
        Button createButton = new Button("Create");
        Label deleteLabel = new Label("Remove activity from the list");
        comboBox = new ComboBox(sportbook.listActivities());
        comboBox.setPromptText("Choose an activity");
        Button deleteButton = new Button("Remove");
        Label error = new Label("");

        GridPane activityGridPane = new GridPane();

        activityGridPane.add(nameLabel, 0, 0);
        activityGridPane.add(nameField, 0, 1);
        activityGridPane.add(unitsLabel, 0, 2);
        activityGridPane.add(unitsField, 0, 3);
        activityGridPane.add(createButton, 1, 3);
        activityGridPane.add(deleteLabel, 0, 5);
        activityGridPane.add(comboBox, 0, 6);
        activityGridPane.add(deleteButton, 1, 6);
        activityGridPane.add(error, 0, 8);

        activityGridPane.setPrefSize(300, 180);
        activityGridPane.setAlignment(Pos.CENTER);
        activityGridPane.setVgap(10);
        activityGridPane.setHgap(10);
        activityGridPane.setPadding(new Insets(20, 20, 20, 20));

        createButton.setOnAction((event) -> {
            String name = nameField.getText().trim();
            String units = unitsField.getText().trim();
            int result = sportbook.createActivity(name, units);
            if (result == 1) {
                error.setText("Activity already exists");
            } else if (result == 2) {
                error.setText("Activity was created");
                nameField.clear();
                unitsField.clear();
                this.refreshList();
            } else if (result == 3) {
                error.setText("Problem occurred while accessing the database.");
            }
        });

        deleteButton.setOnAction((event) -> {
            String activity = comboBox.getValue().toString();
            int result = sportbook.deleteActivity(activity);
            if (result == 1) {
                error.setText("Activity was removed");
                comboBox.setValue(comboBox.getPromptText());
                nameField.clear();
                unitsField.clear();
                this.refreshList();
            } else if (result == 2) {
                error.setText("Activity is in use and cannot be removed.");
                nameField.clear();
                unitsField.clear();
            } else if (result == 3) {
                error.setText("Problem occurred while accessing the database.");
            }            
        });
        return activityGridPane;
    }

    private void refreshList() {
        comboBox.getItems().clear();
        ObservableList<String> list = sportbook.listActivities();
        list.forEach(a -> {
            comboBox.getItems().add(a);
        });
    }
}
