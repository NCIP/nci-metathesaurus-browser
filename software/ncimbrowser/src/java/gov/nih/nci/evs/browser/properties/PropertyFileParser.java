package gov.nih.nci.evs.browser.properties;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.apache.logging.log4j.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import gov.nih.nci.evs.browser.bean.*;

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

public class PropertyFileParser {
	private static Logger _logger = LogManager.getLogger(PropertyFileParser.class);
    private List _displayItemList;
    private List _termGroupRankList;

    private HashMap _configurableItemMap;
    private String _xmlfile;

    private List _securityTokenList;
    private HashMap _securityTokenHashMap;

    private Document _dom;

    public PropertyFileParser() {
        _displayItemList = new ArrayList();
        _termGroupRankList = new ArrayList();
        _configurableItemMap = new HashMap();

        _securityTokenList = new ArrayList();
        _securityTokenHashMap = new HashMap();
    }

    public PropertyFileParser(String xmlfile) {
        _displayItemList = new ArrayList();
        _termGroupRankList = new ArrayList();
        _configurableItemMap = new HashMap();
        _xmlfile = xmlfile;

        _securityTokenList = new ArrayList();
        _securityTokenHashMap = new HashMap();
    }

    public void run() {
        parseXmlFile(_xmlfile);
        parseDocument();
        // printData();
    }

    public List getDisplayItemList() {
        return _displayItemList;
    }

    public HashMap getConfigurableItemMap() {
        return _configurableItemMap;
    }

    private void parseXmlFile(String xmlfile) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            _dom = db.parse(xmlfile);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void parseDocument() {
        Element docEle = _dom.getDocumentElement();
        NodeList list1 = docEle.getElementsByTagName("DisplayItem");
        if (list1 != null && list1.getLength() > 0) {
            for (int i = 0; i < list1.getLength(); i++) {
                Element el = (Element) list1.item(i);
                DisplayItem e = getDisplayItem(el);
                _displayItemList.add(e);
            }
        }

        NodeList list2 = docEle.getElementsByTagName("ConfigurableItem");
        if (list2 != null && list2.getLength() > 0) {
            for (int i = 0; i < list2.getLength(); i++) {
                Element el = (Element) list2.item(i);
                getConfigurableItem(el);
            }
        }

        NodeList list3 = docEle.getElementsByTagName("TermGroupRank");
        if (list3 != null && list3.getLength() > 0) {
            for (int i = 0; i < list3.getLength(); i++) {
                Element el = (Element) list3.item(i);
                TermGroupRank e = getTermGroupRank(el);
                _termGroupRankList.add(e);
            }
        }

        NodeList list5 = docEle.getElementsByTagName("SecurityTokenHolder");
        if (list5 != null && list5.getLength() > 0) {
            for (int i = 0; i < list5.getLength(); i++) {
                Element el = (Element) list5.item(i);
                SecurityTokenHolder e = getSecurityTokenHolder(el);

                if (e.getValue().indexOf("token") == -1) {
                    _securityTokenList.add(e);
                    _securityTokenHashMap.put(e.getName(), e.getValue());
                }
            }
        }
    }

    private DisplayItem getDisplayItem(Element displayItemElement) {
        String propertyName = getTextValue(displayItemElement, "propertyName");
        String itemLabel = getTextValue(displayItemElement, "itemLabel");
        String url = getTextValue(displayItemElement, "url");
        String hyperlinkText =
            getTextValue(displayItemElement, "hyperlinkText");
        boolean isExternalCode =
            getBooleanValue(displayItemElement, "isExternalCode");

        if (url.compareTo("null") == 0) {
            url = null;
        }
        if (hyperlinkText.compareTo("null") == 0) {
            hyperlinkText = null;
        }

        DisplayItem item =
            new DisplayItem(propertyName, itemLabel, url, hyperlinkText,
                isExternalCode);
        return item;
    }

    private void getConfigurableItem(Element displayItemElement) {
        String key = getTextValue(displayItemElement, "key");
        String value = getTextValue(displayItemElement, "value");
        _configurableItemMap.put(key, value);
    }

    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }
        if (textVal == null) return null;
        return textVal.trim();
    }

    private boolean getBooleanValue(Element ele, String tagName) {
        String textVal = getTextValue(ele, tagName);
        if (textVal.compareToIgnoreCase("true") == 0)
            return true;
        return false;
    }

    private void printData() {
        Iterator it = _displayItemList.iterator();
        while (it.hasNext()) {
            _logger.debug(it.next().toString());
        }
    }

    public List getTermGroupRankList() {
        return _termGroupRankList;
    }

    private TermGroupRank getTermGroupRank(Element termGroupRankElement) {
        String index = getTextValue(termGroupRankElement, "index");
        String source = getTextValue(termGroupRankElement, "source");
        String termgroup = getTextValue(termGroupRankElement, "termgroup");

        TermGroupRank item = new TermGroupRank(index, source, termgroup);
        return item;
    }

    public List getSecurityTokenList() {
        return _securityTokenList;
    }

    public HashMap getSecurityTokenHashMap() {
        return _securityTokenHashMap;
    }

    private SecurityTokenHolder getSecurityTokenHolder(
        Element securityTokenHolder) {
        String name = getTextValue(securityTokenHolder, "name");
        String value = getTextValue(securityTokenHolder, "value");
        SecurityTokenHolder item = new SecurityTokenHolder(name, value);
        return item;
    }

    public static void main(String[] args) {
        PropertyFileParser parser = new PropertyFileParser();
        parser.run();
    }
}
