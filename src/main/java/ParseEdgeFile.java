import java.io.*;
import java.util.*;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//functional
public class ParseEdgeFile {
   private File parseFile;
   private FileReader fr;
   private BufferedReader br;
   
    String currentLine;
    
    String tableName;
    private EdgeField tempField;

    String fieldName;
    ArrayList alFields, alTables, alConnectors;



    private String style;
    private String text;

    private boolean isEntity, isAttribute, isUnderlined = false;
    private int numFigure, numConnector, numFields, numTables, numNativeRelatedFields;
    private int endPoint1, endPoint2;
    private int numLine;
    private String endStyle1, endStyle2;

   public static final String EDGE_ID = "EDGE Diagram File"; //first line of .edg files should be this
   public static final String SAVE_ID = "EdgeConvert Save File"; //first line of save files should be this
   public static final String DELIM = "|";
   private static final Logger logger = LogManager.getLogger(EdgeConvertFileParser.class);

    public ParseEdgeFile(String currentLine, int numFigure, String tableName, int numFields, int numTables,
         String fieldName, ArrayList alFields, ArrayList alTables, BufferedReader br, File parseFile, FileReader fr,
          boolean isEntity, boolean isAttribute, String style, String text, int numConnector, int endPoint1,
           int endPoint2, int numLine, String endStyle1, String endStyle2, ArrayList alConnectors) {
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
            this.isEntity = isEntity;
            this.isAttribute = isAttribute;
            this.style = style;
            this.text = text; 
            this.numConnector = numConnector;
            this.endPoint1 = endPoint1;
            this.endPoint2 = endPoint2;
            this.alConnectors = alConnectors;
            this.numLine = numLine;
            this.endStyle1 = endStyle1;
            this.endStyle2 = endStyle2;
   }

   public void parseEdgeFile() throws IOException {
        logger.info("Parsing Edge Diagrammer file...");
        while ((currentLine = br.readLine()) != null) {
        currentLine = currentLine.trim();
        if (currentLine.startsWith("Figure ")) { //this is the start of a Figure entry
            numFigure = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); //get the Figure number
            currentLine = br.readLine().trim(); // this should be "{"
            currentLine = br.readLine().trim();
            if (!currentLine.startsWith("Style")) { // this is to weed out other Figures, like Labels
                continue;
            } else {
                style = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")); //get the Style parameter
                if (style.startsWith("Relation")) { //presence of Relations implies lack of normalization
                    JOptionPane.showMessageDialog(null, "The Edge Diagrammer file\n" + parseFile + "\ncontains relations.  Please resolve them and try again.");
                    EdgeConvertGUI.setReadSuccess(false);
                    break;
                } 
                if (style.startsWith("Entity")) {
                    isEntity = true;
                }
                if (style.startsWith("Attribute")) {
                    isAttribute = true;
                }
                if (!(isEntity || isAttribute)) { //these are the only Figures we're interested in
                    continue;
                }
                currentLine = br.readLine().trim(); //this should be Text
                text = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")).replaceAll(" ", ""); //get the Text parameter
                if (text.equals("")) {
                    JOptionPane.showMessageDialog(null, "There are entities or attributes with blank names in this diagram.\nPlease provide names for them and try again.");
                    logger.warn("There are entities or attributes with blank names in this diagram. Please provide names for them and try again.");
                    EdgeConvertGUI.setReadSuccess(false);
                    break;
                }
                int escape = text.indexOf("\\");
                if (escape > 0) { //Edge denotes a line break as "\line", disregard anything after a backslash
                    logger.warn("A line break is denoted using a backslash '\\'. Disregarding anything after the backslash.");
                    text = text.substring(0, escape);
                }

                do { //advance to end of record, look for whether the text is underlined
                    currentLine = br.readLine().trim();
                    if (currentLine.startsWith("TypeUnderl")) {
                    isUnderlined = true;
                    }
                } while (!currentLine.equals("}")); // this is the end of a Figure entry
                
                if (isEntity) { //create a new EdgeTable object and add it to the alTables ArrayList
                    if (isTableDup(text)) {
                    JOptionPane.showMessageDialog(null, "There are multiple tables called " + text + " in this diagram.\nPlease rename all but one of them and try again.");
                    EdgeConvertGUI.setReadSuccess(false);
                    break;
                    }
                    alTables.add(new EdgeTable(numFigure + DELIM + text));
                }
                if (isAttribute) { //create a new EdgeField object and add it to the alFields ArrayList
                    tempField = new EdgeField(numFigure + DELIM + text);
                    tempField.setIsPrimaryKey(isUnderlined);
                    alFields.add(tempField);
                }
                //reset flags
                isEntity = false;
                isAttribute = false;
                isUnderlined = false;
            }
        } // if("Figure")
        if (currentLine.startsWith("Connector ")) { //this is the start of a Connector entry
            numConnector = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); //get the Connector number
            currentLine = br.readLine().trim(); // this should be "{"
            currentLine = br.readLine().trim(); // not interested in Style
            currentLine = br.readLine().trim(); // Figure1
            endPoint1 = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
            currentLine = br.readLine().trim(); // Figure2
            endPoint2 = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
            currentLine = br.readLine().trim(); // not interested in EndPoint1
            currentLine = br.readLine().trim(); // not interested in EndPoint2
            currentLine = br.readLine().trim(); // not interested in SuppressEnd1
            currentLine = br.readLine().trim(); // not interested in SuppressEnd2
            currentLine = br.readLine().trim(); // End1
            endStyle1 = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")); //get the End1 parameter
            currentLine = br.readLine().trim(); // End2
            endStyle2 = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")); //get the End2 parameter

            do { //advance to end of record
                currentLine = br.readLine().trim();
            } while (!currentLine.equals("}")); // this is the end of a Connector entry
            
            alConnectors.add(new EdgeConnector(numConnector + DELIM + endPoint1 + DELIM + endPoint2 + DELIM + endStyle1 + DELIM + endStyle2));
        } // if("Connector")
        } // while()
        logger.debug("Finished parsing Edge Diagrammer file.");
 } // parseEdgeFile()


 private boolean isTableDup(String testTableName) {
    for (int i = 0; i < alTables.size(); i++) {
       EdgeTable tempTable = (EdgeTable)alTables.get(i);
       if (tempTable.getName().equals(testTableName)) {
          return true;
       }
    }
    return false;
 }

}