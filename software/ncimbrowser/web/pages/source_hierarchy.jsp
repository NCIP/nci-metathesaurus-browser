<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.util.Vector" %>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.common.Constants" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="org.apache.logging.log4j.*" %>

<% String basePath = request.getContextPath(); %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <script
        src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js">
    </script>
<!-- Google tag (gtag.js) -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-N0G7WV400Q"></script>
<script>
	window.dataLayer = window.dataLayer || [];
	function gtag(){dataLayer.push(arguments);}
	gtag('js', new Date());
	gtag('config', 'G-N0G7WV400Q');
</script>
    <script src="https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js"></script>
    <title>Vocabulary Hierarchy</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>


<style>
ul {text-align: left;}
li {text-align: left;}
div {text-align: left;}
</style>


    <script type="text/javascript">

        var currOpener = opener;
	function load(url,target) {
		if (target != '')
			target.window.location.href = url;
		else
			window.location.href = url;
	}
      
	function onClickTreeNode(ontology_node_id) {
		var ontology_display_name = document.forms["pg_form"].ontology_display_name.value;
		load('/ncimbrowser/pages/concept_details.jsf?dictionary='+ ontology_display_name
		+ '&code=' + ontology_node_id, currOpener);
	}  
	
	function show_hide_div(div_id) {
		var img_id = "IMG_" + div_id.substring(4, div_id.length);
		var img_obj = document.getElementById(img_id);
		if (img_obj.getAttribute("src").indexOf("minus") != -1) {
			document.getElementById(div_id).style.display = "none";
		} else if (img_obj.getAttribute("src").indexOf("plus") != -1) {
			document.getElementById(div_id).style.display = "block";
		}
		changeImage(img_id);
	}
	
	function show_hide(div_id) {
		var curr_node = document.getElementById(div_id);
		var code = curr_node.getAttribute("code");
		var img_id = "IMG_" + div_id.substring(4, div_id.length);
		var img_obj = document.getElementById(img_id);
		
		if (img_obj.getAttribute("src").indexOf("plus") != -1) {
		        expand_node(div_id, code);
		        changeImage(img_id); 
		} else if (img_obj.getAttribute("src").indexOf("minus") != -1) {
		     var i = 1;
		     var next_div_id = div_id + "_" + i;
		     var next_img_id = img_id + "_" + i;
		     var e = document.getElementById(next_div_id);
		     while (e != null) {
		             document.getElementById(next_img_id).remove();
		             document.getElementById(next_div_id).remove();
			     i = i+1;
			     next_div_id = div_id + "_" + i;
			     next_img_id = img_id + "_" + i;
			     e = document.getElementById(next_div_id);
		     }
		     changeImage(img_id); 
		}
	}

	function changeImage(img_id) {
		var img_obj = document.getElementById(img_id);
		if (img_obj.getAttribute("src").indexOf("minus") != -1) {
			var s = img_obj.getAttribute("src");
			s = s.replace("minus", "plus");
			img_obj.setAttribute("src", s);
		} else if (img_obj.getAttribute("src").indexOf("plus") != -1) {
			var s = img_obj.getAttribute("src");
			s = s.replace("plus", "minus");
			img_obj.setAttribute("src", s);
		}
	} 

	function search(ontology_node_id, ontology_sab, ontology_display_name) {
		var ajax = new XMLHttpRequest();
		ajax.open("GET", '/ncimbrowser/ajax?action=search_tree&ontology_display_name=' + ontology_display_name + '&ontology_sab=' + ontology_sab + '&ontology_node_id=' + ontology_node_id, true);
		ajax.send(); 
		ajax.onreadystatechange = function() {
			if (ajax.readyState == 4 && ajax.status == 200) {
				document.getElementById("tree").innerHTML = this.responseText;
			}
		}
	}

	function initTree() {
		var ontology_node_id = document.forms["pg_form"].ontology_node_id.value;
		var ontology_display_name = document.forms["pg_form"].ontology_display_name.value;
		var ontology_sab = document.forms["pg_form"].ontology_sab.value;
		if (ontology_node_id == null || ontology_node_id == "null")
		{
                   var content = "<center><br></br><img src='/ncimbrowser/images/loading.gif' alt='Loading'/>" + 
                        "<p>Loading hierarchy. Please wait...</p><center>";
		        document.getElementById("tree").innerHTML = content;
			init(ontology_sab, ontology_display_name);
			
		}
		else
		{
                   var content = "<center><br></br><img src='/ncimbrowser/images/loading.gif' alt='Loading'/>" + 
                        "<p>Searching concept in hierarchy. Please wait...</p><center>";
		        document.getElementById("tree").innerHTML = content;
			search(ontology_node_id, ontology_sab, ontology_display_name);
		}
	}
    
	function init(ontology_sab, ontology_display_name) {
		var ajax = new XMLHttpRequest();
		ajax.open("GET", '/ncimbrowser/ajax?action=build_tree&ontology_sab=' +ontology_sab+'&ontology_display_name='+ontology_display_name, true);
		ajax.send();
		ajax.onreadystatechange = function() {
			if (ajax.readyState == 4 && ajax.status == 200) {
				document.getElementById("tree").innerHTML = this.responseText;
			}
		}
	}

	function expand_node(div_id, code) {
                var ontology_display_name = document.forms["pg_form"].ontology_display_name.value;
                var ontology_sab = document.forms["pg_form"].ontology_sab.value;
		var ajax = new XMLHttpRequest();
		ajax.open("GET", '/ncimbrowser/ajax?action=expand_tree&ontology_node_id=' +code +'&ontology_sab='+ontology_sab+'&ontology_display_name='+ontology_display_name+'&id=' + div_id, true);
		ajax.send();
		ajax.onreadystatechange = function() {
			if (ajax.readyState == 4 && ajax.status == 200) {
			     var content = document.getElementById(div_id).innerHTML;
			     document.getElementById(div_id).innerHTML = content + this.responseText;
			}
		}
	}


	</script>
</head>
<body onload="javascript:initTree()">
  <header class="flex-grow-0">
	<div style='text-align: left'>
	<include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html"></include-html>
	</div>
  </header>
  <%!
      private static Logger _logger = LogManager.getLogger("source_hierarchy.jsp");
  %>
  <f:view>
    <!-- Begin Skip Top Navigation -->
        <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">skip navigation links</A>
    <!-- End Skip Top Navigation --> 

<div id="popupContainer">
    
      <!-- nci popup banner -->
      <div class="ncipopupbanner">
        <a href="https://www.cancer.gov" target="_blank" rel="noopener"  alt="National Cancer Institute"><img src="<%=basePath%>/images/banner-red.png" width="680" height="39" border="0" alt="National Cancer Institute" /></a>
        <a href="https://www.cancer.gov" target="_blank" rel="noopener"  alt="National Cancer Institute"><img src="<%=basePath%>/images/spacer.gif" width="48" height="39" border="0" alt="National Cancer Institute" class="print-header" /></a>
      </div>
      <!-- end nci popup banner -->
      <div id="popupMainArea">
        <table class="evsLogoBg" cellspacing="0" cellpadding="0" border="0" role='presentation'>
        <tr>
          <td valign="top">
            <a href="https://evs.nci.nih.gov/" target="_blank" rel="noopener"  alt="Enterprise Vocabulary Services">
              <img src="<%=basePath%>/images/evs-popup-logo.gif" width="213" height="26" alt="EVS: Enterprise Vocabulary Services" title="EVS: Enterprise Vocabulary Services" border="0" />
            </a>
          </td>
          <td valign="top"><div id="closeWindow"><a href="javascript:window.close();"><img src="<%=basePath%>/images/thesaurus_close_icon.gif" width="10" height="10" border="0" alt="Close Window" />&nbsp;CLOSE WINDOW</a></div></td>
        </tr>
        </table>

<%
String ontology_sab = null;
String ontology_formalname = null;
ontology_sab = HTTPUtils.cleanXSS((String)request.getParameter("sab"));
_logger.debug("SAB: " + ontology_sab);

if (ontology_sab == null) {
      Object selectedSource_obj = request.getSession().getAttribute("selectedSource");
      if (selectedSource_obj != null) {
    ontology_sab = (String) selectedSource_obj;
      }
}
ontology_formalname = NCImMetadataUtils.getSABDefinition(ontology_sab);

        if (ontology_sab.compareTo("NCI") == 0) {
%>
           <div><img src="<%=basePath%>/images/thesaurus_popup_banner.gif" width="612" height="56" alt="NCI Thesaurus" title="" border="0" /></div>
<%
        } else {

%>
           <div><%=ontology_formalname%></div>
<%
        }
%>

        <div id="popupContentArea">
          <a name="evs-content" id="evs-content" tabindex="0"></a>
         
          <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
            <tr class="textbody">
              <td class="pageTitle" align="left">

<%
                  if (ontology_sab.compareTo("NCI") == 0) {
%>
                      NCI Thesaurus Hierarchy
<%
                  } else {
%>
                      <%=ontology_sab%> Hierarchy
<%
                  }
%>

              </td>
              <td class="pageTitle" align="right">
               
                <font size="1" color="red" align="right">
                  <a href="javascript:printPage()"><img src="<%= request.getContextPath() %>/images/printer.bmp" border="0" alt="Send to Printer"><i>Send to Printer</i></a>
                </font>
              </td>
            </tr>
          </table>
          <!-- Tree content -->
          <div id="rootDesc">
            <div id="bd"></div>
            <div id="ft"></div>
          </div>
          <div id="treeStatus">
            <div id="bd"></div>
          </div>
          <div id="emptyRoot">
            <div id="bd"></div>
          </div>
          <div id="treecontainer"></div>
          <form id="pg_form">
            <%
              String ontology_node_id = HTTPUtils.cleanXSS((String)request.getParameter("code"));

            %>
            <input type="hidden" id="ontology_node_id" name="ontology_node_id" value="<%=ontology_node_id%>" />
            <%
              String ontology_display_name = HTTPUtils.cleanXSS((String)request.getParameter("dictionary"));
        if (ontology_display_name == null) ontology_display_name = Constants.CODING_SCHEME_NAME;//"NCI Metathesaurus";

        //hidden variables
        //String licenseAgreementAccepted_str = new Boolean(licenseAgreementAccepted).toString();
        //String isLicensed_str = new Boolean(isLicensed).toString();
            %>

            <input type="hidden" id="ontology_display_name" name="ontology_display_name" value="<%=ontology_display_name%>" />
            <input type="hidden" id="ontology_sab" name="ontology_sab" value="<%=ontology_sab%>" />

          </form>
          <!-- End of Tree control content -->
        </div>
      </div>
    </div>
  </f:view>
                <div id="status"><div>
		<div id="tree"><div>             
<script type="text/javascript">_satellite.pageBottom();</script>
</body>
</html>
