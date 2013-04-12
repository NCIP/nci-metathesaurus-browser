<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
L--%>

<!-- Thesaurus, banner search area -->
<div class="bannerarea">
    <div class="banner"><a href="<%=basePath%>"><img src="<%=basePath%>/images/thesaurus_browser_logo.gif" width="383" height="130" alt="Thesaurus Browser Logo" border="0"/></a></div>
    <div class="search-empty">
        <table border="0" height="105px" width="100%" class="global-nav">
          <tr>
            <td valign="middle" align="center">
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