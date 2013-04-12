/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.bean;

import java.io.*;
import java.util.*;

import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.apache.log4j.*;

import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.properties.*;

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

public class IteratorBean extends Object {
    private static Logger _logger = Logger.getLogger(IteratorBean.class);
    private static int DEFAULT_MAX_RETURN = 100;
    private ResolvedConceptReferencesIterator _iterator = null;
    private int _size = 0;
    private List _list = null;

    private int _pageNumber;
    private int _pageSize;
    private int _startIndex;
    private int _endIndex;
    private int _numberOfPages;

    private int _lastResolved;
    private int _maxReturn = 100;

    private String _matchText = null;

    private String _key = null;
    private boolean _timeout = false;

    public String _randomNumberString = null;

    public IteratorBean(ResolvedConceptReferencesIterator iterator) {
        _iterator = iterator;
        _maxReturn = DEFAULT_MAX_RETURN;
        initialize();
    }

    public IteratorBean(ResolvedConceptReferencesIterator iterator,
        int maxReturn) {
        _iterator = iterator;
        _maxReturn = maxReturn;
        initialize();
    }

    public int getNumberOfPages() {
        return _numberOfPages;
    }

    public void setIterator(ResolvedConceptReferencesIterator iterator) {
        _iterator = iterator;
        _maxReturn = DEFAULT_MAX_RETURN;
        initialize();
    }

    public ResolvedConceptReferencesIterator getIterator() {
        return _iterator;
    }

    public boolean getTimeout() {
        return _timeout;
    }

    public String getRandomNumberString() {
        return _randomNumberString;
    }

    public void initialize() {
        try {
            if (_iterator == null) {
                _size = 0;
            } else {
                _size = _iterator.numberRemaining();

 System.out.println("Iterator beam _iterator.numberRemaining() " + _iterator.numberRemaining());

            }

            int randomNumber = new Random().nextInt();
            _randomNumberString = Integer.toString(randomNumber);

            _pageNumber = 1;
            /*
             * list = new ArrayList(size); for (int i=0; i<size; i++) {
             * list.add(null); }
             */
            _list = new ArrayList();

            _pageSize = Constants.DEFAULT_PAGE_SIZE;
            _numberOfPages = _size / _pageSize;
            if (_pageSize * _numberOfPages < _size) {
                _numberOfPages = _numberOfPages + 1;
            }
            _lastResolved = -1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getMumberOfPages() {
        return _numberOfPages;
    }

    public int getSize() {
        return _size;
    }

    public void setPageSize(int pageSize) {
        _pageSize = pageSize;
    }

    public int getPageSize() {
        return _pageSize;
    }

    public int getLastResolved() {
        return _lastResolved;
    }

    public int getStartIndex(int pageNumber) {
        _startIndex = (pageNumber - 1) * _pageSize;
        if (_startIndex < 0)
            _startIndex = 0;
        return _startIndex;
    }

    public int getEndIndex(int pageNumber) {
        _endIndex = pageNumber * _pageSize - 1;
        if (_endIndex > (_size - 1))
            _endIndex = _size - 1;
        return _endIndex;
    }

    public List getData(int pageNumber) {
        int idx1 = getStartIndex(pageNumber);
        int idx2 = getEndIndex(pageNumber);
        return getData(idx1, idx2);
    }

    public List getData(int idx1, int idx2) {
        _logger.debug("IteratorBean Retrieving data (from: " + idx1 + " to: "
            + idx2 + ")");

        long ms = System.currentTimeMillis();
        long dt = 0;
        long total_delay = 0;
        _timeout = false;
        try {
            while (_iterator != null && _iterator.hasNext()
                && _lastResolved < idx2 && _lastResolved < _size) {
                ResolvedConceptReference[] refs =
                    _iterator.next(_maxReturn).getResolvedConceptReference();

                _logger.debug("IteratorBean iterator.next(" + _maxReturn
                    + ") returns refs: " + refs.length);

                for (ResolvedConceptReference ref : refs) {
                    _lastResolved++;
                    // list.set(lastResolved, ref);
                    _list.add(ref);
                }

                _logger.debug("Advancing iterator: " + _lastResolved);

                dt = System.currentTimeMillis() - ms;
                ms = System.currentTimeMillis();
                total_delay = total_delay + dt;
                if (total_delay > NCImBrowserProperties.getPaginationTimeOut() * 60 * 1000) {
                    _timeout = true;

                    _logger.debug("Time out at: " + _lastResolved);
                    break;
                }
            }
        } catch (Exception ex) {

        }

        List rcr_list = new ArrayList();
        // KLO 012710
        if (idx2 <= 0)
            idx2 = 1;
        int upper = idx2;
        /*
         * if (upper >= lastResolved) upper = lastResolved; if (upper <= 0)
         * upper = 1;
         */
        if (upper > _list.size())
            upper = _list.size();
        for (int i = idx1; i < upper; i++) {
            ResolvedConceptReference rcr =
                (ResolvedConceptReference) _list.get(i);
            rcr_list.add(rcr);
            // if (i > lastResolved) break;
            // if (i >= lastResolved) break;
        }

        _logger.debug("getData Run time (ms): "
            + (System.currentTimeMillis() - ms));

        return rcr_list;
    }

    protected void displayRef(ResolvedConceptReference ref) {
        _logger.debug(ref.getConceptCode() + ":"
            + ref.getEntityDescription().getContent());
    }

    protected void displayRef(int k, ResolvedConceptReference ref) {
        _logger.debug("(" + k + ") " + ref.getCodingSchemeName() + " "
            + ref.getConceptCode() + ":"
            + ref.getEntityDescription().getContent());
    }

    protected void displayRef(OutputStreamWriter osWriter, int k,
        ResolvedConceptReference ref) {
        try {
            osWriter.write("(" + k + ") " + ref.getConceptCode() + ":"
                + ref.getEntityDescription().getContent() + "\n");
        } catch (Exception ex) {

        }
    }

    public void dumpData(List list) {
        if (list == null) {
            _logger.warn("WARNING: dumpData list = null???");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            ResolvedConceptReference rcr =
                (ResolvedConceptReference) list.get(i);
            int j = i + 1;
            displayRef(j, rcr);
        }
    }

    public void dumpData(OutputStreamWriter osWriter, List list) {
        if (list == null) {
            _logger.warn("WARNING: dumpData list = null???");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            ResolvedConceptReference rcr =
                (ResolvedConceptReference) list.get(i);
            int j = i + 1;
            displayRef(osWriter, j, rcr);
        }
    }

    public void setKey(String key) {
        _logger.debug("IteratorBean setKey: " + key);
        _key = key;
    }

    public String getKey() {
        return _key;
    }

    public void setMatchText(String matchText) {
        _matchText = matchText;
    }

    public String getMatchText() {
        return _matchText;
    }

}