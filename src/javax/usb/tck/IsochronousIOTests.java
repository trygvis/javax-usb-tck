/**
 * Copyright (c) 2004, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */
 
package javax.usb.tck;
 
import java.util.*;
 
import javax.usb.*;
 
 
 
import junit.framework.*;
 
/**
 * Isochronous IO Test -- Synchronous and asynchronous byte[], IRP, and IRP List submissions
 * <p>
 * The goal of the Bulk, Interrupt, and Isochronous IO test is to
 * verify that IN and OUT pipes can be opened and closed, and verify
 * that bulk, interrupt, and isochronous transfer operations work successfully, proper
 * events are generated, and proper exceptions are thrown in the operation.
 *
 * @author Leslie Blair
 */
 
 
public class IsochronousIOTests extends TestCase
{
    public void setUp() throws Exception
    {
        endpointType = UsbConst.ENDPOINT_TYPE_ISOCHRONOUS;
        usbDevice = FindProgrammableDevice.getInstance().getProgrammableDevice();
        Assert.assertNotNull("Device required for test not found",usbDevice);
        IOMethods.createListofAllAvailablePipesOfSpecifiedEndpointType(usbDevice, endpointType, usbPipeListGlobal);
        super.setUp();
    }
    public void tearDown() throws Exception
    {
        IOMethods.releaseListOfPipes(usbPipeListGlobal);
        super.tearDown();
    }
 
 
 
    public void testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf32bytes_buffer32bytes_invertBits()
    {
 
        byte testType = IOTests.BYTE_ARRAY;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 32;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_BITS};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {32};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {0};  //isochronous INs are submitted in individual buffers
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
 
    public void testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer64bytes_invertAltBits()
    {
 
        byte testType = IOTests.BYTE_ARRAY;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 64;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_ALTERNATE_BITS};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {64};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {0};  //isochronous INs are submitted in individual buffers
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf128bytes_buffer128bytes_passthrough()
    {
 
        byte testType = IOTests.BYTE_ARRAY;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 128;
        byte []transformType = {IOTests.TRANSFORM_TYPE_PASSTHROUGH};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {128};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {0};  //isochronous INs are submitted in individual buffers
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf32bytes_buffer30bytes_passthrough()
    {
 
        byte testType = IOTests.BYTE_ARRAY;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 32;
        byte []transformType = {IOTests.TRANSFORM_TYPE_PASSTHROUGH};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {30};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {0};  //isochronous INs are submitted in individual buffers
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer25bytes_invertBits()
    {
 
        byte testType = IOTests.BYTE_ARRAY;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 64;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_BITS};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {25};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {0};  //isochronous INs are submitted in individual buffers
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf128bytes_buffer127bytes_invertAltBits()
    {
 
        byte testType = IOTests.BYTE_ARRAY;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 128;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_ALTERNATE_BITS};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {127};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {0};  //isochronous INs are submitted in individual buffers
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf32bytes_buffer32bytes_invertBits()
    {
 
        byte testType = IOTests.IRP;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 32;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_BITS};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {32};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {0};  //isochronous INs are submitted in individual buffers
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer64bytes_invertAltBits()
    {
 
        byte testType = IOTests.IRP;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 64;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_ALTERNATE_BITS};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {64};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {0};  //isochronous INs are submitted in individual buffers
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf128bytes_buffer128bytes_passthrough()
    {
 
        byte testType = IOTests.IRP;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 128;
        byte []transformType = {IOTests.TRANSFORM_TYPE_PASSTHROUGH};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {128};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {0};  //isochronous INs are submitted in individual buffers
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf32bytes_buffer25bytes_invertBits()
    {
 
        byte testType = IOTests.IRP;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 32;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_BITS};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {25};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {0};  //isochronous INs are submitted in individual buffers
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer60bytes_invertAltBits()
    {
 
        byte testType = IOTests.IRP;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 64;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_ALTERNATE_BITS};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {60};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {0};  //isochronous INs are submitted in individual buffers
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf128bytes_buffer15bytes_passthrough()
    {
 
        byte testType = IOTests.IRP;
        /*
         * values from table
         */
        int numberOfIrps = 1;
        int endpointmaxPacketSize = 128;
        byte []transformType = {IOTests.TRANSFORM_TYPE_PASSTHROUGH};
 
        boolean[] IrpAcceptShortPacket = {true};
        boolean[] verifyAcceptShortPacket = {true};
 
 
        int []OUTLength = {15};
        int []OUTOffset = {0};
        int []OUTExpectedActualLength = {OUTLength[0]};
        Exception[] OUTExpectedException = {null};
 
        int []INLength = {OUTLength[0]};
        int []INOffset = {0};  //isochronous INs are submitted in individual buffers
        int []INExpectedActualLength = {OUTLength[0]};
        Exception[] INExpectedException = {null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testIRPListBuffersOfMaxPacketSizeOf32bytes()
    {
 
        byte testType = IOTests.IRPLIST;
        /*
         * values from table
         */
        int numberOfIrps = 3;
        int endpointmaxPacketSize = 32;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_BITS, IOTests.TRANSFORM_TYPE_INVERT_ALTERNATE_BITS,
            IOTests.TRANSFORM_TYPE_PASSTHROUGH};
 
        boolean[] IrpAcceptShortPacket = {true, true, true};
        boolean[] verifyAcceptShortPacket = {true, true, true};
 
 
        int []OUTLength = {32, 32, 32};
        int []OUTOffset = {0, 32, 64};
        int []OUTExpectedActualLength = {OUTLength[0], OUTLength[1], OUTLength[2]};
        Exception[] OUTExpectedException = {null, null, null};
 
        int []INLength = {OUTLength[0], OUTLength[1], OUTLength[2]};
        int []INOffset = {0, 0, 0};  //isochronous INs are submitted in individual buffers
        int []INExpectedActualLength = {OUTLength[0],OUTLength[1], OUTLength[2]};
        Exception[] INExpectedException = {null, null, null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
    };
    public void testIRPListBuffersLessThanMaxPacketSizeOf16bytes()
    {
        byte testType = IOTests.IRPLIST;
        /*
         * values from table
         */
        int numberOfIrps = 3;
        int endpointmaxPacketSize = 16;
        byte []transformType = {IOTests.TRANSFORM_TYPE_INVERT_BITS, IOTests.TRANSFORM_TYPE_INVERT_ALTERNATE_BITS,
            IOTests.TRANSFORM_TYPE_PASSTHROUGH};
 
        boolean[] IrpAcceptShortPacket = {true, true, true};
        boolean[] verifyAcceptShortPacket = {true, true, true};
 
 
        int []OUTLength = {12, 15, 10};
        int []OUTOffset = {0, 12, 27};
        int []OUTExpectedActualLength = {OUTLength[0], OUTLength[1], OUTLength[2]};
        Exception[] OUTExpectedException = {null, null, null};
 
        int []INLength = {OUTLength[0], OUTLength[1], OUTLength[2]};
        int []INOffset = {0, 0, 0};  //isochronous INs are submitted in individual buffers
        int []INExpectedActualLength = {OUTLength[0],OUTLength[1], OUTLength[2]};
        Exception[] INExpectedException = {null, null, null};
 
        IOTests thisIOTest = new IOTests(usbDevice, usbPipeListGlobal, endpointType, testType);
        thisIOTest.RoundTripIOTest(testType, numberOfIrps, endpointmaxPacketSize,
                                   IrpAcceptShortPacket, verifyAcceptShortPacket, OUTLength,  OUTOffset, OUTExpectedActualLength,
                                   OUTExpectedException,
                                   INLength, INOffset, INExpectedActualLength,
                                   INExpectedException,
                                   transformType );
 
 
 
    };
 
    /**
     * Constructor
     */
    public IsochronousIOTests()
    {
        super();
    };
 
    protected IsochronousIOTests(UsbDevice newUsbDevice, List newUsbPipeList, byte newEndpointType)
    {
        usbPipeListGlobal = newUsbPipeList;
        usbDevice = newUsbDevice;
        endpointType = newEndpointType;
    };
 
 
 
	private List usbPipeListGlobal = new ArrayList();
    private byte endpointType;
    private UsbDevice usbDevice;
 
 
    private static void printDebug(String infoString)
    {
        if ( printDebug )
        {
            System.out.println(infoString);
        }
    }
    private static boolean printDebug = false;
    //private static boolean printDebug = true;
}
