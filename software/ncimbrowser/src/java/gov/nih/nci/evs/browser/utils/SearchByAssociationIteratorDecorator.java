package gov.nih.nci.evs.browser.utils;

import java.util.*;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Utility.*;
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
 * The Class SearchByAssociationIteratorDecorator. Decorates a
 * ResolvedConceptReferencesIterator to provide paging support for Associated
 * Concept-type searches. As the iterator advances, subconcepts are queried from
 * the decorated iterator on demand, rather than all at once. This elminates the
 * need to resolve large CodedNodeGraphs.
 */
public class SearchByAssociationIteratorDecorator implements
        ResolvedConceptReferencesIterator {
    // private static Logger _logger =
    // Logger.getLogger(SearchByAssociationIteratorDecorator.class);

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4126716487618136771L;

    /** The lbs. */
    private static LexBIGService lbs = RemoteServerUtil.createLexBIGService();

    /** The quick iterator. */
    private ResolvedConceptReferencesIterator quickIterator;

    /** The resolve forward. */
    private boolean resolveForward;

    /** The resolve backward. */
    private boolean resolveBackward;

    /** The resolve association depth. */
    private int resolveAssociationDepth;

    /** The max to return. */
    private int maxToReturn;

    /** The position. */
    private int position = 0;

    /** The current children. */
    private List<ResolvedConceptReference> currentChildren =
        new ArrayList<ResolvedConceptReference>();

    private NameAndValueList associationNameAndValueList;
    private NameAndValueList associationQualifierNameAndValueList;

    private HashSet hset = null;

    /**
     * Instantiates a new search by association iterator decorator.
     * 
     * @param quickIterator the quick iterator
     * @param resolveForward the resolve forward
     * @param resolveBackward the resolve backward
     * @param resolveAssociationDepth the resolve association depth
     * @param maxToReturn the max to return
     */
    public SearchByAssociationIteratorDecorator(
        ResolvedConceptReferencesIterator quickIterator,
        boolean resolveForward, boolean resolveBackward,
        int resolveAssociationDepth, int maxToReturn) {
        this.quickIterator = quickIterator;
        this.resolveForward = resolveForward;
        this.resolveBackward = resolveBackward;
        this.resolveAssociationDepth = resolveAssociationDepth;
        this.maxToReturn = maxToReturn;
        this.associationNameAndValueList = null;
        this.associationQualifierNameAndValueList = null;

        this.hset = new HashSet();

        // _logger.debug("Type 1 SearchByAssociationIteratorDecorator ");

    }

    public SearchByAssociationIteratorDecorator(
        ResolvedConceptReferencesIterator quickIterator,
        boolean resolveForward, boolean resolveBackward,
        NameAndValueList associationNameAndValueList,
        NameAndValueList associationQualifierNameAndValueList,
        int resolveAssociationDepth, int maxToReturn) {

        this.quickIterator = quickIterator;
        this.resolveForward = resolveForward;
        this.resolveBackward = resolveBackward;
        this.resolveAssociationDepth = resolveAssociationDepth;
        this.maxToReturn = maxToReturn;
        this.associationNameAndValueList = associationNameAndValueList;
        this.associationQualifierNameAndValueList =
            associationQualifierNameAndValueList;

        this.hset = new HashSet();

        // _logger.debug("Type 2 SearchByAssociationIteratorDecorator ");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator
     * #get(int, int)
     */
    public ResolvedConceptReferenceList get(int arg0, int arg1)
            throws LBResourceUnavailableException, LBInvocationException,
            LBParameterException {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator
     * #getNext()
     */
    public ResolvedConceptReferenceList getNext() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator
     * #next()
     */
    public ResolvedConceptReference next()
            throws LBResourceUnavailableException, LBInvocationException {

        try {
            return doGetNext();
        } catch (Exception e) {
            throw new LBResourceUnavailableException(e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator
     * #next(int)
     */
    public ResolvedConceptReferenceList next(int page)
            throws LBResourceUnavailableException, LBInvocationException {
        // long startTime = System.currentTimeMillis();
        ResolvedConceptReferenceList returnList =
            new ResolvedConceptReferenceList();

        // _logger.debug("next method: getResolvedConceptReferenceCount() " +
        // returnList.getResolvedConceptReferenceCount());
        // _logger.debug("next method: page " + page);

        while (returnList.getResolvedConceptReferenceCount() < page
            && this.hasNext()) {
            try {
                returnList.addResolvedConceptReference(doGetNext());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return returnList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator
     * #scroll(int)
     */
    public ResolvedConceptReferencesIterator scroll(int arg0)
            throws LBResourceUnavailableException, LBInvocationException {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.Utility.Iterators.EntityListIterator#hasNext()
     */
    public boolean hasNext() throws LBResourceUnavailableException {
        try {
            pageIfNecessary();
        } catch (Exception e) {
            throw new LBResourceUnavailableException(e.getMessage());
        }
        return this.currentChildren.size() > 0;
    }

    /**
     * Gets the number remaining in this Iterator.
     * 
     * NOTE: This is not an exact number. The Iterator is guarenteed to have AT
     * LEAST this amount remaining -- it may actually have more.
     */
    public int numberRemaining() throws LBResourceUnavailableException {
        // _logger.debug("SearchByAssociationIteratorDecorator: calling numberRemaining()	");
        try {
            pageIfNecessary();
        } catch (Exception e) {
            throw new LBResourceUnavailableException(e.getMessage());
        }

        // _logger.debug("SearchByAssociationIteratorDecorator: this.quickIterator.numberRemaining(): "
        // + this.quickIterator.numberRemaining());
        // _logger.debug("SearchByAssociationIteratorDecorator: this.currentChildren.size(): "
        // + this.currentChildren.size());

        // int total = this.quickIterator.numberRemaining() +
        // this.currentChildren.size();
        int total = this.currentChildren.size();
        // _logger.debug("SearchByAssociationIteratorDecorator: total: " +
        // total);

        // return this.quickIterator.numberRemaining() +
        // this.currentChildren.size();
        return this.currentChildren.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.Utility.Iterators.EntityListIterator#release()
     */
    public void release() throws LBResourceUnavailableException {
        quickIterator.release();
    }

    /**
     * Do get next.
     * 
     * @return the resolved concept reference
     * 
     * @throws Exception the exception
     */
    protected ResolvedConceptReference doGetNext() throws Exception {
        pageIfNecessary();
        ResolvedConceptReference returnRef =
            this.currentChildren.get(this.position);

        // _logger.debug("doGetNext() position: " + this.position);
        displayRef(returnRef);

        position++;
        return returnRef;
    }

    /**
     * Page if necessary.
     * 
     * @throws Exception the exception
     */
    protected void pageIfNecessary() throws Exception {

        // _logger.debug("pageIfNecessary ...");

        LexBIGService lbs = RemoteServerUtil.createLexBIGService();

        // _logger.debug("position: " + position +
        // " ----------- currentChildren.size: " + this.currentChildren.size());

        if (position == this.currentChildren.size()) {
            currentChildren.clear();
            this.position = 0;

            // [#26965] Contains Relationship search returns invalid result
            // if (this.quickIterator.hasNext()) {

            while (this.quickIterator.hasNext()
                && this.currentChildren.size() == 0) {
                // while (this.quickIterator.hasNext()) {
                ResolvedConceptReference ref = this.quickIterator.next();
                if (ref != null) {
                    // KLO
                    String formalName = ref.getCodingSchemeName();
                    CodedNodeGraph cng =
                        lbs.getNodeGraph(formalName, null, null);

                    if (associationNameAndValueList != null) {
                        cng =
                            cng.restrictToAssociations(
                                associationNameAndValueList,
                                associationQualifierNameAndValueList);
                    }

                    ResolvedConceptReferenceList list =
                        cng.resolveAsList(Constructors.createConceptReference(
                            ref.getCode(), ref.getCodingSchemeName()),
                            this.resolveForward, this.resolveBackward, 0,
                            this.resolveAssociationDepth, null, null, null,
                            this.maxToReturn);

                    // _logger.debug("Calling populateCurrentChildren ...");
                    // populateCurrentChildren(list.getResolvedConceptReference(),
                    // false);
                    populateCurrentChildren(list.getResolvedConceptReference(),
                        false);

                }
            }
        }
        // _logger.debug("Exiting pageIfNecessary(): this.currentChildren.size() "
        // + this.currentChildren.size());

    }

    protected void displayRef(ResolvedConceptReference ref) {
        // _logger.debug(ref.getConceptCode() + ":" +
        // ref.getEntityDescription().getContent());
    }

    protected void displayRef(String msg, ResolvedConceptReference ref) {
        // _logger.debug(msg + " " + ref.getConceptCode() + ":" +
        // ref.getEntityDescription().getContent());
    }

    /**
     * Populate current children.
     * 
     * @param list the list
     */
    // [#26965] Contains Relationship search returns invalid result
    public void populateCurrentChildren(ResolvedConceptReference[] list,
        boolean addRoot) {
        if (list == null)
            return;

        for (ResolvedConceptReference ref : list) {

            displayRef("Root: ", ref);

            if (addRoot) {
                if (!hset.contains(ref.getConceptCode())) {
                    hset.add(ref.getConceptCode());
                    // _logger.debug("\tbefore addRoot this.currentChildren.size() "
                    // + this.currentChildren.size());
                    displayRef(ref);
                    this.currentChildren.add(ref);
                    // _logger.debug("\tafter addRoot this.currentChildren.size() "
                    // + this.currentChildren.size());
                }
            } else {
                // _logger.debug("\tDO NOT add: ");
                displayRef("discarded ", ref);
            }

            if (ref.getSourceOf() != null) {
                if (ref.getSourceOf().getAssociation() != null) {
                    for (Association assoc : ref.getSourceOf().getAssociation()) {
                        populateCurrentChildren(assoc.getAssociatedConcepts()
                            .getAssociatedConcept(), true);
                    }
                }
            } else {
                // _logger.debug("\tref.getSourceOf() == null -- nothing done.");
            }

            if (ref.getTargetOf() != null) {
                if (ref.getTargetOf().getAssociation() != null) {
                    for (Association assoc : ref.getTargetOf().getAssociation()) {
                        populateCurrentChildren(assoc.getAssociatedConcepts()
                            .getAssociatedConcept(), true);
                    }
                }
            } else {
                // _logger.debug("\tref.getTargetOf() == null -- nothing done.");
            }
        }

        // _logger.debug("\tExiting populateCurrentChildren");
    }

}
