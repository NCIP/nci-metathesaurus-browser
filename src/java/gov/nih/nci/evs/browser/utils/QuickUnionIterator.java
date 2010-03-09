//package org.lexevs.codednodeset;
package gov.nih.nci.evs.browser.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

/**
 * The Class QuickUnionIterator. Provides Union-like resolving of CodedNodeSets
 * without the Union performance penalty. This Iterator assumes that all restrictions
 * have been placed on the CodedNodeSets BEFORE being passed into this Iterator.
 */
public class QuickUnionIterator implements ResolvedConceptReferencesIterator{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6285970594380754741L;

	/** The current iterator. */
	private int currentIterator = 0;

	/** The iterators. */
	private List<ResolvedConceptReferencesIterator> iterators = new ArrayList<ResolvedConceptReferencesIterator>();

	/**
	 * Instantiates a new Iterator. Be sure that any desired restrictions
	 * have already been placed on the CodedNodeSets before passing into
	 * this constructor
	 *
	 * @param codedNodeSets the coded node sets
	 * @param sortOptions the sort options
	 * @param filterOptions the filter options
	 * @param restrictToProperties the restrict to properties
	 * @param restrictToPropertyTypes the restrict to property types
	 * @param resolve the resolve
	 *
	 * @throws LBException the LB exception
	 */
	public QuickUnionIterator(Vector<CodedNodeSet> codedNodeSets,
			SortOptionList sortOptions,
			LocalNameList filterOptions,
			LocalNameList restrictToProperties,
			PropertyType[] restrictToPropertyTypes,
			boolean resolve) throws LBException {

		for(CodedNodeSet cns : codedNodeSets){
			//KLO 012310
			if (cns != null) {
				try {
					ResolvedConceptReferencesIterator iterator = cns.resolve(sortOptions, filterOptions, restrictToProperties, restrictToPropertyTypes, resolve);
					if (iterator != null) {
						iterators.add(iterator);
					}
				} catch (Exception ex) {
					System.out.println("QuickUnionIterator constructor - cns.resolve throws exception???");
				}
		    }
		}

		Collections.sort(iterators, new IteratorSizeComparator());
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator#get(int, int)
	 */
	public ResolvedConceptReferenceList get(int start, int end)
			throws LBResourceUnavailableException, LBInvocationException,
			LBParameterException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator#getNext()
	 */
	public ResolvedConceptReferenceList getNext() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator#next()
	 */
	public ResolvedConceptReference next()
			throws LBResourceUnavailableException, LBInvocationException {
		return this.next(1).getResolvedConceptReference(0);
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator#next(int)
	 */
	public ResolvedConceptReferenceList next(int maxToReturn)
			throws LBResourceUnavailableException, LBInvocationException {
		ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
		while(returnList.getResolvedConceptReferenceCount() < maxToReturn){
			ResolvedConceptReference ref = getNextFromList();
			if(ref == null){
				return returnList;
			} else {
				returnList.addResolvedConceptReference(ref);
			}
		}
		return returnList;
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator#scroll(int)
	 */
	public ResolvedConceptReferencesIterator scroll(int maxToReturn)
			throws LBResourceUnavailableException, LBInvocationException {
		throw new UnsupportedOperationException();

	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Utility.Iterators.EntityListIterator#hasNext()
	 */
	public boolean hasNext() throws LBResourceUnavailableException {
		removeEmptyIterators();
		return this.iterators.size() > 0;
	}

	/**
	 * Removes the empty iterators.
	 */
	private void removeEmptyIterators(){
		List<ResolvedConceptReferencesIterator> newList = new ArrayList<ResolvedConceptReferencesIterator>();
		for(ResolvedConceptReferencesIterator itr : this.iterators){
			try {
				if(itr.hasNext()){
					newList.add(itr);
				}
			} catch (LBResourceUnavailableException e) {
				throw new RuntimeException(e);
			}
		}
		iterators = newList;
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Utility.Iterators.EntityListIterator#numberRemaining()
	 */
	public int numberRemaining() throws LBResourceUnavailableException {
		int number = 0;
		for(ResolvedConceptReferencesIterator itr : this.iterators){
			number += itr.numberRemaining();
		}
		return number;
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Utility.Iterators.EntityListIterator#release()
	 */
	public void release() throws LBResourceUnavailableException {
		for(ResolvedConceptReferencesIterator itr : this.iterators){
			itr.release();
		}
	}

	/**
	 * Gets the next from list.
	 *
	 * @return the next from list
	 */
	private ResolvedConceptReference getNextFromList(){
		try {
			while(this.hasNext()){
				int iterator = currentIterator % this.iterators.size();
				ResolvedConceptReferencesIterator itr = this.iterators.get(iterator);
				if(itr.hasNext()){
					currentIterator++;
					return itr.next();
				} else {
					currentIterator++;
				}
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * The Class IteratorSizeComparator.
	 */
	private class IteratorSizeComparator implements Comparator<ResolvedConceptReferencesIterator>{

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(ResolvedConceptReferencesIterator itr1,
				ResolvedConceptReferencesIterator itr2) {
			try {
				return itr2.numberRemaining() - itr1.numberRemaining();
			} catch (LBResourceUnavailableException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
