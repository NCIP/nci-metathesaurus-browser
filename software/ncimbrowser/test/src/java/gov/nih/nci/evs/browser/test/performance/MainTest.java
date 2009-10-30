package gov.nih.nci.evs.browser.test.performance;

public class MainTest {
    private static final String SEPARATOR = 
        "========================================" + 
        "========================================";
    private static String INDENT = "  ";
    
    private static void println(String text) {
        System.out.println(text);
    }
    
    public static void prompt(String[] args) {
        int i = 1;
        while (true) {
            println(SEPARATOR);
            println("Main Menu:");
            println(INDENT + "1) " + "SearchUtilsTest");
            println(INDENT + "2) " + "RelationshipTest");
            println(INDENT + "3) " + "RelationshipTest2");
            println(INDENT + "4) " + "BySourceTest");
            println(INDENT + "5) " + "BySourceTest2");
            println(INDENT + "0) " + "Quit");
            println(SEPARATOR);
            i = Prompt.prompt("Choose", i);
            if (i == 0)
                break;
            try {
                switch (i) {
                case 1: SearchUtilsTest.main(args); break;
                case 2: RelationshipTest.main(args); break;
                case 3: RelationshipTest2.main(args); break;
                case 4: BySourceTest.main(args); break;
                case 5: BySourceTest2.main(args); break;
                default: println("Invalid choice.  Try again."); break;
                }
                println("");
            } catch (Exception e) {
            }
        }
        println("Quit");
    }
    
    public static void main(String[] args) {
        int which = 5;
        
        switch (which) {
        case 0: prompt(args); break;
        case 1: SearchUtilsTest.main(args); break;
        case 2: RelationshipTest.main(args); break;
        case 3: RelationshipTest2.main(args); break;
        case 4: BySourceTest.main(args); break;
        case 5: BySourceTest2.main(args); break;
        }
    }
}
