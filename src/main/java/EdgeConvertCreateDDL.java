import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class EdgeConvertCreateDDL {
   private static Logger logger = LogManager.getLogger(EdgeConvertCreateDDL.class.getName());

   static String[] products = {"MySQL"};
   protected EdgeTable[] tables; //master copy of EdgeTable objects
   protected EdgeField[] fields; //master copy of EdgeField objects
   protected int[] numBoundTables;
   protected int maxBound;
   
   public EdgeConvertCreateDDL(EdgeTable[] tables, EdgeField[] fields) {
      this.tables = tables;
      this.fields = fields;
      logger.trace("Fields and tables: {} and {}", fields.toString(), tables.toString());
      initialize();
   } //EdgeConvertCreateDDL(EdgeTable[], EdgeField[])
   
   public EdgeConvertCreateDDL() { //default constructor with empty arg list for to allow output dir to be set before there are table and field objects
      
   } //EdgeConvertCreateDDL()

   public void initialize() {
      numBoundTables = new int[tables.length];
      maxBound = 0;

      for (int i = 0; i < tables.length; i++) { //step through list of tables
         int numBound = 0; //initialize counter for number of bound tables
         int[] relatedFields = tables[i].getRelatedFieldsArray();
         logger.debug("Table: {}, relatedFields: {}", tables[i].toString(), relatedFields.toString());
         for (int j = 0; j < relatedFields.length; j++) { //step through related fields list
            if (relatedFields[j] != 0) {
               numBound++; //count the number of non-zero related fields
            }
         }
         numBoundTables[i] = numBound;
         if (numBound > maxBound) {
            maxBound = numBound;
         }
      }
   }
   
   protected EdgeTable getTable(int numFigure) {
      for (int tIndex = 0; tIndex < tables.length; tIndex++) {
         if (numFigure == tables[tIndex].getNumFigure()) {
            logger.debug("Found table: {}", tables[tIndex].toString());
            return tables[tIndex];
         }
      }
      return null;
   }
   
   protected EdgeField getField(int numFigure) {
      for (int fIndex = 0; fIndex < fields.length; fIndex++) {
         if (numFigure == fields[fIndex].getNumFigure()) {
            logger.debug("Found field: {}", fields[fIndex].toString());
            return fields[fIndex];
         }
      }
      return null;
   }

   public abstract String getDatabaseName();

   public abstract String getProductName();
   
   public abstract String getSQLString();
   
   public abstract void createDDL();

   @Override
   public String toString() {
      return "EdgeConvertCreateDDL [tables=" + Arrays.toString(tables) + ", fields=" + Arrays.toString(fields)
         + ", numBoundTables=" + Arrays.toString(numBoundTables) + ", maxBound=" + maxBound + "]";
   }

   
   
}//EdgeConvertCreateDDL
