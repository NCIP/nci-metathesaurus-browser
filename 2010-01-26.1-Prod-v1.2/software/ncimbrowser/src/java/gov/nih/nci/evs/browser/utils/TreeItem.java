package gov.nih.nci.evs.browser.utils;

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

/*
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
*/

public class TreeItem implements Serializable, Comparable<TreeItem> {
        public String code = null;
        public String text = null;
        public String auis = null;
        public boolean expandable = false;
        public Map<String, List<TreeItem>> assocToChildMap = new TreeMap<String, List<TreeItem>>();

        public boolean equals(Object o) {
            return o instanceof TreeItem && code.compareTo(((TreeItem) o).code) == 0;
        }

        public int compareTo(TreeItem ti) {
            String c1 = code;
            String c2 = ti.code;
            if (c1.startsWith("@")) return 1;
            if (c2.startsWith("@")) return -1;
            int i = c1.compareTo(c2);
            return i != 0 ? i
                : text.compareTo(ti.text);
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

