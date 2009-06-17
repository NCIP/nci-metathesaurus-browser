<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Concept" %>
<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
  <head>
    <title>NCI MetaThesaurus</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
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
  <body>
    <f:view>
      <%@ include file="/pages/templates/header.xhtml" %>
      <div class="center-page">
        <%@ include file="/pages/templates/sub-header.xhtml" %>
        <div id="main-area">
          <%@ include file="/pages/templates/content-header.xhtml" %>
          <div class="pagecontent">
            <div class="texttitle-blue">Contact Us</div>
            <hr></hr>
            <p><b>If you would like information immediately, please call:</b></p>
            <p>
            &nbsp;&nbsp;&nbsp;&nbsp;<b>1-800-4-CANCER (1-800-422-6237)</b><br/>
            &nbsp;&nbsp;&nbsp;&nbsp;<i>9:00 a.m. to 4:30 p.m. local time, Monday through Friday</i><br/>
            &nbsp;&nbsp;&nbsp;&nbsp;TTY 1-800-332-8615<br/>
            </p>
            <p>
              <b>
                The email contact point for questions or suggestions on NCIt
                content, browsers, distribution files, or other issues is:
                <br/><br/>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a href="mailto:NCIThesaurus@mail.nih.gov">NCIThesaurus@mail.nih.gov</a>
                <br/><br/>
              </b>
              For questions related to NCI’s Cancer.gov Web site,
              see the
              <a href="http://www.cancer.gov/help" target="_blank" alt="Cancer.gov help">Cancer.gov help page</a>. &nbsp;
              For help and other questions concerning NCI Enterprise Vocabulary
              Services (EVS),
              see the <a href="http://evs.nci.nih.gov/" target="_blank" alt="EVS">EVS Web site</a>.
            </p>

            <%
              String color = "";
              if (error)
                color = "style=\"color:#FF0000;\"";
            %>
            <p><b <%= color %>>You must fill in every box below.</b>
              <%
                if (errorMsg != null && errorMsg.length() > 0) {
              %>
                  <br/><br/><i style="color:#FF0000;"><%= errorMsg %></i>
              <%
                }
              %>
            </p>
            <form method="post">
              <p>
                <% if (error) %> <i style="color:#FF0000;">* Required)</i>
                <i>Enter the subject of your email:</i>
              </p>
              <input CLASS="input.formField" size="100" name="subject" alt="Subject" value="<%= subject %>" onFocus="active = true" onBlur="active = false" onKeyPress="return ifenter(event,this.form)">
              <p>
                <% if (error) %> <i style="color:#FF0000;">* Required)</i>
                <i>Enter your message:<br/>
                  &nbsp;&nbsp;&nbsp;&nbsp;Please include all pertinent details within the contact message box.<br/>
                  &nbsp;&nbsp;&nbsp;&nbsp;We do not open attachments to e-mail messages.
                </i>
              </p>
              <TEXTAREA Name="message" alt="Message" rows="4" cols="75"><%= message %></TEXTAREA>
              <p>
                <% if (error) %> <i style="color:#FF0000;">* Required)</i>
                <i>Enter your e-mail address:<br/>
                  &nbsp;&nbsp;&nbsp;&nbsp;For example, jdoe@yahoo.com
                </i>
              </p>
              <input CLASS="input.formField" size="100" name="emailaddress" alt="Email Address" value="<%= emailaddress %>" onFocus="active = true" onBlur="active = false" onKeyPress="return ifenter(event,this.form)">
              <br/><br/>

              <h:commandButton
                id="mail"
                value="Submit"
                alt="Submit"
                action="#{userSessionBean.contactUs}" >
              </h:commandButton>
              &nbsp;&nbsp;<INPUT type="reset" value="Clear" alt="Clear">
            </form>
            <a href="http://www.cancer.gov/policies/page3" target="_blank" alt="Privacy Policy"><i>Privacy Policy on E-mail Messages Sent to the NCI Web Site</i></a>
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
