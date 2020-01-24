<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.CartActionBean" %>
<%@ page import="gov.nih.nci.evs.browser.bean.CartActionBean.Concept" %>
<%@ page import="javax.faces.context.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
<script src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js"></script>
  <title>NCI Metathesaurus</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>


<script>(function(i,s,o,g,r,a,m){i["GoogleAnalyticsObject"]=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,"script","//www.google-analytics.com/analytics.js","ga");ga("create", "UA-150112876-2", {"cookieDomain":"auto"});ga("send", "pageview");</script>
</head>
<body onLoad="javascript:popupMessage();document.forms.searchTerm.matchText.focus();">
<script type="text/javascript" src="<%= request.getContextPath() %>/js/wz_tooltip.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_centerwindow.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_followscroll.js"></script>

	<script type="text/javascript">
	function submitform()
	{
	  document.cartFormId.submit();
	}
	</script>


<f:view>
    <%
      String default_cs = "NCI Metathesaurus";
      String contactUsUrl = request.getContextPath() + "/pages/contact_us.jsf";
      String subsetsUrl = request.getContextPath() + "/pages/subset.jsf";
      gov.nih.nci.evs.browser.bean.CartActionBean.Concept item = null;
      CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");  
      if (cartActionBean == null) {
	    cartActionBean = new CartActionBean();
	    cartActionBean._init();
	    request.getSession().setAttribute("cartActionBean", cartActionBean);  
      }        
      Collection<gov.nih.nci.evs.browser.bean.CartActionBean.Concept> items = cartActionBean.getConcepts(); 
      int count = items.size();//cartActionBean.getCount();
      
    %>
    
 
     <%@ include file="/pages/include/header.jsp" %>
     <div class="center-page">
       <%@ include file="/pages/include/sub-header.jsp" %>
       <!-- Main box -->
       <div id="main-area">
      <%@ include file="/pages/include/content-header.jsp" %>
         <!-- Page content -->
         <div class="pagecontent">
          <a name="evs-content" id="evs-content" tabindex="0"></a>
  
<form name="cartFormId" method="post" action="<%=request.getContextPath() %>/ajax?action=cart"><br>
             
            <table border="0" class="datatable_960">
              <tr>
                <td width="200px">
                <table border="0" width="100%" role='presentation'>
                  <tr>
                    <td class="texttitle-blue" width="40">Cart</td>
                    <td class="texttitle-gray"> (<%=count%>)</td>
                   
                    <td class="texttitle-gray">
<a href="<%=request.getContextPath() %>/pages/home.jsf"><font size="2">Exit Cart</font></a>
                    </td>
                  </tr>
                </table>
                </td>
                
  <%               
  if (count > 0) {              
  %>                
              <td align="right" valign="bottom" nowrap>
              
<input type=image src="<%=request.getContextPath() %>/images/selectall.gif"  id="cartAction" name="cartAction1" value="selectall" alt="Select All" title="Select all concepts" onclick="this.form.submit();">
&nbsp;
<input type=image src="<%=request.getContextPath() %>/images/clearselections.gif"  id="cartAction" name="cartAction2" value="unselectall" alt="Unselect" title="Unselect all concepts"  onclick="this.form.submit();">
&nbsp;
<input type=image src="<%=request.getContextPath() %>/images/remove.gif"  id="cartAction" name="cartAction3" value="removefromcart" alt="Remove" title="Remove concepts from the cart" onclick="this.form.submit();">
&nbsp; 
<input type=image src="<%=request.getContextPath() %>/images/exportxml.gif"  id="cartAction" name="cartAction4" value="exportxml" alt="Export XML" title="Export cart contents in LexGrid XML format" onclick="this.form.submit();">
&nbsp; 
<input type=image src="<%=request.getContextPath() %>/images/exportcsv.gif"  id="cartAction" name="cartAction5" value="exportcsv" alt="Export CSV" title="Generate a list of cart concepts in CSV format readable from Excel" onclick="this.form.submit();">

              </td>              
            </tr>
         </table>
        <hr/>
  <%
  

      
  String message = (String) request.getSession().getAttribute("message");
  request.getSession().removeAttribute("message");
  
  
  if (message != null) {
  %>
  <p class="textbodyred">&nbsp;<%=message%></p>
  <%
  } 
  %>
          <table class="datatable_960" summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
              <tr>
                <th class="dataTableHeader" scope="col" align="left" width="20px">&nbsp;</th>
                <th class="dataTableHeader" scope="col" align="left">Concept</th>
                <th class="dataTableHeader" scope="col" align="left">Semantic Type</th>
              </tr>
            
              
  <%
  if (cartActionBean == null) {
  %>
          <tr><td>cartActionBean is null???</td></tr>
  <%        
  } else {
  	items = cartActionBean.getConcepts();
  	if (items != null) {
 	   try { 
  		Iterator it = items.iterator();
  		int i=0;
  		while (it.hasNext()) {
                      item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it.next(); 
                      String checked = "";
                      String checkbox_name = "code_" + item.getCode();
                      if (item.getSelected()) {
                          checked = "checked";
                      }
                          if (i%2 == 0) {
  %>                         
                               <tr class="dataRowDark">
  <%                             
                           } else {
  %>                             
                               <tr class="dataRowLight">
  <%
                           }
                           i++;
  %>
                         <td>
 <input type="checkbox" id=<%=checkbox_name%> name=<%=checkbox_name%> value=<%=item.getCode()%> <%=checked%> />
                         </td>
                         <td>
  			     <label for=<%=item.getCode()%>>
<a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=default_cs%>&code=<%=item.getCode()%>"><%=DataUtils.encodeTerm(item.getName())%></a>
  			     </label>
  			 </td>
  
  			 
  			 <td><%=item.getSemanticType()%></td>
  			 </tr>
  	<%		  
  		}
  		
  	    } catch (Exception ex) {
  	        ex.printStackTrace();
  	    }
  		
  	}
  }
  
  }
  %>               
          </table>
        </form>
 
          <br/>
          <%@ include file="/pages/include/nciFooter.jsp" %>
        </div> <!-- end pagecontent -->
      </div> <!-- end main-area -->
      <div class="mainbox-bottom"><img src="<%=basePath%>/images/mainbox-bottom.gif" width="962" height="5" alt="Mainbox Bottom" /></div>
      <!-- end Main box -->
    </div> <!-- end center-page -->
    
</f:view>

<script type="text/javascript">_satellite.pageBottom();</script>
</body>
</html>
