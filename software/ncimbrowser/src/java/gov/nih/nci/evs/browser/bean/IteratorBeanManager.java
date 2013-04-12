/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.bean;

import java.util.*;

/**
 * 
 */

/**
 * @author EVS Team
 * @version 1.0
 * 
 *          Modification history Initial implementation kim.ong@ngc.com
 * 
 */

public class IteratorBeanManager {
    private HashMap iteratorBeanHashMap = new HashMap();

    public boolean addIteratorBean(IteratorBean bean) {
        String key = bean.getKey();
        if (iteratorBeanHashMap.containsKey(key))
            return false;
        iteratorBeanHashMap.put(key, bean);
        iteratorBeanHashMap.put(bean.getRandomNumberString(), bean);
        return true;
    }

    public IteratorBean getIteratorBean(String key) {
        if (key == null)
            return null;
        if (!containsIteratorBean(key))
            return null;
        return (IteratorBean) iteratorBeanHashMap.get(key);
    }

    public boolean containsIteratorBean(String key) {
        if (key == null)
            return false;
        return iteratorBeanHashMap.containsKey(key);
    }

    public Vector getKeys() {
        if (iteratorBeanHashMap == null)
            return null;
        Vector key_vec = new Vector();
        Iterator iterator = iteratorBeanHashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            IteratorBean bean = (IteratorBean) iteratorBeanHashMap.get(key);
            key_vec.add(bean.getKey());
        }
        return key_vec;
    }

}