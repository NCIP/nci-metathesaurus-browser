/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.servlet;

/**
 * 
 */

/**
 * @author EVS Team
 * @version 1.0
 *
 * Modification history
 *     Initial implementation kim.ong@ngc.com
 *
 */

import org.json.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import gov.nih.nci.evs.browser.utils.*;
import org.apache.log4j.*;

import gov.nih.nci.evs.browser.bean.LicenseBean;
import gov.nih.nci.evs.browser.properties.NCImBrowserProperties.*;
import gov.nih.nci.evs.browser.utils.MetadataUtils;
import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.properties.*;



public final class RedirectServlet extends HttpServlet {
    private static Logger _logger = Logger.getLogger(RedirectServlet.class);

    /**
     * Validates the Init and Context parameters, configures authentication URL
     *
     * @throws ServletException if the init parameters are invalid or any other
     *         problems occur during initialisation
     */
    public void init() throws ServletException {

    }

    /**
     * Route the user to the execute method
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        execute(request, response);
    }

    /**
     * Route the user to the execute method
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        execute(request, response);
    }

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

		LicenseBean licenseBean = (LicenseBean) request.getSession().getAttribute("licenseBean");
		if (licenseBean == null) {
		    licenseBean = new LicenseBean();
		    request.getSession().setAttribute("licenseBean", licenseBean);
		}

        String nciterm_browser_url = NCImBrowserProperties.getTermBrowserURL();

        //KLO, 020413
        String action = HTTPUtils.cleanXSS((String) request.getParameter("action"));
        String dictionary = HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
        String code = HTTPUtils.cleanXSS((String) request.getParameter("code"));
        String term_source = HTTPUtils.cleanXSS((String) request.getParameter("sab"));
        String type = HTTPUtils.cleanXSS((String) request.getParameter("type"));

	    boolean licenseAgreementAccepted = false;
	    String formal_name = MetadataUtils.getSABFormalName(term_source);
	    boolean isLicensed = DataUtils.checkIsLicensed(term_source);

	    String cs_name = Constants.CODING_SCHEME_NAME;

	    if (term_source != null && isLicensed ) {
	        //formal_name = MetadataUtils.getSABFormalName(term_source);
	        licenseAgreementAccepted = licenseBean.licenseAgreementAccepted(formal_name);
	    }

	    if (!isLicensed) {
	        licenseAgreementAccepted = true;
	    }

        if (action.equals("details")) {
		      if (!licenseAgreementAccepted) {
				  String url = request.getContextPath() + "/pages/accept_license.jsf?"
														+ "dictionary=" + formal_name
														+ "&code=" + code;
				  response.sendRedirect(response.encodeRedirectURL(url));

 	          } else {

				  String url = nciterm_browser_url + "/ConceptReport.jsp?"
														+ "dictionary=" + formal_name
														+ "&code=" + code
														+ "&type=" + type
														+ "&sortBy=name#SynonymsDetails";

				  response.sendRedirect(response.encodeRedirectURL(url));
              }

		} else if (action.equals("tree")) {
		      if (!licenseAgreementAccepted) {

				  String url = request.getContextPath() + "/pages/accept_license.jsf?"
														+ "dictionary=" + formal_name
														+ "&code=" + code
														+ "&sab=" + term_source
														+ "&type=hierarchy";

				  response.sendRedirect(response.encodeRedirectURL(url));

	          } else {
				  String url = request.getContextPath() + "/pages/source_hierarchy.jsf?"
														+ "dictionary=" + dictionary
														+ "&code=" + code
														+ "&sab=" + term_source
														+ "&type=hierarchy";

				  response.sendRedirect(response.encodeRedirectURL(url));

			  }
		}
	}
}
