package gov.nih.nci.evs.browser.utils;

/**
 * HTTP Utility methods
 * @author garciawa2
 *
 */
public class HTTPUtils {

    /**
     * Remove potentially bad XSS syntax 
     * @param value
     * @return
     */
    public static String cleanXSS(String value) {
    	
      if (value == null || value.length() < 1) return value; 
      value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
      value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
      value = value.replaceAll("'", "& #39;");
      value = value.replaceAll("eval\\((.*)\\)", "");
      value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']","\"\"");
      value = value.replaceAll("script", "");
      return value;
      
    }
    
}
