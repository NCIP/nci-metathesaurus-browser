/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.utils;

import java.util.*;

import org.LexGrid.LexBIG.caCore.interfaces.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Impl.*;
import org.apache.log4j.*;

import gov.nih.nci.system.client.*;
import gov.nih.nci.evs.browser.properties.*;
import gov.nih.nci.evs.security.*;
import gov.nih.nci.evs.browser.bean.*;

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

public class RemoteServerUtil {
    private static Logger _logger = Logger.getLogger(RemoteServerUtil.class);
    private static boolean debug = false;
    private static String _serviceInfo = "EvsServiceInfo";
    // private Properties _systemProperties = null;
    private static String _serviceURL = null;

    public RemoteServerUtil() {
        // Do nothing
    }

    public static LexBIGService createLexBIGService() {
        String url = "http://ncias-d177-v.nci.nih.gov:19480/lexevsapi51";

        NCImBrowserProperties properties = null;
        try {
            properties = NCImBrowserProperties.getInstance();
            url = properties.getProperty(NCImBrowserProperties.EVS_SERVICE_URL);
            return createLexBIGService(url);
        } catch (Exception ex) {
            // Do nothing
            // _logger.error("WARNING: NCImBrowserProperties loading error...");
            // _logger.error("\t-- trying to connect to " + url + " instead.");
            ex.printStackTrace();
        }
        return null;// createLexBIGService(url);
    }

    public static LexBIGService createLexBIGService(
        boolean registerSecurityTokens) {
        String url = "http://ncias-d177-v.nci.nih.gov:19480/lexevsapi51";

        NCImBrowserProperties properties = null;
        try {
            properties = NCImBrowserProperties.getInstance();
            url = properties.getProperty(NCImBrowserProperties.EVS_SERVICE_URL);
            return createLexBIGService(url, registerSecurityTokens);
        } catch (Exception ex) {
            // Do nothing
            // _logger.error("WARNING: NCImBrowserProperties loading error...");
            // _logger.error("\t-- trying to connect to " + url + " instead.");
            ex.printStackTrace();
        }
        return null;// createLexBIGService(url);
    }

    public static LexBIGService createLexBIGService(String serviceUrl) {
        return createLexBIGService(serviceUrl, false);
    }

    public static LexBIGService createLexBIGService(String serviceUrl,
        boolean registerSecurityTokens) {
        try {
            NCImBrowserProperties properties = null;
            properties = NCImBrowserProperties.getInstance();

            if (serviceUrl == null || serviceUrl.compareTo("") == 0) {
                String lg_config_file =
                    properties
                        .getProperty(NCImBrowserProperties.LG_CONFIG_FILE);
                System.setProperty(NCImBrowserProperties.LG_CONFIG_FILE,
                    lg_config_file);
                LexBIGService lbSvc = new LexBIGServiceImpl();
                return lbSvc;
            }
            if (debug) {
                _logger.debug(Utils.SEPARATOR);
                _logger.debug("LexBIGService(remote): " + serviceUrl);
            }
            LexEVSApplicationService lexevsService =
                (LexEVSApplicationService) ApplicationServiceProvider
                    .getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");
            if (registerSecurityTokens)
                lexevsService = registerAllSecurityTokens(lexevsService);
            return (LexBIGService) lexevsService;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * public static LexBIGService createLexBIGService(String serviceUrl) { try
     * { NCImBrowserProperties properties = null; properties =
     * NCImBrowserProperties.getInstance();
     * 
     * if (serviceUrl == null || serviceUrl.compareTo("") == 0) { String
     * lg_config_file =
     * properties.getProperty(NCImBrowserProperties.LG_CONFIG_FILE);
     * System.setProperty(NCImBrowserProperties.LG_CONFIG_FILE,lg_config_file);
     * LexBIGService lbSvc = new LexBIGServiceImpl(); return lbSvc; } if (debug)
     * { _logger.debug(Utils.SEPARATOR); _logger.debug("LexBIGService(remote): "
     * + serviceUrl); } LexEVSApplicationService lexevsService =
     * (LexEVSApplicationService
     * )ApplicationServiceProvider.getApplicationServiceFromUrl(serviceUrl,
     * "EvsServiceInfo"); lexevsService =
     * registerAllSecurityTokens(lexevsService); return (LexBIGService)
     * lexevsService; } catch (Exception e) { e.printStackTrace(); } return
     * null; }
     */
    // KLO 100709
    public static LexEVSApplicationService registerAllSecurityTokens(
        LexEVSApplicationService lexevsService) {
        List list = NCImBrowserProperties.getSecurityTokenList();
        if (list == null || list.size() == 0)
            return lexevsService;
        for (int i = 0; i < list.size(); i++) {
            SecurityTokenHolder holder = (SecurityTokenHolder) list.get(i);
            lexevsService =
                registerSecurityToken(lexevsService, holder.getName(), holder
                    .getValue());
        }
        return lexevsService;
    }

    // KLO 100709
    public static LexEVSApplicationService registerSecurityToken(
        LexEVSApplicationService lexevsService, String codingScheme,
        String token) {
        SecurityToken securityToken = new SecurityToken();
        securityToken.setAccessToken(token);
        Boolean retval = null;
        try {
            retval =
                lexevsService
                    .registerSecurityToken(codingScheme, securityToken);
            if (retval != null && retval.equals(Boolean.TRUE)) {
                // _logger.debug("Registration of SecurityToken was successful.");
            } else {
                _logger.warn("WARNING: Registration of SecurityToken failed.");
            }
        } catch (Exception e) {
            _logger.error("WARNING: Registration of SecurityToken failed.");
        }
        return lexevsService;
    }

    // KLO 100709
    public static LexBIGService createLexBIGService(String serviceUrl,
        String codingScheme, String token) {
        SecurityToken securityToken = new SecurityToken();
        securityToken.setAccessToken(token);
        return createLexBIGService(serviceUrl, codingScheme, securityToken);
    }

    // KLO 100709
    public static LexBIGService createLexBIGService(String serviceUrl,
        String codingScheme, SecurityToken securityToken) {
        try {
            if (serviceUrl == null || serviceUrl.compareTo("") == 0) {
                LexBIGService lbSvc = new LexBIGServiceImpl();
                return lbSvc;
            }

            LexEVSApplicationService lexevsService =
                (LexEVSApplicationService) ApplicationServiceProvider
                    .getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");

            Boolean retval = false;
            retval =
                lexevsService
                    .registerSecurityToken(codingScheme, securityToken);

            if (retval.equals(Boolean.TRUE)) {
                // _logger.debug("Registration of SecurityToken was successful.");
            } else {
                _logger.warn("WARNING: Registration of SecurityToken failed.");
            }

            _logger.debug("Connected to " + serviceUrl);
            return (LexBIGService) lexevsService;
        } catch (Exception e) {
            _logger.error("Unable to connected to " + serviceUrl);
            e.printStackTrace();
        }
        return null;
    }

    public static String getServiceURL() {
        return _serviceURL;
    }
}
