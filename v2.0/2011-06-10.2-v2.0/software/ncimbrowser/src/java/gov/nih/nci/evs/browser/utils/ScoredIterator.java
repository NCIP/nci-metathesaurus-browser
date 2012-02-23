package gov.nih.nci.evs.browser.utils;

import java.util.*;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;

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
