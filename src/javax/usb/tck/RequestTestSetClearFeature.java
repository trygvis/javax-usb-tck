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
 * $P4           tck.rel1 300916 raulortz Clear global variables and wait for new data
 *                                        or error event occurred when needed
 */

import javax.usb.*;
import javax.usb.event.*;
import javax.usb.util.*;

import junit.framework.TestCase;

/**
 * Request Test - Set and Clear Feature tests
 * 
 * This test creates a Request object and verifies that the SetFeature 
 * and ClearFeature methods send messages to the Default Control Pipe
 * 
 * @author Bob Rossi
 * 
 * The topology.b6 image must be programmed into the programmable device EEPROM
 * for the StandardRequestTest.
 *
 */

public class RequestTestSetClearFeature extends TestCase
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

    public void testSetAndClearFeatureStatic()
    {
        /* The methods being tested are:
         * public static void setFeature(UsbDevice usbDevice, byte recipient,
         *                                                short featureSelector, short target)
         *                             throws usbException, IllegalArgumentException
         *
         * public static void clearFeature(UsbDevice usbDevice, byte recipient,
         * 												 short featureSelector, short target)
         * 							throws usbException, IllegalArgumentException
         */

        short configurationNumber;
        byte recipientType;
        short featureSelection;
        short target;
        short wIndex;

        short status;
        short statusExpected;
        boolean usbExceptionThrown = false;

        /*****************************************************************
         *	First, attempt to setFeature for a Device to a known value
         *****************************************************************/

        configurationNumber = 1;
        //set recipientType to Device
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
        //set featureSelection to Device Remote Wakeup
        featureSelection = UsbConst.FEATURE_SELECTOR_DEVICE_REMOTE_WAKEUP;
        target = 0;
        wIndex = 0;
        statusExpected = 2;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

                                                                                              // @P2D16
            try
            {

                StandardRequest.setFeature(usbDevice, recipientType, featureSelection, target);

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
                }
                catch ( InterruptedException e )
                {
                    fail("Sleep was interrupted");
                }

            }
            catch ( UsbException uE )
            {
                fail ("Got exception setting Feature to a valid value. Exception message: " + uE.getMessage() );
            }
            catch ( IllegalArgumentException iE )
            {
                fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
            }

            /* Verify proper listener event received */
            assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
            assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);
            verifySetFeatureIrp(recipientType, featureSelection, wIndex);                     // @P4A
            
            LastUsbDeviceDataEvent = null;                                                    // @P4A
            LastUsbDeviceErrorEvent = null;                                                   // @P4A
            try                                                                               // @P2A
	    {                                                                                 // @P2A
                status = StandardRequest.getStatus(usbDevice, recipientType, target);
                try                                                                           // @P4A
                {                                                                             // @P4A
                    for ( int i = 0; i < 100; i++ )                                           // @P4A
                    {                                                                         // @P4A
                        if ( (LastUsbDeviceDataEvent != null)|                                // @P4A
                             (LastUsbDeviceErrorEvent != null) )                              // @P4A
                        {                                                                     // @P4A
                            break;                                                            // @P4A
                        }                                                                     // @P4A
                        Thread.sleep( 20 ); //wait 20 ms before checking for event            // @P4A
                    }                                                                         // @P4A
                }                                                                             // @P4A
                catch ( InterruptedException e )                                              // @P4A
                {                                                                             // @P4A
                    fail("Sleep was interrupted");                                            // @P4A
                }                                                                             // @P4A
                assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                             statusExpected, status);
                                                                                              // @P4D
            } catch (UsbException uE)                                                         // @P2A
	    {                                                                                 // @P2A
	        fail("Got exception getting status. Exception message: "+ uE.getMessage() );  // @P2A
	    }                                                                                 // @P2A

                                                                                              // @P2D5
        /**************************************************************
         * Now, do a clearFeature for the same device
         **************************************************************/

        statusExpected = 0;

        try
        {

            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null;

            StandardRequest.clearFeature(usbDevice, recipientType, featureSelection, target);

            try
            {
                /*
                * Wait for device event before leaving clearFeature routine
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
            verifyClearFeatureIrp(recipientType, featureSelection, wIndex);                   // @P4A

            /* Verify proper listener event received */
            assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
            assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);

            LastUsbDeviceDataEvent = null;                                                    // @P4A
            LastUsbDeviceErrorEvent = null;                                                   // @P4A
            status = StandardRequest.getStatus(usbDevice, recipientType, target);
            try                                                                               // @P4A
            {                                                                                 // @P4A
                for ( int i = 0; i < 100; i++ )                                               // @P4A
                {                                                                             // @P4A
                    if ( (LastUsbDeviceDataEvent != null)|                                    // @P4A
                         (LastUsbDeviceErrorEvent != null) )                                  // @P4A
                    {                                                                         // @P4A
                        break;                                                                // @P4A
                    }                                                                         // @P4A
                    Thread.sleep( 20 ); //wait 20 ms before checking for event                // @P4A
                }                                                                             // @P4A
            }                                                                                 // @P4A
            catch ( InterruptedException e )                                                  // @P4A
            {                                                                                 // @P4A
                fail("Sleep was interrupted");                                                // @P4A
            }                                                                                 // @P4A
            assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                         statusExpected, status);

                                                                                              // @P4D

        }
        catch ( UsbException uE )
        {
            fail ("Got exception clearing Feature. Exception message: " + uE.getMessage() );
        }
        catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        /*****************************************************************
         *	Now attempt to setFeature for a Device to an invalid value.
         * UsbException expected.
         *****************************************************************/

        configurationNumber = 2;
        //set recipientType to Device
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
        //set featureSelection to an invalid value
        featureSelection = 3;
        target = 0;
        statusExpected = 0;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
            
                                                                                              // @P2D16
            try
            {

                StandardRequest.setFeature(usbDevice, recipientType, featureSelection, target);

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
                }
                catch ( InterruptedException e )
                {
                    fail("Sleep was interrupted");
                }

            }
            catch ( UsbException uE )
            {
                usbExceptionThrown = true;
            }
            catch ( IllegalArgumentException iE )
            {
                fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
            }
            finally
            {
                assertTrue("UsbException should have been thrown for setting feature to bad value of  "
                           + UsbUtil.toHexString(featureSelection), usbExceptionThrown);
            }
            try
            {
                Thread.sleep(250);
            }
            catch ( InterruptedException e )
            {
            }
            /* Verify proper listener event received */
            assertNull("DeviceDataEvent should be null.", LastUsbDeviceDataEvent);
            assertNotNull("DeviceErrorEvent should not be null.", LastUsbDeviceErrorEvent);

            LastUsbDeviceDataEvent = null;                                                    // @P4A
            LastUsbDeviceErrorEvent = null;                                                   // @P4A
            try                                                                               // @P2A
	    {                                                                                 // @P2A
	        status = StandardRequest.getStatus(usbDevice, recipientType, target);
                try                                                                           // @P4A
                {                                                                             // @P4A
                    for ( int i = 0; i < 100; i++ )                                           // @P4A
                    {                                                                         // @P4A
                        if ( (LastUsbDeviceDataEvent != null)|                                // @P4A
                             (LastUsbDeviceErrorEvent != null) )                              // @P4A
                        {                                                                     // @P4A
                            break;                                                            // @P4A
                        }                                                                     // @P4A
                        Thread.sleep( 20 ); //wait 20 ms before checking for event            // @P4A
                    }                                                                         // @P4A
                }                                                                             // @P4A
                catch ( InterruptedException e )                                              // @P4A 
                {                                                                             // @P4A
                    fail("Sleep was interrupted");                                            // @P4A
                }                                                                             // @P4A
                assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                             statusExpected, status);
            } catch (UsbException uE)                                                          // @P2A
	    {                                                                                  // @P2A
	        fail("Got exception getting status. Exception message: "+ uE.getMessage() );   // @P2A
	    }                                                                                  // @P2A

                                                                                              // @P2D5

        /*****************************************************************
         *	Now attempt to setFeature for an Interface 
         *****************************************************************/

        configurationNumber = 1;
        //set recipientType to Interface
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE;       
        //set featureSelection to any value, no value will be set for an interface
        featureSelection = 1;
        target = 0;
        statusExpected = 0;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;     
        wIndex = 0;


                                                                                              // @P2D16
            try
            {

                try {
                    usbDevice.getActiveUsbConfiguration().getUsbInterface((byte) target).claim();
                } catch ( UsbClaimException uCE ) {
                    fail("Config " + configurationNumber + " Interface " + target + " is already claimed!");
                } catch ( UsbNotActiveException uNAE ) {
                    fail("Config " + configurationNumber + " is not active!");
                } catch ( UsbException uE ) {
                    fail("Config " + configurationNumber + " could not be claimed!");
                } catch ( UsbDisconnectedException uDE )                                      // @P1C
                {                                                                             // @P1A
                    fail ("A connected device should't throw the UsbDisconnectedException!"); // @P1A
                }                                                                             // @P1A

                StandardRequest.setFeature(usbDevice, recipientType, featureSelection, target);

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
                }
                catch ( InterruptedException e )
                {
                    fail("Sleep was interrupted");
                }

            }
            catch ( UsbException uE )
            {
                fail ("Got exception setting Feature to a valid value. Exception message: " + uE.getMessage() );
            }
            catch ( IllegalArgumentException iE )
            {
                fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
            }

            /* Verify proper listener event received */
            assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
            assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

            verifySetFeatureIrp(recipientType, featureSelection, wIndex);

            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null;

            try                                                                               // @P2A
	    {                                                                                 // @P2A
	        status = StandardRequest.getStatus(usbDevice, recipientType, target);

                try                                                                           // @P4A
                {                                                                             // @P4A
                    for ( int i = 0; i < 100; i++ )                                           // @P4A
                    {                                                                         // @P4A
                        if ( (LastUsbDeviceDataEvent != null)|                                // @P4A
                             (LastUsbDeviceErrorEvent != null) )                              // @P4A
                        {                                                                     // @P4A
                            break;                                                            // @P4A
                        }                                                                     // @P4A
                        Thread.sleep( 20 ); //wait 20 ms before checking for event            // @P4A
                    }                                                                         // @P4A
                }                                                                             // @P4A
                catch ( InterruptedException e )                                              // @P4A 
                {                                                                             // @P4A
                    fail("Sleep was interrupted");                                            // @P4A
                }                                                                             // @P4A
                assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                             statusExpected, status);
            } catch (UsbException uE)                                                         // @P2A
	    {                                                                                 // @P2A
	        fail("Got exception getting status. Exception message: "+ uE.getMessage() );  // @P2A
	    }                                                                                 // @P2A

                                                                                              // @P2D5
        /**************************************************************
         * Now, do a clearFeature for the same interface 
         **************************************************************/

        try
        {

            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null;

            StandardRequest.clearFeature(usbDevice, recipientType, featureSelection, target);               

            try
            {
                /*
                * Wait for device event before leaving clearFeature routine
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

            // Verify proper listener event received
            assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
            assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);
            verifyClearFeatureIrp(recipientType, featureSelection, wIndex);                   // @P4A

            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null;

            status = StandardRequest.getStatus(usbDevice, recipientType, target);
            try                                                                               // @P4A
            {                                                                                 // @P4A
                for ( int i = 0; i < 100; i++ )                                               // @P4A
                {                                                                             // @P4A
                    if ( (LastUsbDeviceDataEvent != null)|                                    // @P4A
                         (LastUsbDeviceErrorEvent != null) )                                  // @P4A
                    {                                                                         // @P4A
                        break;                                                                // @P4A
                    }                                                                         // @P4A
                    Thread.sleep( 20 ); //wait 20 ms before checking for event                // @P4A
                }                                                                             // @P4A
            }                                                                                 // @P4A
            catch ( InterruptedException e )                                                  // @P4A
            {                                                                                 // @P4A
                fail("Sleep was interrupted");                                                // @P4A
            }                                                                                 // @P4A
            assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                         statusExpected, status);

                                                                                              // @P4D

        }
        catch ( UsbException uE )
        {
            fail ("Got exception clearing Feature. Exception message: " + uE.getMessage() );
        }
        catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }


        /*********************************************************************
         * Cannot test an invalid value with an interface because no values
         * with an interface are actually set.
         *********************************************************************/

        /*****************************************************************
         *	Now attempt to setFeature for an Endpoint
         *****************************************************************/
        short interfaceNumber;
        short alternateSetting;
        byte endptAddress;
        short endpoint;

        configurationNumber = 1;
        //set recipientType to Endpoint
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;        
        //set featureSelection to Endpoint Halt
        featureSelection = UsbConst.FEATURE_SELECTOR_ENDPOINT_HALT;
        status = 0;
        statusExpected = 1;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;     

                                                                                              // @P2D21        
        /* Set target to a known endpoint on this configuration. */
        endpoint = 0x82;

        try
        {

            StandardRequest.setFeature(usbDevice, recipientType, featureSelection, endpoint);

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
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted");
            }

        }
        catch ( UsbException uE )
        {
            fail ("Got exception setting Feature to a valid value. Exception message: " + uE.getMessage() );
        }
        catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifySetFeatureIrp(recipientType, featureSelection, endpoint);


        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null; 
        try
        {

            status = StandardRequest.getStatus(usbDevice, recipientType, endpoint);
            try                                                                               // @P4A
            {                                                                                 // @P4A
                for ( int i = 0; i < 100; i++ )                                               // @P4A
                {                                                                             // @P4A
                    if ( (LastUsbDeviceDataEvent != null)|                                    // @P4A
                         (LastUsbDeviceErrorEvent != null) )                                  // @P4A
                    {                                                                         // @P4A
                        break;                                                                // @P4A
                    }                                                                         // @P4A
                    Thread.sleep( 20 ); //wait 20 ms before checking for event                // @P4A
                }                                                                             // @P4A
            }                                                                                 // @P4A
            catch ( InterruptedException e )                                                  // @P4A 
            {                                                                                 // @P4A
                fail("Sleep was interrupted");                                                // @P4A
            }                                                                                 // @P4A

            Thread.sleep(250);  
        }
        catch ( InterruptedException e )
        {

        }
        catch ( UsbException uE )
        {
            fail ("Got exception getting the device status.  Exception message: " + uE.getMessage() );          
        }
        assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                     statusExpected, status);            


        /**************************************************************
         * Now, do a clearFeature for the same endpoint
         **************************************************************/    

        statusExpected = 0; 

        try
        {

            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null;

            StandardRequest.clearFeature(usbDevice, recipientType, featureSelection, endpoint);             

            try
            {
                /*
                * Wait for device event before leaving clearFeature routine
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

            /* Verify proper listener event received */
            assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
            assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);
            verifyClearFeatureIrp(recipientType, featureSelection, endpoint);                 // @P4A

            LastUsbDeviceDataEvent = null;                                                    // @P4A
            LastUsbDeviceErrorEvent = null;                                                   // @P4A
            status = StandardRequest.getStatus(usbDevice, recipientType, endpoint);
            try                                                                               // @P4A
            {                                                                                 // @P4A
                for ( int i = 0; i < 100; i++ )                                               // @P4A
                {                                                                             // @P4A
                    if ( (LastUsbDeviceDataEvent != null)|                                    // @P4A
                         (LastUsbDeviceErrorEvent != null) )                                  // @P4A
                    {                                                                         // @P4A
                        break;                                                                // @P4A
                    }                                                                         // @P4A
                    Thread.sleep( 20 ); //wait 20 ms before checking for event                // @P4A
                }                                                                             // @P4A
            }                                                                                 // @P4A
            catch ( InterruptedException e )                                                  // @P4A
            {                                                                                 // @P4A
                fail("Sleep was interrupted");                                                // @P4A
            }                                                                                 // @P4A
            assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                         statusExpected, status);

                                                                                              // @P4D

        }
        catch ( UsbException uE )
        {
            fail ("Got exception clearing Feature. Exception message: " + uE.getMessage() );
        }
        catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }


        /************************************************************************
         *	Now attempt to setFeature for an Endpoint with an invalid value.
         * UsbException expected.
         ************************************************************************/


        configurationNumber = 1;
        //set recipientType to Endpoint
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;        
        //set featureSelection to an invalid value
        featureSelection = 3;
        status = 0;
        statusExpected = 0;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;     

                                                                                              // @P2D21        
        /* Set target to a known endpoint on this configuration. */
        endpoint = 0x82;

        try
        {

            StandardRequest.setFeature(usbDevice, recipientType, featureSelection, endpoint);

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
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted");
            }

        }
        catch ( UsbException uE )
        {
            usbExceptionThrown = true;
        }
        catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }
        finally
        {
            assertTrue("UsbException should have been thrown for setting feature to bad value of  " 
                       + UsbUtil.toHexString(featureSelection), usbExceptionThrown);   
        }

        try
        {
            Thread.sleep(250);
        }
        catch ( InterruptedException e )
        {
        }
        /* Verify proper listener event received */
        assertNull("DeviceDataEvent should be null.", LastUsbDeviceDataEvent);
        assertNotNull("DeviceErrorEvent should not be null.", LastUsbDeviceErrorEvent); 

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {

            status = StandardRequest.getStatus(usbDevice, recipientType, endpoint);

            Thread.sleep(250);  
        }
        catch ( InterruptedException e )
        {

        }
        catch ( UsbException uE )
        {
            fail ("Got exception getting the device status.  Exception message: " + uE.getMessage() );          
        }
        assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                     statusExpected, status);            


        /************************************************************************
         *	Now attempt to setFeature for an Endpoint with an Illegal Argument
         *	for recipientType.  IllegalArgument exception expected.
         ************************************************************************/  


        configurationNumber = 1;
        //set recipientType to an illegal index
        recipientType = 3;      
        //set featureSelection to an invalid value
        featureSelection = UsbConst.FEATURE_SELECTOR_ENDPOINT_HALT;
        status = 0;
        statusExpected = 0;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;     

                                                                                              // @P2D21        
        /* Set target to an endpoint that is not on this configuration. */
        endpoint = 0x82;

        try
        {

            StandardRequest.setFeature(usbDevice, recipientType, featureSelection, endpoint);

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
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted");
            }

        }
        catch ( UsbException uE )
        {
            fail ("Got unexpected exception");
        }
        catch ( IllegalArgumentException iE )
        {
            usbExceptionThrown = true;
        }
        finally
        {
            assertTrue("UsbException should have been thrown for setting feature to bad value of  " 
                       + UsbUtil.toHexString(featureSelection), usbExceptionThrown);   
        }

        try
        {
            Thread.sleep(250);
        }
        catch ( InterruptedException e )
        {
        }

        /* Verify proper listener event received */
        /* Don't get data or error event with illegal argument exception*/
        assertNull("DeviceDataEvent should be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);    

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null; 
        //set recipientType to back to Endpoint
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;        

        try
        {

            status = StandardRequest.getStatus(usbDevice, recipientType, endpoint);

            Thread.sleep(250);  
        }
        catch ( InterruptedException e )
        {

        }
        catch ( UsbException uE )
        {
            usbExceptionThrown = true;
        }
        finally
        {
            assertTrue("UsbException should have been thrown for getStatus with a bad endpoint address." 
                       + UsbUtil.toHexString(featureSelection), usbExceptionThrown);   
        }
        assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                     statusExpected, status);

        try {
            usbDevice.getActiveUsbConfiguration().getUsbInterface((byte) target).release();
        } catch ( UsbClaimException uCE ) {
            fail("Config " + configurationNumber + " Interface " + target + " is not claimed!");
        } catch ( UsbException uE ) {
            fail("Config " + configurationNumber + " could not be released!");
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A

    }


    public void testSetAndClearFeatureNonStatic()
    {
        /* The methods being tested are:
         * public static void setFeature(byte recipient,
         *                                                short featureSelector, short target)
         *                             throws usbException, IllegalArgumentException
         *
         * public static void clearFeature(byte recipient,
         * 												 short featureSelector, short target)
         * 							throws usbException, IllegalArgumentException
         */


        StandardRequest standardRequest = new StandardRequest(usbDevice);

        short configurationNumber;
        byte recipientType;
        short featureSelection;
        short target;
        short wIndex;

        short status;
        short statusExpected;
        boolean usbExceptionThrown = false;

        /*****************************************************************
         *	First, attempt to setFeature for a Device to a known value
         *****************************************************************/

        configurationNumber = 1;
        //set recipientType to Device
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
        //set featureSelection to Device Remote Wakeup
        featureSelection = UsbConst.FEATURE_SELECTOR_DEVICE_REMOTE_WAKEUP;
        target = 0;
        wIndex = 0;
        statusExpected = 2;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;


                                                                                              // @P2D16
            try
            {

                standardRequest.setFeature(recipientType, featureSelection, target);

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
                }
                catch ( InterruptedException e )
                {
                    fail("Sleep was interrupted");
                }

            }
            catch ( UsbException uE )
            {
                fail ("Got exception setting Feature to a valid value. Exception message: " + uE.getMessage() );
            }
            catch ( IllegalArgumentException iE )
            {
                fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
            }
            verifySetFeatureIrp(recipientType, featureSelection, wIndex);                     // @P4A

            /* Verify proper listener event received */
            assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
            assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);

            LastUsbDeviceDataEvent = null;                                                    // @P4A
            LastUsbDeviceErrorEvent = null;                                                   // @P4A
            try                                                                               // @P2A
	    {                                                                                 // @P2A
	        status = standardRequest.getStatus(recipientType, target);
                try                                                                           // @P4A
                {                                                                             // @P4A
                    for ( int i = 0; i < 100; i++ )                                           // @P4A
                    {                                                                         // @P4A
                        if ( (LastUsbDeviceDataEvent != null)|                                // @P4A
                             (LastUsbDeviceErrorEvent != null) )                              // @P4A
                        {                                                                     // @P4A
                            break;                                                            // @P4A
                        }                                                                     // @P4A
                        Thread.sleep( 20 ); //wait 20 ms before checking for event            // @P4A
                    }                                                                         // @P4A
                }                                                                             // @P4A
                catch ( InterruptedException e )                                              // @P4A
                {                                                                             // @P4A
                    fail("Sleep was interrupted");                                            // @P4A
                }                                                                             // @P4A
                assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                             statusExpected, status);
                                                                                              // @P4D
            } catch (UsbException uE)                                                         // @P2A
	    {                                                                                 // @P2A
	        fail("Got exception getting status. Exception message: "+ uE.getMessage() );  // @P2A
	    }                                                                                 // @P2A

                                                                                              // @P2D5
        /**************************************************************
         * Now, do a clearFeature for the same device 
         **************************************************************/

        statusExpected = 0;

        try
        {

            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null;

            standardRequest.clearFeature(recipientType, featureSelection, target);              

            try
            {
                /*
                * Wait for device event before leaving clearFeature routine
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
            verifyClearFeatureIrp(recipientType, featureSelection, wIndex);                   // @P4A

            /* Verify proper listener event received */
            assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
            assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);

            LastUsbDeviceDataEvent = null;                                                    // @P4A
            LastUsbDeviceErrorEvent = null;                                                   // @P4A
            status = standardRequest.getStatus(recipientType, target);
            try                                                                               // @P4A
            {                                                                                 // @P4A
                for ( int i = 0; i < 100; i++ )                                               // @P4A
                {                                                                             // @P4A
                    if ( (LastUsbDeviceDataEvent != null)|                                    // @P4A
                         (LastUsbDeviceErrorEvent != null) )                                  // @P4A
                    {                                                                         // @P4A
                        break;                                                                // @P4A
                    }                                                                         // @P4A
                    Thread.sleep( 20 ); //wait 20 ms before checking for event                // @P4A
                }                                                                             // @P4A
            }                                                                                 // @P4A
            catch ( InterruptedException e )                                                  // @P4A
            {                                                                                 // @P4A
                fail("Sleep was interrupted");                                                // @P4A
            }                                                                                 // @P4A
            assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                         statusExpected, status);

                                                                                              // @P4D

        }
        catch ( UsbException uE )
        {
            fail ("Got exception clearing Feature. Exception message: " + uE.getMessage() );
        }
        catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        /*****************************************************************
         *	Now attempt to setFeature for a Device to an invalid value
         *****************************************************************/

        configurationNumber = 2;
        //set recipientType to Device
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;      
        //set featureSelection to an invalid value
        featureSelection = 3;
        target = 0;
        statusExpected = 0;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;             


                                                                                              // @P2D16
            try
            {

                standardRequest.setFeature(recipientType, featureSelection, target);

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
                }
                catch ( InterruptedException e )
                {
                    fail("Sleep was interrupted");
                }

            }
            catch ( UsbException uE )
            {
                usbExceptionThrown = true;
            }
            catch ( IllegalArgumentException iE )
            {
                fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
            }
            finally
            {
                assertTrue("UsbException should have been thrown for setting feature to bad value of  " 
                           + UsbUtil.toHexString(featureSelection), usbExceptionThrown);                   
            }
            try
            {
                Thread.sleep(250);
            }
            catch ( InterruptedException e )
            {
            }
            /* Verify proper listener event received */
            assertNull("DeviceDataEvent should be null.", LastUsbDeviceDataEvent);
            assertNotNull("DeviceErrorEvent should not be null.", LastUsbDeviceErrorEvent);

            LastUsbDeviceDataEvent = null;                                                    // @P4A
            LastUsbDeviceErrorEvent = null;                                                   // @P4A
            try                                                                               // @P2A
	    {                                                                                 // @P2A
	        status = standardRequest.getStatus(recipientType, target);

                try                                                                           // @P4A
                {                                                                             // @P4A
                    for ( int i = 0; i < 100; i++ )                                           // @P4A
                    {                                                                         // @P4A
                        if ( (LastUsbDeviceDataEvent != null)|                                // @P4A
                             (LastUsbDeviceErrorEvent != null) )                              // @P4A
                        {                                                                     // @P4A
                            break;                                                            // @P4A
                        }                                                                     // @P4A
                        Thread.sleep( 20 ); //wait 20 ms before checking for event            // @P4A
                    }                                                                         // @P4A
                }                                                                             // @P4A
                catch ( InterruptedException e )                                              // @P4A 
                {                                                                             // @P4A
                    fail("Sleep was interrupted");                                            // @P4A
                }                                                                             // @P4A
                assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                             statusExpected, status);
            } catch (UsbException uE)                                                         // @P2A
	    {                                                                                 // @P2A
	        fail("Got exception getting status. Exception message: "+ uE.getMessage() );  // @P2A
	    }                                                                                 // @P2A

                                                                                              // @P2D5

        /*****************************************************************
         *	Now attempt to setFeature for an Interface 
         *****************************************************************/

        configurationNumber = 1;
        //set recipientType to Interface
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE;       
        //set featureSelection to any value, no value will be set for an interface
        featureSelection = 1;
        target = 0;
        statusExpected = 0;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;     
        wIndex = 0;     


                                                                                              // @P2D16
            try
            {

                try {
                    usbDevice.getActiveUsbConfiguration().getUsbInterface((byte) target).claim();
                } catch ( UsbClaimException uCE ) {
                    fail("Config " + configurationNumber + " Interface " + target + " is already claimed!");
                } catch ( UsbNotActiveException uNAE ) {
                    fail("Config " + configurationNumber + " is not active!");
                } catch ( UsbException uE ) {
                    fail("Config " + configurationNumber + " could not be claimed!");
                } catch ( UsbDisconnectedException uDE )                                      // @P1C
                {                                                                             // @P1A
                    fail ("A connected device should't throw the UsbDisconnectedException!"); // @P1A
                }                                                                             // @P1A
                standardRequest.setFeature(recipientType, featureSelection, target);

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
                }
                catch ( InterruptedException e )
                {
                    fail("Sleep was interrupted");
                }

            }
            catch ( UsbException uE )
            {
                fail ("Got exception setting Feature to a valid value. Exception message: " + uE.getMessage() );
            }
            catch ( IllegalArgumentException iE )
            {
                fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
            }

            /* Verify proper listener event received */
            assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
            assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

            verifySetFeatureIrp(recipientType, featureSelection, wIndex);

            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null; 

	    try                                                                               // @P2A
	    {                                                                                 // @P2A
                status = standardRequest.getStatus(recipientType, target);
                try                                                                           // @P4A
                {                                                                             // @P4A
                    for ( int i = 0; i < 100; i++ )                                           // @P4A
                    {                                                                         // @P4A
                        if ( (LastUsbDeviceDataEvent != null)|                                // @P4A
                             (LastUsbDeviceErrorEvent != null) )                              // @P4A
                        {                                                                     // @P4A
                            break;                                                            // @P4A
                        }                                                                     // @P4A
                        Thread.sleep( 20 ); //wait 20 ms before checking for event            // @P4A
                    }                                                                         // @P4A
                }                                                                             // @P4A
                catch ( InterruptedException e )                                              // @P4A 
                {                                                                             // @P4A
                    fail("Sleep was interrupted");                                            // @P4A
                }                                                                             // @P4A
                assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                             statusExpected, status);
            } catch (UsbException uE)                                                         // @P2A
	    {                                                                                 // @P2A
	        fail("Got exception getting status. Exception message: "+ uE.getMessage() );  // @P2A
	    }                                                                                 // @P2A

                                                                                              // @P2D5
        /**************************************************************
         * Now, do a clearFeature for the same interface 
         **************************************************************/

        try
        {

            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null;

            standardRequest.clearFeature(recipientType, featureSelection, target);              

            try
            {
                /*
                * Wait for device event before leaving clearFeature routine
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

            /* Verify proper listener event received */
            assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
            assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);
            verifyClearFeatureIrp(recipientType, featureSelection, wIndex);                   // @P4A

            LastUsbDeviceDataEvent = null;                                                    // @P4A
            LastUsbDeviceErrorEvent = null;                                                   // @P4A
            status = standardRequest.getStatus(recipientType, target);
            try                                                                               // @P4A
            {                                                                                 // @P4A
                for ( int i = 0; i < 100; i++ )                                               // @P4A
                {                                                                             // @P4A
                    if ( (LastUsbDeviceDataEvent != null)|                                    // @P4A
                         (LastUsbDeviceErrorEvent != null) )                                  // @P4A
                    {                                                                         // @P4A
                        break;                                                                // @P4A
                    }                                                                         // @P4A
                    Thread.sleep( 20 ); //wait 20 ms before checking for event                // @P4A
                }                                                                             // @P4A
            }                                                                                 // @P4A
            catch ( InterruptedException e )                                                  // @P4A
            {                                                                                 // @P4A
                fail("Sleep was interrupted");                                                // @P4A
            }                                                                                 // @P4A
            assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                         statusExpected, status);

                                                                                              // @P4D

        }
        catch ( UsbException uE )
        {
            fail ("Got exception clearing Feature. Exception message: " + uE.getMessage() );
        }
        catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }


        /*********************************************************************
         * Cannot test an invalid value with an interface because no values
         * with an interface are actually set.
         *********************************************************************/

        /*****************************************************************
         *	Now attempt to setFeature for an Endpoint
         *****************************************************************/
        short interfaceNumber;
        short alternateSetting;
        byte endptAddress;
        short endpoint;

        configurationNumber = 1;
        //set recipientType to Endpoint
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;        
        //set featureSelection to Endpoint Halt
        featureSelection = UsbConst.FEATURE_SELECTOR_ENDPOINT_HALT;
        status = 0;
        statusExpected = 1;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;     


                                                                                              // @P2D21
        /* Set target to a known endpoint on this configuration. */
        endpoint = 0x82;

        try
        {

            standardRequest.setFeature(recipientType, featureSelection, endpoint);

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
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted");
            }

        }
        catch ( UsbException uE )
        {
            fail ("Got exception setting Feature to a valid value. Exception message: " + uE.getMessage() );
        }
        catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifySetFeatureIrp(recipientType, featureSelection, endpoint);

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null; 
        try
        {

            status = standardRequest.getStatus(recipientType, endpoint);

            Thread.sleep(250);  
        }
        catch ( InterruptedException e )
        {

        }
        catch ( UsbException uE )
        {
            fail ("Got exception getting the device status.  Exception message: " + uE.getMessage() );          
        }
        assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                     statusExpected, status);            

        /**************************************************************
         * Now, do a clearFeature for the same endpoint
         **************************************************************/    

        statusExpected = 0; 

        try
        {

            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null;

            standardRequest.clearFeature(recipientType, featureSelection, endpoint);                

            try
            {
                /*
                * Wait for device event before leaving clearFeature routine
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

            /* Verify proper listener event received */
            assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
            assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);
            verifyClearFeatureIrp(recipientType, featureSelection, endpoint);                 // @P4A

            LastUsbDeviceDataEvent = null;                                                    // @P4A
            LastUsbDeviceErrorEvent = null;                                                   // @P4A
            status = standardRequest.getStatus(recipientType, endpoint);
            try                                                                               // @P4A
            {                                                                                 // @P4A
                for ( int i = 0; i < 100; i++ )                                               // @P4A
                {                                                                             // @P4A
                    if ( (LastUsbDeviceDataEvent != null)|                                    // @P4A
                         (LastUsbDeviceErrorEvent != null) )                                  // @P4A
                    {                                                                         // @P4A
                        break;                                                                // @P4A
                    }                                                                         // @P4A
                    Thread.sleep( 20 ); //wait 20 ms before checking for event                // @P4A
                }                                                                             // @P4A
            }                                                                                 // @P4A
            catch ( InterruptedException e )                                                  // @P4A
            {                                                                                 // @P4A
                fail("Sleep was interrupted");                                                // @P4A
            }                                                                                 // @P4A
            assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                         statusExpected, status);

                                                                                              // @P4D

        }
        catch ( UsbException uE )
        {
            fail ("Got exception clearing Feature. Exception message: " + uE.getMessage() );
        }
        catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }


        /************************************************************************
         *	Now attempt to setFeature for an Endpoint with an invalid value.
         * UsbException expected.
         ************************************************************************/


        configurationNumber = 1;
        //set recipientType to Endpoint
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;        
        //set featureSelection to an invalid value
        featureSelection = 3;
        status = 0;
        statusExpected = 0;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;     

                                                                                              // @P2D21
        /* Set target to a known endpoint on this configuration. */
        endpoint = 0x82;

        try
        {

            standardRequest.setFeature(recipientType, featureSelection, endpoint);

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
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted");
            }

        }
        catch ( UsbException uE )
        {
            usbExceptionThrown = true;
        }
        catch ( IllegalArgumentException iE )
        {
            fail ("Got illegal argument exception.  Exception message" + iE.getMessage() );
        }
        finally
        {
            assertTrue("UsbException should have been thrown for setting feature to bad value of  " 
                       + UsbUtil.toHexString(featureSelection), usbExceptionThrown);   
        }
        try
        {
            Thread.sleep(250);
        }
        catch ( InterruptedException e )
        {
        }
        /* Verify proper listener event received */
        assertNull("DeviceDataEvent should be null.", LastUsbDeviceDataEvent);
        assertNotNull("DeviceErrorEvent should not be null.", LastUsbDeviceErrorEvent); 

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null; 
        try
        {

            status = standardRequest.getStatus(recipientType, endpoint);

            Thread.sleep(250);  
        }
        catch ( InterruptedException e )
        {

        }
        catch ( UsbException uE )
        {
            fail ("Got exception getting the device status.  Exception message: " + uE.getMessage() );          
        }
        assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                     statusExpected, status);            


        /************************************************************************
         *	Now attempt to setFeature for an Endpoint with an Illegal Argument
         *	for recipientType.  IllegalArgument exception expected.
         ************************************************************************/  


        configurationNumber = 1;
        //set recipientType to an illegal index
        recipientType = 3;      
        featureSelection = UsbConst.FEATURE_SELECTOR_ENDPOINT_HALT;
        status = 0;
        statusExpected = 0;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;     

                                                                                              // @P2D21
        /* Set target to a valid endpoint for this configuration */
        endpoint = 0x82;

        try
        {

            standardRequest.setFeature(recipientType, featureSelection, endpoint);

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
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted");
            }

        }
        catch ( UsbException uE )
        {
            fail ("Got unexpected exception");
        }
        catch ( IllegalArgumentException iE )
        {
            usbExceptionThrown = true;
        }
        finally
        {
            assertTrue("UsbException should have been thrown for setting feature to bad value of  " 
                       + UsbUtil.toHexString(featureSelection), usbExceptionThrown);   
        }

        try
        {
            Thread.sleep(250);
        }
        catch ( InterruptedException e )
        {
        }

        /* Verify proper listener event received */
        /* Don't get data or error event with illegal argument exception*/
        assertNull("DeviceDataEvent should be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null; 
        //set recipientType to back to Endpoint
        recipientType = UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;        

        try
        {

            status = standardRequest.getStatus(recipientType, endpoint);

            Thread.sleep(250);  
        }
        catch ( InterruptedException e )
        {

        }
        catch ( UsbException uE )
        {
            usbExceptionThrown = true;
        }
        finally
        {
            assertTrue("UsbException should have been thrown for getStatus with a bad endpoint address." 
                       + UsbUtil.toHexString(featureSelection), usbExceptionThrown);   
        }
        assertEquals("Status is not as expected.  Expected value = " + statusExpected + ", Actual value = " + status,
                     statusExpected, status);

        try {
            usbDevice.getActiveUsbConfiguration().getUsbInterface((byte) target).release();
        } catch ( UsbClaimException uCE ) {
            fail("Config " + configurationNumber + " Interface " + target + " is not claimed!");
        } catch ( UsbException uE ) {
            fail("Config " + configurationNumber + " could not be released!");
        } catch ( UsbDisconnectedException uDE )                                              // @P1C
        {                                                                                     // @P1A
            fail ("A connected device should't throw the UsbDisconnectedException!");         // @P1A
        }                                                                                     // @P1A

}




    private void verifySetFeatureIrp (byte expectedbmRequestType, short expectedwValue, short expectedwIndex)
    {
        //IRP values expected in set feature Irp


        byte expectedbRequest = UsbConst.REQUEST_SET_FEATURE;
        short expectedwLength = 0;

        expectedbmRequestType = (byte) ( expectedbmRequestType |                              // @P3A
                                         UsbConst.REQUESTTYPE_TYPE_STANDARD |                 // @P3A
                                         UsbConst.REQUESTTYPE_DIRECTION_OUT );                // @P3A

        VerifyIrpMethods.verifyRequestTest(LastUsbDeviceDataEvent.getUsbControlIrp(),
                                           expectedbmRequestType,
                                           expectedbRequest,
                                           expectedwValue,
                                           expectedwIndex);
    }


    private void verifyClearFeatureIrp (byte expectedbmRequestType, short expectedwValue, short expectedwIndex)
    {
        //IRP values expected in get feature Irp

        byte expectedbRequest = UsbConst.REQUEST_CLEAR_FEATURE;


        expectedbmRequestType = (byte) ( expectedbmRequestType |                              // @P3A
                                         UsbConst.REQUESTTYPE_TYPE_STANDARD |                 // @P3A
                                         UsbConst.REQUESTTYPE_DIRECTION_OUT );                // @P3A

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
