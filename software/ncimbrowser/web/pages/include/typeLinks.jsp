<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="org.apache.logging.log4j.*" %>

<%!
    private static Logger _logger = LogManager.getLogger("typeLinks.jsp");
%>
<%
  Entity concept_typelinks = (Entity) request.getSession().getAttribute("concept");
  if (concept_typelinks == null) {
_logger.warn("(*) concept_typelinks == null???");
  }

  String concept_typelinks_id = concept_typelinks.getEntityCode();

  //request.getSession().setAttribute("code", concept_src_id);
  //Vector sources_typelinks = new DataUtils().getSources(Constants.CODING_SCHEME_NAME, null, null, concept_typelinks_id);
%>

<table class="tabTable" width="100%" cellspacing="0" cellpadding="0" border="0" role='presentation'>
  <tr>
    <%
      String scheme = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("dictionary"));
      String id = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("code"));

      String data_type = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("type"));

      if (data_type == null) data_type = "properties";

     %>
     <td width="134" height="21">
     <%
        if (data_type == null) {
          %>
            <img name="tpTab"
              src="<%=request.getContextPath() %>/images/tab_tp_clicked.gif"
              width="134" height="21" border="0" alt="Terms &amp; Properties"
              title="Terms &amp; Properties" />
          <%
        } else if (data_type.compareTo("properties") == 0) {
          %>
            <img name="tpTab"
              src="<%=request.getContextPath() %>/images/tab_tp_clicked.gif"
              width="134" height="21" border="0" alt="Terms &amp; Properties"
              title="Terms &amp; Properties" />
          <%
        } else if (data_type.compareTo("properties") != 0) {
          %>
            <a id="a_tpTab" href="<%=request.getContextPath() %>/pages/concept_details.jsf?dictionary=<%=scheme%>&code=<%=id%>&type=properties">
              <img name="tpTab"
                src="<%=request.getContextPath() %>/images/tab_tp.gif"
                width="134" height="21" border="0" alt="Terms &amp; Properties"
                title="Terms &amp; Properties" />
            </a>
          <%
        }
          %>
      </td>
      <td width="119" height="21">
        <%
          if (data_type == null ||
            (data_type != null && data_type.compareTo("synonym") != 0)) {
        %>
        <a id="a_synTab" href="<%=request.getContextPath() %>/pages/concept_details.jsf?dictionary=<%=scheme%>&code=<%=id%>&type=synonym">
              
          <img name="sdTab"
            src="<%=request.getContextPath() %>/images/tab_sd.gif"
            width="119" height="21" border="0" alt="Synonym Details"
            title="Synonym Details" />
        </a>
        <%
          } else {
        %>
          <img name="sdTab"
            src="<%=request.getContextPath() %>/images/tab_sd_clicked.gif"
            width="119" height="21" border="0" alt="Synonym Details"
            title="Synonym Details" />
        <%
          }
        %>
      </td>
      <td width="102" height="21">
        <%
          if (data_type == null ||
            (data_type != null && data_type.compareTo("relationship") != 0)) {
        %>
        <a id="a_relTab" href="<%=request.getContextPath() %>/pages/concept_details.jsf?dictionary=<%=scheme%>&code=<%=id%>&type=relationship">
          <img name="relTab"
            src="<%=request.getContextPath() %>/images/tab_rel.gif"
            width="102" height="21" border="0" alt="Relationships"
            title="Relationships" />
        </a>
        <%
          } else {
        %>
          <img name="relTab"
            src="<%=request.getContextPath() %>/images/tab_rel_clicked.gif"
            width="102" height="21" border="0" alt="Relationships"
            title="Relationships" />
        <%
          }
        %>
        </td>
        <td width="71" height="21">
        <%
          if (data_type == null ||
            (data_type != null && data_type.compareTo("sources") != 0)) {
        %>
        <a id="a_srcTab" href="<%=request.getContextPath() %>/pages/concept_details.jsf?dictionary=<%=scheme%>&code=<%=id%>&type=sources">
          <img name="sourceTab"
            src="<%=request.getContextPath() %>/images/tab_sou.gif"
            width="83" height="21" border="0" alt="Sources"
            title="Sources" />
        </a>
        <%
          } else {
        %>
          <img name="sourceTab"
            src="<%=request.getContextPath() %>/images/tab_sou_clicked.gif"
            width="71" height="21" border="0" alt="Sources"
            title="Sources" />
        <%
          }
        %>
        </td>
        <td width="71" height="21">
        <%
          if (data_type == null ||
            (data_type != null && data_type.compareTo("all") != 0)) {
        %>
          <a id="a_allTab" href="<%=request.getContextPath() %>/pages/concept_details.jsf?dictionary=<%=scheme%>&code=<%=id%>&type=all">
            <img name="vaTab"
              src="<%=request.getContextPath() %>/images/tab_va.gif"
              width="71" height="21" border="0" alt="View All"
              title="View All" />
          </a>
        <%
          } else {
        %>
          <img name="vaTab"
            src="<%=request.getContextPath() %>/images/tab_va_clicked.gif"
            width="71" height="21" border="0" alt="View All"
            title="View All" />
        <%
        }
        %>
        </td>


        <td align="right" valign="top">
             &nbsp;
         </td>
  </tr>
</table>
