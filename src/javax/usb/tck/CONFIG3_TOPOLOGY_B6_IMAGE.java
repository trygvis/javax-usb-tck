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
 * ---- -------- -------- ------ -------  ------------------------------------
 * 0000 nnnnnnn           yymmdd          Initial Development
 * $P1           tck.rel1 040916 raulortz Redesign TCK to create base and optional
 *                                        tests. Separate setConfig, setInterface
 *                                        and isochronous transfers as optionals.
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
        testSuite.addTestSuite(TopologyTestConfig3Limited.class);
        testSuite.addTestSuite(ControlIOTest.class);
        testSuite.addTestSuite(RequestTestGetDescriptor.class);
        testSuite.addTestSuite(RequestTestGetConfiguration.class); // Change names without set   @P1C
        testSuite.addTestSuite(RequestTestGetInterface.class);     // no setting anymore here    @P1C
        testSuite.addTestSuite(RequestTestGetStatus.class);
        testSuite.addTestSuite(RequestTestSetClearFeature.class);
        //The SynchFrame does not work with the Cypress Board                                    @P1D3
        return testSuite;
    }
 
    public static void main(String[] argv)
    {
        //use the textual Test Runner to call the test suite
        junit.textui.TestRunner.run(suite());
    }
}
