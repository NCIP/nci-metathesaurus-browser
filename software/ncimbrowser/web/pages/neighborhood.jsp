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
  Concept concept_neighborhood = (Concept) request.getSession().getAttribute("concept");
  String neighborhood_sab = (String) request.getSession().getAttribute("selectedConceptSource");
 
System.out.println("*** neighborhood_sab: " + neighborhood_sab); 
if (neighborhood_sab == null) {
    neighborhood_sab = (String) request.getParameter("sab");
}
  
  String concept_neighborhood_name = concept_neighborhood.getEntityDescription().getContent();
  
  String code = concept_neighborhood.getEntityCode();
  String sort_by = (String) request.getParameter("sortBy");
  if (sort_by == null) {
      sort_by = "name";
  }
  String sort_by2 = (String) request.getParameter("sortBy2");
  if (sort_by2 == null) {
      sort_by2 = "name";
  } 
  
 
  Vector neighborhood_synonyms = (Vector) request.getSession().getAttribute("neighborhood_synonyms");
  if (neighborhood_synonyms == null) {
      neighborhood_synonyms = new DataUtils().getSynonyms(concept_neighborhood, neighborhood_sab);
      request.getSession().setAttribute("neighborhood_synonyms", neighborhood_synonyms);
  }
  neighborhood_synonyms = new DataUtils().sortSynonyms(neighborhood_synonyms, sort_by);
  
%>
        
    <h2>Neighborhood of `<%=concept_neighborhood_name%>' in <%=neighborhood_sab%></h2>
    <div>
      <table class="dataTable" border="0">
        <tr>
           <td>

<b>Synonyms</b>
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
      <table class="dataTable" border="0">
      
        <tr>
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
        
	  Vector neighborhood_atoms = (Vector) request.getSession().getAttribute("neighborhood_atoms");
	  if (neighborhood_atoms == null) {
	      neighborhood_atoms = new DataUtils().getNeighborhoodSynonyms("NCI MetaThesaurus", null, concept_neighborhood.getEntityCode(), neighborhood_sab);
	      request.getSession().setAttribute("neighborhood_atoms", neighborhood_atoms);
	  }
	  neighborhood_atoms = new DataUtils().sortSynonyms(neighborhood_atoms, sort_by2);
          for (int i=0; i<neighborhood_atoms.size(); i++) {
		    String s = (String) neighborhood_atoms.elementAt(i);
		    Vector synonym_data = DataUtils.parseData(s, "|");
		    String term_name = (String) synonym_data.elementAt(0);
		    String term_type = (String) synonym_data.elementAt(1);
		    String term_source = (String) synonym_data.elementAt(2);
		    String term_source_code = (String) synonym_data.elementAt(3);
		    String rowColor = (i%2 == 0) ? "dataRowDark" : "dataRowLight";
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
                <FORM NAME="changeNeighborhoodForm" METHOD="POST" CLASS="change_neighborhood-form" >
           
		     <h:outputLabel id="conceptSourceLabel" value="Change to Neighborhood in" styleClass="textbody">
			 <h:selectOneMenu id="concept_source"
			    value="#{userSessionBean.selectedConceptSource}" valueChangeListener="#{userSessionBean.conceptSourceSelectionChanged}"
			    styleClass="textbody">
			    <f:selectItems value="#{userSessionBean.conceptSourceList}"/>
			 </h:selectOneMenu>
		     </h:outputLabel> 
		     <h:commandButton
		        id="ok"
		        value="Ok"
		        action="#{userSessionBean.viewNeighborhoodAction}"
		        alt="View Neighborhood">
		     </h:commandButton>
	        </FORM>
		     
           </td>
        </tr>
      </table>
    </div>
        
        
        
        
        
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