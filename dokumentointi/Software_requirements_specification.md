# Software requirements specification

## Purpose

Users are able to keep track of their workouts by setting goals and logging their physical activities.

## Users

Users can create a user account. Registered users can only see and modify their own personal data.

It is possible to create an administrator account to control the list of users.

## Features and additional ideas

### Features
* User accounts can be created and users can log in
* Users can create and view workouts
* Users can create and view goals and mark them as completed
* Workouts and goals can be deleted
* Activities can be added to database
* Users can change usernames and passwords and delete their accounts
* Users can see their statistics
* An administrator account can be created
* The administrator can see a list of all users and delete user accounts

### Possibilities for further development
* Saving a workout automatically checks if there are uncompleted goals of the same activity and marks them as completed
* Users can add new goals and workouts easily by using saved routines and workout sets
* Users can add comments to workouts and goals
* Users can save both the time and the length of the same workout. The application can then calculate and save their speed.
* etc

## System requirements

The software is a Java desktop application that can be run in Linux operating systems. All data will be saved in the computer's local drive.

## User interface and functions

The software has a graphical user interface that consists of 7 different views.

[The first draft of the user interface](https://github.com/mshroom/otm-harjoitustyo/blob/master/dokumentointi/ui_first_draft.md)
had 10 views. Some views were later combined or created as pop-up features.

The final views are:

1.  **Admin registration view** is shown when the application is launched for the first time. Here the user can decide
to create an administrator's account or to use Sportbook without an administrator's account.
2. **Login view**. Here a registered user can login and a new user can ask to create an account.
3. **Registration view**. User can either create an account or go back to login view without creating an account.
4. **Settings view**. User can select a new username or password or delete their account. A confirmation is asked
before the user account is deleted.
5. **My workouts and goals view**. A calendar view that shows the current day by default. User's daily workouts and goals
are listed here. User can add and delete workouts and goals for the selected day. Goals can also be marked as complete.
There are buttons to navigate the calendar to the next and previous days.
6. **Activity view**. One can add an activity to the database by giving the name of the activity and specifying the units one wants to use. It is possible to create several activities with the same name that use different units (eg. swimming/metres and swimming/hours). Activities can also be removed from the database if there are no saved workouts or goals for the activity.
7. **Statistics view**. Here the user can see the sum of their workouts and the amount of completed and uncompleted goals for each activity that has been used during the selected month. The view shows the current month by default and there are buttons to navigate to the next and previous months.
