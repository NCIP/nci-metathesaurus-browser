/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.bean;

import java.util.*;
import gov.nih.nci.evs.browser.utils.*;
import org.apache.log4j.*;

/**
 * 
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
        // _logger.debug("LicenseBean addLicenseAgreement " + scheme);
        _licenseAgreementHashSet.add(scheme);
        String formalName = MetadataUtils.getFormalName(scheme);

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