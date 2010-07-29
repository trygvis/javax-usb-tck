package javax.usb.tck;
 
/**
 * Copyright (c) 2004, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */
 
/*
 * Change Activity: See below.
 *
 * FLAG REASON   RELEASE  DATE   WHO      DESCRIPTION
 * ---- -------- -------- ------ -------- ------------------------------------
 * 0000          tck.rel1 040916 raulortz Initial Development. Redesign TCK to 
 *                                        create base and optional tests. 
 *                                        Separate setConfig, setInterface
 *                                        and isochronous transfers as optionals.
 */
 
import junit.framework.*;
 
/**
 * This class executes the following tests of the Javax.usb TCK Test Suite:
 * <ul>
 * <li> Signature Test
 * <li> Request Test Get and Set Interface
 * </ul>
 * @author Raul Ortiz
 */
 
public class OPT_CONFIG3_TOPOLOGY_SET_INTERFACE extends TestCase
{
    public static Test suite()
    {
        //Create test suite
        TestSuite testSuite = new TestSuite();
 
        //Add tests to the test suite.
        //Each method beginning with testXXX() in the specified class will
        //be called as a separate test.
        testSuite.addTestSuite(SignatureTest.class);
        testSuite.addTestSuite(RequestTestGetSetInterface.class);
        return testSuite;
    }
 
    public static void main(String[] argv)
    {
        //use the textual Test Runner to call the test suite
        System.exit(junit.textui.TestRunner.run(suite()).wasSuccessful() ? 0 : 1);
    }
}
