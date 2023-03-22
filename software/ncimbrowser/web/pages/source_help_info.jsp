<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="java.util.Vector"%>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.common.*" %>

<%
  String basePath = request.getContextPath();
  String ncit_url = new DataUtils().getNCItURL();
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
    <title>NCIm Sources</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  </head>
  <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
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
          <a name="evs-content" id="evs-content" tabindex="0"></a>
          <!-- Source content -->
          <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
            <tr class="pageTitle">
              <td align="left">
                <b>NCIm Sources</b>
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
The NCI Metathesaurus (NCIm) includes the sources listed below,
some of which are proprietary and included, by permission, for non-commercial use only.
<ul>
  <li>
    NCIm is built using the National Library of Medicine's UMLS Metathesaurus, use of which is restricted
    under the UMLS license agreement
    (see <a href="https://www.nlm.nih.gov/research/umls/license.html" target="_blank" rel="noopener" >
    https://www.nlm.nih.gov/research/umls/license.html</a>).
  </li>
  <li>
    The World Health Organization allows use of ICD-10 and ICD-O-3 in NCI Enterprise Vocabulary Services, but
    requires licensing for other purposes
    (see <a href="http://www.who.int/classifications/icd/en/" target="_blank" rel="noopener" >http://www.who.int/classifications/icd/en/</a>).
  </li>

  <li>
    The International Organization for Standardization (ISO) allows reference use of its 3166-2 codes in NCI Enterprise Vocabulary Services, 
    but requires licensing for other purposes
    (see <a href="http://www.iso.org/iso/home/standards/country_codes.htm" target="_blank" rel="noopener" >http://www.iso.org/iso/home/standards/country_codes.htm</a>).
  </li>

  <li>
    The International Health Terminology Standards Development Organisation (IHTSDO) allows use of SNOMED CT in
    NCI Enterprise Vocabulary Services, but requires licensing for other purposes.
    Terms of use for SNOMED CT are contained in Appendix 2, SNOMED CT Affiliate Licence Agreement, of the License for Use of the UMLS Metathesaurus.
    (see <a href="https://www.nlm.nih.gov/research/umls/knowledge_sources/metathesaurus/release/license_agreement_snomed.html" target="_blank" rel="noopener"  alt="SNOMED CT License">SNOMED CT license agreement</a>).
  </li>

  <li>
    The Medical Dictionary for Regulatory Activities (MedDRA) terminology is licensed for NCI work and may be
    viewed on NCI browsers. All other uses are prohibited, unless covered by separate subscription to MedDRA
    from the MedDRA MSSO
    (see <a href="http://www.meddra.org" target="_blank" rel="noopener" >http://www.meddra.org</a>
    or contact at mssohelp@meddra.org, 877.258.8280, or 7575 Colshire Drive McLean, VA 22102).
  </li>
</ul>
</p>


<p>
Additional information on many of these sources can be found on related pages.
<ul>

  <li>
    NCI Term Browser sources are described in more detail
    at: <a href="<%=ncit_url%>pages/source_help_info-termbrowser.jsf" target="_blank" rel="noopener" ><%=ncit_url%>pages/source_help_info-termbrowser.jsf</a>
  </li>

  <li>
    Sources with tagged content in NCI Thesaurus are listed at:
    <a href="<%=ncit_url%>pages/source_help_info.jsf?dictionary=NCI%20Thesaurus" target="_blank" rel="noopener" ><%=ncit_url%>pages/source_help_info.jsf?dictionary=NCI%20Thesaurus</a>
  </li>

  <li>
    UMLS Metathesaurus sources are described in detail at:
    <a href="https://www.nlm.nih.gov/research/umls/sourcereleasedocs/index.html" target="_blank" rel="noopener" >https://www.nlm.nih.gov/research/umls/sourcereleasedocs/index.html</a>
  </li>

  <li>
    Many are described at: 
    <a href="https://wiki.nci.nih.gov/x/FgjrAw" target="_blank" rel="noopener" >https://wiki.nci.nih.gov/x/FgjrAw</a>
  </li>
  
  <li>
    Statistical profiles of sources included in NCI Thesaurus and NCI Metathesaurus are
    at: <a href="https://wiki.nci.nih.gov/display/EVS/10+-+Shared+Terminology+Development" target="_blank" rel="noopener" >https://wiki.nci.nih.gov/display/EVS/10+-+Shared+Terminology+Development</a>
  </li>
</ul>
</p>


            </td></tr>
          </table>
          <br/>
          <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
             <tr class="dataRowDark">
               <th scope="col" align="left">Source</th>
               <th scope="col" align="left">Description</th>
             </tr>
          
            <%
              String propertyName = "son";//"formalName";
              //Vector abbr_vec = new NCImMetadataUtils().getMetadataForCodingSchemes(Constants.CODING_SCHEME_NAME, propertyName);
              Vector abbr_vec = NCImMetadataUtils.getMetadataForCodingSchemes();
              String prev_t = "";
              for (int n=0; n<abbr_vec.size(); n++) {
                 String t = (String) abbr_vec.elementAt(n);
                 if (t.compareTo(prev_t) != 0) {
       Vector w = DataUtils.parseData(t, "|");
       String abbr = (String) w.elementAt(0);
       String def = (String) w.elementAt(1);
       String rowColor = (n%2 == 0) ? "dataRowLight" : "dataRowDark";
        %>
          <tr class="<%=rowColor%>">
	      <td><%=abbr%></td>
	      <td><%=def%></td>
          </tr>
        <%
        prev_t = t;
                 }
              }
            %>
          </table>
          <br/>

        </div>
        <!-- End of Source content -->
      </div>
  </div>
  </f:view>
<script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>
