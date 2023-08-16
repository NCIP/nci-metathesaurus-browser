<!-- Thesaurus, banner search area -->
<div class="bannerarea">
    <a href="<%=basePath%>" style="text-decoration: none;">
      <div class="vocabularynamebanner">
        <span class="vocabularynamelong"><%=DataUtils.getApplicationVersionJspDisplay()%></span>
      </div>
    </a>

    <div class="search-globalnav">
        <!-- Global Navigation -->
            <img src="<%=basePath%>/images/shim.gif" width="1" height="100" alt="Shim" border="0" />
            <%@ include file="menuBar.jsp" %>
        <!-- end Global Navigation -->
    </div>
</div>
<!-- end Thesaurus, banner search area -->
<!-- Quick links bar -->
<%@ include file="quickLink.jsp" %>
<!-- end Quick links bar -->
