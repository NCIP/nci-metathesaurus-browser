package gov.nih.nci.evs.browser.utils;

import gov.nih.nci.evs.browser.common.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Vector;

import org.LexGrid.LexBIG.DataModel.Collections.NCIChangeEventList;
import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Concept;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.apache.log4j.Logger;

public class HistoryUtils {
    private static Logger _logger = Logger.getLogger(HistoryUtils.class);
    private static DateFormat _dataFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public static Vector<String> getTableHeader() {
        Vector<String> v = new Vector<String>();
        v.add("Edit Actions");
        v.add("Date");
        v.add("Reference Concept");
        return v;
    }

/*
    public static Vector<String> getEditActions(String codingSchemeName,
            String vers, String ltag, String code) throws LBException {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        HistoryService hs = lbSvc.getHistoryService(codingSchemeName);//Constants.CODING_SCHEME_NAME);
        return getEditActions(lbSvc, hs, codingSchemeName, vers, ltag, code);
    }

    public static Vector<String> getEditActions(LexBIGService lbSvc, HistoryService hs, String codingSchemeName,
            String vers, String ltag, String code) throws LBException {
        //LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        //HistoryService hs = lbSvc.getHistoryService(codingSchemeName);//Constants.CODING_SCHEME_NAME);

        try {
			NCIChangeEventList list = hs.getEditActionList(Constructors
				.createConceptReference(code, null), null, null);
			return getEditActions(lbSvc, codingSchemeName, vers, ltag, code, list);
	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
    }


    private static Vector<String> getEditActions(String codingSchemeName,
            String vers, String ltag, String code, NCIChangeEventList list) {
        Enumeration<NCIChangeEvent> enumeration = list.enumerateEntry();
        Vector<String> v = new Vector<String>();
        HashSet<String> hset = new HashSet<String>();
        while (enumeration.hasMoreElements()) {
            NCIChangeEvent event = enumeration.nextElement();
            ChangeType type = event.getEditaction();
            Date date = event.getEditDate();

			//_logger.debug(" HistoryUtils date: " + date);

            String rCode = event.getReferencecode();
            String desc = "N/A";
            if (rCode != null && rCode.length() > 0 &&
                    ! rCode.equalsIgnoreCase("null")) {
                Concept c = getConceptByCode(
                    codingSchemeName, vers, ltag, rCode);
                //KLO
                if (c != null) {
					String name = c.getEntityDescription().getContent();
					desc = name + " (Code " + rCode + ")";
			    } else {
					desc = rCode;
				}
            }

            String info = type + "|" + _dataFormatter.format(date) + "|" + desc;
            if (hset.contains(info))
                continue;

            //_logger.debug("NCIChangeEvent: " + info);
            v.add(info);
            hset.add(info);
        }
        return v;
    }

    private static Vector<String> getEditActions(LexBIGService lbSvc, String codingSchemeName,
            String vers, String ltag, String code, NCIChangeEventList list) {
        Enumeration<NCIChangeEvent> enumeration = list.enumerateEntry();
        Vector<String> v = new Vector<String>();
        HashSet<String> hset = new HashSet<String>();
        while (enumeration.hasMoreElements()) {
            NCIChangeEvent event = enumeration.nextElement();
            ChangeType type = event.getEditaction();
            Date date = event.getEditDate();

            String rCode = event.getReferencecode();
            String desc = "N/A";
            if (rCode != null && rCode.length() > 0 &&
                    ! rCode.equalsIgnoreCase("null")) {
                Concept c = getConceptByCode(lbSvc,
                    codingSchemeName, vers, ltag, rCode);
                //KLO
                if (c != null) {
					String name = c.getEntityDescription().getContent();
					desc = name + " (Code " + rCode + ")";
			    } else {
					desc = rCode;
				}
            }

            String info = type + "|" + _dataFormatter.format(date) + "|" + desc;
            if (hset.contains(info))
                continue;

            //_logger.debug("NCIChangeEvent: " + info);
            v.add(info);
            hset.add(info);
        }
        return v;
    }




    public static ConceptReferenceList createConceptReferenceList(String[] codes, String codingSchemeName)
    {
        if (codes == null)
        {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++)
        {
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(codes[i]);
            list.addConceptReference(cr);
        }
        return list;
    }

 	public static Concept getConceptByCode(LexBIGService lbSvc, String codingSchemeName, String vers, String ltag, String code)
	{
        try {
			if (lbSvc == null)
			{
				_logger.warn("lbSvc == null???");
				return null;
			}
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			versionOrTag.setVersion(vers);

			ConceptReferenceList crefs =
				createConceptReferenceList(
					new String[] {code}, codingSchemeName);

			CodedNodeSet cns = null;
			try {
				cns = lbSvc.getCodingSchemeConcepts(codingSchemeName, versionOrTag);
				cns = cns.restrictToCodes(crefs);
				ResolvedConceptReferenceList matches = cns.resolveToList(null, null, null, 1);
				if (matches == null)
				{
					_logger.warn("Concept not found.");
					return null;
				}
                int count = matches.getResolvedConceptReferenceCount();
				// Analyze the result ...
				if (count == 0) return null;
				if (count > 0) {
                    try {
					    ResolvedConceptReference ref =
							(ResolvedConceptReference) matches.enumerateResolvedConceptReference().nextElement();
						Concept entry = ref.getReferencedEntry();
						return entry;
					} catch (Exception ex1) {
						_logger.error("Exception entry == null");
						return null;
					}
				}
		    } catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
		 } catch (Exception e) {
			 e.printStackTrace();
			 return null;
		 }
		 return null;
	}


 	public static Concept getConceptByCode(String codingSchemeName, String vers, String ltag, String code)
	{
        try {
			LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
			return getConceptByCode(lbSvc, codingSchemeName, vers, ltag, code);
		 } catch (Exception e) {
			 e.printStackTrace();
			 return null;
		 }
	}


    public static Vector<String> getAncestors(String codingSchemeName,
            String vers, String ltag, String code) throws LBException {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        HistoryService hs = lbSvc.getHistoryService(codingSchemeName);//Constants.CODING_SCHEME_NAME);

        try {
 			NCIChangeEventList list = hs.getAncestors(Constructors
                     .createConceptReference(code, null));

			return getEditActions(lbSvc, codingSchemeName, vers, ltag, code, list);
	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
    }

    public static Vector<String> getDescendants(String codingSchemeName,
            String vers, String ltag, String code) throws LBException {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        HistoryService hs = lbSvc.getHistoryService(codingSchemeName);//Constants.CODING_SCHEME_NAME);

        try {
 			NCIChangeEventList list = hs.getAncestors(Constructors
                     .createConceptReference(code, null));

			return getEditActions(lbSvc, codingSchemeName, vers, ltag, code, list);
	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
    }


    public static void main(String[] args) {
	   try {
		   HistoryUtils test = new HistoryUtils();

		   String scheme = "NCI Metathesaurus";
		   String version = null;
		   String code = "C0260526";

		   code = "C1325880";

		   _logger.info("\n\nTesting getEditActions ...");

           Vector<String> v = test.getEditActions(scheme, version, null, code);
           _logger.info(v.size());

           _logger.info("\n\nTesting getAncestors ...");
           v = test.getAncestors(scheme, version, null, code);
           _logger.info(v.size());

           _logger.info("\n\nTesting getDescendants ...");
           v = test.getDescendants(scheme, version, null, code);
           _logger.info(v.size());

	   } catch (Exception ex) {
		   ex.printStackTrace();
	   }
   }
*/


	public static boolean isHistoryServiceAvailable(String codingSchemeName) {
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		HistoryService hs = null;
		try {
			hs = lbSvc.getHistoryService(codingSchemeName);
			if (hs != null) return true;
		} catch (Exception ex) {
			_logger.error("Unable to getHistoryService for " + codingSchemeName);
		}
		return false;
	}

	public static Vector<String> getEditActions(String codingSchemeName,
			String vers, String ltag, String code) throws LBException {

		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		HistoryService hs = lbSvc.getHistoryService(codingSchemeName);
		return getEditActions(lbSvc, hs, codingSchemeName, vers, ltag, code);
	}

    //[#23370] NCIt concept history shows wrong Code (KLO, 09/28/09)
    private static Vector<String> getEditActions(LexBIGService lbSvc, HistoryService hs, String codingSchemeName,
            String vers, String ltag, String code) throws LBException {

        try {
			/*
			Concept c = getConceptByCode(codingSchemeName, vers, ltag, code);
			_logger.debug("getConceptByCode codingSchemeName: " + codingSchemeName);
			_logger.debug("getConceptByCode vers: " + vers);
			_logger.debug("getConceptByCode ltag: " + ltag);
			_logger.debug("getConceptByCode code: " + code);

			if (c == null) {
				_logger.warn("getConceptByCode returns null??? ");
			}

			if (c != null) {
				_logger.debug("calling hs.getEditActionList... ");
				NCIChangeEventList list = hs.getEditActionList(Constructors.createConceptReference(code, null), null, null);
				_logger.debug("calling getEditActions... ");
				return getEditActions(codingSchemeName, vers, ltag, code, list);
			}
			*/
			NCIChangeEventList list = hs.getEditActionList(Constructors.createConceptReference(code, null), null, null);
			return getEditActions(codingSchemeName, vers, ltag, code, list);

	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
    }


	private static Vector<String> getEditActions(String codingSchemeName,
			String vers, String ltag, String code, NCIChangeEventList list) {

		Enumeration<NCIChangeEvent> enumeration = list.enumerateEntry();
		LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

		int count =	list.getEntryCount();

		Vector<String> v = new Vector<String>();
		HashSet<String> hset = new HashSet<String>();
		while (enumeration.hasMoreElements()) {
			NCIChangeEvent event = enumeration.nextElement();
			event = convertNCIChangeEvent(lbSvc, codingSchemeName, vers, ltag, code, event);
			ChangeType type = event.getEditaction();
			Date date = event.getEditDate();
			String rCode = event.getReferencecode();
			String desc = "N/A";
			if (rCode != null && rCode.length() > 0
					&& !rCode.equalsIgnoreCase("null")) {
				Concept c = getConceptByCode(codingSchemeName, vers,
						ltag, rCode);
				//KLO
				if (c != null) {
					String name = c.getEntityDescription().getContent();
					desc = name + " (Code " + rCode + ")";
				} else {
					desc = rCode;
				}
			}
			String info = type + "|" + _dataFormatter.format(date) + "|" + desc;
			if (hset.contains(info))
				continue;
			v.add(info);
			hset.add(info);
		}
		return v;
	}

    public static Vector<String> getAncestors(String codingSchemeName,
            String vers, String ltag, String code) {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        HistoryService hs = null;
        try {
			hs = lbSvc.getHistoryService(codingSchemeName);
		} catch (Exception ex) {
			_logger.error("Unable to getHistoryService for " + codingSchemeName);
			return null;
		}

        try {
 			NCIChangeEventList list = hs.getAncestors(Constructors
                     .createConceptReference(code, null));

			int count =	list.getEntryCount();
			return getEditActions(codingSchemeName, vers, ltag, code, list);
	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
    }

    public static Vector<String> getDescendants(String codingSchemeName,
            String vers, String ltag, String code) {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        HistoryService hs = null;
        try {
			hs = lbSvc.getHistoryService(codingSchemeName);
		} catch (Exception ex) {
			_logger.error("Unable to getHistoryService for " + codingSchemeName);
			return null;
		}

        try {
 			NCIChangeEventList list = hs.getDescendants(Constructors
                     .createConceptReference(code, null));

			return getEditActions(codingSchemeName, vers, ltag, code, list);
	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
    }

    public static Vector<String> getDescendantCodes(String codingSchemeName,
            String vers, String ltag, String code) {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        HistoryService hs = null;
        try {
			hs = lbSvc.getHistoryService(codingSchemeName);
		} catch (Exception ex) {
			_logger.error("Unable to getHistoryService for " + codingSchemeName);
			return null;
		}

        Vector<String> v = new Vector<String>();
        try {
 			NCIChangeEventList list = hs.getDescendants(Constructors
                     .createConceptReference(code, null));

			int count =	list.getEntryCount();

			HashSet<String> hset = new HashSet<String>();
			Enumeration<NCIChangeEvent> enumeration = list.enumerateEntry();
			while (enumeration.hasMoreElements()) {
				NCIChangeEvent event = enumeration.nextElement();
				ChangeType type = event.getEditaction();
				Date date = event.getEditDate();
				String rCode = event.getReferencecode();
				String name = "unassigned";
				if (rCode != null && rCode.length() > 0
						&& !rCode.equalsIgnoreCase("null")) {
					Concept c = getConceptByCode(codingSchemeName, vers,
							ltag, rCode);

					if (c != null) {
						name = c.getEntityDescription().getContent();
					}
				}
				String info = name + "|" + rCode;
				if (hset.contains(info))
					continue;
				v.add(info);
				hset.add(info);
			}

	    } catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return v;
	}

    private static NCIChangeEvent convertNCIChangeEvent(LexBIGService lbSvc, String codingSchemeName,
            String vers, String ltag, String code, NCIChangeEvent event) {
        if (event == null) return null;
        ChangeType type = event.getEditaction();
        String type_str = type.toString();
        if (type_str.compareTo("merge") == 0) {
			Date date = event.getEditDate();
			String rCode = event.getReferencecode();
			if (rCode.compareTo(code) == 0) {

				_logger.debug("rCode." + rCode + " == code == " + code);

				try {
					HistoryService hs = lbSvc.getHistoryService(codingSchemeName);
					if (hs == null) {
						_logger.warn("Unable to getHistoryService for " + codingSchemeName);
						return null;
					}
					try {
						_logger.debug("\tcheck Ancestors");

						NCIChangeEventList list = hs.getAncestors(Constructors.createConceptReference(code, null));
						int count =	list.getEntryCount();

						Enumeration<NCIChangeEvent> enumeration = list.enumerateEntry();
						Vector<String> v = new Vector<String>();
						HashSet<String> hset = new HashSet<String>();
						while (enumeration.hasMoreElements()) {
							NCIChangeEvent event2 = enumeration.nextElement();
							String con_code = event2.getConceptcode();
							String ref_code = event2.getReferencecode();

							_logger.debug("\tAncestor -- con_code " + con_code + "; ref_code == " + ref_code);

							Date date2 = event2.getEditDate();
							ChangeType type2 = event2.getEditaction();
							String type_str2 = type2.toString();
							if (type_str.compareTo("merge") == 0 && ref_code.compareTo(con_code) != 0 && ref_code.compareTo(code) == 0 && date.toString().compareTo(date2.toString()) == 0) {
								//_logger.debug("(***) con_code: " + con_code + " ref_code: " + ref_code);

								_logger.debug("\tsubstituting...");
							    event2.setConceptcode(ref_code);
							    event2.setReferencecode(con_code);
							    return event2;
							}
						}
					} catch (Exception ex) {
						//ex.printStackTrace();
						_logger.error("getAncestors throws exception.");
					}

					try {
						_logger.debug("\tcheck Descendants");

						NCIChangeEventList list = hs.getDescendants(Constructors.createConceptReference(code, null));
						Enumeration<NCIChangeEvent> enumeration = list.enumerateEntry();
						Vector<String> v = new Vector<String>();
						HashSet<String> hset = new HashSet<String>();
						while (enumeration.hasMoreElements()) {
							NCIChangeEvent event2 = enumeration.nextElement();
							String con_code = event2.getConceptcode();
							String ref_code = event2.getReferencecode();

							_logger.debug("\tDescendant con_code " + con_code + "; ref_code == " + ref_code);

							Date date2 = event2.getEditDate();
							ChangeType type2 = event2.getEditaction();
							String type_str2 = type2.toString();
							if (type_str.compareTo("merge") == 0 && ref_code.compareTo(con_code) != 0 && ref_code.compareTo(code) == 0 && date.toString().compareTo(date2.toString()) == 0) {
								//_logger.debug("(***) con_code: " + con_code + " ref_code: " + ref_code);

								_logger.debug("\tsubstituting...");
							    event2.setConceptcode(ref_code);
							    event2.setReferencecode(con_code);
							    return event2;
							}
						}
					} catch (Exception ex) {
						//ex.printStackTrace();
						_logger.error("getAncestors throws exception.");
					}

				} catch (Exception ex) {
					//ex.printStackTrace();
					_logger.error("getAncestors throws exception.");
				}
			}

		}

		return event;
	}


    public static ConceptReferenceList createConceptReferenceList(String[] codes, String codingSchemeName)
    {
        if (codes == null)
        {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++)
        {
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(codes[i]);
            list.addConceptReference(cr);
        }
        return list;
    }

    public static Concept getConceptByCode(String codingSchemeName, String vers, String ltag, String code) {
        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
            if (lbSvc == null) {
                _logger.warn("lbSvc == null???");
                return null;
            }
            CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
            if (vers != null) versionOrTag.setVersion(vers);

            ConceptReferenceList crefs = createConceptReferenceList(
                    new String[] { code }, codingSchemeName);

            CodedNodeSet cns = null;
            try {
                cns = lbSvc.getCodingSchemeConcepts(codingSchemeName,
                        versionOrTag);
                cns = cns.restrictToCodes(crefs);
                //ResolvedConceptReferenceList matches = cns.resolveToList(null, null, null, 1);
 				ResolvedConceptReferenceList matches = null;
				try {
					matches = cns.resolveToList(null, null, null, 1);
				} catch (Exception e) {
					_logger.error("cns.resolveToList failed???");
				}

                if (matches == null) {
                    _logger.warn("Concept not found.");
                    return null;
                }
                int count = matches.getResolvedConceptReferenceCount();
                // Analyze the result ...
                if (count == 0)
                    return null;
                if (count > 0) {
                    try {
                        ResolvedConceptReference ref = (ResolvedConceptReference) matches
                                .enumerateResolvedConceptReference()
                                .nextElement();
                        Concept entry = ref.getReferencedEntry();
                        return entry;
                    } catch (Exception ex1) {
                        _logger.error("Exception entry == null");
                        return null;
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    public static void dumpVector(Vector v) {
		if (v == null) return;
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			_logger.debug(t);
		}
	}


    //[#23463] Linking retired concept to corresponding new concept
    public static String getReferencedCUI(String code) {
		code = code.trim();
		_logger.debug("code Length: " + code.length() + " " + code);
		_logger.debug("scheme: " + Constants.CODING_SCHEME_NAME);

		if (code.length() != 8) {
			return null;
		}

		try {
			Vector<String> v = getEditActions(Constants.CODING_SCHEME_NAME, null, null, code);
			if (v != null) {
				if (v.size() == 0) _logger.debug("(*) HistoryUtils.getEditActions returns nothing");
				for (int i=0; i<v.size(); i++) {
					String s = (String) v.elementAt(i);
					//_logger.debug("s: " + s);
//11:55:42,983 INFO  [STDOUT] s: retire|2008-08-01|unclassified Enteroviruses (Code C1040101)
					Vector w = DataUtils.parseData(s, "|");
					String action = (String) w.elementAt(0);
					if (action.compareTo("merge") == 0 || action.compareTo("retire") == 0) {
						String date = (String) w.elementAt(1);
						String nameAndCode = (String) w.elementAt(2);

						_logger.debug("(*) nameAndCode: " + nameAndCode);
						////merge|2006-01-01|LAS17 protein, S cerevisiae (Code C1433544)
						int idx = nameAndCode.indexOf("(Code");
						if (idx != -1) {
							String t = nameAndCode.substring(idx+6, nameAndCode.length()-1);
							_logger.debug("(*) new CUI: " + t);
							return t;
						}
				    }
				}
			} else {
				_logger.warn("(*) HistoryUtils.getEditActions returns null");
			}
		} catch (Exception ex) {

		}
		return null;
	}



	//C1065558|200808|RB|||C1040101|Y|
	//if the user search for C1065558, we should find C1040101 (merge)

    public static void main(String[] args) {
	   try {
		   HistoryUtils test = new HistoryUtils();

		   String scheme = "NCI Metathesaurus";

		   boolean avail = test.isHistoryServiceAvailable(scheme);
		   _logger.info("History service available? " + avail);

		   String version = null;
		   String code = "C0260526";

		   code = "C1325880";
		   code = "C0107699";

           //Breast Carcinoma (CUI C0678222)
           code = "C0678222";
           code = "C0536142";

		   _logger.info("\n\nTesting getEditActions ..." + scheme + " " + code);

           Vector<String> v = test.getEditActions(scheme, version, null, code);

           _logger.info("\n\nExited Testing getEditActions ...");

           if (v != null) {
			   test.dumpVector(v);
		   } else {
			   _logger.warn("\n\ngetEditActions returns null???");
		   }


	   } catch (Exception ex) {
		   ex.printStackTrace();
	   }
   }
}

/*
//C0536142|200601|SY|||C1433544|Y|
//LAS17 protein, S cerevisiae (CUI C1433544)



Testing getEditActions ...
Connect to http://lexevsapi-qa.nci.nih.gov/lexevsapi50
NCIChangeEvent: merge|2007-12-01|cellular process (Code C1325880)
NCIChangeEvent: merge|2006-01-01|cellular process (Code C1325880)
NCIChangeEvent: merge|2007-07-01|cellular process (Code C1325880)
3


Testing getAncestors ...
Connect to http://lexevsapi-qa.nci.nih.gov/lexevsapi50
NCIChangeEvent: merge|2007-12-01|cellular process (Code C1325880)
NCIChangeEvent: merge|2006-01-01|cellular process (Code C1325880)
NCIChangeEvent: merge|2007-07-01|cellular process (Code C1325880)
3


Testing getDescendants ...
Connect to http://lexevsapi-qa.nci.nih.gov/lexevsapi50
NCIChangeEvent: merge|2007-12-01|cellular process (Code C1325880)
NCIChangeEvent: merge|2006-01-01|cellular process (Code C1325880)
NCIChangeEvent: merge|2007-07-01|cellular process (Code C1325880)
3

*/