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
        
        public StopWatch() {
            start();
        }
    
        public void start() {
            _startMS = System.currentTimeMillis();
        }
        
        public long duration() {
            return System.currentTimeMillis() - _startMS;
        }
        
        public String getResult() {
            long time = duration();
            double timeSec = time/1000.0;
            double timeMin = timeSec/60.0;
            
            return "Run time: " + time + " ms, " + 
                _doubleFormatter.format(timeSec) + " sec, " + 
                _doubleFormatter.format(timeMin) + " min";
        }
    }
    
    public static String[] toStrings(String value, String delimiter, 
        boolean includeDelimiter) {
        StringTokenizer tokenizer = new StringTokenizer(
            value, delimiter, includeDelimiter);
        ArrayList<String> list = new ArrayList<String>();
        while (tokenizer.hasMoreElements()) {
            String s = tokenizer.nextToken().trim();
            if (s.length() > 0)
                list.add(s);
        }
        return list.toArray(new String[list.size()]);
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
}