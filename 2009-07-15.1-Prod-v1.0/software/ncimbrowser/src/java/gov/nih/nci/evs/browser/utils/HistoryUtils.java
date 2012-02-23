package gov.nih.nci.evs.browser.utils;

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

//import gov.nih.nci.evs.browser.common.Constants;

public class HistoryUtils {
    //private static final String CODING_SCHEME = "NCI MetaThesaurus";
    private static DateFormat _dataFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public static Vector<String> getTableHeader() {
        Vector<String> v = new Vector<String>();
        v.add("Edit Actions");
        v.add("Date");
        v.add("Reference Concept");
        return v;
    }

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

			//System.out.println(" HistoryUtils date: " + date);

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

            //System.out.println("NCIChangeEvent: " + info);
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

            //System.out.println("NCIChangeEvent: " + info);
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
				System.out.println("lbSvc == null???");
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
					System.out.println("Concep not found.");
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
						System.out.println("Exception entry == null");
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

		   String scheme = "NCI MetaThesaurus";
		   String version = null;
		   String code = "C0260526";

		   code = "C1325880";

		   System.out.println("\n\nTesting getEditActions ...");

           Vector<String> v = test.getEditActions(scheme, version, null, code);
           System.out.println(v.size());

           System.out.println("\n\nTesting getAncestors ...");
           v = test.getAncestors(scheme, version, null, code);
           System.out.println(v.size());

           System.out.println("\n\nTesting getDescendants ...");
           v = test.getDescendants(scheme, version, null, code);
           System.out.println(v.size());

	   } catch (Exception ex) {
		   ex.printStackTrace();
	   }
   }
}

/*
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