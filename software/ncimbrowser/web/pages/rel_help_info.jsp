<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Vector"%>

<%@ page import="gov.nih.nci.evs.browser.common.Constants"%>
<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils"%>

<%
  String basePath = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core"> 
  <head>
<script src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js"></script>
<!-- Google tag (gtag.js) -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-N0G7WV400Q"></script>
<script>
	window.dataLayer = window.dataLayer || [];
	function gtag(){dataLayer.push(arguments);}
	gtag('js', new Date());
	gtag('config', 'G-N0G7WV400Q');
</script>
    <script src="https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js"></script>
    <title>Relationship Help Informaton</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  </head>
  <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
  <header class="flex-grow-0">
	<include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html"></include-html>
  </header>
  <f:view>
    <!-- Begin Skip Top Navigation -->
      <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">skip navigation links</a>
    <!-- End Skip Top Navigation -->
  <div id="popupContainer">
      <!-- nci popup banner -->
      <div class="ncipopupbanner">
        <a href="https://www.cancer.gov" target="_blank" rel="noopener"  alt="National Cancer Institute"><img src="<%=basePath%>/images/banner-red.png" width="680" height="39" border="0" alt="National Cancer Institute" /></a>
        <a href="https://www.cancer.gov" target="_blank" rel="noopener"  alt="National Cancer Institute"><img src="<%=basePath%>/images/spacer.gif" width="60" height="39" border="0" alt="National Cancer Institute" class="print-header" /></a>
      </div>
      <!-- end nci popup banner -->
      <div id="popupMainArea">
        <a name="evs-content" id="evs-content" tabindex="0"></a>
        <table class="evsLogoBg" cellspacing="3" cellpadding="0" border="0" width="570px" role='presentation'>
        <tr>
          <td valign="top">
            <a href="https://evs.nci.nih.gov/" target="_blank" rel="noopener"  alt="Enterprise Vocabulary Services">
              <img src="<%=basePath%>/images/evs-popup-logo.gif" width="213" height="26" alt="EVS: Enterprise Vocabulary Services" title="EVS: Enterprise Vocabulary Services" border="0" />
            </a>
          </td>
          <td valign="top"><div id="closeWindow"><a href="javascript:window.close();"><img src="<%=basePath%>/images/thesaurus_close_icon.gif" width="10" height="10" border="0" alt="Close Window" />&nbsp;CLOSE WINDOW</a></div></td>
        </tr>
        </table>
        <div><img src="<%=basePath%>/images/thesaurus_popup_banner.gif" width="612" height="56" alt="NCI Thesaurus" title="" border="0" /></div>
        <div id="popupContentArea">
          <!-- Term Type content -->
          <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
            <tr class="pageTitle">
              <td align="left">
                <b>NCIm Relationships</b>
              </td>
              <td align="right">
                <font size="1" color="red" align="right">
                  <a href="javascript:printPage()"><img src="<%= request.getContextPath() %>/images/printer.bmp" border="0" alt="Send to Printer"><i>Send to Printer</i></a>
                </font>
              </td>
            </tr>
          </table>
          <hr/>
          <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
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
                These relationships reflect assertions each source makes about relationships between its own terms, and occasionally
                between its own terms and terms from other sources.  The browser Relationships tab presents these relationships at
                the NCIm concept level: Based on all terms in the current concept, it displays relationships pointing to other NCIm
                concepts and shows the source that asserts each  relationship.  The By Source tab shows the specific term being
                pointed to by relationships from the selected source, including that term's source (sometimes different), type,
                and code.  Depending on user feedback, a future browser release may show as well the source-specific term from the
                current concept for which the relationship was asserted.  The browser omits relationships asserted between two terms
                in the same concept (such terms are seen together with their term types in the concept's Synonyms listing).
              </p>
              <p>
                <b>The Relationship (REL)</b>labels in NCIm are listed below, grouped by the broader categories used to report them:
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

        </div>
        <!-- End of Term Type content -->
      </div>
  </div>
  </f:view>
  <br>
<script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>
