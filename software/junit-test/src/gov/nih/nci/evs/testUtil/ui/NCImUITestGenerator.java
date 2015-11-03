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
import org.LexGrid.commonTypes.EntityDescription;

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

public class NCImUITestGenerator extends BaseUITestGenerator {

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

   private MetathesaurusUtils metathesaurusUtils = null; //new MetathesaurusUtils(lbSvc);

   private static String[] DEFAULT_VIH_TEST_CASES = {"C0005767", "C0007634", "C1261473"}; //blood, cells, sarcoma

   private ResolvedConceptReferenceList ncimTestCases = null; //generateNCImResolvedConceptReferenceList(int number) {
   private int NCIM_TEST_CASES = 20;
   private int NCIT_TEST_CASES = 500;


   public NCImUITestGenerator(CodeGeneratorConfiguration config) {
	   this.config = config;
       initialize();
       //dumpVector("Supported Sources:", supportedSources, "\t");
   }

   public void dumpVector(String label, Vector v, String indent) {
	   if (v == null) return;
	   System.out.println(label);
	   for (int i=0; i<v.size(); i++) {
		   String t = (String) v.elementAt(i);
		   int j = i+1;
		   System.out.println(indent + "(" + j + ") " + t);
	   }
   }

   public void initialize() {
	   test_counter = 0;
	   rvGenerator = new RandomVariateGenerator();
	   this.outputfile = config.getClassName() + ".java";

       System.out.println("Instantiate TestUtils...");
	   testUtils = new TestUtils(config.getServiceUrl());
	   System.out.println("TestUtils instantiated.");

	   remoteWebDriverURL = config.getRemoteWebDriverURL();
	   if (remoteWebDriverURL == null || remoteWebDriverURL.compareTo("") == 0) {
		   remoteWebDriverURL = CHROME_SERVER_URL;
	   } else {
		   CHROME_SERVER_URL = remoteWebDriverURL;
	   }

	   lbSvc = testUtils.getLexBIGService();
	   conceptDetails = new ConceptDetails(lbSvc);

	   codingSchemeDataUtils = testUtils.getCodingSchemeDataUtils();
	   NCIm_PROD_Version = codingSchemeDataUtils.getVocabularyVersionByTag(gov.nih.nci.evs.browser.common.Constants.NCI_METATHESAURUS, "PRODUCTION");
	   System.out.println("NCIm_PROD_Version: " + NCIm_PROD_Version);
	   testMethodName = getTestMethodName();
	   supportedSources = codingSchemeDataUtils.getSupportedSources(gov.nih.nci.evs.browser.common.Constants.NCI_METATHESAURUS, NCIm_PROD_Version);
       metathesaurusUtils = new MetathesaurusUtils(lbSvc);
       System.out.println("Generating random NCIm CUIs...");
       ncimTestCases = generateNCImResolvedConceptReferenceList(NCIT_TEST_CASES);
   }

   public String getTestMethodName() {
	   String className = config.getClassName();
	   String firstChar = className.substring(0, 1);
	   firstChar = firstChar.toLowerCase();
	   return firstChar + className.substring(1, className.length());
   }

   public String getRandomSource() {
	   int n = rvGenerator.uniform(0, supportedSources.size()-1);
	   return (String) supportedSources.elementAt(n);
   }

   public static void printSetUp(PrintWriter out, String CHROME_SERVER_URL, String baseUrl, String serviceUrl) {
	   out.println("  @Before");
       out.println("  public void setUp() throws Exception {");
       String meddra_token = ServiceTestCase.MEDDRA_TOKEN;
       out.println("    SimpleRemoteServerUtil lexEVSSvr = new SimpleRemoteServerUtil(\"" + serviceUrl + "\");");
       if (meddra_token != null && ServiceTestCase.MEDDRA_NAMES.length > 0) {
		   out.println("	Vector names = new Vector();");
		   out.println("	Vector values = new Vector();");
		   out.println("	String name = null;");
		   out.println("	String value = null;");
		   out.println("	String meddra_name = null;");
		   for (int i=0; i<ServiceTestCase.MEDDRA_NAMES.length; i++) {
			   String nm = ServiceTestCase.MEDDRA_NAMES[i];
			   out.println("	meddra_name = \"" + nm + "\";");
			   out.println("	names.add(meddra_name);");
			   out.println("	values.add(\"" + meddra_token + "\");");
		   }
		   out.println("	lexEVSSvr.setSecurityTokens(names, values);");
       }
       out.println("    lbSvc = lexEVSSvr.getLexBIGService(\"" + serviceUrl + "\");");
	   out.println("    simpleSearchUtils = new SimpleSearchUtils(lbSvc);");
	   out.println("    mappingSearchUtils = new MappingSearchUtils(lbSvc);");
	   out.println("    valueSetSearchUtils = new ValueSetSearchUtils(lbSvc);");
	   out.println("    conceptDetails = new ConceptDetails(lbSvc);");
	   out.println("    searchUtils = new SearchUtils(lbSvc);");
       out.println("\n");
       out.println("    driver = new RemoteWebDriver(new URL(\"" + CHROME_SERVER_URL + "\"), DesiredCapabilities.chrome());");
       out.println("    serviceUrl = \"" + serviceUrl + "\";");
       out.println("    baseUrl = \"" + baseUrl + "\";");
       out.println("    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);");
       out.println("    Thread.sleep(1000);");
       out.println("    driver.get(baseUrl);");
       out.println("  }");
   }


   public void run() {
	   long ms = System.currentTimeMillis();
	   pw = null;
	   boolean success = true;
	   try {
   		   pw = new PrintWriter(outputfile, "UTF-8");
		   String browserHomePage = "";
		   testCaseWriter = new NCImTestCaseWriter(
			   pw, remoteWebDriverURL, serviceUrl, baseUrl, browserHomePage);
           testCaseWriter.setDelay(config.getDelay());
           testCaseWriter.setMethodName("testNCImBrowserTestCase");
		   if (config.getPackageName() != null) {
		   	   printPackageStatement(pw, config.getPackageName());
		   }

		   System.out.println("printImportStatements...");
		   printImportStatements(pw);
		   System.out.println("printLicenseStatement...");
		   printLicenseStatement(pw);
		   System.out.println("printClassDefinition...");
		   printClassDefinition(pw, config.getClassName());
		   printSetUp(pw, CHROME_SERVER_URL, config.getBaseUrl(), config.getServiceUrl());
		   System.out.println("printWindowUtils...");
		   printWindowUtils(pw);
           System.out.println("printNavigationTabTest...");
		   printNavigationTabTest(pw);
           System.out.println("generateSimpleSearchTests...");
           generateSimpleSearchTests(ServiceTestCase.TERMINOLOGY_SAMPLE_SIZE);
           System.out.println("generateAdvancedSearchTests...");
           generateAdvancedSearchTests(ServiceTestCase.TERMINOLOGY_SAMPLE_SIZE);
           System.out.println("generateViewSourceHierarchyTests...");
           generateViewSourceHierarchyTests(pw);
           System.out.println("generateViewInSourceHierarchyLinkTests...");
           generateViewInSourceHierarchyLinkTests(pw);
           System.out.println("printNCItHierarchyLinkTest...");
           printNCItHierarchyLinkTest(pw);
           System.out.println("printTestSourcesLink...");
           printTestSourcesLink(pw);
           System.out.println("printTestSourceHelp...");
           printTestSourceHelp(pw);
           System.out.println("printTestTermTypeDefinitionsHelp...");
           printTestTermTypeDefinitionsHelp(pw);
           System.out.println("printTestFooters...");
           printTestFooters(pw);
           System.out.println("printBaseURLExternalLinksTest...");
           printBaseURLExternalLinksTest(pw);
		   printAfter(pw);
		   System.out.println("Done...");

	   } catch (Exception ex) {
		   ex.printStackTrace();
		   success = false;
	   } finally {
		   try {
			   pw.close();
			   if (success) {
			   	   System.out.println("Output file " + outputfile + " generated.");
			   	   System.out.println("NCImUITestGenerator run time (ms): " + (System.currentTimeMillis() - ms));
			   } else {
				   System.out.println("WARNING: Output file " + outputfile + " is incomplete.");
			   }

		   } catch (Exception ex) {
			   ex.printStackTrace();
		   }
	   }
   }


   public ResolvedConceptReference getRandomResolvedConceptReference() {
	   String scheme = "NCI_Thesaurus";
	   String version = null;
	   ResolvedConceptReferenceList rcrl = testUtils.generateTestCases(scheme, version, 10, TestCaseGenerator.TYPE_TERMINOLOGY);
	   int n = rvGenerator.uniform(0, rcrl.getResolvedConceptReferenceCount()-1);
	   ResolvedConceptReference rcr = (ResolvedConceptReference) rcrl.getResolvedConceptReference(n);

       ResolvedConceptReferencesIterator iterator = null;
       try {
		   iterator = new SimpleSearchUtils(lbSvc).search(
                             gov.nih.nci.evs.browser.common.Constants.NCI_METATHESAURUS, NCIm_PROD_Version, rcr.getEntityDescription().getContent(),
                             "names", "contains");
           if (iterator.hasNext()) {
			   rcr = (ResolvedConceptReference) iterator.next();
			   return rcr;
		   }
	   } catch (Exception ex) {

	   }
       return null;
   }

   public ResolvedConceptReference getRandomResolvedConceptReference(String source) {
	    SourceTreeUtils sourceTreeUtils = new SourceTreeUtils(lbSvc);
		TreeItem ti = sourceTreeUtils.getSourceTree(source);
		if (ti == null) return null;
		if (!ti._expandable) {
			return null;
		}
		Vector v = new Vector();
		Iterator iterator = ti._assocToChildMap.keySet().iterator();
		while (iterator.hasNext()) {
			String assocText = (String) iterator.next();
			List<TreeItem> children = ti._assocToChildMap.get(assocText);
			for (int k=0; k<children.size(); k++) {
				TreeItem child_ti = (TreeItem) children.get(k);
				ResolvedConceptReference rcr = new ResolvedConceptReference();
				EntityDescription ed = new EntityDescription();
				ed.setContent(child_ti._text);
				rcr.setEntityDescription(ed);
				rcr.setCode(child_ti._code);
				v.add(rcr);
			}
		}
		if (v.size() == 0) return null;
		int m = new RandomVariateGenerator().uniform(0, v.size()-1);
		return (ResolvedConceptReference) v.elementAt(m);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// simple search tests
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    gov.nih.nci.evs.testUtil.Constants
//    public static final String[] SIMPLE_SEARCH_FORM_ALGORITHMS = new String[] {"exactMatch", "contains", "startsWith"};
//    public static final String[] SIMPLE_SEARCH_FORM_TARGETS = new String[] {"names", "codes", "properties", "relationships"};


   public void generateSimpleSearchTests(int number) {
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
	   String scheme = gov.nih.nci.evs.browser.common.Constants.NCI_METATHESAURUS;
	   String version = NCIm_PROD_Version;
	   propertyName = null;
	   relationshipSourceName = null;
	   relationshipSourceCode = null;
	   relationshipName = null;
	   source = null;

	   ResolvedConceptReference rcr = getRandomResolvedConceptReference();
	   String code = rcr.getConceptCode();
	   String name = rcr.getEntityDescription().getContent();

	   // code search
	   test_counter++;
	   int testNumber = test_counter;
	   String browserLink = "";
	   String algorithm = "exactMatch";
	   String matchText = code;
	   target = "codes";
	   testCase = testCaseWriter.createSimpleSearchTestCase(testNumber, target, browserLink, scheme, version, algorithm, matchText);
	   testCaseWriter.writeTestCase(testCase);

       // name search
       String[] simple_search_algorithms = gov.nih.nci.evs.testUtil.Constants.SIMPLE_SEARCH_FORM_ALGORITHMS;
       for (int k=0; k<simple_search_algorithms.length; k++) {
		   algorithm = simple_search_algorithms[k];
		   test_counter++;
		   testNumber = test_counter;
		   matchText = name;
		   target = "names";
	       testCase = testCaseWriter.createSimpleSearchTestCase(testNumber, target, browserLink, scheme, version, algorithm, matchText);
	       testCaseWriter.writeTestCase(testCase);
	   }

	   // property search
	   Vector properties = conceptDetails.getPresentationProperties(scheme, version, code);
	   int m = rvGenerator.uniform(0, properties.size()-1);
	   String t = (String) properties.elementAt(m);
	   Vector u = StringUtils.parseData(t, "$");
	   propertyName = (String) u.elementAt(0);
	   matchText = (String) u.elementAt(1);
	   target = "properties";
       for (int k=0; k<simple_search_algorithms.length; k++) {
		   algorithm = simple_search_algorithms[k];
		   test_counter++;
		   testNumber = test_counter;
		   testCase = testCaseWriter.createSimpleSearchTestCase(testNumber, target, browserLink, scheme, version, algorithm, matchText);
		   testCaseWriter.writeTestCase(testCase);
	   }

	   // relationship search
	   Vector relationships = conceptDetails.getRelationshipSource(scheme, version, code);
	   if (relationships.size() > 0) {
		   int m2 = rvGenerator.uniform(0, relationships.size()-1);
		   String t2 = (String) relationships.elementAt(m2);
		   Vector w = StringUtils.parseData(t2, "$");
		   relationshipSourceName = (String) w.elementAt(0);
		   relationshipSourceCode = (String) w.elementAt(1);
		   relationshipName = (String) w.elementAt(2);
		   //direction = "source";
	   } else {
		   relationships = conceptDetails.getRelationshipTarget(scheme, version, code);
		   int m2 = rvGenerator.uniform(0, relationships.size()-1);
		   String t2 = (String) relationships.elementAt(m2);
		   Vector w = StringUtils.parseData(t2, "$");
		   relationshipSourceName = (String) w.elementAt(0);
		   relationshipSourceCode = (String) w.elementAt(1);
		   relationshipName = (String) w.elementAt(2);
		   //direction = "target";
	   }
	   target = "relationships";
	   for (int k=0; k<simple_search_algorithms.length; k++) {
		   algorithm = simple_search_algorithms[k];
		   matchText = relationshipSourceName;
		   test_counter++;
		   testNumber = test_counter;
		   testCase = testCaseWriter.createSimpleSearchTestCase(testNumber, target, browserLink, scheme, version, algorithm, matchText);
		   testCaseWriter.writeTestCase(testCase);
	   }
   }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    public static final String[] ADVANCED_SEARCH_ALGORITHMS = new String[] {"contains", "exactMatch", "startsWith", "lucene"};
//    public static final String[] ADVANCED_SEARCH_TARGETS = new String[] {"Code", "Name", "Property", "Relationship"};

   public void generateAdvancedSearchTests(int number) {
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
	   String scheme = gov.nih.nci.evs.browser.common.Constants.NCI_METATHESAURUS;
	   String version = NCIm_PROD_Version;
	   propertyName = null;
	   relationshipSourceName = null;
	   relationshipSourceCode = null;
	   relationshipName = null;

	   ResolvedConceptReference rcr = getRandomResolvedConceptReference();
	   String code = rcr.getConceptCode();
	   String name = rcr.getEntityDescription().getContent();

	   // code search
	   test_counter++;
	   int testNumber = test_counter;
	   String browserLink = "";
	   String algorithm = "exactMatch";
	   String matchText = code;
	   target = "Code";
	   testCase = testCaseWriter.createAdvancedSearchTestCase(testNumber, target, browserLink, scheme, version,
									algorithm, matchText,
									source, property_name,
									rel_search_association, rel_search_rela, direction);
	   testCaseWriter.writeTestCase(testCase);

       target = "Name";
       String[] adv_search_algorithms = gov.nih.nci.evs.testUtil.Constants.ADVANCED_SEARCH_ALGORITHMS;
       for (int k=0; k<adv_search_algorithms.length; k++) {
		   algorithm = adv_search_algorithms[k];
		   test_counter++;
		   propertyName = null;
		   relationshipSourceName = null;
		   relationshipSourceCode = null;
		   relationshipName = null;
		   testNumber = test_counter;
		   matchText = name;

		   testCase = testCaseWriter.createAdvancedSearchTestCase(testNumber, target, browserLink, scheme, version,
										algorithm, matchText,
										source, property_name,
										rel_search_association, rel_search_rela, direction);
		   testCaseWriter.writeTestCase(testCase);
	   }

	   propertyName = null;
	   relationshipSourceName = null;
	   relationshipSourceCode = null;
	   relationshipName = null;

	   Vector properties = conceptDetails.getPresentationProperties(scheme, version, code);

	   int m = 0;
	   String t = null;
	   Vector u = null;
	   propertyName = null;
	   matchText = null;
	   target = "Property";

       for (int k=0; k<adv_search_algorithms.length; k++) {
		   algorithm = adv_search_algorithms[k];
		   test_counter++;
		   testNumber = test_counter;

		   m = rvGenerator.uniform(0, properties.size()-1);
		   t = (String) properties.elementAt(m);
		   u = StringUtils.parseData(t, "$");
		   propertyName = (String) u.elementAt(0);
		   matchText = (String) u.elementAt(1);

		   testCase = testCaseWriter.createAdvancedSearchTestCase(testNumber, target, browserLink, scheme, version,
										algorithm, matchText,
										source, propertyName,
										rel_search_association, rel_search_rela, direction);
		   testCaseWriter.writeTestCase(testCase);
	   }

	   // relationships
	   Vector relationships = conceptDetails.getRelationshipSource(scheme, version, code);

	   propertyName = null;
	   relationshipSourceName = null;
	   relationshipSourceCode = null;
	   relationshipName = null;
	   source = null;

	   target = "Relationship";
       for (int k=0; k<adv_search_algorithms.length; k++) {
		   algorithm = adv_search_algorithms[k];
		   test_counter++;
		   testNumber = test_counter;


		   if (relationships.size() > 0) {
			   int m2 = rvGenerator.uniform(0, relationships.size()-1);
			   String t2 = (String) relationships.elementAt(m2);
			   Vector w = StringUtils.parseData(t2, "$");
			   relationshipSourceName = (String) w.elementAt(0);
			   relationshipSourceCode = (String) w.elementAt(1);
			   relationshipName = (String) w.elementAt(2);
			   direction = "source";
		   } else {
			   relationships = conceptDetails.getRelationshipTarget(scheme, version, code);
			   int m2 = rvGenerator.uniform(0, relationships.size()-1);
			   String t2 = (String) relationships.elementAt(m2);
			   Vector w = StringUtils.parseData(t2, "$");
			   relationshipSourceName = (String) w.elementAt(0);
			   relationshipSourceCode = (String) w.elementAt(1);
			   relationshipName = (String) w.elementAt(2);
			   direction = "target";
		   }
		   matchText = relationshipSourceName;
		   rel_search_association = relationshipName;
		   rel_search_rela = null;



		   testCase = testCaseWriter.createAdvancedSearchTestCase(testNumber, target, browserLink, scheme, version,
										algorithm, matchText,
										source, propertyName,
										rel_search_association, rel_search_rela, direction);
		   testCaseWriter.writeTestCase(testCase);
	   }
   }

//////////////////////////////////////////////////////////////////////////////////////////////////////////
// View Hierarchy
//////////////////////////////////////////////////////////////////////////////////////////////////////////
   public void generateViewSourceHierarchyTests(PrintWriter pw) {
       String source_hierarchies = gov.nih.nci.evs.testUtil.Constants.SOURCE_HIERARCHIES;
       Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(source_hierarchies);
       for (int i=0; i<u.size(); i++) {
            String source = (String) u.elementAt(i);
            printViewSourceHierarchyTest(pw, source);
            pw.println("\n");
	   }
   }


   public void printViewSourceHierarchyTest(PrintWriter out, String source) {
	  test_counter++;
      out.println("    @Test");
      out.println("    public void testView" + source + "HierarchyTestCase_" + test_counter + "() throws Exception {");
      out.println("		driver.get(baseUrl + \"/ncimbrowser/\");");
      out.println("		int searchType = 30; //(VIEW_HIERARCHY)");
      out.println("		String matchedString = \"No root nodes available\";");
      out.println("		new Select(driver.findElement(By.id(\"searchTerm:source\"))).selectByVisibleText(\"" + source + "\");");
      out.println("		Thread.sleep(1000);");
      out.println("		driver.findElement(By.cssSelector(\"img[alt=\\\"View " + source + " Hierarchy\\\"]\")).click();");
      out.println("		Thread.sleep(10000);");
      out.println("		String bodyText = null;");
      //out.println("		bodyText = driver.findElement(By.tagName(\"body\")).getText();");
      out.println("		bodyText = getPopupWindowBodyText(driver);");
      out.println("		try {");
      out.println("		    assertTrue(!bodyText.contains(matchedString));");
      out.println("		} catch (Exception ex) {");
      out.println("		    System.out.println(\"Hierarchy not available.\");");
      out.println("		    assertTrue(false);");
      out.println("		}");
      out.println("    }");
  }



//////////////////////////////////////////////////////////////////////////////////////////////////////////
// View Hierarchy
//////////////////////////////////////////////////////////////////////////////////////////////////////////



   public void generateSourceViewInHierarchyTests(int number) {
	   String scheme = gov.nih.nci.evs.browser.common.Constants.NCI_METATHESAURUS;
	   String version = NCIm_PROD_Version;
	   ResolvedConceptReferenceList rcrl = testUtils.generateTestCases(scheme, version, 10, number);
	   int n = rvGenerator.uniform(0, rcrl.getResolvedConceptReferenceCount()-1);
	   ResolvedConceptReference rcr = (ResolvedConceptReference) rcrl.getResolvedConceptReference(n);
	   String code = rcr.getConceptCode();
	   String name = rcr.getEntityDescription().getContent();

	   // code search
	   test_counter++;
	   int testNumber = test_counter;
	   String browserLink = null;
	   String algorithm = "exactMatch";
	   String matchText = code;
	   String target = "codes";

	   TestCase testCase = testCaseWriter.createTestCase(testNumber, TestCase.VIEW_IN_HIERARCHY,
											  browserLink,
											  scheme, version, algorithm, target, matchText);
	   testCaseWriter.writeTestCase(testCase);
   }



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   public void printNavigationTabTest(PrintWriter out) {
	   ResolvedConceptReference rcr = getRandomResolvedConceptReference();
	   String code = rcr.getConceptCode();
	   test_counter++;
       out.println("    @Test");
       out.println("    public void testNavigationTabTestCase_" + test_counter + "() throws Exception {");
       out.println("    	driver.get(baseUrl + \"/ncimbrowser/\");");
       out.println("		int searchType = 0; //(NAVIGATION_TAB)");
       out.println("    	try {");
       out.println("	    	driver.findElement(By.id(\"matchText\")).clear();");
       out.println("			Thread.sleep(1000);");
       out.println("			driver.findElement(By.id(\"matchText\")).sendKeys(\"" + code + "\");");
       out.println("			Thread.sleep(1000);");
       out.println("			driver.findElement(By.id(\"searchTarget1\")).click();");
       out.println("			Thread.sleep(1000);");
       out.println("			driver.findElement(By.id(\"searchTerm:search\")).click();");
       out.println("			Thread.sleep(1000);");
       out.println("			driver.findElement(By.name(\"sdTab\")).click();");
       out.println("			Thread.sleep(1000);");
       out.println("			driver.findElement(By.name(\"relTab\")).click();");
       out.println("			Thread.sleep(2000);");
       out.println("			driver.findElement(By.name(\"sourceTab\")).click();");
       out.println("			Thread.sleep(2000);");
       out.println("			driver.findElement(By.name(\"vaTab\")).click();");
       out.println("			Thread.sleep(1000);");
       out.println("			driver.findElement(By.cssSelector(\"div.vocabularynamebanner\")).click();");
       out.println("			Thread.sleep(1000);");
       out.println("			assertTrue(true);");
       out.println("    	} catch (Exception ex) {");
       out.println("			ex.printStackTrace();");
       out.println("			assertTrue(false);");
       out.println("    	}");
       out.println("    }");
   }


   public void printNCItHierarchyLinkTest(PrintWriter out) {
	   test_counter++;
	   String methodName = "testViewNCItHierarchyTestCase_" + test_counter;
       out.println("    @Test // (View NCI Hierarchy)");
	   out.println("    public void " + methodName + "() throws Exception {");
	   out.println("		String matchedString = \"" + gov.nih.nci.evs.browser.common.Constants.NO_ROOT_NODES_AVAILABLE + "\";");
       out.println("		String bodyText = null;");
       out.println("		Thread.sleep(1000);");
       out.println("		driver.get(baseUrl);");
       out.println("		Thread.sleep(1000);");
       out.println("		try {");
       out.println("		    popUpWindow(\"NCIt Hierarchy\");");
       out.println("		    bodyText = getPopupWindowBodyText(driver);");
       out.println("		    assertTrue(!bodyText.contains(matchedString));");
       out.println("		} catch (Exception ex) {");
       out.println("		    assertTrue(false);");
       out.println("        }");
       out.println("    }");
       out.println("\n");
   }

   public void generateViewInSourceHierarchyLinkTests(PrintWriter out) {
	   //default test cases
	   for (int i=0; i<DEFAULT_VIH_TEST_CASES.length; i++) {
		   String cui = DEFAULT_VIH_TEST_CASES[i];
		   printViewInSourceHierarchyLinkTest(out, cui);
	   }
	   pw.println("\n");
       String source_hierarchies = gov.nih.nci.evs.testUtil.Constants.SOURCE_HIERARCHIES;
       Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(source_hierarchies);
       for (int i=0; i<u.size(); i++) {
		   int n = rvGenerator.uniform(0, 3);
		   if (n == 0) {
			   String source = (String) u.elementAt(i);
			   ResolvedConceptReference rcr = getRandomResolvedConceptReference(source);
			   printViewInSourceHierarchyLinkTest(out, rcr.getCode());
			   pw.println("\n");
	       }
	   }
   }

   public void printViewInSourceHierarchyLinkTest(PrintWriter out, String cui) {
	  test_counter++;
      out.println("	@Test // View In Source Hierarchy Hyperlinks");
      out.println("	public void testViewInSourceHierarhyTestCase_" + test_counter + "() throws Exception {");
      out.println("		driver.get(baseUrl + \"/ncimbrowser/\");");
      out.println("		int searchType = 31; //(VIEW_IN_HIERARCHY)");
      out.println("		String matchText = \"" + cui + "\";");
      out.println("		Thread.sleep(1000);");
      out.println("		driver.get(baseUrl);");
      out.println("		Thread.sleep(1000);");
      out.println("		driver.findElement(By.xpath(\"//input[@name='algorithm'][@value='exactMatch']\")).click();");
      out.println("		Thread.sleep(1000);");
      out.println("		driver.findElement(By.xpath(\"//input[@name='searchTarget'][@value='codes']\")).click();");
      out.println("		Thread.sleep(1000);");
      out.println("		driver.findElement(By.name(\"matchText\")).clear();");
      out.println("		driver.findElement(By.name(\"matchText\")).sendKeys(matchText);");
      out.println("		Thread.sleep(1000);");
      out.println("		driver.findElement(By.name(\"searchTerm:search\")).click();");
      out.println("		Thread.sleep(4000);");
      out.println("		driver.findElement(By.name(\"sdTab\")).click();");
      out.println("		List<WebElement> listImg = driver.findElements(By.xpath(\"//img[contains(@src,'/ncimbrowser/images/visualize.gif')]\"));");
      out.println("		if (listImg != null && listImg.size() > 0) {");
      out.println("			try {");
      out.println("				for (int i=0; i<listImg.size(); i++) {");
      out.println("					listImg.get(i).click();");
      out.println("					Thread.sleep(4000);");
      out.println("				}");
      out.println("			} catch (Exception ex) {");
      out.println("				assertTrue(false);");
      out.println("			}");
      out.println("	    }");
      out.println("		assertTrue(true);");
      out.println("	}");
      out.println("\n");
   }

   public void printBaseURLExternalLinksTest(PrintWriter out) {
      out.println("    @Test //testBaseURLExternalLinks");
      out.println("    public void testBaseURLExternalLinks() throws Exception {");
      out.println("		try {");
      out.println("			driver.get(baseUrl);");
      out.println("			java.util.List<WebElement> links = driver.findElements(By.tagName(\"a\"));");
      out.println("			HashSet hset = new HashSet();");
      out.println("			int lcv = 0;");
      out.println("			for (int i=0; i<links.size(); i++) {");
      out.println("				String href = links.get(i).getAttribute(\"href\");");
      out.println("				if (href != null && href.length()>0) {");
      out.println("					if (!hset.contains(href)) {");
      out.println("						hset.add(href);");
      out.println("					}");
      out.println("			    }");
      out.println("			}");
      out.println("			Iterator it = hset.iterator();");
      out.println("			while (it.hasNext()) {");
      out.println("				String href = (String) it.next();");
      out.println("				if (!href.startsWith(baseUrl)) {");
      out.println("					lcv++;");
      out.println("					int responseCode = getHTTPResponseCode(href);");
      out.println("					System.out.println(\"(\" + lcv + \") \" + href + \" (response code: \" + responseCode + \")\");");
      out.println("					assertTrue(responseCode == 200);");
      out.println("				}");
      out.println("			}");
      out.println("	    } catch (Exception ex) {");
      out.println("            ex.printStackTrace();");
      out.println("            assertTrue(false);");
      out.println("		}");
      out.println("		assertTrue(true);");
      out.println("	}");
      out.println("\n");
  }


   public void printTestFooter(PrintWriter out, String linkLabel) {
	   String method_name = "test" + linkLabel + "Footer";
	   method_name = StringUtils.removeWhiteSpaces(method_name);//method_name.replaceAll(" ", "_");
	   out.println("    @Test // (" + method_name + ")");
	   out.println("    public void " + method_name + "() throws Exception {");
	   out.println("		String bodyText = null;");
	   out.println("		driver.get(baseUrl);");
       out.println("		Thread.sleep(1000);");
       out.println("		try {");
       out.println("		    popUpWindow(\"" + linkLabel + "\");");
       out.println("		    assertTrue(true);");
       out.println("		} catch (Exception ex) {");
       out.println("		    assertTrue(false);");
       out.println("        }");
       out.println("    }");
       out.println("\n");
   }

   public void printTestFooters(PrintWriter out) {
	   printTestFooter(out, "NCI Home");
	   printTestFooter(out, "Policies");
	   printTestFooter(out, "Accessibility");
	   printTestFooter(out, "FOIA");
	   printTestFooter(out, "Contact Us");
   }


   public void printTestTermTypeDefinitionsHelp(PrintWriter out) {
	   String method_name = "testTermTypeDefinitionsHelp";
	   out.println("    @Test // (" + method_name + ")");
	   out.println("    public void testTermTypeDefinitionsHelp() throws Exception {");
	   out.println("		driver.get(baseUrl + \"/ncimbrowser/\");");
	   String scheme = gov.nih.nci.evs.browser.common.Constants.NCI_METATHESAURUS;
	   String version = NCIm_PROD_Version;

	   //ResolvedConceptReferenceList testCases = testUtils.generateTestCases(scheme, version, 10, TestCaseGenerator.TYPE_TERMINOLOGY);
	   ResolvedConceptReferenceList testCases = ncimTestCases;
	   int n = rvGenerator.uniform(0, testCases.getResolvedConceptReferenceCount()-1);
	   ResolvedConceptReference rcr = (ResolvedConceptReference) testCases.getResolvedConceptReference(n);

	   if (rcr == null) {
	    	System.out.println("ResolvedConceptReference is NULL -- return ");
	   }
	   String code = rcr.getConceptCode();
       out.println("        driver.findElement(By.xpath(\"//input[@name='algorithm'][@value='exactMatch']\")).click();");
	   out.println("		driver.findElement(By.name(\"matchText\")).clear();");
	   out.println("		driver.findElement(By.id(\"searchTarget1\")).click();");
	   out.println("		driver.findElement(By.name(\"matchText\")).sendKeys(\"" + code + "\");");

       out.println("		driver.findElement(By.id(\"searchTerm:search\")).click();");
       out.println("		Thread.sleep(1000);");
       out.println("		driver.findElement(By.name(\"sdTab\")).click();");
       out.println("		Thread.sleep(1000);");
       out.println("		driver.findElement(By.cssSelector(\"img[alt=\\\"Term Type Definitions\\\"]\")).click();");

       out.println("		String bodyText = null;");
       out.println("		String matchedString = \"Abbreviation in any source vocabulary\";");
       out.println("		Thread.sleep(4000);");
       out.println("		bodyText = getPopupWindowBodyText(driver);");
       out.println("		assertTrue(bodyText.contains(matchedString));");
       out.println("		Thread.sleep(1000);    ");
	   out.println("    }");
	   out.println("");
   }

   public void printTestSourceHelp(PrintWriter out) {
	   String method_name = "testSourceHelp";
	   out.println("    @Test // (" + method_name + ")");
	   out.println("    public void testSourceHelp() throws Exception {");
	   out.println("		driver.get(baseUrl + \"/ncimbrowser/\");");
	   String scheme = gov.nih.nci.evs.browser.common.Constants.NCI_METATHESAURUS;
	   String version = NCIm_PROD_Version;
	   //ResolvedConceptReferenceList testCases = testUtils.generateTestCases(scheme, version, 10, TestCaseGenerator.TYPE_TERMINOLOGY);
	   ResolvedConceptReferenceList testCases = ncimTestCases;//testUtils.generateTestCases(scheme, version, 10, TestCaseGenerator.TYPE_TERMINOLOGY);
	   int n = rvGenerator.uniform(0, testCases.getResolvedConceptReferenceCount()-1);
	   ResolvedConceptReference rcr = (ResolvedConceptReference) testCases.getResolvedConceptReference(n);
	   if (rcr == null) {
	    	System.out.println("ResolvedConceptReference is NULL -- return ");
	   }
	   String code = rcr.getConceptCode();
       out.println("        driver.findElement(By.xpath(\"//input[@name='algorithm'][@value='exactMatch']\")).click();");
	   out.println("		driver.findElement(By.name(\"matchText\")).clear();");
	   out.println("		driver.findElement(By.id(\"searchTarget1\")).click();");
	   out.println("		driver.findElement(By.name(\"matchText\")).sendKeys(\"" + code + "\");");

       out.println("		driver.findElement(By.id(\"searchTerm:search\")).click();");
       out.println("		Thread.sleep(1000);");
       out.println("		driver.findElement(By.name(\"sdTab\")).click();");
       out.println("		Thread.sleep(1000);");
       out.println("		driver.findElement(By.cssSelector(\"img[alt=\\\"Source List\\\"]\")).click();");

       out.println("		String bodyText = null;");
       out.println("		String matchedString = \"National Cancer Institute Thesaurus\";");
       out.println("		Thread.sleep(4000);");
       out.println("		bodyText = getPopupWindowBodyText(driver);");
       out.println("		assertTrue(bodyText.contains(matchedString));");
       out.println("		Thread.sleep(1000);    ");
	   out.println("    }");
	   out.println("");
   }


   public void printTestSourcesLink(PrintWriter out) {
	   String method_name = "testSourcesLink";
	   out.println("    @Test // (" + method_name + ")");
	   out.println("    public void testSourcesLink() throws Exception {");
	   out.println("		driver.get(baseUrl + \"/ncimbrowser/\");");

	   out.println("        driver.findElement(By.linkText(\"Sources\")).click();");
       out.println("		String bodyText = null;");
       out.println("		String matchedString = \"National Cancer Institute Thesaurus\";");
       out.println("		Thread.sleep(4000);");
       out.println("		bodyText = getPopupWindowBodyText(driver);");
       out.println("		assertTrue(bodyText.contains(matchedString));");
       out.println("		Thread.sleep(1000);    ");
	   out.println("    }");
	   out.println("");
   }


   public static void printTestStatement(PrintWriter pw, String label) {
	   pw.println("   @Test " + "//" + label);
   }

   ResolvedConceptReferenceList generateNCImResolvedConceptReferenceList(int number) {
	   String scheme = gov.nih.nci.evs.browser.common.Constants.NCI_THESAURUS;
	   String version = null;
	   ResolvedConceptReferenceList rcrl = testUtils.generateTestCases(scheme, version, number, TestCaseGenerator.TYPE_TERMINOLOGY);
		//Generate NCI Thesaurus
		// map NCI Thesaurus to NCIm Metahesaurus
	   String ncim_scheme = gov.nih.nci.evs.browser.common.Constants.NCI_METATHESAURUS;
	   String ncim_version = NCIm_PROD_Version;
	   ResolvedConceptReferenceList testCases = new ResolvedConceptReferenceList();
       if (rcrl != null) {
		   for (int i=0; i<rcrl.getResolvedConceptReferenceCount(); i++) {
			   ResolvedConceptReference rcr = (ResolvedConceptReference) rcrl.getResolvedConceptReference(i);
			   String code = rcr.getConceptCode();
			   Vector v = metathesaurusUtils.getMatchedMetathesaurusCUIs(scheme, version, null, code);
			   if (v != null && v.size() > 0) {
				   ResolvedConceptReference ncim_rcr = new ResolvedConceptReference();
				   Entity entity = new Entity();
				   String cui = (String) v.elementAt(0);
				   entity.setEntityCodeNamespace(ncim_scheme);
				   entity.setEntityCode(cui);
				   ncim_rcr.setEntity(entity);
				   testCases.addResolvedConceptReference(ncim_rcr);
				   if (testCases.getResolvedConceptReferenceCount() == NCIM_TEST_CASES) break;
		       }
		   }

	   } else {
    		System.out.println("WARNING: generateNCImResolvedConceptReferenceList rcrl returns null???");
       }
	   return testCases;
   }

   public static void main(String[] args) {
	   CodeGeneratorConfiguration config = new CodeGeneratorConfiguration();
	   config.setPackageName("selenium.webapps.termbrowser");
	   config.setClassName("TestTermBrowserTestCase");
	   config.setBaseUrl("http://nciterms.nci.nih.gov/ncimbrowser");
	   config.setServiceUrl("http://lexevsapi.nci.nih.gov/lexevsapi63");
	   NCImUITestGenerator generator = new NCImUITestGenerator(config);
	   generator.run();
   }
}


