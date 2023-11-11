import static org.junit.Assert.*;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.BeforeClass;
import org.junit.Test;

public class CreateDDLMySQLTest {
    
    private static EdgeTable[] tables;
    private static EdgeField[] fields;

    private static CreateDDLMySQL coursesDDL;
    private static CreateDDLMySQL emptyDDL;
    private static CreateDDLMySQL verboseDDL;

    @BeforeClass
    public static void setUpTables() throws Exception {
        Dictionary<String, Integer> dataTypes = new Hashtable<>();
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
        emptyDDL = new CreateDDLMySQL(new EdgeTable[0], new EdgeField[0]);

        EdgeTable table1 = new EdgeTable("1|table1");

        EdgeField pk = new EdgeField("2|primary_key");

        EdgeField nn = new EdgeField("3|non_null");
        nn.setDataType(dataTypes.get("Varchar"));

        EdgeField dv = new EdgeField("4}|default_value");
        dv.setDataType(dataTypes.get("Varchar"));
        dv.setDefaultValue("Example Default Value");

        EdgeTable table2 = new EdgeTable("99|table2");

        verboseDDL = new CreateDDLMySQL(new EdgeTable[]{table1, table2}, new EdgeField[]{});
    }

    // @Test
    // public void testDDL() {
    //     for (int i = 0; i < fields.length; i++) {
    //         System.out.println(fields[i].toString());
    //     }
    //     for (int i = 0; i < tables.length; i++) {
    //         System.out.println(tables[i].toString());
    //     }
        // System.out.println(verboseDDL.getSQLString("courses"));
    // }

    @Test
    public void testObjectInstantiation() {
        assertNotEquals("CreateDDLMySQL object instantiated correctly", coursesDDL.sb, null);
    }

    @Test
    public void testValidCreateAndUseDatabaseClause() {
        String output = emptyDDL.getSQLString("test_db");
        assertTrue("Create and use clause generated correctly", output.contains("CREATE DATABASE test_db") && output.contains("USE test_db"));
    }

    @Test
    public void testInvalidCreateAndUseDatabaseClause() {
        String output = emptyDDL.getSQLString("correct_db");
        assertFalse("Create and use clause generated incorrectly", output.contains("CREATE DATABASE wrong_db") || output.contains("USE wrong_db"));
    }

    @Test
    public void testPrimaryKeyClause() {
        EdgeTable table = new EdgeTable("1|table1");
        EdgeField pk = new EdgeField("2|primary_key");
        pk.setTableBound(1);
        table.makeArrays();
        // assertTrue("Create clause generated correctly", )
    }

}
