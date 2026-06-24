<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Vector" %>

<%
  String queryString = request.getQueryString();
%>  

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>NCI Metathesaurus Browser</title>
  </head>
  <body>

<%     
  if (queryString != null && (queryString.indexOf("style") != -1 || queryString.indexOf("alert") != -1 || queryString.indexOf("netsparker") != -1)) {
%>       
<h2>
<center>Server Error</center>
</h2>
      	<center><b>The server encountered an unexpected condition that prevented it from fulfilling the request.</b></center>
<%      
  } else {
%>  
    <jsp:forward page="/pages/home.jsf" />
    
<% } %>     

 </body>
</html>
