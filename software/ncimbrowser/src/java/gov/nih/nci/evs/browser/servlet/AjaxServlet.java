package gov.nih.nci.evs.browser.servlet;

import org.LexGrid.LexBIG.LexBIGService.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction
 * with the National Cancer Institute, and so to the extent government
 * employees are co-authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without
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
 * Modification history
 *     Initial implementation kim.ong@ngc.com
 *
 */

import gov.nih.nci.evs.browser.utils.*;

import org.json.*;
import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import gov.nih.nci.evs.browser.utils.*;
import org.apache.logging.log4j.*;
import org.LexGrid.concepts.Entity;

import gov.nih.nci.evs.browser.bean.*;
import gov.nih.nci.evs.browser.common.*;

import javax.faces.context.*;

public final class AjaxServlet extends HttpServlet {
	private static Logger _logger = LogManager.getLogger(AjaxServlet.class);

    /**
     * Validates the Init and Context parameters, configures authentication URL
     *
     * @throws ServletException if the init parameters are invalid or any other
     *         problems occur during initialisation
     */
    public void init() throws ServletException {

    }

    /**
     * Route the user to the execute method
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        execute(request, response);
    }

    /**
     * Route the user to the execute method
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        execute(request, response);
    }

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

			 //[NCIM-209] AppScan Performance.
			 boolean retval = HTTPUtils.validateRequestParameters(request);
			 if (!retval) {
				 try {
					 String nextJSP = "/pages/appscan_response.jsf";
					 RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
					 dispatcher.forward(request,response);
					 return;

				 } catch (Exception ex) {
					 ex.printStackTrace();
				 }
			 }

        // Determine request by attributes
        String action = HTTPUtils.cleanXSS((String)request.getParameter("action"));// DataConstants.ACTION);
        if (action == null) action = "concept";
        String node_id = HTTPUtils.cleanXSS((String)request.getParameter("ontology_node_id"));// DataConstants.ONTOLOGY_NODE_ID);
        String ontology_display_name =
            HTTPUtils.cleanXSS((String)request.getParameter("ontology_display_name"));// DataConstants.ONTOLOGY_DISPLAY_NAME);
        String ontology_source = HTTPUtils.cleanXSS((String)request.getParameter("ontology_source"));

        long ms = System.currentTimeMillis();

        if (action.equals("build_tree")) {

            build_hierarchy(request, response);
        } else if (action.equals("expand_tree")) {

            expand_hierarchy(request, response);

        } else if (action.equals("search_tree")) {
			search_hierarchy(request, response);

        } else if (action.equals("expand_tree0")) {
            String node_id_0 = node_id;
            if (node_id != null && ontology_display_name != null) {
                int pos = node_id.indexOf("|");
                if (pos != -1) {
                    //String parent_id =
                    //    node_id.substring(pos + 1, node_id.length());
                    //node_id = node_id.substring(0, pos);
                    response.setContentType("text/html");
                    response.setHeader("Cache-Control", "no-cache");
                    JSONObject json = new JSONObject();
                    JSONArray nodesArray = null;
                    try {
                        // KLO, 041210
                        // nodesArray =
                        // CacheController.getInstance().getRemainingSubconcepts(ontology_display_name,
                        // null, parent_id, node_id);
                        nodesArray = CacheController.getInstance().getRemainingSubconcepts(node_id_0);

                        if (nodesArray != null) {
                            json.put("nodes", nodesArray);
                        }
                    } catch (Exception e) {
						e.printStackTrace();
                    }

                    response.getWriter().write(json.toString());
                    _logger.debug("Run time (milliseconds): "
                        + (System.currentTimeMillis() - ms));
                    return;
                }
                response.setContentType("text/html");
                response.setHeader("Cache-Control", "no-cache");
                JSONObject json = new JSONObject();
                JSONArray nodesArray = null;
                try {
                    if (ontology_source == null) {
                        nodesArray =
                            CacheController.getInstance().getSubconcepts(
                                ontology_display_name, null, node_id);
                    } else {
						/*
                        nodesArray =
                            CacheController.getInstance()
                                .getSubconceptsBySource(ontology_display_name,
                                    null, node_id, ontology_source);
                        */
                        nodesArray = CacheController.getInstance().getRemainingSubconcepts(node_id_0 + "|" + ontology_source + "|0");
                    }

                    if (nodesArray != null) {
                        json.put("nodes", nodesArray);
                    }

                } catch (Exception e) {
					e.printStackTrace();
                }
                response.getWriter().write(json.toString());
                _logger.debug("Run time (milliseconds): "
                    + (System.currentTimeMillis() - ms));
                return;
            }
        }

        else if (action.equals("search_tree0")) {
            if (node_id != null && ontology_display_name != null) {
                response.setContentType("text/html");
                response.setHeader("Cache-Control", "no-cache");
                /*
                 * JSONObject json = new JSONObject();
                 *
                 * try { String max_tree_level_str = null; int maxLevel = -1;
                 * try { max_tree_level_str =
                 * NCImBrowserProperties.getInstance()
                 * .getProperty(NCImBrowserProperties.MAXIMUM_TREE_LEVEL);
                 * maxLevel = Integer.parseInt(max_tree_level_str); } catch
                 * (Exception ex) { }
                 *
                 * JSONArray rootsArray = null; rootsArray =
                 * CacheController.getInstance
                 * ().getPathsToRoots(ontology_display_name, null, node_id,
                 * ontology_source, true, maxLevel); if (rootsArray.length() ==
                 * 0) {//_logger.debug(
                 * "AjaxServlet getPathsToRoots finds no path -- calling getRootConceptsBySource..."
                 * ); //rootsArray =
                 * CacheController.getInstance().getRootConceptsBySource
                 * (ontology_display_name, null, ontology_source); }
                 *
                 * json.put("root_nodes", rootsArray); } catch (Exception e) {
                 * e.printStackTrace(); }
                 *
                 * response.getWriter().write(json.toString());
                 * _logger.debug("search_tree: " + json.toString());
                 */
                String t =
                    CacheController.getInstance().getPathsToRootsExt(
                        ontology_display_name, null, node_id, ontology_source,
                        false);
                response.getWriter().write(t);
                _logger.debug("Run time (milliseconds): "
                    + (System.currentTimeMillis() - ms));
                return;
            }
        }

        else if (action.equals("build_tree1")) {
            if (ontology_display_name == null)
                ontology_display_name = "NCI Thesaurus";

            response.setContentType("text/html");
            response.setHeader("Cache-Control", "no-cache");

            JSONObject json = new JSONObject();
            JSONArray nodesArray = null;// new JSONArray();
            try {
                if (ontology_source == null
                    || ontology_source.compareTo("null") == 0) {

                    nodesArray =
                        CacheController.getInstance().getRootConcepts(
                            ontology_display_name, null);
                } else {


                    nodesArray =
                        CacheController.getInstance().getSourceRoots(
                            ontology_display_name, null, ontology_source, true);
                    // nodesArray =
                    // CacheController.getInstance().getRootConceptsBySource(ontology_display_name,
                    // null, ontology_source);
                }

                if (nodesArray != null) {

                    json.put("root_nodes", nodesArray);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.getWriter().write(json.toString());
            _logger.debug("Run time (milliseconds): "
                + (System.currentTimeMillis() - ms));
            return;
        }

        else if (action.equals("concept")) {
 			String concept_detail_scheme = HTTPUtils.cleanXSS((String)request.getParameter("dictionary"));
			String concept_detail_code = HTTPUtils.cleanXSS((String)request.getParameter("code"));
			String concept_detail_type = HTTPUtils.cleanXSS((String)request.getParameter("type"));

			Entity c = DataUtils.getConceptByCode(
							Constants.CODING_SCHEME_NAME, null, null, concept_detail_code);

			request.getSession().setAttribute("code", concept_detail_code);
			request.getSession().setAttribute("concept", c);
			request.getSession().setAttribute("type", "properties");
			request.getSession().setAttribute("new_search", Boolean.TRUE);

			response.setContentType("text/html;charset=utf-8");

			String response_page_url = request.getContextPath()
			                         + "/pages/concept_details.jsf?dictionary="
									 + concept_detail_scheme
									 + "&code="
									 + concept_detail_code
									 + "&type="
									 + concept_detail_type;
	        response.sendRedirect(response_page_url);
	    } else if (action.equals("cart")) {
			processCartActions(request, response);
        } else if (action.equals("addtocart")) {
			addToCart(request, response);
		}
    }


     public String addToCart(HttpServletRequest request, HttpServletResponse response) {
//https://ncim.nci.nih.gov/ncimbrowser/ConceptReport.jsp?dictionary=NCI%20Metathesaurus&code=C0007581
		CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");
		if (cartActionBean == null) {
			cartActionBean = new CartActionBean();
			cartActionBean._init();
			request.getSession().setAttribute("cartActionBean", cartActionBean);
		}
		cartActionBean.setEntity("concept");
		String retval = null;
		try {
			retval = cartActionBean.addToCart(request, response);
			request.getSession().setAttribute("cartActionBean", cartActionBean);

			String code = HTTPUtils.cleanXSS((String) request.getParameter("code"));
			String nextJSP = "/pages/concept_details.jsf?"
			    + "dictionary=NCI%20Metathesaurus"
			    + "&code=" + code;

			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return retval;
	}

    public String selectAllInCart(HttpServletRequest request, HttpServletResponse response) {
		try {
			CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");
			if (cartActionBean.getCount() < 1) {
				cartActionBean._messageflag = true;
				cartActionBean._message = CartActionBean.NO_CONCEPTS;
			} else {
				HashMap hmap = new HashMap();
				for (Iterator<gov.nih.nci.evs.browser.bean.CartActionBean.Concept> i = cartActionBean.getConcepts().iterator(); i.hasNext();) {
					gov.nih.nci.evs.browser.bean.CartActionBean.Concept item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept)i.next();
					item.setSelected(true);
					hmap.put(item.getCode(), item);
				}
				cartActionBean.setCart(hmap);
				request.getSession().setAttribute("cartActionBean", cartActionBean);

				String nextJSP = "/pages/cart.jsf";
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			    dispatcher.forward(request,response);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        return "refresh_cart";
    }

    public String unselectAllInCart(HttpServletRequest request, HttpServletResponse response) {
		try {
			CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");
			if (cartActionBean.getCount() < 1) {
				cartActionBean._messageflag = true;
				cartActionBean._message = CartActionBean.NO_CONCEPTS;
			} else {
				HashMap hmap = new HashMap();
				for (Iterator<gov.nih.nci.evs.browser.bean.CartActionBean.Concept> i = cartActionBean.getConcepts().iterator(); i.hasNext();) {
					gov.nih.nci.evs.browser.bean.CartActionBean.Concept item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept)i.next();
					item.setSelected(false);
					hmap.put(item.getCode(), item);
				}
				cartActionBean.setCart(hmap);
				request.getSession().setAttribute("cartActionBean", cartActionBean);

				String nextJSP = "/pages/cart.jsf";
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
				dispatcher.forward(request,response);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        return "refresh_cart";
	}


    public String exportCart2XML(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("exportCart2XML");
		try {
			CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");
			cartActionBean.exportCartXML(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "refresh_cart";
	}

    public String exportCart2CSV(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("exportCart2CSV");
		try {
			CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");
			cartActionBean.exportCartCSV(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "refresh_cart";
	}

/*
    public String removeFromCart(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("message");
		try {
			Set<String> paramNames = request.getParameterMap().keySet();
			CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");
			gov.nih.nci.evs.browser.bean.CartActionBean.Concept item = null;
			HashMap cart_hmap = new HashMap();
			Collection<gov.nih.nci.evs.browser.bean.CartActionBean.Concept> items = cartActionBean.getConcepts();
			Iterator it = items.iterator();
			while (it.hasNext()) {
				item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it.next();
				cart_hmap.put(item.getCode(), item);
			}
			Vector removed_codes = new Vector();
			for (String name : paramNames) {
				String value = request.getParameter(name);
				Iterator it2 = items.iterator();
				while (it2.hasNext()) {
					item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it2.next();
					if (item.getCode().compareTo(value) == 0) {
						removed_codes.add(value);
					}
				}
			}
			String ans = (String) request.getParameter("ans");
			if (ans == null) {
				String message = "Are you sure you want to permanently remove the following selected concepts from the cart? " +
				"&nbsp;<input type=\"radio\" name=\"ans\" checked=\"checked\" value=\"yes\" >Yes</input>&nbsp;<input type=\"radio\" value=\"no\" name=\"ans\">No</input>" +
				". &nbsp;Click <a href=\"javascript:submitform()\">here</a> to confirm.";
				request.getSession().setAttribute("message", message);
				request.getSession().setAttribute("confirmation", "true");
				String nextJSP = "/pages/cart.jsf";
				try {
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
					dispatcher.forward(request,response);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return null;
			}
			if (ans != null && ans.compareToIgnoreCase("yes") == 0) {
				for (int i=0; i<removed_codes.size(); i++) {
					String code = (String) removed_codes.elementAt(i);
					cart_hmap.remove(code);
				}
				cartActionBean.setCart(cart_hmap);
				request.getSession().setAttribute("cartActionBean", cartActionBean);
		    }

			String nextJSP = "/pages/cart.jsf";

			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "refresh_cart";
	}
*/

/*
    public String processCartActions(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("message");
        Set<String> paramNames = request.getParameterMap().keySet();
		CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");
		Collection<gov.nih.nci.evs.browser.bean.CartActionBean.Concept> items = cartActionBean.getConcepts();
		int count = items.size();//cartActionBean.getCount();
		if (count == 0) {
			String message = "WARNING: The cart is empty.";
			request.getSession().setAttribute("message", message);
			String nextJSP = "/pages/cart.jsf";
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			try {
				dispatcher.forward(request,response);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		gov.nih.nci.evs.browser.bean.CartActionBean.Concept item = null;
		int cart_action = 0;
        // iterating over parameter names and get its value
        for (String name : paramNames) {
            String value = request.getParameter(name);
			Iterator it = items.iterator();
			while (it.hasNext()) {
				item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it.next();
				if (item.getCode().compareTo(value) == 0) {
					item.setSelected(true);
				}
				if (name.startsWith("cartAction1")) {
					//select all
					cart_action = 1;
				} else if (name.startsWith("cartAction2")) {
					//unselect all
					cart_action = 2;
				} else if (name.startsWith("cartAction3")) {
					//remove selected
					cart_action = 3;
				} else if (name.startsWith("cartAction4")) {
					//export xml
					cart_action = 4;
				} else if (name.startsWith("cartAction5")) {
					//export csv
					cart_action = 5;
				}
			}
        }

        HashMap cart_hmap = new HashMap();
		Iterator it = items.iterator();
		int selected_count = 0;
		while (it.hasNext()) {
			item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it.next();
			if (item.getSelected()) {
				selected_count++;
			}
			cart_hmap.put(item.getCode(), item);
		}
        cartActionBean.setCart(cart_hmap);
        request.getSession().setAttribute("cartActionBean", cartActionBean);

        if (cart_action > 2 && selected_count == 0) {
			String message = "WARNING: No concept is selected.";
			request.getSession().setAttribute("message", message);

			String nextJSP = "/pages/cart.jsf";

			if (cartActionBean.getCount() == 0) {
				nextJSP = "/pages/home.jsf";
			}

			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			try {
				dispatcher.forward(request,response);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}


        if (cart_action > 2 && cart_hmap.keySet().size() == 0) {
			String message = "INFORMATION: The cart is empty.";
			request.getSession().setAttribute("message", message);
			String nextJSP = "/pages/cart.jsf";
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			try {
				dispatcher.forward(request,response);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

        String ans = (String) request.getSession().getAttribute("ans");
        if (ans != null) {
			cart_action = 3;
		}

        switch (cart_action) {
            case 0:  removeFromCart(request, response);
                     break;
            case 1:  selectAllInCart(request, response);

                     break;
            case 2:  unselectAllInCart(request, response);
                     break;
            case 3:  removeFromCart(request, response);
                     break;
            case 4:  exportCart2XML(request, response);
                     break;
            case 5:  exportCart2CSV(request, response);
                     break;
		}
        return "refresh_cart";
	}
*/


    public String removeFromCart(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("message");
		try {
			Set<String> paramNames = request.getParameterMap().keySet();
			CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");
			gov.nih.nci.evs.browser.bean.CartActionBean.Concept item = null;
			HashMap cart_hmap = new HashMap();
			Collection<gov.nih.nci.evs.browser.bean.CartActionBean.Concept> items = cartActionBean.getConcepts();
			Iterator it = items.iterator();
			while (it.hasNext()) {
				item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it.next();
				cart_hmap.put(item.getCode(), item);
			}
			Vector removed_codes = new Vector();
			for (String name : paramNames) {
				String value = request.getParameter(name);
				Iterator it2 = items.iterator();
				while (it2.hasNext()) {
					item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it2.next();
					if (item.getCode().compareTo(value) == 0) {
						removed_codes.add(value);
					}
				}
			}
			String ans = (String) request.getParameter("ans");
			if (ans == null) {
				/*
				String message = "Are you sure you want to permanently remove the following selected concepts from the cart? " +
				"&nbsp;<input type=\"radio\" name=\"ans\" checked=\"checked\" value=\"yes\" >Yes</input>&nbsp;<input type=\"radio\" value=\"no\" name=\"ans\">No</input>" +
				". &nbsp;Click <a href=\"javascript:submitform()\">here</a> to confirm.";
				*/
				String message = "Are you sure you want to permanently remove the following selected concepts from the cart? " +
				"&nbsp;<input type=\"radio\" name=\"ans\" checked=\"checked\" value=\"yes\" >Yes</input>&nbsp;<input type=\"radio\" value=\"no\" name=\"ans\">No</input>";

				request.getSession().setAttribute("message", message);
				request.getSession().setAttribute("confirmation", "true");
				String nextJSP = "/pages/cart.jsf";
				try {
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
					dispatcher.forward(request,response);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return null;
			} else {
			    if (ans.compareToIgnoreCase("yes") == 0) {
					for (int i=0; i<removed_codes.size(); i++) {
						String code = (String) removed_codes.elementAt(i);
						cart_hmap.remove(code);
					}
					cartActionBean.setCart(cart_hmap);
				}
			}
			request.getSession().setAttribute("cartActionBean", cartActionBean);
			//items = cartActionBean.getConcepts();
			//int count = items.size();
		    String nextJSP = "/pages/cart.jsf";
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "refresh_cart";
	}


    public String processCartActions(HttpServletRequest request, HttpServletResponse response) {
		Set<String> paramNames = request.getParameterMap().keySet();
		CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");
		gov.nih.nci.evs.browser.bean.CartActionBean.Concept item = null;
		Collection<gov.nih.nci.evs.browser.bean.CartActionBean.Concept> items = cartActionBean.getConcepts();

		Iterator it0 = items.iterator();
		while (it0.hasNext()) {
			item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it0.next();
			item.setSelected(false);
		}

		for (String name : paramNames) {
			String value = request.getParameter(name);
			Iterator it2 = items.iterator();
			while (it2.hasNext()) {
				item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it2.next();
				if (item.getCode().compareTo(value) == 0) {
					item.setSelected(true);
				}
			}
		}

		HashMap cart_hmap = new HashMap();
		Iterator it = items.iterator();
		while (it.hasNext()) {
			item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it.next();
			cart_hmap.put(item.getCode(), item);
		}
        cartActionBean.setCart(cart_hmap);
        request.getSession().setAttribute("cartActionBean", cartActionBean);

		//String ans = (String) request.getSession().getAttribute("ans");
		request.getSession().removeAttribute("message");
		int count = items.size();
		String btn = (String) request.getParameter("btn");
		if (count == 0 && btn.compareTo("exit_cart") != 0) {
			String message = "INFO: The cart is empty.";
			request.getSession().setAttribute("message", message);
			String nextJSP = "/pages/cart.jsf";
			if (cartActionBean.getCount() == 0) {
				nextJSP = "/pages/home.jsf";
			}
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			try {
				dispatcher.forward(request,response);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		Iterator it3 = items.iterator();
		int selected_count = 0;
		while (it3.hasNext()) {
			item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it3.next();
			if (item.getSelected()) {
				selected_count++;
			}
		}
		if ((btn.compareTo("removefromcart") == 0
		    || btn.compareTo("exportxml") == 0
		    || btn.compareTo("exportcsv") == 0)
		    && selected_count == 0) {
			String message = "WARNING: No concept is selected.";
			request.getSession().setAttribute("message", message);

			String nextJSP = "/pages/cart.jsf";
			if (cartActionBean.getCount() == 0) {
				nextJSP = "/pages/home.jsf";
			}

			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			try {
				dispatcher.forward(request,response);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
		if (btn.compareTo("exit_cart") == 0) {
			//String scheme = (String) request.getParameter("scheme");
			//String version = (String) request.getParameter("version");
			//String nextJSP = "/pages/multiple_search.jsf?nav_type=terminologies";
			String nextJSP = "/pages/home.jsf";
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			try {
				dispatcher.forward(request,response);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
        switch (btn) {
            case "selectall":  selectAllInCart(request, response);
                     break;
            case "unselectall":  unselectAllInCart(request, response);
                     break;
            case "removefromcart":  removeFromCart(request, response);
                     break;
            case "exportxml":  exportCart2XML(request, response);
                     break;
            case "exportcsv":  exportCart2CSV(request, response);
                     break;
            default:
                selectAllInCart(request, response);
		}
        return "refresh_cart";
	}

    public void build_hierarchy(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/csv");
		ServletOutputStream ouputStream = null;
		StringBuffer sb = new StringBuffer();
		try {
			ouputStream = response.getOutputStream();
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			MetaTreeHelper metaTreeHelper = new MetaTreeHelper(lbSvc);
			String scheme = request.getParameter("ontology_display_name");
			String sab = request.getParameter("ontology_sab");
			if (scheme == null) {
				scheme = "NCI Thesaurus";
			}
	        String version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(scheme, "PRODUCTION");
			String content = metaTreeHelper.build_tree(scheme, version, sab);

			sb.append(content);
			ouputStream.write(sb.toString().getBytes("UTF-8"), 0, sb.length());
			ouputStream.flush();
			try {
				ouputStream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			//FacesContext.getCurrentInstance().responseComplete();
			return;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    public void expand_hierarchy(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/csv");
		ServletOutputStream ouputStream = null;
		StringBuffer sb = new StringBuffer();
		try {
			ouputStream = response.getOutputStream();
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			MetaTreeHelper metaTreeHelper = new MetaTreeHelper(lbSvc);
			String scheme = request.getParameter("ontology_display_name");
			String version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(scheme, "PRODUCTION");
			String sab = request.getParameter("ontology_sab");
			String cui = request.getParameter("ontology_node_id");
			String id = request.getParameter("id");

			String content = metaTreeHelper.expand_tree(scheme, version, cui, sab, id);

			sb.append(content);
			ouputStream.write(sb.toString().getBytes("UTF-8"), 0, sb.length());
			ouputStream.flush();

			try {
				ouputStream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			//FacesContext.getCurrentInstance().responseComplete();
			return;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    public void search_hierarchy(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/csv");
		ServletOutputStream ouputStream = null;
		StringBuffer sb = new StringBuffer();
		try {
			ouputStream = response.getOutputStream();
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			MetaTreeHelper metaTreeHelper = new MetaTreeHelper(lbSvc);
			String scheme = request.getParameter("ontology_display_name");
			String version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(scheme, "PRODUCTION");
			String sab = request.getParameter("ontology_sab");
			String cui = request.getParameter("ontology_node_id");

			String content = metaTreeHelper.search_tree(scheme, version, sab, cui);
			if (content == null) {
				content = "<p><center>Unable to resolve paths to roots. Source hierarchy is not available.</center></p>";
			}

			sb.append(content);
			ouputStream.write(sb.toString().getBytes("UTF-8"), 0, sb.length());
			ouputStream.flush();

			try {
				ouputStream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			//FacesContext.getCurrentInstance().responseComplete();
			return;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
