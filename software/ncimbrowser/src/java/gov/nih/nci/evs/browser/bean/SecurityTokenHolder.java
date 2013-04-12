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

public class SecurityTokenHolder {
    private String _name;
    private String _value;

    public SecurityTokenHolder() {

    }

    public SecurityTokenHolder(String name, String value) {
        _name = name;
        _value = value;
    }

    public String getName() {
        return _name;
    }

    public String getValue() {
        return _value;
    }

    public void setName(String name) {
        _name = name;
    }

    public void setValue(String value) {
        _value = value;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SecurityTokenHolder ");
        sb.append("\n");
        sb.append("\tname: " + _name);
        sb.append("\tvalue: " + _value);
        sb.append("\n");
        sb.append("\n");

        return sb.toString();
    }
}
