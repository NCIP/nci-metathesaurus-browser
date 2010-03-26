
package gov.nih.nci.evs.browser.bean;

import gov.nih.nci.evs.browser.utils.MailUtils;
import gov.nih.nci.evs.browser.utils.SearchUtils;
import gov.nih.nci.evs.browser.utils.Utils;

import gov.nih.nci.evs.browser.properties.NCImBrowserProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.LexGrid.concepts.Concept;

import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.browser.common.Constants;
import gov.nih.nci.evs.searchlog.SearchLog;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

/**
  * <!-- LICENSE_TEXT_START -->
* Copyright 2008,2009 NGIT. This software was developed in conjunction with the National Cancer Institute,
* and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
* in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
* materials provided with the distribution.
* 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
* "This product includes software developed by NGIT and the National Cancer Institute."
* If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
* wherever such third-party acknowledgments normally appear.
* 3. The names "The National Cancer Institute", "NCI" and "NGIT" must not be used to endorse or promote products derived from this software.
* 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
* the recipient to use any trademarks owned by either NCI or NGIT
* 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
* NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  * <!-- LICENSE_TEXT_END -->
  */

/**
  * @author EVS Team
  * @version 1.0
  *
  * Modification history
  *     Initial implementation kim.ong@ngc.com
  *
 */

/*
      <FORM NAME="searchOptions" METHOD="POST" CLASS="search-form">
		<h:selectOneMenu id="selectSearchOption"
		 value="#{searchStatusBean.selectedSearchOption}" >
		  <f:selectItems value="#{searchStatusBean.searchOptionList}" />
		</h:selectOneMenu>
      </form>
*/

public class SearchStatusBean extends Object
{
	private static Logger logger = Logger.getLogger(SearchStatusBean.class);

    private String selectedSearchOption = null;
    private List searchOptionList = null;

    public void setSelectedSearchOption(String selectedSearchOption) {
        this.selectedSearchOption = selectedSearchOption;
    }

    public String getSelectedSearchOption() {
        return this.selectedSearchOption;
    }

    public void searchOptionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null) return;
        String newValue = (String) event.getNewValue();
        setSelectedSearchOption(newValue);
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        request.getSession().setAttribute("AdvancedSearchOption", newValue);
    }

    public List getSearchOptionList() {
        searchOptionList = new ArrayList();
        searchOptionList.add(new SelectItem("Property"));
        searchOptionList.add(new SelectItem("Relationship"));
        return searchOptionList;
    }




//////////////////////////////////////////////////////////////////////////////////////////

    private String selectedProperty = null;
    private List propertyList = null;

    public void setSelectedProperty(String selectedProperty) {
        this.selectedProperty = selectedProperty;
    }

    public String getSelectedProperty() {
        return this.selectedProperty;
    }

    public void selectedPropertyChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null) return;
        String newValue = (String) event.getNewValue();

        System.out.println("propertyChanged; " + newValue);
        setSelectedProperty(newValue);
    }

    public List getPropertyList() {
		if (propertyList == null) {
			propertyList = OntologyBean.getPropertyNameList();
            if (propertyList != null && propertyList.size() > 0) {
                this.selectedProperty = ((SelectItem) propertyList.get(0)).getLabel();
            }
		}
        return propertyList;
    }



//////////////////////////////////////////////////////////////////////////////////////////
    private String selectedSource = null;
    private List sourceList = null;

    public void setSelectedSource(String selectedSource) {
        this.selectedSource = selectedSource;
    }

    public String getSelectedSource() {
        return this.selectedSource;
    }

    public void selectedSourceChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null) return;
        String newValue = (String) event.getNewValue();

        System.out.println("selectedSourceChanged; " + newValue);
        setSelectedSource(newValue);
    }

    public List getSourceList() {
		if (sourceList == null) {
			sourceList = OntologyBean.getSourceList();
            if (sourceList != null && sourceList.size() > 0) {
                this.selectedSource = ((SelectItem) sourceList.get(0)).getLabel();
            }
		}
        return sourceList;
    }


//////////////////////////////////////////////////////////////////////////////////////////
    private String selectedPropertyType = null;
    private List propertyTypeList = null;

    public void setSelectedPropertyType(String selectedPropertyType) {
        this.selectedPropertyType = selectedPropertyType;
    }

    public String getSelectedPropertyType() {
        return this.selectedPropertyType;
    }

    public void selectedPropertyTypeChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null) return;
        String newValue = (String) event.getNewValue();

        System.out.println("PropertyTypeChanged; " + newValue);
        setSelectedPropertyType(newValue);
    }

    public List getPropertyTypeList() {
		if (propertyTypeList == null) {
			propertyTypeList = OntologyBean.getPropertyTypeList();
            if (propertyTypeList != null && propertyTypeList.size() > 0) {
                this.selectedPropertyType = ((SelectItem) propertyTypeList.get(0)).getLabel();
            }
		}
        return propertyTypeList;
    }


//////////////////////////////////////////////////////////////////////////////////////////
    private String selectedAssociation = null;
    private List associationList = null;

    public void setSelectedAssociation(String selectedAssociation) {
        this.selectedAssociation = selectedAssociation;
    }

    public String getSelectedAssociation() {
        return this.selectedAssociation;
    }

    public void selectedAssociationChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null) return;
        String newValue = (String) event.getNewValue();

        System.out.println("selectedAssociationChanged; " + newValue);
        setSelectedAssociation(newValue);
    }

    public List getAssociationList() {
		if (associationList == null) {
			associationList = OntologyBean.getAssociationNameList();
            if (associationList != null && associationList.size() > 0) {
                this.selectedAssociation = ((SelectItem) associationList.get(0)).getLabel();
            }
		}
        return associationList;
    }


//////////////////////////////////////////////////////////////////////////////////////////

}

