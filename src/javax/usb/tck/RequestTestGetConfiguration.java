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
 * Request Test - Get Configuration tests
 *
 * This test creates a Request object and verifies that the GetConfiguration
 * method sends messages to the Default Control Pipe
 *
 * @author leslie
 *
 * The topology.b6 image must be programmed into the programmable device EEPROM
 * for the StandardRequestTest.
 *
 */
public class RequestTestGetConfiguration extends TestCase
{

    public void setUp()
    {
        usbDevice = FindProgrammableDevice.getInstance().getTopologyTestDevice();
        assertNotNull("Device required for test not found",usbDevice);
        usbDevice.addUsbDeviceListener(deviceListener);

    }
    public void tearDown()
    {
        usbDevice.removeUsbDeviceListener(deviceListener);

    }

    public void testGetConfigurationStatic()

    {
        /* The method being tested is:
         *  public static byte getConfiguration(UsbDevice usbDevice)
         *							throws UsbException
         */

        /*
         * Configuration value 1 is valid with topology.b6 image as default configuration
         */

        short expectedConfigurationValue;
        byte actualConfigurationValue;
        boolean usbExceptionThrown;




        expectedConfigurationValue = 1;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        // Now we'll get the current configuration number we just set.
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {
            actualConfigurationValue = StandardRequest.getConfiguration(usbDevice);
            assertEquals("Configuration value: expected = " + UsbUtil.toHexString(expectedConfigurationValue)
                         + "actual: "+ UsbUtil.toHexString(actualConfigurationValue),
                         expectedConfigurationValue, actualConfigurationValue );

            try
            {
                /*
                * Wait for device event before leaving getConfiguration routine
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
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted");
            }

        }
        catch ( UsbException uE )
        {
            fail("Couldn't get current configuration number. Exception message:  " + uE.getMessage());
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);
        verifyGetConfigurationIrp();
    }

    public void testGetConfigurationNonStatic()

    {
        /* The methods being tested are as follows:
         *	public void setConfiguration(short configurationValue) throws UsbException
         *
         *  public byte getConfiguration()throws UsbException
         */

        /*
         * Configuration values of 1 and 2 are valid with topology.b6 image
         */

        StandardRequest standardRequest = new StandardRequest(usbDevice);

        short expectedConfigurationValue;
        byte actualConfigurationValue;
        boolean usbExceptionThrown;

        /********************************************************************************
         * Set configuration to a known value and verify it was set with getConfiguration
         ********************************************************************************/
        expectedConfigurationValue = 1;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {
            actualConfigurationValue = standardRequest.getConfiguration();
            try
            {
                /*
                * Wait for device event before leaving getConfiguration routine
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
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted");
            }

        }
        catch ( UsbException uE )
        {
            fail("Couldn't get current configuration number. Exception message:  " + uE.getMessage());
        }

        verifyGetConfigurationIrp();
    }


    private void verifyGetConfigurationIrp ()
    {
        //IRP values expected in get configuration Irp

        byte expectedbmRequestType = UsbConst.REQUESTTYPE_RECIPIENT_DEVICE |                  // @P1C
                                     UsbConst.REQUESTTYPE_TYPE_STANDARD |                     // @P1A
                                     UsbConst.REQUESTTYPE_DIRECTION_IN;                       // @P1A

        byte expectedbRequest = UsbConst.REQUEST_GET_CONFIGURATION;
        short expectedwValue = 0;
        short expectedwIndex = 0;

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
