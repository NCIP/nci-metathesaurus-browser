package gov.nih.nci.evs.browser.utils.test;

import java.util.*;

import org.LexGrid.concepts.*;
import gov.nih.nci.evs.browser.utils.*;

public class SearchUtilsTest extends SearchUtils {
    private static String _url = "http://lexevsapi-qa.nci.nih.gov/lexevsapi50";
    private boolean _displayParameters = false;
    private boolean _displayConcepts = false;
    private boolean _displayResults = true;

    public SearchUtilsTest(String url) {
        super(url);
    }
    
    public Vector<org.LexGrid.concepts.Concept> searchByName(String scheme,
        String version, String matchText, String source, String matchAlgorithm,
        int maxToReturn) {
        if (_displayParameters) {
            DBG.debug("* Search parameters:");
            DBG.debug("  * scheme = " + scheme);
            DBG.debug("  * version = " + version);
            DBG.debug("  * matchText = " + matchText);
            DBG.debug("  * source = " + source);
            DBG.debug("  * matchAlgorithm = " + matchAlgorithm);
            DBG.debug("  * maxToReturn = " + maxToReturn);
        }
        DBG.debug(DBG.isDisplayDetails(), "* Search Details: " + matchAlgorithm + " " + matchText);
        return super.searchByName(scheme, version, matchText, source,
            matchAlgorithm, maxToReturn);
    }
    
    public void search(String scheme, String version, String matchText,
        String source, String matchAlgorithm, int maxToReturn) {
        DBG.clearTabbbedValues();
        Utils.StopWatch stopWatch = new Utils.StopWatch();
        Vector<Concept> v = searchByName(scheme, version, matchText,
            source, matchAlgorithm, maxToReturn);
        long duration = stopWatch.getDuration();

        if (_displayConcepts && v.size() > 0) {
            DBG.debug("* List of concepts:");
            for (int i = 0; i < v.size(); i++) {
                int j = i + 1;
                Concept ce = v.elementAt(i);
                DBG.debug("  " + j + ") " + ce.getEntityCode() + " "
                    + ce.getEntityDescription().getContent());
            }
        }
        if (_displayResults) {
            DBG.debug("* Result: " + matchAlgorithm + " " + matchText);
            DBG.debug("  * Number of concepts: " + v.size());
            DBG.debug("  * Total runtime: " + stopWatch.getResult(duration));
        }
        if (DBG.isDisplayTabDelimitedFormat()) {
            int i=0;
            DBG.debugTabbedValue(i++, "* Tabbed", "");
            DBG.debugTabbedValue(i++, "Keyword", matchText);
            DBG.debugTabbedValue(i++, "Algorithm", matchAlgorithm);
            DBG.debugTabbedValue(i++, "Hits", Integer.toString(v.size()));
            DBG.debugTabbedValue(i++, "Run Time", stopWatch.formatInSec(duration));
            DBG.displayTabbedValues();
        }
    }
    
    private void prompt() {
        boolean isTrue = false;
        
        DBG.debug("* Prompt:");
        isTrue = Prompt.prompt(
            "  * Suppress other debugging messages", ! Debug.isDisplay());
        Debug.setDisplay(isTrue);
        _displayParameters = Prompt.prompt("  * Display parameters",
            _displayParameters);
        isTrue = Prompt.prompt("  * Display details", DBG.isDisplayDetails());
        DBG.setDisplayDetails(isTrue);
        _displayConcepts = Prompt.prompt("  * Display concepts", _displayConcepts);
        _displayResults = Prompt.prompt("  * Display results", _displayResults);
        isTrue = Prompt.prompt("  * Display tab delimited",
            DBG.isDisplayTabDelimitedFormat());
        DBG.setDisplayTabDelimitedFormat(isTrue);
    }

    private void testSearch() {
        String scheme = "NCI MetaThesaurus";
        String version = null;
        String matchAlgorithm = "contains";
        String source = null;
        int maxToReturn = -1;
        String[] matchTexts = new String[] { 
            "100",
            "bone",
            "blood",
            "allele",
            "tumor",
            "cancer",
            "gene",
            "neoplasm",
            "grade",
            "cell",
            "ctcae",
            "carcinoma",
            "infection",
            "event",
//            "adverse",
            "stage",
            "device",
            "protein",
            "gland",
            "injury"
        };
        
        matchAlgorithm = "exactMatch";
        matchTexts = new String[] { "cell" };
        prompt();
        
        for (int i = 0; i < matchTexts.length; ++i) {
            String matchText = matchTexts[i];
            if (DBG.isDisplayDetails()) {
                DBG.debug("");
                DBG.debug(Utils.SEPARATOR);
            }
            search(scheme, version, matchText, source, matchAlgorithm, maxToReturn);
        }
        DBG.debug("* Done");
    }
    
    private static void parse(String[] args) {
        String prevArg = "";
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (arg.equals("-url")) {
                prevArg = arg;
            } else if (prevArg.equals("-url")) {
                _url = arg;
                prevArg = "";
            } else if (arg.equals("-propertyFile")) {
                prevArg = arg;
            } else if (prevArg.equals("-propertyFile")) {
                System.setProperty(
                    "gov.nih.nci.evs.browser.NCImBrowserProperties", arg);
                prevArg = "";
            }
        }
    }

    public static void main(String[] args) {
        parse(args);
        
        DBG.setPerformanceTesting(true);
        SearchUtilsTest test = new SearchUtilsTest(_url);
        boolean isContinue = true;
        do {
            test.testSearch();
            DBG.debug("");
            DBG.debug(Utils.SEPARATOR);
            isContinue = Prompt.prompt("Rerun", isContinue);
            if (!isContinue)
                break;
        } while (isContinue);
        DBG.debug("Done");
        DBG.setPerformanceTesting(false);
    }
}
