package gov.nih.nci.evs.browser.common;

import static gov.nih.nci.evs.browser.common.Constants.ERROR_MESSAGE;
import static gov.nih.nci.evs.browser.common.Constants.ERROR_UNEXPECTED;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author garciawa2
 *
 */
public class AErrorHandler {

	public static final String ERROR_PAGE = "/pages/error_handler.jsf";

	/**
	 * @param _facesContext
	 * @param _ex
	 */
	public static void displayPhaseListenerException(
			FacesContext _facesContext, Exception _ex) {
		HttpServletResponse response = (HttpServletResponse) _facesContext
				.getExternalContext().getResponse();
		HttpServletRequest request = (HttpServletRequest) _facesContext
				.getExternalContext().getRequest();
		try {
			setPageErrorData(_ex, request);
			response.sendRedirect(request.getContextPath() + ERROR_PAGE);
			_facesContext.responseComplete();
			_ex.printStackTrace();
		} catch (IOException ex) {
			_ex.printStackTrace();
			ex.printStackTrace();
		}
		throw new AbortProcessingException("An Error has occurred.");
	}

	/**
	 * @param _e
	 * @param _request
	 */
	public static void setPageErrorData(Throwable _e,
			HttpServletRequest _request) {
		_request.getSession().setAttribute(ERROR_MESSAGE, ERROR_UNEXPECTED);
	}

}
