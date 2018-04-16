/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.ui;

import sportbook.domain.Sportbook;
import sportbook.dao.Database;
import sportbook.dao.UserDao;
import sportbook.dao.ActivityDao;
import sportbook.dao.ActionDao;
import java.util.Calendar;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


/**
 *
 * @author minna
 */
public class Main extends Application {

    static Sportbook sportbook;
    static Calendar calendar;
    Scene loginScene;
    Scene registerScene;
    Scene mainScene;
    Scene welcomeScene;
    Label welcomeLabel;
    BorderPane mainBorderPane;
    StackPane welcomeStackPane;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Sportbook");

        // Creating login scene
        
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Label passwordLabel = new Label("Password:");
        Button loginButton = new Button("Login");
        Label error = new Label("");
        Button registerButton = new Button("Create an account");

        GridPane loginGridPane = new GridPane();

        loginGridPane.add(usernameLabel, 0, 0);
        loginGridPane.add(usernameField, 0, 1);
        loginGridPane.add(passwordLabel, 0, 2);
        loginGridPane.add(passwordField, 0, 3);
        loginGridPane.add(loginButton, 0, 4);
        loginGridPane.add(error, 0, 5);
        loginGridPane.add(registerButton, 0, 6);

        loginGridPane.setPrefSize(300, 180);
        loginGridPane.setAlignment(Pos.CENTER);
        loginGridPane.setVgap(10);
        loginGridPane.setHgap(10);
        loginGridPane.setPadding(new Insets(20, 20, 20, 20));

        loginButton.setOnAction((event) -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            int result = sportbook.login(username, password);
            if (result == 1) {
                error.setText("Unknown username");
                return;
            } else if (result == 2) {
                error.setText("Password is not valid");
                passwordField.clear();
                return;
            } else if (result == 3) {
                welcomeLabel.setText("Welcome " + sportbook.getLoggedIn().getUsername() + "!");
                mainBorderPane.setCenter(welcomeStackPane);
                usernameField.clear();
                passwordField.clear();
                stage.setScene(mainScene);
            }
        });

        registerButton.setOnAction((event) -> {
            stage.setScene(registerScene);
        });

        loginScene = new Scene(loginGridPane);

        // Creating register scene
        
        Label usernameLabel2 = new Label("Choose a username:");
        TextField usernameField2 = new TextField();
        PasswordField passwordField2 = new PasswordField();
        Label passwordLabel2 = new Label("Choose a password:");
        Button registerButton2 = new Button("Create an account");
        Label error2 = new Label("");
        Button backButton = new Button("Back");

        GridPane registerGridPane = new GridPane();

        registerGridPane.add(usernameLabel2, 0, 0);
        registerGridPane.add(usernameField2, 0, 1);
        registerGridPane.add(passwordLabel2, 0, 2);
        registerGridPane.add(passwordField2, 0, 3);
        registerGridPane.add(registerButton2, 0, 4);
        registerGridPane.add(error2, 0, 5);
        registerGridPane.add(backButton, 0, 6);

        registerGridPane.setPrefSize(300, 180);
        registerGridPane.setAlignment(Pos.CENTER);
        registerGridPane.setVgap(10);
        registerGridPane.setHgap(10);
        registerGridPane.setPadding(new Insets(20, 20, 20, 20));

        registerButton2.setOnAction((event) -> {
            String username = usernameField2.getText().trim();
            String password = passwordField2.getText().trim();
            int result = sportbook.register(username, password);
            if (result == 1) {
                error2.setText("Username is already in use");
                return;
            } else if (result == 2) {
                welcomeLabel.setText("Welcome " + sportbook.getLoggedIn().getUsername() + "!");
                mainBorderPane.setCenter(welcomeStackPane);
                usernameField2.clear();
                passwordField2.clear();
                stage.setScene(mainScene);
            }
        });

        backButton.setOnAction((event) -> {
            stage.setScene(loginScene);
        });

        registerScene = new Scene(registerGridPane);

        // Creating main scene        
        mainBorderPane = new BorderPane();

        // Creating welcome view        
        welcomeLabel = new Label("Welcome " + sportbook.getLoggedIn().getUsername() + "!");
        welcomeStackPane = new StackPane();

        welcomeStackPane.setPrefSize(300, 180);
        welcomeStackPane.getChildren()
                .add(welcomeLabel);
        welcomeStackPane.setAlignment(Pos.CENTER);

        // Creating settings view
        SettingsView settingsView = new SettingsView(sportbook, stage, loginScene);

        // Creating calendar view
        CalendarView calendarView = new CalendarView(sportbook, calendar);

        // Creating activity view
        ActivityView activityView = new ActivityView(sportbook);

        // Adding menu to main scene
        HBox menu = new HBox();
        menu.setPadding(new Insets(20, 20, 20, 20));
        menu.setSpacing(10);

        Button logoutButton = new Button("Logout");
        Button settingsButton = new Button("Settings");
        Button calendarButton = new Button("My workouts and goals");
        Button activityButton = new Button("Add and remove activities");
        menu.getChildren().addAll(logoutButton, settingsButton, calendarButton, activityButton);
        mainBorderPane.setTop(menu);

        logoutButton.setOnAction((event) -> {
            stage.setScene(loginScene);
        });

        settingsButton.setOnAction((event) -> mainBorderPane.setCenter(settingsView.getView()));

        calendarButton.setOnAction((event) -> {
            mainBorderPane.setCenter(calendarView.getView());
        });

        activityButton.setOnAction((event) -> {
            mainBorderPane.setCenter(activityView.getView());
        });

        // Setting main scene to start with welcome view
        mainBorderPane.setCenter(welcomeStackPane);
        mainScene = new Scene(mainBorderPane, 800, 600);

        // Showing application
        stage.setScene(loginScene);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        
        // Preparing database
        Database database = new Database("jdbc:sqlite:sportbookdata.db");
        database.init();
        
        // Preparing calendar and sportbook
        calendar = Calendar.getInstance();
        UserDao userDao = new UserDao(database);
        ActivityDao activityDao = new ActivityDao(database);
        ActionDao actionDao = new ActionDao(database, activityDao, userDao);
        sportbook = new Sportbook(userDao, activityDao, actionDao);

        launch(Main.class);
    }

}
