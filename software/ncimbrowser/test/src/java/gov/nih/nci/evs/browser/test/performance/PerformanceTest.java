/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.test.performance;

import gov.nih.nci.evs.browser.test.utils.*;

public class PerformanceTest extends TestBase {
    public PerformanceTest(int choice, String[] args) {
        super(choice, args);
    }
    
    protected void displayOptions() {
        println(getClass().getSimpleName() + " Menu:");
        println(INDENT + "1) " + "SearchUtilsTest");
        println(INDENT + "2) " + "RelationshipTest");
        println(INDENT + "3) " + "RelationshipTest2");
        println(INDENT + "4) " + "BySourceTest");
        println(INDENT + "5) " + "BySourceTest2");
    }
    
    protected boolean runOption(int choice, String[] args) throws Exception {
        switch (choice) {
        case 1: SearchUtilsTest.main(args); return true;
        case 2: RelationshipTest.main(args); return true;
        case 3: RelationshipTest2.main(args); return true;
        case 4: BySourceTest.main(args); return true;
        case 5: BySourceTest2.main(args); return true;
        default: return false;
        }
    }

    public static void main(String[] args) {
//        new PerformanceTest(3, args);
        new PerformanceTest(0, args);
    }    
}
