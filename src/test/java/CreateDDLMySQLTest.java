import static org.junit.Assert.*;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.BeforeClass;
import org.junit.Test;

public class CreateDDLMySQLTest {
    
    private static EdgeTable[] tables;
    private static EdgeField[] fields;
    private static CreateDDLMySQL coursesDDL;
    private static Dictionary<String, Integer> dataTypes;

    @BeforeClass
    public static void setUpTables() throws Exception {
        dataTypes = new Hashtable<>();
        dataTypes.put("Varchar", 1);
        dataTypes.put("Boolean", 2);
        dataTypes.put("Integer", 3);
        dataTypes.put("Double", 4);

        // Create tables
        EdgeTable studentTable = new EdgeTable("1|STUDENT");
        EdgeTable facultyTable = new EdgeTable("2|FACULTY");
        EdgeTable coursesTable = new EdgeTable("13|COURSES");

        // Create fields
        EdgeField gradeField = new EdgeField("3|grade");
        EdgeField courseNameField = new EdgeField("4|courseName");
        EdgeField courseNumberField = new EdgeField("5|number");
        EdgeField facSSNField = new EdgeField("6|facSSN");
        EdgeField studentSSNField = new EdgeField("7|studentSSN");
        EdgeField studentNameField = new EdgeField("8|studentName");
        EdgeField facultyNameField = new EdgeField("11|facultyName");

        // Set relations between tables
        studentTable.addRelatedTable(13);
        facultyTable.addRelatedTable(13);
        coursesTable.addRelatedTable(1);
        coursesTable.addRelatedTable(2);

        // Assign fields to tables
        studentNameField.setTableID(1);
        studentSSNField.setTableID(1);
        gradeField.setTableID(1);
        facultyNameField.setTableID(2);
        facSSNField.setTableID(2);
        courseNameField.setTableID(13);
        courseNumberField.setTableID(13);

        // Bind reference fields to tables
        studentSSNField.setTableBound(13);
        courseNumberField.setTableBound(1);
        facSSNField.setTableBound(13);
        courseNumberField.setTableBound(2);

        // Bind reference fields to other reference fields
        courseNumberField.setFieldBound(studentSSNField.getNumFigure());
        courseNumberField.setFieldBound(facSSNField.getNumFigure());

        // Bind native fields to tables
        studentTable.addNativeField(studentNameField.getNumFigure());
        studentTable.addNativeField(gradeField.getNumFigure());
        facultyTable.addNativeField(facultyNameField.getNumFigure());
        coursesTable.addNativeField(courseNameField.getNumFigure());

        // Populate test data arrays
        studentTable.makeArrays();
        facultyTable.makeArrays();
        coursesTable.makeArrays();
        tables = new EdgeTable[]{studentTable, facultyTable, coursesTable};
        fields = new EdgeField[]{gradeField, courseNameField, courseNumberField, facSSNField, studentSSNField, studentNameField, facultyNameField};

        coursesDDL = new CreateDDLMySQL(tables, fields);
    }

    @Test
    public void testObjectInstantiation() {
        assertNotEquals("CreateDDLMySQL object instantiated correctly", coursesDDL.sb, null);
    }

    @Test
    public void testValidCreateAndUseDatabaseClause() {
        CreateDDLMySQL ddl = new CreateDDLMySQL(new EdgeTable[0], new EdgeField[0]);
        String output = ddl.getSQLString("test_db");
        assertTrue("Create and use clause generated correctly", 
            output.contains("CREATE DATABASE test_db") && output.contains("USE test_db"));
    }

    @Test
    public void testInvalidCreateAndUseDatabaseClause() {
        CreateDDLMySQL ddl = new CreateDDLMySQL(new EdgeTable[0], new EdgeField[0]);
        String output = ddl.getSQLString("correct_db");
        assertFalse("Create and use clause generated incorrectly", 
            output.contains("CREATE DATABASE wrong_db") || output.contains("USE wrong_db"));
    }

    @Test
    public void testVariableTypes() {
        EdgeTable[] tables = new EdgeTable[]{new EdgeTable("1|table")};
        EdgeField[] fields = new EdgeField[]{
            new EdgeField("2|varchar_field"),
            new EdgeField("3|boolean_field"),
            new EdgeField("4|integer_field"),
            new EdgeField("5|double_field"),
        };
        fields[0].setTableBound(1);
        fields[0].setDataType(0);
        fields[1].setTableBound(1);
        fields[1].setDataType(1);
        fields[2].setTableBound(1);
        fields[2].setDataType(2);
        fields[3].setTableBound(1);
        fields[3].setDataType(3);
        tables[0].addNativeField(2);
        tables[0].addNativeField(3);
        tables[0].addNativeField(4);
        tables[0].addNativeField(5);
        tables[0].makeArrays();
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("test");
        assertTrue("Data types are correctly identified", 
            output.contains("varchar_field VARCHAR") &&
            output.contains("boolean_field BOOL") &&
            output.contains("integer_field INT") &&
            output.contains("double_field DOUBLE"));
    }

    @Test
    public void testValidVarcharLengthConstraint() {
        EdgeTable[] tables = new EdgeTable[]{new EdgeTable("1|table")};
        EdgeField[] fields = new EdgeField[]{new EdgeField("2|varchar_field")};
        fields[0].setTableBound(1);
        fields[0].setVarcharValue(20);
        tables[0].addNativeField(2);
        tables[0].makeArrays();
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("test");
        assertTrue("Primary key clause generated correctly", 
            output.contains("varchar_field VARCHAR(20)"));
    }

    @Test
    public void testInvalidVarcharLengthConstraint() {
        EdgeTable[] tables = new EdgeTable[]{new EdgeTable("1|table")};
        EdgeField[] fields = new EdgeField[]{new EdgeField("2|varchar_field")};
        fields[0].setTableBound(1);
        fields[0].setVarcharValue(99);
        tables[0].addNativeField(2);
        tables[0].makeArrays();
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("test");
        assertFalse("Primary key clause generated correctly", 
            output.contains("varchar_field VARCHAR(20)"));
    }

    @Test
    public void testPrimaryKeyClause() {
        EdgeTable[] tables = new EdgeTable[]{new EdgeTable("1|table")};
        EdgeField[] fields = new EdgeField[]{new EdgeField("2|primary_key")};
        fields[0].setTableBound(1);
        fields[0].setIsPrimaryKey(true);
        tables[0].addNativeField(2);
        tables[0].makeArrays();
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("test");
        assertTrue("Primary key clause generated correctly", 
            output.contains("PRIMARY KEY (primary_key)"));
    }

    @Test
    public void testNonNullClause() {
        EdgeTable[] tables = new EdgeTable[]{new EdgeTable("1|table")};
        EdgeField[] fields = new EdgeField[]{new EdgeField("2|non_null_field")};
        fields[0].setTableBound(1);
        fields[0].setDisallowNull(true);
        tables[0].addNativeField(2);
        tables[0].makeArrays();
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("test");
        assertTrue("Non null clause generated correctly", 
            output.contains("non_null_field") && output.contains("NOT NULL"));
    }

    @Test
    public void testDefaultValueClause() {
        EdgeTable[] tables = new EdgeTable[]{new EdgeTable("1|table")};
        EdgeField[] fields = new EdgeField[]{new EdgeField("2|default_value_field")};
        fields[0].setTableBound(1);
        fields[0].setDefaultValue("A default value");
        tables[0].addNativeField(2);
        tables[0].makeArrays();
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("test");
        assertTrue("Default value clause generated correctly", 
            output.contains("default_value_field") && output.contains("DEFAULT A default value"));
    }

}
