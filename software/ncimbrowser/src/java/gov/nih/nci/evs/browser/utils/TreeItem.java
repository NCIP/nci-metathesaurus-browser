package gov.nih.nci.evs.browser.utils;

import java.util.*;
import java.io.*;

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

/*
 * public class TreeItem implements Serializable, Comparable<TreeItem> { public
 * String code = null; public String text = null; public boolean expandable =
 * false; public Map<String, List<TreeItem>> assocToChildMap = new
 * TreeMap<String, List<TreeItem>>(); public boolean equals(Object o) { return o
 * instanceof TreeItem && code.compareTo(((TreeItem) o).code) == 0; } public int
 * compareTo(TreeItem ti) { String c1 = code; String c2 = ti.code; if
 * (c1.startsWith("@")) return 1; if (c2.startsWith("@")) return -1; return
 * c1.compareTo(c2); } public TreeItem(String code, String text) { super();
 * this.code = code; this.text = text; } public void addAll(String assocText,
 * List<TreeItem> children) { for (TreeItem item : children) addChild(assocText,
 * item); } public void addChild(String assocText, TreeItem child) {
 * List<TreeItem> children = assocToChildMap.get(assocText); if (children ==
 * null) { children = new ArrayList<TreeItem>(); assocToChildMap.put(assocText,
 * children); } int i; if ((i = children.indexOf(child)) >= 0) { TreeItem
 * existingTreeItem = children.get(i); for (String assoc :
 * child.assocToChildMap.keySet()) { List<TreeItem> toAdd =
 * child.assocToChildMap.get(assoc); if (!toAdd.isEmpty()) {
 * existingTreeItem.addAll(assoc, toAdd); existingTreeItem.expandable = false; }
 * } } else children.add(child); }
 */

public class TreeItem implements Serializable, Comparable<TreeItem> {
    public String code = null;
    public String text = null;
    public String auis = null;
    public boolean expandable = false;
    public Map<String, List<TreeItem>> assocToChildMap =
        new TreeMap<String, List<TreeItem>>();

    public boolean equals(Object o) {
        return o instanceof TreeItem
            && code.compareTo(((TreeItem) o).code) == 0;
    }

    public int compareTo(TreeItem ti) {
        String c1 = code;
        String c2 = ti.code;
        if (c1.startsWith("@"))
            return 1;
        if (c2.startsWith("@"))
            return -1;
        int i = c1.compareTo(c2);
        return i != 0 ? i : text.compareTo(ti.text);
    }

    public TreeItem(String code, String text) {
        super();
        this.code = code;
        this.text = text;
        this.auis = null;
    }

    public TreeItem(String code, String text, String auiText) {
        super();
        this.code = code;
        this.text = text;
        this.auis = auiText;
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
