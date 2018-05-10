# Testing the software

Both automatic JUnit tests and manual testing has been used to test the software.

## UNit tests

There are test classes for each *domain* and *dao* class. Tests cover all main features of the software.
The most crucial tests are in the class SportbookTest. 

While testing methods that save data, a temporary database is created. The temporary database is deleted after each test.

### Test coverage

Tests cover c. 90 % of the code, excluding the user interface classes.

## Manual testing

### Installing and launching the application

The application has been tested in a Linux environment, following the steps in the User Guide.

Starting the application has been tested both with and without a pre-existing database.

When the application is launched for the first time, it lets the user to create an admin account or to use
the application without an admin account. Both possibilities have been tested.

### Software features

All the features listed in the software requirements specification document have been tested.
All situations where user types text or numbers to input data, have been tested with empty or invalid values.





