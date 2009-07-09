<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Concept" %>
<%@ page import="gov.nih.nci.evs.browser.common.Constants" %>

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
  <link rel="icon" href="/ncimbrowser/favicon.ico" type="image/x-icon">
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
  String code = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("code"));
  Concept concept_neighborhood = null;
  if (code == null) {
    concept_neighborhood = (Concept) request.getSession().getAttribute("concept");
    code = concept_neighborhood.getEntityCode();
  } else {
          concept_neighborhood = DataUtils.getConceptByCode(Constants.CODING_SCHEME_NAME, null, null, code);
          request.getSession().setAttribute("concept", concept_neighborhood);
          request.getSession().removeAttribute("neighborhood_atoms");
          request.getSession().removeAttribute("neighborhood_synonyms");
  }

  Vector source_vec = new DataUtils().getSources("NCI MetaThesaurus", null, null, code);

  String neighborhood_sab = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("selectedConceptSource"));

  if (neighborhood_sab == null) {
    neighborhood_sab = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("sab"));
  }

  Vector src_vec = new Vector();
  for (int k=0; k<source_vec.size(); k++) {
      String src = (String) source_vec.elementAt(k);
      if (src.compareTo(neighborhood_sab) != 0) src_vec.add(src);
  }


  String concept_neighborhood_name = concept_neighborhood.getEntityDescription().getContent();

  Vector neighborhood_synonyms = (Vector) request.getSession().getAttribute("neighborhood_synonyms");
  if (neighborhood_synonyms == null) {
      neighborhood_synonyms = new DataUtils().getSynonyms(concept_neighborhood, neighborhood_sab);
      request.getSession().setAttribute("neighborhood_synonyms", neighborhood_synonyms);
  }

  Vector neighborhood_atoms = (Vector) request.getSession().getAttribute("neighborhood_atoms");
  if (neighborhood_atoms == null) {
      neighborhood_atoms = new DataUtils().getNeighborhoodSynonyms(Constants.CODING_SCHEME_NAME, null, concept_neighborhood.getEntityCode(), neighborhood_sab);
      request.getSession().setAttribute("neighborhood_atoms", neighborhood_atoms);
  }

  String sort_by = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("sortBy"));
  if (sort_by == null) {
      sort_by = "name";
  }
  String sort_by2 = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("sortBy2"));
  if (sort_by2 == null) {
      sort_by2 = "name";
  }
  neighborhood_synonyms = new DataUtils().sortSynonyms(neighborhood_synonyms, sort_by);
  neighborhood_atoms = new DataUtils().sortSynonymData(neighborhood_atoms, sort_by2);
%>
    <h2>Concept information of `<%=concept_neighborhood_name%>' from <%=neighborhood_sab%></h2>
    <div>
      <table class="dataTable" border="0">
        <%
        if (src_vec.size() > 0) {
        %>
        <tr>
           <td styleClass="textbody">
           Switch to: &nbsp;
        <%
          for (int n=0; n<src_vec.size(); n++)
          {
              String s = (String) src_vec.elementAt(n);
              if (s.compareTo("NCI") == 0) {
              %>
        <a href="<%= request.getContextPath() %>/pages/neighborhood.jsf?dictionary=NCI%20MetaThesaurus&code=<%=code%>&sab=<%=s%>">
           <%=s%>
        </a>&nbsp;
        <%
        }
          }
          for (int n=0; n<src_vec.size(); n++)
          {
              String s = (String) src_vec.elementAt(n);
              if (s.compareTo("NCI") != 0) {
              %>
        <a href="<%= request.getContextPath() %>/pages/neighborhood.jsf?dictionary=NCI%20MetaThesaurus&code=<%=code%>&sab=<%=s%>">
           <%=s%>
        </a>&nbsp;
        <%
        }
          }
        }
        %>
           </td>
        </tr>
        <tr>
           <td>
<br/><b>Synonyms</b>
      <table class="dataTable" border="0">

        <tr>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort_by == null || sort_by.compareTo("name") == 0) {
              %>
                 Term
              <%
              } else {
              %>
                <a href="<%=request.getContextPath() %>/pages/neighborhood.jsf?sortBy=name&&sortBy2=<%=sort_by2%>&&sab=<%=neighborhood_sab%>">Term</a>
              <%
              }
              %>
          </th>
<!--
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort_by != null && sort_by.compareTo("source") == 0) {
              %>
                 Source
              <%
              } else if ((sort_by == null) || sort_by != null  && sort_by.compareTo("source") != 0) {
              %>
                <a href="<%=request.getContextPath() %>/pages/neighborhood.jsf?sortBy=source&&sortBy2=<%=sort_by2%>&&sab=<%=neighborhood_sab%>">Source</a>
              <%
              }
              %>
          </th>
-->

          <th class="dataTableHeader" scope="col" align="left">
                 Source
          </th>

          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort_by != null && sort_by.compareTo("type") == 0) {
              %>
                 Type
              <%
              } else if ((sort_by == null) || sort_by != null  && sort_by.compareTo("type") != 0) {
              %>
                <a href="<%=request.getContextPath() %>/pages/neighborhood.jsf?sortBy=type&&sortBy2=<%=sort_by2%>&&sab=<%=neighborhood_sab%>">Type</a>
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
                <a href="<%=request.getContextPath() %>/pages/neighborhood.jsf?sortBy=code&&sortBy2=<%=sort_by2%>&&sab=<%=neighborhood_sab%>">Code</a>
              <%
              }
              %>
          </th>
        </tr>


        <%
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
              <td class="dataCellText"><%=term_name%></td>
              <td class="dataCellText"><%=term_source%></td>
              <td class="dataCellText"><%=term_type%></td>
              <td class="dataCellText"><%=term_source_code%></td>
            </tr>
        <%
          }
        %>
      </table>
</p>
           </td>
        </tr>
        <tr>
           <td>

<b>Neighborhood</b>
<%
if (neighborhood_atoms.size() == 0) {
%>
<br/>
<i>(No data is found.)</i>
<%
} else {
%>
      <table class="dataTable" border="0">

        <tr>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort_by2 == null || sort_by2.compareTo("rel_type") == 0) {
              %>
                 Relationship
              <%
              } else {
              %>
                <a href="<%=request.getContextPath() %>/pages/neighborhood.jsf?sortBy2=rel_type&&sortBy=<%=sort_by%>&&sab=<%=neighborhood_sab%>">Relationship</a>
              <%
              }
              %>
          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort_by2 == null || sort_by2.compareTo("rel") == 0) {
              %>
                 Rel. Label
              <%
              } else {
              %>
                <a href="<%=request.getContextPath() %>/pages/neighborhood.jsf?sortBy2=rel&&sortBy=<%=sort_by%>&&sab=<%=neighborhood_sab%>">Rel. Label</a>
              <%
              }
              %>
          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort_by2 == null || sort_by2.compareTo("cui") == 0) {
              %>
                 CUI
              <%
              } else {
              %>
                <a href="<%=request.getContextPath() %>/pages/neighborhood.jsf?sortBy2=cui&&sortBy=<%=sort_by%>&&sab=<%=neighborhood_sab%>">CUI</a>
              <%
              }
              %>
          </th>

          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort_by2 == null || sort_by2.compareTo("name") == 0) {
              %>
                 Term
              <%
              } else {
              %>
                <a href="<%=request.getContextPath() %>/pages/neighborhood.jsf?sortBy2=name&&sortBy=<%=sort_by%>&&sab=<%=neighborhood_sab%>">Term</a>
              <%
              }
              %>
          </th>
<!--
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort_by2 != null && sort_by2.compareTo("source") == 0) {
              %>
                 Source
              <%
              } else if ((sort_by2 == null) || sort_by2 != null  && sort_by.compareTo("source") != 0) {
              %>
                <a href="<%=request.getContextPath() %>/pages/neighborhood.jsf?sortBy2=source&&sortBy=<%=sort_by%>&&sab=<%=neighborhood_sab%>">Source</a>
              <%
              }
              %>
          </th>
-->
          <th class="dataTableHeader" scope="col" align="left">
                 Source
          </th>

          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort_by2 != null && sort_by2.compareTo("type") == 0) {
              %>
                 Type
              <%
              } else if ((sort_by2 == null) || sort_by2 != null  && sort_by.compareTo("type") != 0) {
              %>
                <a href="<%=request.getContextPath() %>/pages/neighborhood.jsf?sortBy2=type&&sortBy=<%=sort_by%>&&sab=<%=neighborhood_sab%>">Type</a>
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
              if (sort_by2 != null && sort_by2.compareTo("code") == 0) {
              %>
                 Code
              <%
              } else if ((sort_by2 == null) || sort_by2 != null && sort_by2.compareTo("code") != 0) {
              %>
                <a href="<%=request.getContextPath() %>/pages/neighborhood.jsf?sortBy2=code&&sortBy=<%=sort_by%>&&sab=<%=neighborhood_sab%>">Code</a>
              <%
              }
              %>
          </th>
        </tr>

        <%
          for (int i=0; i<neighborhood_atoms.size(); i++) {
        String s = (String) neighborhood_atoms.elementAt(i);
        Vector synonym_data = DataUtils.parseData(s, "|");
        String term_name = (String) synonym_data.elementAt(0);
        String term_type = (String) synonym_data.elementAt(1);
        String term_source = (String) synonym_data.elementAt(2);
        String term_source_code = (String) synonym_data.elementAt(3);
        String cui = (String) synonym_data.elementAt(4);
        String rel = (String) synonym_data.elementAt(5);
        String rel_type = (String) synonym_data.elementAt(6);

        String rowColor = (i%2 == 0) ? "dataRowDark" : "dataRowLight";
    %>
        <tr class="<%=rowColor%>">
          <td class="dataCellText"><%=rel_type%></td>
          <td class="dataCellText"><%=rel%></td>
          <td class="dataCellText">
        <a href="<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=Constants.CODING_SCHEME_NAME%>&code=<%=cui%>">
          <%=cui%>
        </a>
          </td>

          <td class="dataCellText"><a href="<%=request.getContextPath() %>/pages/neighborhood.jsf?code=<%=cui%>&&sab=<%=neighborhood_sab%>"><%=term_name%></a></td>
          <td class="dataCellText"><%=term_source%></td>
          <td class="dataCellText"><%=term_type%></td>
          <td class="dataCellText"><a href="<%=request.getContextPath() %>/pages/neighborhood.jsf?code=<%=cui%>&&sab=<%=neighborhood_sab%>"><%=term_source_code%></a></td>
        </tr>
    <%
   }
        %>
      </table>
<%
}
%>
</p>
          </td>
        </tr>
      </table>
    </div>


      <p></p>
      Return to <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?dictionary=NCI%20MetaThesaurus&code=<%=code%>&type=sources&sab=<%=neighborhood_sab%>&sortBy=name">Sources</a>



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