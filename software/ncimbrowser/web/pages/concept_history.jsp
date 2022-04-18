<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page import="java.util.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="org.LexGrid.concepts.*" %>
<%@ page import="org.apache.logging.log4j.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String basePath = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core"> 
  <head>
<script src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js"></script>
    <title>NCI Metathesaurus History</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
<script>(function(i,s,o,g,r,a,m){i["GoogleAnalyticsObject"]=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,"script","//www.google-analytics.com/analytics.js","ga");ga("create", "UA-150112876-2", {"cookieDomain":"auto"});ga("send", "pageview");</script>
  </head>
  <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
  <%!
      private static Logger _logger = LogManager.getLogger("concept_history.jsp");
  %>
  <%
    String code = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("code"));
    code = HTTPUtils.cleanXSS(code);

    String dictionary = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
    dictionary = HTTPUtils.cleanXSS(dictionary);

_logger.debug("concept_history.jsp " + dictionary);

    String vers = null;
    String ltag = null;
    Concept concept = (Concept) request.getSession().getAttribute("concept");
    if (concept == null) {
        concept = DataUtils.getConceptByCode(dictionary, vers, ltag, code);
    } else {
        request.getSession().setAttribute("concept", concept);
    }
    String msg = null;
    if (concept == null) {
           msg = "ERROR: Invalid code.";
    } else {
           msg = "ERROR: Unable to generate the requested page.";
    }
  %>
  <f:view>
  <%
    if (concept == null) {
   %>
      <div class="textbody">
          <%=msg%>
      </div>
  <%
    } else {
      Vector rows = HistoryUtils.getEditActions(dictionary, vers, ltag, code);

_logger.debug("concept_history.jsp rows " + rows.size());


      String concept_name = concept.getEntityDescription().getContent();
      Vector headers = HistoryUtils.getTableHeader();
  %>
  <div id="popupContainer">
      <!-- nci popup banner -->
      <div class="ncipopupbanner">
        <a href="http://www.cancer.gov" target="_blank" rel="noopener"  alt="National Cancer Institute"><img src="<%=basePath%>/images/banner-red.png" width="680" height="39" border="0" alt="National Cancer Institute" /></a>
        <a href="http://www.cancer.gov" target="_blank" rel="noopener"  alt="National Cancer Institute"><img src="<%=basePath%>/images/spacer.gif" width="60" height="39" border="0" alt="National Cancer Institute" class="print-header" /></a>
      </div>
      <!-- end nci popup banner -->
      <div id="popupMainArea">
        <table class="evsLogoBg" cellspacing="0" cellpadding="0" border="0" role='presentation'>
        <tr>
          <td valign="top">
            <a href="http://evs.nci.nih.gov/" target="_blank" rel="noopener"  alt="Enterprise Vocabulary Services">
              <img src="<%=basePath%>/images/evs-popup-logo.gif" width="213" height="26" alt="EVS: Enterprise Vocabulary Services" title="EVS: Enterprise Vocabulary Services" border="0" />
            </a>
          </td>
          <td valign="top"><div id="closeWindow"><a href="javascript:window.close();"><img src="<%=basePath%>/images/thesaurus_close_icon.gif" width="10" height="10" border="0" alt="Close Window" />&nbsp;CLOSE WINDOW</a></div></td>
        </tr>
        </table>
        <div><img src="<%=basePath%>/images/thesaurus_popup_banner.gif" width="612" height="56" alt="NCI Metathesaurus" title="" border="0" /></div>
        <div id="popupContentArea">
          <!-- History content -->
          <div class="pageTitle"><b><%=DataUtils.encodeTerm(concept_name)%> (CUI <%=code%>)</b></div>
          <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
            <tr class="textbody">
              <td align="left" class="texttitle-gray">
                History
              </td>
              <td align="right">
                <font size="1" color="red" align="right">
                  <a href="javascript:printPage()"><img src="<%= request.getContextPath() %>/images/printer.bmp" border="0" alt="Send to Printer"><i>Send to Printer</i></a>
                </font>
              </td>
            </tr>
          </table>
          <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
            <tr>
              <%
                for (int i=0; i<headers.size(); ++i) {
                  Object header = headers.elementAt(i);
              %>
                  <th class="dataTableHeader" scope="col" align="left"><%=header%></th>
              <%
                }
              %>
            </tr>
            <%
              for (int i=0; i<rows.size(); ++i) {
                String rowColor = (i%2 == 0) ? "dataRowDark" : "dataRowLight";
            %>
                <tr class="<%=rowColor%>">
            <%
                String row = (String) rows.elementAt(i);
                Vector cols = DataUtils.parseData(row, "|");
                for (int j=0; j<cols.size(); ++j) {
                  Object cell = cols.elementAt(j);
                  String iFormat = "", iFormatEnd = "";
                  if (j==0 || j==2)
                    { iFormat = "<i>"; iFormatEnd = "</i>"; }
                  %>
                    <td class="dataCellText"><%=iFormat%><%=cell%><%=iFormatEnd%></td>
                  <%
                }
            %>
                </tr>
            <%
              }
            %>
            <tr><td height="10px"></td></tr>
          </table>
          <!-- End of history content -->
        </div>
      </div>
    </div>
    <%
    }
    %>
  </f:view>
<script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>
