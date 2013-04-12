/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.servlet;

import gov.nih.nci.evs.browser.common.*;

import java.io.*;
import javax.faces.webapp.*;
import javax.servlet.*;
import javax.servlet.http.*;

import static gov.nih.nci.evs.browser.common.Constants.*;

/**
 * 
 */

/**
 * @author garciawa2
 * 
 */
public class AFacesServlet extends HttpServlet {

    /**
     * Default Serial Version UID
     */
    private static final long serialVersionUID = 1L;
    private FacesServlet _delegate = null;
    private String _errorPage = null;

    /**
     * Constructor
     */
    public AFacesServlet() {
        // Do nothing
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    public void init(ServletConfig _servletConfig) throws ServletException {
        _delegate = new FacesServlet();
        _delegate.init(_servletConfig);
        _errorPage = _servletConfig.getInitParameter(INIT_PARAM_ERROR_PAGE);
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#destroy()
     */
    public void destroy() {
        _delegate.destroy();
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#getServletConfig()
     */
    public ServletConfig getServletConfig() {
        return _delegate.getServletConfig();
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#getServletInfo()
     */
    public String getServletInfo() {
        return _delegate.getServletInfo();
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse)
     */
    public void service(ServletRequest _request, ServletResponse _response)
            throws ServletException, IOException {
        try {
            _delegate.service(_request, _response);
        } catch (Throwable ex) {
            // Make Sure the Stack Trace is Printed to the Log
            ex.printStackTrace();
            // Set the Data to be Displayed
            AErrorHandler.setPageErrorData(ex, (HttpServletRequest) _request);
            // Re-direct to Error Page
            redirectToErrorPage((HttpServletRequest) _request,
                (HttpServletResponse) _response);
        }
    }

    /**
     * @param _request
     * @param _response
     * @throws IOException
     */
    private void redirectToErrorPage(HttpServletRequest _request,
        HttpServletResponse _response) throws IOException {
        if (!"".equals(_errorPage)) {
            _response.sendRedirect(_request.getContextPath() + _errorPage);
        }
    }
}
