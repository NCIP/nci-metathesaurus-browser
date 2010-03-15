<%@ page import="gov.nih.nci.evs.browser.properties.NCImBrowserProperties" %>
<%@ page import="gov.nih.nci.evs.browser.utils.MetadataUtils" %>

<%
  HashMap hmap = MetadataUtils.getSAB2FormalNameHashMap();
  String entry_type_syn = type;
  String available_hierarchies = NCImBrowserProperties.getSourceHierarchies();
  
  String nciterm_browser_url = NCImBrowserProperties.getTermBrowserURL();
  
  if (type.compareTo("synonym") == 0 || type.compareTo("all") == 0)
  {
    Concept syn_details_concept = (Concept) request.getSession().getAttribute("concept");
    String syn_details_concept_code = syn_details_concept.getEntityCode();
    %>
    <span class="textsubtitle-blue">Synonym Details</span><a name="SynonymsDetails"></a>
      <table class="dataTable" border="0" width=1000>
        <tr>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              String sort_by = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS(request.getParameter("sortBy"));
              if (sort_by == null) {
                  sort_by = "name";
              }
              if (sort_by.compareTo("name") == 0) {
              %>
                 Term
              <%
              } else {
              %>
              <a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=<%=syn_details_concept_code%>&type=<%=entry_type_syn%>&sortBy=name#SynonymsDetails">Term</a>
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
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
        </a>
              <%
              } else if ((sort_by == null) || sort_by != null  && sort_by.compareTo("source") != 0) {
              %>
                <a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=<%=syn_details_concept_code%>&type=<%=entry_type_syn%>&sortBy=source#SynonymsDetails">Source</a>
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
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
                <a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=<%=syn_details_concept_code%>&type=<%=entry_type_syn%>&sortBy=type#SynonymsDetails">Type</a>
              <%
              }
              %>
              <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/term_type_help_info.jsf',
                '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
                <img src="<%= request.getContextPath() %>/images/help.gif" alt="Term Type Definitions" title="Term Type Definitions" border="0">
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
                <a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=<%=syn_details_concept_code%>&type=<%=entry_type_syn%>&sortBy=code#SynonymsDetails">Code</a>
              <%
              }
              %>
          </th>
        </tr>
        <%
          Concept concept_syn = (Concept) request.getSession().getAttribute("concept");
          Vector synonyms = (Vector) request.getSession().getAttribute("synonyms");
          //if (synonyms == null) {
              synonyms = new DataUtils().getSynonyms(concept_syn);
          //    request.getSession().setAttribute("synonyms", synonyms);
          //}
          synonyms = new DataUtils().sortSynonyms(synonyms, sort_by);
          for (int n=0; n<synonyms.size(); n++)
          {
            String s = (String) synonyms.elementAt(n);
            Vector synonym_data = DataUtils.parseData(s, "|");
            String term_name = (String) synonym_data.elementAt(0);
            String term_type = (String) synonym_data.elementAt(1);
            String term_source = (String) synonym_data.elementAt(2);
            String term_browser_formalname = null;
            String term_source_code = (String) synonym_data.elementAt(3);
            
            if (term_source != null 
                && term_source.compareTo("") != 0 
                && term_source.compareTo("null") != 0
                && term_source_code != null 
                && term_source_code.compareTo("") != 0 
                && term_source_code.compareTo("null") != 0
                && hmap.containsKey(term_source)) {
                term_browser_formalname = (String) hmap.get(term_source);
            }
            
            String rowColor = (n%2 == 0) ? "dataRowDark" : "dataRowLight";
        %>
            <tr class="<%=rowColor%>">
              <td class="dataCellText" width=675><%=term_name%></td>
              <td class="dataCellText" width=100><%=term_source%></td>
              <td class="dataCellText" width=100><%=term_type%></td>
              
              <%
              if (term_browser_formalname == null) {
              %>              
              <td class="dataCellText" width=125><%=term_source_code%></td>
              <%
              } else {     
              %>

                  <td class="dataCellText" width=100>
                
                  <a href="#" onclick="javascript:window.open('<%=nciterm_browser_url%>/pages/concept_details.jsf?dictionary=<%=term_browser_formalname%>&code=<%=term_source_code%>',
                  '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
                      <%=term_source_code%>
                  </a>
<%
if (term_source != null) {
	if (available_hierarchies != null && available_hierarchies.indexOf("|" + term_source + "|") != -1) {
	    String cs_name = Constants.CODING_SCHEME_NAME;
	%>
		  <a class="icon_blue" href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_hierarchy.jsf?dictionary=<%=cs_name%>&code=<%=id%>&sab=<%=term_source%>&type=hierarchy', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
			<img src="<%=basePath%>/images/visualize.gif" width="16px" height="16px" alt="tree" border="0"/>
		  </a>                 
	<% 
	}
} 
%>                  
                  </td> 
              <%
              }
              %>      
              
            </tr>
        <%
          }
        %>
      </table>
    <%
  }
%>