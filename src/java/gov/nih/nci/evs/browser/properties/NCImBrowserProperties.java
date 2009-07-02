package gov.nih.nci.evs.browser.properties;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import gov.nih.nci.evs.browser.utils.DataUtils;
import gov.nih.nci.evs.browser.bean.TermGroupRank;

/**
  * <!-- LICENSE_TEXT_START -->
* Copyright 2008,2009 NGIT. This software was developed in conjunction with the National Cancer Institute,
* and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
* in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
* materials provided with the distribution.
* 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
* "This product includes software developed by NGIT and the National Cancer Institute."
* If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
* wherever such third-party acknowledgments normally appear.
* 3. The names "The National Cancer Institute", "NCI" and "NGIT" must not be used to endorse or promote products derived from this software.
* 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
* the recipient to use any trademarks owned by either NCI or NGIT
* 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
* NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  * <!-- LICENSE_TEXT_END -->
  */

/**
  * @author EVS Team
  * @version 1.0
  *
  * Modification history
  *     Initial implementation kim.ong@ngc.com
  *
 */

public class NCImBrowserProperties {

        private static List displayItemList;
        private static List termGroupRankList;
        private static HashMap configurableItemMap;

        // KLO
        public static final String DEBUG_ON = "DEBUG_ON";
        public static final String EVS_SERVICE_URL = "EVS_SERVICE_URL";
        public static final String LG_CONFIG_FILE = "LG_CONFIG_FILE";
        public static final String MAXIMUM_RETURN = "MAXIMUM_RETURN";
        public static final String EHCACHE_XML_PATHNAME = "EHCACHE_XML_PATHNAME";
        public static final String SORT_BY_SCORE = "SORT_BY_SCORE";
        public static final String MAIL_SMTP_SERVER = "MAIL_SMTP_SERVER";
        public static final String NCICB_CONTACT_URL = "NCICB_CONTACT_URL";
        public static final String MAXIMUM_TREE_LEVEL = "MAXIMUM_TREE_LEVEL";
        public static final String TERMINOLOGY_SUBSET_DOWNLOAD_URL= "TERMINOLOGY_SUBSET_DOWNLOAD_URL";
        public static final String NCIM_BUILD_INFO = "NCIM_BUILD_INFO";

        public static final String TERM_SUGGESTION_APPLIATION_URL= "TERM_SUGGESTION_APPLIATION_URL";


        private static Logger log = Logger.getLogger(NCImBrowserProperties.class);

        private static NCImBrowserProperties NCImBrowserProperties = null;

        private static Properties properties = new Properties();

        public static boolean debugOn = false;
        public static int maxToReturn = 1000;
        public static int maxTreeLevel = 1000;
        private static String service_url = null;
        private static String lg_config_file = null;

        private static String sort_by_score = null;
        private static String mail_smtp_server = null;
        private static String ncicb_contact_url = null;
        private static String terminology_subset_download_url = null;
        private static String term_suggestion_application_url = null;

        /**
         * Private constructor for singleton pattern.
         */
        private NCImBrowserProperties() {}

        /**
         * Gets the single instance of NCImBrowserProperties.
         *
         * @return single instance of NCImBrowserProperties
         *
         * @throws Exception the exception
         */
        public static NCImBrowserProperties getInstance() throws Exception{
            if(NCImBrowserProperties == null) {
                synchronized(NCImBrowserProperties.class) {
                    if(NCImBrowserProperties == null) {
                        NCImBrowserProperties = new NCImBrowserProperties();
                        loadProperties();

                        debugOn = Boolean.parseBoolean(getProperty(DEBUG_ON));

                        String max_str = NCImBrowserProperties.getProperty(NCImBrowserProperties.MAXIMUM_RETURN);
                        maxToReturn = Integer.parseInt(max_str);

                        String max_tree_level_str = NCImBrowserProperties.getProperty(NCImBrowserProperties.MAXIMUM_TREE_LEVEL);
                        maxTreeLevel = Integer.parseInt(max_tree_level_str);

                        service_url = NCImBrowserProperties.getProperty(NCImBrowserProperties.EVS_SERVICE_URL);
                        //System.out.println("EVS_SERVICE_URL: " + service_url);

                        lg_config_file = NCImBrowserProperties.getProperty(NCImBrowserProperties.LG_CONFIG_FILE);
                        //System.out.println("LG_CONFIG_FILE: " + lg_config_file);

                        sort_by_score = NCImBrowserProperties.getProperty(NCImBrowserProperties.SORT_BY_SCORE);
                        ncicb_contact_url = NCImBrowserProperties.getProperty(NCImBrowserProperties.NCICB_CONTACT_URL);
                        mail_smtp_server = NCImBrowserProperties.getProperty(NCImBrowserProperties.MAIL_SMTP_SERVER);
                        terminology_subset_download_url = NCImBrowserProperties.getProperty(NCImBrowserProperties.TERMINOLOGY_SUBSET_DOWNLOAD_URL);
                        term_suggestion_application_url = NCImBrowserProperties.getProperty(NCImBrowserProperties.TERM_SUGGESTION_APPLIATION_URL);
                    }
                }
            }

            return NCImBrowserProperties ;
        }


        //public String getProperty(String key) throws Exception{
        public static String getProperty(String key) throws Exception{
            //return properties.getProperty(key);
            String ret_str = (String) configurableItemMap.get(key);
            if (ret_str == null) return null;
            if (ret_str.compareToIgnoreCase("null") == 0) return null;
            return ret_str;
        }


        public List getDisplayItemList() {
            return this.displayItemList;
        }

        public List getTermGroupRankList() {
            return this.termGroupRankList;
        }

        //KLO, 052909
        public static String getRank(String term_type, String term_source) {
            if (termGroupRankList == null || termGroupRankList.size() == 0) return "0";
			for (int i=0; i<termGroupRankList.size(); i++) {
			   TermGroupRank termGroupRank = (TermGroupRank) termGroupRankList.get(i);
			   if (term_type.compareTo(termGroupRank.getTermGroup()) == 0 && term_source.compareTo(termGroupRank.getSource()) == 0) {
				   return termGroupRank.getIndex();
			   }
		    }
 		    return "0";
		}

        //KLO, 052909
        public static String getHighestTermGroupRank(Vector synonyms) {
            if(synonyms == null || synonyms.size() == 0) return null;
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

			for (int j=1; j<synonyms.size(); j++) {
				//t = term_name + "|" + term_type + "|" + term_source + "|" + term_source_code;
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
            String propertyFile = System.getProperty("gov.nih.nci.evs.browser.NCImBrowserProperties");

            log.info("NCImBrowserProperties File Location= "+ propertyFile);

            PropertyFileParser parser = new PropertyFileParser(propertyFile);
            parser.run();

            displayItemList = parser.getDisplayItemList();
            termGroupRankList = parser.getTermGroupRankList();
            configurableItemMap = parser.getConfigurableItemMap();
        }


    }
