package gov.nih.nci.evs.browser.utils;

import java.text.*;
import java.util.*;

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

public class NCImUtils {
    private static Logger _logger = Logger.getLogger(NCImUtils.class);
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