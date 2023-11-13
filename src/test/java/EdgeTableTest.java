import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EdgeTableTest {
  private EdgeTable et1;

  @Before
  public void setup() {
    et1 = new EdgeTable("42|TestTable"); // Updated numFigure to 42

    et1.addNativeField(42); // Updated native field numbers
    et1.addNativeField(24);
    et1.addNativeField(15);
    et1.addNativeField(37);
    et1.addNativeField(9);

    et1.addRelatedTable(20); // Updated related table numbers
    et1.addRelatedTable(10);
    et1.addRelatedTable(35);
    et1.addRelatedTable(50);
    et1.addRelatedTable(5);
  }

  @Test
  public void shouldReturnNumFigure() {
    assertThat(et1.getNumFigure(), is(42));
  }

  @Test
  public void shouldReturnTableName() {
    assertThat(et1.getName(), is("TestTable"));
  }

  @Test
  public void shouldGetNativeFields() {
    et1.makeArrays();
    int[] arr = et1.getNativeFieldsArray();

    assertThat(arr[1], is(24));
    assertThat(arr[3], is(37));
    assertThat(arr[4], is(9));
  }

  @Test
  public void shouldAddNativeField() {
    et1.addNativeField(55); // Added new native fields
    et1.addNativeField(18);

    et1.makeArrays();
    int[] arr = et1.getNativeFieldsArray();

    assertThat(arr[5], is(55));
    assertThat(arr[6], is(18));
  }

  @Test
  public void shouldGetRelatedFields() {
    et1.makeArrays();
    int[] arr = et1.getRelatedFieldsArray();

    assertThat(arr[0], is(0));
    assertThat(arr[2], is(0));
    assertThat(arr[arr.length - 1], is(0));
  }

  @Test
  public void shouldSetRelatedFields() {
    et1.makeArrays();
    et1.setRelatedField(1, 25); // Updated related field values
    et1.setRelatedField(3, 30);
    et1.setRelatedField(4, 45);

    int[] arr = et1.getRelatedFieldsArray();

    assertThat(arr[1], is(25));
    assertThat(arr[3], is(30));
    assertThat(arr[4], is(45));
    assertThat(arr[0], is(0));
  }

  @Test
  public void shouldGetRelatedTables() {
    et1.makeArrays();
    int[] arr = et1.getRelatedTablesArray();

    assertThat(arr[0], is(20));
    assertThat(arr[2], is(35));
    assertThat(arr[3], is(50));
  }

  @Test
  public void shouldAddRelatedTable() {
    et1.addRelatedTable(60); // Added new related tables
    et1.addRelatedTable(70);

    et1.makeArrays();
    int[] arr = et1.getRelatedTablesArray();

    assertThat(arr[5], is(60));
    assertThat(arr[6], is(70));
  }

  @Test
  public void shouldMoveFieldUp() {
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
  public void shouldMoveFieldDown() {
    et1.makeArrays();
    et1.setRelatedField(0, 1);
    et1.setRelatedField(1, 2);
    et1.setRelatedField(2, 3);
    et1.setRelatedField(3, 4);
    et1.setRelatedField(4, 5);

    et1.moveFieldDown(4); // Move the last field down
    int[] arr = et1.getRelatedFieldsArray();
    assertThat(arr[arr.length - 1], is(5));

    et1.moveFieldDown(2); // Move the field at index 2 down
    assertThat(arr[3], is(3));
    assertThat(arr[2], is(4));

    et1.moveFieldDown(0); // Move the first field down
    assertThat(arr[0], is(2));
    assertThat(arr[1], is(1));
  }

  @Test
  public void shouldMakeArrays() {
    et1.makeArrays();
    int[] tableArr = et1.getRelatedTablesArray();
    int[] fieldArr = et1.getNativeFieldsArray();

    assertThat(fieldArr[0], is(42));
    assertThat(fieldArr[3], is(37));

    assertThat(tableArr[2], is(35));
    assertThat(tableArr[4], is(5));
  }
}
