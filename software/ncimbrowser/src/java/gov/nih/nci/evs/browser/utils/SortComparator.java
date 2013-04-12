/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.utils;

import java.util.*;

import org.LexGrid.LexBIG.DataModel.Core.*;

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

public class SortComparator implements Comparator<Object> {

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
        if (c instanceof org.LexGrid.concepts.Concept) {
            org.LexGrid.concepts.Concept concept =
                (org.LexGrid.concepts.Concept) c;
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
}
