package gov.nih.nci.evs.browser.utils;

/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 */

/**
  * @author EVS Team
  * @version 1.0
  *
  * Note: This class is created based on Mayo's BuildTreForCode.java sample code
  *
  * Modification history
  *     Initial modification kim.ong@ngc.com
  *
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.io.Serializable;


import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.HierarchyPathResolveOption;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.naming.SupportedHierarchy;
import org.apache.commons.lang.StringUtils;

import org.LexGrid.codingSchemes.Mappings;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.concepts.Concept;

import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;

/**
 * Attempts to provide a tree, based on a focus code, that includes the
 * following information:
 * <pre>
 * - All paths from the hierarchy root to one or more focus codes.
 * - Immediate children of every node in path to root
 * - Indicator to show whether any unexpanded node can be further expanded
 * </pre>
 *
 * This example accepts two parameters...
 * The first parameter is required, and must contain at least one code
 * in a comma-delimited list. A tree is produced for each code. Time to
 * produce the tree for each code is printed in milliseconds. In order
 * to factor out costs of startup and shutdown, resolving multiple
 * codes may offer a better overall estimate performance.
 *
 * The second parameter is optional, and can indicate a hierarchy ID to
 * navigate when resolving child nodes. If not provided, "is_a" is
 * assumed.
 */
public class TreeUtils {
    LocalNameList noopList_ = Constructors.createLocalNameList("_noop_");

    public TreeUtils() {

    }

    public HashMap getTreePathData(String scheme, String version, String hierarchyID, String code) throws LBException {
		/*
		LexBIGService lbsvc = RemoteServerUtil.createLexBIGService();
		LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbsvc
				.getGenericExtension("LexBIGServiceConvenienceMethods");
		lbscm.setLexBIGService(lbsvc);
		if (hierarchyID == null) hierarchyID = "is_a";
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		if (version != null) csvt.setVersion(version);
		SupportedHierarchy hierarchyDefn = getSupportedHierarchy(lbsvc, scheme, csvt, hierarchyID);
 		return getTreePathData(lbsvc, lbscm, scheme, csvt, hierarchyDefn, code);
 		*/
 		return getTreePathData(scheme, version, hierarchyID, code, -1);
    }


    public HashMap getTreePathData(String scheme, String version, String hierarchyID, String code, int maxLevel) throws LBException {
		LexBIGService lbsvc = RemoteServerUtil.createLexBIGService();
		LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbsvc
				.getGenericExtension("LexBIGServiceConvenienceMethods");
		lbscm.setLexBIGService(lbsvc);
		if (hierarchyID == null) hierarchyID = "is_a";
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		if (version != null) csvt.setVersion(version);
		SupportedHierarchy hierarchyDefn = getSupportedHierarchy(lbsvc, scheme, csvt, hierarchyID);
 		return getTreePathData(lbsvc, lbscm, scheme, csvt, hierarchyDefn, code, maxLevel);
    }


    public HashMap getTreePathData(LexBIGService lbsvc, LexBIGServiceConvenienceMethods lbscm, String scheme,
            CodingSchemeVersionOrTag csvt, SupportedHierarchy hierarchyDefn, String focusCode) throws LBException {
		return getTreePathData(lbsvc, lbscm, scheme, csvt, hierarchyDefn, focusCode, -1);
	}


    public HashMap getTreePathData(LexBIGService lbsvc, LexBIGServiceConvenienceMethods lbscm, String scheme,
            CodingSchemeVersionOrTag csvt, SupportedHierarchy hierarchyDefn, String focusCode, int maxLevel) throws LBException {
        HashMap hmap = new HashMap();
        TreeItem ti = new TreeItem("<Root>", "Root node");
        long ms = System.currentTimeMillis();
        int pathsResolved = 0;
        try {

            // Resolve 'is_a' hierarchy info. This example will
            // need to make some calls outside of what is covered
            // by existing convenience methods, but we use the
            // registered hierarchy to prevent need to hard code
            // relationship and direction info used on lookup ...
            String hierarchyID = hierarchyDefn.getLocalId();
            String[] associationsToNavigate = hierarchyDefn.getAssociationIds();
            boolean associationsNavigatedFwd = hierarchyDefn.getIsForwardNavigable();

            // Identify the set of all codes on path from root
            // to the focus code ...
            Map<String, EntityDescription> codesToDescriptions = new HashMap<String, EntityDescription>();
            AssociationList pathsFromRoot = getPathsFromRoot(lbsvc, lbscm, scheme, csvt, hierarchyID, focusCode,
                    codesToDescriptions, maxLevel);

            // Typically there will be one path, but handle multiple just in
            // case.  Each path from root provides a 'backbone', from focus
            // code to root, for additional nodes to hang off of in our
            // printout. For every backbone node, one level of children is
            // printed, along with an indication of whether those nodes can
            // be expanded.

            for (Iterator<Association> paths = pathsFromRoot.iterateAssociation(); paths.hasNext();) {
                addPathFromRoot(ti, lbsvc, lbscm, scheme, csvt, paths.next(), associationsToNavigate, associationsNavigatedFwd,
                        codesToDescriptions);
                pathsResolved++;
            }

        } finally {
            System.out.println("Run time (milliseconds): " + (System.currentTimeMillis() - ms) +
                " to resolve " + pathsResolved + " paths from root.");
        }

        hmap.put(focusCode, ti);
        return hmap;

    }

    public void run(String scheme, String version, String hierarchyId, String focusCode) throws LBException {
        HashMap hmap = getTreePathData(scheme, version, hierarchyId, focusCode);
        Set keyset = hmap.keySet();
        Object[] objs = keyset.toArray();
        String code = (String) objs[0];
        TreeItem ti = (TreeItem) hmap.get(code);
        printTree(ti, focusCode, 0);
    }


    public void printTree(HashMap hmap) {
        Object[] objs = hmap.keySet().toArray();
        String code = (String) objs[0];
        TreeItem ti = (TreeItem) hmap.get(code);
        printTree(ti, code, 0);
	}



    protected void addPathFromRoot(TreeItem ti, LexBIGService lbsvc, LexBIGServiceConvenienceMethods lbscm,
            String scheme, CodingSchemeVersionOrTag csvt, Association path, String[] associationsToNavigate,
            boolean associationsNavigatedFwd, Map<String, EntityDescription> codesToDescriptions) throws LBException {

        // First, add the branch point from the path ...
        ConceptReference branchRoot = path.getAssociationReference();
        //=======================================================================v.50
        String branchCode = branchRoot.getConceptCode();
        String branchCodeDescription = codesToDescriptions.containsKey(branchCode) ? codesToDescriptions
                .get(branchCode).getContent() : getCodeDescription(lbsvc, scheme, csvt, branchCode);

        TreeItem branchPoint = new TreeItem(branchCode, branchCodeDescription);
        String branchNavText = getDirectionalLabel(lbscm, scheme, csvt, path, associationsNavigatedFwd);

        // Now process elements in the branch ...
        AssociatedConceptList concepts = path.getAssociatedConcepts();
        for (int i = 0; i < concepts.getAssociatedConceptCount(); i++) {

            // Determine the next concept in the branch and
            // add a corresponding item to the tree.
            AssociatedConcept concept = concepts.getAssociatedConcept(i);
            String code = concept.getConceptCode();
            TreeItem branchItem = new TreeItem(code, getCodeDescription(concept));
            branchPoint.addChild(branchNavText, branchItem);

            // Recurse to process the remainder of the backbone ...
            AssociationList nextLevel = concept.getSourceOf();
            if (nextLevel != null) {
                if (nextLevel.getAssociationCount() != 0) {
                    // Add immediate children of the focus code with an
                    // indication of sub-nodes (+).  Codes already
                    // processed as part of the path are ignored since
                    // they are handled through recursion.
                    addChildren(branchItem, lbsvc, lbscm, scheme, csvt, code, codesToDescriptions.keySet(),
                            associationsToNavigate, associationsNavigatedFwd);

                    // More levels left to process ...
                    for (int j = 0; j < nextLevel.getAssociationCount(); j++)
                        addPathFromRoot(branchPoint, lbsvc, lbscm, scheme, csvt, nextLevel.getAssociation(j),
                                associationsToNavigate, associationsNavigatedFwd, codesToDescriptions);
                } else {
                    // End of the line ...
                    // Always add immediate children of the focus code,
                    // in this case with no exclusions since we are moving
                    // beyond the path to root and allowed to duplicate
                    // nodes that may have occurred in the path to root.
                    addChildren(branchItem, lbsvc, lbscm, scheme, csvt, code, Collections.EMPTY_SET,
                            associationsToNavigate, associationsNavigatedFwd);
                }
            } else {
                // Add immediate children of the focus code with an
                // indication of sub-nodes (+).  Codes already
                // processed as part of the path are ignored since
                // they are handled through recursion.
                addChildren(branchItem, lbsvc, lbscm, scheme, csvt, code, codesToDescriptions.keySet(),
                        associationsToNavigate, associationsNavigatedFwd);
            }
        }

        // Add immediate children of the node being processed,
        // and indication of sub-nodes.
        addChildren(branchPoint, lbsvc, lbscm, scheme, csvt, branchCode, codesToDescriptions.keySet(),
                associationsToNavigate, associationsNavigatedFwd);

        // Add the populated tree item to those tracked from root.
        ti.addChild(branchNavText, branchPoint);
    }

    /**
     * Populate child nodes for a single branch of the tree, and indicates
     * whether further expansion (to grandchildren) is possible.
     */
    protected void addChildren(TreeItem ti, LexBIGService lbsvc, LexBIGServiceConvenienceMethods lbscm, String scheme,
            CodingSchemeVersionOrTag csvt, String branchRootCode, Set<String> codesToExclude,
            String[] associationsToNavigate, boolean associationsNavigatedFwd) throws LBException {

        // Resolve the next branch, representing children of the given
        // code, navigated according to the provided relationship and
        // direction. Resolve the children as a code graph, looking 2
        // levels deep but leaving the final level unresolved.
        CodedNodeGraph cng = lbsvc.getNodeGraph(scheme, csvt, null);
        ConceptReference focus = Constructors.createConceptReference(branchRootCode, scheme);
        cng = cng.restrictToAssociations(Constructors.createNameAndValueList(associationsToNavigate), null);
        ResolvedConceptReferenceList branch = cng.resolveAsList(focus, associationsNavigatedFwd,
                !associationsNavigatedFwd, -1, 2, noopList_, null, null, null, -1, true);

        // The resolved branch will be represented by the first node in
        // the resolved list. The node will be subdivided by source or
        // target associations (depending on direction). The associated
        // nodes define the children.
        for (Iterator<ResolvedConceptReference> nodes = branch.iterateResolvedConceptReference(); nodes.hasNext();) {
            ResolvedConceptReference node = nodes.next();
            AssociationList childAssociationList = associationsNavigatedFwd ? node.getSourceOf() : node.getTargetOf();

            // Process each association defining children ...
            for (Iterator<Association> pathsToChildren = childAssociationList.iterateAssociation(); pathsToChildren
                    .hasNext();) {
                Association child = pathsToChildren.next();
                String childNavText = getDirectionalLabel(lbscm, scheme, csvt, child, associationsNavigatedFwd);

                // Each association may have multiple children ...
                AssociatedConceptList branchItemList = child.getAssociatedConcepts();
                for (Iterator<AssociatedConcept> branchNodes = branchItemList.iterateAssociatedConcept(); branchNodes
                        .hasNext();) {
                    AssociatedConcept branchItemNode = branchNodes.next();
                    String branchItemCode = branchItemNode.getConceptCode();

                    // Add here if not in the list of excluded codes.
                    // This is also where we look to see if another level
                    // was indicated to be available. If so, mark the
                    // entry with a '+' to indicate it can be expanded.
                    if (!codesToExclude.contains(branchItemCode)) {
                        TreeItem childItem = new TreeItem(branchItemCode, getCodeDescription(branchItemNode));
                        AssociationList grandchildBranch = associationsNavigatedFwd ? branchItemNode.getSourceOf()
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
     * Prints the given tree item, recursing through all branches.
     * @param ti
     */

    //protected void printTree(TreeItem ti, String focusCode, int depth) {
	public void printTree(TreeItem ti, String focusCode, int depth) {
        StringBuffer indent = new StringBuffer();
        for (int i = 0; i < depth * 2; i++)
            indent.append("| ");

        StringBuffer codeAndText = new StringBuffer(indent)
            .append(focusCode.equals(ti.code) ? ">>>>" : "")
            .append(ti.code).append(':')
            .append(ti.text.length() > 64 ? ti.text.substring(0, 62) + "..." : ti.text)
            .append(ti.expandable ? " [+]" : "");
        System.out.println(codeAndText.toString());

        indent.append("| ");
        for (String association : ti.assocToChildMap.keySet()) {
            System.out.println(indent.toString() + association);
            List<TreeItem> children = ti.assocToChildMap.get(association);
            Collections.sort(children);
            for (TreeItem childItem : children)
                printTree(childItem, focusCode, depth + 1);
        }
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


	public static boolean isBlank(String str) {
		if ((str == null) || str.matches("^\\s*$")) {
		    return true;
		} else {
			return false;
	    }
	}


    /**
     * Returns the label to display for the given association and directional
     * indicator.
     */
    protected String getDirectionalLabel(LexBIGServiceConvenienceMethods lbscm,
            String scheme, CodingSchemeVersionOrTag csvt,
            Association assoc, boolean navigatedFwd)
        throws LBException {

        String assocLabel = navigatedFwd
            ? lbscm.getAssociationForwardName(assoc.getAssociationName(), scheme, csvt)
            : lbscm.getAssociationReverseName(assoc.getAssociationName(), scheme, csvt);
        //if (StringUtils.isBlank(assocLabel))
        if (isBlank(assocLabel))
            assocLabel = (navigatedFwd ? "" : "[Inverse]") + assoc.getAssociationName();
        return assocLabel;
    }

    /**
     * Resolves one or more paths from the hierarchy root to the given code
     * through a list of connected associations defined by the hierarchy.
     */
    protected AssociationList getPathsFromRoot(LexBIGService lbsvc, LexBIGServiceConvenienceMethods lbscm,
            String scheme, CodingSchemeVersionOrTag csvt, String hierarchyID, String focusCode,
            Map<String, EntityDescription> codesToDescriptions) throws LBException {
        return getPathsFromRoot(lbsvc, lbscm, scheme, csvt, hierarchyID, focusCode, codesToDescriptions, -1);

    }


    protected AssociationList getPathsFromRoot(LexBIGService lbsvc, LexBIGServiceConvenienceMethods lbscm,
            String scheme, CodingSchemeVersionOrTag csvt, String hierarchyID, String focusCode,
            Map<String, EntityDescription> codesToDescriptions, int maxLevel) throws LBException {
        // Get paths from the focus code to the root from the
        // convenience method.  All paths are resolved.  If only
        // one path is required, it would be possible to use
        // HierarchyPathResolveOption.ONE to reduce processing
        // and improve overall performance.
        AssociationList pathToRoot = lbscm.getHierarchyPathToRoot(scheme, csvt, null, focusCode, false,
                HierarchyPathResolveOption.ALL, null);

        // But for purposes of this example we need to display info
        // in order coming from root direction. Process the paths to root
        // recursively to reverse the order for processing ...
        AssociationList pathFromRoot = new AssociationList();
        for (int i = pathToRoot.getAssociationCount() - 1; i >= 0; i--)
            reverseAssoc(lbsvc, lbscm, scheme, csvt, pathToRoot.getAssociation(i), pathFromRoot, codesToDescriptions, maxLevel, 0);

        return pathFromRoot;
    }


    /**
     * Returns a description of the hierarchy defined by the given coding
     * scheme and matching the specified ID.
     */
    protected static SupportedHierarchy getSupportedHierarchy(LexBIGService lbsvc, String scheme,
            CodingSchemeVersionOrTag csvt, String hierarchyID) throws LBException {

        CodingScheme cs = lbsvc.resolveCodingScheme(scheme, csvt);
        if (cs == null) {
            throw new LBResourceUnavailableException("Coding scheme not found: " + scheme);
        }
        for (SupportedHierarchy h : cs.getMappings().getSupportedHierarchy())
            if (h.getLocalId().equals(hierarchyID))
                return h;
        throw new LBResourceUnavailableException("Hierarchy not defined: " + hierarchyID);
    }



    ///////////////////////////////////////////////////////
    // Helper classes
    ///////////////////////////////////////////////////////

    /**
     * Inner class to hold tree items for printout.
     */
    public class TreeItem implements Serializable, Comparable<TreeItem> {
        public String code = null;
        public String text = null;
        public boolean expandable = false;
        public Map<String, List<TreeItem>> assocToChildMap = new TreeMap<String, List<TreeItem>>();
        public boolean equals(Object o) {
            return o instanceof TreeItem
                && code.compareTo(((TreeItem) o).code) == 0;
        }
        public int compareTo(TreeItem ti) {
            String c1 = code;
            String c2 = ti.code;
            if (c1.startsWith("@")) return 1;
            if (c2.startsWith("@")) return -1;
            return c1.compareTo(c2);
        }
        public TreeItem(String code, String text) {
            super();
            this.code = code;
            this.text = text;
        }
        public void addAll(String assocText, List<TreeItem> children) {
            for (TreeItem item : children)
                addChild(assocText, item);
        }
        public void addChild(String assocText, TreeItem child) {
            List<TreeItem> children = assocToChildMap.get(assocText);
            if (children == null) {
                children = new ArrayList<TreeItem>();
                assocToChildMap.put(assocText, children);
            }
            int i;
            if ((i = children.indexOf(child)) >= 0) {
                TreeItem existingTreeItem = children.get(i);
                for (String assoc : child.assocToChildMap.keySet()) {
                    List<TreeItem> toAdd = child.assocToChildMap.get(assoc);
                    if (!toAdd.isEmpty()) {
                        existingTreeItem.addAll(assoc, toAdd);
                        existingTreeItem.expandable = false;
                    }
                }
            } else
                children.add(child);
        }
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public HashMap getSubconcepts(String scheme, String version, String code)
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

	public HashMap getAssociationTargets(String scheme, String version, String code, String assocName)
	{
        HashMap hmap = new HashMap();
        TreeItem ti = null;
        long ms = System.currentTimeMillis();

        Set<String> codesToExclude = Collections.EMPTY_SET;

		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		if (version != null) csvt.setVersion(version);
		ResolvedConceptReferenceList matches = null;
		Vector v = new Vector();
		try {
			//EVSApplicationService lbSvc = new RemoteServerUtil().createLexBIGService();
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbSvc
					.getGenericExtension("LexBIGServiceConvenienceMethods");
			lbscm.setLexBIGService(lbSvc);

            String name = getCodeDescription(lbSvc, scheme, csvt, code);
            ti = new TreeItem(code, name);
            ti.expandable = false;

			CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
			ConceptReference focus = Constructors.createConceptReference(code, scheme);
			cng = cng.restrictToAssociations(Constructors.createNameAndValueList(assocName), null);
			boolean associationsNavigatedFwd = true;
			ResolvedConceptReferenceList branch = cng.resolveAsList(focus, associationsNavigatedFwd,
					!associationsNavigatedFwd, -1, 2, noopList_, null, null, null, -1, true);

			for (Iterator<ResolvedConceptReference> nodes = branch.iterateResolvedConceptReference(); nodes.hasNext();) {
				ResolvedConceptReference node = nodes.next();
				AssociationList childAssociationList = associationsNavigatedFwd ? node.getSourceOf() : node.getTargetOf();

				// Process each association defining children ...
				for (Iterator<Association> pathsToChildren = childAssociationList.iterateAssociation(); pathsToChildren.hasNext();) {
					Association child = pathsToChildren.next();
					String childNavText = getDirectionalLabel(lbscm, scheme, csvt, child, associationsNavigatedFwd);

					// Each association may have multiple children ...
					AssociatedConceptList branchItemList = child.getAssociatedConcepts();

                    /*
					for (Iterator<AssociatedConcept> branchNodes = branchItemList.iterateAssociatedConcept(); branchNodes
							.hasNext();) {
						AssociatedConcept branchItemNode = branchNodes.next();
					*/

					List child_list = new ArrayList();
					for (Iterator<AssociatedConcept> branchNodes = branchItemList.iterateAssociatedConcept(); branchNodes
							.hasNext();) {
						AssociatedConcept branchItemNode = branchNodes.next();
                        child_list.add(branchItemNode);
					}

					SortUtils.quickSort(child_list);

					for (int i=0; i<child_list.size(); i++) {
					    AssociatedConcept branchItemNode = (AssociatedConcept) child_list.get(i);
						String branchItemCode = branchItemNode.getConceptCode();
						// Add here if not in the list of excluded codes.
						// This is also where we look to see if another level
						// was indicated to be available.  If so, mark the
						// entry with a '+' to indicate it can be expanded.
						if (!codesToExclude.contains(branchItemCode)) {
							TreeItem childItem = new TreeItem(branchItemCode, getCodeDescription(branchItemNode));
							ti.expandable = true;
							AssociationList grandchildBranch =
								associationsNavigatedFwd
									? branchItemNode.getSourceOf()
									: branchItemNode.getTargetOf();
							if (grandchildBranch != null)
								childItem.expandable = true;
							ti.addChild(childNavText, childItem);
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

//Focus code: C7387 C26709


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Configurable tree size (MAXIMUM_TREE_LEVEL)
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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
			cr.setCodingScheme(codingSchemeName);
			cr.setConceptCode(codes[i]);
			list.addConceptReference(cr);
		}
		return list;
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
			ResolvedConceptReferenceList matches = cns.resolveToList(null, null, null, 1);

			if (matches == null)
			{
				System.out.println("Concep not found.");
				return null;
			}

			// Analyze the result ...
			if (matches.getResolvedConceptReferenceCount() > 0) {
				ResolvedConceptReference ref =
					(ResolvedConceptReference) matches.enumerateResolvedConceptReference().nextElement();

				Concept entry = ref.getReferencedEntry();
				return entry;
			}
		 } catch (Exception e) {
			 e.printStackTrace();
			 return null;
		 }
		 return null;
	}

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



    protected AssociationList reverseAssoc(LexBIGService lbsvc, LexBIGServiceConvenienceMethods lbscm, String scheme,
            CodingSchemeVersionOrTag csvt, Association assoc, AssociationList addTo,
            Map<String, EntityDescription> codeToEntityDescriptionMap, int maxLevel, int currLevel) throws LBException {

        if (maxLevel != -1 && currLevel >= maxLevel) return addTo;

        ConceptReference acRef = assoc.getAssociationReference();
        AssociatedConcept acFromRef = new AssociatedConcept();
        //===============================================================================v5.0
        acFromRef.setConceptCode(acRef.getConceptCode());
        AssociationList acSources = new AssociationList();
        acFromRef.setIsNavigable(Boolean.TRUE);
        acFromRef.setSourceOf(acSources);

        // Use cached description if available (should be cached
        // for all but original root) ...
        if (codeToEntityDescriptionMap.containsKey(acRef.getConceptCode()))
            acFromRef.setEntityDescription(codeToEntityDescriptionMap.get(acRef.getConceptCode()));
        // Otherwise retrieve on demand ...
        else
            acFromRef.setEntityDescription(Constructors.createEntityDescription(getCodeDescription(lbsvc, scheme, csvt,
                    acRef.getConceptCode())));

        AssociatedConceptList acl = assoc.getAssociatedConcepts();
        for (AssociatedConcept ac : acl.getAssociatedConcept()) {
            // Create reverse association (same non-directional name)
            Association rAssoc = new Association();
            rAssoc.setAssociationName(assoc.getAssociationName());

            // On reverse, old associated concept is new reference point.
            ConceptReference ref = new ConceptReference();
            //===============================================================================v5.0
            //ref.setCodingSchemeName(ac.getCodingSchemeName());
            ref.setConceptCode(ac.getConceptCode());
            rAssoc.setAssociationReference(ref);

            // And old reference is new associated concept.
            AssociatedConceptList rAcl = new AssociatedConceptList();
            rAcl.addAssociatedConcept(acFromRef);
            rAssoc.setAssociatedConcepts(rAcl);

            // Set reverse directional name, if available.
            String dirName = assoc.getDirectionalName();
            if (dirName != null)
                try {
                    rAssoc.setDirectionalName(lbscm.isForwardName(scheme, csvt, dirName) ? lbscm
                            .getAssociationReverseName(assoc.getAssociationName(), scheme, csvt) : lbscm
                            .getAssociationReverseName(assoc.getAssociationName(), scheme, csvt));
                } catch (LBException e) {
                }

            // Save code desc for future reference when setting up
            // concept references in recursive calls ...
            codeToEntityDescriptionMap.put(ac.getConceptCode(), ac.getEntityDescription());

            AssociationList sourceOf = ac.getSourceOf();
            if (sourceOf != null)
                for (Association sourceAssoc : sourceOf.getAssociation()) {
                    AssociationList pos = reverseAssoc(lbsvc, lbscm, scheme, csvt, sourceAssoc, addTo,
                            codeToEntityDescriptionMap, maxLevel, currLevel+1);
                    pos.addAssociation(rAssoc);
                }
            else
                addTo.addAssociation(rAssoc);
        }
        return acSources;
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

            for (int k=0; k<hierarchies.length; k++) {
				java.lang.String[] ids = hierarchies[k].getAssociationIds();

				for (int i=0; i<ids.length; i++)
				{
					if (!association_vec.contains(ids[i])) {
						association_vec.add(ids[i]);
						//System.out.println(ids[i]);
					}
				}
		    }

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return association_vec;
	}


    public String getHierarchyAssociationId(String scheme, String version, int index) {
		try {
			Vector hierarchicalAssoName_vec = getHierarchyAssociationId(scheme, version);
			if (hierarchicalAssoName_vec != null && hierarchicalAssoName_vec.size() > 0)
			{
				String hierarchicalAssoName = (String) hierarchicalAssoName_vec.elementAt(0);
				return hierarchicalAssoName;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}



    public List getTopNodes(TreeItem ti) {
		List list = new ArrayList();
		getTopNodes(ti, list, 0, 1);
		return list;
	}


    public void getTopNodes(TreeItem ti, List list, int currLevel, int maxLevel) {
        if (list == null) list = new ArrayList();
        if (currLevel > maxLevel) return;
        if (ti.assocToChildMap.keySet().size() > 0) {
			if (ti.text.compareTo("Root node") != 0)
			{
				ResolvedConceptReference rcr = new ResolvedConceptReference();
				rcr.setConceptCode(ti.code);
				EntityDescription entityDescription = new EntityDescription();
				entityDescription.setContent(ti.text);
				rcr.setEntityDescription(entityDescription);
				//System.out.println("Root: " + ti.text);
				list.add(rcr);
		    }
		}

        for (String association : ti.assocToChildMap.keySet()) {
            List<TreeItem> children = ti.assocToChildMap.get(association);
            Collections.sort(children);
            for (TreeItem childItem : children) {
                getTopNodes(childItem, list, currLevel+1, maxLevel);
			}
        }
    }


	public static void main(String[] args)
	{
		 String url = "http://lexevsapi-dev.nci.nih.gov/lexevsapi42";
		 url = "http://lexevsapi.nci.nih.gov/lexevsapi42";

         String scheme = "NCI Thesaurus";
         String version = null;
         String code = "C26709";

         TreeUtils test = new TreeUtils();
         HashMap hmap = test.getSubconcepts(scheme, version, code);
         test.printTree(hmap);

         code = "C2910";
         hmap = test.getSubconcepts(scheme, version, code);
         test.printTree(hmap);

	 }
}

