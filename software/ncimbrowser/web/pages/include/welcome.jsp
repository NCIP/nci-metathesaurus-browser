<%
  // String version = new DataUtils().getVocabularyVersionByTag(
  //   "NCI Metathesaurus", "PRODUCTION");
  String version = DataUtils.getMetadataValue(Constants.CODING_SCHEME_NAME, "version");
  if (version == null)
    version = new DataUtils().getVocabularyVersionByTag(
      "NCI Metathesaurus", null);
  if (version == null)
    version = "";
  String ncit_url = new DataUtils().getNCItURL();
  String html_compatable_description_value = DataUtils.getMetadataValue(Constants.CODING_SCHEME_NAME, "html_compatable_description");

  if (html_compatable_description_value == null) {
    html_compatable_description_value = "WARNING: html_compatable_description metadata element not found.";
  }
%>
<div class="textbody">
<!--
  <table border="0" width="700px">
 -->
  <table class="datatable_960" border="0" width="100%">
  <tr>
    <td><div class="texttitle-blue">Welcome</div></td>
    <!--
    <td><div class="texttitle-blue-rightJust">Version: <%= version %></div></td>
    -->
  </tr></table>
  <hr/>
  <table border="0">
    <tr>
      <td class="textbody" valign="top" width="70%">
        <%=html_compatable_description_value%>
      </td>
      <td valign="top" width="30%">
        <table border="0">
          <tr valign="top">
            <td width="10px"></td>
            <td>
              <a href="http://evs.nci.nih.gov/" target="_blank" alt="NCI Enterprise Vocabulary Services">
                <img src="<%=basePath%>/images/EVSTile.gif"
                  width="77" height="38px" alt="EVS" border="0"/>
              </a>
            </td>
            <td width="3px"></td>
            <td class="textbody" valign="top">
              <a href="http://evs.nci.nih.gov/" target="_blank" alt="NCI Enterprise Vocabulary Services">
                NCI Enterprise Vocabulary Services</a>:
              Terminology resources and services for NCI and the biomedical community.
            </td>
          </tr>
          <tr valign="top">
            <td width="10px"></td>
            <td>
              <a href="<%=ncit_url%>" target="_blank" alt="NCI Thesaurus">
                <img src="<%=basePath%>/images/NCItTile.jpg"
                  width="77" height="38px" alt="NCIt" border="0"/>
              </a>
            </td>
            <td width="3px"></td>
            <td class="textbody" valign="top">
              <a href="<%=ncit_url%>" target="_blank" alt="NCI Thesaurus">
                NCI Thesaurus</a>:
                Reference terminology for NCI, NCI Metathesaurus and NCI informatics infrastructure.
            </td>
          </tr>
          <tr valign="top">
            <td width="10px"></td>
            <td>
              <a href="<%=ncit_url%>start.jsf" target="_blank" alt="NCI Term Browser">
                <img src="<%=basePath%>/images/EVSTermsBrowserTile.gif"
                  width="77" height="38px" alt="NCI Term Browser" border="0"/>
              </a>
            </td>
            <td width="3px"></td>
            <td class="textbody" valign="top">
              <a href="<%=ncit_url%>start.jsf" target="_blank" alt="Bio Portal">
                NCI Term Browser</a>:
                NCI and other terminologies in an integrated environment.
            </td>
          </tr>
          <tr valign="top">
            <td width="10px"></td>
            <td>
              <a href="http://www.cancer.gov/cancertopics/terminologyresources/" target="_blank" alt="Cancer.gov">
                <img src="<%=basePath%>/images/Cancer_govTile.gif"
                  alt="Cancer.gov" border="0"/>
              </a>
            </td>
            <td width="3px"></td>
            <td class="textbody" valign="top">
              <a href="http://www.cancer.gov/cancertopics/terminologyresources/" target="_blank" alt="Cancer.gov">
                NCI Terminology Resources</a>:
              More information on NCI dictionaries and resources.
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</div>
