package gov.nih.nci.evs.browser.test;

import gov.nih.nci.evs.browser.test.lexevs.*;
import gov.nih.nci.evs.browser.test.performance.*;
import gov.nih.nci.evs.browser.test.utils.*;

public class Test extends TestBase {
    public Test(int choice, String[] args) {
        super(choice, args);
    }

    protected void displayOptions() {
        println(getClass().getSimpleName() + " Menu:");
        println(INDENT + "1) " + "LexEvsTest");
        println(INDENT + "2) " + "PerformanceTest");
    }
    
    protected boolean runOption(int choice, String[] args) {
        switch (choice) {
        case 1: LexEvsTest.main(args); return true;
        case 2: PerformanceTest.main(args); return true;
        default: return false;
        }
    }

    public static void main(String[] args) {
        new Test(0, args);
    }
}
