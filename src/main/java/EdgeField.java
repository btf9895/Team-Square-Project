import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EdgeField {
   private int numFigure, tableID, tableBound, fieldBound, dataType, varcharValue;
   private String name, defaultValue;
   private boolean disallowNull, isPrimaryKey;
   private static String[] strDataType = {"Varchar", "Boolean", "Integer", "Double"};
   public static final int VARCHAR_DEFAULT_LENGTH = 1;
   private static final Logger logger = LogManager.getLogger(EdgeField.class.getName());

   public EdgeField(String inputString) {
      StringTokenizer st = new StringTokenizer(inputString, EdgeConvertFileParser.DELIM);
      numFigure = Integer.parseInt(st.nextToken());
      name = st.nextToken();
      tableID = 0;
      tableBound = 0;
      fieldBound = 0;
      disallowNull = false;
      isPrimaryKey = false;
      defaultValue = "";
      varcharValue = VARCHAR_DEFAULT_LENGTH;
      dataType = 0;
      logger.debug("EdgeField constructed with inputString: " + inputString);
   }
   
   public int getNumFigure() {
      return numFigure;
   }
   
   public String getName() {
      return name;
   }
   
   public int getTableID() {
      return tableID;
   }
   
   public void setTableID(int value) {
      tableID = value;
   }
   
   public int getTableBound() {
      return tableBound;
   }
   
   public void setTableBound(int value) {
      tableBound = value;
   }

   public int getFieldBound() {
      return fieldBound;
   }
   
   public void setFieldBound(int value) {
      fieldBound = value;
   }

   public boolean getDisallowNull() {
      return disallowNull;
   }
   
   public void setDisallowNull(boolean value) {
      disallowNull = value;
   }
   
   public boolean getIsPrimaryKey() {
      return isPrimaryKey;
   }
   
   public void setIsPrimaryKey(boolean value) {
      isPrimaryKey = value;
   }
   
   public String getDefaultValue() {
      return defaultValue;
   }
   
   public void setDefaultValue(String value) {
      defaultValue = value;
   }
   
   public int getVarcharValue() {
      return varcharValue;
   }
   
   public void setVarcharValue(int value) {
      if (value > 0) {
         varcharValue = value;
         logger.info("Set varcharValue to: " + value);
      } else {
         logger.warn("Attempted to set varcharValue with a non-positive value: {}", value);
      }
   }
   public int getDataType() {
      return dataType;
   }
   
   public void setDataType(int value) {
      if (value >= 0 && value < strDataType.length) {
         dataType = value;
         logger.info("Set dataType to: " + value);
      } else {
         logger.error("Attempted to set invalid dataType: {}", value);
      }
   }
   
   public static String[] getStrDataType() {
      return strDataType;
   }
   
   public String toString() {
      logger.debug("Generated string representation for EdgeField: {}", name);
      return "numFigure: " + numFigure + EdgeConvertFileParser.DELIM +
      "name: " + name + EdgeConvertFileParser.DELIM +
      "tableID: " + tableID + EdgeConvertFileParser.DELIM +
      "tableBound: " + tableBound + EdgeConvertFileParser.DELIM +
      "fieldBound: " + fieldBound + EdgeConvertFileParser.DELIM +
      "dataType: " + dataType + EdgeConvertFileParser.DELIM +
      "varcharValue: " + varcharValue + EdgeConvertFileParser.DELIM +
      "isPrimaryKey: " + isPrimaryKey + EdgeConvertFileParser.DELIM +
      "disallowNull: " + disallowNull + EdgeConvertFileParser.DELIM +
      "defaultValue: " + defaultValue;

   }
}
