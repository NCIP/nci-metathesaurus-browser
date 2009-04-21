
package gov.nih.nci.evs.browser.bean;

import java.io.File;

import gov.nih.nci.evs.browser.utils.MailUtils;
import gov.nih.nci.evs.browser.utils.SortUtils;
import gov.nih.nci.evs.browser.utils.SearchUtils;
import gov.nih.nci.evs.browser.utils.Utils;

import gov.nih.nci.evs.browser.properties.NCImBrowserProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.HashSet;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import java.util.Collection;

import org.LexGrid.concepts.Concept;

import gov.nih.nci.evs.browser.properties.NCImBrowserProperties;
import gov.nih.nci.evs.browser.utils.*;

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

public class UserSessionBean extends Object
{
    private static Logger KLO_log = Logger.getLogger("UserSessionBean KLO");

    private String selectedQuickLink = null;
    private List quickLinkList = null;

    public void setSelectedQuickLink(String selectedQuickLink) {
        this.selectedQuickLink = selectedQuickLink;
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        request.getSession().setAttribute("selectedQuickLink", selectedQuickLink);
    }

    public String getSelectedQuickLink() {
        return this.selectedQuickLink;
    }


    public void quickLinkChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null) return;
        String newValue = (String) event.getNewValue();

        System.out.println("quickLinkChanged; " + newValue);
        setSelectedQuickLink(newValue);

        HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();

        String targetURL = null;//"http://nciterms.nci.nih.gov/";
        if (selectedQuickLink.compareTo("NCI Terminology Browser") == 0) {
           targetURL = "http://nciterms.nci.nih.gov/";
        }
        try {
            response.sendRedirect(response.encodeRedirectURL(targetURL));
        } catch (Exception ex) {
            ex.printStackTrace();
            // send error message
        }

    }



    public List getQuickLinkList() {
        quickLinkList = new ArrayList();
        quickLinkList.add(new SelectItem("Quick Links"));
        quickLinkList.add(new SelectItem("NCI Terminology Browser"));
        quickLinkList.add(new SelectItem("NCI MetaThesaurus"));
        quickLinkList.add(new SelectItem("EVS Home"));
        quickLinkList.add(new SelectItem("NCI Terminology Resources"));
        return quickLinkList;
    }


    public String searchAction() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

        String matchText = (String) request.getParameter("matchText");
        matchText = matchText.trim();
        //[#19965] Error message is not displayed when Search Criteria is not proivded
        if (matchText.length() == 0)
        {
            String message = "Please enter a search string.";
            request.getSession().setAttribute("message", message);
            return "message";
        }
        request.getSession().setAttribute("matchText", matchText);

        String matchAlgorithm = (String) request.getParameter("algorithm");
        setSelectedAlgorithm(matchAlgorithm);


        String matchtype = (String) request.getParameter("matchtype");
        String source = (String) request.getParameter("source");

        System.out.println("\n*** criteria: " + matchText);
        System.out.println("*** matchType: " + matchtype);
        System.out.println("*** Source: " + source);

        String scheme = "NCI MetaThesaurus";
        String version = null;
        String max_str = null;
        int maxToReturn = -1;//1000;
        try {
            max_str = NCImBrowserProperties.getInstance().getProperty(NCImBrowserProperties.MAXIMUM_RETURN);
            maxToReturn = Integer.parseInt(max_str);
        } catch (Exception ex) {

        }
        Utils.StopWatch stopWatch = new Utils.StopWatch();
        Vector<org.LexGrid.concepts.Concept> v = null;
        if (matchtype.compareToIgnoreCase("CUI") == 0) {
			//Concept c = DataUtils.getConceptByCode(scheme, version, null, matchText);
			Concept c = DataUtils.getConceptByCode(scheme, version, null, matchText, source);
			if (c != null) {
				v = new Vector<org.LexGrid.concepts.Concept>();
				v.add(c);
			}
		}
		else if (matchtype.compareToIgnoreCase("Code") == 0) { // source code
			boolean searchInactive = true;
            v = new SearchUtils().findConceptWithSourceCodeMatching(scheme, version,
												   source, matchText,
												   maxToReturn, searchInactive);
		} else { // String
            //v = new SearchUtils().searchByName(scheme, version, matchText, matchAlgorithm, maxToReturn);
            v = new SearchUtils().searchByName(scheme, version, matchText, source, matchAlgorithm, maxToReturn);
		}

        request.getSession().setAttribute("vocabulary", scheme);
        request.getSession().setAttribute("matchtype", matchtype);
        request.getSession().setAttribute("source", source);

        setSelectedMatchType(matchtype);
        setSelectedSource(source);

        if (v != null && v.size() > 1)
        {
            request.getSession().setAttribute("search_results", v);
            String match_size = Integer.toString(v.size());
            request.getSession().setAttribute("match_size", match_size);
            request.getSession().setAttribute("page_string", "1");
            request.getSession().setAttribute("selectedResultsPerPage", "50");
            return "search_results";
        }
        else if (v != null && v.size() == 1)
        {
            request.getSession().setAttribute("singleton", "true");
            request.getSession().setAttribute("dictionary", "NCI MetaThesaurus");
            Concept c = (Concept) v.elementAt(0);
            request.getSession().setAttribute("code", c.getEntityCode());
            return "concept_details";
        }
        String message = "No match found.";
        request.getSession().setAttribute("message", message);
        return "message";

    }



    private String selectedResultsPerPage = null;
    private List resultsPerPageList = null;

    public List getResultsPerPageList() {
        resultsPerPageList = new ArrayList();
        resultsPerPageList.add(new SelectItem("10"));
        resultsPerPageList.add(new SelectItem("25"));
        resultsPerPageList.add(new SelectItem("50"));
        resultsPerPageList.add(new SelectItem("75"));
        resultsPerPageList.add(new SelectItem("100"));
        resultsPerPageList.add(new SelectItem("250"));
        resultsPerPageList.add(new SelectItem("500"));

        selectedResultsPerPage = ((SelectItem) resultsPerPageList.get(2)).getLabel(); // default to 50
        return resultsPerPageList;
    }

    public void setSelectedResultsPerPage(String selectedResultsPerPage) {
        this.selectedResultsPerPage = selectedResultsPerPage;
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        request.getSession().setAttribute("selectedResultsPerPage", selectedResultsPerPage);
    }

    public String getSelectedResultsPerPage() {
        return this.selectedResultsPerPage;
    }


    public void resultsPerPageChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null) return;
        String newValue = (String) event.getNewValue();

        System.out.println("resultsPerPageChanged; " + newValue);
        setSelectedResultsPerPage(newValue);

    }



    public String linkAction() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
/*
        String link = (String) request.getParameter("link");
        if (link.compareTo("NCI Terminology Browser") == 0)
        {
            return "nci_terminology_browser";
        }
        return "message";
*/
        return "";
    }

    private String selectedAlgorithm = null;
    private List algorithmList = null;

    public List getAlgorithmList() {
        algorithmList = new ArrayList();
        algorithmList.add(new SelectItem("exactMatch", "exactMatch"));
        algorithmList.add(new SelectItem("startsWith", "Begins With"));
        algorithmList.add(new SelectItem("contains", "Contains"));
        selectedAlgorithm = ((SelectItem) algorithmList.get(0)).getLabel();
        return algorithmList;
    }

    public void algorithmChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null) return;
        String newValue = (String) event.getNewValue();

        //System.out.println("algorithmChanged; " + newValue);
        setSelectedAlgorithm(newValue);
    }

    public void setSelectedAlgorithm(String selectedAlgorithm) {
        this.selectedAlgorithm = selectedAlgorithm;
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        request.getSession().setAttribute("selectedAlgorithm", selectedAlgorithm);
    }

    public String getSelectedAlgorithm() {
        return this.selectedAlgorithm;
    }

    public String contactUs() throws Exception {
        String msg = "Your message was successfully sent.";
        HttpServletRequest request = (HttpServletRequest) FacesContext
            .getCurrentInstance().getExternalContext().getRequest();

        try {
            String subject = request.getParameter("subject");
            String message = request.getParameter("message");
            String from = request.getParameter("emailaddress");
            String recipients[] = MailUtils.getRecipients();
            MailUtils.postMail(from, recipients, subject, message);
        } catch (Exception e) {
            msg = e.getMessage();
            request.setAttribute("errorMsg", Utils.toHtml(msg));
            return "error";
        }

        request.getSession().setAttribute("message", Utils.toHtml(msg));
        return "message";
    }


    ////////////////////////////////////////////////////////////////
    // source
    ////////////////////////////////////////////////////////////////

	private String selectedSource = null;
	private List sourceList = null;
	private Vector<String> sourceListData = null;


	public List getSourceList() {
		String codingSchemeName = "NCI MetaThesaurus";
		String version = null;
		sourceListData = DataUtils.getSourceListData(codingSchemeName, version);
		sourceList = new ArrayList();
		if (sourceListData == null) return sourceList;

		for (int i=0; i<sourceListData.size(); i++) {
			String t = (String) sourceListData.elementAt(i);
			sourceList.add(new SelectItem(t));
		}
		if (sourceList != null && sourceList.size() > 0) {
			selectedSource = ((SelectItem) sourceList.get(0)).getLabel();
		}
		return sourceList;
	}

	public void setSelectedSource(String selectedSource) {
		this.selectedSource = selectedSource;
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		request.getSession().setAttribute("selectedSource", selectedSource);
	}


	public String getSelectedSource() {
		return this.selectedSource;
	}

	public void sourceSelectionChanged(ValueChangeEvent event) {
		if (event.getNewValue() == null) return;
		//int id = Integer.parseInt((String) event.getNewValue());
		setSelectedSource(selectedSource);
	}

	//////////////////////////////////////////////////////////////////////

	private String selectedMatchType = null;
	private List matchTypeList = null;
	private Vector<String> matchTypeListData = null;


	public List getMatchTypeList() {
		String codingSchemeName = "NCI MetaThesaurus";
		String version = null;
		matchTypeListData = DataUtils.getMatchTypeListData(codingSchemeName, version);
		matchTypeList = new ArrayList();
		for (int i=0; i<matchTypeListData.size(); i++) {
			String t = (String) matchTypeListData.elementAt(i);
			matchTypeList.add(new SelectItem(t));
		}
		if (matchTypeList != null && matchTypeList.size() > 0) {
			selectedMatchType = ((SelectItem) matchTypeList.get(0)).getLabel();
		}
		return matchTypeList;
	}

	public void setSelectedMatchType(String selectedMatchType) {
		this.selectedMatchType = selectedMatchType;
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		request.getSession().setAttribute("selectedMatchType", selectedMatchType);
	}


	public String getSelectedMatchType() {
		return this.selectedMatchType;
	}

	public void matchTypeSelectionChanged(ValueChangeEvent event) {
		if (event.getNewValue() == null) return;
		//int id = Integer.parseInt((String) event.getNewValue());
		setSelectedMatchType(selectedMatchType);
	}




}
