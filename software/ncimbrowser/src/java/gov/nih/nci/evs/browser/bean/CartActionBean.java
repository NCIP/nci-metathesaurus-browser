package gov.nih.nci.evs.browser.bean;

import gov.nih.nci.evs.browser.utils.SearchCart;
import gov.nih.nci.evs.browser.utils.SearchUtils;
import gov.nih.nci.evs.browser.utils.ExportCartXML;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;

import org.LexGrid.concepts.Entity;
import org.LexGrid.commonTypes.Property;

import org.apache.log4j.Logger;

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
 * Action bean for cart operations
 *
 * @author garciawa2
 */
public class CartActionBean {

    // Local class variables
    private static Logger _logger = Logger.getLogger(SearchUtils.class);
    private String _entity = null;
    private String _codingScheme = null;
    private HashMap<String, Concept> _cart = null;
    private String _backurl = null;

    // Local constants
    static public final String XML_FILE_NAME = "cart.xml";
    static public final String XML_CONTENT_TYPE = "text/xml";
    static public final String CSV_FILE_NAME = "cart.csv";
    static public final String CSV_CONTENT_TYPE = "text/csv";

    // Getters & Setters

    /**
     * Set name of parameter to use to acquire the code parameter
     * @param codename
     */
    public void setEntity(String entity) {
        this._entity = entity;
    }

    /**
     * Set name of parameter to use to acquire the coding scheme parameter
     * @param codingScheme
     */
    public void setCodingScheme(String codingScheme) {
        this._codingScheme = codingScheme;
    }

    /**
     * Return number of items in cart
     * @return
     */
    public int getCount() {
        if (_cart == null) return 0;
        return _cart.size();
    }

    /**
     * Compute a back to url that is not the cart page
     * @return
     */
    public String getBackurl() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        String targetPage = request.getHeader("Referer");
        targetPage = (targetPage == null) ? request.getContextPath() : targetPage;
        if (!targetPage.contains("cart.jsf")) _backurl = targetPage;
        return _backurl;
    }

    /**
     * Return the concept collection
     * @return
     */
    public Collection<Concept> getConcepts() {
        if (_cart == null) _init();
        return _cart.values();
    }

    /**
     * Initialize the cart container
     */
    private void _init() {
        if (_cart == null) _cart = new HashMap<String, Concept>();
    }

    /**
     * Add concept to the Cart
     * @return
     * @throws Exception
     */
    public void addToCart() throws Exception {
        String code = null;
        String codingScheme = null;
        String nameSpace = null;
        String name = null;
        String url = null;
        String version = null;
        String semanticType = null;

        SearchCart search = new SearchCart();        

        // Get concept information from the Entity item passed in

        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        // Get Entity object
        Entity curr_concept = null;
        curr_concept = (Entity) request.getSession().getAttribute(_entity);
        code = curr_concept.getEntityCode(); // Story identifier

        // Get coding scheme
        codingScheme = (String)request.getSession().getAttribute(_codingScheme);

        // Get concept name space
        nameSpace = curr_concept.getEntityCodeNamespace();

        // Get concept name
        name = curr_concept.getEntityDescription().getContent();

        // Get scheme version
        ResolvedConceptReference ref = null;
        ref = search.getConceptByCode(codingScheme, code);
        version = ref.getCodingSchemeVersion();

        // Get concept URL
        url = request.getRequestURI() +  "?dictionary=" + codingScheme
            + "&version=" + version
            + "&code=" + code;

        // Get Semantic type
        semanticType = search.getSemanticType(curr_concept.getEntityCode()); 

        // Add concept to cart
        if (_cart == null) _init();
        Concept item = new Concept();
        item.setCode(code);
        item.setCodingScheme(codingScheme);
        item.setNameSpace(nameSpace);
        item.setName(name);
        item.setVersion(version);
        item.setUrl(url);
        item.setSemanticType(semanticType);

        if (!_cart.containsKey(code))
            _cart.put(code,item);
    }

    /**
     * Remove concept(s) from the Cart
     * @return
     * @throws Exception
     */
    public void removeFromCart() throws Exception {
        if (_cart != null && _cart.size() > 0) {
            for (Iterator<Concept> i = getConcepts().iterator(); i.hasNext();) {
                Concept item = (Concept)i.next();
                if (item.getSelected()) {
                    if (_cart.containsKey(item.code))
                        i.remove();
                }
            }
        }
    }

    /**
     * Export cart in XML format
     * @return
     * @throws Exception
     */
    public void exportCartXML() throws Exception {

        SearchCart search = new SearchCart();
        ExportCartXML xml = new ExportCartXML();
        ResolvedConceptReference ref = null;
        String sb = null;

        // Get Entities to be exported and build export xml string
        // in memory

        if (_cart != null && _cart.size() > 0) {

            xml.addDocumentTag();
            xml.addCommentTag();

            // Add all terms from the cart
            for (Iterator<Concept> i = getConcepts().iterator(); i.hasNext();) {
                Concept item = (Concept) i.next();
                ref = search.getConceptByCode(item.codingScheme, item.code);
                if (ref != null) {
                    _logger.debug("Exporting: " + ref.getCode());

                    // Add parent concepts
                    Vector<String> parents = search.getParentConcepts(ref);

                    // Add child concepts
                    Vector<String> children = search.getChildConcepts(ref);

                    // Add terms and properties
                    Property[] pres = search.getPresentationValues(ref);
                    Property[] def = search.getDefinitionValues(ref);
                    Property[] prop = search.getPropertyValues(ref);
                    Property[] comm = search.getCommentValues(ref);
                    xml.addTermTag(item.name, item.code, item.codingScheme,
                            pres, def, prop, comm, parents, children);
                }
            }

            // Send export XML string to browser

            sb = xml.generateXMLString();
            HttpServletResponse response = (HttpServletResponse) FacesContext
                    .getCurrentInstance().getExternalContext().getResponse();
            response.setContentType(XML_CONTENT_TYPE);
            response.setHeader("Content-Disposition", "attachment; filename="
                    + XML_FILE_NAME);
            response.setContentLength(sb.length());
            ServletOutputStream ouputStream = response.getOutputStream();
            ouputStream.write(sb.toString().getBytes(), 0, sb.length());
            ouputStream.flush();
            ouputStream.close();

            // Don't allow JSF to forward to cart.jsf
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    /**
     * Export cart in Excel format
     * @return
     * @throws Exception
     */
    public void exportCartCSV() throws Exception {

        SearchCart search = new SearchCart();
        ResolvedConceptReference ref = null;
        StringBuffer sb = new StringBuffer();

        // Get Entities to be exported and build export file
        // in memory

        if (_cart != null && _cart.size() > 0) {
            // Add header
            sb.append("Concept,");
            sb.append("Terminology,");
            sb.append("Code,");
            sb.append("URL");
            sb.append("\r\n");

            // Add concepts
            for (Iterator<Concept> i = getConcepts().iterator(); i.hasNext();) {
                Concept item = (Concept)i.next();
                ref = search.getConceptByCode(item.codingScheme,item.code);
                if (ref != null) {
                    _logger.debug("Exporting: " + ref.getCode());
                    sb.append("\"" + clean(item.name) + "\",");
                    sb.append("\"" + clean(item.codingScheme) + "\",");
                    sb.append("\"" + clean(item.code) + "\",");
                    sb.append("\"" + clean(item.url) + "\"");
                    sb.append("\r\n");
                }
            }

            // Send export file to browser

            HttpServletResponse response = (HttpServletResponse) FacesContext
                    .getCurrentInstance().getExternalContext().getResponse();
            response.setContentType(CSV_CONTENT_TYPE);
            response.setHeader("Content-Disposition", "attachment; filename="
                    + CSV_FILE_NAME);
            response.setContentLength(sb.length());
            ServletOutputStream ouputStream = response.getOutputStream();
            ouputStream.write(sb.toString().getBytes(), 0, sb.length());
            ouputStream.flush();
            ouputStream.close();

            // Don't allow JSF to forward to cart.jsf
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    /**
     * Subclass to hold contents of the cart
     * @author garciawa2
     */
    public class Concept {
        private String code = null;
        private String codingScheme = null;
        private String nameSpace = null;
        private String name = null;
        private boolean selected = false;
        private String version = null;
        private String url = null;
        private String semanticType = null;

        // Getters & setters

        public String getCode() {
            return this.code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCodingScheme() {
            return this.codingScheme;
        }

        public void setCodingScheme(String codingScheme) {
            this.codingScheme = codingScheme;
        }

        public String getNameSpace() {
            return this.nameSpace;
        }

        public void setNameSpace(String namespace) {
            this.nameSpace = namespace;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSelected(Boolean value) {
            this.selected = value;
        }

        public Boolean getSelected() {
            return this.selected;
        }

        public String getVersion() {
            return this.version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSemanticType() {
            return this.semanticType;
        }

        public void setSemanticType(String semanticType) {
            this.semanticType = semanticType;
        }        
        
    } // End of Concept

    //**
    //* Utility methods
    //**

    /**
     * Dump contents of cart object
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Listing cart contents...\n");

        if (_cart != null && _cart.size() > 0) {
            sb.append("\tCart:\n");
            for (Iterator<Concept> i = getConcepts().iterator(); i.hasNext();) {
                Concept item = (Concept)i.next();
                sb.append("\t         Code = " + item.code + "\n");
                sb.append("\tCoding scheme = " + item.codingScheme + "\n");
                sb.append("\t      Version = " + item.version + "\n");
                sb.append("\t   Name space = " + item.nameSpace + "\n");
                sb.append("\t         Name = " + item.name + "\n");
                sb.append("\t     Selected = " + item.selected + "\n");
                sb.append("\t          URL = " + item.url + "\n");
                sb.append("\tSemantic Type = " + item.semanticType + "\n");                
            }
        } else {
            sb.append("Cart is empty.");
        }

        return sb.toString();
    }

    /**
     * Clean a string for use in file type CSV
     * @param str
     * @return
     */
    private String clean(String str) {
        String tmpStr = str.replace('"', ' ');
        return tmpStr;
    }

} // End of CartActionBean
