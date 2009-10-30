package gov.nih.nci.evs.browser.test.performance;

import gov.nih.nci.evs.browser.test.utils.*;
import gov.nih.nci.evs.browser.utils.*;

public class PerformanceTest extends TestBase {
    public PerformanceTest(int choice, String[] args) {
        super(choice, args);
    }
    
    protected int prompt(int choice) {
        println(Utils.SEPARATOR);
        println("Main Menu:");
        println(INDENT + "1) " + "SearchUtilsTest");
        println(INDENT + "2) " + "RelationshipTest");
        println(INDENT + "3) " + "RelationshipTest2");
        println(INDENT + "4) " + "BySourceTest");
        println(INDENT + "5) " + "BySourceTest2");
        println(INDENT + "0) " + "Quit");
        println(Utils.SEPARATOR);
        choice = Prompt.prompt("Choose", choice);
        return choice;
    }
    
    protected boolean run(int choice, String[] args) {
        boolean returnValue = true;
        try {
            switch (choice) {
                case 1: SearchUtilsTest.main(args); break;
                case 2: RelationshipTest.main(args); break;
                case 3: RelationshipTest2.main(args); break;
                case 4: BySourceTest.main(args); break;
                case 5: BySourceTest2.main(args); break;
                default: returnValue = false; break;
            }
            println("");
        } catch (Exception e) {
            println(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        return returnValue;
    }

    public static void main(String[] args) {
        new PerformanceTest(2, args);
    }    
}
