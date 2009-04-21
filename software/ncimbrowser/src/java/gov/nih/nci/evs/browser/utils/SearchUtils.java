package gov.nih.nci.evs.browser.utils;

import java.io.*;
import java.util.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.HashSet;
import java.util.Arrays;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.LexGrid.concepts.Concept;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;


import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Presentation;

import org.apache.log4j.Logger;

import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;

import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.commonTypes.EntityDescription;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Concept;

import org.LexGrid.relations.Relations;

import org.LexGrid.commonTypes.PropertyQualifier;

import org.LexGrid.commonTypes.Source;
import org.LexGrid.naming.SupportedSource;

import org.LexGrid.naming.SupportedPropertyQualifier;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;

import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedAssociationQualifier;

import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.SupportedPropertyQualifier;
import org.LexGrid.naming.SupportedRepresentationalForm;
import org.LexGrid.naming.SupportedSource;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedHierarchy;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeTagList;

//import gov.nih.nci.evs.reportwriter.properties.NCImBrowserProperties;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;

import gov.nih.nci.evs.browser.properties.NCImBrowserProperties;


import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.LexGrid.concepts.Entity;

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

public class SearchUtils {
    //int maxReturn = 5000;
    private boolean apply_sort_score = false;
    Connection con;
    Statement stmt;
    ResultSet rs;

    private List supportedStandardReportList = new ArrayList();

    private static List standardReportTemplateList = null;
    private static List adminTaskList = null;
    private static List userTaskList = null;

    private static List propertyTypeList = null;

    private static List _ontologies = null;

    private static org.LexGrid.LexBIG.LexBIGService.LexBIGService lbSvc = null;
    public org.LexGrid.LexBIG.Utility.ConvenienceMethods lbConvMethods = null;
    public CodingSchemeRenderingList csrl = null;
    private Vector supportedCodingSchemes = null;
    private static HashMap codingSchemeMap = null;
    private Vector codingSchemes = null;

    private static HashMap csnv2codingSchemeNameMap = null;
    private static HashMap csnv2VersionMap = null;

    private static List directionList = null;

    private static String url = null;

    //==================================================================================
    // For customized query use

    public static int ALL = 0;
    public static int PREFERRED_ONLY = 1;
    public static int NON_PREFERRED_ONLY = 2;

    static int RESOLVE_SOURCE = 1;
    static int RESOLVE_TARGET = -1;
    static int RESTRICT_SOURCE = -1;
    static int RESTRICT_TARGET = 1;

    public static final int SEARCH_NAME_CODE = 1;
    public static final int SEARCH_DEFINITION = 2;

    public static final int SEARCH_PROPERTY_VALUE = 3;
    public static final int SEARCH_ROLE_VALUE = 6;
    public static final int SEARCH_ASSOCIATION_VALUE = 7;

    static final List<String> STOP_WORDS = Arrays.asList(new String[] {
        "a", "an", "and", "by", "for", "of", "on", "in", "nos", "the", "to", "with"});

    //static LocalNameList noopList_ = Constructors.createLocalNameList("_noop_");

    //==================================================================================


    public SearchUtils()
    {
        //setCodingSchemeMap();
        try {
            NCImBrowserProperties properties = NCImBrowserProperties.getInstance();
            String sort_str = properties.getProperty(NCImBrowserProperties.SORT_BY_SCORE);
            apply_sort_score = false;
            if (sort_str.compareTo("true") == 0) apply_sort_score = true;
        } catch (Exception ex) {

        }
    }

    public SearchUtils(String url)
    {
        //setCodingSchemeMap();
        this.url = url;
        //setCodingSchemeMap();
        try {
            NCImBrowserProperties properties = NCImBrowserProperties.getInstance();
            String sort_str = properties.getProperty(NCImBrowserProperties.SORT_BY_SCORE);
            apply_sort_score = false;
            if (sort_str.compareTo("true") == 0) apply_sort_score = true;
        } catch (Exception ex) {

        }
    }


    private static void setCodingSchemeMap()
    {
        codingSchemeMap = new HashMap();
        csnv2codingSchemeNameMap = new HashMap();
        csnv2VersionMap = new HashMap();


        try {
            //RemoteServerUtil rsu = new RemoteServerUtil();
            //EVSApplicationService lbSvc = rsu.createLexBIGService();
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingSchemeRenderingList csrl = lbSvc.getSupportedCodingSchemes();
            if(csrl == null) System.out.println("csrl is NULL");

            CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
            for (int i=0; i<csrs.length; i++)
            {
                CodingSchemeRendering csr = csrs[i];
                Boolean isActive = csr.getRenderingDetail().getVersionStatus().equals(CodingSchemeVersionStatus.ACTIVE);
                if (isActive != null && isActive.equals(Boolean.TRUE))
                {
                    CodingSchemeSummary css = csr.getCodingSchemeSummary();
                    String formalname = css.getFormalName();
                    String representsVersion = css.getRepresentsVersion();
                    CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
                    vt.setVersion(representsVersion);


                    CodingScheme scheme = null;
                    try {
                        scheme = lbSvc.resolveCodingScheme(formalname, vt);
                        if (scheme != null)
                        {
                            codingSchemeMap.put((Object) formalname, (Object) scheme);

                            String value = formalname + " (version: " + representsVersion + ")";
                            System.out.println(value);

                            csnv2codingSchemeNameMap.put(value, formalname);
                            csnv2VersionMap.put(value, representsVersion);

                        }

                    } catch (Exception e) {
                        String urn = css.getCodingSchemeURI();
                        try {
                            scheme = lbSvc.resolveCodingScheme(urn, vt);
                            if (scheme != null)
                            {
                                codingSchemeMap.put((Object) formalname, (Object) scheme);

                                String value = formalname + " (version: " + representsVersion + ")";
                                System.out.println(value);

                                csnv2codingSchemeNameMap.put(value, formalname);
                                csnv2VersionMap.put(value, representsVersion);

                            }

                        } catch (Exception ex) {

                            String localname = css.getLocalName();
                            try {
                                scheme = lbSvc.resolveCodingScheme(localname, vt);
                                if (scheme != null)
                                {
                                    codingSchemeMap.put((Object) formalname, (Object) scheme);

                                    String value = formalname + " (version: " + representsVersion + ")";
                                    System.out.println(value);

                                    csnv2codingSchemeNameMap.put(value, formalname);
                                    csnv2VersionMap.put(value, representsVersion);

                                }
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Vector getSuperconceptCodes(String scheme, String version, String code) { //throws LBException{
        long ms = System.currentTimeMillis();
        Vector v = new Vector();
        try {
            //EVSApplicationService lbSvc = new RemoteServerUtil().createLexBIGService(this.url);
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbSvc.getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(version);
            String desc = null;
            try {
                desc = lbscm.createCodeNodeSet(new String[] {code}, scheme, csvt)
                    .resolveToList(null, null, null, 1)
                    .getResolvedConceptReference(0)
                    .getEntityDescription().getContent();
            } catch (Exception e) {
                desc = "<not found>";
            }

            // Iterate through all hierarchies and levels ...
            String[] hierarchyIDs = lbscm.getHierarchyIDs(scheme, csvt);
            for (int k = 0; k < hierarchyIDs.length; k++) {
                String hierarchyID = hierarchyIDs[k];
                AssociationList associations = lbscm.getHierarchyLevelPrev(scheme, csvt, hierarchyID, code, false, null);
                for (int i = 0; i < associations.getAssociationCount(); i++) {
                    Association assoc = associations.getAssociation(i);
                    AssociatedConceptList concepts = assoc.getAssociatedConcepts();
                    for (int j = 0; j < concepts.getAssociatedConceptCount(); j++) {
                        AssociatedConcept concept = concepts.getAssociatedConcept(j);
                        String nextCode = concept.getConceptCode();
                        v.add(nextCode);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("Run time (ms): " + (System.currentTimeMillis() - ms));
        }
        return v;
    }


    public Vector getSuperconceptCodes_Local(String scheme, String version, String code) { //throws LBException{
        long ms = System.currentTimeMillis();
        Vector v = new Vector();
        try {
            LexBIGService lbSvc = new LexBIGServiceImpl();
            LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbSvc.getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(version);
            String desc = null;
            try {
                desc = lbscm.createCodeNodeSet(new String[] {code}, scheme, csvt)
                    .resolveToList(null, null, null, 1)
                    .getResolvedConceptReference(0)
                    .getEntityDescription().getContent();
            } catch (Exception e) {
                desc = "<not found>";
            }

            // Iterate through all hierarchies and levels ...
            String[] hierarchyIDs = lbscm.getHierarchyIDs(scheme, csvt);
            for (int k = 0; k < hierarchyIDs.length; k++) {
                String hierarchyID = hierarchyIDs[k];
                AssociationList associations = lbscm.getHierarchyLevelPrev(scheme, csvt, hierarchyID, code, false, null);
                for (int i = 0; i < associations.getAssociationCount(); i++) {
                    Association assoc = associations.getAssociation(i);
                    AssociatedConceptList concepts = assoc.getAssociatedConcepts();
                    for (int j = 0; j < concepts.getAssociatedConceptCount(); j++) {
                        AssociatedConcept concept = concepts.getAssociatedConcept(j);
                        String nextCode = concept.getConceptCode();
                        v.add(nextCode);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("Run time (ms): " + (System.currentTimeMillis() - ms));
        }
        return v;
    }


    public Vector getHierarchyAssociationId(String scheme, String version) {

        Vector association_vec = new Vector();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();

            // Will handle secured ontologies later.
            CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(version);
            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, versionOrTag);
            Mappings mappings = cs.getMappings();
            SupportedHierarchy[] hierarchies = mappings.getSupportedHierarchy();
            java.lang.String[] ids = hierarchies[0].getAssociationNames();

            for (int i=0; i<ids.length; i++)
            {
                if (!association_vec.contains(ids[i])) {
                    association_vec.add(ids[i]);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return association_vec;
    }

    public static String getVocabularyVersionByTag(String codingSchemeName, String ltag)
    {
         if (codingSchemeName == null) return null;
         try {
             //EVSApplicationService lbSvc = new RemoteServerUtil().createLexBIGService();
             LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
             CodingSchemeRenderingList lcsrl = lbSvc.getSupportedCodingSchemes();
             CodingSchemeRendering[] csra = lcsrl.getCodingSchemeRendering();
             for (int i=0; i<csra.length; i++)
             {
                CodingSchemeRendering csr = csra[i];
                CodingSchemeSummary css = csr.getCodingSchemeSummary();
                if (css.getFormalName().compareTo(codingSchemeName) == 0 || css.getLocalName().compareTo(codingSchemeName) == 0)
                {
                    if (ltag == null) return css.getRepresentsVersion();
                    RenderingDetail rd = csr.getRenderingDetail();
                    CodingSchemeTagList cstl = rd.getVersionTags();
                    java.lang.String[] tags = cstl.getTag();
                    for (int j=0; j<tags.length; j++)
                    {
                        String version_tag = (String) tags[j];
                        if (version_tag.compareToIgnoreCase(ltag) == 0)
                        {
                            return css.getRepresentsVersion();
                        }
                    }
                }
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
         System.out.println("Version corresponding to tag " + ltag + " is not found " + " in " + codingSchemeName);
         return null;
     }

    public static Vector<String> getVersionListData(String codingSchemeName) {

        Vector<String> v = new Vector();
        try {
            RemoteServerUtil rsu = new RemoteServerUtil();
            //EVSApplicationService lbSvc = rsu.createLexBIGService();
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingSchemeRenderingList csrl = lbSvc.getSupportedCodingSchemes();
            if(csrl == null) System.out.println("csrl is NULL");

            CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
            for (int i=0; i<csrs.length; i++)
            {
                CodingSchemeRendering csr = csrs[i];
                Boolean isActive = csr.getRenderingDetail().getVersionStatus().equals(CodingSchemeVersionStatus.ACTIVE);
                if (isActive != null && isActive.equals(Boolean.TRUE))
                {
                    CodingSchemeSummary css = csr.getCodingSchemeSummary();
                    String formalname = css.getFormalName();
                    if (formalname.compareTo(codingSchemeName) == 0)
                    {
                        String representsVersion = css.getRepresentsVersion();
                        v.add(representsVersion);
                    }
                }
            }
       } catch (Exception ex) {

       }
       return v;
   }

    protected static Association processForAnonomousNodes(Association assoc){
        //clone Association except associatedConcepts
        Association temp = new Association();
        temp.setAssociatedData(assoc.getAssociatedData());
        temp.setAssociationName(assoc.getAssociationName());
        temp.setAssociationReference(assoc.getAssociationReference());
        temp.setDirectionalName(assoc.getDirectionalName());
        temp.setAssociatedConcepts(new AssociatedConceptList());

        for(int i = 0; i < assoc.getAssociatedConcepts().getAssociatedConceptCount(); i++)
        {
            //Conditionals to deal with anonymous nodes and UMLS top nodes "V-X"
            //The first three allow UMLS traversal to top node.
            //The last two are specific to owl anonymous nodes which can act like false
            //top nodes.
            if(
                assoc.getAssociatedConcepts().getAssociatedConcept(i).getReferencedEntry() != null &&
                assoc.getAssociatedConcepts().getAssociatedConcept(i).getReferencedEntry().getIsAnonymous() != null &&
                assoc.getAssociatedConcepts().getAssociatedConcept(i).getReferencedEntry().getIsAnonymous() != false &&
                !assoc.getAssociatedConcepts().getAssociatedConcept(i).getConceptCode().equals("@") &&
                !assoc.getAssociatedConcepts().getAssociatedConcept(i).getConceptCode().equals("@@")
                )
            {
                //do nothing
            }
            else{
                temp.getAssociatedConcepts().addAssociatedConcept(assoc.getAssociatedConcepts().getAssociatedConcept(i));
            }
        }
        return temp;
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static NameAndValueList createNameAndValueList(String[] names, String[] values)
    {
        NameAndValueList nvList = new NameAndValueList();
        for (int i=0; i<names.length; i++)
        {
            NameAndValue nv = new NameAndValue();
            nv.setName(names[i]);
            if (values != null)
            {
                nv.setContent(values[i]);
            }
            nvList.addNameAndValue(nv);
        }
        return nvList;
    }

    public static LocalNameList vector2LocalNameList(Vector<String> v)
    {
        if (v == null) return null;
        LocalNameList list = new LocalNameList();
        for (int i=0; i<v.size(); i++)
        {
            String vEntry = (String) v.elementAt(i);
            list.addEntry(vEntry);
        }
        return list;
    }

    protected static NameAndValueList createNameAndValueList(Vector names, Vector values)
    {
        if (names == null) return null;
        NameAndValueList nvList = new NameAndValueList();
        for (int i=0; i<names.size(); i++)
        {
            String name = (String) names.elementAt(i);
            String value = (String) values.elementAt(i);
            NameAndValue nv = new NameAndValue();
            nv.setName(name);
            if (value != null)
            {
                nv.setContent(value);
            }
            nvList.addNameAndValue(nv);
        }
        return nvList;
    }


    public static ResolvedConceptReferencesIterator restrictToMatchingProperty(
                                             String codingSchemeName,
                                             String version,
                                             Vector property_vec,
                                             Vector source_vec,
                                             Vector qualifier_name_vec,
                                             Vector qualifier_value_vec,
                                             java.lang.String matchText,
                                             java.lang.String matchAlgorithm,
                                             java.lang.String language)
    {

        LocalNameList propertyList = vector2LocalNameList(property_vec);
        CodedNodeSet.PropertyType[] propertyTypes = null;
        LocalNameList sourceList = vector2LocalNameList(source_vec);

        NameAndValueList qualifierList = createNameAndValueList(qualifier_name_vec, qualifier_value_vec);

        return restrictToMatchingProperty(codingSchemeName,
                                       version,
                                       propertyList,
                                       propertyTypes,
                                       sourceList,
                                       qualifierList,
                                       matchText,
                                       matchAlgorithm,
                                       language);


    }


    public static ResolvedConceptReferencesIterator restrictToMatchingProperty(
                                                String codingSchemeName,
                                                String version,

                                                LocalNameList propertyList,
                                                CodedNodeSet.PropertyType[] propertyTypes,
                                                LocalNameList sourceList,
                                                NameAndValueList qualifierList,

                                                java.lang.String matchText,
                                                java.lang.String matchAlgorithm,
                                                java.lang.String language)
    {

         CodedNodeSet cns = null;
         ResolvedConceptReferencesIterator iterator = null;
         try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

            if (lbSvc == null)
            {
                System.out.println("lbSvc = null");
                return null;
            }
            else {
				//System.out.println("*** Connected to " + RemoteServerUtil.getServiceURL());
			}

            CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
            if (version != null) versionOrTag.setVersion(version);

            cns = lbSvc.getCodingSchemeConcepts(codingSchemeName, versionOrTag);
            if (cns == null)
            {
                System.out.println("cns = null");
                return null;
            }

            LocalNameList contextList = null;
            try {
                cns = cns.restrictToMatchingProperties(propertyList,
                                               propertyTypes,
                                               sourceList,
                                               contextList,
                                               qualifierList,
                                               matchText,
                                               matchAlgorithm,
                                               language
                                               );
            } catch (Exception ex) {
                return null;
            }

            LocalNameList restrictToProperties = new LocalNameList();
            // KLO, 030509
            SortOptionList sortCriteria = null;
                //Constructors.createSortOptionList(new String[]{"matchToQuery"});

            try {
                // resolve nothing
                boolean resolveConcepts = false;
                //iterator = cns.resolve(sortCriteria, null, restrictToProperties, null);
                try {
                    iterator = cns.resolve(sortCriteria, null, restrictToProperties, null, resolveConcepts);
                }  catch (Exception e) {
                    System.out.println("ERROR: cns.resolve throws exceptions.");
                }

                //ResolvedConceptReferencesIterator     resolve(SortOptionList sortOptions, LocalNameList filterOptions, LocalNameList propertyNames, CodedNodeSet.PropertyType[] propertyTypes, boolean resolveConcepts)

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return iterator;
    }


    private boolean isNull(String s) {
        if (s == null) return true;
        s = s.trim();
        if (s.length() == 0) return true;
        if (s.compareTo("") == 0) return true;
        if (s.compareToIgnoreCase("null") == 0) return true;
        return false;
    }


    public static Concept getConceptByCode(String codingSchemeName, String vers, String ltag, String code)
    {
        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
            if (lbSvc == null)
            {
                System.out.println("lbSvc == null???");
                return null;
            }

            CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(vers);

            ConceptReferenceList crefs =
                createConceptReferenceList(
                    new String[] {code}, codingSchemeName);

            CodedNodeSet cns = null;

            try {
                cns = lbSvc.getCodingSchemeConcepts(codingSchemeName, versionOrTag);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            cns = cns.restrictToCodes(crefs);
            ResolvedConceptReferenceList matches = cns.resolveToList(null, null, null, null, false, 1);

            if (matches == null)
            {
                System.out.println("Concep not found.");
                return null;
            }

            // Analyze the result ...
            if (matches.getResolvedConceptReferenceCount() > 0) {
                ResolvedConceptReference ref =
                    (ResolvedConceptReference) matches.enumerateResolvedConceptReference().nextElement();

                org.LexGrid.concepts.Concept entry = new org.LexGrid.concepts.Concept();
                entry.setEntityCode(ref.getConceptCode());
                entry.setEntityDescription(ref.getEntityDescription());

                //Concept entry = ref.getReferencedEntry();
                return entry;
            }
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
         return null;
    }


    public static ConceptReferenceList createConceptReferenceList(String[] codes, String codingSchemeName)
    {
        if (codes == null)
        {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++)
        {
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(codes[i]);
            list.addConceptReference(cr);
        }
        return list;
    }


    public Vector getParentCodes(String scheme, String version, String code) {
        //SearchUtils util = new SearchUtils();
        Vector hierarchicalAssoName_vec = getHierarchyAssociationId(scheme, version);
        if (hierarchicalAssoName_vec == null || hierarchicalAssoName_vec.size() == 0)
        {
            return null;
        }
        String hierarchicalAssoName = (String) hierarchicalAssoName_vec.elementAt(0);
        Vector superconcept_vec = getAssociationSources(scheme, version, code, hierarchicalAssoName);
        if (superconcept_vec == null) return null;
        return superconcept_vec;

    }

    public ResolvedConceptReferenceList getNext(ResolvedConceptReferencesIterator iterator)
    {
        return iterator.getNext();
    }

    /**
    * Dump_matches to output, for debug purposes
    *
    * @param iterator the iterator
    * @param maxToReturn the max to return
    */
    public static Vector  resolveIterator(ResolvedConceptReferencesIterator iterator, int maxToReturn)
    {
        return resolveIterator(iterator, maxToReturn, null);
    }


    public static Vector resolveIterator(ResolvedConceptReferencesIterator iterator, int maxToReturn, String code)
    {
        Vector v = new Vector();
        if (iterator == null)
        {
            System.out.println("No match.");
            return v;
        }
        try {
            int iteration = 0;


            while (iterator.hasNext())
            {
                iteration++;
                iterator = iterator.scroll(maxToReturn);
                ResolvedConceptReferenceList rcrl = iterator.getNext();
                ResolvedConceptReference[] rcra = rcrl.getResolvedConceptReference();
                for (int i=0; i<rcra.length; i++)
                {
                    ResolvedConceptReference rcr = rcra[i];
                    //org.LexGrid.concepts.Concept ce = rcr.getReferencedEntry();
                    org.LexGrid.concepts.Concept ce = new org.LexGrid.concepts.Concept();
                    ce.setEntityCode(rcr.getConceptCode());
                    ce.setEntityDescription(rcr.getEntityDescription());
                    if (code == null)
                    {
                        v.add(ce);
                    }
                    else
                    {
                        if (ce.getEntityCode().compareTo(code) != 0) v.add(ce);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public ResolvedConceptReferencesIterator codedNodeGraph2CodedNodeSetIterator(
                            CodedNodeGraph cng,
                            ConceptReference graphFocus,
                            boolean resolveForward,
                            boolean resolveBackward,
                            int resolveAssociationDepth,
                            int maxToReturn) {
         CodedNodeSet cns = null;
         try {
             cns = cng.toNodeList(graphFocus,
                             resolveForward,
                             resolveBackward,
                             resolveAssociationDepth,
                             maxToReturn);

             if (cns == null)
             {
                 System.out.println("cng.toNodeList returns null???");
                 return null;
             }


             SortOptionList sortCriteria = null;
                //Constructors.createSortOptionList(new String[]{"matchToQuery", "code"});

             LocalNameList propertyNames = null;
             CodedNodeSet.PropertyType[] propertyTypes = null;
             ResolvedConceptReferencesIterator iterator = null;
             try {
                 iterator = cns.resolve(sortCriteria, propertyNames, propertyTypes);
             } catch (Exception e) {
                 e.printStackTrace();
             }

             if(iterator == null)
             {
                 System.out.println("cns.resolve returns null???");
             }
             return iterator;

         } catch (Exception ex) {
             ex.printStackTrace();
             return null;
         }
    }



    public Vector getSuperconcepts(String scheme, String version, String code)
    {
        //String assocName = "hasSubtype";
        String hierarchicalAssoName = "hasSubtype";
        Vector hierarchicalAssoName_vec = getHierarchyAssociationId(scheme, version);
        if (hierarchicalAssoName_vec != null && hierarchicalAssoName_vec.size() > 0)
        {
            hierarchicalAssoName = (String) hierarchicalAssoName_vec.elementAt(0);
        }
        return getAssociationSources(scheme, version, code, hierarchicalAssoName);
    }

    public Vector getAssociationSources(String scheme, String version, String code, String assocName)
    {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null) csvt.setVersion(version);
        ResolvedConceptReferenceList matches = null;
        Vector v = new Vector();
        try {
            //EVSApplicationService lbSvc = new RemoteServerUtil().createLexBIGService();
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
            NameAndValueList nameAndValueList =
                createNameAndValueList(
                    new String[] {assocName}, null);

            NameAndValueList nameAndValueList_qualifier = null;
            cng = cng.restrictToAssociations(nameAndValueList, nameAndValueList_qualifier);
            ConceptReference graphFocus = ConvenienceMethods.createConceptReference(code, scheme);

            boolean resolveForward = false;
            boolean resolveBackward = true;

            int resolveAssociationDepth = 1;
            int maxToReturn = 1000;

            ResolvedConceptReferencesIterator iterator = codedNodeGraph2CodedNodeSetIterator(
                            cng,
                            graphFocus,
                            resolveForward,
                            resolveBackward,
                            resolveAssociationDepth,
                            maxToReturn);

            v = resolveIterator(iterator, maxToReturn, code);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }

    public Vector getSubconcepts(String scheme, String version, String code)
    {
        //String assocName = "hasSubtype";
        String hierarchicalAssoName = "hasSubtype";
        Vector hierarchicalAssoName_vec = getHierarchyAssociationId(scheme, version);
        if (hierarchicalAssoName_vec != null && hierarchicalAssoName_vec.size() > 0)
        {
            hierarchicalAssoName = (String) hierarchicalAssoName_vec.elementAt(0);
        }
        return getAssociationTargets(scheme, version, code, hierarchicalAssoName);
    }

    public Vector getAssociationTargets(String scheme, String version, String code, String assocName)
    {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null) csvt.setVersion(version);
        ResolvedConceptReferenceList matches = null;
        Vector v = new Vector();
        try {
            //EVSApplicationService lbSvc = new RemoteServerUtil().createLexBIGService();
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
            NameAndValueList nameAndValueList =
                createNameAndValueList(
                    new String[] {assocName}, null);

            NameAndValueList nameAndValueList_qualifier = null;
            cng = cng.restrictToAssociations(nameAndValueList, nameAndValueList_qualifier);
            ConceptReference graphFocus = ConvenienceMethods.createConceptReference(code, scheme);

            boolean resolveForward = true;
            boolean resolveBackward = false;

            int resolveAssociationDepth = 1;
            int maxToReturn = 1000;

            ResolvedConceptReferencesIterator iterator = codedNodeGraph2CodedNodeSetIterator(
                            cng,
                            graphFocus,
                            resolveForward,
                            resolveBackward,
                            resolveAssociationDepth,
                            maxToReturn);

            v = resolveIterator(iterator, maxToReturn, code);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }

    //search for "lycogen"
    public static String preprocessContains(String s)
    {
        if (s == null) return null;
        if (s.length() == 0) return null;
        s = s.trim();
        List<String> words = toWords(s, "\\s", false, false);
        String delim = ".*";
        StringBuffer searchPhrase = new StringBuffer();

        int k = -1;
        searchPhrase.append(delim);

        for (int i = 0; i < words.size(); i++) {
            String wd = (String) words.get(i);
            wd = wd.trim();
            if (wd.compareTo("") != 0)
            {
                searchPhrase.append(wd);
                if (words.size() > 1 && i < words.size()-1)
                {
                    searchPhrase.append("\\s");
                }
            }
        }
        searchPhrase.append(delim);
        String t = searchPhrase.toString();
        //System.out.println("preprocessContains " + t);
        return t;
    }

    private boolean isNumber(String s) {
        if (s.length() == 0) return false;
        for(int i=0; i<s.length(); i++)
        {
            if(!Character.isDigit(s.charAt(i)))
               return false;
        }
        return true;
    }

    public Vector<org.LexGrid.concepts.Concept> searchByName(String scheme, String version, String matchText, String matchAlgorithm, int maxToReturn) {
		return searchByName(scheme, version, matchText, null, matchAlgorithm, maxToReturn);
	}


    public Vector<org.LexGrid.concepts.Concept> searchByName(String scheme, String version, String matchText, String source, String matchAlgorithm, int maxToReturn) {
        String matchText0 = matchText;
        String matchAlgorithm0 = matchAlgorithm;
        matchText0 = matchText0.trim();
        boolean preprocess = true;
        if (matchText == null || matchText.length() == 0)
        {
            return new Vector();
        }

        matchText = matchText.trim();
        if (matchAlgorithm.compareToIgnoreCase("exactMatch") == 0)
        {
            //KLO 032409
            if (!isNumber(matchText)) {
                if (nonAlphabetic(matchText) || matchText.indexOf(".") != -1 || matchText.indexOf("/") != -1)
                {
                    return searchByName(scheme, version, matchText, "RegExp", maxToReturn);
                }
            }
            if (containsSpecialChars(matchText))
            {
                return searchByName(scheme, version, matchText, "RegExp", maxToReturn);
            }
        }

        else if (matchAlgorithm.compareToIgnoreCase("startsWith") == 0)
        {
            /*
            matchText = "^" + matchText;
            matchAlgorithm = "RegExp";
            preprocess = false;
            */
        }
        else if (matchAlgorithm.compareToIgnoreCase("contains") == 0)
        {
            matchText = replaceSpecialCharsWithBlankChar(matchText);
            if (matchText.indexOf(" ") != -1) {
                matchText = preprocessContains(matchText);
                matchAlgorithm = "RegExp";
                preprocess = false;
            }
        }

        if (matchAlgorithm.compareToIgnoreCase("RegExp") == 0 && preprocess)
        {
            matchText = preprocessRegExp(matchText);
        }

        LocalNameList propertyList = null;
        CodedNodeSet.PropertyType[] propertyTypes = new CodedNodeSet.PropertyType[1];
        propertyTypes[0] = CodedNodeSet.PropertyType.PRESENTATION;

        LocalNameList sourceList = null;
        NameAndValueList qualifierList = null;
		if (source != null && source.compareTo("*") != 0 && source.compareTo("") != 0 && source.compareTo("ALL") != 0)
		{
			Vector<String> w2 = new Vector<String>();
			w2.add(source);
			sourceList = vector2LocalNameList(w2);
		}
        String language = null;
        ResolvedConceptReferencesIterator iterator = restrictToMatchingProperty(
                        scheme,
                        version,

                        propertyList,
                        propertyTypes,
                        sourceList,
                        qualifierList,

                        matchText,
                        matchAlgorithm,
                        language);

        //return sortByScore(searchPhrase, resultIterator, maxToReturn);
        if (apply_sort_score)
        {
                long ms = System.currentTimeMillis();
                try {
                    //iterator = sortByScore(matchText, iterator, maxToReturn);
                    iterator = sortByScore(matchText, iterator, maxToReturn, true);
                } catch (Exception ex) {

                }
                System.out.println("Sorting delay ---- Run time (ms): " + (System.currentTimeMillis() - ms));
        }
        if (iterator != null) {
            Vector v = resolveIterator( iterator, maxToReturn);
            if (v != null && v.size() > 0)
            {
                if(!apply_sort_score)
                {
                    SortUtils.quickSort(v);
                }
                return v;
            }
            /*
            if (matchAlgorithm.compareToIgnoreCase("exactMatch") == 0) {
                Concept c = getConceptByCode(scheme, null, null, matchText0);
                if (c != null)
                {
                    v = new Vector();
                    v.add(c);
                    return v;
                }
            }
            */
            //return new Vector();
        }
        /*
        if (matchAlgorithm0.compareToIgnoreCase("exactMatch") == 0) {
            matchText0 = matchText0.trim();
            Concept c = getConceptByCode(scheme, null, null, matchText0);
            if (c != null)
            {
                Vector v = new Vector();
                v.add(c);
                return v;
            }
        }
        else
        {
            return searchByName(scheme, version, matchText0, "exactMatch", maxToReturn);
        }
        */
        return new Vector();
    }



    public void testSearchByName() {
        String scheme = "NCI Thesaurus";
        String version = null;
        String matchText = "blood";
        String matchAlgorithm = "contains";
        int maxToReturn = 1000;

        long ms = System.currentTimeMillis();
        Vector<org.LexGrid.concepts.Concept> v = searchByName(scheme, version, matchText, matchAlgorithm, maxToReturn);
        System.out.println("Run time (ms): " + (System.currentTimeMillis() - ms));
        if (v != null)
        {
            System.out.println("v.size() = " + v.size());
            for (int i=0; i<v.size(); i++)
            {
                int j = i + 1;
                Concept ce = (Concept) v.elementAt(i);
                System.out.println("(" + j + ")" + " " + ce.getEntityCode() + " " + ce.getEntityDescription().getContent());
            }
        }
    }

    protected static List<String> toWords(String s, String delimitRegex, boolean removeStopWords, boolean removeDuplicates) {
        String[] words = s.split(delimitRegex);
        List<String> adjusted = new ArrayList<String>();
        for (int i = 0; i < words.length; i++) {
            String temp = words[i].toLowerCase();
            if (removeDuplicates && adjusted.contains(temp))
                continue;
            if (!removeStopWords || !STOP_WORDS.contains(temp))
                adjusted.add(temp);
        }
        return adjusted;
    }

    protected static String[] toWords(String s, boolean removeStopWords) {
        String[] words = s.split("\\s");
        List<String> adjusted = new ArrayList<String>();
        for (int i = 0; i < words.length; i++) {
            if (!removeStopWords || !STOP_WORDS.contains(words[i]))
                adjusted.add(words[i].toLowerCase());
        }
        return adjusted.toArray(new String[adjusted.size()]);
    }

    public static String preprocessSearchString(String s)
    {
        if (s == null) return null;
        if (s.length() == 0) return null;
        StringBuffer searchPhrase = new StringBuffer();
        List<String> words = toWords(s, "\\s", true, false);
        int k = -1;
        for (int i = 0; i < words.size(); i++) {
            String wd = (String) words.get(i);
            wd = wd.trim();
            if (wd.compareTo("") != 0)
            {
                k++;
                if (k > 0)
                {
                    searchPhrase.append(" ");
                }
                searchPhrase.append(wd);
            }
        }
        String t = searchPhrase.toString();
        return t;
    }

    public static boolean nonAlphabetic(String s)
    {
        if (s == null) return false;
        if (s.length() == 0) return true;
        for (int i=0; i<s.length(); i++)
        {
            char ch = s.charAt(i);
            if (Character.isLetter(ch)) return false;
        }
        return true;
    }
/*
    private static String replaceSpecialChars(String s){
        //String escapedChars = "|!(){}[]^\"~*?:;-";
        String escapedChars = "|!(){}[]^\"~*?;-_";
        for (int i=0; i<escapedChars.length(); i++)
        {
            char c = escapedChars.charAt(i);
            s = s.replace(c, ' ');
        }
        return s;
    }
*/

   private static String escapeSpecialChars(String s, String specChars)
   {
       String escapeChar = "\\";
       StringBuffer regex = new StringBuffer();
       for (int i=0; i<s.length(); i++)
       {
           char c = s.charAt(i);
           if (specChars.indexOf(c) != -1)
           {
               regex.append(escapeChar);

           }
           regex.append(c);
       }
       return regex.toString();
   }

    private static String replaceSpecialChars(String s){
        String escapedChars = "/";
        for (int i=0; i<escapedChars.length(); i++)
        {
            char c = escapedChars.charAt(i);
            s = s.replace(c, '.');
        }
        return s;
    }


   public static String preprocessRegExp(String s)
   {
       s = replaceSpecialChars(s);
       //s = escapeSpecialChars(s, "()");
       s = escapeSpecialChars(s, "(){}\\,-[]");
       String prefix = s.toLowerCase();
       String[] words = toWords(prefix, false); // include stop words
       StringBuffer regex = new StringBuffer();
       regex.append('^');
       for (int i = 0; i < words.length; i++) {
          boolean lastWord = i == words.length - 1;
          String word = words[i];
          int word_length = word.length();
          if (word_length > 0)
          {
              regex.append('(');
              if (word.charAt(word.length() - 1) == '.') {
                 regex.append(word.substring(0, word.length()));
                 regex.append("\\w*");
              }
              else
              {
                 regex.append(word);
              }
              regex.append("\\s").append(lastWord ? '*' : '+');
              regex.append(')');
          }
       }
       return regex.toString();
   }

    /************************
     * Custom sort processing
     ************************/
    /**
     * Sorts the given concept references based on a scoring algorithm
     * designed to provide a more natural ordering.  Scores are determined by
     * comparing each reference against a provided search term.
     * @param searchTerm The term used for comparison; single or multi-word.
     * @param toSort The iterator containing references to sort.
     * @param maxToReturn Sets upper limit for number of top-scored items returned.
     * @return Iterator over sorted references.
     * @throws LBException
     */
    protected ResolvedConceptReferencesIterator sortByScore(String searchTerm, ResolvedConceptReferencesIterator toSort, int maxToReturn) throws LBException {
        //logger.debug("Sorting by score: " + searchTerm);

        // Determine the set of individual words to compare against.
        List<String> compareWords = toScoreWords(searchTerm);

        // Create a bucket to store results.
        Map<String, ScoredTerm> scoredResult = new TreeMap<String, ScoredTerm>();

        // Score all items ...
        while (toSort.hasNext()) {
            // Working in chunks of 100.
            ResolvedConceptReferenceList refs = toSort.next(100);
            for (int i = 0; i < refs.getResolvedConceptReferenceCount(); i++) {
                ResolvedConceptReference ref = refs.getResolvedConceptReference(i);

                String code = ref.getConceptCode();
                Concept ce = ref.getReferencedEntry();

                // Note: Preferred descriptions carry more weight,
                // but we process all terms to allow the score to improve
                // based on any contained presentation.
                Presentation[] allTermsForConcept = ce.getPresentation();
                for (Presentation p : allTermsForConcept) {
                    float score = score(p.getValue().getContent(), compareWords, p.isIsPreferred(), i);

                    // Check for a previous match on this code for a different presentation.
                    // If already present, keep the highest score.
                    if (scoredResult.containsKey(code)) {
                        ScoredTerm scoredTerm = (ScoredTerm) scoredResult.get(code);
                        if (scoredTerm.score > score)
                            continue;
                    }
                    scoredResult.put(code, new ScoredTerm(ref, score));
                }
            }
        }
        // Return an iterator that will sort the scored result.
        return new ScoredIterator(scoredResult.values(), maxToReturn);
    }


    protected ResolvedConceptReferencesIterator sortByScore(String searchTerm, ResolvedConceptReferencesIterator toSort, int maxToReturn, boolean descriptionOnly) throws LBException {

        if (!descriptionOnly) return sortByScore(searchTerm, toSort, maxToReturn);
        // Determine the set of individual words to compare against.
        List<String> compareWords = toScoreWords(searchTerm);

        // Create a bucket to store results.
        Map<String, ScoredTerm> scoredResult = new TreeMap<String, ScoredTerm>();

        // Score all items ...
        while (toSort.hasNext()) {
            // Working in chunks of 100.
            ResolvedConceptReferenceList refs = toSort.next(100);
            for (int i = 0; i < refs.getResolvedConceptReferenceCount(); i++) {
                ResolvedConceptReference ref = refs.getResolvedConceptReference(i);
                String code = ref.getConceptCode();
                String name = ref.getEntityDescription().getContent();
                float score = score(name, compareWords, true, i);
                scoredResult.put(code, new ScoredTerm(ref, score));
            }
        }
        // Return an iterator that will sort the scored result.
        return new ScoredIterator(scoredResult.values(), maxToReturn);
    }



    /**
     * Returns a score providing a relative comparison of the given
     * text against a set of keywords.
     * <p>
     * Currently the score is evaluated as a simple percentage
     * based on number of words in the first set that are also in the
     * second, with minor adjustment for order (matching later
     * words given slightly higher weight, anticipating often the
     * subject of search will follow descriptive text).  Weight
     * is also increased for shorter phrases (measured in # words)
     * If the text is indicated to be preferred, the score is doubled
     * to promote 'bubbling to the top'.
     * <p>
     * Ranking from the original search is available but not
     * currently used in the heuristic (tends to throw-off desired
     * alphabetic groupings later).
     *
     * @param text
     * @param keywords
     * @param isPreferred
     * @param searchRank
     * @return The score; a higher value indicates a stronger match.
     */
    protected float score(String text, List<String> keywords, boolean isPreferred, float searchRank) {
        List<String> wordsToCompare = toScoreWords(text);
        float totalWords = wordsToCompare.size();
        float matchScore = 0;
        float position = 0;
        for (Iterator<String> words = wordsToCompare.listIterator(); words.hasNext(); position++) {
            String word = words.next();
            if (keywords.contains(word))
                matchScore += ((position / 10) + 1);
        }
        return
            Math.max(0, 100 + (matchScore / totalWords * 100) - (totalWords * 2))
                * (isPreferred ? 2 : 1);
    }

    /**
     * Return words from the given string to be used in scoring
     * algorithms, in order of occurrence and ignoring duplicates,
     * stop words, whitespace and common separators.
     * @param s
     * @return List
     */
    protected List<String> toScoreWords(String s) {
        return toWords(s, "[\\s,:+-;]", true, true);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean containsSpecialChars(String s){
        String escapedChars = "/.|!(){}[]^\"~*?;-_";
        for (int i=0; i<escapedChars.length(); i++)
        {
            char c = escapedChars.charAt(i);
            if (s.indexOf(c) != -1) return true;
        }
        return false;
    }

    private static String replaceSpecialCharsWithBlankChar(String s){
        String escapedChars = "/.|!(){}[]^\"~*?;-_";
        for (int i=0; i<escapedChars.length(); i++)
        {
            char c = escapedChars.charAt(i);
            s = s.replace(c, ' ');
        }
        s = s.trim();
        return s;
    }

////////////////////////////////////////////////////////////////////////////

   private CodedNodeSet restrictToActiveStatus(CodedNodeSet cns, boolean activeOnly)
   {
		 if (cns == null) return null;
		 if (!activeOnly) return cns; // no restriction, do nothing
		 try {
		 	 cns = cns.restrictToStatus(CodedNodeSet.ActiveOption.ACTIVE_ONLY, null);
		 	 return cns;
		 } catch (Exception e) {
			 e.printStackTrace();
			 return null;
		 }
   }

   public Vector findConceptWithSourceCodeMatching(String scheme, String version,
												   String sourceAbbr, String code,
												   int maxToReturn, boolean searchInactive)
   {
		LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion(version);

		if (lbSvc == null)
		{
			System.out.println("lbSvc = null");
			return null;
		}

	    LocalNameList contextList = null;
        NameAndValueList qualifierList = null;

 		Vector<String> v = null;

		if (code != null && code.compareTo("") != 0)
		{
			qualifierList = new NameAndValueList();
			NameAndValue nv = new NameAndValue();
			nv.setName("source-code");
			nv.setContent(code);
			qualifierList.addNameAndValue(nv);
		}

        LocalNameList propertyLnL = null;
     // sourceLnL
        Vector<String> w2 = new Vector<String>();
        w2.add(sourceAbbr);
        LocalNameList sourceLnL = vector2LocalNameList(w2);
        if (sourceAbbr.compareTo("*") == 0 || sourceAbbr.compareToIgnoreCase("ALL") == 0)
        {
			sourceLnL = null;
		}

        ResolvedConceptReferencesIterator matchIterator = null;
		SortOptionList sortCriteria = null;//Constructors.createSortOptionList(new String[]{"matchToQuery", "code"});
		try {
			CodedNodeSet cns = lbSvc.getCodingSchemeConcepts(scheme, null);
			if (cns == null)
			{
				System.out.println("lbSvc.getCodingSchemeConceptsd returns null");
				return null;
			}
			CodedNodeSet.PropertyType[] types = new PropertyType[] {PropertyType.PRESENTATION};
			cns = cns.restrictToProperties(propertyLnL, types, sourceLnL, contextList, qualifierList);

  		    boolean activeOnly = !searchInactive;
		    cns = restrictToActiveStatus(cns, activeOnly);

			try {
			    matchIterator = cns.resolve(sortCriteria, null,null);//ConvenienceMethods.createLocalNameList(getPropertyForCodingScheme(cs)),null);
			} catch (Exception ex) {

		    }
			if (matchIterator != null) {
				v = resolveIterator(	matchIterator, maxToReturn);
				return v;
			}

		} catch (Exception e) {
			 //getLogger().error("ERROR: Exception in findConceptWithSourceCodeMatching.");
			 return null;
		}
		return null;
    }



    public static void main(String[] args)
    {
         String url = "http://lexevsapi.nci.nih.gov/lexevsapi42";
         if (args.length == 1)
         {
             url = args[0];
             System.out.println(url);
         }

         SearchUtils test = new SearchUtils(url);
         test.testSearchByName();
    }

}

