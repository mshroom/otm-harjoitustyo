# Software architecture

## Structure and logic

The software structure has three levels and the code is divided in three packages:

* Package *ui* contains the classes responsible for creating the JavaFX user interface.

* Package *domain* contains the classes responsible for the software logic.

* Package *dao* contains the classes responsible for storing data to database.

### Package / Class diagram

![diagram](https://github.com/mshroom/otm-harjoitustyo/blob/master/dokumentointi/images/diagram.png)

## Main features

Some main features of the software are shown here as sequence diagrams.

### Login

In the login view, the user inputs their username and password to corresponding text fields and then clicks the loginButton.

![login diagram](https://github.com/mshroom/otm-harjoitustyo/blob/master/dokumentointi/images/diagram_login.png)

The user interface calls the **login** method in *Sportbook* class, with the username and password as parameters. *Sportbook* then calls *userDao* to find out if the username exists in the database. If the username exists, *Sportbook* checks if the given password is correct. If the username or the password is incorrect, the user interface shows an error message. Otherwise the user is logged in and the user interface will show the main scene with a welcome view that shows the username of the current user.
