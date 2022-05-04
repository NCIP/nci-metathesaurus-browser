<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.NCImMetadataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.common.Constants" %>

<%
  String terminology_subset_download_URL = new DataUtils().getTerminologySubsetDownloadURL();
  String ql_ncit_url = new DataUtils().getNCItURL();

  String term_suggestion_application_url2 = (String) request.getSession().getAttribute("term_suggestion_application_url");
  if (DataUtils.isNull(term_suggestion_application_url2)) {
  
     term_suggestion_application_url2 = NCImMetadataUtils.getMetadataValue(Constants.CODING_SCHEME_NAME, null, null, "term_suggestion_application_url");
     if (term_suggestion_application_url2 != null) {
         //request.getSession().setAttribute("term_suggestion_application_url", term_suggestion_application_url);
         request.getSession().setAttribute("term_suggestion_application_url", term_suggestion_application_url2);
     }
  }
  
  String dictionaryName = Constants.CODING_SCHEME_NAME;
  String dictionary_encoded2 = dictionaryName.replaceAll(" ", "%20");

%>
<!--
<div class="bluebar">
-->
<div style='clear:both;padding:3px;height:30px;color:white;background-color:#C31F40'>


  <div id="quicklinksholder">

    <ul id="quicklinks"
        onmouseover="document.quicklinksimg.src='<%=basePath%>/images/quicklinks-active.gif';"
        onmouseout="document.quicklinksimg.src='<%=basePath%>/images/quicklinks-inactive.gif';"
        tabindex="0">

      <li>
        <a href="#">
          <img src="<%=basePath%>/images/quicklinks-inactive.gif" width="162"
            height="18" border="0" name="quicklinksimg" alt="Quick Links" />
        </a>
        <ul>
          <li><a href="https://evs.nci.nih.gov/" target="_blank" rel="noopener"  alt="EVS">EVS Home</a></li>
          <li><a href="<%=ql_ncit_url%>" target="_blank" rel="noopener"  alt="EVS">NCI Thesaurus</a></li>
          <!--
          <li><a href="https://ncimeta.nci.nih.gov/MetaServlet/" target="_blank" rel="noopener"  alt="NCI Metathesaurus">NCI Metathesaurus</a></li>
           -->


          <li><a href="<%=ql_ncit_url%>start.jsf" target="_blank" rel="noopener"  alt="NCI Term Browser">NCI Term Browser</a></li>

            <% if (term_suggestion_application_url2 != null && term_suggestion_application_url2.length() > 0) { %>
              <li><a href="<%=term_suggestion_application_url2%>?dictionary=<%=dictionary_encoded2%>" target="_blank" rel="noopener"  alt="Term Suggestion">Term Suggestion</a></li>
            <% } %>


          <li><a href="https://www.cancer.gov/cancertopics/terminologyresources" target="_blank" rel="noopener"  alt="NCI Terminology Resources">NCI Terminology Resources</a></li>
        </ul>
      </li>
    </ul>
  </div>
</div>
