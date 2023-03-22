<%@ page import="gov.nih.nci.evs.browser.common.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>


<%@ page import="gov.nih.nci.evs.browser.properties.NCImBrowserProperties" %>
<%@ page import="java.util.Vector"%>

<%
    NCImBrowserProperties properties = null;
    properties = NCImBrowserProperties.getInstance();
    String term_suggestion_application_url = properties.getProperty(NCImBrowserProperties.TERM_SUGGESTION_APPLICATION_URL);
    String tg_dictionary0 = "NCI%20Metathesaurus";
    String menu_bar_term_source = "NCI";
%>
<table border="0" width="100%" class="global-nav" role='presentation'>
  <tr>
    <td align="left">
      <a href="<%= request.getContextPath() %>" tabindex="0">Home</a> |
      <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_hierarchy.jsf?&sab=<%=menu_bar_term_source%>', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');" tabindex="0">
        NCIt Hierarchy</a> |
      <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
        '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');" tabindex="0">
        Sources</a> |
      <a href="<%= request.getContextPath() %>/pages/help.jsf" alt="Help" tabindex="0">Help</a>
    </td>
    <td align="right">
    <!--
	  <c:choose>	
		<c:when test="${sessionScope.CartActionBean.count>0}">
		  <a href="<%= request.getContextPath() %>/pages/cart.jsf" tabindex="0">Cart</a>&nbsp;|
	        </c:when>
          </c:choose>
    -->
<%
CartActionBean cartbean = (CartActionBean) request.getSession().getAttribute("cartActionBean"); 
if (cartbean != null && cartbean.getCount() > 0) {
%>
  <a href="<%= request.getContextPath() %>/pages/cart.jsf" tabindex="0">Cart</a>
  |
<%     
}
%>
      
    <%
      Vector visitedConcepts = (Vector) request.getSession().getAttribute("visitedConcepts");
      if (visitedConcepts != null && visitedConcepts.size() > 0) {
          String visitedConceptsStr = DataUtils.getVisitedConceptLink(visitedConcepts);
      %> 
      <%=visitedConceptsStr%>
      <%
      }
      %>&nbsp;
    </td>
  </tr>
</table>
