package javax.usb.tck;

/**
 * Copyright (c) 2004, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
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


        /********************************************************************************
         * Set configuration to 1, interface to 0 and alternate setting to 0.
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
                                
        try {
            // First set configuration to known value 
            StandardRequest.setConfiguration(usbDevice, expectedConfigurationValue);
            try {
                // Wait for setConfiguration event before continuing 
            Thread.sleep(250);
            } catch (InterruptedException e) {
                fail("Sleep was interrupted.");
            }		
        } catch (UsbException uE) {
            fail("Got exception setting Configuration to a valid value. Exception message:  " + uE.getMessage());		
        }
         
        // Now set interface and alternate setting to known values. 
        try { 	

            // Claim the interface before setting it

            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null;		
        
            StandardRequest.setInterface(usbDevice, expectedInterfaceNumber, expectedAlternateSetting);

            frameInterface= usbDevice.getUsbConfiguration((byte)expectedConfigurationValue).getUsbInterface((byte)expectedInterfaceNumber);
            frameInterface.claim();
    
            try {
                // Wait for setInterface event before continuing 
            Thread.sleep(250);
            } catch (InterruptedException e) {
                fail("Sleep was interrupted.");
            }		
        } catch (UsbException uE) {
			try
				{
					frameInterface.release();
				}
				catch (Exception e)
				{
					fail("Got exception releasing Interface. Exception message:  " + e.getMessage());	
				}
            fail("Got exception setting Interface to a valid value. Exception message " + uE.getMessage());
        }
         
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;	 
        // Now call synch frame for a known endpoint to get the frame number 
        try {
            frameNumber = StandardRequest.synchFrame(usbDevice, endpoint);
            try {							
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
            } catch (InterruptedException e) {
                fail("Sleep was interrupted");
            }
            
        } catch (UsbException uE) {
			if (frameInterface != null)
			{
				try
				{
					frameInterface.release();
				}
				catch (Exception e)
				{
					fail("Got exception releasing Interface. Exception message:  " + e.getMessage());	
				}
				
			}
            fail ("Got exception setting Synch Frame.  Exception message: " + uE.getMessage());			
        } 
        
        try {
            Thread.sleep(250);
        } catch ( InterruptedException e) {
            fail ("Sleep was interrupted.");
        }			
        
        // Verify proper listener event received 
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);	
        verifySynchFrame(endpoint);
    
        // Release the interface, so it may be claimed by other threads
        try {
			if (frameInterface != null)
			{
				frameInterface.release();
			}
        	
           // usbDevice.getUsbConfiguration((byte)expectedConfigurationValue).getUsbInterface((byte)expectedInterfaceNumber).release();
        } catch (UsbException uE) {
            fail("Got exception releasing Interface. Exception message:  " + uE.getMessage());					
        } 





        /********************************************************************************
         * Set configuration to 1, interface to 0 and alternate setting to 1.
         * Then attempt to get the frame number for an endpoint.
         * Should get exception using this non-isochronous endpoint
         * *******************************************************************************/

        expectedConfigurationValue = 1;
        expectedInterfaceNumber = 0;
        expectedAlternateSetting = 1;
        endpoint = 0x81;         
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        usbExceptionThrown = false;

        try
        {
            /* First set configuration to known value */
            StandardRequest.setConfiguration(usbDevice, expectedConfigurationValue);
            try
            {
                /*Wait for setConfiguration event before continuing */
                Thread.sleep(250);
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }
        }
        catch ( UsbException uE )
        {
            fail("Got exception setting Configuration to a valid value. Exception message:  " + uE.getMessage());       
        }

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;  
        /* Now set interface and alternate setting to known values. */
        try
        {

            StandardRequest.setInterface(usbDevice, expectedInterfaceNumber, expectedAlternateSetting);

            try
            {
                /*Wait for setInterface event before continuing */
                Thread.sleep(250);
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }
        }
        catch ( UsbException uE )
        {
            fail("Got exception setting Interface to a valid value. Exception message " + uE.getMessage());
        }

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;  
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
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted");
            }

        }
        catch ( UsbException uE )
        {
            usbExceptionThrown = true;
            //fail ("Got exception setting Synch Frame.  Exception message: " + uE.getMessage());
        }
        finally
        {
            assertTrue("UsbException should have been thrown for calling synchFrame on this non-Isochronous endpoint, "
                       + UsbUtil.toHexString(endpoint), usbExceptionThrown);
        }


        try
        {
            Thread.sleep(250);
        }
        catch ( InterruptedException e )
        {
            fail("Sleep was interrupted");
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
                                
        try {
            // First set configuration to known value 
            standardRequest.setConfiguration(expectedConfigurationValue);
            try {
                // Wait for setConfiguration event before continuing 
            Thread.sleep(250);
            } catch (InterruptedException e) {
                fail("Sleep was interrupted.");
            }		
        } catch (UsbException uE) {
            fail("Got exception setting Configuration to a valid value. Exception message:  " + uE.getMessage());		
        }
         
        // Now set interface and alternate setting to known values. 
        try { 	

            // Claim the interface before setting it

            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null;		
        
            standardRequest.setInterface(expectedInterfaceNumber, expectedAlternateSetting);

            frameInterface= usbDevice.getUsbConfiguration((byte)expectedConfigurationValue).getUsbInterface((byte)expectedInterfaceNumber);
            frameInterface.claim();
    
            try {
                // Wait for setInterface event before continuing 
            Thread.sleep(250);
            } catch (InterruptedException e) {
                fail("Sleep was interrupted.");
            }		
        } catch (UsbException uE) {
            fail("Got exception setting Interface to a valid value. Exception message " + uE.getMessage());
        }
         
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;	 
        // Now call synch frame for a known endpoint to get the frame number 
        try {
            frameNumber = standardRequest.synchFrame(endpoint);
            try {							
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
            } catch (InterruptedException e) {
                fail("Sleep was interrupted");
            }
            
        } catch (UsbException uE) {
			// Release the interface, so it may be claimed by other threads
			if (frameInterface != null)
			{
				try
				{
					frameInterface.release();
				}
				catch (Exception e)
				{
					fail("Got exception releasing Interface. Exception message:  " + e.getMessage());	
				}
				
			}
            fail ("Got exception setting Synch Frame.  Exception message: " + uE.getMessage());			
        } 
        
        try {
            Thread.sleep(250);
        } catch ( InterruptedException e) {
            fail ("Sleep was interrupted.");
        }			
        
        // Verify proper listener event received 
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);	
        verifySynchFrame(endpoint);
    
        // Release the interface, so it may be claimed by other threads
        try {
			// Release the interface, so it may be claimed by other threads
			if (frameInterface != null)
			{
				frameInterface.release();
			}
            //usbDevice.getUsbConfiguration((byte)expectedConfigurationValue).getUsbInterface((byte)expectedInterfaceNumber).release();
        } catch (UsbException uE) {
            fail("Got exception releasing Interface. Exception message:  " + uE.getMessage());					
        } 






        /********************************************************************************
         * Set configuration to 1, interface to 0 and alternate setting to 1.
         * Then attempt to get the frame number for an endpoint.
         * Should get exception using this non-isochronous endpoint
         * *******************************************************************************/

        expectedConfigurationValue = 1;
        expectedInterfaceNumber = 0;
        expectedAlternateSetting = 1;
        endpoint = 0x81;         
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        usbExceptionThrown = false;


        try
        {
            /* First set configuration to known value */
            standardRequest.setConfiguration(expectedConfigurationValue);
            try
            {
                /*Wait for setConfiguration event before continuing */
                Thread.sleep(250);
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }
        }
        catch ( UsbException uE )
        {
            fail("Got exception setting Configuration to a valid value. Exception message:  " + uE.getMessage());       
        }

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;  
        /* Now set interface and alternate setting to known values. */
        try
        {

            standardRequest.setInterface(expectedInterfaceNumber, expectedAlternateSetting);
            try
            {
                /*Wait for setInterface event before continuing */
                Thread.sleep(250);
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }
        }
        catch ( UsbException uE )
        {
            fail("Got exception setting Interface to a valid value. Exception message " + uE.getMessage());
        }

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;  
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
        finally
        {
            assertTrue("UsbException should have been thrown for calling synchFrame on this non-Isochronous endpoint, " 
                       + UsbUtil.toHexString(endpoint), usbExceptionThrown);   
        }
        try
        {
            Thread.sleep(250);
        }
        catch ( InterruptedException e )
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

        byte expectedbmRequestType = (UsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT +
                                      UsbConst.REQUESTTYPE_DIRECTION_IN);
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