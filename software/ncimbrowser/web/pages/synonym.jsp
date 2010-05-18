<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.properties.PropertyFileParser" %>
<%@ page import="gov.nih.nci.evs.browser.properties.NCImBrowserProperties" %>
<%@ page import="gov.nih.nci.evs.browser.bean.DisplayItem" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="org.LexGrid.concepts.Concept" %>
<%@ page import="org.LexGrid.concepts.Presentation" %>
<%@ page import="org.LexGrid.commonTypes.Source" %>
<%@ page import="org.LexGrid.commonTypes.EntityDescription" %>
<%@ page import="org.LexGrid.commonTypes.Property" %>
<%@ page import="org.LexGrid.commonTypes.PropertyQualifier" %>
<%@ page import="org.LexGrid.concepts.Presentation" %>
<%@ page import="org.LexGrid.commonTypes.Source" %>
<%@ page import="org.LexGrid.commonTypes.EntityDescription" %>
<%@ page import="org.LexGrid.commonTypes.Property" %>
<%@ page import="org.LexGrid.commonTypes.PropertyQualifier" %>
<%@ page import="org.apache.log4j.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>NCI Metathesaurus</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
    <script type="text/javascript"
      src="<%=request.getContextPath()%>/js/wz_tooltip.js"></script>
    <script type="text/javascript"
      src="<%=request.getContextPath()%>/js/tip_centerwindow.js"></script>
    <script type="text/javascript"
      src="<%=request.getContextPath()%>/js/tip_followscroll.js"></script>
  <%!
      private static Logger _logger = Utils.getJspLogger("synonym.jsp");
  %>     
  <f:view>
    <%@ include file="/pages/include/header.jsp" %>
    <div class="center-page">
      <%@ include file="/pages/include/sub-header.jsp" %>
      <!-- Main box -->
      <div id="main-area">
        <%@ include file="/pages/include/content-header.jsp" %>
        <!-- Page content -->
        <div class="pagecontent">
          <%
            String dictionary = null;
            Concept c = (Concept) request.getSession().getAttribute("concept");
            String name = c.getEntityDescription().getContent();
            String code = c.getEntityCode();
            String sort_by = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS(request.getParameter("sortBy"));

_logger.debug("Sortby: " + sort_by);

            if (sort_by == null) {
_logger.debug("set Sortby to: " + sort_by);
                sort_by = "name";
            }
            //request.getSession().setAttribute("sortBy", sortBy);
            Vector synonyms = (Vector) request.getSession().getAttribute("synonyms");
            if (synonyms == null) {
                synonyms = new DataUtils().getSynonyms(c);
                request.getSession().setAttribute("synonyms", synonyms);
            }
            synonyms = new DataUtils().sortSynonyms(synonyms, sort_by);
          %>
          <div class="texttitle-blue">
            <%=name%> (Code <%=code%>)
          </div>
          <hr>
          <%@ include file="/pages/include/typeLinks.jsp" %>
          <div class="tabTableContentContainer">


    <h2>Synonym Details</h2>
    <div>
      <table class="dataTable" border="0">
        <tr>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              //String sort_by = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("sortBy"));
              if (sort_by.compareTo("name") == 0) {
              %>
                 Term
              <%
              } else {
              %>
                <a href="<%=request.getContextPath() %>/pages/synonym.jsf?sortBy=name">Term</a>
              <%
              }
              %>
          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort_by != null && sort_by.compareTo("source") == 0) {
              %>
                 Source
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" border="0">
        </a>
              <%
              } else if ((sort_by == null) || sort_by != null  && sort_by.compareTo("source") != 0) {
              %>
                <a href="<%=request.getContextPath() %>/pages/synonym.jsf?sortBy=source">Source</a>
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" border="0">
        </a>
              <%
              }
              %>
          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort_by != null && sort_by.compareTo("type") == 0) {
              %>
                 Type
              <%
              } else if ((sort_by == null) || sort_by != null  && sort_by.compareTo("type") != 0) {
              %>
                <a href="<%=request.getContextPath() %>/pages/synonym.jsf?sortBy=type">Type</a>
              <%
              }
              %>
              <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/term_type_help_info.jsf',
                '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
                <img src="<%= request.getContextPath() %>/images/help.gif" alt="Term Type Definitions" border="0">
              </a>
          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort_by != null && sort_by.compareTo("code") == 0) {
              %>
                 Code
              <%
              } else if ((sort_by == null) || sort_by != null && sort_by.compareTo("code") != 0) {
              %>
                <a href="<%=request.getContextPath() %>/pages/synonym.jsf?sortBy=code">Code</a>
              <%
              }
              %>
          </th>
        </tr>

        <%
          for (int n=0; n<synonyms.size(); n++)
          {
            String s = (String) synonyms.elementAt(n);
            Vector synonym_data = DataUtils.parseData(s, "|");
            String term_name = (String) synonym_data.elementAt(0);
            String term_type = (String) synonym_data.elementAt(1);
            String term_source = (String) synonym_data.elementAt(2);
            String term_source_code = (String) synonym_data.elementAt(3);
            String rowColor = (n%2 == 0) ? "dataRowDark" : "dataRowLight";
        %>
            <tr class="<%=rowColor%>">
              <td class="dataCellText"><%=term_name%></td>
              <td class="dataCellText"><%=term_source%></td>
              <td class="dataCellText"><%=term_type%></td>
              <td class="dataCellText"><%=term_source_code%></td>
            </tr>
        <%
          }
        %>
      </table>
    </div>

          </div>
        </div>
        <!-- end Page content -->
      </div>
      <div class="mainbox-bottom"><img src="<%=basePath%>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
      <!-- end Main box -->
    </div>
  </f:view>
</body>
</html>




