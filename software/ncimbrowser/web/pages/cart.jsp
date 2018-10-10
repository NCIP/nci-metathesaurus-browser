<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html; charset=UTF-8" %>

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
</head>
<body onLoad="javascript:popupMessage();document.forms.searchTerm.matchText.focus();">
<script type="text/javascript" src="<%= request.getContextPath() %>/js/wz_tooltip.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_centerwindow.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_followscroll.js"></script>
<f:view>
    <!-- Begin Skip Top Navigation -->
      <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">skip navigation links</A>
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
      function confirmRemoveMessage() {
        var count = <h:outputText value="#{CartActionBean.count}"/>;
        var flag = false;
    var first = document.getElementById("cartFormId:checkboxId");
    if (first != null) {
      if (first.checked) {
        flag = true;
      } else {
        for (i=1;i<count;i++) {
          var element = document.getElementById('cartFormId:checkboxIdj_id_' + i);
          if (element.checked) {
            flag = true;
            break;
          }
        }
      }
    }
    if (flag) {
      return confirm("Are you sure?");
    }
    return true;
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
          <a name="evs-content" id="evs-content" tabindex="0"></a>
          <h:form id="cartFormId">
          <table border="0" class="datatable_960">
            <tr>
              <td width="200px">
              <table border="0" width="100%" role='presentation'>
                <tr>
                  <td class="texttitle-blue" width="40">Cart</td>
                  <td class="texttitle-gray">(<h:outputText value="#{CartActionBean.count}"/>)</td>
                  <td class="texttitle-gray">
                  <h:commandLink value="Exit Cart" onclick="backButton();return false;" title="Return to previous screen" styleClass="texttitle-blue-small"/>
                  </td>
                </tr>
              </table>
              </td>
            <td align="right" valign="bottom" nowrap>
              <h:commandLink action="#{CartActionBean.selectAllInCart}" styleClass="texttitle-blue-small">
                <h:graphicImage value="../images/selectall.gif" alt="Select All" title="Select all concepts" style="border: none" />
              </h:commandLink>&nbsp;
              <h:commandLink action="#{CartActionBean.unselectAllInCart}" styleClass="texttitle-blue-small">
                <h:graphicImage value="../images/clearselections.gif" alt="Unselect" title="Unselect all concepts" style="border: none" />
              </h:commandLink>&nbsp;
              <h:commandLink action="#{CartActionBean.removeFromCart}" styleClass="texttitle-blue-small" onclick="return confirmRemoveMessage();">
                <h:graphicImage value="../images/remove.gif" alt="Remove" title="Remove concepts from the cart" style="border: none" />
              </h:commandLink>&nbsp;
              <h:commandLink action="#{CartActionBean.exportCartXML}" styleClass="texttitle-blue-small">
                <h:graphicImage value="../images/exportxml.gif" alt="Export XML" title="Export cart contents in LexGrid XML format" style="border: none" />
              </h:commandLink>&nbsp;
              <h:commandLink action="#{CartActionBean.exportCartCSV}" styleClass="texttitle-blue-small">
                <h:graphicImage value="../images/exportcsv.gif" alt="Export CSV" title="Generate a list of cart concepts in CSV format readable from Excel" style="border: none" />
              </h:commandLink>
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
          <c:forEach var="item" begin="0" items="#{CartActionBean.concepts}" varStatus="status">
          <c:choose>
            <c:when test="${status.index % 2 == 0}">
              <tr class="dataRowDark">
            </c:when>
            <c:otherwise>
              <tr class="dataRowLight">
            </c:otherwise>
            </c:choose>
              <td><h:selectBooleanCheckbox id="checkboxId" binding="#{item.checkbox}" onclick="submit()"/></td>
              <td>
<h:outputLabel value="#{item.name}" for="checkboxId" />                <!--
                <h:outputLabel value="#{item.name}" for="checkboxId" />
                <h:outputLink value="#{item.url}">${item.name}</h:outputLink>
                -->
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
      <div class="mainbox-bottom"><img src="<%=basePath%>/images/mainbox-bottom.gif" width="962" height="5" alt="Mainbox Bottom" /></div>
      <!-- end Main box -->
    </div> <!-- end center-page -->
</f:view>
<script type="text/javascript">_satellite.pageBottom();</script>
</body>
</html>
