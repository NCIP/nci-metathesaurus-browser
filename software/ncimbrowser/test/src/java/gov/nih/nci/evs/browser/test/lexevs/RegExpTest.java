/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.test.lexevs;

import java.util.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.apache.log4j.Logger;

import gov.nih.nci.evs.browser.utils.*;

public class RegExpTest {
    private static Logger _logger = Logger.getLogger(RegExpTest.class);
    private LexBIGService _lbs = RemoteServerUtil.createLexBIGService();
    private Vector<String> _searchTexts = new Vector<String>();
    private String _codingSchemeName = "NCI MetaThesaurus"; //"NCI Thesaurus"
    private String _algorithm = "exactMatch";
    
    public RegExpTest() {
        initializeTestCases();
    }

    private void initializeTestCases() {
        _searchTexts = new Vector<String>();
        _searchTexts.add("blood");
        _searchTexts.add("cell");
    }

    public void testIterator() throws Exception {
        for (int i = 0; i < _searchTexts.size(); i++) {
            String t = (String) _searchTexts.elementAt(i);
            _logger.debug("---------------------------------------------");
            _logger.debug("* codingSchemeName: " + _codingSchemeName);
            _logger.debug("* Search string: " + t);

            CodedNodeSet cns = null;
            try {
                cns = _lbs.getCodingSchemeConcepts(_codingSchemeName, null);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            _logger.debug("* cns: " + (cns == null ? "null" : "not null"));

            cns = cns.restrictToMatchingDesignations(t,
                SearchDesignationOption.ALL, _algorithm, null);
            ResolvedConceptReferencesIterator itr = null;
            int cnt = 0;
            try {
                LocalNameList restrictToProperties = new LocalNameList();
                SortOptionList sortCriteria = null;
                boolean resolveConcepts = false;

                itr = cns.resolve(sortCriteria, null, restrictToProperties,
                    null, resolveConcepts);
                while (itr.hasNext()) {
                    _logger.debug("* hasNext cnt: " + cnt);
                    ResolvedConceptReference[] refs = itr.next(100)
                        .getResolvedConceptReference();
                    for (ResolvedConceptReference ref : refs) {
                        _logger.debug(cnt + ") " + getConceptInfo(ref));
                        cnt++;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (cnt == 0)
                _logger.debug("No match.");
            _logger.debug("");
        }
    }

    public void testList() throws Exception {
        for (int i = 0; i < _searchTexts.size(); i++) {
            String t = (String) _searchTexts.elementAt(i);
            _logger.debug("---------------------------------------------");
            _logger.debug("* Search string: " + t);

            CodedNodeSet cns = _lbs.getCodingSchemeConcepts(_codingSchemeName,
                null);
            cns = cns.restrictToMatchingDesignations(t,
                SearchDesignationOption.ALL, _algorithm, null);
            ResolvedConceptReference[] list = null;
            int cnt = 0;
            try {
                list = cns.resolveToList(null, null, null, 500)
                    .getResolvedConceptReference();

                for (ResolvedConceptReference ref : list) {
                    _logger.debug(cnt + ") " + getConceptInfo(ref));
                    cnt++;
                }
            } catch (Exception ex) {
                _logger.debug("Exception thrown #2");
            }
            if (cnt == 0)
                _logger.debug("No match.");
            _logger.debug("");
        }
    }

    protected String getConceptInfo(ResolvedConceptReference ref) {
        return ref.getConceptCode() + ":" + 
            ref.getEntityDescription().getContent();
    }

    public static void main(String[] args) throws Exception {
        RegExpTest test = new RegExpTest();
        test.testIterator();
        //test.testList();
    }
}
