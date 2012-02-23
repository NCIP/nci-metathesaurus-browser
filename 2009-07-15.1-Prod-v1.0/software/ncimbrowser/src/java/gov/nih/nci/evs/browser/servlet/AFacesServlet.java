package gov.nih.nci.evs.browser.servlet;

import gov.nih.nci.evs.browser.common.AErrorHandler;

import java.io.IOException;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static gov.nih.nci.evs.browser.common.Constants.*;

/**
 * @author garciawa2
 * 
 */
public class AFacesServlet extends HttpServlet {

	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	private FacesServlet delegate = null;
	private String errorPage = null;

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
		delegate = new FacesServlet();
		delegate.init(_servletConfig);
		errorPage = _servletConfig.getInitParameter(INIT_PARAM_ERROR_PAGE);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	public void destroy() {
		delegate.destroy();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		return delegate.getServletConfig();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#getServletInfo()
	 */
	public String getServletInfo() {
		return delegate.getServletInfo();
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
			delegate.service(_request, _response);
		} catch (Throwable ex) {
			// Make Sure the Stack Trace is Printed to the Log
			ex.printStackTrace();
            // Set the Data to be Displayed 
            AErrorHandler.setPageErrorData(ex, (HttpServletRequest)_request); 
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
		if (!"".equals(errorPage)) {
			_response.sendRedirect(_request.getContextPath() + errorPage);
		}
	}

}
