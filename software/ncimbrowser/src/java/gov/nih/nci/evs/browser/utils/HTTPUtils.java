package gov.nih.nci.evs.browser.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

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

		if (value == null || value.length() < 1)
			return value;
		
		try {
			value = URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Do nothing, just use the input
		}

		// Remove XSS attacks
		value = replaceAll(value,"<\\s*script\\s*>.*</\\s*script\\s*>", "");		
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		value = value.replaceAll("'", "&#39;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = replaceAll(value,"[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");		
		value = value.replaceAll("\"", "&quot;");
		return value;

	}
    
	/**
	 * @param string
	 * @param regex
	 * @param replaceWith
	 * @return
	 */
	public static String replaceAll(String string, String regex, String replaceWith) {
		
		Pattern myPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		string = myPattern.matcher(string).replaceAll(replaceWith);
		return string;
		
	} 
	
}
