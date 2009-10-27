package gov.nih.nci.evs.browser.test;

import java.util.*;

public abstract class TestBase {
    protected static String INDENT = "  ";
    
    public TestBase(int choice, String[] args) {
        args = parse(args);
        if (choice > 0)
            run(choice, args);
        else prompt(args);
    }
    
    protected void println(String text) {
        System.out.println(text);
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
    
    private void prompt(String[] args) {
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
    
    protected abstract int prompt(int choice);
    
    protected abstract boolean run(int choice, String[] args);
}
