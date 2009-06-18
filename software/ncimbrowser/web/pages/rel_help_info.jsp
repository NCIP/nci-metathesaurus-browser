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
        <%
          Vector abbr_vec = new Vector();
          Vector def_vec = new Vector();
          abbr_vec.add("AOD2000");
          abbr_vec.add("AOT2003");
          abbr_vec.add("BioC_0812D");
          abbr_vec.add("CBO2007_06");
          abbr_vec.add("CCS2005");
          abbr_vec.add("CDC_0812D");
          abbr_vec.add("CDISC_0812D");
          abbr_vec.add("CDT5");
          abbr_vec.add("COH_0812D");
          abbr_vec.add("COSTAR_89-95");
          abbr_vec.add("CRCH_0812D");
          abbr_vec.add("CSP2006");
          abbr_vec.add("CST95");
          abbr_vec.add("CTCAE_0812D");
          abbr_vec.add("DTP_0812D");
          abbr_vec.add("DXP94");
          abbr_vec.add("ELC2001");

          def_vec.add("Alcohol and Other Drug Thesaurus, 2000");
          def_vec.add("Authorized Osteopathic Thesaurus, 2003");
          def_vec.add("Based on BioCarta online maps of molecular relationships, adapted for NCI use., 0812D");
          def_vec.add("Cerner Clinical Bioinformatics Ontology, June 2007");
          def_vec.add("Clinical Classifications Software, 2005");
          def_vec.add("U.S. Centers for Disease Control and Prevention, 0812D");
          def_vec.add("Clinical Data Interchange Standards Consortium, 0812D");
          def_vec.add("Current Dental Terminology 2005 (CDT-5), 5");
          def_vec.add("City of Hope, 0812D");
          def_vec.add("COSTAR, 1989-1995");
          def_vec.add("US State Department country code");
          def_vec.add("Cancer Research Center of Hawaii, 0812D");
          def_vec.add("CRISP Thesaurus, 2006");
          def_vec.add("COSTART, 1995");
          def_vec.add("Common Terminology Criteria for Adverse Events, 0812D");
          def_vec.add("Cancer Therapy Evaluation Program (CTEP), 2004");
          def_vec.add("Division of Cancer Prevention Program, 0812D");
        %>
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
                <b>Source Help Information</b>
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
The NCI Metathesaurus includes the following sources,
with the NCI Local Sources and Sub-Sources shown in red.
</p>
<p>
Certain vocabularies, such as ICD-10 and ICD-O3,
are made available, by permission, for NON-COMMERCIAL USE ONLY.  
</p>
<p>
MedDRA License Agreement.  The Medical Dictionary for Regulatory Activities (MedDRA) terminology is a licensed terminology.  
The use of MedDRA from this NCI website is licensed for NCI employees and contractors performing work for NCI.  
All other use is prohibited.  Users who are NOT NCI employees or contractors performing work for NCI must subscribe to 
MedDRA from the MedDRA MSSO.  For questions regarding MedDRA, please contact the MSSO at mssohelp@ngc.com, or visit 
the MSSO website at www.meddramsso.com, or write the MSSO at 12011 Sunset Hills Road Reston Virginia, 20190-3285 or phone 877.258.8280.
</p>      

            
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

        </div>
        <!-- End of Term Type content -->
      </div>
  </div>
  </f:view>
  </body>
</html>