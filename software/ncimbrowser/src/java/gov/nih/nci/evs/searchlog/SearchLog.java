/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.searchlog;

import gov.nih.nci.evs.browser.utils.*;
import org.apache.log4j.*;

/**
 * 
 */

/**
 * @author garciawa2 AccessLog class
 */
public class SearchLog {
    private static Logger _logger = Logger.getLogger(SearchLog.class);
    static Logger logger = null;
    public static final char SEPARATOR = '|';
    public static final String UK = "Unknown";

    /**
     * Constructor
     */
    public SearchLog() {
        init();
    }

    /**
     * Destructor - called to release logger
     */
    public static void destroy() {
        if (logger != null) {
            logger = null;
        }
        _logger.debug("Search log is shutdown.");
    }

    /**
     * Initializer
     */
    public static void init() {
        if (logger == null) {
            logger = Logger.getLogger(SearchLog.class);
        }
    }

    /**
     * @param term
     */
    public static void writeEntry(SearchFields fields, int maxReturn,
        String referrer) {
        init();

        // Report format:
        // SEARCH_TYPE|TERM|ALGORITHM|TARGET|SOURCE|COUNT|PROPERTY_NAME|PROPERTY_TYPE
        // REL_ASSOCICATION|REL_RELA|REFERRER

        logger.log(SearchLevel.SEARCH_LOG_LEVEL, fields.getType().toString()
            + SEPARATOR + fields.getMatchText() + SEPARATOR
            + fields.getMatchAlgorithm() + SEPARATOR + fields.getSearchTarget()
            + SEPARATOR + fields.getSource() + SEPARATOR + maxReturn
            + SEPARATOR + fields.getPropertyName() + SEPARATOR
            + fields.getPropertyType() + SEPARATOR
            + fields.getRelSearchAssociation() + SEPARATOR
            + fields.getRelSearchRela() + SEPARATOR + referrer);
    }
}
