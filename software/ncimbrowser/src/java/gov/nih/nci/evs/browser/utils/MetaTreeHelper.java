package gov.nih.nci.evs.browser.utils;

import java.util.*;
//import net.sf.ehcache.*;
import gov.nih.nci.evs.browser.properties.*;
import org.json.*;

import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;

import org.LexGrid.lexevs.metabrowser.*;
import org.LexGrid.lexevs.metabrowser.model.*;
import org.LexGrid.lexevs.metabrowser.model.MetaTreeNode.*;
import org.LexGrid.lexevs.metabrowser.impl.*;

import org.apache.logging.log4j.*;
import org.LexGrid.lexevs.metabrowser.helper.*;

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

public class MetaTreeHelper {
	private static Logger _logger = LogManager.getLogger(MetaTreeHelper.class);
    public static final String ONTOLOGY_ADMINISTRATORS =
        "ontology_administrators";
    public static final String ONTOLOGY_FILE = "ontology_file";
    public static final String ONTOLOGY_FILE_ID = "ontology_file_id";
    public static final String ONTOLOGY_DISPLAY_NAME = "ontology_display_name";
    public static final String ONTOLOGY_NODE = "ontology_node";
    public static final String ONTOLOGY_NODE_ID = "ontology_node_id";

    public static final String ONTOLOGY_SOURCE = "ontology_source";

    public static final String ONTOLOGY_NODE_NAME = "ontology_node_name";
    public static final String ONTOLOGY_NODE_PARENT_ASSOC =
        "ontology_node_parent_assoc";
    public static final String ONTOLOGY_NODE_CHILD_COUNT =
        "ontology_node_child_count";
    public static final String ONTOLOGY_NODE_DEFINITION =
        "ontology_node_definition";

    public static final String ONTOLOGY_NODE_EXPANDABLE =
        "ontology_node_expandable";

    public static final String CHILDREN_NODES = "children_nodes";
    public static final String NCI_SOURCE = "NCI";

    public static final String NCI_METATHESAURUS = "NCI Metathesaurus";


    LexBIGService lbSvc = null;
    MetaTreeUtils metaTreeUtils = null;
    private SourceTreeUtils sourceTreeUtils = null; //(LexBIGService lbSvc)
    MetaBrowserService mbs = null;
    String version = null;


    public MetaTreeHelper(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
		this.metaTreeUtils = new MetaTreeUtils(lbSvc);
		this.sourceTreeUtils = new SourceTreeUtils(lbSvc);
		try {
            mbs =
                (MetaBrowserService) lbSvc
                    .getGenericExtension("metabrowser-extension");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Vector getSourceRoots(String scheme, String version, String sab) {
        //String scheme = "NCI Metathesaurus";
        if (version == null) {
		     version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(scheme, "PRODUCTION");
		}
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		if (version != null) {
			csvt.setVersion(version);
		}
        Vector w = new Vector();
		try {
			String tag = null;
            List list =
                    (new SourceTreeUtils(lbSvc)).getSourceHierarchyRoots(scheme,
                        csvt, sab);

            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    String code = "";
                    String name = "";
					ResolvedConceptReference node =
						(ResolvedConceptReference) list.get(i);
					code = node.getConceptCode();
					name = node.getEntityDescription().getContent();
                    boolean is_expandable = true;
                    try {
						w.add(scheme + "|" + version + "|" + sab + "|"  + code + "|" + name + "|" + is_expandable);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
				System.out.println("list: null???");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
        return w;
	}

    public String build_tree(String scheme, String version, String sab) {
		Vector w = new Vector();
		Vector roots = getSourceRoots(scheme, version, sab);
		for (int i=0; i<roots.size(); i++) {
			String line = (String) roots.elementAt(i);
			Vector u = StringUtils.parseData(line, '|');
			String name = (String) u.elementAt(4);
			String code = (String) u.elementAt(3);
			String is_expandable = (String) u.elementAt(5);
			w.add("Root" + "|" + "<Root>" + "|" + name + "|" + code + "|" + sab + "|" + is_expandable);
		}

		TreeNavigationHelper treeNavigationHelper = new TreeNavigationHelper(null, w);
		return treeNavigationHelper.print_tree();
	}


    public Vector getSubconcepts(String scheme,  String version, String cui, String sab) {
        if (version == null) {
		     version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(scheme, "PRODUCTION");
		}
        Vector v = new Vector();
        String childNavText = "CHD";
        int knt0 = 0;
        try {

            MetaTree tree = mbs.getMetaNeighborhood(cui, sab);
            MetaTreeNode focus = tree.getCurrentFocus();
            Iterator iterator = focus.getChildren();
            if (iterator == null) {
                 return null;
            }
            while (iterator.hasNext()) {
                MetaTreeNode child = (MetaTreeNode) iterator.next();
				boolean expandabe = false;
				if (child.getExpandedState() == MetaTreeNode.ExpandedState.EXPANDABLE) {
					expandabe = true;
				}
                v.add(scheme + "|" + version + "|" + child.getName() + "|" + child.getCui() + "|" +  sab + "|" + expandabe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SortUtils().quickSort(v);
    }

    public String expand_tree(String scheme, String version, String cui, String sab, String id) {
        Vector subs = getSubconcepts(scheme,  version, cui, sab);
        return new TreeNavigationUtils().expand_node(id, subs);
	}

	public String getNodeData(MetaTreeNode node) {
		boolean expandabe = false;
		if (node.getExpandedState() == MetaTreeNode.ExpandedState.EXPANDABLE) {
			expandabe = true;
		}
		return node.getName() + "|" + node.getCui() + "|" + node.getSab() + "|" + expandabe;
	}

    public boolean isNodeExpandable(String cui, String sab) {
		boolean expandabe = false;
        try {
            //MetaTreeImp mti = new MetaTreeImp(mbs, sab);
            MetaTree tree = mbs.getMetaNeighborhood(cui, sab);
            MetaTreeNode focus = tree.getCurrentFocus();
			if (focus.getExpandedState() == MetaTreeNode.ExpandedState.EXPANDABLE) {
				expandabe = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return expandabe;
	}

 	public Vector get_tree_data_from_root(String cui, String sab) {
        try {
            //MetaTreeImp mti = new MetaTreeImp(mbs, sab);
            MetaTree tree = mbs.getMetaNeighborhood(cui, sab);
            MetaTreeNode focus = tree.getCurrentFocus();
            return build_children_path_to_root_nodes(focus);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	    return null;
	}

    public Vector searchTree(String cui, String sab) {
		Vector topNodes = getTopNode(sab, true);
		Vector w = new Vector();
		HashMap parentCUIMap = new HashMap();
		HashSet hset = new HashSet();
        try {
            MetaTreeImp mti = new MetaTreeImp(mbs, sab);
            MetaTree tree = mbs.getMetaNeighborhood(cui, sab);
            MetaTreeNode focus = tree.getCurrentFocus();

            Stack stack = new Stack();
            stack.push(focus);
            while (!stack.empty()) {
				MetaTreeNode node = (MetaTreeNode) stack.pop();
				try {
					List<String> previousParentCuis = null;
					List<MetaTreeNode> list = mti.getParents(node, previousParentCuis);
					for (int i=0; i<list.size(); i++) {
						MetaTreeNode parentNode = (MetaTreeNode) list.get(i);
						if (!topNodes.contains(parentNode.getCui())) {
							parentCUIMap.put(getNodeData(parentNode), getNodeData(node));
							stack.push(parentNode);

                            //[NCIM-283] Excessively High CPU Usage.
							if (hset.contains(parentNode.getCui())) {
								System.out.println("WARNING: LOOP encountered while trying to find paths to roots from (CUI: " + cui + " SAB: " + sab + ")");
								System.out.println("\t\t-- Conflicting relationship found at: " + parentNode.getName() + "(CUI: " + parentNode.getCui() + ")");
								return null;
							}
							hset.add(parentNode.getCui());
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Iterator it = parentCUIMap.keySet().iterator();
        while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) parentCUIMap.get(key);
			w.add(key + "|" + value);
		}
        return w;
    }


    public Vector getTopNode(String source, boolean codeOnly) {
		boolean searchInactive = true;
        String scheme = "NCI Metathesaurus";
        String version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(scheme, "PRODUCTION");
        Vector w = new Vector();
		try {
			ResolvedConceptReferencesIterator iterator =
				sourceTreeUtils.findConceptWithSourceCodeMatching(scheme, version, "SRC",
					"V-" + source, -1, searchInactive);
			if (iterator != null) {
				try {
					int knt = 0;
					try {
						while (iterator.hasNext()) {
							ResolvedConceptReference[] refs =
								iterator.next(100)
									.getResolvedConceptReference();

							for (ResolvedConceptReference ref : refs) {
								knt++;
								if (codeOnly) {
									w.add(ref.getConceptCode());
							    } else {
									w.add(ref.getEntityDescription().getContent() + "|" + ref.getConceptCode());
								}
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return w;
	}

/*
    public String search_tree(String scheme, String version, String sab, String cui) {
		Vector w = new Vector();
		//w.add(scheme + "|" + version + "|" + sab + "|"  + code + "|" + name + "|" + is_expandable);
		Vector roots = getSourceRoots(scheme, version, sab);
		for (int i=0; i<roots.size(); i++) {
			String line = (String) roots.elementAt(i);
			//        (1) NCI Metathesaurus|202008|C0333717|Abnormal Cell|true
			Vector u = StringUtils.parseData(line, '|');
			String name = (String) u.elementAt(4);
			String code = (String) u.elementAt(3);
			String is_expandable = (String) u.elementAt(5);
			w.add("Root" + "|" + "<Root>" + "|" + name + "|" + code + "|" + sab + "|" + is_expandable);
		}

        Vector w2 = searchTree(cui, sab);
		for (int i=0; i<w2.size(); i++) {
			String line = (String) w2.elementAt(i);
			Vector u = StringUtils.parseData(line, '|');
			w.add((String) u.elementAt(0) +  "|"
                 +(String) u.elementAt(1) +  "|"
                 +(String) u.elementAt(4) +  "|"
                 +(String) u.elementAt(5) +  "|"
                 +(String) u.elementAt(6) +  "|"
                 +(String) u.elementAt(7));

			Vector v = get_tree_data_from_root((String) u.elementAt(1), sab);
			w.addAll(v);
		}

		Vector v = get_tree_data_from_root(cui, sab);
		w.addAll(v);


		TreeNavigationHelper treeNavigationHelper = new TreeNavigationHelper(cui, w);
		treeNavigationHelper.setFocusNodeExpandable(isNodeExpandable(cui, sab));
		return treeNavigationHelper.print_tree();
	}
*/
    public String search_tree(String scheme, String version, String sab, String cui) {
        Vector w2 = searchTree(cui, sab);
        if (w2 == null) return null;

		Vector w = new Vector();
		Vector roots = getSourceRoots(scheme, version, sab);
		for (int i=0; i<roots.size(); i++) {
			String line = (String) roots.elementAt(i);
			//NCI Metathesaurus|202008|C0333717|Abnormal Cell|true
			Vector u = StringUtils.parseData(line, '|');
			String name = (String) u.elementAt(4);
			String code = (String) u.elementAt(3);
			String is_expandable = (String) u.elementAt(5);
			w.add("Root" + "|" + "<Root>" + "|" + name + "|" + code + "|" + sab + "|" + is_expandable);
		}

		for (int i=0; i<w2.size(); i++) {
			String line = (String) w2.elementAt(i);
			Vector u = StringUtils.parseData(line, '|');
			w.add((String) u.elementAt(0) +  "|"
                 +(String) u.elementAt(1) +  "|"
                 +(String) u.elementAt(4) +  "|"
                 +(String) u.elementAt(5) +  "|"
                 +(String) u.elementAt(6) +  "|"
                 +(String) u.elementAt(7));

			Vector v = get_tree_data_from_root((String) u.elementAt(1), sab);
			w.addAll(v);
		}
		Vector v = get_tree_data_from_root(cui, sab);
		w.addAll(v);
		TreeNavigationHelper treeNavigationHelper = new TreeNavigationHelper(cui, w);
		treeNavigationHelper.setFocusNodeExpandable(isNodeExpandable(cui, sab));
		return treeNavigationHelper.print_tree();
	}


    public String getPathsToRootsExt(String node_id, String sab) {
        String scheme = "NCI Metathesaurus";
 		String version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(scheme, "PRODUCTION");
        boolean fromCache = false; // no cache PTR
        return getPathsToRootsExt(scheme, version, node_id, sab, fromCache);
    }

    public String getPathsToRootsExt(String scheme, String version,
        String node_id, String sab, boolean fromCache) {
        try {
            MetaTree tree = mbs.getMetaNeighborhood(node_id, sab);
            MetaTreeNode focus = tree.getCurrentFocus();
            return new MetaTreeImp(mbs, node_id, sab).getJsonFromRoot(focus);

        } catch (Exception ex) {
            // to be modified
            //return "[]";
            return null;
        }
    }

    public MetaTreeNode getFocusNode(String node_id, String sab) {
        String scheme = "NCI Metathesaurus";
 		String version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(scheme, "PRODUCTION");
		return getFocusNode(scheme, version, node_id, sab);
	}

    public MetaTreeNode getFocusNode(String scheme, String version,
        String node_id, String sab) {
        //LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        try {
            MetaTree tree = mbs.getMetaNeighborhood(node_id, sab);
            MetaTreeNode focus = tree.getCurrentFocus();
            return focus;

        } catch (Exception ex) {
            return null;
        }
    }

	private String build_node(MetaTreeNode node){
		try {
			boolean isExpandable = false;
			if(node.getExpandedState().equals(ExpandedState.EXPANDABLE)){
				isExpandable = true;
			}
			return node.getName() + "|" +  node.getCui() + "|" +  node.getSab() + "|" + isExpandable;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Vector build_children_path_to_root_nodes(MetaTreeNode focusNode){
		try {
			return walk_tree_from_root(focusNode, true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int expandableStatusToInt(ExpandedState status){
		if(status.equals(ExpandedState.EXPANDABLE)){
			return 1;
		} else {
			return 0;
		}
	}

	public static MetaTreeNode getRoot(MetaTreeNode focus){
		if(focus.getParents() == null || focus.getParents().size() == 0){
			return focus;
		} else {
			return getRoot(focus.getParents().get(0));
		}
	}

	private boolean knownChildrenContainsCode(List<MetaTreeNode> list, String cui){
		for(MetaTreeNode node : list){
			if(node.getCui().equals(cui)){
				return true;
			}
		}
		return false;
	}

	private Vector walk_tree_from_root(MetaTreeNode node, boolean isRoot){
		Vector w = new Vector();
		//String node_obj = build_node(node);
		try {
			if(node.getPathToRootChilden() == null){
				System.out.println("getPathToRootChilden == null???");
			}

			if(node.getPathToRootChilden() != null){
				//List<String> childrenCuis = new ArrayList<String>();
				for(MetaTreeNode child : node.getPathToRootChilden()){
					String child_obj = build_node(child);
					w.add(node.getName() + "|" + node.getCui() + "|" + child_obj);
					//System.out.println("walk_tree_from_root child " + child.getName() + " " + child.getCui());
					//childrenCuis.add(child.getCui());
					Vector v = walk_tree_from_root(child, false);

					if (v != null) {
						w.addAll(v);
					}
				}


				ChildIterator itr = node.getChildren();
				if(itr != null){
					while(itr.hasNext()){
						MetaTreeNode child = itr.next();
						String child_obj = build_node(child);
						w.add(node.getName() + "|" + node.getCui() + "|" + child_obj);
						if(!knownChildrenContainsCode(node.getPathToRootChilden(), child.getCui())){
							Vector v = walk_tree_from_root(child, false);
							if (v != null && v.size() > 0) {
								w.addAll(v);
							}
							//childrenCuis.add(child.getCui());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return w;
	}

    public void test() {
		String scheme = NCI_METATHESAURUS;
		String version = null;
		String sab = "NCI";
		Vector w = new Vector();

		//build_tree:
		Vector v = getSourceRoots(scheme, version, NCI_SOURCE);
		Utils.dumpVector("roots", v);

		String content = build_tree(scheme, version, NCI_SOURCE);
        content = content.replace("/ncimbrowser/", "ncimbrowser/");
		w.add(content);
		Utils.saveToFile(sab + "_build_tree.html", w);

		//search_tree
		String cui = "C1332226";
		w = new Vector();
		content = search_tree(scheme, version, sab, cui);
        content = content.replace("/ncimbrowser/", "ncimbrowser/");
		w.add(content);
		Utils.saveToFile(cui + "_search_tree.html", w);

		//expand_tree
        String id = "N_1";
		w = new Vector();
		content = expand_tree(scheme, version, cui, sab, id);
        content = content.replace("/ncimbrowser/", "ncimbrowser/");
		w.add(content);
		Utils.saveToFile(cui + "_expand_tree.html", w);
	}

	public static void main(String[] args) {
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		MetaTreeHelper metaTreeHelper = new MetaTreeHelper(lbSvc);
		metaTreeHelper.test();
	}
}
