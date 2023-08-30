package gov.nih.nci.evs.browser.bean;

import java.util.*;

import javax.faces.context.*;
import javax.faces.event.*;
import javax.faces.model.*;
import javax.servlet.http.*;

import org.apache.logging.log4j.*;
import gov.nih.nci.evs.browser.utils.*;

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
 * @author EVS Team
 * @version 1.0
 *
 *          Modification history Initial implementation kim.ong@ngc.com
 *
 */

/*
 * <FORM NAME="searchOptions" METHOD="POST" CLASS="search-form">
 * <h:selectOneMenu id="selectSearchOption"
 * value="#{searchStatusBean.selectedSearchOption}" > <f:selectItems
 * value="#{searchStatusBean.searchOptionList}" /> </h:selectOneMenu> </form>
 */

public class SearchStatusBean extends Object {
	private static Logger _logger = LogManager.getLogger(SearchStatusBean.class);

    public SearchStatusBean() {
    }

    public String setSessionAttribute(String attributeName, String value) {
        HttpServletRequest request = HTTPUtils.getRequest();
        // Note: Reuse previous value if null.
        if (value == null)
            value = (String) request.getSession().getAttribute(attributeName);
        request.getSession().setAttribute(attributeName, value);
        return value;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////

    private String _selectedSearchOption = "Property";
    private String _matchText = null;
    private List _searchOptionList = null;

    public void setSelectedSearchOption(String selectedSearchOption) {
        // selectedSearchOption = setSessionAttribute(
        // "advancedSearchOption", selectedSearchOption);

        _selectedSearchOption = selectedSearchOption;


        setSearchType(selectedSearchOption);

    }

    public String getSelectedSearchOption() {
        return _selectedSearchOption;
    }

    public void searchOptionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("searchOptionChanged to " + newValue);
        setSelectedSearchOption(newValue);
        HttpServletRequest request = HTTPUtils.getRequest();

        request.setAttribute("searchOptionChangedTo", newValue);

        Object bean_obj =
            FacesContext.getCurrentInstance().getExternalContext()
                .getRequestMap().get("searchStatusBean");
        SearchStatusBean bean = null;
        if (bean_obj == null) {
            bean = new SearchStatusBean();
            FacesContext.getCurrentInstance().getExternalContext()
                .getRequestMap().put("searchStatusBean", bean);

        } else {
            bean = (SearchStatusBean) bean_obj;
            bean.setAlgorithm(getAlgorithm());
        }
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
            .put("searchStatusBean", bean);

        // SearchStatusBean bean = (SearchStatusBean)
        // FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("searchStatusBean");

        // HttpServletRequest request =
        // (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

    }

    public List getSearchOptionList() {
        _searchOptionList = new ArrayList();
        _searchOptionList.add(new SelectItem("Property", "Property"));
        _searchOptionList.add(new SelectItem("Relationship", "Relationship"));
        return _searchOptionList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////

    private String _selectedProperty = "ALL";
    private List _propertyList = null;

    public void setSelectedProperty(String selectedProperty) {
        // selectedProperty = setSessionAttribute(
        // "advancedPropertyOption", selectedProperty);

        _selectedProperty = selectedProperty;
    }

    public String getSelectedProperty() {
        return _selectedProperty;
    }

    public void selectedPropertyChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedPropertyChanged to " + newValue);
        setSelectedProperty(newValue);
    }

    public List getPropertyList() {
        if (_propertyList == null) {
            _propertyList = OntologyBean.getPropertyNameList();
            if (_propertyList != null && _propertyList.size() > 0) {
                _selectedProperty =
                    ((SelectItem) _propertyList.get(0)).getLabel();
            }
        }
        return _propertyList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    private String _selectedSource = "ALL";
    private List _sourceList = null;

    public void setSelectedSource(String selectedSource,
        boolean updateUserSessionBean) {
        _selectedSource = selectedSource;

        if (updateUserSessionBean) {
            UserSessionBean bean = BeanUtils.getUserSessionBean();
            bean.setSelectedSource(selectedSource);
        }
    }

    public void setSelectedSource(String selectedSource) {
        setSelectedSource(selectedSource, true);
    }

    public String getSelectedSource() {
        return _selectedSource;
    }

    public void selectedSourceChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedSourceChanged to " + newValue);
        setSelectedSource(newValue);
    }

    public List getSourceList() {
        if (_sourceList == null) {
            _sourceList = OntologyBean.getSourceList();
            if (_sourceList != null && _sourceList.size() > 0) {
                _selectedSource =
                    ((SelectItem) _sourceList.get(0)).getLabel();
            }
        }
        return _sourceList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    private String _selectedPropertyType = null;
    private List _propertyTypeList = null;

    public void setSelectedPropertyType(String selectedPropertyType) {
        _selectedPropertyType = selectedPropertyType;
    }

    public String getSelectedPropertyType() {
        return _selectedPropertyType;
    }

    public void selectedPropertyTypeChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedPropertyTypeChanged to " + newValue);
        setSelectedPropertyType(newValue);
    }

    public List getPropertyTypeList() {
        if (_propertyTypeList == null) {
            _propertyTypeList = OntologyBean.getPropertyTypeList();
            if (_propertyTypeList != null && _propertyTypeList.size() > 0) {
                _selectedPropertyType =
                    ((SelectItem) _propertyTypeList.get(0)).getLabel();
            }
        }
        return _propertyTypeList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    private String _selectedAssociation = null;
    private List _associationList = null;

    public void setSelectedAssociation(String selectedAssociation) {
        _selectedAssociation = selectedAssociation;
    }

    public String getSelectedAssociation() {
        return _selectedAssociation;
    }

    public void selectedAssociationChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedAssociationChanged to " + newValue);
        setSelectedAssociation(newValue);
    }

    public List getAssociationList() {
        if (_associationList == null) {
            _associationList = OntologyBean.getAssociationNameList();
            if (_associationList != null && _associationList.size() > 0) {
                _selectedAssociation =
                    ((SelectItem) _associationList.get(0)).getLabel();
            }
        }
        return _associationList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    private String _selectedRelationship = "ALL";
    private List _relationshipList = null;

    public void setSelectedRelationship(String selectedRelationship) {
        _selectedRelationship =
            setSessionAttribute("advancedRelationOption", selectedRelationship);
    }

    public String getSelectedRelationship() {
        return _selectedRelationship;
    }

    public void selectedRelationshipChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedRelationshipChanged to " + newValue);
        setSelectedRelationship(newValue);
    }

    public List getRelationshipList() {
        if (_relationshipList == null) {
            _relationshipList = OntologyBean.getAssociationNameList();
            if (_relationshipList != null) {
				_relationshipList.add(0, new SelectItem("ALL", "ALL"));
				//if (_relationshipList != null && _relationshipList.size() > 0) {
				if (_relationshipList.size() > 0) {
					_selectedRelationship =
						((SelectItem) _relationshipList.get(0)).getLabel();
				}
		    }
        }
        return _relationshipList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    private String _selectedRELA = "";
    private List _RELAList = null;

    public void setSelectedRELA(String selectedRELA) {
        _selectedRELA =
            setSessionAttribute("advancedRELAOption", selectedRELA);
    }

    public String getSelectedRELA() {
        return _selectedRELA;
    }

    public void selectedRELAChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedRELAChanged to " + newValue);
        setSelectedRELA(newValue);
    }

    public List getRELAList() {
        if (_RELAList == null) {
            _RELAList = OntologyBean.getRELAList();
            if (_RELAList != null && _RELAList.size() > 0) {
                _selectedRELA = ((SelectItem) _RELAList.get(0)).getLabel();
            }
        }
        return _RELAList;
    }

    // //////////////////////////////////////////////////////////////////////////////////
    public void setMatchText(String t) {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("matchText", t);
        _matchText = t;

        _logger.debug("Set matchText to: " + t);
    }

    public HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance()
            .getExternalContext().getRequest();
    }

    public String getMatchText() {
        return _matchText;
    }

    private String _algorithm;

    public void setAlgorithm(String algorithm, boolean updateUserSessionBean) {
        _algorithm = algorithm;

        if (updateUserSessionBean) {
            UserSessionBean bean = BeanUtils.getUserSessionBean();
            bean.setSelectedAlgorithm(_algorithm);
        }
    }

    public void setAlgorithm(String algorithm) {
        setAlgorithm(algorithm, true);
    }

    public String getAlgorithm() {
        return _algorithm;
    }

    private String _searchType;

/*
    private String mapSearchTypeToSimpleSearch(String searchType) {
        if (searchType.toLowerCase(Locale.ENGLISH).startsWith("relationship"))
            return "relationships";
        else if (searchType.toLowerCase(Locale.ENGLISH).startsWith("propert"))
            return "properties";

        else if (searchType.toLowerCase(Locale.ENGLISH).startsWith("code"))
            return "codes";

        else return "names";
    }
*/
    public void setSearchType(String searchType, boolean updateUserSessionBean) {
        _searchType = searchType;

/*
        if (updateUserSessionBean) {
            String value = mapSearchTypeToSimpleSearch(searchType);
            UserSessionBean bean = BeanUtils.getUserSessionBean();
            bean.setSelectedSearchTarget(value);
        }
*/
    }

    public void setSearchType(String searchType) {
        setSearchType(searchType, true);
    }

    private String mapSearchTypeToAdvanceSearch(String searchType) {
        if (searchType.toLowerCase(Locale.ENGLISH).startsWith("relationship"))
            return "Relationship";
        else if ((searchType.toLowerCase(Locale.ENGLISH).startsWith("propert")))
            return "Property";
        else return "Name";
    }

    public void setSearchTarget(String searchTarget) {
        String value = mapSearchTypeToAdvanceSearch(searchTarget);
        _searchType = value;
    }

    public String getSearchType() {
        return _searchType;
    }

}
