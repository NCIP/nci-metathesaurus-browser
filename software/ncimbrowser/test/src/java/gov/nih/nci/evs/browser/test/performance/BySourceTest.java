package gov.nih.nci.evs.browser.test.performance;

import java.util.*;
import org.LexGrid.concepts.*;
import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.properties.*;
import gov.nih.nci.evs.browser.test.utils.*;
import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.browser.utils.test.*;

public class BySourceTest extends DataUtils {
    private int _runAmount = 1;
    private boolean _suppressOtherMessages = true;
    private boolean _displayParameters = false;
    private boolean _displayRelationships = false;
    private boolean _displayResults = true;
    private String _sab = "NCI";  // Source Abbreviation
    private String _sortBy = "cui";

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

    public void getNeighborhoodSynonymsTest(String scheme, String version, 
        String code, String sab, String sortBy) {
        DBG.clearTabbbedValues();
        Utils.StopWatch stopWatch = new Utils.StopWatch();
        Vector vector = getNeighborhoodSynonyms(scheme, version, code, sab);
        long duration = stopWatch.getDuration();
        stopWatch.start();
        DataUtils.sortSynonymData(vector, sortBy);
        long duration2 = stopWatch.getDuration();

        if (_displayRelationships && vector.size() > 0)
            DBG.debugVector("", "* List:", vector);

        Concept concept = getConceptByCode(scheme, version, null, code);
        String conceptName = "";
        if (concept != null)
            conceptName = concept.getEntityDescription().getContent();
        if (_displayResults) {
            DBG.debug("* Result: " + code + " (" + conceptName + ")");
            DBG.debug("  * sab: " + sab);
            DBG.debug("  * Hits: " + vector.size());
            DBG.debug("  * Run Time: " + stopWatch.getResult(duration));
            DBG.debug("  * Sort By: " + sortBy);
            DBG.debug("  * Run Time: " + stopWatch.getResult(duration2));
        }
        if (DBG.isDisplayTabDelimitedFormat()) {
            int i=0;
            DBG.debugTabbedValue(i++, "* Tabbed", "");
            DBG.debugTabbedValue(i++, "code", code);
            DBG.debugTabbedValue(i++, "sab", sab);
            DBG.debugTabbedValue(i++, "Hits", vector.size());
            DBG.debugTabbedValue(i++, "Run Time", stopWatch.formatInSec(duration));
            DBG.debugTabbedValue("Sort By", sortBy);
            DBG.debugTabbedValue("Run Time", stopWatch.formatInSec(duration2));
            DBG.debugTabbedValue("Concept name", conceptName);
            DBG.displayTabbedValues();
        }
    }

    private void prompt(String[] codes) {
        boolean isTrue = false;

        while (true) {
            DBG.debug(MyUtils.wrap(75, "      ", 
                "* codes: " + Utils.toString(codes)));
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
            _displayRelationships = Prompt.prompt("  * Display relationships", _displayRelationships);
            _displayResults = Prompt.prompt("  * Display results", _displayResults);
            isTrue = Prompt.prompt("  * Display tab delimited",
                DBG.isDisplayTabDelimitedFormat());
            DBG.setDisplayTabDelimitedFormat(isTrue);
            _sab = Prompt.prompt("  * Source", _sab);
            _sortBy = Prompt.prompt("  * Sort By", _sortBy);
            
            boolean changeSettings = false;
            changeSettings = Prompt.prompt("  # Change Settings", changeSettings);
            if (! changeSettings)
                break;
            DBG.debug("");
            DBG.debug(Utils.SEPARATOR);
        }
    }

    public void runTest() throws Exception {
        String scheme = Constants.CODING_SCHEME_NAME;
        String version = null;
        
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
         // "CL342077", // Cell
            "C1516728", // Common Terminology Criteria for Adverse Events
            "C0007097", // Carcinoma
            "C0021311", // Infection 
            "C0441471", // Event
            "C1306673", // Stage
            "C0699733", // Devices
            "C0033684", // Proteins
            "C1285092", // Gland
            "C0175677", // Injury
            "C0439793", // Severities (Worst Case)

            //Note: List from Brian Carlsen with >10K relationships
            "C0001688", // aspects of adverse effects
            "C0005768", // In Blood
            "C0007807", // In Cerebrospinal Fluid
            "C0008903", // Taxonomic
            "C0013182", // Drug Allergy
            "C0013557", // Economic
            "C0017399", // genetic aspects
            "C0019665", // Historical aspects qualifier
            "C0022189", // isolation & purification analysis
            "C0025520", // metabolic aspects
            "C0031843", // physiological aspects
            "C0034526", // aspects of radiation effects
            "C0038137", // standards characteristics
            "C0042037", // In Urine
            "C0079107", // chemical aspects
            "C0229671", // Serum
         // "C0439793", // Severities
            "C0441987", // Side
            "C0549179", // Priority
            "C0565958", // Episodicities
            "C0587266", // Surgical access values
            "C0750729", // Course
            "C0991511", // Injectable Solution
            "C0993159", // Oral tablet
            "C1522005", // immunology aspects
        };

//        codes = new String[] { "C0017636" };
//        codes = new String[] { "C0439793" }; // Severities (Worst Case)
        _sab = "SNOMEDCT";

        NCImBrowserProperties.getInstance();
        DBG.debug("* Setting(s):");
        DBG.debug("  * EVS_SERVICE_URL: " + NCImBrowserProperties
            .getProperty(NCImBrowserProperties.EVS_SERVICE_URL));
        DBG.debug("  * scheme: " + scheme);
        DBG.debug("  * version: " + version);
        DBG.debug("  * sab: " + _sab);
        DBG.debug("  * sortBy: " + _sortBy);
        prompt(codes);
        for (int i = 0; i < codes.length; ++i) {
            if (i >= _runAmount)
                break;
            String code = codes[i];
            if (DBG.isDisplayDetails()) {
                DBG.debug("");
                DBG.debug(Utils.SEPARATOR);
            }
            getNeighborhoodSynonymsTest(scheme, version, code, _sab, _sortBy);
        }
        DBG.debug("* Done");
    }

    public static void main(String[] args) {
        try {
            DBG.setPerformanceTesting(true);
            BySourceTest test = new BySourceTest();
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
