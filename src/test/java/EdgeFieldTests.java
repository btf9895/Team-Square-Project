/** Alexandria Eddings, JUnit Testing */
import org.junit.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class EdgeFieldTests {
    
    static EdgeField testObj;
    static EdgeField testObject;

    
    @Before
	public void setUp() throws Exception {
		// testObject = new EdgeField("3|&*^*#|4|CourseName|5|Number|6|FacSSN|7|StudentSSN|8|StudentName|11|FacultyName");
        testObj = new EdgeField("3|Grade|4|CourseName|5|Number|6|FacSSN|7|StudentSSN|8|StudentName|11|FacultyName");
	}


    /** Testing Data Type inputs */

    @Test
    public void setInvalidSmallDataType() {
        testObj.setDataType(-6);
        assertEquals(testObj.getDataType(), -6);

     }

     @Test
    public void testSettingValidDataType() {
        testObj.setDataType(2);
        assertEquals(testObj.getDataType(), 2);

     }

    //Should fail
     @Test
    public void setInvalidBigDataType() throws Exception{
        testObj.setDataType(22);
        assertEquals(testObj.getDataType(), 22);
       
    }

    /** Test VarCharValue */

  @Test
    public void setZeroVarCharValue() {
        testObj.setVarcharValue(0);
        assertEquals(testObj.getVarcharValue(), 0);

     }
     
//should fail
      @Test
    public void testSetInvalidNegativeVarCharValue() throws Exception{
        testObj.setVarcharValue(-1);
        assertEquals(testObj.getVarcharValue(), -1);
    }


    @Test
    public void getValidVarCharValue() throws Exception{
        assertEquals(testObj.getVarcharValue(), 1);
       
    }

    @Test
        public void setAndGetVarCharValue() throws Exception{
            testObj.setVarcharValue(77777777);
            assertEquals(testObj.getVarcharValue(), 77777777);
        
        }


    /** Test DefaultValue */
    @Test
    public void setNullDefaultValue() throws Exception{
        testObj.setDisallowNull(true);
        testObj.setDefaultValue(null);
        assertNotNull(testObj.getDefaultValue());
       
    }


     @Test
    public void setDefaultValueZero() throws Exception{
        testObj.setDefaultValue("0");
        assertEquals(testObj.getDefaultValue(), "0");
       
    }


    @Test
    public void setDefaultValueSpecialCharacters() throws Exception{
        testObj.setDefaultValue("$%^!*");
        assertEquals(testObj.getDefaultValue(), "$%^!*");
      
    }

     @Test
    public void setBigDefaultValueNumbers() throws Exception{
        testObj.setDefaultValue("456344444");
        assertEquals(testObj.getDefaultValue(), "456344444");
      
    }
  
    /** Test PrimaryKey */
    @Test
    public void checkIfPrimaryKeyIsTrue() {
     testObj.setIsPrimaryKey(true);
     assertTrue(testObj.getIsPrimaryKey());
   }

   @Test
    public void checkIfPrimaryKeyIsFalse() {
     testObj.setIsPrimaryKey(false);
     assertFalse(testObj.getIsPrimaryKey());
   }

   
   /** Test nullDisallowed */
    @Test
    public void isNullDisallowedTrue() {
        testObj.setDisallowNull(true);
        assertTrue(testObj.getDisallowNull());
 }

    @Test
   public void isNullDisallowedFalse() {
    testObj.setDisallowNull(false);
    assertFalse(testObj.getDisallowNull());
 }

/** Testing TableID */
     @Test
   public void setTableIDHigh() {
    testObj.setTableID(788888888);
    assertEquals(testObj.getTableID(), 788888888);
 }

  @Test
   public void setTableIDZero() {
    testObj.setTableID(0);
    assertEquals(testObj.getTableID(), 0);
 }


/** Testing FieldBound */

 @Test
   public void setFieldBoundHigh() {
    testObj.setFieldBound(33333333);
    assertEquals(testObj.getFieldBound(), 33333333);
 }

 @Test
   public void setFieldBoundZero() {
    testObj.setFieldBound(0);
    assertEquals(testObj.getFieldBound(), 0);
 }

 /** Testing StringDataType array */
    @Test
   public void checkStringDataTypeArrayValues() {
    assertEquals(testObj.getStrDataType()[0], "Varchar");
    assertEquals(testObj.getStrDataType()[1], "Boolean");
    assertEquals(testObj.getStrDataType()[2], "Integer");
    assertEquals(testObj.getStrDataType()[3], "Double");

 }


}
