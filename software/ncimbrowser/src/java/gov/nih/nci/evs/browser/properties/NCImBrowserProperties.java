package gov.nih.nci.evs.browser.properties;

import java.util.*;

import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.browser.bean.*;
import org.apache.log4j.*;

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

public class NCImBrowserProperties {
    private static Logger _logger =
        Logger.getLogger(NCImBrowserProperties.class);

    private static List _displayItemList;
    private static List _termGroupRankList;
    private static HashMap _termGroupRankHashMap;
    private static HashMap _configurableItemMap;

    private static List _securityTokenList;
    private static HashMap _securityTokenHashMap;

    public static final String DEBUG_ON = "DEBUG_ON";
    public static final String EVS_SERVICE_URL = "EVS_SERVICE_URL";
    public static final String LG_CONFIG_FILE = "LG_CONFIG_FILE";
    public static final String MAXIMUM_RETURN = "MAXIMUM_RETURN";
    public static final String EHCACHE_XML_PATHNAME = "EHCACHE_XML_PATHNAME";
    public static final String MAIL_SMTP_SERVER = "MAIL_SMTP_SERVER";
    public static final String NCICB_CONTACT_URL = "NCICB_CONTACT_URL";
    public static final String MAXIMUM_TREE_LEVEL = "MAXIMUM_TREE_LEVEL";
    public static final String TERMINOLOGY_SUBSET_DOWNLOAD_URL =
        "TERMINOLOGY_SUBSET_DOWNLOAD_URL";
    public static final String NCIM_BUILD_INFO = "NCIM_BUILD_INFO";
    public static final String NCIM_APP_VERSION = "APPLICATION_VERSION";
    public static final String TERM_SUGGESTION_APPLICATION_URL =
        "TERM_SUGGESTION_APPLICATION_URL";
    public static final String ANTHILL_BUILD_TAG_BUILT =
        "ANTHILL_BUILD_TAG_BUILT";
    public static final String NCIT_URL = "NCIT_URL";

    public static final String PAGINATION_TIME_OUT = "PAGINATION_TIME_OUT";
    public static final String MINIMUM_SEARCH_STRING_LENGTH =
        "MINIMUM_SEARCH_STRING_LENGTH";
    public static final String SLIDING_WINDOW_HALF_WIDTH =
        "SLIDING_WINDOW_HALF_WIDTH";

    public static final String SOURCE_HIERARCHIES = "SOURCE_HIERARCHIES";

    public static final String SECURED_VOCABULARIES = "SECURED_VOCABULARIES";
    public static final String SUBCONCEPT_PAGE_SIZE = "SUBCONCEPT_PAGE_SIZE";

    public static final String MAXIMUM_SEARCH_ITERATION = "MAXIMUM_SEARCH_ITERATION";

    private static NCImBrowserProperties NCImBrowserProperties = null;
    private static Properties properties = new Properties();

    public static boolean _debugOn = false;
    public static int _maxToReturn = 1000;
    public static int _maxTreeLevel = 1000;
    public static int _max_search_iteration = 100;
    private static String _service_url = null;
    private static String _lg_config_file = null;
    private static String _mail_smtp_server = null;
    private static String _ncicb_contact_url = null;
    private static String _terminology_subset_download_url = null;
    private static String _term_suggestion_application_url = null;
    private static String _term_browser_url = null;
    private static String _source_hierarchies = null;
    private static String _secured_vocabularies = null;

    private static int _pagination_time_out = 4;
    private static int _minimum_search_string_length = 1;
    private static int _sliding_window_half_width = 5;
    public static final String LICENSE_PAGE_OPTION = "LICENSE_PAGE_OPTION";
    private static String _license_page_option = null;
    private static int _subconcept_page_size = 1000;

    /**
     * Private constructor for singleton pattern.
     */
    private NCImBrowserProperties() {
    }

    /**
     * Gets the single instance of NCImBrowserProperties.
     *
     * @return single instance of NCImBrowserProperties
     *
     * @throws Exception the exception
     */
    public static NCImBrowserProperties getInstance() throws Exception {
        if (NCImBrowserProperties == null) {
            synchronized (NCImBrowserProperties.class) {
                if (NCImBrowserProperties == null) {
                    NCImBrowserProperties = new NCImBrowserProperties();
                    loadProperties();

                    _debugOn = Boolean.parseBoolean(getProperty(DEBUG_ON));

                    String max_str =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.MAXIMUM_RETURN);
                    _maxToReturn = Integer.parseInt(max_str);

                    String max_tree_level_str =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.MAXIMUM_TREE_LEVEL);
                    _maxTreeLevel = Integer.parseInt(max_tree_level_str);

                    _service_url =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.EVS_SERVICE_URL);
                    // _logger.debug("EVS_SERVICE_URL: " + service_url);

                    _lg_config_file =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.LG_CONFIG_FILE);
                    // _logger.debug("LG_CONFIG_FILE: " + lg_config_file);

                    // sort_by_score =
                    // NCImBrowserProperties.getProperty(NCImBrowserProperties.SORT_BY_SCORE);
                    _ncicb_contact_url =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.NCICB_CONTACT_URL);
                    _mail_smtp_server =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.MAIL_SMTP_SERVER);
                    _terminology_subset_download_url =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.TERMINOLOGY_SUBSET_DOWNLOAD_URL);
                    _term_suggestion_application_url =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.TERM_SUGGESTION_APPLICATION_URL);

                    String pagination_time_out_str =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.PAGINATION_TIME_OUT);
                    if (pagination_time_out_str != null) {
                        _pagination_time_out =
                            Integer.parseInt(pagination_time_out_str);
                    }

                    String minimum_search_string_length_str =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.MINIMUM_SEARCH_STRING_LENGTH);
                    if (minimum_search_string_length_str != null) {
                        _minimum_search_string_length =
                            Integer.parseInt(minimum_search_string_length_str);
                    }

                    String sliding_window_half_width_str =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.SLIDING_WINDOW_HALF_WIDTH);
                    if (sliding_window_half_width_str != null) {
                        int sliding_window_halfwidth =
                            Integer.parseInt(sliding_window_half_width_str);
                        if (sliding_window_halfwidth > 1) {
                            _sliding_window_half_width =
                                sliding_window_halfwidth;
                        }
                    }
                    _term_browser_url =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.NCIT_URL);
                    _source_hierarchies =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.SOURCE_HIERARCHIES);
                    _secured_vocabularies =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.SECURED_VOCABULARIES);

                    _license_page_option =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.LICENSE_PAGE_OPTION);

                    String subconcept_page_size_str =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.SUBCONCEPT_PAGE_SIZE);
                    if (subconcept_page_size_str != null) {
                        _subconcept_page_size =
                            Integer.parseInt(subconcept_page_size_str);
                    }

                    String _max_search_iteration_str =
                        NCImBrowserProperties
                            .getProperty(NCImBrowserProperties.MAXIMUM_SEARCH_ITERATION);
                    if (_max_search_iteration_str != null) {
                    	_max_search_iteration = Integer.parseInt(_max_search_iteration_str);
				    }
                }
            }
        }

        return NCImBrowserProperties;
    }

    public static String getSourceHierarchies() {
        return _source_hierarchies;
    }

    public static String getSecuredVocabularies() {
        return _secured_vocabularies;
    }

    public static List getSecurityTokenList() {
        return _securityTokenList;
    }

    public static HashMap getSecurityTokenHashMap() {
        return _securityTokenHashMap;
    }

    public static String getProperty(String key) throws Exception {
        String ret_str = (String) _configurableItemMap.get(key);
        if (ret_str == null)
            return null;
        if (ret_str.compareToIgnoreCase("null") == 0)
            return null;
        return ret_str;
    }

    public List getDisplayItemList() {
        return _displayItemList;
    }

    public List getTermGroupRankList() {
        return _termGroupRankList;
    }

    public static String getRank(String term_type, String term_source) {
        String key = term_source + "$" + term_type;
        if (_termGroupRankHashMap.containsKey(key))
            return (String) _termGroupRankHashMap.get(key);
        return "0";
    }

    // KLO, 052909
    public static String getHighestTermGroupRank(Vector synonyms) {
        if (synonyms == null || synonyms.size() == 0)
            return null;
        String maxRank = null;
        String t = (String) synonyms.elementAt(0);
        if (synonyms.size() == 1) {
            return t;
        }
        String rank = null;
        String term_data = t;
        Vector<String> w = DataUtils.parseData(term_data, "|");
        String term_type = w.elementAt(1);
        String term_source = w.elementAt(2);
        maxRank = getRank(term_type, term_source);

        for (int j = 1; j < synonyms.size(); j++) {
            // t = term_name + "|" + term_type + "|" + term_source + "|" +
            // term_source_code;
            t = (String) synonyms.elementAt(j);
            w = DataUtils.parseData(t, "|");
            term_type = w.elementAt(1);
            term_source = w.elementAt(2);
            rank = getRank(term_type, term_source);
            if (rank != null && rank.compareTo(maxRank) > 0) {
                maxRank = rank;
                term_data = t;
            }
        }
        return term_data;
    }

    private static void loadProperties() throws Exception {
        String propertyFile =
            System.getProperty("gov.nih.nci.evs.browser.NCImBrowserProperties");

        _logger.info("NCImBrowserProperties File Location= " + propertyFile);

        PropertyFileParser parser = new PropertyFileParser(propertyFile);
        parser.run();

        _securityTokenList = parser.getSecurityTokenList();
        _securityTokenHashMap = parser.getSecurityTokenHashMap();

        _displayItemList = parser.getDisplayItemList();
        _termGroupRankList = parser.getTermGroupRankList();
        _termGroupRankHashMap = createTermGroupRankHashMap();
        _configurableItemMap = parser.getConfigurableItemMap();
    }

    private static HashMap createTermGroupRankHashMap() throws Exception {
        HashMap hmap = new HashMap();
        for (int i = 0; i < _termGroupRankList.size(); i++) {
            TermGroupRank tgr = (TermGroupRank) _termGroupRankList.get(i);
            String index = tgr.getIndex();
            String source = tgr.getSource();
            String termGroup = tgr.getTermGroup();
            hmap.put(source + "$" + termGroup, index);
        }
        return hmap;
    }

    public static int getPaginationTimeOut() {
        return _pagination_time_out;
    }

    public static int getMinimumSearchStringLength() {
        return _minimum_search_string_length;
    }

    public static int getSlidingWindowHalfWidth() {
        return _sliding_window_half_width;
    }

    public static String getTermBrowserURL() {
        return _term_browser_url;
    }

    public static String getLicensePageOption() {
        return _license_page_option;
    }

    public static int getSubconceptPageSize() {
        return _subconcept_page_size;
    }

    public static int getMaxSearchIteration() {
        return _max_search_iteration;
    }
}
