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
 * <li> Topology Test (configuration 1)
 * </ul>
 * @author Leslie K. Blair
 */

public class CONFIG1_TOPOLOGY_B6_IMAGE extends TestCase
{
    public static Test suite()
    {
        //Create test suite
        TestSuite testSuite = new TestSuite();

        //Add tests to the test suite.
        //Each method beginning with testXXX() in the specified class will
        //be called as a separate test.
        testSuite.addTestSuite(SignatureTest.class);
        testSuite.addTestSuite(TopologyTestConfig1.class);

        return testSuite;
    }

    public static void main(String[] argv)
    {
        //use the textual Test Runner to call the test suite
        junit.textui.TestRunner.run(suite());
    }
}
