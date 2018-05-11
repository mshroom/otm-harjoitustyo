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
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sportbook.domain.User;

/**
 * Main class creates the user interface and starts the application.
 * 
 * @author mshroom
 */
public class Main extends Application {

    static Sportbook sportbook;
    static Calendar calendar;
    Scene loginScene;
    Scene registerScene;
    Scene registerAdminScene;
    Scene adminScene;
    Scene mainScene;
    Scene welcomeScene;
    Label welcomeLabel;
    Label welcomeAdminLabel;
    BorderPane mainBorderPane;
    StackPane welcomeStackPane;
    VBox userNodes;
    Label launchError;

    /**
     * Method creates the initial scenes and shows the application.
     * 
     * @param stage 
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Sportbook");

        // Creating admin registration scene
        Label instructionLabel = new Label("Welcome to use Sportbook!");
        Label instructionLabel2 = new Label("Start by creating an administrator's account. An administrator can see and delete user accounts.");
        Label instructionLabel3 = new Label("You will also need a regular user account to use Sportbook.");
        Label adminNameLabel = new Label("Choose an administrator username:");
        TextField adminNameField = new TextField();
        PasswordField adminPasswordField = new PasswordField();
        Label adminPasswordLabel = new Label("Choose a password:");
        Button registerAdminButton = new Button("Create an administrator's account");
        Label proceedLabel = new Label("You can also use Sportbook without an administor's account. An administrator cannot be added later.");
        Button proceedButton = new Button("Proceed to Sportbook without creating an administrator's account.");
        launchError = new Label("");

        GridPane registerAdminGridPane = new GridPane();

        registerAdminGridPane.add(instructionLabel, 0, 0);
        registerAdminGridPane.add(instructionLabel2, 0, 5);
        registerAdminGridPane.add(instructionLabel3, 0, 6);
        registerAdminGridPane.add(adminNameLabel, 0, 11);
        registerAdminGridPane.add(adminNameField, 0, 12);
        registerAdminGridPane.add(adminPasswordLabel, 0, 13);
        registerAdminGridPane.add(adminPasswordField, 0, 14);
        registerAdminGridPane.add(registerAdminButton, 0, 15);
        registerAdminGridPane.add(launchError, 0, 16);
        registerAdminGridPane.add(proceedLabel, 0, 17);
        registerAdminGridPane.add(proceedButton, 0, 19);

        registerAdminGridPane.setAlignment(Pos.CENTER);
        registerAdminGridPane.setVgap(10);
        registerAdminGridPane.setHgap(10);
        registerAdminGridPane.setPadding(new Insets(20, 20, 20, 20));

        registerAdminButton.setOnAction((event) -> {
            String username = adminNameField.getText().trim();
            String password = adminPasswordField.getText().trim();
            sportbook.register(username, password);
            adminNameField.clear();
            adminPasswordField.clear();
            sportbook.setAdmin(username);
            stage.setScene(loginScene);

        });

        proceedButton.setOnAction((event) -> {
            stage.setScene(loginScene);
        });

        registerAdminScene = new Scene(registerAdminGridPane);

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
                usernameField.clear();
                passwordField.clear();
                if (sportbook.getLoggedIn().getAdmin()) {
                    welcomeAdminLabel.setText("Welcome " + sportbook.getLoggedIn().getUsername() + "! You are logged in as an administrator.");
                    drawUserList();
                    stage.setScene(adminScene);
                } else {
                    welcomeLabel.setText("Welcome " + sportbook.getLoggedIn().getUsername() + "!");
                    mainBorderPane.setCenter(welcomeStackPane);
                    stage.setScene(mainScene);
                }
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

        // Creating admin scene        
        welcomeAdminLabel = new Label("Welcome " + sportbook.getLoggedIn().getUsername() + "! You are logged in as an administrator.");
        Label listLabel = new Label("Here is a list of all users");
        Label adminError = new Label("");
        Button logoutAdminButton = new Button("Logout");

        GridPane upperGridPane = new GridPane();
        upperGridPane.add(welcomeAdminLabel, 0, 0);
        upperGridPane.add(adminError, 0, 2);
        upperGridPane.add(listLabel, 0, 3);
        upperGridPane.add(logoutAdminButton, 1, 0);

        upperGridPane.setVgap(10);
        upperGridPane.setHgap(10);
        upperGridPane.setPadding(new Insets(20, 20, 20, 20));

        logoutAdminButton.setOnAction((event) -> {
            stage.setScene(loginScene);
        });

        Label idLabel = new Label("User id");
        Label nameLabel = new Label("Username");
        Label roleLabel = new Label("Role");

        GridPane labelGridPane = new GridPane();
        labelGridPane.add(idLabel, 0, 0);
        labelGridPane.add(nameLabel, 1, 0);
        labelGridPane.add(roleLabel, 2, 0);

        labelGridPane.setHgap(10);
        labelGridPane.setPadding(new Insets(5, 5, 5, 5));
        labelGridPane.getColumnConstraints().add(new ColumnConstraints(60));
        labelGridPane.getColumnConstraints().add(new ColumnConstraints(200));
        labelGridPane.getColumnConstraints().add(new ColumnConstraints(50));

        ScrollPane userScrollbar = new ScrollPane();
        userNodes = new VBox();
        if (!drawUserList()) {
            adminError.setText("A problem occurred while accessing the database");
        }
        userScrollbar.setContent(userNodes);

        GridPane scrollGridPane = new GridPane();
        scrollGridPane.add(userScrollbar, 0, 0);

        GridPane adminGridPane = new GridPane();
        adminGridPane.add(upperGridPane, 0, 0);
        adminGridPane.add(labelGridPane, 0, 1);
        adminGridPane.add(scrollGridPane, 0, 3);

        adminGridPane.setAlignment(Pos.TOP_CENTER);
        adminGridPane.setVgap(10);
        adminGridPane.setHgap(10);
        adminGridPane.setPadding(new Insets(20, 20, 20, 20));

        adminScene = new Scene(adminGridPane);

        // Creating main scene        
        mainBorderPane = new BorderPane();

        // Creating welcome view        
        welcomeLabel = new Label("Welcome " + sportbook.getLoggedIn().getUsername() + "!");
        welcomeStackPane = new StackPane();

        welcomeStackPane.setPrefSize(800, 600);
        welcomeStackPane.getChildren().add(welcomeLabel);
        welcomeStackPane.setAlignment(Pos.CENTER);

        // Creating settings view
        SettingsView settingsView = new SettingsView(sportbook, stage, loginScene);

        // Creating calendar view
        CalendarView calendarView = new CalendarView(sportbook, calendar);

        // Creating activity view
        ActivityView activityView = new ActivityView(sportbook);

        // Creating statistics view
        StatisticsView statisticsView = new StatisticsView(sportbook, calendar);

        // Adding menu to main scene
        HBox menu = new HBox();
        menu.setPadding(new Insets(20, 20, 20, 20));
        menu.setSpacing(10);

        Button logoutButton = new Button("Logout");
        Button settingsButton = new Button("Settings");
        Button calendarButton = new Button("My workouts and goals");
        Button activityButton = new Button("Add and remove activities");
        Button statisticsButton = new Button("Statistics");
        menu.getChildren().addAll(logoutButton, settingsButton, calendarButton, activityButton, statisticsButton);
        mainBorderPane.setTop(menu);

        logoutButton.setOnAction((event) -> {
            stage.setScene(loginScene);
        });

        settingsButton.setOnAction((event) -> mainBorderPane.setCenter(settingsView.getView()));

        calendarButton.setOnAction((event) -> mainBorderPane.setCenter(calendarView.getView()));

        activityButton.setOnAction((event) -> mainBorderPane.setCenter(activityView.getView()));

        statisticsButton.setOnAction((event) -> mainBorderPane.setCenter(statisticsView.getView()));

        // Setting main scene to start with welcome view
        mainBorderPane.setCenter(welcomeStackPane);
        mainScene = new Scene(mainBorderPane, 800, 600);

        // Showing application
        if (sportbook.hasUsers()) {
            stage.setScene(loginScene);
        } else {
            stage.setScene(registerAdminScene);
        }
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
    
    /**
     * Method refreshes the list of users.
     * 
     * @return true if there are users on the list, false if there are no users
     */
    private boolean drawUserList() {
        userNodes.getChildren().clear();
        List<User> users = sportbook.getUsers();
        if (users != null) {
            users.forEach(user -> {
                userNodes.getChildren().add(createUserNode(user));
            });
            return true;
        }
        return false;
    }

    /**
     * Method creates a user node to be listed in the admin view
     * with information and delete buttons for each user.
     * 
     * @param user User object to be listed
     * @return node to be shown on the list
     */
    private Node createUserNode(User user) {
        GridPane node = new GridPane();
        Label idLabel = new Label("" + user.getId());
        Label nameLabel = new Label(user.getUsername());
        Label adminLabel = new Label("user");
        if (user.getAdmin()) {
            adminLabel.setText("admin");
        }
        Button deleteUserButton = new Button("delete user");

        node.add(idLabel, 0, 0);
        node.add(nameLabel, 1, 0);
        node.add(adminLabel, 2, 0);
        if (!user.getAdmin()) {
            node.add(deleteUserButton, 3, 0);
        }

        node.setHgap(10);
        node.setPadding(new Insets(5, 5, 5, 5));
        node.getColumnConstraints().add(new ColumnConstraints(60));
        node.getColumnConstraints().add(new ColumnConstraints(200));
        node.getColumnConstraints().add(new ColumnConstraints(50));
        node.getColumnConstraints().add(new ColumnConstraints(100));

        deleteUserButton.setOnAction((event) -> {
            if (sportbook.deleteAccount(user)) {
                drawUserList();
            } else {
                launchError.setText("A problem occurred while accessing the database");
            }
        });
        return node;

    }

}
