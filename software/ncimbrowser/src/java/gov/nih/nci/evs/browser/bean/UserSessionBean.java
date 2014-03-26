package gov.nih.nci.evs.browser.bean;

import java.util.*;
import javax.faces.context.*;
import javax.faces.event.*;
import javax.faces.model.*;
import javax.servlet.http.*;

import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.concepts.Entity;

import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.browser.properties.*;
import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.searchlog.*;
import org.apache.log4j.*;

import nl.captcha.Captcha;
import nl.captcha.audio.AudioCaptcha;

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

public class UserSessionBean extends Object {
    private static Logger _logger = Logger.getLogger(UserSessionBean.class);

    private String _selectedQuickLink = null;
    private List _quickLinkList = null;

    public void setSelectedQuickLink(String selectedQuickLink) {
        _selectedQuickLink = selectedQuickLink;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("selectedQuickLink",
            selectedQuickLink);
    }

    public String getSelectedQuickLink() {
        return _selectedQuickLink;
    }

    public void quickLinkChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("quickLinkChanged; " + newValue);
        setSelectedQuickLink(newValue);

        HttpServletResponse response =
            (HttpServletResponse) FacesContext.getCurrentInstance()
                .getExternalContext().getResponse();

        String targetURL = null;// "http://nciterms.nci.nih.gov/";
        if (_selectedQuickLink.compareTo("NCI Terminology Browser") == 0) {
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
        _quickLinkList = new ArrayList();
        _quickLinkList.add(new SelectItem("Quick Links"));
        _quickLinkList.add(new SelectItem("NCI Terminology Browser"));
        _quickLinkList.add(new SelectItem("EVS Home"));
        _quickLinkList.add(new SelectItem("NCI Terminology Resources"));
        return _quickLinkList;
    }

    public String advancedSearchAction() {
        ResolvedConceptReferencesIteratorWrapper wrapper = null;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        SearchStatusBean bean =
            (SearchStatusBean) FacesContext.getCurrentInstance()
                .getExternalContext().getRequestMap().get("searchStatusBean");

        if (bean == null) {
            bean = new SearchStatusBean();
            request.setAttribute("searchStatusBean", bean);
        }

        String matchType =
            //HTTPUtils.cleanXSS((String) request.getParameter("adv_search_type"));
            HTTPUtils.cleanXSS((String) request.getParameter("selectSearchOption"));

        bean.setSearchType(matchType);

        String matchAlgorithm =
            HTTPUtils.cleanXSS((String) request.getParameter("adv_search_algorithm"));
        bean.setAlgorithm(matchAlgorithm);

        String source = HTTPUtils.cleanXSS((String) request.getParameter("adv_search_source"));

        if (source == null) {
            //GForge #28784 If a single source is selected, make it the default source selection in the By Source tab
            request.getSession().removeAttribute("selectedSource");
        } else {
			request.getSession().setAttribute("selectedSource", source);
		}

        bean.setSelectedSource(source);

/*
        String selectSearchOption =
            HTTPUtils.cleanXSS((String) request.getParameter("selectSearchOption"));
        bean.setSelectedSearchOption(selectSearchOption);
*/

        String selectProperty = HTTPUtils.cleanXSS((String) request.getParameter("selectProperty"));
        bean.setSelectedProperty(selectProperty);

        String rel_search_association =
            HTTPUtils.cleanXSS((String) request.getParameter("rel_search_association"));
        bean.setSelectedAssociation(rel_search_association);

        String rel_search_rela =
            HTTPUtils.cleanXSS((String) request.getParameter("rel_search_rela"));
        bean.setSelectedRELA(rel_search_rela);


        //request.setAttribute("searchStatusBean", bean);

        String searchTarget = HTTPUtils.cleanXSS((String) request.getParameter("searchTarget"));

        String matchText = HTTPUtils.cleanXSS((String)  request.getParameter("matchText"));
        if (matchText == null || matchText.length() == 0) {
            String message = "Please enter a search string.";
            // request.getSession().setAttribute("message", message);
            request.setAttribute("message", message);
            return "message";
        }
        matchText = matchText.trim();
        bean.setMatchText(matchText);


FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("searchStatusBean", bean);


request.getSession().setAttribute("searchStatusBean", bean);


        if (NCImBrowserProperties.get_debugOn()) {
            _logger.debug(Utils.SEPARATOR);
            _logger.debug("* criteria: " + matchText);
            _logger.debug("* source: " + source);
        }

        String scheme = Constants.CODING_SCHEME_NAME;
        Vector schemes = new Vector();
        schemes.add(scheme);

        String version = null;
        String max_str = null;
        int maxToReturn = -1;// 1000;
        try {
            max_str =
                NCImBrowserProperties.getInstance().getProperty(
                    NCImBrowserProperties.MAXIMUM_RETURN);
            maxToReturn = Integer.parseInt(max_str);
        } catch (Exception ex) {
        }
        //Utils.StopWatch stopWatch = new Utils.StopWatch();
        Vector<Entity> v = null;

        boolean excludeDesignation = true;
        boolean designationOnly = false;

        // check if this search has been performance previously through
        // IteratorBeanManager
        IteratorBeanManager iteratorBeanManager =
            (IteratorBeanManager) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap()
                .get("iteratorBeanManager");

        if (iteratorBeanManager == null) {
            iteratorBeanManager = new IteratorBeanManager();
            FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap()
                .put("iteratorBeanManager", iteratorBeanManager);
        }

        IteratorBean iteratorBean = null;
        ResolvedConceptReferencesIterator iterator = null;
        boolean ranking = true;

        SearchFields searchFields = null;
        String key = null;

        String searchType = HTTPUtils.cleanXSS((String) request.getParameter("selectSearchOption"));
        _logger.debug("UserSessionBean.java searchType: " + searchType);

        if (searchType != null && searchType.compareTo("Property") == 0) {

            String property_type =
                HTTPUtils.cleanXSS((String) request.getParameter("selectPropertyType"));
            if (property_type != null && property_type.compareTo("ALL") == 0) {
                property_type = null;
            }

            String property_name = selectProperty;
            if (property_name != null) {
                property_name = property_name.trim();
                if (property_name.compareTo("ALL") == 0)
                    property_name = null;
            }

            searchFields =
                SearchFields.setProperty(schemes, matchText, searchTarget,
                    property_type, property_name, source, matchAlgorithm,
                    maxToReturn);
            key = searchFields.getKey();
            if (iteratorBeanManager.containsIteratorBean(key)) {
                iteratorBean = iteratorBeanManager.getIteratorBean(key);
                iterator = iteratorBean.getIterator();
            } else {
                String[] property_types = null;
                if (property_type != null)
                    property_types = new String[] { property_type };
                String[] property_names = null;
                if (property_name != null)
                    property_names = new String[] { property_name };
                excludeDesignation = false;
                wrapper =
                    new SearchUtils().searchByProperties(scheme, version,
                        matchText, property_types, property_names, source,
                        matchAlgorithm, excludeDesignation, ranking,
                        maxToReturn);
                if (wrapper != null) {
                    iterator = wrapper.getIterator();
                }
                if (iterator != null) {
                    iteratorBean = new IteratorBean(iterator);
                    iteratorBean.setKey(key);
                    iteratorBean.setMatchText(matchText);
                    iteratorBeanManager.addIteratorBean(iteratorBean);
                }
            }

        } else if (searchType != null
            && searchType.compareTo("Relationship") == 0) {

			matchText = matchText.trim();

            if (rel_search_association != null
                && rel_search_association.compareTo("ALL") == 0) {
                rel_search_association = null;
			}

            if (rel_search_rela != null) {
				if (matchText.indexOf(" ") == -1 && matchAlgorithm.compareTo("contains") == 0) {
					String msg = Constants.USE_MORE_SPECIFIC_SEARCH_CRITERIA;
					request.setAttribute("message", msg);
					return "message";
				}
                rel_search_rela = rel_search_rela.trim();
                if (rel_search_rela.length() == 0) {
                    rel_search_rela = null;
				}
            }

            int search_direction = Constants.SEARCH_SOURCE;

            searchFields =
                SearchFields.setRelationship(schemes, matchText, searchTarget,
                    rel_search_association, rel_search_rela, source,
                    matchAlgorithm, maxToReturn);
            key = searchFields.getKey();

            if (iteratorBeanManager.containsIteratorBean(key)) {
                iteratorBean = iteratorBeanManager.getIteratorBean(key);
                iterator = iteratorBean.getIterator();
            } else {

                String[] associationsToNavigate = null;
                String[] association_qualifier_names = null;
                String[] association_qualifier_values = null;

                String inverse_rel_search_association = rel_search_association;
                if (rel_search_association != null) {
					inverse_rel_search_association = DataUtils.getAssociationReverseName(rel_search_association);
                    associationsToNavigate =
                        //new String[] { rel_search_association };
                        new String[] { inverse_rel_search_association };
                } else {
                    _logger.debug("rel_search_association == null");
                }

                if (rel_search_rela != null) {
                    association_qualifier_names = new String[] { "rela" };
                    association_qualifier_values =
                        new String[] { rel_search_rela };

                    if (associationsToNavigate == null) {
                        Vector w = OntologyBean.getAssociationNames();
                        if (w == null || w.size() == 0) {
                            _logger
                                .warn("OntologyBean.getAssociationNames() returns null, or nothing???");
                        } else {
                            associationsToNavigate = new String[w.size()];
                            for (int i = 0; i < w.size(); i++) {
                                String nm = (String) w.elementAt(i);
                                associationsToNavigate[i] = nm;
                            }
                        }
                    }

                } else {
                    _logger.warn("(*) qualifiers == null");
                }

				wrapper = new SearchUtils().searchByRELA(scheme,
					version, matchText, source, matchAlgorithm,
					inverse_rel_search_association, rel_search_rela, maxToReturn);

                if (wrapper == null) {
					_logger.debug("searchByAssociations");
					wrapper =
						new SearchUtils().searchByAssociations(scheme, version,
							matchText, associationsToNavigate,
							association_qualifier_names,
							association_qualifier_values, search_direction, source,
							matchAlgorithm, excludeDesignation, ranking,
							maxToReturn);
				}

                if (wrapper != null) {
                    iterator = wrapper.getIterator();
                }
                if (iterator != null) {
                    iteratorBean = new IteratorBean(iterator);
                    iteratorBean.setKey(key);
                    iteratorBean.setMatchText(matchText);
                    iteratorBeanManager.addIteratorBean(iteratorBean);
                }
            }
        } else if (searchType != null && searchType.compareTo("Name") == 0) {

            searchFields =
                SearchFields.setName(schemes, matchText, searchTarget, source,
                    matchAlgorithm, maxToReturn);


            key = searchFields.getKey();
            if (iteratorBeanManager.containsIteratorBean(key)) {
                iteratorBean = iteratorBeanManager.getIteratorBean(key);
                iterator = iteratorBean.getIterator();
            } else {

				if (SimpleSearchUtils.searchAllSources(source) && SimpleSearchUtils.isSimpleSearchSupported(matchAlgorithm, SimpleSearchUtils.NAMES)) {
					try {
						wrapper = new SimpleSearchUtils().search(scheme, version, matchText, SimpleSearchUtils.BY_NAME, matchAlgorithm);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					wrapper =
						new SearchUtils().searchByName(scheme, version, matchText,
							source, matchAlgorithm, ranking, maxToReturn,
							SearchUtils.NameSearchType.Name);
				}

                if (wrapper != null) {
                    iterator = wrapper.getIterator();
                    if (iterator != null) {
                        iteratorBean = new IteratorBean(iterator);
                        iteratorBean.setKey(key);
                        iteratorBean.setMatchText(matchText);
                        iteratorBeanManager.addIteratorBean(iteratorBean);

                        request.getSession().setAttribute("key", key);
                    }
                }
            }

        } else if (searchType != null && searchType.compareTo("Code") == 0) {
            searchFields =
                SearchFields.setCode(schemes, matchText, searchTarget, source,
                    matchAlgorithm, maxToReturn);
            key = searchFields.getKey();
            if (iteratorBeanManager.containsIteratorBean(key)) {
                iteratorBean = iteratorBeanManager.getIteratorBean(key);
                iterator = iteratorBean.getIterator();
            } else {
                /*
                 * wrapper = new SearchUtils().searchByName(scheme, version,
                 * matchText, source, matchAlgorithm, ranking, maxToReturn,
                 * SearchUtils.NameSearchType.Code);
                 */
			    schemes = new Vector();
			    Vector versions = new Vector();
			    schemes.add(scheme);
			    versions.add(version);

                wrapper = new CodeSearchUtils().searchByCode(schemes, versions, matchText, source, matchAlgorithm, ranking, maxToReturn, false);

                if (wrapper != null) {
                    iterator = wrapper.getIterator();
                    if (iterator != null) {
                        iteratorBean = new IteratorBean(iterator);
                        iteratorBean.setKey(key);

                        iteratorBean.setMatchText(matchText);


                        iteratorBeanManager.addIteratorBean(iteratorBean);

                        request.getSession().setAttribute("key", key);
                    }
                }
            }
        }

        request.getSession().setAttribute("key", key);
        request.getSession().setAttribute("matchText", matchText);
        request.getSession().removeAttribute("neighborhood_synonyms");
        request.getSession().removeAttribute("neighborhood_atoms");
        request.getSession().removeAttribute("concept");
        request.getSession().removeAttribute("code");
        request.getSession().removeAttribute("codeInNCI");
        request.getSession().removeAttribute("AssociationTargetHashMap");
        request.getSession().removeAttribute("type");

        if (iterator != null) {

//FacesContext.getCurrentInstance().getExternalContext().getRequestMap().set("searchStatusBean", bean);

            int size = iteratorBean.getSize();
            if (size < 0) {
				int num_iteration = (-1) * size;
				int matchingConceptCount = 0;
				try {
					SearchByAssociationIteratorDecorator assoIterator = (SearchByAssociationIteratorDecorator) iterator;
					matchingConceptCount = assoIterator.getQuickIteratorSize();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				String msg =
					"WARNING: No match found, but only able to test the first " + num_iteration + " out of " + matchingConceptCount + " matching target concepts. "
					+ " Consider using a more specific search or a different relationship.";

				request.setAttribute("message", msg);
				return "message";
			}

            _logger.debug("AdvancedSearchActon size: " + size);

            // Write a search log entry
            SearchLog.writeEntry(searchFields, size, HTTPUtils
                .getRefererParmDecode(request));

            if (size > 1) {
                request.getSession().setAttribute("search_results", v);
                String match_size = Integer.toString(size);
                request.getSession().setAttribute("match_size", match_size);
                request.getSession().setAttribute("page_string", "1");
                request.getSession().setAttribute("new_search", Boolean.TRUE);
                return "search_results";
            } else if (size == 1) {
                request.getSession().setAttribute("singleton", "true");
                request.getSession().setAttribute("dictionary",
                    Constants.CODING_SCHEME_NAME);
                int pageNumber = 1;
                List list = iteratorBean.getData(1);
                ResolvedConceptReference ref =
                    (ResolvedConceptReference) list.get(0);

                Entity c = null;
                if (ref == null) {
                    String msg =
                        "Error: Null ResolvedConceptReference encountered.";
                    request.getSession().setAttribute("message", msg);
                    return "message";

                } else {
                    c = ref.getReferencedEntry();
                    if (c == null) {
                        c =
                            DataUtils.getConceptByCode(
                                Constants.CODING_SCHEME_NAME, null, null, ref
                                    .getConceptCode());
                    }
                }

                request.getSession().setAttribute("code", ref.getConceptCode());
                request.getSession().setAttribute("concept", c);
                request.getSession().setAttribute("type", "properties");
                request.getSession().setAttribute("new_search", Boolean.TRUE);

HttpServletResponse response =
	(HttpServletResponse) FacesContext.getCurrentInstance()
		.getExternalContext().getResponse();
response.setContentType("text/html;charset=utf-8");

                return "concept_details";
            }
        }

        // [#23463] Linking retired concept to corresponding new concept
        // Test case: C0536142|200601|SY|||C1433544|Y|

        if (searchType == null || (searchType.compareTo("Relationship") != 0 && searchType.compareTo("Property") != 0)) {

            String newCUI = HistoryUtils.getReferencedCUI(matchText);

            if (newCUI != null) {
                _logger.debug("Searching for " + newCUI);
                Entity c =
                    DataUtils.getConceptByCode(Constants.CODING_SCHEME_NAME,
                        null, null, newCUI);
                request.getSession().setAttribute("code", newCUI);
                request.getSession().setAttribute("concept", c);
                request.getSession().setAttribute("type", "properties");

                request.getSession().setAttribute("new_search", Boolean.TRUE);
                request.getSession().setAttribute("retired_cui", matchText);

HttpServletResponse response =
	(HttpServletResponse) FacesContext.getCurrentInstance()
		.getExternalContext().getResponse();
response.setContentType("text/html;charset=utf-8");

                return "concept_details";
            }
        }

        String message = "No match found.";
        if (matchAlgorithm.compareTo("exactMatch") == 0 && searchType.compareTo("Relationship") != 0) {
            message = Constants.ERROR_NO_MATCH_FOUND_TRY_OTHER_ALGORITHMS;
        }
        request.setAttribute("message", message);
        return "no_match";

    }

    public String searchAction() {
        ResolvedConceptReferencesIteratorWrapper wrapper = null;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        String matchText = HTTPUtils.cleanXSS((String) request.getParameter("matchText"));
        _logger.debug("matchText: " + matchText);

        if (matchText != null) {
            matchText = matchText.trim();
            request.getSession().setAttribute("matchText", matchText);

        }

        // [#19965] Error message is not displayed when Search Criteria is not
        // proivded
        if (matchText == null || matchText.length() == 0) {
            String message = "Please enter a search string.";
            request.getSession().setAttribute("message", message);
            return "message";
        }

        String matchAlgorithm = HTTPUtils.cleanXSS((String) request.getParameter("algorithm"));

        request.getSession().setAttribute("selectedAlgorithm", matchAlgorithm);
        //setSelectedAlgorithm(matchAlgorithm);

        String searchTarget = HTTPUtils.cleanXSS((String) request.getParameter("searchTarget"));
        if (searchTarget == null || searchTarget.length() == 0) {
            String message = "Please specify a search target.";
            request.getSession().setAttribute("message", message);
            return "message";
        } else {
			request.getSession().setAttribute("searchTarget", searchTarget);
		}

        //setSelectedSearchTarget(searchTarget);



        // Remove ranking check box (KLO, 092409)
        // String rankingStr = (String) request.getParameter("ranking");
        // boolean ranking = rankingStr != null && rankingStr.equals("on");
        // request.getSession().setAttribute("ranking",
        // Boolean.toString(ranking));

		String source = (String) request.getSession().getAttribute("selectedSource");

        if (source == null) {
            source = "ALL";
            //GForge #28784 If a single source is selected, make it the default source selection in the By Source tab
            request.getSession().removeAttribute("selectedSource");
        } else {
			request.getSession().setAttribute("selectedSource", source);
		}

        if (NCImBrowserProperties.get_debugOn()) {
            try {
                _logger.debug(Utils.SEPARATOR);
                _logger.debug("* criteria: " + matchText);
                _logger.debug("* source: " + source);
            } catch (Exception e) {
				e.printStackTrace();
            }
        }
        String scheme = Constants.CODING_SCHEME_NAME;
        String version = null;
        String max_str = null;
        int maxToReturn = -1;// 1000;
        try {
            max_str =
                NCImBrowserProperties.getInstance().getProperty(
                    NCImBrowserProperties.MAXIMUM_RETURN);
            maxToReturn = Integer.parseInt(max_str);
        } catch (Exception ex) {

        }
        //Utils.StopWatch stopWatch = new Utils.StopWatch();
        Vector<org.LexGrid.concepts.Concept> v = null;

        boolean excludeDesignation = true;
        boolean designationOnly = false;

        // check if this search has been performance previously through
        // IteratorBeanManager
        IteratorBeanManager iteratorBeanManager =
            (IteratorBeanManager) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap()
                .get("iteratorBeanManager");

        if (iteratorBeanManager == null) {
            iteratorBeanManager = new IteratorBeanManager();
            FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap()
                .put("iteratorBeanManager", iteratorBeanManager);
        }

        IteratorBean iteratorBean = null;
        ResolvedConceptReferencesIterator iterator = null;
        Vector schemes = new Vector();
        schemes.add(scheme);

        Vector versions = new Vector();
        versions.add(version);

        boolean ranking = true;


        SearchFields searchFields = null;
        String key = null;

		searchFields =
			SearchFields.setSimple(schemes, matchText, searchTarget,
				source, matchAlgorithm, maxToReturn);
		key = searchFields.getKey();

        if (searchTarget.compareTo("names") == 0) {
            if (iteratorBeanManager.containsIteratorBean(key)) {
                iteratorBean = iteratorBeanManager.getIteratorBean(key);
                iterator = iteratorBean.getIterator();
            } else {
                //ResolvedConceptReferencesIteratorWrapper wrapper = null;
                try {
					boolean isSimpleSearchSupported = SimpleSearchUtils.isSimpleSearchSupported(matchAlgorithm, SimpleSearchUtils.NAMES);
					if (SimpleSearchUtils.searchAllSources(source) && SimpleSearchUtils.isSimpleSearchSupported(matchAlgorithm, SimpleSearchUtils.NAMES)) {
						wrapper = new SimpleSearchUtils().search(schemes, versions, matchText, SimpleSearchUtils.BY_NAME, matchAlgorithm);
				    } else {
						wrapper = new SearchUtils()
							.searchByNameOrCode(schemes, versions, matchText, source,
								matchAlgorithm, ranking, maxToReturn, SearchUtils.SEARCH_BY_NAME_ONLY);
					}
				} catch (Exception ex) {
                    ex.printStackTrace();
				}

                if (wrapper != null) {
                    iterator = wrapper.getIterator();

                    if (iterator != null) {
                        iteratorBean = new IteratorBean(iterator);
                        iteratorBean.setKey(key);
                        iteratorBean.setMatchText(matchText);
                        iteratorBeanManager.addIteratorBean(iteratorBean);
                    }
                }
            }

        } else if (searchTarget.compareTo("codes") == 0) {
            if (iteratorBeanManager.containsIteratorBean(key)) {
                iteratorBean = iteratorBeanManager.getIteratorBean(key);
                iterator = iteratorBean.getIterator();
            } else {
                //ResolvedConceptReferencesIteratorWrapper wrapper = null;
                try {
					wrapper = new CodeSearchUtils().searchByCode(
						schemes, versions, matchText,
						source, matchAlgorithm, ranking, maxToReturn, false);

				} catch (Exception ex) {
                    //ex.printStackTrace();
				}

                if (wrapper != null) {
                    iterator = wrapper.getIterator();

                    if (iterator != null) {
                        iteratorBean = new IteratorBean(iterator);
                        iteratorBean.setKey(key);
                        iteratorBean.setMatchText(matchText);
                        iteratorBeanManager.addIteratorBean(iteratorBean);
                    }
                }
            }

		} else if (searchTarget.compareTo("properties") == 0) {
			if (iteratorBeanManager.containsIteratorBean(key)) {
				iteratorBean = iteratorBeanManager.getIteratorBean(key);
				iterator = iteratorBean.getIterator();
			} else {
				wrapper =
					new SearchUtils().searchByProperties(scheme, version,
						matchText, source, matchAlgorithm,
						excludeDesignation, ranking, maxToReturn);
				if (wrapper != null) {
					iterator = wrapper.getIterator();
					if (iterator != null) {
						iteratorBean = new IteratorBean(iterator);
						iteratorBean.setKey(key);
						iteratorBean.setMatchText(matchText);
						iteratorBeanManager.addIteratorBean(iteratorBean);
					}
				}
			}

		} else if (searchTarget.compareTo("relationships") == 0) {

			if (matchText.indexOf(" ") == -1 && matchAlgorithm.compareTo("contains") == 0) {
				String msg = Constants.USE_MORE_SPECIFIC_SEARCH_CRITERIA;
				request.getSession().setAttribute("message", msg);
				return "message";
			}


			designationOnly = true;
			if (iteratorBeanManager.containsIteratorBean(key)) {
				iteratorBean = iteratorBeanManager.getIteratorBean(key);
				iterator = iteratorBean.getIterator();

			} else {
				//[GF#28946] Contains relationship results possibly missing some concepts
				//KLO, 062310
				wrapper = new SearchUtils().searchByRELA(scheme,
					version, matchText, source, matchAlgorithm,
					null, null, maxToReturn);

				if (wrapper == null) {
					wrapper =
						new SearchUtils().searchByAssociations(scheme, version,
							matchText, source, matchAlgorithm, designationOnly,
							ranking, maxToReturn);
				}

				if (wrapper != null) {
					iterator = wrapper.getIterator();
					if (iterator != null) {
						iteratorBean = new IteratorBean(iterator);
						iteratorBean.setKey(key);
						iteratorBean.setMatchText(matchText);
						iteratorBeanManager.addIteratorBean(iteratorBean);
					}
				}
			}
		}

        request.getSession().setAttribute("key", key);
        request.getSession().setAttribute("vocabulary", scheme);
        request.getSession().setAttribute("matchAlgorithm", matchAlgorithm);
        request.getSession().setAttribute("matchText", matchText);
        request.getSession().removeAttribute("neighborhood_synonyms");
        request.getSession().removeAttribute("neighborhood_atoms");
        request.getSession().removeAttribute("concept");
        request.getSession().removeAttribute("code");
        request.getSession().removeAttribute("codeInNCI");
        request.getSession().removeAttribute("AssociationTargetHashMap");
        request.getSession().removeAttribute("type");

        if (iterator != null) {





            int size = iteratorBean.getSize();

            // Write a search log entry
            SearchLog.writeEntry(searchFields, size, HTTPUtils
                .getRefererParmDecode(request));

            if (size > 1) {
                request.getSession().setAttribute("search_results", v);

                String match_size = Integer.toString(size);
                request.getSession().setAttribute("match_size", match_size);
                request.getSession().setAttribute("page_string", "1");
                request.getSession().setAttribute("new_search", Boolean.TRUE);
                return "search_results";
            } else if (size == 1) {
                request.getSession().setAttribute("singleton", "true");
                request.getSession().setAttribute("dictionary",
                    Constants.CODING_SCHEME_NAME);
                int pageNumber = 1;
                List list = iteratorBean.getData(1);
                ResolvedConceptReference ref =
                    (ResolvedConceptReference) list.get(0);

                Entity c = null;
                if (ref == null) {
                    String msg =
                        "Error: Null ResolvedConceptReference encountered.";
                    request.getSession().setAttribute("message", msg);
                    return "message";

                } else {
                    c = ref.getReferencedEntry();
                    if (c == null) {
                        c =
                            DataUtils.getConceptByCode(
                                Constants.CODING_SCHEME_NAME, null, null, ref
                                    .getConceptCode());
                    }
                }

                request.getSession().setAttribute("code", ref.getConceptCode());
                request.getSession().setAttribute("concept", c);
                request.getSession().setAttribute("type", "properties");

                request.getSession().setAttribute("new_search", Boolean.TRUE);

HttpServletResponse response =
	(HttpServletResponse) FacesContext.getCurrentInstance()
		.getExternalContext().getResponse();
response.setContentType("text/html;charset=utf-8");







                return "concept_details";
            }
        }

        // [#23463] Linking retired concept to corresponding new concept
        // Test case: C0536142|200601|SY|||C1433544|Y|
        // [#28861] Searching for "retired" or "redirected" concept codes with Contains or Begins With fails

        //if (searchTarget == null || (searchTarget.compareToIgnoreCase("Relationship") != 0 && searchTarget.compareToIgnoreCase("Property") != 0)) {
        if (searchTarget.compareToIgnoreCase("Relationship") != 0 && searchTarget.compareToIgnoreCase("Property") != 0) {

            String newCUI = HistoryUtils.getReferencedCUI(matchText);

            if (newCUI != null) {
                _logger.debug("Searching for " + newCUI);
                Entity c =
                    DataUtils.getConceptByCode(Constants.CODING_SCHEME_NAME,
                        null, null, newCUI);
                request.getSession().setAttribute("code", newCUI);
                request.getSession().setAttribute("concept", c);
                request.getSession().setAttribute("type", "properties");

                request.getSession().setAttribute("new_search", Boolean.TRUE);
                request.getSession().setAttribute("retired_cui", matchText);

HttpServletResponse response =
	(HttpServletResponse) FacesContext.getCurrentInstance()
		.getExternalContext().getResponse();
response.setContentType("text/html;charset=utf-8");

                return "concept_details";
            }
        }

        String message = "No match found.";
        if (matchAlgorithm.compareTo("exactMatch") == 0) {
            message = Constants.ERROR_NO_MATCH_FOUND_TRY_OTHER_ALGORITHMS;
        }
        request.getSession().setAttribute("message", message);
        return "message";
    }

    private String _selectedResultsPerPage = null;
    private List _resultsPerPageList = null;

    public List getResultsPerPageList() {
        _resultsPerPageList = new ArrayList();
        _resultsPerPageList.add(new SelectItem("10","10"));
        _resultsPerPageList.add(new SelectItem("25","25"));
        _resultsPerPageList.add(new SelectItem("50","50"));
        _resultsPerPageList.add(new SelectItem("75","75"));
        _resultsPerPageList.add(new SelectItem("100","100"));
        _resultsPerPageList.add(new SelectItem("250","250"));
        _resultsPerPageList.add(new SelectItem("500","500"));

        _selectedResultsPerPage = getSelectedResultsPerPage();

        return _resultsPerPageList;
    }

    public void setSelectedResultsPerPage(String selectedResultsPerPage) {

    	if (selectedResultsPerPage == null)
        	_selectedResultsPerPage = String.valueOf(Constants.DEFAULT_PAGE_SIZE);
    	else
    		_selectedResultsPerPage = selectedResultsPerPage;

        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("selectedResultsPerPage",
            selectedResultsPerPage);

    }

    public String getSelectedResultsPerPage() {

    	if (_selectedResultsPerPage == null || _selectedResultsPerPage.length() < 1)
    		_selectedResultsPerPage = String.valueOf(Constants.DEFAULT_PAGE_SIZE);

        return _selectedResultsPerPage;
    }

    public void resultsPerPageChanged(ValueChangeEvent event) {

        if (event.getNewValue() == null) {
            return;
        }
        String newValue = (String) event.getNewValue();
        setSelectedResultsPerPage(newValue);
    }

    public String linkAction() {
		/*
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        */
        return "";
    }

    private String _selectedAlgorithm = null;
    private List _algorithmList = null;

    public List getAlgorithmList() {
        _algorithmList = new ArrayList();
        _algorithmList.add(new SelectItem("exactMatch", "exactMatch"));
        _algorithmList.add(new SelectItem("startsWith", "Begins With"));
        _algorithmList.add(new SelectItem("contains", "Contains"));
        _selectedAlgorithm = ((SelectItem) _algorithmList.get(0)).getLabel();
        return _algorithmList;
    }

    public void algorithmChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedAlgorithm(newValue);
    }

    public void setSelectedAlgorithm(String selectedAlgorithm) {
        _selectedAlgorithm = selectedAlgorithm;
        /* KLO 032614
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("selectedAlgorithm",
            selectedAlgorithm);
        */

        SearchStatusBean bean = BeanUtils.getSearchStatusBean();
        bean.setAlgorithm(_selectedAlgorithm, false);
    }

    public String getSelectedAlgorithm() {
        return _selectedAlgorithm;
    }

    public void setSelectedSearchTarget(String searchTarget) {
		/*
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("searchTarget", searchTarget);
        */

        SearchStatusBean bean = BeanUtils.getSearchStatusBean();
        bean.setSearchTarget(searchTarget);
    }

    // //////////////////////////////////////////////////////////////
    // source
    // //////////////////////////////////////////////////////////////

    private String _selectedSource = "ALL";
    private List _sourceList = null;
    private Vector<String> _sourceListData = null;

    public List getSourceList() {
        if (_sourceList != null)
            return _sourceList;
        //tring codingSchemeName = Constants.CODING_SCHEME_NAME;
        //String version = null;
        //_sourceListData = DataUtils.getSourceListData(codingSchemeName, version);
        _sourceListData = DataUtils.getSourceListData();
        _sourceList = new ArrayList();
        if (_sourceListData != null) {
            for (int i = 0; i < _sourceListData.size(); i++) {
                String t = (String) _sourceListData.elementAt(i);
                _sourceList.add(new SelectItem(t));
            }
        }
        return _sourceList;
    }

    public void setSelectedSource(String selectedSource) {
        if (selectedSource == null)
            return;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("selectedSource", selectedSource);

        _selectedSource = selectedSource;
        SearchStatusBean bean = BeanUtils.getSearchStatusBean();
        bean.setSelectedSource(_selectedSource, false);
    }

    public String getSelectedSource() {
        if (_selectedSource == null) {
            _sourceList = getSourceList();
            if (_sourceList != null && _sourceList.size() > 0) {
                _selectedSource =
                    ((SelectItem) _sourceList.get(0)).getLabel();
            }
        }

        return _selectedSource;
    }

    public void sourceSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            String source = (String) event.getNewValue();
            setSelectedSource(source);
        }

         HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

         String value = HTTPUtils.cleanXSS((String) request.getParameter("matchText"));
         if (value != null)
         	request.getSession().setAttribute("matchText", value);

         value = HTTPUtils.cleanXSS((String) request.getParameter("algorithm"));
         if (value != null)
             setSelectedAlgorithm(value);

         value = HTTPUtils.cleanXSS((String) request.getParameter("searchTarget"));
         if (value != null)
             setSelectedSearchTarget(value);

         String prev_type = (String) request.getSession().getAttribute("prev_type");
         request.getSession().setAttribute("type", prev_type);
         request.getSession().removeAttribute("prev_type");

    }

    // //////////////////////////////////////////////////////////////
    // concept sources
    // //////////////////////////////////////////////////////////////

    private String _selectedConceptSource = null;
    private List _conceptSourceList = null;
    private Vector<String> _conceptSourceListData = null;

    public List getConceptSourceList() {
        String codingSchemeName = Constants.CODING_SCHEME_NAME;
        String version = null;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        String code = (String) request.getSession().getAttribute("code");
        _conceptSourceListData =
            DataUtils.getSources(codingSchemeName, version, null, code);
        _conceptSourceList = new ArrayList();
        /*
        if (_conceptSourceListData == null)
            return _conceptSourceList;
        */
        for (int i = 0; i < _conceptSourceListData.size(); i++) {
            String t = (String) _conceptSourceListData.elementAt(i);
            _conceptSourceList.add(new SelectItem(t));
        }
        return _conceptSourceList;
    }

    public void setSelectedConceptSource(String selectedConceptSource) {
        _selectedConceptSource = selectedConceptSource;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("selectedConceptSource",
            selectedConceptSource);
    }

    public String getSelectedConceptSource() {
        if (_selectedConceptSource == null) {
            _conceptSourceList = getConceptSourceList();
            if (_conceptSourceList != null && _conceptSourceList.size() > 0) {
                _selectedConceptSource =
                    ((SelectItem) _conceptSourceList.get(0)).getLabel();
            }
        }
        return _selectedConceptSource;
    }

    public void conceptSourceSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().removeAttribute("neighborhood_synonyms");
        request.getSession().removeAttribute("neighborhood_atoms");
        String source = (String) event.getNewValue();
        setSelectedConceptSource(source);
    }

    public String viewNeighborhoodAction() {
		/*
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        //String sab = getSelectedConceptSource();
        //String code = (String) request.getParameter("code");
        */
        return "neighborhood";

    }

    public String acceptLicenseAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        String dictionary = HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
        dictionary = dictionary.replaceAll("&#40;", "\\(").replaceAll("&#41;", "\\)");

        String code = HTTPUtils.cleanXSS((String) request.getParameter("code"));
        String sab = HTTPUtils.cleanXSS((String) request.getParameter("sab"));

        if (dictionary != null && code != null) {
            LicenseBean licenseBean =
                (LicenseBean) request.getSession().getAttribute("licenseBean");
            if (licenseBean == null) {
                licenseBean = new LicenseBean();
            }

            licenseBean.addLicenseAgreement(dictionary);

            request.getSession().setAttribute("licenseBean", licenseBean);

            request.getSession().setAttribute("term_browser_dictionary",
                dictionary);
            request.getSession().setAttribute("term_source_code", code);

            if (sab != null) {
                request.getSession().setAttribute("term_source", sab);
            }
            return "redirect";
        } else {
            String message = "Unidentifiable vocabulary name, or code";
            request.getSession().setAttribute("warning", message);
            return "message";
        }
    }


/////////////////////////////////////////////////////////////////////////////
// CAPTCHA
/////////////////////////////////////////////////////////////////////////////

    public String regenerateCaptchaImage() throws Exception {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
			String answer = HTTPUtils.cleanXSS((String) request.getParameter("answer"));
            String subject = HTTPUtils.cleanXSS((String) request.getParameter("subject"));
            String message = HTTPUtils.cleanXSS((String) request.getParameter("message"));
            String from    = HTTPUtils.cleanXSS((String) request.getParameter("emailaddress"));

		request.getSession().setAttribute("answer", answer);
		request.getSession().setAttribute("subject", subject);
		request.getSession().setAttribute("message", message);
		request.getSession().setAttribute("emailaddress", from);

		request.getSession().setAttribute("retry", "true");

        return "retry";
	}

	public boolean isNull(String s) {
		if (s == null) return true;
		s = s.trim();
		if (s.compareTo("") == 0) return true;
		return false;
	}


	public String switchCaptchaMode() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

		String answer = HTTPUtils.cleanXSS((String) request.getParameter("answer"));
		String subject = HTTPUtils.cleanXSS((String) request.getParameter("subject"));
		String message = HTTPUtils.cleanXSS((String) request.getParameter("message"));
		String from    = HTTPUtils.cleanXSS((String) request.getParameter("emailaddress"));

		request.getSession().setAttribute("answer", answer);
		request.getSession().setAttribute("subject", subject);
		request.getSession().setAttribute("message", message);
		request.getSession().setAttribute("emailaddress", from);

        String captcha_option = HTTPUtils.cleanXSS((String) request.getParameter("captcha_option"));

        if (isNull(captcha_option)) {
			captcha_option = "default";
		}
		if (captcha_option.compareTo("default") == 0) {
			captcha_option = "audio";
		} else {
			captcha_option = "default";
		}

        request.getSession().setAttribute("captcha_option", captcha_option);
        return "retry";
	}


    public String contactUs() throws Exception {

        String msg = "Your message was successfully sent.";
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        request.getSession().removeAttribute("errorMsg");
        request.getSession().removeAttribute("errorType");
        request.getSession().removeAttribute("retry");

		String answer = HTTPUtils.cleanXSS((String) request.getParameter("answer"));
		String subject = HTTPUtils.cleanXSS((String) request.getParameter("subject"));
		String message = HTTPUtils.cleanXSS((String) request.getParameter("message"));
		String from    = HTTPUtils.cleanXSS((String) request.getParameter("emailaddress"));

		request.getSession().setAttribute("answer", answer);
		request.getSession().setAttribute("subject", subject);
		request.getSession().setAttribute("message", message);
		request.getSession().setAttribute("emailaddress", from);

		if (isNull(answer) || isNull(subject) || isNull(message) || isNull(from)) {
			msg = Constants.PLEASE_COMPLETE_DATA_ENTRIES;
			request.getSession().setAttribute("errorMsg", msg);
			request.getSession().setAttribute("retry", "true");
			return "retry";
		}

		boolean emailAddressValid = MailUtils.isValidEmailAddress(from);
		if (!emailAddressValid) {
			msg = Constants.INVALID_EMAIL_ADDRESS;
			request.getSession().setAttribute("errorMsg", msg);
			request.getSession().setAttribute("retry", "true");
			return "retry";
		}

        String captcha_option = HTTPUtils.cleanXSS((String) request.getParameter("captcha_option"));
        if (isNull(captcha_option)) {
			captcha_option = "default";
		}
		request.getSession().setAttribute("captcha_option", captcha_option);

        try {
    		String retstr = null;
    		if (captcha_option.compareTo("audio") == 0) {
				retstr = validateAudioCaptcha(request, "incomplete");
			} else {
				retstr = validateCaptcha(request, "incomplete");
			}

            String recipients[] = MailUtils.getRecipients();
            MailUtils.postMail(from, recipients, subject, message);
			request.getSession().setAttribute("message", msg);

        } catch (NoReloadException e) {
            msg = e.getMessage();
            request.getSession().setAttribute("errorMsg", Utils.toHtml(msg));
            request.getSession().setAttribute("errorType", "user");
            return "retry";

        } catch (InvalidCaptChaInputException e) {
            msg = e.getMessage();
            request.getSession().setAttribute("errorMsg", Utils.toHtml(msg));
            request.getSession().setAttribute("answer", "");
            request.getSession().setAttribute("errorType", "user");
            return "retry";

        } catch (Exception e) {
            msg = "Your message was not sent.\n";
            msg += "    (If possible, please contact NCI systems team.)\n";
            msg += "\n";
            msg += e.getMessage();
            request.getSession().setAttribute("errorMsg", Utils.toHtml(msg));
            request.getSession().setAttribute("errorType", "system");
            e.printStackTrace();
            return "error";
        }
        return "message";
    }

    private String validateCaptcha(HttpServletRequest request,
        String returnIncompleteState) throws Exception {
        Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
        if (captcha == null) {
            captcha = new Captcha.Builder(200, 50).addText().addBackground()
                // .addNoise()
                .gimp()
                // .addBorder()
                .build();
            request.getSession().setAttribute(Captcha.NAME, captcha);
        }

        // Do this so we can capture non-Latin chars
        request.setCharacterEncoding("UTF-8");
        String answer = HTTPUtils.cleanXSS((String) request.getParameter("answer"));
        if (answer == null || answer.length() == 0) {
            throw new NoReloadException(
                "Please enter the characters appearing in the image. ");
        }

        request.getSession().removeAttribute("reload");
        if (!captcha.isCorrect(answer)) {
            throw new InvalidCaptChaInputException(
                "WARNING: The string you entered does not match"
                    + " with what is shown in the image. Please try again.");
		}
		request.getSession().removeAttribute(Captcha.NAME);
        return null;
    }

    private String validateAudioCaptcha(HttpServletRequest request,
        String returnIncompleteState) throws Exception {
        AudioCaptcha captcha = (AudioCaptcha) request.getSession().getAttribute(AudioCaptcha.NAME);
/*
        if (captcha == null) {
			AudioCaptcha ac = new AudioCaptcha.Builder()
				.addAnswer()
				.addNoise()
				.build();

			request.getSession().setAttribute(AudioCaptcha.NAME, ac);
		}
*/
        // Do this so we can capture non-Latin chars
        request.setCharacterEncoding("UTF-8");
        String answer = HTTPUtils.cleanXSS((String) request.getParameter("answer"));

        if (answer == null || answer.length() == 0) {
            throw new NoReloadException(
                "Please enter the numbers you heard in the audio.");
        }

        request.getSession().removeAttribute("reload");
        if (!captcha.isCorrect(answer)) {
            throw new InvalidCaptChaInputException(
                "WARNING: The numbers you entered does not match"
                    + " with what is set in the audio. Please try again.");
		}
        request.getSession().removeAttribute(AudioCaptcha.NAME);
        return null;
    }


    private static class NoReloadException extends Exception {
        private static final long serialVersionUID = 1L;
        public NoReloadException(String text) {
            super(text);
        }
    }


    private static class InvalidCaptChaInputException extends Exception {
        private static final long serialVersionUID = 2L;
        public InvalidCaptChaInputException(String text) {
            super(text);
        }
    }

    public String clearContactUs() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

		request.getSession().setAttribute("errorMsg", "");
		request.getSession().setAttribute("answer", "");
		request.getSession().setAttribute("subject", "");
		request.getSession().setAttribute("message", "");
		request.getSession().setAttribute("emailaddress", "");

        return "retry";

    }


}
