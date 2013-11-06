package gov.nih.nci.evs.browser.utils;

import java.util.*;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.codingSchemes.*;
import org.apache.log4j.*;

import org.LexGrid.LexBIG.DataModel.Core.types.*;
import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;

import org.apache.commons.codec.language.*;


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
 * @author EVS Team
 * @version 1.0
 *
 *          Modification history Initial implementation kim.ong@ngc.com
 *
 */
 public class CodeSearchUtils {
    private static Logger _logger = Logger.getLogger(CodeSearchUtils.class);
    private static final boolean CASE_SENSITIVE = true;

    public CodeSearchUtils() {

    }

    public static CodedNodeSet getNodeSet(LexBIGService lbSvc, String scheme, CodingSchemeVersionOrTag versionOrTag)
        throws Exception {
		CodedNodeSet cns = null;
		try {
			cns = lbSvc.getCodingSchemeConcepts(scheme, versionOrTag);
			CodedNodeSet.AnonymousOption restrictToAnonymous = CodedNodeSet.AnonymousOption.NON_ANONYMOUS_ONLY;
			cns = cns.restrictToAnonymous(restrictToAnonymous);
	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return cns;
	}

    public String findBestContainsAlgorithm(String matchText) {
        if (matchText == null)
            return "nonLeadingWildcardLiteralSubString";
        matchText = matchText.trim();
        if (matchText.length() == 0)
            return "nonLeadingWildcardLiteralSubString"; // or null
        if (matchText.length() > 1)
            return "nonLeadingWildcardLiteralSubString";
        char ch = matchText.charAt(0);
        if (Character.isDigit(ch))
            return "literal";
        else if (Character.isLetter(ch))
            return "LuceneQuery";
        else
            return "literalContains";
    }

    public static CodedNodeSet restrictToSource(CodedNodeSet cns, String source) {
        if (cns == null)
            return cns;
        if (source == null || source.compareTo("*") == 0
            || source.compareTo("") == 0 || source.compareTo("ALL") == 0)
            return cns;

        LocalNameList contextList = null;
        LocalNameList sourceLnL = null;
        NameAndValueList qualifierList = null;

        Vector<String> w2 = new Vector<String>();
        w2.add(source);
        sourceLnL = vector2LocalNameList(w2);
        LocalNameList propertyLnL = null;
        CodedNodeSet.PropertyType[] types =
            new PropertyType[] { PropertyType.PRESENTATION };
        try {
            cns =
                cns.restrictToProperties(propertyLnL, types, sourceLnL,
                    contextList, qualifierList);
        } catch (Exception ex) {
            _logger.error("restrictToSource throws exceptions.");
            return null;
        }
        return cns;
    }

    private static CodedNodeSet restrictToActiveStatus(CodedNodeSet cns,
        boolean activeOnly) {
        if (cns == null) return null;
        if (!activeOnly) return cns; // no restriction, do nothing
        try {
            cns =
                cns.restrictToStatus(CodedNodeSet.ActiveOption.ACTIVE_ONLY,
                    null);
            return cns;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LocalNameList vector2LocalNameList(Vector<String> v) {
        if (v == null)
            return null;
        LocalNameList list = new LocalNameList();
        for (int i = 0; i < v.size(); i++) {
            String vEntry = (String) v.elementAt(i);
            list.addEntry(vEntry);
        }
        return list;
    }


    public CodedNodeSet getCodedNodeSetContainingSourceCode(
        String scheme, String version, String sourceAbbr, String code,
        int maxToReturn, boolean searchInactive) {

        if (sourceAbbr == null || code == null) {
            return null;
		}

        LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);

        if (lbSvc == null) {
            _logger.warn("lbSvc = null");
            return null;
        }

        LocalNameList contextList = null;
        NameAndValueList qualifierList = null;

        Vector<String> v = null;
		if (code.compareTo("") != 0) {
            qualifierList = new NameAndValueList();
            NameAndValue nv = new NameAndValue();
            nv.setName("source-code");
            nv.setContent(code);
            qualifierList.addNameAndValue(nv);
        }


        LocalNameList propertyLnL = null;
        // sourceLnL
        Vector<String> w2 = new Vector<String>();
        w2.add(sourceAbbr);
        LocalNameList sourceLnL = vector2LocalNameList(w2);
        if (sourceAbbr.compareTo("*") == 0
            || sourceAbbr.compareToIgnoreCase("ALL") == 0) {
            sourceLnL = null;
        }

        SortOptionList sortCriteria = null;// Constructors.createSortOptionList(new
                                           // String[]{"matchToQuery", "code"});
        CodedNodeSet cns = null;
        try {
            cns = getNodeSet(lbSvc, scheme, versionOrTag);
            if (cns == null) {
                _logger.warn("lbSvc.getCodingSchemeConceptsd returns null");
                return null;
            }
            CodedNodeSet.PropertyType[] types =
                new PropertyType[] { PropertyType.PRESENTATION };


            cns =
                cns.restrictToProperties(propertyLnL, types, sourceLnL,
                    contextList, qualifierList);

            if (cns != null) {
                boolean activeOnly = !searchInactive;
                cns = restrictToActiveStatus(cns, activeOnly);
                if (cns == null) {
					return null;
				}
            }
        } catch (Exception e) {
            // getLogger().error("ERROR: Exception in findConceptWithSourceCodeMatching.");
            e.printStackTrace();
            return null;
        }
        return cns;
    }




    public ResolvedConceptReferencesIterator findConceptWithSourceCodeMatching(
        String scheme, String version, String sourceAbbr, String code,
        int maxToReturn, boolean searchInactive) {
        if (sourceAbbr == null || code == null)
            return null;
        ResolvedConceptReferencesIterator matchIterator = null;

        LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);

        if (lbSvc == null) {
            _logger.warn("lbSvc = null");
            return null;
        }

        LocalNameList contextList = null;
        NameAndValueList qualifierList = null;

        Vector<String> v = null;

        //if (code != null && code.compareTo("") != 0) {
		if (code.compareTo("") != 0) {
            qualifierList = new NameAndValueList();
            NameAndValue nv = new NameAndValue();
            nv.setName("source-code");
            nv.setContent(code);
            qualifierList.addNameAndValue(nv);
        }

        LocalNameList propertyLnL = null;
        // sourceLnL
        Vector<String> w2 = new Vector<String>();
        w2.add(sourceAbbr);
        LocalNameList sourceLnL = vector2LocalNameList(w2);
        if (sourceAbbr.compareTo("*") == 0
            || sourceAbbr.compareToIgnoreCase("ALL") == 0) {
            sourceLnL = null;
        }

        SortOptionList sortCriteria = null;// Constructors.createSortOptionList(new
                                           // String[]{"matchToQuery", "code"});
        try {
            CodedNodeSet cns = lbSvc.getCodingSchemeConcepts(scheme, null);
            if (cns == null) {
                _logger.warn("lbSvc.getCodingSchemeConceptsd returns null");
                return null;
            }
            CodedNodeSet.PropertyType[] types =
                new PropertyType[] { PropertyType.PRESENTATION };
            cns =
                cns.restrictToProperties(propertyLnL, types, sourceLnL,
                    contextList, qualifierList);

            if (cns != null) {
                boolean activeOnly = !searchInactive;
                cns = restrictToActiveStatus(cns, activeOnly);

                try {
                    matchIterator = cns.resolve(sortCriteria, null, null);// ConvenienceMethods.createLocalNameList(getPropertyForCodingScheme(cs)),null);
                } catch (Exception ex) {

                }
            }

        } catch (Exception e) {
            // getLogger().error("ERROR: Exception in findConceptWithSourceCodeMatching.");
            return null;
        }
        return matchIterator;
    }

     public ResolvedConceptReferencesIteratorWrapper searchByCode(String scheme,
         String version, String matchText, String source, String matchAlgorithm,
         boolean ranking, int maxToReturn) {
/*
		 if (searchAllSources(source)) {
			 return SimpleSearchUtils.search(scheme, version, matchText, SimpleSearchUtils.BY_CODE, null);
		 }
*/
         ResolvedConceptReferencesIterator iterator = null;
         iterator = matchConceptCode(scheme, version, matchText, source, "LuceneQuery");
         try {
             int size = iterator.numberRemaining();
             if (size == 0) {
                 iterator =
                     findConceptWithSourceCodeMatching(scheme, version, source,
                         matchText, maxToReturn, true);
             }

             return new ResolvedConceptReferencesIteratorWrapper(iterator);
         } catch (Exception ex) {
             ex.printStackTrace();
         }
         return null;
    }


    public static CodedNodeSet getCodedNodeSetContainingCode(String codingSchemeName, String vers, String code,
                                                String source, boolean searchInactive) {
        try {
			if (code == null) {
				return null;
			}
			if (code.indexOf("@") != -1) return null; // anonymous class

            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
            if (lbSvc == null) {
                return null;
            }
            CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
            if (vers != null) versionOrTag.setVersion(vers);

            ConceptReferenceList crefs = createConceptReferenceList(
                    new String[] { code }, codingSchemeName);

            CodedNodeSet cns = null;
            try {
				cns = getNodeSet(lbSvc, codingSchemeName, versionOrTag);
				cns = cns.restrictToCodes(crefs);

				if (cns != null) {
					boolean activeOnly = !searchInactive;
					cns = restrictToActiveStatus(cns, activeOnly);
				}
				cns = restrictToSource(cns, source);
				return cns;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean searchAllSources(String source) {
		if (source != null && source.compareTo("ALL") != 0) return false;
		return true;
	}



    public CodedNodeSet findCodedNodeSetContainingCode(String scheme,
        String version, String matchText, String source, boolean searchInactive) {
        LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        if (lbs == null) return null;
        ResolvedConceptReferencesIterator iterator = null;
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        if (version != null)
            versionOrTag.setVersion(version);
        CodedNodeSet cns = null;
        String matchAlgorithm = "LuceneQuery";
        try {
            //cns = lbs.getNodeSet(scheme, versionOrTag, null);
            cns = getNodeSet(lbs, scheme, versionOrTag);
            if (cns == null) return null;
            if (source != null && source.compareTo("ALL") != 0) {
                cns = restrictToSource(cns, source);
            }
            CodedNodeSet.PropertyType[] propertyTypes = null;
            LocalNameList sourceList = null;
            LocalNameList contextList = null;
            NameAndValueList qualifierList = null;
            cns =
                cns.restrictToMatchingProperties(ConvenienceMethods
                    .createLocalNameList(new String[] { "conceptCode" }),
                    propertyTypes, sourceList, contextList, qualifierList,
                    matchText, matchAlgorithm, null);
            if (cns != null) {
                boolean activeOnly = !searchInactive;
                cns = restrictToActiveStatus(cns, activeOnly);
            }
        } catch (Exception ex) {
            _logger.error("WARNING: searchByCode throws exception.");
        }
        return cns;
	}


    public ResolvedConceptReferencesIterator matchConceptCode(String scheme,
        String version, String matchText, String source, String matchAlgorithm) {

    	return matchConceptCode(scheme, version, matchText, source, matchAlgorithm, CASE_SENSITIVE);

	}



    public ResolvedConceptReferencesIterator matchConceptCode(String scheme,
        String version, String matchText, String source, String matchAlgorithm, boolean caseSensitive) {
        ResolvedConceptReferencesIterator iterator = null;
        CodedNodeSet cns = null;
        if (caseSensitive) {
			cns = getCodedNodeSetContainingCode(scheme,
				 version, matchText, source, false);
	    } else {
			cns = findCodedNodeSetContainingCode(scheme,
				 version, matchText, source, false);

		}

		LocalNameList restrictToProperties = new LocalNameList();
		SortOptionList sortCriteria = null;
		try {
			boolean resolveConcepts = false;
			try {
				long ms = System.currentTimeMillis(), delay = 0;
				iterator =
					cns.resolve(sortCriteria, null, restrictToProperties,
						null, resolveConcepts);

				int size = iterator.numberRemaining();
				_logger.debug("cns.resolve size: " + size);

			} catch (Exception e) {
				_logger.error("ERROR: cns.resolve throws exceptions.");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
        return iterator;
    }

    public ResolvedConceptReferencesIteratorWrapper searchByCode(
        Vector<String> schemes, Vector<String> versions, String matchText,
        String source, String matchAlgorithm, boolean ranking, int maxToReturn) {
/*
		if (searchAllSources(source)) {
			return SimpleSearchUtils.search(schemes, versions, matchText, SimpleSearchUtils.BY_CODE, null);
		}
*/
		return searchByCode(
			schemes,  versions, matchText,
			source, matchAlgorithm, ranking, maxToReturn, false, CASE_SENSITIVE);
	}

	public CodedNodeSet union(CodedNodeSet cns, CodedNodeSet cns_2) {
		if (cns == null && cns_2 == null) return null;
		else if (cns != null && cns_2 == null) return cns;
		else if (cns == null && cns_2 != null) return cns_2;
		else {
			try {
				if (cns == null) return null;
				cns = cns.union(cns_2);
				return cns;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
	    }
		return null;
	}

    public static ConceptReferenceList createConceptReferenceList(
        String[] codes, String codingSchemeName) {
        if (codes == null) {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++) {
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(codes[i]);
            list.addConceptReference(cr);
        }
        return list;
    }

////////////////////////////////
// main method
////////////////////////////////
    public ResolvedConceptReferencesIteratorWrapper searchByCode(
        Vector<String> schemes, Vector<String> versions, String matchText,
        String source, String matchAlgorithm, boolean ranking, int maxToReturn, boolean activeOnly) {
/*
		if (searchAllSources(source)) {
			return SimpleSearchUtils.search(schemes, versions, matchText, SimpleSearchUtils.BY_CODE, null);
		}
*/
		return searchByCode(schemes, versions, matchText,
            source, matchAlgorithm, ranking, maxToReturn, activeOnly, CASE_SENSITIVE);
    }

    public ResolvedConceptReferencesIteratorWrapper searchByCode(
        Vector<String> schemes, Vector<String> versions, String matchText,
        String source, String matchAlgorithm, boolean ranking, int maxToReturn, boolean activeOnly, boolean caseSensitive) {

		if (matchText == null || matchText.trim().length() == 0) return null;

		LocalNameList contextList = null;
		NameAndValueList qualifierList = null;
		LocalNameList propertyLnL = null;
		SortOptionList sortCriteria = null;
		LocalNameList sourceLnL = null;
		LocalNameList sourceList = null;
		LocalNameList restrictToProperties = new LocalNameList();
		CodedNodeSet.PropertyType[] propertyTypes = null;
		boolean resolveConcepts = false;
		//CodedNodeSet.PropertyType[] types = new PropertyType[] { PropertyType.PRESENTATION };
        Vector<CodedNodeSet> cns_vec = new Vector<CodedNodeSet>();

        try {
            matchText = matchText.trim();
            String code = matchText;

            for (int i = 0; i < schemes.size(); i++) {
                String scheme = (String) schemes.elementAt(i);
                String version = (String) versions.elementAt(i);
				CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
				if (version != null)
					versionOrTag.setVersion(version);

                boolean searchInactive = !activeOnly;
                // Match concept code:
                CodedNodeSet cns = null;

                if (caseSensitive) {
					cns = getCodedNodeSetContainingCode(scheme,
						version, matchText, source, searchInactive);
				} else {
					cns = findCodedNodeSetContainingCode(scheme,
						version, matchText, source, searchInactive);
				}

				CodedNodeSet cns_2 = getCodedNodeSetContainingSourceCode(
					scheme, version, source, code, maxToReturn, searchInactive);
                cns = union(cns, cns_2);
				if (cns != null) {
					cns_vec.add(cns);
				}
			}

			ResolvedConceptReferencesIterator iterator =
				new QuickUnionIterator(cns_vec, sortCriteria, null,
					restrictToProperties, null, resolveConcepts);
			if (iterator != null) {
				return new ResolvedConceptReferencesIteratorWrapper(iterator);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}