<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="java.util.*"%>
<%@ page import="org.LexGrid.concepts.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.common.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="org.apache.log4j.*" %>

<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/yahoo-min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/event-min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/dom-min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/animation-min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/container-min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/connection-min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/autocomplete-min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/treeview-min.js"></script>
<%
  String basePath = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core"> 
  <head>
<script src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js"></script>
  <title>Source Hierarchy</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/fonts.css" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/grids.css" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/code.css" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/tree.css" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>

  <script language="JavaScript">

    var tree;
    var nodeIndex;
    var rootDescDiv;
    var emptyRootDiv;
    var treeStatusDiv;
    var nodes = [];
    var currOpener;

    function load(url,target) {
      if (target != '')
        target.window.location.href = url;
      else
        window.location.href = url;
    }

    function init() {

      rootDescDiv = new YAHOO.widget.Module("rootDesc", {visible:false} );
      resetRootDesc();

      emptyRootDiv = new YAHOO.widget.Module("emptyRoot", {visible:true} );
      resetEmptyRoot();

      treeStatusDiv = new YAHOO.widget.Module("treeStatus", {visible:true} );
      resetTreeStatus();

      currOpener = opener;
      initTree();
    }


    function addTreeNode(rootNode, nodeInfo) {
      var newNodeDetails = "javascript:onClickTreeNode('" + nodeInfo.ontology_node_id + "');";
      var newNodeData = { label:nodeInfo.ontology_node_name, id:nodeInfo.ontology_node_id, href:newNodeDetails };
      var newNode = new YAHOO.widget.TextNode(newNodeData, rootNode, false);
      if (nodeInfo.ontology_node_child_count > 0) {
          newNode.setDynamicLoad(loadNodeData);
      }
    }


    function buildTree(ontology_node_id, ontology_display_name) {

      var handleBuildTreeSuccess = function(o) {
        var respTxt = o.responseText;
        var respObj = eval('(' + respTxt + ')');
        if ( typeof(respObj) != "undefined") {
          if ( typeof(respObj.root_nodes) != "undefined") {
            var root = tree.getRoot();
            if (respObj.root_nodes.length == 0) {
              showEmptyRoot();
            }
            else {
              for (var i=0; i < respObj.root_nodes.length; i++) {
                var nodeInfo = respObj.root_nodes[i];
                var expand = false;
                addTreeNode(root, nodeInfo, expand);
              }
            }

            tree.draw();
          }
        }
        resetTreeStatus();
      }

      var handleBuildTreeFailure = function(o) {
        resetTreeStatus();
        resetEmptyRoot();
        alert('responseFailure: ' + o.statusText);
      }

      var buildTreeCallback =
      {
        success:handleBuildTreeSuccess,
        failure:handleBuildTreeFailure
      };

      if (ontology_display_name!='') {
        resetEmptyRoot();

        showTreeLoadingStatus();
        //var ontology_source = ontology_sab;//document.pg_form.ontology_source.value;
        var ontology_source = document.forms["pg_form"].ontology_sab.value;
        var request = YAHOO.util.Connect.asyncRequest('GET','<%= request.getContextPath() %>/ajax?action=build_tree&ontology_node_id=' +ontology_node_id+'&ontology_display_name='+ontology_display_name+'&ontology_source='+ontology_source,buildTreeCallback);

      }
    }

    function resetTree(ontology_node_id, ontology_display_name) {

      var handleResetTreeSuccess = function(o) {
        var respTxt = o.responseText;
        var respObj = eval('(' + respTxt + ')');
        if ( typeof(respObj) != "undefined") {
          if ( typeof(respObj.root_node) != "undefined") {
            var root = tree.getRoot();
            var nodeDetails = "javascript:onClickTreeNode('" + respObj.root_node.ontology_node_id + "');";
            var rootNodeData = { label:respObj.root_node.ontology_node_name, id:respObj.root_node.ontology_node_id, href:nodeDetails };
            var expand = false;
            if (respObj.root_node.ontology_node_child_count > 0) {
              expand = true;
            }
            var ontRoot = new YAHOO.widget.TextNode(rootNodeData, root, expand);

            if ( typeof(respObj.child_nodes) != "undefined") {
              for (var i=0; i < respObj.child_nodes.length; i++) {
                var nodeInfo = respObj.child_nodes[i];
                addTreeNode(ontRoot, nodeInfo);
              }
            }
            tree.draw();
            setRootDesc(respObj.root_node.ontology_node_name, ontology_display_name);
          }
        }
        resetTreeStatus();
      }

      var handleResetTreeFailure = function(o) {
        resetTreeStatus();
        alert('responseFailure: ' + o.statusText);
      }

      var resetTreeCallback =
      {
        success:handleResetTreeSuccess,
        failure:handleResetTreeFailure
      };
      if (ontology_node_id!= '') {
        showTreeLoadingStatus();
        var ontology_source = null;//document.pg_form.ontology_source.value;
        var request = YAHOO.util.Connect.asyncRequest('GET','<%= request.getContextPath() %>/ajax?action=reset_tree&ontology_node_id=' +ontology_node_id+'&ontology_display_name='+ontology_display_name+'&ontology_source='+ontology_source,resetTreeCallback);
      }
    }

    function onClickTreeNode(ontology_node_id) {
      //document.pg_form.ontology_node_id.value = ontology_node_id;
      //var ontology_display_name = document.pg_form.ontology_display_name.value;
      //var graph_type = document.pg_form.graph_type.options[document.pg_form.graph_type.selectedIndex].value;
      //setNodeDetails(ontology_node_id, ontology_display_name);
      //buildGraph(ontology_node_id, ontology_display_name, graph_type);

      if (ontology_node_id.indexOf("|") != -1) return;
      load('<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=NCI%20Metathesaurus&code=' + ontology_node_id, currOpener);

    }

    function onClickViewEntireOntology(ontology_display_name) {
      var ontology_display_name = "NCI Metathesaurus";//document.pg_form.ontology_display_name.value;
      tree = new YAHOO.widget.TreeView("treecontainer");
      tree.draw();
      resetRootDesc();
      buildTree('', ontology_display_name);
    }

    function initTree() {

      tree = new YAHOO.widget.TreeView("treecontainer");
      var ontology_node_id = document.forms["pg_form"].ontology_node_id.value;
      var ontology_sab = document.forms["pg_form"].ontology_sab.value;
      var ontology_display_name = "NCI Metathesaurus";

      if (ontology_node_id == null || ontology_node_id == "null")
      {
          buildTree(ontology_node_id, ontology_display_name);
      }
      else
      {
          searchTree(ontology_node_id, ontology_display_name);
      }
    }

    function initRootDesc() {
      rootDescDiv.setBody('');
      initRootDesc.show();
      rootDescDiv.render();
    }

    function resetRootDesc() {
      rootDescDiv.hide();
      rootDescDiv.setBody('');
      rootDescDiv.render();
    }

    function resetEmptyRoot() {
      emptyRootDiv.hide();
      emptyRootDiv.setBody('');
      emptyRootDiv.render();
    }

    function resetTreeStatus() {
      treeStatusDiv.hide();
      treeStatusDiv.setBody('');
      treeStatusDiv.render();
    }

    function showEmptyRoot() {
      emptyRootDiv.setBody("<span class='instruction_text'>No root nodes available.</span>");
      emptyRootDiv.show();
      emptyRootDiv.render();
    }

    function showPartialHierarchy() {
      rootDescDiv.setBody("<span class='instruction_text'>(Note: This tree only shows partial hierarchy.)</span>");
      rootDescDiv.show();
      rootDescDiv.render();
    }


    function showNoPathFoundStatus() {
      rootDescDiv.setBody("<span class='instruction_text'>Concept not part of the parent-child hierarchy in this source – check other relationships.</span>");
      rootDescDiv.show();
      rootDescDiv.render();
    }


    function showTreeLoadingStatus() {
      treeStatusDiv.setBody("<img src='<%=basePath%>/images/loading.gif' alt='Loading'/> <span class='instruction_text'>Building tree ...</span>");
      treeStatusDiv.show();
      treeStatusDiv.render();
    }

    function showSearchingTreeStatus() {
      treeStatusDiv.setBody("<img src='<%=basePath%>/images/loading.gif' alt='Loading'/> <span class='instruction_text'>Retrieving data -- please wait ...</span>");
      treeStatusDiv.show();
      treeStatusDiv.render();
    }

    function showRenderingTreeStatus() {
      treeStatusDiv.setBody("<img src='<%=basePath%>/images/loading.gif' alt='Loading'/> <span class='instruction_text'>Rendering tree -- please wait ...</span>");
      treeStatusDiv.show();
      treeStatusDiv.render();
    }

    function loadNodeData(node, fnLoadComplete) {
      var id = node.data.id;

      var responseSuccess = function(o)
      {
        var path;
        var dirs;
        var files;
        var respTxt = o.responseText;
        var respObj = eval('(' + respTxt + ')');
        var fileNum = 0;
        var categoryNum = 0;
        var pos = id.indexOf("|");
        if ( typeof(respObj.nodes) != "undefined") {

      if (pos == -1) {
          for (var i=0; i < respObj.nodes.length; i++) {
      var name = respObj.nodes[i].ontology_node_name;
      var nodeDetails = "javascript:onClickTreeNode('" + respObj.nodes[i].ontology_node_id + "');";
      var newNodeData = { label:name, id:respObj.nodes[i].ontology_node_id, href:nodeDetails };
      var newNode = new YAHOO.widget.TextNode(newNodeData, node, false);
      if (respObj.nodes[i].ontology_node_child_count > 0) {
          newNode.setDynamicLoad(loadNodeData);
      }
          }

      } else {
    var parent = node.parent;
    var parent_label = parent.label;
    if (parent_label == undefined) {
        parent = tree.getRoot();
    }
    for (var i=0; i < respObj.nodes.length; i++) {
        var name = respObj.nodes[i].ontology_node_name;
        var nodeDetails = "javascript:onClickTreeNode('" + respObj.nodes[i].ontology_node_id + "');";
        var newNodeData = { label:name, id:respObj.nodes[i].ontology_node_id, href:nodeDetails };
        var newNode = new YAHOO.widget.TextNode(newNodeData, parent, true);
        if (respObj.nodes[i].ontology_node_child_count > 0) {
      newNode.setDynamicLoad(loadNodeData);
        }
    }
    tree.removeNode(node,true);
    if (parent_label == undefined) {
        tree.draw();
    }
      }
        }
        fnLoadComplete();
      }

      var responseFailure = function(o){
        alert('responseFailure: ' + o.statusText);
      }

      var callback =
      {
        success:responseSuccess,
        failure:responseFailure
      };

      var ontology_display_name = document.forms["pg_form"].ontology_display_name.value;
      var ontology_source = document.forms["pg_form"].ontology_sab.value;

      var cObj = YAHOO.util.Connect.asyncRequest('GET','<%= request.getContextPath() %>/ajax?action=expand_tree&ontology_node_id=' +id+'&ontology_display_name='+ontology_display_name+'&ontology_source='+ontology_source,callback);
    }

    function setRootDesc(rootNodeName, ontology_display_name) {
      var newDesc = "<span class='instruction_text'>Root set to <b>" + rootNodeName + "</b></span>";
      rootDescDiv.setBody(newDesc);
      var footer = "<a onClick='javascript:onClickViewEntireOntology();' href='#' class='link_text'>view full ontology}</a>";
      rootDescDiv.setFooter(footer);
      rootDescDiv.show();
      rootDescDiv.render();
    }


    function searchTree(ontology_node_id, ontology_display_name) {
        var handleBuildTreeSuccess = function(o) {
        var respTxt = o.responseText;
        var respObj = eval('(' + respTxt + ')');
        if ( typeof(respObj) != "undefined") {
          if ( typeof(respObj.root_nodes) != "undefined") {
            var root = tree.getRoot();
            if (respObj.root_nodes.length == 0) {
                showNoPathFoundStatus();
            }
            else {
              showPartialHierarchy();
              for (var i=0; i < respObj.root_nodes.length; i++) {
                var nodeInfo = respObj.root_nodes[i];
                addTreeBranch(ontology_node_id, root, nodeInfo);
              }
            }
          }
        }
        resetTreeStatus();
      }

      var handleBuildTreeFailure = function(o) {
        resetTreeStatus();
        resetEmptyRoot();
        alert('responseFailure: ' + o.statusText);
      }

      var buildTreeCallback =
      {
        success:handleBuildTreeSuccess,
        failure:handleBuildTreeFailure
      };

      if (ontology_display_name!='') {
        resetEmptyRoot();
        showSearchingTreeStatus();
        var ontology_source = document.forms["pg_form"].ontology_sab.value;
        var request = YAHOO.util.Connect.asyncRequest('GET','<%= request.getContextPath() %>/ajax?action=search_tree&ontology_node_id=' +ontology_node_id+'&ontology_display_name='+ontology_display_name+'&ontology_source='+ontology_source,buildTreeCallback);
      }
    }




    function addTreeBranch(ontology_node_id, rootNode, nodeInfo) {

      var newNodeDetails = "javascript:onClickTreeNode('" + nodeInfo.ontology_node_id + "');";
      var newNodeData = { label:nodeInfo.ontology_node_name, id:nodeInfo.ontology_node_id, href:newNodeDetails };

      var expand = false;
      var childNodes = nodeInfo.children_nodes;

      if (childNodes.length > 0) {
          expand = true;
      }
      var newNode = new YAHOO.widget.TextNode(newNodeData, rootNode, expand);
      if (nodeInfo.ontology_node_id == ontology_node_id) {
          newNode.labelStyle = "ygtvlabel_highlight";
      }

      if (nodeInfo.ontology_node_id == ontology_node_id) {
         newNode.isLeaf = true;
         if (nodeInfo.ontology_node_child_count > 0) {
             newNode.isLeaf = false;
             //newNode.setDynamicLoad(loadNodeData);
         }

      } else {
          if (nodeInfo.ontology_node_child_count == 0 && nodeInfo.ontology_node_id != ontology_node_id) {
              newNode.isLeaf = true;
          } else if (childNodes.length == 0) {
              newNode.setDynamicLoad(loadNodeData);
          }
      }
      tree.draw();

      for (var i=0; i < childNodes.length; i++) {
          var childnodeInfo = childNodes[i];
          addTreeBranch(ontology_node_id, newNode, childnodeInfo);
      }
    }



    // Note: Need to refresh the parent window so the LicenseBean has a
    //  chance to update.  The following line helps avoid reprompting
    //  the license again.

    //function refresh() {
    //        var licensed = document.forms["pg_form"].isLicensed_str.value;
    //        if (licensed == "true") {
  //      var accepted = document.forms["pg_form"].licenseAgreementAccepted.value;
  //      if (accepted == "true") {
  //    window.opener.location.reload(true);
  //      }
//      }
//   }


    YAHOO.util.Event.addListener(window, "load", init);

    //YAHOO.util.Event.addListener(window, "load", refresh);


  </script>
</head>
<body>
  <%!
      private static Logger _logger = Utils.getJspLogger("source_hierarchy.jsp");
  %>
  <f:view>
    <!-- Begin Skip Top Navigation -->
        <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">skip navigation links</A>
    <!-- End Skip Top Navigation --> 

<div id="popupContainer">
    
      <!-- nci popup banner -->
      <div class="ncipopupbanner">
        <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute"><img src="<%=basePath%>/images/banner-red.png" width="680" height="39" border="0" alt="National Cancer Institute" /></a>
        <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute"><img src="<%=basePath%>/images/spacer.gif" width="48" height="39" border="0" alt="National Cancer Institute" class="print-header" /></a>
      </div>
      <!-- end nci popup banner -->
      <div id="popupMainArea">
        <table class="evsLogoBg" cellspacing="0" cellpadding="0" border="0" role='presentation'>
        <tr>
          <td valign="top">
            <a href="http://evs.nci.nih.gov/" target="_blank" alt="Enterprise Vocabulary Services">
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
<script type="text/javascript">_satellite.pageBottom();</script>
</body>
</html>
