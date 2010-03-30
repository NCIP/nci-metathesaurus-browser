package gov.nih.nci.evs.browser.utils;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;


/**
 * The Class SearchByAssociationIteratorDecorator.
 * Decorates a ResolvedConceptReferencesIterator to provide
 * paging support for Associated Concept-type searches. As the iterator
 * advances, subconcepts are queried from the decorated iterator on
 * demand, rather than all at once. This elminates the need to resolve
 * large CodedNodeGraphs.
 */
public class SearchByAssociationIteratorDecorator implements ResolvedConceptReferencesIterator {

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
	private List<ResolvedConceptReference> currentChildren = new ArrayList<ResolvedConceptReference>();


	private NameAndValueList associationNameAndValueList;
	private NameAndValueList associationQualifierNameAndValueList;


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
			boolean resolveForward,
			boolean resolveBackward,
			int resolveAssociationDepth,
			int maxToReturn){
		this.quickIterator = quickIterator;
		this.resolveForward = resolveForward;
		this.resolveBackward = resolveBackward;
		this.resolveAssociationDepth = resolveAssociationDepth;
		this.maxToReturn = maxToReturn;
	    this.associationNameAndValueList = null;
	    this.associationQualifierNameAndValueList = null;

	    System.out.println("Type 1 SearchByAssociationIteratorDecorator ");

	}

	public SearchByAssociationIteratorDecorator(
			ResolvedConceptReferencesIterator quickIterator,
			boolean resolveForward,
			boolean resolveBackward,
			NameAndValueList associationNameAndValueList,
			NameAndValueList associationQualifierNameAndValueList,
			int resolveAssociationDepth,
			int maxToReturn){

		this.quickIterator = quickIterator;
		this.resolveForward = resolveForward;
		this.resolveBackward = resolveBackward;
		this.resolveAssociationDepth = resolveAssociationDepth;
		this.maxToReturn = maxToReturn;
	    this.associationNameAndValueList = associationNameAndValueList;
	    this.associationQualifierNameAndValueList = associationQualifierNameAndValueList;

	    System.out.println("Type 2 SearchByAssociationIteratorDecorator ");
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator#get(int, int)
	 */
	public ResolvedConceptReferenceList get(int arg0, int arg1)
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

			try {
				return doGetNext();
			} catch (Exception e) {
				throw new LBResourceUnavailableException(e.getMessage());
			}
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator#next(int)
	 */
	public ResolvedConceptReferenceList next(int page)
			throws LBResourceUnavailableException, LBInvocationException {
		long startTime = System.currentTimeMillis();
		ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
		while(returnList.getResolvedConceptReferenceCount() < page && this.hasNext()){
			try {
				returnList.addResolvedConceptReference(doGetNext());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		//System.out.println("Paged " + page + " concepts in: " + (System.currentTimeMillis() - startTime));
		return returnList;
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator#scroll(int)
	 */
	public ResolvedConceptReferencesIterator scroll(int arg0)
			throws LBResourceUnavailableException, LBInvocationException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
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
	 *  Gets the number remaining in this Iterator.
	 *
	 *  NOTE: This is not an exact number. The Iterator is guarenteed to
	 *  have AT LEAST this amount remaining -- it may actually have more.
	 */
	public int numberRemaining() throws LBResourceUnavailableException {
		try {
			pageIfNecessary();
		} catch (Exception e) {
			throw new LBResourceUnavailableException(e.getMessage());
		}
		return this.quickIterator.numberRemaining() + this.currentChildren.size();
	}

	/* (non-Javadoc)
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
	protected ResolvedConceptReference doGetNext() throws Exception{
		pageIfNecessary();
		ResolvedConceptReference returnRef = this.currentChildren.get(this.position);
		position++;
		return returnRef;
	}

	/**
	 * Page if necessary.
	 *
	 * @throws Exception the exception
	 */
	protected void pageIfNecessary() throws Exception {
		LexBIGService lbs = RemoteServerUtil.createLexBIGService();
		if(position == this.currentChildren.size()){
			currentChildren.clear();
			this.position = 0;

			boolean cont_flag = true;

            //[#26965] Contains Relationship search returns invalid result
			//if (this.quickIterator.hasNext()) {
			while (this.quickIterator.hasNext() && this.currentChildren.size() == 0) {
				ResolvedConceptReference ref = this.quickIterator.next();
				if (ref != null) {
					//KLO
					String formalName = ref.getCodingSchemeName();
					/*


					ResolvedConceptReferenceList list =
						lbs.getNodeGraph(formalName, null, null)
						.resolveAsList(Constructors.createConceptReference(
								ref.getCode(),
								ref.getCodingSchemeName()),
								this.resolveForward,
								this.resolveBackward,
								0,
								this.resolveAssociationDepth,
								null,
								null,
								null,
								this.maxToReturn);
                    */

                    CodedNodeGraph cng = lbs.getNodeGraph(formalName, null, null);
                    // restrictToAssociations
                    // CodedNodeGraph restrictToAssociations(NameAndValueList association, NameAndValueList associationQualifiers)


					if (associationNameAndValueList != null) {
						cng = cng.restrictToAssociations(associationNameAndValueList, associationQualifierNameAndValueList);
					}

					ResolvedConceptReferenceList list =
						cng.resolveAsList(Constructors.createConceptReference(
								ref.getCode(),
								ref.getCodingSchemeName()),
								this.resolveForward,
								this.resolveBackward,
								0,
								this.resolveAssociationDepth,
								null,
								null,
								null,
								this.maxToReturn);

					populateCurrentChildren(list.getResolvedConceptReference(), false);

			    }
			}
		}
	}


	protected void displayRef(ResolvedConceptReference ref){
		System.out.println(ref.getConceptCode() + ":" + ref.getEntityDescription().getContent());
	}


	/**
	 * Populate current children.
	 *
	 * @param list the list
	 */
	//[#26965] Contains Relationship search returns invalid result
	public void populateCurrentChildren(ResolvedConceptReference[] list, boolean addRoot){
		if (list == null) return;

		for(ResolvedConceptReference ref : list){

			//displayRef(ref);

			if(addRoot) {
				this.currentChildren.add(ref);
			}
			if(ref.getSourceOf() != null){
				if (ref.getSourceOf().getAssociation() != null) {
					for(Association assoc : ref.getSourceOf().getAssociation()){
						populateCurrentChildren(assoc.getAssociatedConcepts().getAssociatedConcept(), true);
					}
			    }
			}

			if(ref.getTargetOf() != null){
				if (ref.getTargetOf().getAssociation() != null) {
					for(Association assoc : ref.getTargetOf().getAssociation()){
						populateCurrentChildren(assoc.getAssociatedConcepts().getAssociatedConcept(), true);
					}
			    }
			}
		}
	}
}
