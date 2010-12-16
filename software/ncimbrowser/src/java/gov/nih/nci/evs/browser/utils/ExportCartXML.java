package gov.nih.nci.evs.browser.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Vector;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

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
 * Class to generate a XML document of Concepts (Entity)
 *
 * @author garciawa2
 */
public class ExportCartXML {

    private Document doc = null;
    private DocumentBuilder dBuilder = null;
    private static Logger _logger = Logger.getLogger(SearchUtils.class);
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    // XML attributes
    static private final String ATT_RESOURCE = "rdf:resource";
    static private final String ATT_RES_MARK = "#";

    // XML tags
    static private final String TAG_RDF = "rdf:RDF";
    static private final String TAG_CLASS = "owl:Class";
    static private final String TAG_SCHEME = "scheme";
    static private final String TAG_PARENT = "rdfs:subClassOf";
    static private final String TAG_CHILD = "owl:hasClass";

    // XML formatting defaults
    static private final int INDENT = 2;
    static private final int LINE_WIDTH = 500;

    /**
     * Constructor
     */
    public ExportCartXML() {
        // Setup a new empty XML document
        Document nDoc = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            nDoc = dBuilder.newDocument();
            this.doc = nDoc;
        } catch (ParserConfigurationException e) {
            _logger.info("Error: " + e.getMessage());
        }
    }

    /**
     * Add document tag
     */
    public void addDocumentTag() {
        if (doc == null) {
            _logger.info("Error addCartTag: document is null!");
            return;
        }
        Element rdftag = doc.createElement(TAG_RDF);
        // RDF Header
        rdftag.setAttribute("xmlns:owl","http://www.w3.org/2002/07/owl#");
        rdftag.setAttribute("xmlns:rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        rdftag.setAttribute("xmlns:rdfs","http://www.w3.org/2000/01/rdf-schema#");
        rdftag.setAttribute("xmlns:xsd","http://www.w3.org/2001/XMLSchema#");
        // Add new RDF tag
        doc.appendChild(rdftag);
    }

    /**
     * Add a comment tag
     */
    public void addCommentTag() {
        if (doc == null) {
            _logger.info("Error addCommentTag: document is null!");
            return;
        }
        // Add description
        Element root = doc.getDocumentElement();
        Element desChild = doc.createElement("rdfs:comment");
        desChild.setTextContent("NCI Term Browser: Export of user cart on " + now());
        root.appendChild(desChild);
    }

    /**
     * Add a term tag with its properties
     *
     * @param name
     * @param code
     * @param codingScheme
     * @param properties
     */
    public void addTermTag(String name, String code, String codingScheme,
            Property[] pres, Property[] def, Property[] prop, Property[] com,
            Vector<Entity> parents, Vector<Entity> children) {
        if (doc == null) {
            _logger.info("Error addTermTag: document is null!");
            return;
        }

        Element root = doc.getDocumentElement();

        // Class element
        Element classtag = doc.createElement(TAG_CLASS);
        classtag.setAttribute("rdf:ID", name);

        // Scheme element
        Element scheme = doc.createElement(TAG_SCHEME);
        scheme.setTextContent(codingScheme);
        classtag.appendChild(scheme);

        // Add presentation elements
        for (int x = 0; x < pres.length; x++) {
            Property p = (Property) pres[x];
            Element property = doc.createElement(p.getPropertyName());
            property.setTextContent(p.getValue().getContent());
            classtag.appendChild(property);
        }

        // Add definition elements
        for (int x = 0; x < def.length; x++) {
            Property p = (Property) def[x];
            Element property = doc.createElement(p.getPropertyName());
            property.setTextContent(p.getValue().getContent());
            classtag.appendChild(property);
        }

        // Add property elements
        for (int x = 0; x < prop.length; x++) {
            Property p = (Property) prop[x];
            Element property = doc.createElement(p.getPropertyName());
            property.setTextContent(p.getValue().getContent());
            classtag.appendChild(property);
        }

        // Add comment elements
        for (int x = 0; x < com.length; x++) {
            Property p = (Property) com[x];
            Element property = doc.createElement(p.getPropertyName());
            property.setTextContent(p.getValue().getContent());
            classtag.appendChild(property);
        }

        // Add parent elements
        if (parents != null) {
            for (int x = 0; x < parents.size(); x++) {
                Entity concept = parents.get(x);
                Element parent = doc.createElement(TAG_PARENT);
                parent.setAttribute(ATT_RESOURCE, ATT_RES_MARK + concept.getEntityDescription()
                        .getContent());
                classtag.appendChild(parent);
            }
        }

        // Add Children elements
        if (children != null) {
            for (int x = 0; x < children.size(); x++) {
                Entity concept = children.get(x);
                Element child = doc.createElement(TAG_CHILD);
                child.setAttribute(ATT_RESOURCE, ATT_RES_MARK + concept.getEntityDescription()
                        .getContent());
                classtag.appendChild(child);
            }
        }

        root.appendChild(classtag);
    }

    /**
     * Generate an XML String of the document
     * @return
     */
    public String generateXMLString() {
        if (doc == null) return null;
        StringWriter strWriter = new StringWriter();
        try {
            doc.normalizeDocument();
            OutputFormat format = new OutputFormat(doc);
            format.setIndenting(true);
            format.setIndent(INDENT);
            format.setLineWidth(LINE_WIDTH);
            XMLSerializer serializer = new XMLSerializer(strWriter,format);
            serializer.serialize(doc);
            return strWriter.toString();
        } catch (TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Return a date stamp
     * @return
     */
    private static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());

    }

} // End of ExportCartXML
