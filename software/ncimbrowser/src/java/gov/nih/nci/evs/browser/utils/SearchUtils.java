package gov.nih.nci.evs.browser.utils;

import java.util.*;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.codingSchemes.*;
import org.apache.log4j.*;

import org.LexGrid.LexBIG.DataModel.Core.types.*;
import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;

import gov.nih.nci.evs.browser.properties.*;

import org.apache.commons.codec.language.*;
import gov.nih.nci.evs.browser.common.*;

import gov.nih.nci.evs.browser.bean.*;

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

public class SearchUtils {
    private static Logger _logger = Logger.getLogger(SearchUtils.class);
    private int _max_str_length = 1000;
    private int _penalty_multiplier_1 = 1;
    private int _penalty_multiplier_2 = 2;

    public CodingSchemeRenderingList _csrl = null;
    private static HashMap _codingSchemeMap = null;
    private Vector _codingSchemes = null;

    private static HashMap _csnv2codingSchemeNameMap = null;
    private static HashMap _csnv2VersionMap = null;

    private static final List<String> STOP_WORDS =
        Arrays.asList(new String[] { "a", "an", "and", "by", "for", "of", "on",
            "in", "nos", "the", "to", "with" });

    private DoubleMetaphone _doubleMetaphone = null;

    public static final String CONTAIN_SEARCH_ALGORITHM =
        "nonLeadingWildcardLiteralSubString";// "literalSubString";//"subString";

    private static int RESOLVE_SOURCE = 1;
    private static int RESOLVE_TARGET = -1;

    private static HashMap _propertyLocalNameListHashMap = null;

    // ==================================================================================

    public SearchUtils() {
        initializeSortParameters();
    }

    private void initializeSortParameters() {
        _doubleMetaphone = new DoubleMetaphone();
    }

    public Vector getSuperconceptCodes(String scheme, String version,
        String code) {
        long ms = System.currentTimeMillis();
        Vector v = new Vector();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            LexBIGServiceConvenienceMethods lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(version);

            // Iterate through all hierarchies and levels ...
            String[] hierarchyIDs = lbscm.getHierarchyIDs(scheme, csvt);
            for (int k = 0; k < hierarchyIDs.length; k++) {
                String hierarchyID = hierarchyIDs[k];
                AssociationList associations =
                    lbscm.getHierarchyLevelPrev(scheme, csvt, hierarchyID,
                        code, false, null);
                for (int i = 0; i < associations.getAssociationCount(); i++) {
                    Association assoc = associations.getAssociation(i);
                    AssociatedConceptList concepts =
                        assoc.getAssociatedConcepts();
                    for (int j = 0; j < concepts.getAssociatedConceptCount(); j++) {
                        AssociatedConcept concept =
                            concepts.getAssociatedConcept(j);
                        String nextCode = concept.getConceptCode();
                        v.add(nextCode);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            _logger
                .debug("Run time (ms): " + (System.currentTimeMillis() - ms));
        }
        return v;
    }

    public Vector getHierarchyAssociationId(String scheme, String version) {

        Vector association_vec = new Vector();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();

            // Will handle secured ontologies later.
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(version);
            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, versionOrTag);
            Mappings mappings = cs.getMappings();
            SupportedHierarchy[] hierarchies = mappings.getSupportedHierarchy();
            java.lang.String[] ids = hierarchies[0].getAssociationNames();

            for (int i = 0; i < ids.length; i++) {
                if (!association_vec.contains(ids[i])) {
                    association_vec.add(ids[i]);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return association_vec;
    }

    public static String getVocabularyVersionByTag(String codingSchemeName,
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
                    || css.getLocalName().compareTo(codingSchemeName) == 0) {
                    version = css.getRepresentsVersion();
                    knt++;

                    if (ltag == null)
                        return css.getRepresentsVersion();
                    RenderingDetail rd = csr.getRenderingDetail();
                    CodingSchemeTagList cstl = rd.getVersionTags();
                    java.lang.String[] tags = cstl.getTag();

                    for (int j = 0; j < tags.length; j++) {
                        String version_tag = (String) tags[j];

                        if (version_tag.compareToIgnoreCase(ltag) == 0) {
                            return css.getRepresentsVersion();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        _logger.debug("Version corresponding to tag " + ltag + " is not found "
            + " in " + codingSchemeName);
        if (ltag == null) return null;
        if (ltag.compareToIgnoreCase("PRODUCTION") == 0 & knt == 1) {
            _logger.debug("\tUse " + version + " as default.");
            return version;
        }
        return null;
    }

    public static Vector<String> getVersionListData(String codingSchemeName) {

        Vector<String> v = new Vector();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingSchemeRenderingList csrl = lbSvc.getSupportedCodingSchemes();
            if (csrl == null) {
                _logger.warn("csrl is NULL");
                return null;
			}

            CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
            for (int i = 0; i < csrs.length; i++) {
                CodingSchemeRendering csr = csrs[i];
                Boolean isActive =
                    csr.getRenderingDetail().getVersionStatus().equals(
                        CodingSchemeVersionStatus.ACTIVE);
                if (isActive != null && isActive.equals(Boolean.TRUE)) {
                    CodingSchemeSummary css = csr.getCodingSchemeSummary();
                    String formalname = css.getFormalName();
                    if (formalname.compareTo(codingSchemeName) == 0) {
                        String representsVersion = css.getRepresentsVersion();
                        v.add(representsVersion);
                    }
                }
            }
        } catch (Exception ex) {
			ex.printStackTrace();
        }
        return v;
    }

    protected static Association processForAnonomousNodes(Association assoc) {
        // clone Association except associatedConcepts
        Association temp = new Association();
        temp.setAssociatedData(assoc.getAssociatedData());
        temp.setAssociationName(assoc.getAssociationName());
        temp.setAssociationReference(assoc.getAssociationReference());
        temp.setDirectionalName(assoc.getDirectionalName());
        temp.setAssociatedConcepts(new AssociatedConceptList());

        for (int i = 0; i < assoc.getAssociatedConcepts()
            .getAssociatedConceptCount(); i++) {
            // Conditionals to deal with anonymous nodes and UMLS top nodes
            // "V-X"
            // The first three allow UMLS traversal to top node.
            // The last two are specific to owl anonymous nodes which can act
            // like false
            // top nodes.
            if (assoc.getAssociatedConcepts().getAssociatedConcept(i)
                .getReferencedEntry() != null
                && assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getReferencedEntry().getIsAnonymous() != null
                && assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getReferencedEntry().getIsAnonymous() != false
                && !assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getConceptCode().equals("@")
                && !assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getConceptCode().equals("@@")) {
                // do nothing
            } else {
                temp.getAssociatedConcepts().addAssociatedConcept(
                    assoc.getAssociatedConcepts().getAssociatedConcept(i));
            }
        }
        return temp;
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

    public static Entity getConceptByCode(String codingSchemeName,
        String vers, String ltag, String code) {
        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
            if (lbSvc == null) {
                _logger.warn("lbSvc == null???");
                return null;
            }

            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(vers);

            ConceptReferenceList crefs =
                createConceptReferenceList(new String[] { code },
                    codingSchemeName);

            CodedNodeSet cns = null;

            try {
                cns =
                    lbSvc.getCodingSchemeConcepts(codingSchemeName,
                        versionOrTag);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            if (cns == null) return null;

            cns = cns.restrictToCodes(crefs);
            ResolvedConceptReferenceList matches =
                cns.resolveToList(null, null, null, null, false, 1);

            if (matches == null) {
                _logger.warn("Concept not found.");
                return null;
            }

            // Analyze the result ...
            if (matches.getResolvedConceptReferenceCount() > 0) {
                ResolvedConceptReference ref =
                    (ResolvedConceptReference) matches
                        .enumerateResolvedConceptReference().nextElement();

                Entity entry =
                    new Entity();
                entry.setEntityCode(ref.getConceptCode());
                entry.setEntityDescription(ref.getEntityDescription());

                // Concept entry = ref.getReferencedEntry();
                return entry;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    public Vector getParentCodes(String scheme, String version, String code) {
        Vector hierarchicalAssoName_vec =
            getHierarchyAssociationId(scheme, version);
        if (hierarchicalAssoName_vec == null
            || hierarchicalAssoName_vec.size() == 0) {
            return null;
        }
        String hierarchicalAssoName =
            (String) hierarchicalAssoName_vec.elementAt(0);
        Vector superconcept_vec =
            getAssociationSources(scheme, version, code, hierarchicalAssoName);
            /*
        if (superconcept_vec == null)
            return null;
            */
        return superconcept_vec;

    }

    public ResolvedConceptReferenceList getNext(
        ResolvedConceptReferencesIterator iterator) {
        return iterator.getNext();
    }

    /**
     * Dump_matches to output, for debug purposes
     *
     * @param iterator the iterator
     * @param maxToReturn the max to return
     */
    public static Vector resolveIterator(
        ResolvedConceptReferencesIterator iterator, int maxToReturn) {
        return resolveIterator(iterator, maxToReturn, null);
    }

    public static Vector resolveIterator(
        ResolvedConceptReferencesIterator iterator, int maxToReturn, String code) {
        return resolveIterator(iterator, maxToReturn, code, true);
    }

    public static Vector resolveIterator(
        ResolvedConceptReferencesIterator iterator, int maxToReturn,
        String code, boolean sortLight) {
        Vector v = new Vector();
        if (maxToReturn == 0)
            return v;
        if (iterator == null) {
            _logger.warn("No match.");
            return v;
        }
        int scroll_size = 200;
        try {
            int iteration = 0;
            while (iterator.hasNext()) {
                iterator = iterator.scroll(scroll_size);
                ResolvedConceptReferenceList rcrl = iterator.getNext();
                if (rcrl != null) {
                    ResolvedConceptReference[] rcra =
                        rcrl.getResolvedConceptReference();
                    if (rcra == null)
                        break;
                    if (rcra.length > 0) {
                        for (int i = 0; i < rcra.length; i++) {
                            ResolvedConceptReference rcr = rcra[i];
                            if (rcr == null) {
                                _logger.warn("resolveIterator rcr == null???");
                                break;
                            }

                            if (sortLight) {
                                Entity ce = new Entity();
                                ce.setEntityCode(rcr.getConceptCode());
                                ce.setEntityDescription(rcr
                                    .getEntityDescription());
                                if (code == null) {
                                    v.add(ce);
                                } else {
                                    if (ce.getEntityCode().compareTo(code) != 0)
                                        v.add(ce);
                                }
                            } else {
                                Entity ce = rcr.getReferencedEntry();
                                if (ce == null) {
                                    _logger
                                        .warn("resolveIterator rcr.getReferencedEntry() returns null???");
                                    break;
                                } else {
                                    if (code == null)
                                        v.add(ce);
                                    else if (ce.getEntityCode().compareTo(code) != 0)
                                        v.add(ce);
                                }
                            }
                        }
                        iteration++;
                        if (maxToReturn > 0
                            & iteration * scroll_size >= maxToReturn) {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public ResolvedConceptReferencesIterator codedNodeGraph2CodedNodeSetIterator(
        CodedNodeGraph cng, ConceptReference graphFocus,
        boolean resolveForward, boolean resolveBackward,
        int resolveAssociationDepth, int maxToReturn) {
        CodedNodeSet cns = null;
        try {

            cns =
                cng.toNodeList(graphFocus, resolveForward, resolveBackward,
                    resolveAssociationDepth, maxToReturn);

            if (cns == null) {
                _logger.warn("cng.toNodeList returns null???");
                return null;
            }

            SortOptionList sortCriteria = null;
            LocalNameList propertyNames = null;
            CodedNodeSet.PropertyType[] propertyTypes = null;
            ResolvedConceptReferencesIterator iterator = null;
            try {
                iterator =
                    cns.resolve(sortCriteria, propertyNames, propertyTypes);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (iterator == null) {
                _logger.warn("cns.resolve returns null???");
            }
            return iterator;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Vector getSuperconcepts(String scheme, String version, String code) {
        String hierarchicalAssoName = "hasSubtype";
        Vector hierarchicalAssoName_vec =
            getHierarchyAssociationId(scheme, version);
        if (hierarchicalAssoName_vec != null
            && hierarchicalAssoName_vec.size() > 0) {
            hierarchicalAssoName =
                (String) hierarchicalAssoName_vec.elementAt(0);
        }
        return getAssociationSources(scheme, version, code,
            hierarchicalAssoName);
    }

    public Vector getAssociationSources(String scheme, String version,
        String code, String assocName) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);
        ResolvedConceptReferenceList matches = null;
        Vector v = new Vector();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
            NameAndValueList nameAndValueList =
                createNameAndValueList(new String[] { assocName }, null);

            NameAndValueList nameAndValueList_qualifier = null;
            cng =
                cng.restrictToAssociations(nameAndValueList,
                    nameAndValueList_qualifier);
            ConceptReference graphFocus =
                ConvenienceMethods.createConceptReference(code, scheme);

            boolean resolveForward = false;
            boolean resolveBackward = true;

            int resolveAssociationDepth = 1;
            int maxToReturn = -1;

            ResolvedConceptReferencesIterator iterator =
                codedNodeGraph2CodedNodeSetIterator(cng, graphFocus,
                    resolveForward, resolveBackward, resolveAssociationDepth,
                    maxToReturn);

            v = resolveIterator(iterator, maxToReturn, code);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }

    public Vector getSubconcepts(String scheme, String version, String code) {
        String hierarchicalAssoName = "hasSubtype";
        Vector hierarchicalAssoName_vec =
            getHierarchyAssociationId(scheme, version);
        if (hierarchicalAssoName_vec != null
            && hierarchicalAssoName_vec.size() > 0) {
            hierarchicalAssoName =
                (String) hierarchicalAssoName_vec.elementAt(0);
        }
        return getAssociationTargets(scheme, version, code,
            hierarchicalAssoName);
    }

    public Vector getAssociationTargets(String scheme, String version,
        String code, String assocName) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);

        Vector v = new Vector();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
            NameAndValueList nameAndValueList =
                createNameAndValueList(new String[] { assocName }, null);

            NameAndValueList nameAndValueList_qualifier = null;
            cng =
                cng.restrictToAssociations(nameAndValueList,
                    nameAndValueList_qualifier);
            ConceptReference graphFocus =
                ConvenienceMethods.createConceptReference(code, scheme);

            boolean resolveForward = true;
            boolean resolveBackward = false;

            int resolveAssociationDepth = 1;
            int maxToReturn = -1;

            ResolvedConceptReferencesIterator iterator =
                codedNodeGraph2CodedNodeSetIterator(cng, graphFocus,
                    resolveForward, resolveBackward, resolveAssociationDepth,
                    maxToReturn);

            v = resolveIterator(iterator, maxToReturn, code);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }

    public static String preprocessContains(String s) {
        if (s == null)
            return null;
        if (s.length() == 0)
            return null;
        s = s.trim();
        List<String> words = toWords(s, "\\s", false, false);
        String delim = ".*";
        StringBuffer searchPhrase = new StringBuffer();

        searchPhrase.append(delim);

        for (int i = 0; i < words.size(); i++) {
            String wd = (String) words.get(i);
            wd = wd.trim();
            if (wd.compareTo("") != 0) {
                searchPhrase.append(wd);
                if (words.size() > 1 && i < words.size() - 1) {
                    searchPhrase.append("\\s");
                }
            }
        }
        searchPhrase.append(delim);
        String t = searchPhrase.toString();
        return t;
    }

    public static CodedNodeSet restrictToSource(CodedNodeSet cns, String source) {
        if (cns == null)
            return cns;
        if (source == null || source.compareTo("*") == 0
            || source.compareTo("") == 0 || source.compareTo("ALL") == 0)
            return cns;

        LocalNameList contextList = null;
        LocalNameList sourceLnL = null;
        NameAndValueList qualifierList = null;

        Vector<String> w2 = new Vector<String>();
        w2.add(source);
        sourceLnL = vector2LocalNameList(w2);
        LocalNameList propertyLnL = null;
        CodedNodeSet.PropertyType[] types =
            new PropertyType[] { PropertyType.PRESENTATION };
        try {
            cns =
                cns.restrictToProperties(propertyLnL, types, sourceLnL,
                    contextList, qualifierList);
        } catch (Exception ex) {
            _logger.error("restrictToSource throws exceptions.");
            return null;
        }
        return cns;
    }

    public static Entity getConceptByCode(String codingSchemeName,
        String vers, String ltag, String code, String source) {
        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
            if (lbSvc == null) {
                _logger.warn("lbSvc == null???");
                return null;
            }

            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            if (vers != null)
                versionOrTag.setVersion(vers);

            ConceptReferenceList crefs =
                createConceptReferenceList(new String[] { code },
                    codingSchemeName);

            CodedNodeSet cns = null;

            try {
                cns =
                    lbSvc.getCodingSchemeConcepts(codingSchemeName,
                        versionOrTag);
            } catch (Exception e1) {
                // e1.printStackTrace();
            }
            if (cns == null) return null;

            cns = cns.restrictToCodes(crefs);
            cns = restrictToSource(cns, source);

            ResolvedConceptReferenceList matches =
                cns.resolveToList(null, null, null, 1);

            if (matches == null) {
                _logger.warn("Concept not found.");
                return null;
            }
            // Analyze the result ...
            if (matches.getResolvedConceptReferenceCount() > 0) {
                ResolvedConceptReference ref =
                    (ResolvedConceptReference) matches
                        .enumerateResolvedConceptReference().nextElement();

                Entity entry = ref.getReferencedEntry();

                return entry;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public ResolvedConceptReferencesIteratorWrapper searchByCode(String scheme,
        String version, String matchText, String source, String matchAlgorithm,
        boolean ranking, int maxToReturn) {

        ResolvedConceptReferencesIterator iterator = null;

        iterator =
            matchConceptCode(scheme, version, matchText, source, "LuceneQuery");
        try {
            int size = iterator.numberRemaining();
            if (size == 0) {
                iterator =
                    findConceptWithSourceCodeMatching(scheme, version, source,
                        matchText, maxToReturn, true);
            }
            return new ResolvedConceptReferencesIteratorWrapper(iterator);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

    ResolvedConceptReferencesIterator getResolvedConceptReferencesIteratorUnion(
        String scheme, String version,
        Vector<ResolvedConceptReferencesIterator> v) {
        if (v == null)
            return null;
        int maxReturn = 100;
        Vector w = new Vector();
        HashSet hset = new HashSet();
        ResolvedConceptReferencesIterator matchIterator = null;

        for (int i = 0; i < v.size(); i++) {
            ResolvedConceptReferencesIterator iterator =
                (ResolvedConceptReferencesIterator) v.elementAt(i);
            try {
                while (iterator != null && iterator.hasNext()) {
                    ResolvedConceptReferenceList rcrl =
                        iterator.next(maxReturn);
                    if (rcrl != null) {
                        ResolvedConceptReference[] refs =
                            rcrl.getResolvedConceptReference();

                        if (refs != null) {
                            for (ResolvedConceptReference ref : refs) {
                                if (ref != null) {
                                    if (!hset.contains(ref.getConceptCode())) {
                                        w.add(ref.getConceptCode());
                                        hset.add(ref.getConceptCode());
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (w.size() > 0) {
            String[] codes = new String[w.size()];
            for (int i = 0; i < w.size(); i++) {
                String code = (String) w.elementAt(i);
                codes[i] = code;
            }

            try {
                LexBIGService lbSvc =
                    new RemoteServerUtil().createLexBIGService();
                if (lbSvc == null) {
                    _logger.warn("lbSvc == null???");
                    return null;
                }

                CodingSchemeVersionOrTag versionOrTag =
                    new CodingSchemeVersionOrTag();
                if (version != null)
                    versionOrTag.setVersion(version);

                ConceptReferenceList crefs =
                    createConceptReferenceList(codes, scheme);
                CodedNodeSet cns = lbSvc.getCodingSchemeConcepts(scheme, null);
                cns = cns.restrictToCodes(crefs);
                CodedNodeSet.PropertyType[] types =
                    new PropertyType[] { PropertyType.PRESENTATION };
                cns = cns.restrictToProperties(null, types, null, null, null);

                try {
                    matchIterator = cns.resolve(null, null, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            hset.clear();
        }
        return matchIterator;

    }

    // V5.1 Implementation
    public ResolvedConceptReferencesIteratorWrapper searchByName(String scheme,
        String version, String matchText, String source, String matchAlgorithm,
        int maxToReturn) {
        return searchByName(scheme, version, matchText, source, matchAlgorithm,
            true, maxToReturn);
    }

    public ResolvedConceptReferencesIteratorWrapper searchByName(String scheme,
        String version, String matchText, String matchAlgorithm,
        boolean ranking, int maxToReturn) {
        return searchByName(scheme, version, matchText, null, matchAlgorithm,
            ranking, maxToReturn);
    }

    public ResolvedConceptReferencesIteratorWrapper searchByName(String scheme,
        String version, String matchText, String source, String matchAlgorithm,
        boolean ranking, int maxToReturn) {
        return searchByName(scheme, version, matchText, source, matchAlgorithm,
            ranking, maxToReturn, NameSearchType.All);
    }

    public enum NameSearchType {
        All, Name, Code
    };

    public ResolvedConceptReferencesIteratorWrapper searchByName(String scheme,
        String version, String matchText, String source, String matchAlgorithm,
        boolean ranking, int maxToReturn, NameSearchType nameSearchType) {
        if (matchText == null || matchText.length() == 0)
            return null;

        matchText = matchText.trim();
        if (matchAlgorithm.compareToIgnoreCase("startsWith") == 0) {
            // No literalStartsWith support
        } else if (matchAlgorithm.compareToIgnoreCase("contains") == 0) { // p11.1-q11.1
                                                                          // /100{WBC}
            // matchAlgorithm = CONTAIN_SEARCH_ALGORITHM;
            matchAlgorithm = findBestContainsAlgorithm(matchText);
            // _logger.debug("algorithm: " + matchAlgorithm);
        }

        ResolvedConceptReferencesIterator iterator = null;



        if (nameSearchType == NameSearchType.All
            || nameSearchType == NameSearchType.Name) {
            CodedNodeSet cns = null;
            CodedNodeSet.PropertyType[] propertyTypes =
                new CodedNodeSet.PropertyType[1];
            propertyTypes[0] = PropertyType.PRESENTATION;

            try {
                LexBIGService lbSvc =
                    new RemoteServerUtil().createLexBIGService();
                if (lbSvc == null) {
                    _logger.warn("lbSvc = null");
                    return null;
                }
                CodingSchemeVersionOrTag versionOrTag =
                    new CodingSchemeVersionOrTag();
                if (version != null)
                    versionOrTag.setVersion(version);

                cns = lbSvc.getNodeSet(scheme, versionOrTag, null);
                if (cns == null) {
                    _logger.warn("cns = null");
                    return null;
                }

                // LocalNameList contextList = null;
                try {

                    long restriction_ms = System.currentTimeMillis();
                    cns =
                        cns.restrictToMatchingDesignations(matchText,
                            SearchDesignationOption.ALL, matchAlgorithm, null);

                    LocalNameList sourceList = null;
                    if (source != null && source.compareToIgnoreCase("ALL") != 0) {
                        sourceList = new LocalNameList();
                        sourceList.addEntry(source);
                        cns = restrictToSource(cns, source);
                    }
/*

                    cns =
                        cns.restrictToProperties(null, propertyTypes,
                            sourceList, null, null);
*/

		            System.out.println("searchByName restriction delay (ms): "
					     + (System.currentTimeMillis() - restriction_ms));


                } catch (Exception ex) {
                    return null;
                }

                LocalNameList restrictToProperties = null;// new
                                                          // LocalNameList();
                /*
                LocalNameList propertyNames =
                    Constructors.createLocalNameList("Semantic_Type");
                // boolean resolveConcepts = true; // Semantic_Type is no longer
                // required.
                */
                LocalNameList propertyNames = null;

                SortOptionList sortCriteria = null;
                boolean resolveConcepts = false;
                LocalNameList filterOptions = null;
                propertyTypes = null;
                try {
                    try {
                        long ms = System.currentTimeMillis(), delay = 0;
                        delay = System.currentTimeMillis() - ms;


                        // iterator = cns.resolve(sortCriteria, propertyNames,
                        // restrictToProperties, null, resolveConcepts);
                        iterator =
                            cns.resolve(sortCriteria, filterOptions,
                                propertyNames, propertyTypes, resolveConcepts);
                        Debug.println("cns.resolve delay ---- Run time (ms): "
                            + delay
                            //+ (delay = System.currentTimeMillis() - ms)
                            + " -- matchAlgorithm " + matchAlgorithm);
                        // DBG.debugDetails(delay, "cns.resolve",
                        // "searchByName, CodedNodeSet.resolve");

                    } catch (Exception e) {
                        _logger.error("ERROR: cns.resolve throws exceptions.");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        if (nameSearchType == NameSearchType.All
            || nameSearchType == NameSearchType.Code) {
            try {
                int size = iterator != null ? iterator.numberRemaining() : 0;
                if (size == 0) {
                    iterator =
                        matchConceptCode(scheme, version, matchText, source,
                            "LuceneQuery");
                }
                size = iterator.numberRemaining();
                if (size == 0) {
                    iterator =
                        findConceptWithSourceCodeMatching(scheme, version,
                            source, matchText, maxToReturn, true);
                }
                // Find ICD9CM concepts by code
                //size = iterator.numberRemaining();
                if (size < 500) {
                    // heuristic rule (if the number of matches is large,
                    // then it's less likely that the matchText is a code)
                    Vector w = new Vector();
                    w.add(iterator);
                    ResolvedConceptReferencesIterator itr1 =
                        matchConceptCode(scheme, version, matchText, source,
                            "LuceneQuery");
                    if (itr1 != null)
                        w.add(itr1);
                    ResolvedConceptReferencesIterator itr2 =
                        findConceptWithSourceCodeMatching(scheme, version,
                            source, matchText, maxToReturn, true);
                    if (itr2 != null)
                        w.add(itr2);
                    iterator =
                        getResolvedConceptReferencesIteratorUnion(scheme,
                            version, w);
                }
            } catch (Exception e) {
            }
        }
        return new ResolvedConceptReferencesIteratorWrapper(iterator);
    }

    protected static List<String> toWords(String s, String delimitRegex,
        boolean removeStopWords, boolean removeDuplicates) {
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

    protected static List<String> toWords2(String s, String delimitRegex,
        boolean removeStopWords, boolean removeDuplicates) {
        s = s.trim();
        s = replaceSpecialCharsWithBlankChar(s);
        List<String> adjusted = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreTokens()) {
            String temp = st.nextToken().toLowerCase();
            if (removeDuplicates && adjusted.contains(temp))
                continue;
            if (!removeStopWords || !STOP_WORDS.contains(temp)) {
                adjusted.add(temp);
            }
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

    public static String preprocessSearchString(String s) {
        if (s == null)
            return null;
        if (s.length() == 0)
            return null;
        StringBuffer searchPhrase = new StringBuffer();
        List<String> words = toWords(s, "\\s", true, false);
        int k = -1;
        for (int i = 0; i < words.size(); i++) {
            String wd = (String) words.get(i);
            wd = wd.trim();
            if (wd.compareTo("") != 0) {
                k++;
                if (k > 0) {
                    searchPhrase.append(" ");
                }
                searchPhrase.append(wd);
            }
        }
        String t = searchPhrase.toString();
        return t;
    }

    public static boolean nonAlphabetic(String s) {
        if (s == null)
            return false;
        if (s.length() == 0)
            return true;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (Character.isLetter(ch))
                return false;
        }
        return true;
    }

    private static String escapeSpecialChars(String s, String specChars) {
        String escapeChar = "\\";
        StringBuffer regex = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (specChars.indexOf(c) != -1) {
                regex.append(escapeChar);

            }
            regex.append(c);
        }
        return regex.toString();
    }

    private static String replaceSpecialChars(String s) {
        String escapedChars = "/";
        for (int i = 0; i < escapedChars.length(); i++) {
            char c = escapedChars.charAt(i);
            s = s.replace(c, '.');
        }
        return s;
    }

    public static String preprocessRegExp(String s) {
        s = replaceSpecialChars(s);
        s = escapeSpecialChars(s, "(){}\\,-[]");
        String prefix = s.toLowerCase();
        String[] words = toWords(prefix, false); // include stop words
        StringBuffer regex = new StringBuffer();
        regex.append('^');
        for (int i = 0; i < words.length; i++) {
            boolean lastWord = i == words.length - 1;
            String word = words[i];
            int word_length = word.length();
            if (word_length > 0) {
                regex.append('(');
                if (word.charAt(word.length() - 1) == '.') {
                    regex.append(word.substring(0, word.length()));
                    regex.append("\\w*");
                } else {
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
     * Returns a score providing a relative comparison of the given text against
     * a set of keywords.
     * <p>
     * Currently the score is evaluated as a simple percentage based on number
     * of words in the first set that are also in the second, with minor
     * adjustment for order (matching later words given slightly higher weight,
     * anticipating often the subject of search will follow descriptive text).
     * Weight is also increased for shorter phrases (measured in # words) If the
     * text is indicated to be preferred, the score is doubled to promote
     * 'bubbling to the top'.
     * <p>
     * Ranking from the original search is available but not currently used in
     * the heuristic (tends to throw-off desired alphabetic groupings later).
     *
     * @param text
     * @param keywords
     * @param isPreferred
     * @param searchRank
     * @return The score; a higher value indicates a stronger match.
     */
    protected float score(String text, List<String> keywords) {
        List<String> wordsToCompare = toScoreWords(text);
        float totalWords = wordsToCompare.size();
        float matchScore = 0;
        float position = 0;
        for (Iterator<String> words = wordsToCompare.listIterator(); words
            .hasNext(); position++) {
            String word = words.next();
            if (keywords.contains(word))
                matchScore += ((position / 10) + 1);
        }
        return Math.max(0, 100 + (matchScore / totalWords * 100)
            - (totalWords * 2));
    }

    /* scoring method for DoubleMetaphoneLuceneQuery */
    protected float score(String text, List<String> keywords,
        List<String> keyword_codes, String target, boolean fuzzy_match) {
        List<String> wordsToCompare = toScoreWords(text);
        float totalWords = wordsToCompare.size();
        float matchScore = 0;
        float position = 0;
        //String s = "";
        StringBuffer buf = new StringBuffer();

        int k = 0;
        for (Iterator<String> words = wordsToCompare.listIterator(); words
            .hasNext(); position++) {
            String word = words.next();

            if (keywords.contains(word)) {
                matchScore = matchScore + (float) 10. * ((position / 10) + 1);
                word = doubleMetaphoneEncode(word);
            } else if (fuzzy_match) {
                word = doubleMetaphoneEncode(word);
                if (keyword_codes.contains(word)) {
                    matchScore += ((position / 10) + 1);
                }
            }
            buf.append(word);
            //s = s + word;
            if (k < wordsToCompare.size() - 1)
                //s = s + " ";
                buf.append(" ");
            k++;
        }

        String s = buf.toString();
        if (s.indexOf(target) == -1) {
            return (float) 0.0;
        }
        float score =
            Math.max(0, 100 + (matchScore / totalWords * 100)
                - (totalWords * 2));
        return score;
        // Math.max(0, 100 + (matchScore / totalWords * 100) - (totalWords * 2))
        // * (isPreferred ? 2 : 1);
    }

    /**
     * Return words from the given string to be used in scoring algorithms, in
     * order of occurrence and ignoring duplicates, stop words, whitespace and
     * common separators.
     *
     * @param s
     * @return List
     */
    protected List<String> toScoreWords(String s) {
        return toWords2(s, "[\\s,:+-;]", true, true);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static String replaceSpecialCharsWithBlankChar(String s) {
        String escapedChars = "/.|!(){}[]^\"~*?;-_,";
        for (int i = 0; i < escapedChars.length(); i++) {
            char c = escapedChars.charAt(i);
            s = s.replace(c, ' ');
        }
        s = s.trim();
        return s;
    }

    // //////////////////////////////////////////////////////////////////////////

    private CodedNodeSet restrictToActiveStatus(CodedNodeSet cns,
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

    public ResolvedConceptReferencesIterator findConceptWithSourceCodeMatching(
        String scheme, String version, String sourceAbbr, String code,
        int maxToReturn, boolean searchInactive) {
        if (sourceAbbr == null || code == null)
            return null;
        ResolvedConceptReferencesIterator matchIterator = null;

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

        //if (code != null && code.compareTo("") != 0) {
		if (code.compareTo("") != 0) {
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

        SortOptionList sortCriteria = null;// Constructors.createSortOptionList(new
                                           // String[]{"matchToQuery", "code"});
        try {
            CodedNodeSet cns = lbSvc.getCodingSchemeConcepts(scheme, null);
            if (cns == null) {
                _logger.warn("lbSvc.getCodingSchemeConceptsd returns null");
                return null;
            }
            CodedNodeSet.PropertyType[] types =
                new PropertyType[] { PropertyType.PRESENTATION };
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
                /*
                 * if (matchIterator != null) { v = resolveIterator(
                 * matchIterator, maxToReturn); return v; }
                 */
            }

        } catch (Exception e) {
            // getLogger().error("ERROR: Exception in findConceptWithSourceCodeMatching.");
            return null;
        }
        return matchIterator;
    }

    // /////////////////////////////////

    private static final boolean isInteger(final String s) {
        for (int x = 0; x < s.length(); x++) {
            final char c = s.charAt(x);
            if (x == 0 && (c == '-'))
                continue; // negative
            if ((c >= '0') && (c <= '9'))
                continue; // 0 - 9
            return false; // invalid
        }
        return true; // valid
    }

    private static final boolean containsDigit(final String s) {
        for (int x = 0; x < s.length(); x++) {
            final char c = s.charAt(x);
            if (x == 0 && (c == '-'))
                continue; // negative
            if ((c >= '0') && (c <= '9'))
                return true;
        }
        return false; // does not contain digit
    }

    protected String doubleMetaphoneEncode(String word) {
        if (word == null || word.length() == 0)
            return word;
        // if (isInteger(word)) return word;
        if (containsDigit(word))
            return word;

        return _doubleMetaphone.encode(word);
    }

    protected float score_contains(String text, String target) {
        String text_lower = text.toLowerCase();
        String target_lower = target.toLowerCase();
        int n = text_lower.indexOf(target_lower);
        if (n == -1)
            return -1 * _max_str_length;
        int m1 = n;
        int m2 = text.length() - (n + target.length());
        int score =
            _max_str_length - _penalty_multiplier_2 * m2 - _penalty_multiplier_1
                * m1;
        return Math.max(0, score);
    }

    public Vector searchByCode(String scheme, String version, String matchText,
        String source, String matchAlgorithm) {
        LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        Vector v = new Vector();
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        if (version != null)
            versionOrTag.setVersion(version);
        CodedNodeSet cns = null;
        try {
            cns = lbs.getNodeSet(scheme, versionOrTag, null);
            if (source != null)
                cns = restrictToSource(cns, source);
            CodedNodeSet.PropertyType[] propertyTypes = null;
            LocalNameList sourceList = null;
            LocalNameList contextList = null;
            NameAndValueList qualifierList = null;
            cns =
                cns.restrictToMatchingProperties(ConvenienceMethods
                    .createLocalNameList(new String[] { "conceptCode" }),
                    propertyTypes, sourceList, contextList, qualifierList,
                    matchText, matchAlgorithm, null);

            ResolvedConceptReference[] list = null;
            try {
                list =
                    cns.resolveToList(null, null, null, 500)
                        .getResolvedConceptReference();

                for (ResolvedConceptReference ref : list) {
                    v.add(ref.getReferencedEntry());
                }
            } catch (Exception ex) {
                _logger.error("WARNING: searchByCode throws exception.");
            }
        } catch (Exception ex) {
            _logger.error("WARNING: searchByCode throws exception.");
        }
        return v;
    }

    public ResolvedConceptReferencesIterator matchConceptCode(String scheme,
        String version, String matchText, String source, String matchAlgorithm) {
        LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        if (lbs == null) return null;

        //Vector v = new Vector();
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        if (version != null)
            versionOrTag.setVersion(version);
        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;
        try {
            cns = lbs.getNodeSet(scheme, versionOrTag, null);
            if (cns == null) return null;
            if (source != null && source.compareTo("ALL") != 0) {
                cns = restrictToSource(cns, source);
            }
            CodedNodeSet.PropertyType[] propertyTypes = null;
            LocalNameList sourceList = null;
            LocalNameList contextList = null;
            NameAndValueList qualifierList = null;
            cns =
                cns.restrictToMatchingProperties(ConvenienceMethods
                    .createLocalNameList(new String[] { "conceptCode" }),
                    propertyTypes, sourceList, contextList, qualifierList,
                    matchText, matchAlgorithm, null);

            LocalNameList restrictToProperties = new LocalNameList();
            SortOptionList sortCriteria = null;
            try {
                boolean resolveConcepts = false;
                try {
                    long ms = System.currentTimeMillis(), delay = 0;
                    iterator =
                        cns.resolve(sortCriteria, null, restrictToProperties,
                            null, resolveConcepts);

                    int size = iterator.numberRemaining();
                    _logger.debug("cns.resolve size: " + size);

                } catch (Exception e) {
                    _logger.error("ERROR: cns.resolve throws exceptions.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        } catch (Exception ex) {
            _logger.error("WARNING: searchByCode throws exception.");
        }
        return iterator;
    }

    public static CodedNodeSet.PropertyType[] createPropertyTypes(String[] types) {
        if (types == null || types.length == 0)
            return null;
        CodedNodeSet.PropertyType[] propertyTypes =
            new CodedNodeSet.PropertyType[types.length];
        int knt = -1;
        for (int i = 0; i < types.length; i++) {
            String type = (String) types[i];
            if (type.compareToIgnoreCase("PRESENTATION") == 0) {
                knt++;
                propertyTypes[knt] = PropertyType.PRESENTATION;
            } else if (type.compareToIgnoreCase("DEFINITION") == 0) {
                knt++;
                propertyTypes[knt] = PropertyType.DEFINITION;
            } else if (type.compareToIgnoreCase("GENERIC") == 0) {
                knt++;
                propertyTypes[knt] = PropertyType.GENERIC;
            } else if (type.compareToIgnoreCase("COMMENT") == 0) {
                knt++;
                propertyTypes[knt] = PropertyType.COMMENT;
            }
        }
        return propertyTypes;
    }

    public ResolvedConceptReferencesIteratorWrapper searchByProperties(
        String scheme, String version, String matchText,
        String[] property_types, String[] property_names, String source,
        String matchAlgorithm, boolean excludeDesignation, boolean ranking,
        int maxToReturn) {
		if (matchText == null) return null;

        boolean restrictToProperties = false;
        CodedNodeSet.PropertyType[] propertyTypes = null;
        LocalNameList propertyNames = null;
        LocalNameList sourceList = null;
        if (property_types != null || property_names != null) {
            _logger
                .debug("Case #1 -- property_types != null || property_names != null");

            restrictToProperties = true;
            if (property_types != null)
                propertyTypes = createPropertyTypes(property_types);
            if (property_names != null)
                propertyNames =
                    ConvenienceMethods.createLocalNameList(property_names);
        } else {
            _logger.debug("Case #2 ");
            propertyNames = new LocalNameList();
            propertyTypes = getAllPropertyTypes();
            if (excludeDesignation) {
                _logger.debug("Case #3 ");
                propertyTypes = getAllNonPresentationPropertyTypes();
            }
        }
        _logger.debug("searchByProperties scheme: " + scheme);
        _logger.debug("searchByProperties matchAlgorithm: " + matchAlgorithm);
        _logger.debug("searchByProperties source: " + source);
        _logger.debug("searchByProperties excludeDesignation: "
            + excludeDesignation);

/*
        if (source != null)
            sourceList =
                ConvenienceMethods.createLocalNameList(new String[] { source });
*/
        NameAndValueList qualifierList = null;

        String matchText0 = matchText;
        String matchAlgorithm0 = matchAlgorithm;
        matchText0 = matchText0.trim();
        /*
        if (matchText == null || matchText.length() == 0) {
            return null;
        }
        */
        if (matchText.length() == 0) {
            return null;
        }
        matchText = matchText.trim();
        if (matchAlgorithm.compareToIgnoreCase("contains") == 0) {
            matchAlgorithm = findBestContainsAlgorithm(matchText);
        }

        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;
        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }

            cns = null;
            iterator = null;
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            if (version != null)
                versionOrTag.setVersion(version);

            try {
				/*
                if (lbSvc == null) {
                    _logger.warn("lbSvc = null");
                    return null;
                }
                */

                cns = lbSvc.getNodeSet(scheme, versionOrTag, null);

                if (cns != null) {
                    try {
                        String language = null;
                        try {
                            _logger
                                .debug("*** restrictToMatchingProperties ...matchText "
                                    + matchText
                                    + " matchAlgorithm "
                                    + matchAlgorithm);
                            cns =
                                cns.restrictToMatchingProperties(propertyNames,
                                    propertyTypes, matchText, matchAlgorithm,
                                    language);

                        } catch (Exception e) {
                            _logger
                                .debug("\t(*) restrictToMatchingProperties throws exceptions???: "
                                    + matchText
                                    + " matchAlgorithm: "
                                    + matchAlgorithm);
                            // e.printStackTrace();
                        }
                        try {
                            _logger.debug("restrictToSource ...source "
                                + source);
                            cns = restrictToSource(cns, source);
                        } catch (Exception e) {
                            _logger
                                .error("\t(*) restrictToSource throws exceptions???: "
                                    + matchText
                                    + " matchAlgorithm: "
                                    + matchAlgorithm);
                            // e.printStackTrace();
                        }

                    } catch (Exception ex) {
                        // ex.printStackTrace();
                        _logger
                            .error("\t(*) searchByProperties2 throws exceptions.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // return null;
            }
            iterator = null;
            if (cns == null) {
                return null;
            }

            // LocalNameList restrictToProperties = null;//new LocalNameList();
            boolean resolveConcepts = false;
            SortOptionList sortCriteria = null;
            if (ranking) {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "matchToQuery" });

            } else {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "entityDescription" }); // code
                _logger.debug("*** Sort alphabetically...");
                resolveConcepts = false;
            }
            try {
                try {
                    long ms = System.currentTimeMillis(), delay = 0;
                    cns = restrictToSource(cns, source);
                    _logger.debug("cns.resolve ...");
                    iterator =
                        cns.resolve(sortCriteria, null, null, null,
                            resolveConcepts);

                } catch (Exception e) {
                    _logger.error("Method: SearchUtil.searchByProperties");
                    _logger.error("* ERROR: cns.resolve throws exceptions.");
                    _logger.error("* " + e.getClass().getSimpleName() + ": "
                        + e.getMessage());
                    e.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (!excludeDesignation) {
            int lcv = 0;
            int iterator_size = 0;
            if (iterator != null) {
                try {
                    iterator_size = iterator.numberRemaining();

                    _logger.debug("Number of matches: " + iterator_size);
                } catch (Exception ex) {
					ex.printStackTrace();
                }
            }

            if (iterator_size == 0) {
                iterator =
                    matchConceptCode(scheme, version, matchText0, source,
                        "LuceneQuery");
                if (iterator != null) {
                    try {
                        iterator_size = iterator.numberRemaining();
                    } catch (Exception ex) {

                    }
                }
            }
        }
        return new ResolvedConceptReferencesIteratorWrapper(iterator);
    }

    public ResolvedConceptReferencesIteratorWrapper searchByProperties(
        String scheme, String version, String matchText, String source,
        String matchAlgorithm, boolean excludeDesignation, boolean ranking,
        int maxToReturn) {
        return searchByProperties(scheme, version, matchText, null, null,
            source, matchAlgorithm, excludeDesignation, ranking, maxToReturn);

    }

    private CodedNodeSet.PropertyType[] getAllPropertyTypes() {
        CodedNodeSet.PropertyType[] propertyTypes =
            new CodedNodeSet.PropertyType[4];
        propertyTypes[0] = PropertyType.COMMENT;
        propertyTypes[1] = PropertyType.DEFINITION;
        propertyTypes[2] = PropertyType.GENERIC;
        propertyTypes[3] = PropertyType.PRESENTATION;
        return propertyTypes;
    }

    private CodedNodeSet.PropertyType[] getAllNonPresentationPropertyTypes() {
        CodedNodeSet.PropertyType[] propertyTypes =
            new CodedNodeSet.PropertyType[3];
        propertyTypes[0] = PropertyType.COMMENT;
        propertyTypes[1] = PropertyType.DEFINITION;
        propertyTypes[2] = PropertyType.GENERIC;
        return propertyTypes;
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Search by matching ALL relationships
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public CodedNodeGraph getRestrictedCodedNodeGraph(LexBIGService lbSvc,
        String scheme, String version, String associationName,
        CodedNodeSet cns, int direction) {
        CodedNodeGraph cng = null;
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);

        try {
            cng = lbSvc.getNodeGraph(scheme, versionOrTag, null);
            if (associationName != null) {
                NameAndValueList asso_list =
                    createNameAndValueList(new String[] { associationName },
                        null);
                cng = cng.restrictToAssociations(asso_list, null);
            }
            if (cns != null) {
                if (direction == -1) {
                    cng = cng.restrictToSourceCodes(cns);
                } else if (direction == 1) {
                    cng = cng.restrictToTargetCodes(cns);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cng;
    }

    protected static NameAndValueList createNameAndValueList(Vector names,
        Vector values) {
        if (names == null)
            return null;
        NameAndValueList nvList = new NameAndValueList();
        for (int i = 0; i < names.size(); i++) {
            String name = (String) names.elementAt(i);
            String value = null;
            if (values != null && values.size() > i) {
                value = (String) values.elementAt(i);
            }
            NameAndValue nv = new NameAndValue();
            nv.setName(name);
            if (value != null) {
                nv.setContent(value);
            }
            nvList.addNameAndValue(nv);
        }
        return nvList;
    }

    public static NameAndValueList createNameAndValueList(String[] names,
        String[] values) {
        NameAndValueList nvList = null;
        if (names != null && names.length > 0) {
            nvList = new NameAndValueList();
            for (int i = 0; i < names.length; i++) {
                NameAndValue nv = new NameAndValue();
                nv.setName(names[i]);
                if (values != null) {
                    nv.setContent(values[i]);
                }
                nvList.addNameAndValue(nv);
            }
        }
        return nvList;
    }

    public ResolvedConceptReferencesIteratorWrapper searchByAssociations(
        String scheme, String version, String matchText, String source,
        String matchAlgorithm, boolean designationOnly, boolean ranking,
        int maxToReturn) {
        return searchByAssociations(scheme, version, matchText, null, null,
            null, Constants.SEARCH_SOURCE, source, matchAlgorithm,
            designationOnly, ranking, maxToReturn);
    }

    public ResolvedConceptReferencesIteratorWrapper searchByAssociations(
        String scheme, String version, String matchText,
        String[] associationsToNavigate, String[] association_qualifier_names,
        String[] association_qualifier_values, int search_direction,
        String source, String matchAlgorithm, boolean designationOnly,
        boolean ranking, int maxToReturn) {

		if (matchText == null) return null;

        NameAndValueList associationList = null;
        if (associationsToNavigate != null) {
            associationList =
                createNameAndValueList(associationsToNavigate, null);
        }
        NameAndValueList qualifiers = null;
        if (association_qualifier_names != null) {
            qualifiers =
                createNameAndValueList(association_qualifier_names,
                    association_qualifier_values);
        }

        //String matchText0 = matchText;
        String matchAlgorithm0 = matchAlgorithm;
        //matchText0 = matchText0.trim();
        String message = null;

        long ms = System.currentTimeMillis();
        long dt = 0;
        long total_delay = 0;
        boolean timeout = false;

        boolean preprocess = true;
        //if (matchText == null || matchText.length() == 0) {
		if (matchText.length() == 0) {
            return null;
        }

        matchText = matchText.trim();
        if (matchAlgorithm.compareToIgnoreCase("contains") == 0) {
            matchAlgorithm = findBestContainsAlgorithm(matchText);
        }

        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;

        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }

            Vector cns_vec = new Vector();

            cns = null;
            iterator = null;
            _logger.debug("\tsearching " + scheme);

            ms = System.currentTimeMillis();
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            if (version != null)
                versionOrTag.setVersion(version);

            try {
				/*
                if (lbSvc == null) {
                    _logger.warn("lbSvc = null");
                    return null;
                }
                */

                cns = lbSvc.getNodeSet(scheme, versionOrTag, null);

                if (cns != null) {
                    try {
                        // find cns
                        if (designationOnly) {
                            cns =
                                cns.restrictToMatchingDesignations(matchText,
                                    null, matchAlgorithm, null);
                        }
                        cns = restrictToSource(cns, source);
                        int resolveAssociationDepth = 1;
                        ConceptReference graphFocus = null;
                        if (cns != null) {
                            cns_vec.add(cns);
                        }
                        // }
                    } catch (Exception ex) {
                        // return null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // return null;
            }

            dt = System.currentTimeMillis() - ms;
            ms = System.currentTimeMillis();
            total_delay = total_delay + dt;

            //if (total_delay > NCImBrowserProperties.getPaginationTimeOut() * 60 * 1000) {
			if (total_delay > 1000L * NCImBrowserProperties.getPaginationTimeOut() * 60) {
                message =
                    "WARNING: Search is incomplete -- please enter more specific search criteria.";
            }

            iterator = null;
            if (cns_vec.size() == 0)
                return null;

            LocalNameList restrictToProperties = null;// new LocalNameList();
            boolean resolveConcepts = false;

            SortOptionList sortCriteria = null;

            if (ranking) {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "matchToQuery" });

            } else {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "entityDescription" }); // code
            }
            resolveConcepts = false;
            try {
                try {
                    boolean resolveForward = true;
                    boolean resolveBackward = true;

                    if (search_direction == Constants.SEARCH_SOURCE) {
                        resolveForward = false;
                        resolveBackward = true;
                    } else if (search_direction == Constants.SEARCH_TARGET) {
                        resolveForward = true;
                        resolveBackward = false;
                    }

                    int resolveAssociationDepth = 1;
                    ResolvedConceptReferencesIterator quickUnionIterator =
                        new QuickUnionIterator(cns_vec, sortCriteria, null,
                            restrictToProperties, null, resolveConcepts);
                    if (associationsToNavigate == null && qualifiers == null) {
                        iterator =
                            new SearchByAssociationIteratorDecorator(
                                quickUnionIterator, resolveForward,
                                resolveBackward, resolveAssociationDepth,
                                maxToReturn);
                    } else {
                        iterator =
                            new SearchByAssociationIteratorDecorator(
                                quickUnionIterator, resolveForward,
                                resolveBackward, associationList, qualifiers,
                                resolveAssociationDepth, maxToReturn);
                    }
                } catch (Exception e) {
                    _logger.error("Method: SearchUtil.searchByAssociations");
                    _logger.error("* ERROR: cns.resolve throws exceptions.");
                    _logger.error("* " + e.getClass().getSimpleName() + ": "
                        + e.getMessage());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            _logger
                .debug("Run time (ms): " + (System.currentTimeMillis() - ms));
        }

        if (iterator == null) {
            _logger
                .warn("=================== searchByAssociations returns iterator == null???");
        }

        return new ResolvedConceptReferencesIteratorWrapper(iterator, message);
    }

    private CodedNodeSet.PropertyType[] getAllPropertypes() {
        CodedNodeSet.PropertyType[] propertyTypes =
            new CodedNodeSet.PropertyType[4];
        propertyTypes[0] = PropertyType.COMMENT;
        propertyTypes[1] = PropertyType.DEFINITION;
        propertyTypes[2] = PropertyType.GENERIC;
        propertyTypes[3] = PropertyType.PRESENTATION;
        return propertyTypes;
    }

    private ResolvedConceptReferencesIterator filterOutAnonymousClasses(
        LexBIGService lbSvc, String scheme, CodedNodeSet cns,
        ResolvedConceptReferencesIterator iterator) {

        int maxReturn = 100;
        ConceptReferenceList codeList = new ConceptReferenceList();
        int knt = 0;
        int knt_concept = 0;

        try {
            if (iterator == null || iterator.numberRemaining() == 0)
                return iterator;

            while (iterator.hasNext()) {
                ResolvedConceptReference[] refs =
                    iterator.next(maxReturn).getResolvedConceptReference();
                for (ResolvedConceptReference ref : refs) {
                    String code = ref.getConceptCode();
                    knt++;
                    _logger.debug("(" + knt + ") code: " + code);
                    if (code.indexOf("@") == -1) {
                        codeList.addConceptReference(ref);
                        knt_concept++;
                    }
                }
            }

            cns = lbSvc.getNodeSet(scheme, null, null);
            cns = cns.restrictToCodes(codeList);
            SortOptionList sortCriteria = null;
            LocalNameList restrictToProperties = new LocalNameList();
            boolean resolveConcepts = false;
            iterator =
                cns.resolve(sortCriteria, null, restrictToProperties, null,
                    resolveConcepts);
            return iterator;
        } catch (Exception ex) {

        }
        return null;
    }

    private CodedNodeSet filterOutAnonymousClasses(LexBIGService lbSvc,
        String scheme, CodedNodeSet cns) {
        if (cns == null)
            return cns;

        SortOptionList sortCriteria = null;
        LocalNameList restrictToProperties = new LocalNameList();
        boolean resolveConcepts = false;

        int maxReturn = 100;
        ConceptReferenceList codeList = new ConceptReferenceList();
        int knt = 0;
        int knt_concept = 0;

        try {
            ResolvedConceptReferencesIterator iterator =
                cns.resolve(sortCriteria, null, restrictToProperties, null,
                    resolveConcepts);

            while (iterator.hasNext()) {
                ResolvedConceptReference[] refs =
                    iterator.next(maxReturn).getResolvedConceptReference();
                for (ResolvedConceptReference ref : refs) {
                    String code = ref.getConceptCode();
                    knt++;
                    if (code.indexOf("@") == -1) {
                        codeList.addConceptReference(ref);
                        knt_concept++;
                    }
                }
            }
            if (knt_concept == 0)
                return null;
            cns = lbSvc.getNodeSet(scheme, null, null);
            cns = cns.restrictToCodes(codeList);

            return cns;
        } catch (Exception ex) {

        }
        return null;
    }

    private String findBestContainsAlgorithm(String matchText) {
        if (matchText == null)
            return "nonLeadingWildcardLiteralSubString";
        matchText = matchText.trim();
        if (matchText.length() == 0)
            return "nonLeadingWildcardLiteralSubString"; // or null
        if (matchText.length() > 1)
            return "nonLeadingWildcardLiteralSubString";
        char ch = matchText.charAt(0);
        if (Character.isDigit(ch))
            return "literal";
        else if (Character.isLetter(ch))
            return "LuceneQuery";
        else
            return "literalContains";
    }

    public static LocalNameList getPropertyLocalNameList(String codingSchemeName) {
        if (_propertyLocalNameListHashMap == null) {
            _propertyLocalNameListHashMap = new HashMap();
        }
        if (!_propertyLocalNameListHashMap.containsKey(codingSchemeName)) {
            try {
                LocalNameList lnl = getAllPropertyNames(codingSchemeName);
                _propertyLocalNameListHashMap.put(codingSchemeName, lnl);
                return lnl;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            return (LocalNameList) _propertyLocalNameListHashMap
                .get(codingSchemeName);
        }
        return null;
    }

    public static LocalNameList getAllPropertyNames(String codingSchemeName)
            throws Exception {
        LocalNameList propertyNames = new LocalNameList();
        LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
        CodingScheme cs = lbSvc.resolveCodingScheme(codingSchemeName, null);
        for (SupportedProperty prop : cs.getMappings().getSupportedProperty()) {
            propertyNames.addEntry(prop.getLocalId());
        }
        return propertyNames;
    }

    public static ResolvedConceptReferencesIterator matchConceptCode(
        String scheme, String version, String code) {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        //Vector v = new Vector();
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        if (version != null)
            versionOrTag.setVersion(version);
        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;
        try {
            cns = lbSvc.getNodeSet(scheme, versionOrTag, null);
            CodedNodeSet.PropertyType[] propertyTypes = null;
            ConceptReferenceList crefs =
                createConceptReferenceList(new String[] { code }, scheme);
            try {
                cns = lbSvc.getCodingSchemeConcepts(scheme, versionOrTag);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            if (cns == null) {
                _logger
                    .warn("getConceptByCode getCodingSchemeConcepts returns null??? "
                        + scheme);
                return null;
            }

            cns = cns.restrictToCodes(crefs);
            LocalNameList restrictToProperties = new LocalNameList();
            SortOptionList sortCriteria = null;
            try {
                boolean resolveConcepts = false;
                try {
                    long ms = System.currentTimeMillis(), delay = 0;
                    iterator =
                        cns.resolve(sortCriteria, null, restrictToProperties,
                            null, resolveConcepts);

                    //int size = iterator.numberRemaining();
                } catch (Exception e) {
                    _logger.error("Method: SearchUtil.matchConceptCode");
                    _logger.error("* ERROR: cns.resolve throws exceptions.");
                    _logger.error("* " + e.getClass().getSimpleName() + ": "
                        + e.getMessage());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        } catch (Exception ex) {
            _logger.error("WARNING: searchByCode throws exception.");
        }
        // [#26386] Need app to be able to distinguish/prioritize/display
        // matching code vrs. matching name results

        return iterator;
    }

    public static Entity getConceptInIterator(
        ResolvedConceptReferencesIterator iterator, int idx) {
        if (iterator == null)
            return null;
        try {
            int numRemaining = iterator.numberRemaining();
            if (idx >= numRemaining)
                return null;

            int lcv = -1;
            while (iterator.hasNext()) {
                ResolvedConceptReference[] refs =
                    iterator.next(100).getResolvedConceptReference();
                for (ResolvedConceptReference ref : refs) {
                    lcv++;
                    if (lcv == idx) {
                        return ref.getReferencedEntry();
                    }
                }
            }
        } catch (Exception e) {
            _logger.error("Method: SearchUtil.matchConceptByCode");
            _logger.error("* ERROR: cns.resolve throws exceptions.");
            _logger.error("* " + e.getClass().getSimpleName() + ": "
                + e.getMessage());
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////

    public CodedNodeGraph getRestrictedCodedNodeGraph(LexBIGService lbSvc,
        String scheme, String version, String rel, String rela,
        CodedNodeSet cns, int direction) {
        CodedNodeGraph cng = null;
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);

        _logger
            .warn("getRestrictedCodedNodeGraph rel: " + rel);
        _logger
            .warn("getRestrictedCodedNodeGraph rela: " + rela);

        try {
            cng = lbSvc.getNodeGraph(scheme, versionOrTag, null);
            NameAndValueList asso_list = null;
            String [] associationsToNavigate = null;

            if (rel == null) {
                Vector w = OntologyBean.getAssociationNames();
                if (w == null || w.size() == 0) {
                    _logger
                        .warn("OntologyBean.getAssociationNames() returns null, or nothing???");
                } else {
                    associationsToNavigate = new String[w.size()];
                    for (int i = 0; i < w.size(); i++) {
                        String nm = (String) w.elementAt(i);
                        associationsToNavigate[i] = nm;
                    }
                }
            } else {
                associationsToNavigate = new String[] { rel };
            }
            asso_list = createNameAndValueList(associationsToNavigate, null);

            NameAndValueList qualifier_list = null;
            if (rela != null) {
                qualifier_list = createNameAndValueList(new String[] { "rela" }, new String[] { rela });
            }

            cng = cng.restrictToAssociations(asso_list, qualifier_list);

            if (cns != null) {
                if (direction == -1) {
                    _logger
                        .warn("restrictToSourceCodes... ");

                    cng = cng.restrictToSourceCodes(cns);
                } else if (direction == 1) {
                    _logger
                        .warn("restrictToTargetCodes... ");


                    cng = cng.restrictToTargetCodes(cns);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cng;
    }

    public ResolvedConceptReferencesIterator codedNodeGraph2CodedNodeSetIterator(
        CodedNodeGraph cng, CodedNodeSet cns_to_remove, ConceptReference graphFocus,
        boolean resolveForward, boolean resolveBackward,
        int resolveAssociationDepth, int maxToReturn) {
        CodedNodeSet cns = null;
        try {
            _logger.debug("codedNodeGraph2CodedNodeSetIterator cng.toNodeList");
            cns =
                cng.toNodeList(graphFocus, resolveForward, resolveBackward,
                    resolveAssociationDepth, maxToReturn);

            _logger.debug("exit codedNodeGraph2CodedNodeSetIterator cng.toNodeList");
            if (cns == null) {
                _logger.warn("cng.toNodeList returns null???");
                return null;
            }

            cns = cns.difference(cns_to_remove);

            SortOptionList sortCriteria = null;

            LocalNameList propertyNames = null;
            CodedNodeSet.PropertyType[] propertyTypes = null;
            ResolvedConceptReferencesIterator iterator = null;
            try {
                iterator =
                    cns.resolve(sortCriteria, propertyNames, propertyTypes);

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (iterator == null) {
                _logger.warn("cns.resolve returns null???");
            }
            return iterator;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ResolvedConceptReferencesIteratorWrapper searchByRELA(String scheme,
        String version, String matchText, String source, String matchAlgorithm,
        String rel, String rela, int maxToReturn) {

        if (matchText == null || matchText.length() == 0)
            return null;

        matchText = matchText.trim();
        if (matchAlgorithm.compareToIgnoreCase("contains") == 0) {
            matchAlgorithm = findBestContainsAlgorithm(matchText);
        }

        ResolvedConceptReferencesIterator iterator = null;
        CodedNodeSet cns = null;
        CodedNodeSet.PropertyType[] propertyTypes =
            new CodedNodeSet.PropertyType[1];
        propertyTypes[0] = PropertyType.PRESENTATION;
        LexBIGService lbSvc = null;
        try {
            lbSvc = new RemoteServerUtil().createLexBIGService();
            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            if (version != null)
                versionOrTag.setVersion(version);

            cns = lbSvc.getNodeSet(scheme, versionOrTag, null);
            if (cns == null) {
                _logger.warn("cns = null");
                return null;
            }

            try {
                LocalNameList sourceList = null;
                if (source != null
                    && source.compareToIgnoreCase("ALL") != 0) {
                    sourceList = new LocalNameList();
                    sourceList.addEntry(source);
                }

                cns =
                    cns.restrictToMatchingDesignations(matchText,
                        SearchDesignationOption.ALL, matchAlgorithm, null);

                if(source != null && source.compareTo("ALL") != 0) {
                    cns =
                        cns.restrictToProperties(null, propertyTypes,
                            sourceList, null, null);
                }

                try {
                    ResolvedConceptReferencesIterator it = cns.resolve(null, null, null, null, false);
                    int num = it.numberRemaining();
                    if (num > 1000) {
                        System.out.println("Matching target concepts size: " + num + " > 1000 -- method aborts.");
                        return null;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } catch (Exception ex) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        if (cns == null) {
            return null;
        }

        try {
            CodedNodeGraph cng = getRestrictedCodedNodeGraph(lbSvc,
                scheme, version, rel, rela, cns, 1);

            if (cng == null) {
                return null;
            }

            boolean resolveForward = false;
            boolean resolveBackward = true;
            int resolveAssociationDepth = 0;

            System.out.println("Calling resolveCodedNodeGraph");
            iterator = resolveCodedNodeGraph(lbSvc, scheme, version, cng, RESOLVE_SOURCE, -1);

            if (iterator != null) {
                return new ResolvedConceptReferencesIteratorWrapper(iterator);
            }


        } catch (Exception ex) {

        }
        return null;
    }

    public ResolvedConceptReferencesIterator resolveCodedNodeGraph(LexBIGService lbSvc, String scheme, String version,
        CodedNodeGraph cng, int direction, int maxToReturn)
    {
        ConceptReferenceList codeList = new ConceptReferenceList();
        try {
            ResolvedConceptReferenceList rcrl = null;
            try {
                boolean resolveForward = true;
                boolean resolveBackward = false;
                int resolveAssociationDepth = 0;
                int resolveCodedEntryDepth = 0;

                rcrl = cng.resolveAsList(null, resolveForward, resolveBackward, // graphFocus, resolveForward, resolveBackward
                    resolveCodedEntryDepth, resolveAssociationDepth, null, null, null, maxToReturn);

            } catch (Exception ex) {
                System.out.println("Exception thrown -- cng.resolveAsList.");
                return null;
            }

            ResolvedConceptReference[] rcrArray = rcrl.getResolvedConceptReference();
            Entity ce = null;

            for (int i=0; i<rcrArray.length; i++)
            {
                if (direction == RESOLVE_SOURCE)
                {
                    codeList.addConceptReference(rcrArray[i]);

                }
                else if (direction == RESOLVE_TARGET)
                {
                    AssociationList al = rcrArray[i].getSourceOf();
                    Association[] assos = al.getAssociation();
                    if (assos != null)
                    {
                        for (int j=0; j<assos.length; j++)
                        {
                            Association asso = (Association) assos[j];
                            AssociatedConceptList acl = asso.getAssociatedConcepts();
                            AssociatedConcept[] ac_array = acl.getAssociatedConcept();
                            for (int k=0; k<ac_array.length; k++)
                            {
                                AssociatedConcept ac = (AssociatedConcept) ac_array[k];
                                codeList.addConceptReference(ac);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CodedNodeSet cns = null;
        try {
            CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
            if (version != null)
                versionOrTag.setVersion(version);

            cns = lbSvc.getNodeSet(scheme, versionOrTag, null);
            cns = cns.restrictToCodes(codeList);
            if (cns == null) {
                return null;
            }
            ResolvedConceptReferencesIterator it = cns.resolve(null, null, null, null, false);
            return it;
        } catch (Exception ex) {

        }
        return null;
    }

    // ///////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        //SearchUtils test = new SearchUtils();
    }
}
