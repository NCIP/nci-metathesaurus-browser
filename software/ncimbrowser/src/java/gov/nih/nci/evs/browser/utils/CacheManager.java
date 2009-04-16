package gov.nih.nci.evs.browser.utils;

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

import org.apache.jcs.JCS;
import org.apache.jcs.engine.behavior.IElementAttributes;
import gov.nih.nci.evs.browser.utils.TreeUtils.TreeItem;


import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Vector;
import java.util.Set;
import java.util.Collections;
import java.util.Map;


import org.json.*;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.concepts.Concept;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.concepts.Presentation;

public class CacheManager
{
    private static CacheManager instance;
    private static int checkedOut = 0;
    private static JCS treeCache;
/*
	  public static final String ONTOLOGY_DISPLAY_NAME = "ontology_display_name";
	  public static final String ONTOLOGY_NODE = "ontology_node";


	  public static final String ONTOLOGY_SOURCE = "ontology_source";
*/
      public static final String ONTOLOGY_NODE_ID = "ontology_node_id";
	  public static final String ONTOLOGY_NODE_NAME = "ontology_node_name";
	  public static final String ONTOLOGY_NODE_PARENT_ASSOC = "ontology_node_parent_assoc";
	  public static final String ONTOLOGY_NODE_CHILD_COUNT = "ontology_node_child_count";
	  public static final String ONTOLOGY_NODE_DEFINITION = "ontology_node_definition";


    private CacheManager()
    {
        try
        {
            treeCache = JCS.getInstance("treeCache");
        }
        catch (Exception e)
        {
			e.printStackTrace();
        }
    }


    public static CacheManager getInstance()
    {
        synchronized (CacheManager.class)
        {
            if (instance == null)
            {
                instance = new CacheManager();
            }
        }

        synchronized (instance)
        {
            instance.checkedOut++;
        }

        return instance;
    }


    public HashMap getSubconcepts(String scheme, String version, String code)
    {
		return getSubconcepts(scheme, version, code, true);
	}


    public HashMap getSubconcepts(String scheme, String version, String code, boolean fromCache)
    {
		HashMap map = null;
		String key = scheme + "$" + version + "$" + code;
        if (fromCache)
        {
            map = (HashMap) treeCache.get(key);
        }
        if (map == null)
        {
			System.out.println("Not in cache -- calling getSubconcepts " );
            map = new TreeUtils().getSubconcepts(scheme, version, code);
            try {
	            treeCache.put(key, map);
			} catch (Exception ex) {

			}
        }
        else
        {
			System.out.println("Retrieved from cache." );
		}
        return  map;
    }

    public List getRootConcepts(String scheme, String version)
    {
		return getRootConcepts(scheme, version, true);
	}


    public List getRootConcepts(String scheme, String version, boolean fromCache)
    {
		List list = new ArrayList();
		String key = scheme + "$" + version + "$root";
        if (fromCache)
        {
            list = (List) treeCache.get(key);
        }
        if (list == null)
        {
			System.out.println("Not in cache -- calling getHierarchyRoots " );
            try {
				list = new DataUtils().getHierarchyRoots(scheme, version, null);
	            treeCache.put(key, list);
			} catch (Exception ex) {

			}
        }
        else
        {
			System.out.println("Retrieved from cache." );
		}
        return list;
    }
/*

    public JSONArray getRootConcepts(String scheme, String version)
    {
		return getRootConcepts(scheme, version, true);
	}


    public JSONArray getRootConcepts(String scheme, String version, boolean fromCache)
    {
		JSONArray nodesArray = null;
		//List list = new ArrayList();
		String key = scheme + "$" + version + "$root";
		//System.out.println("key: " + key);
        if (fromCache)
        {
            nodesArray = (JSONArray) treeCache.get(key);
        }
        if (nodesArray == null)
        {
			System.out.println("Not in cache -- calling getHierarchyRoots " );
            try {
				List list = new DataUtils().getHierarchyRoots(scheme, version, null);
				if (list != null)
				{
					nodesArray = new JSONArray();
					for (int i=0; i<list.size(); i++) {
					  ResolvedConceptReference node = (ResolvedConceptReference) list.get(i);
					  Concept concept = node.getReferencedEntry();
					  int childCount = 1; // assumption

					  JSONObject nodeObject = new JSONObject();
					  nodeObject.put(ONTOLOGY_NODE_ID, node.getConceptCode());

					  //String pt = getPreferredName(concept);

					  String name = concept.getEntityDescription().getContent();
					  nodeObject.put(ONTOLOGY_NODE_NAME, name);
					  nodeObject.put(ONTOLOGY_NODE_CHILD_COUNT, childCount);
					  nodesArray.put(nodeObject);
					}
					if (fromCache) {
						try {
							treeCache.put(key, nodesArray);
							System.out.println("treeCache.put successful -- " + key);
						} catch (Exception ex) {
							System.out.println("treeCache.put failed -- " + key);
						}
					}
			    }
			} catch (Exception ex) {
                ex.printStackTrace();
			}
        }
        return nodesArray;
    }


    public JSONArray getSubconcepts(String scheme, String version, String code)
    {
		return getSubconcepts(scheme, version, code, true);
	}


    public JSONArray getSubconcepts(String scheme, String version, String code, boolean fromCache)
    {
		JSONArray nodesArray = null;
		HashMap map = null;
		String key = scheme + "$" + version + "$" + code;
        if (fromCache)
        {
            nodesArray = (JSONArray) treeCache.get(key);
        }
        if (nodesArray == null)
        {
			System.out.println("Not in cache -- calling getSubconcepts " );
			try {
				map = new TreeUtils().getSubconcepts(scheme, version, code);
				nodesArray = new JSONArray();
				Set keyset = map.keySet();
				Object[] objs = keyset.toArray();
				//String code = (String) objs[0];
				TreeItem ti = (TreeItem) map.get(code);

				for (String association : ti.assocToChildMap.keySet()) {
					List<TreeItem> children = ti.assocToChildMap.get(association);
					Collections.sort(children);
					for (TreeItem childItem : children) {
						//printTree(childItem, focusCode, depth + 1);
						JSONObject nodeObject = new JSONObject();

						nodeObject.put(ONTOLOGY_NODE_ID, childItem.code);
						nodeObject.put(ONTOLOGY_NODE_NAME, childItem.text);
						int knt = 0;
						if (childItem.expandable)
						{
							knt = 1;
						}
						nodeObject.put(ONTOLOGY_NODE_CHILD_COUNT, knt);
						nodesArray.put(nodeObject);
					}
				}
				if (fromCache) {
					try {
						treeCache.put(key, nodesArray);
						System.out.println("treeCache.put successful -- " + key);
					} catch (Exception ex) {
                        System.out.println("treeCache.put failed -- " + key);
					}
				}

			} catch (Exception e) {

			}

        }
        return nodesArray;
    }
*/
}