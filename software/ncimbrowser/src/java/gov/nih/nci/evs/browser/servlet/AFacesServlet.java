package gov.nih.nci.evs.browser.servlet;

import gov.nih.nci.evs.browser.common.*;

import java.io.*;
import javax.faces.webapp.*;
import javax.servlet.*;
import javax.servlet.http.*;

import static gov.nih.nci.evs.browser.common.Constants.*;

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
