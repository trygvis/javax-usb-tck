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
 * $P1           tck.rel1 040804 raulortz Support for UsbDisconnectedException
 * $P2           tck.rel1 040916 raulortz Redesign TCK to create base and optional
 *                                        tests. Separate setConfig, setInterface
 *                                        and isochronous transfers as optionals.
 * $P3           tck.rel1 300916 raulortz Change code to check RECIPIENT, DIRECTION
 *                                        and TYPE in the verification of bmRequestType
 */

import javax.usb.*;
import javax.usb.event.*;
import javax.usb.util.*;

import junit.framework.TestCase;

/**
 * Request Test - Synch Frame tests
 * 
 * This test creates a Request object and verifies that the Synch Frame 
 * method sends messages to the Default Control Pipe.
 * Using the topology image should only produce exceptions, since it 
 * does not have an isochronous pipe configured.
 * 
 * @author Bob Rossi
 * 
 * The topology.b6 image must be programmed into the programmable device EEPROM
 * for the StandardRequestTest.
 *
 */

public class RequestTestSynchFrame extends TestCase
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

    public void testSynchFrameStatic()
    {
        /* The method being tested is:
         * public static short synchFrame(UsbDevice usbDevice,
         *                                                    short EndpointAddress)
         *                               throws usbException
         */

        short expectedConfigurationValue;
        byte actualConfigurationValue;
        short expectedInterfaceNumber;
        short expectedAlternateSetting;

        short endpoint;
        short frameNumber;
        boolean usbExceptionThrown;
        UsbInterface frameInterface = null;

        /********************************************************************************        @P2D
         * Attempt to get the frame number for an endpoint.
         * Should get exception using this non-isochronous endpoint
         * *******************************************************************************/
/*  This code attempts to properly call the synch Frame method -and have
 *  it return the Frame Number.  After setting the interface, claim it, then
 *  release it when through.
 */

        expectedConfigurationValue = 1;
        expectedInterfaceNumber = 0;
        expectedAlternateSetting = 0;
        endpoint = 0x88;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        usbExceptionThrown = false;

                                                                                              // @P2D71
        // Now call synch frame for a known endpoint to get the frame number
        try
        {
            frameNumber = StandardRequest.synchFrame(usbDevice, endpoint);
            try
            {
                //* Wait for device event before leaving setInterface routine
                for (int i = 0; i < 100; i++)
                {
                    if ((LastUsbDeviceDataEvent != null)|
                        (LastUsbDeviceErrorEvent != null))
                    {
                        break;
                    }
                    Thread.sleep( 20 ); //wait 20 ms before checking for event
                }
            } catch (InterruptedException e)
            {
                fail("Sleep was interrupted");
            }

        } catch (UsbException uE)
        {
                                                                                              // @P2D14
            fail ("Got exception setting Synch Frame.  Exception message: " + uE.getMessage());
        }

        try
        {
            Thread.sleep(250);
        } catch ( InterruptedException e)
        {
            fail ("Sleep was interrupted.");
        }

        // Verify proper listener event received
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);
        verifySynchFrame(endpoint);

        /********************************************************************************        @P2D
         * Attempt to get the frame number for an endpoint.
         * Should get exception using this non-isochronous endpoint
         * *******************************************************************************/

        expectedConfigurationValue = 1;
        expectedInterfaceNumber = 0;
        expectedAlternateSetting = 0;                                                         // @P2C
        endpoint = 0x82;                                                                      // @P2C
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        usbExceptionThrown = false;

                                                                                              // @P2D39
        /* Now call synch frame for a known endpoint to get the frame number */
        try
        {
            frameNumber = StandardRequest.synchFrame(usbDevice, endpoint);
            try
            {
                /*
                * Wait for device event before leaving setInterface routine
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
            usbExceptionThrown = true;
        } finally
        {
            assertTrue("UsbException should have been thrown for calling synchFrame on this non-Isochronous endpoint, " 
                       + UsbUtil.toHexString(endpoint), usbExceptionThrown);   
        }
        try
        {
            Thread.sleep(250);
        } catch ( InterruptedException e )
        {
            fail ("Sleep was interrupted.");
        }

        /* Verify proper listener event received */
        assertNull("DeviceDataEvent should be null.", LastUsbDeviceDataEvent);
        assertNotNull("DeviceErrorEvent should not be null.", LastUsbDeviceErrorEvent);

    }

    public void testSynchFrameNonStatic()
    {
        /* The method being tested is:
         * public static short synchFrame(short EndpointAddress) 
         *                               throws usbException
         */      

        StandardRequest standardRequest = new StandardRequest(usbDevice);

        short expectedConfigurationValue;
        byte actualConfigurationValue;
        short expectedInterfaceNumber;
        short expectedAlternateSetting;

        short endpoint;
        short frameNumber;
        boolean usbExceptionThrown; 

        /********************************************************************************
         * Set configuration to 1, interface to 1 and alternate setting to 0.
         * Then attempt to get the frame number for an endpoint.
         * Should get exception using this non-isochronous endpoint
         * *******************************************************************************/
/*  This code attempts to properly call the synch Frame method -and have
 *  it return the Frame Number.  After setting the interface, claim it, then 
 *  release it when through.  
*/

        expectedConfigurationValue = 1;
        expectedInterfaceNumber = 0;
        expectedAlternateSetting = 0;
        endpoint = 0x88;         
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        usbExceptionThrown = false;
        UsbInterface frameInterface = null;


        // Now call synch frame for a known endpoint to get the frame number 
        try
        {
            frameNumber = standardRequest.synchFrame(endpoint);
            try
            {
                //* Wait for device event before leaving setInterface routine				
                for (int i = 0; i < 100; i++)
                {
                    if ((LastUsbDeviceDataEvent != null)|
                        (LastUsbDeviceErrorEvent != null))
                    {
                        break;
                    }
                    Thread.sleep( 20 ); //wait 20 ms before checking for event
                }
            } catch (InterruptedException e)
            {
                fail("Sleep was interrupted");
            }

        } catch (UsbException uE)
        {                                                                                     // @P2D15
            fail ("Got exception setting Synch Frame.  Exception message: " + uE.getMessage());         
        }

        try
        {
            Thread.sleep(250);
        } catch ( InterruptedException e)
        {
            fail ("Sleep was interrupted.");
        }

        // Verify proper listener event received 
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);    
        verifySynchFrame(endpoint);

        // Release the interface, so it may be claimed by other threads
        try
        {
            // Release the interface, so it may be claimed by other threads
            if (frameInterface != null)
            {
                frameInterface.release();
            }
            //usbDevice.getUsbConfiguration((byte)expectedConfigurationValue).getUsbInterface((byte)expectedInterfaceNumber).release();
        } catch (UsbException uE)
        {
            fail("Got exception releasing Interface. Exception message:  " + uE.getMessage());                  
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A






        /********************************************************************************        @P2D
         * Attempt to get the frame number for an endpoint.
         * Should get exception using this non-isochronous endpoint
         * *******************************************************************************/

        expectedConfigurationValue = 1;
        expectedInterfaceNumber = 0;
        expectedAlternateSetting = 0;                                                         // @P2C
        endpoint = 0x82;                                                                      // @P2C
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        usbExceptionThrown = false;

                                                                                              // @P2D39
        /* Now call synch frame for a known endpoint to get the frame number */
        try
        {
            frameNumber = standardRequest.synchFrame(endpoint);
            try
            {
                /*
                * Wait for device event before leaving setInterface routine
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
            usbExceptionThrown = true;
        } finally
        {
            assertTrue("UsbException should have been thrown for calling synchFrame on this non-Isochronous endpoint, " 
                       + UsbUtil.toHexString(endpoint), usbExceptionThrown);   
        }
        try
        {
            Thread.sleep(250);
        } catch ( InterruptedException e )
        {
            fail ("Sleep was interrupted.");
        }

        /* Verify proper listener event received */
        assertNull("DeviceDataEvent should be null.", LastUsbDeviceDataEvent);
        assertNotNull("DeviceErrorEvent should not be null.", LastUsbDeviceErrorEvent);


    }   

    private void verifySynchFrame (short expectedwIndex)
    {
        //IRP values expected in SynchFrame Irp

        byte expectedbmRequestType = UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT |                // @P3C
                                     UsbConst.REQUESTTYPE_TYPE_STANDARD |                     // @P3A
                                     UsbConst.REQUESTTYPE_DIRECTION_IN;                       // @P3C
        byte expectedbRequest = UsbConst.REQUEST_SYNCH_FRAME;
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
