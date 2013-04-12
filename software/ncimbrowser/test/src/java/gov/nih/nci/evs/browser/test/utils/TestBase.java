/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.test.utils;

import gov.nih.nci.evs.browser.utils.*;
import java.util.*;

import org.apache.log4j.Logger;

public abstract class TestBase {
    private static Logger _logger = Logger.getLogger(TestBase.class);
    private static final String PROPERTY_FILE = 
        "gov.nih.nci.evs.browser.NCImBrowserProperties";
    private static String DEFAULT_PROPERTY_FILE =
        "C:/apps/evs/ncim-webapp/conf/NCImBrowserProperties.xml";
    protected static String INDENT = "  ";
    
    public TestBase(int choice, String[] args) {
        System.setProperty(PROPERTY_FILE, DEFAULT_PROPERTY_FILE);
        args = parse(args);
        if (choice > 0)
            run(choice, args);
        else prompt(args);
    }
    
    protected void println(String text) {
        _logger.debug(text);
    }
    
    private String[] parse(String[] args) {
        String prevArg = "";
        ArrayList<String> newArgs = new ArrayList<String>();
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (arg.equals("-propertyFile")) {
                prevArg = arg;
            } else if (prevArg.equals("-propertyFile")) {
                System.setProperty(PROPERTY_FILE, arg);
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
    
    private int prompt(int choice) {
        println(Utils.SEPARATOR);
        displayOptions();
        println(INDENT + "0) " + "Quit");
        println(Utils.SEPARATOR);
        choice = Prompt.prompt("Choose", choice);
        return choice;
    }
    
    protected abstract void displayOptions();

    private boolean run(int choice, String[] args) {
        boolean returnValue = true;
        try {
            returnValue = runOption(choice, args);
            println("");
        } catch (Exception e) {
            println(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        return returnValue;
    }
    
    protected abstract boolean runOption(int choice, String[] args)
        throws Exception;
}
