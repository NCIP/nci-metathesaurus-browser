/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.test;

import org.apache.log4j.Logger;

import gov.nih.nci.evs.browser.properties.*;

public class PropertiesTest {
    private static Logger _logger = Logger.getLogger(PropertiesTest.class);
    private static final String PROPERTY_FILE = 
        "gov.nih.nci.evs.browser.NCImBrowserProperties";
    private static String DEFAULT_PROPERTY_FILE =
        "C:/apps/evs/ncim-webapp/conf/NCImBrowserProperties.xml";

    public static void main(String[] args) {
        try {
            System.setProperty(PROPERTY_FILE, DEFAULT_PROPERTY_FILE);
            new PropertiesTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void debug(String text) {
        _logger.debug(text);
    }
    
    public PropertiesTest() throws Exception {
        NCImBrowserProperties.getInstance();
        debug("* Setting(s):");
        debug("  * EVS_SERVICE_URL: " + NCImBrowserProperties
            .getProperty(NCImBrowserProperties.EVS_SERVICE_URL));
        debug("  * LG_CONFIG_FILE: " + NCImBrowserProperties
            .getProperty(NCImBrowserProperties.LG_CONFIG_FILE));
        debug("  * SECURED_VOCABULARIES: " + NCImBrowserProperties
            .getProperty(NCImBrowserProperties.SECURED_VOCABULARIES));
    }
}
