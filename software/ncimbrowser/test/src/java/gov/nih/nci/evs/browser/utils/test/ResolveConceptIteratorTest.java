package gov.nih.nci.evs.browser.utils.test;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;

public class ResolveConceptIteratorTest {
    private String _codingSchemeName = "NCI MetaThesaurus"; // "NCI Thesaurus"

    private String getConceptInfo(ResolvedConceptReference ref) {
        return ref.getConceptCode() + ": "
            + ref.getEntityDescription().getContent();
    }
    
    private void printConcepts(ResolvedConceptReference[] refs, int offset) {
        for (int i=0; i < refs.length; ++i)
            System.out.println((i+offset+1) + ") " + getConceptInfo(refs[i]));
    }
    
    private ResolvedConceptReferencesIterator search(String matchText, 
        String algorithm, boolean ranking) throws Exception {
        LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        CodedNodeSet cns = lbs.getNodeSet(_codingSchemeName, versionOrTag,
            null);
        cns = cns.restrictToMatchingDesignations(matchText,
            SearchDesignationOption.ALL, algorithm, null);

        LocalNameList restrictToProperties = new LocalNameList();
        boolean resolveConcepts = true;
        SortOptionList sortCriteria = null;

        if (ranking) {
            System.out.println("* Sort by Lucene score...");
            sortCriteria = Constructors
                .createSortOptionList(new String[] { "matchToQuery" });
        } else {
            System.out.println("* Sort alphabetically...");
            sortCriteria = Constructors
                .createSortOptionList(new String[] { "entityDescription" }); // code
            resolveConcepts = false;
        }

        ResolvedConceptReferencesIterator iterator = cns.resolve(sortCriteria,
            null, restrictToProperties, null, resolveConcepts);
        System.out.println("* Search found: " + iterator.numberRemaining());
        return iterator;
    }

    private void getConcepts(ResolvedConceptReferencesIterator iterator, 
        int maxReturn) throws Exception {
        ResolvedConceptReference[] refs = iterator.next(maxReturn)
            .getResolvedConceptReference();
        printConcepts(refs, 0);
    }

    private void test1() throws Exception {
        String matchText = "protein";
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
