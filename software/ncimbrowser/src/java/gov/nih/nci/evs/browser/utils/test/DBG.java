package gov.nih.nci.evs.browser.utils.test;

import java.util.*;
import gov.nih.nci.evs.browser.utils.*;
import org.apache.log4j.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction
 * with the National Cancer Institute, and so to the extent government
 * employees are co-authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the disclaimer of Article 3,
 *      below. Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions and the following
 *      disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution,
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not
 *      authorize the recipient to use any trademarks owned by either NCI
 *      or NGIT
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT,
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */


public class DBG {
    private static Logger _logger = Logger.getLogger(DBG.class);
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
            _logger.debug(text);
    }

    public static void debug(boolean display, String text) {
        if (display)
            debug(text);
    }

    public static void debugDetails(String text) {
        if (_displayDetails)
            debug("  " + text);
    }

    public static void debugDetails(String name, String value) {
        debugDetails("* " + name + " = " + value);
        debugTabbedValue(name, value);
    }

    public static void debugDetails(String name, int value) {
        debugDetails(name, Integer.toString(value));
    }

    public static void debugDetails(long timeMS, String text,
        String additionalText) {
        if (_displayDetails) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("* " + text + ": ");
            buffer.append(NCImUtils.timeToString(timeMS));
            if (additionalText != null && additionalText.length() > 0)
                buffer.append(" [" + additionalText + "]");
            debugDetails(buffer.toString());
        }
        debugTabbedValue(text, NCImUtils.timeInSec(timeMS));
    }

    public static void clearTabbbedValues() {
        _tabList = new ArrayList<String>();
    }

    public static void debugTabbedValue(int index, String name, String value) {
        if (!_displayTabDelimitedFormat || _tabList == null)
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
        else
            _tabList.add(index, text);
    }

    public static void debugTabbedValue(String name, String value) {
        debugTabbedValue(-1, name, value);
    }

    public static void debugTabbedValue(int index, String name, int value) {
        debugTabbedValue(index, name, Integer.toString(value));
    }

    public static void debugTabbedValue(String name, int value) {
        debugTabbedValue(-1, name, value);
    }

    public static void displayTabbedValues() {
        if (!_displayTabDelimitedFormat || _tabList == null
            || _tabList.size() <= 0)
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
        for (int i = 0; i < list.length; ++i)
            debug(indent + "  " + (i + 1) + ") " + list[i].toString());
        debug(indent + "  * Total: " + list.length);
    }
}
