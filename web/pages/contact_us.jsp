<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
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
  <%
    String ncicb_contact_url = new DataUtils().getNCICBContactURL();
    String subject = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS(request.getParameter("subject"));
    String message = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS(request.getParameter("message"));
    String emailaddress = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS(request.getParameter("emailaddress"));
    if (subject == null) subject = "";
    if (message == null) message = "";
    if (emailaddress == null) emailaddress = "";
    String errorMsg = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getAttribute("errorMsg"));
    if (errorMsg == null) errorMsg = "";
    boolean error = errorMsg.length() > 0;
  %>
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
        <div id="main-area">
          <%@ include file="/pages/include/content-header.jsp" %>
          <div class="pagecontent">
            <a name="evs-content" id="evs-content"></a>
            <div class="texttitle-blue">Contact Us</div>
            <hr></hr>
            <p>
              <b>You can request help or make suggestions by filling out the online form below, or by
              using any one of these contact points:</b>
            </p>
            <table class="textbody">
              <tr>
                <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                <td>Telephone:</td>
                <td>301.451.4384 or Toll-Free: 888.478.4423</td>
              </tr>
              <tr>
                <td/>
                <td>Email:</td>
                <td><a href="mailto:ncicb@pop.nci.nih.gov">ncicb@pop.nci.nih.gov</a></td>
              </tr>
              <tr>
                <td/>
                <td>Web Page:</td>
                <td><a href="http://ncicb.nci.nih.gov/support" target="_blank" alt="NCICB Support">http://ncicb.nci.nih.gov/support</a></td>
              </tr>
            </table>
            <p>
              Telephone support is available Monday to Friday, 8 am – 8 pm
              Eastern Time, excluding government holidays. You may leave a
              message, send an email, or submit a support request via the Web
              at any time.  Please include:
              <ul>
                <li>Your contact information;</li>
                <li>Reference to the NCIm Browser; and</li>
                <li>A detailed description of your problem or suggestion.</li>
              </ul>

              For questions related to NCI’s Cancer.gov Web site,
              see the
              <a href="http://www.cancer.gov/help" target="_blank" alt="Cancer.gov help">
                Cancer.gov help page</a>. &nbsp;
              For help and other questions concerning NCI Enterprise Vocabulary
              Services (EVS),
              see the <a href="http://evs.nci.nih.gov/" target="_blank" alt="EVS">
                EVS Web site</a>.
            </p>
            <%
              String color = "";
              if (error)
                color = "style=\"color:#FF0000;\"";
            %>
            <p><b <%= color %>>Online Form</b></p>
            <p <%= color %>>
            To use this web form, please fill in every box below and then click on “Submit”.
              <%
                if (errorMsg != null && errorMsg.length() > 0) {
                    errorMsg = errorMsg.replaceAll("&lt;br/&gt;", "\n");
                    String[] list = Utils.toStrings(errorMsg, "\n", false, false);
                    for (int i=0; i<list.length; ++i) {
                      String text = list[i];
                      text = Utils.toHtml(text); // For leading spaces (indentation)
              %>
                      <br/><i style="color:#FF0000;"><%= text %></i>
              <%
                    }
                }
              %>
            </p>
            <h:form>
              <p>
                <% if (error) %> <i style="color:#FF0000;">* Required)</i>
                <i><label for="subject">Subject of your email:</label></i>
              </p>
              <input CLASS="input.formField" size="100" name="subject" id="subject" alt="Subject" value="<%= subject %>" onFocus="active = true" onBlur="active = false" onKeyPress="return ifenter(event,this.form)">
              <p>
                <% if (error) %> <i style="color:#FF0000;">* Required)</i>
                <i><label for="message">Detailed description of your problem or suggestion (no attachments):</label></i>
              </p>
              <TEXTAREA Name="message" id="message" alt="Message" rows="4" cols="75"><%= message %></TEXTAREA>
              <p>
                <% if (error) %> <i style="color:#FF0000;">* Required)</i>
                <i><label for="emailaddress">Enter your e-mail address:</label></i>
              </p>
              <input CLASS="input.formField" size="100" name="emailaddress" id="emailaddress" alt="Email Address" value="<%= emailaddress %>" onFocus="active = true" onBlur="active = false" onKeyPress="return ifenter(event,this.form)">
              <br/><br/>

              <h:commandButton
                id="mail"
                value="Submit"
                alt="Submit"
                action="#{userSessionBean.contactUs}" >
              </h:commandButton>
              &nbsp;&nbsp;<INPUT type="reset" value="Clear" alt="Clear">
            </h:form>
            <a href="http://www.cancer.gov/policies/page3" target="_blank" alt="Privacy Policy"><i>Privacy Policy on E-mail Messages Sent to the NCI Web Site</i></a>
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
