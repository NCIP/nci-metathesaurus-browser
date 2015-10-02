package gov.nih.nci.evs.testUtil.ui;
import gov.nih.nci.evs.testUtil.*;

import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.browser.bean.*;
import gov.nih.nci.evs.browser.common.*;

import java.io.*;
import java.util.*;
import java.net.*;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.concepts.Entity;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;


/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2015 NGIT. This software was developed in conjunction
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

public class NCImTestCaseDataGenerator extends BaseUITestGenerator {

   private String outputfile = null;
   private String packageName = null;
   private String className = null;
   private String testMethodName = null;
   private String baseUrl = null; //browser home
   private String remoteWebDriverURL = null; //browser home

   private HashMap rootHashMap = null;
   private String serviceUrl = null; // lexevsapi service url
   private RandomVariateGenerator rvGenerator = null;

   private String CHROME_SERVER_URL = "http://localhost:9515";

   private Vector rootHashmapKeys = null;
   private CodeGeneratorConfiguration config = null;

   private CodingSchemeDataUtils codingSchemeDataUtils = null;

   private int test_counter = 0;

   private String NCIm_PROD_Version = null;

   private LexBIGService lbSvc = null;

   private TestUtils testUtils = null;

   private ConceptDetails conceptDetails = null;
   private NCImTestCaseWriter testCaseWriter = null;

   private PrintWriter pw = null;

   private Vector supportedSources = null;

   private static String ViewInSourceHierarchyCUI = "C0005767"; //Blood

// String data = scheme + "$" + version + "$" + code + "$" + name + "$" + propertyNameAndValue + "$" + associationNameAndValueAndSource + "$" + source;

   private static int SCHEME_INDEX = 0;
   private static int VERSION_INDEX = 1;
   private static int CODE_INDEX = 2;
   private static int NAME_INDEX = 3;

   private static int PROPERTY_NAME_INDEX = 4;
   private static int PROPERTY_VALUE_INDEX = 5;
   private static int IS_PREFERRED_INDEX = 6; // "true" or "false" string


   private static int ASSOCIATION_NAME_INDEX = 7;
   private static int ASSOCIATION_VALUE_INDEX = 8;
   private static int ASSOCIATION_SRC_INDEX = 9;

   private static int SOURCE_INDEX = 10;


   public NCImTestCaseDataGenerator(CodeGeneratorConfiguration config) {
	   this.config = config;
       initialize();
   }

   public void initialize() {
	   test_counter = 0;
	   rvGenerator = new RandomVariateGenerator();
	   this.outputfile = config.getClassName() + ".java";
	   testUtils = new TestUtils(config.getServiceUrl());

	   lbSvc = testUtils.getLexBIGService();
	   conceptDetails = new ConceptDetails(lbSvc);

	   codingSchemeDataUtils = testUtils.getCodingSchemeDataUtils();
	   NCIm_PROD_Version = codingSchemeDataUtils.getVocabularyVersionByTag(gov.nih.nci.evs.browser.common.Constants.NCI_METATHESAURUS, "PRODUCTION");
	   System.out.println("NCIm_PROD_Version: " + NCIm_PROD_Version);
	   supportedSources = codingSchemeDataUtils.getSupportedSources(gov.nih.nci.evs.browser.common.Constants.NCI_METATHESAURUS, NCIm_PROD_Version);
   }

   public String getNCIm_PROD_Version() {
	   return NCIm_PROD_Version;
   }


   public String generateSource() {
	   int n = rvGenerator.uniform(0, supportedSources.size()-1);
	   return (String) supportedSources.elementAt(n);
   }


   public String patch(String t) {
	   t = t.replaceAll(":", " ");
	   return t;
   }

   public ResolvedConceptReference generateResolvedConceptReference() {
	   String scheme = "NCI_Thesaurus";
	   String version = null;
	   ResolvedConceptReferenceList rcrl = testUtils.generateTestCases(scheme, version, ServiceTestCase.SAMPLE_SIZE, TestCaseGenerator.TYPE_TERMINOLOGY);
	   int n = rvGenerator.uniform(0, rcrl.getResolvedConceptReferenceCount()-1);
	   ResolvedConceptReference rcr = (ResolvedConceptReference) rcrl.getResolvedConceptReference(n);

       ResolvedConceptReferencesIterator iterator = null;
       try {
           String t = rcr.getEntityDescription().getContent();
           t = patch(t);
		   iterator = new SimpleSearchUtils(lbSvc).search(
                             gov.nih.nci.evs.browser.common.Constants.NCI_METATHESAURUS, NCIm_PROD_Version, t, "names", "contains");

           if (iterator.hasNext()) {
			   rcr = (ResolvedConceptReference) iterator.next();
			   return rcr;
		   }
	   } catch (Exception ex) {

	   }
       return null;
   }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// TestCaseData
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
    public static final String[] SIMPLE_SEARCH_ALGORITHMS = new String[] {"exactMatch", "contains", "lucene"};
    public static final String[] SIMPLE_SEARCH_TARGETS = new String[] {"names", "codes"};

    public static final String[] ADVANCED_SEARCH_ALGORITHMS = new String[] {"contains", "exactMatch", "startsWith", "lucene"};
    public static final String[] ADVANCED_SEARCH_TARGETS = new String[] {"Code", "Name", "Property", "Relationship"};

    public static final String[] SIMPLE_SEARCH_FORM_ALGORITHMS = new String[] {"exactMatch", "contains", "startsWith"};
    public static final String[] SIMPLE_SEARCH_FORM_TARGETS = new String[] {"names", "codes", "properties", "relationships"};
*/



   public String generateTarget() {
	   int n = rvGenerator.uniform(0, gov.nih.nci.evs.testUtil.Constants.SIMPLE_SEARCH_FORM_TARGETS.length-1);
	   return gov.nih.nci.evs.testUtil.Constants.SIMPLE_SEARCH_FORM_TARGETS[n];
   }

   public String generateAlgorithm() {
	   int n = rvGenerator.uniform(0, gov.nih.nci.evs.testUtil.Constants.SIMPLE_SEARCH_FORM_ALGORITHMS.length-1);
	   return gov.nih.nci.evs.testUtil.Constants.SIMPLE_SEARCH_FORM_ALGORITHMS[n];
   }

   public String generateProperty(String scheme, String version, String code) {
	   Vector properties = conceptDetails.getPresentationProperties(scheme, version, code);
	   if (properties.size() > 0) {
		   int m = rvGenerator.uniform(0, properties.size()-1);
		   String t = (String) properties.elementAt(m);
		   return t;
	   }
	   return null;
   }

   public String generateRelationship(String scheme, String version, String code) {
	   Vector relationships = conceptDetails.getRelationshipSource(scheme, version, code);
	   if (relationships.size() > 0) {
		   int m2 = rvGenerator.uniform(0, relationships.size()-1);
		   String t2 = (String) relationships.elementAt(m2);
		   return t2;
	   } else {
		   relationships = conceptDetails.getRelationshipTarget(scheme, version, code);
		   int m2 = rvGenerator.uniform(0, relationships.size()-1);
		   String t2 = (String) relationships.elementAt(m2);
		   return t2;
	   }
   }


   //String scheme = gov.nih.nci.evs.browser.common.Constants.NCI_METATHESAURUS;
   //String version = NCIm_PROD_Version;
   public Vector generateTestCaseData(String scheme, String version, int number) {
	   Vector w = new Vector();
	   String target = null;
	   String source = null;
	   String property_name = null;
	   String rel_search_association = null;
	   String rel_search_rela = null;
	   String direction = null;
	   String propertyName = null;
	   String relationshipSourceName = null;
	   String relationshipSourceCode = null;
	   String relationshipName = null;
	   TestCase testCase = null;
	   propertyName = null;
	   relationshipSourceName = null;
	   relationshipSourceCode = null;
	   relationshipName = null;
	   source = null;

       for (int i=0; i<number; i++) {
		   ResolvedConceptReference rcr = generateResolvedConceptReference();
		   String code = rcr.getConceptCode();
		   String name = rcr.getEntityDescription().getContent();
		   String propertyNameAndValue = generateProperty(scheme, version, code);
		   String associationNameAndValueAndSource = generateRelationship(scheme, version, code);

//System.out.println("*propertyNameAndValue: " + propertyNameAndValue);
//System.out.println("*associationNameAndValueAndSource: " + associationNameAndValueAndSource);


		   source = generateSource();
		   //CL429748$Blood$presentation$Blood$true$HEMATOPOIETIC AND RETICULOENDOTHELIAL SYSTEMS$CL028727$CHD$MTHHH
		   String data = scheme + "$" + version + "$" + code + "$" + name + "$" + propertyNameAndValue + "$" + associationNameAndValueAndSource + "$" + source;
		   w.add(data);
	   }
	   return w;
   }

   public void dumpVector(Vector v) {
	   for (int i=0; i<v.size(); i++) {
		   String t = (String) v.elementAt(i);
		   System.out.println(t);
	   }
   }

   public static void main(String[] args) {
	   CodeGeneratorConfiguration config = new CodeGeneratorConfiguration();
	   config.setPackageName("selenium.webapps.termbrowser");
	   config.setClassName("TestNCImBrowserTestCase");
	   config.setBaseUrl("http://ncim-stage.nci.nih.gov/ncitbrowser");
	   config.setServiceUrl("http://lexevsapi6-stage.nci.nih.gov/lexevsapi63");
	   NCImTestCaseDataGenerator generator = new NCImTestCaseDataGenerator(config);
	   String scheme = gov.nih.nci.evs.browser.common.Constants.NCI_METATHESAURUS;
	   String version = generator.getNCIm_PROD_Version();
	   long ms = System.currentTimeMillis();
	   Vector w = generator.generateTestCaseData(scheme, version, 10);
	   System.out.println("generateTestCaseData run time (ms): " + (System.currentTimeMillis() - ms));
	   generator.dumpVector(w);
   }
}



