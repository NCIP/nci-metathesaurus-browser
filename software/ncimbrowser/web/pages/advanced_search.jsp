<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.LexGrid.concepts.Concept" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.properties.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.HTTPUtils" %>
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

    <script type="text/javascript">
    
    function refresh() {
        var text = document.forms["advancedSearchForm"].matchText.value;
        var selectSearchOption = document.forms["advancedSearchForm"].selectSearchOption.value;
        algorithm = "exactMatch";
        var algorithmObj = document.forms["advancedSearchForm"].adv_search_algorithm;
        for (var i=0; i<algorithmObj.length; i++) {
            if (algorithmObj[i].checked) {
                algorithm = algorithmObj[i].value;
            }
        }
        var adv_search_source = document.forms["advancedSearchForm"].adv_search_source.value;

	var rel_search_association = document.forms["advancedSearchForm"].rel_search_association.value;
	var rel_search_rela = document.forms["advancedSearchForm"].rel_search_rela.value;
        
        var selectProperty = document.forms["advancedSearchForm"].selectProperty.value;
		window.location.href="/ncimbrowser/pages/advanced_search.jsf?refresh=1&opt=" 
		    + selectSearchOption
		    + "&text="
		    + text
		    + "&algorithm="
		    + algorithm
		    + "&sab="
		    + adv_search_source 
		    + "&prop="
		    + selectProperty
		    + "&rel="
		    + rel_search_association 		    
		    + "&rela="
		    + rel_search_rela; 	
    }
    
    
    </script>


<f:view>
  <%@ include file="/pages/include/header.jsp" %>
  <div class="center-page">
    <%@ include file="/pages/include/sub-header.jsp" %>
    <!-- Main box -->
    <div id="main-area">

       <%@ include file="/pages/include/content-header-alt.jsp" %>
 
     
<%

    String refresh = request.getParameter("refresh");
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
    
    String t = null;
    String selectSearchOption = null;
    
    if (refresh_page) {

        selectSearchOption = (String) request.getParameter("opt");
        search_string = (String) request.getParameter("text");
        adv_search_algorithm = (String) request.getParameter("algorithm");
        adv_search_source = (String) request.getParameter("sab");
        rel_search_association = (String) request.getParameter("rel");
        rel_search_rela = (String) request.getParameter("rela");
        selectProperty = (String) request.getParameter("prop");
        
        
    } else {
	selectSearchOption = (String) request.getAttribute("selectSearchOption");
    }

    if (selectSearchOption == null || selectSearchOption.compareTo("null") == 0) {
        selectSearchOption = "Property";
    }


       SearchStatusBean bean = null;
	if (!refresh_page) {       

	       Object bean_obj = FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("searchStatusBean");
	       if (bean_obj == null) {
			   bean = new SearchStatusBean();
			   FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("searchStatusBean", bean);

	       } else {
			   bean = (SearchStatusBean) bean_obj;
			   adv_search_algorithm = bean.getAlgorithm();
			   adv_search_source = bean.getSelectedSource();
			   selectProperty = bean.getSelectedProperty();
			   search_string = bean.getMatchText();
			   rel_search_association = bean.getSelectedAssociation();
			   rel_search_rela = bean.getSelectedRELA();

	       }
	}


if (rel_search_association == null) rel_search_association = "ALL";
if (rel_search_rela == null) rel_search_rela = " ";
if (selectProperty == null) selectProperty = "ALL";
if (adv_search_source == null) adv_search_source = "ALL";
if (search_string == null) search_string = "";
if (adv_search_algorithm == null) adv_search_algorithm = "exactMatch";

/*
System.out.println("adv_search_algorithm: " + adv_search_algorithm);
System.out.println("search_string: " + search_string);
System.out.println("adv_search_source: " + adv_search_source);
System.out.println("selectProperty: " + selectProperty);
System.out.println("rel_search_association: " + rel_search_association);
System.out.println("rel_search_rela: " + rel_search_rela);
System.out.println("\n"); 
*/

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

     <tr class="textbody"><td>
       <%
      String message = (String) request.getAttribute("message");
      if (message != null) {
      %>
	 <p class="textbodyred">&nbsp;<%=message%></p>
      <%
      }
      %>
      </td></tr>

             <tr class="textbody">
                  <td>    
 		      <FORM NAME="advancedSearchForm" METHOD="POST" CLASS="search-form" >
                          <table>
                          
                             <tr><td>
                             <input CLASS="searchbox-input" name="matchText" value="<%=search_string%>">
                              
 			    <h:commandButton id="adv_search" value="Search" action="#{userSessionBean.advancedSearchAction}"
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
         

<tr><td>
&nbsp;&nbsp;
</td></tr>         
         

              <tr><td>
			    <h:outputLabel id="searchOptionLabel" value="Search By" styleClass="textbody">


<select id="selectSearchOption" name="selectSearchOption" size="1" onchange="javascript:refresh()" >
if (selectSearchOption == null) selectSearchOption = "Property";
<%
if (selectSearchOption.compareTo("Property") == 0) {
%>
    <option value="Property" selected>Property</option>
    <option value="Relationship" >Relationship</option>
<%
} else {
%>
    <option value="Property" >Property</option>
    <option value="Relationship" selected >Relationship</option>
<%
}
%>

</select>
				
			    </h:outputLabel> 
			    
	      </td></tr>
			  
             

<tr><td>
<table>
<%
 if (selectSearchOption.compareTo("Property") == 0) {
%>

    <input type="hidden" name="rel_search_association" id="rel_search_association" value="<%=rel_search_association%>">
    <input type="hidden" name="rel_search_rela" id="rel_search_rela" value="<%=rel_search_rela%>">


              <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>
  			    <h:outputLabel id="selectPropertyLabel" value="Property" styleClass="textbody">


             <select id="selectProperty" name="selectProperty" size="1">
        <%    
                t = "ALL";
		if (t.compareTo(selectProperty) == 0) {
	%>            
		    <option value="<%=t%>" selected><%=t%></option>
	<%            
		} else {
	%>       
		    <option value="<%=t%>"><%=t%></option>
	<%             
		}
        %>
        
             <%   
                Vector property_vec = OntologyBean.getSupportedPropertyNames();
                for (int i=0; i<property_vec.size(); i++) {
                    t = (String) property_vec.elementAt(i);
			if (t.compareTo(selectProperty) == 0) {
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
             
<!--             
  			    
 				<h:selectOneMenu id="selectProperty" value="#{searchStatusBean.selectedProperty}" 
 				    valueChangeListener="#{searchStatusBean.selectedPropertyChanged}"
 				    immediate="true" >
 				  <f:selectItems value="#{searchStatusBean.propertyList}" />
 				</h:selectOneMenu> 
--> 				
 				
 				
 			    </h:outputLabel> 
              </td></tr>                         

<% } else { %> 

           <input type="hidden" name="selectProperty" id="selectProperty" value="<%=selectProperty%>">
    
           <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>
           
             <h:outputLabel id="rel_search_associationLabel" value="Relationship" styleClass="textbody">
             <select id="rel_search_association" name="rel_search_association" size="1">
        <%    
                t = "ALL";
		if (t.compareTo(rel_search_association) == 0) {
	%>            
		    <option value="<%=t%>" selected><%=t%></option>
	<%            
		} else {
	%>       
		    <option value="<%=t%>"><%=t%></option>
	<%             
		}
        %>
        
             <%   
                Vector association_vec = OntologyBean.getSupportedAssociationNames();
                for (int i=0; i<association_vec.size(); i++) {
                    t = (String) association_vec.elementAt(i);
			if (t.compareTo(rel_search_association) == 0) {
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
           
           
           <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>
             <h:outputLabel id="rel_search_rela_Label" value="RELA" styleClass="textbody">
             <select id="rel_search_rela" name="rel_search_rela" size="1">
<%             
             t = " ";
             if (t.compareTo(rel_search_rela) == 0) {
%>             
                 <option value="<%=t%>" selected><%=t%></option>
<%                 
             } else {
%>             
                 <option value="<%=t%>"><%=t%></option>
<%                 
             }
 %>            
             <% 
                Vector rela_vec = OntologyBean.getRELAs();
                for (int i=0; i<rela_vec.size(); i++) {
                     t = (String) rela_vec.elementAt(i);
                     //System.out.println("rela: " + t);
                     if (t.compareTo(rel_search_rela) == 0) {
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
} 
%>  


</table>

</td></tr>
 
                             
                         </table>   
                        
		     <input type="hidden" name="referer" id="referer" value="<%=HTTPUtils.getRefererParmEncode(request)%>">
         </form> 
		     
		</td>             
 
             </tr>

          </table>    

          <%@ include file="/pages/include/nciFooter.jsp" %>
      </div>
      <!-- end Page content -->
    </div>
    <div class="mainbox-bottom"><img src="<%=basePath%>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    <!-- end Main box -->
  </div>
</f:view>
</body>
</html>