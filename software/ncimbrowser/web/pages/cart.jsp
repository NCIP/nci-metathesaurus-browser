<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
  <title>NCI Metathesaurus</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<body onLoad="javascript:popupMessage();document.forms.searchTerm.matchText.focus();">
<script type="text/javascript" src="<%= request.getContextPath() %>/js/wz_tooltip.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_centerwindow.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_followscroll.js"></script>
<f:view>
    <!-- Begin Skip Top Navigation -->
      <a href="#evs-content" class="hideLink" accesskey="1" title="Skip repetitive navigation links">skip navigation links</A>
    <!-- End Skip Top Navigation -->
    <script language="javascript" type="text/javascript">
      function backButton() {
        location.href = '<h:outputText value="#{CartActionBean.backurl}"/>';
      }    
      function popupMessage() {
         if (<h:outputText value="#{CartActionBean.messageflag}"/>) {
         	alert('<h:outputText value="#{CartActionBean.message}"/>');
         }	
      }
    </script>  
    <%
      String contactUsUrl = request.getContextPath() + "/pages/contact_us.jsf";
      String subsetsUrl = request.getContextPath() + "/pages/subset.jsf";
    %>
    <%@ include file="/pages/include/header.jsp" %>
    <div class="center-page">
      <%@ include file="/pages/include/sub-header.jsp" %>
      <!-- Main box -->
      <div id="main-area">
     <%@ include file="/pages/include/content-header.jsp" %>
        <!-- Page content -->
        <div class="pagecontent">
          <a name="evs-content" id="evs-content"></a>
          <h:form>
          <table border="0" class="dataTable">
            <tr>
              <td width="200px">
	            <table border="0">
	              <tr>
	                <td class="texttitle-blue">Cart</td>
	                <td class="texttitle-gray">(<h:outputText value="#{CartActionBean.count}"/>)</td>
	              </tr>
	            </table>
          	  </td>
	          <td align="right" nowrap>
	            <h:commandLink onclick="backButton();return false;" value="Back" title="Return to previous screen" styleClass="texttitle-blue-small"/> |
	            <h:commandLink value="Select All" action="#{CartActionBean.selectAllInCart}" title="Select all concepts" styleClass="texttitle-blue-small"/> |
	            <h:commandLink value="Clear" action="#{CartActionBean.unselectAllInCart}" title="Unselect all concepts" styleClass="texttitle-blue-small"/> |
	            <h:commandLink value="Remove" action="#{CartActionBean.removeFromCart}" title="Remove concepts from the cart" styleClass="texttitle-blue-small" onclick="if (!confirm('Are you sure?')) return false;"/> |
	            <h:commandLink value="Export XML" action="#{CartActionBean.exportCartXML}" title="Export cart contents in RDF/XML format" styleClass="texttitle-blue-small"/> |
	            <h:commandLink value="Export CSV" action="#{CartActionBean.exportCartCSV}" title="Generate a list of cart concepts in CSV format readable from Excel" styleClass="texttitle-blue-small"/>
	          </td>
        	</tr>
     	 </table>
      <hr/>
      <table class="dataTable" summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
            <tr>
              <th class="dataTableHeader" scope="col" align="left" width="20px">&nbsp;</th>
              <th class="dataTableHeader" scope="col" align="left">Concept</th>
              <th class="dataTableHeader" scope="col" align="left">Semantic Type</th>
        </tr>
          <c:forEach var="item" begin="0" items="#{CartActionBean.concepts}" varStatus="status">
          <c:choose>
            <c:when test="${status.index % 2 == 0}">
              <tr class="dataRowDark">
            </c:when>
            <c:otherwise>
              <tr class="dataRowLight">
            </c:otherwise>
            </c:choose>
              <td><h:selectBooleanCheckbox value="#{item.selected}"/></td>
              <td>
                <h:outputLink value="#{item.url}">${item.name}</h:outputLink>
              </td>
              <td>${item.semanticType}</td>
              </tr>
          </c:forEach>
      </table>
      </h:form>
          <br/>
          <%@ include file="/pages/include/nciFooter.jsp" %>
        </div> <!-- end pagecontent -->
      </div> <!-- end main-area -->
      <div class="mainbox-bottom"><img src="<%=basePath%>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
      <!-- end Main box -->
    </div> <!-- end center-page -->
</f:view>
</body>
</html>