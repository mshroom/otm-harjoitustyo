# Software architecture

## Structure and logic

The software structure has three levels and the code is divided in three packages:

* Package *ui* contains the classes responsible for creating the JavaFX user interface.

* Package *domain* contains the classes responsible for the software logic.

* Package *dao* contains the classes responsible for storing data to database.

### Package / Class diagram

![diagram](https://github.com/mshroom/otm-harjoitustyo/blob/master/dokumentointi/images/diagram.png)

### User interface

The user interface has 8 different views:

- Admin registration view. It is shown only when the application is used for the first time and no users have registrated yet.
- User login view
- User registration view
- Admin view to list and delete users
- Settings view to change user's personal data
- Calendar view to see, add and delete daily goals and activities
- Activity view to create and delete activities from the database
- Statistics view to see user's personal monthly statistics

User interface views are Scene objects created in the classes of the *ui* package. User interface can access software functionalities and database via calling the methods in *Sportbook* class.

### Domain classes

The three domain classes describe the users, activities and users' actions. Users' actions can be accomplished workouts, unaccomplished goals or accomplished goals. Each action relates to one user and one activity:

![class diagram](https://github.com/mshroom/otm-harjoitustyo/blob/master/dokumentointi/images/diagram_domain_classes.png)

The fourth domain class Sportbook contains the essential software logic and functionalities. The user interface calls the methods in Sportbook and Sportbook can access stored data via Dao classes. Sportbook methods include user login and registration, creation and deletion of activities and actions, data modification etc.

## Storing data

All data is saved to a local database file that is created when the application is launched for the first time. The class *Database* is responsible for initializing and connecting to the database. Classes *UserDao*, *ActivityDao* and *ActionDao* contain all methods to save data to the database and fetching data from the database. The user interface has no direct access to the database. Instead, the user interface calls the methods in Sportbook class, which then calls the methods in *dao* classes.

## Main features

Some main features of the software are shown here as sequence diagrams.

### Login

In the login view, the user inputs their username and password to corresponding text fields and then clicks the loginButton.

![login diagram](https://github.com/mshroom/otm-harjoitustyo/blob/master/dokumentointi/images/diagram_login.png)

The user interface calls the **login** method in *Sportbook* class, with the username and password as parameters. *Sportbook* then calls *userDao* to find out if the username exists in the database. If the username exists, *Sportbook* checks if the given password is correct. If the username or the password is incorrect, the user interface shows an error message. Otherwise the user is logged in and the user interface will show the main scene with a welcome view that shows the username of the current user.
