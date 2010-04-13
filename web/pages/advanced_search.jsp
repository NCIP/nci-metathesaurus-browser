<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.LexGrid.concepts.Concept" %>
<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.HTTPUtils" %>
<%@ page import="gov.nih.nci.evs.browser.properties.NCImBrowserProperties" %>

<%@ page import="gov.nih.nci.evs.browser.bean.IteratorBean" %>
<%@ page import="gov.nih.nci.evs.browser.bean.OntologyBean" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>

<%@ page import="java.io.*" %>


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
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

    <script type="text/javascript"
      src="<%=request.getContextPath()%>/js/wz_tooltip.js"></script>
    <script type="text/javascript"
      src="<%=request.getContextPath()%>/js/tip_centerwindow.js"></script>
    <script type="text/javascript"
      src="<%=request.getContextPath()%>/js/tip_followscroll.js"></script>

<f:view>
  <%@ include file="/pages/include/header.jsp" %>
  <div class="center-page">
    <%@ include file="/pages/include/sub-header.jsp" %>
    <!-- Main box -->
    <div id="main-area">

       <%@ include file="/pages/include/content-header-alt.jsp" %>
 
      <!-- Page content -->
      
      
      
<%
    String t = null;
    String adv_search_algorithm = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("adv_search_algorithm"));
    String check__e = "", check__b = "", check__s = "" , check__c ="";
    if (adv_search_algorithm == null || adv_search_algorithm.compareTo("exactMatch") == 0)
      check__e = "checked";
    else if (adv_search_algorithm.compareTo("startsWith") == 0)
      check__s= "checked";
    else if (adv_search_algorithm.compareTo("DoubleMetaphoneLuceneQuery") == 0)
      check__b= "checked";
    else
      check__c = "checked";
      
 %>      
      
      <div class="pagecontent">
          <table>


<%
String rel_search_direction = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("rel_search_direction"));
String check_source = "";
String check_target = "";
if (rel_search_direction == null || rel_search_direction.compareToIgnoreCase("source") == 0)
check_source = "checked";
else if (rel_search_direction.compareToIgnoreCase("target") == 0)
check_target= "checked"; 


String advancedSearchOption = HTTPUtils.getSessionAttribute(
    request, "advancedSearchOption", "Property");
String search_string = (String) request.getParameter("searchText");
if (search_string == null || search_string.compareTo("null") == 0) search_string = "";

%>

             <tr class="textbody">
                  <td>    
 		      <FORM NAME="advancedSearchForm" METHOD="POST" CLASS="search-form">
 		      
 		          <input type="hidden" name="searchType" value="<%=advancedSearchOption%>">

                          <table>
                          
                             <tr><td>
                             <input CLASS="searchbox-input" name="matchText" value="<%=search_string%>">
                             
                             <!--
 			    <input CLASS="searchbox-input" name="matchText" value="<%=match_text%>" onFocus="active = true"
 				onBlur="active = false" onkeypress="return submitEnter('search',event)" />
                              -->
                              
 			    <h:commandButton id="adv_search" value="Search" action="#{userSessionBean.searchAction}"
 			      onclick="javascript:cursor_wait();"
 			      image="#{facesContext.externalContext.requestContextPath}/images/search.gif"
 			      alt="Search">
 			    </h:commandButton>
                             
                             </td></tr>
 
                              <tr><td>
 
 				  <table border="0" cellspacing="0" cellpadding="0">
 				    <tr valign="top" align="left">
 				      <td align="left" class="textbody"><input type="radio"
 					name="adv_search_algorithm" value="exactMatch" alt="Exact Match" <%=check__e%>>Exact
 				      Match&nbsp; <input type="radio" name="adv_search_algorithm" value="startsWith"
 					alt="Begins With" <%=check__s%>>Begins With&nbsp; <input
 					type="radio" name="adv_search_algorithm" value="contains" alt="Containts"
 					<%=check__c%>>Contains
 					</td>
 				    </tr>
 				    
 				 </table>   
                             </td></tr>
                             
                             
<tr><td>
<h:outputLabel id="rel_search_source_Label" value="Source" styleClass="textbody">

<select id="adv_search_source" name="adv_search_source" size="1">
<%  
    t = "ALL";
%>   
   <option value="<%=t%>"><%=t%></option>
<% 
   Vector src_vec = OntologyBean.getSupportedSources();
   for (int i=0; i<src_vec.size(); i++) {
        t = (String) src_vec.elementAt(i);
%>       
        <option value="<%=t%>"><%=t%></option>
<%        
   }
%>   
</select>
</h:outputLabel>  
</td></tr>
         

<tr><td>
&nbsp;&nbsp;
</td></tr>         
         

              <tr><td>
			    <h:outputLabel id="searchOptionLabel" value="Search By" styleClass="textbody">
				<h:selectOneMenu id="selectSearchOption" value="#{searchStatusBean.selectedSearchOption}" 
				    valueChangeListener="#{searchStatusBean.searchOptionChanged}"
				    immediate="true" onchange="submit()" >
				  <f:selectItems value="#{searchStatusBean.searchOptionList}" />
				</h:selectOneMenu> 
			    </h:outputLabel> 
			  </td></tr>

<tr><td>
<table>
<%
 if (advancedSearchOption.compareTo("Property") == 0) {
%>
 
<!-- 
              <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>
      			<h:outputLabel id="selectPropertyTypeLabel" value="Property Type" styleClass="textbody">
   				<h:selectOneMenu id="selectPropertyType" value="#{searchStatusBean.selectedPropertyType}" 
   				    valueChangeListener="#{searchStatusBean.selectedPropertyTypeChanged}"
   				    immediate="true" >
   				  <f:selectItems value="#{searchStatusBean.propertyTypeList}" />
   				</h:selectOneMenu> 
 			    </h:outputLabel> 
              </td></tr>    
-->                              

              <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>
  			    <h:outputLabel id="selectPropertyLabel" value="Property" styleClass="textbody">
 				<h:selectOneMenu id="selectProperty" value="#{searchStatusBean.selectedProperty}" 
 				    valueChangeListener="#{searchStatusBean.selectedPropertyChanged}"
 				    immediate="true" >
 				  <f:selectItems value="#{searchStatusBean.propertyList}" />
 				</h:selectOneMenu> 
 			    </h:outputLabel> 
              </td></tr>                         

<% } else { %> 

           <f:subview id="tmp">
             <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>
             <h:outputLabel id="rel_search_associationLabel" value="Relationship" styleClass="textbody">
             <h:selectOneMenu id="rel_search_association" value="#{searchStatusBean.selectedRelationship}" 
                 valueChangeListener="#{searchStatusBean.selectedRelationshipChanged}"
                 immediate="true" >
               <f:selectItems value="#{searchStatusBean.relationshipList}" />
             </h:selectOneMenu> 
             </h:outputLabel>  
             </td></tr>
           
             <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>
             <h:outputLabel id="rel_search_rela_Label" value="RELA" styleClass="textbody">
             <h:selectOneMenu id="rel_search_rela" value="#{searchStatusBean.selectedRELA}" 
                 valueChangeListener="#{searchStatusBean.selectedRELAChanged}"
                 immediate="true" >
               <f:selectItems value="#{searchStatusBean.RELAList}" />
             </h:selectOneMenu> 
             </h:outputLabel>  
             </td></tr>
           </f:subview>

<!--
             <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>
               <table border="0" cellspacing="0" cellpadding="0">
			     <tr valign="top" align="left">
			       <td align="left" class="textbody"><input type="radio"
				     name="rel_search_direction" value="source" alt="Source" <%=check_source%>>Source
			         &nbsp; <input type="radio" name="rel_search_direction" value="target"
				     alt="Target" <%=check_target%>>Target
				   </td>
			     </tr>
			   </table>   
             </td></tr>         
-->                             

<%                              
} 
%>  


</table>

</td></tr>





 
                             
                         </table>    
		     </form> 
		     
		</td>             
 
             </tr>

          </table>    

          <%@ include file="/pages/include/nciFooter.jsp" %>
      </div>
      <!-- end Page content -->
    </div>
    <div class="mainbox-bottom"><img src="images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    <!-- end Main box -->
  </div>
</f:view>
</body>
</html>