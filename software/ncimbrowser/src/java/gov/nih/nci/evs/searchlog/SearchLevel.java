package gov.nih.nci.evs.searchlog;

import org.apache.logging.log4j.*;

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


public class SearchLevel {

    private static final long serialVersionUID = 1L;

    // High enough so only SL events get logged in the file
    public static final int FATAL_INT = 50000;
    public static final int SEARCH_LOG_INT = FATAL_INT + 1000;
    public static final SearchLevel SEARCH_LOG_LEVEL =
        new SearchLevel(SEARCH_LOG_INT, "SEARCH", 7);

    int level = 0;
    String levelStr = null;
    int syslogEquivalent = 0;

    public SearchLevel(int level, String levelStr, int syslogEquivalent) {
        //super(level, levelStr, syslogEquivalent);
        this.level = level;
        this.levelStr = levelStr;
        this.syslogEquivalent = syslogEquivalent;
    }

    public static SearchLevel toLevel(String sArg) {
        return SEARCH_LOG_LEVEL;
    }

    public static SearchLevel toLevel(int val) {
        return SEARCH_LOG_LEVEL;
    }

    public static SearchLevel toLevel(int val, Level defaultLevel) {
        return SEARCH_LOG_LEVEL;
    }

}


/*
public class SearchLevel extends Level {

    private static final long serialVersionUID = 1L;

    // High enough so only SL events get logged in the file
    public static final int SEARCH_LOG_INT = FATAL_INT + 1000;
    public static final SearchLevel SEARCH_LOG_LEVEL =
        new SearchLevel(SEARCH_LOG_INT, "SEARCH", 7);

    protected SearchLevel(int level, String levelStr, int syslogEquivalent) {
        super(level, levelStr, syslogEquivalent);
    }

    public static SearchLevel toLevel(String sArg) {
        return SEARCH_LOG_LEVEL;
    }

    public static SearchLevel toLevel(int val) {
        return SEARCH_LOG_LEVEL;
    }

    public static Level toLevel(int val, Level defaultLevel) {
        return SEARCH_LOG_LEVEL;
    }

}
*/

