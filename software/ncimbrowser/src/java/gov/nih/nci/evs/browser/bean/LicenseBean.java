package gov.nih.nci.evs.browser.bean;

import java.util.*;
import gov.nih.nci.evs.browser.utils.*;
import org.apache.log4j.*;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;


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
 * @author EVS Team
 * @version 1.0
 *
 *          Modification history Initial implementation kim.ong@ngc.com
 *
 */

public class LicenseBean extends Object {
    private static Logger _logger = Logger.getLogger(LicenseBean.class);
    private HashSet _licenseAgreementHashSet = null;

    public LicenseBean() {
        _licenseAgreementHashSet = new HashSet();
    }

    public void addLicenseAgreement(String scheme) {
        _logger.debug("(*) LicenseBean addLicenseAgreement " + scheme);
        _licenseAgreementHashSet.add(scheme);
        //LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        String formalName = NCImMetadataUtils.getFormalName(scheme);


        _logger.debug("(*) LicenseBean addLicenseAgreement formalname " + formalName);

        if (formalName != null) {
            _licenseAgreementHashSet.add(formalName);
        }
    }

    public boolean licenseAgreementAccepted(String scheme) {
        // option to not pop-up the license agreement page:
        /*
         * String license_page_option =
         * NCImBrowserProperties.getLicensePageOption(); if (license_page_option
         * != null && license_page_option.compareToIgnoreCase("true") != 0)
         * return true;
         */

        boolean retval = _licenseAgreementHashSet.contains(scheme);
        // _logger.debug("licenseAgreementAccepted " + scheme + ": " + retval);
        return (retval);
    }

    public static boolean isLicensed(String codingSchemeName, String version) {
        // MedDRA, SNOMED CT, and UMLS Semantic Network.
        String license_display = null;

        license_display =
            getLicenseDisplay(codingSchemeName, "license_display");
        if (license_display != null && license_display.compareTo("accept") == 0) {
            return true;
        }

        return false;
    }

    public static String getLicenseDisplay(String codingSchemeName,
        String version) {
        // MedDRA, SNOMED CT, and UMLS Semantic Network.
        return DataUtils.getMetadataValue(codingSchemeName, "license_display");
    }

    public static String resolveCodingSchemeCopyright(String codingSchemeName,
        String version) {
        return DataUtils
            .getMetadataValue(codingSchemeName, "license_statement");
    }

}