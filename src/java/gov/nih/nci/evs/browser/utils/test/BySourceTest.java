package gov.nih.nci.evs.browser.utils.test;

import java.util.*;

import org.LexGrid.concepts.*;

import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.utils.*;

public class BySourceTest extends DataUtils {
    private boolean _suppressOtherMessages = true;
    private boolean _displayParameters = false;
    private boolean _displayRelationships = false;
    private boolean _displayResults = true;

    public Vector getNeighborhoodSynonyms(String scheme, String version, String code, String sab) {
        if (_displayParameters) {
            DBG.debug("* Search parameters:");
            DBG.debug("  * scheme = " + scheme);
            DBG.debug("  * version = " + version);
            DBG.debug("  * code = " + code);
            DBG.debug("  * sab = " + sab);
        }
        DBG.debug(DBG.isDisplayDetails(), "* Details: " + code);
        return super.getNeighborhoodSynonyms(scheme, version, code, sab);
    }

    public void getNeighborhoodSynonymsTest(String scheme, String version, String code, String sab) {
        DBG.clearTabbbedValues();
        Utils.StopWatch stopWatch = new Utils.StopWatch();
        Vector vector = getNeighborhoodSynonyms(scheme, version, code, sab);
        long duration = stopWatch.getDuration();

        if (_displayRelationships && vector.size() > 0)
            DBG.debugVector("", "* List:", vector);

        Concept concept = getConceptByCode(scheme, version, null, code);
        String conceptName = concept.getEntityDescription().getContent();
        if (_displayResults) {
            DBG.debug("* Result: " + code + " (" + conceptName + ")");
            DBG.debug("  * Hits: " + vector.size());
            DBG.debug("  * Total run time: " + stopWatch.getResult(duration));
        }
        if (DBG.isDisplayTabDelimitedFormat()) {
            int i=0;
            DBG.debugTabbedValue(i++, "* Tabbed", "");
            DBG.debugTabbedValue(i++, "code", code);
            DBG.debugTabbedValue(i++, "Hits", Integer.toString(vector.size()));
            DBG.debugTabbedValue(i++, "Run Time", stopWatch.formatInSec(duration));
            DBG.debugTabbedValue("Concept name", conceptName);
            DBG.displayTabbedValues();
        }
    }

    private void prompt() {
        boolean isTrue = false;
        
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
    }

    public void test1() {
        String scheme = Constants.CODING_SCHEME_NAME;
        String version = null;
        String sab = "NCI";
        
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
        };

        codes = new String[] { "C0017636" };
                
        prompt();
        for (int i = 0; i < codes.length; ++i) {
            String code = codes[i];
            if (DBG.isDisplayDetails()) {
                DBG.debug("");
                DBG.debug(Utils.SEPARATOR);
            }
            getNeighborhoodSynonymsTest(scheme, version, code, sab);
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
        BySourceTest test = new BySourceTest();
        boolean isContinue = true;
        do {
            test.test1();
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
