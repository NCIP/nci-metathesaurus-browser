/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.utils;

import java.text.*;
import java.util.*;

import org.apache.log4j.*;

/**
 * 
 */

public class Utils {
    private static Logger _logger = Logger.getLogger(Utils.class);
    public static final String SEPARATOR =
        "----------------------------------------"
            + "----------------------------------------";
    private static DecimalFormat _doubleFormatter = new DecimalFormat("0.00");

    public static Logger getJspLogger(String fileName) {
        String name = fileName;
        int i = fileName.lastIndexOf('.');
        if (i > 0)
            name = fileName.substring(0, i) + "_" + fileName.substring(i+1);
        return Logger.getLogger("gov.nih.nci.evs.browser.jsp." + name);
    }
    
    public static class StopWatch {
        private long _startMS = 0;
        private long _incrementMS = 0;

        public StopWatch() {
            start();
        }

        public static DecimalFormat getFormatter() {
            return _doubleFormatter;
        }

        public void reset() {
            _startMS = 0;
            _incrementMS = 0;
        }

        public void start() {
            _startMS = System.currentTimeMillis();
        }

        public void storeIncrement() {
            _incrementMS += getIncrement();
        }

        public long getIncrement() {
            return System.currentTimeMillis() - _startMS;
        }

        public long getDuration() {
            if (_incrementMS > 0)
                return _incrementMS;
            return getIncrement();
        }

        public String getResult(long time) {
            return timeToString(time);
        }

        public String getResult() {
            long time = getDuration();
            return getResult(time);
        }

        public String formatInSec(long time) {
            return timeInSec(time);
        }

        public String formatInSec() {
            long time = getDuration();
            return formatInSec(time);
        }
    }

    public static String timeToString(long time) {
        double timeSec = time / 1000.0;
        double timeMin = timeSec / 60.0;

        return "" + time + " ms, " + _doubleFormatter.format(timeSec)
            + " sec, " + _doubleFormatter.format(timeMin) + " min";
    }

    public static String timeInSec(long time) {
        double timeSec = time / 1000.0;
        return _doubleFormatter.format(timeSec);
    }

    public static String[] toStrings(String value, String delimiter,
        boolean includeDelimiter, boolean trim) {
        StringTokenizer tokenizer =
            new StringTokenizer(value, delimiter, includeDelimiter);
        ArrayList<String> list = new ArrayList<String>();
        while (tokenizer.hasMoreElements()) {
            String s = tokenizer.nextToken();
            if (trim)
                s = s.trim();
            if (s.length() > 0)
                list.add(s);
        }
        return list.toArray(new String[list.size()]);
    }

    public static String[] toStrings(String value, String delimiter,
        boolean includeDelimiter) {
        return toStrings(value, delimiter, includeDelimiter, true);
    }

    public static void debug(String msg, String[] list) {
        if (msg != null && msg.length() > 0)
            _logger.debug(msg);
        if (list == null)
            return;
        for (int i = 0; i < list.length; ++i) {
            _logger.debug("  " + (i + 1) + ") " + list[i]);
        }
    }

    public static String toHtml(String text) {
        text = text.replaceAll("\n", "<br/>");
        text = text.replaceAll("  ", "&nbsp;&nbsp;");
        return text;
    }

    public static String toString(String[] list) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        for (int i = 0; i < list.length; ++i) {
            if (i > 0)
                buffer.append(", ");
            buffer.append(list[i]);
        }
        buffer.append(" }");
        return buffer.toString();
    }
}