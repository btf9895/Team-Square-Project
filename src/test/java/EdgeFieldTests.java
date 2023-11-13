import org.junit.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
public class EdgeFieldTests {
    
    static EdgeField testObj;
    
    @Before
	public void setUp() throws Exception {
		testObj = new EdgeField("3|Grade|4|CourseName|5|Number|6|FacSSN|7|StudentSSN|8|StudentName|11|FacultyName");
	}

    @Test
    public void testGetVarCharValue() throws Exception{
        assertEquals(testObj.getVarcharValue(), 1);
       // System.out.println("uwvkb");
    }

    @Test
    public void setValidDefaultValue() throws Exception{
        testObj.setDisallowNull(true);
        testObj.setDefaultValue(null);
        assertEquals(testObj.getDefaultValue(), null);

        
       // System.out.println("uwvkb");
    }

}
