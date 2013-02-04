package gov.nih.nci.evs.browser.utils;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

import javax.faces.context.*;
import javax.servlet.http.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction
 * with the National Cancer Institute, and so to the extent government
 * employees are co-authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the disclaimer of Article 3,
 *      below. Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions and the following
 *      disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution,
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not
 *      authorize the recipient to use any trademarks owned by either NCI
 *      or NGIT
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT,
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * HTTP Utility methods
 *
 * @author garciawa2
 *
 */
public class HTTPUtils {

    private final static String REFERER = "referer";

    /**
     * Remove potentially bad XSS syntax
     *
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
        } catch (IllegalArgumentException e) {
            // Do nothing, just use the input
            // Note: The following exception was triggered:
            // java.lang.IllegalArgumentException: URLDecoder: Illegal hex
            // characters in escape (%) pattern - For input string: "^&".
        }

        // Remove XSS attacks
        value = replaceAll(value, "<\\s*(?i)script\\s*>.*</\\s*(?i)script\\s*>", "");
        value = value.replaceAll(".*<\\s*(?i)iframe.*>", "");
        value = value.replaceAll(".*<\\s*(?i)img.*>", "");
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value =
            replaceAll(value, "[\\\"\\\'][\\s]*(?i)javascript:(.*)[\\\"\\\']",
                "\"\"");
        value = value.replaceAll("\"", "&quot;");
        return value;

    }

    /**
     * @param string
     * @param regex
     * @param replaceWith
     * @return
     */
    public static String replaceAll(String string, String regex,
        String replaceWith) {

        Pattern myPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        string = myPattern.matcher(string).replaceAll(replaceWith);
        return string;

    }

    /**
     * @param name
     * @param classPath
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object getBean(String name, String classPath) {
        try {
            Map<String, Object> map =
                FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap();

            Object bean = map.get(name);
            if (bean == null) {
                Class klass = Class.forName(classPath);
                bean = klass.newInstance();
                map.put(name, bean);
            }
            return bean;
        } catch (Exception e) {
			e.printStackTrace();
            return null;
        }
    }

    /**
     * @return
     */
    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance()
            .getExternalContext().getRequest();
    }

    /**
     * @param request
     * @param attributeName
     * @param defaultValue
     * @return
     */
    public static String getAttribute(HttpServletRequest request,
        String attributeName, String defaultValue) {
        String value = (String) request.getAttribute(attributeName);
        return getValue(value, defaultValue);
    }

    /**
     * @param request
     * @param attributeName
     * @param defaultValue
     * @return
     */
    public static String getSessionAttribute(HttpServletRequest request,
        String attributeName, String defaultValue) {
        String value =
            (String) request.getSession().getAttribute(attributeName);
        return getValue(value, defaultValue);
    }

    /**
     * @param value
     * @param defaultValue
     * @return
     */
    public static String getValue(String value, String defaultValue) {
        if (value == null || value.trim().length() <= 0 || value.equals("null"))
            return defaultValue;
        return value;
    }

    /**
     * @param request
     * @return
     */
    public static String getRefererParmEncode(HttpServletRequest request) {
        String iref = request.getHeader(REFERER);
        String referer = "N/A";
        if (iref != null)
            try {
                referer = URLEncoder.encode(iref, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // return N/A if encoding is not supported.
            }
        return cleanXSS(referer);
    }

    /**
     * @param request
     * @return
     */
    public static String getRefererParmDecode(HttpServletRequest request) {
        String refurl = "N/A";
        try {
            String iref = request.getParameter(REFERER);
            if (iref != null)
                refurl =
                    URLDecoder.decode(request.getParameter(REFERER), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // return N/A if encoding is not supported.
        }
        return cleanXSS(refurl);
    }

    /**
     * @param request
     */
    public static void clearRefererParm(HttpServletRequest request) {
        request.setAttribute(REFERER, null);
    }

    /**
     * @param t
     * @return
     */
    public static String convertJSPString(String t) {
        // Convert problem characters to JavaScript Escaped values
        if (t == null) {
            return "";
        }

        if (t.compareTo("") == 0) {
            return "";
        }

        String sigleQuoteChar = "'";
        String doubleQuoteChar = "\"";

        String dq = "&quot;";

        t = t.replaceAll(sigleQuoteChar, "\\" + sigleQuoteChar);
        t = t.replaceAll(doubleQuoteChar, "\\" + dq);
        t = t.replaceAll("\r", "\\r"); // replace CR with \r;
        t = t.replaceAll("\n", "\\n"); // replace LF with \n;

        return t;
    }

}
