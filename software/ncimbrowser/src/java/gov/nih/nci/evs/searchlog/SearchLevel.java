package gov.nih.nci.evs.searchlog;

import org.apache.log4j.Level;

public class SearchLevel extends Level {

  private static final long serialVersionUID = 1L;

  // High enough so only SL events get logged in the file
  public static final int SEARCH_LOG_INT = FATAL_INT + 1000;
  public static final SearchLevel SEARCH_LOG_LEVEL = new SearchLevel(SEARCH_LOG_INT, "SEARCH", 7);

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
