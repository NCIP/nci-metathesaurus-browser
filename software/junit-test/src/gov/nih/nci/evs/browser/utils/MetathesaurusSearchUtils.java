package gov.nih.nci.evs.browser.utils;

import java.io.*;
import java.util.*;
import java.text.*;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;

import gov.nih.nci.evs.security.SecurityToken;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;

import org.lexevs.tree.json.JsonConverter;
import org.lexevs.tree.json.JsonConverterFactory;
import org.lexevs.tree.model.LexEvsTree;
import org.lexevs.tree.model.LexEvsTreeNode;
import org.lexevs.tree.model.LexEvsTreeNode.ExpandableStatus;
import org.lexevs.tree.service.TreeService;
import org.lexevs.tree.service.TreeServiceFactory;

import org.lexevs.tree.dao.iterator.ChildTreeNodeIterator;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeTagList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;

import gov.nih.nci.evs.browser.utils.*;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

import org.LexGrid.concepts.Entity;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.PropertyQualifier;

/**
 * @author EVS Team
 * @version 1.0
 *
 *      Modification history Initial implementation kim.ong@ngc.com
 *
 */


/**
 * The Class MetathesaurusSearchUtils.
 */


public class MetathesaurusSearchUtils {
	LexBIGService lbSvc = null;

	public MetathesaurusSearchUtils(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
	}


    public Vector getConceptPropertyValues(Entity c, String propertyName) {
        if (c == null)
            return null;
        Vector v = new Vector();
        Property[] properties = c.getProperty();

        for (int j = 0; j < properties.length; j++) {
            Property prop = properties[j];

            if (prop.getPropertyName().compareTo(propertyName) == 0) {
                v.add(prop.getValue().getContent());
            }
        }
        return v;
    }



    // Restrictions: property name and value pair, list of sources of presentation properties
	public ResolvedConceptReferencesIterator searchByPresentationProperty(String codingSchemeURN, String codingSchemeVersion,
	                                            String propertyName, String propertyValue, String[] sourceList, String algorithm) {
		ResolvedConceptReferencesIterator iterator = null;
		try {
	        CodedNodeSet cns = getEntitiesWithProperty(codingSchemeURN, codingSchemeVersion,
	                                                   propertyName, propertyValue, sourceList, algorithm);
            SortOptionList sortOptions = null;
            LocalNameList filterOptions = null;
            LocalNameList propertyNames = null;
            CodedNodeSet.PropertyType[] propertyTypes = null;
            boolean resolveObjects = false;
			iterator = cns.resolve(sortOptions, filterOptions, propertyNames, propertyTypes, resolveObjects);

	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return iterator;
	}


	public CodedNodeSet getEntitiesWithProperty(String codingSchemeURN, String codingSchemeVersion,
	                                            String propertyName, String propertyValue, String[] sourceList, String algorithm) {
		return getEntitiesWithProperty(codingSchemeURN, codingSchemeVersion, "concept",
	                                            propertyName, propertyValue, sourceList, algorithm);

	}


	public CodedNodeSet getEntitiesWithProperty(String codingSchemeURN, String codingSchemeVersion, String type,
	                                            String propertyName, String propertyValue, String[] sourceList, String algorithm) {
		CodedNodeSet cns = null;
		try {
			cns = getEntities(codingSchemeURN, codingSchemeVersion, type);
			LocalNameList propertyNames = new LocalNameList();
			propertyNames.addEntry(propertyName);
			cns = cns.restrictToMatchingProperties(propertyNames, null, propertyValue, algorithm, null);

            if (sourceList != null && sourceList.length > 0) {
				for (int i=0; i<sourceList.length; i++) {
					String source = sourceList[i];
					LocalNameList sources = new LocalNameList();
					sources.addEntry(source);
					LocalNameList contextList = null;
					NameAndValueList qualifierList = null;
					CodedNodeSet.PropertyType[] propertyTypes = new CodedNodeSet.PropertyType[1];
					propertyTypes[0] = CodedNodeSet.PropertyType.PRESENTATION;
					cns = cns.restrictToProperties(null, propertyTypes, sources, contextList, qualifierList);
			    }
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cns;
	}



	public CodedNodeSet getEntitiesWithProperty(String codingSchemeURN, String codingSchemeVersion, String type,
	                                            String propertyName, String propertyValue, String algorithm) {
		CodedNodeSet cns = null;
		try {
			cns = getEntities(codingSchemeURN, codingSchemeVersion, type);
			LocalNameList propertyNames = new LocalNameList();
			propertyNames.addEntry(propertyName);
			cns = cns.restrictToMatchingProperties(propertyNames, null, propertyValue, algorithm, null);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cns;
	}


	public CodedNodeSet getEntitiesWithProperty(String codingSchemeURN, String codingSchemeVersion, String type, String propertyName) {
		CodedNodeSet cns = null;
		try {
			cns = getEntities(codingSchemeURN, codingSchemeVersion, type);
			LocalNameList propertyList = new LocalNameList();
			propertyList.addEntry(propertyName);
			cns = cns.restrictToProperties(propertyList, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cns;
	}

	public CodedNodeSet getEntities(String codingSchemeURN, String codingSchemeVersion, String type) {
		LocalNameList lnl = new LocalNameList();
		lnl.addEntry(type);
		CodedNodeSet cns = null;
		try {
			cns = lbSvc.getNodeSet(codingSchemeURN, Constructors.createCodingSchemeVersionOrTagFromVersion(codingSchemeVersion), lnl);
		} catch (Exception ex) {
            ex.printStackTrace();
		}
		return cns;
	}

	public CodedNodeSet getEntities(String codingSchemeURN, String codingSchemeVersion) {
		LocalNameList lnl = new LocalNameList();
		lnl.addEntry("concept");
		CodedNodeSet cns = null;
		try {
			cns = lbSvc.getNodeSet(codingSchemeURN, Constructors.createCodingSchemeVersionOrTagFromVersion(codingSchemeVersion), lnl);
		} catch (Exception ex) {
            ex.printStackTrace();
		}
		return cns;
	}

	public static void main(String [] args) {
		try {
			String serviceUrl = "http://lexevsapi62.nci.nih.gov/lexevsapi62";
			SimpleRemoteServerUtil SimpleRemoteServerUtil = new SimpleRemoteServerUtil(serviceUrl);
			LexBIGService lbSvc = SimpleRemoteServerUtil.getLexBIGService();
			MetathesaurusSearchUtils test = new MetathesaurusSearchUtils(lbSvc);

			ConceptDetails conceptDetails = new ConceptDetails(lbSvc);
			String codingSchemeURN = "NCI Metathesaurus";
			String codingSchemeVersion = null;
			String type = "concept";
			String propertyName = "GO_NAMESPACE";
			String propertyValue = "biological_process";
			String[] sourceList = new String[2];
			sourceList[0] = "NCI";
			sourceList[1] = "GO";
			String algorithm = "exactMatch";

	        CodedNodeSet cns = test.getEntitiesWithProperty(codingSchemeURN, codingSchemeVersion, type,
	                                            propertyName, propertyValue, sourceList, algorithm);

            SortOptionList sortOptions = null;
            LocalNameList filterOptions = null;
            LocalNameList propertyNames = null;
            CodedNodeSet.PropertyType[] propertyTypes = null;
            boolean resolveObjects = false;
            org.LexGrid.commonTypes.Property[] properties = null;

            ResolvedConceptReferencesIterator iterator = cns.resolve(sortOptions, filterOptions, propertyNames, propertyTypes, resolveObjects);
            try {
				int numberRemaining = iterator.numberRemaining();
				System.out.println("Count: " + numberRemaining);
				int lcv = 0;
				while (iterator.hasNext()) {
					lcv++;
					ResolvedConceptReference rcr = iterator.next();
					//System.out.println("(" + lcv + ") " + rcr.getEntityDescription().getContent() + "(" + rcr.getConceptCode() + ")");

					Entity entity = conceptDetails.getConceptByCode(codingSchemeURN, codingSchemeVersion, rcr.getConceptCode());
					properties = entity.getPresentation();
					String syn_1 = null;
					String syn_2 = null;
					String src_abbr = null;

					String code_1 = null;
					String code_2 = null;

					for (int i = 0; i < properties.length; i++) {
						Property p = (Property) properties[i];
						String t = p.getValue().getContent();
						Source[] sources = p.getSource();
						if (sources != null && sources.length > 0) {
							Source src = sources[0];
							src_abbr = src.getContent();
							if (src_abbr.compareTo(sourceList[0]) == 0) {
								//System.out.println(p.getPropertyName() + " " + p.getValue().getContent() + " " + src);
								syn_1 = p.getValue().getContent();
							} else if (src_abbr.compareTo(sourceList[1]) == 0) {
								//System.out.println(p.getPropertyName() + " " + p.getValue().getContent() + " " + src);
								syn_2 = p.getValue().getContent();
							}
						}


						PropertyQualifier[] qualifiers = p.getPropertyQualifier();
						if (qualifiers != null && qualifiers.length > 0) {
							for (int j = 0; j < qualifiers.length; j++) {
								PropertyQualifier q = qualifiers[j];
								String qualifier_name = q.getPropertyQualifierName();
								String qualifier_value = q.getValue().getContent();
								if (qualifier_name.compareTo("source-code") == 0) {
									if (src_abbr.compareTo(sourceList[0]) == 0) {
									     //System.out.println(sourceList[0] + " " + qualifier_name + ": " + qualifier_value);
									     code_1 = qualifier_value;
									} else if (src_abbr.compareTo(sourceList[1]) == 0) {
										 //System.out.println(sourceList[1] + " " + qualifier_name + ": " + qualifier_value);
										 code_2 = qualifier_value;
									}
								}
							}
						}
					}

					System.out.println(lcv + "|" + rcr.getEntityDescription().getContent() + "|" + rcr.getConceptCode() + "|" + code_1 + "|" + code_2);

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex) {
            ex.printStackTrace();
		}
	}

}