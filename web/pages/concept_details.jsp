<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.properties.PropertyFileParser" %>
<%@ page import="gov.nih.nci.evs.browser.properties.NCImBrowserProperties" %>
<%@ page import="gov.nih.nci.evs.browser.bean.DisplayItem" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.common.*" %>
<%@ page import="org.LexGrid.concepts.Concept" %>
<%@ page import="org.LexGrid.concepts.Presentation" %>
<%@ page import="org.LexGrid.commonTypes.Source" %>
<%@ page import="org.LexGrid.commonTypes.EntityDescription" %>
<%@ page import="org.LexGrid.commonTypes.Property" %>
<%@ page import="org.LexGrid.commonTypes.PropertyQualifier" %>
<%@ page import="org.LexGrid.concepts.Presentation" %>
<%@ page import="org.LexGrid.commonTypes.Source" %>
<%@ page import="org.LexGrid.commonTypes.EntityDescription" %>
<%@ page import="org.LexGrid.commonTypes.Property" %>
<%@ page import="org.LexGrid.commonTypes.PropertyQualifier" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <title>NCI Metathesaurus</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
  <f:view>

<%
Concept concept_details_c = null;
String concept_details_code = null;
Object concept_details_obj = request.getSession().getAttribute("concept");
if (concept_details_obj == null) {
    concept_details_code = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("code"));
    request.getSession().setAttribute("code", concept_details_code);
} else {
    concept_details_c = (Concept) concept_details_obj;
    concept_details_code = concept_details_c.getEntityCode();
}

String concept_details_type = (String) request.getSession().getAttribute("type");
Boolean isNew = (Boolean) request.getSession().getAttribute("new_search");
request.getSession().removeAttribute("new_search");

%>

      <%@ include file="/pages/templates/header.xhtml" %>
      <div class="center-page">
        <%@ include file="/pages/templates/sub-header.xhtml" %>
        <!-- Main box -->
        <div id="main-area">
    <%@ include file="/pages/templates/content-header.xhtml" %>

        <!-- Page content -->
        <div class="pagecontent">


<%
    String dictionary = null;
    String code = null;
    String type = null;
    String sortBy = null;
    String name = null;
    Concept c = null;
    String vers = null;
    String ltag = null;
    String sab = null;
    String sourcecode = null;


    String checkmultiplicity = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("checkmultiplicity"));
    if (checkmultiplicity == null) checkmultiplicity = "false";

type = "properties";
if (isNew == null)
{
  type = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("type"));
} else if (isNew.equals(Boolean.FALSE)) {
        type = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("type"));
}

request.getSession().setAttribute("type", type);

            if (type == null) {
                type = "properties";
            }
            else if (type.compareTo("properties") != 0 &&
                     type.compareTo("relationship") != 0 &&
                     type.compareTo("synonym") != 0 &&
                     type.compareTo("sources") != 0 &&
                     type.compareTo("all") != 0) {
                type = "properties";
            }




    boolean multipleCUIs = false;

    if (type.compareTo("sources") == 0 && checkmultiplicity.compareTo("true") == 0) {

        request.getSession().setAttribute("type", type);

  boolean searchInactive = true;
  sab = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("sab"));
  sourcecode = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("sourcecode"));
  int maxToReturn = 100;
  Vector u = new SearchUtils().findConceptWithSourceCodeMatching(Constants.CODING_SCHEME_NAME, null, sab, sourcecode, maxToReturn, searchInactive);
        if (u != null && u.size() > 1) {
            multipleCUIs = true;
    %>

    <table width="700px">
      <tr>
        <table>
          <tr>
      <td class="texttitle-blue">Result for:</td>
      <td class="texttitle-gray"><%=sab%> code &nbsp;<%=sourcecode%></td>
          </tr>
        </table>
      </tr>
      <tr>
        <td><hr></td>
      </tr>
      <tr>
        <td>
          <b>Multiple concepts are found containing <%=sab%> code &nbsp;<%=sourcecode%>:</b>
        </td>
      </tr>
      <tr>
        <td class="textbody">
          <table class="dataTable" summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
      <%
        for (int i=0; i<u.size(); i++) {
            c = (Concept) u.elementAt(i);
            code = c.getEntityCode();
            name = c.getEntityDescription().getContent();
            Vector semantic_types = new DataUtils().getPropertyValues(c, "GENERIC", "Semantic_Type");
            String semantic_type = "";
            if (semantic_types != null && semantic_types.size() > 0) {
          for (int j=0; j<semantic_types.size(); j++) {
              String t = (String) semantic_types.elementAt(j);
              semantic_type = semantic_type + t;
              if (j < semantic_types.size()-1) semantic_type = semantic_type + ";";
          }
            }

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
          <!--
            <a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=<%=code%>" ><%=name%></a>
           -->
            <a href="<%=request.getContextPath() %>/pages/concept_details.jsf?type=sources&code=<%=code%>&sab=<%=sab%>&sourcecode=<%=sourcecode%>"><%=name%></a>
          </td>
          <td class="dataCellText">
              <%=semantic_type%>
          </td>
        </tr>
            <%
         }
      %>
          </table>
        </td>
      </tr>
    </table>
    <%
        }
    }

    if (!multipleCUIs) {

    String singleton = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("singleton"));
    if (singleton != null && singleton.compareTo("true") == 0) {
      dictionary = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("dictionary"));
      if (dictionary == null) {
  dictionary = Constants.CODING_SCHEME_NAME;
      }
      code = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("code"));
    } else {
      dictionary = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
      if (dictionary == null) {
         dictionary = Constants.CODING_SCHEME_NAME;
      }
      code = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("code"));
      if (code == null) {
          code = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("code"));
      } else {
          request.getSession().removeAttribute("AssociationTargetHashMap");
      }
      sortBy = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("sortBy"));
    }



    c = DataUtils.getConceptByCode(dictionary, vers, ltag, code);
    if (c != null) {
      Vector synonyms = DataUtils.getSynonyms(c, "NCI");
      Boolean code_In_NCI = Boolean.FALSE;
      if (synonyms != null && synonyms.size() > 0) {
    code_In_NCI = Boolean.TRUE;;
      }
      request.getSession().setAttribute("codeInNCI", code_In_NCI);
    }
%>


          <%
            if (type == null) {
              type = "properties";
            }

            if (sortBy == null) {
              sortBy = "name";
            }

            name = "";
            if (dictionary.compareTo(Constants.CODING_SCHEME_NAME) != 0) {
               name = "ERROR: Invalid coding scheme name.";
            } else {
        if (c != null) {
           request.getSession().setAttribute("concept", c);
           request.getSession().setAttribute("code", code);
           name = c.getEntityDescription().getContent();

        } else {
           name = "ERROR: Invalid code.";
        }
     }

String tg_dictionary = "NCI%20MetaThesaurus";
          if (c != null) {
        request.getSession().setAttribute("dictionary", dictionary);
        request.getSession().setAttribute("singleton", "false");
 request.getSession().setAttribute("Concept", c);
 request.getSession().setAttribute("code", c.getEntityCode());

          %>
      <div class="texttitle-blue">

      <table border="0" width="700px">
        <tr>
          <td class="texttitle-blue"><%=name%> (CUI <%=code%>)</td>
          <td align="right" valign="bottom" class="texttitle-blue-rightJust" nowrap>
             <a href="<%=term_suggestion_application_url%>?dictionary=<%=tg_dictionary%>&code=<%=code%>" target="_blank" alt="Term Suggestion">Suggest changes to this concept</a>
          </td>
        </tr>
      </table>

      </div>

      <hr>
      <%@ include file="/pages/templates/typeLinks.xhtml" %>
      <div class="tabTableContentContainer">
          <%@ include file="/pages/templates/property.xhtml" %>
          <%@ include file="/pages/templates/relationship.xhtml" %>
          <%@ include file="/pages/templates/synonym.xhtml" %>
          <%@ include file="/pages/templates/sources.xhtml" %>
      </div>
          <%
          } else {
          %>
      <div class="textbody">
          <%=name%>
      </div>
    <%
          }
      }

request.getSession().removeAttribute("type");
          %>
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