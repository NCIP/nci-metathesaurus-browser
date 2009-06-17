package gov.nih.nci.evs.browser.utils.test;

import java.util.*;

import org.LexGrid.concepts.*;
import gov.nih.nci.evs.browser.utils.*;

public class SearchUtilsTest extends SearchUtils {
    private boolean suppressOtherMessages = false;
    private boolean displayParameters = false;
    private boolean displayConcepts = false;
    private boolean displayTabDelimitedFormat = false;

    public SearchUtilsTest(String url) {
        super(url);
    }

    public Vector<org.LexGrid.concepts.Concept> searchByName(String scheme,
        String version, String matchText, String source, String matchAlgorithm,
        int maxToReturn) {
        if (displayParameters) {
            debug(Utils.SEPARATOR);
            debug("* Search parameters:");
            debug("  * scheme = " + scheme);
            debug("  * version = " + version);
            debug("  * matchText = " + matchText);
            debug("  * source = " + source);
            debug("  * matchAlgorithm = " + matchAlgorithm);
            debug("  * maxToReturn = " + maxToReturn);
        }
        return super.searchByName(scheme, version, matchText, source,
            matchAlgorithm, maxToReturn);
    }

    private static void debug(String text) {
        System.out.println(text);
    }

    private static void debug(boolean display, String text) {
        if (display)
            debug(text);
    }

    public void search(String scheme, String version, String matchText,
        String matchAlgorithm, int maxToReturn) {
        Utils.StopWatch stopWatch = new Utils.StopWatch();
        Vector<Concept> v = searchByName(scheme, version, matchText,
            matchAlgorithm, maxToReturn);
        long duration = stopWatch.getDuration();

        if (displayConcepts && v.size() > 0) {
            debug("* List of concepts:");
            for (int i = 0; i < v.size(); i++) {
                int j = i + 1;
                Concept ce = v.elementAt(i);
                debug("  " + j + ") " + ce.getEntityCode() + " "
                    + ce.getEntityDescription().getContent());
            }
        }
        debug(!displayTabDelimitedFormat, "* Number of concepts: " + v.size());
        debug(!displayTabDelimitedFormat, "* " + stopWatch.getResult(duration));
        debug(displayTabDelimitedFormat, "* Tabbed: " + matchText + "\t"
            + matchAlgorithm + "\t" + stopWatch.formatInSec(duration) + "\t"
            + v.size());
    }

    private void test1() {
        String scheme = "NCI MetaThesaurus";
        String version = null;
        String matchAlgorithm = "contains";
        matchAlgorithm = "exactMatch";
        int maxToReturn = -1;

        suppressOtherMessages = Prompt.prompt(
            "Suppress other debugging messages", suppressOtherMessages);
        Debug.setDisplay(!suppressOtherMessages);
        displayParameters = Prompt.prompt("Display parameters",
            displayParameters);
        displayConcepts = Prompt.prompt("Display concepts", displayConcepts);
        displayTabDelimitedFormat = Prompt.prompt("Display tab delimited",
            displayTabDelimitedFormat);

        String[] matchTexts = new String[] { "blood", "cell" };

        for (int i = 0; i < matchTexts.length; ++i)
            search(scheme, version, matchTexts[i], matchAlgorithm, maxToReturn);
        debug("* Done");
    }

    public static void main(String[] args) {
        String prevArg = "";
        String url = "http://lexevsapi-qa.nci.nih.gov/lexevsapi50";
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (arg.equals("-url")) {
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
        boolean isContinue = true;
        do {
            test.test1();
            debug("");
            debug(Utils.SEPARATOR);
            isContinue = Prompt.prompt("Continue", isContinue);
            if (!isContinue)
                break;
        } while (isContinue);
        debug("Done");
    }
}
