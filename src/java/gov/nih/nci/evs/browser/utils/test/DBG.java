package gov.nih.nci.evs.browser.utils.test;

import gov.nih.nci.evs.browser.utils.*;

import java.util.*;

public class DBG {
    private static boolean _isPerformanceTesting = false;
    private static boolean _displayDetails = false;
    private static boolean _displayTabDelimitedFormat = false;
    private static ArrayList<String> _tabList = new ArrayList<String>();

    public static void setPerformanceTesting(boolean on) {
        _isPerformanceTesting = on;
    }
    
    public static boolean isPerformanceTesting() {
        return _isPerformanceTesting;
    }

    public static void setDisplayDetails(boolean on) {
        _displayDetails = on;
    }
    
    public static boolean isDisplayDetails() {
        return _displayDetails;
    }
    
    public static void setDisplayTabDelimitedFormat(boolean on) {
        _displayTabDelimitedFormat = on;
    }
    
    public static boolean isDisplayTabDelimitedFormat() {
        return _displayTabDelimitedFormat;
    }

    public static void debug(String text) {
        if (_isPerformanceTesting)
            System.out.println(text);
    }

    public static void debug(boolean display, String text) {
        if (display)
            debug(text);
    }
    
    public static void debugDetails(String text) {
        if (_displayDetails)
            debug("  " + text);
    }
    
    public static void debugDetails(long timeMS, String text, 
        String additionalText) {
        if (_displayDetails) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("* " + text + ": ");
            buffer.append(Utils.timeToString(timeMS));
            if (additionalText != null && additionalText.length() > 0)
                buffer.append(" [" + additionalText+ "]");
            debugDetails(buffer.toString());
        }
        debugTabbedValue(text, Utils.timeInSec(timeMS));
    }
    
    public static void clearTabbbedValues() {
        _tabList = new ArrayList<String>();
    }
    
    public static void debugTabbedValue(int index, String name, String value) {
        if (! _displayTabDelimitedFormat || _tabList == null)
            return;

        String delimiter = "\t";
        delimiter = " | ";
        String text = "";
        
        if (name != null && name.length() > 0)
            text += name + ":" + delimiter;
        if (value != null && value.length() > 0)
            text += value + delimiter;
        
        if (index < 0)
            _tabList.add(text);
        else _tabList.add(index, text);
    }
    
    public static void debugTabbedValue(String name, String value) {
        debugTabbedValue(-1, name, value);
    }

    public static void displayTabbedValues() {
        if (! _displayTabDelimitedFormat || _tabList == null || _tabList.size() <= 0)
            return;

        StringBuffer buffer = new StringBuffer();
        Iterator<String> iterator = _tabList.iterator();
        while (iterator.hasNext()) {
            String value = iterator.next();
            buffer.append(value);
        }
        debug(buffer.toString());
    }
    
    public static void debugVector(String indent, String text, Vector<?> vector) {
        debug(indent + text);
        Object[] list = vector.toArray();
        for (int i=0; i<list.length; ++i)
            debug(indent + "  " + (i+1) + ") " + list[i].toString());
        debug(indent + "  * Total: " + list.length);
    }
}
