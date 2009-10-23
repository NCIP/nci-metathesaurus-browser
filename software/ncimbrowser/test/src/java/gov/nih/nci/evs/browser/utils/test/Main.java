package gov.nih.nci.evs.browser.utils.test;

import java.util.*;

public class Main {
    private static String[] parse(String[] args) {
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
    
    public static void main(String[] args) throws Exception 
    {
        args = parse(args);
        //RegExpTest.main(args);
        ResolveConceptIteratorTest.main(args);
        //TestExtension.main(args);
    }
}
