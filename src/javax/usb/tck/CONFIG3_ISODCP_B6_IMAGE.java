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
 * <li> IRP Test
 * <li> Interface Policy Test
 * <li> Default Control Pipe Test
 * </ul>
 * @author Leslie K. Blair
 */
 
public class CONFIG3_ISODCP_B6_IMAGE extends TestCase
{
    public static Test suite()
    {
        //Create test suite
        TestSuite testSuite = new TestSuite();
 
        //Add tests to the test suite.
        //Each method beginning with testXXX() in the specified class will
        //be called as a separate test.
        testSuite.addTestSuite(SignatureTest.class);
        testSuite.addTestSuite(IrpTest.class);
	testSuite.addTestSuite(UsbInterfacePolicyTest.class);
        // Moved isochronous tests into the optional isochronous tests                           @P1D3
        testSuite.addTestSuite(DefaultControlPipeTestIRP.class);
        testSuite.addTestSuite(DefaultControlPipeTestIRPwithSynchronizedUsbDevice.class);
        testSuite.addTestSuite(DefaultControlPipeTestIRPList.class);
        testSuite.addTestSuite(DefaultControlPipeTestIRPListwithSynchronizedUsbDevice.class);
        testSuite.addTestSuite(DefaultControlPipeTestErrorConditions.class);
        testSuite.addTestSuite(DefaultControlPipeTestErrorConditionswithSynchronizedUsbDevice.class);
	 
        return testSuite;
    }
 
    public static void main(String[] argv)
    {
        //use the textual Test Runner to call the test suite
        junit.textui.TestRunner.run(suite());
    }
}
