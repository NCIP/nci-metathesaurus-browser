package gov.nih.nci.evs.browser.utils;

import gov.nih.nci.evs.browser.properties.*;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction 
 * with the National Cancer Institute, and so to the extent government 
 * employees are co-authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 *   1. Redistributions of source code must retain the above copyright 
 *      notice, this list of conditions and the disclaimer of Article 3, 
 *      below. Redistributions in binary form must reproduce the above 
 *      copyright notice, this list of conditions and the following 
 *      disclaimer in the documentation and/or other materials provided 
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution, 
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National 
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must 
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not 
 *      authorize the recipient to use any trademarks owned by either NCI 
 *      or NGIT 
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED 
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE 
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

public class MailUtils extends Object {
    private static final long serialVersionUID = 1L;

    public static String getProperty(String property, String propertyName)
            throws Exception {
        String value = null;
        try {
            value = NCImBrowserProperties.getProperty(property);
        } catch (Exception e) {
            throw new Exception("Error reading \"" + propertyName
                + "\" property.");
        }
        return value;
    }

    public static String[] getRecipients() throws Exception {
        String value =
            getProperty(NCImBrowserProperties.NCICB_CONTACT_URL,
                "ncicb.contact.url");
        return Utils.toStrings(value, ";", false);
    }

    public static String getMailSmtpServer() throws Exception {
        String value =
            getProperty(NCImBrowserProperties.MAIL_SMTP_SERVER,
                "mail.smtp.server");
        return value;
    }

    public static boolean isValidEmailAddress(String text) {
        int posOfAtChar = text.indexOf('@');
        int posOfDotChar = text.lastIndexOf('.');

        if (posOfAtChar <= 0 || posOfDotChar <= 0)
            return false;
        if (posOfAtChar > posOfDotChar)
            return false;
        if (posOfAtChar == posOfDotChar - 1)
            return false;
        if (posOfDotChar == text.length() - 1)
            return false;
        return true;
    }

    private static void postMailValidation(String mail_smtp_server,
        String from, String recipients[], String subject, String message)
            throws Exception {
        StringBuffer error = new StringBuffer();
        String indent = "    ";
        int ctr = 0;

        if (mail_smtp_server == null || mail_smtp_server.length() <= 0) {
            error.append(indent + "* mail host\n");
            ++ctr;
        }
        if (subject == null || subject.length() <= 0) {
            error.append(indent + "* subject of your email\n");
            ++ctr;
        }
        if (message == null || message.length() <= 0) {
            error.append(indent + "* message\n");
            ++ctr;
        }
        if (from == null || from.length() <= 0) {
            error.append(indent + "* e-mail address\n");
            ++ctr;
        }
        if (error.length() > 0) {
            String s = "Warning: Your message was not sent.\n";
            if (ctr > 1)
                s += "The following fields were not set:\n";
            else
                s += "The following field was not set:\n";
            error.insert(0, s);
            throw new Exception(error.toString());
        }

        if (!isValidEmailAddress(from)) {
            error.append("Warning: Your message was not sent.\n");
            error.append(indent + "* Invalid e-mail address.");
            throw new Exception(error.toString());
        }
    }

    public static void postMail(String from, String recipients[],
        String subject, String message) throws MessagingException, Exception {
        String mail_smtp_server = getMailSmtpServer();
        postMailValidation(mail_smtp_server, from, recipients, subject, message);

        // Sets the host smtp address.
        Properties props = new Properties();
        props.put("mail.smtp.host", mail_smtp_server);

        // Creates some properties and get the default session.
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(false);

        // Creates a message.
        Message msg = new MimeMessage(session);

        // Sets the from and to addresses.
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);
        msg.setRecipient(Message.RecipientType.BCC, addressFrom);

        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++)
            addressTo[i] = new InternetAddress(recipients[i]);
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        // Optional: You can set your custom headers in the email if you want.
        msg.addHeader("MyHeaderName", "myHeaderValue");

        // Setting the Subject and Content Type.
        msg.setSubject(subject);
        msg.setContent(message, "text/plain");
        Transport.send(msg);
    }
}
