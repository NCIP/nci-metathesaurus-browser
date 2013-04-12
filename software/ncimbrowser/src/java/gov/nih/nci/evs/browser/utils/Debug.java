/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.utils;

import org.apache.log4j.*;

/**
 * 
 */

/**
 * This class allows us to turn off logger message at various places to help
 * with the Performance testing.
 */
public class Debug {
    private static Logger _logger = Logger.getLogger(Debug.class);
    private static boolean _display = true;

    public static void setDisplay(boolean display) {
        _display = display;
    }

    public static boolean isDisplay() {
        return _display;
    }

    public static void println(String text) {
        if (_display)
            _logger.debug(text);
    }
}
