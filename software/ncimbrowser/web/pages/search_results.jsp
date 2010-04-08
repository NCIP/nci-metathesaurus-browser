<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.LexGrid.concepts.Concept" %>
<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.properties.NCImBrowserProperties" %>

<%@ page import="gov.nih.nci.evs.browser.bean.IteratorBean" %>
<%@ page import="gov.nih.nci.evs.browser.bean.IteratorBeanManager" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>

<%@ page import="java.io.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>NCI Metathesaurus</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

    <script type="text/javascript"
      src="<%=request.getContextPath()%>/js/wz_tooltip.js"></script>
    <script type="text/javascript"
      src="<%=request.getContextPath()%>/js/tip_centerwindow.js"></script>
    <script type="text/javascript"
      src="<%=request.getContextPath()%>/js/tip_followscroll.js"></script>

<f:view>
  <%@ include file="/pages/include/header.jsp" %>
  <div class="center-page">
    <%@ include file="/pages/include/sub-header.jsp" %>
    <!-- Main box -->
    <div id="main-area">
      <%@ include file="/pages/include/content-header.jsp" %>
      <!-- Page content -->
      <div class="pagecontent">
        <%

          long ms = System.currentTimeMillis();
          long iterator_delay;

          String page_string = null;
          

          IteratorBean iteratorBean = null;
          
String key = null;          
String randomKey = (String) request.getParameter("key");  
IteratorBeanManager iteratorBeanManager = (IteratorBeanManager) FacesContext.getCurrentInstance().getExternalContext()
    .getSessionMap().get("iteratorBeanManager");

if (iteratorBeanManager == null) {
    System.out.println("iteratorBeanManager == null???");
    iteratorBeanManager = new IteratorBeanManager();
    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("iteratorBeanManager", iteratorBeanManager);
}

if (randomKey != null) {
    iteratorBean = iteratorBeanManager.getIteratorBean(randomKey);
} else {
	key = (String) request.getAttribute("key");  
	iteratorBean = iteratorBeanManager.getIteratorBean(key);
}


          String matchText = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getAttribute("matchText"));

	  if (iteratorBean == null) {
	      System.out.println("iteratorBean NOT FOUND???" + key);  
	  } else {
	      matchText = iteratorBean.getMatchText();
	      //KLO
	      request.setAttribute("matchText", matchText);
	  }

          page_string = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("page_string"));

          Boolean new_search = (Boolean) request.getSession().getAttribute("new_search");
          String page_number = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("page_number"));
          String selectedResultsPerPage = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("selectedResultsPerPage"));

          if (page_number != null && new_search == Boolean.FALSE)
          {
              page_string = page_number;
          }

          request.getSession().setAttribute("new_search", Boolean.FALSE);
          int page_num = Integer.parseInt(page_string);
          int next_page_num = page_num + 1;
          int prev_page_num = page_num - 1;
          int page_size = 50;
          if (selectedResultsPerPage != null)
          {
              page_size = Integer.parseInt(selectedResultsPerPage);
          }
          int iend = page_num * page_size;
          int istart = iend - page_size;

          int size = iteratorBean.getSize();
          String match_size = new Integer(size).toString();

          if (iend > size) iend = size;
          int num_pages = size / page_size;
          if (num_pages * page_size < size) num_pages++;

          String istart_str = Integer.toString(istart+1);
          String iend_str = Integer.toString(iend);
          String prev_page_num_str = Integer.toString(prev_page_num);
          String next_page_num_str = Integer.toString(next_page_num);

        %>
        <table width="700px">

          <tr>
            <table>
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
              <table class="dataTable" summary="" cellpadding="3" cellspacing="0" border="0" width=1000>

          <%
          //String sortOptionType = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("sortOptionType"));
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
                  System.out.println("iteratorBean.getData Run time (ms): " + iterator_delay);


                  Vector code_vec = new Vector();
                  for (int k=0; k<list.size(); k++) {
                      ResolvedConceptReference rcr = (ResolvedConceptReference) list.get(k);
                      code_vec.add(rcr.getConceptCode());
                  }
                  if (code_vec.size() > 0) {
			  HashMap type_hmap = DataUtils.getPropertyValuesForCodes(Constants.CODING_SCHEME_NAME, null, code_vec, "Semantic_Type");

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
				    <a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=<%=code%>" ><%=name%></a>
				  </td>
				  <td class="dataCellText" width=400>
				      <%=semantic_type%>
				  </td>
				</tr>
			      <%

			  } 
                  } else {
                  %>
                       <tr>
                       <td class="textbodyred"> 
                           WARNING: Application timeout.
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
        System.out.println("Page rendering Run time (ms): " + pageRenderingDelay + " (excluding iterator next call delay.)");
        %>

      </div>
      <!-- end Page content -->
    </div>
    <div class="mainbox-bottom"><img src="images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    <!-- end Main box -->
  </div>
</f:view>
</body>
</html>