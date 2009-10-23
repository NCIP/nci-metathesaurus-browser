package gov.nih.nci.evs.browser.utils.test;

import java.util.*;
import gov.nih.nci.evs.browser.utils.*;

public class Main {
    private static String INDENT = "  ";
    
    private void println(String text) {
        System.out.println(text);
    }
    
    public Main(String[] args) {
        args = parse(args);
        prompt(args);
    }
    
    private String[] parse(String[] args) {
        String prevArg = "";
        ArrayList<String> newArgs = new ArrayList<String>();
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (arg.equals("-propertyFile")) {
                prevArg = arg;
            } else if (prevArg.equals("-propertyFile")) {
                System.setProperty(
                    "gov.nih.nci.evs.browser.NCImBrowserProperties", arg);
                prevArg = "";
            } else {
                newArgs.add(arg);
            }
        }
        return newArgs.toArray(new String[newArgs.size()]);
    }
    
    public void prompt(String[] args) {
        int choice = 1;
        while (true) {
            choice = prompt(choice);
            if (choice == 0)
                break;
            if (! run(choice, args))
                println("Invalid choice.  Try again.");
        }
        println("Quit");
    }
    
    private int prompt(int choice) {
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
    
    private boolean run(int choice, String[] args) {
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
        }
        return returnValue;
    }

    public static void main(String[] args) {
        new Main(args);
    }
}
