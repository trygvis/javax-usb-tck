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
 * Request Test - Get Status tests
 * 
 * This test creates a Request object and verifies that the GetStatus 
 * method sends messages to the Default Control Pipe
 * 
 * @author Bob Rossi
 * 
 * The topology.b6 image must be programmed into the programmable device EEPROM
 * for the StandardRequestTest.
 *
 */

public class RequestTestGetStatus extends TestCase
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

    public void testGetStatusStatic()
    {
        /* The method being tested is:
         * public static short getStatus(UsbDevice usbDevice, byte recipient, 
         *                                              short target)
         *                              throws usbException, IllegalArgumentException
         */

        short configurationNumber;
        byte recipientType;
        short featureSelection;
        short target;
        short wIndex;
        short status;
        short statusExpected;
        byte requestType;

        boolean usbExceptionThrown = false;


        /************************************************************************
         * First, getStatus for a new configuration
         ************************************************************************/

        configurationNumber = 1;
        status = 0;
        //no target needed when recipient is a device
        target = 0;
        //set recipientType to Device
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
        requestType = UsbConst.REQUESTTYPE_DIRECTION_IN;
        statusExpected = 0;

                                                                                              // @P2D16
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            status = StandardRequest.getStatus(usbDevice, recipientType, target);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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

            /*status is unknown at this point, so don't check the value yet*/

        } catch ( UsbException uE )
        {
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyGetStatusIrp(target, requestType);         

        /************************************************************************
         * Now, do a setFeature for the device and check getStatus 
         ************************************************************************/

        featureSelection = UsbConst.FEATURE_SELECTOR_DEVICE_REMOTE_WAKEUP;
        statusExpected = 2;

        try
        {

            StandardRequest.setFeature(usbDevice, recipientType, featureSelection, target);
            try
            {
                /*Wait for setConfiguration Data event before continuing */
                Thread.sleep(250);
            } catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }


        } catch ( UsbException uE )
        {
            fail ("Got exception setting Feature. Exception message: " + uE.getMessage() );
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            status = StandardRequest.getStatus(usbDevice, recipientType, target);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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

            assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                         statusExpected, status);

        } catch ( UsbException uE )
        {
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyGetStatusIrp(target, requestType);    


        /************************************************************************
         * Now, do a clearFeature for the device and check getStatus 
         ************************************************************************/

        featureSelection = UsbConst.FEATURE_SELECTOR_DEVICE_REMOTE_WAKEUP;
        statusExpected = 0;

        try
        {

            StandardRequest.clearFeature(usbDevice, recipientType, featureSelection, target);
            try
            {
                /*Wait for setConfiguration Data event before continuing */
                Thread.sleep(250);
            } catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }


        } catch ( UsbException uE )
        {
            fail ("Got exception clearing Feature. Exception message: " + uE.getMessage() );
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            status = StandardRequest.getStatus(usbDevice, recipientType, target);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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

            assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                         statusExpected, status);

        } catch ( UsbException uE )
        {
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyGetStatusIrp(target, requestType);    



        /************************************************************************
         * GetStatus for a device with an illegal argument 
         ************************************************************************/
        recipientType = 3;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            status = StandardRequest.getStatus(usbDevice, recipientType, target);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            usbExceptionThrown = true;
        } finally
        {
            assertTrue("UsbException should have been thrown for using an illegal recipient type:  " 
                       + UsbUtil.toHexString(recipientType), usbExceptionThrown);       
        }

        try
        {
            Thread.sleep (250);
        } catch ( InterruptedException e )
        {
            fail("Sleep was interrupted");
        }

        /* Verify proper listener event received */
        /* Don't get data or error event with illegal argument exception*/
        assertNull("DeviceDataEvent should be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);


        /************************************************************************
        * Now getStatus for an interface
        *************************************************************************/

        configurationNumber = 1;

        //no target needed when recipient is a device
        target = 0;
        //set recipientType to Interface
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE;
        requestType = UsbConst.REQUESTTYPE_DIRECTION_IN + 
                      UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE;
        statusExpected = 0;

                                                                                              // @P2D15
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {
            try
            {
                usbDevice.getActiveUsbConfiguration().getUsbInterface((byte) target).claim();
            } catch ( UsbClaimException uCE )
            {
                fail("Config " + configurationNumber + " Interface " + target + " is already claimed!");
            } catch ( UsbNotActiveException uNAE )
            {
                fail("Config " + configurationNumber + " is not active!");
            } catch ( UsbException uE )
            {
                fail("Config " + configurationNumber + " could not be claimed!");
            } catch ( UsbDisconnectedException uDE )                                          // @P1C
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            }                                                                                 // @P1A
            status = StandardRequest.getStatus(usbDevice, recipientType, target);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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

            // status should be 0 since interface status cannot be changed		
            status = 0;                     
            assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                         statusExpected, status);

        } catch ( UsbException uE )
        {
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        // Verify proper listener event received 
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyGetStatusIrp(target, requestType);    


        /************************************************************************
        * Now getStatus for an endpoint
        *************************************************************************/

        short endpoint;

        configurationNumber = 1;
        status = 0;
        //no target needed when recipient is a device
        target = 0;
        //set recipientType to Interface
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;
        requestType = UsbConst.REQUESTTYPE_DIRECTION_IN + 
                      UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;
        statusExpected = 1;
        endpoint = 0x82;

                                                                                              // @P2D15
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            status = StandardRequest.getStatus(usbDevice, recipientType, endpoint);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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

            /*status is unknown at this point, so don't check the value yet */

        } catch ( UsbException uE )
        {
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyGetStatusIrp(endpoint, requestType);      

        /************************************************************************
        * getStatus after setting feature for the endpoint
        *************************************************************************/

        featureSelection = UsbConst.FEATURE_SELECTOR_ENDPOINT_HALT;
        statusExpected = 1;


        try
        {

            StandardRequest.setFeature(usbDevice, recipientType, featureSelection, endpoint);
            try
            {
                /*Wait for setConfiguration Data event before continuing */
                Thread.sleep(250);
            } catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }


        } catch ( UsbException uE )
        {
            fail ("Got exception setting Feature. Exception message: " + uE.getMessage() );
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            status = StandardRequest.getStatus(usbDevice, recipientType, endpoint);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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

            assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                         statusExpected, status);

        } catch ( UsbException uE )
        {
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyGetStatusIrp(endpoint, requestType);  

        /************************************************************************
         * Now, do a clearFeature for the endpoint and check getStatus 
         ************************************************************************/

        featureSelection = UsbConst.FEATURE_SELECTOR_ENDPOINT_HALT;
        statusExpected = 0;

        try
        {

            StandardRequest.clearFeature(usbDevice, recipientType, featureSelection, endpoint);
            try
            {
                /*Wait for setConfiguration Data event before continuing */
                Thread.sleep(250);
            } catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }


        } catch ( UsbException uE )
        {
            fail ("Got exception clearing Feature. Exception message: " + uE.getMessage() );
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            status = StandardRequest.getStatus(usbDevice, recipientType, endpoint);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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

            assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                         statusExpected, status);

        } catch ( UsbException uE )
        {
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyGetStatusIrp(endpoint, requestType);  




        /************************************************************************
         * GetStatus for an endpoint with an illegal argument 
         ************************************************************************/

        //set recipientType to an illegal value
        recipientType = 3;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            status = StandardRequest.getStatus(usbDevice, recipientType, endpoint);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            usbExceptionThrown = true;
        } finally
        {
            assertTrue("UsbException should have been thrown for using an illegal recipient type:  " 
                       + UsbUtil.toHexString(recipientType), usbExceptionThrown);       
        }

        try
        {
            Thread.sleep (250);
        } catch ( InterruptedException e )
        {
            fail("Sleep was interrupted");
        }

        try
        {
            usbDevice.getActiveUsbConfiguration().getUsbInterface((byte) target).release();
        } catch ( UsbClaimException uCE )
        {
            fail("Config " + configurationNumber + " Interface " + target + " is not claimed!");
        } catch ( UsbException uE )
        {
            fail("Config " + configurationNumber + " could not be released!");
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A

        /* Verify proper listener event received */
        assertNull("DeviceDataEvent should be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);

    }

    public void testGetStatusNonStatic()
    {
        /* The method being tested is:
         * public static short getStatus(byte recipient, short target) 
         *                              throws usbException, IllegalArgumentException
         */

        StandardRequest standardRequest = new StandardRequest(usbDevice);

        short configurationNumber;
        byte recipientType;
        short featureSelection;
        short target;
        short wIndex;
        short status;
        short statusExpected;
        byte requestType;

        boolean usbExceptionThrown = false;


        /************************************************************************
         * First, getStatus for a new configuration
         ************************************************************************/

        configurationNumber = 1;
        status = 0;
        //no target needed when recipient is a device
        target = 0;
        //set recipientType to Device
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
        requestType = UsbConst.REQUESTTYPE_DIRECTION_IN;
        statusExpected = 0;

                                                                                              // @P2D16
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            status = standardRequest.getStatus(recipientType, target);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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

            /*status is unknown at this point, so don't check the value yet*/

        } catch ( UsbException uE )
        {
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyGetStatusIrp(target, requestType);         

        /************************************************************************
         * Now, do a setFeature for the device and check getStatus 
         ************************************************************************/

        featureSelection = UsbConst.FEATURE_SELECTOR_DEVICE_REMOTE_WAKEUP;
        statusExpected = 2;

        try
        {

            standardRequest.setFeature(recipientType, featureSelection, target);
            try
            {
                /*Wait for setConfiguration Data event before continuing */
                Thread.sleep(250);
            } catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }


        } catch ( UsbException uE )
        {
            fail ("Got exception setting Feature. Exception message: " + uE.getMessage() );
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            status = standardRequest.getStatus(recipientType, target);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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

            assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                         statusExpected, status);

        } catch ( UsbException uE )
        {
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyGetStatusIrp(target, requestType);    


        /************************************************************************
         * Now, do a clearFeature for the device and check getStatus 
         ************************************************************************/

        featureSelection = UsbConst.FEATURE_SELECTOR_DEVICE_REMOTE_WAKEUP;
        statusExpected = 0;

        try
        {

            standardRequest.clearFeature(recipientType, featureSelection, target);
            try
            {
                /*Wait for setConfiguration Data event before continuing */
                Thread.sleep(250);
            } catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }


        } catch ( UsbException uE )
        {
            fail ("Got exception clearing Feature. Exception message: " + uE.getMessage() );
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            status = standardRequest.getStatus(recipientType, target);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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

            assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                         statusExpected, status);

        } catch ( UsbException uE )
        {
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyGetStatusIrp(target, requestType);    


        /************************************************************************
         * GetStatus for a device with an illegal argument 
         ************************************************************************/
        recipientType = 3;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            status = standardRequest.getStatus(recipientType, target);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            usbExceptionThrown = true;
        } finally
        {
            assertTrue("UsbException should have been thrown for using an illegal recipient type:  " 
                       + UsbUtil.toHexString(recipientType), usbExceptionThrown);       
        }

        try
        {
            Thread.sleep (250);
        } catch ( InterruptedException e )
        {
            fail("Sleep was interrupted");
        }

        /* Verify proper listener event received */
        /* Don't get data or error event with illegal argument exception*/
        assertNull("DeviceDataEvent should be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);            

        /************************************************************************
        * Now getStatus for an interface
        *************************************************************************/

        configurationNumber = 1;

        //no target needed when recipient is a device
        target = 0;
        //set recipientType to Interface
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE;
        requestType = UsbConst.REQUESTTYPE_DIRECTION_IN + 
                      UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE;
        statusExpected = 0;

                                                                                              // @P2D16
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            try
            {
                usbDevice.getActiveUsbConfiguration().getUsbInterface((byte) target).claim();
            } catch ( UsbClaimException uCE )
            {
                fail("Config " + configurationNumber + " Interface " + target + " is already claimed!");
            } catch ( UsbNotActiveException uNAE )
            {
                fail("Config " + configurationNumber + " is not active!");
            } catch ( UsbException uE )
            {
                fail("Config " + configurationNumber + " could not be claimed!");
            } catch ( UsbDisconnectedException uDE )                                          // @P1C
            {                                                                                 // @P1A
                fail ("A connected device should't throw the UsbDisconnectedException!");     // @P1A
            }                                                                                 // @P1A
            status = standardRequest.getStatus(recipientType, target);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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

            // status should be 0 since interface status cannot be changed		
            status = 0;                     
            assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                         statusExpected, status);

        } catch ( UsbException uE )
        {
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        // Verify proper listener event received 
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyGetStatusIrp(target, requestType);    


        /************************************************************************
        * Now getStatus for an endpoint
        *************************************************************************/

        short endpoint;

        configurationNumber = 1;
        status = 0;
        //no target needed when recipient is a device
        target = 0;
        //set recipientType to Interface
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;
        requestType = UsbConst.REQUESTTYPE_DIRECTION_IN + 
                      UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;
        statusExpected = 1;
        endpoint = 0x82;

                                                                                              // @P2D16
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            status = standardRequest.getStatus(recipientType, endpoint);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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

            /*status is unknown at this point, so don't check the value yet	*/

        } catch ( UsbException uE )
        {
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyGetStatusIrp(endpoint, requestType);      

        /************************************************************************
        * getStatus after setting feature for the endpoint
        *************************************************************************/

        featureSelection = UsbConst.FEATURE_SELECTOR_ENDPOINT_HALT;
        statusExpected = 1;


        try
        {

            standardRequest.setFeature(recipientType, featureSelection, endpoint);
            try
            {
                /*Wait for setConfiguration Data event before continuing */
                Thread.sleep(250);
            } catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }


        } catch ( UsbException uE )
        {
            fail ("Got exception setting Feature. Exception message: " + uE.getMessage() );
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            status = standardRequest.getStatus(recipientType, endpoint);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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

            assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                         statusExpected, status);

        } catch ( UsbException uE )
        {
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyGetStatusIrp(endpoint, requestType);  

        /************************************************************************
         * Now, do a clearFeature for the endpoint and check getStatus 
         ************************************************************************/

        featureSelection = UsbConst.FEATURE_SELECTOR_ENDPOINT_HALT;
        statusExpected = 0;

        try
        {

            standardRequest.clearFeature(recipientType, featureSelection, endpoint);
            try
            {
                /*Wait for setConfiguration Data event before continuing */
                Thread.sleep(250);
            } catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }


        } catch ( UsbException uE )
        {
            fail ("Got exception clearing Feature. Exception message: " + uE.getMessage() );
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            status = standardRequest.getStatus(recipientType, endpoint);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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

            assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                         statusExpected, status);

        } catch ( UsbException uE )
        {
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyGetStatusIrp(endpoint, requestType);  




        /************************************************************************
         * GetStatus for an endpoint with an illegal argument 
         ************************************************************************/

        //set recipientType to an illegal value
        recipientType = 3;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {

            status = standardRequest.getStatus(recipientType, endpoint);

            try
            {
                /*
                * Wait for device event before leaving setFeature routine
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
            fail ("Got exception getting the device status. Exception message: " + uE.getMessage());
        } catch ( IllegalArgumentException iE )
        {
            usbExceptionThrown = true;
        } finally
        {
            assertTrue("UsbException should have been thrown for using an illegal recipient type:  " 
                       + UsbUtil.toHexString(recipientType), usbExceptionThrown);       
        }

        try
        {
            Thread.sleep (250);
        } catch ( InterruptedException e )
        {
            fail("Sleep was interrupted");
        }

        try
        {
            usbDevice.getActiveUsbConfiguration().getUsbInterface((byte) target).release();
        } catch ( UsbClaimException uCE )
        {
            fail("Config " + configurationNumber + " Interface " + target + " is not claimed!");
        } catch ( UsbException uE )
        {
            fail("Config " + configurationNumber + " could not be released!");
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A

        /* Verify proper listener event received */
        assertNull("DeviceDataEvent should be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);

    }


    private void verifyGetStatusIrp (short expectedwIndex,byte expectedbmRequestType)
    {
        //IRP values expected in get feature Irp

        byte expectedbRequest = UsbConst.REQUEST_GET_STATUS;
        short expectedwValue = 0;
        short expectedLength = 2;
        byte[] expectedData = new byte[expectedLength];

        expectedbmRequestType = expectedbmRequestType |                                       // @P3A
                                UsbConst.REQUESTTYPE_TYPE_STANDARD |                          // @P3A
                                UsbConst.REQUESTTYPE_DIRECTION_IN;                            // @P3A

        expectedData[1] = (byte)0;
        expectedData[0] = (byte)0;

        VerifyIrpMethods.verifyRequestTestData(LastUsbDeviceDataEvent.getUsbControlIrp(),
                                               expectedbmRequestType,
                                               expectedbRequest,
                                               expectedwValue,
                                               expectedwIndex,
                                               expectedLength);
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