/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.bean;

import gov.nih.nci.evs.browser.utils.*;

/**
 * 
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class BeanUtils {
    public static UserSessionBean getUserSessionBean() {
        return (UserSessionBean) HTTPUtils.getBean("userSessionBean",
            "gov.nih.nci.evs.browser.bean.UserSessionBean");
    }

    public static IteratorBeanManager getIteratorBeanManager() {
        return (IteratorBeanManager) HTTPUtils.getBean("iteratorBeanManager",
            "gov.nih.nci.evs.browser.bean.IteratorBeanManager");
    }

    public static SearchStatusBean getSearchStatusBean() {
        return (SearchStatusBean) HTTPUtils.getBean("searchStatusBean",
            "gov.nih.nci.evs.browser.bean.SearchStatusBean");
    }
}
