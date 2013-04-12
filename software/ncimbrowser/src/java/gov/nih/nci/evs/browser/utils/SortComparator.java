/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.utils;

import java.io.*;
import java.util.*;

import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.concepts.Entity;

/**
 * 
 */

/**
 * @author EVS Team
 * @version 1.0
 *
 *          Modification history Initial implementation kim.ong@ngc.com
 *
 */

public class SortComparator implements Serializable, Comparator<Object> {

    private static final int SORT_BY_NAME = 1;
    private static final int SORT_BY_CODE = 2;
    private int _sort_option = SORT_BY_NAME;

    public SortComparator() {

    }

    public SortComparator(int sort_option) {
        _sort_option = sort_option;
    }

    private String getKey(Object c, int sort_option) {
        if (c == null)
            return "NULL";
        if (c instanceof Entity) {
            Entity concept =
                (Entity) c;
            if (sort_option == SORT_BY_CODE)
                return concept.getEntityCode();
            return concept.getEntityDescription().getContent();

        }

        else if (c instanceof AssociatedConcept) {
            AssociatedConcept ac = (AssociatedConcept) c;
            if (sort_option == SORT_BY_CODE)
                return ac.getConceptCode();
            return ac.getEntityDescription().getContent();
        }

        else if (c instanceof ResolvedConceptReference) {
            ResolvedConceptReference ac = (ResolvedConceptReference) c;
            if (sort_option == SORT_BY_CODE)
                return ac.getConceptCode();
            return ac.getEntityDescription().getContent();
        }

        else if (c instanceof TreeItem) {
            TreeItem ti = (TreeItem) c;
            if (sort_option == SORT_BY_CODE)
                return ti._code;
            return ti._text;
        }

        else if (c instanceof String) {
            String s = (String) c;
            return s;
        }

        return c.toString();
    }
/*
    public int compare(Object object1, Object object2) {
        // case insensitive sort
        String key1 = getKey(object1, _sort_option).toLowerCase();
        String key2 = getKey(object2, _sort_option).toLowerCase();

        if (key1.startsWith("..."))
            return 1;
        else if (key2.startsWith("..."))
            return -1;
        else
            return key1.compareTo(key2);
    }
*/

    private String replaceCharacter(String s, char from, char to) {
		if (s == null) return null;
		int ascii_from = (int) from;
		int ascii_to   = (int) to;
		//String t = "";
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			int ascii_c = (int) c;

			if (ascii_c == ascii_from) {
				//t = t + to;
				buf.append(to);
			} else {
				//t = t + c;
				buf.append(c);
			}
		}
		String t = buf.toString();
	    return t;
	}

    public int compare(Object object1, Object object2) {
        // case insensitive sort
        String key1 = getKey(object1, _sort_option);
        String key2 = getKey(object2, _sort_option);

        if (key1 == null || key2 == null)
            return 0;
        key1 = getKey(object1, _sort_option).toLowerCase();
        key2 = getKey(object2, _sort_option).toLowerCase();

        key1 = replaceCharacter(key1, ' ', '~');
        key1 = replaceCharacter(key1, '|', ' ');
        key1 = replaceCharacter(key1, '$', ' ');

        key2 = replaceCharacter(key2, ' ', '~');
        key2 = replaceCharacter(key2, '|', ' ');
        key2 = replaceCharacter(key2, '$', ' ');

        return key1.compareTo(key2);
    }

}
