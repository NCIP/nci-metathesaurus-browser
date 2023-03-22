<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="java.util.Vector"%>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>

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
    <title>NCIm Term Types</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  </head>
  <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
  <f:view>

  <div id="popupContainer">
      <!-- nci popup banner -->
      <!--
      <div class="ncipopupbanner">
      -->
      <div style='clear:both;margin-top:-5px;padding:8px;height:32px;color:white;background-color:#C31F40'>

        <a href="https://www.cancer.gov" target="_blank" rel="noopener"  alt="National Cancer Institute"><img src="<%=basePath%>/images/banner-red.png" width="680" height="39" border="0" alt="National Cancer Institute" /></a>
        <a href="https://www.cancer.gov" target="_blank" rel="noopener"  alt="National Cancer Institute"><img src="<%=basePath%>/images/spacer.gif" width="48" height="39" border="0" alt="National Cancer Institute" class="print-header" /></a>
      </div>
      <!-- end nci popup banner -->
      <div id="popupMainArea">
        <%
          Vector abbr_vec = new Vector();
          Vector def_vec = new Vector();
          Vector v = (Vector) request.getSession().getAttribute("TermTypeMetaData");
          if (v == null) {
            v = NCImMetadataUtils.getTermTypeDescriptionMetaData("NCI Metathesaurus", null);
            if (v != null) {
                request.getSession().setAttribute("TermTypeMetaData", v);
            } else {
                v = new Vector();
            }
          }

    for (int i=0; i<v.size(); i++) {
       String t = (String) v.elementAt(i);
       Vector w = DataUtils.parseData(t);
       abbr_vec.add((String) w.elementAt(0));
       def_vec.add((String) w.elementAt(1));
    }


        %>
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
                <b>NCIm Term Types</b>
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
NCI Metathesaurus Term Types use short abbreviations to code the nature of each term associated with a concept. Original source term types are often preserved, but are sometimes changed so that the same meaning is coded consistently across all sources.  Here is a listing of the term type codes and their meanings:
            </td></tr>
          </table>
          <br/>
          <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
            <%
              for (int n=0; n<abbr_vec.size(); n++) {
              String abbr = (String) abbr_vec.elementAt(n);
              String def = (String) def_vec.elementAt(n);
              String rowColor = (n%2 == 0) ? "dataRowDark" : "dataRowLight";
            %>
              <tr class="<%=rowColor%>">
                <td><%=abbr%></td>
                <td>&nbsp;</td>
                <td><%=def%></td>
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
<script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>
