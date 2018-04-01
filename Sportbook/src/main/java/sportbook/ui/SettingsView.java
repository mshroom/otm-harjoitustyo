/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.ui;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sportbook.dao.UserDao;
import sportbook.domain.User;
/**
 *
 * @author minna
 */
public class SettingsView {
    
    private UserDao userDao;
    private Stage stage;
    private Scene loginScene;

    public SettingsView(UserDao userDao, Stage stage, Scene loginScene) {
        this.userDao = userDao;
        this.stage = stage;
        this.loginScene = loginScene;
    }

    public Parent getView() {
        Label usernameLabel = new Label("Select a new username");
        TextField usernameField = new TextField();
        Button usernameButton = new Button("Change username");

        Label passwordLabel = new Label("Select a new password");
        PasswordField passwordField = new PasswordField();
        Button passwordButton = new Button("Change password");
        Label error = new Label("");
        Button deleteButton = new Button("Delete account");
        Label confirmationLabel = new Label("Are you sure you want to permanently delete your account?");
        confirmationLabel.setVisible(false);
        Button confirmationButton = new Button("Yes");
        confirmationButton.setVisible(false);
        Button cancelButton = new Button("Cancel");
        cancelButton.setVisible(false);

        GridPane settingsGridPane = new GridPane();

        settingsGridPane.add(usernameLabel, 0, 0);
        settingsGridPane.add(usernameField, 0, 1);
        settingsGridPane.add(usernameButton, 1, 1);
        settingsGridPane.add(passwordLabel, 0, 2);
        settingsGridPane.add(passwordField, 0, 3);
        settingsGridPane.add(passwordButton, 1, 3);
        settingsGridPane.add(error, 0, 4);
        settingsGridPane.add(deleteButton, 0, 5);
        settingsGridPane.add(confirmationLabel, 0, 6);
        settingsGridPane.add(confirmationButton, 0, 7);
        settingsGridPane.add(cancelButton, 1, 7);

        settingsGridPane.setPrefSize(300, 180);
        settingsGridPane.setAlignment(Pos.CENTER);
        settingsGridPane.setVgap(10);
        settingsGridPane.setHgap(10);
        settingsGridPane.setPadding(new Insets(20, 20, 20, 20));

        usernameButton.setOnAction((event) -> {
            try {
                User user = userDao.findByUsername(usernameField.getText().trim());
                if (user != null) {
                    error.setText("Username is already in use");
                } else {
                    userDao.changeUsername(userDao.getCurrentUser(), usernameField.getText());
                    error.setText("Username was changed");
                }
            } catch (SQLException ex) {
                error.setText("A problem occurred while accessing the database");
            }

        });

        passwordButton.setOnAction((event) -> {
            try {
                userDao.changePassword(userDao.getCurrentUser(), passwordField.getText());
                error.setText("Password was changed");
            } catch (SQLException ex) {
                System.out.println("A problem occurred while accessing the database");
            }            
        });

        deleteButton.setOnAction((event) -> {
            confirmationLabel.setVisible(true);
            confirmationButton.setVisible(true);
            cancelButton.setVisible(true);
        });

        cancelButton.setOnAction((event) -> {
            confirmationButton.setVisible(false);
            confirmationLabel.setVisible(false);
            cancelButton.setVisible(false);
        });

        confirmationButton.setOnAction((event) -> {
            try {
                userDao.delete(userDao.getCurrentUser());
                stage.setScene(loginScene);
            } catch (SQLException ex) {
                error.setText("A problem occurred while accessing the database");
            }            
        });

        return settingsGridPane;
    }

}
