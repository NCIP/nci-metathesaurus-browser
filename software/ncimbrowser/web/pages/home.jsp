<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Entity"%>
<%
  String ncim_build_info = new DataUtils().getNCIMBuildInfo();
  String application_version = new DataUtils().getApplicationVersion();
  String app_build_tag = new DataUtils().getNCITAppBuildTag();
  String evs_service_url = new DataUtils().getEVSServiceURL();
%>
<!--
   Build info: <%=ncim_build_info%>
 Version info: <%=application_version%>
          Tag: <%=app_build_tag%>
   LexEVS URL: <%=evs_service_url%>
  -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core">
  <head>
<script src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js"></script>
    <title>NCI Metathesaurus</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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
<script>(function(i,s,o,g,r,a,m){i["GoogleAnalyticsObject"]=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,"script","//www.google-analytics.com/analytics.js","ga");ga("create", "UA-150112876-2", {"cookieDomain":"auto"});ga("send", "pageview");</script>
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
        <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">skip navigation links</A>
      <!-- End Skip Top Navigation -->
      <%@ include file="/pages/include/header.jsp"%>
      <div class="center-page">
        <%@ include file="/pages/include/sub-header.jsp"%> <!-- Main box -->
      <div id="main-area">
        <%@ include file="/pages/include/content-header.jsp"%>
      <!-- Page content -->
      <div class="pagecontent">
        <a name="evs-content" id="evs-content" tabindex="0"></a>
        <%@ include file="/pages/include/welcome.jsp"%>
        <%@ include file="/pages/include/nciFooter.jsp"%></div>
      <!-- end Page content --></div>
      <div class="mainbox-bottom">

        <img src="<%=basePath%>/images/mainbox-bottom.gif" width="962" height="5" alt="Mainbox Bottom" />
       
      </div>
      <!-- end Main box --></div>
    </f:view>
<script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>
