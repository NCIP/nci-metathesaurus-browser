package gov.nih.nci.evs.browser.test;

import java.util.*;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import gov.nih.nci.evs.browser.utils.*;

public class Test1 {
    private LexBIGService evsService = null;
    private ResolvedConceptReferenceList concepts = null;
    
    public Test1() {
        process();
    }
    
    private void debug(String text) {
        System.out.println("Debug: " + text);
    }
    
    private void process() {
        try {
            debug("Method: Test1.process");
            evsService = RemoteServerUtil.createLexBIGService();
    
            String dtsVocab = "NCI Metathesaurus";
            String prefName = "blood";
            int sMetaLimit = 1000;
    
            process(dtsVocab, prefName, sMetaLimit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void process(String dtsVocab, String prefName, int sMetaLimit) 
        throws Exception {
        debug("Calling: evsService.getNodeSet");
        CodedNodeSet metaNodes = evsService.getNodeSet(dtsVocab, null, null); 

        debug("Calling: metaNodes.restrictToMatchingDesignations");
        metaNodes = metaNodes.restrictToMatchingDesignations(prefName, //the text to match 
                    CodedNodeSet.SearchDesignationOption.ALL,  //whether to search all designation, only Preferred or only Non-Preferred
                    "contains", //the match algorithm to use
                    null); //the language to match (null matches all)


        debug("Calling: metaNodes.resolveToList");
        concepts = metaNodes.resolveToList(
                    null, //Sorts used to sort results (null means sort by match score)
                    null, //PropertyNames to resolve (null resolves all)
                    null,  //PropertyTypess to resolve (null resolves all)
                    sMetaLimit    //cap the number of results returned (-1 resolves all)
        );
        debug("Done");
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
    
    public static void main(String[] args) {
        parse(args);
        new Test1();
    }
}
