<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.properties.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="javax.faces.context.*" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.*" %>
<%@ page import="org.apache.log4j.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
<script src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js"></script>
  <title>NCI Metathesaurus</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<body onLoad="document.forms.advancedSearchForm.matchText.focus();">
  <script type="text/javascript"
    src="<%=request.getContextPath()%>/js/wz_tooltip.js"></script>
  <script type="text/javascript"
    src="<%=request.getContextPath()%>/js/tip_centerwindow.js"></script>
  <script type="text/javascript"
    src="<%=request.getContextPath()%>/js/tip_followscroll.js"></script>
  <script type="text/javascript">
    function cursor_wait() {
        document.body.style.cursor = 'wait';
    }
    
    
    function refresh() {
      var text = document.forms["advancedSearchForm"].matchText.value;
      algorithm = "exactMatch";
      var algorithmObj = document.forms["advancedSearchForm"].adv_search_algorithm;
      for (var i=0; i<algorithmObj.length; i++) {
        if (algorithmObj[i].checked) {
          algorithm = algorithmObj[i].value;
        }
      }
      var adv_search_source = document.forms["advancedSearchForm"].adv_search_source.value;

      selectSearchOption = "Name";
      var selectSearchOptionObj = document.forms["advancedSearchForm"].selectSearchOption;
      for (var i=0; i<selectSearchOptionObj.length; i++) {
        if (selectSearchOptionObj[i].checked) {
          selectSearchOption = selectSearchOptionObj[i].value;
        }
      }

      var rel_search_association = document.forms["advancedSearchForm"].rel_search_association.value;
      var rel_search_rela = document.forms["advancedSearchForm"].rel_search_rela.value;
      var selectProperty = document.forms["advancedSearchForm"].selectProperty.value;
      
      window.location.href="/ncimbrowser/pages/advanced_search.jsf?refresh=1"
          + "&opt="+ selectSearchOption
          + "&text="+ text
          + "&algorithm="+ algorithm
          + "&sab="+ adv_search_source
          + "&prop="+ selectProperty
          + "&rel="+ rel_search_association
          + "&rela="+ rel_search_rela;
    }
    
    
    function refresh_code() {

      var text = escape(document.forms["advancedSearchForm"].matchText.value);
 
      var selectSearchOption = "Code";
      var algorithm = "exactMatch";
      var adv_search_source = document.forms["advancedSearchForm"].adv_search_source.value;

      var rel_search_association = document.forms["advancedSearchForm"].rel_search_association.value;
      var selectProperty = document.forms["advancedSearchForm"].selectProperty.value;

      var rel_search_association = document.forms["advancedSearchForm"].rel_search_association.value;
      var rel_search_rela = document.forms["advancedSearchForm"].rel_search_rela.value;
      
      window.location.href="/ncimbrowser/pages/advanced_search.jsf?refresh=1"
          + "&opt="+ selectSearchOption
          + "&text="+ text
          + "&algorithm="+ algorithm
          + "&sab="+ adv_search_source
          + "&prop="+ selectProperty
          + "&rel="+ rel_search_association
          + "&rela="+ rel_search_rela;
    }    
    
    
    
    
     function refresh_algorithm() {
       var text = escape(document.forms["advancedSearchForm"].matchText.value);
 
       var algorithm = "exactMatch";
       var algorithmObj = document.forms["advancedSearchForm"].adv_search_algorithm;
       for (var i=0; i<algorithmObj.length; i++) {
         if (algorithmObj[i].checked) {
            algorithm = algorithmObj[i].value;
            break;
         }
       }  

       var selectSearchOption = "";
       var selectSearchOptionObj = document.forms["advancedSearchForm"].selectSearchOption;
       for (var i=0; i<selectSearchOptionObj.length; i++) {
         if (selectSearchOptionObj[i].checked) {
           selectSearchOption = selectSearchOptionObj[i].value;
           break;
         }
       }
      
      
      if (algorithm != "exactMatch" && selectSearchOption == "Code") {
          selectSearchOption = "Name";
      }
            
      
      var adv_search_source = document.forms["advancedSearchForm"].adv_search_source.value;
      var rel_search_association = document.forms["advancedSearchForm"].rel_search_association.value;
      var selectProperty = document.forms["advancedSearchForm"].selectProperty.value;

      var rel_search_association = document.forms["advancedSearchForm"].rel_search_association.value;
      var rel_search_rela = document.forms["advancedSearchForm"].rel_search_rela.value;

      window.location.href="/ncimbrowser/pages/advanced_search.jsf?refresh=1"
          + "&opt="+ selectSearchOption
          + "&text="+ text
          + "&algorithm="+ algorithm
          + "&sab="+ adv_search_source
          + "&prop="+ selectProperty
          + "&rel="+ rel_search_association
          + "&rela="+ rel_search_rela;
    }        
    
    
    
  </script>
  <%!
      private static Logger _logger = Utils.getJspLogger("advanced_search.jsp");
  %>
  <f:view>
    <!-- Begin Skip Top Navigation -->
      <a href="#evs-content" class="hideLink" accesskey="1" title="Skip repetitive navigation links">skip navigation links</A>
    <!-- End Skip Top Navigation -->
    <%@ include file="/pages/include/header.jsp" %>
    <div class="center-page">
      <%@ include file="/pages/include/sub-header.jsp" %>
      <!-- Main box -->
      <div id="main-area">
        <%@ include file="/pages/include/content-header-alt.jsp" %>
<%
    String defaultSearchOption = "Name";
    String defaultAlgorithm = "contains";
    String refresh = HTTPUtils.cleanXSS((String) request.getParameter("refresh"));
    boolean refresh_page = false;
    if (refresh != null) {
        refresh_page = true;
    }

    String adv_search_algorithm = null;
    String search_string = "";
    String selectProperty = null;
    String rel_search_association = null;
    String rel_search_rela = null;
    String adv_search_source = null;
    String adv_search_type = null;

    String t = null;
    String selectSearchOption = null;

    if (refresh_page) {
        // Note: Called when the user selects "Search By" fields.
        selectSearchOption = HTTPUtils.cleanXSS((String) request.getParameter("opt"));
        search_string = HTTPUtils.cleanMatchTextXSS((String) request.getParameter("text"));
        adv_search_algorithm = HTTPUtils.cleanXSS((String) request.getParameter("algorithm"));
        adv_search_source = HTTPUtils.cleanXSS((String) request.getParameter("sab"));
        rel_search_association = HTTPUtils.cleanXSS((String) request.getParameter("rel"));
        rel_search_rela = HTTPUtils.cleanXSS((String) request.getParameter("rela"));
        selectProperty = HTTPUtils.cleanXSS((String) request.getParameter("prop"));
        
        
        
        
    } else {
        //SearchStatusBean bean = BeanUtils.getSearchStatusBean();
        SearchStatusBean bean = (SearchStatusBean) request.getSession().getAttribute("searchStatusBean");
        if (bean != null) {
		selectSearchOption = bean.getSearchType();
		adv_search_algorithm = bean.getAlgorithm();
		adv_search_source = bean.getSelectedSource();
		selectProperty = bean.getSelectedProperty();
		//search_string = bean.getMatchText();
		search_string = (String) request.getSession().getAttribute("matchText");
		rel_search_association = bean.getSelectedAssociation();
		rel_search_rela = bean.getSelectedRELA();

		_logger.debug("advanced_search.jsp adv_search_algorithm: " + adv_search_algorithm);
		_logger.debug("advanced_search.jsp adv_search_source: " + adv_search_source);
		_logger.debug("advanced_search.jsp selectProperty: " + selectProperty);
		_logger.debug("advanced_search.jsp search_string: " + search_string);
		_logger.debug("advanced_search.jsp rel_search_association: " + rel_search_association);
		_logger.debug("advanced_search.jsp rel_search_rela: " + rel_search_rela);
        }
        
	if (selectSearchOption == null)
	    selectSearchOption = defaultSearchOption;
        

	if (adv_search_algorithm == null)
	    adv_search_algorithm = defaultAlgorithm;
        
    }

    if (DataUtils.isNull(selectSearchOption)) {
        selectSearchOption = defaultSearchOption;
    }

    SearchStatusBean bean = null;
    String message = (String) request.getAttribute("message");
    if (message != null) {
        request.removeAttribute("message");
    }

    if (!refresh_page || message != null) {
        // Note: Called when search contains no match.
        Object bean_obj = FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("searchStatusBean");


        if (bean_obj == null) {
            bean_obj = request.getAttribute("searchStatusBean");
        }

        if (bean_obj == null) {
            bean = new SearchStatusBean();
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("searchStatusBean", bean);

        } else {
            bean = (SearchStatusBean) bean_obj;
            selectSearchOption = bean.getSearchType();
            
            if (selectSearchOption == null)
                selectSearchOption = defaultSearchOption;

            adv_search_algorithm = bean.getAlgorithm();
            adv_search_source = bean.getSelectedSource();
            selectProperty = bean.getSelectedProperty();
            search_string = bean.getMatchText();
            rel_search_association = bean.getSelectedAssociation();
            rel_search_rela = bean.getSelectedRELA();

/*
            _logger.debug("advanced_search.jsp adv_search_algorithm: " + adv_search_algorithm);
            _logger.debug("advanced_search.jsp adv_search_source: " + adv_search_source);
            _logger.debug("advanced_search.jsp selectProperty: " + selectProperty);
            _logger.debug("advanced_search.jsp search_string: " + search_string);
            _logger.debug("advanced_search.jsp rel_search_association: " + rel_search_association);
            _logger.debug("advanced_search.jsp rel_search_rela: " + rel_search_rela);
*/
            
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("searchStatusBean", bean);
        }
    }

    adv_search_type = selectSearchOption;

    if (rel_search_association == null) rel_search_association = "ALL";
    if (rel_search_rela == null) rel_search_rela = " ";
    if (selectProperty == null) selectProperty = "ALL";
    if (adv_search_source == null) adv_search_source = "ALL";
    if (search_string == null) search_string = "";
    if (adv_search_algorithm == null) adv_search_algorithm = "contains";


    String check__e = "", check__b = "", check__s = "" , check__c ="";
    if (adv_search_algorithm == null || adv_search_algorithm.compareTo("contains") == 0)
        check__c = "checked";
    else if (adv_search_algorithm.compareTo("startsWith") == 0)
        check__s= "checked";
    else if (adv_search_algorithm.compareTo("lucene") == 0)
        check__b= "checked";
    else
        check__e = "checked";
        

    String check_n2 = "", check_c2 = "", check_p2 = "" , check_r2 ="";
    if (selectSearchOption == null || selectSearchOption.compareTo("Name") == 0)
      check_n2 = "checked";
    else if (selectSearchOption.compareTo("Code") == 0)
        check_c2 = "checked";
    else if (selectSearchOption.compareTo("Property") == 0)
      check_p2 = "checked";
    else if (selectSearchOption.compareTo("Relationship") == 0)
      check_r2 = "checked";
    else check_n2 = "checked";


%>
        <div class="pagecontent">
          <a name="evs-content" id="evs-content"></a>
          <table>
            <tr>
            <td class="texttitle-blue">Advanced Search</td>
            </tr>

            <% if (message != null) { %>
        <tr class="textbodyred"><td>
      <p class="textbodyred">&nbsp;<%=message%></p>
        </td></tr>
            <% } %>

            <tr class="textbody"><td>
            <!--
               <FORM NAME="advancedSearchForm" METHOD="POST" CLASS="search-form" >
             -->

               <h:form id="advancedSearchForm" styleClass="search-form" >

                <table>
                  <tr><td>
    <input CLASS="searchbox-input" id="matchText" name="adv_matchText" value="<%=HTTPUtils.cleanXSS(search_string)%>" onFocus="active=true"
        onBlur="active=false"  onkeypress="return submitEnter('advancedSearchForm:adv_search',event)"  />

                    <h:commandButton id="adv_search" value="Search" action="#{userSessionBean.advancedSearchAction}"
                      accesskey="13"
                      onclick="javascript:cursor_wait();"
                      image="#{requestContextPath}/images/search.gif"
                      alt="Search">
                    </h:commandButton>

                  </td></tr>
                  <tr><td>
                     <table border="0" cellspacing="0" cellpadding="0">
                    <tr valign="top" align="left"><td align="left" class="textbody">
                      <input type="radio" name="adv_search_algorithm" id="adv_search_algorithm3" value="contains" alt="Contains" <%=check__c%> onclick="refresh_algorithm()"; /><label for="adv_search_algorithm3">Contains</label>
                      <input type="radio" name="adv_search_algorithm" id="adv_search_algorithm1" value="exactMatch" alt="Exact Match" <%=check__e%> onclick="refresh_algorithm()"; /><label for="adv_search_algorithm1">Exact Match&nbsp;</label>
                      <input type="radio" name="adv_search_algorithm" id="adv_search_algorithm2" value="startsWith" alt="Begins With" <%=check__s%> onclick="refresh_algorithm()"; /><label for="adv_search_algorithm2">Begins With&nbsp;</label>
                     <input type="radio" name="adv_search_algorithm" id="adv_search_algorithm4" value="lucene" alt="Lucene" <%=check__b%> tabindex="3" onclick="refresh_algorithm()"; >
                      Lucene
                    </td></tr>
                  </table>
                </td></tr>


<%
if (adv_search_algorithm.compareToIgnoreCase("lucene") != 0) {
%>


                <tr><td>
                  <h:outputLabel for="adv_search_source" id="rel_search_source_Label" value="Source" styleClass="textbody">
                    <select id="adv_search_source" name="adv_search_source" size="1">
                    <%
                      Vector src_vec = OntologyBean.getSupportedSources();
                      t = "ALL";
                      if (adv_search_source == null) adv_search_source = "ALL";
                        if (t.compareTo(adv_search_source) == 0) {
                    %>
                          <option value="<%=t%>" selected><%=t%></option>
                    <%
                        } else {
                    %>
                          <option value="<%=t%>"><%=t%></option>
                    <%
                       }

                       for (int i=0; i<src_vec.size(); i++) {
                         t = (String) src_vec.elementAt(i);
                         if (t.compareTo(adv_search_source) == 0) {
                    %>
                           <option value="<%=t%>" selected><%=t%></option>
                    <%
                         } else {
                    %>
                           <option value="<%=t%>"><%=t%></option>
                    <%
                         }
                       }
                    %>
                    </select>
                  </h:outputLabel>
                </td></tr>


<%
} else {
%>
    <input type="hidden" name="adv_search_source" id="adv_search_source" value="<%=HTTPUtils.cleanXSS(adv_search_source)%>">
<%
}
%>

                <tr><td>
                  &nbsp;&nbsp;
                </td></tr>

                <tr valign="top" align="left"><td align="left" class="textbody">
                Concepts with this value in:
                </td></tr>

                <tr valign="top" align="left"><td align="left" class="textbody">
                  <input type="radio" name="selectSearchOption" id="selectSearchOption2" value="Name" alt="Name" <%=check_n2%> onclick="javascript:refresh()" /><label for="selectSearchOption2">Name&nbsp;</label>

<%
if (adv_search_algorithm.compareToIgnoreCase("lucene") != 0) {
%>
                  <input type="radio" name="selectSearchOption" id="selectSearchOption1" value="Code" alt="Code" <%=check_c2%> onclick="refresh_code()" /><label for="selectSearchOption1">Code&nbsp;</label>
                  <input type="radio" name="selectSearchOption" id="selectSearchOption3" value="Property" alt="Property" <%=check_p2%> onclick="javascript:refresh()" /><label for="selectSearchOption3">Property&nbsp;</label>
                  <input type="radio" name="selectSearchOption" id="selectSearchOption4" value="Relationship" alt="Relationship" <%=check_r2%> onclick="javascript:refresh()" /><label for="selectSearchOption4">Relationship</label>
<%
}
%>

                </td></tr>

                <tr><td>
                  <table>
                  <% if (selectSearchOption.equals("Property")) { %>
                    <input type="hidden" name="rel_search_association" id="rel_search_association" value="<%=HTTPUtils.cleanXSS(rel_search_association)%>" />
                    <input type="hidden" name="rel_search_rela" id="rel_search_rela" value="<%=HTTPUtils.cleanXSS(rel_search_rela)%>" />
                    <tr>
                      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                      <td>
                        <h:outputLabel for="selectProperty" id="selectPropertyLabel" value="Property" styleClass="textbody">
                          <select id="selectProperty" name="selectProperty" size="1">
                          <%
                            t = "ALL";
                            if (t.compareTo(selectProperty) == 0) {
                          %>
                              <option value="<%=t%>" selected><%=t%></option>
                          <%} else {%>
                              <option value="<%=t%>"><%=t%></option>
                          <%}%>

                          <%
                            Vector property_vec = OntologyBean.getSupportedPropertyNames();
                            for (int i=0; i<property_vec.size(); i++) {
                              t = (String) property_vec.elementAt(i);
                              if (t.compareTo(selectProperty) == 0) {
                          %>
                                <option value="<%=t%>" selected><%=t%></option>
                          <%  } else { %>
                                <option value="<%=t%>"><%=t%></option>
                          <%
                              }
                            }
                          %>
                          </select>
                          <!--
                          <h:selectOneMenu id="selectProperty" value="#{searchStatusBean.selectedProperty}"
                              valueChangeListener="#{searchStatusBean.selectedPropertyChanged}"
                              immediate="true" >
                            <f:selectItems value="#{searchStatusBean.propertyList}" />
                          </h:selectOneMenu>
                          -->
                        </h:outputLabel>
                      </td>
                    </tr>

                  <% } else if (selectSearchOption.equals("Relationship")) { %>
                    <input type="hidden" name="selectProperty" id="selectProperty" value="<%=HTTPUtils.cleanXSS(selectProperty)%>" />
                    <tr>
                      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                      <td>
                        <h:outputLabel for="rel_search_association" id="rel_search_associationLabel" value="Relationship" styleClass="textbody">
                          <select id="rel_search_association" name="rel_search_association" size="1">
                          <%
                            t = "ALL";
                            if (t.compareTo(rel_search_association) == 0) {
                          %>
                              <option value="<%=t%>" selected><%=t%></option>
                          <%} else {%>
                              <option value="<%=t%>"><%=t%></option>
                          <%} %>

                          <%
                            Vector association_vec = OntologyBean.getSupportedAssociationNames();
                            for (int i=0; i<association_vec.size(); i++) {
                              t = (String) association_vec.elementAt(i);
                              if (t.compareTo(rel_search_association) == 0) {
                          %>
                                <option value="<%=t%>" selected><%=t%></option>
                          <%  } else { %>
                                <option value="<%=t%>"><%=t%></option>
                          <%
                              }
                            }
                          %>
                          </select>
                        </h:outputLabel>
                        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rel_help_info.jsf',
                          '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
                            <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Definitions" title="Relationship Definitions" border="0">
                        </a>
                      </td>
                    </tr>

                    <tr>
                      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                      <td>
                        <h:outputLabel for="rel_search_rela" id="rel_search_rela_Label" value="RELA" styleClass="textbody">
                          <select id="rel_search_rela" name="rel_search_rela" size="1">
                          <%
                            t = " ";
                            if (t.compareTo(rel_search_rela) == 0) {
                          %>
                              <option value="<%=t%>" selected><%=t%></option>
                          <%} else {%>
                              <option value="<%=t%>"><%=t%></option>
                          <%}%>

                          <%
                            Vector rela_vec = OntologyBean.getRELAs();
                            for (int i=0; i<rela_vec.size(); i++) {
                              t = (String) rela_vec.elementAt(i);
                              //_logger.debug("rela: " + t);
                              if (t.compareTo(rel_search_rela) == 0) {
                          %>
                                <option value="<%=t%>" selected><%=t%></option>
                          <%  } else { %>
                                <option value="<%=t%>"><%=t%></option>
                          <%
                              }
                            }
                          %>
                          </select>
                        </h:outputLabel>
                        <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/rela_help_info.jsf',
                          '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
                            <img src="<%= request.getContextPath() %>/images/help.gif" alt="Relationship Attr. Definitions" title="Relationship Attr. Definitions" border="0">
                        </a>
                      </td>
                    </tr>
                  <% } else { %>
                    <input type="hidden" name="selectProperty" id="selectProperty" value="<%=HTTPUtils.cleanXSS(selectProperty)%>" />
                    <input type="hidden" name="rel_search_association" id="rel_search_association" value="<%=HTTPUtils.cleanXSS(rel_search_association)%>" />
                    <input type="hidden" name="rel_search_rela" id="rel_search_rela" value="<%=HTTPUtils.cleanXSS(rel_search_rela)%>" />
                  <% }%>

                  </table>
                </td></tr>




<%
if (adv_search_algorithm.compareToIgnoreCase("lucene") == 0) { 
%>

<tr><td>
<p>
<table>
   <tr><td class="textbody">&nbsp;Examples:</td></tr>
   <tr>
       <td class="textbody">&nbsp;&nbsp;
<i>Wildcard (multiple characters): heart*</i>
       </td>
   </tr>
   <tr>
       <td class="textbody">&nbsp;&nbsp;
<i>Wildcard (single character): he?rt</i>
       </td>
   </tr>
   <tr>
       <td class="textbody">&nbsp;&nbsp;
<i>Fuzzy match: heart~</i>
       </td>
   </tr>
   <tr>
       <td class="textbody">&nbsp;&nbsp;
<i>Boolean: heart AND attack</i>
       </td>
   </tr>
       <td class="textbody">&nbsp;&nbsp;
<i>Boosting: heart^5 AND attack</i>
       </td>
   </tr>
   <tr>
       <td class="textbody">&nbsp;&nbsp;
<i>Negation: heart -attack</i>
       </td>
   </tr>

</table>
</p>
</td></tr>

<%
}
%>




              </table>
              <input type="hidden" name="referer" id="referer" value="<%=HTTPUtils.getRefererParmEncode(request)%>" />
              <input type="hidden" name="adv_search_type" id="adv_search_type" value="<%=HTTPUtils.cleanXSS(adv_search_type)%>" />


            </h:form>

          </td></tr>
        </table>
        <%@ include file="/pages/include/nciFooter.jsp" %>
      </div>
      <!-- end Page content -->
    </div>
    <div class="mainbox-bottom"><img src="<%=basePath%>/images/mainbox-bottom.gif" width="962" height="5" alt="Mainbox Bottom" /></div>
    <!-- end Main box -->
  </div>
</f:view>
<script type="text/javascript">_satellite.pageBottom();</script>
</body>
</html>
