<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
L--%>

<!-- Thesaurus, banner search area -->
<div class="bannerarea">
    <div class="banner"><a href="<%=basePath%>"><img src="<%=basePath%>/images/thesaurus_browser_logo.gif" width="383" height="130" alt="Thesaurus Browser Logo" border="0"/></a></div>
    <div class="search-globalnav">
        <!-- Search box -->
        <div class="searchbox-top"><img src="<%=basePath%>/images/searchbox-top.gif" width="352" height="2" alt="SearchBox Top" /></div>
        <div class="searchbox">
            <%@ include file="searchForm.jsp" %>
        </div>
        <div class="searchbox-bottom"><img src="<%=basePath%>/images/searchbox-bottom.gif" width="352" height="2" alt="SearchBox Bottom" /></div>
        <!-- end Search box -->
        <!-- Global Navigation -->
            <%@ include file="menuBar.jsp" %>
        <!-- end Global Navigation -->
    </div>
</div>
<!-- end Thesaurus, banner search area -->
<!-- Quick links bar -->
<%@ include file="quickLink.jsp" %>
<!-- end Quick links bar -->