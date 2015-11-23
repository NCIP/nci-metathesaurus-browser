<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.LicenseBean" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
  <title>NCI Metathesaurus Browser</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<%

  String dictionary = HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
  String dictionary_0 = dictionary;
  if (dictionary_0 != null) {
	  dictionary_0 = dictionary_0.replaceAll("&#40;", "(");
	  dictionary_0 = dictionary_0.replaceAll("&#41;", ")");
	  dictionary_0 = dictionary_0.replaceAll("%20", " ");
	  dictionary = dictionary_0;
  }
    
  
  String display_name = DataUtils.getMetadataValue(dictionary, "display_name");
 
  
  String code = HTTPUtils.cleanXSS((String) request.getParameter("code"));
  String sab = HTTPUtils.cleanXSS((String) request.getParameter("sab"));

  String type = HTTPUtils.cleanXSS((String) request.getParameter("type"));

  String licenseStmt = LicenseBean.resolveCodingSchemeCopyright(dictionary, null);
  

  if (licenseStmt == null) licenseStmt = " ";

  String base_path = request.getContextPath();

  //if (dictionary != null) dictionary = dictionary.replaceAll("%20", " ");
%>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<f:view>
  <!-- Begin Skip Top Navigation -->
    <a href="#evs-content" class="hideLink" accesskey="1" title="Skip repetitive navigation links">skip navigation links</A>
  <!-- End Skip Top Navigation -->
  <%@ include file="/pages/include/header.jsp" %>
  <div class="center-page">
    <%@ include file="/pages/include/sub-header.jsp" %>
    <!-- Main box -->
    <div id="main-area">
      <!-- Thesaurus, banner search area -->
      <div class="bannerarea">
	    <a href="<%=basePath%>" style="text-decoration: none;">
	      <div class="vocabularynamebanner">
	        <span class="vocabularynamelong"><%=DataUtils.getApplicationVersionJspDisplay()%></span>
	      </div>
	    </a>        
      </div>
      <!-- end Thesaurus, banner search area -->
      <!-- Quick links bar -->
      <%@ include file="/pages/include/quickLink.jsp" %>
      <!-- end Quick links bar -->
      <!-- Page content -->
      <div class="pagecontent">
        <a name="evs-content" id="evs-content"></a>
        <p>
          <%
          	if (display_name == null) {
          %>
          	No copyright/license statement has been found for this terminology.<br>
          	Metadata 'short name' may not have been loaded.
          <%
          	} else {
          %>          
            To access <b><%=display_name%></b>, please review and accept the copyright/license statement below:
          <%
          	}
          %>  
        </p>
        <%
        	if (display_name != null) {
        %>        
        <textarea cols="87" rows="15" readonly align="left"><%=licenseStmt%></textarea>
        <p>
          If and only if you agree to these terms and conditions, click the Accept button to proceed.
        </p>
        <%
        	}
        %>
          <h:form>
            <%
            	if (display_name != null) {
            %>
            <h:commandButton
              id="accept"
              value="accept"
              action="#{userSessionBean.acceptLicenseAction}"
              image="../images/accept.gif"
              alt="Accept">
            </h:commandButton>
            &nbsp;&nbsp;
            <%
            	}
            %>
            <a href="javascript:window.close();">
              <img src="<%= request.getContextPath() %>/images/cancel.gif" border="0" alt="Cancel"/>
            </a>

            <input type="hidden" id="dictionary" name="dictionary" value="<%=dictionary%>" />
            <input type="hidden" id="code" name="code" value="<%=code%>" />
            <input type="hidden" id="sab" name="sab" value="<%=sab%>" />
            <input type="hidden" id="type" name="type" value="<%=type%>" />
          </h:form>
        <%@ include file="/pages/include/nciFooter.jsp" %>
      </div>
      <!-- end Page content -->
    </div>
    <div class="mainbox-bottom"><img src="<%=base_path%>/images/mainbox-bottom.gif" width="962" height="5" alt="Mainbox Bottom" /></div>
    <!-- end Main box -->
  </div>
</f:view>
</body>
</html>
