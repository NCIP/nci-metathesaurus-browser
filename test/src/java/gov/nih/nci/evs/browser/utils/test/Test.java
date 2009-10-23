package gov.nih.nci.evs.browser.utils.test;

import gov.nih.nci.evs.browser.utils.*;

public class Test extends TestBase {
    public Test(int choice, String[] args) {
        super(choice, args);
    }

    protected int prompt(int choice) {
        println(Utils.SEPARATOR);
        println("Main Menu:");
        println(INDENT + "1) " + "RegExpTest");
        println(INDENT + "2) " + "ResolveConceptIteratorTest");
        println(INDENT + "3) " + "TestExtension");
        println(INDENT + "0) " + "Quit");
        println(Utils.SEPARATOR);
        choice = Prompt.prompt("Choose", choice);
        return choice;
    }
    
    protected boolean run(int choice, String[] args) {
        boolean returnValue = true;
        try {
            switch (choice) {
                case 1: RegExpTest.main(args); break;
                case 2: ResolveConceptIteratorTest.main(args); break;
                case 3: TestExtension.main(args); break;
                default: returnValue = false; break;
            }
            println("");
        } catch (Exception e) {
            println(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        return returnValue;
    }

    public static void main(String[] args) {
        new Test(3, args);
    }
}
