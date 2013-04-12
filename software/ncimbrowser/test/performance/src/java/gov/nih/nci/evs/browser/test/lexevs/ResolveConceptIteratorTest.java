/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.test.lexevs;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.apache.log4j.Logger;

import gov.nih.nci.evs.browser.utils.*;

public class ResolveConceptIteratorTest {
    private static Logger _logger = Logger.getLogger(ResolveConceptIteratorTest.class);
    private final String SEPARATOR = 
        "---------------------------------------------------------------------";
    private String _codingSchemeName = "NCI MetaThesaurus"; // "NCI Thesaurus"

    private String getConceptInfo(ResolvedConceptReference ref) {
        return ref.getConceptCode() + ": "
            + ref.getEntityDescription().getContent();
    }
    
    private void printConcepts(ResolvedConceptReference[] refs, int offset) {
        for (int i=0; i < refs.length; ++i)
            _logger.debug((i+offset) + ") " + getConceptInfo(refs[i]));
    }
    
    private ResolvedConceptReferencesIterator search(String matchText, 
        String algorithm, boolean ranking) throws Exception {
        LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        _logger.debug(SEPARATOR);
        _logger.debug("* matchText: " + matchText);
        _logger.debug("* algorithm: " + algorithm);
        _logger.debug("* ranking: " + ranking);

        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        CodedNodeSet cns = lbs.getNodeSet(_codingSchemeName, versionOrTag,
            null);
        cns = cns.restrictToMatchingDesignations(matchText,
            SearchDesignationOption.ALL, algorithm, null);

        LocalNameList restrictToProperties = new LocalNameList();
        boolean resolveConcepts = true;
        SortOptionList sortCriteria = null;

        if (ranking) {
            _logger.debug("* Sort by Lucene score...");
            sortCriteria = Constructors
                .createSortOptionList(new String[] { "matchToQuery" });
        } else {
            _logger.debug("* Sort alphabetically...");
            sortCriteria = Constructors
                .createSortOptionList(new String[] { "entityDescription" }); // code
            resolveConcepts = false;
        }

        _logger.debug(SEPARATOR);
        ResolvedConceptReferencesIterator iterator = cns.resolve(sortCriteria,
            null, restrictToProperties, null, resolveConcepts);
        _logger.debug("* Search found: " + iterator.numberRemaining());
        return iterator;
    }

    private void getConcepts(ResolvedConceptReferencesIterator iterator, 
        int maxReturn) throws Exception {

        int n = iterator.numberRemaining();
        int loops = n / maxReturn;
        if (n % maxReturn != 0)
            ++loops;
        
        _logger.debug("* n: " + n);
        _logger.debug("* maxReturn: " + maxReturn);
        _logger.debug("* loops: " + loops);
        
        ResolvedConceptReference[] refs = null;
        int i=0;
        for (; i<loops; ++i) {
            refs = iterator.next(maxReturn).getResolvedConceptReference();
            _logger.debug((i * maxReturn) + ") " + getConceptInfo(refs[0]));
        }
        
        boolean printLastPage = true;
        if (printLastPage && refs != null && refs.length > 0) {
            _logger.debug(SEPARATOR);
            _logger.debug("* Print last page:");
            printConcepts(refs, (i-1) * maxReturn);
        }
    }

    private void test1() throws Exception {
        String matchText = "protein";
        //matchText = "blood";
        String algorithm = "subString";
        boolean ranking = false;
        int maxReturn = 100;

        ResolvedConceptReferencesIterator iterator =
            search(matchText, algorithm, ranking);
        getConcepts(iterator, maxReturn);
    }

    public static void main(String[] args) throws Exception {
        ResolveConceptIteratorTest test = new ResolveConceptIteratorTest();
        test.test1();
    }
}
