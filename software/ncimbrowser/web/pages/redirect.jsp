<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page import="gov.nih.nci.evs.browser.properties.NCImBrowserProperties" %>
<%@ page import="gov.nih.nci.evs.browser.common.Constants" %>

<%
String nciterm_browser_url = NCImBrowserProperties.getTermBrowserURL();
String term_browser_formalname = (String) request.getSession().getAttribute("term_browser_dictionary");
String term_source_code = (String) request.getSession().getAttribute("term_source_code");
String term_source = (String) request.getSession().getAttribute("term_source");


request.getSession().removeAttribute("term_browser_dictionary");
request.getSession().removeAttribute("term_source_code");


String redirect_url = null;
if (term_source == null || term_source.compareTo("null") == 0) {
	//redirect_url = nciterm_browser_url + "//ncitbrowser//pages//concept_details.jsf?dictionary=" + term_browser_formalname + "&code=" + term_source_code;
	redirect_url = nciterm_browser_url + "pages/concept_details.jsf?dictionary=" + term_browser_formalname + "&code=" + term_source_code;

} else {
        request.getSession().removeAttribute("term_source");
        if (term_source == null || term_source.compareTo("null") == 0) {
	    redirect_url = request.getContextPath() + "/pages/source_hierarchy.jsf?sab=" + term_source + "&type=hierarchy";
        } else {
	    redirect_url = request.getContextPath() + "/pages/source_hierarchy.jsf?dictionary=" + Constants.CODING_SCHEME_NAME + "&code=" + term_source_code
	        + "&sab=" + term_source + "&type=hierarchy";
	}
}

String url = response.encodeRedirectURL(redirect_url);
response.sendRedirect(url);

%>     