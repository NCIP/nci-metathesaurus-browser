package gov.nih.nci.evs.browser.utils;

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

import java.io.*;
import java.util.*;

public class FormatUtils
{

   static final String ncit_url = "http://ncit.nci.nih.gov/ncitbrowser/ConceptReport.jsp?dictionary=NCI%20Thesaurus&";


   public FormatUtils()
   {

   }

  public static Vector<String> findHyperlinks(String t, String target)
  {
	  Vector<String> v = new Vector<String>();
	  boolean found = false;
	  //"aspx?"
	  String t1 = t;
	  String t2 = t;
	  String doubleQuote = "\"";
	  String t6 = null;
	  String replacedWith = null;
	  String t5 = null;

	  for (int i=0; i<t.length()-target.length(); i++) {
          String substr = t.substring(i, i+target.length());
		  if (substr.compareTo(target) == 0) {
			  found = true;
			  t1 = t.substring(0, i);
			  int k1 = i;
			  while (k1 < t.length()-1) {
				  k1++;
				  String c = t.substring(k1, k1+1);
				  if (c.compareTo(doubleQuote) == 0) {
					  t1 = t.substring(0, k1);
					  break;
				  }
			  }
			  int k2 = i;
			  while (k2 > 0) {
				  k2--;
				  String c = t.substring(k2, k2+1);
				  if (c.compareTo(doubleQuote) == 0) {
					  t2 = t.substring(0, k2);
					  break;
				  }
			  }

			  String t3 = t.substring(k2, k1+1);
			  String t4 = t.substring(k2+1, k1);
			  v.add(t4);
		  }
      }
	  return v;
  }

  public static String replaceHyperlinks(String s, String target, String hyperlinktext) {
	  Vector<String> v = findHyperlinks(s, target);
	  String t3 = "";
	  for (int i=0; i<v.size(); i++) {
		 String str = (String) v.elementAt(i);
		 String s1 = str;
		 int n1 = s.indexOf(s1);
		 String t1 = s.substring(0, n1);
		 String t2 = s.substring(n1+str.length(), s.length());
		 s = t2;

		 String link = null;

		 int index = s.indexOf(hyperlinktext);
		 if (index != -1) {
			 String text = s.substring(0, index + hyperlinktext.length());
			 s = s.substring(index + hyperlinktext.length(), s.length());
			 text = text.trim();
			 if (text.charAt(0) == '\"') {
				 text = text.substring(1, text.length());
				 text = text.trim();
			 }
			 if (hyperlinktext.compareTo("NCI Thesaurus") == 0) {
				 str = replaceBrowserURL(str);
			 }
			 link = createHyperLink(str, text);
		 }

		 String s2 = link;
		 t2 = t1 + s2;
    	 t3 = t3 + t2;
	  }
	  t3 = t3 + s;

	  t3 = t3.replaceAll("\"<a", "<a");
	  t3 = t3.replaceAll("</a>\"", "</a>");

	  return t3;
  }


   public static String createHyperLink(String url, String text) {
	   /*
       String s = "<a href=\"javascript:window.open('" + url + "', 'window id', 'status,scrollbars,resizable,width=800,height=500')\", alt='"
                  + text + "'>" + text + "</a>";
	   return s;
	   */


	   String s = "<a href=\"" + url + "\" target=\"_blank\" alt=\"" + text + "\">" + text + "</a>";
	   return s;

   }


   public static String reformatPDQDefinition(String s) {
      String target = "aspx?";
      String t = replaceHyperlinks(s, target, "clinical trials");
	  s = t;
	  target = "jsp?";
	  t = replaceHyperlinks(s, target, "NCI Thesaurus");
	  return t;
   }

   public static String replaceBrowserURL(String url) {
/*
http://ncit.nci.nih.gov/ConceptReport.jsp?dictionary=NCI%20Thesaurus&code=C1723
http://ncit.nci.nih.gov/ncitbrowser/ConceptReport.jsp?dictionary=NCI%20Thesaurus&code=C1723
*/
       int n = url.indexOf("code=");
       if (n != -1) {
		   String t = url.substring(n, url.length());
		   return ncit_url + t;
	   }
	   return url;
   }


   public static void main(String argv[])
   {

      String def = "PDQ Definition: A recombinant, chimeric monoclonal antibody directed against the epidermal growth factor (EGFR) with antineoplastic activity. Cetuximab binds to the extracellular domain of the EGFR, thereby preventing the activation and subsequent dimerization of the receptor; the decrease in receptor activation and dimerization may result in an inhibition in signal transduction and anti-proliferative effects. This agent may inhibit EGFR-dependent primary tumor growth and metastasis. EGFR is overexpressed on the cell surfaces of various solid tumors."
      + " Check for \"http://www.cancer.gov/Search/ClinicalTrialsLink.aspx?id=42384&idtype=1\" active clinical trials or \"http://www.cancer.gov/Search/ClinicalTrialsLink.aspx?id=42384&idtype=1&closed=1\" closed clinical trials using this agent."
      + "(\"http://nciterms.nci.nih.gov:80/NCIBrowser/ConceptReport.jsp?dictionary=NCI_Thesaurus&code=C1723\" NCI Thesaurus)\";";
      FormatUtils test = new FormatUtils();

      String t = test.reformatPDQDefinition(def);
	  System.out.println(t);
   }
}

