package gov.nih.nci.evs.browser.utils;

import java.util.*;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.commonTypes.*;
import org.LexGrid.concepts.*;
import org.apache.commons.lang.*;
import org.apache.log4j.*;

import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;

import org.LexGrid.lexevs.metabrowser.*;
import org.LexGrid.lexevs.metabrowser.MetaBrowserService.*;
import org.LexGrid.lexevs.metabrowser.model.*;

import java.util.Map;
import java.util.Map.Entry;

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

public class MetaTreeUtils {
    private static Logger _logger = Logger.getLogger(MetaTreeUtils.class);
    private static final String[] _hierAssocToParentNodes =
        new String[] { "PAR", "isa", "branch_of", "part_of", "tributary_of" };

    private static final String[] _hierAssociationToParentNodes = new String[] { "PAR" };

    // static String[] _hierAssocToChildNodes = new String[] { "CHD",
    // "hasSubtype" };
    private static final String[] _hierAssocToChildNodes = new String[] { "CHD" };
    private static SortOptionList _sortByCode =
        Constructors.createSortOptionList(new String[] { "code" });

    private LocalNameList _noopList = Constructors.createLocalNameList("_noop_");
    private LexBIGServiceConvenienceMethods _lbscm = null;
    private LexBIGService _lbsvc = null;

    private static final String NCI_META_THESAURUS = "NCI Metathesaurus";
    private static final String NCI_SOURCE = "NCI";

    private static boolean _resolveConcept = false;// true;

    // KLO, 020210
    private static String _nciThesaurusCui = "C1140168";

    public MetaTreeUtils() {
    }

    public static String[] getHierAssociationToParentNodes() {
		return Arrays.copyOf(_hierAssociationToParentNodes,_hierAssociationToParentNodes.length);
	}

    public static String[] getHierAssociationToChildNodes() {
		return Arrays.copyOf(_hierAssocToChildNodes, _hierAssocToChildNodes.length);
	}

    // /////////////////
    // Source Roots //
    // /////////////////

    /**
     * Finds the root node of a given sab.
     *
     * @param sab
     * @throws Exception
     */

    /*
     * public static List getSourceHierarchyRoots( String scheme,
     * CodingSchemeVersionOrTag csvt, String sab) throws LBException { ArrayList
     * list = new ArrayList(); ResolvedConceptReferenceList rcrl = null; try {
     * rcrl = getSourceRoots(sab); for (int i=0;
     * i<rcrl.getResolvedConceptReferenceCount(); i++) {
     * ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(i);
     * list.add(rcr); } SortUtils.quickSort(list); return list; } catch
     * (Exception ex) {
     *
     * } return new ArrayList(); }
     */

    public List getSourceHierarchyRoots(String scheme,
        CodingSchemeVersionOrTag csvt, String sab) throws LBException {
        try {
            String code = "C1140168";
            HashMap hmap = getSubconcepts(code, sab, "CHD", true);

            ArrayList list = new ArrayList();
            TreeItem ti = (TreeItem) hmap.get(code);
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

    /*
     *
     * public void getRoots(String sab) throws Exception {
     * ResolvedConceptReference root =
     * resolveReferenceGraphForward(getCodingSchemeRoot(sab)); AssociationList
     * assocList = root.getSourceOf(); for(Association assoc :
     * assocList.getAssociation()){ for(AssociatedConcept ac :
     * assoc.getAssociatedConcepts().getAssociatedConcept()){
     * if(isSabQualifiedAssociation(ac, sab)){ displayRoot(ac); } } } }
     */

    /*
     * public ResolvedConceptReferenceList getSourceRoots(String sab) throws
     * Exception { ResolvedConceptReferenceList rcrl = new
     * ResolvedConceptReferenceList(); ResolvedConceptReference root =
     * resolveReferenceGraphForward(getCodingSchemeRoot(sab)); AssociationList
     * assocList = root.getSourceOf(); if (assocList != null) { for(Association
     * assoc : assocList.getAssociation()){ for(AssociatedConcept ac :
     * assoc.getAssociatedConcepts().getAssociatedConcept()){ if
     * (RESOLVE_CONCEPT) { if(isSabQualifiedAssociation(ac, sab)){
     * ResolvedConceptReference r = new ResolvedConceptReference();
     * EntityDescription entityDescription = new EntityDescription();
     * entityDescription.setContent(ac.getEntityDescription().getContent());
     * r.setEntityDescription(entityDescription); r.setCode(ac.getCode());
     * rcrl.addResolvedConceptReference(r); } } else {
     *
     * ResolvedConceptReference r = new ResolvedConceptReference();
     * EntityDescription entityDescription = new EntityDescription();
     * entityDescription.setContent(ac.getEntityDescription().getContent());
     * r.setEntityDescription(entityDescription); r.setCode(ac.getCode());
     * rcrl.addResolvedConceptReference(r); } } } } return rcrl; }
     */

    public static ResolvedConceptReferenceList getSourceRoots(String sab)
            throws Exception {
        ResolvedConceptReferenceList rcrl = new ResolvedConceptReferenceList();
        ResolvedConceptReference root = null;
        try {
            ResolvedConceptReference ref = getCodingSchemeRoot(sab);
            root = resolveReferenceGraphForward(ref);
        } catch (Exception ex) {
            ex.printStackTrace();
            return rcrl;
        }
        AssociationList assocList = root.getSourceOf();
        if (assocList != null) {
            for (Association assoc : assocList.getAssociation()) {
                for (AssociatedConcept ac : assoc.getAssociatedConcepts()
                    .getAssociatedConcept()) {
                    if (isSabQualifiedAssociation(ac, sab)) {
                        ResolvedConceptReference r =
                            new ResolvedConceptReference();
                        EntityDescription entityDescription =
                            new EntityDescription();
                        entityDescription.setContent(ac.getEntityDescription()
                            .getContent());
                        r.setEntityDescription(entityDescription);
                        r.setCode(ac.getCode());
                        rcrl.addResolvedConceptReference(r);
                    }
                }
            }
        }
        return rcrl;
    }

    /**
     * Displays the root node.
     *
     * @param ac
     */
    protected void displayRoot(AssociatedConcept ac) {
        _logger.debug(ac.getCode() + " - "
            + ac.getEntityDescription().getContent());
    }

    /**
     * Gets the UMLS root node of a given SAB.
     *
     * @param sab
     * @return
     * @throws LBException
     */
    private static ResolvedConceptReference getCodingSchemeRoot(String sab)
            throws LBException {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        CodedNodeSet cns =
            lbSvc.getCodingSchemeConcepts(NCI_META_THESAURUS, null);

        PropertyType[] propertytypes = null;
        // if (RESOLVE_CONCEPT) {
        propertytypes = new PropertyType[] { PropertyType.PRESENTATION };
        // }
        cns =
            cns.restrictToProperties(null, propertytypes, Constructors
                .createLocalNameList("SRC"), null, Constructors
                .createNameAndValueList("source-code", "V-" + sab));
        ResolvedConceptReference[] refs =
            cns.resolveToList(null, null, propertytypes, -1)
                .getResolvedConceptReference();

        if (refs.length > 1) {
            throw new LBException("Found more than one Root for SAB: " + sab);
        }
        if (refs.length == 0) {
            throw new LBException("Didn't find a Root for SAB: " + sab);
        }
        return refs[0];
    }

    /**
     * Resolve the relationships of a ResolvedConceptReference forward one
     * level.
     *
     * @param ref
     * @return
     * @throws Exception
     */
    private static ResolvedConceptReference resolveReferenceGraphForward(
        ResolvedConceptReference ref) throws Exception {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        CodedNodeGraph cng = lbSvc.getNodeGraph(NCI_META_THESAURUS, null, null);
        // cng =
        // cng.restrictToAssociations(Constructors.createNameAndValueList(new
        // String[]{"CHD", "hasSubtype"}), null);
        cng =
            cng.restrictToAssociations(Constructors
                .createNameAndValueList(new String[] { "CHD" }), null);
        // cng = cng.restrictToAssociations(null,
        // Constructors.createNameAndValueList(sab, SOURCE));
        ResolvedConceptReference[] refs =
            cng.resolveAsList(ref, true, false, 1, 1, null, null, null, -1)
                .getResolvedConceptReference();
        return refs[0];
    }

    /**
     * Determines whether or not the given reference is a root Concept for the
     * given Coding Scheme.
     *
     * @param reference
     * @param sourceCodingScheme
     * @return
     */
    private static boolean isSabQualifiedAssociation(AssociatedConcept ac,
        String sab) {
        NameAndValue[] nvl = ac.getAssociationQualifiers().getNameAndValue();
        for (NameAndValue nv : nvl) {
            if (nv.getName().equals("source") && nv.getContent().equals(sab)) {
                return true;
            }
        }
        return false;
    }

    // ///////////////////
    // Tree
    // ///////////////////
    private static void Util_displayMessage(String s) {
        _logger.debug(s);
    }

    private static void Util_displayAndLogError(String s, Exception e) {
        _logger.debug(s);
    }

    /**
     * Process the provided code, constraining relationships to the given source
     * abbreviation.
     *
     * @throws LBException
     */
    public void run(String cui, String sab) throws LBException {
        // Resolve the coding scheme.
        /*
         * CodingSchemeSummary css = Util.promptForCodeSystem(); if (css ==
         * null) return;
         *
         * String scheme = css.getCodingSchemeURI();
         */
        String scheme = "NCI Metathesaurus";
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        // csvt.setVersion(css.getRepresentsVersion());

        // Resolve the requested concept.
        ResolvedConceptReference rcr = resolveConcept(scheme, csvt, cui);
        if (rcr == null) {
            Util_displayMessage("Unable to resolve a concept for CUI = '" + cui
                + "'");
            return;
        }

        // Print a header for the item being processed.
        Util_displayMessage("============================================================");
        Util_displayMessage("Concept Information");
        ;
        Util_displayMessage("============================================================");
        printHeader(rcr, sab);

        // Print the hierarchies for the requested SAB.
        Util_displayMessage("");
        Util_displayMessage("============================================================");
        Util_displayMessage("Hierarchies applicable for CUI " + cui
            + " for SAB " + sab);
        Util_displayMessage("============================================================");
        TreeItem ti = new TreeItem("<Start>", "Start of Tree", null);
        long ms = System.currentTimeMillis();
        int pathsResolved = 0;
        int maxLevel = -1;
        try {
            // Identify the set of all codes on path from root
            // to the focus code ...
            TreeItem[] pathsFromRoot =
                buildPathsToRoot(rcr, scheme, csvt, sab, maxLevel);
            pathsResolved = pathsFromRoot.length;

            for (TreeItem rootItem : pathsFromRoot) {
                dumpTreeItem(rootItem);

                ti.addChild("CHD", rootItem);
            }
            // ti.expandable = true;

        } finally {
            _logger.debug("Run time (milliseconds): "
                + (System.currentTimeMillis() - ms) + " to resolve "
                + pathsResolved + " paths from root.");
        }
        printTree(ti, cui, 0);

        // Print the neighboring CUIs/AUIs for this SAB.
        Util_displayMessage("");
        Util_displayMessage("============================================================");
        Util_displayMessage("Neighboring CUIs and AUIs for CUI " + cui
            + " for SAB " + sab);
        ;
        Util_displayMessage("============================================================");
        printNeighborhood(scheme, csvt, rcr, sab);
    }

    public HashMap getTreePathData(String scheme, String version, String sab,
        String code) throws LBException {
        if (sab == null)
            sab = NCI_SOURCE;
        return getTreePathData(scheme, version, sab, code, -1);
    }

    // ////////////////
    // search_tree
    // ////////////////

    public HashMap getTreePathData(String scheme, String version, String sab,
        String code, int maxLevel) throws LBException {
        if (sab == null)
            sab = NCI_SOURCE;

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
        if (sab == null)
            sab = NCI_SOURCE;
        return getTreePathData(lbsvc, lbscm, scheme, csvt, sab, focusCode, -1);
    }

    /*
     * public HashMap getTreePathData(LexBIGService lbsvc,
     * LexBIGServiceConvenienceMethods lbscm, String scheme,
     * CodingSchemeVersionOrTag csvt, String sab, String cui, int maxLevel)
     * throws LBException { if (sab == null) sab = NCI_SOURCE; HashMap hmap =
     * new HashMap(); long ms = System.currentTimeMillis();
     *
     * ResolvedConceptReference rcr = resolveConcept(scheme, csvt, cui); if (rcr
     * == null) { Util_displayMessage("Unable to resolve a concept for CUI = '"
     * + cui + "'"); return null; } else { _logger.debug("getTreePathData " +
     * rcr.getEntityDescription().getContent()); }
     *
     * // Dummy root (place holder) TreeItem ti = new TreeItem("<Root>",
     * "Root node", null); int pathsResolved = 0; try { // Identify the set of
     * all codes on path from root // to the focus code ... TreeItem[]
     * pathsFromRoot = buildPathsToRoot(rcr, scheme, csvt, sab, maxLevel);
     * pathsResolved = pathsFromRoot.length; for (TreeItem rootItem :
     * pathsFromRoot) { ti.addChild("CHD", rootItem); } ti.expandable = true;
     *
     * } finally { _logger.debug("MetaTreeUtils Run time (milliseconds): " +
     * (System.currentTimeMillis() - ms) + " to resolve " + pathsResolved +
     * " paths from root."); }
     *
     * hmap.put(cui, ti);
     *
     *
     * return hmap; }
     */

    public HashMap getTreePathData(LexBIGService lbsvc,
        LexBIGServiceConvenienceMethods lbscm, String scheme,
        CodingSchemeVersionOrTag csvt, String sab, String cui, int maxLevel)
            throws LBException {
        if (sab == null)
            sab = NCI_SOURCE;
        HashMap hmap = new HashMap();
        long ms = System.currentTimeMillis();

        ResolvedConceptReference rcr = resolveConcept(scheme, csvt, cui);
        if (rcr == null) {
            Util_displayMessage("Unable to resolve a concept for CUI = '" + cui
                + "'");
            return null;
        }

        // Dummy root (place holder)
        TreeItem ti = new TreeItem("<Root>", "Root node", null);
        int pathsResolved = 0;
        try {
            // Identify the set of all codes on path from root
            // to the focus code ...
            TreeItem[] pathsFromRoot =
                buildPathsToRoot(rcr, scheme, csvt, sab, maxLevel);
            // KLO
            pathsResolved = pathsFromRoot.length;

            for (TreeItem rootItem : pathsFromRoot) {
                if (rootItem._text.compareTo("NCI Thesaurus") == 0) {
                    // rootItem is NCI Thesaurus
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
            _logger.debug("MetaTreeUtils Run time (milliseconds): "
                + (System.currentTimeMillis() - ms) + " to resolve "
                + pathsResolved + " paths from root.");
        }

        hmap.put(cui, ti);

        return hmap;
    }

    /**
     * Prints formatted text providing context for the given item including CUI,
     * SAB, AUI, and Text.
     *
     * @throws LBException
     */
    protected void printHeader(ResolvedConceptReference rcr, String sab)
            throws LBException {

        Util_displayMessage("CUI ....... : " + rcr.getConceptCode());
        Util_displayMessage("Description : "
            + StringUtils.abbreviate(rcr.getEntityDescription().getContent(),
                60));
        Util_displayMessage("SAB ....... : " + sab);
        Util_displayMessage("");
        Util_displayMessage("AUIs with this CUI associated for this SAB :");
        for (String line : getAtomText(rcr, sab).split("\\|"))
            Util_displayMessage("  {" + line + '}');
    }

    /**
     * Prints the given tree item, recursing through all branches.
     *
     * @param ti
     */
    public void printTree(TreeItem ti, String focusCode, int depth) {
        StringBuffer indent = new StringBuffer();
        for (int i = 0; i < depth * 2; i++)
            indent.append("| ");

        StringBuffer codeAndText =
            new StringBuffer(indent).append(
                focusCode.equals(ti._code) ? ">" : " ").append(ti._code).append(
                ':').append(StringUtils.abbreviate(ti._text, 60)).append(
                ti._expandable ? " [+]" : "");
        if (ti._auis != null)
            for (String line : ti._auis.split("\\|"))
                codeAndText.append('\n').append(indent).append("    {").append(
                    StringUtils.abbreviate(line, 60)).append('}');
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

    /**
     * Prints formatted text with the CUIs and AUIs of neighboring concepts for
     * the requested SAB.
     *
     * @throws LBException
     */
    protected void printNeighborhood(String scheme,
        CodingSchemeVersionOrTag csvt, ResolvedConceptReference rcr, String sab)
            throws LBException {

        // Resolve neighboring concepts with associations
        // qualified by the SAB.
        CodedNodeGraph neighborsBySource =
            getLexBIGService().getNodeGraph(scheme, csvt, null);
        // neighborsBySource.restrictToAssociations(null,
        // Constructors.createNameAndValueList(sab, "Source"));
        neighborsBySource =
            neighborsBySource.restrictToAssociations(null, Constructors
                .createNameAndValueList("source", sab));
        ResolvedConceptReferenceList nodes =
            neighborsBySource.resolveAsList(rcr, true, true, Integer.MAX_VALUE,
                1, null, new PropertyType[] { PropertyType.PRESENTATION },
                _sortByCode, null, -1);

        List<AssociatedConcept> neighbors = new ArrayList<AssociatedConcept>();
        for (ResolvedConceptReference node : nodes
            .getResolvedConceptReference()) {
            // Process sources and targets ...
            if (node.getSourceOf() != null)
                for (Association assoc : node.getSourceOf().getAssociation())
                    for (AssociatedConcept ac : assoc.getAssociatedConcepts()
                        .getAssociatedConcept())
                        if (isValidForSAB(ac, sab))
                            neighbors.add(ac);
            if (node.getTargetOf() != null)
                for (Association assoc : node.getTargetOf().getAssociation())
                    for (AssociatedConcept ac : assoc.getAssociatedConcepts()
                        .getAssociatedConcept())
                        if (isValidForSAB(ac, sab))
                            neighbors.add(ac);

            // Add to printed output
            for (ResolvedConceptReference neighbor : neighbors) {
                Util_displayMessage(neighbor.getCode()
                    + ':'
                    + StringUtils.abbreviate(neighbor.getEntityDescription()
                        .getContent(), 60));
                for (String line : getAtomText(neighbor, sab).split("\\|"))
                    Util_displayMessage("    {"
                        + StringUtils.abbreviate(line, 60) + '}');
            }
        }
    }

    /**
     * Populate child nodes for a single branch of the tree, and indicates
     * whether further expansion (to grandchildren) is possible.
     */

    // public void addChildren(TreeItem ti, String scheme,
    // CodingSchemeVersionOrTag csvt,
    public TreeItem addChildren(TreeItem ti, String scheme,
        CodingSchemeVersionOrTag csvt, String sab, String code,
        Set<String> codesToExclude, String[] associationsToNavigate,
        boolean associationsNavigatedFwd) throws LBException {

        //long ms = System.currentTimeMillis();
        HashSet hset = new HashSet();

        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            LexBIGServiceConvenienceMethods lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
            String name = getCodeDescription(lbSvc, scheme, csvt, code);
            ti = new TreeItem(code, name);
            ti._expandable = false;

            CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, null, null);

            if (sab != null) {
                cng =
                    cng.restrictToAssociations(Constructors
                        .createNameAndValueList(associationsToNavigate),
                        Constructors.createNameAndValueList("source", sab));
            } else {
                cng =
                    cng.restrictToAssociations(Constructors
                        .createNameAndValueList(associationsToNavigate), null);
            }

            /*
             * CodedNodeSet.PropertyType[] propertyTypes = new
             * CodedNodeSet.PropertyType[1]; propertyTypes[0] =
             * PropertyType.PRESENTATION;
             */
            CodedNodeSet.PropertyType[] propertytypes = null;
            if (_resolveConcept) {
                propertytypes =
                    new PropertyType[] { PropertyType.PRESENTATION };
            }

            // int resolveCodedEntryDepth = 0;
            ResolvedConceptReferenceList branch = null;
            // branch =
            // cng.resolveAsList(Constructors.createConceptReference(code,
            // scheme), !associationsNavigatedFwd, associationsNavigatedFwd,
            // Integer.MAX_VALUE, 2, null, propertyTypes, null, -1);
            branch =
                cng.resolveAsList(Constructors.createConceptReference(code,
                    scheme), associationsNavigatedFwd,
                    associationsNavigatedFwd, Integer.MAX_VALUE, 2, null,
                    propertytypes, null, -1);

            if (branch.getResolvedConceptReferenceCount() > 0) {
                Enumeration<ResolvedConceptReference> refEnum =
                    (Enumeration<ResolvedConceptReference>) branch.enumerateResolvedConceptReference();
                while (refEnum.hasMoreElements()) {
                    ResolvedConceptReference ref = refEnum.nextElement();
                    AssociationList childAssociationList = ref.getSourceOf();
                    if (childAssociationList != null) {
                        // Process each association defining children ...
                        for (Association child : childAssociationList
                            .getAssociation()) {
                            String childNavText =
                                getDirectionalLabel(lbscm, scheme, csvt, child,
                                    associationsNavigatedFwd);
                            // Each association may have multiple children ...
                            AssociatedConceptList branchItemList =
                                child.getAssociatedConcepts();
                            for (AssociatedConcept branchItemNode : branchItemList
                                .getAssociatedConcept()) {
                                if (isValidForSAB(branchItemNode, sab)) {
                                    String branchItemCode =
                                        branchItemNode.getCode();
                                    // Add here if not in the list of excluded
                                    // codes.
                                    // This is also where we look to see if
                                    // another level
                                    // was indicated to be available. If so,
                                    // mark the
                                    // entry with a '+' to indicate it can be
                                    // expanded.

                                    if (!codesToExclude
                                        .contains(branchItemCode)) {
                                        if (!hset.contains(branchItemCode)) {

                                            hset.add(branchItemCode);

                                            TreeItem childItem =
                                                new TreeItem(branchItemCode,
                                                    branchItemNode
                                                        .getEntityDescription()
                                                        .getContent());

                                            childItem._expandable = false;

                                            AssociationList grandchildBranch =
                                                branchItemNode.getSourceOf();
                                            if (grandchildBranch != null) {

                                                for (Association grandchild : grandchildBranch
                                                    .getAssociation()) {
                                                    java.lang.String association_name =
                                                        grandchild
                                                            .getAssociationName();
                                                    AssociatedConceptList grandchildbranchItemList =
                                                        grandchild
                                                            .getAssociatedConcepts();
                                                    for (AssociatedConcept grandchildbranchItemNode : grandchildbranchItemList
                                                        .getAssociatedConcept()) {

                                                        if (isValidForSAB(
                                                            grandchildbranchItemNode,
                                                            sab)) {
                                                            childItem._expandable =
                                                                true;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            ti
                                                .addChild(childNavText,
                                                    childItem);
                                            ti._expandable = true;
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
        return ti;
    }

    /**
     * Returns a resolved concept for the specified code and scheme.
     *
     * @throws LBException
     */
    protected ResolvedConceptReference resolveConcept(String scheme,
        CodingSchemeVersionOrTag csvt, String code) throws LBException {

        CodedNodeSet cns =
            getLexBIGService().getCodingSchemeConcepts(scheme, csvt);
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

    /**
     * Returns a cached instance of a LexBIG service.
     */
    protected LexBIGService getLexBIGService() throws LBException {
        if (_lbsvc == null)
            // lbsvc_ = LexBIGServiceImpl.defaultInstance();
            _lbsvc = RemoteServerUtil.createLexBIGService();
        return _lbsvc;
    }

    /**
     * Returns a cached instance of convenience methods.
     */
    protected LexBIGServiceConvenienceMethods getConvenienceMethods()
            throws LBException {
        if (_lbscm == null)
            _lbscm =
                (LexBIGServiceConvenienceMethods) getLexBIGService()
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
        _lbscm.setLexBIGService(_lbsvc);
        return _lbscm;
    }

    /**
     * Returns the label to display for the given association and directional
     * indicator.
     */
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

    /**
     * Returns a string representing the AUIs and text presentations applicable
     * only for the given source abbreviation (SAB). All AUI text combinations
     * are qualified by SAB and delimited by '|'.
     */
    protected String getAtomText(ResolvedConceptReference rcr, String sab) {
        StringBuffer text = new StringBuffer();
        boolean first = true;
        for (Presentation p : getSourcePresentations(rcr, sab)) {
            if (!first)
                text.append('|');
            text.append(sab).append(':').append(getAtomText(p)).append(':')
                .append('\'').append(p.getValue().getContent()).append('\'');
            first = false;
        }
        return text.length() > 0 ? text.toString() : "<No Match for SAB>";
    }

    /**
     * Returns text for AUI qualifiers for the given property. This method
     * iterates through available property qualifiers. Typically only one AUI is
     * expected. If more are discovered, returned values are delimited by '|'.
     */
    protected String getAtomText(Property prop) {
        StringBuffer text = new StringBuffer();
        boolean first = true;
        for (PropertyQualifier pq : prop.getPropertyQualifier())
            if ("AUI".equalsIgnoreCase(pq.getPropertyQualifierName())) {
                if (!first)
                    text.append('|');
                text.append(pq.getValue().getContent());
                first = false;
            }
        return text.length() > 0 ? text.toString() : "<No AUI>";
    }

    /**
     * Returns all assigned presentations matching the given source abbreviation
     * (SAB). This method iterates through the available presentations to find
     * any qualified to match the specified source.
     */
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

    /**
     * Indicates whether the given associated concept contains a qualifier for
     * the given source abbreviation (SAB).
     *
     * @return true if a qualifier exists; false otherwise.
     */
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

    // //////////////////////
    public HashMap getSubconcepts(String scheme, String version, String code,
        String sab) {
        return getSubconcepts(scheme, version, code, sab, true);
    }

    public int getSubconceptCount(String scheme, String version, String code,
        String sab, String asso_name, boolean direction) {
        // HashMap hmap = getSubconcepts(scheme, null, code, sab, asso_name,
        // direction);
        HashMap hmap =
            getSubconcepts(scheme, version, code, sab, _hierAssocToParentNodes,
                false);
/*
        if (hmap == null)
            return 0;
*/
        Set keyset = hmap.keySet();
        Object[] objs = keyset.toArray();

        if (objs.length == 0) {
            return 0;
        }

        String id = (String) objs[0];
        int knt = 0;
        TreeItem ti = (TreeItem) hmap.get(id);
        for (String association : ti._assocToChildMap.keySet()) {
            List<TreeItem> children = ti._assocToChildMap.get(association);
            knt = knt + children.size();
        }
        return knt;
    }

    public int getSubconceptCountExt(String scheme, String version, String CUI,
        String sab, String asso_name, boolean direction) {
        List<String> par_chd_assoc_list = new ArrayList();
        par_chd_assoc_list.add(asso_name);

        Map<String, List<BySourceTabResults>> map = null;

        LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        MetaBrowserService mbs = null;
        try {
            mbs =
                (MetaBrowserService) lbs
                    .getGenericExtension("metabrowser-extension");
            if (direction) {
                map =
                    mbs.getBySourceTabDisplay(CUI, sab, par_chd_assoc_list,
                        Direction.SOURCEOF);
            } else {
                map =
                    mbs.getBySourceTabDisplay(CUI, sab, par_chd_assoc_list,
                        Direction.TARGETOF);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
        int knt = 0;
        HashSet hset = new HashSet();

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
			Entry thisEntry = (Entry) it.next();
			String rel = (String) thisEntry.getKey();
			List<BySourceTabResults> relations = (List<BySourceTabResults>) thisEntry.getValue();

        //for (String rel : map.keySet()) {
            //List<BySourceTabResults> relations = map.get(rel);

            for (BySourceTabResults result : relations) {
                String code = result.getCui();
                if (code.compareTo(CUI) != 0 && !hset.contains(code)) {
                    hset.add(code);
                }
                String name = result.getTerm();
                // _logger.debug("(***) subconcept: " + name + " " + code);
                knt++;
            }
        }
        return hset.size();
    }

    public static boolean hasSubconcepts(LexBIGService lbs,
        MetaBrowserService mbs, String CUI, String sab, String asso_name,
        boolean direction) {
        List<String> par_chd_assoc_list = new ArrayList();
        par_chd_assoc_list.add(asso_name);

        Map<String, List<BySourceTabResults>> map = null;

        // LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        // MetaBrowserService mbs = null;
        try {
            mbs =
                (MetaBrowserService) lbs
                    .getGenericExtension("metabrowser-extension");
            if (direction) {
                map =
                    mbs.getBySourceTabDisplay(CUI, sab, par_chd_assoc_list,
                        Direction.SOURCEOF);
            } else {
                map =
                    mbs.getBySourceTabDisplay(CUI, sab, par_chd_assoc_list,
                        Direction.TARGETOF);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
			Entry thisEntry = (Entry) it.next();
			String rel = (String) thisEntry.getKey();
			List<BySourceTabResults> relations = (List<BySourceTabResults>) thisEntry.getValue();

        //for (String rel : map.keySet()) {
            //List<BySourceTabResults> relations = map.get(rel);
            for (BySourceTabResults result : relations) {
                String code = result.getCui();
                if (code.compareTo(CUI) != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getSubconceptCount(String scheme, String version, String code,
        String sab) {
        // HashMap hmap = getSubconcepts(scheme, null, code, sab, "PAR", false);

        HashMap hmap = getSubconcepts(scheme, null, code, sab, "CHD", true);

        Set keyset = hmap.keySet();
        Object[] objs = keyset.toArray();
        if (objs.length == 0)
            return 0;

        String id = (String) objs[0];
        int knt = 0;
        TreeItem ti = (TreeItem) hmap.get(id);
        for (String association : ti._assocToChildMap.keySet()) {
            List<TreeItem> children = ti._assocToChildMap.get(association);
            knt = knt + children.size();
        }
        return knt;
    }

    public HashMap getSubconcepts(String scheme, String version, String code,
        String sab, boolean associationsNavigatedFwd) {
        /*
         * HashMap hmap = new HashMap(); TreeItem ti = null; long ms =
         * System.currentTimeMillis();
         *
         * Set<String> codesToExclude = Collections.EMPTY_SET; boolean fwd =
         * true; String[] associationsToNavigate = fwd ? hierAssocToChildNodes_
         * : hierAssocToParentNodes_; //boolean associationsNavigatedFwd = true;
         *
         * CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag(); if
         * (version != null) csvt.setVersion(version);
         * ResolvedConceptReferenceList matches = null; //Vector v = new
         * Vector(); try { LexBIGService lbsvc =
         * RemoteServerUtil.createLexBIGService();
         * LexBIGServiceConvenienceMethods lbscm =
         * (LexBIGServiceConvenienceMethods) lbsvc
         * .getGenericExtension("LexBIGServiceConvenienceMethods");
         * lbscm.setLexBIGService(lbsvc); String name =
         * getCodeDescription(lbsvc, scheme, csvt, code); ti = new
         * TreeItem(code, name); ti.expandable = false;
         *
         * // Resolve the next branch, representing children of the given //
         * code, navigated according to the provided relationship and //
         * direction. Resolve the children as a code graph, looking 2 // levels
         * deep but leaving the final level unresolved.
         *
         * CodedNodeGraph cng = lbsvc.getNodeGraph(scheme, csvt, null);
         * ConceptReference focus = Constructors.createConceptReference(code,
         * scheme); cng = cng.restrictToAssociations(
         * Constructors.createNameAndValueList(associationsToNavigate),
         * ConvenienceMethods.createNameAndValueList("source", sab));
         *
         * CodedNodeSet.PropertyType[] propertytypes = null; if
         * (RESOLVE_CONCEPT) { propertytypes = new PropertyType[]
         * {PropertyType.PRESENTATION}; }
         *
         * ResolvedConceptReferenceList branch = cng.resolveAsList( focus,
         * associationsNavigatedFwd, !associationsNavigatedFwd,
         * Integer.MAX_VALUE, 2, //null, propertytypes, sortByCode_, null, -1,
         * true); null, propertytypes, sortByCode_, null, -1, RESOLVE_CONCEPT);
         *
         * // The resolved branch will be represented by the first node in //
         * the resolved list. The node will be subdivided by source or // target
         * associations (depending on direction). The associated // nodes define
         * the children. for (ResolvedConceptReference node :
         * branch.getResolvedConceptReference()) { AssociationList
         * childAssociationList = associationsNavigatedFwd ? node.getSourceOf()
         * : node.getTargetOf(); // Process each association defining children
         * ... for (Association child : childAssociationList.getAssociation()) {
         * String childNavText = getDirectionalLabel(lbscm, scheme, csvt, child,
         * associationsNavigatedFwd); // Each association may have multiple
         * children ... AssociatedConceptList branchItemList =
         * child.getAssociatedConcepts(); for (AssociatedConcept branchItemNode
         * : branchItemList.getAssociatedConcept()) { if
         * (isValidForSAB(branchItemNode, sab)) { String branchItemCode =
         * branchItemNode.getCode(); // Add here if not in the list of excluded
         * codes. // This is also where we look to see if another level // was
         * indicated to be available. If so, mark the // entry with a '+' to
         * indicate it can be expanded. if
         * (!codesToExclude.contains(branchItemCode)) { ti.expandable = true;
         *
         * TreeItem childItem = new TreeItem(branchItemCode,
         * branchItemNode.getEntityDescription().getContent()); AssociationList
         * grandchildBranch = associationsNavigatedFwd ?
         * branchItemNode.getSourceOf() : branchItemNode.getTargetOf(); if
         * (grandchildBranch != null) {
         *
         * for (Association grandchild : grandchildBranch.getAssociation()) {
         * java.lang.String association_name = grandchild.getAssociationName();
         * //String grandchildNavText = getDirectionalLabel(lbscm, scheme, csvt,
         * child, associationsNavigatedFwd); // Each association may have
         * multiple children ... AssociatedConceptList grandchildbranchItemList
         * = grandchild.getAssociatedConcepts(); for (AssociatedConcept
         * grandchildbranchItemNode :
         * grandchildbranchItemList.getAssociatedConcept()) {
         *
         * if (isValidForSAB(grandchildbranchItemNode, sab)) {
         * childItem.expandable = true; break; } } } }
         *
         * ti.addChild(childNavText, childItem); ti.expandable = true; } } } } }
         * hmap.put(code, ti); } catch (Exception ex) { ex.printStackTrace(); }
         * _logger.debug("Run time (milliseconds) getSubconcepts: " +
         * (System.currentTimeMillis() - ms) + " to resolve " ); return hmap;
         */
        return getSubconcepts(code, sab, "CHD", associationsNavigatedFwd);
    }

    // KLO
    public HashMap getSubconcepts(String code, String sab, String association,
        boolean associationsNavigatedFwd) {
        LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        MetaBrowserService mbs = null;

        TreeItem ti = null;
        HashMap hmap = new HashMap();
        long ms = System.currentTimeMillis();

        try {
            // LexBIGServiceConvenienceMethods lbscm =
            // (LexBIGServiceConvenienceMethods) lbSvc
            // .getGenericExtension("LexBIGServiceConvenienceMethods");
            // lbscm.setLexBIGService(lbSvc);
            String name =
                getCodeDescription(lbs, "NCI Metathesaurus", null, code);
            ti = new TreeItem(code, name);
            ti._expandable = false;

            List<String> par_chd_assoc_list = new ArrayList();
            par_chd_assoc_list.add(association);

            Map<String, List<BySourceTabResults>> map = null;
            try {
                mbs =
                    (MetaBrowserService) lbs
                        .getGenericExtension("metabrowser-extension");
                if (associationsNavigatedFwd) {
                    map =
                        mbs.getBySourceTabDisplay(code, sab,
                            par_chd_assoc_list, Direction.SOURCEOF);
                } else {
                    map =
                        mbs.getBySourceTabDisplay(code, sab,
                            par_chd_assoc_list, Direction.TARGETOF);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return hmap;
            }

            HashMap cui2SynonymsMap = createCUI2SynonymsHahMap(map);


			Iterator iterator = cui2SynonymsMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry thisEntry = (Entry) iterator.next();
				String child_cui = (String) thisEntry.getKey();
				Vector v = (Vector) thisEntry.getValue();
/*
            Set keyset = cui2SynonymsMap.keySet();
            Iterator iterator = keyset.iterator();
            while (iterator.hasNext()) {
                String child_cui = (String) iterator.next();
                Vector v = (Vector) cui2SynonymsMap.get(child_cui);
*/

                TreeItem sub = null;
                // temporary
                BySourceTabResults result =
                    DataUtils.findHighestRankedAtom(v, sab);
                // BySourceTabResults result = findHighestRankedAtom(v, sab);
                if (result == null) {
                    result = (BySourceTabResults) v.elementAt(0);
                }
                sub = new TreeItem(child_cui, result.getTerm());
                sub._expandable =
                    hasSubconcepts(lbs, mbs, child_cui, "NCI", association,
                        associationsNavigatedFwd);
                ti.addChild(association, sub);
                ti._expandable = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return hmap;
        }

        hmap.put(code, ti);

        _logger.debug("Run time (milliseconds) getSubconcepts: "
            + (System.currentTimeMillis() - ms) + " to resolve ");
        return hmap;
    }

    // /////////////////////////////////////////////////////
    // Helper Methods
    // /////////////////////////////////////////////////////

    /**
     * Returns the entity description for the given code.
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

    /**
     * Returns the entity description for the given resolved concept reference.
     */
    protected String getCodeDescription(ResolvedConceptReference ref)
            throws LBException {
        EntityDescription desc = ref.getEntityDescription();
        if (desc != null)
            return desc.getContent();
        return "<Not assigned>";
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

    public static void dumpTreeItem(TreeItem ti) {
        dumpTreeItem(ti, 0);
    }

    public static void dumpTreeItem(TreeItem ti, int level) {
        //String indent = "";
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < level; i++) {
            //indent = indent + "\t";
            buf.append("\t");
        }
        String indent = buf.toString();
        _logger.debug(indent + ti._text + " (" + ti._code + ")");
        try {
            for (String association : ti._assocToChildMap.keySet()) {
                _logger.debug("\n" + indent + " --- " + association);
                List<TreeItem> children = ti._assocToChildMap.get(association);

                for (TreeItem childItem : children) {
                    dumpTreeItem(childItem, level + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("\tdumpTreeItem throws exception.");
        }
    }

    public static void dumpTreeItems(HashMap hmap) {

        try {
            Set keyset = hmap.keySet();
            Object[] objs = keyset.toArray();
            String code = (String) objs[0];

            TreeItem ti = (TreeItem) hmap.get(code);
            for (String association : ti._assocToChildMap.keySet()) {
                _logger.debug("\n--- " + association);
                List<TreeItem> children = ti._assocToChildMap.get(association);

                for (TreeItem childItem : children) {
                    // _logger.debug(childItem.text + "(" + childItem.code +
                    // ")");
                    /*
                     * int knt = 0; if (childItem.expandable) { knt = 1;
                     * _logger.debug("\tnode.expandable"); } else {
                     * _logger.debug("\tnode.NOT expandable"); }
                     */
                    dumpTreeItem(childItem, 0);
                }
            }
        } catch (Exception e) {

        }
    }

    public void run(String scheme, String version, String code) {

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        ResolvedConceptReference rcr = null;
        try {
            rcr = resolveConcept(scheme, csvt, code);
        } catch (Exception ex) {

        }
        if (rcr == null) {
            Util_displayMessage("Unable to resolve a concept for CUI = '"
                + code + "'");
            //System.exit(1);
            return;
        }

        String name = null;
        try {
            name = getCodeDescription(rcr);
        } catch (Exception ex) {
            name = "Unknown";
        }
        _logger.debug("Coding scheme: " + scheme);
        _logger.debug("code: " + code);
        _logger.debug("name: " + name);

        String sab = "NCI";
        // boolean associationsNavigatedFwd = true;

        Long startTime = System.currentTimeMillis();
        HashMap hmap1 = getSubconcepts(scheme, version, code, sab, true);
        _logger.debug("Call getSubconcepts true took: "
            + (System.currentTimeMillis() - startTime) + "ms");
        dumpTreeItems(hmap1);

        startTime = System.currentTimeMillis();
        HashMap hmap2 = getSubconcepts(scheme, version, code, sab, false);
        _logger.debug("Call getSubconcepts false took: "
            + (System.currentTimeMillis() - startTime) + "ms");
        dumpTreeItems(hmap2);

    }

    protected String getDisplayRef(ResolvedConceptReference ref) {
        return "[" + ref.getEntityDescription().getContent() + "("
            + ref.getConceptCode() + ")]";
    }

    public HashMap getSubconcepts(String scheme, String version, String code,
        String sab, String asso_name, boolean associationsNavigatedFwd) {
        String[] asso_names = new String[] { asso_name };
        return getSubconcepts(scheme, version, code, sab, asso_names,
            associationsNavigatedFwd);
    }

    public HashMap getSubconcepts(String scheme, String version, String code,
        String sab, String[] asso_names, boolean associationsNavigatedFwd) {
        HashSet hset = new HashSet();
        HashMap hmap = new HashMap();
        TreeItem ti = null;
        //Vector w = new Vector();

        long ms = System.currentTimeMillis();

        Set<String> codesToExclude = Collections.EMPTY_SET;
        boolean fwd = true;

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();

        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            LexBIGServiceConvenienceMethods lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
            String name = getCodeDescription(lbSvc, scheme, csvt, code);
            ti = new TreeItem(code, name);
            ti._expandable = false;

            CodedNodeGraph cng = null;
            cng = lbSvc.getNodeGraph(scheme, null, null);

            if (sab != null) {
                cng =
                    cng.restrictToAssociations(Constructors
                        .createNameAndValueList(_hierAssocToChildNodes),
                        Constructors.createNameAndValueList("source", sab));
            } else {
                cng =
                    cng.restrictToAssociations(Constructors
                        .createNameAndValueList(_hierAssocToChildNodes), null);
            }

            CodedNodeSet.PropertyType[] propertytypes = null;
            if (_resolveConcept) {
                propertytypes =
                    new PropertyType[] { PropertyType.PRESENTATION };
            }

            // int resolveCodedEntryDepth = 0;
            ResolvedConceptReferenceList branch = null;
            branch =
                cng.resolveAsList(Constructors.createConceptReference(code,
                    scheme), !associationsNavigatedFwd,
                    associationsNavigatedFwd, Integer.MAX_VALUE, 2, null,
                    propertytypes, null, -1);

            if (branch.getResolvedConceptReferenceCount() > 0) {
                Enumeration<ResolvedConceptReference> refEnum =
                    (Enumeration<ResolvedConceptReference>) branch.enumerateResolvedConceptReference();

                while (refEnum.hasMoreElements()) {
                    ResolvedConceptReference ref = refEnum.nextElement();
                    // AssociationList childAssociationList = ref.getTargetOf();
                    AssociationList childAssociationList = ref.getSourceOf();

                    if (childAssociationList == null) {
                        return hmap;
                    }

                    // Process each association defining children ...
                    for (Association child : childAssociationList
                        .getAssociation()) {
                        String childNavText =
                            getDirectionalLabel(lbscm, scheme, csvt, child,
                                associationsNavigatedFwd);
                        // Each association may have multiple children ...
                        AssociatedConceptList branchItemList =
                            child.getAssociatedConcepts();
                        for (AssociatedConcept branchItemNode : branchItemList
                            .getAssociatedConcept()) {

                            if (isValidForSAB(branchItemNode, sab)) {
                                String branchItemCode =
                                    branchItemNode.getCode();
                                // Add here if not in the list of excluded
                                // codes.
                                // This is also where we look to see if another
                                // level
                                // was indicated to be available. If so, mark
                                // the
                                // entry with a '+' to indicate it can be
                                // expanded.
                                if (!codesToExclude.contains(branchItemCode)) {

                                    if (!hset.contains(branchItemCode)) {
                                        hset.add(branchItemCode);

                                        TreeItem childItem =
                                            new TreeItem(branchItemCode,
                                                branchItemNode
                                                    .getEntityDescription()
                                                    .getContent());

                                        childItem._expandable = false;

                                        AssociationList grandchildBranch =
                                            branchItemNode.getSourceOf();
                                        if (grandchildBranch != null) {

                                            for (Association grandchild : grandchildBranch
                                                .getAssociation()) {

                                                java.lang.String association_name =
                                                    grandchild
                                                        .getAssociationName();

                                                // String grandchildNavText =
                                                // getDirectionalLabel(lbscm,
                                                // scheme, csvt, child,
                                                // associationsNavigatedFwd);
                                                // Each association may have
                                                // multiple children ...
                                                AssociatedConceptList grandchildbranchItemList =
                                                    grandchild
                                                        .getAssociatedConcepts();
                                                for (AssociatedConcept grandchildbranchItemNode : grandchildbranchItemList
                                                    .getAssociatedConcept()) {

                                                    if (isValidForSAB(
                                                        grandchildbranchItemNode,
                                                        sab)) {
                                                        childItem._expandable =
                                                            true;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        ti.addChild(childNavText, childItem);
                                        ti._expandable = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            hmap.put(code, ti);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        _logger.debug("Run time (milliseconds) getSubconcepts: "
            + (System.currentTimeMillis() - ms) + " to resolve ");
        return hmap;
    }

    protected String getAssociationSourceString(AssociatedConcept ac) {
        //String sources = "";
        StringBuffer buf = new StringBuffer();
        NameAndValue[] nvl = ac.getAssociationQualifiers().getNameAndValue();
        int knt = 0;
        for (int i = 0; i < nvl.length; i++) {
            NameAndValue nv = nvl[i];
            if (nv.getName().compareToIgnoreCase("source") == 0) {
                knt++;
                if (knt == 1) {
                    //sources = sources + nv.getContent();
                    buf.append(nv.getContent());
                } else {
                    //sources = sources + " ;" + nv.getContent();
                    buf.append(" ;" + nv.getContent());
                }
            }
        }
        String sources = buf.toString();
        return sources;
    }

    protected Vector getAssociationSources(AssociatedConcept ac) {
        Vector sources = new Vector();
        NameAndValue[] nvl = ac.getAssociationQualifiers().getNameAndValue();
        for (int i = 0; i < nvl.length; i++) {
            NameAndValue nv = nvl[i];
            if (nv.getName().compareToIgnoreCase("source") == 0) {
                sources.add(nv.getContent());
            }
        }
        return sources;
    }

    /**
     * Build and returns tree items that represent the root and core concepts of
     * resolved paths for printing.
     *
     * @throws LBException
     */
    protected TreeItem[] buildPathsToRoot(ResolvedConceptReference rcr,
        String scheme, CodingSchemeVersionOrTag csvt, String sab, int maxLevel)
            throws LBException {

        // Create a starting point for tree building.
        // TreeItem ti = new TreeItem(rcr.getCode(),
        // rcr.getEntityDescription().getContent(), getAtomText(rcr, sab));

        String root_name =
            getAtomName(rcr.getReferencedEntry(), NCI_SOURCE, "PT");

        // TreeItem ti = new TreeItem(rcr.getCode(),
        // rcr.getEntityDescription().getContent());
        TreeItem ti = new TreeItem(rcr.getCode(), root_name);

        LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        MetaBrowserService mbs = null;
        try {
            mbs =
                (MetaBrowserService) lbs
                    .getGenericExtension("metabrowser-extension");
        } catch (Exception ex) {

        }

        ti._expandable =
            hasSubconcepts(lbs, mbs, rcr.getCode(), sab, "CHD", true);
        // Maintain root tree items.
        Set<TreeItem> rootItems = new HashSet<TreeItem>();

        Set<String> visited_links = new HashSet<String>();

        // Natural flow of hierarchy relations moves forward
        // from tree root to leaves. Build the paths to root here
        // by processing upstream (child to parent) relationships.

        // buildPathsToUpperNodes(

        buildPathsToUpperNodesExt(lbs, mbs, ti, sab,
            new HashMap<String, TreeItem>(), rootItems, visited_links,
            maxLevel, 0);// , hierAssocToParentNodes_, false);
        // Return root items discovered during child to parent
        // processing.
        return rootItems.toArray(new TreeItem[rootItems.size()]);
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

    /**
     * Add all hierarchical paths to root that start from the referenced concept
     * and move backward in the tree. If the natural flow of relations is
     * thought of moving from tree root to leaves, this method processes nodes
     * in the reverse direction (from child to parent).
     *
     * @throws LBException
     */

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
        if (code2Tree.containsKey(rcr.getCode()))
            return;

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

        for (int i = 0; i <= 1; i++) {
            boolean fwd = i < 1;
            String[] upstreamAssoc =
                fwd ? _hierAssocToParentNodes : _hierAssocToChildNodes;

            // Define a code graph for all relationships tagged with
            // the specified sab.

            CodedNodeGraph graph =
                getLexBIGService().getNodeGraph(scheme, csvt, null);
            graph =
                graph.restrictToAssociations(ConvenienceMethods
                    .createNameAndValueList(upstreamAssoc), ConvenienceMethods
                    .createNameAndValueList("source", sab));

            // Resolve one hop, retrieving presentations for
            // comparison of source assignments.

            ResolvedConceptReference[] refs =
                graph.resolveAsList(rcr, fwd, !fwd, Integer.MAX_VALUE, 1, null,
                    new PropertyType[] { PropertyType.PRESENTATION },
                    _sortByCode, null, -1).getResolvedConceptReference();
            // null, null, sortByCode_, null, -1).getResolvedConceptReference();

            // Create a new tree item for each upstream node, add the current
            // tree item as a child, and recurse to go higher (if available).

            if (refs.length > 0) {

                // Each associated concept represents an upstream branch.
                AssociationList aList =
                    fwd ? refs[0].getSourceOf() : refs[0].getTargetOf();
                if (aList != null) {

                    for (Association assoc : aList.getAssociation()) {
                        // Go through the concepts one by one, adding the
                        // current tree item as a child of a new tree item
                        // representing the upstream node. If a tree item
                        // already exists for the parent, we reuse it to
                        // keep a single branch per parent.
                        for (AssociatedConcept refParent : assoc
                            .getAssociatedConcepts().getAssociatedConcept()) {
                            if (isValidForSAB(refParent, sab)) {
                                // Fetch the term for this context ...
                                Presentation[] sabMatch =
                                    getSourcePresentations(refParent, sab);
                                if (sabMatch.length > 0) {

                                    // We need to take into account direction of
                                    // navigation on each pass to get the right
                                    // label.
                                    String directionalName =
                                        getDirectionalLabel(scheme, csvt,
                                            assoc, !fwd);
                                    // Check for a previously registered item
                                    // for the
                                    // parent. If found, re-use it. Otherwise,
                                    // create
                                    // a new parent tree item.
                                    String parentCode = refParent.getCode();
                                    String link =
                                        rcr.getConceptCode() + "|" + parentCode;
                                    if (!visited_links.contains(link)) {
                                        visited_links.add(link);
                                        TreeItem tiParent =
                                            code2Tree.get(parentCode);
                                        if (tiParent == null) {

                                            // Create a new tree item.
                                            // tiParent = new
                                            // TreeItem(parentCode,
                                            // refParent.getEntityDescription().getContent(),
                                            // getAtomText(refParent, sab));
                                            tiParent =
                                                new TreeItem(parentCode,
                                                    refParent
                                                        .getEntityDescription()
                                                        .getContent());// ,
                                                                       // getAtomText(refParent,
                                                                       // sab));

                                            // Add immediate children of the
                                            // parent code with an
                                            // indication of sub-nodes (+).
                                            // Codes already
                                            // processed as part of the path are
                                            // ignored since
                                            // they are handled through
                                            // recursion.

                                            String[] downstreamAssoc =
                                                fwd ? _hierAssocToChildNodes
                                                    : _hierAssocToParentNodes;
                                            // addChildren(tiParent, scheme,
                                            // csvt, sab, parentCode,
                                            // code2Tree.keySet(),
                                            // downstreamAssoc, fwd);
                                            tiParent =
                                                addChildren(tiParent, scheme,
                                                    csvt, sab, parentCode,
                                                    code2Tree.keySet());

                                            // Try to go higher through
                                            // recursion.
                                            buildPathsToUpperNodes(tiParent,
                                                refParent, scheme, csvt, sab,
                                                code2Tree, roots,
                                                visited_links, maxLevel,
                                                currLevel + 1);
                                        }

                                        // Add the child (eliminate redundancy
                                        // -- e.g., hasSubtype and CHD)
                                        if (!hasChildren(tiParent, ti._code)) {
                                            tiParent.addChild(directionalName,
                                                ti);
                                            tiParent._expandable = true;
                                        }
                                        isRoot = false;
                                    }
                                }
                            }
                        }
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

    public static HashMap createCUI2SynonymsHahMap(
        Map<String, List<BySourceTabResults>> map) {
        HashMap hmap = new HashMap();

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
			Entry thisEntry = (Entry) it.next();
			String rel = (String) thisEntry.getKey();
			List<BySourceTabResults> relations = (List<BySourceTabResults>) thisEntry.getValue();
/*
        for (String rel : map.keySet()) {
            List<BySourceTabResults> relations = map.get(rel);
*/
            for (BySourceTabResults result : relations) {
                String rela = result.getRela();
                String cui = result.getCui();
                String source = result.getSource();
                String name = result.getTerm();

                Vector v = null;
                if (hmap.containsKey(cui)) {
                    v = (Vector) hmap.get(cui);
                } else {
                    v = new Vector();
                }
                v.add(result);
                hmap.put(cui, v);
            }
        }
        return hmap;
    }

    // //////////////////////////////////
    // search_tree using extension
    // //////////////////////////////////

    protected void buildPathsToUpperNodesExt(LexBIGService lbs,
        MetaBrowserService mbs, TreeItem ti, String sab,
        Map<String, TreeItem> code2Tree, Set<TreeItem> roots,
        Set<String> visited_links, int maxLevel, int currLevel)
            throws LBException {

        HashSet new_root_codes = new HashSet();

        if (maxLevel != -1 && currLevel > maxLevel) {
            return;
        }

        // Only need to process a code once ...
        if (code2Tree.containsKey(ti._code))
            return;

        // Cache for future reference.
        code2Tree.put(ti._code, ti);

        // UMLS relations can be defined with forward direction
        // being parent to child or child to parent on a source
        // by source basis. Iterate twice to ensure completeness;
        // once navigating child to parent relations forward
        // and once navigating parent to child relations
        // backward. Both have the net effect of navigating
        // from the bottom of the hierarchy to the top.
        boolean isRoot = true;

        // find parents:
        List<String> par_chd_assoc_list = new ArrayList();
        par_chd_assoc_list.add("CHD");

        Map<String, List<BySourceTabResults>> map = null;

        // LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        // MetaBrowserService mbs = null;
        try {
            mbs =
                (MetaBrowserService) lbs
                    .getGenericExtension("metabrowser-extension");
            map =
                mbs.getBySourceTabDisplay(ti._code, sab, par_chd_assoc_list,
                    Direction.TARGETOF);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        HashMap cui2SynonymsMap = createCUI2SynonymsHahMap(map);

        Iterator iterator = cui2SynonymsMap.entrySet().iterator();
        while (iterator.hasNext()) {
			Entry thisEntry = (Entry) iterator.next();
			String parent_cui = (String) thisEntry.getKey();
        /*
        Set keyset = cui2SynonymsMap.keySet();
        Iterator iterator = keyset.iterator();
        while (iterator.hasNext()) {
            String parent_cui = (String) iterator.next();
        */
            // KLO, 020210
            if (parent_cui.compareTo(_nciThesaurusCui) != 0) {
                //Vector v = (Vector) cui2SynonymsMap.get(parent_cui);
                Vector v = (Vector) thisEntry.getValue();

                BySourceTabResults result =
                    DataUtils.findHighestRankedAtom(v, sab);
                // BySourceTabResults result = findHighestRankedAtom(v, sab);
                if (result == null) {
                    result = (BySourceTabResults) v.elementAt(0);
                }
                // BySourceTabResults result = (BySourceTabResults)
                // v.elementAt(0);
                String link = ti._code + "|" + parent_cui;

                if (!visited_links.contains(link)) {
                    visited_links.add(link);
                    TreeItem tiParent = code2Tree.get(parent_cui);
                    if (tiParent == null) {
                        tiParent = new TreeItem(parent_cui, result.getTerm());
                    }

                    // Add immediate children of the parent code with an
                    // indication of sub-nodes (+). Codes already
                    // processed as part of the path are ignored since
                    // they are handled through recursion.

                    // tiParent = addChildrenExt(lbs, mbs, tiParent, sab,
                    // parent_cui, code2Tree.keySet(), code2Tree);
                    tiParent =
                        addChildrenExt(lbs, mbs, tiParent, sab, parent_cui,
                            code2Tree.keySet(), code2Tree, ti._code);
                    tiParent.addChild("CHD", ti);

                    // Try to go higher through recursion.
                    buildPathsToUpperNodesExt(lbs, mbs, tiParent, sab,
                        code2Tree, roots, visited_links, maxLevel,
                        currLevel + 1);

                    code2Tree.put(parent_cui, tiParent);
                }
            } else {
                roots.add(ti);
                new_root_codes.add(ti._code);
            }
        }

        code2Tree.put(ti._code, ti);
        isRoot = false;
        if (maxLevel != -1 && currLevel == maxLevel) {
            isRoot = true;
        }
        if (isRoot) {
            // //KLO, 020210
            if (!new_root_codes.contains(ti._code)) {
                roots.add(ti);
            }
        }
    }

    public void dumpTree(HashMap hmap, String focusCode, int level) {
        try {
            Set keyset = hmap.keySet();
            Object[] objs = keyset.toArray();
            String code = (String) objs[0];
            TreeItem ti = (TreeItem) hmap.get(code);
            for (String association : ti._assocToChildMap.keySet()) {
                _logger.debug("\nassociation: " + association);
                List<TreeItem> children = ti._assocToChildMap.get(association);
                for (TreeItem childItem : children) {
                    _logger.debug(childItem._text + "(" + childItem._code + ")");
                    int knt = 0;
                    if (childItem._expandable) {
                        knt = 1;
                        _logger.debug("\tnode.expandable");

                        printTree(childItem, focusCode, level);

                        List list = getTopNodes(childItem);
                        for (int i = 0; i < list.size(); i++) {
                            Object obj = list.get(i);
                            String nd_code = "";
                            String nd_name = "";
                            if (obj instanceof ResolvedConceptReference) {
                                ResolvedConceptReference node =
                                    (ResolvedConceptReference) list.get(i);
                                nd_code = node.getConceptCode();
                                nd_name =
                                    node.getEntityDescription().getContent();
                            } else if (obj instanceof Entity) {
                                Entity node = (Entity) list.get(i);
                                nd_code = node.getEntityCode();
                                nd_name =
                                    node.getEntityDescription().getContent();
                            }
                            _logger.debug("TOP NODE: " + nd_name + " ("
                                + nd_code + ")");
                        }
                    } else {
                        _logger.debug("\tnode.NOT expandable");
                    }
                }
            }
        } catch (Exception e) {
			e.printStackTrace();
        }
    }

    // public void addChildren(TreeItem ti, String scheme,
    // CodingSchemeVersionOrTag csvt,
    public TreeItem addChildren(TreeItem ti, String scheme,
        CodingSchemeVersionOrTag csvt, String sab, String code,
        Set<String> codesToExclude) throws LBException {
        boolean associationsNavigatedFwd = true;
        String[] associationsToNavigate = new String[] { "CHD" };
        return addChildren(ti, scheme, csvt, sab, code, codesToExclude,
            associationsToNavigate, associationsNavigatedFwd);
    }

    public TreeItem addChildrenExt(LexBIGService lbs, MetaBrowserService mbs,
        TreeItem ti, String sab, String code, Set<String> codesToExclude,
        Map<String, TreeItem> code2Tree, String target_code) throws LBException {

        List<String> par_chd_assoc_list = new ArrayList();
        par_chd_assoc_list.add("CHD");

        Map<String, List<BySourceTabResults>> map = null;
        try {
            // mbs =
            // (MetaBrowserService)lbs.getGenericExtension("metabrowser-extension");
            map =
                mbs.getBySourceTabDisplay(ti._code, sab, par_chd_assoc_list,
                    Direction.SOURCEOF);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ti;
        }

        Vector w = new Vector();
        /*
         * KLO, 020210 HashMap cui2SynonymsMap = createCUI2SynonymsHahMap(map);
         * ti.expandable = false; Set keyset = cui2SynonymsMap.keySet();
         * Iterator iterator = keyset.iterator(); while (iterator.hasNext()) {
         * String child_cui = (String) iterator.next();
         * //_logger.debug("\tchild_cui: " + child_cui); TreeItem sub = null; if
         * (code2Tree.containsKey(child_cui)) { sub = (TreeItem)
         * code2Tree.get(child_cui); } else { Vector v = (Vector)
         * cui2SynonymsMap.get(child_cui); //BySourceTabResults result =
         * DataUtils.findHighestRankedAtom(v, sab); BySourceTabResults result =
         * findHighestRankedAtom(v, sab); if (result == null) { result =
         * (BySourceTabResults) v.elementAt(0); } //BySourceTabResults result =
         * (BySourceTabResults) v.elementAt(0); sub = new TreeItem(child_cui,
         * result.getTerm()); sub.expandable = hasSubconcepts(lbs, mbs,
         * child_cui, "NCI", "CHD", true); } ti.addChild("CHD", sub);
         * ti.expandable = true; }
         */
        HashMap cui2SynonymsMap = createCUI2SynonymsHahMap(map);
        ti._expandable = false;

        Iterator iterator = cui2SynonymsMap.entrySet().iterator();
        while (iterator.hasNext()) {
			Entry thisEntry = (Entry) iterator.next();
			String child_cui = (String) thisEntry.getKey();
/*
        Set keyset = cui2SynonymsMap.keySet();
        Iterator iterator = keyset.iterator();
        while (iterator.hasNext()) {
            String child_cui = (String) iterator.next();
*/
            TreeItem sub = null;
            if (code2Tree.containsKey(child_cui)) {
                sub = (TreeItem) code2Tree.get(child_cui);
            } else {
                //Vector v = (Vector) cui2SynonymsMap.get(child_cui);
                Vector v = (Vector) thisEntry.getValue();
                BySourceTabResults result =
                    DataUtils.findHighestRankedAtom(v, sab);
                // BySourceTabResults result = findHighestRankedAtom(v, sab);
                if (result == null) {
                    result = (BySourceTabResults) v.elementAt(0);
                }
                // BySourceTabResults result = (BySourceTabResults)
                // v.elementAt(0);
                sub = new TreeItem(child_cui, result.getTerm());
                sub._expandable =
                    hasSubconcepts(lbs, mbs, child_cui, NCI_SOURCE, "CHD", true);
            }
            w.add(sub);
        }
        w = SortUtils.quickSort(w);

        /*
         * for (int i=0; i<w.size(); i++) { TreeItem sub = (TreeItem)
         * w.elementAt(i); ti.expandable = true; ti.addChild("CHD", sub); }
         */

        // Truncate subconcept list to enhance search_tree performance
        int target_idx = -1;
        for (int i = 0; i < w.size(); i++) {
            TreeItem sub = (TreeItem) w.elementAt(i);
            if (sub._code.compareTo(target_code) == 0) {
                target_idx = i;
                break;
            }
        }

        for (int i = 0; i <= target_idx; i++) {
            TreeItem sub = (TreeItem) w.elementAt(i);
            ti._expandable = true;
            ti.addChild("CHD", sub);
        }

        if (target_idx == w.size() - 1) {
            return ti;
        }

        for (int i = target_idx + 1; i < w.size(); i++) {
            TreeItem sub = (TreeItem) w.elementAt(i);
            if (sub._expandable) {
                sub._text = "...";// + sub.text;
                sub._code = sub._code + "|" + ti._code;
                ti._expandable = true;
                ti.addChild("CHD", sub);
                break;
            } else {
                ti._expandable = true;
                ti.addChild("CHD", sub);
            }
        }

        // ti.addChild("CHD", sub);
        // }
        return ti;
    }

    // For testing use.
    // to be replaced by DataUtils.findHighestRankedAtom
    public BySourceTabResults findHighestRankedAtom(
        Vector<BySourceTabResults> v, String sab) {
        if (v == null)
            return null;
        return (BySourceTabResults) v.elementAt(0);
    }

    // pre-sort, include only the subconcept with subconcept_code and all other
    // remaining subconcepts
    public HashMap getRemainingSubconcepts(String scheme, String version,
        String code, String sab, String subconcept_code) {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);

        String name = null;
        try {
            name = getCodeDescription(lbSvc, scheme, csvt, code);
        } catch (Exception ex) {
        }

        TreeItem ti = new TreeItem(code, name);
        ti._expandable = false;

        //HashSet hset = new HashSet();
        HashMap hmap = new HashMap();
        Vector w = new Vector();

        //long ms = System.currentTimeMillis();
        //Set<String> codesToExclude = Collections.EMPTY_SET;

        List<String> par_chd_assoc_list = new ArrayList();
        par_chd_assoc_list.add("CHD");

        Map<String, List<BySourceTabResults>> map = null;

        MetaBrowserService mbs = null;

        try {
            mbs =
                (MetaBrowserService) lbSvc
                    .getGenericExtension("metabrowser-extension");
            map =
                mbs.getBySourceTabDisplay(ti._code, sab, par_chd_assoc_list,
                    Direction.SOURCEOF);

        } catch (Exception ex) {
            ex.printStackTrace();
            hmap.put(code, ti);
            return hmap;
        }

        HashMap cui2SynonymsMap = createCUI2SynonymsHahMap(map);
        ti._expandable = false;

        Iterator iterator = cui2SynonymsMap.entrySet().iterator();
        while (iterator.hasNext()) {
			Entry thisEntry = (Entry) iterator.next();
			String child_cui = (String) thisEntry.getKey();
/*

        Set keyset = cui2SynonymsMap.keySet();
        Iterator iterator = keyset.iterator();
        while (iterator.hasNext()) {
            String child_cui = (String) iterator.next();
*/

            TreeItem sub = null;
            //Vector v = (Vector) cui2SynonymsMap.get(child_cui);
            Vector v = (Vector) thisEntry.getValue();
            BySourceTabResults result = DataUtils.findHighestRankedAtom(v, sab);
            if (result == null) {
                result = (BySourceTabResults) v.elementAt(0);
            }
            sub = new TreeItem(child_cui, result.getTerm());
            sub._expandable =
                hasSubconcepts(lbSvc, mbs, child_cui, "NCI", "CHD", true);
            w.add(sub);
        }

        /*
         * // testing par_chd_assoc_list = new ArrayList();
         * par_chd_assoc_list.add("CHD"); try { mbs =
         * (MetaBrowserService)lbSvc.getGenericExtension
         * ("metabrowser-extension"); map = mbs.getBySourceTabDisplay(ti.code,
         * sab, par_chd_assoc_list, Direction.TARGETOF);
         *
         * } catch (Exception ex) { ex.printStackTrace(); hmap.put(code, ti);
         * return hmap; }
         *
         * cui2SynonymsMap = createCUI2SynonymsHahMap(map); //ti.expandable =
         * false; keyset = cui2SynonymsMap.keySet(); iterator =
         * keyset.iterator(); while (iterator.hasNext()) { String child_cui =
         * (String) iterator.next(); TreeItem sub = null; Vector v = (Vector)
         * cui2SynonymsMap.get(child_cui); BySourceTabResults result =
         * DataUtils.findHighestRankedAtom(v, sab); if (result == null) { result
         * = (BySourceTabResults) v.elementAt(0); } sub = new
         * TreeItem(child_cui, result.getTerm()); sub.expandable =
         * hasSubconcepts(lbSvc, mbs, child_cui, "NCI", "CHD", true);
         * w.add(sub); }
         */
        w = SortUtils.quickSort(w);
        boolean include = false;
        int istart = 0;
        if (subconcept_code != null) {
            for (int i = 0; i < w.size(); i++) {
                TreeItem sub = (TreeItem) w.elementAt(i);
                if (sub._code.compareTo(subconcept_code) == 0) {
                    istart = i;
                    break;
                }
            }
        }

        for (int i = 0; i < w.size(); i++) {
            TreeItem sub = (TreeItem) w.elementAt(i);
            if (subconcept_code == null) {
                include = true;
            } else {
                if (i >= istart)
                    include = true;
            }
            ti._expandable = true;
            if (include) {
                ti.addChild("CHD", sub);
            }
        }
        hmap.put(code, ti);
        return hmap;
    }

    protected String getAtomName(Entity ce, String sab_name, String term_type) {
        if (ce == null)
            return null;
        Property[] properties = ce.getPresentation();
        for (int i = 0; i < properties.length; i++) {
            Property property = (Property) properties[i];
            java.lang.String name = property.getPropertyName();
            java.lang.String prop_value = property.getValue().getContent();
            if (property instanceof org.LexGrid.concepts.Presentation) {
                Presentation presentation = (Presentation) property;
                java.lang.String form = presentation.getRepresentationalForm();
                if (form != null) {
                    if (form.compareTo(term_type) == 0) {
                        Source[] sources = property.getSource();
                        for (int j = 0; j < sources.length; j++) {
                            Source source = (Source) sources[j];
                            java.lang.String sab = source.getContent();
                            if (sab.compareTo(sab_name) == 0) {
                                return prop_value;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        MetaTreeUtils test = new MetaTreeUtils();

        String scheme = "NCI Metathesaurus";
        String version = null;
        String code = "C1325880";// "C0001206";
        boolean associationsNavigatedFwd = true;
        String sab = "NCI";

        HashMap new_map = null;
        code = "C1154313";

        code = "C0012634";

        code = "C0007581";

        // new_map = test.getSubconcepts(code, sab, "CHD", true);
        // test.dumpTreeItems(new_map);

        try {
            new_map = test.getTreePathData(scheme, version, sab, code, -1);
            test.dumpTreeItems(new_map);
        } catch (Exception ex) {

        }

    }

}

// NCI Thesaurus (CUI C1140168)
// biological_process (CUI C1154313)
// cellular process (CUI C1325880)
// Cell Aging (CUI C0007581)

