package gov.nih.nci.evs.browser.utils;

/**
 * This class allows us to turn off System.out.println message at
 * various places to help with the Performance testing.
 */
public class Debug {
    private static boolean _display = true;
    
    public static void setDisplay(boolean display) {
        _display = display;
    }
    
    public static boolean isDisplay() {
        return _display;
    }

    public static void println(String text) {
        if (_display)
            System.out.println(text);
    }
}
