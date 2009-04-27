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
  <title>NCI MetaThesaurus</title>
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
            String neighborhood_sab = (String) request.getParameter("sab");
            String neighborhood_code = (String) request.getParameter("code");
%>
              <h2>Source: <%=neighborhood_sab%></h2> 
	      <table class="dataTable" border="0">
		<tr>
		  <th class="dataTableHeader" scope="col" align="left">Source Code</th>
		  <th class="dataTableHeader" scope="col" align="left">Type</th>
		    <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/term_type_help_info.jsf',
			'_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
		      <img src="<%= request.getContextPath() %>/images/help.gif" alt="Term Type Definitions" border="0">
		    </a>
		  </th>
		  <th class="dataTableHeader" scope="col" align="left">Name</th>
		</tr>

		<%
		  Concept concept_syn = (Concept) request.getSession().getAttribute("concept");
		  Vector synonyms = new DataUtils().getSynonyms(concept_syn);
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
		      <td class="dataCellText"><%=term_source_code%></td>
		      <td class="dataCellText"><%=term_type%></td>
		      <td class="dataCellText"><%=term_name%></td>
		    </tr>
		<%
		  }
		%>
	      </table>        
        
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