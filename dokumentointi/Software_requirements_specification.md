# Software requirements specification

## Purpose

Users are able to keep track of their workouts by setting goals and logging their physical activities.

## Users

Users can create a user account. Registered users can only see and modify their own personal data.

## Features and additional ideas

### Most crucial features
* User accounts can be created and users can log in
* Users can create and view workouts

### The next important features
* Users can create and view goals and mark them as completed
* Workouts and goals can be deleted
* Activities can be added to database
* Users can change usernames and passwords and user accounts can be deleted
* Users can see their statistics

### Possibilities for further development
* An administrator account can be created. The administrator can see a list of all users and delete user accounts.
* Users can add new goals and workouts easily by using saved routines and workout sets
* Users can add comments to workouts and goals
* Users can save both the time and the length of the same workout. The application can then calculate and save their speed.
* etc

## System requirements

The software is a Java desktop application that can be run in Linux operating systems. All data will be saved in the computer's local drive.

## User interface and functions: a draft

The software has a graphical user interface that consists of 10 different views:

1. **Login view**. Here a registered user can login (> User's menu) and a new user can ask to create an account (> Create account).
2. **Create account view**. User can either create an account (> Login) or go back without creating an account (> Login).
3. **User's menu**. The menu is used to navigate (> Settings, > Workouts and goals, > Statistics) or logout (> Login).
4. **Settings view**. User can select a new username or password, go back to menu (> User's menu) or delete their account (> Confirmation).
5. **Confirmation view**. A confirmation is needed to delete the account or cancel the deletion (> Settings). If confirmed, the account will be deleted permanently (> Login).
![Views 1-5](https://github.com/mshroom/otm-harjoitustyo/blob/master/dokumentointi/images/draft1.png)
6. **Workouts and goals view**. Here the user can choose a date and see their daily workouts and goals. Workouts and goals can be deleted, after which they disappear from the view. Goals can also be marked as complete. When a goal is completed, the application automatically creates a workout to match the goal's data (or updates an existing workout of the same activity). There are links to go back to menu (> User's menu) and to add a new workout (> New workout) or a new goal (> New goal) for the selected day.
7. **New workout view**. User can create a new workout by choosing an activity and entering the workout's data (eg. Running, 5000 meters). After saving or cancelling the entry one gets back to the previous view (> Workouts and goals). If the wanted activity is not in the list, it is possible to add an activity to the list (> New Activity).
8. **New goal view**. A new goal can be created just like a new workout, so these views are almost identical.
9. **New Activity view**. One can add an activity to the database by giving the name of the activity and specifying the units one wants to use. It is possible to create several activities with the same name that use different units (eg. swimming/metres and swimming/hours). One can save the changes or go back without saving (> Workouts and goals). 
![Views 6-9](https://github.com/mshroom/otm-harjoitustyo/blob/master/dokumentointi/images/draft2.png)
10. **Statistics view**. Here the user can choose a month and see the sum of their workouts and the amount of completed and uncompleted goals for each activity. There is also a link back to the menu (> User's menu).
![View 10](https://github.com/mshroom/otm-harjoitustyo/blob/master/dokumentointi/images/draft3.png)


