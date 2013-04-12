/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.common;

import static gov.nih.nci.evs.browser.common.Constants.*;

import java.io.*;

import javax.faces.context.*;
import javax.faces.event.*;
import javax.servlet.http.*;

/**
 * 
 */

/**
 * @author garciawa2
 * 
 */
public class AErrorHandler {

    public static final String ERROR_PAGE = "/pages/error_handler.jsf";

    /**
     * @param facesContext
     * @param exception
     */
    public static void displayPhaseListenerException(
        FacesContext facesContext, Exception exception) {
        HttpServletResponse response =
            (HttpServletResponse) facesContext.getExternalContext()
                .getResponse();
        HttpServletRequest request =
            (HttpServletRequest) facesContext.getExternalContext()
                .getRequest();
        try {
            setPageErrorData(exception, request);
            response.sendRedirect(request.getContextPath() + ERROR_PAGE);
            facesContext.responseComplete();
            exception.printStackTrace();
        } catch (IOException ex) {
            exception.printStackTrace();
            ex.printStackTrace();
        }
        throw new AbortProcessingException("An Error has occurred.");
    }

    /**
     * @param throwable
     * @param request
     */
    public static void setPageErrorData(Throwable throwable,
        HttpServletRequest request) {
        request.getSession().setAttribute(ERROR_MESSAGE, ERROR_UNEXPECTED);
    }
}
