// import org.junit.Before;
// import org.junit.Test;

// public class EdgeConvertCreateDDLTest {
// 	private EdgeConvertCreateDDL testObj;

// 	// Concrete subclass of EdgeConvertCreateDDL for testing
// 	private static class TestEdgeConvertCreateDDL extends EdgeConvertCreateDDL {

// 		public TestEdgeConvertCreateDDL(EdgeTable[] tables, EdgeField[] fields) {
// 			super(tables, fields);
// 		}

// 		@Override
// 		public String getDatabaseName() {
// 			return "TestDatabase";
// 		}

// 		@Override
// 		public String getProductName() {
// 			return "TestProduct";
// 		}

// 		@Override
// 		public String getSQLString() {
// 			return "Test SQL String";
// 		}

// 		@Override
// 		public void createDDL() {
// 			// Test implementation
// 		}
// 	}

// 	// @Before
// 	// public void setUp() {

// 	// EdgeTable table1 = new EdgeTable("1|testTable1");
// 	// EdgeTable table2 = new EdgeTable("2|testTable2");
// 	// System.out.println("table1: " + table1.getName());
// 	// // System.out.println("table2: " + table2.getName());

// 	// EdgeTable[] tables = { table1, table2 };
// 	// EdgeField[] fields = { new EdgeField("1|testField1"), new
// 	// EdgeField("2|testField2") };
// 	// testObj = new TestEdgeConvertCreateDDL(tables, fields);

// 	// }

// 	@Before
// 	public void setUp() {
// 		EdgeTable edgeTable = new EdgeTable("1|NameOfTable");
// 		EdgeTable edgeTable2 = new EdgeTable("2|NameOfTable2");

// 		EdgeTable[] edgeTables = { edgeTable, edgeTable2 };
// 		EdgeField edgeField = new EdgeField("1|NameOfField");
// 		EdgeField edgeField2 = new EdgeField("2|NameOfField2");
// 		EdgeField[] edgeFields = { edgeField, edgeField2 };
// 		this.testObj = new TestEdgeConvertCreateDDL(new EdgeTable[0], edgeFields);
//         edgeTable.makeArrays();
//         edgeTable2.makeArrays();
// 		this.testObj = new TestEdgeConvertCreateDDL(edgeTables, edgeFields);
// 	}

// 	@Test
// 	public void testInitialize() {
// 		System.out.println("testObj: " + this.testObj.toString());
// 	}

// }
