package gov.nih.nci.evs.testUtil.ui;


import java.io.*;
import java.net.*;
import java.util.*;


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


public class TestCaseData
{

// Variable declaration
	private int searchType;
	private String scheme;
	private String version;
	private String codingSchemeName;
	private String codingSchemeURI;
	private String namespace;
	private String target;
	private String algorithm;
	private String matchText;
	private String propertyName;
	private String relationshipName;
	private String source;
	private String rela;
	private String direction;

// Default constructor
	public TestCaseData() {
	}

// Constructor
	public TestCaseData(
		int searchType,
		String scheme,
		String version,
		String codingSchemeName,
		String codingSchemeURI,
		String namespace,
		String target,
		String algorithm,
		String matchText,
		String propertyName,
		String relationshipName,
		String source,
		String rela,
		String direction) {

		this.searchType = searchType;
		this.scheme = scheme;
		this.version = version;
		this.codingSchemeName = codingSchemeName;
		this.codingSchemeURI = codingSchemeURI;
		this.namespace = namespace;
		this.target = target;
		this.algorithm = algorithm;
		this.matchText = matchText;
		this.propertyName = propertyName;
		this.relationshipName = relationshipName;
		this.source = source;
		this.rela = rela;
		this.direction = direction;
	}

// Set methods
	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setCodingSchemeName(String codingSchemeName) {
		this.codingSchemeName = codingSchemeName;
	}

	public void setCodingSchemeURI(String codingSchemeURI) {
		this.codingSchemeURI = codingSchemeURI;
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
		this.direction = direction;
	}


// Get methods
	public int getSearchType() {
		return this.searchType;
	}

	public String getScheme() {
		return this.scheme;
	}

	public String getVersion() {
		return this.version;
	}

	public String getCodingSchemeName() {
		return this.codingSchemeName;
	}

	public String getCodingSchemeURI() {
		return this.codingSchemeURI;
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


	public TestCase createTestCase(
		int testNumber,
		String methodName,
		Integer int_obj,
		String browserLink) {

        String validationMethod = null;
		String matchedString = null;
		boolean assertion = true;

        return new TestCase(
			testNumber,
			searchType,
			methodName,
			browserLink,
			this.scheme,
			this.codingSchemeName,
			this.codingSchemeURI,
			this.version,
			this.namespace,
			this.target,
			this.algorithm,
			validationMethod,
			this.matchText,
			this.propertyName,
			this.relationshipName,
			this.source,
			this.rela,
			this.direction,
			null,
			matchedString,
			assertion);
	}
}
