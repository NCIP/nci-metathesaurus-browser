<%@ page import="gov.nih.nci.evs.browser.properties.NCImBrowserProperties" %>
<%@ page import="gov.nih.nci.evs.browser.utils.NCImMetadataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.bean.LicenseBean" %>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.common.*" %>

<%@ page contentType="text/html; charset=UTF-8" %>


<%
  HashMap hmap = NCImMetadataUtils.getSAB2FormalNameHashMap();
  String entry_type_syn = type;
  String available_hierarchies = NCImBrowserProperties.getSourceHierarchies();

  String nciterm_browser_url = NCImBrowserProperties.getTermBrowserURL();
  String secured_vocabularies = NCImBrowserProperties.getSecuredVocabularies();
  
  LicenseBean licenseBean = (LicenseBean) request.getSession().getAttribute("licenseBean");
  if (licenseBean == null) {
      licenseBean = new LicenseBean();
      request.getSession().setAttribute("licenseBean", licenseBean);
  }
 
  if (type.compareTo("synonym") == 0 || type.compareTo("all") == 0)
  {
    Entity syn_details_concept = (Entity) request.getSession().getAttribute("concept");
    String syn_details_concept_code = syn_details_concept.getEntityCode();
    %>
	<table border="0" width="708px">
		<tr>
			<td class="textsubtitle-blue" align="left">Synonym Details:<a name="SynonymsDetails"></a></td>
		</tr>
	</table>    
      <table class="datatable_960" border="0" width="100%">
      
  <col width="530">
  <col width="120">
  <col width="80">
  <col width="200">      
      
        <tr>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              String sort_by = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("sortBy"));
              if (sort_by == null) {
                  sort_by = "name";
              }
              if (sort_by.compareTo("name") == 0) {
              %>
                 Term
              <%
              } else {
              %>
              <a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=Constants.NCI_METATHESAURUS%>&code=<%=syn_details_concept_code%>&type=<%=entry_type_syn%>&sortBy=name#SynonymsDetails">Term</a>
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
                <a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=Constants.NCI_METATHESAURUS%>&code=<%=syn_details_concept_code%>&type=<%=entry_type_syn%>&sortBy=source#SynonymsDetails">Source</a>
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
                <a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=Constants.NCI_METATHESAURUS%>&code=<%=syn_details_concept_code%>&type=<%=entry_type_syn%>&sortBy=type#SynonymsDetails">Type</a>
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
                <a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=Constants.NCI_METATHESAURUS%>&code=<%=syn_details_concept_code%>&type=<%=entry_type_syn%>&sortBy=code#SynonymsDetails">Code</a>
              <%
              }
              %>
          </th>
        </tr>
        <%
          Entity concept_syn = (Entity) request.getSession().getAttribute("concept");

          Vector synonyms = new DataUtils().getSynonyms(concept_syn);

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
            
	    boolean licenseAgreementAccepted = false;
	    String formal_name = NCImMetadataUtils.getSABFormalName(term_source);
	    boolean isLicensed = DataUtils.checkIsLicensed(term_source);
	    String cs_name = Constants.CODING_SCHEME_NAME;
	    String view_in_source_hierarchy_label = "View In Source Hierarchy";
	    if (term_source != null) {
	        view_in_source_hierarchy_label = "View In " + term_source + " Hierarchy";
	    }
	    
	    if (term_source != null && isLicensed ) {
	        licenseAgreementAccepted = licenseBean.licenseAgreementAccepted(formal_name);
	    }
	
	    boolean source_hierarchy_available = false;
	    if (available_hierarchies != null && available_hierarchies.indexOf("|" + term_source + "|") != -1) {
	         source_hierarchy_available = true;
	    }
	    
	    if (!isLicensed) {
	        licenseAgreementAccepted = true;
	    }
	    
      
        %>
            <tr class="<%=rowColor%>">
              <td class="dataCellTextwrap" ><%=DataUtils.encodeTerm(term_name)%></td>
              <td class="dataCellText" ><%=term_source%></td>
              <td class="dataCellText" ><%=term_type%></td>

<%
               // source code 
		if (term_browser_formalname == null) {
%>
			  <td class="dataCellText" >
			      <%=term_source_code%>
			  </td>
<%
		} else {
%>		      
	                  <td class="dataCellText" width=125>
			  <a href="#" onclick="javascript:window.open('<%= request.getContextPath() %>/redirect?action=details&dictionary=<%=formal_name%>&code=<%=term_source_code%>&sab=<%=term_source%>',
			  '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
			      <%=term_source_code%>
			  </a>
		                <%	  
				if (source_hierarchy_available) {
		                %>		          
		                          &nbsp;
					  <a class="icon_blue" href="#" onclick="javascript:window.open('<%= request.getContextPath() %>/redirect?action=tree&dictionary=<%=cs_name%>&code=<%=id%>&sab=<%=term_source%>&type=hierarchy',
					  '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
					      <img src="<%=basePath%>/images/visualize.gif" width="12px" height="12px" title="<%=view_in_source_hierarchy_label%>" alt="<%=view_in_source_hierarchy_label%>" border="0"/>
					  </a>
                                <%
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
