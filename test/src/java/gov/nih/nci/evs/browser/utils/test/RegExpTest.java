package gov.nih.nci.evs.browser.utils.test;

import java.util.*;

import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;

import org.LexGrid.LexBIG.DataModel.Collections.*;

public class RegExpTest {
    private LexBIGService lbs = RemoteServerUtil.createLexBIGService();
    private Vector<String> searchTexts = new Vector<String>();
    private String codingSchemeName = "NCI MetaThesaurus"; //"NCI Thesaurus"
    private String algorithm = "exactMatch";
    
    public RegExpTest() {
        initializeTestCases();
    }

    private void initializeTestCases() {
        searchTexts = new Vector<String>();
        searchTexts.add("blood");
        searchTexts.add("cell");
    }

    public void testIterator() throws Exception {
        for (int i = 0; i < searchTexts.size(); i++) {
            String t = (String) searchTexts.elementAt(i);
            System.out.println("---------------------------------------------");
            System.out.println("* codingSchemeName: " + codingSchemeName);
            System.out.println("* Search string: " + t);

            CodedNodeSet cns = null;
            try {
                cns = lbs.getCodingSchemeConcepts(codingSchemeName, null);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            System.out.println("* cns: " + (cns == null ? "null" : "not null"));

            cns = cns.restrictToMatchingDesignations(t,
                SearchDesignationOption.ALL, algorithm, null);
            ResolvedConceptReferencesIterator itr = null;
            int cnt = 0;
            try {
                LocalNameList restrictToProperties = new LocalNameList();
                SortOptionList sortCriteria = null;
                boolean resolveConcepts = false;

                itr = cns.resolve(sortCriteria, null, restrictToProperties,
                    null, resolveConcepts);
                while (itr.hasNext()) {
                    System.out.println("* hasNext cnt: " + cnt);
                    ResolvedConceptReference[] refs = itr.next(100)
                        .getResolvedConceptReference();
                    for (ResolvedConceptReference ref : refs) {
                        System.out.println(cnt + ") " + getConceptInfo(ref));
                        cnt++;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (cnt == 0)
                System.out.println("No match.");
            System.out.println("");
        }
    }

    public void testList() throws Exception {
        for (int i = 0; i < searchTexts.size(); i++) {
            String t = (String) searchTexts.elementAt(i);
            System.out.println("---------------------------------------------");
            System.out.println("* Search string: " + t);

            CodedNodeSet cns = lbs.getCodingSchemeConcepts(codingSchemeName,
                null);
            cns = cns.restrictToMatchingDesignations(t,
                SearchDesignationOption.ALL, algorithm, null);
            ResolvedConceptReference[] list = null;
            int cnt = 0;
            try {
                list = cns.resolveToList(null, null, null, 500)
                    .getResolvedConceptReference();

                for (ResolvedConceptReference ref : list) {
                    System.out.println(cnt + ") " + getConceptInfo(ref));
                    cnt++;
                }
            } catch (Exception ex) {
                System.out.println("Exception thrown #2");
            }
            if (cnt == 0)
                System.out.println("No match.");
            System.out.println("");
        }
    }

    protected String getConceptInfo(ResolvedConceptReference ref) {
        return ref.getConceptCode() + ":" + 
            ref.getEntityDescription().getContent();
    }

    public static void main(String[] args) throws Exception {
        RegExpTest test = new RegExpTest();
        test.testIterator();
        //test.testList();
    }
}
