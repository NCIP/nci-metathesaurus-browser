package gov.nih.nci.evs.browser.test.lexevs;

import gov.nih.nci.evs.browser.test.utils.*;
import gov.nih.nci.evs.browser.utils.*;

public class LexEvsTest extends TestBase {
    public LexEvsTest(int choice, String[] args) {
        super(choice, args);
    }

    protected void displayOptions()
    {
        println(getClass().getSimpleName() + " Menu:");
        println(INDENT + "1) " + "RegExpTest");
        println(INDENT + "2) " + "ResolveConceptIteratorTest");
        println(INDENT + "3) " + "TestExtension");
    }
    
    protected boolean runOption(int choice, String[] args) {
        switch (choice) {
        case 1: RegExpTest.main(args); return true;
        case 2: ResolveConceptIteratorTest.main(args); return true;
        case 3: TestExtension.main(args); return true;
        default: return false;
        }
    }

    public static void main(String[] args) {
        new LexEvsTest(2, args);
    }
}
