<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="java.util.*"%>
<%@ page import="org.LexGrid.concepts.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.common.*" %>

<%@ page import="gov.nih.nci.evs.browser.properties.*" %>

<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="javax.faces.context.*" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.*" %>

<%@ page import="java.io.*" %>
<%@ page import="org.apache.logging.log4j.*" %>

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
  <title>NCI Metathesaurus</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<body onLoad="document.forms.searchTerm.matchText.focus();">

    <script type="text/javascript"
      src="<%=request.getContextPath()%>/js/wz_tooltip.js"></script>
    <script type="text/javascript"
      src="<%=request.getContextPath()%>/js/tip_centerwindow.js"></script>
    <script type="text/javascript"
      src="<%=request.getContextPath()%>/js/tip_followscroll.js"></script>
  <%!
      private static Logger _logger = LogManager.getLogger("search_results.jsp");
  %>

<f:view>
  <!-- Begin Skip Top Navigation -->
    <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">skip navigation links</a>
  <!-- End Skip Top Navigation -->
  <%@ include file="/pages/include/header.jsp" %>
  <div class="center-page">
    <%@ include file="/pages/include/sub-header.jsp" %>
    <!-- Main box -->
    <div id="main-area">
      <%@ include file="/pages/include/content-header.jsp" %>
      <!-- Page content -->
      <div class="pagecontent">
        <a name="evs-content" id="evs-content" tabindex="0"></a>
        <%
          long ms = System.currentTimeMillis();
          long iterator_delay;

          String page_string = null;
          IteratorBean iteratorBean = null;

String key = null;
String randomKey = (String) request.getSession().getAttribute("key");
if (randomKey == null) {
    randomKey = HTTPUtils.cleanXSS((String) request.getParameter("key"));
}

System.out.println("randomKey: " + randomKey);

IteratorBeanManager iteratorBeanManager = (IteratorBeanManager) FacesContext.getCurrentInstance().getExternalContext()
    .getSessionMap().get("iteratorBeanManager");

if (iteratorBeanManager == null) {
    _logger.warn("iteratorBeanManager == null???");
    iteratorBeanManager = new IteratorBeanManager();
    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("iteratorBeanManager", iteratorBeanManager);
}


if (randomKey != null) {
        iteratorBean = iteratorBeanManager.getIteratorBean(randomKey);
} else {
    System.out.println("randomKey == null???");
    _logger.warn("iteratorBean NOT FOUND???" + randomKey);
}

	

          String matchText = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("matchText"));
request.getSession().setAttribute("match_match", matchText);
          
/*
	    if (iteratorBean == null) {
		_logger.warn("iteratorBean NOT FOUND???" + key);
	    } else {
		request.getSession().setAttribute("matchText", matchText);
	    }
*/

          page_string = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("page_string"));

          Boolean new_search = (Boolean) request.getSession().getAttribute("new_search");
          String page_number = HTTPUtils.cleanXSS((String) request.getParameter("page_number"));
          String selectedResultsPerPage = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("selectedResultsPerPage"));

          if (page_number != null && new_search == Boolean.FALSE)
          {
              page_string = page_number;
              request.getSession().setAttribute("page_string", page_string);
          }

          request.getSession().setAttribute("new_search", Boolean.FALSE);
          int page_num = Integer.parseInt(page_string);
          
          int next_page_num = page_num + 1;
          int prev_page_num = page_num - 1;
          int page_size = 50;
          if (selectedResultsPerPage != null)
          {
        	  try {
              	page_size = Integer.parseInt(selectedResultsPerPage);
        	  } catch (Exception e) {
        		// Do nothing, bad number  
        	  }
          }
          int iend = page_num * page_size;
          int istart = iend - page_size;

          int size = iteratorBean.getSize();
          String match_size = Integer.valueOf(size).toString();

          if (iend > size) iend = size;
          int num_pages = size / page_size;
          if (num_pages * page_size < size) num_pages++;

          String istart_str = Integer.toString(istart+1);
          String iend_str = Integer.toString(iend);
          String prev_page_num_str = Integer.toString(prev_page_num);
          String next_page_num_str = Integer.toString(next_page_num);

        %>
        <table width="700px" role='presentation'>

          <tr>
            <table role='presentation'>
              <tr>
                <td class="texttitle-blue">Result for:</td>
                <td class="texttitle-gray"><%=matchText%></td>
              </tr>
            </table>
          </tr>
          <tr>
            <td><hr></td>
          </tr>
          <tr>
            <td>
              <b>Results <%=istart_str%>-<%=iend_str%> of&nbsp;<%=match_size%> for: <%=matchText%></b>
            </td>
          </tr>
          <tr>
            <td class="textbody">
              <table class="datatable_960" summary="" cellpadding="3" cellspacing="0" border="0" width="100%">

          <%
          //String sortOptionType = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("sortOptionType"));
          //if (sortOptionType == null)
          //    sortOptionType = "false";
          //if (sortOptionType.compareToIgnoreCase("all") == 0) {
          boolean showSemanticType = true;
          
          if (showSemanticType) {
          %>
      <th class="dataTableHeader" scope="col" align="left">Concept</th>
      <th class="dataTableHeader" scope="col" align="left">Semantic Type</th>
          <%
          }
          %>
                <%
		      long ms0 = System.currentTimeMillis();
		      List list = iteratorBean.getData(istart, iend);
		      iterator_delay = System.currentTimeMillis() - ms0;

			  //Vector code_vec = new Vector();
			  String[] code_array = new String[list.size()];
			  for (int k=0; k<list.size(); k++) {
			      ResolvedConceptReference rcr = (ResolvedConceptReference) list.get(k);
			      code_array[k] = rcr.getConceptCode();
			  }

			  HashMap type_hmap = DataUtils.getSemanticTypes(code_array);

		//HashMap type_hmap = DataUtils.getPropertyValuesForCodes(Constants.CODING_SCHEME_NAME, null, code_vec, "Semantic_Type");


			for (int i=0; i<list.size(); i++) {
			    ResolvedConceptReference rcr = (ResolvedConceptReference) list.get(i);
			    String code = rcr.getConceptCode();
			    String name = rcr.getEntityDescription().getContent();
			    String semantic_type = "";
			    String t = (String) type_hmap.get(code);

			    if (t != null) semantic_type = t;
			    if (i % 2 == 0) {
			%>
			  <tr class="dataRowDark">
			<%
			    } else {
			%>
			  <tr class="dataRowLight">
			<%
			    }
			    %>
			  <td class="dataCellText" width=600>
			    <a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=Constants.NCI_METATHESAURUS%>&code=<%=code%>"><%=DataUtils.encodeTerm(name)%></a>
			  </td>
			  <td class="dataCellText" width=400>
			      <%=semantic_type%>
			  </td>
			</tr>
			    <%

			}

                %>
              </table>
            </td>
          </tr>
        </table>
        <%@ include file="/pages/include/pagination.jsp" %>
        
        <%@ include file="/pages/include/nciFooter.jsp" %>

        <%
        long pageRenderingDelay = System.currentTimeMillis() - ms - iterator_delay;
        _logger.debug("Page rendering Run time (ms): " + pageRenderingDelay + " (excluding iterator next call delay.)");
        %>

      </div>
      <!-- end Page content -->
    </div>
    <div class="mainbox-bottom"><img src="<%=basePath%>/images/mainbox-bottom.gif" width="962" height="5" alt="Mainbox Bottom" /></div>
    <!-- end Main box -->
  </div>
</f:view>
<script type="text/javascript">_satellite.pageBottom();</script>
</body>
</html>
