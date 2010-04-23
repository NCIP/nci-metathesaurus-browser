package gov.nih.nci.evs.browser.utils;

import java.util.*;

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
