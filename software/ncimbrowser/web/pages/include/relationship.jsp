<%
  String entry_type_rel = type;
  if (type.compareTo("relationship") == 0 || type.compareTo("all") == 0)
  {
    Entity concept_curr = (Entity) request.getSession().getAttribute("concept");
    String scheme_curr = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("dictionary"));
    String version_curr = null;
    String code_curr = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("code"));
    String defaultSortOption = "source";

    Vector sort_option = new Vector();
    int num_tables = 6;
    String sort0 = null;
    String sort1 = null;
    String sort2 = null;
    String sort3 = null;
    String sort4 = null;
    String sort5 = null;

    for (int k=0; k<num_tables; k++) {
        sort_option.add(defaultSortOption);
    }
    for (int k=0; k<num_tables; k++) {
        String option = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("sort" + k));
        if (option != null) {
            sort_option.set(k, option);
        }
    }
    sort0 = (String) sort_option.elementAt(0);
    sort1 = (String) sort_option.elementAt(1);
    sort2 = (String) sort_option.elementAt(2);
    sort3 = (String) sort_option.elementAt(3);
    sort4 = (String) sort_option.elementAt(4);
    sort5 = (String) sort_option.elementAt(5);


    if (sort0 == null) sort0 = defaultSortOption;
    if (sort1 == null) sort1 = defaultSortOption;
    if (sort2 == null) sort2 = defaultSortOption;
    if (sort3 == null) sort3 = defaultSortOption;
    if (sort4 == null) sort4 = defaultSortOption;
    if (sort5 == null) sort5 = defaultSortOption;

    DataUtils util = new DataUtils();

    HashMap hmap_rel = null;//(HashMap) request.getSession().getAttribute("AssociationTargetHashMap");
    if (hmap_rel == null) {
      //hmap_rel = util.getAssociationTargetHashMap(scheme_curr, version_curr, code_curr, sort_option);
      hmap_rel = util.getAssociationTargetHashMap(code_curr, sort_option);
      request.getSession().setAttribute("AssociationTargetHashMap", hmap_rel);
    }


    Vector superconcept_vec = (Vector) hmap_rel.get("Parent");
    Vector subconcept_vec = (Vector) hmap_rel.get("Child");
    Vector broader_vec = (Vector) hmap_rel.get("Broader");
    Vector narrower_vec = (Vector) hmap_rel.get("Narrower");
    Vector sibling_vec = (Vector) hmap_rel.get("Sibling");
    //Vector sibling_vec = util.getSiblings(code_curr);

    Vector other_vec = (Vector) hmap_rel.get("Other");

    String label = "";
    String rel_value = "";

 %>
<table border="0" width="708px">
  <tr>
    <td class="textsubtitle-blue" align="left">
      Relationships with other NCI Metathesaurus Concepts:<a name="SynonymsDetails"></a>
    </td>
  </tr>
</table>
<%
  Object incomplete_obj = hmap_rel.get(DataUtils.INCOMPLETE);
  String incomplete = "[Not Set]";
  if (incomplete_obj != null) {
      incomplete = (String) incomplete_obj;
      if (incomplete.compareToIgnoreCase("true") == 0) {
 %>
  <p class="textbodyred">(WARNING: The relationship list shown below is incomplete due to size limits.)</p>
<%
      }
  }
%>
  <table border="0" width="690px" bgcolor="#ffffff" cellpadding="5" cellspacing="0">
    <tr>
      <td class="subtab" align="center">
        <b>
          <a href="#Parent">Parents</a> |
          <a href="#Child">Children</a> |
          <a href="#Broader">Broader</a> |
          <a href="#Narrower">Narrower</a> |
          <a href="#Sibling">Siblings</a> |
          <a href="#Other">Other</a>
        </b>
      </td>
    </tr>
  </table>
  <p>
<%
      label = "Parent Concepts:";
      rel_value = "Parent";
      if (superconcept_vec.size() <= 0)
      {
 %>
        <span class="textsubtitle-blue-small"><%=label%></span><a name="Parent"></a> <i>(none)</i>
<%
      } else {
 %>
        <span class="textsubtitle-blue-small"><%=label%></span><a name="Parent"></a>
  <table class="dataTable">
    <th class="dataTableHeader" scope="col" align="left">Relationship
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rel_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Definitions" title="Relationship Definitions" border="0">
        </a>
    </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort0.compareTo("rela") == 0) {
              %>
                 Rel. Attribute
              <%
              } else {
                  sort0 = "rela";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Parent">
                   Rel. Attribute
                </a>
              <%
              }
              %>
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rela_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Attr. Definitions" title="Relationship Attr. Definitions" border="0">
        </a>
          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort0.compareTo("name") == 0) {
              %>
                 Name
              <%
              } else {
                  sort0 = "name";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Parent">
                   Name
                </a>
              <%
              }
              %>
          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort0.compareTo("source") == 0) {
              %>
                 Rel. Source
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
        </a>
              <%
              } else {
                  sort0 = "source";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Parent">
                   Rel. Source
                </a>

        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
        </a>

              <%
              }
              %>
          </th>
    <%
      int n2 = 0;
      for (int i=0; i<superconcept_vec.size(); i++) {
        String s = (String) superconcept_vec.get(i);
        Vector ret_vec = DataUtils.parseData(s, "|");
        String relationship_name = (String) ret_vec.elementAt(0);
        String rel_display_value = relationship_name;
        if (DataUtils.containsAllUpperCaseChars(relationship_name)) {
            rel_display_value = "";
        }

        String target_concept_name = (String) ret_vec.elementAt(1);
        String target_concept_code = (String) ret_vec.elementAt(2);
        String rel_sab = (String) ret_vec.elementAt(3);

        if (n2 % 2 == 0) {
    %>
        <tr class="dataRowDark">
    <%
    } else {
    %>
        <tr class="dataRowLight">
    <%
    }
    n2++;
    %>
          <td width=100><%=rel_value%></td>
          <td width=180><%=rel_display_value%></td>
          <td>
            <a href="<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=<%=target_concept_code%>">
              <%=target_concept_name%>
            </a>
          </td>
          <td width=85><%=rel_sab%></td>
        </tr>
    <%
    }
    %>
   </table>
    <%
        sort0 = (String) sort_option.elementAt(0);
      }
    %>
  </p>

  <p>
    <%
      label = "Child Concepts:";
      rel_value = "Child";
      if (subconcept_vec.size() <= 0)
      {
    %>
        <span class="textsubtitle-blue-small"><%=label%></span><a name="Child"></a> <i>(none)</i>
    <%
      } else {
    %>
        <span class="textsubtitle-blue-small"><%=label%></span><a name="Child"></a>
  <table class="dataTable">
    <th class="dataTableHeader" scope="col" align="left">Relationship
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rel_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Definitions" title="Relationship Definitions" border="0">
        </a>
    </th>
    <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort1.compareTo("rela") == 0) {
              %>
                 Rel. Attribute
              <%
              } else {
                  sort1 = "rela";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Child">
                   Rel. Attribute
                </a>
              <%
              }
              %>
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rela_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Attr. Definitions" title="Relationship Attr. Definitions" border="0">
        </a>

          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort1.compareTo("name") == 0) {
              %>
                 Name
              <%
              } else {
                  sort1 = "name";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Child">
                   Name
                </a>
              <%
              }
              %>
          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort1.compareTo("source") == 0) {
              %>
                 Rel. Source
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
        </a>

              <%
              } else {
                  sort1 = "source";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Child">
                   Rel. Source
                </a>
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
        </a>

              <%
              }
              %>
          </th>
    <%
      int n2 = 0;
      for (int i=0; i<subconcept_vec.size(); i++) {
        String s = (String) subconcept_vec.get(i);
        Vector ret_vec = DataUtils.parseData(s, "|");
        String relationship_name = (String) ret_vec.elementAt(0);
        String rel_display_value = relationship_name;
        if (DataUtils.containsAllUpperCaseChars(relationship_name)) {
            rel_display_value = "";
        }
        String target_concept_name = (String) ret_vec.elementAt(1);
        String target_concept_code = (String) ret_vec.elementAt(2);
        String rel_sab = (String) ret_vec.elementAt(3);

        if (n2 % 2 == 0) {
    %>
        <tr class="dataRowDark">
    <%
    } else {
    %>
        <tr class="dataRowLight">
    <%
    }
    n2++;
    %>
          <td width=100><%=rel_value%></td>
          <td width=180><%=rel_display_value%></td>
          <td>
            <a href="<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=<%=target_concept_code%>">
              <%=target_concept_name%>
            </a>
          </td>
          <td width=85><%=rel_sab%></td>
        </tr>
    <%
    }
    %>
   </table>
    <%
        sort1 = (String) sort_option.elementAt(1);
      }
    %>
  </p>

  <p>
    <%
      label = "Broader Concepts:";
      rel_value = "Broader";
      if (broader_vec.size() <= 0)
      {
    %>
        <span class="textsubtitle-blue-small"><%=label%></span><a name="Broader"></a> <i>(none)</i>
    <%
      } else {
    %>
        <span class="textsubtitle-blue-small"><%=label%></span><a name="Broader"></a>
  <table class="dataTable">
    <th class="dataTableHeader" scope="col" align="left">Relationship
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rel_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Definitions" title="Relationship Definitions" border="0">
        </a>
    </th>
    <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort2.compareTo("rela") == 0) {
              %>
                 Rel. Attribute
              <%
              } else {
                  sort2 = "rela";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Broader">
                   Rel. Attribute
                </a>
              <%
              }
              %>
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rela_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Attr. Definitions" title="Relationship Attr. Definitions" border="0">
        </a>

          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort2.compareTo("name") == 0) {
              %>
                 Name
              <%
              } else {
                  sort2 = "name";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Broader">
                   Name
                </a>
              <%
              }
              %>
          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort2.compareTo("source") == 0) {
              %>
                 Rel. Source
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
        </a>

              <%
              } else {
                  sort2 = "source";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Broader">
                   Rel. Source
                </a>
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
        </a>

              <%
              }
              %>
          </th>
          <%
      int n2 = 0;
      for (int i=0; i<broader_vec.size(); i++) {
        String s = (String) broader_vec.get(i);
        Vector ret_vec = DataUtils.parseData(s, "|");
        String relationship_name = (String) ret_vec.elementAt(0);
        String rel_display_value = relationship_name;
        if (DataUtils.containsAllUpperCaseChars(relationship_name)) {
            rel_display_value = "";
        }
        String target_concept_name = (String) ret_vec.elementAt(1);
        String target_concept_code = (String) ret_vec.elementAt(2);
        String rel_sab = (String) ret_vec.elementAt(3);

        if (n2 % 2 == 0) {
    %>
        <tr class="dataRowDark">
    <%
    } else {
    %>
        <tr class="dataRowLight">
    <%
    }
    n2++;
    %>
          <td width=100><%=rel_value%></td>
          <td width=180><%=rel_display_value%></td>
          <td>
            <a href="<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=<%=target_concept_code%>">
              <%=target_concept_name%>
            </a>
          </td>
          <td width=85><%=rel_sab%></td>
        </tr>
    <%
    }
    %>
   </table>
    <%
        sort2 = (String) sort_option.elementAt(2);
      }
    %>
  </p>

  <p>
    <%
      label = "Narrower Concepts:";
      rel_value = "Narrower";
      if (narrower_vec.size() <= 0)
      {
    %>
        <span class="textsubtitle-blue-small"><%=label%></span><a name="Narrower"></a> <i>(none)</i>
    <%
      } else {
    %>
        <span class="textsubtitle-blue-small"><%=label%></span><a name="Narrower"></a>
  <table class="dataTable">
    <th class="dataTableHeader" scope="col" align="left">Relationship
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rel_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Definitions" title="Relationship Definitions" border="0">
        </a>
    </th>
    <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort3.compareTo("rela") == 0) {
              %>
                 Rel. Attribute
              <%
              } else {
                  sort3 = "rela";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Narrower">
                   Rel. Attribute
                </a>
              <%
              }
              %>
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rela_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Attr. Definitions" title="Relationship Attr. Definitions" border="0">
        </a>
          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort3.compareTo("name") == 0) {
              %>
                 Name
              <%
              } else {
                  sort3 = "name";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>%#Narrower">
                   Name
                </a>
              <%
              }
              %>
          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort3.compareTo("source") == 0) {
              %>
                 Rel. Source
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
        </a>

              <%
              } else {
                  sort3 = "source";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Narrower">
                   Rel. Source
                </a>
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
        </a>

              <%
              }
              %>
          </th>
          <%
      int n2 = 0;
      for (int i=0; i<narrower_vec.size(); i++) {
        String s = (String) narrower_vec.get(i);
        Vector ret_vec = DataUtils.parseData(s, "|");
        String relationship_name = (String) ret_vec.elementAt(0);
        String rel_display_value = relationship_name;
        if (DataUtils.containsAllUpperCaseChars(relationship_name)) {
            rel_display_value = "";
        }
        String target_concept_name = (String) ret_vec.elementAt(1);
        String target_concept_code = (String) ret_vec.elementAt(2);
        String rel_sab = (String) ret_vec.elementAt(3);

        if (n2 % 2 == 0) {
    %>
        <tr class="dataRowDark">
    <%
    } else {
    %>
        <tr class="dataRowLight">
    <%
    }
    n2++;
    %>
          <td width=100><%=rel_value%></td>
          <td width=180><%=rel_display_value%></td>
          <td>
            <a href="<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=<%=target_concept_code%>">
              <%=target_concept_name%>
            </a>
          </td>
          <td width=85><%=rel_sab%></td>
        </tr>
    <%
    }
    %>
   </table>
    <%
        sort3 = (String) sort_option.elementAt(3);
      }
    %>
  </p>

  <p>
    <%
      label = "Sibling Concepts:";
      rel_value = "Sibling";
      if (sibling_vec.size() <= 0)
      {
    %>
        <span class="textsubtitle-blue-small"><%=label%></span><a name="Sibling"></a> <i>(none)</i>
    <%
      } else {
    %>
        <span class="textsubtitle-blue-small"><%=label%></span><a name="Sibling"></a>
  <table class="dataTable">
    <th class="dataTableHeader" scope="col" align="left">Relationship
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rel_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Definitions" title="Relationship Definitions" border="0">
        </a>
    </th>
    <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort4.compareTo("rela") == 0) {
              %>
                 Rel. Attribute
              <%
              } else {
                  sort4 = "rela";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Sibling">
                   Rel. Attribute
                </a>
              <%
              }
              %>
         <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rela_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Attr. Definitions" title="Relationship Attr. Definitions" border="0">
        </a>
          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort4.compareTo("name") == 0) {
              %>
                 Name
              <%
              } else {
                  sort4 = "name";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Sibling">
                   Name
                </a>
              <%
              }
              %>
          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort4.compareTo("source") == 0) {
              %>
                 Rel. Source
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
        </a>

              <%
              } else {
                  sort4 = "source";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Sibling">
                   Rel. Source
                </a>
         <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
        </a>

              <%
              }
              %>
          </th>
          <%
      int n2 = 0;
      for (int i=0; i<sibling_vec.size(); i++) {
        String s = (String) sibling_vec.get(i);
        Vector ret_vec = DataUtils.parseData(s, "|");
        String relationship_name = (String) ret_vec.elementAt(0);
        String rel_display_value = relationship_name;
        if (DataUtils.containsAllUpperCaseChars(relationship_name)) {
            rel_display_value = "";
        }
        String target_concept_name = (String) ret_vec.elementAt(1);
        String target_concept_code = (String) ret_vec.elementAt(2);
        String rel_sab = (String) ret_vec.elementAt(3);

        if (n2 % 2 == 0) {
    %>
        <tr class="dataRowDark">
    <%
    } else {
    %>
        <tr class="dataRowLight">
    <%
    }
    n2++;
    %>
          <td width=100><%=rel_value%></td>
          <td width=180><%=rel_display_value%></td>
          <td>
            <a href="<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=<%=target_concept_code%>">
              <%=target_concept_name%>
            </a>
          </td>
          <td width=85><%=rel_sab%></td>
        </tr>
    <%
    }
    %>
   </table>
    <%
        sort4 = (String) sort_option.elementAt(4);
      }
    %>
  </p>

  <p>
    <%
      label = "Other Relationships:";
      rel_value = "Other";
      if (other_vec.size() <= 0)
      {
    %>
        <span class="textsubtitle-blue-small"><%=label%></span><a name="Other"></a> <i>(none)</i>
    <%
      } else {
    %>
        <span class="textsubtitle-blue-small"><%=label%></span><a name="Other"></a>
  <table class="dataTable">
    <th class="dataTableHeader" scope="col" align="left">Relationship
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rel_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Definitions" title="Relationship Definitions" border="0">
        </a>
    </th>
    </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort5.compareTo("rela") == 0) {
              %>
                 Rel. Attribute
              <%
              } else {
                  sort5 = "rela";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Other">
                   Rel. Attribute
                </a>
              <%
              }
              %>
         <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rela_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Attr. Definitions" title="Relationship Attr. Definitions" border="0">
        </a>
          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort5.compareTo("name") == 0) {
              %>
                 Name
              <%
              } else {
                  sort5 = "name";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Other">
                   Name
                </a>
              <%
              }
              %>
          </th>
          <th class="dataTableHeader" scope="col" align="left">
              <%
              if (sort5.compareTo("source") == 0) {
              %>
                 Rel. Source
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
        </a>

              <%
              } else {
                  sort5 = "source";
              %>
                <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=<%=entry_type_rel%>&sort0=<%=sort0%>&sort1=<%=sort1%>&sort2=<%=sort2%>&sort3=<%=sort3%>&sort4=<%=sort4%>&sort5=<%=sort5%>#Other">
                   Rel. Source
                </a>
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
        </a>

              <%
              }
              %>
          </th>
          <%
      int n2 = 0;
      for (int i=0; i<other_vec.size(); i++) {
        String s = (String) other_vec.get(i);
        Vector ret_vec = DataUtils.parseData(s, "|");
        String relationship_name = (String) ret_vec.elementAt(0);
        String rel_display_value = relationship_name;
        if (DataUtils.containsAllUpperCaseChars(relationship_name)) {
            rel_display_value = "";
        }
        String target_concept_name = (String) ret_vec.elementAt(1);
        String target_concept_code = (String) ret_vec.elementAt(2);
        String rel_sab = (String) ret_vec.elementAt(3);

        if (n2 % 2 == 0) {
    %>
        <tr class="dataRowDark">
    <%
    } else {
    %>
        <tr class="dataRowLight">
    <%
    }
    n2++;
    %>
          <td width=100><%=rel_value%></td>
          <td width=180><%=rel_display_value%></td>
          <td>
            <a href="<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=<%=target_concept_code%>">
              <%=target_concept_name%>
            </a>
          </td>
          <td width=85><%=rel_sab%></td>
        </tr>
    <%
    }
    %>
   </table>
    <%
        sort5 = (String) sort_option.elementAt(5);
      }
    %>
  </p>
  <p>
    <%
      Vector sab_vec = DataUtils.getConceptSources(scheme_curr, version_curr, code_curr);
      ArrayList self_referential_relationships = SourceTreeUtils.getIntraCUIRelationships(scheme_curr, version_curr, code_curr, sab_vec, true);
      label = "Self-Referential Relationships:";
      if (self_referential_relationships != null && self_referential_relationships.size() > 0)
      {
    %>
        <span class="textsubtitle-blue-small"><%=label%></span><a name="Self"></a>
    <table class="dataTable">
      <th class="dataTableHeader" scope="col" align="left">Relationship
      </th>
      <th class="dataTableHeader" scope="col" align="left">Source AUI
      </th>
      <th class="dataTableHeader" scope="col" align="left">Source Term
      </th>
      <th class="dataTableHeader" scope="col" align="left">Target AUI
      </th>
      <th class="dataTableHeader" scope="col" align="left">Target Term
      </th>
      <th class="dataTableHeader" scope="col" align="left">Rel. Source
        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
    '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
    <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source List" title="Source List" border="0">
        </a>
      </th>
      <%
        int m = 0;
        for (int i=0; i<self_referential_relationships.size(); i++) {
    String s = (String) self_referential_relationships.get(i);
    Vector ret_vec = DataUtils.parseData(s, "$");
    String source_aui = (String) ret_vec.elementAt(0);
    String source_term = (String) ret_vec.elementAt(1);
    String self_rela = (String) ret_vec.elementAt(2);
    String target_aui = (String) ret_vec.elementAt(3);
    String target_term = (String) ret_vec.elementAt(4);
    String rel_source = (String) ret_vec.elementAt(5);

    if (m % 2 == 0) {
        %>
      <tr class="dataRowDark">
        <%
          } else {
          %>
        <tr class="dataRowLight">
          <%
          }
          m++;
           %>
            <td width=80><%=self_rela%></td>
      <td width=100><%=source_aui%></td>
      <td width=200><%=source_term%></td>
      <td width=100><%=target_aui%></td>
      <td width=200><%=target_term%></td>
      <td width=100><%=rel_source%></td>
    </tr>
       <%
              }
             %>
         </table>
      <%
      }
      %>
   </p>
  <%-- DEBUG: incomplete_obj: <%=incomplete_obj%> incomplete: <%=incomplete%><br/> --%>
<%
}
%>