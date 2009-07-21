<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Concept" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>NCI Metathesaurus</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<f:view>
  <%@ include file="/pages/templates/header.xhtml" %>
  <div class="center-page">
    <%@ include file="/pages/templates/sub-header.xhtml" %>
    <!-- Main box -->
    <div id="main-area">
      <%@ include file="/pages/templates/content-header.xhtml" %>
      <!-- Page content -->
      <div class="pagecontent">
        <!-- ======================================= -->
        <!--                 HELP CONTENT            -->
        <!-- ======================================= -->
        <div class="texttitle-blue">Help</div>
        <p class="textbody">
          <A HREF="#introduction">Introduction</A><br>
          <A HREF="#searchhelp">Search</A><br>
          <A HREF="#conceptdetails">Concept Details</A><br>
          <A HREF="#hierarchy">View NCIt Hierarchy</A><br>
          <A HREF="#sources">Sources</A><br>
          <A HREF="#knownissues">Known Issues</A><br>
          <A HREF="#additionalinformation">Additional Information</A>
        </p>
        <p class="textbody">
          <table width="100%" cellpadding="0" cellspacing="0" border="0"><tr>
            <td><h2><A NAME="introduction">Introduction</A></h2></td>
            <td align="right"><a href="#"><img src="<%= request.getContextPath() %>/images/up_arrow.jpg" width="16" height="16" border="0" alt="top" /></td>
          </tr></table>
          <b>NCI Metathesaurus (NCIm)</b> is a comprehensive biomedical terminology database, connecting
          4,600,000 terms from more than 70 terminologies. It contains most public domain vocabularies from
          the National Library of Medicine's <a href="http://www.nlm.nih.gov/research/umls/umlsmain.html">UMLS Metathesaurus</a>,
          as well as many other biomedical vocabularies created by or of interest to NCI and its partners, including
          some propriety vocabularies with restrictions on their use.
          <br><br>
          <b>The NCIm Browser</b> is for the retrieval of concepts from the NCI Metathesaurus.  It is designed for ease of
          use by a diverse user community. This first release focuses on the data and features most users want. Future
          releases will add advanced search options, additional information from source terminologies, and other things
          requested by users. <a href="<%= request.getContextPath() %>/pages/contact_us.jsf">Contact Us</a> to provide feedback
          and get additional help on the NCIm Browser.
          <br><br>
          <b>This help file</b> provides basic information about how to use the NCIm Browser effectively. It also provides
          pointers on how to learn more about NCIm and related resources. The following typeface font conventions are used
          for describing search and the browser interface:
          <ul>
            <li><span style="font-weight:bold;font-family:sans-serif;">Sans Serif Bold</span>: Browser links, buttons, and drop-down boxes.
            <li><span style="font-family:monospace;">Fixed Width:</span> Search strings
            <li><span style="font-style:italic;">Italics</span>: Concept terms
          </ul>
        </p>
        <p class="textbody">
          <table width="100%" cellpadding="0" cellspacing="0" border="0"><tr>
            <td><h2><A NAME="searchhelp">Search</A></h2></td>
            <td align="right"><a href="#"><img src="<%= request.getContextPath() %>/images/up_arrow.jpg" width="16" height="16" border="0" alt="top" /></td>
          </tr></table>
          <b>In the Search box,</b> enter all or part of what you are looking for and click the Search button.
          Some details:
          <ul>
            <li>You can search for a concept’s name, synonyms, acronyms, or codes.
            <li><b>Exact Match</b> is the default: Only terms or codes that are identical will match.
            <li><b>Begins With</b> can be selected to find all terms or codes that start with the words or characters you enter.
            <li><b>Contains</b> will search for what you enter anywhere within a term or code (e.g.,
            <i>carcinoma</i> will match <i>adenocarcinoma</i>).
            <li><b>Rank (Slow)</b> sorts results from best match to weakest if the check box is selected.  For example, a <b>Contains</b>
            search on <i>melanocyte</i> returns <i>melanocyte</i> at the top, followed by concepts with two word matches (e.g.,
            <i>Spindle Melanocyte</i>), followed by concepts whose terms have more non-<i>melanocyte</i> content.It is off by
            default due to poor performance.
            <li>Concept Unique Identifiers (CUIs) and codes from individual sources will only match if they exactly match what you enter, even if you select <b>Begins With</b> or <b>Contains</b>.
            <li>Search is not case sensitive (e.g., aids will match <i>aids</i>,
            <i>Aids</i>, and <i>AIDS</i>).
            <li>There are no wildcard characters. All characters are matched literally (e.g., searching for <b>Begins With</b> <span style="font-family:monospace;">NAT2*</span> will match
            <i>NAT2*5 Allele</i> but not <i>NAT2 Gene</i>).
            <li>Do not use quotes - they will be searched for literally, as characters in concept terms.
            <li>Searching for multiple words does not search on each word separately. To match, all words have to be found
            in the same order you provided. For example, if you do a <b>Contains</b> search on <i>Melanoma Corneal</i>
            no results will be returned, but if you search on <i>Corneal Melanoma</i> you
            get the detail page for <i>Corneal Melanoma</i>.
            <li><b>Source</b> drop-down box: You can choose to limit your search to a specific source. For example, if
            you only want to find <i>breast cancer</i> concepts that include concepts from
            SNOMEDCT, you would choose <span style="font-family:monospace;">SNOMEDCT</span> from the <b>Source</b> box.
          </ul>
          Search of other concept data, approximate matching, and other features will be added to future releases of
          this browser. Some of these features are currently available in the <a href="http://bioportal.nci.nih.gov/ncbo/faces/index.xhtml">NCI BioPortal Browser.</a>
          <br><br>
          <b>Search results</b> are displayed by concept name. (If there is only one match, the concept details page
          is shown directly without first listing results.) Some details:
          <ul>
            <li>All matching concepts are returned.
            <li>Results are listed alphabetically unless the <b>Rank</b> option is selected.
            <li>If <b>Rank</b> is selected, results are listed from best match to weakest. For example, a <b>Contains</b> search on
            <i>melanocyte</i> returns <i>melanocyte</i> at the top, followed by concepts with two word matches (e.g.,
            <i>Spindle Melanocyte</i>), followed by concepts whose terms have more non-<i>melanocyte</i> content.  An additional
            column displays the semantic type(s) assigned to each concept.
            <li>The match will often be to synonyms or codes only visible on the concept details page (e.g.,
            searching <b>Begins With</b> <i>melanoma</i> will show <i>Corneal Melanoma</i> in the results list because that
            concept contains a synonym of <i>Melanoma of the Cornea</i>.) A future release will show these matches in the
            results window.
            <li>If there are too many to show on one page, you can page through the results with a default of 50 per page.
            To change the default number, use the <b>Show results per page</b> drop-down menu at the bottom of the results
            page.
            <li>Click on the name to see a concept’s details.
          </ul>
        </p>
        <p class="textbody">
          <table width="100%" cellpadding="0" cellspacing="0" border="0"><tr>
            <td><h2><A NAME="conceptdetails">Concept Details</A></h2></td>
            <td align="right"><a href="#"><img src="<%= request.getContextPath() %>/images/up_arrow.jpg" width="16" height="16" border="0" alt="top" /></td>
          </tr></table>
          Detailed information on the selected concept is grouped and shown on several related pages:
         <ul>
            <li>Tabbed information gives the concept’s meaning, labels, and direct relationships:
            <ul>
              <li><b>Terms & Properties</b>: Gives definitions, synonyms, abbreviations, codes, and other information.
              <li><b>Relationships</b>: Shows how other concepts are directly related to this concept as parents, children,
              or in other ways.
              <li><b>Synonym Details</b>: For each term or abbreviation, shows its source, term type, and code.
              <li><b>By Source</b>: Shows concept information one source at a time. NCI Thesaurus content is shown if available,
              otherwise the initial source is chosen alphabetically. To view concept information from a different source, select
              on the link for that source at the top of the page. NOTE: Select the ? icon next to the Source header to view a list
              of the source abbreviations and full names.
              <li><b>View All</b>: Combines all of the above information on a single page.
            </ul>
            <li><b>In NCIt Hierarchy</b>: Click the button to see where the concept is found within the NCIt hierarchy as
            presented through NCIm concepts. Concepts are often found in several different places. The focus concept will
            be bold, underlined, and colored red.  This function only appears in concepts with NCIt content.  Hierarchy
            displays from other sources will be provided in a later browser release.
         </ul>
        </p>
        <p class="textbody">
          <table width="100%" cellpadding="0" cellspacing="0" border="0"><tr>
            <td><h2><A NAME="hierarchy">View NCIt Hierarchy</A></h2></td>
            <td align="right"><a href="#"><img src="<%= request.getContextPath() %>/images/up_arrow.jpg" width="16" height="16" border="0" alt="top" /></td>
          </tr></table>
          Click on the <b>View NCIt Hierarchy</b> link at the top of the page to bring up a separate window showing the
          NCI Thesaurus hierarchy as presented through NCIm concepts.  Some details:
          <ul>
            <li>At first, only the top level nodes of the hierarchy are shown.
            <li>At each level, concepts are listed alphabetically by concept name.
            <li>Browse through the levels by clicking on the + next to each concept.
            <li>Click on the concept name itself to see the concept’s details in the main browser window.
          </ul>
        </p>
        <p class="textbody">
          <table width="100%" cellpadding="0" cellspacing="0" border="0"><tr>
            <td><h2><A NAME="sources">Sources</A></h2></td>
            <td align="right"><a href="#"><img src="<%= request.getContextPath() %>/images/up_arrow.jpg" width="16" height="16" border="0" alt="top" /></td>
          </tr></table>
          Click on the <b>Sources</b> link at the top of the page to bring up a separate window showing the list of sources
          included in the current release of NCI Metathesaurus.  Sources are listed alphabetically  by abbreviation, showing
          full source names and other details.
        </p>
        <p class="textbody">
          <table width="100%" cellpadding="0" cellspacing="0" border="0"><tr>
            <td><h2><A NAME="knownissues">Known Issues</A></h2></td>
            <td align="right"><a href="#"><img src="<%= request.getContextPath() %>/images/up_arrow.jpg" width="16" height="16" border="0" alt="top" /></td>
          </tr></table>
          There are important known issues in data, functionality and documentation that we are working to address for release 2.
          These include the following:
          <ul>
            <li><b>Data</b>: NCI Metathesaurus data were significantly abbreviated in the LexEVS 5.0 implementation. Of particular
            importance,
            <ul>
              <li>Relationship data are not consistently stored and linked, and are represented incompletely as described in the
              browser’s
              <a href="#" onclick="javascript:window.open('<%= request.getContextPath()%>/pages/rela_help_info.jsf',
                      '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
              Relationship Attribute Help</a>.
              This also made it impossible to reliably display individual source hierarchies within their broader NCIm context.
              <li>The display of history data has been postponed until the next release.
              <li>Other source specific concept data were also only partly loaded.
            </ul>
            These problems should be fixed with the LexEVS 5.1 release in late 2009.<br>
            <li><b>Functionality</b>: Performance problems have been partly designed around but are still an important issue that
            we intend to fix.  The ranking of search results best matches first could not achieve acceptable performance and was
            made a user option with warning attached.  The Relationship and By Source tabs are also too slow. User-settable options
            and the ability to search other concept data and relationships are needed, and should be part of forthcoming browser
            releases.
            <li><b>Documentation</b>: Online and standalone documentation are still under development.
            <li>For the latest updates of known issues, <a href="https://wiki.nci.nih.gov/display/EVS/NCI+Metathesaurus+Browser+1.0+Release+Notes" target="_blank" alt="NCI Metathesaurus Browser 1.0 Release Notes">(see NCI Metathesaurus Browser 1.0 Release Notes)</a>
          </ul>
          Please report any bugs or suggestions using the browser’s <a href="contact_us.jsf">Contact Us</a> page.
        </p>
        <p class="textbody">
          <table width="100%" cellpadding="0" cellspacing="0" border="0"><tr>
            <td><h2><A NAME="additionalinformation">Additional Information</A></h2></td>
            <td align="right"><a href="#"><img src="<%= request.getContextPath() %>/images/up_arrow.jpg" width="16" height="16" border="0" alt="top" /></td>
          </tr></table>
          Additional information about NCIm and EVS can be found on the <a href="http://evs.nci.nih.gov/">EVS Web</a>
          and <a href="https://wiki.nci.nih.gov/display/EVS/EVS+Wiki">EVS Wiki</a> sites.
        </p>
        <br>
        <%@ include file="/pages/templates/nciFooter.html" %>
      </div>
      <!-- end Page content -->
    </div>
    <div class="mainbox-bottom"><img src="<%=basePath%>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    <!-- end Main box -->
  </div>
</f:view>
</body>
</html>