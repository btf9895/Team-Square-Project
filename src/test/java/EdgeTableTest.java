import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EdgeTableTest {
  private EdgeTable et1;

  @Before
  public void setup() {
    et1 = new EdgeTable("42|TestTable");

    et1.addNativeField(42);
    et1.addNativeField(24);
    et1.addNativeField(15);
    et1.addNativeField(37);
    et1.addNativeField(9);

    et1.addRelatedTable(20);
    et1.addRelatedTable(10);
    et1.addRelatedTable(35);
    et1.addRelatedTable(50);
    et1.addRelatedTable(5);
  }

  @Test
  public void testNumFigure() {
    assertThat(et1.getNumFigure(), is(42));
  }

  @Test
  public void testName() {
    assertThat(et1.getName(), is("TestTable"));
  }

  @Test
  public void testGetNativeFieldsArray() {
    et1.makeArrays();
    int[] arr = et1.getNativeFieldsArray();

    assertThat(arr[1], is(24));
    assertThat(arr[3], is(37));
    assertThat(arr[4], is(9));
  }

  @Test
  public void testAddNativeField() {
    et1.addNativeField(55);
    et1.addNativeField(18);

    et1.makeArrays();
    int[] arr = et1.getNativeFieldsArray();

    assertThat(arr[5], is(55));
    assertThat(arr[6], is(18));
  }

  @Test
  public void testGetRelatedFieldsArray() {
    et1.makeArrays();
    int[] arr = et1.getRelatedFieldsArray();

    assertThat(arr[0], is(0));
    assertThat(arr[2], is(0));
    assertThat(arr[arr.length - 1], is(0));
  }

  @Test
  public void testSetRelatedFields() {
    et1.makeArrays();
    et1.setRelatedField(1, 25);
    et1.setRelatedField(3, 30);
    et1.setRelatedField(4, 45);

    int[] arr = et1.getRelatedFieldsArray();

    assertThat(arr[1], is(25));
    assertThat(arr[3], is(30));
    assertThat(arr[4], is(45));
    assertThat(arr[0], is(0));
  }

  @Test
  public void testGetRelatedTablesArray() {
    et1.makeArrays();
    int[] arr = et1.getRelatedTablesArray();

    assertThat(arr[0], is(20));
    assertThat(arr[2], is(35));
    assertThat(arr[3], is(50));
  }

  @Test
  public void testAddRelatedTable() {
    et1.addRelatedTable(60);
    et1.addRelatedTable(70);

    et1.makeArrays();
    int[] arr = et1.getRelatedTablesArray();

    assertThat(arr[5], is(60));
    assertThat(arr[6], is(70));
  }

  @Test
  public void testMoveFieldUp() {
    et1.makeArrays();
    et1.setRelatedField(0, 1);
    et1.setRelatedField(1, 2);
    et1.setRelatedField(2, 3);
    et1.setRelatedField(3, 4);
    et1.setRelatedField(4, 5);

    et1.moveFieldUp(0);
    int[] arr = et1.getRelatedFieldsArray();
    assertThat(arr[0], is(1));

    et1.moveFieldUp(2);
    assertThat(arr[1], is(3));
    assertThat(arr[2], is(2));

    et1.moveFieldUp(1);
    assertThat(arr[0], is(3));
    assertThat(arr[1], is(1));
  }

  @Test
  public void testMoveFieldDown() {
    et1.makeArrays();
    et1.setRelatedField(0, 1);
    et1.setRelatedField(1, 2);
    et1.setRelatedField(2, 3);
    et1.setRelatedField(3, 4);
    et1.setRelatedField(4, 5);

    et1.moveFieldDown(4);
    int[] arr = et1.getRelatedFieldsArray();
    assertThat(arr[arr.length - 1], is(5));

    et1.moveFieldDown(2);
    assertThat(arr[3], is(3));
    assertThat(arr[2], is(4));

    et1.moveFieldDown(0);
    assertThat(arr[0], is(2));
    assertThat(arr[1], is(1));
  }

  @Test
  public void testMakeArrays() {
    et1.makeArrays();
    int[] tableArr = et1.getRelatedTablesArray();
    int[] fieldArr = et1.getNativeFieldsArray();

    assertThat(fieldArr[0], is(42));
    assertThat(fieldArr[3], is(37));

    assertThat(tableArr[2], is(35));
    assertThat(tableArr[4], is(5));
  }
}
