<!-- Thesaurus, banner search area -->
<div class="bannerarea">
    <a href="<%=basePath%>" style="text-decoration: none;">
      <div class="vocabularynamebanner">
        <span class="vocabularynamelong"><%=DataUtils.getApplicationVersionJspDisplay()%></span>
      </div>
    </a>    
    <div class="search-globalnav">
        <!-- Search box -->
        <div class="searchbox-top"><img src="<%=basePath%>/images/searchbox-top.gif" width="352" height="2" alt="SearchBox Top" /></div>
        <div class="searchbox">
            <%@ include file="searchFormNoAdvancedLink.jsp" %>
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