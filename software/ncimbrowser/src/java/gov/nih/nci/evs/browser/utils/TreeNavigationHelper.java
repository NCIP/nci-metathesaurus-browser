package gov.nih.nci.evs.browser.utils;
import gov.nih.nci.evs.browser.bean.*;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.*;
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

public class TreeNavigationHelper {
	Vector parent_child_vec = null;
	HashSet expandableSet = null;
	HashMap nsMap = null;
	HierarchyHelper hh = null;
	Vector roots = null;
    static String TAB = "";
    private String URL = null;
    private int tabindex = 0;

    private HashSet path_to_roots_nodes = null;
    private String focusCode = null;
    String contextPath = "/ncimbrowser/";
    boolean focusNodeExpandable = true;

    public TreeNavigationHelper() {

	}

	public TreeNavigationHelper(String focusCode, Vector parent_child_vec) {
		this.parent_child_vec = parent_child_vec;
		initialize();
		if (focusCode != null) {
			set_focus_code(focusCode);
		}
	}

	public void set_context_path(String contextPath) {
		this.contextPath = contextPath;
	}

	public void initialize() {
		tabindex = 0;
	    hh = new HierarchyHelper(parent_child_vec);
		roots = hh.getRoots();
        expandableSet = new HashSet();
        nsMap = new HashMap();
        for (int i=0; i<parent_child_vec.size(); i++) {
			String line = (String) parent_child_vec.elementAt(i);
			Vector u = StringUtils.parseData(line, '|');
			String child_code = (String) u.elementAt(3);

			String child_ns = (String) u.elementAt(4);
			nsMap.put(child_code, child_ns);

			String expandable = (String) u.elementAt(5);
			if (expandable.compareTo("true") == 0) {
				expandableSet.add(child_code);
			}
		}
	}

	public String getTabIndex() {
		tabindex++;
		return "tabindex=\"" + tabindex + "\"";
	}

	private JSONObject buildNode(String root) {
		String description = hh.getLabel(root);
		String ns = (String) nsMap.get(root);
		int count = 0;
		if (expandableSet.contains(root)) {
			count = 1;
		}
		JSONObject nodeObject = new JSONObject();
		try {
			nodeObject.put(TreeNavigationUtils.ONTOLOGY_NODE_CHILD_COUNT, count);
			nodeObject.put(TreeNavigationUtils.ONTOLOGY_NODE_ID, root);
			nodeObject.put(TreeNavigationUtils.ONTOLOGY_NODE_NS, ns == null ? "" : ns);
			nodeObject.put(TreeNavigationUtils.ONTOLOGY_NODE_NAME, description);
			Vector child_codes = hh.getSubclassCodes(root);
			if (child_codes != null) {
				JSONArray childrenArray = new JSONArray();
				for (int j=0; j<child_codes.size(); j++) {
					String child_code = (String) child_codes.elementAt(j);
					childrenArray.put(buildNode(child_code));
				}
				nodeObject.put(TreeNavigationUtils.CHILDREN_NODES, childrenArray);
    		}

		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return nodeObject;
	}


    public void printPath2Roots(PrintWriter pw, String line) {
        Vector v = StringUtils.parseData(line, '|');
        String indent = "";
		for (int i=0; i<v.size(); i++) {
			String code = (String) v.elementAt(i);
			String label = hh.getLabel(code);
			if (pw != null) {
				pw.println(indent + label + " (" + code + ")");
			} else {
				System.out.println(indent + label + " (" + code + ")");
			}
			indent = indent + "\t";
		}
	}


	public void path2Roots(PrintWriter pw, String code) {
		Stack stack = new Stack();
		stack.push(code);
		while (!stack.isEmpty()) {
            String line = (String) stack.pop();
            Vector u = StringUtils.parseData(line, '|');
            String next_code = (String) u.elementAt(u.size()-1);
            Vector v = hh.getSuperclassCodes(next_code);
            if (v != null) {
                for (int i=0; i<v.size(); i++) {
					String sup = (String) v.elementAt(i);
					String nextLine = line + "|" + sup;
					stack.push(nextLine);
				}
			} else {
				printPath2Roots(pw, line);
			}
		}
	}


	public Vector find_path_to_roots(String code) {
		Vector w = new Vector();
		Stack stack = new Stack();
		stack.push(code);
		while (!stack.isEmpty()) {
            String line = (String) stack.pop();
            Vector u = StringUtils.parseData(line, '|');
            String next_code = (String) u.elementAt(u.size()-1);
            Vector v = hh.getSuperclassCodes(next_code);
            if (v != null) {
                for (int i=0; i<v.size(); i++) {
					String sup = (String) v.elementAt(i);
					String nextLine = line + "|" + sup;
					stack.push(nextLine);
				}
			} else {
				w.add(line);
			}
		}
		return w;
	}


	public void path2Roots(String code) {
		//PrintWriter pw = new PrintWriter(System.out);
		path2Roots(null, code);

	}


	public JSONObject build_node(int count, String code, String ns, String description){
		JSONObject nodeObject = new JSONObject();
		try {
			nodeObject.put(TreeNavigationUtils.ONTOLOGY_NODE_CHILD_COUNT, count);
			nodeObject.put(TreeNavigationUtils.ONTOLOGY_NODE_ID, code);
			nodeObject.put(TreeNavigationUtils.ONTOLOGY_NODE_NS, ns == null ? "" : ns);
			nodeObject.put(TreeNavigationUtils.ONTOLOGY_NODE_NAME, description);

			JSONArray childrenArray = new JSONArray();
			nodeObject.put(TreeNavigationUtils.CHILDREN_NODES, childrenArray);

			return nodeObject;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public StringBuffer writeNode(StringBuffer buf, String label, String code) {
		buf.append("<li>").append("\n");
		buf.append(label).append("\n");
		Vector subs = hh.getSubclassCodes(code);
		if (subs != null) {
			buf.append("<ul>").append("\n");
			for (int i=0; i<subs.size(); i++) {
				String sub_code = (String) subs.elementAt(i);
				String sub_label = hh.getLabel(sub_code);
				buf = writeNode(buf, sub_label, sub_code);
			}
			buf.append("</ul>").append("\n");
		}
		buf.append("</li>");
		return buf;
	}

	public String writeTree() {
		Vector roots = hh.getSubclassCodes("<Root>");
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<roots.size(); i++) {
			buf.append("<ul>").append("\n");
		    String code = (String) roots.elementAt(i);
		    String label = hh.getLabel(code);
		    buf = writeNode(buf, label, code);
			buf.append("</ul>").append("\n");
	 	}
	 	return buf.toString();
	}

	public void set_focus_code(String focusCode) {
		this.focusCode = focusCode;
		set_path_to_roots_nodes(focusCode);
	}

	public void set_path_to_roots_nodes(String focusCode) {
		Vector w = new Vector();
		path_to_roots_nodes = new HashSet();
		Vector path_to_roots = find_path_to_roots(focusCode);
		for (int i=0; i<path_to_roots.size(); i++) {
			String path = (String) path_to_roots.elementAt(i);
			Vector u = StringUtils.parseData(path, '|');
            w.addAll(u);
		}
       path_to_roots_nodes = new HashSet();
       for (int i=0; i<w.size(); i++) {
		   String code = (String) w.elementAt(i);
		   if (!path_to_roots_nodes.contains(code)) {
			   path_to_roots_nodes.add(code);
		   }
	   }
    }

	public void printTree(PrintWriter pw) {
		Vector roots = hh.getSubclassCodes("<Root>");
        pw.println("<ul>");
		for (int i=0; i<roots.size(); i++) {
		    String code = (String) roots.elementAt(i);
		    String label = hh.getLabel(code);
		    String id = "N_" + String.valueOf(i+1);
		    printNode(pw, label, code, id, 0);
	 	}
	 	pw.println("</ul>");
	 	pw.flush();
	}

    public void printNode(PrintWriter pw, String label, String code, String id, int level) {
		String indentation = getIndentation(level);
		pw.println(indentation + "<li>");
		String img_id = "IMG_" + id;
		if (expandableSet.contains(code)) {
			String div_id = "DIV_" + id;

            if (path_to_roots_nodes.contains(code)) {
				pw.println("<img src=\"" + contextPath + "images/minus.gif\" id=\"" + img_id + "\" alt=\"show_hide\" onclick=\"show_hide('" + div_id + "');\" "
					+ getTabIndex()
					+ ">"
					+ getHyperLink(label, code));
			} else {
				pw.println("<img src=\"" + contextPath + "images/plus.gif\" id=\"" + img_id + "\" alt=\"show_hide\" onclick=\"show_hide('" + div_id + "');\" "
					+ getTabIndex()
					+ ">"
					+ getHyperLink(label, code));
			}

            pw.println(indentation + "<div id=\"" +  div_id   + "\">");
		    pw.println(indentation + "<ul>");

			Vector subs = hh.getSubclassCodes(code);
			if (subs != null) {
				for (int i=0; i<subs.size(); i++) {
					String sub_code = (String) subs.elementAt(i);
					String sub_label = hh.getLabel(sub_code);
					String sub_id = id + "_" + String.valueOf(i+1);
					printNode(pw, sub_label, sub_code, sub_id, level+1);
				}
			}

		    pw.println(indentation + "</ul>");
		    pw.println(indentation + "</div>");
		    pw.flush();
	    } else {
			String div_id = "DIV_" + id;
				pw.println("<img src=\"" + contextPath + "images/dot.gif\" id=\"" + img_id + "\" alt=\"show_hide\" onclick=\"show_hide('" + div_id + "');\" "
				+ ">"
				+ getHyperLink(label, code));
		}
		pw.println(indentation + "</li>");
		pw.flush();
	}

    private String getIndentation(int level) {
		if (level <= 0) return "";
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<level; i++) {
			buf.append(TAB);
		}
		return buf.toString();
	}

	private String getHyperLink(String label, String code) {
		if (focusCode != null && focusCode.compareTo(code) == 0) {
			label = "<font color=\"red\">" + label + "</font>";
		}
        return "&nbsp;<a href=\"#\" onclick=\"javascript:onClickTreeNode('" + code + "');\">" + label + "</a>";
	}


	public String print_tree() {
		StringBuffer buf = new StringBuffer();
		Vector roots = hh.getSubclassCodes("<Root>");
        buf.append("<ul>");
		for (int i=0; i<roots.size(); i++) {
		    String code = (String) roots.elementAt(i);
		    String label = hh.getLabel(code);
		    String id = "N_" + String.valueOf(i+1);

		    StringBuffer buf2 = print_node(new StringBuffer(), label, code, id, 0);
		    buf.append(buf2);
	 	}
	 	buf.append("</ul>");
        return buf.toString();
	}

    public void setFocusNodeExpandable(boolean expandable) {
		this.focusNodeExpandable = expandable;
	}

    public StringBuffer print_node(StringBuffer buf, String label, String code, String id, int level) {
		String indentation = getIndentation(level);
		String div_id = "DIV_" + id;
		buf.append("<li id=\"" + div_id + "\"" + " code=\"" + code + "\"><span>");
		String img_id = "IMG_" + id;
		if (expandableSet.contains(code)) {

            if (path_to_roots_nodes !=null && path_to_roots_nodes.contains(code)) {
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

            buf.append(indentation + "<div id=\"" +  div_id   + "\">");
		    buf.append(indentation + "<ul>");

			Vector subs = hh.getSubclassCodes(code);
			if (subs != null) {
				for (int i=0; i<subs.size(); i++) {
					String sub_code = (String) subs.elementAt(i);
					String sub_label = hh.getLabel(sub_code);
					String sub_id = id + "_" + String.valueOf(i+1);
					StringBuffer buf2 = print_node(new StringBuffer(), sub_label, sub_code, sub_id, level+1);
				    buf.append(buf2.toString());
				}
			}

		    buf.append(indentation + "</ul>");
		    buf.append(indentation + "</div>");
	    } else {
				buf.append("<img src=\"" + contextPath + "images/dot.gif\" id=\"" + img_id + "\" alt=\"show_hide\" onclick=\"show_hide('" + div_id + "');\" "
				+ ">"
				+ getHyperLink(label, code));
		}
		buf.append(indentation + "</li>");
		return buf;
	}


    public void generate(String outputfile) {
		set_context_path("ncimbrowser/");
        long ms = System.currentTimeMillis();
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
			pw.println("<html>");
			pw.println("<head>");
			pw.println("<title>");
			pw.println(outputfile);
			pw.println("</title>");
			pw.println("</head>");
			pw.println("<body>");

            pw.println(print_tree());

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

    public void generate(String outputfile, String content) {
		set_context_path("ncimbrowser/");
        long ms = System.currentTimeMillis();
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
			pw.println("<html>");
			pw.println("<head>");
			pw.println("<title>");
			pw.println(outputfile);
			pw.println("</title>");
			pw.println("</head>");
			pw.println("<body>");

            pw.println(content);

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

	public static void main(String[] args) {
		Vector v = Utils.readFile(args[0]);
		String focusCode = args[1];
		TreeNavigationHelper treeNavigationHelper = new TreeNavigationHelper(focusCode, v);
		String outputfile= "test.html";
		treeNavigationHelper.generate(outputfile);
	}
}
