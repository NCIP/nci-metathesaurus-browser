package gov.nih.nci.evs.testUtil;


import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.testUtil.ui.*;
import java.io.*;
import java.util.*;


/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008-2015 NGIT. This software was developed in conjunction
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
 * Modification history:
 *     Initial implementation kim.ong@ngc.com
 *
 */


public class TestCaseExtractor {

	String classname = null;
	String new_classname = null;

	public static Vector readFile(String filename)
	{
		Vector v = new Vector();
		try {

            FileReader a = new FileReader(filename);
            BufferedReader br = new BufferedReader(a);
            String line;
            line = br.readLine();
            while(line != null){
                v.add(line);
                line = br.readLine();
            }
            br.close();
		} catch (Exception ex) {
            ex.printStackTrace();
		}
		return v;
	}

    public static String searchType2Name(int searchType) {
	    if (searchType == 1) return "SIMPLE_SEARCH_ON_NAME_OR_CODE";
	    else if (searchType == 2) return "SIMPLE_SEARCH_ON_PROPERTY";
	    else if (searchType == 3) return "SIMPLE_SEARCH_ON_RELATIONSHIP";

	    else if (searchType == 4) return "MAPPING_SEARCH_ON_NAME_OR_CODE";
	    else if (searchType == 5) return "MAPPING_SEARCH_ON_PROPERTY";
	    else if (searchType == 6) return "MAPPING_SEARCH_ON_RELATIONSHIP";

	    else if (searchType == 7) return "ALT_MAPPING_SEARCH_ON_NAME_OR_CODE";
	    else if (searchType == 8) return "ALT_MAPPING_SEARCH_ON_PROPERTY";
	    else if (searchType == 9) return "ALT_MAPPING_SEARCH_ON_RELATIONSHIP";

	    else if (searchType == 10) return "VALUE_SET_SEARCH_ON_NAME";
	    else if (searchType == 11) return "VALUE_SET_SEARCH_ON_CODE";

	    else if (searchType == 12) return "MULTIPLE_SEARCH_ON_NAME";
	    else if (searchType == 13) return "MULTIPLE_SEARCH_ON_CODE";
	    else if (searchType == 14) return "MULTIPLE_SEARCH_ON_PROPERTY";
	    else if (searchType == 15) return "MULTIPLE_SEARCH_ON_RELATIONSHIP";

	    else if (searchType == 16) return "ADVANCED_SEARCH_ON_NAME";
	    else if (searchType == 17) return "ADVANCED_SEARCH_ON_CODE";
	    else if (searchType == 18) return "ADVANCED_SEARCH_ON_PROPERTY";
	    else if (searchType == 19) return "ADVANCED_SEARCH_ON_RELATIONSHIP";

	    else if (searchType == 20) return "ALL_TERMINOLOGY_SEARCH_ON_CODE";
	    else if (searchType == 21) return "ALL_TERMINOLOGY_SEARCH_ON_NAME";
	    else if (searchType == 22) return "ALL_TERMINOLOGY_SEARCH_ON_PROPERTY";
	    else if (searchType == 23) return "ALL_TERMINOLOGY_SEARCH_ON_RELATIONSHIP";

	    else if (searchType == 24) return "ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_CODE";
	    else if (searchType == 25) return "ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_NAME";
	    else if (searchType == 26) return "ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_PROPERTY";
	    else if (searchType == 27) return "ALL_BUT_NCIM_TERMINOLOGY_SEARCH_ON_RELATIONSHIP";

	    else if (searchType == 28) return "ALL_VALUE_SET_SEARCH_ON_CODE";
	    else if (searchType == 29) return "ALL_VALUE_SET_SEARCH_ON_NAME";

	    else if (searchType == 30) return "VIEW_HIERARCHY";
	    else if (searchType == 31) return "VIEW_IN_HIERARCHY";
	    else return "Others";
	}

    public static int extractSearchType(String line) {
		int m = line.lastIndexOf(";");
		line = line.substring(0, m);
		line = line.trim();
		int n = line.indexOf("=");
		if (n == -1) return 0;
		String s = line.substring(n+2, line.length());
		int type = Integer.parseInt(s);
		return type;
	}


	public static void extractFailureCases(String inputfile, String failures) {
		System.out.println("Input file: " + inputfile);
		int n = inputfile.lastIndexOf(".java");
		String outputfile = null;
		if (n != -1) {
			String classname = inputfile.substring(0, n);
			outputfile = classname + "Failures.java";
		}

		String method_prefix = "public void testTermBrowserTestCase_";
		Vector targets = new Vector();
		Vector w = StringUtils.parseData(failures);
		for (int i=0; i<w.size(); i++) {
			String t = (String) w.elementAt(i);
			String target = method_prefix + t + "()";
			targets.add(target);
		}

		Vector v = readFile(inputfile);
		Vector searchTypes = new Vector();
		PrintWriter pw = null;
		HashMap caseNumber2TypeMap = new HashMap();
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
			for (int i=0; i<v.size(); i++) {
				String s = (String) v.elementAt(i);
				if (s.indexOf(method_prefix) != -1) {
					boolean isFailureCase = false;
					String target = null;
					for(int j=0; j<targets.size(); j++) {
					    target = (String) targets.elementAt(j);
					    if (s.indexOf(target) != -1) {
							isFailureCase = true;
							break;
						}
					}

					if (!isFailureCase) {
						String s0 = (String) v.elementAt(i-1);
						s0 = "// " + s0;
						v.set(i-1, s0);

					} else {
						String line = (String) v.elementAt(i+2);
						int searchtype = extractSearchType(line);
						String type = searchType2Name(searchtype);
						caseNumber2TypeMap.put(target, type);
						searchTypes.add(type);

					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {

			try {
				pw.println("// Failure/Error cases:");
				for (int k=0; k<w.size(); k++) {
					String t = (String) w.elementAt(k);
					String target = method_prefix + t + "()";
					String value = (String) caseNumber2TypeMap.get(target);
					pw.println("// \t" + target + " (type: " + value + ")");
				}
				pw.println("\n");
				for (int i=0; i<v.size(); i++) {
					String s = (String) v.elementAt(i);
					pw.println(s);
				}
				pw.close();
				System.out.println("Output file " + outputfile + " generated.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String args[]) {
		String classname = null;
		String new_classname = null;
		String filename = "TestNCImBrowserTestCase.java";
		String failures = "";
		if (args.length > 0) {
			filename = args[0];
		}
		if (args.length > 1) {
			failures = args[1];
		}
		extractFailureCases(filename, failures);
	}
}

