package gov.nih.nci.evs.browser.test.utils;

import java.util.*;

public class MyUtils {
    public static String wrap(int maxCharInLine, String indentation, String text) {
        if (indentation == null)
            indentation = "";
        StringTokenizer tokenizer = new StringTokenizer(text, "\n", true);
        StringBuffer buffer = new StringBuffer();
        while (tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken();
            buffer.append(wrapOneLine(maxCharInLine, "\n" + indentation, line));
        }
        return buffer.toString();
    }

    public static String wrapOneLine(int maxCharInLine, String endOfLine,
        String text) {
        StringBuffer buffer = new StringBuffer();
        do {
            int start = 0, n = text.length();
            int end = start + maxCharInLine;
            if (end >= n)
                end = n;

            if (end < n && !Character.isWhitespace(text.charAt(end))) {
                String line = text.substring(start, end);
                int i = indexOfLastWhiteSpace(line);
                if (i > 0)
                    end = i;
            }

            String line = text.substring(start, end);
            if (buffer.length() > 0)
                buffer.append(endOfLine);
            buffer.append(line);
            text = text.substring(end).trim();
        } while (text.length() > 0);
        return buffer.toString();
    }

    public static int indexOfLastWhiteSpace(String text) {
        for (int i = text.length() - 1; i >= 0; --i) {
            char c = text.charAt(i);
            if (Character.isWhitespace(c))
                return i;
        }
        return -1;
    }
}
