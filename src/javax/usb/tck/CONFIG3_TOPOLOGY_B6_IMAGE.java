package javax.usb.tck;
 
/**
 * Copyright (c) 2004, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */
 
 
import junit.framework.*;
 
/**
 * This class executes the following tests of the Javax.usb TCK Test Suite:
 * <ul>
 * <li> Signature Test
 * <li> Topology Test (configuration 3)
 * <li> Control IO Test
 * <li> Request Test
 * </ul>
 * @author Leslie K. Blair
 */
 
public class CONFIG3_TOPOLOGY_B6_IMAGE extends TestCase
{
    public static Test suite()
    {
        //Create test suite
        TestSuite testSuite = new TestSuite();
 
        //Add tests to the test suite.
        //Each method beginning with testXXX() in the specified class will
        //be called as a separate test.
        testSuite.addTestSuite(SignatureTest.class);
        testSuite.addTestSuite(TopologyTestConfig3.class);
        testSuite.addTestSuite(ControlIOTest.class);
        testSuite.addTestSuite(RequestTestGetDescriptor.class);
        testSuite.addTestSuite(RequestTestGetSetConfiguration.class);
        testSuite.addTestSuite(RequestTestGetSetInterface.class);
        testSuite.addTestSuite(RequestTestGetStatus.class);
        testSuite.addTestSuite(RequestTestSetClearFeature.class);
        testSuite.addTestSuite(RequestTestSynchFrame.class);
        return testSuite;
    }
 
    public static void main(String[] argv)
    {
        //use the textual Test Runner to call the test suite
        junit.textui.TestRunner.run(suite());
    }
}
