package gov.nih.nci.evs.browser.utils.test;

import java.util.*;

import org.LexGrid.concepts.*;
import gov.nih.nci.evs.browser.utils.*;

public class SearchUtilsTest extends SearchUtils {
    private static boolean isPerformanceTesting = false;
    private static boolean displayDetails = true;
    private boolean suppressOtherMessages = true;
    private boolean displayParameters = false;
    private boolean displayConcepts = false;
    private static boolean displayTabDelimitedFormat = false;
    private static ArrayList<String> tabList = new ArrayList<String>();

    public SearchUtilsTest(String url) {
        super(url);
    }
    
    public Vector<org.LexGrid.concepts.Concept> searchByName(String scheme,
        String version, String matchText, String source, String matchAlgorithm,
        int maxToReturn) {
        if (displayParameters) {
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
        if (isPerformanceTesting)
            System.out.println(text);
    }

    private static void debug(boolean display, String text) {
        if (display)
            debug(text);
    }
    
    public static void debugDetails(String text) {
        if (displayDetails)
            debug("  " + text);
    }
    
    public static void debugTabbedValue(int index, String name, String value) {
        if (! displayTabDelimitedFormat || tabList == null)
            return;

        String text = "";
        if (name != null && name.length() > 0)
            text += name + ":\t";
        if (value != null && value.length() > 0)
            text += value + "\t";
        if (index < 0)
            tabList.add(text);
        else tabList.add(index, text);
    }
    
    public static void debugTabbedValue(String name, String value) {
        debugTabbedValue(-1, name, value);
    }

    private static void displayTabbedValues() {
        if (! displayTabDelimitedFormat || tabList == null || tabList.size() <= 0)
            return;

        StringBuffer buffer = new StringBuffer();
        Iterator<String> iterator = tabList.iterator();
        while (iterator.hasNext()) {
            String value = iterator.next();
            buffer.append(value);
        }
        debug(buffer.toString());
    }

    public void search(String scheme, String version, String matchText,
        String source, String matchAlgorithm, int maxToReturn) {
        tabList = new ArrayList<String>();
        Utils.StopWatch stopWatch = new Utils.StopWatch();
        Vector<Concept> v = searchByName(scheme, version, matchText,
            source, matchAlgorithm, maxToReturn);
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
        if (displayDetails) {
            debug("* Result: " + matchAlgorithm + " " + matchText);
            debug("  * Number of concepts: " + v.size());
            debug("  * Total runtime: " + stopWatch.getResult(duration));
        }
        if (displayTabDelimitedFormat) {
            int i=0;
            debugTabbedValue(i++, "* Tabbed", "");
            debugTabbedValue(i++, "matchText", matchText);
            debugTabbedValue(i++, "algorithm", matchAlgorithm);
            debugTabbedValue(i++, "hits", Integer.toString(v.size()));
            debugTabbedValue(i++, "runtime", stopWatch.formatInSec(duration));
            displayTabbedValues();
        }
    }

    private void testSearch() {
        String scheme = "NCI MetaThesaurus";
        String version = null;
        String matchAlgorithm = "contains";
        String source = null;
        matchAlgorithm = "exactMatch";
        int maxToReturn = -1;

        suppressOtherMessages = Prompt.prompt(
            "Suppress other debugging messages", suppressOtherMessages);
        Debug.setDisplay(!suppressOtherMessages);
        displayParameters = Prompt.prompt("Display parameters",
            displayParameters);
        displayDetails = Prompt.prompt("Display details", displayDetails);
        displayConcepts = Prompt.prompt("Display concepts", displayConcepts);
        displayTabDelimitedFormat = Prompt.prompt("Display tab delimited",
            displayTabDelimitedFormat);

        String[] matchTexts = new String[] { "blood", "cell" };

        for (int i = 0; i < matchTexts.length; ++i) {
            String matchText = matchTexts[i];
            if (displayDetails) {
                debug("");
                debug(Utils.SEPARATOR);
                debug("* Details: " + matchAlgorithm + " " + matchText);
            }
            search(scheme, version, matchText, source, matchAlgorithm, maxToReturn);
        }
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

        isPerformanceTesting = true;
        SearchUtilsTest test = new SearchUtilsTest(url);
        boolean isContinue = true;
        do {
            test.testSearch();
            debug("");
            debug(Utils.SEPARATOR);
            isContinue = Prompt.prompt("Rerun", isContinue);
            if (!isContinue)
                break;
        } while (isContinue);
        debug("Done");
        isPerformanceTesting = false;
    }
}
