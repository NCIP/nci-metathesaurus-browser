/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.utils;

import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.commonTypes.*;

/**
 * 
 */

/**
 * Used to manage and sort search results based on a scoring algorithm.
 */
class ScoredTerm implements Comparable<ScoredTerm> {
    public ResolvedConceptReference _ref = null;
    private float _score = 0;

    /**
     * Construct a ScoredTerm based on the given concept reference and score.
     * 
     * @param ref
     * @param score
     */
    public ScoredTerm(ResolvedConceptReference ref, float score) {
        _ref = ref;
        _score = score;
    }

    /**
     * Compare this ScoredTerm to another. Comparison is by score, using
     * description text as tie-breaker ...
     */
    public int compareTo(ScoredTerm st) {
        float f = st._score - _score;
        if (f != 0)
            return f > 0 ? 1 : 0;
        EntityDescription ed1 = _ref.getEntityDescription();
        EntityDescription ed2 = st._ref.getEntityDescription();
        String term1 = ed1 != null ? ed1.getContent() : "";
        String term2 = ed2 != null ? ed2.getContent() : "";
        return term1.compareTo(term2);
    }
}
