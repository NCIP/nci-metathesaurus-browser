package gov.nih.nci.evs.browser.utils;
import gov.nih.nci.evs.browser.bean.*;

import java.io.*;
import java.util.*;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.*;
import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator.ChildTreeNodeIterator;
import org.LexGrid.LexBIG.Impl.Extensions.tree.json.JsonConverter;
import org.LexGrid.LexBIG.Impl.Extensions.tree.json.JsonConverterFactory;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTree;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.TreeService;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.TreeServiceFactory;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.commonTypes.*;
import org.LexGrid.concepts.*;
import org.LexGrid.naming.*;
import org.apache.log4j.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

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
 *      Modification history Initial implementation kim.ong@ngc.com
 *
 */

// Note: Version with the has more (...) nodes feature.

public class TreeNavigationUtils {
	public static int MAX_CHILDREN = 5;
	private static Logger _logger = Logger.getLogger(TreeNavigationUtils.class);
	private static Random rand = new Random();

	String contextPath = "/ncimbrowser/";

    private LexBIGService lbSvc = null;
    private LexBIGServiceConvenienceMethods lbscm = null;

	/** The Constant ONTOLOGY_NODE_CHILD_COUNT. */
	public static final String ONTOLOGY_NODE_CHILD_COUNT = "ontology_node_child_count";

	/** The Constant ONTOLOGY_NODE_ID. */
	public static final String ONTOLOGY_NODE_ID = "ontology_node_id";

	/** The Constant ONTOLOGY_NODE_NAME. */
	public static final String ONTOLOGY_NODE_NAME = "ontology_node_name";

	/** The Constant ONTOLOGY_NODE_NS. */
	public static final String ONTOLOGY_NODE_NS = "ontology_node_ns"; // namespace

	/** The Constant CHILDREN_NODES. */
	public static final String CHILDREN_NODES = "children_nodes";

	/** The Constant NODES. */
	public static final String NODES = "nodes";

	static String TAB = "";

    public TreeNavigationUtils() {

	}

    public TreeNavigationUtils(LexBIGService lbSvc) {
		try {
			this.lbSvc = lbSvc;
			this.lbscm = (LexBIGServiceConvenienceMethods) lbSvc.getGenericExtension("LexBIGServiceConvenienceMethods");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    public String getNamespaceByCode(String codingSchemeName, String vers, String code) {
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		return new ConceptDetails(lbSvc).getNamespaceByCode(codingSchemeName, vers, code);
	}

    public String getVocabularyVersionByTag(String codingSchemeName) {
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		return new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(codingSchemeName, "PRODUCTION");
	}

/*
		LexEvsTree tree = null;
		if (StringUtils.isNullOrBlank(namespace)) {
			String ns = getNamespaceByCode(codingScheme, versionOrTag.getVersion(), code);
			tree = treeService.getTree(codingScheme, versionOrTag, code, ns);
		} else {
			tree = treeService.getTree(codingScheme, versionOrTag, code, namespace);
		}
*/

	public Vector searchTree(String scheme, String version, String ns, String code) {

		TreeService treeService =
				TreeServiceFactory.getInstance().getTreeService(lbSvc);

        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);

        if (ns == null) {
			ns = getNamespaceByCode(scheme, version, code);
		}
		System.out.println("namespace: " + ns);
		LexEvsTree tree = treeService.getTree(scheme, versionOrTag, code, ns);

	    Map<String, LexEvsTreeNode> codeMap = tree.getCodeMap();
	    Iterator it = codeMap.keySet().iterator();

	    Vector parent_child_vec = new Vector();
	    while (it.hasNext()) {
			String key = (String) it.next();
			LexEvsTreeNode node = (LexEvsTreeNode) codeMap.get(key);
			boolean expandabe = false;
			if (node.getExpandableStatus() == LexEvsTreeNode.ExpandableStatus.IS_EXPANDABLE) {
				expandabe = true;
			}

			List<LexEvsTreeNode> parents = node.getPathToRootParents();
			if (parents != null) {
				for (int i=0; i<parents.size(); i++) {
					LexEvsTreeNode parent = (LexEvsTreeNode) parents.get(i);
                    String parentLabel = parent.getEntityDescription();
                    String parentCode = parent.getCode();
                    if (parent.getEntityDescription() == null) {
						parentLabel = "Root";
						parentCode = "<Root>";
					}

					expandabe = false;
					if (node.getExpandableStatus() == LexEvsTreeNode.ExpandableStatus.IS_EXPANDABLE) {
						expandabe = true;
					}

					parent_child_vec.add(
						parentLabel + "|" +
					    parentCode + "|" +
						node.getEntityDescription() + "|" +
					    node.getCode() + "|" +
					    node.getNamespace() + "|" +
					    expandabe);
				}
			}
		}

		parent_child_vec = new SortUtils().quickSort(parent_child_vec);
		System.out.println("parent_child_vec: " + parent_child_vec.size());
        return parent_child_vec;
	}

	public String search_tree(String scheme, String version, String ns, String code) {
		Vector parent_child_vec = searchTree(scheme, version, ns, code);
		return new TreeNavigationHelper(code, parent_child_vec).print_tree();
	}

	public TreeNode expandTree(String scheme, String version, String ns, String code) {
        TreeNode ti = new TreeNode();
		TreeService service =
				TreeServiceFactory.getInstance().getTreeService(lbSvc);

        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);

	    LexEvsTreeNode focusNode = service.getSubConcepts(scheme, versionOrTag, code, ns);
		ChildTreeNodeIterator itr = focusNode.getChildIterator();
		Vector w = new Vector();
		try {
			HashSet hset = new HashSet();
			int lcv = 0;
			while(itr.hasNext()){
				LexEvsTreeNode child = itr.next();
				lcv++;
				if (child != null) {
					String child_code = child.getCode();
					String child_label = child.getEntityDescription();
					String child_ns = child.getNamespace();
					boolean expandabe = false;
					if (child.getExpandableStatus() == LexEvsTreeNode.ExpandableStatus.IS_EXPANDABLE) {
						expandabe = true;
					}
					w.add(child_label + "|" + child_code + "|" + child_ns + "|" + expandabe);
				}
			}
			w = new SortUtils().quickSort(w);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Utils.dumpVector("children.txt", w);
		Utils.saveToFile("children.txt", w);
		return ti;
	}

	public String expandTreeNode(String scheme, String version, String ns, String code) {
        JSONArray jsonArray = new JSONArray();
		TreeService service =
				TreeServiceFactory.getInstance().getTreeService(lbSvc);

        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);

	    LexEvsTreeNode focusNode = service.getSubConcepts(scheme, versionOrTag, code, ns);
		ChildTreeNodeIterator itr = focusNode.getChildIterator();
		Vector w = new Vector();
		try {
			HashSet hset = new HashSet();
			int lcv = 0;
			while(itr.hasNext()){
				LexEvsTreeNode child = itr.next();
				lcv++;
				if (child != null) {
					String child_code = child.getCode();
					String child_label = child.getEntityDescription();
					String child_ns = child.getNamespace();
					boolean expandabe = false;
					if (child.getExpandableStatus() == LexEvsTreeNode.ExpandableStatus.IS_EXPANDABLE) {
						expandabe = true;
					}
					w.add(child_label + "|" + child_code + "|" + child_ns + "|" + expandabe);

					JSONObject jsonObject = buildNode(child);
					jsonArray.put(jsonObject);
				}
			}
			w = new SortUtils().quickSort(w);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Utils.dumpVector("children.txt", w);
		Utils.saveToFile("children.txt", w);
		return jsonArray.toString();
	}

	private JSONObject buildNode(int count, String code, String ns, String description){
		JSONObject nodeObject = new JSONObject();
		try {
			nodeObject.put(ONTOLOGY_NODE_CHILD_COUNT, count);
			nodeObject.put(ONTOLOGY_NODE_ID, code);
			nodeObject.put(ONTOLOGY_NODE_NS, ns == null ? "" : ns);
			nodeObject.put(ONTOLOGY_NODE_NAME, description);

			JSONArray childrenArray = new JSONArray();
			nodeObject.put(CHILDREN_NODES, childrenArray);

			return nodeObject;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	private JSONObject buildNode(LexEvsTreeNode node){
		JSONObject nodeObject = new JSONObject();

		try {
			int count = 0;
			if (node.getExpandableStatus() == LexEvsTreeNode.ExpandableStatus.IS_EXPANDABLE) {
				count = 1;
			}

			nodeObject.put(ONTOLOGY_NODE_CHILD_COUNT, count);
			nodeObject.put(ONTOLOGY_NODE_ID, node.getCode());
			nodeObject.put(ONTOLOGY_NODE_NS, node.getNamespace() == null ? "" : node.getNamespace());
			nodeObject.put(ONTOLOGY_NODE_NAME, node.getEntityDescription());

			JSONArray childrenArray = new JSONArray();
			nodeObject.put(CHILDREN_NODES, childrenArray);

			return nodeObject;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public void dumpTreeItem(TreeItem vih_tree) {
        TreeItem.printTree(vih_tree, 0);
	}


	public String buildTree(String scheme, String version, String ns) {

        JSONArray jsonArray = new JSONArray();
		TreeService service =
				TreeServiceFactory.getInstance().getTreeService(lbSvc);

        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);

	    LexEvsTreeNode focusNode = null;
		try {
			focusNode = service.getSubConcepts(scheme, versionOrTag, "@@", ns);
			if (focusNode == null) {
				focusNode = service.getSubConcepts(scheme, versionOrTag, "@", ns);
			}
		} catch (Exception ex) {
			System.out.println(	"service.getSubConcepts failed.");
			return null;
		}


		ChildTreeNodeIterator itr = focusNode.getChildIterator();
		try {
			HashMap hmap = new HashMap();
			Vector keys = new Vector();
			int lcv = 0;
			while(itr.hasNext()){
				LexEvsTreeNode child = itr.next();
				keys.add(child.getEntityDescription());
				hmap.put(child.getEntityDescription(), child);
			}
			keys = new SortUtils().quickSort(keys);
			for (int i=0; i<keys.size(); i++) {
				String key = (String) keys.elementAt(i);
				LexEvsTreeNode child = (LexEvsTreeNode) hmap.get(key);
				JSONObject jsonObject = buildNode(child);
				jsonArray.put(jsonObject);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Vector v = new Vector();
		v.add(jsonArray.toString());
		Utils.saveToFile("roots.json", v);
		return jsonArray.toString();
	}

    private String getIndentation(int level) {
		if (level <= 0) return "";
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<level; i++) {
			buf.append(TAB);
		}
		return buf.toString();
	}

	public String getTabIndex() {
		int tabindex = 1;
		return "tabindex=\"" + tabindex + "\"";
	}

	private String getHyperLink(String label, String code) {
        return "&nbsp;<a href=\"#\" onclick=\"javascript:onClickTreeNode('" + code + "');\">" + label + "</a>";
	}

    public StringBuffer print_node(StringBuffer buf, String label, String code, String id, int level, boolean expandable, boolean expanded) {
		String indentation = getIndentation(level);

		if (id.length() > 4) {
			id = id.substring(4, id.length());
		}

		String div_id = "DIV_" + id;
		buf.append("<li id=\"" + div_id + "\"" + " code=\"" + code + "\"><span>");
		String img_id = "IMG_" + id;
		if (expandable) {

            if (expanded) {
				buf.append("<img src=\"" + contextPath + "images/minus.gif\" id=\"" + img_id + "\" alt=\"show_hide\" onclick=\"show_hide('" + div_id + "');\" "
					+ getTabIndex()
					+ ">"
					+ getHyperLink(label, code));
			} else {
				buf.append("<img src=\"" + contextPath + "images/plus.gif\" id=\"" + img_id + "\" alt=\"show_hide\" onclick=\"show_hide('" + div_id + "');\" "
					+ getTabIndex()
					+ ">"
					+ getHyperLink(label, code));
			}

	    } else {
				buf.append("<img src=\"" + contextPath + "images/dot.gif\" id=\"" + img_id + "\" alt=\"show_hide\" onclick=\"show_hide('" + div_id + "');\" "
				+ ">"
				+ getHyperLink(label, code));
		}
		buf.append(indentation + "</li>");
		return buf;
	}

	public String build_tree(String scheme, String version, String ns) {
        Vector roots = getRoots(scheme, version);
		StringBuffer buf = new StringBuffer();
		buf.append("<ul>");
		try {
			for (int i=0; i<roots.size(); i++) {
				String line = (String) roots.elementAt(i);
				Vector u = StringUtils.parseData(line, '|');
				String label = (String) u.elementAt(5);
				String code = (String) u.elementAt(4);
				String expandable_str = (String) u.elementAt(6);
				boolean expandable = false;
				boolean expanded = false;
				if (expandable_str.compareTo("true") == 0) {
					expandable = true;
				}
				String id = "N_" + String.valueOf(i+1);
				StringBuffer buf2 = print_node(new StringBuffer(), label, code, id, 0, expandable, expanded);
				buf.append(buf2);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		buf.append("<ul>");
		return buf.toString();
	}

	public String build_tree(String scheme, String version, String ns, Vector roots, int labelIndex, int codeIndex, int expandableIndex) {
        //Vector roots = getRoots(scheme, version);
		StringBuffer buf = new StringBuffer();
		buf.append("<ul>");
		try {
			for (int i=0; i<roots.size(); i++) {
				String line = (String) roots.elementAt(i);
				Vector u = StringUtils.parseData(line, '|');
				String label = (String) u.elementAt(labelIndex);
				String code = (String) u.elementAt(codeIndex);
				String expandable_str = (String) u.elementAt(expandableIndex);
				boolean expandable = false;
				boolean expanded = false;
				if (expandable_str.compareTo("true") == 0) {
					expandable = true;
				}
				String id = "N_" + String.valueOf(i+1);
				StringBuffer buf2 = print_node(new StringBuffer(), label, code, id, 0, expandable, expanded);
				buf.append(buf2);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		buf.append("<ul>");
		return buf.toString();
	}


    public void generate(String scheme,
        String version,
        String code,
        String namespace,
        String outputfile) {
		//set_context_path("ncitbrowser/");
        long ms = System.currentTimeMillis();
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
			pw.println("<html>");
			pw.println("<head>");
			pw.println("<title>");
			pw.println("</title>");
			pw.println("<body>");

            String str = search_tree(scheme, version, namespace, code);

            pw.println(str);

			pw.println("</body>");
			pw.println("</html>");

		} catch (Exception ex) {

		} finally {
			try {
				pw.close();
				System.out.println("Output file " + outputfile + " generated.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("Total run time (ms): " + (System.currentTimeMillis() - ms));
	}


	public String expand_tree(String scheme, String version, String ns, String code, String id) {
        StringBuffer buf = new StringBuffer();
		TreeService service =
				TreeServiceFactory.getInstance().getTreeService(lbSvc);

        if (version == null) {
			version = getVocabularyVersionByTag(scheme);
		}
        if (ns == null) {
			ns = getNamespaceByCode(scheme, version, code);
		}

        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);

	    LexEvsTreeNode focusNode = service.getSubConcepts(scheme, versionOrTag, code, ns);

	    if (focusNode.getExpandableStatus() != LexEvsTreeNode.ExpandableStatus.IS_EXPANDABLE) {
			return "";
		}
		buf.append("<ul>");
		ChildTreeNodeIterator itr = focusNode.getChildIterator();
		Vector w = new Vector();
		try {
			HashMap hmap = new HashMap();
			Vector keys = new Vector();
			while(itr.hasNext()){
				LexEvsTreeNode child = itr.next();
				if (child != null) {
					String child_label = child.getEntityDescription();
					hmap.put(child_label, child);
					keys.add(child_label);
				}
			}
			keys = new SortUtils().quickSort(keys);
			int lcv = 0;
			for (int i=0; i<keys.size(); i++) {
				String key = (String) keys.elementAt(i);
				LexEvsTreeNode child = (LexEvsTreeNode) hmap.get(key);
				lcv++;
				if (child != null) {
					String child_code = child.getCode();
					String child_label = child.getEntityDescription();
					String child_ns = child.getNamespace();
					boolean expandable = false;
					boolean expanded = false;
					if (child.getExpandableStatus() == LexEvsTreeNode.ExpandableStatus.IS_EXPANDABLE) {
						expandable = true;
					}
					String child_id = id + "_" + String.valueOf(lcv);
					StringBuffer buf2 = print_node(new StringBuffer(), child_label, child_code, child_id, 0, expandable, expanded);
					buf.append(buf2);
				}
			}
			w = new SortUtils().quickSort(w);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		buf.append("</ul>");
		return buf.toString();
	}


	public String expand_node(String id, Vector subs) {
        StringBuffer buf = new StringBuffer();
		buf.append("<ul>");
		//v.add(scheme + "|" + version + "|" + child.getName() + "|" + child.getCui() + "|" +  sab + "|" + expandabe);
		for (int i=0; i<subs.size(); i++) {
			int lcv = i+1;
			String line = (String) subs.elementAt(i);
			String child_id = id + "_" + String.valueOf(lcv);

			Vector u = StringUtils.parseData(line, '|');
			String child_label = (String) u.elementAt(2);
			String child_code = (String) u.elementAt(3);
			String expandable_str = (String) u.elementAt(5);

			boolean expandable = false;
			boolean expanded = false;
			if (expandable_str.compareTo("true") == 0) {
				expandable = true;
			}

			StringBuffer buf2 = print_node(new StringBuffer(),
			    child_label,
			    child_code,
			    child_id,
			    0,
			    expandable,
			    expanded);
			buf.append(buf2);
		}
		buf.append("</ul>");
		return buf.toString();
	}


    public ResolvedConceptReferenceList getHierarchyRoots(
        String codingScheme, String version) {
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		return new TreeUtils(lbSvc).getHierarchyRoots(codingScheme, version);
	}

	public boolean isExpandable(TreeService treeService, String scheme, String version, String code, String namespace) {
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		if (version != null) {
			versionOrTag.setVersion(version);
		}
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		LexEvsTreeNode node = treeService.getSubConcepts(scheme, versionOrTag, code, namespace);
        if (node != null && node.getExpandableStatus().toString().compareTo("IS_EXPANDABLE") == 0) return true;
        return false;
	}

    public Vector getRoots(String codingScheme, String version) {
		Vector roots = new Vector();
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		ResolvedConceptReferenceList rcrl = getHierarchyRoots(codingScheme, version);
		TreeService treeService =
			TreeServiceFactory.getInstance().getTreeService(lbSvc);

        Vector w = new Vector();
        for (int i=0; i<rcrl.getResolvedConceptReferenceCount(); i++) {
			ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(i);
			w.add(rcr);
		}

		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		if (version != null) {
			versionOrTag.setVersion(version);
		}

		w = new SortUtils().quickSort(w);
        for (int i=0; i<w.size(); i++) {
			ResolvedConceptReference rcr = (ResolvedConceptReference) w.elementAt(i);
			String cs_name = rcr.getCodingSchemeName();
			String cs_version = rcr.getCodingSchemeVersion();
			String cs_ns = rcr.getCodeNamespace();
			String code = rcr.getConceptCode();
			String name = rcr.getEntityDescription().getContent();

			LexEvsTreeNode node = treeService.getSubConcepts(cs_name, versionOrTag, code, cs_ns);
			boolean is_expandable = false;
			if (node != null && node.getExpandableStatus().toString().compareTo("IS_EXPANDABLE") == 0) {
				is_expandable = true;
			}
			roots.add(cs_name + "|" + cs_version + "|" + cs_version + "|" + cs_ns + "|" + code + "|" + name + "|" + is_expandable);
		}
		return roots;
	}


    public static void main(String[] args) throws Exception {
		//LexBIGService lbSvc = LexBIGServiceImpl.defaultInstance();
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        TreeNavigationUtils test = new TreeNavigationUtils(lbSvc);

        String scheme = args[0];//"NCI_Thesaurus";
        String code = args[1];//"C12345";
        String version = test.getVocabularyVersionByTag(scheme);
        String namespace = test.getNamespaceByCode(scheme, version, code);
        String outputfile = "test2.html";
/*
        test.generate(scheme,
			version,
			code,
			namespace,
			outputfile);
*/
		//String s = test.build_tree(scheme, version, namespace);
		//System.out.println(s);

		code = "C2991";
		String id = "DIV_N_1_1";
		String s = test.expand_tree(scheme, version, namespace, code, id);
		System.out.println(s);


    }
}

