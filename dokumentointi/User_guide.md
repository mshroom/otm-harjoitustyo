# User guide

Download the file [sportbook.jar](https://github.com/mshroom/otm-harjoitustyo/releases/tag/week6)

## Starting the application

Start the application with the following command:

```
java -jar sportbook.jar
```

## Admin registration

The application starts with the admin registration view. If an admin account is added, the admin is able
to see a list of all users and delete user accounts. If you choose to use Sportbook without an admin account,
user accounts cannot be deleted. An admin account cannot be added afterwards.

## Login view

You will then proceed to the login view. Insert your username and password to log in.
If you do not have a personal user account, choose "Create an account".

## Registration view

In the registration view you can create an account by choosing a username and a password.
After creating an account you will be automatically logged in.

## Admin view

If you log in with the admin account, you will see a view that lists all registrated users.
To remove a user account, press the *Delete user* button.
Note that the user and all user's data will be permanently deleted from the database.
To logout, press the *Logout* button.

## Menu

The Sportbook menu gives you five choices:

- **Logout** button will log you out and open the login view.
- **Settings** button will open the settings view.
- **My workouts and goals** button will show a calendar view to see and add workouts and goals.
- **Add and remove activities** button will open a view where activities can be added and deleted.
- **Statistics** button will show your personal statistics.

## Settings view

In the settings view, you can change your username and password. You can also delete your user account.
If you press the delete button, a confirmation is asked.
Note that deleting your account will destroy all your personal data permanently. 

## My workouts and goals calendar view

The view shows all your saved goals and workouts for each day. The view opens for the current day. 
Press *Previous day* or *Next day* buttons to change the day.

Add a workout by pressing *Add a workout* button. A menu will be opened where you can choose an activity from a dropdown list and
insert the amount of accomplished unitss. Press *Save workout* to save.

If the dropdown menu for activities is empty or does not contain the activity you wish, go to **Add and remove activities**.

A goal is added similarly pressing the *Add a goal* button and giving the same data.

Saved workouts and goals will show in the lists below. You can delete a workout or a goal by pressing the *Delete* button
next to it. 

Complete a goal by pressing  the *Mark as complete* below the goal. The goal will then be shown on the left side with
other accomplished workouts. If you press a *Delete* button next to a completed goal, it will be moved back to the right
side with other uncompleted goals. To remove the goal permanently, press the *Delete* button again.

## Add and remove activities

Add an activity to the database by giving the name of the activity and the name of the units you want to use.
For example, you can create an activity "swimming" with the units "minutes" or "meters". The same activity can be
added again with different units.

To remove an activity from the database, choose the activity in the dropdown menu and press *Remove*.
If any user has saved workouts or goals for the activity, it cannot be deleted from the database unless all
saved workouts and goals are first deleted.

## Statistics view

In the statistics view you can see you personal workout statistics for each month. To change a month, 
press the *Previous month* or *Next month* buttons.

All activities for which you have saved workouts or goals in the specific month, are shown in the list below.
*All workouts* column shows the sum of the workouts you have accomplished in each activity.
*Completed goals* shows how many goals you have completed in each activity.
*Uncompleted goals* shows how many uncompleted goals there are for each activity.


