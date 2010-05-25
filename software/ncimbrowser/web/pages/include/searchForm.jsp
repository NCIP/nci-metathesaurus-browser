<%@ page import="gov.nih.nci.evs.browser.properties.NCImBrowserProperties" %>
<%@ page import="gov.nih.nci.evs.browser.utils.MetadataUtils" %>
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
</script>

  
<%
    String userAgent = request.getHeader("user-agent");
    boolean isIE = userAgent != null && userAgent.toLowerCase().contains("msie");

    String match_text = (String) request.getSession().getAttribute("matchText");
    if (match_text == null || match_text.compareTo("null") == 0) match_text = "";

    String displayed_match_text = HTTPUtils.convertJSPString(match_text);

    String algorithm = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("selectedAlgorithm"));
    String check_e = "", check_b = "", check_s = "" , check_c ="";
    if (algorithm == null || algorithm.compareTo("exactMatch") == 0)
      check_e = "checked";
    else if (algorithm.compareTo("startsWith") == 0)
      check_s= "checked";
    else if (algorithm.compareTo("DoubleMetaphoneLuceneQuery") == 0)
      check_b= "checked";
    else
      check_c = "checked";

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
  <h:form id="searchTerm" styleClass="search-form" onsubmit="javascript:disableAnchor();" >
 
  <label for="matchText" />
    <input CLASS="searchbox-input" id="matchText" name="matchText" value="<%=displayed_match_text%>" onFocus="active=true"
        onBlur="active=false"  onkeypress="return submitEnter('searchTerm:search',event)"  />   
    
    <h:commandButton id="search" value="Search" action="#{userSessionBean.searchAction}"
      accesskey="13"
      onclick="javascript:cursor_wait();"
      image="#{facesContext.externalContext.requestContextPath}/images/search.gif"
      alt="Search">
    </h:commandButton>
    
    
    <h:outputLink value="#{facesContext.externalContext.requestContextPath}/pages/help.jsf#searchhelp">
      <h:graphicImage value="/images/search-help.gif" style="border-width:0;" />
    </h:outputLink>


  <table border="0" cellspacing="0" cellpadding="0" width="340px">
    <tr valign="top" align="left">
      <td align="left" class="textbody" colspan="2">
        <input type="radio" name="algorithm" id="algorithm1" value="exactMatch" alt="Exact Match" <%=check_e%> /><label for="algorithm1">Exact Match&nbsp;</label>
        <input type="radio" name="algorithm" id="algorithm2" value="startsWith" alt="Begins With" <%=check_s%> /><label for="algorithm2">Begins With&nbsp;</label>
        <input type="radio" name="algorithm" id="algorithm3" value="contains" alt="Containts" <%=check_c%> /><label for="algorithm3">Contains</label>
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
        <input type="radio" name="searchTarget" id="searchTarget1" value="names" alt="Names" <%=check_n%> /><label for="searchTarget1">Name/Code&nbsp;</label>
        <input type="radio" name="searchTarget" id="searchTarget2" value="properties" alt="Properties" <%=check_p%> /><label for="searchTarget2">Property&nbsp;</label>
        <input type="radio" name="searchTarget" id="searchTarget3" value="relationships" alt="Relationships" <%=check_r%> /><label for="searchTarget3">Relationship</label>
      </td>
    </tr>
    <tr><td height="5px;"></td></tr>
    <tr><td colspan="2">
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr valign="top">
          <td align="left" class="textbody">
          
            <h:outputLabel id="sourceLabel" value="Source" styleClass="textbody">
              <h:selectOneMenu styleClass="textbody" id="source" value="#{userSessionBean.selectedSource}"
                valueChangeListener="#{userSessionBean.sourceSelectionChanged}"
                immediate="true" onchange="submit()" >
                <f:selectItems value="#{userSessionBean.sourceList}" />
              </h:selectOneMenu>
            </h:outputLabel>

    <input type="hidden" name="referer" id="referer" value="<%=HTTPUtils.getRefererParmEncode(request)%>" />
    



   <%

   if (!MetadataUtils.isMetadataAvailable()) {
       MetadataUtils.initialize();
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
      String formal_name = MetadataUtils.getSABFormalName(selectedSource);
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
              <img src="<%=basePath%>/images/visualize.gif" width="16px" height="16px" alt="<%=view_source_hierarchy_label%>" title="<%=view_source_hierarchy_label%>" border="0"/>
            </a>
    <%
            } else {
    %>
            <a class="icon_blue" href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/accept_license.jsf?dictionary=<%=formal_name%>&sab=<%=selectedSource%>&type=browsehierarchy', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
                    <img src="<%=basePath%>/images/visualize.gif" width="16px" height="16px" alt="<%=view_source_hierarchy_label%>" title="<%=view_source_hierarchy_label%>"  border="0"/>
            </a>
    <%
            }
    }

   %>

          </td>
          <!--
          <td valign="middle" align="right">
          -->
          <td align="right" class="textbody">
            <a class="global-nav" href="<%=request.getContextPath() %>/pages/advanced_search.jsf">Advanced Search</a>
          </td>
          
        </tr>
      </table>

    </td></tr>
  </table>

</h:form> 