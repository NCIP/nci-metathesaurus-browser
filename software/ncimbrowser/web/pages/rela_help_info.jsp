<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Vector"%>

<%@ page import="gov.nih.nci.evs.browser.common.Constants"%>
<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils"%>

<%
  String basePath = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>
    <title>Relationship Help Informaton</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  </head>
  <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
  <f:view>
  <div id="popupContainer">
      <!-- nci popup banner -->
      <div class="ncipopupbanner">
        <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute"><img src="<%=basePath%>/images/nci-banner-1.gif" width="440" height="39" border="0" alt="National Cancer Institute" /></a>
        <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute"><img src="<%=basePath%>/images/spacer.gif" width="48" height="39" border="0" alt="National Cancer Institute" class="print-header" /></a>
      </div>
      <!-- end nci popup banner -->
      <div id="popupMainArea">
        <table class="evsLogoBg" cellspacing="3" cellpadding="0" border="0" width="570px">
        <tr>
          <td valign="top">
            <a href="http://evs.nci.nih.gov/" target="_blank" alt="Enterprise Vocabulary Services">
              <img src="<%=basePath%>/images/evs-popup-logo.gif" width="213" height="26" alt="EVS: Enterprise Vocabulary Services" title="EVS: Enterprise Vocabulary Services" border="0" />
            </a>
          </td>
          <td valign="top"><div id="closeWindow"><a href="javascript:window.close();"><img src="<%=basePath%>/images/thesaurus_close_icon.gif" width="10" height="10" border="0" alt="Close Window" />&nbsp;CLOSE WINDOW</a></div></td>
        </tr>
        </table>
        <div><img src="<%=basePath%>/images/thesaurus_popup_banner.gif" width="612" height="56" alt="NCI Thesaurus" title="" border="0" /></div>
        <div id="popupContentArea">
          <!-- Term Type content -->
          <table width="580px" cellpadding="3" cellspacing="0" border="0">
            <tr class="pageTitle">
              <td align="left">
                <b>Relationship Attribute Help Information</b>
              </td>
              <td align="right">
                <font size="1" color="red" align="right">
                  <a href="javascript:printPage()"><img src="<%= request.getContextPath() %>/images/printer.bmp" border="0" alt="Send to Printer" ><i>Send to Printer</i></a>
                </font>
              </td>
            </tr>
          </table>
          <hr/>
          <table width="580px" cellpadding="3" cellspacing="0" border="0">
            <tr class="textbody"><td align="left">
            
<p> 
Relationship information in the NCI Metathesaurus is represented by two interconnected sets of descriptions:
<ul>
<li>
<b>Relationship (REL)</b>: A Metathesaurus relationship label, providing a consistent set of codes for all relationships from all sources.
</li>
<li>
<b>Relationship Attribute (RELA)</b>: The more specific label, when available, used by individual sources to represent relationships.
</li>
</ul>
These data are not consistently stored and linked in the current LexBIG database and LexEVS 5.0 interface, so this browser release presents relationships in a smaller set of categories that can be applied fairly reliably, and presents RELA data where available with the caveat that it is seriously incomplete.  The  next browser release will completely report both sets of values, as asserted by individual sources. 
</p>
<p>
<b>The Relationship (REL)</b> labels in NCIm are listed below, grouped by the broader categories used to report them in the initial release of the NCIm Browser:
</p>            
            </td></tr>
          </table>
          <br/>
          
          <table width="580px" cellpadding="3" cellspacing="0" border="0">
             <th class="dataTableHeader" scope="col" align="left">Browser Category</th>
             <th class="dataTableHeader" scope="col" align="left">REL Code</th>
             <th class="dataTableHeader" scope="col" align="left">Description</th>
         
            <%
              List list = Constants.REL;
              for (int n=0; n<list.size(); n++) {
                 String t = (String) list.get(n);
                 Vector v = DataUtils.parseData(t, "\t");
                 String col1 = (String) v.elementAt(0);
                 String col2 = (String) v.elementAt(1);
                 String col3 = (String) v.elementAt(2);
                 String rowColor = (n%2 == 0) ? "dataRowDark" : "dataRowLight";
            %>
              <tr class="<%=rowColor%>">
                <td><%=col1%></td>
                <td><%=col2%></td>
                <td><%=col3%></td>
              </tr>
            <%
              }
            %>
          </table>
          <br/>

          <table width="580px" cellpadding="3" cellspacing="0" border="0">
            <tr class="textbody"><td align="left">
            

<p>
<b>The Relationship Attribute (RELA)</b> categories in NCIm are taken from individual sources, which define their meanings.  
Below is a chart showing all REL and RELA pairs found in the December 2008 version of NCIm, grouped as above by browser category:
</p>            
            </td></tr>
          </table>
          <br/>
          
          <table width="580px" cellpadding="3" cellspacing="0" border="0">
             <th class="dataTableHeader" scope="col" align="left">Browser Category</th>
             <th class="dataTableHeader" scope="col" align="left">REL Code</th>
             <th class="dataTableHeader" scope="col" align="left">Description</th>
         
            <%
              List list2 = Constants.RELA;
              for (int n=0; n<list2.size(); n++) {
                 String t = (String) list2.get(n);
                 Vector v = DataUtils.parseData(t, "\t");
                 String col1 = (String) v.elementAt(0);
                 String col2 = (String) v.elementAt(1);
                 String col3 = (String) v.elementAt(2);
                 String rowColor = (n%2 == 0) ? "dataRowDark" : "dataRowLight";
            %>
              <tr class="<%=rowColor%>">
                <td><%=col1%></td>
                <td><%=col2%></td>
                <td><%=col3%></td>
              </tr>
            <%
              }
            %>
          </table>
          <br/>

        </div>
        <!-- End of Term Type content -->
      </div>
  </div>
  </f:view>
  </body>
</html>