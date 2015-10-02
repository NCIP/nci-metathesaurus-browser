package gov.nih.nci.evs.testUtil;


import gov.nih.nci.evs.browser.bean.*;
import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.security.SecurityToken;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import java.io.*;
import java.text.*;
import java.util.*;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.*;
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


public class TestCaseGenerator {
	private LexBIGService lbSvc = null;
	private CodingSchemeDataUtils codingSchemeDataUtils = null;
	private RandomVariateGenerator rvGenerator = null;
	private MappingUtils mappingUtils = null;

    public static final int TYPE_TERMINOLOGY = 1;
    public static final int TYPE_MAPPING = 2;
    public static final int TYPE_VALUE_SET = 3;

	public TestCaseGenerator(LexBIGService lbSvc) {
		long ms = System.currentTimeMillis();
		this.lbSvc= lbSvc;
		this.codingSchemeDataUtils = new CodingSchemeDataUtils(lbSvc);
		this.rvGenerator = new RandomVariateGenerator();
		this.mappingUtils = new MappingUtils(lbSvc);
	}

	public void printTestCases(ResolvedConceptReferenceList rcrl) {
		if (rcrl == null) return;
		for (int i=0; i<rcrl.getResolvedConceptReferenceCount(); i++) {
			int j = i+1;
			ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(i);
			StringBuffer buf = new StringBuffer();
			buf.append("(" + j + ") " + rcr.getEntityDescription().getContent() + " (" + rcr.getCode() + ")");
			buf.append("\n\tcoding scheme: " + rcr.getCodingSchemeName());
			buf.append("\n\tversion: " + rcr.getCodingSchemeVersion());
			if (rcr.getCodeNamespace() != null && rcr.getCodeNamespace().length() > 0) {
				buf.append("\n\tnamespace: " + rcr.getCodeNamespace());
			}
			buf.append("\n");
			System.out.println(buf.toString());
		}
	}

    public ResolvedConceptReferenceList generateResolvedConceptReferences(String codingScheme, String version, int number) {
		if (version == null) {
			version = codingSchemeDataUtils.getVocabularyVersionByTag(codingScheme, gov.nih.nci.evs.browser.common.Constants.PRODUCTION);
		}

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null) {
            csvt.setVersion(version);
		}

		ResolvedConceptReferenceList rcrl = new ResolvedConceptReferenceList();
		try {
			LocalNameList entityTypes = new LocalNameList();
			entityTypes.addEntry("concept");
			CodedNodeSet cns = lbSvc.getNodeSet(codingScheme, csvt, entityTypes);

			SortOptionList sortOptions = null;
			LocalNameList filterOptions = null;
			LocalNameList propertyNames = null;
			CodedNodeSet.PropertyType[] propertyTypes = null;
			boolean resolveObjects = false;
			int maxToReturn = number;
            ResolvedConceptReferenceList rvrlist = cns.resolveToList(sortOptions, filterOptions, propertyNames, propertyTypes, resolveObjects, maxToReturn);

            for (int i=0; i<rvrlist.getResolvedConceptReferenceCount(); i++) {
				ResolvedConceptReference rcr = rvrlist.getResolvedConceptReference(i);
				rcrl.addResolvedConceptReference(rcr);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rcrl;
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ResolvedConceptReferenceList generateValueSetTestCases(String codingScheme, String version, int number) {
		if (version == null) {
			version = codingSchemeDataUtils.getVocabularyVersionByTag(codingScheme, gov.nih.nci.evs.browser.common.Constants.PRODUCTION);
		}
		return generateResolvedConceptReferences(codingScheme, version, number);
	}


    public ResolvedConceptReferenceList generateTerminologyTestCases(String codingScheme, String version, int number) {
		if (version == null) {
			version = codingSchemeDataUtils.getVocabularyVersionByTag(codingScheme, gov.nih.nci.evs.browser.common.Constants.PRODUCTION);
		}
		return generateResolvedConceptReferences(codingScheme, version, number);
	}


    public ResolvedConceptReferenceList generateMappingTestCases(String codingScheme, String version, int number) {
		if (version == null) {
			version = codingSchemeDataUtils.getVocabularyVersionByTag(codingScheme, gov.nih.nci.evs.browser.common.Constants.PRODUCTION);
		}

		ResolvedConceptReferenceList rcrl = new ResolvedConceptReferenceList();
		ResolvedConceptReferencesIterator rcri = mappingUtils.getMappingDataIterator(codingScheme, version);
		int knt = 0;
        try {
			while (rcri.hasNext()) {
				ResolvedConceptReference rcr = (ResolvedConceptReference) rcri.next();
				rcrl.addResolvedConceptReference(rcr);
				knt++;
				if (knt == number) break;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rcrl;
	}

    public ResolvedConceptReferenceList generateTestCases(String codingScheme, String version, int number, int type) {
		switch (type) {
		    case TYPE_TERMINOLOGY:
				return generateTerminologyTestCases(codingScheme, version, number);
		    case TYPE_MAPPING:
				return generateMappingTestCases(codingScheme, version, number);
		    case TYPE_VALUE_SET:
		        return generateValueSetTestCases(codingScheme, version, number);
		    default:
				return generateTerminologyTestCases(codingScheme, version, number);
        }
	}


    public String patch(String t) {
	   t = t.replaceAll(":", " ");
	   return t;
    }

    public ResolvedConceptReference generateNCIMResolvedConceptReference() {
	   String version = null;
	   ResolvedConceptReferenceList rcrl = generateTestCases(gov.nih.nci.evs.testUtil.Constants.NCIM_CS_NAME,
	       version, ServiceTestCase.SAMPLE_SIZE, TestCaseGenerator.TYPE_TERMINOLOGY);
	   int n = rvGenerator.uniform(0, rcrl.getResolvedConceptReferenceCount()-1);
	   ResolvedConceptReference rcr = (ResolvedConceptReference) rcrl.getResolvedConceptReference(n);

       ResolvedConceptReferencesIterator iterator = null;
	   String NCIm_PROD_Version = codingSchemeDataUtils.getVocabularyVersionByTag(gov.nih.nci.evs.browser.common.Constants.NCI_METATHESAURUS, "PRODUCTION");

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


}
