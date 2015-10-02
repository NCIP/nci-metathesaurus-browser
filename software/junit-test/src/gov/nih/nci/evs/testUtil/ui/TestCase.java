package gov.nih.nci.evs.testUtil.ui;


import java.io.*;
import java.net.*;
import java.util.*;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;


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


public class TestCase
{

// Variable declaration
    public static int SIMPLE_SEARCH_ON_NAME_OR_CODE = 1;
    public static int SIMPLE_SEARCH_ON_PROPERTY = 2;
    public static int SIMPLE_SEARCH_ON_RELATIONSHIP = 3;

    public static int MAPPING_SEARCH_ON_NAME_OR_CODE = 4;
    public static int MAPPING_SEARCH_ON_PROPERTY = 5;
    public static int MAPPING_SEARCH_ON_RELATIONSHIP = 6;

    public static int ALT_MAPPING_SEARCH_ON_NAME_OR_CODE = 7;
    public static int ALT_MAPPING_SEARCH_ON_PROPERTY = 8;
    public static int ALT_MAPPING_SEARCH_ON_RELATIONSHIP = 9;

    public static int VALUE_SET_SEARCH_ON_NAME = 10;
    public static int VALUE_SET_SEARCH_ON_CODE = 11;

    public static int MULTIPLE_SEARCH_ON_NAME = 12;
    public static int MULTIPLE_SEARCH_ON_CODE = 13;
    public static int MULTIPLE_SEARCH_ON_PROPERTY = 14;
    public static int MULTIPLE_SEARCH_ON_RELATIONSHIP = 15;

    public static int ADVANCED_SEARCH_ON_NAME = 16;
    public static int ADVANCED_SEARCH_ON_CODE = 17;
    public static int ADVANCED_SEARCH_ON_PROPERTY = 18;
    public static int ADVANCED_SEARCH_ON_RELATIONSHIP = 19;

    public static int ALL_TERMINOLOGY_SEARCH_ON_CODE = 20;
    public static int ALL_TERMINOLOGY_SEARCH_ON_NAME = 21;
    public static int ALL_TERMINOLOGY_SEARCH_ON_PROPERTY = 22;
    public static int ALL_TERMINOLOGY_SEARCH_ON_RELATIONSHIP = 23;

    public static int ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_CODE = 24;
    public static int ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_NAME = 25;
    public static int ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_PROPERTY = 26;
    public static int ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_RELATIONSHIP = 27;

    public static int ALL_VALUE_SET_SEARCH_ON_CODE = 28;
    public static int ALL_VALUE_SET_SEARCH_ON_NAME = 29;

    public static int VIEW_HIERARCHY = 30;
    public static int VIEW_IN_HIERARCHY = 31;


	private int testNumber;
	private int searchType;
	private String methodName;
	private Integer int_obj;
	private String browserLink;
	private String scheme;
	private String codingSchemeName;
	private String codingSchemeURI;
	private String version;
	private String namespace;
	private String target;
	private String algorithm;
	private String validationMethod;
	private String matchText;
	private String propertyName;
	private String relationshipName;

	private String source;
	private String rela;
	private String direction;

	private ResolvedConceptReference rcr;
	private String matchedString;
	private boolean assertion;

// Default constructor
	public TestCase() {
	}

// Constructor
	public TestCase(
		int testNumber,
		int searchType,
		String methodName,
		String browserLink,
		String scheme,
		String codingSchemeName,
		String codingSchemeURI,
		String version,
		String namespace,
		String target,
		String algorithm,
		String validationMethod,
		String matchText,
		String propertyName,
		String relationshipName,
		ResolvedConceptReference rcr,
		String matchedString,
		boolean assertion) {

		this.testNumber = testNumber;
		this.searchType = searchType;
		this.methodName = methodName;
		this.int_obj = null;
		this.browserLink = browserLink;
		this.scheme = scheme;
		this.codingSchemeName = codingSchemeName;
		this.codingSchemeURI = codingSchemeURI;
		this.version = version;
		this.namespace = namespace;
		this.target = target;
		this.algorithm = algorithm;
		this.validationMethod = validationMethod;
		this.matchText = matchText;
		this.propertyName = propertyName;
		this.relationshipName = relationshipName;
		this.rcr = rcr;
		this.matchedString = matchedString;
		this.assertion = assertion;
	}

	public TestCase(
		int testNumber,
		int searchType,
		String methodName,
		Integer int_obj,
		String browserLink,
		String scheme,
		String codingSchemeName,
		String codingSchemeURI,
		String version,
		String namespace,
		String target,
		String algorithm,
		String validationMethod,
		String matchText,
		String propertyName,
		String relationshipName,
		ResolvedConceptReference rcr,
		String matchedString,
		boolean assertion) {

		this.testNumber = testNumber;
		this.searchType = searchType;
		this.methodName = methodName;
		this.int_obj = int_obj;
		this.browserLink = browserLink;
		this.scheme = scheme;
		this.codingSchemeName = codingSchemeName;
		this.codingSchemeURI = codingSchemeURI;
		this.version = version;
		this.namespace = namespace;
		this.target = target;
		this.algorithm = algorithm;
		this.validationMethod = validationMethod;
		this.matchText = matchText;
		this.propertyName = propertyName;
		this.relationshipName = relationshipName;
		this.rcr = rcr;
		this.matchedString = matchedString;
		this.assertion = assertion;
	}


	public TestCase(
		int testNumber,
		int searchType,
		String methodName,
		String browserLink,
		String scheme,
		String codingSchemeName,
		String codingSchemeURI,
		String version,
		String namespace,
		String target,
		String algorithm,
		String validationMethod,
		String matchText,
		String propertyName,
		String relationshipName,
		String source,
		String rela,
		String direction,

		ResolvedConceptReference rcr,
		String matchedString,
		boolean assertion) {

		this.testNumber = testNumber;
		this.searchType = searchType;
		this.methodName = methodName;
		this.int_obj = null;
		this.browserLink = browserLink;
		this.scheme = scheme;
		this.codingSchemeName = codingSchemeName;
		this.codingSchemeURI = codingSchemeURI;
		this.version = version;
		this.namespace = namespace;
		this.target = target;
		this.algorithm = algorithm;
		this.validationMethod = validationMethod;
		this.matchText = matchText;
		this.propertyName = propertyName;
		this.relationshipName = relationshipName;

		this.source = source;
		this.rela = rela;
		this.direction = direction; // source/target

		this.rcr = rcr;
		this.matchedString = matchedString;
		this.assertion = assertion;
	}

// Set methods
	public void setTestNumber(int testNumber) {
		this.testNumber = testNumber;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}


	public void setInt_obj(Integer int_obj) {
		this.int_obj = int_obj;
	}

	public void setBrowserLink(String browserLink) {
		this.browserLink = browserLink;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public void setCodingSchemeName(String codingSchemeName) {
		this.codingSchemeName = codingSchemeName;
	}

	public void setCodingSchemeURI(String codingSchemeURI) {
		this.codingSchemeURI = codingSchemeURI;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public void setTarget(String target) {
		this.target = target;
	}


	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public void setValidationMethod(String validationMethod) {
		this.validationMethod = validationMethod;
	}

	public void setMatchText(String matchText) {
		this.matchText = matchText;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public void setRelationshipName(String relationshipName) {
		this.relationshipName = relationshipName;
	}


	public void setSource(String source) {
		this.source = source;
	}

	public void setRela(String rela) {
		this.rela = rela;
	}

	public void setDirection(String direction) {
		this.direction = direction; // source/target
	}

	public void setRcr(ResolvedConceptReference rcr) {
		this.rcr = rcr;
	}

	public void setMatchedString(String matchedString) {
		this.matchedString = matchedString;
	}

	public void setAssertion(boolean assertion) {
		this.assertion = assertion;
	}


// Get methods
	public int getTestNumber() {
		return this.testNumber;
	}

	public int getSearchType() {
		return this.searchType;
	}

	public String getMethodName() {
		return this.methodName;
	}


	public Integer getInt_obj() {
		return this.int_obj;
	}

	public String getBrowserLink() {
		return this.browserLink;
	}

	public String getScheme() {
		return this.scheme;
	}

	public String getCodingSchemeName() {
		return this.codingSchemeName;
	}

	public String getCodingSchemeURI() {
		return this.codingSchemeURI;
	}

	public String getVersion() {
		return this.version;
	}

	public String getNamespace() {
		return this.namespace;
	}

	public String getTarget() {
		return this.target;
	}


	public String getAlgorithm() {
		return this.algorithm;
	}

	public String getValidationMethod() {
		return this.validationMethod;
	}

	public String getMatchText() {
		return this.matchText;
	}

	public String getPropertyName() {
		return this.propertyName;
	}

	public String getRelationshipName() {
		return this.relationshipName;
	}


	public String getSource() {
		return this.source;
	}

	public String getRela() {
		return this.rela;
	}

	public String getDirection() {
		return this.direction;
	}

	public ResolvedConceptReference getRcr() {
		return this.rcr;
	}

	public String getMatchedString() {
		return this.matchedString;
	}

	public boolean getAssertion() {
		return this.assertion;
	}



    public static String searchType2Name(int searchType) {
	    if (searchType == 1) return "SIMPLE_SEARCH_ON_NAME_OR_CODE";
	    else if (searchType == 2) return "SIMPLE_SEARCH_ON_PROPERTY";
	    else if (searchType == 3) return "SIMPLE_SEARCH_ON_RELATIONSHIP";

	    else if (searchType == 4) return "MAPPING_SEARCH_ON_NAME_OR_CODE";
	    else if (searchType == 5) return "MAPPING_SEARCH_ON_PROPERTY";
	    else if (searchType == 6) return "MAPPING_SEARCH_ON_RELATIONSHIP";

	    else if (searchType == 7) return "ALT_MAPPING_SEARCH_ON_NAME_OR_CODE";
	    else if (searchType == 8) return "ALT_MAPPING_SEARCH_ON_PROPERTY";
	    else if (searchType == 9) return "ALT_MAPPING_SEARCH_ON_RELATIONSHIP";

	    else if (searchType == 10) return "VALUE_SET_SEARCH_ON_NAME";
	    else if (searchType == 11) return "VALUE_SET_SEARCH_ON_CODE";

	    else if (searchType == 12) return "MULTIPLE_SEARCH_ON_NAME";
	    else if (searchType == 13) return "MULTIPLE_SEARCH_ON_CODE";
	    else if (searchType == 14) return "MULTIPLE_SEARCH_ON_PROPERTY";
	    else if (searchType == 15) return "MULTIPLE_SEARCH_ON_RELATIONSHIP";

	    else if (searchType == 16) return "ADVANCED_SEARCH_ON_NAME";
	    else if (searchType == 17) return "ADVANCED_SEARCH_ON_CODE";
	    else if (searchType == 18) return "ADVANCED_SEARCH_ON_PROPERTY";
	    else if (searchType == 19) return "ADVANCED_SEARCH_ON_RELATIONSHIP";

	    else if (searchType == 20) return "ALL_TERMINOLOGY_SEARCH_ON_CODE";
	    else if (searchType == 21) return "ALL_TERMINOLOGY_SEARCH_ON_NAME";
	    else if (searchType == 22) return "ALL_TERMINOLOGY_SEARCH_ON_PROPERTY";
	    else if (searchType == 23) return "ALL_TERMINOLOGY_SEARCH_ON_RELATIONSHIP";

	    else if (searchType == 24) return "ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_CODE";
	    else if (searchType == 25) return "ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_NAME";
	    else if (searchType == 26) return "ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_PROPERTY";
	    else if (searchType == 27) return "ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_RELATIONSHIP";

	    else if (searchType == 28) return "ALL_VALUE_SET_SEARCH_ON_CODE";
	    else if (searchType == 29) return "ALL_VALUE_SET_SEARCH_ON_NAME";

	    else if (searchType == 30) return "VIEW_HIERARCHY";
	    else if (searchType == 31) return "VIEW_IN_HIERARCHY";
	    else return "Others";
	}

}
