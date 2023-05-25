package gov.nih.nci.evs.testUtil.ui;


import gov.nih.nci.evs.browser.bean.*;
import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.security.SecurityToken;
import gov.nih.nci.evs.testUtil.*;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import java.io.*;
import java.util.*;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.concepts.Entity;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.*;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.naming.*;


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


public class TestUtils {
	private LexBIGService lbSvc;
	private CodingSchemeDataUtils codingSchemeDataUtils = null;
	private MetadataUtils metadataUtils = null;
    private HashMap label2CSMap = null;
    private MappingUtils mappingUtils = null;
    private MappingSearchUtils mappingSearchUtils = null;
    private TestCaseGenerator testCaseGenerator = null;
    private RandomVariateGenerator rvGenerator = null;
    private Vector csVec = null;
    private Vector mappingVec = null;
    private Vector RVSCSVec = null;
    private String serviceUrl = null;
    private Vector mappingDisplayLabels = null;

    private HashMap displayLabel2TerminologyBeanHashMap = null;
    private HashMap displayLabel2MappingBeanHashMap = null;

    public TestUtils(String serviceUrl) {
	    this.serviceUrl = serviceUrl;
        initialize();
    }

    public LexBIGService getLexBIGService() {
		return this.lbSvc;
	}

    public void initialize() {
		long ms = System.currentTimeMillis();
	    if (serviceUrl == null) {
	        serviceUrl = LexEVSServiceUtil.getServiceUrl();
		}
		try {
			lbSvc = LexEVSServiceUtil.getLexBIGService(serviceUrl);
			if (lbSvc == null) {
				System.out.println("ERROR: lbSvc == null???");
				return;
			}
	    } catch (Exception ex) {
			System.out.println("Exception LexEVSServiceUtil.getLexBIGService(serviceUrl) ???");
		}

        rvGenerator = new RandomVariateGenerator();
	    metadataUtils = new MetadataUtils(lbSvc);
	    //mappingUtils = new MappingUtils(lbSvc);
	    //mappingSearchUtils = new MappingSearchUtils(lbSvc);
	    testCaseGenerator = new TestCaseGenerator(lbSvc);
	    codingSchemeDataUtils = new CodingSchemeDataUtils(lbSvc);
	    System.out.println("\nInitialization in progress. Please wait...");
		createDisplayLabel2TerminologyAndMappingBeanHashMap();
		System.out.println("Total initialization run time (ms): " + (System.currentTimeMillis() - ms));
    }

    public void createDisplayLabel2TerminologyAndMappingBeanHashMap() {
		displayLabel2TerminologyBeanHashMap = new HashMap();
		displayLabel2MappingBeanHashMap = new HashMap();

        csVec = new Vector();
        mappingVec = new Vector();

		boolean excludeMappings = false;
		Vector v = codingSchemeDataUtils.getCodingSchemes(excludeMappings);
        int lcv = 0;
        for (int i = 0; i < v.size(); i++) {
            String t = (String) v.get(i);
            Vector u = StringUtils.parseData(t);
            String scheme = (String) u.elementAt(0);
            if (scheme != null && scheme.compareTo(gov.nih.nci.evs.browser.common.Constants.TERMINOLOGY_VALUE_SET) != 0
                && scheme.compareTo(gov.nih.nci.evs.browser.common.Constants.TERMINOLOGY_VALUE_SET_NAME) != 0
                && scheme.compareTo(gov.nih.nci.evs.browser.common.Constants.TERMINOLOGY_VALUE_SET_ALT_NAME) != 0) {
				String version = (String) u.elementAt(1);
				String urn = null;
				String displayName = metadataUtils.getMetadataValue(scheme, version, urn,
				    gov.nih.nci.evs.testUtil.Constants.DISPLAY_NAME);
				String fullName = metadataUtils.getMetadataValue(scheme, version, urn,
				    gov.nih.nci.evs.testUtil.Constants.FULL_NAME);
				String termBrowserVersion = metadataUtils.getMetadataValue(scheme, version, urn,
				    gov.nih.nci.evs.testUtil.Constants.TERM_BROWSER_VERSION);

				if (displayName.indexOf("|") != -1) {
				    displayName = displayName.substring(0, displayName.indexOf("|"));
				    System.out.println("WARNING -- double entries of metadata displayName found in " + scheme + " (version: " + version
				        + ")");
				}
				if (fullName.indexOf("|") != -1) {
				    fullName = fullName.substring(0, fullName.indexOf("|"));
				    System.out.println("WARNING -- double entries of metadata fullName found in " + scheme + " (version: " + version
				        + ")");
				}
				if (termBrowserVersion.indexOf("|") != -1) {
				    termBrowserVersion = termBrowserVersion.substring(0, termBrowserVersion.indexOf("|"));
				    System.out.println("WARNING -- double entries of metadata termBrowserVersion found in " + scheme + " (version: " + version
				        + ")");
				}
				String displayLabel = displayName + ": " + fullName + " (" + termBrowserVersion + ")";

			    TerminologyBean tb = new TerminologyBean(
					displayLabel,
					displayName,
					fullName,
					termBrowserVersion,
					scheme,
					version);

					lcv++;
					System.out.println("\n(" + lcv + ") " + tb.toString());

				String scheme_lowser = scheme.toLowerCase();
				if (scheme_lowser.indexOf("mapping") == -1) {
					csVec.add(t);
					displayLabel2TerminologyBeanHashMap.put(displayLabel, tb);
				} else {
					mappingVec.add(t);
					displayLabel2MappingBeanHashMap.put(displayLabel, tb);
				}
		    }
		}
	}

    public CodingSchemeDataUtils getCodingSchemeDataUtils() {
		return this.codingSchemeDataUtils;
	}

    public MappingSearchUtils getMappingSearchUtils() {
	    return mappingSearchUtils;
    }

    public HashMap getDisplayLabel2TerminologyBeanHashMap() {
		return displayLabel2TerminologyBeanHashMap;
	}

    public HashMap getDisplayLabel2MappingBeanHashMap() {
		return displayLabel2MappingBeanHashMap;
	}

    public Vector getMappingDisplayLabels() {
		if (mappingDisplayLabels != null) {
			return mappingDisplayLabels;
		}
		mappingDisplayLabels = new Vector();
		if (displayLabel2MappingBeanHashMap == null) {
			displayLabel2MappingBeanHashMap = createDisplayLabel2MappingBeanHashMap();
		}
		Iterator it = displayLabel2MappingBeanHashMap.keySet().iterator();
		while (it.hasNext()) {
			String label = (String) it.next();
			mappingDisplayLabels.add(label);
		}
		return new SortUtils().quickSort(mappingDisplayLabels);
	}

    public HashMap createDisplayLabel2TerminologyBeanHashMap() {
		HashMap map = new HashMap();
        csVec = codingSchemeDataUtils.getCodingSchemes();
        int lcv = 0;
        for (int i = 0; i < csVec.size(); i++) {
            String t = (String) csVec.get(i);
            Vector u = StringUtils.parseData(t);
            String scheme = (String) u.elementAt(0);
            if (scheme != null && scheme.compareTo(gov.nih.nci.evs.browser.common.Constants.TERMINOLOGY_VALUE_SET) != 0
                && scheme.compareTo(gov.nih.nci.evs.browser.common.Constants.TERMINOLOGY_VALUE_SET_NAME) != 0
                && scheme.compareTo(gov.nih.nci.evs.browser.common.Constants.TERMINOLOGY_VALUE_SET_ALT_NAME) != 0) {
				String version = (String) u.elementAt(1);
				String urn = null;

				String displayName = metadataUtils.getMetadataValue(scheme, version, urn,
				    gov.nih.nci.evs.testUtil.Constants.DISPLAY_NAME);
				String fullName = metadataUtils.getMetadataValue(scheme, version, urn,
				    gov.nih.nci.evs.testUtil.Constants.FULL_NAME);
				String termBrowserVersion = metadataUtils.getMetadataValue(scheme, version, urn,
				    gov.nih.nci.evs.testUtil.Constants.TERM_BROWSER_VERSION);

				if (displayName.indexOf("|") != -1) {
				    displayName = displayName.substring(0, displayName.indexOf("|"));
				    System.out.println("WARNING -- double entries of metadata displayName found in " + scheme + " (version: " + version
				        + ")");
				}
				if (fullName.indexOf("|") != -1) {
				    fullName = fullName.substring(0, fullName.indexOf("|"));
				    System.out.println("WARNING -- double entries of metadata fullName found in " + scheme + " (version: " + version
				        + ")");
				}
				if (termBrowserVersion.indexOf("|") != -1) {
				    termBrowserVersion = termBrowserVersion.substring(0, termBrowserVersion.indexOf("|"));
				    System.out.println("WARNING -- double entries of metadata termBrowserVersion found in " + scheme + " (version: " + version
				        + ")");
				}
				String displayLabel = displayName + ": " + fullName + " (" + termBrowserVersion + ")";

			    TerminologyBean tb = new TerminologyBean(
					displayLabel,
					displayName,
					fullName,
					termBrowserVersion,
					scheme,
					version);

					lcv++;
					System.out.println("\n(" + lcv + ") " + tb.toString());

			    map.put(displayLabel, tb);
		    }
		}
		return map;
    }

    public HashMap createDisplayLabel2MappingBeanHashMap() {
		HashMap map = new HashMap();
        mappingVec = codingSchemeDataUtils.getMappingCodingSchemes();
        for (int i = 0; i < mappingVec.size(); i++) {
            String t = (String) mappingVec.get(i);
            Vector u = StringUtils.parseData(t);
            String scheme = (String) u.elementAt(0);
			String version = (String) u.elementAt(1);
			String urn = null;

			String displayName = metadataUtils.getMetadataValue(scheme, version, urn,
				gov.nih.nci.evs.testUtil.Constants.DISPLAY_NAME);
			String fullName = metadataUtils.getMetadataValue(scheme, version, urn,
				gov.nih.nci.evs.testUtil.Constants.FULL_NAME);
			String termBrowserVersion = metadataUtils.getMetadataValue(scheme, version, urn,
				gov.nih.nci.evs.testUtil.Constants.TERM_BROWSER_VERSION);

			if (displayName.indexOf("|") != -1) {
				displayName = displayName.substring(0, displayName.indexOf("|"));
				System.out.println("WARNING -- double entries of metadata displayName found in " + scheme + " (version: " + version
					+ ")");
			}
			if (fullName.indexOf("|") != -1) {
				fullName = fullName.substring(0, fullName.indexOf("|"));
				System.out.println("WARNING -- double entries of metadata fullName found in " + scheme + " (version: " + version
					+ ")");
			}
			if (termBrowserVersion.indexOf("|") != -1) {
				termBrowserVersion = termBrowserVersion.substring(0, termBrowserVersion.indexOf("|"));
				System.out.println("WARNING -- double entries of metadata termBrowserVersion found in " + scheme + " (version: " + version
					+ ")");
			}

			String displayLabel = displayName + ": " + fullName + " (" + termBrowserVersion + ")";


			TerminologyBean tb = new TerminologyBean(
				displayLabel,
				displayName,
				fullName,
				termBrowserVersion,
				scheme,
				version);

				int j=i+1;
				System.out.println("\n(" + j + ") " + tb.toString());

			map.put(displayLabel, tb);
		}
		return map;
    }

    public String getCodingSchemeName(String displayLabel) {
		if (displayLabel2TerminologyBeanHashMap == null) {
			displayLabel2TerminologyBeanHashMap = createDisplayLabel2TerminologyBeanHashMap();
		}
		if (displayLabel2TerminologyBeanHashMap == null) return null;
		TerminologyBean tb = (TerminologyBean) displayLabel2TerminologyBeanHashMap.get(displayLabel);
		if (tb == null) return null;
		return tb.getCodingSchemeName();
	}

    public String getCodingSchemeVersion(String displayLabel) {
		if (displayLabel2TerminologyBeanHashMap == null) {
			displayLabel2TerminologyBeanHashMap = createDisplayLabel2TerminologyBeanHashMap();
		}
		if (displayLabel2TerminologyBeanHashMap == null) return null;
		TerminologyBean tb = (TerminologyBean) displayLabel2TerminologyBeanHashMap.get(displayLabel);
		if (tb == null) return null;
		return tb.getCodingSchemeVersion();
	}

    public void dumpDisplayLabel2TerminologyBeanHashMap() {
		if (displayLabel2TerminologyBeanHashMap == null) return;
        Iterator it = displayLabel2TerminologyBeanHashMap.keySet().iterator();
        while (it.hasNext()) {
            String displayLabel = (String) it.next();
            TerminologyBean tb = (TerminologyBean) displayLabel2TerminologyBeanHashMap.get(displayLabel);
            System.out.println(tb.toString());
		}
    }


    public Vector getMappingCodingSchemes() {
		if (mappingVec == null) {
			mappingVec = codingSchemeDataUtils.getMappingCodingSchemes();
		}
		return mappingVec;
	}


	public String getMappingCodingSchemeName(String label) {
		if (!displayLabel2MappingBeanHashMap.containsKey(label)) return null;
		TerminologyBean tb = (TerminologyBean) displayLabel2MappingBeanHashMap.get(label);
		return tb.getCodingSchemeName();
	}

	public String getMappingCodingSchemeVersion(String label) {
		if (!displayLabel2MappingBeanHashMap.containsKey(label)) return null;
		TerminologyBean tb = (TerminologyBean) displayLabel2MappingBeanHashMap.get(label);
		return tb.getCodingSchemeVersion();
	}


	public void dumpMappingCodingSchemes() {
		int sample_size = 5;
		Vector v = getMappingCodingSchemes();
        for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			int j = i+1;
			System.out.println("\n" + " (" + j + ") " + t);
			String cs_name = getMappingCodingSchemeName(t);
			String cs_version = getMappingCodingSchemeVersion(t);
			System.out.println("\t" + cs_name + " (" + cs_version + ")");

			ResolvedConceptReferenceList samples = generateTestCases(cs_name, cs_version, sample_size, TestCaseGenerator.TYPE_MAPPING);
			for (int k=0; k<samples.getResolvedConceptReferenceCount(); k++) {
				ResolvedConceptReference rcr = (ResolvedConceptReference) samples.getResolvedConceptReference(k);
				System.out.println("\t" + rcr.getEntityDescription().getContent() + " (" + rcr.getCode() + ")");
			}
		}
	}

    public ResolvedConceptReferenceList generateTestCases(String codingScheme, String version, int number, int type) {
		return testCaseGenerator.generateTestCases(codingScheme, version, number, type);
	}

    public Vector getResolvedValueSetCodingSchemes() {
		if (RVSCSVec == null) {
			RVSCSVec = codingSchemeDataUtils.getResolvedValueSetCodingSchemes();
		}
		return RVSCSVec;
	}

	public String getValueSetCodingSchemeName(String vscsStr) {
		return StringUtils.getStringComponent(vscsStr, 0);
	}

	public String getValueSetCodingSchemeVersion(String vscsStr) {
		return StringUtils.getStringComponent(vscsStr, 1);
	}

	public void dumpResolvedValueSetCodingSchemes() {
		int sample_size = 5;
		Vector v = getResolvedValueSetCodingSchemes();
		System.out.println("\nNumber of Resolved Value Set coding schemes: " + v.size());
        for (int i=0; i<5; i++) {
			String t = (String) v.elementAt(i);
			int j = i+1;
			System.out.println("\n" + " (" + j + ") " + t);

			String cs_name = getValueSetCodingSchemeName(t);
			String cs_version = getValueSetCodingSchemeVersion(t);
			System.out.println("\t" + cs_name + " (" + cs_version + ")");

			ResolvedConceptReferenceList samples = generateTestCases(cs_name, cs_version, sample_size, TestCaseGenerator.TYPE_VALUE_SET);
			for (int k=0; k<samples.getResolvedConceptReferenceCount(); k++) {
				ResolvedConceptReference rcr = (ResolvedConceptReference) samples.getResolvedConceptReference(k);
				System.out.println("\t" + rcr.getEntityDescription().getContent() + " (" + rcr.getCode() + ")");
			}
		}
	}

	public ResolvedConceptReferenceList selectRandomTestCases(ResolvedConceptReferenceList list, int number) {
		ResolvedConceptReferenceList samples = new ResolvedConceptReferenceList();
		if (list.getResolvedConceptReferenceCount() == 0) return samples;
		int max_to_return = number;
		if (max_to_return > list.getResolvedConceptReferenceCount()) {
			max_to_return = list.getResolvedConceptReferenceCount();
		}

		List selected_list = rvGenerator.selectWithNoReplacement(max_to_return, list.getResolvedConceptReferenceCount()-1);
		for (int i=0; i<selected_list.size(); i++) {
			Integer int_obj = (Integer) selected_list.get(i);
			ResolvedConceptReference rcr = list.getResolvedConceptReference(int_obj.intValue());
			samples.addResolvedConceptReference(rcr);
		}
        return samples;
	}

    public static void main(String[] args) {
		String serviceUrl = "http://lexevsapi62-stage.nci.nih.gov/lexevsapi62";
		TestUtils testUtils = new TestUtils(serviceUrl);

		long ms = System.currentTimeMillis();
		testUtils.dumpMappingCodingSchemes();
		System.out.println("TestUtils dumpMappingCodingSchemes run time (ms): " + (System.currentTimeMillis() - ms));

		ms = System.currentTimeMillis();
		testUtils.dumpResolvedValueSetCodingSchemes();
        System.out.println("TestUtils dumpResolvedValueSetCodingSchemes run time (ms): " + (System.currentTimeMillis() - ms));
	}

}
