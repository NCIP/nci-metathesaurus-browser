package gov.nih.nci.evs.browser.utils.test;

import gov.nih.nci.evs.browser.utils.*;

public class DataUtilsTest extends DataUtils {
    private void testSearch() {
        
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

        DBG.setPerformanceTesting(true);
        DataUtilsTest test = new DataUtilsTest();
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
