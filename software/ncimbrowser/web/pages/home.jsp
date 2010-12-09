<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Entity"%>
<%
  String ncim_build_info = new DataUtils().getNCIMBuildInfo();
  String application_version = new DataUtils().getApplicationVersion();
  String anthill_build_tag_built = new DataUtils().getNCITAnthillBuildTagBuilt();
  String evs_service_url = new DataUtils().getEVSServiceURL();
%>
<!--
   Build info: <%=ncim_build_info%>
 Version info: <%=application_version%>
          Tag: <%=anthill_build_tag_built%>
   LexEVS URL: <%=evs_service_url%>
  -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
  <head>
    <title>NCI Metathesaurus</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link rel="stylesheet" type="text/css"
      href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <link rel="shortcut icon"
      href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
    <script type="text/javascript"
      src="<%= request.getContextPath() %>/js/script.js"></script>
    <script type="text/javascript"
      src="<%= request.getContextPath() %>/js/search.js"></script>
    <script type="text/javascript"
      src="<%= request.getContextPath() %>/js/dropdown.js"></script>
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
      <%@ include file="/pages/include/header.jsp"%>
      <div class="center-page">
        <%@ include file="/pages/include/sub-header.jsp"%> <!-- Main box -->
      <div id="main-area">
        <%@ include file="/pages/include/content-header.jsp"%>
      <!-- Page content -->
      <div class="pagecontent">
        <a name="evs-content" id="evs-content"></a>
        <%@ include file="/pages/include/welcome.jsp"%>
        <%@ include file="/pages/include/nciFooter.jsp"%></div>
      <!-- end Page content --></div>
      <div class="mainbox-bottom">
        <img src="<%=basePath%>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" />
      </div>
      <!-- end Main box --></div>
    </f:view>
  </body>
</html>