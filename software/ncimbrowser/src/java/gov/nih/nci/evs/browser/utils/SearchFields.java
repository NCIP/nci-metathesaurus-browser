package gov.nih.nci.evs.browser.utils;

import java.util.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction 
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
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class SearchFields {

    public interface Interface {

        // Default getters

        public String getKey();

        public String getType();

        public String getMatchText();

        public String getMatchAlgorithm();
        
        public String getSearchTarget();
        
        public String getSource();
        
        public int getMaxReturn();
        
        public String getPropertyType(); 

        public String getPropertyName(); 
        
        public String getRelSearchAssociation();
        
        public String getRelSearchRela();

    }

    /**
     * @author dyee Class describing fields for a basic (simple) search
     */
    public static class Simple implements Interface {
        public String type;
        public Vector schemes = null;
        public String matchText;
        public String searchTarget;
        public String source;
        public String matchAlgorithm;
        public int maxReturn;
        public String key;

        /**
         * Constructor
         * 
         * @param type
         * @param schemes
         * @param matchText
         * @param searchTarget
         * @param source
         * @param matchAlgorithm
         * @param maxReturn
         */
        public Simple(Vector schemes, String matchText, String searchTarget,
            String source, String matchAlgorithm, int maxReturn) {
            this.type = Simple.class.getSimpleName();
            this.schemes = schemes;
            this.matchText = matchText.trim();
            this.searchTarget = searchTarget;
            this.source = source;
            this.matchAlgorithm = matchAlgorithm;
            this.maxReturn = maxReturn;
            this.key = randomKey();
        }

        public String getKey() {
            return this.key;
        }

        public String getType() {
            return this.type;
        }

        public String getMatchText() {
            return this.matchText;
        }

        public String getMatchAlgorithm() {
            return this.matchAlgorithm;
        }

        public String getSearchTarget() {
            return this.searchTarget;
        }

        public String getSource() {
            return this.source;
        }        

        public int getMaxReturn() {
            return this.maxReturn;
        }         

        public String getPropertyType() {
            return null;
        }        

        public String getPropertyName() {        
            return null;
        }       
        
        public String getRelSearchAssociation() {
            return null;
        }        

        public String getRelSearchRela() {
            return null;
        }         
        
        protected String randomKey() {
            Random random = new Random();
            int i = random.nextInt();
            String s = Integer.toString(i);
            return s;
        }

        public String toString() {
            return "schemes= " + schemes + ", matchText=" + matchText
                + ", searchTarget=" + searchTarget + ", source=" + source
                + ", matchAlgorithm=" + matchAlgorithm + ", maxReturn="
                + maxReturn + ", key=" + key;
        }
    }

    /**
     * @author dyee Class describing fields for a complex property search
     */
    public static class Property extends Simple {
        public String propertyType;
        public String propertyName;

        public Property(Vector schemes, String matchText, String searchTarget,
            String propertyType, String propertyName, String source,
            String matchAlgorithm, int maxReturn) {
            super(schemes, matchText, searchTarget, source, matchAlgorithm,
                maxReturn);
            this.type = Property.class.getSimpleName();
            this.propertyType = propertyType;
            this.propertyName = propertyName;
        }

        public String propertyType() {
            return this.propertyType;
        }        

        public String propertyName() {
            return this.propertyName;
        }        
        
        public String toString() {
            return super.toString() + ", propertyType=" + this.propertyType
                + ", propertyName=" + this.propertyName;
        }
    }

    /**
     * @author dyee Class describing fields for a complex relationship search
     */

    public static class Relationship extends Simple {
        public String relSearchAssociation;
        public String relSearchRela;

        public Relationship(Vector schemes, String matchText,
            String searchTarget, String relSearchAssociation,
            String relSearchRela, String source, String matchAlgorithm,
            int maxReturn) {
            super(schemes, matchText, searchTarget, source, matchAlgorithm,
                maxReturn);
            this.type = Relationship.class.getSimpleName();
            this.relSearchAssociation = relSearchAssociation;
            this.relSearchRela = relSearchRela;
        }

        public String getRelSearchAssociation() {
            return this.relSearchAssociation;
        }
        
        public String getRelSearchRela() {
            return this.relSearchRela;
        }         
        
        public String toString() {
            return super.toString() + ", relSearchAssociation="
                + relSearchAssociation + ", relSearchRela=" + relSearchRela;
        }
    }
}
