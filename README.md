# PersonalFinances

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Main user interface](#main-user-interface)

## General info
The main purpose of the application, in addition to allowing the user to save and read both credits and debits, is an algorithm that facilitates the planning of long-term expenses, as well as the ability to analyze finances based on graphs.

## Technologies
* Java 14,
* libraries:
            - javafx-sdk-14.0.1,
            - mysql-connector-java:8.0.22,
            - ormlite-jdbc:4.48.

## Setup
If compilation failed, the localized path to the javafx-sdk library is probably missing. It should be downloaded from the official website and added in the tab "configurations" -> "VM options".

In my case:
"--module-path
//Users/maciekfladzinski/Documents/javafx-sdk-14.0.1/lib
--add-modules
javafx.controls, javafx.fxml ".

## Main user interface
![debits img](https://github.com/MaciejFladzinski/PersonalFinances/blob/master/img%20(UI)/Debits.png "Debits:")

![credits img](https://github.com/MaciejFladzinski/PersonalFinances/blob/master/img%20(UI)/Credits.png "Credits:")

![planning expenses img](https://github.com/MaciejFladzinski/PersonalFinances/blob/master/img%20(UI)/Planning%20expenses.png "Planning expenses:")

![statistics img](https://github.com/MaciejFladzinski/PersonalFinances/blob/master/img%20(UI)/Statistics.png "Statistics:")

![settings img](https://github.com/MaciejFladzinski/PersonalFinances/blob/master/img%20(UI)/Settings.png "Settings:")
