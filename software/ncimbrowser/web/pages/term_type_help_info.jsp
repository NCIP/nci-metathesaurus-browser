<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%
  String basePath = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>
    <title>Term Type Help Informaton</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  </head>
  <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
  <f:view>
  <div id="popupContainer">
      <!-- nci popup banner -->
      <div class="ncipopupbanner"><a href="http://www.cancer.gov"><img src="<%=basePath%>/images/nci-banner-1.gif" width="440" height="39" border="0" alt="National Cancer Institute" /></a><a href="http://www.cancer.gov"><img src="<%=basePath%>/images/spacer.gif" width="48" height="39" border="0" alt="National Cancer Institute" class="print-header" /></a></div>
      <!-- end nci popup banner -->
      <div id="popupMainArea">
        <%
          Vector abbr_vec = new Vector();
          Vector def_vec = new Vector();
          abbr_vec.add("AB");
          abbr_vec.add("AD");
          abbr_vec.add("AQ<sup>*</sup>");
          abbr_vec.add("AQS");
          abbr_vec.add("BR");
          abbr_vec.add("CA2");
          abbr_vec.add("CA3");
          abbr_vec.add("CNU");
          abbr_vec.add("CI");
          abbr_vec.add("CN");
          abbr_vec.add("CS");
          abbr_vec.add("DN");
          abbr_vec.add("FB");
          abbr_vec.add("HD<sup>*</sup>");
          abbr_vec.add("PT<sup>*</sup>");
          abbr_vec.add("SN");
          abbr_vec.add("SY");

          def_vec.add("Abbreviation");
          def_vec.add("Adjectival form (and other parts of grammer)");
          def_vec.add("Antiquated preferred term");
          def_vec.add("Antiquated term, use when there are antiquated synonyms within a concept");
          def_vec.add("US brand name");
          def_vec.add("ISO 3166 alpha-2 country code");
          def_vec.add("ISO 3166 alpha-3 country code");
          def_vec.add("ISO 3166 numeric country code");
          def_vec.add("IO coutry code (deprecated)");
          def_vec.add("Drug study code");
          def_vec.add("US State Department country code");
          def_vec.add("Display name");
          def_vec.add("Foreign brand name");
          def_vec.add("Header (groups concepts, but not used for coding data)");
          def_vec.add("Preferred term");
          def_vec.add("Chemical structure name");
          def_vec.add("Synonym");
        %>
        <table class="evsLogoBg" cellspacing="3" cellpadding="0" border="0" width="570px">
        <tr>
          <td valign="top"><img src="<%=basePath%>/images/evs-popup-logo.gif" width="213" height="26" alt="EVS: Enterprise Vocabulary Services" title="EVS: Enterprise Vocabulary Services" border="0" /></td>
          <td valign="top"><div id="closeWindow"><a href="javascript:window.close();"><img src="<%=basePath%>/images/thesaurus_close_icon.gif" width="10" height="10" border="0" alt="Close Window" />&nbsp;CLOSE WINDOW</a></div></td>
        </tr>
        </table>
        <div><img src="<%=basePath%>/images/thesaurus_popup_banner.gif" width="612" height="56" alt="NCI Thesaurus" title="" border="0" /></div>
        <div id="popupContentArea">
          <!-- Term Type content -->
          <table width="580px" cellpadding="3" cellspacing="0" border="0">
            <tr class="pageTitle">
              <td align="left">
                <b>Term Type Help Information</b>
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
              NCI Thesaurus Term Types use 2- or 3-character abbreviations to code the nature of each term associated with a concept. Here is a listing of the term type codes and their meanings:
            </td></tr>
          </table>
          <br/>
          <table width="580px" cellpadding="3" cellspacing="0" border="0">
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
          <table width="580px" cellpadding="3" cellspacing="0" border="0">
            <tr class="textbody"><td align="left">
              *Note on special rules governing NCI PT, HD, and AQ term types:
              Each concept should have one, and only one, term coded with one
              of these three values. The NCI Preferred Term is always taken
              from one of the NCI terms, normally that with a type of PT
              (Preferred Term). However, in special cases, a concept will
              not have a PT term, but instead, will have either an HD (Header)
              term or an AQ term. These tags are considered equivalent to PT
              by the software. This means that a concept may have only as
              single NCI PT, or HD, or AQ term. In those cases where multiple
              antiquated terms are needed for a concept which is itself coded
              as antiquated, one should be tagged AQ and the rest tagged AQS.
            </td></tr>
            <tr><td height="10px"></td></tr>
          </table>
        </div>
        <!-- End of Term Type content -->
      </div>
  </div>
  </f:view>
  </body>
</html>