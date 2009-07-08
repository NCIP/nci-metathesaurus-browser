package gov.nih.nci.evs.browser.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Utils {
	public static final String SEPARATOR = 
		"----------------------------------------" +
		"----------------------------------------";
    private static DecimalFormat _doubleFormatter = new DecimalFormat("0.00");

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
        double timeSec = time/1000.0;
        double timeMin = timeSec/60.0;
        
        return "" + time + " ms, " + 
            _doubleFormatter.format(timeSec) + " sec, " + 
            _doubleFormatter.format(timeMin) + " min";
    }
    
    public static String timeInSec(long time) {
        double timeSec = time/1000.0;
        return _doubleFormatter.format(timeSec);
    }
    
    public static String[] toStrings(String value, String delimiter, 
        boolean includeDelimiter, boolean trim) {
        StringTokenizer tokenizer = new StringTokenizer(
            value, delimiter, includeDelimiter);
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
            System.out.println(msg);
        if (list == null)
            return;
        for (int i=0; i<list.length; ++i) {
            System.out.println("  " + (i+1) + ") " + list[i]);
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
        for (int i=0; i<list.length; ++i) {
            if (i>0)
                buffer.append(", ");
            buffer.append(list[i]);
        }
        buffer.append(" }");
        return buffer.toString();
    }
}