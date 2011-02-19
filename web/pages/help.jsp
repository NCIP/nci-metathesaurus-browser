<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%
  String ncit_url = new DataUtils().getNCItURL();
  String lexevs_version = new DataUtils().getLexVersion();
%>
<html xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
  <title>NCI Metathesaurus</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
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

<f:view>
  <!-- Begin Skip Top Navigation -->
    <a href="#evs-content" class="hideLink" accesskey="1" title="Skip repetitive navigation links">skip navigation links</A>
  <!-- End Skip Top Navigation -->
  <%@ include file="/pages/include/header.jsp" %>
  <div class="center-page">
    <%@ include file="/pages/include/sub-header.jsp" %>
    <!-- Main box -->
    <div id="main-area">
      <%@ include file="/pages/include/content-header.jsp" %>
      <!-- Page content -->
      <div class="pagecontent">
        <a name="evs-content" id="evs-content"></a>
        <!-- ======================================= -->
        <!--                 HELP CONTENT            -->
        <!-- ======================================= -->
        <div class="texttitle-blue">Help</div>
        <p class="textbody">
          <A HREF="#introduction">Introduction</A><br>
          <A HREF="#searchhelp">Search</A><br>
          <A HREF="#searchavdhelp">Advanced Search</A><br>
          <A HREF="#searchresults">Search Results</A><br>
          <A HREF="#conceptdetails">Concept Details</A><br>
          <A HREF="#hierarchy">View Hierarchy</A><br>
          <A HREF="#sources">Sources</A><br>
          <A HREF="#knownissues">Known Issues</A><br>
          <A HREF="#additionalinformation">Additional Information</A>
        </p>
        <p class="textbody">
          <table width="725px" cellpadding="0" cellspacing="0" border="0"><tr>
            <td><h2><A NAME="introduction">Introduction</A></h2></td>
            <td align="right"><a href="#"><img src="<%= request.getContextPath() %>/images/up_arrow.jpg" width="16" height="16" border="0" alt="top" /></a></td>
          </tr></table>
          <b>NCI Metathesaurus (NCIm)</b> is a comprehensive biomedical terminology database, connecting
          3,600,000 terms from more than 70 terminologies. It contains most public domain vocabularies from
          the National Library of Medicine's <a href="http://www.nlm.nih.gov/research/umls/umlsmain.html" target="_blank">UMLS Metathesaurus</a>,
          as well as many other biomedical vocabularies created by or of interest to NCI and its partners, including some propriety
          vocabularies with restrictions on their use
          (see <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
          '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
          NCIm Sources</a>).
          <br><br>
          <b>The NCIm Browser</b>
          is for the retrieval of concepts from the NCI Metathesaurus, and for viewing the contents, structure,
          and cross mappings of individual source terminologies. It is designed for ease of use by a diverse
          user community. This release adds some advanced search options, additional information from source
          terminologies, and other features requested by users.
          <a href="<%= request.getContextPath() %>/pages/contact_us.jsf">Contact Us</a>
          to provide feedback and get additional help on the NCIm Browser.
          <br><br>
          <b>This help file</b> provides basic information about how to use the NCIm Browser effectively. It also provides
          pointers on how to learn more about NCIm and related resources. The following typeface font conventions are used
          for describing search and the browser interface:
          <ul>
            <li><b>Bold</b>: Browser links, buttons, page tabs, and drop-down boxes.
            <li><span style="font-family:monospace;">Fixed Width:</span> Search strings
            <li><span style="font-style:italic;">Italics</span>: Concept terms
          </ul>
        </p>
        <p class="textbody">
          <table width="725px" cellpadding="0" cellspacing="0" border="0"><tr>
            <td><h2><A NAME="searchhelp">Search</A></h2></td>
            <td align="right"><a href="#"><img src="<%= request.getContextPath() %>/images/up_arrow.jpg" width="16" height="16" border="0" alt="top" /></a></td>
          </tr></table>
          <b>In the Search box,</b>
          enter all or part of what you are looking for, set what and how you want to search, and click
          the <b>Search</b> button. Some details:
          <!-- ************* Search ************** -->
          <ul>
            <li><b>Text Box:</b> Enter the exact string of characters you want to search for.
            <ul>
              <li>Search is not case sensitive (e.g., aids will match <i>aids</i>, <i>Aids</i>, and <i>AIDS</i>).
              <li>There are no wildcard characters. All characters are matched literally (e.g., searching
              for <b>Begins With</b> <span style="font-family:monospace;">NAT2*</span> will match <i>NAT2*5 Allele</i> but not <i>NAT2 Gene</i>).
              <li>Do not use quotes - they will be searched for literally, as characters to be matched.
              <li>Searching for multiple words does not search on each word separately. To match, all
              words have to be found in the same order you provided. For example, if you do a <b>Contains</b>
              search on <span style="font-family:monospace;">Melanoma Corneal</span> no results will be returned, but if you search on
              <span style="font-family:monospace;">Corneal Melanoma</span> you get the detail page
              for <i>Corneal Melanoma</i>.
            </ul>
            <li><b>Match method radio buttons</b> select how your search string will be matched.
            <ul>
              <li><b>Exact Match</b> is the default: Only terms or codes that are identical will match.
              <li><b>Begins With</b> can be selected to find all terms or codes that start with the words
              or characters you enter.
              <li><b>Contains</b> will search for what you enter anywhere within a term or code (e.g.,
              <span style="font-family:monospace;">carcinoma</span> will match <i>adenocarcinoma</i>).
              <li>Concept Codes will only match if they exactly match what you enter, even if you
              select <b>Begins With</b> or <b>Contains</b>.
            </ul>
            <li><b>Match target radio buttons</b> select what category of concept information is searched
            <ul>
              <li><b>Name/Code</b> is the default: Search text is matched to a concept's preferred name, synonyms, acronyms, or codes. Unless stated otherwise, all search examples in this Help page use the default name/code search.
              <li><b>Property</b> will match to other direct property attributes of a concept, such as definitions.
              <li><b>Relationship</b> will return concepts that have relationships to concepts that match
              by name/code (e.g., an exact relationship search on <span style="font-family:monospace;">toe</span>
              does not return the concept <i>toe</i>, but does return <i>toenail</i> and other related concepts).
            </ul>
            <li><b>Source</b> drop-down box: You can choose to limit your search to concepts with terms from
            a specific source. For example, if you only want to find <i>breast cancer</i> concepts that
            include terms from SNOMEDCT, you would choose <span style="font-family:monospace;">SNOMEDCT</span>
            from the <b>Source</b> box. This will return concepts with at least one SNOMDEDCT term and at
            least one term that matches your search criteria, even if the matching terms are not from the
            selected source. This search method is helpful for users looking for source coverage of a term
            that may be expressed differently in that source (e.g., searching for grey for source FDA will
            return <i>Gray color</i>, even though the only FDA term is the American spelling gray).
            <li><b>Source Hierarchy button</b>: If you select a source with a concept hierarchy that can
            be viewed, an icon will appear to the right of the source selection box; click on the icon to
            view the hierarchy in a separate window.
            <li><b>Search</b> button starts a search.
            <li><b>"?"</b> button takes you to this Search section of the Help file.
          </ul>
        </p>
        <p class="textbody">
          <table width="725px" cellpadding="0" cellspacing="0" border="0"><tr>
            <td><h2><A NAME="searchavdhelp">Advanced Search</A></h2></td>
            <td align="right"><a href="#"><img src="<%= request.getContextPath() %>/images/up_arrow.jpg" width="16" height="16" border="0" alt="top" /></a></td>
          </tr></table>
          <!-- ************* Advanced Search ************** -->
          The <b>Advanced Search</b> link, at the bottom right corner of the main search box, leads to a
          separate page.  Most options and selections are the same as those described under "Search" above.
          The key differences are:
          <ul>
            <li><b>Code</b> and <b>Name</b> are searched separately.
            <li><b>Property</b> search offers a drop down list of all specific properties in NCIm, so that
            they can be selected and searched individually.
            <li><b>Relationship</b> search offers drop down lists of broad relationship labels (RELs) and
            specific relationship attributes (RELAs) that can be searched on, as explained in the
            <a href="#" onclick="javascript:window.open('<%= request.getContextPath()%>/pages/rela_help_info.jsf',
            '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
             NCIm Relationship Attributes</a>
            help file. Note that many relationships have no source-assigned RELA value. For example, searching
            on isa RELA value will retrieve only those specifically tagged as such and not others having only a
            broader PAR (Parent) REL relationship.
          </ul>
          Advanced Search options will continue to grow in future releases.
        </p>
        <p class="textbody">
          <table width="725px" cellpadding="0" cellspacing="0" border="0"><tr>
            <td><h2><A NAME="searchresults">Search Results</A></h2></td>
            <td align="right"><a href="#"><img src="<%= request.getContextPath() %>/images/up_arrow.jpg" width="16" height="16" border="0" alt="top" /></a></td>
          </tr></table>
          <!-- ************* Seach Results ************** -->
          <b>Search results</b> are displayed by concept name. (If there is only one match, the concept details page
          is shown directly without first listing results.) Some details:
          <ul>
            <li>All matching concepts are returned.
            <li>Results are listed from best match to weakest. For example, a <b>Contains </b> search on <i>melanocyte</i>
            returns <i>melanocyte</i> at the top followed by concepts with two word matches (e.g., <i>Spindle Melanocyte</i>),
            followed by concepts whose terms have more non-<i>melanocyte</i> content. An additional column displays the semantic type(s) assigned to each concept.
            <li>The match will often be to synonyms or codes only visible on the concept details page (e.g., searching
            <b>Begins With</b> <i>melanoma</i> will show <i>Cutaneous Melanoma</i> in the results list because that concept contains a synonym of <i>Melanoma of the Skin</i>.)
            <li>If there are too many results to show on one page, you can page through the results with a default of 50 per page. To
            change the default number, use the <b>Show results per page</b> drop-down menu at the bottom of the results page.
            <li>Click on the name to see a concept’s details.
          </ul>
        </p>
        <p class="textbody">
          <table width="725px" cellpadding="0" cellspacing="0" border="0"><tr>
            <td><h2><A NAME="conceptdetails">Concept Details</A></h2></td>
            <td align="right"><a href="#"><img src="<%= request.getContextPath() %>/images/up_arrow.jpg" width="16" height="16" border="0" alt="top" /></a></td>
          </tr></table>
          <!-- ************* Concept Details ************** -->
          Detailed information on the selected concept is grouped and shown on several related pages:
         <ul>
            <li>Tabbed information gives the concept’s meaning, labels, and direct relationships:
            <ul>
              <li><b>Terms & Properties</b>: Gives definitions, synonyms, abbreviations, codes, and other information.
              <li><b>Relationships</b>: Shows how other concepts are directly related to this concept, where a source
              asserts that terms in those concepts are related as parents, children, or in other ways.
              <li><b>Synonym Details</b>: For each term or abbreviation, shows its source, term type, and code.
              <li><b>By Source:</b> Shows concept information one source at a time. NCI Thesaurus content is shown
              if available, otherwise the initial source is chosen alphabetically. To view concept information from
              a different source, select the link for that source at the top of the page. NOTE: Select the <b>?</b>
              icon next to the Source header to view a list of the source abbreviations and full names. The Relationships
              section shows the specific term being pointed to by relationships from the selected source, including that
              term’s source (sometimes different), type, and code.
              <li><b>View All</b>: Combines all of the above information on a single page.
            </ul>
            <li><b>In NCIt Hierarchy</b>: Click the button to see where the concept is found within the NCIt hierarchy as
            presented through NCIm concepts. Concepts are often found in several different places in the hierarchy. The focus concept will
            be bold, underlined, and colored red.  This function only appears in concepts with NCIt content.  Hierarchy
            displays from other sources will be provided in a later browser release.
         </ul>
        </p>
        <p class="textbody">
          <table width="725px" cellpadding="0" cellspacing="0" border="0"><tr>
            <td><h2><A NAME="hierarchy">View Hierarchy</A></h2></td>
            <td align="right"><a href="#"><img src="<%= request.getContextPath() %>/images/up_arrow.jpg" width="16" height="16" border="0" alt="top" /></a></td>
          </tr></table>
          <!-- ************* View Hierarchy ************** -->
          Many sources have hierarchies of concepts, arranged from the most general at the top to the most
          specific at the bottom leaf nodes. Most common are "is-a" hierarchies, where child concepts are
          subtypes of their parent(s) (e.g., Mold is-a Fungus), but other relationships, such as "part-of",
          are also used by some source terminologies. The browser provides several ways of viewing hierarchies
          when they are available.
          <br><br>
          Click on the <b>View NCIt Hierarchy</b> link at the top of the page to bring up a separate window showing the
          NCI Thesaurus hierarchy as presented through NCIm concepts. Some details:
          <ul>
            <li>At first, only the top level nodes of the hierarchy are shown.
            <li>At each level, concepts are listed alphabetically by concept name.
            <li>Browse through the levels by clicking on the + next to each concept.
            <li>Click on the concept name itself to see the concept’s details in the main browser window.
          </ul>
          <b>Source</b> drop-down box: If you select a source with a concept hierarchy that can be viewed, an
          icon will appear to the right of the source selection box; click on the icon to view the hierarchy
          in a separate window.
        </p>
        <p class="textbody">
          <table width="725px" cellpadding="0" cellspacing="0" border="0"><tr>
            <td><h2><A NAME="sources">Sources</A></h2></td>
            <td align="right"><a href="#"><img src="<%= request.getContextPath() %>/images/up_arrow.jpg" width="16" height="16" border="0" alt="top" /></a></td>
          </tr></table>
          <!-- ************* Sources ************** -->
          Click on the
          <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/source_help_info.jsf',
          '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
          <b>Sources</b></a>
          link at the top of the page to bring up a separate window showing the list of sources
          included in the current release of NCI Metathesaurus.  Sources are listed alphabetically  by abbreviation,
          showing full source names and other details. Use restrictions are described for proprietary sources.
          This page is also displayed if you click on the <b>?</b> icon above  the source information in the
          <b>Relationships, Synonym Details,</b> and <b>By Source</b> tabs.
        </p>
        <p class="textbody">
          <table width="725px" cellpadding="0" cellspacing="0" border="0"><tr>
            <td><h2><A NAME="knownissues">Known Issues</A></h2></td>
            <td align="right"><a href="#"><img src="<%= request.getContextPath() %>/images/up_arrow.jpg" width="16" height="16" border="0" alt="top" /></a></td>
          </tr></table>
          <!-- ************* Known Issues ************** -->
          This release, based on LexEVS <%=lexevs_version %>, addresses most known data and performance issues. We are still
          working on remaining issues of data, functionality and documentation.  For the latest updates of
          known issues, see
          <a href="https://wiki.nci.nih.gov/display/EVS/NCI+Metathesaurus+Browser+2.0+Release+Notes" target="_blank">
          NCI Metathesaurus Browser 2.0 Release Notes.
          </a>
          <br><br>
          Please report any bugs or suggestions using the browser's <a href="contact_us.jsf">Contact Us</a> page.
          Suggestions to add a new concept or make changes to an existing concept can also be made using the
          <b>Term Suggestion</b> link below the Search box or the <b>Suggest changes to this concept</b> link
          in the upper right of all concept details pages.
        </p>
        <p class="textbody">
          <table width="725px" cellpadding="0" cellspacing="0" border="0"><tr>
            <td><h2><A NAME="additionalinformation">Additional Information</A></h2></td>
            <td align="right"><a href="#"><img src="<%= request.getContextPath() %>/images/up_arrow.jpg" width="16" height="16" border="0" alt="top" /></a></td>
          </tr></table>
          <!-- ************* Additional Information ************** -->
          Additional information about NCIm and EVS can be found on the
          <a href="http://evs.nci.nih.gov/" target="_blank">EVS Web</a>,
          <a href="https://wiki.nci.nih.gov/display/EVS/EVS+Wiki" target="_blank">EVS Wiki</a>
          sites and
          <a href="https://cabig.nci.nih.gov/concepts/EVS/" target="_blank">EVS caBIG Web</a>,
          sites.
        </p>
        <br>
        <%@ include file="/pages/include/nciFooter.jsp" %>
      </div>
      <!-- end Page content -->
    </div>
    <div class="mainbox-bottom"><img src="<%=basePath%>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    <!-- end Main box -->
  </div>
</f:view>
</body>
</html>