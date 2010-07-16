package gov.nih.nci.evs.browser.test;

import java.util.*;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.apache.log4j.Logger;

import gov.nih.nci.evs.browser.utils.*;

public class ContainsRelationTest {
    private static final String SEPARATOR =
        "----------------------------------------"
            + "----------------------------------------";
    private static Logger _logger =
        Logger.getLogger(ContainsRelationTest.class);

    public ContainsRelationTest() {
        String scheme = "NCI Metathesaurus";
        // String matchText = "single dose vial";
        String matchText = "single dose";
        String source = "ALL";
        String matchAlgorithm = "contains";
        String version = null;
        String rel = null;
        String rela = null;
        int maxToReturn = 1000;
        search(scheme, matchText, source, matchAlgorithm, version, rel, rela,
            maxToReturn);
    }

    private void search(String scheme, String matchText, String source,
        String matchAlgorithm, String version, String rel, String rela,
        int maxToReturn) {
        try {
            _logger.debug(SEPARATOR);
            _logger.debug("Calling new SearchUtils().searchByRELA");
            _logger.debug("  * scheme: " + scheme);
            _logger.debug("  * matchText: " + matchText);
            _logger.debug("  * source: " + source);
            _logger.debug("  * matchAlgorithm: " + matchAlgorithm);
            _logger.debug("  * version: " + version);
            _logger.debug("  * rel: " + rel);
            _logger.debug("  * rela: " + rela);
            _logger.debug("  * maxToReturn: " + maxToReturn);
            ResolvedConceptReferencesIteratorWrapper wrapper =
                new SearchUtils().searchByRELA(scheme, version, matchText,
                    source, matchAlgorithm, rel, rela, maxToReturn);

            ResolvedConceptReferencesIterator iterator = wrapper.getIterator();
            printConcepts(iterator, maxToReturn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printConcepts(ResolvedConceptReferencesIterator iterator,
        int maxToReturn) throws Exception {
        _logger.debug(SEPARATOR);
        int i = 0, size = iterator.numberRemaining();
        while (iterator.hasNext()) {
            ResolvedConceptReference[] rcrs =
                iterator.next(maxToReturn).getResolvedConceptReference();
            for (ResolvedConceptReference rcr : rcrs) {
                String code = rcr.getConceptCode();
                String name = rcr.getEntityDescription().getContent();
                _logger.debug(++i + ") " + code + ": " + name);
            }
        }
        _logger.debug("size: " + size);
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
        new ContainsRelationTest();
    }
}
