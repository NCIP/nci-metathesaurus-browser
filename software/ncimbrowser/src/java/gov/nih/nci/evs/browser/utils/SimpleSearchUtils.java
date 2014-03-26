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
 * SimpleSearchUtils (uses LexEVSAPI 6.1 SearchExtension)
 *
 * @author kimong
 *
 */
package gov.nih.nci.evs.browser.utils;

import java.util.*;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;


import org.apache.log4j.*;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension;
//import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;

//import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

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
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension;


public class SimpleSearchUtils {
	private static Logger _logger = Logger.getLogger(SimpleSearchUtils.class);

    final static String testID = "SearchExtensionImplTest";

    public static final int BY_CODE = 1;
    public static final int BY_NAME = 2;

    public static final String EXACT_MATCH = "exactMatch";
    public static final String STARTS_WITH = "startsWith";
    public static final String CONTAINS = "contains";
    public static final String LUCENE = "lucene";

    public static final String NAMES = "names";
    public static final String CODES = "codes";
    public static final String PROPERTIES = "properties";
    public static final String RELATIONSHIPS = "relationships";

    private static String charactersToRemove = ",./\\`\"+*=@#$%^&?!";
    private static String charactersTreatedAsWhitespace = "-;(){}[]<>|";

    public SimpleSearchUtils() {

	}

    public ResolvedConceptReferencesIteratorWrapper search(
        Vector<String> schemes, Vector<String> versions, String matchText, String algorithm, String target) throws LBException {
        if (algorithm == null|| target == null) return null;

        if (algorithm.compareToIgnoreCase(EXACT_MATCH) == 0 && target.compareToIgnoreCase(CODES) == 0) {
			return search(schemes, versions, matchText, BY_CODE, "exactMatch");
        } else if (algorithm.compareToIgnoreCase(LUCENE) == 0 && target.compareToIgnoreCase(CODES) == 0) {
			return search(schemes, versions, matchText, BY_CODE, "exactMatch");
        } else if (algorithm.compareToIgnoreCase(LUCENE) == 0 && target.compareToIgnoreCase(NAMES) == 0) {
			return search(schemes, versions, matchText, BY_NAME, "lucene");
        } else if (algorithm.compareToIgnoreCase(CONTAINS) == 0 && target.compareToIgnoreCase(NAMES) == 0) {
			return search(schemes, versions, matchText, BY_NAME, "contains");
		}
		return null;
	}

    public String preprocessingString(String input) {
		if (input == null) return null;
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<input.length(); i++) {
			//char c = input.charAt(i);
			String ch = input.substring(i, i+1);
			if (charactersTreatedAsWhitespace.indexOf(ch) > 0) {
				buf.append(" ");
			} else if (charactersToRemove.indexOf(ch) == -1) {
				buf.append(ch);
			}
		}
		return buf.toString();

	}

	public static boolean isSimpleSearchSupported(String algorithm, String target) {

		SearchExtension extension = getSearchExtension();
        if (extension == null) {
			System.out.println("search extension is not available.");
			return false;
		}

		if (algorithm == null|| target == null) return false;

        if (algorithm.compareToIgnoreCase(EXACT_MATCH) == 0 && target.compareToIgnoreCase(CODES) == 0) {
			return true;
        } else if (algorithm.compareToIgnoreCase(EXACT_MATCH) == 0 && target.compareToIgnoreCase(NAMES) == 0) {
			return true;
        } else if (algorithm.compareToIgnoreCase(LUCENE) == 0 && target.compareToIgnoreCase(CODES) == 0) {
			return true;
        } else if (algorithm.compareToIgnoreCase(LUCENE) == 0 && target.compareToIgnoreCase(NAMES) == 0) {
			return true;
        } else if (algorithm.compareToIgnoreCase(CONTAINS) == 0 && target.compareToIgnoreCase(NAMES) == 0) {
			return true;
		}
		return false;
	}


    public static boolean isSearchExtensionAvaliable() {
		if (getSearchExtension() == null) return false;
		return true;
	}


    public static SearchExtension getSearchExtension() {
		SearchExtension searchExtension = null;

		try {
			LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
			if (lbSvc == null) {
				_logger.warn("createLexBIGService returns NULL???");
				return null;
			}
			searchExtension = (SearchExtension) lbSvc.getGenericExtension("SearchExtension");
			return searchExtension;
		} catch (Exception e){
			_logger.warn("SearchExtension is not available.");
			return null;
		}
	}



    public static boolean searchAllSources(String source) {
		if (source != null && source.compareTo("ALL") != 0) return false;
		return true;
	}


    public ResolvedConceptReferencesIteratorWrapper search(
        String scheme, String version, String matchText, int searchOption, String algorithm) throws LBException {
		if (scheme == null) return null;
		Vector<String> schemes = new Vector();
		Vector<String> versions = new Vector();
		schemes.add(scheme);
		versions.add(version);
		return search(schemes, versions, matchText, searchOption, algorithm);
    }

    public ResolvedConceptReferencesIteratorWrapper search(
        Vector<String> schemes, Vector<String> versions, String matchText, int searchOption, String algorithm) throws LBException {
//wrapper = new SimpleSearchUtils().search(schemes, versions, matchText, SimpleSearchUtils.BY_NAME, matchAlgorithm);

	    if (schemes == null|| versions == null) return null;
	    if (schemes.size() != versions.size()) return null;
	    if (schemes.size() == 0) return null;
	    if (matchText == null) return null;
	    if (searchOption != BY_CODE && searchOption != BY_NAME) return null;
	    if (searchOption != BY_CODE && algorithm == null) return null;

		LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

		if (lbSvc == null) {
			return null;
		}

		SearchExtension searchExtension = null;
		try {
			searchExtension = (SearchExtension) lbSvc.getGenericExtension("SearchExtension");
		} catch (Exception e){
			_logger.warn("SearchExtension is not available.");
			System.out.println("SearchExtension is not available.");
			return null;
		}

        Set<CodingSchemeReference> includes = new HashSet();

        for (int i=0; i<schemes.size(); i++) {
			String scheme = (String) schemes.elementAt(i);
			String version = (String) versions.elementAt(i);
			CodingSchemeReference ref = new CodingSchemeReference();
			ref.setCodingScheme(scheme);

			if (version != null) {
				CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
				versionOrTag.setVersion(version);
		    }
			includes.add(ref);
		}
		/*
		if (searchOption == BY_CODE) {
			matchText = "code:" + matchText;
		}
		*/

		ResolvedConceptReferencesIterator iterator = null;
		try {
			iterator = searchExtension.search(matchText, includes, converToMatchAlgorithm(searchOption, algorithm));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (iterator != null) {
			return new ResolvedConceptReferencesIteratorWrapper(iterator);
		}
		return null;
	}


    protected static void displayRef(ResolvedConceptReference ref) {
        System.out.println(ref.getConceptCode() + ":" + ref.getEntityDescription().getContent());
    }

    public static void dumpIterator(ResolvedConceptReferencesIterator itr) {
        try {
            while (itr.hasNext()) {
                try {
                    ResolvedConceptReference[] refs =
                        itr.next(100).getResolvedConceptReference();
                    for (ResolvedConceptReference ref : refs) {
                        displayRef(ref);
                    }
                } catch (Exception ex) {
                    break;
                }
            }
        } catch (Exception ex) {
			ex.printStackTrace();
        }
    }


	public static SearchExtension.MatchAlgorithm converToMatchAlgorithm(int searchOption, String algorithm) {
		if (algorithm == null) return null;
	    if (searchOption != BY_CODE && searchOption != BY_NAME) return null;
	    if (searchOption == BY_NAME) {
			if (algorithm.compareTo("exactMatch") == 0) {
				return SearchExtension.MatchAlgorithm.PRESENTATION_EXACT;
			} else if (algorithm.compareTo("contains") == 0) {
				return SearchExtension.MatchAlgorithm.PRESENTATION_CONTAINS;
			} else if (algorithm.compareTo("lucene") == 0) {
				return SearchExtension.MatchAlgorithm.LUCENE;
			}
		} else if (algorithm.compareTo("exactMatch") == 0 && searchOption == BY_CODE) {
			return SearchExtension.MatchAlgorithm.CODE_EXACT;
		}
		return null;
	}

	public static void main(String [ ] args) {
		boolean searchExtensionAvaliable = isSearchExtensionAvaliable();
		if (!searchExtensionAvaliable) {
			System.out.println("SearchExtension is not available.");
			System.exit(1);
		}

		SimpleSearchUtils test = new SimpleSearchUtils();
        try {
			Vector<String> schemes = new Vector();
			Vector<String> versions = new Vector();
			schemes.add("NCI_Thesaurus");
			//versions.add("12.05d");
			versions.add("13.03d");

			schemes.add("NCI Metathesaurus");
			versions.add("201105");

			String matchText = "cell aging";

			ResolvedConceptReferencesIteratorWrapper wrapper = test.search(schemes, versions, matchText, SimpleSearchUtils.BY_NAME, "contains");
			if (wrapper != null) {
				ResolvedConceptReferencesIterator iterator = wrapper.getIterator();
				if (iterator != null) {
					try {
						int numRemaining = iterator.numberRemaining();
						System.out.println("Number of matches: " + numRemaining);
						dumpIterator(iterator);

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} else {
				System.out.println("wrapper is NULL??? " + matchText);
			}


		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}

