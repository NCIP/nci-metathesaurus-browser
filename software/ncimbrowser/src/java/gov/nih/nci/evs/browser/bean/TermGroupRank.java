/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.bean;

/**
 * 
 */

/**
 * @author EVS Team
 * @version 1.0
 * 
 *          Modification history Initial implementation kim.ong@ngc.com
 * 
 */

public class TermGroupRank {
    private String _index;
    private String _source;
    private String _termGroup;

    public TermGroupRank() {
    }

    public TermGroupRank(String index, String source, String termGroup) {
        _index = index;
        _source = source;
        _termGroup = termGroup;
    }

    public String getIndex() {
        return _index;
    }

    public void setIndex(String index) {
        _index = index;
    }

    public String getSource() {
        return _source;
    }

    public void setSource(String source) {
        _source = source;
    }

    public String getTermGroup() {
        return _termGroup;
    }

    public void setTermGroup(String termGroup) {
        _termGroup = termGroup;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("TermGroupRank ");
        sb.append("\n");
        sb.append("\tindex: " + getIndex());
        sb.append("\n");
        sb.append("\tsource: " + getSource());
        sb.append("\n");
        sb.append("\ttermGroup: " + getTermGroup());
        sb.append("\n");
        return sb.toString();
    }
}
