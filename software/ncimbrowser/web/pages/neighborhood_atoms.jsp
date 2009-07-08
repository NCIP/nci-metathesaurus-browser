<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Concept" %>
<%
  String ncim_build_info = new DataUtils().getNCIMBuildInfo();
%>
<!-- Build info: <%=ncim_build_info%> -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>NCI Metathesaurus</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<f:view>
  <%@ include file="/pages/templates/header.xhtml" %>
  <div class="center-page">
    <%@ include file="/pages/templates/sub-header.xhtml" %>
    <!-- Main box -->
    <div id="main-area">
      <%@ include file="/pages/templates/content-header.xhtml" %>
      <!-- Page content -->
      <div class="pagecontent">
<%
            String neighborhood_sab = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("sab"));
            String neighborhood_code = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("code"));
            String sort_by = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("sortBy"));

            if (sort_by == null) sort_by = "name";
%>
              <h2>Source: <%=neighborhood_sab%></h2>

        <table class="dataTable" border="0">

        <tr>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort_by.compareTo("code") == 0) {
              %>
                 Code
              <%
              } else {
              %>
                <a href="<%=request.getContextPath() %>/pages/neighborhood_atoms.jsf?code=<%=neighborhood_code%>&sab=<%=neighborhood_sab%>&sortBy=code">Code</a>
              <%
              }
              %>
          </th>

          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort_by.compareTo("type") == 0) {
              %>
                 Type
              <%
              } else {
              %>
                <a href="<%=request.getContextPath() %>/pages/neighborhood_atoms.jsf?code=<%=neighborhood_code%>&sab=<%=neighborhood_sab%>&sortBy=type">Type</a>
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

              if (sort_by.compareTo("name") == 0) {
              %>
                 Term
              <%
              } else {
              %>
                <a href="<%=request.getContextPath() %>/pages/neighborhood_atoms.jsf?code=<%=neighborhood_code%>&sab=<%=neighborhood_sab%>&sortBy=name">Term</a>
              <%
              }
              %>
          </th>

        </tr>

    <%
      Concept concept_syn = (Concept) request.getSession().getAttribute("concept");
      Vector neighborhood_synonyms = (Vector) request.getSession().getAttribute("neighborhood_synonyms");
      if (neighborhood_synonyms == null) {
        neighborhood_synonyms = new DataUtils().getSynonyms(concept_syn, neighborhood_sab);
      }
      neighborhood_synonyms = new DataUtils().sortSynonyms(neighborhood_synonyms, sort_by);
      request.getSession().setAttribute("neighborhood_synonyms", neighborhood_synonyms);

      for (int n=0; n<neighborhood_synonyms.size(); n++)
      {
        String s = (String) neighborhood_synonyms.elementAt(n);
        Vector synonym_data = DataUtils.parseData(s, "|");
        String term_name = (String) synonym_data.elementAt(0);
        String term_type = (String) synonym_data.elementAt(1);
        String term_source = (String) synonym_data.elementAt(2);
        String term_source_code = (String) synonym_data.elementAt(3);
        String rowColor = (n%2 == 0) ? "dataRowDark" : "dataRowLight";
    %>
        <tr class="<%=rowColor%>">
          <td class="dataCellText"><%=term_source_code%></td>
          <td class="dataCellText"><%=term_type%></td>
          <td class="dataCellText"><%=term_name%></td>
        </tr>
    <%
      }
    %>
        </table>

        <p></p>
              <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?dictionary=NCI%20MetaThesaurus&code=<%=neighborhood_code%>&type=sources&sab=<%=neighborhood_sab%>&sortBy=name">Return to Sources</a>

        <%@ include file="/pages/templates/nciFooter.html" %>
      </div>
      <!-- end Page content -->
    </div>
    <div class="mainbox-bottom"><img src="<%=basePath%>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    <!-- end Main box -->
  </div>
</f:view>
</body>
</html>