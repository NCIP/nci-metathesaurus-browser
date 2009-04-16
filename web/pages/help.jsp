<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Concept" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>NCI Thesaurus Browser Home</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
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
          <A HREF="#contactus">Contact Us</A><br>
          <A HREF="#searchhelp">Search Help</A><br>
          <A HREF="#viewhierarchy">View Hierarchy</A><br>
          <A HREF="#subsets">Subsets</A><br>
          <A HREF="#additionalinfo">Additional Information</A>
        </p>
        <p class="textbody">
          <h2><A NAME="introduction">Introduction</A></h2>
          <b>NCI Thesaurus (NCIt)</b> is an extensive reference terminology with some complex features.
          Each specific meaning, such as melanoma, lung, or chemotherapy, is represented by a distinct
          concept with a unique, permanent code.  Each concept provides additional information, such as
          a preferred name, other terms and codes, definitions, and relationships with other concepts.
          Concepts are organized within major categories (kinds), such as anatomy and gene, and arranged
          in logical parent-child hierarchies from very broad top concepts down to the most specific subcategories.
        </p>
        <p class="textbody">
          <b>This help file</b> provides a basic starting point, to help use the NCIt browser effectively. It also provides pointers on how to learn more, get additional help or offer suggestions.
        </p>
        <p class="textbody">
          <h2><A NAME="contactus">Contact Us</A></h2>
          The email contact point for questions or suggestions on NCIt content, browsers, distribution files,
          or other issues is:<br>
          <br>
          &nbsp;&nbsp;&nbsp;&nbsp;
          <a href="mailto:NCIThesaurus@mail.nih.gov">NCIThesaurus@mail.nih.gov</a><br>
          <br>
          You can also use the <a href="contact_us.jsf">online</a> form available on this browser. For questions related
          to NCI’s Cancer.gov Web site, see the <a href="http://www.cancer.gov/help">Cancer.gov help page</a>. For help and
          other questions concerning NCI Enterprise Vocabulary Services (EVS), see the <a href="http://evs.nci.nih.gov/">EVS Web site</a>.
        </p>
        <p class="textbody">
          <h2><A NAME="searchhelp">Search Help</A></h2>
          <b>In the Search box</b>, enter all or part of what you are looking for and click the "Search" button. Some details:
          <ul>
            <li>You can search for a concept’s preferred name, synonyms, acronyms, or codes.
            <li>"Exact Match" is the default: Only terms or codes that are identical will match.
            <li>"Begins With" can be selected to find all terms or codes that start with the words or characters you enter.
            <li>"Contains" will search for what you enter anywhere within a term or code (e.g., "carcinoma" will match adenocarcinoma).
            <li>Search is not case sensitive (e.g., aids will match aids, Aids, and AIDS).
            <li>There are no wildcard characters.  All characters are matched literally (e.g., using "Begins With", NAT2* will match NAT2*5 Allele but not NAT2 Gene).
          </ul>
          Search of other concept data, approximate matching, and other features will be added to future releases of this
          browser. Some of these features are currently available in the <a href="http://bioportal.nci.nih.gov/ncbo/faces/index.xhtml">NCI BioPortal Browser</a>.
        </p>
        <p class="textbody">
          <b>Search results</b> are displayed by concept preferred name in alphabetical order.  Some details:
          <ul>
            <li>All matching concepts are returned.
              <ul>
                <li>NOTE: The match will often be to synonyms or codes only visible on the concept details page (e.g., searching "Begins With" melanoma will show Corneal Melanoma in the results list because that concept contains a synonym of Melanoma of the Cornea.) A future release will show these matches in the results window.
              </ul>
            <li>In the next release, concepts whose status is unusual (e.g., retired or obsolete) will show their status in parentheses in the results listing. If there are too many to show on one page, you can page through the results with a default of 50 per page. Use the "Show results per page" drop-down menu at the bottom of the results page to display more or less.
            <li>In the next release, concepts whose status is unusual (e.g., retired or obsolete) will show their status in parentheses in the results listing.
            <li>Click on the preferred name to see a concept’s details. This includes:
              <ul>
                <li>Tabbed information for the following:
                  <ul>
                    <li>Terms & Properties - including definition, synonyms, and abbreviations.
                    <li>Relationships - including parent concepts, child concepts, roles, and associations.
                    <li>Synonym Details - including the synonym term, term type, source, and code.
                    <li>View All - allows you to view the entire concept record at once.
                  </ul>
                <li>View hierarchy button - click on this to see where the concept exists within the NCI Thesaurus hierarchy.
                <li>View History button - click on this to view a history of edit actions on this concept, including dates and reference concepts.
              </ul>
            <li>If there is only one match, the concept details page is shown directly without first listing results.
          </ul>
        </p>
        <p class="textbody">
          <h2><A NAME="viewhierarchy">View Hierarchy</A></h2>
          <ul>
            <li>Click on the View Hierarchy link at the top of the page to bring up a separate window showing the NCI Thesaurus hierarchy.
            <li>Browse through the levels by clicking on the + next to each concept.
            <li>Click on the concept name itself to see the concept’s details in the main browser window.
          </ul>
        </p>
        <p class="textbody">
          <h2><A NAME="subsets">Subsets</A></h2>
          <ul>
            <li>Click on the Subsets link at the top of the page to read about and link to NCI Thesaurus Subsets.
          </ul>
        </p>
        <p class="textbody">
          <h2><A NAME="additionalinfo">Additional Information</A></h2>
          Several journal articles describe NCIt in greater detail.  These are listed in the <a href="http://evs.nci.nih.gov/aboutEVS">About EVS</a> page on the EVS Web site.
          (Need to make available, describe, and link to other NCIt documentation.  This section will be provided soon.)
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