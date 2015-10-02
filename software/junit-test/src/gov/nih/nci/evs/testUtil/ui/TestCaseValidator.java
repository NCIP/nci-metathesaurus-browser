package gov.nih.nci.evs.testUtil.ui;


import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.utils.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;


/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008-2015 NGIT. This software was developed in conjunction
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
 * @author EVS Team
 * @version 1.0
 *
 * Modification history:
 *     Initial implementation kim.ong@ngc.com
 *
 */


public class TestCaseValidator
{

// Variable declaration
    private LexBIGService lbSvc = null;
    private ResolvedConceptReferencesIterator rcr_iterator = null;
    private ResolvedConceptReference rcref = null;

// Default constructor
	public TestCaseValidator(LexBIGService lbSvc) {
	    this.lbSvc = lbSvc;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Simple search
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//name or code search
    public ResolvedConceptReference validate(String scheme, String version, String matchText, String target, String algorithm) {
		ResolvedConceptReferencesIterator iterator = search(scheme, version, matchText, target, algorithm);
		if (iterator == null) return null;
		ResolvedConceptReference rcref = null;
		try {
			while(iterator.hasNext()) {
				rcref = iterator.next();
				return rcref;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

    public ResolvedConceptReferencesIterator search(String scheme, String version, String matchText, String target, String algorithm) {
	    ResolvedConceptReferencesIterator rcr_iterator = null;
	    try {
			rcr_iterator = new SimpleSearchUtils(lbSvc).search(scheme, version, matchText, target, algorithm);
			return rcr_iterator;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

    public ResolvedConceptReference	validatePropertySearch(
        String scheme, String version, String matchText, String source,
        String matchAlgorithm, boolean excludeDesignation, boolean ranking,
        int maxToReturn) {

        ResolvedConceptReferencesIterator iterator = searchByProperties(
			scheme, version, matchText, source,
			matchAlgorithm, excludeDesignation, ranking,
			maxToReturn);

		if (iterator == null) return null;
		ResolvedConceptReference rcref = null;
		try {
			while(iterator.hasNext()) {
				rcref = iterator.next();
				return rcref;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

    // property search
    public ResolvedConceptReferencesIterator search(
        String scheme, String version, String matchText,
        String[] property_types, String[] property_names, String source,
        String matchAlgorithm, boolean excludeDesignation, boolean ranking,
        int maxToReturn) {

	    ResolvedConceptReferencesIterator rcr_iterator = null;
	    try {
			ResolvedConceptReferencesIteratorWrapper wrapper = new SearchUtils(lbSvc).searchByProperties(
				scheme, version, matchText,
				property_types, property_names, source,
				matchAlgorithm, excludeDesignation, ranking,
				maxToReturn);
			if (wrapper != null) {
				rcr_iterator = wrapper.getIterator();
				return rcr_iterator;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return null;
	}


    public ResolvedConceptReferencesIterator searchByProperties(
        String scheme, String version, String matchText, String source,
        String matchAlgorithm, boolean excludeDesignation, boolean ranking,
        int maxToReturn) {
	    Vector schemes = new Vector();
	    Vector versions = new Vector();
	    schemes.add(scheme);
	    versions.add(version);
	    return searchByProperties(
			schemes, versions, matchText, source,
			matchAlgorithm, excludeDesignation, ranking,
			maxToReturn);
	}


    public ResolvedConceptReferencesIterator searchByProperties(
        Vector schemes, Vector versions, String matchText, String source,
        String matchAlgorithm, boolean excludeDesignation, boolean ranking,
        int maxToReturn) {

	    ResolvedConceptReferencesIterator rcr_iterator = null;
	    try {
			ResolvedConceptReferencesIteratorWrapper wrapper = new SearchUtils(lbSvc).searchByProperties(
				schemes, versions, matchText, source,
				matchAlgorithm, excludeDesignation, ranking,
				maxToReturn);
			if (wrapper != null) {
				rcr_iterator = wrapper.getIterator();
				return rcr_iterator;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


	//relationship search
    public ResolvedConceptReference	validate(String scheme, String version,
                                             String matchText,
											 String[] associationsToNavigate, String[] association_qualifier_names,
											 String[] association_qualifier_values, int search_direction,
											 String source, String matchAlgorithm, boolean designationOnly,
											 boolean ranking, int maxToReturn
                                             ) {
	    ResolvedConceptReferencesIterator iterator = search(scheme, version, matchText,
											 associationsToNavigate, association_qualifier_names,
											 association_qualifier_values, search_direction,
											 source, matchAlgorithm, designationOnly,
											 ranking, maxToReturn);
		if (iterator == null) return null;
		ResolvedConceptReference rcref = null;
		try {
			while(iterator.hasNext()) {
				rcref = iterator.next();
				return rcref;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


    public ResolvedConceptReferencesIterator search(String scheme, String version,
                                             String matchText,
											 String[] associationsToNavigate, String[] association_qualifier_names,
											 String[] association_qualifier_values, int search_direction,
											 String source, String matchAlgorithm, boolean designationOnly,
											 boolean ranking, int maxToReturn
                                             ) {
	    ResolvedConceptReferencesIterator rcr_iterator = null;
	    try {
			ResolvedConceptReferencesIteratorWrapper wrapper = new SearchUtils(lbSvc).searchByAssociations(
				scheme, version, matchText,
				associationsToNavigate, association_qualifier_names,
				association_qualifier_values, search_direction,
				source, matchAlgorithm, designationOnly,
				ranking, maxToReturn);
			if (wrapper != null) {
				rcr_iterator = wrapper.getIterator();
				return rcr_iterator;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Mapping search
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//validate mapping search
    public ResolvedConceptReference	validateMappingSearch(
		                                     String scheme, String version,
                                             String matchText,
                                             String target,
											 String matchAlgorithm,
											 int maxToReturn
                                             ) {
	    ResolvedConceptReferencesIterator iterator = searchMapping(
		                                     scheme, version,
                                             matchText,
                                             target,
											 matchAlgorithm,
											 maxToReturn);

		if (iterator == null) return null;
		ResolvedConceptReference rcref = null;
		try {
			while(iterator.hasNext()) {
				rcref = iterator.next();
				return rcref;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// mapping search
    public ResolvedConceptReferencesIterator searchMapping(
		                                     String scheme, String version,
                                             String matchText,
                                             String target,
											 String matchAlgorithm,
											 int maxToReturn
                                             ) {
	       return new MappingSearchUtils(lbSvc).searchMapping(
		                                     scheme, version,
                                             matchText,
                                             target,
											 matchAlgorithm,
											 maxToReturn);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Resolved value set search
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//resolved value set search
    public ResolvedConceptReference validateValueSetSearch(
		                                     String scheme, String version,
                                             String matchText,
                                             String target,
											 String matchAlgorithm,
											 int maxToReturn
                                             ) {
	    ResolvedConceptReferencesIterator iterator = searchValueSet(
		                                     scheme, version,
                                             matchText,
                                             target,
											 matchAlgorithm,
											 maxToReturn);

		if (iterator == null) return null;
		ResolvedConceptReference rcref = null;
		try {
			while(iterator.hasNext()) {
				rcref = iterator.next();
				return rcref;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}



    public ResolvedConceptReferencesIterator searchValueSet(
		                                     String scheme, String version,
                                             String matchText,
                                             String target,
											 String matchAlgorithm,
											 int maxToReturn
                                             ) {
		   if (target.compareTo("Code") == 0 || target.compareTo("codes") == 0) {
                return new ValueSetSearchUtils().searchByCode(
				        scheme, matchText, maxToReturn);
		   } else if (target.compareTo("Name") == 0 || target.compareTo("names") == 0) {
                return new ValueSetSearchUtils().searchByName(
				        scheme, matchText, matchAlgorithm, maxToReturn);
		   }
	       return null;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Multiple search
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ResolvedConceptReference validateMultipleSearch(
		                                     Vector schemes, Vector versions,
                                             String matchText,
                                             String target,
											 String matchAlgorithm,
											 int maxToReturn
                                             ) {
	    ResolvedConceptReferencesIterator iterator = multipleSearch(
		                                     schemes, versions,
                                             matchText,
                                             target,
											 matchAlgorithm,
											 maxToReturn);

		if (iterator == null) return null;
		ResolvedConceptReference rcref = null;
		try {
			while(iterator.hasNext()) {
				rcref = iterator.next();
				return rcref;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


    public ResolvedConceptReferencesIterator multipleSearch(
		                                     Vector schemes, Vector versions,
                                             String matchText,
                                             String target,
											 String matchAlgorithm,
											 int maxToReturn
                                             ) {
	    ResolvedConceptReferencesIterator iterator = null;
	    ResolvedConceptReferencesIteratorWrapper wrapper = null;
		String source = "ALL";
		boolean designationOnly = false;
		boolean ranking = true;
		boolean excludeDesignation = true;

	    try {
			if (target.compareTo("Name") == 0 || target.compareTo("names") == 0) {
				return new SimpleSearchUtils(lbSvc).search(schemes, versions, matchText, SimpleSearchUtils.BY_NAME, matchAlgorithm);
			} else if (target.compareTo("Code") == 0 || target.compareTo("codes") == 0) {
                wrapper = new CodeSearchUtils(lbSvc).searchByCode(schemes, versions, matchText,
                    source, matchAlgorithm, ranking, maxToReturn);
				if (wrapper != null) {
					iterator = wrapper.getIterator();
				}
				return iterator;
			} else if (target.compareTo("Property") == 0 || target.compareTo("properties") == 0) {
				wrapper = new SearchUtils(lbSvc).searchByProperties(schemes, versions,
						matchText, source, matchAlgorithm, excludeDesignation,
						ranking, maxToReturn);
				if (wrapper != null) {
					iterator = wrapper.getIterator();
				}
			} else if (target.compareTo("Property") == 0 || target.compareTo("properties") == 0) {

				wrapper =
					new SearchUtils(lbSvc).searchByAssociations(schemes, versions,
						matchText, source, matchAlgorithm, designationOnly,
						ranking, maxToReturn);
				if (wrapper != null) {
					iterator = wrapper.getIterator();
					return iterator;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Advanced search
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ResolvedConceptReference validateAdvancedSearch(
		                                     String scheme,
		                                     String version,
                                             String matchText,
                                             String target,
											 String matchAlgorithm,
											 int maxToReturn,
											 String source,
											 String property_name,
											 String rel_search_association,
											 String rel_search_rela,
											 String direction
                                             ) {

	    ResolvedConceptReferencesIterator iterator = advancedSearch(
		                                     scheme,
		                                     version,
                                             matchText,
                                             target,
											 matchAlgorithm,
											 maxToReturn,
											 source,
											 property_name,
											 rel_search_association,
											 rel_search_rela,
											 direction);

		if (iterator == null) return null;
		ResolvedConceptReference rcref = null;
		try {
			while(iterator.hasNext()) {
				rcref = iterator.next();
				return rcref;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

    public ResolvedConceptReferencesIterator advancedSearch(
		                                     String scheme,
		                                     String version,
                                             String matchText,
                                             String target,
											 String matchAlgorithm,
											 int maxToReturn,
											 String source,
											 String property_name,
											 String rel_search_association,
											 String rel_search_rela,
											 String direction
                                             ) {
	    ResolvedConceptReferencesIterator iterator = null;
	    ResolvedConceptReferencesIteratorWrapper wrapper = null;
		//String source = "ALL";
		boolean designationOnly = false;
		boolean ranking = true;
		boolean excludeDesignation = true;
		String property_type = null;

	    try {
			if (target.compareTo("Name") == 0 || target.compareTo("names") == 0) {
				return new SimpleSearchUtils(lbSvc).search(scheme, version, matchText, SimpleSearchUtils.BY_NAME, matchAlgorithm);

			} else if (target.compareTo("Code") == 0 || target.compareTo("codes") == 0) {
				Vector schemes = new Vector();
				schemes.add(scheme);
				Vector versions = new Vector();
				versions.add(version);
                wrapper = new CodeSearchUtils(lbSvc).searchByCode(schemes, versions, matchText,
                    source, matchAlgorithm, ranking, maxToReturn);
				if (wrapper != null) {
					iterator = wrapper.getIterator();
				}
				return iterator;
			} else if (target.compareTo("Property") == 0 || target.compareTo("properties") == 0) {

                String[] property_types = null;
                if (property_type != null) {
                    property_types = new String[] { property_type };
				}
                String[] property_names = null;
                if (property_name != null) {
                    property_names = new String[] { property_name };
				}
                excludeDesignation = false;
                wrapper =
                    new SearchUtils(lbSvc).searchByProperties(scheme, version,
                        matchText, property_types, property_names, source,
                        matchAlgorithm, excludeDesignation, ranking,
                        maxToReturn);
                if (wrapper != null) {
                    iterator = wrapper.getIterator();
                }
 			} else if (target.compareTo("Relationship") == 0 || target.compareTo("relationships") == 0) {
				if (rel_search_association != null
					&& rel_search_association.compareTo("ALL") == 0) {
					rel_search_association = null;
				}

				if (rel_search_rela != null) {
					rel_search_rela = rel_search_rela.trim();
					if (rel_search_rela.length() == 0)
						rel_search_rela = null;
				}
				int search_direction = Constants.SEARCH_SOURCE;
				if (direction != null && direction.compareToIgnoreCase("source") == 0) {
					search_direction = Constants.SEARCH_TARGET;
				}

                String[] associationsToNavigate = null;
                String[] association_qualifier_names = null;
                String[] association_qualifier_values = null;

				if (rel_search_association != null) {
					String assocName = rel_search_association;
					associationsToNavigate =
						new String[] { assocName };
				}
				if (rel_search_rela != null) {
					association_qualifier_names = new String[] { "rela" };
					association_qualifier_values =
						new String[] { rel_search_rela };

					if (associationsToNavigate == null) {
						Vector w = new CodingSchemeDataUtils(lbSvc).getSupportedAssociationNames(scheme, null);
						if (w == null || w.size() == 0) {
						} else {
							associationsToNavigate = new String[w.size()];
							for (int i = 0; i < w.size(); i++) {
								String nm = (String) w.elementAt(i);
								associationsToNavigate[i] = nm;
							}
						}
					}
				}
				wrapper =
				new SearchUtils(lbSvc).searchByAssociations(scheme, version,
					matchText, associationsToNavigate,
					association_qualifier_names,
					association_qualifier_values, search_direction, source,
					matchAlgorithm, excludeDesignation, ranking,
					maxToReturn);
                if (wrapper != null) {
                    iterator = wrapper.getIterator();
                    return iterator;
                }
 			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
