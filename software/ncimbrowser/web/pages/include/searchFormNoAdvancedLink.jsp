<%@ page import="gov.nih.nci.evs.browser.properties.NCImBrowserProperties" %>
<%@ page import="gov.nih.nci.evs.browser.utils.NCImMetadataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.HTTPUtils" %>
<%@ page import="gov.nih.nci.evs.browser.bean.LicenseBean" %>

<script type="text/javascript">
  function cursor_wait() {
     document.body.style.cursor = 'wait';
  }

  function disableAnchor(){

    var obj1 = document.getElementById("a_tpTab");
    if (obj1 != null) obj1.removeAttribute('href');

    var obj2 = document.getElementById("a_relTab");
    if (obj2 != null) obj2.removeAttribute('href');

    var obj3 = document.getElementById("a_synTab");
    if (obj3 != null) obj3.removeAttribute('href');

    var obj4 = document.getElementById("a_srcTab");
    if (obj4 != null) obj4.removeAttribute('href');

    var obj5 = document.getElementById("a_allTab");
    if (obj5 != null) obj5.removeAttribute('href');

    var obj6 = document.getElementById("a_hierBut");
    if (obj6 != null) obj6.removeAttribute('href');
  }
  
  
    function onCodeButtonPressed(formname) {
	  var algorithmObj = document.forms[formname].algorithm;
	  for (var j=0; j<algorithmObj.length; j++) {
		  algorithm = algorithmObj[j].value;
		  if (algorithm == "exactMatch") {
			 algorithmObj[j].checked = true;
		  }
	  }
    }

    function getSearchTarget(formname) {
          var searchTargetObj = document.forms[formname].searchTarget;
	  for (var j=0; j<searchTargetObj.length; j++) {
	      if (searchTargetObj[j].checked == true) {
	         return searchTargetObj[j].value;
	      }
	  }
    }

    function onAlgorithmChanged(formname) {
      var curr_target = getSearchTarget(formname);
      if (curr_target != "codes") return;

          var searchTargetObj = document.forms[formname].searchTarget;
	  for (var j=0; j<searchTargetObj.length; j++) {
		  target = searchTargetObj[j].value;
		  if (target == "codes") {
			  searchTargetObj[0].checked = true;
			  return;
		  }
	  }
    }	  
    
  
</script>


<%


    String userAgent = request.getHeader("user-agent");
    boolean isIE = userAgent != null && userAgent.toLowerCase().contains("msie");

    String match_text = (String) request.getSession().getAttribute("matchText");
    if (match_text == null || match_text.compareTo("null") == 0) match_text = "";

    String displayed_match_text = HTTPUtils.convertJSPString(match_text);
    String algorithm = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("selectedAlgorithm"));
    String check_e = "", check_b = "", check_s = "" , check_c ="", check_cd = "";

/*
    if (algorithm == null || algorithm.compareTo("exactMatch") == 0)
      check_e = "checked";
    else if (algorithm.compareTo("startsWith") == 0)
      check_s= "checked";
    else if (algorithm.compareTo("lucene") == 0)
      check_b= "checked";
    else
      check_c = "checked";
*/

    if (algorithm == null || algorithm.compareTo("contains") == 0)
      check_c = "checked";
    else if (algorithm.compareTo("startsWith") == 0)
      check_s= "checked";
    else if (algorithm.compareTo("lucene") == 0)
      check_b= "checked";
    else
      check_e = "checked";


        String searchTarget = (String) request.getSession().getAttribute("searchTarget");
        String check_n = "", check_p = "" , check_r ="";
        if (searchTarget == null || searchTarget.compareTo("names") == 0)
          check_n = "checked";
        else if (searchTarget.compareTo("properties") == 0)
          check_p= "checked";
        else
          check_r = "checked";

 %>


 <!--
 <FORM NAME="searchTerm" METHOD="POST" CLASS="search-form" onsubmit="javascript:disableAnchor();">
  -->
  <h:form id="searchTerm" styleClass="search-form" onsubmit="javascript:disableAnchor();">

    <input CLASS="searchbox-input" id="matchText" name="matchText" value="<%=displayed_match_text%>" onFocus="active=true"
        onBlur="active=false"  onkeypress="return submitEnter('searchTerm:search',event)" tabindex="1"/>

    <h:commandButton id="search" value="Search" action="#{userSessionBean.searchAction}"
      accesskey="13"
      onclick="javascript:cursor_wait();"
      image="#{requestContextPath}/images/search.gif"
      alt="Search"
      styleClass="searchbox-btn"
      tabindex="2">
    </h:commandButton>
    
  <h:outputLink
    value="#{facesContext.externalContext.requestContextPath}/pages/help.jsf#searchhelp"
    tabindex="3">
    <h:graphicImage value="/images/search-help.gif" styleClass="searchbox-btn" alt="Search Help"
    style="border-width:0;"/>
  </h:outputLink>

  <table border="0" cellspacing="0" cellpadding="0" width="340px" role='presentation'>
    <tr valign="top" align="left">
      <td align="left" class="textbody" colspan="2">
        <input type="radio" name="algorithm" id="algorithm3" value="contains"   alt="Contains"    <%=check_c%> tabindex="4" onclick="onAlgorithmChanged('searchTerm');"><label for="algorithm3">Contains&nbsp;</label>
        <input type="radio" name="algorithm" id="algorithm1" value="exactMatch" alt="Exact Match" <%=check_e%> tabindex="5"/><label for="algorithm1">Exact Match&nbsp;</label>
        <input type="radio" name="algorithm" id="algorithm2" value="startsWith" alt="Begins With" <%=check_s%> tabindex="6" onclick="onAlgorithmChanged('searchTerm');"><label for="algorithm2">Begins With&nbsp;</label>

      </td>
    </tr>
    <tr align="left">
      <td width="263px" height="1px" bgcolor="#2F2F5F"></td>
      <!-- The following lines are needed to make "Advanced Search" link flush right -->
      <% if (isIE) { %>
          <td width="77px"></td>
      <% } else { %>
          <td></td>
      <% } %>
    </tr>

    <tr valign="top" align="left">
      <td align="left" class="textbody" colspan="2">
      
	<input type="radio" name="searchTarget" id="searchTarget0" value="names"         alt="Name"         <%=check_n%>  tabindex="7"><label for="searchTarget0">Name&nbsp;</label>
	<input type="radio" name="searchTarget" id="searchTarget1" value="codes"         alt="Code"         <%=check_cd%> tabindex="8" onclick="onCodeButtonPressed('searchTerm');"><label for="searchTarget1">Code&nbsp;</label>
        <input type="radio" name="searchTarget" id="searchTarget2" value="properties" alt="Properties" <%=check_p%> tabindex="9"/><label for="searchTarget2">Property&nbsp;</label>
        <input type="radio" name="searchTarget" id="searchTarget3" value="relationships" alt="Relationships" <%=check_r%> tabindex="10"/><label for="searchTarget3">Relationship</label>

</td>
    </tr>
    <tr><td height="5px;"></td></tr>
    <tr><td colspan="2">
      <table border="0" cellspacing="0" cellpadding="0" width="100%" role='presentation'>
        <tr valign="top">
          <td align="left" class="textbody">
            <h:outputLabel id="sourceLabel" value="Source" styleClass="textbody" for="source"/>&nbsp;
            <h:selectOneMenu styleClass="textbody" id="source" value="#{userSessionBean.selectedSource}"
               valueChangeListener="#{userSessionBean.sourceSelectionChanged}"
               immediate="true" tabindex="11">
               <f:selectItems value="#{userSessionBean.sourceList}" />
            </h:selectOneMenu>

    <input type="hidden" name="referer" id="referer" value="<%=HTTPUtils.getRefererParmEncode(request)%>" />

<%
    String searchForm_key = (String) request.getSession().getAttribute("key");
    if (searchForm_key != null) {
%>
    <input type="hidden" name="key" id="key" value="<%=searchForm_key%>" />
<%
    }
%>


   <%

   if (!NCImMetadataUtils.isMetadataAvailable()) {
       NCImMetadataUtils.initialize();
   }

   String selectedSource = "ALL";
   Object obj = request.getSession().getAttribute("selectedSource");
   if (obj != null) {
       selectedSource = (String) obj;
   }


    String sf_available_hierarchies = NCImBrowserProperties.getSourceHierarchies();
    if (sf_available_hierarchies != null && selectedSource.compareTo("ALL") != 0 && sf_available_hierarchies.indexOf("|" + selectedSource + "|") != -1) {
      boolean isLicensed = DataUtils.checkIsLicensed(selectedSource);
      boolean licenseAgreementAccepted = false;
      String formal_name = NCImMetadataUtils.getSABFormalName(selectedSource);
      String view_source_hierarchy_label = "View " + selectedSource + " Hierarchy";

      LicenseBean licenseBean = (LicenseBean) request.getSession().getAttribute("licenseBean");
      if (licenseBean == null) {
          licenseBean = new LicenseBean();
          request.getSession().setAttribute("licenseBean", licenseBean);
      }

      licenseAgreementAccepted = licenseBean.licenseAgreementAccepted(formal_name);

            if (!isLicensed || licenseAgreementAccepted) {

    %>
            <a class="icon_blue" href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_hierarchy.jsf?sab=<%=selectedSource%>', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
              <img src="<%=basePath%>/images/visualize.gif" width="12px" height="12px" alt="<%=view_source_hierarchy_label%>" title="<%=view_source_hierarchy_label%>" border="0"/>
            </a>
    <%
            } else {
    %>
            <a class="icon_blue" href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/accept_license.jsf?dictionary=<%=formal_name%>&sab=<%=selectedSource%>&type=browsehierarchy', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
                    <img src="<%=basePath%>/images/visualize.gif" width="12px" height="12px" alt="<%=view_source_hierarchy_label%>" title="<%=view_source_hierarchy_label%>"  border="0"/>
            </a>
    <%
            }
    }

   %>

          </td>
          <td valign="middle" align="right">
          </td>

        </tr>
      </table>

    </td></tr>
  </table>

</h:form>
