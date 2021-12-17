package gov.nih.nci.evs.browser.utils;
/*
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
import java.util.Arrays;
*/
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.lexevs.metabrowser.MetaBrowserService;
import org.LexGrid.lexevs.metabrowser.MetaBrowserService.Direction;
import org.LexGrid.lexevs.metabrowser.model.RelationshipTabResults;
import org.LexGrid.naming.SupportedHierarchy;

import org.apache.logging.log4j.*;
import gov.nih.nci.evs.browser.common.Constants;
import java.util.Map.Entry;
import java.util.*;

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
 * Search utility for the cart
 *
 * @author garciawa2
 */
public class SearchCart {

    // Local variables
	private static Logger _logger = LogManager.getLogger(SearchCart.class);
    //private static LexBIGService lbSvc = null;
    //private static LexBIGServiceConvenienceMethods lbscm = null;
    private static MetaBrowserService mbs = null;
    private static String SEMANTIC_TYPE = "Semantic_Type";

    // Local constants
    private static final String LB_EXTENSION = "LexBIGServiceConvenienceMethods";
    private static final List<String> _hierAssocToParentNodes =
        Arrays.asList("PAR", "isa", "branch_of", "part_of", "tributary_of");
    private static final List<String> _hierAssocToChildNodes =
        Arrays.asList("CHD", "inverse_isa");

    /**
     * Constructor
     * @throws LBException
     */
	public SearchCart() throws LBException {
/*
		// Setup lexevs service
		if (lbSvc == null) {
			lbSvc = RemoteServerUtil.createLexBIGService();
		}

		// Setup lexevs generic extension

		lbscm = (LexBIGServiceConvenienceMethods) lbSvc
				.getGenericExtension(LB_EXTENSION);
		lbscm.setLexBIGService(lbSvc);


		// Setup Metabrowser extension
		if (mbs == null) {
			mbs = (MetaBrowserService) lbSvc
					.getGenericExtension("metabrowser-extension");
			if (mbs == null) {
				_logger.error("Error! metabrowser-extension is null!");
			}
		}
*/
	}

    /**
     * Get concept Entity by code
     * @param codingScheme
     * @param code
     * @return
     */
    public ResolvedConceptReference getConceptByCode(String codingScheme, String code) {
        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;

        try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            cns = lbSvc.getCodingSchemeConcepts(codingScheme, null);
            ConceptReferenceList crefs =
                createConceptReferenceList(new String[] { code }, codingScheme);
            cns.restrictToCodes(crefs);
            iterator = cns.resolve(null, null, null);
            if (iterator.numberRemaining() > 0) {
                ResolvedConceptReference ref = (ResolvedConceptReference) iterator.next();
                return ref;
            }
        } catch (LBException e) {
            _logger.info("Error: " + e.getMessage());
        }

        return null;
    }

    /**
     * Return list of Presentations
     * @param ref
     * @return
     */
    public Property[] getPresentationValues(ResolvedConceptReference ref) {
        return returnProperties(ref.getReferencedEntry().getPresentation());
    }

    /**
     * Return list of Definitions
     * @param ref
     * @return
     */
    public Property[] getDefinitionValues(ResolvedConceptReference ref) {
        return returnProperties(ref.getReferencedEntry().getDefinition());
    }

    /**
     * Return list of Properties
     * @param ref
     * @return
     */
    public Property[] getPropertyValues(ResolvedConceptReference ref) {
        return returnProperties(ref.getReferencedEntry().getProperty());
    }

    /**
     * Returns list of Parent Concepts
     * @param ref
     * @return
     * @throws LBException
     */
    public Vector<String> getParentConcepts(ResolvedConceptReference ref) throws Exception {
        String code = ref.getCode();
        Vector<String> superconcepts = getAssociatedConcepts(code, true);
        return superconcepts;
    }

    /**
     * Returns list of Child Concepts
     * @param ref
     * @return
     */
    public Vector<String> getChildConcepts(ResolvedConceptReference ref) throws Exception {
        String code = ref.getCode();
        Vector<String> supconcepts = getAssociatedConcepts(code, false);
        return supconcepts;
    }

    /**
     * Returns Associated Concepts
     *
     * @param scheme
     * @param version
     * @param code
     * @param assocName
     * @param forward
     * @return
     */
    public Vector<String> getAssociatedConcepts(String cui, boolean parents) {

            Vector<String> v = new Vector<String>();

            try {
				LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
				MetaBrowserService mbs = (MetaBrowserService) lbSvc
						.getGenericExtension("metabrowser-extension");
				if (mbs == null) {
					_logger.error("Error! metabrowser-extension is null!");
				}

            	Map<String, List<RelationshipTabResults>> map = null;
            	map = mbs.getRelationshipsDisplay(cui, null, Direction.SOURCEOF);

				Iterator it = map.entrySet().iterator();
				while (it.hasNext()) {
					Entry thisEntry = (Entry) it.next();
					String rel = (String) thisEntry.getKey();
					List<RelationshipTabResults> relations = (List<RelationshipTabResults>) thisEntry.getValue();

            	//for (String rel : map.keySet()) {
            		//List<RelationshipTabResults> relations = map.get(rel);

            		// Add parents
            		if (parents && _hierAssocToChildNodes.contains(rel)) {
	                    for (RelationshipTabResults result : relations) {
	                        String name = result.getName();
	                    	if (!v.contains(name)) v.add(name);
	                    }

            		}

            		// Add children
            		if (!parents && _hierAssocToParentNodes.contains(rel)) {
	                    for (RelationshipTabResults result : relations) {
	                    	String name = result.getName();
	                    	if (!v.contains(name)) v.add(name);
	                    }
            		}

            	}

            } catch (Exception ex) {
                _logger.warn(ex.getMessage());
            }
            return v;
        }

    /**
     * Resolve the Iterator
     *
     * @param iterator
     * @param maxToReturn
     * @param code
     * @return
     */
    public Vector<Entity> resolveIterator(
            ResolvedConceptReferencesIterator iterator, int maxToReturn,
            String code) {

        Vector<Entity> v = new Vector<Entity>();

        if (iterator == null) {
            _logger.warn("No match.");
            return v;
        }
        try {
            int iteration = 0;
            while (iterator.hasNext()) {
                iteration++;
                iterator = iterator.scroll(maxToReturn);
                ResolvedConceptReferenceList rcrl = iterator.getNext();
                ResolvedConceptReference[] rcra = rcrl
                        .getResolvedConceptReference();
                for (int i = 0; i < rcra.length; i++) {
                    ResolvedConceptReference rcr = rcra[i];
                    Entity ce = rcr.getReferencedEntry();
                    if (code == null) {
                        v.add(ce);
                    } else {
                        if (ce.getEntityCode().compareTo(code) != 0)
                            v.add(ce);
                    }
                }
            }
        } catch (Exception e) {
            _logger.warn(e.getMessage());
        }
        return v;
    }

    /**
     * Return Iterator for codedNodeGraph
     *
     * @param cng
     * @param graphFocus
     * @param resolveForward
     * @param resolveBackward
     * @param resolveAssociationDepth
     * @param maxToReturn
     * @return
     */
    public ResolvedConceptReferencesIterator codedNodeGraph2CodedNodeSetIterator(
            CodedNodeGraph cng, ConceptReference graphFocus,
            boolean resolveForward, boolean resolveBackward,
            int resolveAssociationDepth, int maxToReturn) {
        CodedNodeSet cns = null;

        try {
            cns = cng.toNodeList(graphFocus, resolveForward, resolveBackward,
                    resolveAssociationDepth, maxToReturn);
            if (cns == null) return null;
            return cns.resolve(null, null, null);
        } catch (Exception ex) {
            _logger.warn(ex.getMessage());
        }

        return null;
    }

    /**
     * Return a list of Association names
     *
     * @param scheme
     * @param version
     * @return
     */
    public Vector<String> getAssociationNames(String scheme, String version) {
        Vector<String> association_vec = new Vector<String>();
        try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
            if (version != null) {
				versionOrTag.setVersion(version);
			}
            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, versionOrTag);

            SupportedHierarchy[] hierarchies = cs.getMappings().getSupportedHierarchy();
            String[] ids = hierarchies[0].getAssociationNames();
            for (int i = 0; i < ids.length; i++) {
                if (!association_vec.contains(ids[i])) {
                    association_vec.add(ids[i]);
                    _logger.debug("AssociationName: " + ids[i]);
                }
            }
        } catch (Exception ex) {
            _logger.warn(ex.getMessage());
        }
        return association_vec;
    }

    /**
     * Return list of Comments
     * @param ref
     * @return
     */
    public Property[] getCommentValues(ResolvedConceptReference ref) {
        return returnProperties(ref.getReferencedEntry().getComment());
    }

	/**
	 * Return Semantic Type of the concept code passed in
	 *
	 * @param conceptCode
	 * @return
	 */
	public String getSemanticType(String conceptCode) {
		Vector<String> code_vec = new Vector<String>();
		code_vec.add(conceptCode);
		HashMap<?, ?> map = DataUtils.getPropertyValuesForCodes(
				Constants.CODING_SCHEME_NAME, null, code_vec, SEMANTIC_TYPE);
		return (String) map.get(conceptCode);
	}

    // -----------------------------------------------------
    // Internal utility methods
    // -----------------------------------------------------

    /**
     * @param properties
     * @return
     */
    private Property[] returnProperties(Property[] properties) {
        if (properties == null)
            return new Property[0]; // return empty list
        return properties;
    }

    /**
     * @param codes
     * @param codingSchemeName
     * @return
     */
    private ConceptReferenceList createConceptReferenceList(String[] codes,
            String codingSchemeName) {
        if (codes == null)
            return null;
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++) {
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(codes[i]);
            list.addConceptReference(cr);
        }
        return list;
    }

} // End of SearchCart
