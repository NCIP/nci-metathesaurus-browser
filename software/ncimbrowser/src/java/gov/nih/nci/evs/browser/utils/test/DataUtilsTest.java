package gov.nih.nci.evs.browser.utils.test;

import java.util.*;

import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.utils.*;

public class DataUtilsTest extends DataUtils {
    private boolean _displayParameters = false;
    private boolean _displayAssociations = false;
    private boolean _displayResults = true;

    public HashMap getAssociationTargetHashMap(String scheme, String version, String code, Vector sort_option) {
        if (_displayParameters) {
            DBG.debug("* Search parameters:");
            DBG.debug("  * scheme = " + scheme);
            DBG.debug("  * version = " + version);
            DBG.debug("  * code = " + code);
            Object items[] = sort_option.toArray();
            for (int i=0; i<items.length; ++i) {
                Object value = items[i];
                DBG.debug("  * sort_option[" + i + "]: " + value.toString());
            }
        }
        DBG.debugDetails("* Search Details: " + code);
        return super.getAssociationTargetHashMap(scheme, version, code, sort_option);
    }

    public void getAssociationTarget(String scheme, String version, String code, Vector sort_option) {
        DBG.clearTabbbedValues();
        Utils.StopWatch stopWatch = new Utils.StopWatch();
        HashMap hmap = getAssociationTargetHashMap(scheme, version, code, sort_option);
        long duration = stopWatch.getDuration();

        Vector<String> v = new Vector(hmap.keySet());
        int count = 0;
        
        if (_displayAssociations && v.size() > 0) {
            DBG.debug("* List of associations:");
            for (int i = 0; i < v.size(); i++) {
                int j = i + 1;
                String key = v.elementAt(i);
                Vector value = (Vector) hmap.get(key);
                DBG.debug("  " + j + ") " + key + " " + value);
                count += value.size();
            }
        }
        if (_displayResults) {
            DBG.debug("* Result: " + code);
            DBG.debug("  * Number of associations: " + count);
            DBG.debug("  * Total runtime: " + stopWatch.getResult(duration));
        }
        if (DBG.isDisplayTabDelimitedFormat()) {
            int i=0;
            DBG.debugTabbedValue(i++, "* Tabbed", "");
            DBG.debugTabbedValue(i++, "code", code);
            DBG.debugTabbedValue(i++, "Hits", Integer.toString(v.size()));
            DBG.debugTabbedValue(i++, "Run Time", stopWatch.formatInSec(duration));
            DBG.displayTabbedValues();
        }
    }

    private void prompt() {
        boolean isTrue = false;
        
        DBG.debug("* Prompt:");
        isTrue = Prompt.prompt(
            "  * Suppress other debugging messages", Debug.isDisplay());
        Debug.setDisplay(isTrue);
        _displayParameters = Prompt.prompt("  * Display parameters",
            _displayParameters);
        isTrue = Prompt.prompt("  * Display details", DBG.isDisplayDetails());
        DBG.setDisplayDetails(isTrue);
        _displayAssociations = Prompt.prompt("  * Display associations", _displayAssociations);
        _displayResults = Prompt.prompt("  * Display results", _displayResults);
        isTrue = Prompt.prompt("  * Display tab delimited",
            DBG.isDisplayTabDelimitedFormat());
        DBG.setDisplayTabDelimitedFormat(isTrue);
    }

    public void testData() {
        prompt();
        
        String scheme = Constants.CODING_SCHEME_NAME;
        String version = null;
        String code = "C0017636";
        Vector<String> sort_option = new Vector<String>();
        for (int i=0; i<6; ++i)
            sort_option.add("source");
        
        getAssociationTarget(scheme, version, code, sort_option);
    }
    
    private static void parse(String[] args) {
        String prevArg = "";
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (arg.equals("-propertyFile")) {
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
        DataUtilsTest test = new DataUtilsTest();
        boolean isContinue = true;
        do {
            test.testData();
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
