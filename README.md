# Team-Square-Project

## Repository Link

###### [Team-Square-Project](https://github.com/btf9895/Team-Square-Project)

---

## Classes Tested

CreateDDLMySQL - Ben

---

## Compile and Run Instructions

(...)

---

## Test Plan

###### CreateDDLMySQL:
- createDDL()
    - Create database/use database statement
        - Given a database name, the create database and use database statement should appear
    - Create table statement
        - Given a table name, the create table statement should appear
    - Primary key
        - If an EdgeField is set as a primary key, the primary key constraint should appear
    - Foreign key
        - If an EdgeField is set as a foreign key, the foreign key constraint should appear and list the referenced field
        - [NEEDS FIXING] If an EdgeField is set as a foreign key, if the referenced field is in the same table, the foreign key constraint should not be generated
    - Non null values
        - If an EdgeField is set as a non null value, the non null clause should appear next to the name of the field
    - Default values
        - If an EdgeField is given a default value, the default value clause should appear next to the name of the variable
    - Variable types
        - If a variable is given a variable type of 1, the variable should be of type VARCHAR
        - If a variable is given a variable type of 2, the variable should be of type BOOL
        - If a variable is given a variable type of 3, the variable should be of type INT
        - If a variable is given a variable type of 4, the variable should be of type DOUBLE
        - If a variable is declared as type BOOL, if a default value of "notaboolean" is given, a default value should be given
        - [NEEDS FIXING] If a variable is declared as type INT, if a default value of "notanint" is given, a default value should be given
        - If a variable is declared as type VARCHAR, if a varchar length is specified, it should appear in parentheses next to VARCHAR