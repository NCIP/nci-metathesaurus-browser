<!-- Thesaurus, banner search area -->
<div class="bannerarea">
    <div class="banner"><a href="<%=basePath%>"><img src="<%=basePath%>/images/thesaurus_browser_logo.gif" width="383" height="130" alt="Thesaurus Browser Logo" border="0"/></a></div>
    <div class="search-empty">
        <table border="0" height="105px" class="global-nav">
          <tr>
            <td valign="bottom">
              <a href="<%= request.getContextPath() %>">Simple Search</a>        
            </td>
          </tr>         
        </table>
        <%@ include file="menuBar.jsp" %>
    </div>
</div>
<!-- end Thesaurus, banner search area -->
<!-- Quick links bar -->
<%@ include file="quickLink.jsp" %>
<!-- end Quick links bar -->