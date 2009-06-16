package gov.nih.nci.evs.browser.utils.test;

import java.util.*;

import org.LexGrid.concepts.*;
import gov.nih.nci.evs.browser.utils.*;

public class SearchUtilsTest extends SearchUtils {
    public static boolean isPerformanceTesting = false;
    private static boolean isExcelFormat = false;
    
    public SearchUtilsTest(String url) {
        super(url);
    }

    public Vector<org.LexGrid.concepts.Concept> searchByName(String scheme,
        String version, String matchText, String source, String matchAlgorithm,
        int maxToReturn) {
        debug(! isExcelFormat, Utils.SEPARATOR);
        debug(! isExcelFormat, "scheme = " + scheme);
        debug(! isExcelFormat, "version = " + version);
        debug(! isExcelFormat, "matchText = " + matchText);
        debug(! isExcelFormat, "source = " + source);
        debug(! isExcelFormat, "matchAlgorithm = " + matchAlgorithm);
        debug(! isExcelFormat, "maxToReturn = " + maxToReturn);
        return super.searchByName(scheme, version, matchText, source,
            matchAlgorithm, maxToReturn);
    }

    private void debug(String text) {
        if (isPerformanceTesting)
            System.out.println("PERF: " + text);
    }

    private void debug(boolean display, String text) {
        if (display)
            debug(text);
    }

    public void search(String scheme, String version, String matchText,
        String matchAlgorithm, int maxToReturn) {
        Utils.StopWatch stopWatch = new Utils.StopWatch();
        Vector<org.LexGrid.concepts.Concept> v = searchByName(scheme, version,
            matchText, matchAlgorithm, maxToReturn);
        String runtime = stopWatch.getResultInSeconds();
        debug(! isExcelFormat, "Run time: " + runtime + " sec");
        if (v != null) {
            debug(! isExcelFormat, "# of results: " + v.size());
            for (int i = 0; i < v.size(); i++) {
                int j = i + 1;
                Concept ce = (Concept) v.elementAt(i);
                debug(! isExcelFormat, "(" + j + ")" + " " + ce.getEntityCode() + " "
                    + ce.getEntityDescription().getContent());
            }
        }
        debug(isExcelFormat, "Excel: " + matchText + "\t" + matchAlgorithm + 
            "\t" + runtime + "\t" + v.size());
    }
    
    private void test1() {
        String scheme = "NCI MetaThesaurus";
        String version = null;
        String matchAlgorithm = "contains";
        matchAlgorithm = "exactMatch";
        int maxToReturn = 200;

        String[] matchTexts = new String[] {
            "blood", "cell"
        };

        for (int i=0; i<matchTexts.length; ++i)
            search(scheme, version, matchTexts[i], matchAlgorithm, maxToReturn);
        debug("Done");
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
        test.test1();
    }
}
