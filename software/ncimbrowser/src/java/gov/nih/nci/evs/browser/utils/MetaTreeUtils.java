package gov.nih.nci.evs.browser.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Presentation;
import org.apache.commons.lang.StringUtils;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;


import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.commonTypes.EntityDescription;





public class MetaTreeUtils {
    static String[] hierAssocToParentNodes_ = new String[] { "PAR", "isa", "branch_of", "part_of", "tributary_of" };
    static String[] hierAssocToChildNodes_ = new String[] { "CHD", "hasSubtype" };
    static SortOptionList sortByCode_ = Constructors.createSortOptionList(new String[] {"code"});

    LocalNameList noopList_ = Constructors.createLocalNameList("_noop_");
    LexBIGServiceConvenienceMethods lbscm_ = null;
    LexBIGService lbsvc_ = null;

	private LexBIGService lbs;
	private static String NCI_META_THESAURUS = "NCI MetaThesaurus";

	public static void main(String[] args) throws Exception {
		MetaTreeUtils getRoots = new MetaTreeUtils();

		Long startTime = System.currentTimeMillis();
		getRoots.getRoots("NCI");
		System.out.println("Call took: " + (System.currentTimeMillis() - startTime) + "ms");
	}

	public MetaTreeUtils(){
		init();
	}

	private void init(){
		//lbs = LexBIGServiceImpl.defaultInstance();
		lbs = RemoteServerUtil.createLexBIGService();
	}

    ///////////////////
    // Source Roots  //
    ///////////////////

	/**
	 * Finds the root node of a given sab.
	 *
	 * @param sab
	 * @throws Exception
	 */


	public void getRoots(String sab) throws Exception {
		ResolvedConceptReference root = resolveReferenceGraphForward(getCodingSchemeRoot(sab));
		AssociationList assocList = root.getSourceOf();
		for(Association assoc : assocList.getAssociation()){
			for(AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept()){
				if(this.isSabQualifiedAssociation(ac, sab)){
					displayRoot(ac);
				}
			}
		}
	}

	public ResolvedConceptReferenceList getSourceRoots(String sab) throws Exception {
		ResolvedConceptReferenceList rcrl = new ResolvedConceptReferenceList();
		ResolvedConceptReference root = resolveReferenceGraphForward(getCodingSchemeRoot(sab));
		AssociationList assocList = root.getSourceOf();
		for(Association assoc : assocList.getAssociation()){
			for(AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept()){
				if(this.isSabQualifiedAssociation(ac, sab)){
					ResolvedConceptReference r = new ResolvedConceptReference();
					EntityDescription entityDescription = new EntityDescription();
					entityDescription.setContent(ac.getEntityDescription().getContent());
					r.setEntityDescription(entityDescription);
					r.setCode(ac.getCode());
					rcrl.addResolvedConceptReference(r);
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
	protected void displayRoot(AssociatedConcept ac){
		System.out.println(ac.getCode() + " - " + ac.getEntityDescription().getContent());
	}

	/**
	 * Gets the UMLS root node of a given SAB.
	 *
	 * @param sab
	 * @return
	 * @throws LBException
	 */
	private ResolvedConceptReference getCodingSchemeRoot(String sab) throws LBException {
		CodedNodeSet cns = lbs.getCodingSchemeConcepts(NCI_META_THESAURUS, null);
		cns.restrictToProperties(null, new PropertyType[] {PropertyType.PRESENTATION}, Constructors.createLocalNameList("SRC"), null, Constructors.createNameAndValueList("source-code", "V-"+sab));
		ResolvedConceptReference[] refs = cns.resolveToList(null, null, new PropertyType[] {PropertyType.PRESENTATION}, -1).getResolvedConceptReference();

		if(refs.length > 1){
			throw new LBException("Found more than one Root for SAB: " + sab);
		}
		if(refs.length == 0){
			throw new LBException("Didn't find a Root for SAB: " + sab);
		}
		return refs[0];
	}

	/**
     * Resolve the relationships of a ResolvedConceptReference forward one level.
     *
     * @param ref
     * @return
     * @throws Exception
     */
    private ResolvedConceptReference resolveReferenceGraphForward(ResolvedConceptReference ref) throws Exception {
        CodedNodeGraph cng = lbs.getNodeGraph(NCI_META_THESAURUS, null, null);
        cng.restrictToAssociations(Constructors.createNameAndValueList(new String[]{"CHD", "hasSubtype"}), null);
        ResolvedConceptReference[] refs = cng.resolveAsList(ref, true, false, 1, 1, null, null, null, -1).getResolvedConceptReference();
        return refs[0];
    }

    /**
     * Determines whether or not the given reference is a root Concept for the given Coding Scheme.
     *
     * @param reference
     * @param sourceCodingScheme
     * @return
     */
    private boolean isSabQualifiedAssociation(AssociatedConcept ac, String sab){
    	NameAndValue[] nvl = ac.getAssociationQualifiers().getNameAndValue();
    	for(NameAndValue nv : nvl){
    		if(nv.getName().equals(sab) &&
    				nv.getContent().equals("Source")){
    			return true;
    		}
    	}
    	return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Util_displayMessage(String s) {
	    System.out.println(s);
	}

    /**
     * Prints formatted text providing context for
     * the given item including CUI, SAB, AUI, and Text.
     * @throws LBException
     */
    protected void printHeader(ResolvedConceptReference rcr, String sab)
            throws LBException {

        Util_displayMessage("CUI ....... : " + rcr.getConceptCode());
        Util_displayMessage("Description : " + StringUtils.abbreviate(rcr.getEntityDescription().getContent(), 60));
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

     /*
    protected void printTree(TreeItem ti, String focusCode, int depth) {
        StringBuffer indent = new StringBuffer();
        for (int i = 0; i < depth * 2; i++)
            indent.append("| ");

        StringBuffer codeAndText = new StringBuffer(indent)
            .append(focusCode.equals(ti.code) ? ">" : " ")
            .append(ti.code).append(':')
            .append(StringUtils.abbreviate(ti.text, 60))
            .append(ti.expandable ? " [+]" : "");
        if (ti.auis != null)
            for (String line : ti.auis.split("\\|"))
                codeAndText.append('\n').append(indent)
                    .append("    {")
                    .append(StringUtils.abbreviate(line, 60))
                    .append('}');
        Util_displayMessage(codeAndText.toString());

        indent.append("| ");
        for (String association : ti.assocToChildMap.keySet()) {
            Util_displayMessage(indent.toString() + association);
            List<TreeItem> children = ti.assocToChildMap.get(association);
            Collections.sort(children);
            for (TreeItem childItem : children)
                printTree(childItem, focusCode, depth + 1);
        }
    }
    */

    /**
     * Prints formatted text with the CUIs and AUIs of
     * neighboring concepts for the requested SAB.
     * @throws LBException
     */
    protected void printNeighborhood(String scheme, CodingSchemeVersionOrTag csvt,
            ResolvedConceptReference rcr, String sab)
            throws LBException {

        // Resolve neighboring concepts with associations
        // qualified by the SAB.
        CodedNodeGraph neighborsBySource = getLexBIGService().getNodeGraph(scheme, csvt, null);
        neighborsBySource.restrictToAssociations(null, Constructors.createNameAndValueList(sab, "Source"));
        ResolvedConceptReferenceList nodes = neighborsBySource.resolveAsList(
            rcr, true, true, Integer.MAX_VALUE, 1,
            null, new PropertyType[] { PropertyType.PRESENTATION },
            sortByCode_, null, -1);

        List<AssociatedConcept> neighbors = new ArrayList<AssociatedConcept>();
        for (ResolvedConceptReference node : nodes.getResolvedConceptReference()) {
            // Process sources and targets ...
            if (node.getSourceOf() != null)
                for (Association assoc : node.getSourceOf().getAssociation())
                    for (AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept())
                        if (isValidForSAB(ac, sab))
                            neighbors.add(ac);
            if (node.getTargetOf() != null)
                for (Association assoc : node.getTargetOf().getAssociation())
                    for (AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept())
                        if (isValidForSAB(ac, sab))
                            neighbors.add(ac);

            // Add to printed output
            for (ResolvedConceptReference neighbor : neighbors) {
                Util_displayMessage(neighbor.getCode() + ':' +
                        StringUtils.abbreviate(neighbor.getEntityDescription().getContent(), 60));
                for (String line : getAtomText(neighbor, sab).split("\\|"))
                    Util_displayMessage("    {" + StringUtils.abbreviate(line, 60) + '}');
            }
        }
    }

    /**
     * Populate child nodes for a single branch of the tree, and indicates
     * whether further expansion (to grandchildren) is possible.
     */
    protected void addChildren(TreeItem ti, String scheme, CodingSchemeVersionOrTag csvt,
            String sab, String branchRootCode, Set<String> codesToExclude,
            String[] associationsToNavigate, boolean associationsNavigatedFwd) throws LBException {

        LexBIGService lbsvc = getLexBIGService();

        // Resolve the next branch, representing children of the given
        // code, navigated according to the provided relationship and
        // direction. Resolve the children as a code graph, looking 2
        // levels deep but leaving the final level unresolved.
        CodedNodeGraph cng = lbsvc.getNodeGraph(scheme, csvt, null);
        ConceptReference focus = Constructors.createConceptReference(branchRootCode, scheme);
        cng = cng.restrictToAssociations(
                Constructors.createNameAndValueList(associationsToNavigate),
                ConvenienceMethods.createNameAndValueList(sab, "Source"));
        ResolvedConceptReferenceList branch = cng.resolveAsList(
                focus, associationsNavigatedFwd, !associationsNavigatedFwd,
                Integer.MAX_VALUE, 2,
                null, new PropertyType[] { PropertyType.PRESENTATION },
                sortByCode_, null, -1, true);

        // The resolved branch will be represented by the first node in
        // the resolved list. The node will be subdivided by source or
        // target associations (depending on direction). The associated
        // nodes define the children.
        for (ResolvedConceptReference node : branch.getResolvedConceptReference()) {
            AssociationList childAssociationList = associationsNavigatedFwd ? node.getSourceOf() : node.getTargetOf();

            // Process each association defining children ...
            for (Association child : childAssociationList.getAssociation()) {
                String childNavText = getDirectionalLabel(scheme, csvt, child, associationsNavigatedFwd);

                // Each association may have multiple children ...
                AssociatedConceptList branchItemList = child.getAssociatedConcepts();
                for (AssociatedConcept branchItemNode : branchItemList.getAssociatedConcept())
                    if (isValidForSAB(branchItemNode, sab)) {
                        String branchItemCode = branchItemNode.getCode();

                        // Add here if not in the list of excluded codes.
                        // This is also where we look to see if another level
                        // was indicated to be available. If so, mark the
                        // entry with a '+' to indicate it can be expanded.
                        if (!codesToExclude.contains(branchItemCode)) {
                            TreeItem childItem =
                                new TreeItem(branchItemCode,
                                    branchItemNode.getEntityDescription().getContent(),
                                    getAtomText(branchItemNode, sab));
                            AssociationList grandchildBranch =
                                associationsNavigatedFwd ? branchItemNode.getSourceOf()
                                    : branchItemNode.getTargetOf();
                            if (grandchildBranch != null)
                                childItem.expandable = true;
                            ti.addChild(childNavText, childItem);
                        }
                    }
            }
        }
    }

    /**
     * Build and returns tree items that represent the root
     * and core concepts of resolved paths for printing.
     * @throws LBException
     */
    protected TreeItem[] buildPathsToRoot(ResolvedConceptReference rcr,
            String scheme, CodingSchemeVersionOrTag csvt,
            String sab) throws LBException {

        // Create a starting point for tree building.
        TreeItem ti =
            new TreeItem(rcr.getCode(), rcr.getEntityDescription().getContent(),
                getAtomText(rcr, sab));

        // Maintain root tree items.
        Set<TreeItem> rootItems = new HashSet<TreeItem>();

        // Natural flow of hierarchy relations moves forward
        // from tree root to leaves.  Build the paths to root here
        // by processing upstream (child to parent) relationships.
        buildPathsToUpperNodes(
            ti, rcr, scheme, csvt, sab,
            new HashMap<String, TreeItem>(),
            rootItems);

        // Return root items discovered during child to parent
        // processing.
        return rootItems.toArray(new TreeItem[rootItems.size()]);
    }

    /**
     * Add all hierarchical paths to root that start from the
     * referenced concept and move backward in the tree. If
     * the natural flow of relations is thought of moving from tree
     * root to leaves, this method processes nodes in the
     * reverse direction (from child to parent).
     * @throws LBException
     */
    protected void buildPathsToUpperNodes(TreeItem ti, ResolvedConceptReference rcr,
            String scheme, CodingSchemeVersionOrTag csvt,
            String sab, Map<String, TreeItem> code2Tree,
            Set<TreeItem> roots)
        throws LBException {

        // Only need to process a code once ...
        if (code2Tree.containsKey(rcr.getCode()))
            return;

        // Cache for future reference.
        code2Tree.put(rcr.getCode(), ti);

        // UMLS relations can be defined with forward direction
        // being parent to child or child to parent on a source
        // by source basis.  Iterate twice to ensure completeness;
        // once navigating child to parent relations forward
        // and once navigating parent to child relations
        // backward.  Both have the net effect of navigating
        // from the bottom of the hierarchy to the top.
        boolean isRoot = true;
        for (int i = 0; i <= 1; i++) {
           boolean fwd = i < 1;
           String[] upstreamAssoc = fwd ? hierAssocToParentNodes_ : hierAssocToChildNodes_;

            // Define a code graph for all relationships tagged with
            // the specified sab.
            CodedNodeGraph graph = getLexBIGService().getNodeGraph(scheme, csvt, null);
            graph.restrictToAssociations(
                ConvenienceMethods.createNameAndValueList(upstreamAssoc),
                ConvenienceMethods.createNameAndValueList(sab, "Source"));

            // Resolve one hop, retrieving presentations for
            // comparison of source assignments.
            ResolvedConceptReference[] refs = graph.resolveAsList(
                rcr, fwd, !fwd, Integer.MAX_VALUE, 1,
                null, new PropertyType[] { PropertyType.PRESENTATION },
                sortByCode_, null, -1).getResolvedConceptReference();

            // Create a new tree item for each upstream node, add the current
            // tree item as a child, and recurse to go higher (if available).
            if (refs.length > 0) {

                // Each associated concept represents an upstream branch.
                AssociationList aList = fwd ? refs[0].getSourceOf() : refs[0].getTargetOf();
                for (Association assoc : aList.getAssociation()) {

                    // Go through the concepts one by one, adding the
                    // current tree item as a child of a new tree item
                    // representing the upstream node. If a tree item
                    // already exists for the parent, we reuse it to
                    // keep a single branch per parent.
                    for (AssociatedConcept refParent : assoc.getAssociatedConcepts().getAssociatedConcept())
                        if (isValidForSAB(refParent, sab)) {

                            // Fetch the term for this context ...
                            Presentation[] sabMatch = getSourcePresentations(refParent, sab);
                            if (sabMatch.length > 0) {

                                // We need to take into account direction of
                                // navigation on each pass to get the right label.
                                String directionalName = getDirectionalLabel(scheme, csvt, assoc, !fwd);

                                // Check for a previously registered item for the
                                // parent.  If found, re-use it.  Otherwise, create
                                // a new parent tree item.
                                String parentCode = refParent.getCode();
                                TreeItem tiParent = code2Tree.get(parentCode);
                                if (tiParent == null) {

                                    // Create a new tree item.
                                    tiParent =
                                        new TreeItem(parentCode, refParent.getEntityDescription().getContent(),
                                            getAtomText(refParent, sab));

                                    // Add immediate children of the parent code with an
                                    // indication of sub-nodes (+).  Codes already
                                    // processed as part of the path are ignored since
                                    // they are handled through recursion.
                                    String[] downstreamAssoc = fwd ? hierAssocToChildNodes_ : hierAssocToParentNodes_;
                                    addChildren(tiParent, scheme, csvt, sab, parentCode, code2Tree.keySet(),
                                            downstreamAssoc, fwd);

                                    // Try to go higher through recursion.
                                    buildPathsToUpperNodes(tiParent, refParent,
                                        scheme, csvt, sab, code2Tree, roots);
                                }

                                // Add the child
                                tiParent.addChild(directionalName, ti);
                                isRoot = false;
                            }
                        }
                }
            }
        }
        if (isRoot)
            roots.add(ti);
    }

    /**
     * Returns a resolved concept for the specified code and
     * scheme.
     * @throws LBException
     */
    protected ResolvedConceptReference resolveConcept(String scheme,
            CodingSchemeVersionOrTag csvt, String code)
            throws LBException {

        CodedNodeSet cns = getLexBIGService().getCodingSchemeConcepts(scheme, csvt);
        cns.restrictToMatchingProperties(ConvenienceMethods.createLocalNameList("conceptCode"),
            null, code, "exactMatch", null);
        ResolvedConceptReferenceList cnsList = cns.resolveToList(
            null, null,  new PropertyType[] { PropertyType.PRESENTATION },
            1);
        return (cnsList.getResolvedConceptReferenceCount() == 0) ? null
                : cnsList.getResolvedConceptReference(0);
    }

    /**
     * Returns a cached instance of a LexBIG service.
     */
    protected LexBIGService getLexBIGService() throws LBException {
        if (lbsvc_ == null)
            lbsvc_ = LexBIGServiceImpl.defaultInstance();
        return lbsvc_;
    }

    /**
     * Returns a cached instance of convenience methods.
     */
    protected LexBIGServiceConvenienceMethods getConvenienceMethods() throws LBException {
        if (lbscm_ == null)
            lbscm_ = (LexBIGServiceConvenienceMethods)
                getLexBIGService().getGenericExtension("LexBIGServiceConvenienceMethods");
        return lbscm_;
    }

    /**
     * Returns the label to display for the given association and directional
     * indicator.
     */
    protected String getDirectionalLabel(LexBIGServiceConvenienceMethods lbscm, String scheme, CodingSchemeVersionOrTag csvt,
            Association assoc, boolean navigatedFwd) throws LBException {

        String assocLabel = navigatedFwd ? lbscm.getAssociationForwardName(assoc.getAssociationName(), scheme, csvt)
                : lbscm.getAssociationReverseName(assoc.getAssociationName(), scheme, csvt);
        if (StringUtils.isBlank(assocLabel))
            assocLabel = (navigatedFwd ? "" : "[Inverse]") + assoc.getAssociationName();
        return assocLabel;
    }

    protected String getDirectionalLabel(String scheme, CodingSchemeVersionOrTag csvt,
            Association assoc, boolean navigatedFwd) throws LBException {

        //LexBIGServiceConvenienceMethods lbscm = getConvenienceMethods();

		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbSvc
				.getGenericExtension("LexBIGServiceConvenienceMethods");
		lbscm.setLexBIGService(lbSvc);

        String assocLabel = navigatedFwd ? lbscm.getAssociationForwardName(assoc.getAssociationName(), scheme, csvt)
                : lbscm.getAssociationReverseName(assoc.getAssociationName(), scheme, csvt);
        if (StringUtils.isBlank(assocLabel))
            assocLabel = (navigatedFwd ? "" : "[Inverse]") + assoc.getAssociationName();
        return assocLabel;
    }

    /**
     * Returns a string representing the AUIs and
     * text presentations applicable only for the
     * given source abbreviation (SAB). All AUI
     * text combinations are qualified by SAB and
     * delimited by '|'.
     */
    protected String getAtomText(ResolvedConceptReference rcr, String sab) {
        StringBuffer text = new StringBuffer();
        boolean first = true;
        for (Presentation p : getSourcePresentations(rcr, sab)) {
            if (!first)
                text.append('|');
            text.append(sab).append(':')
                .append(getAtomText(p)).append(':')
                .append('\'')
                .append(p.getValue().getContent())
                .append('\'');
            first = false;
        }
        return
            text.length() > 0 ? text.toString()
                : "<No Match for SAB>";
    }

    /**
     * Returns text for AUI qualifiers for the given property.
     * This method iterates through available property qualifiers.
     * Typically only one AUI is expected.  If more are
     * discovered, returned values are delimited by '|'.
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
        return
            text.length() > 0 ? text.toString()
                : "<No AUI>";
    }

    /**
     * Returns all assigned presentations matching the given
     * source abbreviation (SAB). This method iterates through the
     * available presentations to find any qualified to match
     * the specified source.
     */
    protected Presentation[] getSourcePresentations(ResolvedConceptReference rcr, String sab) {

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
     * Indicates whether the given associated concept contains
     * a qualifier for the given source abbreviation (SAB).
     * @return true if a qualifier exists; false otherwise.
     */
    protected boolean isValidForSAB(AssociatedConcept ac, String sab) {
        for (NameAndValue qualifier : ac.getAssociationQualifiers().getNameAndValue())
            if ("Source".equalsIgnoreCase(qualifier.getContent())
                    && sab.equalsIgnoreCase(qualifier.getName()))
                return true;
        return false;
    }


////////////////////////


	public HashMap getSubconcepts(String scheme, String version, String code, String sab)
	{
        HashMap hmap = new HashMap();
        TreeItem ti = null;
        long ms = System.currentTimeMillis();

        Set<String> codesToExclude = Collections.EMPTY_SET;
        boolean fwd = true;
        String[] associationsToNavigate = fwd ? hierAssocToChildNodes_ : hierAssocToParentNodes_;
        boolean associationsNavigatedFwd = true;

		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		if (version != null) csvt.setVersion(version);
		ResolvedConceptReferenceList matches = null;
		//Vector v = new Vector();
		try {
			LexBIGService lbsvc = RemoteServerUtil.createLexBIGService();
			LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbsvc
					.getGenericExtension("LexBIGServiceConvenienceMethods");
			lbscm.setLexBIGService(lbsvc);
            String name = getCodeDescription(lbsvc, scheme, csvt, code);
            ti = new TreeItem(code, name);
            ti.expandable = false;

			// Resolve the next branch, representing children of the given
			// code, navigated according to the provided relationship and
			// direction. Resolve the children as a code graph, looking 2
			// levels deep but leaving the final level unresolved.

			CodedNodeGraph cng = lbsvc.getNodeGraph(scheme, csvt, null);
			ConceptReference focus = Constructors.createConceptReference(code, scheme);
			cng = cng.restrictToAssociations(
					Constructors.createNameAndValueList(associationsToNavigate),
					ConvenienceMethods.createNameAndValueList(sab, "Source"));
			ResolvedConceptReferenceList branch = cng.resolveAsList(
					focus, associationsNavigatedFwd, !associationsNavigatedFwd,
					Integer.MAX_VALUE, 2,
					null, new PropertyType[] { PropertyType.PRESENTATION },
					sortByCode_, null, -1, true);

			// The resolved branch will be represented by the first node in
			// the resolved list. The node will be subdivided by source or
			// target associations (depending on direction). The associated
			// nodes define the children.

			for (ResolvedConceptReference node : branch.getResolvedConceptReference()) {
				AssociationList childAssociationList = associationsNavigatedFwd ? node.getSourceOf() : node.getTargetOf();
				// Process each association defining children ...
				for (Association child : childAssociationList.getAssociation()) {
					String childNavText = getDirectionalLabel(lbscm, scheme, csvt, child, associationsNavigatedFwd);
					// Each association may have multiple children ...
					AssociatedConceptList branchItemList = child.getAssociatedConcepts();
					for (AssociatedConcept branchItemNode : branchItemList.getAssociatedConcept()) {
						if (isValidForSAB(branchItemNode, sab)) {
							String branchItemCode = branchItemNode.getCode();
							// Add here if not in the list of excluded codes.
							// This is also where we look to see if another level
							// was indicated to be available. If so, mark the
							// entry with a '+' to indicate it can be expanded.
							if (!codesToExclude.contains(branchItemCode)) {
								ti.expandable = true;
								/*
								TreeItem childItem =
									new TreeItem(branchItemCode,
										branchItemNode.getEntityDescription().getContent(),
										getAtomText(branchItemNode, sab));
								*/
								TreeItem childItem =
									new TreeItem(branchItemCode,
										branchItemNode.getEntityDescription().getContent());

								AssociationList grandchildBranch =
									associationsNavigatedFwd ? branchItemNode.getSourceOf()
										: branchItemNode.getTargetOf();
								if (grandchildBranch != null)
									childItem.expandable = true;
								ti.addChild(childNavText, childItem);
							}
						}
				    }
			    }
			}
			hmap.put(code, ti);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("Run time (milliseconds) getSubconcepts: " + (System.currentTimeMillis() - ms) + " to resolve " );
		return hmap;
    }

    ///////////////////////////////////////////////////////
    // Helper Methods
    ///////////////////////////////////////////////////////

    /**
     * Returns the entity description for the given code.
     */
    protected String getCodeDescription(LexBIGService lbsvc, String scheme, CodingSchemeVersionOrTag csvt, String code)
            throws LBException {

        CodedNodeSet cns = lbsvc.getCodingSchemeConcepts(scheme, csvt);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(code, scheme));
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, noopList_, null, 1);
        if (rcrl.getResolvedConceptReferenceCount() > 0) {
            EntityDescription desc = rcrl.getResolvedConceptReference(0).getEntityDescription();
            if (desc != null)
                return desc.getContent();
        }
        return "<Not assigned>";
    }

    /**
     * Returns the entity description for the given resolved concept reference.
     */
    protected String getCodeDescription(ResolvedConceptReference ref) throws LBException {
        EntityDescription desc = ref.getEntityDescription();
        if (desc != null)
            return desc.getContent();
        return "<Not assigned>";
    }

}