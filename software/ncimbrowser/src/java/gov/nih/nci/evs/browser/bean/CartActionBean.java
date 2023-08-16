package gov.nih.nci.evs.browser.bean;

import gov.nih.nci.evs.browser.common.Constants;
import gov.nih.nci.evs.browser.utils.RemoteServerUtil;
import gov.nih.nci.evs.browser.utils.SearchCart;
import gov.nih.nci.evs.browser.utils.SearchUtils;
import gov.nih.nci.evs.browser.utils.ExportCartXML;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.Set;
import java.util.List;
import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;

import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;

import org.LexGrid.concepts.Entity;
import org.LexGrid.commonTypes.Property;

import org.apache.logging.log4j.*;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitionReference;
import org.LexGrid.valueSets.types.DefinitionOperator;

import javax.faces.context.*;
import org.apache.commons.io.IOUtils;

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
	private static Logger _logger = LogManager.getLogger(CartActionBean.class);

    /*
    private String _entity = null;
    private String _codingScheme = null;
    private HashMap<String, Concept> _cart = null;
    private String _backurl = null;
    private boolean _messageflag = false;
    private String _message = null;
    */

    public String _entity = null;
    public String _codingScheme = null;
    public HashMap<String, Concept> _cart = null;
    public String _backurl = null;
    public boolean _messageflag = false;
    public String _message = null;

    // Local constants
    static public final String XML_FILE_NAME = "cart.xml";
    static public final String XML_CONTENT_TYPE = "text/xml";
    static public final String CSV_FILE_NAME = "cart.csv";
    static public final String CSV_CONTENT_TYPE = "text/csv";

    // Error messages

    static public final String NO_CONCEPTS = "No concepts in cart.";
    static public final String NOTHING_SELECTED = "No concepts selected.";


    public CartActionBean() {

	}

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
     * Return Popup message flag
     * @return
     */
    public boolean getMessageflag() {
    	return _messageflag;
    }

    /**
     * Return Popup message text
     * @return
     */
    public String getMessage() {
    	_messageflag = false;
    	return _message;
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


    public void setCart(HashMap hmap) {
        this._cart = hmap;
    }


    // ******************** Class methods ************************

    /**
     * Initialize the cart container
     */
    public void _init() {
        if (_cart == null) _cart = new HashMap<String, Concept>();
/*
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("cartActionBean", this);
*/
    }


    /**
     * Add concept to the Cart
     * @return
     * @throws Exception
     */
    public String addToCart() throws Exception {
        String code = null;
        String codingScheme = null;
        String nameSpace = null;
        String name = null;
        String url = null;
        String version = null;
        String semanticType = null;

        _messageflag = false;
        SearchCart search = new SearchCart();

        // Get concept information from the Entity item passed in
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().removeAttribute("message");

        // Get Entity object
        Entity curr_concept = (Entity) request.getSession().getAttribute(_entity);
        if (curr_concept == null) {
        	// Called from a non search area
        	_logger.error("*** Cart error: Entity object is null!");
        	return null;
        }
        code = curr_concept.getEntityCode(); // Store identifier

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
        String protocol = request.getScheme();
        String domain = request.getServerName();
        String port = Integer.toString(request.getServerPort());
        if (port.equals("80")) port = ""; else port = ":" + port;
        String path = request.getContextPath();
        url = protocol + "://" + domain
            + port + path
            + "/pages/concept_details.jsf?dictionary=" + codingScheme
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

        request.getSession().setAttribute("_cart", _cart);

        return null;
    }


    public String addToCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = null;
        String codingScheme = null;
        String nameSpace = null;
        String name = null;
        String url = null;
        String version = null;
        String semanticType = null;

        _messageflag = false;
        SearchCart search = new SearchCart();

        // Get concept information from the Entity item passed in

        request.getSession().removeAttribute("message");

        // Get Entity object
        Entity curr_concept = (Entity) request.getSession().getAttribute(_entity);


        if (curr_concept == null) {
        	// Called from a non search area
        	_logger.error("*** Cart error: Entity object is null!");
        	return null;
        }
        code = curr_concept.getEntityCode(); // Store identifier

        // Get coding scheme
        codingScheme = (String)request.getSession().getAttribute(_codingScheme);
        if (codingScheme == null) {
			codingScheme = "NCI Metathesaurus";
		}

        // Get concept name space
        nameSpace = curr_concept.getEntityCodeNamespace();

        // Get concept name
        name = curr_concept.getEntityDescription().getContent();

        // Get scheme version
        ResolvedConceptReference ref = null;
        ref = search.getConceptByCode(codingScheme, code);


        version = ref.getCodingSchemeVersion();

        // Get concept URL
        String protocol = request.getScheme();
        String domain = request.getServerName();
        String port = Integer.toString(request.getServerPort());
        if (port.equals("80")) port = ""; else port = ":" + port;
        String path = request.getContextPath();
        url = protocol + "://" + domain
            + port + path
            + "/pages/concept_details.jsf?dictionary=" + codingScheme
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


System.out.println("code: " + code);
System.out.println("CodingScheme: " + codingScheme);
System.out.println("NameSpace: " + nameSpace);
System.out.println("name: " + name);
System.out.println("version: " + version);
System.out.println("url: " + url);
System.out.println("semanticType: " + semanticType);

        if (!_cart.containsKey(code))
            _cart.put(code,item);

        //KLO 08162023
        request.getSession().setAttribute("_cart", _cart);
        System.out.println("exiting addToCart...");

        return null;
    }


    /**
     * Remove concept(s) from the Cart
     * @return
     */

/*
    public String removeFromCart() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().removeAttribute("message");




		CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");

    	_messageflag = false;

    	if (cartActionBean.getCount() < 1) {
        	_messageflag = true;
        	_message = NO_CONCEPTS;
    	} else if (!hasSelected()) {
        	_messageflag = true;
        	_message = NOTHING_SELECTED;
    	} else {
            for (Iterator<Concept> i = cartActionBean.getConcepts().iterator(); i.hasNext();) {
                Concept item = (Concept)i.next();
                if (item.getCheckbox().isSelected()) {
                    if (_cart.containsKey(item.code))
                        i.remove();
                }
            }
    	}

    	//request.getSession().setAttribute("_cart", _cart);
        return "showcart";
    }

*/
    /**
     * Export cart in XML format
     * @return
     * @throws Exception
     */
    public String exportCartXML0(HttpServletRequest request, HttpServletResponse response) throws Exception {
        updateSelection(request);
        _messageflag = false;
        SearchCart search = new SearchCart();
        ResolvedConceptReference ref = null;
    	if (getCount() < 1) {
        	_messageflag = true;
        	_message = NO_CONCEPTS;
        	//return null;
    	}
    	if (!hasSelected()) {
        	_messageflag = true;
        	_message = NOTHING_SELECTED;
        	//return null;
    	}

        // Get Entities to be exported and build export xml string
        // in memory
		LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil
				.getLexEVSValueSetDefinitionServices();

		// Instantiate VSD
		ValueSetDefinition vsd = new ValueSetDefinition();

		// Populate VSD meta-data
		vsd.setValueSetDefinitionURI("EXPORT:VSDREF_CART");
		vsd.setValueSetDefinitionName("VSDREF_CART");
		vsd.setDefaultCodingScheme(Constants.CODING_SCHEME_NAME);
		vsd.setConceptDomain("Concepts");

		// Instantiate DefinitionEntry(Rule Set)
		DefinitionEntry de = new DefinitionEntry();

		// Assign the rule order(order this definitionEntry should be processed)
		de.setRuleOrder(1L);
		// Assign operator (OR, AND or SUBTRACT). This is to OR, AND or SUBTRACT the result of this definitionEntry from vsd resolution
		de.setOperator(DefinitionOperator.OR);

		// Instantiate ValueSetDefinitionReference which is one of the 4 definitionEntry (the other:CodingSchemeReference, EntityReference and PropertyReference)
		ValueSetDefinitionReference vsdRef = new ValueSetDefinitionReference();

		// Assign referenced VSD
		vsdRef.setValueSetDefinitionURI("EXPORT:CART_NODES");

		// Add vsdReference to definitionEntry.
		de.setValueSetDefinitionReference(vsdRef);

		// add the definitionEntry to VSD. With this, we added the first definitionEntry to VSD
		vsd.addDefinitionEntry(de);

        // Add all terms from the cart
        for (Iterator<Concept> i = getConcepts().iterator(); i.hasNext();) {
            Concept item = (Concept) i.next();

            ref = search.getConceptByCode(item.codingScheme, item.code);
            if (ref != null) {
				String EC = ref.getEntity().getEntityCode();
				String ECN = ref.getCodeNamespace();

				if (item.getSelected()) {
					_logger.debug("Exporting: " + ref.getCode());

					// Instantiate EntityReference which is one of the 4 definitionEntry
					EntityReference entityRef = new EntityReference();

					// set appropriate values for entityReference
					entityRef.setEntityCode(EC);
					entityRef.setEntityCodeNamespace(ECN);
					entityRef.setLeafOnly(false);
					entityRef.setTransitiveClosure(false);

					// To add another definitionEntry to VSD, we fist re-instantiate DefinitionEntry
					de = new DefinitionEntry();

					// Set the order and operator for this definitionEntry
					de.setRuleOrder(2L);
					de.setOperator(DefinitionOperator.OR);

					// add entityReference to definitionEntry
					de.setEntityReference(entityRef);

					// add the second definitionEntry to VSD
					vsd.addDefinitionEntry(de);
				}
			}
        }
        // Build a buffer holding the XML data

        StringBuffer buf = new StringBuffer();
		InputStream reader = vsd_service.exportValueSetResolution(vsd, null,
			null, null, false);

        String content = null;

		if (reader != null) {
			buf = new StringBuffer();
			try {
                /*
				for (int c = reader.read(); c != -1; c = reader.read()) {
					char ch = (char) c;
					System.out.println(ch);
					buf.append(ch);
				}
				*/
				int data = reader.read();
				while(data != -1) {

					char ch = (char) data;
					System.out.println(ch);

					// do something with data variable
                    buf.append((char) data);
					data = reader.read(); // read next byte
				}

			} catch (IOException e) {
				throw e;
			} finally {
				try {
					reader.close();
					content = buf.toString();
					if (!content.endsWith(">")) {
						content = content + ">";
					}

				} catch (Exception e) {
					// ignored
					e.printStackTrace();
				}
			}
		}

		// Send export file to browser

        response.setContentType(XML_CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename="
                + XML_FILE_NAME);

		response.setContentLength(content.length());

        //response.setContentLength(buf.length());
        ServletOutputStream ouputStream = response.getOutputStream();
        //ouputStream.write(buf.toString().getBytes(), 0, buf.length());


		ouputStream.write(content.getBytes("UTF-8"), 0, content.length());

        //ouputStream.write(buf.toString().getBytes("UTF-8"), 0, buf.length());

        ouputStream.flush();
        ouputStream.close();

        // Don't allow JSF to forward to cart.jsf
        //FacesContext.getCurrentInstance().responseComplete();

        return null;
    }

    public gov.nih.nci.evs.bean.Concept item2Concept(Concept item) {
         return new gov.nih.nci.evs.bean.Concept(
                    item.getCode(),
                    item.getName(),
			        item.getCodingScheme(),
			        item.getVersion(),
			        item.getNameSpace(),
			        item.getSemanticType(),
			        item.getUrl()
			        );
    }


    /**
     * Export cart in XML format
     * @return
     * @throws Exception
     */
    public String exportCartXML(HttpServletRequest request, HttpServletResponse response) throws Exception {
        updateSelection(request);
        _messageflag = false;
        SearchCart search = new SearchCart();
        ResolvedConceptReference ref = null;
    	if (getCount() < 1) {
        	_messageflag = true;
        	_message = NO_CONCEPTS;
        	//return null;
    	}
    	if (!hasSelected()) {
        	_messageflag = true;
        	_message = NOTHING_SELECTED;
        	//return null;
    	}

        // Get Entities to be exported and build export xml string
        // in memory
        gov.nih.nci.evs.bean.Concept c = null;
        gov.nih.nci.evs.bean.Cart cart = null;
        List concepts = new ArrayList();
        for (Iterator<Concept> i = getConcepts().iterator(); i.hasNext();) {
            Concept item = (Concept) i.next();
            c = item2Concept(item);
            concepts.add(c);
		}
		cart = new gov.nih.nci.evs.bean.Cart(concepts);
		String content = cart.toXML();

		// Send export file to browser
        response.setContentType(XML_CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename="
                + XML_FILE_NAME);
		response.setContentLength(content.length());

        //response.setContentLength(buf.length());
        ServletOutputStream ouputStream = response.getOutputStream();
        //ouputStream.write(buf.toString().getBytes(), 0, buf.length());

		ouputStream.write(content.getBytes("UTF-8"), 0, content.length());

        //ouputStream.write(buf.toString().getBytes("UTF-8"), 0, buf.length());

        ouputStream.flush();
        ouputStream.close();

        // Don't allow JSF to forward to cart.jsf
        //FacesContext.getCurrentInstance().responseComplete();

        return null;
    }


    /**
     * Export cart in Excel format
     * @return
     * @throws Exception
     */
    public String exportCartCSV(HttpServletRequest request, HttpServletResponse response) throws Exception {
        updateSelection(request);
        SearchCart search = new SearchCart();
        ResolvedConceptReference ref = null;
        StringBuffer sb = new StringBuffer();

try {
        _messageflag = false;

    	if (getCount() < 1) {
        	_messageflag = true;
        	_message = NO_CONCEPTS;
    	}
    	if (!hasSelected()) {
        	_messageflag = true;
        	_message = NOTHING_SELECTED;
    	}
} catch (Exception ex) {
	ex.printStackTrace();
}


        // Get Entities to be exported and build export file
        // in memory

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
            if (item.getSelected() && ref != null) {
                _logger.debug("Exporting: " + ref.getCode());
                sb.append("\"" + clean(item.name) + "\",");
                sb.append("\"" + clean(item.codingScheme) + "\",");
                sb.append("\"" + clean(item.code) + "\",");
                sb.append("\"" + clean(item.url) + "\"");
                sb.append("\r\n");
            }
        }
        // Send export file to browser

        response.setContentType(CSV_CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename="
                + CSV_FILE_NAME);
        response.setContentLength(sb.length());
        ServletOutputStream ouputStream = response.getOutputStream();
        //ouputStream.write(sb.toString().getBytes(), 0, sb.length());
        ouputStream.write(sb.toString().getBytes("UTF-8"), 0, sb.length());
        ouputStream.flush();
        ouputStream.close();

        // Don't allow JSF to forward to cart.jsf
        //FacesContext.getCurrentInstance().responseComplete();
        return null;
    }

    /**
     * Subclass to hold contents of the cart
     * @author garciawa2
     */
    public static class Concept implements Serializable {
		private static final long serialVersionUID = 1L;
        private String code = null;
        private String codingScheme = null;
        private String nameSpace = null;
        private String name = null;
        private String version = null;
        private String url = null;
        private String semanticType = null;
        private HtmlSelectBooleanCheckbox checkbox = null;
        private boolean selected = false;

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

        public HtmlSelectBooleanCheckbox getCheckbox() {
         	if (checkbox == null) checkbox = new HtmlSelectBooleanCheckbox();
            return checkbox;
        }

        public void setCheckbox(HtmlSelectBooleanCheckbox checkbox) {
            this.checkbox = checkbox;
        }

        // *** Private Methods ***
/*
        public void setSelected(boolean selected) {
			if (this.checkbox == null) this.checkbox = new HtmlSelectBooleanCheckbox();
        	this.checkbox.setSelected(selected);
        }

        public boolean getSelected() {
			if (this.checkbox == null) return false;
        	return this.checkbox.isSelected();
        }
*/

        public void setSelected(boolean selected) {
        	this.selected = selected;
        }

        public boolean getSelected() {
        	return this.selected;
        }


    } // End of Concept

    //**
    //* Utility methods
    //**

    /**
     * Test any concept in the cart has been selected
     * @return
     */
    private boolean hasSelected() {
        if (_cart != null && _cart.size() > 0) {
            for (Iterator<Concept> i = getConcepts().iterator(); i.hasNext();) {
                Concept item = (Concept)i.next();
                if (item.getSelected()) return true;
            }
        }
        return false;
    }

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
                sb.append("\t     Selected = " + item.getSelected() + "\n");
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

    public void updateSelection(HttpServletRequest request) {
		gov.nih.nci.evs.browser.bean.CartActionBean.Concept item = null;
        Set<String> paramNames = request.getParameterMap().keySet();
		Collection<gov.nih.nci.evs.browser.bean.CartActionBean.Concept> items = getConcepts();
		int count = items.size();//cartActionBean.getCount();
		if (count == 0) {
			return;
		}
		Iterator it = null;
		it = items.iterator();
		while (it.hasNext()) {
			item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it.next();
			item.setSelected(false);
		}

        for (String name : paramNames) {
            String value = request.getParameter(name);
			it = items.iterator();
			while (it.hasNext()) {
				item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it.next();
				if (item.getCode().compareTo(value) == 0) {
					item.setSelected(true);
				}
			}
		}
		HashMap cart = new HashMap();
		it = items.iterator();
		while (it.hasNext()) {
			item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it.next();
			cart.put(item.getCode(), item);
		}
		setCart(cart);
    }

} // End of CartActionBean
