package gov.nih.nci.evs.browser.utils.test;

//import gov.nih.nci.system.client.ApplicationServiceProvider;

//import gov.nih.nci.system.applicationservice.ApplicationException;
//import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.io.*;
import java.util.*;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;

//import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
//import org.LexGrid.LexBIG.testUtil.LexEVSServiceHolder;

public class RegExpTest {

	private OutputStream os;
	private File root;
	private File output;
	private String completeSource="";
	private OutputStreamWriter osWriter;
	private BufferedReader buff;

	private LexBIGService lbs;

	static String infilename;
	static String outfilename;

	private String searchText = "11: 107384855-107482789";
    private Vector searchText_vec = null;
    private String codingSchemeName = "NCI Thesaurus";
    //private String codingSchemeName = "NCI MetaThesaurus";
    private String algorithm = "exactMatch";

    private void initializeTestCases(File inputfile) {
		searchText_vec = new Vector();
		File in = inputfile;
		try{
			BufferedReader buff= new BufferedReader(new FileReader(in));
			String line=buff.readLine();
			while(line != null){
                searchText_vec.add(line);
			    line=buff.readLine();
			}
			buff.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
    }


    private void initializeTestCases() {
		searchText_vec = new Vector();
/*
searchText_vec.add("cis-3,3?,4?,5,7-pentahydroxyflavane 3-gallate");
searchText_vec.add("(10^-6m)3");

		searchText_vec.add("8p11.21");
		searchText_vec.add("Yp11.1-q11.1");
		searchText_vec.add("6q22.33-q24.1");
		searchText_vec.add("3' Flank");
		searchText_vec.add("(17beta)-17-(1-Oxopropoxy)androst-4-en-3-one");
		searchText_vec.add("{Application}");
		//searchText_vec.add("Anatibant (Code C79554)");
		searchText_vec.add("CYP2D6, g.100C>T");
		searchText_vec.add("CYP2D6*10");
		searchText_vec.add("A/He Mouse");
		searchText_vec.add("ELDERLY (> 65)");
		searchText_vec.add("F");
		searchText_vec.add("11: 107384855-107482789");
		searchText_vec.add("CYP2D6, P34S");
		searchText_vec.add("CYP2D6*10 Allele");
		searchText_vec.add("(17beta)-Estra-1,3,5(10)-triene-3,17-diol");
		searchText_vec.add("\"Blue Bone\" Formation");
		searchText_vec.add("D&C");
		searchText_vec.add("(2S)-N-(3-(4-Carbamimidoylbenzamido)propyl)-1-{2,4-dichloro-3-((2,4-dimethyl-8-quinolyloxy)methyl)phenylsulfonyl}pyrrolidine-2-carboxamide");
*/
searchText_vec.add("adverse");
	}

/*
            LocalNameList restrictToProperties = new LocalNameList();
            SortOptionList sortCriteria = null;
                //Constructors.createSortOptionList(new String[]{"matchToQuery"});
            boolean resolveConcepts = false;
            try {
                // resolve nothing unless sort_by_pt_only is set to false

                if (apply_sort_score && !sort_by_pt_only) resolveConcepts = true;
                try {
                    stopWatch.start();
					long ms = System.currentTimeMillis();
                    iterator = cns.resolve(sortCriteria, null, restrictToProperties, null, resolveConcepts);
 */


	public RegExpTest() throws Exception {
		//lbs = (LexEVSApplicationService)ApplicationServiceProvider.getApplicationServiceFromUrl("http://lexevsapi-stage.nci.nih.gov/lexevsapi50", "EvsServiceInfo");
	    lbs = RemoteServerUtil.createLexBIGService();
	}

	public void testIterator(File output) throws Exception {
		try {
			FileOutputStream os= new FileOutputStream(output);
			OutputStreamWriter osWriter=new OutputStreamWriter(os);

			for (int i=0; i<searchText_vec.size(); i++) {
				String t = (String) searchText_vec.elementAt(i);
				System.out.println("codingSchemeName: " + codingSchemeName);
				System.out.println("Search string: " + t);

				CodedNodeSet cns = null;
				try {
					cns = lbs.getCodingSchemeConcepts(codingSchemeName, null);
				} catch (Exception e2) {
					e2.printStackTrace();
				}

				if (cns == null) {
					System.out.println("cns == null???");
				} else {
					System.out.println("cns != null");
				}

				cns = cns.restrictToMatchingDesignations(t, SearchDesignationOption.ALL, algorithm, null);
				ResolvedConceptReferencesIterator itr = null;
				int knt = 0;
				try {

            LocalNameList restrictToProperties = new LocalNameList();
            SortOptionList sortCriteria = null;
            boolean resolveConcepts = false;

				   //itr = cns.resolve(null, null, null);
				   itr = cns.resolve(sortCriteria, null, restrictToProperties, null, resolveConcepts);
				   while(itr.hasNext()){
					    System.out.println("hasNext knt" + knt);
						ResolvedConceptReference[] refs = itr.next(100).getResolvedConceptReference();
						for(ResolvedConceptReference ref : refs){
							displayRef(ref);
							knt++;
						}
					}
				} catch (Exception ex) {
                    ex.printStackTrace();
				}
				if (knt == 0) System.out.println("No match.");
				System.out.println("\n\n");
			}
			osWriter.close();
		} catch (Exception ex) {

		}
        System.out.println("Output file " + outfilename + " generated.");
	}


	public void testList() throws Exception {
		LexBIGService lbs = RemoteServerUtil.createLexBIGService();//LexEVSServiceHolder.instance().getLexEVSAppService();
        for (int i=0; i<searchText_vec.size(); i++) {
			String t = (String) searchText_vec.elementAt(i);
			System.out.println("Search string: " + t);

			CodedNodeSet cns = lbs.getCodingSchemeConcepts(codingSchemeName, null);
			cns = cns.restrictToMatchingDesignations(t, SearchDesignationOption.ALL, algorithm, null);
			ResolvedConceptReference[] list = null;
			int knt = 0;
			try {
				list = cns.resolveToList(null, null, null, 500).getResolvedConceptReference();

				for(ResolvedConceptReference ref : list){
					displayRef(ref);
					knt++;
				}
			} catch (Exception ex) {
				System.out.println("Exception thrown #2");
			}
			if (knt == 0) System.out.println("No match.");
			System.out.println("\n\n");
		}
	}

	protected void displayRef(ResolvedConceptReference ref){
		System.out.println(ref.getConceptCode() + ":" + ref.getEntityDescription().getContent());
	}


	public static void main(String[] args) throws Exception {
		RegExpTest test = new RegExpTest();
		infilename = args[0];
		outfilename = args[1];
		File infile = new File(infilename);
		File outfile = new File(outfilename);
		test.initializeTestCases(infile);

		test.testIterator(outfile);
	}
}

/*
NCI Thesaurus:

Search string: 8p11.21
C25117:8p11.21



Search string: Yp11.1-q11.1
C25147:Yp11.1-q11.1



Search string: 6q22.33-q24.1
C13835:6q22.33-q24.1



Search string: 3' Flank
No match.



Search string: (17beta)-17-(1-Oxopropoxy)androst-4-en-3-one
C863:Testosterone Propionate



Search string: {Application}
C25397:Application



Search string: CYP2D6, g.100C>T
C46055:CYP2D6*36 Allele
C45617:CYP2D6*10 Allele



Search string: CYP2D6*10
C45617:CYP2D6*10 Allele



Search string: A/He Mouse
C14652:A/He Mouse



Search string: ELDERLY (> 65)
C16268:Elderly



Search string: F
C42552:Farad
C14560:F
C16576:Female
C67902:Femto
C502:Fluorine
C44277:Degree Fahrenheit
C68252:Dietary Fluorine



Search string: 11: 107384855-107482789
C42029:11: 107384855-107482789



Search string: CYP2D6, P34S
C46055:CYP2D6*36 Allele
C45617:CYP2D6*10 Allele



Search string: CYP2D6*10 Allele
C45617:CYP2D6*10 Allele



Search string: (17beta)-Estra-1,3,5(10)-triene-3,17-diol
C478:Therapeutic Estradiol
C2295:Estradiol



Search string: "Blue Bone" Formation
C53969:"Blue Bone" Formation



Search string: D&C
No match.



Search string: (2S)-N-(3-(4-Carbamimidoylbenzamido)propyl)-1-{2,4-dichloro-3-((2
,4-dimethyl-8-quinolyloxy)methyl)phenylsulfonyl}pyrrolidine-2-carboxamide
C79554:Anatibant


================================================================
NCI MetaThesaurus:

Search string: 8p11.21
CL102579:8p11.21



Search string: Yp11.1-q11.1
CL102549:Yp11.1-q11.1



Search string: 6q22.33-q24.1
CL102392:6q22.33-q24.1



Search string: 3' Flank
CL366284:3' Region



Search string: (17beta)-17-(1-Oxopropoxy)androst-4-en-3-one
C0039607:Testosterone Propionate



Search string: {Application}
CL025718:Application



Search string: CYP2D6, g.100C>T
CL342775:CYP2D6*36 Allele
CL335120:CYP2D6*10 Allele



Search string: CYP2D6*10
CL335120:CYP2D6*10 Allele



Search string: A/He Mouse
CL099852:A/He Mouse



Search string: ELDERLY (> 65)
C0001792:Elderly



Search string: F
C0016330:Fluorine
CL372157:Dietary Fluorine
C0015780:Female
C1553038:Femto
C0439109:Upper case eff
CL378341:F
C0456628:Degrees fahrenheit
C1552648:Probability Distribution Type - F
C0439132:Lower case eff
C0582515:farad



Search string: 11: 107384855-107482789
CL332596:11: 107384855-107482789



Search string: CYP2D6, P34S
CL342775:CYP2D6*36 Allele
CL335120:CYP2D6*10 Allele



Search string: CYP2D6*10 Allele
CL335120:CYP2D6*10 Allele



Search string: (17beta)-Estra-1,3,5(10)-triene-3,17-diol
CL026901:Therapeutic Estradiol
C0014912:Estradiol



Search string: "Blue Bone" Formation
CL357664:"Blue Bone" Formation



Search string: D&C
C0012358:Dilation and Curettage



Search string: (2S)-N-(3-(4-Carbamimidoylbenzamido)propyl)-1-{2,4-dichloro-3-((2
,4-dimethyl-8-quinolyloxy)methyl)phenylsulfonyl}pyrrolidine-2-carboxamide
No match.

	public DoubleMetaphoneCoder(File input, File output){
		doubleMetaphone = new DoubleMetaphone();
		try{
			os= new FileOutputStream(output);
			this.output = output;
			this.root=input;
			initialize();
		}
		catch(Exception e){
			e.printStackTrace();

		}
	}

*/