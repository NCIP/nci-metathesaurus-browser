package gov.nih.nci.evs.browser.utils;

import java.util.*;
import net.sf.ehcache.*;
import gov.nih.nci.evs.browser.properties.*;
import org.json.*;

import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.concepts.*;

import org.LexGrid.lexevs.metabrowser.*;
import org.LexGrid.lexevs.metabrowser.model.*;
import org.LexGrid.lexevs.metabrowser.model.MetaTreeNode.*;

import org.apache.log4j.*;

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

public class CacheController {
    private static Logger _logger = Logger.getLogger(CacheController.class);
    public static final String ONTOLOGY_ADMINISTRATORS =
        "ontology_administrators";
    public static final String ONTOLOGY_FILE = "ontology_file";
    public static final String ONTOLOGY_FILE_ID = "ontology_file_id";
    public static final String ONTOLOGY_DISPLAY_NAME = "ontology_display_name";
    public static final String ONTOLOGY_NODE = "ontology_node";
    public static final String ONTOLOGY_NODE_ID = "ontology_node_id";

    public static final String ONTOLOGY_SOURCE = "ontology_source";

    public static final String ONTOLOGY_NODE_NAME = "ontology_node_name";
    public static final String ONTOLOGY_NODE_PARENT_ASSOC =
        "ontology_node_parent_assoc";
    public static final String ONTOLOGY_NODE_CHILD_COUNT =
        "ontology_node_child_count";
    public static final String ONTOLOGY_NODE_DEFINITION =
        "ontology_node_definition";

    public static final String ONTOLOGY_NODE_EXPANDABLE =
        "ontology_node_expandable";

    public static final String CHILDREN_NODES = "children_nodes";
    public static final String NCI_SOURCE = "NCI";

    private static CacheController _instance = null;
    private static Cache _cache = null;
    private static CacheManager _cacheManager = null;

    private static final String TREE_CACHE = "treeCache";

/*
    static {
		_cacheManager = getCacheManager();
		if (_cacheManager != null) {
			_cache = _cacheManager.getCache(TREE_CACHE);
			if (_cache == null) {
				System.out.println("_cache == NULL???");
                _cacheManager.addCache(TREE_CACHE);
			}
		}
	}
*/


    public CacheController(String cacheName) {
        _cacheManager = getCacheManager();
        if (!_cacheManager.cacheExists(cacheName)) {
            _cacheManager.addCache(cacheName);
            _cache = _cacheManager.getCache(TREE_CACHE);
        }
        //_cache = _cacheManager.getCache(cacheName);
    }


    public static CacheController getInstance() {
        synchronized (CacheController.class) {
            if (_instance == null) {
                _instance = new CacheController(TREE_CACHE);
            }
        }
        return _instance;
    }


    static {
        try {
            NCImBrowserProperties properties =
                NCImBrowserProperties.getInstance();
            String ehcache_xml_pathname =
                properties
                    .getProperty(NCImBrowserProperties.EHCACHE_XML_PATHNAME);
            _cacheManager = new CacheManager(ehcache_xml_pathname);
            //return _cacheManager;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}


    private static CacheManager getCacheManager() {
        if (_cacheManager != null)
            return _cacheManager;
        try {
            NCImBrowserProperties properties =
                NCImBrowserProperties.getInstance();
            String ehcache_xml_pathname =
                properties
                    .getProperty(NCImBrowserProperties.EHCACHE_XML_PATHNAME);
            _cacheManager = new CacheManager(ehcache_xml_pathname);
            return _cacheManager;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

/*
    private static CacheManager getCacheManager() {
		if (_cacheManager == null) {
			_cacheManager = new CacheManager(TREE_CACHE);
		}
        return _cacheManager;
	}
*/

    public String[] getCacheNames() {
        return getCacheManager().getCacheNames();
    }

    public void clear() {
        _cache.removeAll();
        // cache.flush();
    }

    public boolean containsKey(Object key) {
        return _cache.isKeyInCache(key);
    }

    public boolean containsValue(Object value) {
        return _cache.isValueInCache(value);
    }

    public boolean isEmpty() {
        return _cache.getSize() > 0;
    }

    public JSONArray getSubconcepts(String scheme, String version, String code) {
        return getSubconcepts(scheme, version, code, true);
    }

    public JSONArray getSubconcepts(String scheme, String version, String code,
        boolean fromCache) {
        HashMap map = null;
        String key = scheme + "$" + version + "$" + code;
        JSONArray nodeArray = null;
        if (fromCache) {
            Element element = _cache.get(key);
            if (element != null) {
                nodeArray = (JSONArray) element.getValue();
            }
        }
        if (nodeArray == null) {
            _logger.debug("Not in cache -- calling getSubconcepts...");
            map =
                new MetaTreeUtils().getRemainingSubconcepts(scheme, version,
                    code, NCI_SOURCE, null);

            nodeArray = HashMap2JSONArray(map);
            if (fromCache) {
                try {
                    Element element = new Element(key, nodeArray);
                    _cache.put(element);
                } catch (Exception ex) {

                }
            }
        }
        return nodeArray;
    }

    public JSONArray getRemainingSubconcepts(String scheme, String version,
        String code, String subconcept_code) {
        HashMap map = null;
        JSONArray nodeArray = null;
        map =
            new MetaTreeUtils().getRemainingSubconcepts(scheme, version, code,
                NCI_SOURCE, subconcept_code);
        nodeArray = HashMap2JSONArray(map);
        return nodeArray;
    }

    public JSONArray getRootConcepts(String scheme, String version) {
        return getRootConcepts(scheme, version, true);
    }

    public JSONArray getRootConcepts(String scheme, String version,
        boolean fromCache) {
        List list = null;// new ArrayList();
        String key = scheme + "$" + version + "$root";
        JSONArray nodeArray = null;

        if (fromCache) {
            Element element = _cache.get(key);
            if (element != null) {
                nodeArray = (JSONArray) element.getValue();
            }
        }

        if (nodeArray == null) {
            _logger.debug("Not in cache -- calling getHierarchyRoots ");
            try {
                // list = new DataUtils().getHierarchyRoots(scheme, version,
                // null);
                String tag = null;
                CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
                if (version != null)
                    csvt.setVersion(version);
                // list = new DataUtils().getSourceHierarchyRoots(scheme, csvt,
                // NCI_SOURCE);
                list =
                    (new MetaTreeUtils()).getSourceHierarchyRoots(scheme, csvt,
                        NCI_SOURCE);
                // SortUtils.quickSort(list);

                nodeArray = list2JSONArray(list);

                if (fromCache) {
                    Element element = new Element(key, nodeArray);
                    _cache.put(element);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return nodeArray;
    }

    // /////////////////////////////////////////////////////////////////////////////////////////////////
    // Source Hierarchy
    // ////////////////////
    public JSONArray getSubconceptsBySource(String scheme, String version,
        String code, String sab) {
        return getSubconceptsBySource(scheme, version, code, sab, true);
    }

    public JSONArray getSubconceptsBySource(String scheme, String version,
        String code, String sab, boolean fromCache) {
        HashMap map = null;
        String key = scheme + "$" + version + "$" + sab + "$" + code;
        JSONArray nodeArray = null;
        if (fromCache) {
            Element element = _cache.get(key);
            if (element != null) {
                nodeArray = (JSONArray) element.getValue();
            }
        }
        if (nodeArray == null) {
            // _logger.debug("Not in cache -- calling getSubconceptsBySource..."
            // );
            // KLO, 041210
            // map = new SourceTreeUtils().getSubconcepts(scheme, version, code,
            // sab);
            map = getChildrenExt(code, sab);
            nodeArray = HashMap2JSONArray(map);
            if (fromCache) {
                try {
                    Element element = new Element(key, nodeArray);
                    _cache.put(element);
                } catch (Exception ex) {

                }
            }
        }
        return nodeArray;
    }

    public JSONArray getRootConceptsBySource(String scheme, String version,
        String sab) {
        return getRootConceptsBySource(scheme, version, sab, true);
    }

    public JSONArray getRootConceptsBySource(String scheme, String version,
        String sab, boolean fromCache) {
        List list = null;// new ArrayList();
        String key = scheme + "$" + version + "$" + sab + "$source_roots";
        JSONArray nodeArray = null;

        if (fromCache) {
            Element element = _cache.get(key);
            if (element != null) {
                nodeArray = (JSONArray) element.getValue();
            }
        }

        if (nodeArray == null) {
            _logger.debug("Not in cache -- calling getSourceHierarchyRoots ");
            try {
                String tag = null;
                CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
                if (version != null)
                    csvt.setVersion(version);
                list =
                    (new SourceTreeUtils()).getSourceHierarchyRoots(scheme,
                        csvt, sab);

                nodeArray = list2JSONArray(list);

                if (fromCache) {
                    Element element = new Element(key, nodeArray);
                    _cache.put(element);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return nodeArray;
    }

    public JSONArray getPathsToRoots(String ontology_display_name,
        String version, String node_id, String sab, boolean fromCache) {
        return getPathsToRoots(ontology_display_name, version, node_id, sab,
            fromCache, -1);
    }

    public JSONArray getPathsToRoots(String ontology_display_name,
        String version, String node_id, String sab, boolean fromCache,
        int maxLevel) {
        JSONArray rootsArray = null;
        if (maxLevel == -1) {
            rootsArray =
                getRootConceptsBySource(ontology_display_name, version, sab);
            try {
                SourceTreeUtils util = new SourceTreeUtils();
                HashMap hmap =
                    util.getTreePathData(ontology_display_name, null, sab,
                        node_id, maxLevel);
                Set keyset = hmap.keySet();
                Object[] objs = keyset.toArray();
                String code = (String) objs[0];

                TreeItem ti = (TreeItem) hmap.get(code); // TreeItem ti = new
                                                         // TreeItem("<Root>",
                                                         // "Root node");

                JSONArray nodesArray = getNodesArray(node_id, ti);
                replaceJSONObjects(rootsArray, nodesArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rootsArray;
        } else {
            try {
                SourceTreeUtils util = new SourceTreeUtils();
                HashMap hmap =
                    util.getTreePathData(ontology_display_name, null, sab,
                        node_id, maxLevel);

                Object[] objs = hmap.keySet().toArray();
                String code = (String) objs[0];

                TreeItem ti = (TreeItem) hmap.get(code);
                List list = util.getTopNodes(ti, sab);

                if (list.size() == 0) {
                    return getRootConceptsBySource(ontology_display_name, null,
                        sab, true);
                }

                rootsArray = list2JSONArray(list);
                JSONArray nodesArray = getNodesArray(node_id, ti);
                replaceJSONObjects(rootsArray, nodesArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rootsArray;
        }
    }

    // /////////////////////////////////////////////////////////////////////////////////////////////////
    // Extension
    // /////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean isLeaf(MetaTreeNode focus) {
        return focus.getExpandedState().equals(ExpandedState.LEAF);
    }

    public String getPathsToRootsExt(String node_id, String sab) {
        String scheme = "NCI Metathesaurus";
        String version = null;
        boolean fromCache = false; // no cache PTR
        return getPathsToRootsExt(scheme, version, node_id, sab, fromCache);
    }

    public String getPathsToRootsExt(String scheme, String version,
        String node_id, String sab, boolean fromCache) {
        LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        MetaBrowserService mbs = null;
        try {
            mbs =
                (MetaBrowserService) lbs
                    .getGenericExtension("metabrowser-extension");

            MetaTree tree = mbs.getMetaNeighborhood(node_id, sab);
            MetaTreeNode focus = tree.getCurrentFocus();
            // caching tree -- to be implemented.
            String key =
                scheme + "$" + version + "$" + node_id + "$" + sab + "$path";
            if (fromCache) {
                Element element = _cache.get(key);
                if (element != null) {
                    String t = (String) element.getValue();
                    return t;
                }
            }
            String t0 = tree.getJsonFromRoot(focus);
            String t1 = "{\"root_nodes\":";
            // String t2 = "}]}";

            String t2 = "}";

            if (t0 != null && fromCache) {
                String t = t1 + t0 + t2;
                Element element = new Element(key, t);
                _cache.put(element);
            }
            if (t0 == null)
                return null;
            return t1 + t0 + t2;
        } catch (Exception ex) {
            // to be modified
            return "[]";
        }
    }

    public JSONArray getPathsToRootsExt(String ontology_display_name,
        String version, String node_id, String sab, boolean fromCache,
        int maxLevel) {
        JSONArray rootsArray = null;
        if (maxLevel == -1) {
            rootsArray =
                getRootConceptsBySource(ontology_display_name, version, sab);
            try {
                SourceTreeUtils util = new SourceTreeUtils();
                HashMap hmap =
                    util.getTreePathData(ontology_display_name, null, sab,
                        node_id, maxLevel);
                Set keyset = hmap.keySet();
                Object[] objs = keyset.toArray();
                String code = (String) objs[0];

                TreeItem ti = (TreeItem) hmap.get(code); // TreeItem ti = new
                                                         // TreeItem("<Root>",
                                                         // "Root node");

                JSONArray nodesArray = getNodesArray(node_id, ti);
                replaceJSONObjects(rootsArray, nodesArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rootsArray;
        } else {
            try {
                SourceTreeUtils util = new SourceTreeUtils();
                HashMap hmap =
                    util.getTreePathData(ontology_display_name, null, sab,
                        node_id, maxLevel);

                Object[] objs = hmap.keySet().toArray();
                String code = (String) objs[0];

                TreeItem ti = (TreeItem) hmap.get(code);
                List list = util.getTopNodes(ti, sab);

                if (list.size() == 0) {
                    return getRootConceptsBySource(ontology_display_name, null,
                        sab, true);
                }

                rootsArray = list2JSONArray(list);
                JSONArray nodesArray = getNodesArray(node_id, ti);
                replaceJSONObjects(rootsArray, nodesArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rootsArray;
        }
    }

    //
    public HashMap getChildrenExt(String CUI, String SAB) {
        HashSet hset = new HashSet();
        HashMap hmap = new HashMap();
        TreeItem ti = null;

        int max = NCImBrowserProperties.getSubconceptPageSize();
        Vector v = new Vector();
        String childNavText = "CHD";
        boolean hasMoreChildren = false;
        try {
            LexBIGService lbs = RemoteServerUtil.createLexBIGService();
            MetaBrowserService mbs =
                (MetaBrowserService) lbs
                    .getGenericExtension("metabrowser-extension");
            MetaTree tree = mbs.getMetaNeighborhood(CUI, SAB);
            MetaTreeNode focus = tree.getCurrentFocus();
            ti = new TreeItem(focus.getCui(), focus.getName());
            if (isLeaf(focus)) {
                ti._expandable = false;
                hmap.put(ti._code, ti);
                return hmap;
            } else {
                ti._expandable = true;
            }

            Iterator iterator = focus.getChildren();
            if (iterator == null) {
                hmap.put(ti._code, ti);
                return hmap;
            }

            int knt = 0;
            hasMoreChildren = false;
            while (iterator.hasNext()) {
                knt++;
                if (knt > max) {
                    hasMoreChildren = true;
                    break;
                }
                MetaTreeNode child = (MetaTreeNode) iterator.next();
                if (!hset.contains(child.getCui())) {
                    TreeItem childItem =
                        new TreeItem(child.getCui(), child.getName());
                    childItem._expandable = true;
                    if (isLeaf(child)) {
                        childItem._expandable = false;
                    }
                    v.add(childItem);
                    hset.add(child.getCui());
                }
            }
        } catch (Exception e) {

        }
        v = SortUtils.quickSort(v);
        for (int i = 0; i < v.size(); i++) {
            TreeItem childItem = (TreeItem) v.elementAt(i);
            ti.addChild(childNavText, childItem);
        }
        if (hasMoreChildren) {
            String t = CUI + "|" + SAB + "|" + max;
            TreeItem childItem = new TreeItem(t, "...");
            childItem._expandable = true;
            ti.addChild(childNavText, childItem);
        }
        if (ti != null) {
        	hmap.put(ti._code, ti);
		}
        return hmap;
    }

    public static Vector<String> parseData(String line) {
        return parseData(line, "|");
    }

    public static Vector<String> parseData(String line, String tab) {
        Vector data_vec = new Vector();
        StringTokenizer st = new StringTokenizer(line, tab);
        while (st.hasMoreTokens()) {
            String value = st.nextToken();
            if (value.compareTo("null") == 0)
                value = " ";
            data_vec.add(value);
        }
        return data_vec;
    }

    public JSONArray getRemainingSubconcepts(String s) {
        HashMap hmap = null;
        Vector v = parseData(s);
        if (v.size() == 3) {
            String CUI = (String) v.elementAt(0);
            String SAB = (String) v.elementAt(1);
            String istart_str = (String) v.elementAt(2);

            int records_to_skip = 0; // to skip
            if (istart_str != null) {
                records_to_skip = Integer.parseInt(istart_str);
            }
            hmap = getRemainingSubconcepts(CUI, SAB, records_to_skip);

        } else if (v.size() == 4) { // contains nodes_to_exclude
            String CUI = (String) v.elementAt(0);
            String SAB = (String) v.elementAt(1);
            String nodes_to_exclude_str = (String) v.elementAt(2);
            String batch_number_str = (String) v.elementAt(3);
            int batch_number = 0; // to skip
            if (batch_number_str != null) {
                batch_number = Integer.parseInt(batch_number_str);
            }
            // code: parent CUI|SAB|n
            hmap =
                getRemainingSubconcepts(CUI, SAB, nodes_to_exclude_str,
                    batch_number);
        } else {
            _logger
                .warn("WARNING: getRemainingSubconcepts invalid input string: "
                    + s);
        }

        if (hmap == null)
            return null;
        JSONArray nodeArray = null;
        nodeArray = HashMap2JSONArray(hmap);
        return nodeArray;
    }

    public HashMap getRemainingSubconcepts(String CUI, String SAB,
        int records_to_skip) {
        HashSet hset = new HashSet();
        HashMap hmap = new HashMap();
        TreeItem ti = null;
        int max = NCImBrowserProperties.getSubconceptPageSize();
        Vector v = new Vector();
        String childNavText = "CHD";
        boolean hasMoreChildren = false;
        int knt0 = 0;
        try {
            LexBIGService lbs = RemoteServerUtil.createLexBIGService();
            MetaBrowserService mbs =
                (MetaBrowserService) lbs
                    .getGenericExtension("metabrowser-extension");
            MetaTree tree = mbs.getMetaNeighborhood(CUI, SAB);
            MetaTreeNode focus = tree.getCurrentFocus();
            ti = new TreeItem(focus.getCui(), focus.getName());
            if (isLeaf(focus)) {
                ti._expandable = false;
                hmap.put(ti._code, ti);
                return hmap;
            } else {
                ti._expandable = true;
            }

            Iterator iterator = focus.getChildren();
            if (iterator == null) {
                hmap.put(ti._code, ti);
                return hmap;
            }

            int knt = 0;
            hasMoreChildren = false;
            while (iterator.hasNext()) {
                if (knt0 < records_to_skip) {
                    MetaTreeNode child = (MetaTreeNode) iterator.next();
                    knt0++;
                } else {
                    knt++;
                    if (knt > max) {
                        hasMoreChildren = true;
                        break;
                    }
                    knt0++;
                    MetaTreeNode child = (MetaTreeNode) iterator.next();
                    if (!hset.contains(child.getCui())) {
                        TreeItem childItem =
                            new TreeItem(child.getCui(), child.getName());
                        childItem._expandable = true;
                        if (isLeaf(child)) {
                            childItem._expandable = false;
                        }
                        v.add(childItem);
                        hset.add(child.getCui());
                    }
                }
            }

            if (v.size() > 0) {
                v = SortUtils.quickSort(v);
                for (int i = 0; i < v.size(); i++) {
                    TreeItem childItem = (TreeItem) v.elementAt(i);
                    ti.addChild(childNavText, childItem);
                }
            }

            if (iterator.hasNext()) {
                int to_skip = records_to_skip + v.size();
                String t = CUI + "|" + SAB + "|" + to_skip;
                TreeItem childItem = new TreeItem(t, "...");
                childItem._expandable = true;
                ti.addChild(childNavText, childItem);
            }
            hmap.put(ti._code, ti);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hmap;
    }

    public HashMap getRemainingSubconcepts(String CUI, String SAB,
        String nodes_to_exclude_str, int batch_number) {
        HashSet hset = new HashSet();
        HashMap hmap = new HashMap();
        TreeItem ti = null;

        Vector nodes_to_exclude = parseData(nodes_to_exclude_str, "$");

        int max = NCImBrowserProperties.getSubconceptPageSize();
        int istart = batch_number * max;
        int iend = istart + max - 1;
        Vector v = new Vector();
        String childNavText = "CHD";
        boolean hasMoreChildren = false;
        int knt0 = 0;
        try {
            LexBIGService lbs = RemoteServerUtil.createLexBIGService();
            MetaBrowserService mbs =
                (MetaBrowserService) lbs
                    .getGenericExtension("metabrowser-extension");
            MetaTree tree = mbs.getMetaNeighborhood(CUI, SAB);
            MetaTreeNode focus = tree.getCurrentFocus();
            ti = new TreeItem(focus.getCui(), focus.getName());
            if (isLeaf(focus)) {
                ti._expandable = false;
                hmap.put(ti._code, ti);
                return hmap;
            } else {
                ti._expandable = true;
            }

            Iterator iterator = focus.getChildren();
            if (iterator == null) {
                hmap.put(ti._code, ti);
                return hmap;
            }

            int knt = 0;
            while (iterator.hasNext()) {
                // hasMoreChildren = true;
                if (v.size() == max)
                    break;
                MetaTreeNode child = (MetaTreeNode) iterator.next();
                if (!nodes_to_exclude.contains(child.getCui())) {
                    if (!hset.contains(child.getCui())) {
                        if (knt >= istart && knt <= iend) {
                            TreeItem childItem =
                                new TreeItem(child.getCui(), child.getName());
                            childItem._expandable = true;
                            if (isLeaf(child)) {
                                childItem._expandable = false;
                            }
                            v.add(childItem);
                        }
                        knt++;
                        hset.add(child.getCui());
                    }
                }

            }

            if (v.size() > 0) {
                v = SortUtils.quickSort(v);
                for (int i = 0; i < v.size(); i++) {
                    TreeItem childItem = (TreeItem) v.elementAt(i);
                    ti.addChild(childNavText, childItem);
                }
            }

            if (iterator.hasNext()) {
                batch_number++;
                String t =
                    CUI + "|" + SAB + "|" + "|" + nodes_to_exclude_str + "|"
                        + batch_number;
                TreeItem childItem = new TreeItem(t, "...");
                childItem._expandable = true;
                ti.addChild(childNavText, childItem);
            }

            hmap.put(ti._code, ti);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hmap;
    }

    // /////////////////////////////////////////////////////////////////////////////////////////////////

    private JSONArray list2JSONArray(List list) {
        JSONArray nodesArray = null;
        try {
            if (list != null) {
                nodesArray = new JSONArray();
                for (int i = 0; i < list.size(); i++) {
                    Object obj = list.get(i);
                    String code = "";
                    String name = "";
                    if (obj instanceof ResolvedConceptReference) {
                        ResolvedConceptReference node =
                            (ResolvedConceptReference) list.get(i);
                        code = node.getConceptCode();
                        name = node.getEntityDescription().getContent();
                    } else if (obj instanceof Concept) {
                        Concept node = (Concept) list.get(i);
                        code = node.getEntityCode();
                        name = node.getEntityDescription().getContent();
                    }
                    ResolvedConceptReference node =
                        (ResolvedConceptReference) list.get(i);
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
        //JSONObject json = new JSONObject();
        JSONArray nodesArray = null;
        try {
            nodesArray = new JSONArray();
            Set keyset = hmap.keySet();
            Object[] objs = keyset.toArray();
            String code = (String) objs[0];
            TreeItem ti = (TreeItem) hmap.get(code);
            for (String association : ti._assocToChildMap.keySet()) {
                List<TreeItem> children = ti._assocToChildMap.get(association);

                SortUtils.quickSort(children);

                for (TreeItem childItem : children) {
                    JSONObject nodeObject = new JSONObject();
                    nodeObject.put(ONTOLOGY_NODE_ID, childItem._code);
                    nodeObject.put(ONTOLOGY_NODE_NAME, childItem._text);
                    int knt = 0;
                    if (childItem._expandable) {
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

    public JSONArray getPathsToRoots(String scheme, String version, String code) {
        List list = null;// new ArrayList();
        String key = scheme + "$" + version + "$" + code + "$path";
        JSONArray nodeArray = null;
        Element element = _cache.get(key);
        if (element != null) {
            nodeArray = (JSONArray) element.getValue();
        }

        if (nodeArray == null) {
            _logger.debug("Not in cache -- calling getHierarchyRoots ");
            try {
                nodeArray = getPathsToRoots(scheme, version, code, true);
                element = new Element(key, nodeArray);
                _cache.put(element);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return nodeArray;
    }

    public JSONArray getPathsToRoots(String ontology_display_name,
        String version, String node_id, boolean fromCache) {
        return getPathsToRoots(ontology_display_name, version, node_id,
            fromCache, -1);
    }

    public JSONArray getPathsToRoots(String ontology_display_name,
        String version, String node_id, boolean fromCache, int maxLevel) {
        JSONArray rootsArray = null;
        if (maxLevel == -1) {
            rootsArray = getRootConcepts(ontology_display_name, version, false);
            try {
                MetaTreeUtils util = new MetaTreeUtils();
                HashMap hmap =
                    util.getTreePathData(ontology_display_name, null, null,
                        node_id, maxLevel);
                Set keyset = hmap.keySet();
                Object[] objs = keyset.toArray();
                String code = (String) objs[0];
                TreeItem ti = (TreeItem) hmap.get(code); // TreeItem ti = new
                                                         // TreeItem("<Root>",
                                                         // "Root node");

                JSONArray nodesArray = getNodesArray(node_id, ti);
                replaceJSONObjects(rootsArray, nodesArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rootsArray;
        } else {
            try {
                MetaTreeUtils util = new MetaTreeUtils();
                HashMap hmap =
                    util.getTreePathData(ontology_display_name, null, "NCI",
                        node_id, maxLevel);

                Object[] objs = hmap.keySet().toArray();
                String code = (String) objs[0];
                TreeItem ti = (TreeItem) hmap.get(code);

                List list = util.getTopNodes(ti);
                rootsArray = list2JSONArray(list);
                JSONArray nodesArray = getNodesArray(node_id, ti);
                replaceJSONObjects(rootsArray, nodesArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rootsArray;
        }
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void replaceJSONObject(JSONArray nodesArray, JSONObject obj) {
        String obj_id = null;

        try {
            obj_id = (String) obj.get(ONTOLOGY_NODE_ID);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        for (int i = 0; i < nodesArray.length(); i++) {
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
        for (int i = 0; i < nodesArray2.length(); i++) {
            try {
                JSONObject obj = nodesArray2.getJSONObject(i);
                replaceJSONObject(nodesArray, obj);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private int findFocusNodePosition(String node_id, List<TreeItem> children) {
        for (int i = 0; i < children.size(); i++) {
            TreeItem childItem = (TreeItem) children.get(i);
            if (node_id.compareTo(childItem._code) == 0)
                return i;
        }
        return -1;
    }

    private JSONArray getNodesArray(String node_id, TreeItem ti) {
        JSONArray nodesArray = new JSONArray();
        for (String association : ti._assocToChildMap.keySet()) {
            List<TreeItem> children = ti._assocToChildMap.get(association);

            // KLO 020410
            SortUtils.quickSort(children);

            int cut_off = 200;
            int m = cut_off / 2;
            int m2 = m / 2;

            if (children.size() <= cut_off) {
                for (int i = 0; i < children.size(); i++) {
                    TreeItem childItem = (TreeItem) children.get(i);

                    int knt = 0;
                    if (childItem._expandable) {
                        knt = 1;
                    }

                    JSONObject nodeObject = new JSONObject();
                    try {
                        nodeObject.put(ONTOLOGY_NODE_ID, childItem._code);
                        nodeObject.put(ONTOLOGY_NODE_NAME, childItem._text);

                        nodeObject.put(ONTOLOGY_NODE_CHILD_COUNT, knt);
                        nodeObject.put(CHILDREN_NODES, getNodesArray(node_id,
                            childItem));

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
                    for (int i = 0; i < children.size(); i++) {
                        TreeItem childItem = (TreeItem) children.get(i);
                        int knt = 0;
                        if (childItem._expandable) {
                            knt = 1;
                        }
                        JSONObject nodeObject = new JSONObject();
                        try {
                            nodeObject.put(ONTOLOGY_NODE_ID, childItem._code);
                            nodeObject.put(ONTOLOGY_NODE_NAME, childItem._text);
                            nodeObject.put(ONTOLOGY_NODE_CHILD_COUNT, knt);
                            nodeObject.put(CHILDREN_NODES, getNodesArray(
                                node_id, childItem));

                            nodesArray.put(nodeObject);
                        } catch (Exception ex) {
							ex.printStackTrace();
                        }
                    }
                } else {
                    if (pos < m) {
                        min = 0;
                        max = m + 1;
                        if (max > len)
                            max = len;
                    } else {
                        if (pos - m2 > 0)
                            min = pos - m2;
                        if (pos + m2 < max)
                            max = pos + m2;
                    }

                    JSONObject nodeObject = null;
                    for (int i = min; i < max; i++) {
                        TreeItem childItem = (TreeItem) children.get(i);
                        int knt = 0;
                        if (childItem._expandable) {
                            knt = 1;
                        }
                        nodeObject = new JSONObject();
                        try {
                            nodeObject.put(ONTOLOGY_NODE_ID, childItem._code);
                            nodeObject.put(ONTOLOGY_NODE_NAME, childItem._text);
                            nodeObject.put(ONTOLOGY_NODE_CHILD_COUNT, knt);
                            nodeObject.put(CHILDREN_NODES, getNodesArray(
                                node_id, childItem));
                            nodesArray.put(nodeObject);
                        } catch (Exception ex) {
							ex.printStackTrace();
                        }
                    }

                    nodeObject = new JSONObject();
                    try {
                        nodeObject.put(ONTOLOGY_NODE_ID, node_id);
                        nodeObject.put(ONTOLOGY_NODE_NAME,
                            "(Too many sibling nodes -- only " + m
                                + " of a total " + len + " are displayed.)");
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

    public HashMap getSourceRoots(String CUI, String SAB) {
        HashSet hset = new HashSet();
        HashMap hmap = new HashMap();
        TreeItem ti = null;
        Vector v = new Vector();
        String childNavText = "CHD";
        boolean hasMoreChildren = false;
        try {
            LexBIGService lbs = RemoteServerUtil.createLexBIGService();
            MetaBrowserService mbs =
                (MetaBrowserService) lbs
                    .getGenericExtension("metabrowser-extension");
            MetaTree tree = mbs.getMetaNeighborhood(CUI, SAB);
            MetaTreeNode focus = tree.getCurrentFocus();
            ti = new TreeItem(focus.getCui(), focus.getName());
            if (isLeaf(focus)) {
                ti._expandable = false;
                hmap.put(ti._code, ti);
                return hmap;
            } else {
                ti._expandable = true;
            }

            Iterator iterator = focus.getChildren();
            if (iterator == null) {
                hmap.put(ti._code, ti);
                return hmap;
            }

            int knt = 0;
            hasMoreChildren = false;
            while (iterator.hasNext()) {
                MetaTreeNode child = (MetaTreeNode) iterator.next();
                if (!hset.contains(child.getCui())) {
                    TreeItem childItem =
                        new TreeItem(child.getCui(), child.getName());
                    childItem._expandable = true;
                    if (isLeaf(child)) {
                        childItem._expandable = false;
                    }
                    v.add(childItem);
                    hset.add(child.getCui());
                }
            }
        } catch (Exception e) {

        }
        v = SortUtils.quickSort(v);
        for (int i = 0; i < v.size(); i++) {
            TreeItem childItem = (TreeItem) v.elementAt(i);
            ti.addChild(childNavText, childItem);
        }
        if (ti != null) {
        	hmap.put(ti._code, ti);
		}
        return hmap;
    }

    public JSONArray getSourceRoots(String scheme, String version, String sab,
        boolean fromCache) {
        HashMap map = null;
        String key = scheme + "$" + version + "$" + sab;
        JSONArray nodeArray = null;
        if (fromCache) {
            Element element = _cache.get(key);
            if (element != null) {
                nodeArray = (JSONArray) element.getValue();
            }
        }
        if (nodeArray == null) {
            ResolvedConceptReference src_root =
                SourceTreeUtils.getRootInSRC(scheme, version, sab);
            if (src_root == null)
                return null;

            map = getSourceRoots(src_root.getConceptCode(), sab);
            nodeArray = HashMap2JSONArray(map);
            if (fromCache) {
                try {
                    Element element = new Element(key, nodeArray);
                    _cache.put(element);
                } catch (Exception ex) {

                }
            }
        }
        return nodeArray;
    }

}
