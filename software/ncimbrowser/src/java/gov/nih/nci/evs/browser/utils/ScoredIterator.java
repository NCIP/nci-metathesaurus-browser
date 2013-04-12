/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.utils;

import java.util.*;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;

/**
 * 
 */

/**
 * Used to wrap scored results for consumption as a standard
 * ResolvedConceptReferenceIterator.
 */
public class ScoredIterator implements ResolvedConceptReferencesIterator {
    private static final long serialVersionUID = -4975979409144702081L;
    private ScoredTerm[] _scoredTerms = null;
    private int _position = 0;
    private int _scrolled = 0;

    /**
     * Construct from a map of ScoredTerms, ordered from high to low score. If >
     * 0, the maximum items parameter sets an upper limit on the number of
     * top-scored items to maintain and return.
     * 
     * @param scoredTerms
     * @param maxItems
     */
    public ScoredIterator(Collection<ScoredTerm> scoredTerms, int maxItems) {
        // Add all scored terms, sorted according to ScoredTerm comparator ...
        List<ScoredTerm> temp = new ArrayList<ScoredTerm>();
        temp.addAll(scoredTerms);
        Collections.sort(temp);

        // Maintain only as many items as the specified maximum ...
        int limit =
            maxItems > 0 ? Math.min(maxItems, scoredTerms.size()) : scoredTerms
                .size();
        int count = 0;
        _scoredTerms = new ScoredTerm[limit];
        for (Iterator<ScoredTerm> terms = temp.listIterator(); terms.hasNext()
            && count < limit; count++)
            _scoredTerms[count] = terms.next();
    }

    /**
     * Construct from a pre-sorted array of ScoredTerms.
     * 
     * @param scoredTerms
     */
    public ScoredIterator(ScoredTerm[] scoredTerms) {
        _scoredTerms = scoredTerms;
    }

    /**
     * Returns a specific range of items without altering cursor position.
     */
    public ResolvedConceptReferenceList get(int start, int end)
            throws LBResourceUnavailableException, LBInvocationException,
            LBParameterException {
        verifyResources();
        ResolvedConceptReferenceList result =
            new ResolvedConceptReferenceList();
        int stop = Math.max(0, Math.min(_scoredTerms.length, end));
        if (start < 0 || stop < start)
            throw new LBParameterException("Index out of bounds.");
        for (int i = start; i < stop; i++)
            result.addResolvedConceptReference(_scoredTerms[i]._ref);
        return result;
    }

    /**
     * Returns items skipped by last scroll() without altering cursor position.
     */
    public ResolvedConceptReferenceList getNext() {
        ResolvedConceptReferenceList result = null;
        try {
            result = get(_position - _scrolled, _position);
        } catch (LBException e) {
            result = new ResolvedConceptReferenceList();
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Returns the next item and advances the cursor.
     */
    public ResolvedConceptReference next()
            throws LBResourceUnavailableException, LBInvocationException {
        verifyResources();
        return hasNext() ? _scoredTerms[_position++]._ref : null;
    }

    /**
     * Returns the next 'n' items and advances the cursor.
     */
    public ResolvedConceptReferenceList next(int maxToReturn)
            throws LBResourceUnavailableException, LBInvocationException {
        verifyResources();
        ResolvedConceptReferenceList result = null;
        try {
            int pageSize =
                Math.max(0, Math.min(maxToReturn, numberRemaining()));
            result = get(_position, _position + pageSize);
            _position += pageSize;
        } catch (LBParameterException e) {
            result = new ResolvedConceptReferenceList();
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Skips 'n' items which are available via getNext() until a call to next(),
     * returning self.
     */
    public ResolvedConceptReferencesIterator scroll(int maxToReturn)
            throws LBResourceUnavailableException, LBInvocationException {
        verifyResources();
        _scrolled = Math.max(0, Math.min(maxToReturn, numberRemaining()));
        _position += _scrolled;
        return this;
    }

    /**
     * Indicates if more items are available through next() operations.
     */
    public boolean hasNext() throws LBResourceUnavailableException {
        verifyResources();
        return _position < _scoredTerms.length;
    }

    /**
     * Indicates the number of items available to retrieve via next()
     * operations.
     */
    public int numberRemaining() throws LBResourceUnavailableException {
        return hasNext() ? _scoredTerms.length - _position : 0;
    }

    /**
     * Releases the maintained terms and invalidates the iterator.
     */
    public void release() throws LBResourceUnavailableException {
        _scoredTerms = null;
    }

    /**
     * Verifies the iterator is still valid.
     */
    protected void verifyResources() throws LBResourceUnavailableException {
        if (_scoredTerms == null)
            throw new LBResourceUnavailableException(
                "Iterator resources released.");
    }
}
