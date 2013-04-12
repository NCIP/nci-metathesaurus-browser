<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
L--%>

<%
  String basePath = request.getContextPath();
%>
<!-- nci banner -->
<div class="ncibanner">
  <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute">
    <img src="<%= request.getContextPath() %>/images/logotype.gif"
      width="440" height="39" border="0"
      alt="National Cancer Institute" />
  </a>
  <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute">
    <img src="<%= request.getContextPath() %>/images/spacer.gif"
      width="48" height="39" border="0" 
      alt="National Cancer Institute" class="print-header" /></a>
  <a href="http://www.nih.gov" target="_blank" alt="U.S. National Institutes of Health">
    <img src="<%= request.getContextPath() %>/images/tagline_nologo.gif"
      width="173" height="39" border="0"
      alt="U.S. National Institutes of Health" />
  </a>
  <a href="http://www.cancer.gov" target="_blank" alt="www.cancer.gov">
    <img src="<%= request.getContextPath() %>/images/cancer-gov.gif"
      width="99" height="39" border="0"
      alt="www.cancer.gov" />
  </a>
</div>
<!-- end nci banner -->