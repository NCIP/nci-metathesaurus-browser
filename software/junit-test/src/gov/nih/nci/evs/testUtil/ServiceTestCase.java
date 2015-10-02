package gov.nih.nci.evs.testUtil;


import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008-2015 NGIT. This software was developed in conjunction
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
 * Modification history:
 *     Initial implementation kim.ong@ngc.com
 *
 */

abstract public class ServiceTestCase extends TestCase{

	/** The sys prop. */
	private static Properties sysProp = System.getProperties();
//	private static final String DEFAULT_SAX_DRIVER_CLASS ="org.apache.xerces.parsers.SAXParser";
//	private static SchemaInfo info = readConfig();
//	readConfig();

	/** The dom. */
	private static Document dom;

	/** The properties. */
	private static Properties properties = loadProperties();

	/** The Constant serviceUrl. */
	public final static String serviceUrl = properties.getProperty("serviceUrl");

	/** The Constant termbrowserUrl. */
	public final static String termbrowserUrl = properties.getProperty("termbrowserUrl");

	/** The Constant endpointUrl. */
	public final static String endpointUrl = properties.getProperty("endpointUrl");

    public final static int SAMPLE_SIZE = Integer.parseInt(properties.getProperty("SAMPLE_SIZE"));
    public final static int TERMINOLOGY_SAMPLE_SIZE = Integer.parseInt(properties.getProperty("TERMINOLOGY_SAMPLE_SIZE"));
    public final static int MAPPING_SAMPLE_SIZE = Integer.parseInt(properties.getProperty("MAPPING_SAMPLE_SIZE"));
    public final static int VALUE_SET_SAMPLE_SIZE = Integer.parseInt(properties.getProperty("VALUE_SET_SAMPLE_SIZE"));

	/** The Constant MEDDRA_TOKEN. */
	public final static String MEDDRA_TOKEN = properties.getProperty("MEDDRA_TOKEN");

    public final static String[] MEDDRA_NAMES =
        new String[] {"MedDRA (Medical Dictionary for Regulatory Activities Terminology)", "MedDRA"};


	/**
	 * To be implemented by each descendant testcase.
	 *
	 * @return String
	 */
	protected String getTestID(){
		return "LexEVS Service Test Case";
	}


	/**
	 * Load properties.
	 *
	 * @return the properties
	 */
	private static Properties loadProperties(){
		try{
			//String propertyFile = sysProp.getProperty("resources/Test.properties");
			String propertyFile = "resources/Test.properties";
			Properties lproperties = new Properties();
			FileInputStream fis = new FileInputStream(new File(propertyFile));
			lproperties.load(fis);
			return lproperties;
		} catch (Exception e){
			System.out.println("Error reading properties file");
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * Parses the xml file.
	 *
	 * @param filename the filename
	 */
	private static void parseXMLFile(String filename)
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try{
			DocumentBuilder db = dbf.newDocumentBuilder();

			dom=db.parse(filename);

		}
		catch (Exception e){
			e.printStackTrace();
		}

	}
}


