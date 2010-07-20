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

public class KevinTest {

    // private static LexBIGService lbs = LexBIGServiceImpl.defaultInstance();

    public static void main(String[] args) throws Exception {
        args = parse(args);
        LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        
        CodedNodeSet cns =
            lbs.getCodingSchemeConcepts("NCI MetaThesaurus", null);
        cns =
            cns.restrictToMatchingDesignations("single dose",
                SearchDesignationOption.ALL, "LuceneQuery", null);

        CodedNodeGraph cng = lbs.getNodeGraph("NCI MetaThesaurus", null, null);
        cng = cng.restrictToTargetCodes(cns);
        ResolvedConceptReferenceList list =
            cng.resolveAsList(null, true, false, 0, 0, null, null, null, null,
                500);

        for (ResolvedConceptReference ref : list.getResolvedConceptReference()) {
            System.out.println("Code: " + ref.getCode());
            System.out.println(" Entity Description"
                + ref.getEntityDescription().getContent());
        }
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
