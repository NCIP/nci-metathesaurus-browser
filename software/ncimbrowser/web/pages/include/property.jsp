<%@ page import="gov.nih.nci.evs.browser.utils.SortUtils" %>

<%
  List displayItemList = null;

  try {
    displayItemList = NCImBrowserProperties.getInstance().getDisplayItemList();
  } catch (Exception ex) {
    // Do nothing
  }


  if ((type.compareTo("properties") == 0 || type.compareTo("all") == 0) &&
    displayItemList != null) {

    Vector propertytypes = new Vector();
    propertytypes.add("PRESENTATION");
    propertytypes.add("DEFINITION");
    propertytypes.add("GENERIC");
    propertytypes.add("COMMENT");

    Vector additionalproperties = new Vector();
    additionalproperties.add("CONCEPT_NAME");
    additionalproperties.add("primitive");
    Entity curr_concept = (Entity) request.getSession().getAttribute("concept");
    String curr_concept_code = curr_concept.getEntityCode();

    String retired_cui = (String) request.getSession().getAttribute("retired_cui");
    if (retired_cui != null) {
        request.getSession().removeAttribute("retired_cui");
    }

    Boolean bool_obj = curr_concept.isIsActive();
    String isActive = bool_obj.toString();

    // to be modified
    String concept_status = curr_concept.getStatus();

    if (concept_status != null) {
      concept_status = concept_status.replaceAll("_", " ");
    }

    HashSet hset = new HashSet();
    HashMap hmap = new HashMap();
    Vector propertyvalues = null;

    for (int i=0; i<propertytypes.size(); i++) {
      String propertytype = (String) propertytypes.elementAt(i);
      Vector propertynames = DataUtils.getPropertyNamesByType(curr_concept, propertytype);

      for (int j=0; j<propertynames.size(); j++) {
        String propertyname = (String) propertynames.elementAt(j);
        if (!hset.contains(propertyname)) {
          hset.add(propertyname);
          propertyvalues = DataUtils.getPropertyValues(curr_concept, propertytype, propertyname);

          if (propertyvalues != null) {
             if (propertyname.compareToIgnoreCase("definition") != 0) {
                hmap.put(propertyname, propertyvalues);
             }
             else {
                Vector new_propertyvalues = new Vector();
                for (int k=0; k<propertyvalues.size(); k++) {
                    String definition_str = (String) propertyvalues.elementAt(k);
                    Vector def_data = DataUtils.parseData(definition_str, "|");
        String def_description = (String) def_data.elementAt(0);
        String def_source = "";
        if (def_data.size() > 1) {
          def_source = (String) def_data.elementAt(1);
        }
        if (def_source.compareTo("NCI") == 0) {
        new_propertyvalues.add(def_description + "|" + "NCIt");
    }
            }
                for (int k=0; k<propertyvalues.size(); k++) {
                    String definition_str = (String) propertyvalues.elementAt(k);
        Vector def_data = DataUtils.parseData(definition_str, "|");
        String def_description = (String) def_data.elementAt(0);

        String def_source = "";
        if (def_data.size() > 1) {
           def_source = (String) def_data.elementAt(1);
        }
        if (def_source.compareTo("NCI") != 0) new_propertyvalues.add(definition_str);
                }
                hmap.put(propertyname, new_propertyvalues);
             }
          }
        }
      }
    }

    propertyvalues = new Vector();
    String concept_id = curr_concept.getEntityCode();

    propertyvalues.add(concept_id);
    hmap.put("Code", propertyvalues);

  // NCIt code
  Vector syns = DataUtils.getSynonyms(c, "NCI");
  if (syns != null && syns.size() > 0) {
     Vector nci_code_vec = new Vector();
     String syn_line = (String) syns.elementAt(0);
     Vector synonym_data = DataUtils.parseData(syn_line, "|");
     String term_name = (String) synonym_data.elementAt(0);
     String term_type = (String) synonym_data.elementAt(1);
     String term_source = (String) synonym_data.elementAt(2);
     String term_source_code = (String) synonym_data.elementAt(3);
     nci_code_vec.add(term_source_code);
     hmap.put("NCIt Code", nci_code_vec);
  }


    Vector displayed_properties = new Vector();
    Vector properties_to_display = new Vector();
    Vector properties_to_display_label = new Vector();
    Vector properties_to_display_url = new Vector();
    Vector properties_to_display_linktext = new Vector();

    for (int i=0; i<displayItemList.size(); i++) {
      DisplayItem displayItem = (DisplayItem) displayItemList.get(i);
      if (!displayItem.getIsExternalCode()) {
        properties_to_display.add(displayItem.getPropertyName());
        properties_to_display_label.add(displayItem.getItemLabel());
        properties_to_display_url.add(displayItem.getUrl());
        properties_to_display_linktext.add(displayItem.getHyperlinkText());
      } else if (displayItem.getPropertyName().compareTo("NCIt Code") == 0) {
        properties_to_display.add(displayItem.getPropertyName());
        properties_to_display_label.add(displayItem.getItemLabel());
        properties_to_display_url.add(displayItem.getUrl());
        properties_to_display_linktext.add(displayItem.getHyperlinkText());
      }
    }

    int num_definitions = 0;
    int num_alt_definitions = 0;
    Vector external_source_codes = new Vector();
    Vector external_source_codes_label = new Vector();
    Vector external_source_codes_url = new Vector();
    Vector external_source_codes_linktext = new Vector();
    for (int i=0; i<displayItemList.size(); i++) {
    DisplayItem displayItem = (DisplayItem) displayItemList.get(i);

    if (displayItem.getIsExternalCode()) {
      external_source_codes.add(displayItem.getPropertyName());
      external_source_codes_label.add(displayItem.getItemLabel());
      external_source_codes_url.add(displayItem.getUrl());
      external_source_codes_linktext.add(displayItem.getHyperlinkText());
    }
  }

%>
<table border="0" width="708px">
	<tr>
		<td class="textsubtitle-blue" align="left">Terms and Properties</td>
	</tr>
</table>
<%
if (retired_cui != null) {
%>
    <p class="textbodyred"><b>Note: The CUI&nbsp;<i class="textbodyred"><%=retired_cui%></i>
         you searched for is obsolete.</b>
    </p>
<%
}
if (!bool_obj.equals(Boolean.TRUE) ||
  (concept_status != null &&
    concept_status.compareToIgnoreCase("Retired Concept") == 0)) // non-active

{
%>
    <p class="textbody"><b>Concept Status:</b>&nbsp;<i class="textbodyred">Retired Concept</i></p>
<%
}
else if (concept_status != null && concept_status.compareToIgnoreCase("Retired Concept") != 0) {
%>
    <p class="textbody"><b>Concept Status:</b>&nbsp;<i class="textbody"><%=concept_status%></i></p>
<%
}
%>
<%

  for (int i=0; i<properties_to_display.size(); i++) {
    String propName = (String) properties_to_display.elementAt(i);
    String propName_label = (String) properties_to_display_label.elementAt(i);
    String propName_label2 = propName_label;
    String url = (String) properties_to_display_url.elementAt(i);
    String linktext = (String) properties_to_display_linktext.elementAt(i);
    String qualifier = "";
    if (url != null) {
      displayed_properties.add(propName);
      Vector value_vec = (Vector) hmap.get(propName);

      if (value_vec != null && value_vec.size() > 0) {
        String value = (String) value_vec.elementAt(0);
        String value_wo_qualifier = value;

        int n = value.indexOf("|");

        if (n != -1 && (propName_label.indexOf("Definition") != -1 || propName_label.indexOf("DEFINITION") != -1)) {
          value_wo_qualifier = value.substring(0, n);
          qualifier = value.substring(n+1, value.length());

          value = value_wo_qualifier;
          if (qualifier.compareTo("") != 0) {
            propName_label = qualifier + " " + propName_label2;
          }
        }

        String url_str = url + value;
%>
        <p>
          <b><%=propName_label%>:&nbsp;</b><%=value%>&nbsp;
          <a href="javascript:redirect_site('<%= url_str %>')">(<%=linktext%>)</a>
        </p>
<%
      }
    } else if (propName_label.indexOf("Synonym") == -1) {


      displayed_properties.add(propName);
      Vector value_vec = (Vector) hmap.get(propName);

      if (value_vec != null && value_vec.size() > 0) {
        int k = 0;
        for (int j=0; j<value_vec.size(); j++) {
          String value = (String) value_vec.elementAt(j);
          String value_wo_qualifier = value;
          int n = value.indexOf("|");

          if (n != -1 && (propName_label.indexOf("Definition") != -1 || propName_label.indexOf("DEFINITION") != -1)) {
              value_wo_qualifier = value.substring(0, n);
        qualifier = value.substring(n+1, value.length());

        value = value_wo_qualifier;
        if (qualifier.compareTo("") != 0) {
            propName_label = qualifier + " " + propName_label2;

            if (qualifier.indexOf("PDQ") != -1) {
                value = FormatUtils.reformatPDQDefinition(value);
            }
        }
          }

          if (k == 0) {
%>
            <p><b><%=propName_label%>:&nbsp;</b><%=value%></p>
<%
          } else {
%>
            <p><%=value%></p>
<%
          }

      }
    }
  }
}
%>
<p>
<b>Synonyms &amp; Abbreviations:</b>
<a href="<%=request.getContextPath() %>/pages/concept_details.jsf?dictionary=<%=scheme%>&code=<%=id%>&type=synonym" >(see Synonym Details)</a>
<table class="datatable">
<%
  HashSet presentation_hset = new HashSet();
  presentation_hset.add("textualPresentation");
  presentation_hset.add("Display_Name");
  presentation_hset.add("Preferred_Name");
  presentation_hset.add("FULL_SYN");
  presentation_hset.add("textualPresentation");
  presentation_hset.add("Synonym");
  presentation_hset.add("presentation");

  for (int i=0; i<properties_to_display.size(); i++) {
    String propName = (String) properties_to_display.elementAt(i);
    String propName_label = (String) properties_to_display_label.elementAt(i);

    //if (propName.compareTo("textualPresentation") == 0) {
    if (presentation_hset.contains(propName)) {
      displayed_properties.add(propName);
      Vector value_vec = (Vector) hmap.get(propName);
      value_vec = SortUtils.quickSort(value_vec);

      if (value_vec != null && value_vec.size() > 0) {
        HashSet hset2 = new HashSet();
        int row=0;
        for (int j=0; j<value_vec.size(); j++) {
          String value = (String) value_vec.elementAt(j);
          int n = value.indexOf("|");
          if (n != -1) value = value.substring(0, n);
          String valueLC = value.toLowerCase();
          if (hset2.contains(valueLC))
             continue;
          hset2.add(valueLC);
          if ((row++) % 2 == 0) {
            %>
              <tr class="dataRowDark">
            <%
          } else {
            %>
              <tr class="dataRowLight">
            <%
          }
            %>
                <td><%=value%></td>
              </tr>
            <%
        }
      }
    }
  }
%>
</table>
</p>
<p>

  <%

  boolean has_external_source_codes = false;
  int n = 0;
  for (int i=0; i<external_source_codes.size(); i++) {
    String propName = (String) external_source_codes.elementAt(i);
    String propName_label = (String) external_source_codes_label.elementAt(i);
    String prop_url = (String) external_source_codes_url.elementAt(i);
    String prop_linktext = (String) external_source_codes_linktext.elementAt(i);

    displayed_properties.add(propName);
    Vector value_vec = (Vector) hmap.get(propName);

    if (value_vec != null && value_vec.size() > 0) {
       has_external_source_codes = true;
       break;
    }
  }


  if (!has_external_source_codes) {
  %>
      <b>External Source Codes:&nbsp;</b> <i>(none)</i>
  <%
  } else {
  %>

  <b>External Source Codes:&nbsp;</b>
  <table class="datatable">
    <%

      n = 0;
      for (int i=0; i<external_source_codes.size(); i++) {
        String propName = (String) external_source_codes.elementAt(i);
        String propName_label = (String) external_source_codes_label.elementAt(i);
        String prop_url = (String) external_source_codes_url.elementAt(i);
        String prop_linktext = (String) external_source_codes_linktext.elementAt(i);

        displayed_properties.add(propName);
        Vector value_vec = (Vector) hmap.get(propName);

        if (value_vec != null && value_vec.size() > 0) {
          for (int j=0; j<value_vec.size(); j++) {
            String value = (String) value_vec.elementAt(j);
            if (n % 2 == 0) {
              %>
                <tr class="dataRowDark">
              <%
            } else {
              %>
                <tr class="dataRowLight">
              <%
            }
            n++;
            %>
              <td><i><%=propName_label%></i></td>
              <td>
                <i>
                  <%=value%>
                  <%
                    if (prop_url != null && prop_url.compareTo("null") != 0) {
                      String url_str = prop_url + value;
                      %>
                        <a href="javascript:redirect_site('<%= url_str %>')">(<%=prop_linktext%>)</a>
                      <%
                    }
                  %>
                </i>
              </td>
            </tr>
          <%
          }
       }
    }
    %>
    </table>

    <%
    }
    %>

</p>

<%
      hmap = DataUtils.getPropertyValueHashMap(curr_concept);
      Set keyset = hmap.keySet();
      Iterator iterator = keyset.iterator();
      Vector key_vec = new Vector();
      String prop_name = null;
      while (iterator.hasNext()) {
         prop_name = (String) iterator.next();
         key_vec.add(prop_name);
      }
      key_vec = SortUtils.quickSort(key_vec);

      n = 0;
      displayed_properties.add("textualPresentation");

      boolean show_other_properties = false;
      for (int key_lcv=0; key_lcv<key_vec.size(); key_lcv++) {
         prop_name = (String) key_vec.elementAt(key_lcv);
         if (!displayed_properties.contains(prop_name) && !additionalproperties.contains(prop_name) ) {
            Vector value_vec = (Vector) hmap.get(prop_name);
            if (value_vec.size() > 0) {
                show_other_properties = true;
                break;
            }
         }
      }


boolean has_other_properties = false;
for (int key_lcv=0; key_lcv<key_vec.size(); key_lcv++) {
   prop_name = (String) key_vec.elementAt(key_lcv);
   if (!displayed_properties.contains(prop_name) && !additionalproperties.contains(prop_name) ) {
      Vector value_vec = (Vector) hmap.get(prop_name);
      if (value_vec.size() > 0) {
          has_other_properties = true;
          break;
      }
   }
}



  if (!has_other_properties) {
  %>
      <b>Other Properties:</b>
         <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/property_help_info.jsf',
          '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
         <img src="<%= request.getContextPath() %>/images/help.gif" alt="Property Definitions" title="Property Definitions" border="0">
         </a>
         &nbsp;
      <i>(none)</i>
  <%
  } else {
  %>


<p>
  <b>Other Properties:</b>
         <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/property_help_info.jsf',
          '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
         <img src="<%= request.getContextPath() %>/images/help.gif" alt="Property Definitions" title="Property Definitions" border="0">
         </a>

  <table class="datatable">
    <%
      for (int key_lcv=0; key_lcv<key_vec.size(); key_lcv++) {
        prop_name = (String) key_vec.elementAt(key_lcv);
        if (!displayed_properties.contains(prop_name) && !additionalproperties.contains(prop_name) ) {
          Vector value_vec = (Vector) hmap.get(prop_name);
          if (value_vec == null || value_vec.size() == 0) {
            if (n % 2 == 0) {
              %>
                <tr class="dataRowDark">
              <%
            } else {
              %>
                <tr class="dataRowLight">
              <%
            }
            n++;
            %>
                  <td><i><%=prop_name%></i></td>
                  <td><i>None</i></td>
                </tr>
            <%
          } else {
            value_vec = SortUtils.quickSort(value_vec);
            for (int j=0; j<value_vec.size(); j++) {
              String value = (String) value_vec.elementAt(j);

              if (n % 2 == 0) {
                %>
                  <tr class="dataRowDark">
                <%
              } else {
                %>
                  <tr class="dataRowLight">
                <%
              }
              n++;
              Vector value_source_vec = DataUtils.parseData(value);
              String propertyValue = (String) value_source_vec.elementAt(0);
              String propertySource = (String) value_source_vec.elementAt(1);
              if (propertySource.compareTo("None") == 0) propertySource = "";

              %>
                  <td><i><%=prop_name%></i></td>
                  <td><i><%=propertyValue%></i></td>
                  <td><i><%=propertySource%></i></td>
                </tr>
              <%
            }
          }
        }
      }
    %>
  </table>
</p>

<%
}
%>

<p>
    <%
      String concept_name = curr_concept.getEntityDescription().getContent();
      concept_name = concept_name.replaceAll(" ", "_");
      String concept_name_label = "Concept Name:";

      String primitive = null;
      String primitive_prop_name = "primitive";
      String primitive_label = "Defined Fully by Roles:";
      Vector primitive_value_vec = (Vector) hmap.get(primitive_prop_name);

      if (primitive_value_vec != null) {
        primitive = (String) primitive_value_vec.elementAt(0);
        if (primitive.compareTo("true") == 0) primitive = "No";
        else primitive = "Yes";
      }

      String kind = "not available";
      String kind_prop_name = "Kind";
      String kind_label = "Kind:";
    %>

  <%
  if (primitive == null) {
  %>
      <b>Additional Concept Data:&nbsp;</b> <i>(none)</i>
  <%
  } else {
  %>


  <table class="datatable">

    <%
    if (primitive != null) {
    %>
    <tr class="dataRowLight">
      <td><i><%=primitive_label%></i></td>
      <td><i><%=primitive%></i></td>
    </tr>
    <%
    }
    %>
  </table>

    <%
    }
    %>

</p>
<%
  String requestURL = request.getRequestURL().toString();
  int idx = requestURL.indexOf("pages");
  requestURL = requestURL.substring(0, idx);
  String url = requestURL + "ConceptReport.jsp?dictionary=NCI%20Thesaurus&code=" + concept_id;
  String bookmark_title = "NCImBrowser%20" + concept_id;
%>
<p>
  <b>URL to Bookmark</b>:
  <a href=javascript:bookmark('<%= url %>','<%= bookmark_title %>')>
    <i><%= requestURL %>ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=<%=concept_id%></i>
  </a>

<%
    //NCImBrowserProperties properties = null;
    properties = NCImBrowserProperties.getInstance();
    //String term_suggestion_application_url = properties.getProperty(NCImBrowserProperties.TERM_SUGGESTION_APPLIATION_URL);
    String default_dictionary = "NCI%20MetaThesaurus";
    //if (syns != null && syns.size() > 0) {
    //   tg_dictionary = "NCI%20Thesaurus";
    //}
%>
<%
}
%>