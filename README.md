# Team-Square-Project

## Repository Link

###### [Team-Square-Project](https://github.com/btf9895/Team-Square-Project)

---

## Classes Tested

CreateDDLMySQLTest - Ben
EdgeTabelTest - Max
EdgeConvertCreateDDLTest - Taylor

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

###### EdgeTable:

    EdgeTable Creation:
        - Given a new EdgeTable, when created with numFigure 42 and name "TestTable", then
            - The numFigure should be 42
            - The name should be "TestTable"
            - Native fields and related tables should be empty initially.

    Native Field Operations:
        - Adding Native Fields:
            - Given an EdgeTable, when adding native fields with numbers 42, 24, 15, 37, and 9, then
            - The native fields array should contain these values in the specified order.
        - Getting Native Fields:
            - Given an EdgeTable with native fields added, when calling getNativeFieldsArray(), then
            - The returned array should match the order and values of added native fields.
        - Adding Additional Native Fields:
            - Given an EdgeTable with native fields, when adding new native fields with numbers 55 and 18, then
            - The native fields array should contain these new values in the specified order.

    Related Table Operations:
        - Adding Related Tables:
            - Given an EdgeTable, when adding related tables with numbers 20, 10, 35, 50, and 5, then
            - The related tables array should contain these values in the specified order.
        - Getting Related Tables:
            - Given an EdgeTable with related tables added, when calling getRelatedTablesArray(), then
            - The returned array should match the order and values of added related tables.
        - Adding Additional Related Tables:
            - Given an EdgeTable with related tables, when adding new related tables with numbers 60 and 70, then
            - The related tables array should contain these new values in the specified order.

    Field Movement Operations:
        - Moving Fields Up:
            - Given an EdgeTable with related fields ordered as [1, 2, 3, 4, 5], when moving field 1 up, then
            - Field 1 should move to the second position, resulting in [2, 1, 3, 4, 5].
            - Given an EdgeTable with related fields ordered as [2, 1, 3, 4, 5], when moving field 3 up, then
            - Field 3 should move to the second position, resulting in [2, 3, 1, 4, 5].
        - Moving Fields Down:
            - Given an EdgeTable with related fields ordered as [1, 2, 3, 4, 5], when moving field 4 down, then
            - Field 4 should move to the fourth position, resulting in [1, 2, 3, 5, 4].
            - Given an EdgeTable with related fields ordered as [2, 3, 1, 4, 5], when moving field 2 down, then
            - Field 2 should move to the third position, resulting in [2, 3, 4, 1, 5].

    Array Creation and Verification:
        - Given an EdgeTable with native fields and related tables, when calling makeArrays(), then
            - The native fields array and related tables array should be populated correctly.

    Additional Scenarios:
        - Invalid Operations:
            - Given an EdgeTable, when attempting invalid operations like moving non-existent fields, then
            - The EdgeTable should handle these gracefully, not causing errors or unexpected behavior.

    ToString Method:
        - Given an EdgeTable, when calling its toString() method, then
            - The resulting string representation should contain relevant information about the EdgeTable, including numFigure and name.

    Edge Cases:
        - Testing with Empty Values:
            - Given an EdgeTable with empty values (numFigure = 0, name = ""), then
            - The EdgeTable should be able to handle and store these values without errors.

###### EdgeConvertCreateDDL:

## Test Plan for `EdgeConvertCreateDDLTest`

### 1. `initialize() method`

#### Objective

To test the initialization of various components in `EdgeConvertCreateDDL`.

#### Expected Outcomes

- `numBoundTables` should be initialized to the correct length matching `edgeTables.length`.
- `maxBound` should be initialized to 0.
- `sb` (StringBuilder) should be initialized to an empty string.
- `getRelatedFieldsArray()` should be called for each table and should return an array of length 0 for both tables since there are no related fields.

### 2. `getTable()`

#### Objective

To test the retrieval of tables based on their `numFigure`.

#### Expected Outcomes

- The method `getTable(1)` should return the first table in `edgeTables`.
- The method `getTable(2)` should return the second table in `edgeTables`.

### 3. `getField()`

#### Objective

To test the retrieval of fields based on their `numFigure`.

#### Expected Outcomes

- The method `getField(1)` should return the first field in `edgeFields`.
- The method `getField(2)` should return the second field in `edgeFields`.

### 4. `getDatabaseName()`

#### Objective

To test the `getDatabaseName` method.

#### Expected Outcome

Since this is an abstract method, the test class implimentation will return `null`. Therefore, the method should return `null`.

### 4. `getProductName()`

#### Objective

To test the `getProductName` method.

#### Expected Outcome

Since this is an abstract method, the test class implimentation will return `null`. Therefore, the method should return `null`.

### 6. `getSQLString()`

#### Objective

To test the `getSQLString` method.

#### Expected Outcome

Since this is an abstract method, the test class implimentation will return `null`. Therefore, the method should return `null`.

### 7. `createDDL()`

#### Objective

To test the `createDDL` method.

#### Expected Outcome

Method should execute without errors. Specific behavior needs to be detailed based on the method implementation.

### 8. `toString()`

#### Objective

To test the `toString` method of the object.

#### Expected Outcome

The method should return a string representation of the `EdgeConvertCreateDDL` object, including details about tables, fields, `numBoundTables`, `maxBound`, `sb`, and `selected`.
