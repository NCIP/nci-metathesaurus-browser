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

public class DisplayItem {
    private String _propertyName;
    private String _itemLabel;
    private String _url;
    private String _hyperlinkText;
    private boolean _isExternalCode;

    public DisplayItem() {
    }

    public DisplayItem(String propertyName, String itemLabel, String url,
        String hyperlinkText) {
        _propertyName = propertyName;
        _itemLabel = itemLabel;
        _url = url;
        _hyperlinkText = hyperlinkText;
        _isExternalCode = false;
    }

    public DisplayItem(String propertyName, String itemLabel, String url,
        String hyperlinkText, boolean isExternalCode) {
        _propertyName = propertyName;
        _itemLabel = itemLabel;
        _url = url;
        _hyperlinkText = hyperlinkText;
        _isExternalCode = isExternalCode;
    }

    public String getPropertyName() {
        return _propertyName;
    }

    public void setPropertyName(String propertyName) {
        _propertyName = propertyName;
    }

    public String getItemLabel() {
        return _itemLabel;
    }

    public void setItemLabel(String itemLabel) {
        _itemLabel = itemLabel;
    }

    public String getUrl() {
        return _url;
    }

    public void setUrl(String url) {
        _url = url;
    }

    public String getHyperlinkText() {
        return _hyperlinkText;
    }

    public void setHyperlinkText(String hyperlinkText) {
        _hyperlinkText = hyperlinkText;
    }

    public boolean getIsExternalCode() {
        return _isExternalCode;
    }

    public void setIsExternalCode(String isExternalCode) {
        _hyperlinkText = isExternalCode;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("DisplayItem ");
        sb.append("\n");
        sb.append("\tpropertyName: " + getPropertyName());
        sb.append("\n");
        sb.append("\titemLabel: " + getItemLabel());
        sb.append("\n");
        sb.append("\turl: " + getUrl());
        sb.append("\n");
        sb.append("\thyperlinkText: " + getHyperlinkText());
        sb.append("\n");
        sb.append("\tisExternalCode: " + getIsExternalCode());
        sb.append("\n");
        return sb.toString();
    }
}
