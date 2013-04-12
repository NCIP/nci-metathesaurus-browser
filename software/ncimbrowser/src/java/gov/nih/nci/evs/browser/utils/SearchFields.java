/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.utils;

import java.util.*;

/**
 * 
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class SearchFields {
    public enum Type {
        None, Simple, Name, Code, Property, Relationship
    };

    private String _key;
    private Type _type = Type.None;
    private Vector _schemes = null;
    private String _matchText;
    private String _searchTarget;
    private String _source;
    private String _matchAlgorithm;
    private int _maxReturn;
    private String _propertyType;
    private String _propertyName;
    private String _relSearchAssociation;
    private String _relSearchRela;

    private SearchFields() {
        _key = randomKey();
    }

    private void setBasic(Type type, Vector schemes, String matchText,
        String searchTarget, String source, String matchAlgorithm, int maxReturn) {
        setType(type);
        setSchemes(schemes);
        setMatchText(matchText);
        setSearchTarget(searchTarget);
        setSource(source);
        setMatchAlgorithm(matchAlgorithm);
        setMaxReturn(maxReturn);
    }

    public static SearchFields setSimple(Vector schemes, String matchText,
        String searchTarget, String source, String matchAlgorithm, int maxReturn) {
        SearchFields fields = new SearchFields();
        fields.setBasic(Type.Simple, schemes, matchText, searchTarget, source,
            matchAlgorithm, maxReturn);
        return fields;
    }

    public static SearchFields setName(Vector schemes, String matchText,
        String searchTarget, String source, String matchAlgorithm, int maxReturn) {
        SearchFields fields = new SearchFields();
        fields.setBasic(Type.Name, schemes, matchText, searchTarget, source,
            matchAlgorithm, maxReturn);
        return fields;
    }

    public static SearchFields setCode(Vector schemes, String matchText,
        String searchTarget, String source, String matchAlgorithm, int maxReturn) {
        SearchFields fields = new SearchFields();
        fields.setBasic(Type.Code, schemes, matchText, searchTarget, source,
            matchAlgorithm, maxReturn);
        return fields;
    }

    public static SearchFields setProperty(Vector schemes, String matchText,
        String searchTarget, String propertyType, String propertyName,
        String source, String matchAlgorithm, int maxReturn) {
        SearchFields fields = new SearchFields();
        fields.setBasic(Type.Property, schemes, matchText, searchTarget,
            source, matchAlgorithm, maxReturn);
        fields.setPropertyType(propertyType);
        fields.setPropertyName(propertyName);
        return fields;
    }

    public static SearchFields setRelationship(Vector schemes,
        String matchText, String searchTarget, String relSearchAssociation,
        String relSearchRela, String source, String matchAlgorithm,
        int maxReturn) {
        SearchFields fields = new SearchFields();
        fields.setBasic(Type.Relationship, schemes, matchText, searchTarget,
            source, matchAlgorithm, maxReturn);
        fields.setRelSearchAssociation(relSearchAssociation);
        fields.setRelSearchRela(relSearchRela);
        return fields;
    }

    private String randomKey() {
        Random random = new Random();
        int i = random.nextInt();
        String s = Integer.toString(i);
        return s;
    }

    public String getKey() {
        return _key;
    }

    public void setType(Type type) {
        _type = type;
    }

    public Type getType() {
        return _type;
    }

    public void setSchemes(Vector schemes) {
        _schemes = schemes;
    }

    public Vector getSchemes() {
        return _schemes;
    }

    public void setMatchText(String matchText) {
        _matchText = matchText.trim();
    }

    public String getMatchText() {
        return _matchText;
    }

    public void setMatchAlgorithm(String matchAlgorithm) {
        _matchAlgorithm = matchAlgorithm;
    }

    public String getMatchAlgorithm() {
        return _matchAlgorithm;
    }

    public void setSearchTarget(String searchTarget) {
        _searchTarget = searchTarget;
    }

    public String getSearchTarget() {
        return _searchTarget;
    }

    public void setSource(String source) {
        _source = source;
    }

    public String getSource() {
        return _source;
    }

    public void setMaxReturn(int maxReturn) {
        _maxReturn = maxReturn;
    }

    public int getMaxReturn() {
        return _maxReturn;
    }

    public void setPropertyType(String propertyType) {
        _propertyType = propertyType;
    }

    public String getPropertyType() {
        return _propertyType;
    }

    public void setPropertyName(String propertyName) {
        _propertyName = propertyName;
    }

    public String getPropertyName() {
        return _propertyName;
    }

    public void setRelSearchAssociation(String relSearchAssociation) {
        _relSearchAssociation = relSearchAssociation;
    }

    public String getRelSearchAssociation() {
        return _relSearchAssociation;
    }

    public void setRelSearchRela(String relSearchRela) {
        _relSearchRela = relSearchRela;
    }

    public String getRelSearchRela() {
        return _relSearchRela;
    }

    public String toString() {
        return "key=" + _key + ", type=" + _type + ", schemes= " + _schemes
            + ", matchText=" + _matchText + ", searchTarget=" + _searchTarget
            + ", source=" + _source + ", matchAlgorithm=" + _matchAlgorithm
            + ", maxReturn=" + _maxReturn + ", propertyType=" + _propertyType
            + ", propertyName=" + _propertyName + ", relSearchAssociation="
            + _relSearchAssociation + ", relSearchRela=" + _relSearchRela;
    }
}
