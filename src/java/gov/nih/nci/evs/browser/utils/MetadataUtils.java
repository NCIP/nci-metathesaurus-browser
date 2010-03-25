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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.MetadataProperty;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
import org.LexGrid.LexBIG.Utility.Constructors;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;

import gov.nih.nci.evs.browser.common.*;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;

public class MetadataUtils {

	private static final String SOURCE_ABBREVIATION = "rsab";
	private static final String SOURCE_DESCRIPTION = "son";

	private static HashMap SAB2FormalNameHashMap = null;
	private static HashMap localname2FormalnameHashMap = null;
	private static HashMap SAB2DefinitionHashMap = null;


	public static Vector getMetadataForCodingSchemes() {
		LexBIGService lbs = RemoteServerUtil.createLexBIGService();
		LexBIGServiceMetadata lbsm = null;
		MetadataPropertyList mdpl = null;
		try {
			lbsm = lbs.getServiceMetadata();
			lbsm = lbsm.restrictToProperties(new String[]{SOURCE_ABBREVIATION});
			mdpl = lbsm.resolve();
		} catch (Exception e) {
			return null;
		}

		Vector v = getMetadataCodingSchemeNames(mdpl);

		try {
			lbsm = lbs.getServiceMetadata();
			lbsm = lbsm.restrictToProperties(new String[]{SOURCE_DESCRIPTION});
			mdpl = lbsm.resolve();

		} catch (Exception e) {
			return null;
		}

		Vector v2 = getMetadataCodingSchemeNames(mdpl);
		Vector w = new Vector();
		for(int i=0; i<v.size(); i++){
			String name = (String) v.get(i);
			String value = (String) v2.get(i);
			w.add(name + "|" + value);
		}
		w = SortUtils.quickSort(w);
		return w;
	}



	private static Vector getMetadataCodingSchemeNames(MetadataPropertyList mdpl){
        Vector v = new Vector();
		Iterator<MetadataProperty> metaItr = mdpl.iterateMetadataProperty();
		while(metaItr.hasNext()){
			MetadataProperty property = metaItr.next();
            v.add(property.getValue());
		}
		return v;
	}



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static Vector getTermTypeDescriptionMetaData(String uri, String version) {
		Map<String,String> map = null;
		try {
			map = getTtyExpandedForm(uri, version);
			if (map == null) return null;
			Vector v = new Vector();
			for(String key : map.keySet()){
				String value = (String) map.get(key);
				v.add(key + "|" + value);
			}
			v = SortUtils.quickSort(v);
			return v;
	    } catch (Exception ex) {
			return null;
		}
	}

	private static Map<String,String> getTtyExpandedForm(String uri, String version) throws Exception {
		Map<String,String> ttys = new HashMap<String,String>();
		LexBIGService lbs = RemoteServerUtil.createLexBIGService();
		LexBIGServiceMetadata lbsm = null;
		MetadataPropertyList mdpl = null;
		try {
			lbsm = lbs.getServiceMetadata();
		    lbsm = lbsm.restrictToCodingScheme(Constructors.createAbsoluteCodingSchemeVersionReference(uri, version));

			mdpl = lbsm.resolve();
		} catch (Exception e) {
			return null;
		}

		for(int i=0;i<mdpl.getMetadataPropertyCount();i++){
			MetadataProperty prop = mdpl.getMetadataProperty(i);
			if(prop.getName().equals("dockey") &&
					prop.getValue().equals("TTY")){
				if(mdpl.getMetadataProperty(i + 2).getValue().equals("expanded_form")){
					ttys.put(mdpl.getMetadataProperty(i + 1).getValue(),
							mdpl.getMetadataProperty(i + 3).getValue());
				}
			}
		}
		return ttys;
	}


////////////////////////////////////////////////////////////////////////////////////////////////
// 1.2

    public static Vector getMetadataNameValuePairs(String codingSchemeName,
        String version, String urn) {
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();

		if (version == null) {
			try {
				CodingScheme cs = lbSvc.resolveCodingScheme(codingSchemeName, null);
				version = cs.getRepresentsVersion();
			} catch (Exception ex) {

			}
		}

		MetadataPropertyList mdpl = getMetadataPropertyList(lbSvc, codingSchemeName, version, urn);
		return getMetadataNameValuePairs(mdpl);

	}

	public static Vector getMetadataNameValuePairs(MetadataPropertyList mdpl, boolean sort){
		if (mdpl == null) return null;
		Vector v = new Vector();
		Iterator<MetadataProperty> metaItr = mdpl.iterateMetadataProperty();
		while(metaItr.hasNext()){
			MetadataProperty property = metaItr.next();
			String t = property.getName() + "|" + property.getValue();
            v.add(t);
		}
		if (sort)
		    return SortUtils.quickSort(v);
		return v;
	}

	public static Vector getMetadataNameValuePairs(MetadataPropertyList mdpl){
	    return getMetadataNameValuePairs(mdpl, true);
	}

	public static Vector getMetadataValues(Vector metadata, String propertyName){
		if (metadata == null) return null;
		Vector w = new Vector();
		for (int i=0; i<metadata.size(); i++) {
			String t = (String) metadata.elementAt(i);
			Vector u = DataUtils.parseData(t, "|");
			String name = (String) u.elementAt(0);
			if (name.compareTo(propertyName) == 0) {
				String value = (String) u.elementAt(1);
				w.add(value);
			}
		}
		return w;
	}

	public static Vector getMetadataValues(String codingSchemeName, String version,
	    String urn, String propertyName, boolean sort){
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		MetadataPropertyList mdpl = getMetadataPropertyList(lbSvc, codingSchemeName, version, urn);
		if (mdpl == null) return null;

		Vector metadata = getMetadataNameValuePairs(mdpl, sort);
		if (metadata == null) return null;

		return getMetadataValues(metadata, propertyName);
	}

    public static Vector getMetadataValues(String codingSchemeName, String version,
        String urn, String propertyName){
        return getMetadataValues(codingSchemeName, version,
            urn, propertyName, true);
    }

    public static String getMetadataValue(String codingSchemeName, String version,
        String urn, String propertyName) {
        Vector v = getMetadataValues(codingSchemeName, version, urn, propertyName);
        if (v == null) {
			System.out.println("getMetadataValue returns null??? " + codingSchemeName);
            return "";
		}
        int n = v.size();
        if (n <= 0)
            return "";
        if (v.size() == 1)
            return v.elementAt(0).toString();

        StringBuffer buffer = new StringBuffer();
        for (int i=0; i<n; ++i) {
            if (i > 0)
                buffer.append(" | ");
            buffer.append(v.elementAt(i).toString());
        }
        return buffer.toString();
    }

    public Vector getSupportedVocabularyMetadataValues(String propertyName) {
		String scheme = Constants.CODING_SCHEME_NAME;
		String version = null;
	    String urn = null;
        Vector v = new Vector();
	    Vector w = getMetadataValues(scheme, version, urn, propertyName);
	    if (w == null || w.size() == 0) {
		    v.add(Constants.CODING_SCHEME_NAME + "|" + propertyName + " not available");
	    } else {
		    String t = (String) w.elementAt(0);
		    v.add(Constants.CODING_SCHEME_NAME + " (version: " + version + ")" + "|" + t);
	    }
	    return v;
	}

    public static MetadataPropertyList getMetadataPropertyList(LexBIGService lbSvc, String codingSchemeName, String version, String urn) {
		LexBIGServiceConvenienceMethods lbscm = null;
		MetadataPropertyList mdpl = null;
		try {
			lbscm = (LexBIGServiceConvenienceMethods) lbSvc
					.getGenericExtension("LexBIGServiceConvenienceMethods");
			lbscm.setLexBIGService(lbSvc);

			LexBIGServiceMetadata lbsm = lbSvc.getServiceMetadata();
			lbsm = lbsm.restrictToCodingScheme(Constructors.createAbsoluteCodingSchemeVersionReference(codingSchemeName, version));
			mdpl = lbsm.resolve();

			return mdpl;
	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return mdpl;
    }

////////////////////////////////////////////////////////////////////////////////////////////////
// local name to formal name mapping

	public static Vector<String> parseData(String line) {
		String tab = "|";
		return parseData(line, tab);
	}

    public static Vector<String> parseData(String line, String tab) {
        Vector data_vec = new Vector();
        StringTokenizer st = new StringTokenizer(line, tab);
        while (st.hasMoreTokens()) {
            String value = st.nextToken();
            if (value.compareTo("null") == 0)
                value = " ";
            data_vec.add(value);
        }
        return data_vec;
    }

    // For term browse mapping use:
    public static HashMap getSAB2FormalNameHashMap() {
		if (SAB2FormalNameHashMap == null) {
			setSAB2FormalNameHashMap();
		}
		return SAB2FormalNameHashMap;
	}

	public static String getSABFormalName(String sab) {
		if (SAB2FormalNameHashMap == null) {
			getSAB2FormalNameHashMap();
		}
		return (String) SAB2FormalNameHashMap.get(sab);
	}

	public static String getSABDefinition(String sab) {
		if (SAB2DefinitionHashMap == null) {
			getSAB2FormalNameHashMap();
		}
		return (String) SAB2DefinitionHashMap.get(sab);
	}


    private static void setSAB2FormalNameHashMap() {
		System.out.println("setSAB2FormalNameHashMap ...");
		SAB2FormalNameHashMap = new HashMap();
		localname2FormalnameHashMap = new HashMap();

		boolean includeInactive = false;
        try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService(true);
			if (lbSvc == null) {
				System.out.println("WARNING: Unable to connect to instantiate LexBIGService ???");
			}
            CodingSchemeRenderingList csrl = null;
            try {
				csrl = lbSvc.getSupportedCodingSchemes();
			} catch (LBInvocationException ex) {
				ex.printStackTrace();
				System.out.println("lbSvc.getSupportedCodingSchemes() FAILED..." + ex.getCause() );
                return;
			}
			CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
			for (int i=0; i<csrs.length; i++)
			{
				int j = i+1;
				CodingSchemeRendering csr = csrs[i];
				CodingSchemeSummary css = csr.getCodingSchemeSummary();
				String formalname = css.getFormalName();

				Boolean isActive = null;
				if (csr == null) {
					System.out.println("\tcsr == null???");
				} else if (csr.getRenderingDetail() == null) {
					System.out.println("\tcsr.getRenderingDetail() == null");
				} else if (csr.getRenderingDetail().getVersionStatus() == null) {
					System.out.println("\tcsr.getRenderingDetail().getVersionStatus() == null");
				} else {

					isActive = csr.getRenderingDetail().getVersionStatus().equals(CodingSchemeVersionStatus.ACTIVE);
				}

				String representsVersion = css.getRepresentsVersion();
				//System.out.println("(" + j + ") " + formalname + "  version: " + representsVersion);
				//System.out.println("\tActive? " + isActive);

				if ((includeInactive && isActive == null) || (isActive != null && isActive.equals(Boolean.TRUE))
				     || (includeInactive && (isActive != null && isActive.equals(Boolean.FALSE))))
				{
						CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
						vt.setVersion(representsVersion);

						try {
							CodingScheme cs = lbSvc.resolveCodingScheme(formalname, vt);
							String [] localnames = cs.getLocalName();
							for (int m=0; m<localnames.length; m++) {
								String localname = localnames[m];
								//System.out.println(localname + " --(formal name) --> " + formalname);
								localname2FormalnameHashMap.put(localname, formalname);
							}
							localname2FormalnameHashMap.put(formalname, formalname);
							System.out.println("\n");
						} catch (Exception ex) {
							System.out.println("\tWARNING: Unable to resolve coding scheme " + formalname + " possibly due to missing security token.");
							System.out.println("\t\tAccess to " + formalname + " denied.");
						}
				} else {
					System.out.println("\tWARNING: setCodingSchemeMap discards " + formalname);
					System.out.println("\t\trepresentsVersion " + representsVersion);
				}
			}
	    } catch (Exception e) {
			//e.printStackTrace();
			//return null;
		}

		Vector abbr_vec = getMetadataForCodingSchemes();
		SAB2DefinitionHashMap = new HashMap();
	    for (int n=0; n<abbr_vec.size(); n++) {
		   String t = (String) abbr_vec.elementAt(n);
		   Vector w = parseData(t, "|");
		   String abbr = (String) w.elementAt(0);
		   String def = (String) w.elementAt(1);
		   if (localname2FormalnameHashMap.get(abbr) != null) {
			   String formalname = (String) localname2FormalnameHashMap.get(abbr);
			   SAB2FormalNameHashMap.put(abbr, formalname);
		   }
		   SAB2DefinitionHashMap.put(abbr, def);
	    }
		return;
	}

	public static NameAndValue createNameAndValue(String name, String value)
	{
        NameAndValue nv = new NameAndValue();
		nv.setName(name);
		nv.setContent(value);
		return nv;
	}

	public static NameAndValue[] getMetadataProperties(CodingScheme cs)
	{
        String formalName = cs.getFormalName();
        String version = cs.getRepresentsVersion();
		Vector<NameAndValue> v = new Vector<NameAndValue>();
		try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			LexBIGServiceMetadata svc = lbSvc.getServiceMetadata();
			AbsoluteCodingSchemeVersionReferenceList acsvrl = svc.listCodingSchemes();
			AbsoluteCodingSchemeVersionReference[] acdvra =	acsvrl.getAbsoluteCodingSchemeVersionReference();
			for (int i=0; i<acdvra.length; i++)
			{
				AbsoluteCodingSchemeVersionReference acsvr = acdvra[i];
				String urn = acsvr.getCodingSchemeURN();
				String ver = acsvr.getCodingSchemeVersion();
				if (urn.equals(formalName) && ver.equals(version))
				{
					//100807 KLO
					svc = svc.restrictToCodingScheme(acsvr);
					MetadataPropertyList mdpl = svc.resolve();
					MetadataProperty[] properties = mdpl.getMetadataProperty();
					for (int j=0; j<properties.length; j++)
					{
						MetadataProperty property = properties[j];
						NameAndValue nv = createNameAndValue(property.getName(), property.getValue());
						v.add(nv);
					}
				}
			}

			if (v.size() > 0)
			{
				NameAndValue[] nv_array = new NameAndValue[v.size()];
				for (int i=0; i<v.size(); i++)
				{
					NameAndValue nv = (NameAndValue) v.elementAt(i);
					nv_array[i] = nv;
			    }
			    return nv_array;
			}

	    } catch (Exception e) {
			e.printStackTrace();

		}
		return new NameAndValue[0];

	}



	/**
	 * Simple example to demonstrate extracting a specific Coding Scheme's Metadata.
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		MetadataUtils test = new MetadataUtils();
		String serviceUrl = "http://ncias-d177-v.nci.nih.gov:19480/lexevsapi51";

		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService(serviceUrl);

		if (lbSvc == null) {
			System.out.println("Unable to connect to " + serviceUrl);
			System.exit(1);
		} else {
			System.out.println("Connected to " + serviceUrl);
		}

        Vector v = test.getMetadataForCodingSchemes();
        for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			System.out.println(t);
		}

    }
}








