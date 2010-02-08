package gov.nih.nci.evs.browser.bean;


import java.io.*;
import java.util.*;

import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.concepts.Concept;

//import gov.nih.nci.evs.browser.common.Constants;

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

public class IteratorBeanManager extends Object {

    HashMap iteratorBeanHashMap = null;
    Random random = null;

	public IteratorBeanManager() {
		this.iteratorBeanHashMap = new HashMap();
		random = new Random();
	}

    public String createIteratorKey(Vector schemes, String matchText, String searchTarget, String matchAlgorithm, int maxReturn) {
		 String maxReturn_str = Integer.toString(maxReturn);
		 String key = matchText.trim();
		 key = key + "|" + searchTarget + "|" + matchAlgorithm;
		 for (int i=0; i<schemes.size(); i++) {
			 String scheme = (String) schemes.elementAt(i);
			 key = key + "|" + scheme;
		 }
		 key = key + "|" + maxReturn_str;
		 int randomNumber = random.nextInt();
		 String randomNumber_str = Integer.toString(randomNumber);
         key = key + "|" + randomNumber_str;
         return key;
	}

	public boolean addIteratorBean(IteratorBean bean) {
		String key = bean.getKey();
		if (iteratorBeanHashMap.containsKey(key)) return false;
		iteratorBeanHashMap.put(key, bean);
		return true;
	}

	public IteratorBean getIteratorBean(String key) {
		if (key == null) return null;
		if (!containsIteratorBean(key)) return null;
		return (IteratorBean) iteratorBeanHashMap.get(key);
	}

	public boolean containsIteratorBean(String key) {
		if (key == null) return false;
		return iteratorBeanHashMap.containsKey(key);
	}

	public Vector getKeys() {
		if (iteratorBeanHashMap == null) return null;
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