package gov.nih.nci.evs.browser.bean;


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


public class CodeGeneratorConfiguration
{
// Variable declaration
	private String packageName;
	private String className;
	private String remoteWebDriverURL;
	private String baseUrl;
	private String serviceUrl;
	private int delay = 1;

// Default constructor
	public CodeGeneratorConfiguration() {
	}

// Constructor
	public CodeGeneratorConfiguration(
		String packageName,
		String className,
		String remoteWebDriverURL,
		String baseUrl,
		String serviceUrl) {

		this.packageName = packageName;
		this.className = className;
		this.remoteWebDriverURL = remoteWebDriverURL;
		this.baseUrl = baseUrl;
		this.serviceUrl = serviceUrl;
	}

	public CodeGeneratorConfiguration(
		String packageName,
		String className,
		String remoteWebDriverURL,
		String baseUrl,
		String serviceUrl,
		int delay) {

		this.packageName = packageName;
		this.className = className;
		this.remoteWebDriverURL = remoteWebDriverURL;
		this.baseUrl = baseUrl;
		this.serviceUrl = serviceUrl;
		this.delay = delay;
	}

// Set methods
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setClassName(String className) {
		this.className = className;
	}


	public void setRemoteWebDriverURL(String remoteWebDriverURL) {
		this.remoteWebDriverURL = remoteWebDriverURL;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}


	public void setDelay(int delay) {
		this.delay = delay;
	}

// Get methods
	public String getPackageName() {
		return this.packageName;
	}

	public String getClassName() {
		return this.className;
	}


	public String getRemoteWebDriverURL() {
		return this.remoteWebDriverURL;
	}

	public String getBaseUrl() {
		return this.baseUrl;
	}

	public String getServiceUrl() {
		return this.serviceUrl;
	}


	public int getDelay() {
		return this.delay;
	}
}
