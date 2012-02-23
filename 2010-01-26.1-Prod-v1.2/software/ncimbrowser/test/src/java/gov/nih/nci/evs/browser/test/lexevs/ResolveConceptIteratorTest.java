package gov.nih.nci.evs.browser.test.lexevs;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;

import gov.nih.nci.evs.browser.utils.*;

public class ResolveConceptIteratorTest {
    private final String SEPARATOR = 
        "---------------------------------------------------------------------";
    private String _codingSchemeName = "NCI MetaThesaurus"; // "NCI Thesaurus"

    private String getConceptInfo(ResolvedConceptReference ref) {
        return ref.getConceptCode() + ": "
            + ref.getEntityDescription().getContent();
    }
    
    private void printConcepts(ResolvedConceptReference[] refs, int offset) {
        for (int i=0; i < refs.length; ++i)
            System.out.println((i+offset) + ") " + getConceptInfo(refs[i]));
    }
    
    private ResolvedConceptReferencesIterator search(String matchText, 
        String algorithm, boolean ranking) throws Exception {
        LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        System.out.println(SEPARATOR);
        System.out.println("* matchText: " + matchText);
        System.out.println("* algorithm: " + algorithm);
        System.out.println("* ranking: " + ranking);

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

        System.out.println(SEPARATOR);
        ResolvedConceptReferencesIterator iterator = cns.resolve(sortCriteria,
            null, restrictToProperties, null, resolveConcepts);
        System.out.println("* Search found: " + iterator.numberRemaining());
        return iterator;
    }

    private void getConcepts(ResolvedConceptReferencesIterator iterator, 
        int maxReturn) throws Exception {

        int n = iterator.numberRemaining();
        int loops = n / maxReturn;
        if (n % maxReturn != 0)
            ++loops;
        
        System.out.println("* n: " + n);
        System.out.println("* maxReturn: " + maxReturn);
        System.out.println("* loops: " + loops);
        
        ResolvedConceptReference[] refs = null;
        int i=0;
        for (; i<loops; ++i) {
            refs = iterator.next(maxReturn).getResolvedConceptReference();
            System.out.println((i * maxReturn) + ") " + getConceptInfo(refs[0]));
        }
        
        boolean printLastPage = true;
        if (printLastPage && refs != null && refs.length > 0) {
            System.out.println(SEPARATOR);
            System.out.println("* Print last page:");
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
