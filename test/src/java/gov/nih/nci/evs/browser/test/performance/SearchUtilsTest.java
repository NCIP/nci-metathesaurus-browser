package gov.nih.nci.evs.browser.test.performance;

import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;

import gov.nih.nci.evs.browser.properties.*;
import gov.nih.nci.evs.browser.test.utils.*;
import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.browser.utils.test.*;

public class SearchUtilsTest extends SearchUtils {
    private int _runAmount = 1;
    private boolean _suppressOtherMessages = true;
    private boolean _displayParameters = false;
    private boolean _displayConcepts = false;
    private boolean _displayResults = true;

    public SearchUtilsTest() {
        super(null);
    }

    public ResolvedConceptReferencesIteratorWrapper searchByName(String scheme,
        String version, String matchText, String source, String matchAlgorithm,
        boolean ranking, int maxToReturn) {
        if (_displayParameters) {
            DBG.debug("* Search parameters:");
            DBG.debug("  * scheme = " + scheme);
            DBG.debug("  * version = " + version);
            DBG.debug("  * matchText = " + matchText);
            DBG.debug("  * source = " + source);
            DBG.debug("  * matchAlgorithm = " + matchAlgorithm);
            DBG.debug("  * ranking = " + ranking);
            DBG.debug("  * maxToReturn = " + maxToReturn);
        }
        DBG.debug(DBG.isDisplayDetails(), "* Details: " + matchAlgorithm + " " + matchText);
        return super.searchByName(scheme, version, matchText, source,
            matchAlgorithm, ranking, maxToReturn);
    }


    public void searchByNameTest(String scheme, String version, String matchText,
        String source, String matchAlgorithm, boolean ranking,
        int maxToReturn) throws Exception {
        DBG.clearTabbbedValues();
        Utils.StopWatch stopWatch = new Utils.StopWatch();

        ResolvedConceptReferencesIteratorWrapper wrapper = searchByName(scheme, version, matchText,
            source, matchAlgorithm, ranking, maxToReturn);
        ResolvedConceptReferencesIterator iterator = wrapper.getIterator();
        int n = iterator.numberRemaining(), i=0;
        
        long duration = stopWatch.getDuration();
        if (_displayConcepts && n > 0) {
            DBG.debug("* List of concepts:");
            while (iterator.hasNext()) {
                ResolvedConceptReference ref = iterator.next();
                String code = ref.getCode();
                String name = ref.getEntityDescription().getContent();
                DBG.debug("  " + (++i) + ") " + code + " " + name);
            }
        }
        if (_displayResults) {
            DBG.debug("* Result: " + matchAlgorithm + " " + matchText);
            DBG.debug("  * Number of concepts: " + n);
            DBG.debug("  * Total run time: " + stopWatch.getResult(duration));
        }
        if (DBG.isDisplayTabDelimitedFormat()) {
            i=0;
            DBG.debugTabbedValue(i++, "* Tabbed", "");
            DBG.debugTabbedValue(i++, "Keyword", matchText);
            DBG.debugTabbedValue(i++, "Algorithm", matchAlgorithm);
            DBG.debugTabbedValue(i++, "Hits", n);
            DBG.debugTabbedValue(i++, "Run Time", stopWatch.formatInSec(duration));
            DBG.displayTabbedValues();
        }
    }

    private void prompt() {
        boolean isTrue = false;

        DBG.debug("* Prompt (" + getClass().getSimpleName() + "):");
        _runAmount = Prompt.prompt(
            "  * How many concepts", _runAmount);
        _suppressOtherMessages = Prompt.prompt(
            "  * Suppress other debugging messages", _suppressOtherMessages);
        Debug.setDisplay(!_suppressOtherMessages);
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

    private void runTest() throws Exception {
        String scheme = "NCI MetaThesaurus";
        String version = null;
        String matchAlgorithm = "contains";
        String source = "ALL";
        boolean ranking = true;
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

//        matchAlgorithm = "exactMatch";
//        matchTexts = new String[] { "cell" };

        NCImBrowserProperties.getInstance();
        DBG.debug("* EVS_SERVICE_URL: " + NCImBrowserProperties
            .getProperty(NCImBrowserProperties.EVS_SERVICE_URL));
        DBG.debug("* matchTexts: " + Utils.toString(matchTexts));
        DBG.debug("* matchAlgorithm: " + matchAlgorithm);
        prompt();

        for (int i = 0; i < matchTexts.length; ++i) {
            if (i >= _runAmount)
                break;
            String matchText = matchTexts[i];
            if (DBG.isDisplayDetails()) {
                DBG.debug("");
                DBG.debug(Utils.SEPARATOR);
            }
            searchByNameTest(scheme, version, matchText, source, matchAlgorithm,
                ranking, maxToReturn);
        }
        DBG.debug("* Done");
    }

    public static void main(String[] args) {
        try {
            DBG.setPerformanceTesting(true);
            SearchUtilsTest test = new SearchUtilsTest();
            boolean isContinue = true;
            do {
                test.runTest();
                DBG.debug("");
                DBG.debug(Utils.SEPARATOR);
                isContinue = Prompt.prompt("Rerun", isContinue);
                if (!isContinue)
                    break;
            } while (isContinue);
            DBG.debug("Done");
            DBG.setPerformanceTesting(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
