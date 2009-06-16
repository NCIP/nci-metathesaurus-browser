package gov.nih.nci.evs.browser.utils.test;

import java.util.*;

import org.LexGrid.concepts.*;
import gov.nih.nci.evs.browser.utils.*;

public class SearchUtilsTest extends SearchUtils {
    public static boolean isPerformanceTesting = false;
    
    public SearchUtilsTest(String url) {
        super(url);
    }

    public Vector<org.LexGrid.concepts.Concept> searchByName(String scheme,
        String version, String matchText, String source, String matchAlgorithm,
        int maxToReturn) {
        debug(Utils.SEPARATOR);
        debug("scheme = " + scheme);
        debug("version = " + version);
        debug("matchText = " + matchText);
        debug("source = " + source);
        debug("matchAlgorithm = " + matchAlgorithm);
        debug("maxToReturn = " + maxToReturn);
        return super.searchByName(scheme, version, matchText, source,
            matchAlgorithm, maxToReturn);
    }

    protected void debug(String text) {
        if (isPerformanceTesting)
            System.out.println("PERF: " + text);
    }

    public void testSearchByName() {
        String scheme = "NCI MetaThesaurus";
        String version = null;
        String matchText = "blood";
        String matchAlgorithm = "contains";
        matchAlgorithm = "exactMatch";
        int maxToReturn = 200;

        Utils.StopWatch stopWatch = new Utils.StopWatch();
        Vector<org.LexGrid.concepts.Concept> v = searchByName(scheme, version,
            matchText, matchAlgorithm, maxToReturn);
        String runtime = stopWatch.getResultInSeconds();
        debug("Run time: " + runtime + " sec");
        if (v != null) {
            debug("# of results: " + v.size());
            for (int i = 0; i < v.size(); i++) {
                int j = i + 1;
                Concept ce = (Concept) v.elementAt(i);
                debug("(" + j + ")" + " " + ce.getEntityCode() + " "
                    + ce.getEntityDescription().getContent());
            }
        }
        debug("Excel: " + matchText + "\t" + matchAlgorithm + "\t" + runtime
            + "\t" + v.size());
    }

    public static void main(String[] args) {
        String prevArg = "";
        String url = "http://lexevsapi-qa.nci.nih.gov/lexevsapi50";
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (arg.equals("-performanceTesting")) {
                isPerformanceTesting = true;
            } else if (arg.equals("+performanceTesting")) {
                isPerformanceTesting = false;
            } else if (arg.equals("-url")) {
                prevArg = arg;
            } else if (prevArg.equals("-url")) {
                url = arg;
                prevArg = "";
            } else if (arg.equals("-propertyFile")) {
                prevArg = arg;
            } else if (prevArg.equals("-propertyFile")) {
                System.setProperty(
                    "gov.nih.nci.evs.browser.NCImBrowserProperties", arg);
                prevArg = "";
            }
        }

        SearchUtilsTest test = new SearchUtilsTest(url);
        test.testSearchByName();
    }
}
