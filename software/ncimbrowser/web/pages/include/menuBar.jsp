<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
L--%>

<%@ page import="gov.nih.nci.evs.browser.common.*" %>
<%@ page import="gov.nih.nci.evs.browser.properties.NCImBrowserProperties" %>
<%
    NCImBrowserProperties properties = null;
    properties = NCImBrowserProperties.getInstance();
    String term_suggestion_application_url = properties.getProperty(NCImBrowserProperties.TERM_SUGGESTION_APPLICATION_URL);
    String tg_dictionary0 = "NCI%20Metathesaurus";
    String menu_bar_term_source = "NCI";
%>
<table border="0" width="100%" class="global-nav">
  <tr>
    <td align="left">
      <a href="<%= request.getContextPath() %>">Home</a> |
      <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_hierarchy.jsf?&sab=<%=menu_bar_term_source%>', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
        View NCIt Hierarchy
      </a> |
      <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
        '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
        Sources
      </a> |
      <a href="<%= request.getContextPath() %>/pages/help.jsf">Help</a>
    </td>
    <!--
    <td align="right">
      <a href="<%=term_suggestion_application_url%>?dictionary=<%=tg_dictionary0%>" target="_blank" alt="Term Suggestion">Term Suggestion</a>&nbsp;
    </td>
    -->
    
    <td align="right">
    <%
      Vector visitedConcepts = (Vector) request.getSession().getAttribute("visitedConcepts");
      if (visitedConcepts != null && visitedConcepts.size() > 0) {
          String visitedConceptsStr = DataUtils.getVisitedConceptLink(visitedConcepts);
      %> <%=visitedConceptsStr%> <%
      }
      %>&nbsp;
    </td>
    
  </tr>
</table>