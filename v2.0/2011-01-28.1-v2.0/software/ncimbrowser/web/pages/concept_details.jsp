<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<%@ page import="org.LexGrid.concepts.Entity" %>
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

<%@ page import="org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>NCI Metathesaurus</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<body onLoad="document.forms.searchTerm.matchText.focus();">

    <script type="text/javascript"
      src="<%=request.getContextPath()%>/js/wz_tooltip.js"></script>
    <script type="text/javascript"
      src="<%=request.getContextPath()%>/js/tip_centerwindow.js"></script>
    <script type="text/javascript"
      src="<%=request.getContextPath()%>/js/tip_followscroll.js"></script>
  <f:view>
  <!-- Begin Skip Top Navigation -->
    <a href="#evs-content" class="hideLink" accesskey="1" title="Skip repetitive navigation links">skip navigation links</A>
  <!-- End Skip Top Navigation -->
<%
Entity concept_details_c = null;
String concept_details_code = null;
Object concept_details_obj = request.getSession().getAttribute("concept");
if (concept_details_obj == null) {
    concept_details_code = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("code"));
    request.getSession().setAttribute("code", concept_details_code);
} else {
    concept_details_c = (Entity) concept_details_obj;
    concept_details_code = concept_details_c.getEntityCode();
}


String active_code = (String) request.getSession().getAttribute("active_code");
if (active_code == null) {
    request.getSession().setAttribute("active_code", concept_details_code);
} else {
   if (active_code.compareTo(concept_details_code) != 0) {
       request.getSession().removeAttribute("AssociationTargetHashMap");
       request.getSession().setAttribute("active_code", concept_details_code);
   }
}



String concept_details_type = (String) request.getSession().getAttribute("type");
Boolean isNew = (Boolean) request.getSession().getAttribute("new_search");
request.getSession().removeAttribute("new_search");

%>

      <%@ include file="/pages/include/header.jsp" %>
      <div class="center-page">
        <%@ include file="/pages/include/sub-header.jsp" %>
        <!-- Main box -->
        <div id="main-area">


        <%@ include file="/pages/include/content-header.jsp" %>

        <!-- Page content -->
        <div class="pagecontent">
        <a name="evs-content" id="evs-content"></a>
<%
    String dictionary = null;
    String code = null;
    String type = concept_details_type;
    String sortBy = null;
    String name = null;
    Entity c = null;
    String vers = null;
    String ltag = null;
    String sab = null;
    String sourcecode = null;


    String checkmultiplicity = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("checkmultiplicity"));
    if (checkmultiplicity == null) checkmultiplicity = "false";

if (isNew == null || isNew.equals(Boolean.FALSE))
{
  String ptype = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("type"));
  if (ptype != null)
      type = ptype;
}

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
            request.getSession().setAttribute("type", type);


    boolean multipleCUIs = false;

    if (type.compareTo("sources") == 0 && checkmultiplicity.compareTo("true") == 0) {

        request.getSession().setAttribute("type", type);

    boolean searchInactive = true;
    sab = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("sab"));
    sourcecode = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("sourcecode"));
    int maxToReturn = 100;
    ResolvedConceptReferencesIterator iterator = new SearchUtils().findConceptWithSourceCodeMatching(Constants.CODING_SCHEME_NAME, null, sab, sourcecode, maxToReturn, searchInactive);
          IteratorBean iteratorBean = new IteratorBean(iterator);
          iteratorBean.setIterator(iterator);

   int size = iteratorBean.getSize();
   if (size > 1) {

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


    List list = iteratorBean.getData(0, 100);
    for (int i=0; i<list.size(); i++) {
         ResolvedConceptReference rcr = (ResolvedConceptReference) list.get(i);
         c = rcr.getReferencedEntry();
         code = rcr.getConceptCode();
         name = rcr.getEntityDescription().getContent();

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
                visitedConcepts = (Vector) request.getSession()
                    .getAttribute("visitedConcepts");
                if (visitedConcepts == null) {
                  visitedConcepts = new Vector();
                }

                String visitedConceptStr = code + "|" + c.getEntityDescription().getContent();
                if (!visitedConcepts.contains(visitedConceptStr)) {
                  visitedConcepts.add(visitedConceptStr);
                  request.getSession().removeAttribute("visitedConcepts");
                  request.getSession().setAttribute("visitedConcepts",
                      visitedConcepts);
                }


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
        if (c != null) {
       request.getSession().setAttribute("concept", c);
       request.getSession().setAttribute("code", code);
       name = c.getEntityDescription().getContent();

      } else {
         name = "ERROR: Invalid code.";
      }

  String term_suggestion_application_url1 = (String) request.getSession().getAttribute("term_suggestion_application_url");
  
  if (term_suggestion_application_url1 == null || term_suggestion_application_url1.length() < 1) {
     term_suggestion_application_url1 = MetadataUtils.getMetadataValue(Constants.CODING_SCHEME_NAME, null, null, "term_suggestion_application_url");
     if (term_suggestion_application_url1 == null || term_suggestion_application_url1.length() < 1) {
    	 term_suggestion_application_url1 = NCImBrowserProperties.getTermSuggestionApplicationUrl();    	 
     }
     if (term_suggestion_application_url1 != null) {
         request.getSession().setAttribute("term_suggestion_application_url", term_suggestion_application_url1);
     }
  }
  String tg_dictionary = "NCI%20Metathesaurus";

          if (c != null) {

        request.getSession().setAttribute("dictionary", dictionary);
        request.getSession().setAttribute("singleton", "false");
        request.getSession().setAttribute("concept", c);
        request.getSession().setAttribute("code", c.getEntityCode());

          %>
      <h:form>    
      <div class="texttitle-blue">	  	
      <table border="0" width="700px">
        <tr>
          <td class="texttitle-blue"><%=name%> (CUI <%=code%>)</td>
          <td align="right" valign="bottom" class="texttitle-blue-rightJust" nowrap>
             <a href="<%=term_suggestion_application_url1%>?dictionary=<%=tg_dictionary%>&code=<%=code%>" target="_blank" alt="Term Suggestion">Suggest changes to this concept</a>
			 <br>
			 <h:commandLink action="#{CartActionBean.addToCart}" value="Add to Cart">				
			   <f:setPropertyActionListener target="#{CartActionBean.entity}" value="concept" />
			   <f:setPropertyActionListener target="#{CartActionBean.codingScheme}" value="dictionary" />
			 </h:commandLink>			 			   
          </td>
        </tr>
      </table>            
      </div>
      <hr>
      <input type="hidden" name="type" value="<%=type %>">
      </h:form>
      <%@ include file="/pages/include/typeLinks.jsp" %>
      <div class="tabTableContentContainer">
          <%@ include file="/pages/include/property.jsp" %>
          <%@ include file="/pages/include/relationship.jsp" %>
          <%@ include file="/pages/include/synonym.jsp" %>
          <%@ include file="/pages/include/sources.jsp" %>
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

request.getSession().setAttribute("prev_type", type);
request.getSession().removeAttribute("type");


          %>
            <%@ include file="/pages/include/nciFooter.jsp" %>
        </div>
        <!-- end Page content -->
      </div>
      <div class="mainbox-bottom"><img src="<%=basePath%>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
      <!-- end Main box -->
    </div>
  </f:view>
</body>
</html>