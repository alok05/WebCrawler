/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author YADAV
 */
public class PropertiesSuite extends TestCase {
    
    public PropertiesSuite(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite("PropertiesSuite");
        return suite;
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
