package gov.nih.nci.evs.browser.common;

import static gov.nih.nci.evs.browser.common.Constants.*;

import java.io.*;

import javax.faces.context.*;
import javax.faces.event.*;
import javax.servlet.http.*;

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
public class AErrorHandler {

    public static final String ERROR_PAGE = "/pages/error_handler.jsf";

    /**
     * @param _facesContext
     * @param _ex
     */
    public static void displayPhaseListenerException(
        FacesContext _facesContext, Exception _ex) {
        HttpServletResponse response =
            (HttpServletResponse) _facesContext.getExternalContext()
                .getResponse();
        HttpServletRequest request =
            (HttpServletRequest) _facesContext.getExternalContext()
                .getRequest();
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
