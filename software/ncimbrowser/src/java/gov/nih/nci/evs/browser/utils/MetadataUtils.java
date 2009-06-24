package gov.nih.nci.evs.browser.utils;

/**
  * <!-- LICENSE_TEXT_START -->
* Copyright 2008,2009 NGIT. This software was developed in conjunction with the National Cancer Institute,
* and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
* in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
* materials provided with the distribution.
* 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
* "This product includes software developed by NGIT and the National Cancer Institute."
* If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
* wherever such third-party acknowledgments normally appear.
* 3. The names "The National Cancer Institute", "NCI" and "NGIT" must not be used to endorse or promote products derived from this software.
* 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
* the recipient to use any trademarks owned by either NCI or NGIT
* 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
* NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  * <!-- LICENSE_TEXT_END -->
  */

/**
  * @author EVS Team
  * @version 1.0
  *
  * Modification history
  *     Initial implementation kim.ong@ngc.com
  *
 */

import gov.nih.nci.evs.browser.common.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.MetadataProperty;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.concepts.Concept;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

import org.LexGrid.LexBIG.Utility.Constructors;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;

//v5.0
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDataService;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSService;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;

public class MetadataUtils {

	private final String codingSchemeNameProperty = "codingScheme";

	private Vector getMetadataCodingSchemeNames(MetadataPropertyList mdpl){
		//List<MetadataProperty> metaDataProperties = new ArrayList<MetadataProperty>();
        Vector v = new Vector();
		Iterator<MetadataProperty> metaItr = mdpl.iterateMetadataProperty();
		while(metaItr.hasNext()){
			MetadataProperty property = metaItr.next();
			if (property.getName().equals(codingSchemeNameProperty)) {
	            v.add(property.getValue());
			}
		}
		return v;
	}


	/**
	 * Gets all of the Metadata Properties from a given Coding Scheme.
	 *
	 * @param mdpl The whole set of Metadata Properties
	 * @param codingScheme The Coding Scheme to restrict to
	 * @return All of the Metadata Properties associated with the given Coding Scheme.
	 */
	private List<MetadataProperty> getMetadataForCodingScheme(MetadataPropertyList mdpl, String codingScheme){
		List<MetadataProperty> metaDataProperties = new ArrayList<MetadataProperty>();

		Iterator<MetadataProperty> metaItr = mdpl.iterateMetadataProperty();
		while(metaItr.hasNext()){
			MetadataProperty property = metaItr.next();
			if (property.getName().equals(codingSchemeNameProperty) && property.getValue().equals(codingScheme)) {
				metaDataProperties.add(property);
				while(metaItr.hasNext()){
					property = metaItr.next();
					if(!property.getName().equals(codingSchemeNameProperty)){
						metaDataProperties.add(property);
					} else {
						return metaDataProperties;
					}
				}
			}
		}
		//if it hasn't found anything, throw an exception.
		throw new RuntimeException("Error retrieving Metadata from Coding Scheme: " + codingScheme);
	}


    public MetadataPropertyList getMetadataPropertyList(LexBIGService lbSvc, String codingSchemeName, String version, String urn) {

		System.out.println("\nCoding Scheme " + codingSchemeName);
		System.out.println("\turn: " + urn);
		System.out.println("\tversion: " + version);

		//Vector serviceMetadata_vec = new Vector();
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();

        LexBIGServiceMetadata smd = null;
        MetadataPropertyList mdpl = null;
        try {
			smd = lbSvc.getServiceMetadata();
			acsvr.setCodingSchemeURN(urn);
			acsvr.setCodingSchemeVersion(version);
			smd = smd.restrictToCodingScheme(acsvr);

			try {
				mdpl = smd.resolve();
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}

			if (mdpl == null || mdpl.getMetadataPropertyCount() == 0) {
				acsvr = new AbsoluteCodingSchemeVersionReference();
				acsvr.setCodingSchemeURN(codingSchemeName);
				acsvr.setCodingSchemeVersion(version);
				smd = lbSvc.getServiceMetadata();
				smd = smd.restrictToCodingScheme(acsvr);

				try {
					mdpl = smd.resolve();
				} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}
			}

			if (mdpl == null) return null;
			System.out.println("\tmdpl.getMetadataPropertyCount() = " + mdpl.getMetadataPropertyCount());
			//if (mdpl.getMetadataPropertyCount() == 0) return serviceMetadata_vec;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return mdpl;
	}

 	public Vector<String> getAvailableCodingSchemeVersions(LexBIGService lbSvc, String codingSchemeName) //CodingScheme cs)
	{
		Vector<String> v = new Vector<String>();
		try {
			CodingSchemeRenderingList lcsrl = lbSvc.getSupportedCodingSchemes();
			CodingSchemeRendering[] csrs = lcsrl.getCodingSchemeRendering();
			for (int i=0; i<csrs.length; i++)
			{
				CodingSchemeRendering csr = (CodingSchemeRendering) csrs[i];
				CodingSchemeSummary css = csr.getCodingSchemeSummary();
				String formalName = css.getFormalName();
				String localName = css.getLocalName();
				if (formalName.compareTo(codingSchemeName) == 0 || localName.compareTo(codingSchemeName) == 0)
				{
					v.add(css.getRepresentsVersion());
				}
			}
	    } catch (Exception e) {
			return new Vector<String>();
		}
		return v;
	}

    public Vector getMetadataForCodingSchemes(String propertyName) {
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		String codingSchemeName = Constants.CODING_SCHEME_NAME;

		String version = null;
		Vector versions = getAvailableCodingSchemeVersions(lbSvc, codingSchemeName);
		version = (String) versions.elementAt(0);
		MetadataPropertyList mdpl = getMetadataPropertyList(lbSvc, codingSchemeName, version, null);
		return getMetadataForCodingSchemes(mdpl, propertyName);
	}


    public Vector getMetadataForCodingSchemes(MetadataPropertyList mdpl, String propertyName) {
		Vector v = new Vector();
		Vector codingSchemeNames = getMetadataCodingSchemeNames(mdpl);
		int knt = 0;
		for (int k=0; k<codingSchemeNames.size(); k++) {
			String codingSchemeName = (String) codingSchemeNames.elementAt(k);
			String propertyValue = getEntityDescriptionForCodingScheme(mdpl, codingSchemeName, propertyName);
			v.add(codingSchemeName + "|" + propertyValue);
	    }
	    v = SortUtils.quickSort(v);
		return v;
	}

    public String getEntityDescriptionForCodingScheme(MetadataPropertyList mdpl, String codingSchemeName, String propertyName) {
		try {
			System.out.println("(*) codingSchemeName: " + codingSchemeName);
			List<MetadataProperty> properties = getMetadataForCodingScheme(mdpl, codingSchemeName);
			if (properties == null) return null;

			//Print out all of the Metadata Properties of the Coding Scheme.
			//The propery names ('prop.getName()') are going to be things like 'formalName',
			//'codingSchemeURI', 'representsVersion', etc... so you can pick out which ones
			//you'd like to use.
			for(MetadataProperty prop : properties){
				System.out.println("\tProperty Name: " + prop.getName() + "\n\tProperty Value: " + prop.getValue());
				if (prop.getName().compareTo(propertyName) == 0) {
					return prop.getValue();
				}
				//serviceMetadata_vec.add(prop);
			}
			//return serviceMetadata_vec;
		} catch (Exception ex) {
			System.out.println("getEntityDescriptionForCodingScheme throws exception???? " );
			//ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Simple example to demonstrate extracting a specific Coding Scheme's Metadata.
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		MetadataUtils test = new MetadataUtils();
		String serviceUrl = "http://ncias-d177-v.nci.nih.gov:19280/lexevsapi50";
		//serviceUrl = "http://cbvapp-d1007.nci.nih.gov:19080/lexevsapi50";
		//serviceUrl = "http://lexevsapi-qa.nci.nih.gov/lexevsapi50";
		//serviceUrl = "http://lexevsapi-dev.nci.nih.gov/lexevsapi50";

		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService(serviceUrl);
		//LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();

		if (lbSvc == null) {
			System.out.println("Unable to connect to " + serviceUrl);
			System.exit(1);
		} else {
			System.out.println("Connected to " + serviceUrl);
		}

		String codingSchemeName = "NCI MetaThesaurus";
		String urn = "urn:oid:2.16.840.1.113883.3.26.1.2";//urn:oid:2.16.840.1.113883.3.26.1.2
		String version = "200812";
		urn = null;

		MetadataPropertyList mdpl = test.getMetadataPropertyList(lbSvc, codingSchemeName, version, urn);
        String propertyName = "entityDescription";
        test.getMetadataForCodingSchemes(mdpl, propertyName);

        propertyName = "formalName";
        Vector w = test.getMetadataForCodingSchemes(mdpl, propertyName);
    }
}











