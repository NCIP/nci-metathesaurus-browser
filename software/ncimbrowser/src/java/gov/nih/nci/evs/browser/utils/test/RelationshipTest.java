package gov.nih.nci.evs.browser.utils.test;

import java.util.*;

import org.LexGrid.concepts.*;

import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.utils.*;

public class RelationshipTest extends DataUtils {
    private boolean _suppressOtherMessages = true;
    private boolean _displayParameters = false;
    private boolean _displayRelationships = false;
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
        DBG.debug(DBG.isDisplayDetails(), "* Details: " + code);
        return super.getAssociationTargetHashMap(scheme, version, code, sort_option);
    }
    
    public void getAssociationTargetHashMapTest(String scheme, String version, String code, Vector sort_option) {
        DBG.clearTabbbedValues();
        Utils.StopWatch stopWatch = new Utils.StopWatch();
        HashMap hmap = getAssociationTargetHashMap(scheme, version, code, sort_option);
        long duration = stopWatch.getDuration();

        Vector<String> vector = new Vector<String>(hmap.keySet());
        int count = 0;
        
        if (vector.size() > 0) {
            DBG.debug(_displayRelationships, "* List of associations:");
            for (int i = 0; i < vector.size(); i++) {
                String key = vector.elementAt(i);
                Vector<String> value = (Vector<String>) hmap.get(key);
                if (_displayRelationships)
                    DBG.debugVector("    ", "* List: " + key, value);
                count += value.size();
            }
        }

        Concept concept = getConceptByCode(scheme, version, null, code);
        String conceptName = "";
        if (concept != null) 
            conceptName = concept.getEntityDescription().getContent();
        if (_displayResults) {
            DBG.debug("* Result: " + code + " (" + conceptName + ")");
            DBG.debug("  * Hits: " + count);
            DBG.debug("  * Total run time: " + stopWatch.getResult(duration));
        }
        if (DBG.isDisplayTabDelimitedFormat()) {
            int i=0;
            DBG.debugTabbedValue(i++, "* Tabbed", "");
            DBG.debugTabbedValue(i++, "code", code);
            DBG.debugTabbedValue(i++, "Hits", count);
            DBG.debugTabbedValue(i++, "Run Time", stopWatch.formatInSec(duration));
            DBG.debugTabbedValue("Concept name", conceptName);
            DBG.displayTabbedValues();
        }
    }

    private void prompt(String[] codes) {
        boolean isTrue = false;
        
        while (true) {
            DBG.debug("* codes: " + Utils.toString(codes));
            DBG.debug("* Prompt (" + getClass().getSimpleName() + "):");
            _suppressOtherMessages = Prompt.prompt(
                "  * Suppress other debugging messages", _suppressOtherMessages);
            Debug.setDisplay(!_suppressOtherMessages);
            _displayParameters = Prompt.prompt("  * Display parameters",
                _displayParameters);
            isTrue = Prompt.prompt("  * Display details", DBG.isDisplayDetails());
            DBG.setDisplayDetails(isTrue);
            _displayRelationships = Prompt.prompt("  * Display relationships", _displayRelationships);
            _displayResults = Prompt.prompt("  * Display results", _displayResults);
            isTrue = Prompt.prompt("  * Display tab delimited",
                DBG.isDisplayTabDelimitedFormat());
            DBG.setDisplayTabDelimitedFormat(isTrue);
            
            boolean changeSettings = false;
            changeSettings = Prompt.prompt("  # Change Settings", changeSettings);
            if (! changeSettings)
                break;
            DBG.debug("");
            DBG.debug(Utils.SEPARATOR);
        }
    }

    public void runTest() {
        String scheme = Constants.CODING_SCHEME_NAME;
        String version = null;
        Vector<String> sort_option = new Vector<String>();
        for (int i=0; i<6; ++i)
            sort_option.add("source");
        
        String[] codes = new String[] { 
            "C0017636", // Glioblastoma 
            "C1704407", // Arabic numeral 100
            "C0391978", // Bone Tissue
            "C0005767", // Blood
            "C0002085", // Allele
            "C0000735", // Abdominal Neoplasms
            "C0998265", // Cancer Genus
            "C0017337", // Gene
            "C0027651", // Neoplasms
            "C0441800", // Grade
            "CL342077", // Cell
            "C1516728", // Common Terminology Criteria for Adverse Events
            "C0007097", // Carcinoma
            "C0021311", // Infection 
            "C0441471", // Event
            "C1306673", // Stage
            "C0699733", // Devices
            "C0033684", // Proteins
            "C1285092", // Gland
            "C0175677", // Injury
            "C0439793", // Severities
        };

        // codes = new String[] { "C0017636" };
        // codes = new String[] { "CL342077" };
        // codes = new String[] { "C0439793" };

        prompt(codes);
        for (int i = 0; i < codes.length; ++i) {
            String code = codes[i];
            if (DBG.isDisplayDetails()) {
                DBG.debug("");
                DBG.debug(Utils.SEPARATOR);
            }
            getAssociationTargetHashMapTest(scheme, version, code, sort_option);
        }
        DBG.debug("* Done");
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
        RelationshipTest test = new RelationshipTest();
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
    }
}
