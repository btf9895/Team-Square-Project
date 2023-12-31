import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EdgeConnector {
   private static Logger logger = LogManager.getLogger(EdgeConnector.class.getName());

   private int numConnector, endPoint1, endPoint2;
   private String endStyle1, endStyle2;
   private boolean isEP1Field, isEP2Field, isEP1Table, isEP2Table;
      
   public EdgeConnector(String inputString) {
      StringTokenizer st = new StringTokenizer(inputString, EdgeConvertFileParser.DELIM);
      numConnector = Integer.parseInt(st.nextToken());
      endPoint1 = Integer.parseInt(st.nextToken());
      endPoint2 = Integer.parseInt(st.nextToken());
      endStyle1 = st.nextToken();
      endStyle2 = st.nextToken();
      isEP1Field = false;
      isEP2Field = false;
      isEP1Table = false;
      isEP2Table = false;

      logger.trace("Edge Connector data: {}{}{}{}{}{}{}{}{}", numConnector, endPoint1, endPoint2, endStyle1, endStyle2, isEP1Field, isEP2Field, isEP1Table, isEP2Table);
}
   
   public int getNumConnector() {
      logger.debug("numConnector value: {}", numConnector);
      return numConnector;
   }
   
   public int getEndPoint1() {
      logger.debug("endPoint1 value: {}", endPoint1);
      return endPoint1;
   }
   
   public int getEndPoint2() {
      logger.debug("endPoint2 value: {}", endPoint2);
      return endPoint2;
   }
   
   public String getEndStyle1() {
      logger.debug("endStyle1 value: {}", endStyle1);
      return endStyle1;
   }
   
   public String getEndStyle2() {
      logger.debug("endStyle2 value: {}", endStyle2);
      return endStyle2;
   }
   public boolean getIsEP1Field() {
      logger.debug("isEP1Field value: {}", isEP1Field);
      return isEP1Field;
   }
   
   public boolean getIsEP2Field() {
      logger.debug("isEP2Field value: {}", isEP2Field);
      return isEP2Field;
   }

   public boolean getIsEP1Table() {
      logger.debug("isEP1Table value: {}", isEP1Table);
      return isEP1Table;
   }

   public boolean getIsEP2Table() {
      logger.debug("isEP2Table value: {}", isEP2Table);
      return isEP2Table;
   }

   public void setIsEP1Field(boolean value) {
      logger.debug("New isEP1Field value: {}", value);
      isEP1Field = value;
   }
   
   public void setIsEP2Field(boolean value) {
      logger.debug("New isEP2Field value: {}", value);
      isEP2Field = value;
   }

   public void setIsEP1Table(boolean value) {
      logger.debug("New isEP1Table value: {}", value);
      isEP1Table = value;
   }

   public void setIsEP2Table(boolean value) {
      logger.debug("New isEP2Table value: {}", value);
      isEP2Table = value;
   }
}
