package gov.nih.nci.evs.browser.utils;

import java.util.*;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.DataModel.Core.types.*;

import gov.nih.nci.evs.browser.common.*;
import org.apache.log4j.*;

import java.util.Map;
import java.util.Map.Entry;

import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Properties;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction
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

public class NCImMetadataUtils {
    private static Logger _logger = Logger.getLogger(MetadataUtils.class);
    private static final String SOURCE_ABBREVIATION = "rsab";
    private static final String SOURCE_DESCRIPTION = "son";

    private static HashMap _sab2FormalNameHashMap = null;
    private static HashMap _localname2FormalnameHashMap = null;
    private static HashMap _sab2DefinitionHashMap = null;

    private static HashMap _formalName2MetadataHashMap = null;

    private static Vector _propertyDescriptionsVec = null;

    public boolean isMetadataAvailable() {
        if (_formalName2MetadataHashMap == null)
            return false;
        return true;
    }

    public static HashMap getFormalName2MetadataHashMap() {
        if (_formalName2MetadataHashMap == null) {
            initialize();
        }
        return _formalName2MetadataHashMap;
    }


    public static Vector getMetadataForCodingSchemes() {
        return getMetadataForCodingSchemes(null);
    }

    public static Vector getMetadataForCodingSchemes(LexBIGService lbs) {
		Vector w = new Vector();
        if (lbs == null) {
        	lbs = RemoteServerUtil.createLexBIGService();
		}
        LexBIGServiceMetadata lbsm = null;
        MetadataPropertyList mdpl = null;

		String version = DataUtils.getVocabularyVersionByTag(Constants.CODING_SCHEME_NAME, "PRODUCTION");
        CodingScheme cs = getCodingScheme(Constants.CODING_SCHEME_NAME, version);

        String uri = cs.getCodingSchemeURI();
        String representsVersion = cs.getRepresentsVersion();

        try {
            lbsm = lbs.getServiceMetadata();
            lbsm = lbsm.restrictToProperties(new String[] { SOURCE_ABBREVIATION});
            lbsm = lbsm.restrictToCodingScheme(Constructors
                        .createAbsoluteCodingSchemeVersionReference(uri,representsVersion));
             mdpl = lbsm.resolve();
        } catch (Exception e) {
            return null;
        }

        Vector v = getMetadataCodingSchemeNames(mdpl);

        try {
            lbsm = lbs.getServiceMetadata();
            lbsm = lbsm.restrictToProperties(new String[] { SOURCE_DESCRIPTION });
            lbsm = lbsm.restrictToCodingScheme(Constructors
                    .createAbsoluteCodingSchemeVersionReference(uri,representsVersion));
            mdpl = lbsm.resolve();

        } catch (Exception e) {
            return null;
        }

        Vector v2 = getMetadataCodingSchemeNames(mdpl);
        if (v != null) {
			for (int i = 0; i < v.size(); i++) {
				String name = (String) v.get(i);
				String value = (String) v2.get(i);
				w.add(name + "|" + value);
			}
			w = SortUtils.quickSort(w);
	    }
        return w;
    }

    private static Vector getMetadataCodingSchemeNames(MetadataPropertyList mdpl) {
        Vector v = new Vector();
        Iterator<MetadataProperty> metaItr = (Iterator<MetadataProperty>) mdpl.iterateMetadataProperty();
        while (metaItr.hasNext()) {
            MetadataProperty property = metaItr.next();
            v.add(property.getValue());
        }
        return v;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Vector getTermTypeDescriptionMetaData(String uri,
        String version) {

        Map<String, String> map = null;
        try {
            map = getTtyExpandedForm(uri, version);
            if (map == null)
                return null;
            Vector v = new Vector();

			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry thisEntry = (Entry) it.next();
				String key = (String) thisEntry.getKey();
				String value = (String) thisEntry.getValue();
                v.add(key + "|" + value);
            }
            v = SortUtils.quickSort(v);
            return v;
        } catch (Exception ex) {
            return null;
        }
    }

    private Map<String, String> getTtyExpandedForm(String uri,
        String version) throws Exception {
        Map<String, String> ttys = new HashMap<String, String>();
        LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        LexBIGServiceMetadata lbsm = null;
        MetadataPropertyList mdpl = null;
        try {
            CodingScheme cs = getCodingScheme(uri, version);
            String urn = cs.getCodingSchemeURI();
            lbsm = lbs.getServiceMetadata();
            lbsm =
                lbsm.restrictToCodingScheme(Constructors
                    .createAbsoluteCodingSchemeVersionReference(urn, version));

            mdpl = lbsm.resolve();
        } catch (Exception e) {
        	_logger.error(e.getMessage());
            return null;
        }
        for (int i = 0; i < mdpl.getMetadataPropertyCount(); i++) {
            MetadataProperty prop = mdpl.getMetadataProperty(i);
            if (prop.getName().equals("dockey")
                && prop.getValue().equals("TTY")) {
                if (mdpl.getMetadataProperty(i + 2).getValue().equals(
                    "expanded_form")) {
                    ttys.put(mdpl.getMetadataProperty(i + 1).getValue(), mdpl
                        .getMetadataProperty(i + 3).getValue());
                }
            }
        }
        return ttys;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////
    // 1.2



    public Vector getMetadataNameValuePairs(String codingSchemeName, String version, String urn) {
		/*
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        if (version == null) {
            version = DataUtils.getVocabularyVersionByTag(codingSchemeName, "PRODUCTION");
        }

        MetadataPropertyList mdpl =
            getMetadataPropertyList(lbSvc, codingSchemeName, version, urn);
        return getMetadataNameValuePairs(mdpl);
        */
        NameAndValue[] nvList = getMetadataPropertyNameAndValueList(codingSchemeName, version, urn);
        return nameAndValue2Vector(nvList);

    }

    public Vector getMetadataNameValuePairs(MetadataPropertyList mdpl,
        boolean sort) {
        if (mdpl == null)
            return null;
        Vector v = new Vector();
        Iterator<MetadataProperty> metaItr = (Iterator<MetadataProperty>) mdpl.iterateMetadataProperty();
        while (metaItr.hasNext()) {
            MetadataProperty property = metaItr.next();
            String t = property.getName() + "|" + property.getValue();
            v.add(t);
        }
        if (sort)
            return SortUtils.quickSort(v);
        return v;
    }

    public Vector getMetadataNameValuePairs(MetadataPropertyList mdpl) {
        return getMetadataNameValuePairs(mdpl, true);
    }

    public Vector getMetadataValues(Vector metadata, String propertyName) {
        if (metadata == null)
            return null;
        Vector w = new Vector();
        for (int i = 0; i < metadata.size(); i++) {
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




    public Vector getMetadataValues(String codingSchemeName,
        String version, String urn, String propertyName, boolean sort) {
			/*

        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        MetadataPropertyList mdpl =
            getMetadataPropertyList(lbSvc, codingSchemeName, version, urn);
        if (mdpl == null)
            return null;

        Vector metadata = getMetadataNameValuePairs(mdpl, sort);
        if (metadata == null)
            return null;
            */
        //return getMetadataValues(metadata, propertyName);
        return getMetadataPropertyValues(codingSchemeName, version, urn, propertyName, sort);
    }

    public Vector getMetadataValues(String codingSchemeName,
        String version, String urn, String propertyName) {
        return getMetadataValues(codingSchemeName, version, urn, propertyName,
            true);
    }

    public String getMetadataValue(String codingSchemeName,
        String version, String urn, String propertyName) {
	    Vector v = getMetadataPropertyValues(codingSchemeName, version, urn, propertyName, true);

        // Vector v = getMetadataValues(codingSchemeName, version, urn, propertyName);
        if (v == null) {
            _logger
                .warn("getMetadataValue returns null??? " + codingSchemeName);
            // return "";
            return null;
        }

        int n = v.size();
        if (n <= 0) {
            _logger.warn("WARNING: getMetadataValue(\"" + propertyName + "\"): returns no value.");
            _logger.warn("  * Note: This metadata might not be loaded.");
            return "";
        }
        if (v.size() == 1) {
            return v.elementAt(0).toString();

        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < n; ++i) {
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
            v.add(Constants.CODING_SCHEME_NAME + "|" + propertyName
                + " not available");
        } else {
            String t = (String) w.elementAt(0);
            v.add(Constants.CODING_SCHEME_NAME + " (version: " + version + ")"
                + "|" + t);
        }
        return v;
    }


    public MetadataPropertyList getMetadataPropertyList(
        LexBIGService lbSvc, String codingSchemeName, String version, String urn) {
        LexBIGServiceConvenienceMethods lbscm = null;
        MetadataPropertyList mdpl = null;
        try {
            lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);

            LexBIGServiceMetadata lbsm = lbSvc.getServiceMetadata();
            lbsm =
                lbsm.restrictToCodingScheme(Constructors
                    .createAbsoluteCodingSchemeVersionReference(
                        codingSchemeName, version));
            mdpl = lbsm.resolve();

            return mdpl;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mdpl;
    }


    // //////////////////////////////////////////////////////////////////////////////////////////////
    // local name to formal name mapping

    public Vector<String> parseData(String line) {
        String tab = "|";
        return parseData(line, tab);
    }

    public Vector<String> parseData(String line, String tab) {
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
    public HashMap getSAB2FormalNameHashMap() {
        if (_sab2FormalNameHashMap == null) {
            initialize();
        }
        return _sab2FormalNameHashMap;
    }

    public static String getSABFormalName(String sab) {
		/*
        if (_sab2FormalNameHashMap == null) {
            initialize();
        }
        return (String) _sab2FormalNameHashMap.get(sab);
        */
        if (_sab2FormalNameHashMap == null) {
            return null;
        }
        return (String) _sab2FormalNameHashMap.get(sab);    }

    public String getSABDefinition(String sab) {
        if (_sab2DefinitionHashMap == null) {
            //initialize();
            return null;
        }
        return (String) _sab2DefinitionHashMap.get(sab);
    }

    public static String getFormalName(String localname) {
        try {
            String formalname =
                (String) _localname2FormalnameHashMap.get(localname);
            return formalname;
        } catch (Exception ex) {

        }
        return null;
    }

    public static void initialize() {

	}


	private boolean isResolvedValueSetCodingScheme(CodingScheme cs) {
		for (Property prop: cs.getProperties().getProperty()) {
			if (prop.getPropertyName().equalsIgnoreCase(LexEVSValueSetDefinitionServices.RESOLVED_AGAINST_CODING_SCHEME_VERSION)) {
				return true;
			}
		}
		return false;
	}

    {
        //if (_sab2FormalNameHashMap != null)
        //    return;

        _logger.info("initialize ...");
        _sab2FormalNameHashMap = new HashMap();
        _localname2FormalnameHashMap = new HashMap();
        boolean includeInactive = false;

        if (_formalName2MetadataHashMap == null) {
            _formalName2MetadataHashMap = new HashMap();
        }

        int vocabulary_count = 0;
        try {
            //LexBIGService lbSvc = RemoteServerUtil.createLexBIGService(true);
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            /*
            if (lbSvc == null) {
                _logger
                    .warn("Unable to connect to instantiate LexBIGService ???");
                return;
            }
            */
            CodingSchemeRenderingList csrl = null;
            try {
                csrl = lbSvc.getSupportedCodingSchemes();
            } catch (LBInvocationException ex) {
                ex.printStackTrace();
                _logger.error("lbSvc.getSupportedCodingSchemes() FAILED..."
                    + ex.getCause());
               // return;
            }

            CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
            for (int i = 0; i < csrs.length; i++) {
                int j = i + 1;
                CodingSchemeRendering csr = csrs[i];
                if (csr != null) {
					CodingSchemeSummary css = csr.getCodingSchemeSummary();
					String formalname = css.getFormalName();
					String css_local_name = css.getLocalName();
					Boolean isActive = null;
					if (csr.getRenderingDetail() == null) {
						_logger.warn("\tcsr.getRenderingDetail() == null");
					} else if (csr.getRenderingDetail().getVersionStatus() == null) {
						_logger
							.warn("\tcsr.getRenderingDetail().getVersionStatus() == null");
					} else {
						isActive =
							csr.getRenderingDetail().getVersionStatus().equals(
								CodingSchemeVersionStatus.ACTIVE);
					}

					String representsVersion = css.getRepresentsVersion();
					if ((includeInactive && isActive == null)
						|| (isActive != null && isActive.equals(Boolean.TRUE))
						|| (includeInactive && (isActive != null && isActive
							.equals(Boolean.FALSE)))) {
						CodingSchemeVersionOrTag vt =
							new CodingSchemeVersionOrTag();
						vt.setVersion(representsVersion);
						try {
							CodingScheme cs =
								//lbSvc.resolveCodingScheme(formalname, vt);
								getCodingScheme(formalname, representsVersion);

							if (cs != null && !isResolvedValueSetCodingScheme(cs)) {
								NameAndValue[] nvList =
									//MetadataUtils.getMetadataProperties(cs);
									getMetadataProperties(cs);
								if (nvList != null) {
									Vector metadataProperties = new Vector();
									for (int k = 0; k < nvList.length; k++) {
										NameAndValue nv = (NameAndValue) nvList[k];
										metadataProperties.add(nv.getName() + "|"
											+ nv.getContent());
									}
									vocabulary_count++;
									_logger.info("(" + vocabulary_count + ") "
										+ formalname);
									_formalName2MetadataHashMap.put(formalname,
										metadataProperties);
								}

								String[] localnames = cs.getLocalName();
								boolean contains_css_local_name = false;
								for (int m = 0; m < localnames.length; m++) {
									String localname = localnames[m];
									_logger.info("\tlocal name: " + localname);
									_localname2FormalnameHashMap.put(localname,
										formalname);
									if (localname.compareTo(css_local_name) == 0) {
										contains_css_local_name = true;
									}
								}
								_localname2FormalnameHashMap.put(formalname,
									formalname);
								if (!contains_css_local_name) {
									_logger.info("\tlocal name: " + css_local_name);
									_localname2FormalnameHashMap.put(css_local_name,
										formalname);
								}

								_logger.info("\trepresentsVersion: " + representsVersion);
								String version = "[Not Set]";

								//if (nvList != null) {
									for (int k = 0; k < nvList.length; k++) {
										NameAndValue nv = (NameAndValue) nvList[k];
										if (nv.getName().equals("version"))
											version = nv.getContent();
									}
								//}
								_logger.info("\tMetadata version: " + version);
						    }
						} catch (Exception ex) {
							_logger.warn("\tUnable to resolve coding scheme "
								+ formalname
								+ " possibly due to missing security token.");
							_logger
								.warn("\t\tAccess to " + formalname + " denied.");
							//ex.printStackTrace();
						}

					} else {
						_logger.warn("\tWARNING: setCodingSchemeMap discards "
							+ formalname);
						_logger.warn("\t\trepresentsVersion " + representsVersion);
					}
					//_logger.info("\n");
			    }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // return null;
        }

        Vector abbr_vec = getMetadataForCodingSchemes();
        _sab2DefinitionHashMap = new HashMap();
        if (abbr_vec != null) {
			for (int n = 0; n < abbr_vec.size(); n++) {
				String t = (String) abbr_vec.elementAt(n);
				Vector w = parseData(t, "|");
				String abbr = (String) w.elementAt(0);
				String def = (String) w.elementAt(1);
				if (_localname2FormalnameHashMap.get(abbr) != null) {
					String formalname =
						(String) _localname2FormalnameHashMap.get(abbr);
					_sab2FormalNameHashMap.put(abbr, formalname);
				}
				_sab2DefinitionHashMap.put(abbr, def);
			}
	    }
        //DataUtils.setFormalName2MetadataHashMap(_formalName2MetadataHashMap);
        //return;
    }

    public NameAndValue createNameAndValue(String name, String value) {
        NameAndValue nv = new NameAndValue();
        nv.setName(name);
        nv.setContent(value);
        return nv;
    }

    public NameAndValue[] getMetadataProperties(CodingScheme cs) {
        String cs_urn = cs.getCodingSchemeURI();
        String version = cs.getRepresentsVersion();
        Vector<NameAndValue> v = new Vector<NameAndValue>();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            LexBIGServiceMetadata svc = lbSvc.getServiceMetadata();
            AbsoluteCodingSchemeVersionReferenceList acsvrl =
                svc.listCodingSchemes();
            AbsoluteCodingSchemeVersionReference[] acdvra =
                acsvrl.getAbsoluteCodingSchemeVersionReference();
            for (int i = 0; i < acdvra.length; i++) {
                AbsoluteCodingSchemeVersionReference acsvr = acdvra[i];
                String urn = acsvr.getCodingSchemeURN();
                String ver = acsvr.getCodingSchemeVersion();
                if (urn.equals(cs_urn) && ver.equals(version)) {
                    // 100807 KLO
                    svc = svc.restrictToCodingScheme(acsvr);
                    MetadataPropertyList mdpl = svc.resolve();
                    MetadataProperty[] properties = mdpl.getMetadataProperty();
                    for (int j = 0; j < properties.length; j++) {
                        MetadataProperty property = properties[j];
                        NameAndValue nv =
                            createNameAndValue(property.getName(), property
                                .getValue());
                        v.add(nv);
                    }
                }
            }

            if (v.size() > 0) {
                NameAndValue[] nv_array = new NameAndValue[v.size()];
                for (int i = 0; i < v.size(); i++) {
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

    {
        try {
			String version = DataUtils.getVocabularyVersionByTag(Constants.CODING_SCHEME_NAME, "PRODUCTION");

            _propertyDescriptionsVec = new Vector();
            LexBIGService lbs = RemoteServerUtil.createLexBIGService();
            LexBIGServiceMetadata lbsm = lbs.getServiceMetadata();

//[#31764] NCIm Properties not displaying

            CodingScheme cs = getCodingScheme(Constants.CODING_SCHEME_NAME, version);
            String urn = cs.getCodingSchemeURI();
            lbsm = lbs.getServiceMetadata();
            lbsm =
                lbsm.restrictToCodingScheme(Constructors
                    .createAbsoluteCodingSchemeVersionReference(urn, version));

            MetadataPropertyList mdpl = lbsm.resolve();
            for (int i = 0; i < mdpl.getMetadataPropertyCount(); i++) {
                MetadataProperty prop = mdpl.getMetadataProperty(i);
                if (prop.getName().equals("dockey")
                    && prop.getValue().equals("ATN")) {
                    i++;
                    String propertyName =
                        mdpl.getMetadataProperty(i).getValue();
                    i++;
                    if (mdpl.getMetadataProperty(i).getValue().equals(
                        "expanded_form")) {
                        i++;
                        String propertyValue =
                            mdpl.getMetadataProperty(i).getValue();
                        String t = propertyName + "|" + propertyValue;
                        if (!_propertyDescriptionsVec.contains(t)) {
                            _propertyDescriptionsVec.add(t);
                        }
                    }
                }
            }
            _propertyDescriptionsVec =
                SortUtils.quickSort(_propertyDescriptionsVec);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

	}




    public Vector getPropertyDescriptions() {
        return _propertyDescriptionsVec;
    }


	private static CodingScheme getCodingScheme(String codingScheme,
			String version) {
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		if (version != null)
			versionOrTag.setVersion(version);
		CodingScheme cs = null;
		try {
			LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
			try {
				cs = lbSvc.resolveCodingScheme(codingScheme, versionOrTag);
			} catch (Exception ex2) {
				cs = null;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cs;
	}

    public String getVocabularyVersionByTag(String codingSchemeName,
        String ltag) {

        if (codingSchemeName == null)
            return null;
        String version = null;
        int knt = 0;
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingSchemeRenderingList lcsrl = lbSvc.getSupportedCodingSchemes();
            CodingSchemeRendering[] csra = lcsrl.getCodingSchemeRendering();
            for (int i = 0; i < csra.length; i++) {
                CodingSchemeRendering csr = csra[i];
                CodingSchemeSummary css = csr.getCodingSchemeSummary();
                if (css.getFormalName().compareTo(codingSchemeName) == 0
                    || css.getLocalName().compareTo(codingSchemeName) == 0
                    || css.getCodingSchemeURI().compareTo(codingSchemeName) == 0) {
					version = css.getRepresentsVersion();
                    knt++;

                    if (ltag == null)
                        return version;
                    RenderingDetail rd = csr.getRenderingDetail();
                    CodingSchemeTagList cstl = rd.getVersionTags();
                    java.lang.String[] tags = cstl.getTag();
                    // KLO, 102409
                    if (tags == null)
                        return version;

                    //if (tags != null && tags.length > 0) {
					if (tags.length > 0) {
                        for (int j = 0; j < tags.length; j++) {
                            String version_tag = (String) tags[j];

                            if (version_tag != null && version_tag.compareToIgnoreCase(ltag) == 0) {
                                return version;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        _logger.warn("Version corresponding to tag " + ltag + " is not found "
            + " in " + codingSchemeName);
        if (ltag != null && ltag.compareToIgnoreCase("PRODUCTION") == 0
            & knt == 1) {
            _logger.warn("\tUse " + version + " as default.");
            return version;
        }
        return null;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Vector nameAndValue2Vector(NameAndValue[] nvList) {
        if (nvList == null) return null;
        Vector v = new Vector();
		for (int k=0; k<nvList.length; k++) {
			NameAndValue nv = (NameAndValue) nvList[k];
			v.add(nv.getName() + "|" + nv.getContent());
		}
		return v;
	}


    public NameAndValue[] getMetadataPropertyNameAndValueList(String codingSchemeName, String version, String urn) {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        return getMetadataPropertyNameAndValueList(lbSvc, codingSchemeName, version, urn);
	}


    public NameAndValue[] getMetadataPropertyNameAndValueList(LexBIGService lbSvc, String codingSchemeName, String version, String urn) {
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
        Vector v = new Vector();
        MetadataPropertyList mdpl = null;
        NameAndValue[] nv_array = null;
        try {
            LexBIGServiceMetadata lbsm = lbSvc.getServiceMetadata();
            if (version == null) {
                version = DataUtils.getVocabularyVersionByTag(Constants.CODING_SCHEME_NAME, "PRODUCTION");
                CodingScheme cs = getCodingScheme(Constants.CODING_SCHEME_NAME, version);
                urn = cs.getCodingSchemeURI();
			}

            lbsm =
                lbsm.restrictToCodingScheme(Constructors
                    .createAbsoluteCodingSchemeVersionReference(
                        urn, version));
			try {
				mdpl = lbsm.resolve();
				Iterator<? extends MetadataProperty> metaItr = mdpl.iterateMetadataProperty();
				while (metaItr.hasNext()) {
					MetadataProperty property = (MetadataProperty) metaItr.next();
					v.add(createNameAndValue(property.getName(), property.getValue()));
				}
				if (v.size() > 0) {
					nv_array = new NameAndValue[v.size()];
					for (int i = 0; i < v.size(); i++) {
						NameAndValue nv = (NameAndValue) v.elementAt(i);
						nv_array[i] = nv;
					}
					return nv_array;
				}


			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}

			if (mdpl == null) return null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return nv_array;
	}


    public Vector getMetadataPropertyValues(String codingSchemeName, String version, String urn, String propertyName, boolean sort) {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        return getMetadataPropertyValues(lbSvc, codingSchemeName, version, urn, propertyName, sort);
	}


    public Vector getMetadataPropertyValues(LexBIGService lbSvc, String codingSchemeName, String version, String urn, String propertyName, boolean sort) {
		if (propertyName == null) return null;
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
        Vector v = new Vector();
        MetadataPropertyList mdpl = null;
        try {
            LexBIGServiceMetadata lbsm = lbSvc.getServiceMetadata();
            if (version == null) {
                version = DataUtils.getVocabularyVersionByTag(Constants.CODING_SCHEME_NAME, "PRODUCTION");
                CodingScheme cs = getCodingScheme(Constants.CODING_SCHEME_NAME, version);
                urn = cs.getCodingSchemeURI();
			}
            lbsm =
                lbsm.restrictToCodingScheme(Constructors
                    .createAbsoluteCodingSchemeVersionReference(
                        urn, version));
			try {
				mdpl = lbsm.resolve();
				Iterator<? extends MetadataProperty> metaItr = mdpl.iterateMetadataProperty();
				while (metaItr.hasNext()) {
					MetadataProperty property = (MetadataProperty) metaItr.next();
                    if (propertyName.compareTo(property.getName()) == 0) {
						v.add(property.getValue());
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
			if (mdpl == null) return null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (sort) {
			v = SortUtils.quickSort(v);
		}
		return v;
	}


    /**
     * Simple example to demonstrate extracting a specific Coding Scheme's
     * Metadata.
     *
     * @param args
     * @throws Exception
     */

}
