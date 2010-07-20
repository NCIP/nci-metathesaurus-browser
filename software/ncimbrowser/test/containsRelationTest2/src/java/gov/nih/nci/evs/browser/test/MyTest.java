package gov.nih.nci.evs.browser.test;

import java.util.ArrayList;

import gov.nih.nci.evs.browser.utils.RemoteServerUtil;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;

public class MyTest {

    public static void main(String[] args) throws Exception {
        args = parse(args);
        boolean printList = false;
        String searchString = "single dose";
        String algorithm = "LuceneQuery";
        search(searchString, algorithm, 0, 0, -1, printList);
        search(searchString, algorithm, 0, 0, 500, printList);
        search(searchString, algorithm, 0, 0, 1000, printList);
        search(searchString, algorithm, 1, 1, -1, printList);
        search(searchString, algorithm, 1, 1, 500, printList);
        search(searchString, algorithm, 1, 1, 1000, printList);
       
        algorithm = "nonLeadingWildcardLiteralSubString";
        search(searchString, algorithm, 0, 0, -1, printList);
        search(searchString, algorithm, 0, 0, 500, printList);
        search(searchString, algorithm, 0, 0, 1000, printList);
        search(searchString, algorithm, 1, 1, -1, printList);
        search(searchString, algorithm, 1, 1, 500, printList);
        search(searchString, algorithm, 1, 1, 1000, printList);
    }

    public static void search(String searchString, String searchAlgorithm, 
        int resolveCodedEntryDepth,
        int resolveAssociationDepth, int maxToReturn, boolean printList)
            throws Exception {
        LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        String codingScheme = "NCI MetaThesaurus";

        System.out.println("----------------------------------------");
        System.out.println("* searchString: " + searchString
            + ", searchAlgorithm: " + searchAlgorithm);

        System.out.println("* resolveCodedEntryDepth: "
            + resolveCodedEntryDepth + ", resolveAssociationDepth: "
            + resolveAssociationDepth + ", maxToReturn: " + maxToReturn);

        CodedNodeSet cns = lbs.getCodingSchemeConcepts(codingScheme, null);
        cns =
            cns.restrictToMatchingDesignations(searchString,
                SearchDesignationOption.ALL, searchAlgorithm, null);

        CodedNodeGraph cng = lbs.getNodeGraph(codingScheme, null, null);
        cng = cng.restrictToTargetCodes(cns);

        ResolvedConceptReferenceList list =
            cng.resolveAsList(null, true, false, resolveCodedEntryDepth,
                resolveAssociationDepth, null, null, null, null, maxToReturn);

        int size = list.getResolvedConceptReferenceCount();
        int i = 0;
        if (printList) {
            for (ResolvedConceptReference ref : list
                .getResolvedConceptReference()) {
                System.out.println((i++) + ") " + ref.getCode() + ": "
                    + ref.getEntityDescription().getContent());
            }
        }
        System.out.println("size: " + size);
        System.out.println("");
    }

    private static String[] parse(String[] args) {
        String prevArg = "";
        ArrayList<String> newArgs = new ArrayList<String>();
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (arg.equals("-propertyFile")) {
                prevArg = arg;
            } else if (prevArg.equals("-propertyFile")) {
                System.setProperty(
                    "gov.nih.nci.evs.browser.NCImBrowserProperties", arg);
                prevArg = "";
            } else {
                newArgs.add(arg);
            }
        }
        return newArgs.toArray(new String[newArgs.size()]);
    }
}
