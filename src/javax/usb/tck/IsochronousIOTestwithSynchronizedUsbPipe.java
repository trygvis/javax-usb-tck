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
import javax.usb.util.*;
 
import junit.framework.*;
 
/**
 * Isochronous IO Test -- Synchronous and asynchronous byte[], IRP, and IRP List submissions
 * with UsbUtil.synchronizedUsbPipe
 * <p>
 * The goal of the Bulk, Interrupt, and Isochronous IO test is to
 * verify that IN and OUT pipes can be opened and closed, and verify
 * that bulk, interrupt, and isochronous transfer operations work successfully, proper
 * events are generated, and proper exceptions are thrown in the operation.
 *
 * @author Leslie Blair
 */
 
 
public class IsochronousIOTestwithSynchronizedUsbPipe extends TestCase
{
    public void setUp() throws Exception
    {
 
        endpointType = UsbConst.ENDPOINT_TYPE_ISOCHRONOUS;
        usbDevice = FindProgrammableDevice.getInstance().getProgrammableDevice();
        Assert.assertNotNull("Device required for test not found",usbDevice);
        IOMethods.createListofAllAvailablePipesOfSpecifiedEndpointType(usbDevice, endpointType, usbPipeListNotSynchronized);
        for ( int i=0; i<usbPipeListNotSynchronized.size(); i++ )
        {
            usbPipeListGlobal.add(UsbUtil.synchronizedUsbPipe((UsbPipe) usbPipeListNotSynchronized.get(i)));
        }
        super.setUp();
    }
    public void tearDown() throws Exception
    {
        usbPipeListGlobal.clear();
        IOMethods.releaseListOfPipes(usbPipeListNotSynchronized);
        super.tearDown();
    }
 
 
 
    public void testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf32bytes_buffer32bytes_invertBits()
    {
        IsochronousIOTests thisIsochronousTest = new IsochronousIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisIsochronousTest.testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf32bytes_buffer32bytes_invertBits();
    };
 
    public void testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer64bytes_invertAltBits()
    {
        IsochronousIOTests thisIsochronousTest = new IsochronousIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisIsochronousTest.testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer64bytes_invertAltBits();
    };
    public void testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf128bytes_buffer128bytes_passthrough()
    {
        IsochronousIOTests thisIsochronousTest = new IsochronousIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisIsochronousTest.testByteArray_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf128bytes_buffer128bytes_passthrough();
    };
    public void testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf32bytes_buffer30bytes_passthrough()
    {
        IsochronousIOTests thisIsochronousTest = new IsochronousIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisIsochronousTest.testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf32bytes_buffer30bytes_passthrough();
    };
    public void testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer25bytes_invertBits()
    {
        IsochronousIOTests thisIsochronousTest = new IsochronousIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisIsochronousTest.testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer25bytes_invertBits();
    };
    public void testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf128bytes_buffer127bytes_invertAltBits()
    {
        IsochronousIOTests thisIsochronousTest = new IsochronousIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisIsochronousTest.testByteArray_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf128bytes_buffer127bytes_invertAltBits();
    };
    public void testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf32bytes_buffer32bytes_invertBits()
    {
        IsochronousIOTests thisIsochronousTest = new IsochronousIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisIsochronousTest.testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf32bytes_buffer32bytes_invertBits();
    };
    public void testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer64bytes_invertAltBits()
    {
        IsochronousIOTests thisIsochronousTest = new IsochronousIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisIsochronousTest.testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer64bytes_invertAltBits();
    };
    public void testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf128bytes_buffer128bytes_passthrough()
    {
        IsochronousIOTests thisIsochronousTest = new IsochronousIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisIsochronousTest.testSingleIRP_BuffersMultiplesOfMaxPacketSize_maxPacketSizeOf128bytes_buffer128bytes_passthrough();
    };
    public void testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf32bytes_buffer25bytes_invertBits()
    {
        IsochronousIOTests thisIsochronousTest = new IsochronousIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisIsochronousTest.testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf32bytes_buffer25bytes_invertBits();
    };
    public void testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer60bytes_invertAltBits()
    {
        IsochronousIOTests thisIsochronousTest = new IsochronousIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisIsochronousTest.testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf64bytes_buffer60bytes_invertAltBits();
    };
    public void testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf128bytes_buffer15bytes_passthrough()
    {
        IsochronousIOTests thisIsochronousTest = new IsochronousIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisIsochronousTest.testSingleIRP_BuffersNOTMultiplesOfMaxPacketSize_maxPacketSizeOf128bytes_buffer15bytes_passthrough();
    };
    public void testIRPListBuffersOfMaxPacketSizeOf32bytes()
    {
        IsochronousIOTests thisIsochronousTest = new IsochronousIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisIsochronousTest.testIRPListBuffersOfMaxPacketSizeOf32bytes();
    };
    public void testIRPListBuffersLessThanMaxPacketSizeOf16bytes()
    {
        IsochronousIOTests thisIsochronousTest = new IsochronousIOTests(usbDevice, usbPipeListGlobal, endpointType);
        thisIsochronousTest.testIRPListBuffersLessThanMaxPacketSizeOf16bytes();
    };
 
    /**
     * Constructor
     */
    public IsochronousIOTestwithSynchronizedUsbPipe()
    {
        super();
    };
 
    protected IsochronousIOTestwithSynchronizedUsbPipe(UsbDevice newUsbDevice, List newUsbPipeList, byte newEndpointType)
    {
        usbPipeListGlobal = newUsbPipeList;
        usbDevice = newUsbDevice;
        endpointType = newEndpointType;
    };
 
 
 
    private List usbPipeListGlobal = new ArrayList();
    private byte endpointType;
    private UsbDevice usbDevice;
    private List usbPipeListNotSynchronized = new ArrayList();
   
 
 
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
