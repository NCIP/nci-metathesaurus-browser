package gov.nih.nci.evs.browser.utils.test;

import gov.nih.nci.evs.browser.utils.SearchUtils;
import gov.nih.nci.evs.browser.utils.Utils;

public class SearchUtilsTest extends SearchUtils {
    protected static void debug(String text) {
        if (isPerformanceTesting)
            System.out.println("PERF: " + text);
    }

    protected static void debugArguments(String scheme, String version,
        String matchText, String source, String matchAlgorithm,
        int maxToReturn) {
        debug(Utils.SEPARATOR);
        debug("scheme = " + scheme);
        debug("version = " + version);
        debug("matchText = " + matchText);
        debug("source = " + source);
        debug("matchAlgorithm = " + matchAlgorithm);
        debug("maxToReturn = " + maxToReturn);
    }

    public static void main(String[] args)
    {
        String prevArg = "";
        String url = "http://lexevsapi-qa.nci.nih.gov/lexevsapi50";
        for (int i=0; i<args.length; ++i) {
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
                System.setProperty("gov.nih.nci.evs.browser.NCImBrowserProperties", arg);
                prevArg = "";
            }
        }

        SearchUtils test = new SearchUtils(url);
        test.testSearchByName();
    }
}

