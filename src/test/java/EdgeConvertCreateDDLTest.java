import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class EdgeConvertCreateDDLTest {
	private EdgeConvertCreateDDL testObj;
	private EdgeTable[] edgeTables;
	private EdgeField[] edgeFields;

	// Concrete subclass of EdgeConvertCreateDDL for testing
	private static class TestEdgeConvertCreateDDL extends EdgeConvertCreateDDL {

		public TestEdgeConvertCreateDDL(EdgeTable[] tables, EdgeField[] fields) {
			super(tables, fields);
		}

		@Override
		public String getDatabaseName() {
			return null;
		}

		@Override
		public String getProductName() {
			return null;
		}

		@Override
		public String getSQLString() {
			return null;
		}

		@Override
		public void createDDL() {
			// Test implementation
		}
	}

	@Before
	public void setUp() {
		EdgeTable edgeTable = new EdgeTable("1|NameOfTable");
		EdgeTable edgeTable2 = new EdgeTable("2|NameOfTable2");
		edgeTable.makeArrays();
		edgeTable2.makeArrays();

		EdgeField edgeField = new EdgeField("1|NameOfField");
		EdgeField edgeField2 = new EdgeField("2|NameOfField2");

		this.edgeTables = new EdgeTable[] { edgeTable, edgeTable2 };
		this.edgeFields = new EdgeField[] { edgeField, edgeField2 };
		this.testObj = new TestEdgeConvertCreateDDL(edgeTables, edgeFields);
	}

	@Test
	public void testInitialize() {
		// assert that numBoundTables is initialized to the correct length
		assertEquals(edgeTables.length, this.testObj.numBoundTables.length);
		// assert that maxBound is initialized to 0
		assertEquals(0, this.testObj.maxBound);
		// assert that sb is initialized to an empty string
		assertEquals("", this.testObj.sb.toString());
		// assert that getRelatedFieldsArray is called for each table
		assertEquals(0, this.testObj.getTable(1).getRelatedFieldsArray().length);
		assertEquals(0, this.testObj.getTable(2).getRelatedFieldsArray().length);
	}

	@Test
	public void testGetTable() {
		// assert that the correct table is returned for each numFigure
		assertEquals(edgeTables[0], this.testObj.getTable(1));
		assertEquals(edgeTables[1], this.testObj.getTable(2));
	}

	@Test
	public void testGetField() {
		// assert that the correct field is returned for each numFigure
		assertEquals(edgeFields[0], this.testObj.getField(1));
		assertEquals(edgeFields[1], this.testObj.getField(2));
	}

	@Test
	public void getDatabaseName() {
		// assert that the correct field is returned for each numFigure
		assertEquals(null, this.testObj.getDatabaseName());
	}

	@Test
	public void getProductName() {
		// assert that the correct field is returned for each numFigure
		assertEquals(null, this.testObj.getProductName());
	}

	@Test
	public void getSQLString() {
		// assert that the correct field is returned for each numFigure
		assertEquals(null, this.testObj.getSQLString());
	}

	@Test
	public void createDDL() {
		// assert that the correct field is returned for each numFigure
		this.testObj.createDDL();
	}

	@Test
	public void testToString() {
		// assert that the correct field is returned for each numFigure
		assertEquals(
				"EdgeConvertCreateDDL [tables=" + Arrays.toString(edgeTables) + ", fields=" + Arrays.toString(edgeFields)
						+ ", numBoundTables=" + Arrays.toString(this.testObj.numBoundTables) + ", maxBound=" + this.testObj.maxBound
						+ ", sb=" + this.testObj.sb
						+ ", selected=" + this.testObj.selected + "]",
				this.testObj.toString());
	}

}
