/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.utils;

import java.util.*;
import java.io.*;

/**
 * 
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
 * code = code; text = text; } public void addAll(String assocText,
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
    public String _code = null;
    public String _text = null;
    public String _auis = null;
    public boolean _expandable = false;
    public Map<String, List<TreeItem>> _assocToChildMap =
        new TreeMap<String, List<TreeItem>>();

    public boolean equals(Object o) {
        return o instanceof TreeItem
            && _code.compareTo(((TreeItem) o)._code) == 0;
    }

    public int compareTo(TreeItem ti) {
        String c1 = _code;
        String c2 = ti._code;
        if (c1.startsWith("@"))
            return 1;
        if (c2.startsWith("@"))
            return -1;
        int i = c1.compareTo(c2);
        return i != 0 ? i : _text.compareTo(ti._text);
    }

    public TreeItem(String code, String text) {
        super();
        _code = code;
        _text = text;
        _auis = null;
    }

    public TreeItem(String code, String text, String auiText) {
        super();
        _code = code;
        _text = text;
        _auis = auiText;
    }

    public void addAll(String assocText, List<TreeItem> children) {
        for (TreeItem item : children)
            addChild(assocText, item);
    }

    public void addChild(String assocText, TreeItem child) {
        List<TreeItem> children = _assocToChildMap.get(assocText);
        if (children == null) {
            children = new ArrayList<TreeItem>();
            _assocToChildMap.put(assocText, children);
        }
        int i;
        if ((i = children.indexOf(child)) >= 0) {
            TreeItem existingTreeItem = children.get(i);
            for (String assoc : child._assocToChildMap.keySet()) {
                List<TreeItem> toAdd = child._assocToChildMap.get(assoc);
                if (!toAdd.isEmpty()) {
                    existingTreeItem.addAll(assoc, toAdd);
                    existingTreeItem._expandable = false;
                }
            }
        } else
            children.add(child);
    }

}
