import static org.junit.Assert.*;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CreateDDLMySQLTest {

    private static CreateDDLMySQL emptyDDL;
    private static Dictionary<String, Integer> dataTypes;

    @BeforeClass
    public static void setUp() {
        // Instantiate data type definition and empty DDL object
        dataTypes = new Hashtable<>();
        dataTypes.put("Varchar", 0);
        dataTypes.put("Boolean", 1);
        dataTypes.put("Integer", 2);
        dataTypes.put("Double", 3);
        emptyDDL = new CreateDDLMySQL(new EdgeTable[0], new EdgeField[0]);
    }

    @AfterClass
    public static void close() {
        // Release resources to garbage collector
        emptyDDL = null;
        dataTypes = null;
        System.gc();
    }

    @Test
    public void testObjectInstantiation() {
        assertNotEquals("CreateDDLMySQL object instantiated correctly", emptyDDL.sb, null);
    }

    @Test
    public void testGetProductName() {
        assertTrue("Product name is correct", emptyDDL.getProductName().equals("MySQL"));
    }

    @Test
    public void testValidCreateAndUseDatabaseClause() {
        String output = emptyDDL.getSQLString("test_db");
        assertTrue("Create and use clause generated correctly",
            output.contains("CREATE DATABASE test_db") && output.contains("USE test_db"));
    }

    @Test
    public void testInvalidCreateAndUseDatabaseClause() {
        String output = emptyDDL.getSQLString("correct_db");
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
        fields[0].setTableID(1);
        fields[0].setDataType(dataTypes.get("Varchar"));
        fields[1].setTableID(1);
        fields[1].setDataType(dataTypes.get("Boolean"));
        fields[2].setTableID(1);
        fields[2].setDataType(dataTypes.get("Integer"));
        fields[3].setTableID(1);
        fields[3].setDataType(dataTypes.get("Double"));
        tables[0].addNativeField(2);
        tables[0].addNativeField(3);
        tables[0].addNativeField(4);
        tables[0].addNativeField(5);
        tables[0].makeArrays();
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("");
        assertTrue("Data types are correctly identified",
            output.contains("varchar_field VARCHAR") &&
            output.contains("boolean_field BOOL") &&
            output.contains("integer_field INT") &&
            output.contains("double_field DOUBLE"));
    }

    @Test
    public void testValidConvertStrBooleanToInt() {
        EdgeTable[] tables = new EdgeTable[]{new EdgeTable("1|table")};
        EdgeField[] fields = new EdgeField[]{new EdgeField("2|boolean_field")};
        fields[0].setTableID(1);
        fields[0].setDataType(dataTypes.get("Boolean"));
        fields[0].setDefaultValue("true");
        tables[0].addNativeField(2);
        tables[0].makeArrays();
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("");
        assertTrue("String boolean is converted to integer",
            output.contains("boolean_field BOOL DEFAULT 1"));
    }

    @Test
    public void testInvalidConvertStrBooleanToInt() {
        EdgeTable[] tables = new EdgeTable[]{new EdgeTable("1|table")};
        EdgeField[] fields = new EdgeField[]{new EdgeField("2|boolean_field")};
        fields[0].setTableID(1);
        fields[0].setDataType(dataTypes.get("Boolean"));
        fields[0].setDefaultValue("notaboolean");
        tables[0].addNativeField(2);
        tables[0].makeArrays();
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("");
        assertFalse("Invalid string boolean is given a default value",
            output.contains("boolean_field BOOL DEFAULT notaboolean"));
    }

    @Test
    public void testInvalidDefaultIntegerValue() {
        EdgeTable[] tables = new EdgeTable[]{new EdgeTable("1|table")};
        EdgeField[] fields = new EdgeField[]{new EdgeField("2|integer_field")};
        fields[0].setTableID(1);
        fields[0].setDataType(dataTypes.get("Integer"));
        fields[0].setDefaultValue("notaninteger");
        tables[0].addNativeField(2);
        tables[0].makeArrays();
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("");
        assertFalse("Invalid default integer is given a default value",
            output.contains("integer_field INT DEFAULT notanint"));
    }

    @Test
    public void testValidVarcharLengthConstraint() {
        EdgeTable[] tables = new EdgeTable[]{new EdgeTable("1|table")};
        EdgeField[] fields = new EdgeField[]{new EdgeField("2|varchar_field")};
        fields[0].setTableID(1);
        fields[0].setVarcharValue(20);
        tables[0].addNativeField(2);
        tables[0].makeArrays();
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("");
        assertTrue("Primary key clause generated correctly",
            output.contains("varchar_field VARCHAR(20)"));
    }

    @Test
    public void testInvalidVarcharLengthConstraint() {
        EdgeTable[] tables = new EdgeTable[]{new EdgeTable("1|table")};
        EdgeField[] fields = new EdgeField[]{new EdgeField("2|varchar_field")};
        fields[0].setTableID(1);
        fields[0].setVarcharValue(99);
        tables[0].addNativeField(2);
        tables[0].makeArrays();
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("");
        assertFalse("Primary key clause generated incorrectly",
            output.contains("varchar_field VARCHAR(20)"));
    }

    @Test
    public void testPrimaryKeyClause() {
        EdgeTable[] tables = new EdgeTable[]{new EdgeTable("1|table")};
        EdgeField[] fields = new EdgeField[]{new EdgeField("2|primary_key")};
        fields[0].setTableID(1);
        fields[0].setIsPrimaryKey(true);
        tables[0].addNativeField(2);
        tables[0].makeArrays();
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("");
        assertTrue("Primary key clause generated correctly",
            output.contains("CONSTRAINT table_PK PRIMARY KEY (primary_key)"));
    }

    @Test
    public void testForeignKeyClause() {
        // Definitions
        EdgeTable table1 = new EdgeTable("1|table1");
        EdgeTable table2 = new EdgeTable("2|table2");
        EdgeField field1 = new EdgeField("3|primary_key");
        EdgeField field2 = new EdgeField("4|foreign_key");
        
        // Assignments
        field1.setTableID(1);
        field2.setTableID(2);
        field1.setIsPrimaryKey(true);
        table1.addNativeField(3);
        table2.addNativeField(4);

        // Relations
        table2.addRelatedTable(1);
        field1.setTableBound(2);
        field2.setTableBound(1);
        field1.setFieldBound(4);
        field2.setFieldBound(3);
        table1.makeArrays();
        table2.makeArrays();
        table2.setRelatedField(0, 3);
        
        // Generate SQL string
        EdgeTable[] tables = new EdgeTable[]{table1, table2};
        EdgeField[] fields = new EdgeField[]{field1, field2};
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("");
        assertTrue("Foreign key clause generated correctly",
            output.contains("CONSTRAINT table2_FK1 FOREIGN KEY(foreign_key) REFERENCES table1(primary_key)"));
    }

    @Test
    public void testInvalidForeignKey() {
        EdgeTable[] tables = new EdgeTable[]{new EdgeTable("1|table")};
        EdgeField[] fields = new EdgeField[]{
            new EdgeField("2|do_not_reference"),
            new EdgeField("3|foreign_key")
        };
        fields[0].setTableID(1);
        fields[1].setTableID(1);
        fields[1].setTableBound(1);
        fields[1].setFieldBound(2);
        tables[0].addNativeField(2);
        tables[0].addNativeField(3);
        tables[0].makeArrays();
        tables[0].setRelatedField(1, 2);
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("");
        System.out.println(output);
        assertFalse("Invalid foreign key clause is not generated",
            output.contains("FOREIGN KEY(foreign_key)") && output.contains("REFERENCES table(do_not_reference)"));
    }

    @Test
    public void testNonNullClause() {
        EdgeTable[] tables = new EdgeTable[]{new EdgeTable("1|table")};
        EdgeField[] fields = new EdgeField[]{new EdgeField("2|non_null_field")};
        fields[0].setTableID(1);
        fields[0].setDisallowNull(true);
        tables[0].addNativeField(2);
        tables[0].makeArrays();
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("");
        assertTrue("Non null clause generated correctly",
            output.contains("non_null_field") && output.contains("NOT NULL"));
    }

    @Test
    public void testDefaultValueClause() {
        EdgeTable[] tables = new EdgeTable[]{new EdgeTable("1|table")};
        EdgeField[] fields = new EdgeField[]{new EdgeField("2|default_value_field")};
        fields[0].setTableID(1);
        fields[0].setDefaultValue("A default value");
        tables[0].addNativeField(2);
        tables[0].makeArrays();
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("");
        assertTrue("Default value clause generated correctly",
            output.contains("default_value_field") && output.contains("DEFAULT A default value"));
    }

    @Test
    public void testAllClausesSQLStringGeneration() {
        // Definitions
        EdgeTable table1 = new EdgeTable("1|table1");
        EdgeTable table2 = new EdgeTable("2|table2");
        EdgeField t1_pk = new EdgeField("3|t1_pk");
        EdgeField t1_nn = new EdgeField("4|t1_nn");
        EdgeField t1_dv = new EdgeField("5|t1_dv");
        EdgeField t1_longvarchar = new EdgeField("6|t1_longvarchar");
        EdgeField t2_fk = new EdgeField("7|t2_fk");
        EdgeField t2_varchar = new EdgeField("8|t2_varchar");
        EdgeField t2_boolean = new EdgeField("9|t2_boolean");
        EdgeField t2_integer = new EdgeField("10|t2_integer");
        EdgeField t2_double = new EdgeField("11|t2_double");

        // Assignments
        t1_pk.setTableID(1);
        t1_pk.setIsPrimaryKey(true);
        t1_nn.setTableID(1);
        t1_nn.setDisallowNull(true);
        t1_dv.setTableID(1);
        t1_dv.setDefaultValue("standard");
        t1_longvarchar.setTableID(1);
        t1_longvarchar.setDataType(dataTypes.get("Varchar"));
        t1_longvarchar.setVarcharValue(33);
        t2_fk.setTableID(2);
        t2_varchar.setTableID(2);
        t2_varchar.setDataType(dataTypes.get("Varchar"));
        t2_boolean.setTableID(2);
        t2_boolean.setDataType(dataTypes.get("Boolean"));
        t2_integer.setTableID(2);
        t2_integer.setDataType(dataTypes.get("Integer"));
        t2_double.setTableID(2);
        t2_double.setDataType(dataTypes.get("Double"));
        table1.addNativeField(3);
        table1.addNativeField(4);
        table1.addNativeField(5);
        table1.addNativeField(6);
        table2.addNativeField(7);
        table2.addNativeField(8);
        table2.addNativeField(9);
        table2.addNativeField(10);
        table2.addNativeField(11);

        // Relations
        table2.addRelatedTable(1);
        t1_pk.setTableBound(2);
        t2_fk.setTableBound(1);
        t1_pk.setFieldBound(7);
        t2_fk.setFieldBound(3);
        table1.makeArrays();
        table2.makeArrays();
        table2.setRelatedField(0, 3);

        // Generate SQL String
        EdgeTable[] tables = new EdgeTable[]{table1, table2};
        EdgeField[] fields = new EdgeField[]{t1_pk, t1_nn, t1_dv, t1_longvarchar, t2_fk, t2_varchar, t2_boolean, t2_integer, t2_double};
        CreateDDLMySQL ddl = new CreateDDLMySQL(tables, fields);
        String output = ddl.getSQLString("test_db");
        assertTrue("All clauses in SQL string generated correctly",
            output.contains("CREATE DATABASE test_db") &&
            output.contains("USE test_db") &&
            output.contains("CREATE TABLE table1") &&
            output.contains("t1_nn VARCHAR(1) NOT NULL") &&
            output.contains("t1_dv VARCHAR(1) DEFAULT standard") &&
            output.contains("t1_longvarchar VARCHAR(33)") &&
            output.contains("CONSTRAINT table1_PK PRIMARY KEY (t1_pk)") &&
            output.contains("CREATE TABLE table2") &&
            output.contains("t2_varchar VARCHAR") &&
            output.contains("t2_boolean BOOL") &&
            output.contains("t2_integer INT") &&
            output.contains("t2_double DOUBLE") &&
            output.contains("CONSTRAINT table2_FK1 FOREIGN KEY(t2_fk) REFERENCES table1(t1_pk)"));
    }

}