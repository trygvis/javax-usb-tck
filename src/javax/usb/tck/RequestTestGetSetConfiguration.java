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
 * $P1           tck.rel1 300916 raulortz Change code to check RECIPIENT, DIRECTION
 *                                        and TYPE in the verification of bmRequestType
 */

import javax.usb.*;
import javax.usb.event.*;
import javax.usb.util.*;


import junit.framework.TestCase;

/**
 * Request Test - Get and Set Configuration tests
 * 
 * This test creates a Request object and verifies that the GetConfiguration 
 * and SetConfiguration methods send messages to the Default Control Pipe
 * 
 * @author leslie
 * 
 * The topology.b6 image must be programmed into the programmable device EEPROM
 * for the StandardRequestTest.
 *
 */
public class RequestTestGetSetConfiguration extends TestCase
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

    public void testSetAndGetConfigurationStatic()

    {
        /* The methods being tested are as follows:
         *	public static void setConfiguration(UsbDevice usbDevice,short configurationValue)
         *							throws UsbException
         *
         *  public static byte getConfiguration(UsbDevice usbDevice)
         *							throws UsbException
         */

        /*
         * Configuration values of 1 and 2 are valid with topology.b6 image
         */

        short expectedConfigurationValue;
        byte actualConfigurationValue;
        boolean usbExceptionThrown;



        /********************************************************************************
         * Set configuration to a known value and verify it was set with getConfiguration
         * *******************************************************************************
         */
        expectedConfigurationValue = 1;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {
            /* First set configuration to known value */

            StandardRequest.setConfiguration(usbDevice, expectedConfigurationValue);
            try
            {
                /*
                * Wait for device event before leaving setConfiguration routine
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
            fail("Got exception setting Configuration to a valid value. Exception message:  " + uE.getMessage());
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);
        verifySetConfigurationIrp(expectedConfigurationValue);

        /* Now we'll get the current configuration number we just set. */
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


        /********************************************************************************
         * Set configuration to a different known value and verify it was set with getConfiguration
         ********************************************************************************/
        expectedConfigurationValue = 2;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {
            /* First set configuration to a different known value */

            StandardRequest.setConfiguration(usbDevice, expectedConfigurationValue);

            try
            {
                /*
                * Wait for device event before leaving setConfiguration routine
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
            fail("Got exception setting Configuration to a valid value. Exception message:  " + uE.getMessage());
        }
        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);
        verifySetConfigurationIrp(expectedConfigurationValue);

        /* Now we'll get the current configuration number we just set. */
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

        /********************************************************************************
         * Set configuration to the original known value and verify it was set with getConfiguration
         ********************************************************************************/
        expectedConfigurationValue = 1;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {
            /* First set configuration to a different known value */

            StandardRequest.setConfiguration(usbDevice, expectedConfigurationValue);

            try
            {
                /*
                * Wait for device event before leaving setConfiguration routine
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
            fail("Got exception setting Configuration to a valid value. Exception message:  " + uE.getMessage());
        }
        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);
        verifySetConfigurationIrp(expectedConfigurationValue);

        /* Now we'll get the current configuration number we just set. */
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

        /********************************************************************************
         * Try to set the configuration to an invalid value.  UsbException expected.
         ********************************************************************************/

        usbExceptionThrown = false; 
        expectedConfigurationValue = 3;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {
            /* Try to set configuration to an invalid value */

            StandardRequest.setConfiguration(usbDevice, expectedConfigurationValue);

            fail("Shouldn't get here because exception should be thrown for bad configuration value of " 
                 + UsbUtil.toHexString(expectedConfigurationValue));

        }
        catch ( UsbException uE )
        {
            usbExceptionThrown = true;
        }
        finally
        {
            assertTrue("UsbException should have been thrown for bad configuration value of " 
                       + UsbUtil.toHexString(expectedConfigurationValue), usbExceptionThrown); 
        }
        /* Verify proper listener event received 
         * Note that these asserts are opposite the other event asserts
         * because an exception is expected.
         * */
        try
        {
            Thread.sleep(250);
        }
        catch ( InterruptedException e )
        {
        }
        assertNull("DeviceDataEvent should be null.", LastUsbDeviceDataEvent);
        assertNotNull("DeviceErrorEvent should not be null.", LastUsbDeviceErrorEvent);

        /* Verify that we can get the last valid configuration value. */

        expectedConfigurationValue = 1;
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

    public void testSetAndGetConfigurationNonStatic()

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
            /* First set configuration to known value */

            standardRequest.setConfiguration(expectedConfigurationValue);

            try
            {
                /*
                * Wait for device event before leaving setConfiguration routine
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
            fail("Got exception setting Configuration to a valid value. Exception message:  " + uE.getMessage());
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);
        verifySetConfigurationIrp(expectedConfigurationValue); 

        /* Now we'll get the current configuration number we just set. */
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {
            actualConfigurationValue = standardRequest.getConfiguration();
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


        /********************************************************************************
         * Set configuration to a different known value and verify it was set with getConfiguration
         ********************************************************************************/
        expectedConfigurationValue = 2;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {
            /* First set configuration to a different known value */

            standardRequest.setConfiguration(expectedConfigurationValue);

            try
            {
                /*
                * Wait for device event before leaving setConfiguration routine
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
            fail("Got exception setting Configuration to a valid value. Exception message:  " + uE.getMessage());
        }
        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);
        verifySetConfigurationIrp(expectedConfigurationValue);


        /* Now we'll get the current configuration number we just set. */
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {
            actualConfigurationValue = standardRequest.getConfiguration();
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

        /********************************************************************************
         * Set configuration to the original known value and verify it was set with getConfiguration
         ********************************************************************************/
        expectedConfigurationValue = 1;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {
            /* First set configuration to a different known value */

            standardRequest.setConfiguration(expectedConfigurationValue);

            try
            {
                /*
                * Wait for device event before leaving setConfiguration routine
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
            fail("Got exception setting Configuration to a valid value. Exception message:  " + uE.getMessage());
        }
        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);
        verifySetConfigurationIrp(expectedConfigurationValue);

        /* Now we'll get the current configuration number we just set. */
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {
            actualConfigurationValue = standardRequest.getConfiguration();
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

        /********************************************************************************
         * Try to set the configuration to an invalid value.  UsbException expected.
         ********************************************************************************/

        usbExceptionThrown = false; 
        expectedConfigurationValue = 3;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {
            /* Try to set configuration to an invalid value */

            standardRequest.setConfiguration(expectedConfigurationValue);
            fail("Shouldn't get here because exception should be thrown for bad configuration value of " 
                 + UsbUtil.toHexString(expectedConfigurationValue));

        }
        catch ( UsbException uE )
        {
            usbExceptionThrown = true;
        }
        finally
        {
            assertTrue("UsbException should have been thrown for bad configuration value of " 
                       + UsbUtil.toHexString(expectedConfigurationValue), usbExceptionThrown); 
        }
        /* Verify proper listener event received 
         * Note that these asserts are opposite the other event asserts
         * because an exception is expected.
         * */
        try
        {
            Thread.sleep (250);
        }
        catch ( InterruptedException e )
        {
        }

        assertNull("DeviceDataEvent should be null.", LastUsbDeviceDataEvent);
        assertNotNull("DeviceErrorEvent should not be null.", LastUsbDeviceErrorEvent);


        /* Verify that we can get the last valid configuration value. */

        expectedConfigurationValue = 1;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {

            actualConfigurationValue = standardRequest.getConfiguration();
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

    private void verifySetConfigurationIrp (short expectedwValue)
    {
        //IRP values expected in set configuration Irp

        byte expectedbmRequestType = UsbConst.REQUESTTYPE_RECIPIENT_DEVICE |                  // @P1C
                                     UsbConst.REQUESTTYPE_TYPE_STANDARD |                     // @P1A
                                     UsbConst.REQUESTTYPE_DIRECTION_OUT;                      // @P1A
        byte expectedbRequest = UsbConst.REQUEST_SET_CONFIGURATION;
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
