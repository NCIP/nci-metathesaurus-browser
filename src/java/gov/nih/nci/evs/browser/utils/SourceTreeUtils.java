package gov.nih.nci.evs.browser.utils;

import java.io.*;
import java.util.*;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.naming.*;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.commonTypes.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.lexevs.metabrowser.*;
import org.LexGrid.lexevs.metabrowser.model.*;
import org.LexGrid.lexevs.metabrowser.model.MetaTreeNode.*;

import org.apache.commons.lang.*;
import org.apache.log4j.*;

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

public class SourceTreeUtils {
    private static Logger _logger = Logger.getLogger(SourceTreeUtils.class);

    // protected final Logger logger = Logger.getLogger(getClass());
    private static String[] _hierAssocToParentNodes =
        new String[] { "PAR", "isa", "branch_of", "part_of", "tributary_of" };

    private static String[] _hierAssociationToParentNodes = new String[] { "PAR" };
    private static String[] _hierAssocToChildNodes = new String[] { "CHD" };

    public static final boolean DIRECTION_FORWARD = true;

    private static SortOptionList _sortByCode =
        Constructors.createSortOptionList(new String[] { "code" });
    private LocalNameList _noopList = Constructors.createLocalNameList("_noop_");

    private int _maxLevel = 50;
    private int _maxLinks = 300;

    public SourceTreeUtils() {

    }

    public static Vector getSupportedSources(String codingScheme, String version) {
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            if (version != null)
                csvt.setVersion(version);
            return getSupportedSources(lbSvc, codingScheme, csvt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Vector getSupportedSources(LexBIGService lbSvc,
        String codingScheme, CodingSchemeVersionOrTag versionOrTag) {
        Vector v = new Vector();
        try {
            CodingScheme cs = null;
            try {
                cs = lbSvc.resolveCodingScheme(codingScheme, versionOrTag);
            } catch (Exception ex2) {
                cs = null;
                ex2.printStackTrace();
                _logger.error("Unable to resolveCodingScheme " + codingScheme);
            }
            if (cs != null) {
                SupportedSource[] sources =
                    cs.getMappings().getSupportedSource();
                for (int i = 0; i < sources.length; i++) {
                    v.add(sources[i].getLocalId());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }

    public static NameAndValueList createNameAndValueList(String[] names,
        String[] values) {
        NameAndValueList nvList = new NameAndValueList();
        for (int i = 0; i < names.length; i++) {
            NameAndValue nv = new NameAndValue();
            nv.setName(names[i]);
            if (values != null) {
                nv.setContent(values[i]);
            }
            nvList.addNameAndValue(nv);
        }
        return nvList;
    }

    // intra-CUI (atom-to-atom relationships) ???

    // Tree traversal -- detect and avoid loops (intra-CUI)

    public static ArrayList getNextLevelTreeNodeNamesAndCodes(String scheme,
        String version, String code, String source) {
        return getTreeNodeNamesAndCodes(scheme, version, code, source, true);
    }

    public static ArrayList getPrevLevelTreeNodeNamesAndCodes(String scheme,
        String version, String code, String source) {
        return getTreeNodeNamesAndCodes(scheme, version, code, source, false);
    }

    public static ArrayList getTreeNodeNamesAndCodes(String scheme,
        String version, String code, String source, boolean direction) {
        ArrayList list = new ArrayList();

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);
        long ms = System.currentTimeMillis();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            LexBIGServiceConvenienceMethods lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);

            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, csvt);
            if (cs == null)
                return null;
            Mappings mappings = cs.getMappings();
            SupportedHierarchy[] hierarchies = mappings.getSupportedHierarchy();
            if (hierarchies == null || hierarchies.length == 0)
                return null;

            SupportedHierarchy hierarchyDefn = hierarchies[0];
            String hier_id = hierarchyDefn.getLocalId();

            String[] associationsToNavigate =
                hierarchyDefn.getAssociationNames();
            boolean associationsNavigatedFwd =
                hierarchyDefn.getIsForwardNavigable();

            if (!direction)
                associationsNavigatedFwd = !associationsNavigatedFwd;

            NameAndValueList nameAndValueList =
                createNameAndValueList(associationsToNavigate, null);

            ResolvedConceptReferenceList matches = null;
            CodedNodeSet.PropertyType[] propertyTypes =
                new CodedNodeSet.PropertyType[1];
            propertyTypes[0] = PropertyType.PRESENTATION;
            try {
                CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
                NameAndValueList nameAndValueList_qualifier = null;

                if (source != null) {
                    nameAndValueList_qualifier =
                        createNameAndValueList(new String[] { "source" },
                            new String[] { source });
                }

                cng =
                    cng.restrictToAssociations(nameAndValueList,
                        nameAndValueList_qualifier);
                ConceptReference graphFocus =
                    ConvenienceMethods.createConceptReference(code, scheme);
                matches =
                    cng.resolveAsList(graphFocus, associationsNavigatedFwd,
                        !associationsNavigatedFwd, 1, 1, new LocalNameList(),
                        propertyTypes, null, -1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // Analyze the result ...
            if (matches != null
                && matches.getResolvedConceptReferenceCount() > 0) {
                ResolvedConceptReference ref =
                    (ResolvedConceptReference) matches
                        .enumerateResolvedConceptReference().nextElement();
                if (ref != null) {
                    AssociationList sourceof = ref.getSourceOf();
                    if (!associationsNavigatedFwd)
                        sourceof = ref.getTargetOf();

                    if (sourceof != null) {
                        Association[] associations = sourceof.getAssociation();
                        if (associations != null) {
                            for (int i = 0; i < associations.length; i++) {
                                Association assoc = associations[i];
                                if (assoc != null) {
                                    if (assoc.getAssociatedConcepts() != null) {
                                        AssociatedConcept[] acl =
                                            assoc.getAssociatedConcepts()
                                                .getAssociatedConcept();
                                        if (acl != null) {
                                            for (int j = 0; j < acl.length; j++) {
                                                AssociatedConcept ac = acl[j];
                                                if (ac != null
                                                    && !ac.getConceptCode()
                                                        .startsWith("@")) {
                                                    EntityDescription ed =
                                                        ac
                                                            .getEntityDescription();
                                                    if (ed != null) {
                                                        list
                                                            .add(ed
                                                                .getContent()
                                                                + "|"
                                                                + ac
                                                                    .getConceptCode());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        _logger.debug("Run time (milliseconds) getSubconcepts: "
            + (System.currentTimeMillis() - ms) + " to resolve ");
        SortUtils.quickSort(list);
        return list;
    }

    public static ArrayList getSubconceptsInTree(String scheme, String version,
        String code, String source) {
        return getAssociatedConceptsInTree(scheme, version, code, source, true);
    }

    private boolean isLeaf(MetaTreeNode focus) {
        return focus.getExpandedState().equals(ExpandedState.LEAF);
    }

    public static ArrayList getSuperconceptsInTree(String scheme,
        String version, String code, String source) {

        ArrayList list = new ArrayList();

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);
        long ms = System.currentTimeMillis();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, csvt);
            if (cs == null)
                return null;
            Mappings mappings = cs.getMappings();
            SupportedHierarchy[] hierarchies = mappings.getSupportedHierarchy();
            if (hierarchies == null || hierarchies.length == 0)
                return null;

            SupportedHierarchy hierarchyDefn = hierarchies[0];
            String hier_id = hierarchyDefn.getLocalId();

            String[] associationsToNavigate =
                hierarchyDefn.getAssociationNames();

            NameAndValueList nameAndValueList =
                createNameAndValueList(associationsToNavigate, null);
            CodedNodeSet.PropertyType[] propertyTypes =
                new CodedNodeSet.PropertyType[1];
            propertyTypes[0] = PropertyType.PRESENTATION;

            ResolvedConceptReferenceList matches = null;
            try {
                CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
                NameAndValueList nameAndValueList_qualifier = null;

                if (source != null) {
                    nameAndValueList_qualifier =
                        createNameAndValueList(new String[] { "source" },
                            new String[] { source });
                }

                cng =
                    cng.restrictToAssociations(nameAndValueList,
                        nameAndValueList_qualifier);
                ConceptReference graphFocus =
                    ConvenienceMethods.createConceptReference(code, scheme);
                matches =
                    cng.resolveAsList(graphFocus, false, true, 1, 1,
                        new LocalNameList(), propertyTypes, null, null, -1,
                        true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // Analyze the result ...
            if (matches != null
                && matches.getResolvedConceptReferenceCount() > 0) {

                ResolvedConceptReference[] rcra =
                    matches.getResolvedConceptReference();
                for (int k = 0; k < rcra.length; k++) {
                    ResolvedConceptReference ref =
                        (ResolvedConceptReference) rcra[k];
                    if (ref.getConceptCode().compareTo(code) != 0) {
                        list.add(ref);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        _logger.debug("Run time (milliseconds) getSubconcepts: "
            + (System.currentTimeMillis() - ms) + " to resolve ");
        SortUtils.quickSort(list);
        return list;

    }

    public static ArrayList getAssociatedConceptsInTree(String scheme,
        String version, String code, String source, boolean direction) {
        return getAssociatedConceptsInTree(scheme, version, code, source,
            direction, 1, 1, false);
    }

    public static ArrayList getAssociatedConceptsInTree(String scheme,
        String version, String code, String source, boolean direction,
        int resolveCodedEntryDepth, int resolveAssociationDepth,
        boolean keepLastAssociationLevelUnresolved) {

        ArrayList list = new ArrayList();

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);
        long ms = System.currentTimeMillis();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, csvt);
            if (cs == null)
                return null;
            Mappings mappings = cs.getMappings();
            SupportedHierarchy[] hierarchies = mappings.getSupportedHierarchy();
            if (hierarchies == null || hierarchies.length == 0)
                return null;

            SupportedHierarchy hierarchyDefn = hierarchies[0];
            String hier_id = hierarchyDefn.getLocalId();

            String[] associationsToNavigate =
                hierarchyDefn.getAssociationNames();
            boolean associationsNavigatedFwd =
                hierarchyDefn.getIsForwardNavigable();

            if (!direction)
                associationsNavigatedFwd = !associationsNavigatedFwd;

            NameAndValueList nameAndValueList =
                createNameAndValueList(associationsToNavigate, null);
            CodedNodeSet.PropertyType[] propertyTypes =
                new CodedNodeSet.PropertyType[1];
            propertyTypes[0] = PropertyType.PRESENTATION;

            ResolvedConceptReferenceList matches = null;
            try {
                CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
                NameAndValueList nameAndValueList_qualifier = null;

                if (source != null) {
                    nameAndValueList_qualifier =
                        createNameAndValueList(new String[] { "source" },
                            new String[] { source });
                }

                cng =
                    cng.restrictToAssociations(nameAndValueList,
                        nameAndValueList_qualifier);
                ConceptReference graphFocus =
                    ConvenienceMethods.createConceptReference(code, scheme);
                matches =
                    cng.resolveAsList(graphFocus, associationsNavigatedFwd,
                        !associationsNavigatedFwd, resolveCodedEntryDepth,
                        resolveAssociationDepth, new LocalNameList(),
                        propertyTypes, null, null, -1,
                        keepLastAssociationLevelUnresolved);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // Analyze the result ...
            if (matches != null
                && matches.getResolvedConceptReferenceCount() > 0) {

                ResolvedConceptReference[] rcra =
                    matches.getResolvedConceptReference();

                for (int k = 0; k < rcra.length; k++) {

                    // ResolvedConceptReference ref =
                    // (ResolvedConceptReference)
                    // matches.enumerateResolvedConceptReference().nextElement();

                    ResolvedConceptReference ref =
                        (ResolvedConceptReference) rcra[k];

                    if (ref != null) {
                        AssociationList sourceof = ref.getSourceOf();
                        if (!associationsNavigatedFwd)
                            sourceof = ref.getTargetOf();

                        if (sourceof != null) {
                            Association[] associations =
                                sourceof.getAssociation();
                            if (associations != null) {
                                for (int i = 0; i < associations.length; i++) {
                                    Association assoc = associations[i];
                                    if (assoc != null) {
                                        if (assoc.getAssociatedConcepts() != null) {
                                            AssociatedConcept[] acl =
                                                assoc.getAssociatedConcepts()
                                                    .getAssociatedConcept();
                                            if (acl != null) {
                                                for (int j = 0; j < acl.length; j++) {
                                                    AssociatedConcept ac =
                                                        acl[j];
                                                    if (ac != null
                                                        && !ac.getConceptCode()
                                                            .startsWith("@")) {
                                                        list.add(ac);

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        _logger.debug("Run time (milliseconds) getSubconcepts: "
            + (System.currentTimeMillis() - ms) + " to resolve ");
        SortUtils.quickSort(list);
        return list;
    }

    public static ArrayList getRootConceptNamesAndCodes(String scheme,
        String version, String source) {
        ArrayList list = new ArrayList();
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);
        long ms = System.currentTimeMillis();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            LexBIGServiceConvenienceMethods lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);

            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, csvt);
            if (cs == null) {
                _logger.warn("CodingScheme is NULL??? " + scheme);
                return null;
            }
            Mappings mappings = cs.getMappings();
            SupportedHierarchy[] hierarchies = mappings.getSupportedHierarchy();
            if (hierarchies == null || hierarchies.length == 0)
                return null;

            SupportedHierarchy hierarchyDefn = hierarchies[0];
            String hier_id = hierarchyDefn.getLocalId();

            String[] associationsToNavigate =
                hierarchyDefn.getAssociationNames();
            boolean associationsNavigatedFwd =
                hierarchyDefn.getIsForwardNavigable();

            NameAndValueList nameAndValueList =
                createNameAndValueList(associationsToNavigate, null);
            ResolvedConceptReferenceList matches = null;

            CodedNodeSet.PropertyType[] propertyTypes =
                new CodedNodeSet.PropertyType[1];
            propertyTypes[0] = PropertyType.PRESENTATION;

            try {
                CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
                NameAndValueList nameAndValueList_qualifier = null;
                ConceptReference graphFocus = null;
                try {
                    _logger.debug("cng.resolveAsList ...");
                    // matches = cng.resolveAsList(graphFocus,
                    // associationsNavigatedFwd, !associationsNavigatedFwd, 1,
                    // 1, new LocalNameList(), null, null, -1);
                    matches =
                        cng.resolveAsList(graphFocus, associationsNavigatedFwd,
                            !associationsNavigatedFwd, 1, 1,
                            new LocalNameList(), propertyTypes, null, 10);

                } catch (Exception ex) {
                    // ex.printStackTrace();
                    _logger
                        .error("\tNo top nodes could be located for the supplied restriction set in the requested direction.\n");
                    return new ArrayList();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // Analyze the result ...
            if (matches != null
                && matches.getResolvedConceptReferenceCount() > 0) {
                ResolvedConceptReference ref =
                    (ResolvedConceptReference) matches
                        .enumerateResolvedConceptReference().nextElement();
                if (ref != null) {
                    AssociationList sourceof = ref.getSourceOf();
                    if (!associationsNavigatedFwd)
                        sourceof = ref.getTargetOf();

                    if (sourceof != null) {
                        Association[] associations = sourceof.getAssociation();
                        if (associations != null) {
                            for (int i = 0; i < associations.length; i++) {
                                Association assoc = associations[i];
                                if (assoc != null) {
                                    if (assoc.getAssociatedConcepts() != null) {
                                        AssociatedConcept[] acl =
                                            assoc.getAssociatedConcepts()
                                                .getAssociatedConcept();
                                        if (acl != null) {
                                            for (int j = 0; j < acl.length; j++) {
                                                AssociatedConcept ac = acl[j];
                                                if (ac != null
                                                    && !ac.getConceptCode()
                                                        .startsWith("@")) {
                                                    EntityDescription ed =
                                                        ac
                                                            .getEntityDescription();
                                                    if (ed != null) {
                                                        list
                                                            .add(ed
                                                                .getContent()
                                                                + "|"
                                                                + ac
                                                                    .getConceptCode());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        _logger.debug("Run time (milliseconds) getRootConceptNamesAndCodes: "
            + (System.currentTimeMillis() - ms) + " to resolve ");
        SortUtils.quickSort(list);
        return list;
    }

    public static LocalNameList vector2LocalNameList(Vector<String> v) {
        if (v == null)
            return null;
        LocalNameList list = new LocalNameList();
        for (int i = 0; i < v.size(); i++) {
            String vEntry = (String) v.elementAt(i);
            list.addEntry(vEntry);
        }
        return list;
    }

    private static CodedNodeSet restrictToActiveStatus(CodedNodeSet cns,
        boolean activeOnly) {
        if (cns == null)
            return null;
        if (!activeOnly)
            return cns; // no restriction, do nothing
        try {
            cns =
                cns.restrictToStatus(CodedNodeSet.ActiveOption.ACTIVE_ONLY,
                    null);
            return cns;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResolvedConceptReferencesIterator findConceptWithSourceCodeMatching(
        String scheme, String version, String sourceAbbr, String code,
        int maxToReturn, boolean searchInactive) {
        if (sourceAbbr == null || code == null)
            return null;

        LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);

        if (lbSvc == null) {
            _logger.warn("lbSvc = null");
            return null;
        }

        LocalNameList contextList = null;
        NameAndValueList qualifierList = null;

        Vector<String> v = null;

        if (code != null && code.compareTo("") != 0) {
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
        if (sourceAbbr.compareTo("*") == 0
            || sourceAbbr.compareToIgnoreCase("ALL") == 0) {
            sourceLnL = null;
        }

        ResolvedConceptReferencesIterator matchIterator = null;
        SortOptionList sortCriteria = null;// Constructors.createSortOptionList(new
                                           // String[]{"matchToQuery", "code"});
        try {
            CodedNodeSet cns = lbSvc.getNodeSet(scheme, null, null);
            if (cns == null) {
                _logger.warn("lbSvc.getCodingSchemeConceptsd returns null");
                return null;
            }
            CodedNodeSet.PropertyType[] types =
                new CodedNodeSet.PropertyType[] { CodedNodeSet.PropertyType.PRESENTATION };
            cns =
                cns.restrictToProperties(propertyLnL, types, sourceLnL,
                    contextList, qualifierList);

            if (cns != null) {
                boolean activeOnly = !searchInactive;
                cns = restrictToActiveStatus(cns, activeOnly);

                try {
                    matchIterator = cns.resolve(sortCriteria, null, null);// ConvenienceMethods.createLocalNameList(getPropertyForCodingScheme(cs)),null);
                } catch (Exception ex) {

                }
                return matchIterator;
            }

        } catch (Exception e) {
            // getLogger().error("ERROR: Exception in findConceptWithSourceCodeMatching.");
            return null;
        }
        return null;
    }

    protected static void displayRef(PrintWriter pw, int k,
        ResolvedConceptReference ref) {
        try {
            pw.println("(" + k + ") " + ref.getConceptCode() + ":"
                + ref.getEntityDescription().getContent() + "\n");
        } catch (Exception ex) {

        }
    }

    protected static void displayRef(int k, ResolvedConceptReference ref) {
        try {
            _logger.debug("(" + k + ") " + ref.getConceptCode() + ":"
                + ref.getEntityDescription().getContent() + "\n");
        } catch (Exception ex) {

        }
    }

    public void dumpIterator(ResolvedConceptReferencesIterator iterator) {
        if (iterator == null)
            return;
        int knt = 0;
        try {
            while (iterator.hasNext()) {
                ResolvedConceptReference[] refs =
                    iterator.next(100).getResolvedConceptReference();
                for (ResolvedConceptReference ref : refs) {
                    knt++;
                    displayRef(knt, ref);
                    knt++;
                }
            }
        } catch (Exception ex) {

        }
    }

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

    // /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Paths to roots (View in Source Hierarchy) Traverse up until the unique
    // SRC V-<sab> concept is reached.

    // Check NCIt methods

    // Find parents.
    // For each parent, add children
    // Use recursion until the unique SRC V-<sab> concept is reached

    // /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ////////////////
    // search tree
    // ////////////////

    protected ResolvedConceptReference resolveConcept(String scheme,
        CodingSchemeVersionOrTag csvt, String code) throws LBException {

        CodedNodeSet cns =
            RemoteServerUtil.createLexBIGService().getCodingSchemeConcepts(
                scheme, csvt);
        cns =
            cns.restrictToMatchingProperties(ConvenienceMethods
                .createLocalNameList("conceptCode"), null, code, "exactMatch",
                null);
        ResolvedConceptReferenceList cnsList =
            cns.resolveToList(null, null,
                new PropertyType[] { PropertyType.PRESENTATION }, 1);
        return (cnsList.getResolvedConceptReferenceCount() == 0) ? null
            : cnsList.getResolvedConceptReference(0);
    }

    public boolean hasSubconcepts(String scheme, String version, String code,
        String source) {
        ArrayList list =
            getNextLevelTreeNodeNamesAndCodes(scheme, version, code, source);
        if (list != null && list.size() > 0)
            return true;
        return false;
    }

    protected boolean hasChildren(TreeItem tiParent, String code) {
        if (tiParent == null)
            return false;
        if (tiParent._assocToChildMap == null)
            return false;

        for (String association : tiParent._assocToChildMap.keySet()) {
            List<TreeItem> children = tiParent._assocToChildMap.get(association);
            for (int i = 0; i < children.size(); i++) {
                TreeItem childItem = (TreeItem) children.get(i);
                if (childItem._code.compareTo(code) == 0)
                    return true;
            }
        }
        return false;
    }

    protected boolean isValidForSAB(AssociatedConcept ac, String sab) {
        /*
         * for (NameAndValue qualifier :
         * ac.getAssociationQualifiers().getNameAndValue()) { if
         * ("source".equalsIgnoreCase(qualifier.getName()) &&
         * sab.equalsIgnoreCase(qualifier.getContent())) return true; }
         * //_logger.debug("(*) isValidForSAB returns false " +
         * ac.getEntityDescription().getContent()); return false;
         */
        return true;
    }

    protected Presentation[] getSourcePresentations(
        ResolvedConceptReference rcr, String sab) {

        // Ensure the associated entity was resolved, and look at each
        // assigned presentation for a matching source qualifier.
        List<Presentation> matches = new ArrayList<Presentation>();
        if (rcr.getEntity() != null)
            for (Presentation p : rcr.getEntity().getPresentation())
                for (Source src : p.getSource())
                    if (sab.equalsIgnoreCase(src.getContent()))
                        matches.add(p);
        return matches.toArray(new Presentation[matches.size()]);
    }

    protected String getDirectionalLabel(LexBIGServiceConvenienceMethods lbscm,
        String scheme, CodingSchemeVersionOrTag csvt, Association assoc,
        boolean navigatedFwd) throws LBException {

        String associationName =
            lbscm.getAssociationNameFromAssociationCode(scheme, csvt, assoc
                .getAssociationName());
        String assocLabel =
            navigatedFwd ? lbscm.getAssociationForwardName(associationName,
                scheme, csvt) : lbscm.getAssociationReverseName(
                associationName, scheme, csvt);

        if (StringUtils.isBlank(assocLabel))
            assocLabel =
                (navigatedFwd ? "" : "[Inverse]") + assoc.getAssociationName();
        return assocLabel;
    }

    protected String getDirectionalLabel(String scheme,
        CodingSchemeVersionOrTag csvt, Association assoc, boolean navigatedFwd)
            throws LBException {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        LexBIGServiceConvenienceMethods lbscm =
            (LexBIGServiceConvenienceMethods) lbSvc
                .getGenericExtension("LexBIGServiceConvenienceMethods");
        lbscm.setLexBIGService(lbSvc);

        String assocLabel =
            navigatedFwd ? lbscm.getAssociationForwardName(assoc
                .getAssociationName(), scheme, csvt) : lbscm
                .getAssociationReverseName(assoc.getAssociationName(), scheme,
                    csvt);
        if (StringUtils.isBlank(assocLabel))
            assocLabel =
                (navigatedFwd ? "" : "[Inverse]") + assoc.getAssociationName();
        return assocLabel;
    }

    // ====================================================================================================================

    public static ResolvedConceptReference getRootInSRC(String scheme,
        String version, String source) {
        try {
            ResolvedConceptReferencesIterator iterator =
                findConceptWithSourceCodeMatching(scheme, version, "SRC", "V-"
                    + source, -1, true);
            if (iterator != null) {
                try {
                    int numRemaining = iterator.numberRemaining();
                    if (iterator == null)
                        return null;
                    try {
                        while (iterator.hasNext()) {
                            ResolvedConceptReference[] refs =
                                iterator.next(100)
                                    .getResolvedConceptReference();
                            for (ResolvedConceptReference ref : refs) {
                                return ref;
                            }
                        }
                    } catch (Exception ex) {

                    }
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {

        }
        return null;
    }

    public HashMap getTreePathData(String scheme, String version, String sab,
        String code, int maxLevel) throws LBException {
        long ms = System.currentTimeMillis();

        LexBIGService lbsvc = RemoteServerUtil.createLexBIGService();
        LexBIGServiceConvenienceMethods lbscm =
            (LexBIGServiceConvenienceMethods) lbsvc
                .getGenericExtension("LexBIGServiceConvenienceMethods");
        lbscm.setLexBIGService(lbsvc);
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);

        HashMap map =
            getTreePathData(lbsvc, lbscm, scheme, csvt, sab, code, maxLevel);
        _logger.debug("Run time (milliseconds) getTreePathData: "
            + (System.currentTimeMillis() - ms));

        return map;

    }

    public HashMap getTreePathData(LexBIGService lbsvc,
        LexBIGServiceConvenienceMethods lbscm, String scheme,
        CodingSchemeVersionOrTag csvt, String sab, String focusCode)
            throws LBException {
        return getTreePathData(lbsvc, lbscm, scheme, csvt, sab, focusCode, -1);
    }

    public HashMap getTreePathData(LexBIGService lbsvc,
        LexBIGServiceConvenienceMethods lbscm, String scheme,
        CodingSchemeVersionOrTag csvt, String sab, String cui, int maxLevel)
            throws LBException {
        HashMap hmap = new HashMap();
        long ms = System.currentTimeMillis();

        ResolvedConceptReference rcr = resolveConcept(scheme, csvt, cui);
        if (rcr == null) {
            _logger
                .debug("Unable to resolve a concept for CUI = '" + cui + "'");
            return null;
        } else {
            _logger.debug("Concept found: "
                + rcr.getReferencedEntry().getEntityDescription().getContent()
                + " -- " + rcr.getCode());
        }

        ResolvedConceptReference SRC_root =
            getRootInSRC(scheme, csvt.getVersion(), sab);
        String rootName =
            SRC_root.getReferencedEntry().getEntityDescription().getContent();
        String rootCode = SRC_root.getCode();

        // Dummy root (place holder)
        TreeItem ti = new TreeItem("<Root>", "Root node", null);
        int pathsResolved = 0;
        try {
            // Identify the set of all codes on path from root
            // to the focus code ...
            TreeItem[] pathsFromRoot =
                buildPathsToRoot(lbsvc, lbscm, rcr, scheme, csvt, sab, maxLevel);
            pathsResolved = pathsFromRoot.length;

            for (TreeItem rootItem : pathsFromRoot) {
                if (rootItem._text.compareTo(rootName) == 0) {
                    for (String assoc : rootItem._assocToChildMap.keySet()) {
                        List<TreeItem> children =
                            rootItem._assocToChildMap.get(assoc);
                        for (TreeItem childItem : children) {
                            ti.addChild(assoc, childItem);
                        }
                    }
                } else {
                    ti.addChild("CHD", rootItem);
                }
            }
            ti._expandable = true;

        } finally {
            _logger.debug("SourceTreeUtils Run time (milliseconds): "
                + (System.currentTimeMillis() - ms) + " to resolve "
                + pathsResolved + " paths from root.");

            if (pathsResolved == 0) {
                ti._text = "No path found";
            }
        }

        hmap.put(cui, ti);
        return hmap;
    }

    protected TreeItem[] buildPathsToRoot(LexBIGService lbsvc,
        LexBIGServiceConvenienceMethods lbscm, ResolvedConceptReference rcr,
        String scheme, CodingSchemeVersionOrTag csvt, String sab, int maxLevel)
            throws LBException {

        // String root_name =
        // rcr.getReferencedEntry().getEntityDescription().getContent();//,
        // NCI_SOURCE, "PT");
        String root_name =
            getHighestRankedAtomName(rcr.getReferencedEntry(), sab);
        // TreeItem ti = new TreeItem(rcr.getCode(),
        // rcr.getEntityDescription().getContent());
        TreeItem ti = new TreeItem(rcr.getCode(), root_name);
        // LexBIGService lbs = RemoteServerUtil.createLexBIGService();

        ti._expandable =
            hasSubconcepts(scheme, csvt.getVersion(), rcr.getCode(), sab);
        // Maintain root tree items.
        Set<TreeItem> rootItems = new HashSet<TreeItem>();
        Set<String> visited_links = new HashSet<String>();
        Map<String, TreeItem> code2Tree = new HashMap();

        int currLevel = 0;
        // Natural flow of hierarchy relations moves forward
        // from tree root to leaves. Build the paths to root here
        // by processing upstream (child to parent) relationships.

        buildPathsToUpperNodes(ti, rcr, scheme, csvt, sab, code2Tree,
            rootItems, visited_links, maxLevel, currLevel);
        return rootItems.toArray(new TreeItem[rootItems.size()]);
    }

    protected void buildPathsToUpperNodes(TreeItem ti,
        ResolvedConceptReference rcr, String scheme,
        CodingSchemeVersionOrTag csvt, String sab,
        Map<String, TreeItem> code2Tree, Set<TreeItem> roots,
        Set<String> visited_links, int maxLevel, int currLevel)
            throws LBException {

        if (maxLevel != -1 && currLevel > maxLevel) {
            return;
        }

        // Only need to process a code once ...
        if (code2Tree.containsKey(rcr.getCode())) {
            return;
        }

        // Cache for future reference.
        code2Tree.put(rcr.getCode(), ti);

        // UMLS relations can be defined with forward direction
        // being parent to child or child to parent on a source
        // by source basis. Iterate twice to ensure completeness;
        // once navigating child to parent relations forward
        // and once navigating parent to child relations
        // backward. Both have the net effect of navigating
        // from the bottom of the hierarchy to the top.
        boolean isRoot = true;

        String[] associationsToNavigate = null;
        boolean associationsNavigatedFwd = false;
        boolean fwd = false;

        ArrayList acl =
            getSuperconceptsInTree(scheme, csvt.getVersion(), rcr.getCode(),
                sab);
        if (acl != null) {
            for (int j = 0; j < acl.size(); j++) {
                // AssociatedConcept refParent = (AssociatedConcept) acl.get(j);
                ResolvedConceptReference refParent =
                    (ResolvedConceptReference) acl.get(j);
                Presentation[] sabMatch =
                    getSourcePresentations(refParent, sab);
                if (sabMatch.length > 0) {
                    // We need to take into account direction of
                    // navigation on each pass to get the right label.
                    String directionalName = "CHD";// getDirectionalLabel(scheme,
                                                   // csvt, assoc, !fwd);
                    // Check for a previously registered item for the
                    // parent. If found, re-use it. Otherwise, create
                    // a new parent tree item.
                    String parentCode = refParent.getCode();

                    String parentName =
                        getHighestRankedAtomName(
                            refParent.getReferencedEntry(), sab);
                    String link = rcr.getConceptCode() + "|" + parentCode;

                    if (!visited_links.contains(link)) {
                        visited_links.add(link);
                        TreeItem tiParent = code2Tree.get(parentCode);
                        if (tiParent == null) {

                            // Create a new tree item.
                            // tiParent = new TreeItem(parentCode,
                            // refParent.getEntityDescription().getContent(),
                            // getAtomText(refParent, sab));
                            tiParent = new TreeItem(parentCode, parentName);// ,
                                                                            // getAtomText(refParent,
                                                                            // sab));

                            // Add immediate children of the parent code with an
                            // indication of sub-nodes (+). Codes already
                            // processed as part of the path are ignored since
                            // they are handled through recursion.

                            // String[] downstreamAssoc = fwd ?
                            // hierAssocToChildNodes_ : hierAssocToParentNodes_;
                            // addChildren(tiParent, scheme, csvt, sab,
                            // parentCode, code2Tree.keySet(), downstreamAssoc,
                            // fwd);
                            addChildren(tiParent, scheme, csvt, sab,
                                parentCode, code2Tree.keySet(),
                                associationsToNavigate, true);

                            // Try to go higher through recursion.
                            buildPathsToUpperNodes(tiParent, refParent, scheme,
                                csvt, sab, code2Tree, roots, visited_links,
                                maxLevel, currLevel + 1);
                        }

                        // Add the child (eliminate redundancy -- e.g.,
                        // hasSubtype and CHD)
                        if (!hasChildren(tiParent, ti._code)) {
                            tiParent.addChild(directionalName, ti);
                            tiParent._expandable = true;
                        }
                        isRoot = false;
                    }
                }
            }
        }

        if (maxLevel != -1 && currLevel == maxLevel)
            isRoot = true;
        if (isRoot) {
            roots.add(ti);
        }

    }

    protected void addChildren(TreeItem ti, String scheme,
        CodingSchemeVersionOrTag csvt, String sab, String branchRootCode,
        Set<String> codesToExclude, String[] associationsToNavigate,
        boolean associationsNavigatedFwd) throws LBException {

        // Resolve the next branch, representing children of the given
        // code, navigated according to the provided relationship and
        // direction. Resolve the children as a code graph, looking 2
        // levels deep but leaving the final level unresolved.

        ArrayList branchItemList =
            getAssociatedConceptsInTree(scheme, csvt.getVersion(),
                branchRootCode, sab, true, 1, 2, false);
        if (branchItemList == null) {
            return;
        }

        for (int i = 0; i < branchItemList.size(); i++) {
            AssociatedConcept branchItemNode =
                (AssociatedConcept) branchItemList.get(i);
            String branchItemCode = branchItemNode.getConceptCode();

            // Add here if not in the list of excluded codes.
            // This is also where we look to see if another level
            // was indicated to be available. If so, mark the
            // entry with a '+' to indicate it can be expanded.
            // if (!branchItemNode.getReferencedEntry().getIsAnonymous()) {
            if (!branchItemCode.startsWith("@")) {
                if (!codesToExclude.contains(branchItemCode)) {
                    String branchItemNodeName = null;
                    try {
                        branchItemNodeName =
                            getCodeDescription(branchItemNode, sab);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    TreeItem childItem =
                        new TreeItem(branchItemCode, branchItemNodeName);
                    AssociationList grandchildBranch =
                        associationsNavigatedFwd ? branchItemNode.getSourceOf()
                            : branchItemNode.getTargetOf();
                    if (grandchildBranch != null)
                        childItem._expandable = true;

                    String childNavText = "CHD";
                    ti.addChild(childNavText, childItem);
                } else {
                    // _logger.debug("(*) Excluding self-referential node: " +
                    // branchItemCode);
                    // ti.text = ti.text + " (*)";
                }
            }
        }
    }

    public static void Util_displayMessage(String msg) {
        _logger.debug(msg);
    }

    protected static void printTree(TreeItem ti, String focusCode, int depth) {
        StringBuffer indent = new StringBuffer();
        for (int i = 0; i < depth * 2; i++)
            indent.append("| ");

        StringBuffer codeAndText =
            new StringBuffer(indent).append(
                focusCode.equals(ti._code) ? ">" : " ").append(ti._code).append(
                ':').append(StringUtils.abbreviate(ti._text, 120)).append(
                ti._expandable ? " [+]" : "");
        if (ti._auis != null)
            for (String line : ti._auis.split("\\|"))
                codeAndText.append('\n').append(indent).append("    {").append(
                    StringUtils.abbreviate(line, 120)).append('}');
        Util_displayMessage(codeAndText.toString());

        indent.append("| ");
        for (String association : ti._assocToChildMap.keySet()) {
            Util_displayMessage(indent.toString() + association);
            List<TreeItem> children = ti._assocToChildMap.get(association);
            Collections.sort(children);
            for (TreeItem childItem : children)
                printTree(childItem, focusCode, depth + 1);
        }
    }

    public static void dumpTreeItems(HashMap hmap) {

        try {
            Set keyset = hmap.keySet();
            Object[] objs = keyset.toArray();
            String code = (String) objs[0];

            TreeItem ti = (TreeItem) hmap.get(code);
            printTree(ti, code, 0);

        } catch (Exception e) {

        }
    }

    /*
     * C0879923
     * 
     * presentation: HTLV1 IgG Ser Ql RepresentationalForm: OSN Source: LNC
     * 
     * Qualifier name: LUI Qualifier value: L1740213 Qualifier name: SUI
     * Qualifier value: S0130345 Qualifier name: AUI Qualifier value: A0155015
     * Qualifier name: source-code Qualifier value: 22360-2 Qualifier name:
     * SUPPRESS Qualifier value: N Qualifier name: mrrank Qualifier value: 255
     * 
     * presentation: human t-cell lymphotropic virus-1 Antibody.immunoglobulin
     * G:Arbitrary Concentration:Point in time:Serum:Ordinal
     * RepresentationalForm: LX Source: LNC
     * 
     * 
     * Qualifier name: LUI Qualifier value: L3810313 Qualifier name: SUI
     * Qualifier value: S4356848 Qualifier name: AUI Qualifier value: A8393666
     * Qualifier name: source-code Qualifier value: 22360-2 Qualifier name:
     * mrrank Qualifier value: 259 Qualifier name: SUPPRESS Qualifier value: N
     * 
     * 
     * presentation: HTLV 1 Ab.IgG:ACnc:Pt:Ser:Ord RepresentationalForm: LN
     * Source: LNC
     * 
     * Qualifier name: SUI Qualifier value: S4449216 Qualifier name: AUI
     * Qualifier value: A8490332 Qualifier name: source-code Qualifier value:
     * 22360-2 Qualifier name: mrrank Qualifier value: 258 Qualifier name:
     * SUPPRESS Qualifier value: N Qualifier name: LUI Qualifier value: L0095099
     * 
     * 
     * Associations: (C0879923) analyzed_by Serum (C0229671) qualifiers:
     * RUI:R43827205 STYPE1:AUI STYPE2:AUI SUPPRESS:N rela:analyzed_by
     * source:LNC source-aui:A8490332 target-aui:A7791940
     * 
     * has_expanded_form human t-cell lymphotropic virus-1
     * Antibody.immunoglobulin G:Arbitrary Concentration:Point in
     * time:Serum:Ordinal (C0879923) qualifiers: RUI:R44116328 STYPE1:AUI
     * STYPE2:AUI SUPPRESS:N rela:has_expanded_form self-referencing:true (*)
     * source:LNC source-aui:A8490332 // HTLV 1 Ab.IgG:ACnc:Pt:Ser:Ord
     * target-aui:A0155015 // HTLV1 IgG Ser Ql (extracted from Presentation
     * data)
     * 
     * 
     * C0879923
     * 
     * HTLV1 IgG Ser Ql
     * 
     * Hyperlink NCI Metathesaurus, LNC, A8490332 Hyperlink NCI Metathesaurus,
     * LNC, A0155015
     * 
     * 
     * 
     * (*) Reconstructing source hierarchy based on NCIm can be overly
     * complicated. source-aui -->[rela]-->target-aui (direction)
     * 
     * What rela values are part of source hierarchy? e.g., is the rela
     * has_expanded_form a part of LNC hierarchy? What about the direction? Is
     * there an inverse rela, is_expanded_form_of???
     * 
     * [A] -->(has_expanded_form)-->[B} implies [A] is a subconcept of [B]?, or
     * [B] is a subconcept of [A]?
     * 
     * Where are these data stored?
     */

    protected String getCodeDescription(LexBIGService lbsvc, String scheme,
        CodingSchemeVersionOrTag csvt, String code) throws LBException {

        CodedNodeSet cns = lbsvc.getCodingSchemeConcepts(scheme, csvt);
        cns =
            cns.restrictToCodes(Constructors.createConceptReferenceList(code,
                scheme));
        ResolvedConceptReferenceList rcrl =
            cns.resolveToList(null, _noopList, null, 1);
        if (rcrl.getResolvedConceptReferenceCount() > 0) {
            EntityDescription desc =
                rcrl.getResolvedConceptReference(0).getEntityDescription();
            if (desc != null)
                return desc.getContent();
        }
        return "<Not assigned>";
    }

    protected String getCodeDescription(ResolvedConceptReference ref)
            throws LBException {
        EntityDescription desc = ref.getEntityDescription();
        if (desc != null)
            return desc.getContent();
        return "<Not assigned>";
    }

    protected static String getCodeDescription(ResolvedConceptReference ref,
        String sab) throws LBException {
        return getHighestRankedAtomName(ref.getReferencedEntry(), sab);
    }

    // //////////////////////////////////////////////////////////////////////////
    /*
     * Logical Observation Identifiers Names and Codes (CUI C1136323)
     * 
     * LOINCCLASSTYPES (CUI CL403889)
     * 
     * Laboratory Class (CUI C1314970)
     * 
     * Microbiology procedure (CUI C0085672)
     * 
     * HTLV1 IgG Ser Ql
     * 
     * rela:has_expanded_form self-referencing:true (*) source:LNC
     * source-aui:A8490332 // HTLV 1 Ab.IgG:ACnc:Pt:Ser:Ord target-aui:A0155015
     * // HTLV1 IgG Ser Ql (extracted from Presentation data)
     * 
     * public String getAtom2AtomRelationships(AssociatedConcept ac, String sab)
     * { Vector v = new Vector(); String rela = null; String source = null;
     * String self_referencing = null; String source_aui = null; String
     * target_aui = null;
     * 
     * for (NameAndValue qual : ac.getAssociationQualifiers().getNameAndValue())
     * { String qualifier_name = qual.getName(); String qualifier_value =
     * qual.getContent(); if (qualifier_name.compareTo("source") == 0) source =
     * qualifier_value; if (qualifier_name.compareTo("self-referencing") == 0)
     * self_referencing = qualifier_value; if
     * (qualifier_name.compareTo("source-aui") == 0) source_aui =
     * qualifier_value; if (qualifier_name.compareTo("target-aui") == 0)
     * target_aui = qualifier_value; } if (source.compareTo(sab) == 0 &&
     * self_referencing != null && self_referencing.compareTo("true") == 0) { //
     * convert AUI to name by comparing PRESENTATION properties return
     * source_aui + "$" + rela + "$" + target_aui + "$" + ac.getCode(); } return
     * null; }
     */

    public String getPropertyNameByQualifierValue(AssociatedConcept ac,
        String qual_name, String qual_value) {
    	Entity c = ac.getReferencedEntry();
        Presentation[] presentations = c.getPresentation();
        for (int i = 0; i < presentations.length; i++) {
            Presentation presentation = presentations[i];
            PropertyQualifier[] qualifiers =
                presentation.getPropertyQualifier();
            for (int j = 0; j < qualifiers.length; j++) {
                PropertyQualifier qualifier = qualifiers[j];
                String qualifier_name = qualifier.getPropertyQualifierName();
                String qualifier_value = qualifier.getValue().getContent();
                if (qualifier_name.compareTo(qual_name) == 0
                    && qualifier_value.compareTo(qual_value) == 0) {
                    return presentation.getPropertyName();
                }
            }
        }
        return null;
    }

    public static String getHighestRankedAtomName(Entity c, String sab) {
        int rank = -1;
        String name = "Not assigned";// c.getEntityDescription().getContent();
        Presentation[] presentations = c.getPresentation();
        for (int i = 0; i < presentations.length; i++) {
            Presentation presentation = presentations[i];
            boolean containsSource = false;
            Source[] sources = presentation.getSource();
            if (sources != null && sources.length > 0) {
                for (int k = 0; k < sources.length; k++) {
                    Source src = sources[0];
                    String src_name = src.getContent();
                    if (src_name.compareTo(sab) == 0) {
                        containsSource = true;
                        break;
                    }
                }
            }
            if (containsSource) {
                PropertyQualifier[] qualifiers =
                    presentation.getPropertyQualifier();
                for (int j = 0; j < qualifiers.length; j++) {
                    PropertyQualifier qualifier = qualifiers[j];
                    String qualifier_name =
                        qualifier.getPropertyQualifierName();
                    String qualifier_value = qualifier.getValue().getContent();
                    if (qualifier_name.compareTo("mrrank") == 0) {
                        int curr_rank =
                            Integer.parseInt(qualifier_value.trim());
                        if (curr_rank > rank) {
                            name = presentation.getValue().getContent();
                            rank = curr_rank;
                        }
                    }
                }
            }
        }
        return name;
    }

    // //////////////////////////////////////////////////////////////////////////

    public static PrintWriter openPrintWriter(String outputfile) {
        try {
            PrintWriter pw =
                new PrintWriter(new BufferedWriter(new FileWriter(outputfile)));

            return pw;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeWriter(PrintWriter pw) {
        if (pw == null) {
            pw.println("WARNING: closeWriter is not open.");
            return;
        }
        pw.close();
    }

    public static void dumpRootConceptsBySource(String outputfile) {
        PrintWriter pw = openPrintWriter(outputfile);
        if (pw == null)
            return;
        long ms = System.currentTimeMillis();
        _logger.debug(outputfile + " opened.");
        _logger.debug("Writing root concepts by source - please wait.");

        Vector src_with_roots = new Vector();
        Vector src_without_roots = new Vector();
        String scheme = "NCI Metathesaurus";
        String version = null;

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);

        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        String relation = null;
        // String association = "CHD";
        boolean searchInactive = true;
        Vector sources = getSupportedSources(scheme, version);
        if (sources != null) {
            pw.println("Number of NCIm Supported Sources: " + sources.size());
        } else {
            pw.println("getSupportedSources returns null??? ");
            return;
        }

        int lcv = 0;
        for (int k = 0; k < sources.size(); k++) {
            lcv++;
            // if (lcv > 5) break;

            String source = (String) sources.elementAt(k);
            int k1 = k + 1;
            pw.println("\n(" + k1 + ")" + source);
            _logger.debug("(" + k1 + ")" + source);
            try {
                ResolvedConceptReferencesIterator iterator =
                    findConceptWithSourceCodeMatching(scheme, version, "SRC",
                        "V-" + source, -1, searchInactive);
                pw.println("Concepts in SRC with code matching: " + "V-"
                    + source);
                if (iterator != null) {
                    try {
                        if (iterator == null)
                            break;
                        int knt = 0;
                        try {
                            while (iterator.hasNext()) {
                                ResolvedConceptReference[] refs =
                                    iterator.next(100)
                                        .getResolvedConceptReference();
                                for (ResolvedConceptReference ref : refs) {
                                    knt++;
                                    displayRef(pw, knt, ref);
                                    pw.println("\n\tRoots:");
                                    // ArrayList subconcept_list =
                                    // getNextLevelTreeNodeNamesAndCodes(scheme,
                                    // version, ref.getConceptCode(), source);
                                    ArrayList subconcept_list =
                                        getSubconceptsInTree(scheme, version,
                                            ref.getConceptCode(), source);

                                    if (subconcept_list != null) {
                                        if (subconcept_list.size() > 0) {
                                            src_with_roots.add(source);
                                        } else {
                                            src_without_roots.add(source);
                                        }

                                        int sub_count = 0;
                                        for (int i = 0; i < subconcept_list
                                            .size(); i++) {
                                            sub_count++;
                                            AssociatedConcept ac =
                                                (AssociatedConcept) subconcept_list
                                                    .get(i);
                                            Entity c = ac.getReferencedEntry();
                                            String concept_code =
                                                c.getEntityCode();
                                            String concept_name =
                                                getHighestRankedAtomName(c,
                                                    source);
                                            pw.println("\t(" + sub_count + ") "
                                                + concept_name + " ("
                                                + concept_code + ")");
                                        }
                                    } else {
                                        src_without_roots.add(source);
                                    }
                                }
                            }
                        } catch (Exception ex) {

                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    pw.println("iterator == null??? ");
                }

            } catch (Exception ex) {
                pw.println("getCodingSchemeRoot throws exception??? ");
            }
        }

        pw.println("Sources with roots: ");
        String t = "|";
        for (int i = 0; i < src_with_roots.size(); i++) {
            String src = (String) src_with_roots.elementAt(i);
            int j = i + 1;
            pw.println("(" + j + "): " + src);
            t = t + src + "|";
        }
        pw.println(t);

        pw.println("\nSources without roots: ");
        t = "|";
        for (int i = 0; i < src_without_roots.size(); i++) {
            String src = (String) src_without_roots.elementAt(i);
            int j = i + 1;
            pw.println("(" + j + "): " + src);
            t = t + src + "|";
        }
        pw.println(t);

        closeWriter(pw);
        _logger.debug("Output file " + outputfile + " generated.");

        _logger.debug("Run time (ms): " + (System.currentTimeMillis() - ms));

    }

    public HashMap getChildren(String CUI, String SAB) {
        HashSet hset = new HashSet();
        HashMap hmap = new HashMap();
        TreeItem ti = null;

        try {
            LexBIGService lbs = RemoteServerUtil.createLexBIGService();
            MetaBrowserService mbs =
                (MetaBrowserService) lbs
                    .getGenericExtension("metabrowser-extension");
            MetaTree tree = mbs.getMetaNeighborhood(CUI, SAB);
            MetaTreeNode focus = tree.getCurrentFocus();
            ti = new TreeItem(focus.getCui(), focus.getName());
            if (isLeaf(focus)) {
                _logger.debug("Leaf node -- no children.");
                ti._expandable = false;

                hmap.put(ti._code, ti);

                return hmap;
            } else {
                ti._expandable = true;
            }

            Iterator iterator = focus.getChildren();
            if (iterator == null) {
                return hmap;
            }

            String childNavText = "CHD";
            while (iterator.hasNext()) {
                MetaTreeNode child = (MetaTreeNode) iterator.next();
                TreeItem childItem =
                    new TreeItem(child.getCui(), child.getName());
                childItem._expandable = true;
                if (isLeaf(child)) {
                    childItem._expandable = false;
                }
                ti.addChild(childNavText, childItem);
            }
        } catch (Exception e) {

        }
        hmap.put(ti._code, ti);
        return hmap;
    }

    public List getSourceHierarchyRoots(String scheme,
        CodingSchemeVersionOrTag csvt, String sab) throws LBException {
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            LexBIGServiceConvenienceMethods lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);

            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, csvt);
            if (cs == null)
                return null;
            Mappings mappings = cs.getMappings();
            SupportedHierarchy[] hierarchies = mappings.getSupportedHierarchy();
            if (hierarchies == null || hierarchies.length == 0)
                return null;

            SupportedHierarchy hierarchyDefn = hierarchies[0];
            String hier_id = hierarchyDefn.getLocalId();

            String[] associationsToNavigate =
                hierarchyDefn.getAssociationNames();
            boolean associationsNavigatedFwd =
                hierarchyDefn.getIsForwardNavigable();

            // String code = "C1140168";
            ResolvedConceptReference SRC_root =
                getRootInSRC(scheme, csvt.getVersion(), sab);
            String rootName =
                SRC_root.getReferencedEntry().getEntityDescription()
                    .getContent();
            String rootCode = SRC_root.getCode();

            _logger.debug("Searching for roots in " + sab + " under -- "
                + rootName + " (CUI: " + rootCode + ")");

            // HashMap hmap = getSubconcepts(scheme, csvt.getVersion(),
            // rootCode, sab, associationsToNavigate, associationsNavigatedFwd);
            HashMap hmap = getChildren(rootCode, sab);

            ArrayList list = new ArrayList();
            TreeItem ti = (TreeItem) hmap.get(rootCode);
            for (String assoc : ti._assocToChildMap.keySet()) {
                List<TreeItem> roots = ti._assocToChildMap.get(assoc);
                for (int k = 0; k < roots.size(); k++) {
                    TreeItem root = roots.get(k);
                    ResolvedConceptReference rcr =
                        new ResolvedConceptReference();
                    EntityDescription desc = new EntityDescription();
                    desc.setContent(root._text);
                    rcr.setEntityDescription(desc);
                    rcr.setCode(root._code);
                    list.add(rcr);
                }
            }
            SortUtils.quickSort(list);
            return list;
        } catch (Exception ex) {

        }
        return new ArrayList();
    }

    public HashMap getSubconcepts(String scheme, String version, String code,
        String sab) {
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            /*
             * LexBIGServiceConvenienceMethods lbscm =
             * (LexBIGServiceConvenienceMethods) lbSvc
             * .getGenericExtension("LexBIGServiceConvenienceMethods");
             * lbscm.setLexBIGService(lbSvc);
             */

            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            if (version != null)
                csvt.setVersion(version);

            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, csvt);
            if (cs == null)
                return null;
            Mappings mappings = cs.getMappings();
            SupportedHierarchy[] hierarchies = mappings.getSupportedHierarchy();
            if (hierarchies == null || hierarchies.length == 0)
                return null;

            SupportedHierarchy hierarchyDefn = hierarchies[0];
            String hier_id = hierarchyDefn.getLocalId();

            String[] associationsToNavigate =
                hierarchyDefn.getAssociationNames();
            boolean associationsNavigatedFwd =
                hierarchyDefn.getIsForwardNavigable();
            return getSubconcepts(scheme, version, code, sab,
                associationsToNavigate, associationsNavigatedFwd);
        } catch (Exception ex) {
            _logger.error("getSubconcepts throws exception???");
            ex.printStackTrace();
        }
        return null;
    }

    public HashMap getSubconcepts(String scheme, String version, String code,
        String sab, String[] asso_names, boolean associationsNavigatedFwd) {
        HashSet hset = new HashSet();
        HashMap hmap = new HashMap();
        TreeItem ti = null;
        Vector w = new Vector();

        long ms = System.currentTimeMillis();
        // Set<String> codesToExclude = Collections.EMPTY_SET;

        Set<String> codesToExclude = new HashSet();// Collections.EMPTY_SET;

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            Entity c = DataUtils.getConceptByCode(scheme, version, null, code);
            if (c == null) {
                _logger.warn("Concept not found ??? " + code);
                return null;
            }

            // ResolvedConceptReference SRC_root = getRootInSRC(scheme,
            // csvt.getVersion(), sab);
            String name = c.getEntityDescription().getContent();
            ti = new TreeItem(code, name);
            ti._expandable = false;

            // KLO, testing
            codesToExclude.add(code);

            addChildren(ti, scheme, csvt, sab, code, codesToExclude,
                asso_names, associationsNavigatedFwd);

            hmap.put(code, ti);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        _logger.debug("Run time (milliseconds) getSubconcepts: "
            + (System.currentTimeMillis() - ms) + " to resolve ");
        return hmap;
    }

    public List getTopNodes(TreeItem ti) {
        List list = new ArrayList();
        getTopNodes(ti, list, 0, 1);
        return list;
    }

    public void getTopNodes(TreeItem ti, List list, int currLevel, int maxLevel) {
        if (list == null)
            list = new ArrayList();
        if (currLevel > maxLevel)
            return;
        if (ti._assocToChildMap.keySet().size() > 0) {
            if (ti._text.compareTo("Root node") != 0) {
                ResolvedConceptReference rcr = new ResolvedConceptReference();
                rcr.setConceptCode(ti._code);
                EntityDescription entityDescription = new EntityDescription();
                entityDescription.setContent(ti._text);
                rcr.setEntityDescription(entityDescription);
                list.add(rcr);
            }
        }

        for (String association : ti._assocToChildMap.keySet()) {
            List<TreeItem> children = ti._assocToChildMap.get(association);
            Collections.sort(children);
            for (TreeItem childItem : children) {
                getTopNodes(childItem, list, currLevel + 1, maxLevel);
            }
        }
    }

    public List getTopNodes(TreeItem ti, String sab) {
        List list = new ArrayList();
        getTopNodes(ti, list, 0, 1, sab);
        return list;
    }

    public void getTopNodes(TreeItem ti, List list, int currLevel,
        int maxLevel, String sab) {
        if (list == null)
            list = new ArrayList();
        if (currLevel > maxLevel) {
            return;
        }

        if (ti._assocToChildMap.keySet().size() > 0) {
            if (ti._text.compareTo("Root node") != 0) {
                ResolvedConceptReference rcr = new ResolvedConceptReference();
                rcr.setConceptCode(ti._code);
                EntityDescription entityDescription = new EntityDescription();
                entityDescription.setContent(ti._text);
                rcr.setEntityDescription(entityDescription);
                list.add(rcr);
            }
        }

        for (String association : ti._assocToChildMap.keySet()) {
            List<TreeItem> children = ti._assocToChildMap.get(association);
            Collections.sort(children);
            for (TreeItem childItem : children) {
                getTopNodes(childItem, list, currLevel + 1, maxLevel, sab);
            }
        }
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // self-referential relationships
    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String getAtomName(AssociatedConcept ac, String aui) {
    	Entity c = ac.getReferencedEntry();
        Presentation[] presentations = c.getPresentation();
        for (int i = 0; i < presentations.length; i++) {
            Presentation presentation = (Presentation) presentations[i];
            java.lang.String name = presentation.getPropertyName();
            java.lang.String prop_value = presentation.getValue().getContent();
            PropertyQualifier[] qualifiers =
                presentation.getPropertyQualifier();
            for (int j = 0; j < qualifiers.length; j++) {
                String qualifierName = qualifiers[j].getPropertyQualifierName();
                String qualifierValue = qualifiers[j].getValue().getContent();

                if (qualifierName.compareTo("AUI") == 0
                    && qualifierValue.compareTo(aui) == 0) {
                    return prop_value;
                }
            }
        }

        return null;
    }

    public static String getSelfReferentialRelationship(String associationName,
        AssociatedConcept ac, String sab) {
        Vector v = new Vector();
        String rela = associationName;
        String source = null;
        String self_referencing = null;
        String source_aui = null;
        String target_aui = null;

        String source_aui_name = null;
        String target_aui_name = null;

        for (NameAndValue qual : ac.getAssociationQualifiers()
            .getNameAndValue()) {
            String qualifier_name = qual.getName();
            String qualifier_value = qual.getContent();
            if (qualifier_name.compareTo("source") == 0)
                source = qualifier_value;
            if (qualifier_name.compareTo("self-referencing") == 0)
                self_referencing = qualifier_value;
            if (qualifier_name.compareTo("source-aui") == 0) {
                source_aui = qualifier_value;
                source_aui_name = getAtomName(ac, source_aui);
            }
            if (qualifier_name.compareTo("target-aui") == 0) {
                target_aui = qualifier_value;
                target_aui_name = getAtomName(ac, target_aui);
            }
            if (qualifier_name.compareTo("rela") == 0)
                rela = qualifier_value;
        }
        if (source.compareTo(sab) == 0 && self_referencing != null
            && self_referencing.compareTo("true") == 0) {
            return source_aui + "$" + source_aui_name + "$" + rela + "$"
                + target_aui + "$" + target_aui_name + "$" + source + "$"
                + ac.getCode() + "$" + ac.getEntityDescription().getContent();
        }
        return null;
    }

    public static ConceptReferenceList createConceptReferenceList(
        String[] codes, String codingSchemeName) {
        if (codes == null) {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++) {
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(codes[i]);
            list.addConceptReference(cr);
        }
        return list;
    }

    public static ArrayList getIntraCUIRelationships(String scheme,
        String version, String code, String source, boolean direction) {

        int resolveCodedEntryDepth = 1;
        int resolveAssociationDepth = 1;
        boolean keepLastAssociationLevelUnresolved = false;

        return getIntraCUIRelationships(scheme, version, code, source,
            direction, resolveCodedEntryDepth, resolveAssociationDepth,
            keepLastAssociationLevelUnresolved);

    }

    public static ArrayList getIntraCUIRelationships(String scheme,
        String version, String code, String source, boolean direction,
        int resolveCodedEntryDepth, int resolveAssociationDepth,
        boolean keepLastAssociationLevelUnresolved) {
        if (source == null)
            return null;
        Vector sources = new Vector();
        sources.add(source);

        return getIntraCUIRelationships(scheme, version, code, sources,
            direction, resolveCodedEntryDepth, resolveAssociationDepth,
            keepLastAssociationLevelUnresolved);
    }

    public static ArrayList getIntraCUIRelationships(String scheme,
        String version, String code, Vector sources, boolean direction) {

        int resolveCodedEntryDepth = 1;
        int resolveAssociationDepth = 1;
        boolean keepLastAssociationLevelUnresolved = false;

        return getIntraCUIRelationships(scheme, version, code, sources,
            direction, resolveCodedEntryDepth, resolveAssociationDepth,
            keepLastAssociationLevelUnresolved);

    }

    public static ArrayList getIntraCUIRelationships(String scheme,
        String version, String code, Vector sources, boolean direction,
        int resolveCodedEntryDepth, int resolveAssociationDepth,
        boolean keepLastAssociationLevelUnresolved) {

        ArrayList list = new ArrayList();
        if (sources == null || sources.size() == 0)
            return list;

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);
        long ms = System.currentTimeMillis();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingScheme cs = null;
            try {
                cs = lbSvc.resolveCodingScheme(scheme, csvt);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (cs == null)
                return null;
            LexBIGServiceConvenienceMethods lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);

            Mappings mappings = cs.getMappings();
            SupportedHierarchy[] hierarchies = mappings.getSupportedHierarchy();
            if (hierarchies == null || hierarchies.length == 0)
                return null;

            SupportedHierarchy hierarchyDefn = hierarchies[0];
            String hier_id = hierarchyDefn.getLocalId();

            String[] associationsToNavigate =
                hierarchyDefn.getAssociationNames();
            boolean associationsNavigatedFwd =
                hierarchyDefn.getIsForwardNavigable();

            if (!direction)
                associationsNavigatedFwd = !associationsNavigatedFwd;

            CodedNodeSet cns = lbSvc.getNodeSet(scheme, csvt, null);

            ConceptReferenceList crefs =
                createConceptReferenceList(new String[] { code }, scheme);
            cns = cns.restrictToCodes(crefs);

            NameAndValueList nameAndValueList =
                createNameAndValueList(associationsToNavigate, null);

            ResolvedConceptReferenceList matches = null;

            for (int k = 0; k < sources.size(); k++) {
                String source = (String) sources.elementAt(k);
                try {
                    CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
                    NameAndValueList nameAndValueList_qualifier = null;

                    if (source != null) {
                        nameAndValueList_qualifier =
                            createNameAndValueList(new String[] { "source" },
                                new String[] { source });
                    }
                    cng = cng.restrictToTargetCodes(cns);
                    cng =
                        cng.restrictToAssociations(nameAndValueList,
                            nameAndValueList_qualifier);
                    ConceptReference graphFocus =
                        ConvenienceMethods.createConceptReference(code, scheme);

                    CodedNodeSet.PropertyType[] propertyTypes =
                        new CodedNodeSet.PropertyType[1];
                    propertyTypes[0] = PropertyType.PRESENTATION;
                    try {
                        matches =
                            cng.resolveAsList(graphFocus,
                                associationsNavigatedFwd,
                                !associationsNavigatedFwd,
                                resolveCodedEntryDepth,
                                resolveAssociationDepth, new LocalNameList(),
                                propertyTypes, null, null, -1,
                                keepLastAssociationLevelUnresolved);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // Analyze the result ...
                if (matches == null) {
                    _logger.warn("matches == null ??? ");
                    return list;
                }
                if (matches != null
                    && matches.getResolvedConceptReferenceCount() > 0) {
                    ResolvedConceptReference ref =
                        (ResolvedConceptReference) matches
                            .enumerateResolvedConceptReference().nextElement();
                    if (ref != null) {
                        AssociationList sourceof = ref.getSourceOf();
                        if (!associationsNavigatedFwd)
                            sourceof = ref.getTargetOf();

                        if (sourceof != null) {
                            Association[] associations =
                                sourceof.getAssociation();
                            if (associations != null) {
                                for (int i = 0; i < associations.length; i++) {
                                    Association assoc = associations[i];
                                    String associationName =
                                        lbscm
                                            .getAssociationNameFromAssociationCode(
                                                scheme, csvt, assoc
                                                    .getAssociationName());
                                    if (assoc != null) {
                                        if (assoc.getAssociatedConcepts() != null) {
                                            AssociatedConcept[] acl =
                                                assoc.getAssociatedConcepts()
                                                    .getAssociatedConcept();
                                            if (acl != null) {
                                                for (int j = 0; j < acl.length; j++) {
                                                    AssociatedConcept ac =
                                                        acl[j];
                                                    if (ac != null
                                                        && !ac.getConceptCode()
                                                            .startsWith("@")) {
                                                        // _logger.debug("\t" +
                                                        // ac.getCode());
                                                        String t =
                                                            getSelfReferentialRelationship(
                                                                associationName,
                                                                ac, source);
                                                        if (t != null) {
                                                            // _logger.debug(t);
                                                            list.add(t);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        _logger.debug("Run time (milliseconds) getSubconcepts: "
            + (System.currentTimeMillis() - ms) + " to resolve ");
        // SortUtils.quickSort(list);
        return list;
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {

        SourceTreeUtils test = new SourceTreeUtils();
        String outputfile = "src_roots_new.out";
        test.dumpRootConceptsBySource(outputfile);
    }
}

/*
 * Cartilage Diseases (CUI C0007302) Tendon, ligament and cartilage disorders
 * (CUI C0947764) Musculoskeletal and connective tissue disorders (CUI C0263660)
 * Medical Dictionary for Regulatory Activities Terminology (MedDRA) (CUI
 * C1140263)
 * 
 * SourceTreeUtils buildPathsToRoot scheme: NCI Metathesaurus SourceTreeUtils
 * buildPathsToRoot sab: MDR SourceTreeUtils buildPathsToRoot maxLevel: 2
 * SourceTreeUtils buildPathsToRoot root_name: Cartilage disorders
 * SourceTreeUtils buildPathsToRoot root_code: C0007302 Run time (milliseconds)
 * getSubconcepts: 858 to resolve SourceTreeUtils buildPathsToRoot expandable:
 * true SourceTreeUtils buildPathsToUpperNodes: C0007302 Cartilage disorders Run
 * time (milliseconds) getSubconcepts: 266 to resolve SourceTreeUtils
 * getSuperconceptsInTree: 1 SourceTreeUtils sabMatch.length: 6 SourceTreeUtils
 * parentName: Cartilage disorders C0007302 SourceTreeUtils Run time
 * (milliseconds): 14539 to resolve 0 paths from root.
 */