import java.io.*;
import java.util.*;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//functional
public class ParseSaveFile {
   private File parseFile;
   private FileReader fr;
   private BufferedReader br;
   
   String currentLine;
   int numFigure;
   String tableName;
   int numFields;
   int numTables;

   String fieldName;
   ArrayList alFields, alTables;

   public static final String EDGE_ID = "EDGE Diagram File"; //first line of .edg files should be this
   public static final String SAVE_ID = "EdgeConvert Save File"; //first line of save files should be this
   public static final String DELIM = "|";
   private static final Logger logger = LogManager.getLogger(EdgeConvertFileParser.class);

  

public ParseSaveFile(String currentLine, int numFigure, String tableName, int numFields, int numTables,
         String fieldName, ArrayList alFields, ArrayList alTables, BufferedReader br, File parseFile, FileReader fr) {
            this.currentLine = currentLine ;
            this.numFigure = numFigure;
            this.tableName = tableName;
            this.numFields = numFields;
            this.numTables = numTables;
            this.fieldName = fieldName;
            this.alFields = alFields;
            this.alTables = alTables;
            this.br = br;
            this.parseFile = parseFile;
            this.fr = fr;

   }

public void parseSaveFile() throws IOException { //this method is unclear and confusing in places
      logger.info("Parsing Save file...");
      StringTokenizer stTables, stNatFields, stRelFields, stNatRelFields, stField;
      EdgeTable tempTable;
      EdgeField tempField;
      currentLine = br.readLine();
      currentLine = br.readLine(); //this should be "Table: "

      while (currentLine.startsWith("Table: ")) {

         numFigure = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); //get the Table number
         currentLine = br.readLine(); //this should be "{"
         currentLine = br.readLine(); //this should be "TableName"
         tableName = currentLine.substring(currentLine.indexOf(" ") + 1);
         tempTable = new EdgeTable(numFigure + DELIM + tableName);
         
         currentLine = br.readLine(); //this should be the NativeFields list
         stNatFields = new StringTokenizer(currentLine.substring(currentLine.indexOf(" ") + 1), DELIM);
         numFields = stNatFields.countTokens();
         for (int i = 0; i < numFields; i++) {
            tempTable.addNativeField(Integer.parseInt(stNatFields.nextToken()));
         }
         
         currentLine = br.readLine(); //this should be the RelatedTables list
         stTables = new StringTokenizer(currentLine.substring(currentLine.indexOf(" ") + 1), DELIM);
         numTables = stTables.countTokens();
         for (int i = 0; i < numTables; i++) {
            tempTable.addRelatedTable(Integer.parseInt(stTables.nextToken()));
         }
         tempTable.makeArrays();
         
         currentLine = br.readLine(); //this should be the RelatedFields list
         stRelFields = new StringTokenizer(currentLine.substring(currentLine.indexOf(" ") + 1), DELIM);
         numFields = stRelFields.countTokens();

         for (int i = 0; i < numFields; i++) {
            tempTable.setRelatedField(i, Integer.parseInt(stRelFields.nextToken()));
         }

         alTables.add(tempTable);
         currentLine = br.readLine(); //this should be "}"
         currentLine = br.readLine(); //this should be "\n"
         currentLine = br.readLine(); //this should be either the next "Table: ", #Fields#
      }
      while ((currentLine = br.readLine()) != null) {
       
         stField = new StringTokenizer(currentLine, DELIM);
         
        
         numFigure = Integer.parseInt( stField.nextToken().substring(11) );
         fieldName = stField.nextToken().substring(5) ;
         tempField = new EdgeField(numFigure + DELIM + fieldName);
        
         tempField.setTableID(Integer.parseInt(stField.nextToken().substring(8).trim() ));
         tempField.setTableBound(Integer.parseInt( stField.nextToken().substring(11).trim() ));
        
         tempField.setFieldBound(Integer.parseInt( stField.nextToken().substring(11).trim() ));
         tempField.setDataType(Integer.parseInt( stField.nextToken().substring(9).trim() ));
         tempField.setVarcharValue( Integer.parseInt(stField.nextToken().substring(14).trim() ));
         tempField.setIsPrimaryKey(Boolean.valueOf(stField.nextToken()).booleanValue());
         tempField.setDisallowNull(Boolean.valueOf(stField.nextToken()).booleanValue());

         if (stField.hasMoreTokens()) { //Default Value may not be defined
            tempField.setDefaultValue( stField.nextToken().substring(14) );
         }
         alFields.add(tempField);
      }
      logger.debug("Finished parsing Save file.");
   } // parseSaveFile()


}