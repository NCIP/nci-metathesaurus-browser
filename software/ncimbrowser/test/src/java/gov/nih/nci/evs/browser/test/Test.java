/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.test;

import gov.nih.nci.evs.browser.test.lexevs.*;
import gov.nih.nci.evs.browser.test.performance.*;
import gov.nih.nci.evs.browser.test.utils.*;

public class Test extends TestBase {
    public Test(int choice, String[] args) {
        super(choice, args);
    }

    protected void displayOptions() {
        println(getClass().getSimpleName() + " Menu:");
        println(INDENT + "1) " + "LexEvsTest");
        println(INDENT + "2) " + "PerformanceTest");
    }
    
    protected boolean runOption(int choice, String[] args) throws Exception {
        switch (choice) {
        case 1: LexEvsTest.main(args); return true;
        case 2: PerformanceTest.main(args); return true;
        default: return false;
        }
    }

    public static void main(String[] args) {
        new Test(2, args);
    }
}
