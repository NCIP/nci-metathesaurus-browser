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

/**
 * The Class ResolvedConceptReferencesIteratorWrapper.
 * Decorates a ResolvedConceptReferencesIterator to provide
 * paging support for Associated Concept-type searches. As the iterator
 * advances, subconcepts are queried from the decorated iterator on
 * demand, rather than all at once. This elminates the need to resolve
 * large CodedNodeGraphs.
 */
public class ResolvedConceptReferencesIteratorWrapper {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4126716487618136771L;

	/** The lbs. */
	private ResolvedConceptReferencesIterator iterator;

	/** The quick iterator. */
	private String message = null;


	public ResolvedConceptReferencesIteratorWrapper(ResolvedConceptReferencesIterator iterator) {
		this.iterator = iterator;
		this.message = null;
	}

	public ResolvedConceptReferencesIteratorWrapper(ResolvedConceptReferencesIterator iterator, String message) {
		this.iterator = iterator;
		this.message = message;
	}

	public void setIterator(ResolvedConceptReferencesIterator iterator) {
		this.iterator = iterator;
	}

	public ResolvedConceptReferencesIterator getIterator() {
		return this.iterator;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
