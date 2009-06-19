package gov.nih.nci.evs.browser.utils.test;

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
            println(INDENT + "2) " + "DataUtilsTest");
            println(INDENT + "0) " + "Quit");
            println(SEPARATOR);
            i = Prompt.prompt("Choose", i);
            if (i == 0)
                break;
            try {
                switch (i) {
                case 1: SearchUtilsTest.main(args); break;
                case 2: DataUtilsTest.main(args); break;
                default: println("Invalid choice.  Try again."); break;
                }
                println("");
            } catch (Exception e) {
            }
        }
        println("Quit");
    }
    
    public static void main(String[] args) {
        //prompt(args);
        DataUtilsTest.main(args);
    }
}
