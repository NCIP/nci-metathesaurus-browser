package gov.nih.nci.evs.browser.test;


import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.security.SecurityToken;
import gov.nih.nci.evs.testUtil.*;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import java.io.*;
import java.text.*;
import java.util.*;
import org.junit.*;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
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


public class TestTreeUtils extends ServiceTestCase {
    final static String testID = "TestTreeUtils";

	LexBIGService lbSvc = null;
	CodingSchemeDataUtils codingSchemeDataUtils = null;
    private	Vector csVec = null;
	TreeUtils treeUtils = null;
	TestCaseGenerator testCaseGenerator = null;


    //@Before
	public void setUp(){
		lbSvc = LexEVSServiceUtil.getLexBIGService();
		codingSchemeDataUtils = new CodingSchemeDataUtils(lbSvc);
		treeUtils = new TreeUtils(lbSvc);
		testCaseGenerator = new TestCaseGenerator(lbSvc);
	}


    @Override
    protected String getTestID() {
        return testID;
    }

    @Test
    public void testTreeUtils() throws Exception {
        csVec = codingSchemeDataUtils.getCodingSchemes();
        System.out.println("Number of coding schemes: " + csVec.size());
	    int m = 0;
        for (int i = 0; i < csVec.size(); i++) {
            String t = (String) csVec.get(i);
            Vector u = StringUtils.parseData(t);
            String scheme = (String) u.elementAt(0);

            if (scheme != null && scheme.compareTo(gov.nih.nci.evs.browser.common.Constants.NCI_METATHESAURUS) != 0 &&
                !Arrays.asList(gov.nih.nci.evs.browser.common.Constants.TERMINOLOGY_VALUE_SET_NAMES).contains(scheme)) {
				String version = (String) u.elementAt(1);

				System.out.println("\n" + scheme + " (" + version + ")");
			    ResolvedConceptReferenceList rcrl = testCaseGenerator.generateTestCases(scheme, version, ServiceTestCase.SAMPLE_SIZE,
			                                                                        TestCaseGenerator.TYPE_TERMINOLOGY);

                ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(0);
                System.out.println("\t" + rcr.getEntityDescription().getContent() + " (" + rcr.getCode() + ")");
				String namespace = null;
				HashMap hmap = null;
				System.out.println("\tSubconcepts: ");
				hmap = treeUtils.getSubconcepts(scheme, version, rcr.getCode(), namespace);
				if (hmap != null) {
					TreeItem item = (TreeItem) hmap.get(rcr.getCode());
                    if (item._expandable) {
						treeUtils.printTree(hmap);
					}
				} else {
					m++;
				}


				System.out.println("\tSuperconcepts: ");
				hmap = treeUtils.getSuperconcepts(scheme, version, rcr.getCode(), namespace);
				if (hmap != null) {
					treeUtils.printTree(hmap);
				} else {
					m++;
				}
			}
		}
		assertTrue(m == 0);
    }


    public static void main(String [] args) {
		try {
			TestTreeUtils test = new TestTreeUtils();
			test.setUp();
			test.testTreeUtils();

		} catch (Exception ex) {

		}
	}
}


/*
	NPO (2011-12-08)
	cobalt (2+) ion (NPO_1059)
	Subconcepts:
ERROR [main] (?:?) - TreeUtils getAssociatedConcepts throws exceptions.
	WARNING: treeUtils.getSubconcepts returns null
	Superconcepts:
ERROR [main] (?:?) - TreeUtils getAssociatedConcepts throws exceptions.
	WARNING: treeUtils.getSuperconcepts returns null
	OBI (April2015)
	//purl.org/dc/elements/1.1/ (//purl.org/dc/elements/1.1/)
	Subconcepts:
ERROR [main] (?:?) - TreeUtils getAssociatedConcepts throws exceptions.
	WARNING: treeUtils.getSubconcepts returns null
	Superconcepts:
ERROR [main] (?:?) - TreeUtils getAssociatedConcepts throws exceptions.
	WARNING: treeUtils.getSuperconcepts returns null
	PDQ (2014_08_29)
	study of socioeconomic and demographic variables (CDR0000038616)
	Subconcepts:
	Superconcepts:

*/
