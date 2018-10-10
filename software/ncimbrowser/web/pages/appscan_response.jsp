<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.properties.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>
<%@ page import="org.apache.log4j.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core">
  <head>
<script src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js"></script>
    <title>NCI Metathesaurus</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css"
      href="/ncimbrowser/css/styleSheet.css" />
    <link rel="shortcut icon"
      href="/ncimbrowser/favicon.ico" type="image/x-icon" />
    <script type="text/javascript"
      src="/ncimbrowser/js/script.js"></script>
    <script type="text/javascript"
      src="/ncimbrowser/js/search.js"></script>
    <script type="text/javascript"
      src="/ncimbrowser/js/dropdown.js"></script>
  </head>

  <body onLoad="document.forms.searchTerm.matchText.focus();">
    <script type="text/javascript"
      src="/ncimbrowser/js/wz_tooltip.js"></script>
    <script type="text/javascript"
      src="/ncimbrowser/js/tip_centerwindow.js"></script>
    <script type="text/javascript"
      src="/ncimbrowser/js/tip_followscroll.js"></script>
    
        <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">skip navigation links</A>
      

<%
String application_version = new DataUtils().getApplicationVersion();
String lexevs_version = new DataUtils().getLexVersion();
String ncit_url = new DataUtils().getNCItURL();
%>

<div style='clear:both;margin-top:-5px;padding:8px;height:32px;color:white;background-color:#C31F40'>
  <a href="http://www.cancer.gov" target="_blank">
    <img src="<%=request.getContextPath()%>/images/banner-red.png"
      width="955" height="39" border="0"
      alt="National Cancer Institute"/>
  </a>
  <!--
<div class="ncibanner">
  <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute">
    <img src="/ncimbrowser/images/logotype.gif"
      width="556" height="39" border="0"
      alt="National Cancer Institute" />
  </a>
  <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute">
    <img src="/ncimbrowser/images/spacer.gif"
      width="60" height="39" border="0" 
      alt="National Cancer Institute" class="print-header" /></a>
  <a href="http://www.nih.gov" target="_blank" alt="U.S. National Institutes of Health">
    <img src="/ncimbrowser/images/tagline_nologo.gif"
      width="219" height="39" border="0"
      alt="U.S. National Institutes of Health" />
  </a>
  <a href="http://www.cancer.gov" target="_blank" alt="www.cancer.gov">
    <img src="/ncimbrowser/images/cancer-gov.gif"
      width="125" height="39" border="0"
      alt="www.cancer.gov" />
  </a>
  -->
</div>


      <div class="center-page">
        
<div>
  <a href="http://evs.nci.nih.gov/"
      target="_blank" alt="Enterprise Vocabulary Services">
      
    <img src="/ncimbrowser/images/evs-logo.gif" width="960" height="26" alt="Enterprise Vocabulary Services" border="0"/>
    
  </a>
</div>

<div class="mainbox-top"><img src="/ncimbrowser/images/mainbox-top.gif" width="962" height="5" alt="Box"/></div>


 
      <div id="main-area">
        
<div class="bannerarea">
    <a href="/ncimbrowser" style="text-decoration: none;">
      <div class="vocabularynamebanner">
        <span class="vocabularynamelong">Version <%=application_version%> &#40;using LexEVS <%=lexevs_version%>&#41;</span>
      </div>
    </a>

    <div class="search-globalnav">
        
        <div class="searchbox-top"><img src="/ncimbrowser/images/searchbox-top.gif" width="352" height="2" alt="SearchBox Top" /></div>
        <div class="searchbox">
            





<script type="text/javascript">
  function cursor_wait() {
     document.body.style.cursor = 'wait';
  }

  function disableAnchor(){

    var obj1 = document.getElementById("a_tpTab");
    if (obj1 != null) obj1.removeAttribute('href');

    var obj2 = document.getElementById("a_relTab");
    if (obj2 != null) obj2.removeAttribute('href');

    var obj3 = document.getElementById("a_synTab");
    if (obj3 != null) obj3.removeAttribute('href');

    var obj4 = document.getElementById("a_srcTab");
    if (obj4 != null) obj4.removeAttribute('href');

    var obj5 = document.getElementById("a_allTab");
    if (obj5 != null) obj5.removeAttribute('href');

    var obj6 = document.getElementById("a_hierBut");
    if (obj6 != null) obj6.removeAttribute('href');
  }
  
  
    function onCodeButtonPressed(formname) {
	  var algorithmObj = document.forms[formname].algorithm;
	  for (var j=0; j<algorithmObj.length; j++) {
		  algorithm = algorithmObj[j].value;
		  if (algorithm == "exactMatch") {
			 algorithmObj[j].checked = true;
		  }
	  }
    }

    function getSearchTarget(formname) {
          var searchTargetObj = document.forms[formname].searchTarget;
	  for (var j=0; j<searchTargetObj.length; j++) {
	      if (searchTargetObj[j].checked == true) {
	         return searchTargetObj[j].value;
	      }
	  }
    }

    function onAlgorithmChanged(formname) {
      var curr_target = getSearchTarget(formname);
      if (curr_target != "codes") return;

          var searchTargetObj = document.forms[formname].searchTarget;
	  for (var j=0; j<searchTargetObj.length; j++) {
		  target = searchTargetObj[j].value;
		  if (target == "codes") {
			  searchTargetObj[0].checked = true;
			  return;
		  }
	  }
    }	  
  
  
  
</script>





 
<form id="searchTerm" name="searchTerm" method="post" action="/ncimbrowser/pages/home.jsf;jsessionid=27E6B8FF8E6B0A4E4022E16829F6CFDC" class="search-form" enctype="application/x-www-form-urlencoded" onsubmit="javascript:disableAnchor();">
<input type="hidden" name="searchTerm" value="searchTerm" />

<label for="matchText">Match Text</label>
    <input CLASS="searchbox-input" id="matchText" name="matchText" value="" onFocus="active=true"
        onBlur="active=false"  onkeypress="return submitEnter('searchTerm:search',event)" tabindex="0"/>
<label for="searchTerm:search">Search</label>
    <input id="searchTerm:search" type="image" src="/ncimbrowser/images/search.gif;jsessionid=27E6B8FF8E6B0A4E4022E16829F6CFDC" name="searchTerm:search" accesskey="13" alt="Search" onclick="javascript:cursor_wait();" tabindex="0" class="searchbox-btn" /><a href="/ncimbrowser/pages/help.jsf;jsessionid=27E6B8FF8E6B0A4E4022E16829F6CFDC#searchhelp" tabindex="3"><img src="/ncimbrowser/images/search-help.gif;jsessionid=27E6B8FF8E6B0A4E4022E16829F6CFDC" alt="Search Help" style="border-width:0;" class="searchbox-btn" /></a>

  

  <table border="0" cellspacing="0" cellpadding="0" width="340px" role='presentation'>
    <tr valign="top" align="left">
      <td align="left" class="textbody" colspan="2">
        <input type="radio" name="algorithm" id="algorithm3" value="contains"   alt="Contains"    checked tabindex="0" onclick="onAlgorithmChanged('searchTerm');"><label for="algorithm3">Contains&nbsp;</label>
        <input type="radio" name="algorithm" id="algorithm1" value="exactMatch" alt="Exact Match"  tabindex="0"/><label for="algorithm1">Exact Match&nbsp;</label>
        <input type="radio" name="algorithm" id="algorithm2" value="startsWith" alt="Begins With"  tabindex="0" onclick="onAlgorithmChanged('searchTerm');"><label for="algorithm2">Begins With&nbsp;</label>

      </td>
    </tr>
    <tr align="left">
      <td width="263px" height="1px" bgcolor="#2F2F5F"></td>
      <!-- The following lines are needed to make "Advanced Search" link flush right -->
      
          <td></td>
      
    </tr>

    <tr valign="top" align="left">
      <td align="left" class="textbody" colspan="2">
	<input type="radio" name="searchTarget" id="searchTarget0" value="names"         alt="Name"         checked  tabindex="0">Name&nbsp;
	<input type="radio" name="searchTarget" id="searchTarget1" value="codes"         alt="Code"          tabindex="0" onclick="onCodeButtonPressed('searchTerm');">Code&nbsp;
        <input type="radio" name="searchTarget" id="searchTarget2" value="properties" alt="Properties"  tabindex="0"/><label for="searchTarget2">Property&nbsp;</label>
        <input type="radio" name="searchTarget" id="searchTarget3" value="relationships" alt="Relationships"  tabindex="0"/><label for="searchTarget3">Relationship</label>
      </td>
    </tr>
    <tr><td height="5px;"></td></tr>
    <tr><td colspan="2">
      <table border="0" cellspacing="0" cellpadding="0" width="100%" role='presentation'>
        <tr valign="top">
          <td align="left" class="textbody">
            <label id="searchTerm:sourceLabel" for="searchTerm:source" class="textbody">
Source</label>&nbsp;
            <select id="searchTerm:source" name="searchTerm:source" class="textbody" size="1" onchange="submit();" tabindex="0">	<option value="ALL" selected="selected">ALL</option>
	<option value="AOD">AOD</option>
	<option value="AOT">AOT</option>
	<option value="BioC">BioC</option>
	<option value="BRIDG">BRIDG</option>
	<option value="CARELEX">CARELEX</option>
	<option value="CBO">CBO</option>
	<option value="CCS">CCS</option>
	<option value="CDC">CDC</option>
	<option value="CDISC">CDISC</option>
	<option value="COSTAR">COSTAR</option>
	<option value="CRCH">CRCH</option>
	<option value="CSP">CSP</option>
	<option value="CST">CST</option>
	<option value="CTCAE">CTCAE</option>
	<option value="CTEP">CTEP</option>
	<option value="CTEP-SDC">CTEP-SDC</option>
	<option value="CVX">CVX</option>
	<option value="DCP">DCP</option>
	<option value="DICOM">DICOM</option>
	<option value="DTP">DTP</option>
	<option value="DXP">DXP</option>
	<option value="FDA">FDA</option>
	<option value="FMA">FMA</option>
	<option value="GO">GO</option>
	<option value="HCPCS">HCPCS</option>
	<option value="HGNC">HGNC</option>
	<option value="HL7V3.0">HL7V3.0</option>
	<option value="ICD10">ICD10</option>
	<option value="ICD10CM">ICD10CM</option>
	<option value="ICD10PCS">ICD10PCS</option>
	<option value="ICD9CM">ICD9CM</option>
	<option value="ICDO">ICDO</option>
	<option value="ICH">ICH</option>
	<option value="ICPC">ICPC</option>
	<option value="ISO3166-2">ISO3166-2</option>
	<option value="JAX">JAX</option>
	<option value="KEGG">KEGG</option>
	<option value="LNC">LNC</option>
	<option value="MCM">MCM</option>
	<option value="MDBCAC">MDBCAC</option>
	<option value="MDR">MDR</option>
	<option value="MEDLINEPLUS">MEDLINEPLUS</option>
	<option value="MGED">MGED</option>
	<option value="MSH">MSH</option>
	<option value="MTH">MTH</option>
	<option value="MTHFDA">MTHFDA</option>
	<option value="MTHHH">MTHHH</option>
	<option value="MTHICD9">MTHICD9</option>
	<option value="MTHMST">MTHMST</option>
	<option value="MTHSPL">MTHSPL</option>
	<option value="NCBI">NCBI</option>
	<option value="NCI">NCI</option>
	<option value="NCI-GLOSS">NCI-GLOSS</option>
	<option value="NCI-HL7">NCI-HL7</option>
	<option value="NCIMTH">NCIMTH</option>
	<option value="NCPDP">NCPDP</option>
	<option value="NDFRT">NDFRT</option>
	<option value="NICHD">NICHD</option>
	<option value="NPO">NPO</option>
	<option value="OMIM">OMIM</option>
	<option value="PDQ">PDQ</option>
	<option value="PID">PID</option>
	<option value="PMA">PMA</option>
	<option value="PNDS">PNDS</option>
	<option value="QMR">QMR</option>
	<option value="RADLEX">RADLEX</option>
	<option value="RENI">RENI</option>
	<option value="RXNORM">RXNORM</option>
	<option value="SNOMEDCT_US">SNOMEDCT_US</option>
	<option value="SOP">SOP</option>
	<option value="SPN">SPN</option>
	<option value="SRC">SRC</option>
	<option value="UCUM">UCUM</option>
	<option value="UMD">UMD</option>
	<option value="USPMG">USPMG</option>
	<option value="UWDA">UWDA</option>
	<option value="VANDF">VANDF</option>
	<option value="ZFIN">ZFIN</option>
</select>

    <input type="hidden" name="referer" id="referer" value="N/A" />




   

          </td>
          <td valign="middle" align="right">
            <a class="textbodyredregular" href="/ncimbrowser/pages/advanced_search.jsf">Advanced Search</a>
          </td>

        </tr>
      </table>

    </td></tr>
  </table>
<!--
<input type="hidden" name="javax.faces.ViewState" id="javax.faces.ViewState" value="j_id1:j_id2" />
-->
</form>

        </div>
        <div class="searchbox-bottom"><img src="/ncimbrowser/images/searchbox-bottom.gif" width="352" height="2" alt="SearchBox Bottom" /></div>
       
        <!-- end Search box -->
        <!-- Global Navigation -->
            




<table border="0" width="100%" class="global-nav" role='presentation'>
  <tr>
    <td align="left">
      <a href="/ncimbrowser" tabindex="0">Home</a> |
      <a href="#" onclick="javascript:window.open('/ncimbrowser/pages/source_hierarchy.jsf?&sab=NCI', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');" tabindex="0">
        NCIt Hierarchy</a> |
      <a href="#" onclick="javascript:window.open('/ncimbrowser/pages/source_help_info.jsf',
        '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');" tabindex="0">
        Sources</a> |
      <a href="/ncimbrowser/pages/help.jsf" alt="Help" tabindex="0">Help</a>
    </td>
    <td align="right">
	  	
		
      
    &nbsp;
    </td>
  </tr>
</table>

        <!-- end Global Navigation -->
    </div>
</div>
<!-- end Thesaurus, banner search area -->
<!-- Quick links bar -->

<!--
<div class="bluebar">
-->
<div style='clear:both;padding:3px;height:30px;color:white;background-color:#C31F40'>
  <div id="quicklinksholder">

    <ul id="quicklinks"
        onmouseover="document.quicklinksimg.src='/ncimbrowser/images/quicklinks-active.gif';"
        onmouseout="document.quicklinksimg.src='/ncimbrowser/images/quicklinks-inactive.gif';"
        tabindex="0">

      <li>
        <a href="#">
          <img src="/ncimbrowser/images/quicklinks-inactive.gif" width="162"
            height="18" border="0" name="quicklinksimg" alt="Quick Links" />
        </a>
        <ul>
          <li><a href="http://evs.nci.nih.gov/" target="_blank" alt="EVS">EVS Home</a></li>
          <li><a href="<%=ncit_url%>" target="_blank" alt="EVS">NCI Thesaurus</a></li>
          <!--
          <li><a href="http://ncimeta.nci.nih.gov/MetaServlet/" target="_blank" alt="NCI Metathesaurus">NCI Metathesaurus</a></li>
           -->


          <li><a href="<%=ncit_url%>start.jsf" target="_blank" alt="NCI Term Browser">NCI Term Browser</a></li>

            
              <li><a href="http://ncitermform.nci.nih.gov/ncitermform/?dictionary=NCI%20Metathesaurus" target="_blank" alt="Term Suggestion">Term Suggestion</a></li>
            


          <li><a href="http://www.cancer.gov/cancertopics/terminologyresources" target="_blank" alt="NCI Terminology Resources">NCI Terminology Resources</a></li>
        </ul>
      </li>
    </ul>
  </div>
</div>

<!-- end Quick links bar -->

      <!-- Page content -->
      <div class="pagecontent">
        <a name="evs-content" id="evs-content" tabindex="0"></a>
        
<div class="textbody">

</div>

<%
String error_msg = (String) request.getSession().getAttribute("error_msg");
request.getSession().removeAttribute("error_msg");
%>
<p class="textbodyred">&nbsp;<%=error_msg%></p>

        <!-- footer -->
<div class="footer">
  <ul>
    <li><a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute">NCI Home</a> |</li>
    <li><a href="/ncimbrowser/pages/contact_us.jsf">Contact Us</a> |</i>
    <li><a href="http://www.cancer.gov/policies" target="_blank" alt="National Cancer Institute Policies">Policies</a> |</li>
    <li><a href="http://www.cancer.gov/global/web/policies/page3" target="_blank" alt="National Cancer Institute Accessibility">Accessibility</a> |</li>
    <li><a href="http://www.cancer.gov/global/web/policies/page6" target="_blank" alt="National Cancer Institute FOIA">FOIA</a></li>
  </ul>
  <p>
    A Service of the National Cancer Institute<br />
    <img src="/ncimbrowser/images/external-footer-logos.gif"
      alt="External Footer Logos" width="238" height="34" border="0"
      usemap="#external-footer" />
  </p>
  <map id="external-footer" name="external-footer">
    <area shape="rect" coords="0,0,46,34"
      href="http://www.cancer.gov" target="_blank"
      alt="National Cancer Institute" />
    <area shape="rect" coords="55,1,99,32"
      href="http://www.hhs.gov/" target="_blank"
      alt="U.S. Health &amp; Human Services" />
    <area shape="rect" coords="103,1,147,31"
      href="http://www.nih.gov/" target="_blank"
      alt="National Institutes of Health" />
    <area shape="rect" coords="148,1,235,33"
      href="http://www.usa.gov/" target="_blank"
      alt="USA.gov" />
  </map>
</div>
<!-- end footer -->
</div>
      <!-- end Page content --></div>
      <div class="mainbox-bottom">

        <img src="/ncimbrowser/images/mainbox-bottom.gif" width="962" height="5" alt="Mainbox Bottom" />
       
      </div>
      <!-- end Main box --></div>
    
<script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>
