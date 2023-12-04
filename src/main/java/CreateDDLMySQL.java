import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateDDLMySQL extends EdgeConvertCreateDDL {
   private static Logger logger = LogManager.getLogger(CreateDDLMySQL.class.getName());

   protected String databaseName;
   //this array is for determining how MySQL refers to datatypes
   protected String[] strDataType = {"VARCHAR", "BOOL", "INT", "DOUBLE"};
   protected StringBuffer sb;

   public CreateDDLMySQL(EdgeTable[] inputTables, EdgeField[] inputFields) {
      super(inputTables, inputFields);
      sb = new StringBuffer();
   } //CreateDDLMySQL(EdgeTable[], EdgeField[])
   
   public CreateDDLMySQL() { //default constructor with empty arg list for to allow output dir to be set before there are table and field objects
      
   }

   // Overload to allow for database name to be specified in unit tests
   @Override
   public void createDDL() {
      createDDL(null);
   }
   
   public void createDDL(String defaultDatabaseName) {
      EdgeConvertGUI.setReadSuccess(true);
      databaseName = defaultDatabaseName == null ? generateDatabaseName() : defaultDatabaseName; // Avoid getting user input from JOptionPane.showInputDialog()
      sb.append("CREATE DATABASE " + databaseName + ";\r\n");
      sb.append("USE " + databaseName + ";\r\n");

		logger.debug("Database connection queries: ", sb);

      for (int boundCount = 0; boundCount <= maxBound; boundCount++) { //process tables in order from least dependent (least number of bound tables) to most dependent
         for (int tableCount = 0; tableCount < numBoundTables.length; tableCount++) { //step through list of tables
            logger.trace("Building tables.. ", "Bound count: " + boundCount, "Table count: " + tableCount);
            if (numBoundTables[tableCount] == boundCount) {
               sb.append("CREATE TABLE " + tables[tableCount].getName() + " (\r\n");
               int[] nativeFields = tables[tableCount].getNativeFieldsArray();
               int[] relatedFields = tables[tableCount].getRelatedFieldsArray();
               boolean[] primaryKey = new boolean[nativeFields.length];

               String s = "\tNative fields: ";
               for (int nativeFieldCount = 0; nativeFieldCount < nativeFields.length; nativeFieldCount++) { //print out the fields
                  s += nativeFields[nativeFieldCount] + ", ";
               }
               s += "\tRelated fields: ";
               for (int relatedFieldCount = 0; relatedFieldCount < relatedFields.length; relatedFieldCount++) { //print out the fields
                  s += relatedFields[relatedFieldCount] + ", ";
               }
               logger.warn("Table variables: " + s);
               
               int numPrimaryKey = 0;
               int numForeignKey = 0;
               for (int nativeFieldCount = 0; nativeFieldCount < nativeFields.length; nativeFieldCount++) { //print out the fields
                  EdgeField currentField = getField(nativeFields[nativeFieldCount]);
                  logger.warn("Current field: ", currentField.toString());
                  sb.append("\t" + currentField.getName() + " " + strDataType[currentField.getDataType()]);
                  if (currentField.getDataType() == 0) { //varchar
                     sb.append("(" + currentField.getVarcharValue() + ")"); //append varchar length in () if data type is varchar
                  }
                  if (currentField.getDisallowNull()) {
                     sb.append(" NOT NULL");
                  }
                  if (!currentField.getDefaultValue().equals("")) {
                     if (currentField.getDataType() == 1) { //boolean data type
                        sb.append(" DEFAULT " + convertStrBooleanToInt(currentField.getDefaultValue()));
                     } else { //any other data type

                        if( currentField.getDefaultValue().trim().length() > 1){
                            sb.append(" DEFAULT '" + currentField.getDefaultValue() + "'");
                        } else {

                        }
                       
                       // sb.append(" DEFAULT '" + currentField.getDefaultValue() + "'");
                     }
                  }//if statement for defaultValue
                  if (currentField.getIsPrimaryKey()) {
                     primaryKey[nativeFieldCount] = true;
                     numPrimaryKey++;
                  } else {
                     primaryKey[nativeFieldCount] = false;
                  }
                  if (currentField.getFieldBound() != 0) {
                     numForeignKey++;
                  }

                  // if( nativeFieldCount < nativeFields.length -2){
                  //      sb.append(",\r\n"); //end of field
                  // }

                 sb.append(",\r\n"); //end of field
                  logger.trace("Adding field: {}", sb);
               }
               if (numPrimaryKey > 0) { //table has primary key(s)
                  sb.append("CONSTRAINT " + tables[tableCount].getName() + "_PK PRIMARY KEY (");
                  for (int i = 0; i < primaryKey.length; i++) {
                     if (primaryKey[i]) {
                        sb.append(getField(nativeFields[i]).getName());
                        numPrimaryKey--;
                        if (numPrimaryKey > 0) {
                           sb.append(", ");
                        }
                     }
                  }
                  sb.append(")");
                  if (numForeignKey > 0) {
                     sb.append(",");
                  }
                  sb.append("\r\n");
                  logger.trace("Adding primary key: {}", sb);
               }
               if (numForeignKey > 0) { //table has foreign keys
                  int currentFK = 1;
                  for (int i = 0; i < relatedFields.length; i++) {
                     if (relatedFields[i] != 0) {
                        sb.append("CONSTRAINT " + tables[tableCount].getName() + "_FK" + currentFK +
                              " FOREIGN KEY(" + getField(nativeFields[i]).getName() + ") REFERENCES " +
                              getTable(getField(nativeFields[i]).getTableBound()).getName() + "("
                              + getField(relatedFields[i]).getName() + ")");
                        if (currentFK < numForeignKey) {
                           sb.append(",\r\n");
                        }
                        currentFK++;
                        logger.trace("Adding foreign key: {}", sb);
                     }
                  }
                  sb.append("\r\n");
               }
               sb.append(");\r\n\r\n"); //end of table
            }
            logger.debug("Finished database creation string: ", sb.toString());
         }
      }
   }

   protected int convertStrBooleanToInt(String input) { //MySQL uses '1' and '0' for boolean types
      if (!(input.toLowerCase().equals("true") || input.toLowerCase().equals("false"))) {
         logger.error("Invalid value for boolean variable: ", input);
      }
      if (input.equals("true")) {
         return 1;
      } else {
         return 0;
      }
   }
   
   public String generateDatabaseName() { //prompts user for database name
      String dbNameDefault = "MySQLDB";
      //String databaseName = "";

      do {
         databaseName = (String) JOptionPane.showInputDialog(
               null,
               "Enter the database name:",
               "Database Name",
               JOptionPane.PLAIN_MESSAGE,
               null,
               null,
               dbNameDefault);
         
         if (databaseName == null) {
            EdgeConvertGUI.setReadSuccess(false);
            return "";
         }
         if (databaseName.equals("")) {
            JOptionPane.showMessageDialog(null, "You must select a name for your database.");
         }
      } while (databaseName.equals(""));
      logger.info("Inputted database name: ", databaseName);
      return databaseName;
   }
   
   public String getDatabaseName() {
      logger.info("Database name: ", databaseName);
      return databaseName;
   }
   
   public String getProductName() {
      return "MySQL";
   }

   // Overload to allow for database name to be specified in unit tests
   @Override
   public String getSQLString() {
      return getSQLString(null);
   }

   public String getSQLString(String defaultDatabaseName) {
      logger.info("SB: ", sb.toString());
      createDDL(defaultDatabaseName);
      return sb.toString();
   }
   
}//EdgeConvertCreateDDL
