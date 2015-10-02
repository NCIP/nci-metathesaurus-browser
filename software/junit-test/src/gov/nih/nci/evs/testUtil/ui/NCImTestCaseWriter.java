package gov.nih.nci.evs.testUtil.ui;

import java.io.*;
import java.util.*;
import java.net.*;

import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.browser.common.*;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;


public class NCImTestCaseWriter
{

// Variable declaration
	private PrintWriter out;
	private String remoteWebDriverURL;
	private String serviceUrl;
	private String baseUrl;
	private String browserHomePage;

	private int short_delay = 1; // second
	private int medium_delay = 2 * short_delay; // second
	private int long_delay = 4 * short_delay; // second

	private String methodName = "testNCImBrowserTestCase";


// Default constructor
	public NCImTestCaseWriter() {

	}

	public NCImTestCaseWriter(String methodName) {
		this.methodName = methodName;
	}

// Constructor
	public NCImTestCaseWriter(
		PrintWriter out,
		String remoteWebDriverURL,
		String serviceUrl,
		String baseUrl,
		String browserHomePage) {

		this.out = out;
		this.remoteWebDriverURL = remoteWebDriverURL;
		this.serviceUrl = serviceUrl;
		this.baseUrl = baseUrl;
		this.browserHomePage = browserHomePage;
	}


	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

// Set methods

	public void setDelay(int second) {
		this.short_delay = second;
		this.medium_delay = 2 * second;
		this.long_delay = 4 * second;
	}



	public void setPw(PrintWriter out) {
		this.out = out;
	}

	public void setRemoteWebDriverURL(String remoteWebDriverURL) {
		this.remoteWebDriverURL = remoteWebDriverURL;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setBrowserHomePage(String browserHomePage) {
		this.browserHomePage = browserHomePage;
	}


// Get methods
	public PrintWriter getPrintWriter() {
		return this.out;
	}

	public String getRemoteWebDriverURL() {
		return this.remoteWebDriverURL;
	}

	public String getServiceUrl() {
		return this.serviceUrl;
	}

	public String getBaseUrl() {
		return this.baseUrl;
	}

	public String getBrowserHomePage() {
		return this.browserHomePage;
	}

    public void wait(int second) {
		int milliseconds = second * 1000;
		out.println("		Thread.sleep(" + milliseconds + ");");
	}

	public void writeParameters(TestCase testCase) {
        out.println("		String linkText = \"" +  testCase.getBrowserLink() + "\";");
        String type_description = TestCase.searchType2Name(testCase.getSearchType());
        out.println("		int searchType = " +  testCase.getSearchType() + "; //(" + type_description + ")");

        if (testCase.getSearchType() != TestCase.ALL_TERMINOLOGY_SEARCH_ON_CODE &&
            testCase.getSearchType() != TestCase.ALL_TERMINOLOGY_SEARCH_ON_NAME &&
            testCase.getSearchType() != TestCase.ALL_TERMINOLOGY_SEARCH_ON_PROPERTY &&
            testCase.getSearchType() != TestCase.ALL_TERMINOLOGY_SEARCH_ON_RELATIONSHIP &&
			testCase.getSearchType() != TestCase.ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_CODE &&
            testCase.getSearchType() != TestCase.ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_NAME &&
            testCase.getSearchType() != TestCase.ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_PROPERTY &&
            testCase.getSearchType() != TestCase.ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_RELATIONSHIP) {

			out.println("		String scheme = \"" +  testCase.getScheme() + "\";");
			out.println("		String version = \"" +  testCase.getVersion() + "\";");
		}

        if (testCase.getSearchType() != TestCase.VIEW_HIERARCHY) {
			out.println("		String target = \"" +  testCase.getTarget() + "\";");
			out.println("		String algorithm = \"" +  testCase.getAlgorithm() + "\";");
			out.println("		String matchText = \"" +  testCase.getMatchText() + "\";");
       	    out.println("		String matchedString = \"No match\";");
	    }

        if (testCase.getSearchType() == TestCase.VIEW_HIERARCHY) {
			out.println("		String matchedString = \"" + Constants.NO_ROOT_NODES_AVAILABLE + "\";");


        } else if (testCase.getSearchType() == TestCase.VIEW_IN_HIERARCHY) {
			out.println("		ResolvedConceptReference rcr = new TestCaseValidator(lbSvc).validate(");
			out.println("			scheme, version, matchText, target, algorithm);");
			out.println("		if (rcr != null) {");
			out.println("			matchedString = rcr.getEntityDescription().getContent();");
			out.println("			System.out.println(matchedString + \" (\" + rcr.getConceptCode() + \")\");");
			out.println("		}");


        } else if (testCase.getSearchType() == TestCase.SIMPLE_SEARCH_ON_NAME_OR_CODE) {
			out.println("		ResolvedConceptReference rcr = new TestCaseValidator(lbSvc).validate(");
			out.println("			scheme, version, matchText, target, algorithm);");
			out.println("		if (rcr != null) {");
			out.println("			matchedString = rcr.getEntityDescription().getContent();");
			out.println("			System.out.println(matchedString + \" (\" + rcr.getConceptCode() + \")\");");
			out.println("		}");
		} else if (testCase.getSearchType() == TestCase.SIMPLE_SEARCH_ON_PROPERTY) {
		    out.println("    	String source = null;");
		    out.println("    	boolean excludeDesignation = true;");
		    out.println("    	boolean ranking = true;");
		    out.println("    	int maxToReturn = -1;");
			out.println("		ResolvedConceptReference rcr = new TestCaseValidator(lbSvc).validatePropertySearch(");
			out.println("			scheme, version, matchText, source, algorithm, excludeDesignation, ranking, maxToReturn);");
			out.println("		if (rcr != null) {");
			out.println("			matchedString = rcr.getEntityDescription().getContent();");
			out.println("			System.out.println(matchedString + \" (\" + rcr.getConceptCode() + \")\");");
			out.println("		}");
		} else if (testCase.getSearchType() == TestCase.SIMPLE_SEARCH_ON_RELATIONSHIP) {
		    if (testCase.getRelationshipName() != null) {
				String relationshipName = testCase.getRelationshipName();
				out.println("    	String[] associationsToNavigate = new String[1];");
				out.println("    	associationsToNavigate[0] = \"" + relationshipName + "\";");
				out.println("    	String[] association_qualifier_names = null;");
				out.println("    	int search_direction = 2;");
				out.println("    	String source = null;");
				out.println("    	boolean designationOnly = false;");
				out.println("    	boolean ranking = true;");
				out.println("    	int maxToReturn = -1;");
				out.println("		ResolvedConceptReference rcr = new TestCaseValidator(lbSvc).validate(");
				out.println("			scheme, version, matchText, associationsToNavigate, association_qualifier_names, association_qualifier_values, search_direction, source, algorithm, designationOnly, ranking, maxToReturn);");
				out.println("		if (rcr != null) {");
				out.println("			matchedString = rcr.getEntityDescription().getContent();");
			    out.println("           System.out.println(matchedString + \" (\" + rcr.getConceptCode() + \")\");");
				out.println("		}");
			}

        } else if (testCase.getSearchType() == TestCase.MAPPING_SEARCH_ON_NAME_OR_CODE ||
                   testCase.getSearchType() == TestCase.MAPPING_SEARCH_ON_PROPERTY ||
                   testCase.getSearchType() == TestCase.MAPPING_SEARCH_ON_RELATIONSHIP ||
                   testCase.getSearchType() == TestCase.ALT_MAPPING_SEARCH_ON_NAME_OR_CODE ||
                   testCase.getSearchType() == TestCase.ALT_MAPPING_SEARCH_ON_PROPERTY ||
                   testCase.getSearchType() == TestCase.ALT_MAPPING_SEARCH_ON_RELATIONSHIP
                   ) {
			out.println("    	int maxToReturn = -1;");
			out.println("		ResolvedConceptReference rcr = new TestCaseValidator(lbSvc).validateMappingSearch(");
			out.println("			scheme, version, matchText, target, algorithm, maxToReturn);");
			out.println("		if (rcr != null) {");
			out.println("			matchedString = rcr.getEntityDescription().getContent();");
			out.println("			System.out.println(matchedString + \" (\" + rcr.getConceptCode() + \")\");");
			out.println("		}");


        } else if (testCase.getSearchType() == TestCase.VALUE_SET_SEARCH_ON_NAME ||
                   testCase.getSearchType() == TestCase.VALUE_SET_SEARCH_ON_CODE) {
			out.println("    	int maxToReturn = -1;");
			out.println("		ResolvedConceptReference rcr = new TestCaseValidator(lbSvc).validateValueSetSearch(");
			out.println("			scheme, version, matchText, target, algorithm, maxToReturn);");
			out.println("		if (rcr != null) {");
			out.println("			matchedString = rcr.getEntityDescription().getContent();");
			out.println("			System.out.println(matchedString + \" (\" + rcr.getConceptCode() + \")\");");
			out.println("		}");
        } else if (testCase.getSearchType() == TestCase.MULTIPLE_SEARCH_ON_NAME ||
				   testCase.getSearchType() == TestCase.MULTIPLE_SEARCH_ON_CODE ||
				   testCase.getSearchType() == TestCase.MULTIPLE_SEARCH_ON_PROPERTY ||
                   testCase.getSearchType() == TestCase.MULTIPLE_SEARCH_ON_RELATIONSHIP) {
			out.println("    	int maxToReturn = -1;");
			out.println("    	Vector schemes = new Vector();");
			out.println("    	schemes.add(scheme);");
			out.println("    	Vector versions = new Vector();");
			out.println("    	versions.add(version);");

			out.println("    	int checkbox_index = " + testCase.getInt_obj().intValue() + ";");
			out.println("		ResolvedConceptReference rcr = new TestCaseValidator(lbSvc).validateMultipleSearch(");
			out.println("			schemes, versions, matchText, target, algorithm, maxToReturn);");
			out.println("		if (rcr != null) {");
			out.println("			matchedString = rcr.getEntityDescription().getContent();");
			out.println("			System.out.println(matchedString + \" (\" + rcr.getConceptCode() + \")\");");
			out.println("		}");


        } else if (testCase.getSearchType() == TestCase.ALL_TERMINOLOGY_SEARCH_ON_CODE ||
				   testCase.getSearchType() == TestCase.ALL_TERMINOLOGY_SEARCH_ON_NAME ||
				   testCase.getSearchType() == TestCase.ALL_TERMINOLOGY_SEARCH_ON_PROPERTY ||
                   testCase.getSearchType() == TestCase.ALL_TERMINOLOGY_SEARCH_ON_RELATIONSHIP) {

			out.println("    	int maxToReturn = -1;");
			out.println("    	Vector schemes = new Vector();");
			out.println("    	schemes.add(scheme);");
			out.println("    	Vector versions = new Vector();");
			out.println("    	versions.add(version);");
			String scheme_names = testCase.getScheme();
			String scheme_versions = testCase.getVersion();

			Vector v1 = StringUtils.parseData(scheme_names);
			Vector v2 = StringUtils.parseData(scheme_versions);
			for (int k1=0; k1<v1.size(); k1++) {
				String t1 = (String) v1.elementAt(k1);
				String t2 = (String) v2.elementAt(k1);
				out.println("    	schemes.add(" + "\"" + t1 + "\"" + ");");
				out.println("    	versions.add(" + "\"" + t2 + "\"" + ");");
			}
			out.println("		ResolvedConceptReference rcr = new TestCaseValidator(lbSvc).validateMultipleSearch(");
			out.println("			schemes, versions, matchText, target, algorithm, maxToReturn);");
			out.println("		if (rcr != null) {");
			out.println("			matchedString = rcr.getEntityDescription().getContent();");
			out.println("			System.out.println(matchedString + \" (\" + rcr.getConceptCode() + \")\");");
			out.println("		}");

        } else if (testCase.getSearchType() == TestCase.ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_CODE ||
				   testCase.getSearchType() == TestCase.ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_NAME ||
				   testCase.getSearchType() == TestCase.ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_PROPERTY ||
                   testCase.getSearchType() == TestCase.ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_RELATIONSHIP) {

			out.println("    	int maxToReturn = -1;");
			out.println("    	Vector schemes = new Vector();");
			out.println("    	schemes.add(scheme);");
			out.println("    	Vector versions = new Vector();");
			out.println("    	versions.add(version);");
			String scheme_names = testCase.getScheme();
			String scheme_versions = testCase.getVersion();

			Vector v1 = StringUtils.parseData(scheme_names);
			Vector v2 = StringUtils.parseData(scheme_versions);
			for (int k1=0; k1<v1.size(); k1++) {
				String t1 = (String) v1.elementAt(k1);
				String t2 = (String) v2.elementAt(k1);
				if (t1.compareTo("NCI Metathesaurus") != 0) {
					out.println("    	schemes.add(" + "\"" + t1 + "\"" + ");");
					out.println("    	versions.add(" + "\"" + t2 + "\"" + ");");
			    }
			}
			out.println("		ResolvedConceptReference rcr = new TestCaseValidator(lbSvc).validateMultipleSearch(");
			out.println("			schemes, versions, matchText, target, algorithm, maxToReturn);");
			out.println("		if (rcr != null) {");
			out.println("			matchedString = rcr.getEntityDescription().getContent();");
			out.println("			System.out.println(matchedString + \" (\" + rcr.getConceptCode() + \")\");");
			out.println("		}");

        } else if (testCase.getSearchType() == TestCase.ADVANCED_SEARCH_ON_NAME ||
				   testCase.getSearchType() == TestCase.ADVANCED_SEARCH_ON_CODE ||
				   testCase.getSearchType() == TestCase.ADVANCED_SEARCH_ON_PROPERTY ||
                   testCase.getSearchType() == TestCase.ADVANCED_SEARCH_ON_RELATIONSHIP) {

			String source = testCase.getSource();
			String property_name = testCase.getPropertyName();
			String rel_search_association = testCase.getRelationshipName();
			String rel_search_rela = testCase.getRela();
			String direction = testCase.getDirection();

			out.println("    	int maxToReturn = -1;");
			if (source == null) {
				out.println("    	String source = null;");
			} else {
				out.println("    	String source = \"" + source + "\"");
			}
			if (property_name == null) {
				out.println("    	String property_name = null;");
			} else {
				out.println("    	String property_name = \"" + property_name + "\";");
			}
			if (rel_search_association == null) {
				out.println("    	String rel_search_association = null;");
			} else {
				out.println("    	String rel_search_association = \"" + rel_search_association + "\";");
			}
			if (rel_search_rela == null) {
				out.println("    	String rel_search_rela = null;");
			} else {
				out.println("    	String rel_search_rela = \"" + rel_search_rela + "\";");
			}
			if (direction == null) {
				out.println("    	String direction = null;");
			} else {
				out.println("    	String direction = \"" + direction + "\";");
			}

			out.println("		ResolvedConceptReference rcr = new TestCaseValidator(lbSvc).validateAdvancedSearch(");
			out.println("			scheme, version, matchText, target, algorithm, maxToReturn, source, property_name, ");
			out.println("           rel_search_association, rel_search_rela, direction );");
			out.println("		if (rcr != null) {");
			out.println("			matchedString = rcr.getEntityDescription().getContent();");
			out.println("			System.out.println(matchedString + \" (\" + rcr.getConceptCode() + \")\");");
			out.println("		}");

        } else if (testCase.getSearchType() == TestCase.ALL_VALUE_SET_SEARCH_ON_CODE ||
				   testCase.getSearchType() == TestCase.ALL_VALUE_SET_SEARCH_ON_NAME){

		}
	}

	public void openDriver() {
        out.println("		driver = new RemoteWebDriver(new URL(\"" + remoteWebDriverURL + "\"), DesiredCapabilities.chrome());");
        out.println("		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);");
        out.println("		driver.get(baseUrl + \"" + browserHomePage + "\");");
	}

	public void closeDriver() {
		out.println("		driver.close();");
	}


	public void writeSimpleSearchActions(TestCase testCase) {
		out.println("		driver.get(baseUrl);");
		wait(short_delay);
		out.println("		driver.findElement(By.xpath(\"//input[@name='algorithm'][@value='" + testCase.getAlgorithm() + "']\")).click();");
		wait(short_delay);
		out.println("		driver.findElement(By.xpath(\"//input[@name='searchTarget'][@value='" + testCase.getTarget() + "']\")).click();");
		wait(short_delay);
		out.println("		driver.findElement(By.name(\"matchText\")).clear();");
		out.println("		driver.findElement(By.name(\"matchText\")).sendKeys(matchText);");
		wait(short_delay);
		out.println("		driver.findElement(By.name(\"searchTerm:search\")).click();");
    }

	public void writeViewHierarchyActions(TestCase testCase) {
		out.println("		driver.findElement(By.linkText(linkText)).click();");
	}

	public void writeAdvancedSearchActions(TestCase testCase) {
        out.println("		driver.get(baseUrl);");
        wait(short_delay);
        out.println("		driver.findElement(By.linkText(\"Advanced Search\")).click();");
        wait(short_delay);

        out.println("		driver.findElement(By.xpath(\"//input[@name='selectSearchOption'][@value='" + testCase.getTarget() + "']\")).click();");
        wait(medium_delay);
        out.println("		driver.findElement(By.xpath(\"//input[@name='adv_search_algorithm'][@value='" + testCase.getAlgorithm() + "']\")).click();");
        wait(medium_delay);

        if (testCase.getTarget().compareTo("Property") == 0) {
			out.println("        new Select(driver.findElement(By.id(\"selectProperty\"))).selectByVisibleText(" + "\"" + testCase.getPropertyName() + "\");");
		} else if (testCase.getTarget().compareTo("Relationship") == 0) {
			out.println("        new Select(driver.findElement(By.id(\"rel_search_association\"))).selectByVisibleText(" + "\"" + testCase.getRelationshipName() + "\");");

            if (testCase.getDirection().compareTo("source") == 0) {
				      out.println("		driver.findElement(By.xpath(\"//input[@name='direction'][@value='target']\")).click();");
				      wait(medium_delay);
            } else if (testCase.getDirection().compareTo("target") == 0) {
				      out.println("		driver.findElement(By.xpath(\"//input[@name='direction'][@value='source']\")).click();");
				      wait(medium_delay);
		    }
		}

        out.println("		List<WebElement> matchTexts = driver.findElements(By.xpath(\"//input[@name='matchText']\"));");
        out.println("		((WebElement) matchTexts.get(1)).sendKeys(matchText);");
        wait(short_delay);
        out.println("		if (driver != null && driver.findElement(By.name(\"advancedSearchForm:adv_search\")) != null) {");
        out.println("			driver.findElement(By.name(\"advancedSearchForm:adv_search\")).click();");
        out.println("		}");
    }


	public void writeViewInHierarchyActions(TestCase testCase) {
		out.println("		driver.findElement(By.linkText(linkText)).click();");
		wait(long_delay);

        out.println("		bodyText = driver.findElement(By.tagName(\"body\")).getText();");
        out.println("		if (bodyText.contains(\"copyright/license statement\")) {");
        out.println("				driver.findElement(By.xpath(\"//input[@src='/ncitbrowser/images/accept.gif']\")).click();");
        out.println("		}");
        wait(short_delay);

		out.println("		driver.findElement(By.xpath(\"//input[@name='algorithm'][@value='" + testCase.getAlgorithm() + "']\")).click();");
		wait(short_delay);
		out.println("		driver.findElement(By.xpath(\"//input[@name='searchTarget'][@value='" + testCase.getTarget() + "']\")).click();");
		wait(short_delay);
		out.println("		driver.findElement(By.name(\"matchText\")).clear();");
		out.println("		driver.findElement(By.name(\"matchText\")).sendKeys(matchText);");
		wait(short_delay);
		out.println("		driver.findElement(By.name(\"searchTerm:search\")).click();");
    }


    public void writeAssertStatement(int searchType, String matchedString) {
        if (searchType == TestCase.VIEW_HIERARCHY) {
		    out.println("		try {");
		    out.println("		    popUpWindow(\"Hierarchy\");");
		    out.println("		    bodyText = driver.findElement(By.tagName(\"body\")).getText();");
		    out.println("		    assertTrue(!bodyText.contains(matchedString));");
		    out.println("		} catch (Exception ex) {");
		    out.println("			System.out.println(\"Hierarchy not available.\");");
		    out.println("		    assertTrue(true);");
		    out.println("		}");
		} else if (searchType == TestCase.VIEW_IN_HIERARCHY) {

			  out.println("		try {");
			  out.println("			driver.findElement(By.linkText(\"View in Hierarchy\")).click();");
			  wait(long_delay * 2);
			  out.println("			assertTrue(containsText(matchedString));");
			  out.println("			String windowTitle= getCurrentWindowTitle();");
			  wait(short_delay);
			  out.println("			String mainWindow = getMainWindowHandle(driver);");
			  wait(short_delay);
			  out.println("			closeAllOtherWindows(mainWindow);");
			  out.println("		} catch (Exception ex) {");
			  out.println("			System.out.println(\"Hierarchy not available.\");");
			  out.println("		    assertTrue(true);");
			  out.println("		}");
		}

		wait(short_delay);
	}

    public void writeAssertStatement() {
        wait(long_delay);
        out.println("		bodyText = driver.findElement(By.tagName(\"body\")).getText();");
        out.println("		assertTrue(bodyText.contains(matchedString) || bodyText.contains(matchText));");
        wait(short_delay);
	}

	public void goToTab(String tab) {
	    out.println("		driver.findElement(By.name(\"" + tab + "\")).click();");
	    wait(medium_delay);
	}


    public void writeTestCase(TestCase testCase) {
		out.println("	@Test //" + testCase.getBrowserLink());
        out.println("	public void " + methodName + "_" + testCase.getTestNumber() + "() throws Exception {");
        writeParameters(testCase);
        out.println("		String bodyText = null;");
        wait(short_delay);
        if (testCase.getSearchType() == TestCase.SIMPLE_SEARCH_ON_NAME_OR_CODE) {
	    	writeSimpleSearchActions(testCase);
        } else if (testCase.getSearchType() == TestCase.SIMPLE_SEARCH_ON_PROPERTY) {
	    	writeSimpleSearchActions(testCase);
        } else if (testCase.getSearchType() == TestCase.SIMPLE_SEARCH_ON_RELATIONSHIP) {
	    	writeSimpleSearchActions(testCase);



        } else if (testCase.getSearchType() == TestCase.ADVANCED_SEARCH_ON_NAME) {
	    	writeAdvancedSearchActions(testCase);
        } else if (testCase.getSearchType() == TestCase.ADVANCED_SEARCH_ON_CODE) {
	    	writeAdvancedSearchActions(testCase);
        } else if (testCase.getSearchType() == TestCase.ADVANCED_SEARCH_ON_PROPERTY) {
	    	writeAdvancedSearchActions(testCase);
        } else if (testCase.getSearchType() == TestCase.ADVANCED_SEARCH_ON_RELATIONSHIP) {
	    	writeAdvancedSearchActions(testCase);


        } else if (testCase.getSearchType() == TestCase.VIEW_HIERARCHY) {
	    	writeViewHierarchyActions(testCase);
        } else if (testCase.getSearchType() == TestCase.VIEW_IN_HIERARCHY) {
	    	writeViewInHierarchyActions(testCase);
        }
     	if (testCase.getSearchType() == TestCase.VIEW_HIERARCHY) {
			wait(short_delay);
			writeAssertStatement(TestCase.VIEW_HIERARCHY, Constants.NO_ROOT_NODES_AVAILABLE);
     	} else if (testCase.getSearchType() == TestCase.VIEW_IN_HIERARCHY) {
			wait(short_delay);
			writeAssertStatement(TestCase.VIEW_IN_HIERARCHY, Constants.NO_ROOT_NODES_AVAILABLE);
		} else {
			writeAssertStatement();
		}

		out.println("	}");
		out.println("\n");
	}

    private String getValidationMethodSignature(int searchType) {
		String validationMethod = null;
        if (searchType == TestCase.SIMPLE_SEARCH_ON_NAME_OR_CODE) {
			validationMethod = "simpleSearchUtils.search(scheme, version, matchText, target, algorithm)";
        } else if (searchType == TestCase.SIMPLE_SEARCH_ON_PROPERTY) {
			validationMethod = "searchUtils.searchByProperties(scheme, version, matchText, propertyTypes, propertyNames, null, algorithm, excludeDesignation, ranking, -1)";
		} else if (searchType == TestCase.SIMPLE_SEARCH_ON_RELATIONSHIP) {
			validationMethod
			= "searchUtils.searchByAssociations(scheme, version, matchText, associationsToNavigate, association_qualifier_names, association_qualifier_values, search_direction, source, algorithm, excludeDesignation, ranking, -1)";
		}
		return validationMethod;
	}


	public TestCase createSimpleSearchTestCase(int testNumber, String target, String browserLink, String scheme, String version, String algorithm, String matchText) {
        if (target.compareTo("codes") == 0) {
            return createTestCase(testNumber, TestCase.SIMPLE_SEARCH_ON_NAME_OR_CODE, browserLink, scheme, version, algorithm, target, matchText);
        } else if (target.compareTo("names") == 0) {
        	return createTestCase(testNumber, TestCase.SIMPLE_SEARCH_ON_NAME_OR_CODE, browserLink, scheme, version, algorithm, target, matchText);
        } else if (target.compareTo("properties") == 0) {
	        return createTestCase(testNumber, TestCase.SIMPLE_SEARCH_ON_PROPERTY, browserLink, scheme, version, algorithm, target, matchText);
        } else if (target.compareTo("relationships") == 0) {
        	return createTestCase(testNumber, TestCase.SIMPLE_SEARCH_ON_RELATIONSHIP, browserLink, scheme, version, algorithm, target, matchText);
		}
		return null;
    }


	public TestCase createAdvancedSearchTestCase(int testNumber, String target, String browserLink, String scheme, String version,
	                                             String algorithm, String matchText,
	                                             String source, String property_name,
	                                             String rel_search_association, String rel_search_rela, String direction
	                                             ) {
        if (target.compareTo("Code") == 0) {
            return createTestCase(testNumber, TestCase.ADVANCED_SEARCH_ON_CODE, browserLink, scheme, version, algorithm, target, matchText,
                   source, property_name, rel_search_association, rel_search_rela, direction);
        } else if (target.compareTo("Name") == 0) {
        	return createTestCase(testNumber, TestCase.ADVANCED_SEARCH_ON_NAME, browserLink, scheme, version, algorithm, target, matchText,
        	       source, property_name, rel_search_association, rel_search_rela, direction);
        } else if (target.compareTo("Property") == 0) {
        	return createTestCase(testNumber, TestCase.ADVANCED_SEARCH_ON_PROPERTY, browserLink, scheme, version, algorithm, target, matchText,
                   source, property_name, rel_search_association, rel_search_rela, direction);
        } else if (target.compareTo("Relationship") == 0) {
        	return createTestCase(testNumber, TestCase.ADVANCED_SEARCH_ON_RELATIONSHIP, browserLink, scheme, version, algorithm, target, matchText,
        	       source, property_name, rel_search_association, rel_search_rela, direction);
		}
		return null;
    }




	public TestCase createTestCase(int testNumber, int searchType,
	                                      String browserLink,
	                                      String scheme, String version, String algorithm, String target,
                                          String matchText) {
        return createTestCase(testNumber, searchType, browserLink, scheme, version, algorithm, target, matchText, null, null);
    }

	public TestCase createTestCase(int testNumber, int searchType,
	                                      String browserLink,
	                                      String scheme, String version, String algorithm, String target,
	                                      String matchText, String propertyName, String relationshipName) {

		String method_mame = methodName + "_" + testNumber;
		String codingSchemeName = null;
		String codingSchemeURI = null;
		String namespace = null;
		String validationMethod = getValidationMethodSignature(searchType);

		ResolvedConceptReference rcr = null;
		String matchedString = null;
		boolean assertion = false;

		return new TestCase(
			testNumber,
			searchType,
			method_mame,
			browserLink,
			scheme,
			codingSchemeName,
			codingSchemeURI,
			version,
			namespace,
			target,
			algorithm,
			validationMethod,
			matchText,
			propertyName,
			relationshipName,
			rcr,
			matchedString,
			assertion);
	}

	public TestCase createTestCase(int testNumber, Integer int_obj, int searchType,
	                                      String browserLink,
	                                      String scheme, String version, String algorithm, String target,
	                                      String matchText) {
		return createTestCase(testNumber, int_obj, searchType,
	                                      browserLink,
	                                      scheme, version, algorithm, target,
	                                      matchText, null, null);

	}

	public TestCase createTestCase(int testNumber, Integer int_obj, int searchType,
	                                      String browserLink,
	                                      String scheme, String version, String algorithm, String target,
	                                      String matchText, String propertyName, String relationshipName) {

		String method_mame = methodName + "_" + testNumber;
		String codingSchemeName = null;
		String codingSchemeURI = null;
		String namespace = null;
		String validationMethod = getValidationMethodSignature(searchType);

		ResolvedConceptReference rcr = null;
		String matchedString = null;
		boolean assertion = false;

		return new TestCase(
			testNumber,
			searchType,
			method_mame,
			int_obj,
			browserLink,
			scheme,
			codingSchemeName,
			codingSchemeURI,
			version,
			namespace,
			target,
			algorithm,
			validationMethod,
			matchText,
			propertyName,
			relationshipName,
			rcr,
			matchedString,
			assertion);
	}

	public TestCase createTestCase(int testNumber, int searchType,
	                                      String browserLink,
	                                      String scheme, String version, String algorithm, String target,
	                                      String matchText,
										  String source,
										  String property_name,
										  String rel_search_association,
										  String rel_search_rela,
										  String direction) {

		String method_mame = methodName + "_" + testNumber;
		String codingSchemeName = null;
		String codingSchemeURI = null;
		String namespace = null;
		String validationMethod = getValidationMethodSignature(searchType);

		ResolvedConceptReference rcr = null;
		String matchedString = null;
		boolean assertion = false;

		return new TestCase(
			testNumber,
			searchType,
			method_mame,
			browserLink,
			scheme,
			codingSchemeName,
			codingSchemeURI,
			version,
			namespace,
			target,
			algorithm,
			validationMethod,
			matchText,
			property_name,
			rel_search_association,
			source,
			rel_search_rela,
			direction,
			rcr,
			matchedString,
			assertion);
	}

    public static void main(String[] args) {

		int testNumber = 1;
		int searchType = TestCase.SIMPLE_SEARCH_ON_NAME_OR_CODE;
		String method_mame = "testNCImBrowserTestCase_" + testNumber;
		String browserLink = "NCI Thesaurus: National Cancer Institute Thesaurus (15.05d)";
		String scheme ="NCI_Thesaurus";
		String codingSchemeName = null;
		String codingSchemeURI = null;
		String version = "15.05d";
		String namespace = null;
		String target = "names";
		String algorithm = "contains";
		String validationMethod = "simpleSearchUtils.search(scheme, version, matchText, target, algorithm)";
		String matchText = "blood";
		String propertyName = null;
		String relationshipName = null;
		ResolvedConceptReference rcr = null;
		String matchedString = "blood";
		boolean assertion = false;


        TestCase testCase = new TestCase(
		   testNumber,
		   searchType,
		   method_mame,
		   browserLink,
		   scheme,
		   codingSchemeName,
		   codingSchemeURI,
		   version,
		   namespace,
		   target,
		   algorithm,
		   validationMethod,
		   matchText,
		   propertyName,
		   relationshipName,
		   rcr,
		   matchedString,
		   assertion);

		PrintWriter pw = null;
		String outputfile = "testcase.java";
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
			String remoteWebDriverURL = "http://localhost:9515";
			String serviceUrl = "http://lexevsapi6.nci.nih.gov/lexevsapi63";
			String baseUrl = "http://ncim.nci.nih.gov";
			String browserHomePage = "/ncimbrowser/";
			NCImTestCaseWriter writer = new NCImTestCaseWriter(pw, remoteWebDriverURL, serviceUrl, baseUrl, browserHomePage);
			writer.writeTestCase(testCase);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			pw.close();
			System.out.println("outputfile " + outputfile + " generated.");
		}

    }
}

