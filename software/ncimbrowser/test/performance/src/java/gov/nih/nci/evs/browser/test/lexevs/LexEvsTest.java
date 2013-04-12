/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-metathesaurus-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.test.lexevs;

import gov.nih.nci.evs.browser.test.utils.*;

public class LexEvsTest extends TestBase {
    public LexEvsTest(int choice, String[] args) {
        super(choice, args);
    }

    protected void displayOptions()
    {
        println(getClass().getSimpleName() + " Menu:");
        println(INDENT + "1) " + "RegExpTest");
        println(INDENT + "2) " + "ResolveConceptIteratorTest");
        println(INDENT + "3) " + "TestExtension");
    }
    
    protected boolean runOption(int choice, String[] args) throws Exception {
        switch (choice) {
        case 1: RegExpTest.main(args); return true;
        case 2: ResolveConceptIteratorTest.main(args); return true;
        case 3: TestExtension.main(args); return true;
        default: return false;
        }
    }

    public static void main(String[] args) {
        new LexEvsTest(2, args);
    }
}
