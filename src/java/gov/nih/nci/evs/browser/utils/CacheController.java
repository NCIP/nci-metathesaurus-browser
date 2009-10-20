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

import java.io.Serializable;
import java.util.TreeSet;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


//import gov.nih.nci.evs.browser.utils.TreeItem;
import gov.nih.nci.evs.browser.properties.NCImBrowserProperties;


import java.util.List;
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


public class CacheController
{
  public static final String ONTOLOGY_ADMINISTRATORS = "ontology_administrators";
  public static final String ONTOLOGY_FILE = "ontology_file";
  public static final String ONTOLOGY_FILE_ID = "ontology_file_id";
  public static final String ONTOLOGY_DISPLAY_NAME = "ontology_display_name";
  public static final String ONTOLOGY_NODE = "ontology_node";
  public static final String ONTOLOGY_NODE_ID = "ontology_node_id";

  public static final String ONTOLOGY_SOURCE = "ontology_source";

  public static final String ONTOLOGY_NODE_NAME = "ontology_node_name";
  public static final String ONTOLOGY_NODE_PARENT_ASSOC = "ontology_node_parent_assoc";
  public static final String ONTOLOGY_NODE_CHILD_COUNT = "ontology_node_child_count";
  public static final String ONTOLOGY_NODE_DEFINITION = "ontology_node_definition";

  public static final String ONTOLOGY_NODE_EXPANDABLE = "ontology_node_expandable";

  public static final String CHILDREN_NODES = "children_nodes";
  public static final String NCI_SOURCE = "NCI";

    private static CacheController instance = null;
    private static Cache cache = null;
    private static CacheManager cacheManager = null;

    public CacheController(String cacheName) {
        cacheManager = getCacheManager();
        if (!cacheManager.cacheExists(cacheName)) {
            cacheManager.addCache(cacheName);
        }

        System.out.println("cache added");
        this.cache = cacheManager.getCache(cacheName);
    }

    public static CacheController getInstance()
    {
        synchronized (CacheController.class)
        {
            if (instance == null)
            {
                instance = new CacheController("treeCache");
            }
        }
        return instance;
    }

    private static CacheManager getCacheManager() {
        if (cacheManager != null) return cacheManager;
        try {
            NCImBrowserProperties properties = NCImBrowserProperties.getInstance();
            String ehcache_xml_pathname = properties.getProperty(NCImBrowserProperties.EHCACHE_XML_PATHNAME);
            cacheManager = new CacheManager(ehcache_xml_pathname);
            return cacheManager;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String[] getCacheNames() {
        return getCacheManager().getCacheNames();
    }

    public void clear() {
        cache.removeAll();
        //cache.flush();
    }

    public boolean containsKey(Object key) {
        return cache.isKeyInCache(key);
    }

    public boolean containsValue(Object value) {
        return cache.isValueInCache(value);
    }

    public boolean isEmpty() {
        return cache.getSize() > 0;
    }


    public JSONArray getSubconcepts(String scheme, String version, String code)
    {
        return getSubconcepts(scheme, version, code, true);
    }


    public JSONArray getSubconcepts(String scheme, String version, String code, boolean fromCache)
    {
        HashMap map = null;
        String key = scheme + "$" + version + "$" + code;
        JSONArray nodeArray = null;
        if (fromCache)
        {
            Element element = cache.get(key);
            if (element != null)
            {
                nodeArray = (JSONArray) element.getValue();
            }
        }
        if (nodeArray == null)
        {
            System.out.println("Not in cache -- calling getSubconcepts..." );
			//map = new MetaTreeUtils().getSubconcepts(scheme, version, code, NCI_SOURCE, "PAR", false);
			map = new MetaTreeUtils().getSubconcepts(scheme, version, code, NCI_SOURCE, MetaTreeUtils.hierAssocToParentNodes_, false);

            nodeArray = HashMap2JSONArray(map);
            if (fromCache) {
                try {
                    Element element = new Element(key, nodeArray);
                    cache.put(element);
                } catch (Exception ex) {

                }
            }
        }
        return nodeArray;
    }

    public JSONArray getRootConcepts(String scheme, String version)
    {
        return getRootConcepts(scheme, version, true);
    }


    public JSONArray getRootConcepts(String scheme, String version, boolean fromCache)
    {
        List list = null;//new ArrayList();
        String key = scheme + "$" + version + "$root";
        JSONArray nodeArray = null;

        if (fromCache)
        {
            Element element = cache.get(key);
            if (element != null) {
                //System.out.println("getRootConcepts fromCache element != null returning list" );
                nodeArray = (JSONArray) element.getValue();
            }
        }

        if (nodeArray == null)
        {
            System.out.println("Not in cache -- calling getHierarchyRoots " );
            try {
                //list = new DataUtils().getHierarchyRoots(scheme, version, null);
                String tag = null;
                CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
                if (version != null) csvt.setVersion(version);
                list = new DataUtils().getSourceHierarchyRoots(scheme, csvt, "NCI");

                SortUtils.quickSort(list);

                nodeArray = list2JSONArray(list);

                if (fromCache)
                {
                    Element element = new Element(key, nodeArray);
                    cache.put(element);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return nodeArray;
    }


    private JSONArray list2JSONArray(List list) {
        JSONArray nodesArray = null;
        try {
            if (list != null)
            {
                nodesArray = new JSONArray();
                for (int i=0; i<list.size(); i++) {
                      Object obj = list.get(i);
                      String code = "";
                      String name = "";
                      if (obj instanceof ResolvedConceptReference)
                      {
                          ResolvedConceptReference node = (ResolvedConceptReference) list.get(i);
                          code = node.getConceptCode();
                          name = node.getEntityDescription().getContent();
                      }
                      else if (obj instanceof Concept) {
                          Concept node = (Concept) list.get(i);
                          code = node.getEntityCode();
                          name = node.getEntityDescription().getContent();
                      }
                      ResolvedConceptReference node = (ResolvedConceptReference) list.get(i);
                      int childCount = 1;
                      try {
                          JSONObject nodeObject = new JSONObject();
                          nodeObject.put(ONTOLOGY_NODE_ID, code);
                          nodeObject.put(ONTOLOGY_NODE_NAME, name);
                          nodeObject.put(ONTOLOGY_NODE_CHILD_COUNT, childCount);
                          nodeObject.put(CHILDREN_NODES, new JSONArray());
                          nodesArray.put(nodeObject);

                      } catch (Exception ex) {
                          ex.printStackTrace();
                      }
                }
            }


        } catch (Exception ex) {

        }
        return nodesArray;
    }


    private JSONArray HashMap2JSONArray(HashMap hmap) {
        JSONObject json = new JSONObject();
        JSONArray nodesArray = null;
        try {
            nodesArray = new JSONArray();
            Set keyset = hmap.keySet();
            Object[] objs = keyset.toArray();
            String code = (String) objs[0];
            TreeItem ti = (TreeItem) hmap.get(code);
            for (String association : ti.assocToChildMap.keySet()) {
                List<TreeItem> children = ti.assocToChildMap.get(association);

                SortUtils.quickSort(children);

                for (TreeItem childItem : children) {
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
        } catch (Exception e) {

        }
        return nodesArray;
    }




    public JSONArray getPathsToRoots(String scheme, String version, String code)
    {
        List list = null;//new ArrayList();
        String key = scheme + "$" + version + "$" + code + "$path";
        JSONArray nodeArray = null;
        Element element = cache.get(key);
        if (element != null) {
            nodeArray = (JSONArray) element.getValue();
        }

        if (nodeArray == null)
        {
            System.out.println("Not in cache -- calling getHierarchyRoots " );
            try {
                nodeArray = getPathsToRoots(scheme, version, code, true);
                element = new Element(key, nodeArray);
                cache.put(element);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return nodeArray;
    }


    public JSONArray getPathsToRoots(String ontology_display_name, String version, String node_id, boolean fromCache)
    {
        return getPathsToRoots(ontology_display_name, version, node_id, fromCache, -1);
    }


    public JSONArray getPathsToRoots(String ontology_display_name, String version, String node_id, boolean fromCache, int maxLevel)
    {
        JSONArray rootsArray = null;
        if (maxLevel == -1) {
            rootsArray = getRootConcepts(ontology_display_name, version, false);
            try {
                MetaTreeUtils util = new MetaTreeUtils();
                HashMap hmap = util.getTreePathData(ontology_display_name, null, null, node_id, maxLevel);
                Set keyset = hmap.keySet();
                Object[] objs = keyset.toArray();
                String code = (String) objs[0];
                TreeItem ti = (TreeItem) hmap.get(code); //TreeItem ti = new TreeItem("<Root>", "Root node");

                JSONArray nodesArray = getNodesArray(node_id, ti);
                //JSONArray nodesArray = getNodesArray(node_id, ti, node_id);
                replaceJSONObjects(rootsArray, nodesArray);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return rootsArray;
        }
        else {
            try {
                MetaTreeUtils util = new MetaTreeUtils();
                HashMap hmap = util.getTreePathData(ontology_display_name, null, "NCI", node_id, maxLevel);

                Object[] objs = hmap.keySet().toArray();
                String code = (String) objs[0];
                TreeItem ti = (TreeItem) hmap.get(code);

                List list = util.getTopNodes(ti);
                rootsArray = list2JSONArray(list);
                //Set keyset = hmap.keySet();
                //Object[] objs = keyset.toArray();
                //String code = (String) objs[0];
                //TreeItem ti = (TreeItem) hmap.get(code); //TreeItem ti = new TreeItem("<Root>", "Root node");

                //JSONArray nodesArray = getNodesArray(ti);

                JSONArray nodesArray = getNodesArray(node_id, ti);
                //JSONArray nodesArray = getNodesArray(node_id, ti, node_id);
                replaceJSONObjects(rootsArray, nodesArray);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return rootsArray;
        }
    }

    private void replaceJSONObject(JSONArray nodesArray, JSONObject obj) {
        String obj_id = null;
        try {
            obj_id = (String) obj.get(ONTOLOGY_NODE_ID);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        for (int i=0; i<nodesArray.length(); i++)
        {
            String node_id = null;
            try {
                JSONObject node = nodesArray.getJSONObject(i);
                node_id = (String) node.get(ONTOLOGY_NODE_ID);

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if (obj_id.compareTo(node_id) == 0) {
                try {

                    nodesArray.put(i, obj);
                    break;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    private void replaceJSONObjects(JSONArray nodesArray, JSONArray nodesArray2) {
        for (int i=0; i<nodesArray2.length(); i++)
        {
            try {
                JSONObject obj = nodesArray2.getJSONObject(i);
                replaceJSONObject(nodesArray, obj);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private int findFocusNodePosition(String node_id, List<TreeItem> children) {
        for (int i=0; i<children.size(); i++) {
            TreeItem childItem = (TreeItem) children.get(i);
            if (node_id.compareTo(childItem.code) == 0) return i;
        }
        return -1;
    }

    private JSONArray getNodesArray(String node_id, TreeItem ti) {
        JSONArray nodesArray = new JSONArray();
        for (String association : ti.assocToChildMap.keySet()) {
            List<TreeItem> children = ti.assocToChildMap.get(association);
            SortUtils.quickSort(children);

            int cut_off = 200;
            int m = cut_off / 2;
            int m2 = m / 2;

            if (children.size() <= cut_off) {
                for (int i=0; i<children.size(); i++) {
                    TreeItem childItem = (TreeItem) children.get(i);
                    int knt = 0;
                    if (childItem.expandable)
                    {
                        knt = 1;
					}

                    JSONObject nodeObject = new JSONObject();
                    try {
                        nodeObject.put(ONTOLOGY_NODE_ID, childItem.code);
                        nodeObject.put(ONTOLOGY_NODE_NAME, childItem.text);
                        nodeObject.put(ONTOLOGY_NODE_CHILD_COUNT, knt);
                        nodeObject.put(CHILDREN_NODES, getNodesArray(node_id, childItem));

                        nodesArray.put(nodeObject);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                int len = children.size();
                int min = 0;
                int max = len - 1;
                int pos = findFocusNodePosition(node_id, children);
                if (pos == -1) {
                    for (int i=0; i<children.size(); i++) {
                        TreeItem childItem = (TreeItem) children.get(i);
                        int knt = 0;
                        if (childItem.expandable)
                        {
                            knt = 1;
                        }
                        JSONObject nodeObject = new JSONObject();
                        try {
                            nodeObject.put(ONTOLOGY_NODE_ID, childItem.code);
                            nodeObject.put(ONTOLOGY_NODE_NAME, childItem.text);
                            nodeObject.put(ONTOLOGY_NODE_CHILD_COUNT, knt);
                            nodeObject.put(CHILDREN_NODES, getNodesArray(node_id, childItem));

                            nodesArray.put(nodeObject);
                        } catch (Exception ex) {

                        }
                    }
                } else {
                    if (pos < m) {
                        min = 0;
                        max = m + 1;
                        if (max > len) max = len;
                    } else {
                        if (pos - m2 > 0) min = pos - m2;
                        if (pos + m2 < max) max = pos + m2;
                    }

                    JSONObject nodeObject = null;
                    for (int i=min; i<max; i++) {
                        TreeItem childItem = (TreeItem) children.get(i);
                        int knt = 0;
                        if (childItem.expandable)
                        {
                            knt = 1;
                        }
                        nodeObject = new JSONObject();
                        try {
                            nodeObject.put(ONTOLOGY_NODE_ID, childItem.code);
                            nodeObject.put(ONTOLOGY_NODE_NAME, childItem.text);
                            nodeObject.put(ONTOLOGY_NODE_CHILD_COUNT, knt);
                            nodeObject.put(CHILDREN_NODES, getNodesArray(node_id, childItem));
                            nodesArray.put(nodeObject);
                        } catch (Exception ex) {

                        }
                    }

                    nodeObject = new JSONObject();
                    try {
                        nodeObject.put(ONTOLOGY_NODE_ID, node_id);
                        nodeObject.put(ONTOLOGY_NODE_NAME, "(Too many sibling nodes -- only " + m + " of a total " + len + " are displayed.)");
                        nodeObject.put(ONTOLOGY_NODE_CHILD_COUNT, 0);
                        nodeObject.put(CHILDREN_NODES, new JSONArray());
                        nodesArray.put(nodeObject);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return nodesArray;
    }

}

