package gov.nih.nci.evs.browser.utils.test;

import gov.nih.nci.evs.browser.utils.*;

public class DataUtilsTest extends DataUtils {
    public void testData() {
        
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
