<%
  String entry_type_src = type;
  if (type.compareTo("sources") == 0 || type.compareTo("all") == 0)
  {
    Entity concept_src = (Entity) request.getSession().getAttribute("concept");
%>
    <!-- Page content -->
<%

  Entity concept_neighborhood = null;
  if (concept_neighborhood == null) {
    concept_neighborhood = (Entity) request.getSession().getAttribute("concept");
    code = concept_neighborhood.getEntityCode();

  } else {

      concept_neighborhood = DataUtils.getConceptByCode(Constants.CODING_SCHEME_NAME, null, null, code);
      request.getSession().setAttribute("concept", concept_neighborhood);
      request.getSession().removeAttribute("neighborhood_atoms");
      request.getSession().removeAttribute("selectedConceptSource");
  }

  DataUtils dataUtils = new DataUtils();
  request.getSession().removeAttribute("neighborhood_synonyms");
  String neighborhood_sab = null;
  Vector source_vec = dataUtils.getSources(Constants.CODING_SCHEME_NAME, null, null, code);

  if (source_vec != null && source_vec.size() == 1) {
      neighborhood_sab = (String) source_vec.elementAt(0);
  } else {
      neighborhood_sab = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("sab"));
  }

  if (neighborhood_sab == null) {
      if (source_vec.contains("NCI")) {
          neighborhood_sab = "NCI";
      } else {
          neighborhood_sab = (String) source_vec.elementAt(0);
      }
      request.getSession().setAttribute("selectedConceptSource", neighborhood_sab);
  }

  if (neighborhood_sab == null) {
      neighborhood_sab = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("sab"));
  }


  Vector src_vec = new Vector();
  for (int k=0; k<source_vec.size(); k++) {
      String src = (String) source_vec.elementAt(k);
      //if (src.compareTo(neighborhood_sab) != 0) {
          src_vec.add(src);
      //}
  }

  String concept_neighborhood_name = concept_neighborhood.getEntityDescription().getContent();
  Vector neighborhood_synonyms = dataUtils.getSynonyms(concept_neighborhood, neighborhood_sab);
  //Vector neighborhood_atoms = dataUtils.getNeighborhoodSynonyms(Constants.CODING_SCHEME_NAME, null, concept_neighborhood.getEntityCode(), neighborhood_sab);
  Vector neighborhood_atoms = dataUtils.getNeighborhoodSynonyms(concept_neighborhood.getEntityCode(), neighborhood_sab);

  String sort_by = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("sortBy"));
  if (sort_by == null) {
      sort_by = "name";
  }
  String sort_by2 = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("sortBy2"));
  if (sort_by2 == null) {
      sort_by2 = "rel_type";
  }
  neighborhood_synonyms = new DataUtils().sortSynonyms(neighborhood_synonyms, sort_by);
  neighborhood_atoms = new DataUtils().sortSynonymData(neighborhood_atoms, sort_by2);
%>
    <p class="textsubtitle-blue">'<%=concept_neighborhood_name%>'&nbsp;By Source:&nbsp;<%=neighborhood_sab%></p>
    <div>
      <table class="dataTable" border="0">
        <%
        if (src_vec.size() > 0) {
        String nci_src_name = "NCI";
        %>
        <tr>
           <td class="textbody">
           Select source: &nbsp;
        <%
          for (int n=0; n<src_vec.size(); n++)
          {
              String s = (String) src_vec.elementAt(n);
              if (s.compareTo(nci_src_name) == 0) {
                  if (neighborhood_sab.compareTo(nci_src_name) == 0) {
                  %>
                     <b><%=nci_src_name%>&nbsp;</b>
                  <%
                  } else {
              %>
    <a href="<%= request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_src%>&dictionary=NCI%20MetaThesaurus&code=<%=code%>&sab=<%=s%>">
       <%=s%>
    </a>&nbsp;
    <%
      }
        }
          }
          for (int n=0; n<src_vec.size(); n++)
          {
              String s = (String) src_vec.elementAt(n);
              if (s.compareTo(nci_src_name) != 0) {
                   if (neighborhood_sab.compareTo(s) == 0) {
                  %>
                     <b><%=s%>&nbsp;</b>
                  <%
                  } else {

              %>
    <a href="<%= request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_src%>&dictionary=NCI%20MetaThesaurus&code=<%=code%>&sab=<%=s%>">
       <%=s%>
    </a>&nbsp;
    <%
      }
        }
          }
        }
        %>
           </td>
        </tr>
        <tr>
           <td>
<br/>
     <span class="textsubtitle-blue-small">Synonyms</span><a name="Synonyms"></a>
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
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_src%>&sortBy=name&&sortBy2=<%=sort_by2%>&&sab=<%=neighborhood_sab%>#Synonyms">Term</a>
              <%
              }
              %>
          </th>
          <th class="dataTableHeader" scope="col" align="left" valign="top" nowrap>
            Source
            <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
              '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
              <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
            </a>
          </th>
          <th class="dataTableHeader" scope="col" align="left" valign="top" nowrap>
              <%
              if (sort_by != null && sort_by.compareTo("type") == 0) {
              %>
                 Type
              <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/term_type_help_info.jsf',
                '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
                <img src="<%= request.getContextPath() %>/images/help.gif" alt="Term Type Definition" title="Term Type Definition" border="0">
              </a>
              <%
              } else if ((sort_by == null) || sort_by != null  && sort_by.compareTo("type") != 0) {
              %>
              <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_src%>&sortBy=type&&sortBy2=<%=sort_by2%>&&sab=<%=neighborhood_sab%>#Synonyms">Type</a>
              <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/term_type_help_info.jsf',
                '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
                <img src="<%= request.getContextPath() %>/images/help.gif" alt="Term Type Definition" title="Term Type Definition" border="0">
              </a>
              <%
              }
              %>
          </th>
          <th class="dataTableHeader" scope="col" align="left" valign="top" nowrap>
              <%
              if (sort_by != null && sort_by.compareTo("code") == 0) {
              %>
                 Code
              <%
              } else if ((sort_by == null) || sort_by != null && sort_by.compareTo("code") != 0) {
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_src%>&sortBy=code&&sortBy2=<%=sort_by2%>&&sab=<%=neighborhood_sab%>#Synonyms">Code</a>
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
              <td class="dataCellText" width=60><%=term_source%></td>
              <td class="dataCellText" width=50><%=term_type%></td>
              <td class="dataCellText" width=50><%=term_source_code%></td>
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

<br/>
<span class="textsubtitle-blue-small">Relationships</span><a name="Relationships"></a>
<%
if (neighborhood_atoms.size() == 0) {
%>
<br/>
<i>(none)</i>
<%
} else {
%>
      <table class="dataTable" border="0">
        <tr>
          <th class="dataTableHeader" scope="col" align="left" valign="top" nowrap>
              <%
              if (sort_by2 == null || sort_by2.compareTo("rel_type") == 0) {
              %>
            Relationship
            <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rel_help_info.jsf',
              '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
              <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Definitions" title="Relationship Definitions" border="0">
            </a>
              <%
              } else {
              %>
            <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_src%>&sortBy2=rel_type&&sortBy=<%=sort_by%>&&sab=<%=neighborhood_sab%>#Relationships">Relationship</a>
            <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rel_help_info.jsf',
              '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
              <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Definitions" title="Relationship Definitions" border="0">
            </a>
              <%
              }
              %>
          </th>
          <th class="dataTableHeader" scope="col" align="left" valign="top" nowrap>
              <%
              if (sort_by2 == null || sort_by2.compareTo("rel") == 0) {
              %>
              Rel. Attribute
              <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rela_help_info.jsf',
              '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
              <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Attr. Definitions" title="Relationship Attr. Definitions" border="0">
              </a>
              <%
              } else {
              %>
              <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_src%>&sortBy2=rel&&sortBy=<%=sort_by%>&&sab=<%=neighborhood_sab%>#Relationships">Rel. Attribute</a>
              <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rela_help_info.jsf',
                '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
                <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Attr. Definitions" title="Relationship Attr. Definitions" border="0">
              </a>
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
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_src%>&sortBy2=cui&&sortBy=<%=sort_by%>&&sab=<%=neighborhood_sab%>#Relationships">CUI</a>
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
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_src%>&sortBy2=name&&sortBy=<%=sort_by%>&&sab=<%=neighborhood_sab%>#Relationships">Term</a>
              <%
              }
              %>
          </th>
          <th class="dataTableHeader" scope="col" align="left" valign="top">
            Source
            <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
              '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
              <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
            </a>
          </th>
          <th class="dataTableHeader" scope="col" align="left" valign="top">
              <%
              if (sort_by2 != null && sort_by2.compareTo("type") == 0) {
              %>
              Type
              <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/term_type_help_info.jsf',
                '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
                <img src="<%= request.getContextPath() %>/images/help.gif" alt="Term Type Definition" title="Term Type Definition" border="0">
              </a>
              <%
              } else if ((sort_by2 == null) || (sort_by2 != null  && sort_by.compareTo("type") != 0)) {
              %>
              <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_src%>&sortBy2=type&&sortBy=<%=sort_by%>&&sab=<%=neighborhood_sab%>#Relationships">Type</a>
              <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/term_type_help_info.jsf',
                '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
                <img src="<%= request.getContextPath() %>/images/help.gif" alt="Term Type Definition" title="Term Type Definition" border="0">
              </a>
              <%
              }
              %>
          </th>
          <th class="dataTableHeader" scope="col" align="left" valign="top">
              <%
              if (sort_by2 != null && sort_by2.compareTo("code") == 0) {
              %>
                 Code
              <%
              } else if ((sort_by2 == null) || sort_by2 != null && sort_by2.compareTo("code") != 0) {
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_src%>&sortBy2=code&&sortBy=<%=sort_by%>&&sab=<%=neighborhood_sab%>#Relationships">Code</a>
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
        String rel_display_value = rel;

        if (DataUtils.containsAllUpperCaseChars(rel)) {
            rel_display_value = "";
        }

        String rel_type = (String) synonym_data.elementAt(6);
        String rowColor = (i%2 == 0) ? "dataRowDark" : "dataRowLight";
    %>
        <tr class="<%=rowColor%>">
          <td class="dataCellText" width=80><%=rel_type%></td>
          <td class="dataCellText" width=230><%=rel_display_value%></td>
          <td class="dataCellText" width=66>
        <a href="<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=Constants.CODING_SCHEME_NAME%>&code=<%=cui%>">
          <%=cui%>
        </a>
          </td>

          <td class="dataCellText" width=130><a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=Constants.CODING_SCHEME_NAME%>&code=<%=cui%>"><%=term_name%></a></td>
          <td class="dataCellText" width=60><%=term_source%></td>
          <td class="dataCellText" width=50><%=term_type%></td>
          <%
          if (term_source_code.compareTo(Constants.NA) != 0) {
          %>
          <td class="dataCellText" width=50><a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_src%>&code=<%=cui%>&&sab=<%=term_source%>&&sourcecode=<%=term_source_code%>&&checkmultiplicity=true"><%=term_source_code%></a></td>
          <%
          } else {
          %>
              <td class="dataCellText"><%=term_source_code%></td>
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
</p>
          </td>
        </tr>
      </table>
      </div>
    <%
  }
%>