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
 * FLAG REASON   RELEASE  DATE   WHO   DESCRIPTION
 * ---- -------- -------- ------ ---   ------------------------------------
 * 0000 nnnnnnn           yymmdd       Initial Development
 * $P1           tck.rel1 300916 raulortz Change code to check RECIPIENT, DIRECTION
 *                                        and TYPE in the verification of bmRequestType
 */

import javax.usb.*;
import javax.usb.event.*;
import javax.usb.util.*;


import junit.framework.TestCase;

/**
 * Request Test - Get Interface test
 * 
 * This test creates a Request object and verifies that the GetInterface 
 * method sends messages to the Default Control Pipe
 * 
 * @author Raul Ortiz
 * 
 * The topology.b6 image must be programmed into the programmable device EEPROM
 * for the StandardRequestTest.
 *
 */


public class RequestTestGetInterface extends TestCase
{

    public void setUp()
    {
        usbDevice = FindProgrammableDevice.getInstance().getTopologyTestDevice();
        assertNotNull("Device required for test not found",usbDevice);
        usbDevice.addUsbDeviceListener(deviceListener);

    }
    public void tearDown()
    {
        if (usbDevice.getActiveUsbConfiguration().getUsbInterface((byte) 0).isClaimed())
        {
            try
            {
                usbDevice.getActiveUsbConfiguration().getUsbInterface((byte) 0).release();
            } catch ( UsbClaimException uCE )
            {
                fail("UsbInterface reports interface is claimed, release method throws" +
                     "UsbClaimException (device not claimed): " + uCE.getMessage());
            } catch ( UsbException uE )
            {
                fail("release method unable to release UsbInterface: " + uE.getMessage());
            } catch ( UsbDisconnectedException uDE )
            {
                fail ("A connected device should't throw the UsbDisconnectedException!");
            }
        }
        usbDevice.removeUsbDeviceListener(deviceListener); 

    }   

    public void testGetInterfaceStatic()

    {
        /* The method being tested is:
         *  public static byte getInterface(UsbDevice usbDevice, short interfaceNumber)
         *							throws UsbException
         */

        /*
         * Interface values of 0, 1 and 2 are valid with topology.b6 image
         */     

        short expectedInterfaceNumber;
        byte  actualInterfaceNumber;
        short expectedAlternateSetting;
        byte actualAlternateSetting;
        short configurationNumber;
        boolean usbExceptionThrown;

        /********************************************************************
         * Set values and verify with getInterface
         ********************************************************************/

        configurationNumber = 1;
        expectedInterfaceNumber = 0;
        expectedAlternateSetting = 0;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        // Claim the interface
        try
        {
          usbDevice.getUsbConfiguration((byte)configurationNumber).getUsbInterface((byte)expectedInterfaceNumber).claim();
        } catch ( UsbException uE )
        {
            fail("Got exception setting Interface to a valid value. Exception message:  " + uE.getMessage());                   
        } catch ( UsbDisconnectedException uDE )
        {
            fail ("A connected device should't throw the UsbDisconnectedException!");
        }

        // Get the current Interface Alternate Setting.
        try
        {
            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null;         
            actualAlternateSetting = StandardRequest.getInterface(usbDevice,expectedInterfaceNumber);
            assertEquals("Alternate Interface: expected = " + UsbUtil.toHexString(expectedAlternateSetting)
                         + "actual: "+ UsbUtil.toHexString(actualAlternateSetting), 
                         expectedAlternateSetting, actualAlternateSetting);

            try
            {
                /*
                * Wait for device event before leaving getInterface routine
                */
                for ( int i = 0; i < 100; i++ )
                {
                    if ( (LastUsbDeviceDataEvent != null)|
                         (LastUsbDeviceErrorEvent != null) )
                    {
                        break;
                    }
                    Thread.sleep( 20 ); //wait 20 ms before checking for event
                }
            } catch ( InterruptedException e )
            {
                fail("Sleep was interrupted");
            }

        } catch ( UsbException uE )
        {
            fail("Couldn't get current AS number. Exception message:  " + uE.getMessage());
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);
        verifyGetInterfaceIrp(expectedInterfaceNumber); 

        // Release the interface, so it may be claimed by other threads
        try
        {
            usbDevice.getUsbConfiguration((byte)configurationNumber).getUsbInterface((byte)expectedInterfaceNumber).release();      
        } catch ( UsbException uE )
        {
            fail("Got exception releasing Interface. Exception message:  " + uE.getMessage());                  
        } catch ( UsbDisconnectedException uDE )
        {
            fail ("A connected device should't throw the UsbDisconnectedException!");
        }

    }


    public void testGetInterfaceNonStatic()

    {
        /* The method being tested is:
         *  public byte getInterface(short interfaceNumber)
         *							throws UsbException
         */

        /*
         * Interface values of 0, 1 and 2 are valid with topology.b6 image
         */

        StandardRequest standardRequest = new StandardRequest(usbDevice);

        short expectedInterfaceNumber;
        byte  actualInterfaceNumber;
        short expectedAlternateSetting;
        byte actualAlternateSetting;
        short configurationNumber;
        boolean usbExceptionThrown;

        /********************************************************************
         * Set values and verify with getInterface
         ********************************************************************/

        configurationNumber = 1;
        expectedInterfaceNumber = 0;
        expectedAlternateSetting = 0;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        // Claim the interface
        try
        {
            usbDevice.getUsbConfiguration((byte)configurationNumber).getUsbInterface((byte)expectedInterfaceNumber).claim();
        } catch ( UsbException uE )
        {
            fail("Got exception setting Interface to a valid value. Exception message:  " + uE.getMessage());
        } catch ( UsbDisconnectedException uDE )
        {
            fail ("A connected device should't throw the UsbDisconnectedException!");
        }

        // Get the current Interface Alternate Setting.
        try
        {
            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null;
            actualAlternateSetting = standardRequest.getInterface(expectedInterfaceNumber);
            assertEquals("Alternate Interface: expected = " + UsbUtil.toHexString(expectedAlternateSetting)
                         + "actual: "+ UsbUtil.toHexString(actualAlternateSetting), 
                         expectedAlternateSetting, actualAlternateSetting);

            try
            {
                /*
                * Wait for device event before leaving getInterface routine
                */
                for ( int i = 0; i < 100; i++ )
                {
                    if ( (LastUsbDeviceDataEvent != null)|
                         (LastUsbDeviceErrorEvent != null) )
                    {
                        break;
                    }
                    Thread.sleep( 20 ); //wait 20 ms before checking for event
                }
            } catch ( InterruptedException e )
            {
                fail("Sleep was interrupted");
            }

        } catch ( UsbException uE )
        {
            fail("Couldn't get current AS number. Exception message:  " + uE.getMessage());
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);
        verifyGetInterfaceIrp(expectedInterfaceNumber); 

        // Release the interface, so it may be claimed by other threads
        try
        {
            usbDevice.getUsbConfiguration((byte)configurationNumber).getUsbInterface((byte)expectedInterfaceNumber).release();      
        } catch ( UsbException uE )
        {
            fail("Got exception releasing Interface. Exception message:  " + uE.getMessage());                  
        } catch ( UsbDisconnectedException uDE )
        {
            fail ("A connected device should't throw the UsbDisconnectedException!");
        }
    }

    private void verifyGetInterfaceIrp (short expectedwIndex)
    {
        //IRP values expected in get interface Irp

        byte expectedbmRequestType = UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE |               // @P1C
                                     UsbConst.REQUESTTYPE_TYPE_STANDARD |                     // @P1A
                                     UsbConst.REQUESTTYPE_DIRECTION_IN;                       // @P1A
        byte expectedbRequest = UsbConst.REQUEST_GET_INTERFACE;
        short expectedwValue = 0;

        VerifyIrpMethods.verifyRequestTest(LastUsbDeviceDataEvent.getUsbControlIrp(),
                                           expectedbmRequestType,
                                           expectedbRequest,
                                           expectedwValue,
                                           expectedwIndex);
    }

   
    private UsbDeviceListener deviceListener = new   UsbDeviceListener() 
    {
        public void dataEventOccurred(UsbDeviceDataEvent uddE) 
        {
            assertNotNull(uddE);
            LastUsbDeviceDataEvent = uddE; 

        }
        public void errorEventOccurred(UsbDeviceErrorEvent udeE) 
        {
            assertNotNull(udeE);
            LastUsbDeviceErrorEvent = udeE;

        }
        public void usbDeviceDetached(UsbDeviceEvent udE) 
        {
            assertNotNull(udE);
            fail("No devices should be detached during this test.");
        }           
    };          

    private UsbDeviceDataEvent LastUsbDeviceDataEvent = null ;
    private UsbDeviceErrorEvent LastUsbDeviceErrorEvent = null;
    private UsbDevice usbDevice;    

}
