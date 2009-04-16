<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Concept" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>NCI Thesaurus Browser Home</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
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
          Vector v = (Vector) request.getSession().getAttribute("search_results");
          String matchText = (String) request.getSession().getAttribute("matchText");
          String match_size = (String) request.getSession().getAttribute("match_size");
          String page_string = (String) request.getSession().getAttribute("page_string");
          String page_number = (String) request.getParameter("page_number");
          String selectedResultsPerPage = (String) request.getParameter("selectedResultsPerPage");
          if (page_number != null)
          {
              page_string = page_number;
          }
          int page_num = Integer.parseInt(page_string);
          int next_page_num = page_num + 1;
          int prev_page_num = page_num - 1;
          int page_size = 50;
          if (selectedResultsPerPage != null)
          {
              page_size = Integer.parseInt(selectedResultsPerPage);
          }
          int iend = page_num * page_size;
          int istart = iend - page_size;
          if (iend > v.size()) iend = v.size();
          int num_pages = v.size() / page_size;
          if (num_pages * page_size < v.size()) num_pages++;
          String istart_str = Integer.toString(istart+1);
          String iend_str = Integer.toString(iend);
          String prev_page_num_str = Integer.toString(prev_page_num);
          String next_page_num_str = Integer.toString(next_page_num);
        %>
        <table width="100%">
          <tr>
            <table>
              <tr>
                <td class="texttitle-blue">Result for:</td>
                <td class="texttitle-gray"><%=matchText%></td>
              </tr>
            </table>
          </tr>
          <tr>
            <td><hr></td>
          </tr>
          <tr>
            <td>
              <b>Results <%=istart_str%>-<%=iend_str%> of&nbsp;<%=match_size%> for: <%=matchText%></b>
            </td>
          </tr>
          <tr>
            <td class="textbody">
              <table class="dataTable" summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
                <%
                  for (int i=istart; i<iend; i++) {
                    if (i >= 0 && i<v.size()) {
                      Concept c = (Concept) v.elementAt(i);
                      String code = c.getId();
                      String name = c.getEntityDescription().getContent();

                      if (i % 2 == 0) {
                        %>
                          <tr class="dataRowDark">
                        <%
                      } else {
                        %>
                          <tr class="dataRowLight">
                        <%
                      }
                      %>
                          <td class="dataCellText">
                            <a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=NCI_Thesaurus&code=<%=code%>" ><%=name%></a>
                          </td>
                        </tr>
                      <%
                    }
                  }
                %>
              </table>
            </td>
          </tr>
        </table>
        <%@ include file="/pages/templates/pagination.xhtml" %>
        <%@ include file="/pages/templates/nciFooter.html" %>
      </div>
      <!-- end Page content -->
    </div>
    <div class="mainbox-bottom"><img src="images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    <!-- end Main box -->
  </div>
</f:view>
</body>
</html>