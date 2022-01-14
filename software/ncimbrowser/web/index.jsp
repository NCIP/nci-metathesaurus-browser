<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%
  String queryString = request.getQueryString();
  System.out.println("queryString: " + queryString);
%>  

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>NCIm</title>
  </head>
  <body>

<%     
  if (queryString != null && queryString.indexOf("style") != -1) {
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