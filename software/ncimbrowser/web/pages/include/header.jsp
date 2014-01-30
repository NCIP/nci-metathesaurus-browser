<%
  String basePath = request.getContextPath();
%>
<!-- nci banner -->
<div class="ncibanner">
  <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute">
    <img src="<%= request.getContextPath() %>/images/logotype.gif"
      width="556" height="39" border="0"
      alt="National Cancer Institute" />
  </a>
  <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute">
    <img src="<%= request.getContextPath() %>/images/spacer.gif"
      width="60" height="39" border="0" 
      alt="National Cancer Institute" class="print-header" /></a>
  <a href="http://www.nih.gov" target="_blank" alt="U.S. National Institutes of Health">
    <img src="<%= request.getContextPath() %>/images/tagline_nologo.gif"
      width="219" height="39" border="0"
      alt="U.S. National Institutes of Health" />
  </a>
  <a href="http://www.cancer.gov" target="_blank" alt="www.cancer.gov">
    <img src="<%= request.getContextPath() %>/images/cancer-gov.gif"
      width="125" height="39" border="0"
      alt="www.cancer.gov" />
  </a>
</div>
<!-- end nci banner -->
