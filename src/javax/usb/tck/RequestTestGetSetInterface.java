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
 * Request Test - Get and Set Interface tests
 * 
 * This test creates a Request object and verifies that the GetInterface 
 * and SetInterface methods send messages to the Default Control Pipe
 * 
 * @author Bob Rossi
 * 
 * The topology.b6 image must be programmed into the programmable device EEPROM
 * for the StandardRequestTest.
 *
 */


public class RequestTestGetSetInterface extends TestCase
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

    public void testSetAndGetInterfaceStatic()

    {
        /* The methods being tested are as follows:
         *	public static void setInterface(UsbDevice usbDevice,short interfaceNumber, short alternateSetting)
         *							throws UsbException
         *
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
         * Set Interface AlternateSetting to a known value and verify 
         * with getInterface
         ********************************************************************/

        configurationNumber = 2;    
        expectedInterfaceNumber = 2;
        expectedAlternateSetting = 1;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        try
        {
            // Set configuration then set Interface to a known value 

            StandardRequest.setConfiguration(usbDevice, configurationNumber);
            try
            {
                /*Wait for setConfiguration Data event before continuing */
                Thread.sleep(250);
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }

            // Claim the interface before setting it 	
            usbDevice.getUsbConfiguration((byte)configurationNumber).getUsbInterface((byte)expectedInterfaceNumber).claim();

            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null;    

            StandardRequest.setInterface(usbDevice, expectedInterfaceNumber, expectedAlternateSetting);         

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
            fail("Got exception setting Interface to a valid value. Exception message:  " + uE.getMessage());                   
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);    
        verifySetInterfaceIrp(expectedAlternateSetting,expectedInterfaceNumber);

        /* Now we'll get the current Interface Alternate Setting we just set. */
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {

            StandardRequest.setConfiguration(usbDevice, configurationNumber);
            try
            {
                /*Wait for setConfiguration Data event before continuing */
                Thread.sleep(250);
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }

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
        verifyGetInterfaceIrp(expectedInterfaceNumber); 


        /********************************************************************************
         * Try to set the interface alternate setting to an invalid value.  
         * UsbException expected.
         ********************************************************************************/
        usbExceptionThrown = false;
        configurationNumber = 2;    
        expectedInterfaceNumber = 3;
        expectedAlternateSetting = 2;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;     

        try
        {
            /* First set Interface with an invalid Alternate Setting value */ 

            StandardRequest.setConfiguration(usbDevice, configurationNumber);
            try
            {
                /*Wait for setConfiguration Data event before continuing */ 
                Thread.sleep(250);
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }

            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null;     
            StandardRequest.setInterface(usbDevice, expectedInterfaceNumber, expectedAlternateSetting);
            fail("Shouldn't get here because exception should be thrown for bad interface number of " 
                 + UsbUtil.toHexString(expectedInterfaceNumber));

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
            assertTrue("UsbException should have been thrown for bad Interface number of " 
                       + UsbUtil.toHexString(expectedInterfaceNumber), usbExceptionThrown);    
        }
        try
        {
            Thread.sleep(250);
        }
        catch ( InterruptedException e )
        {
        }
        /* Verify proper listener event received
         * Note that these asserts are opposite the other event asserts
         * because an exception is expected. 
         * */
        assertNull("DeviceDataEvent should be null.", LastUsbDeviceDataEvent);
        assertNotNull("DeviceErrorEvent should not be null.", LastUsbDeviceErrorEvent);

        /* Verify that we can get the last valid interface number */
        usbExceptionThrown = false;
        configurationNumber = 2;
        expectedInterfaceNumber = 2;
        expectedAlternateSetting = 1;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {

            StandardRequest.setConfiguration(usbDevice, configurationNumber);
            try
            {
                /*Wait for setConfiguration Data event before continuing */
                Thread.sleep(250);
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }

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
        verifyGetInterfaceIrp(expectedInterfaceNumber);     


        // Release the interface, so it may be claimed by other threads
        try
        {
            usbDevice.getUsbConfiguration((byte)configurationNumber).getUsbInterface((byte)expectedInterfaceNumber).release();      
        }
        catch ( UsbException uE )
        {
            fail("Got exception releasing Interface. Exception message:  " + uE.getMessage());                  
        }

    }


    public void testSetAndGetInterfaceNonStatic()

    {
        /* The methods being tested are as follows:
         *	public void setInterface(short interfaceNumber, short alternateSetting)
         *							throws UsbException
         *
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
         * Set Interface AlternateSetting to a known value and verify 
         * with getInterface
         ********************************************************************/

        configurationNumber = 2;    
        expectedInterfaceNumber = 2;
        expectedAlternateSetting = 1;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;     

        try
        {
            /* GetConfiguration then set Interface to a known value */

            standardRequest.setConfiguration(configurationNumber);
            try
            {
                /*Wait for setConfiguration Data event before continuing */
                Thread.sleep(250);
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }

            // Claim the interface before setting it 
            usbDevice.getUsbConfiguration((byte)configurationNumber).getUsbInterface((byte)expectedInterfaceNumber).claim();

            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null;     

            standardRequest.setInterface(expectedInterfaceNumber, expectedAlternateSetting);            


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
            fail("Got exception setting Interface to a valid value. Exception message:  " + uE.getMessage());                   
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);    
        verifySetInterfaceIrp(expectedAlternateSetting,expectedInterfaceNumber);

        /* Now we'll get the current Interface Alternate Setting we just set. */
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {

            standardRequest.setConfiguration(configurationNumber);
            try
            {
                /*Wait for setConfiguration Data event before continuing */
                Thread.sleep(250);
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }

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
        verifyGetInterfaceIrp(expectedInterfaceNumber); 

        /********************************************************************************
         * Try to set the interface alternate setting to an invalid value.  
         * UsbException expected.
         ********************************************************************************/
        usbExceptionThrown = false;
        configurationNumber = 2;    
        expectedInterfaceNumber = 3;
        expectedAlternateSetting = 2;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;     

        try
        {
            /* First set Interface with an invalid Alternate Setting value */ 

            standardRequest.setConfiguration(configurationNumber);
            try
            {
                /*Wait for setConfiguration Data event before continuing */ 
                Thread.sleep(250);
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }

            LastUsbDeviceDataEvent = null;
            LastUsbDeviceErrorEvent = null;     
            standardRequest.setInterface(expectedInterfaceNumber, expectedAlternateSetting);
            fail("Shouldn't get here because exception should be thrown for bad interface number of " 
                 + UsbUtil.toHexString(expectedInterfaceNumber));

            try
            {
                /*
                * Wait for device event before leaving setInterface routine */

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
            assertTrue("UsbException should have been thrown for bad Interface number of " 
                       + UsbUtil.toHexString(expectedInterfaceNumber), usbExceptionThrown);    
        }
        try
        {
            Thread.sleep(250);
        }
        catch ( InterruptedException e )
        {
        }
        /* Verify proper listener event received
         * Note that these asserts are opposite the other event asserts
         * because an exception is expected. 
         * */
        assertNull("DeviceDataEvent should be null.", LastUsbDeviceDataEvent);
        assertNotNull("DeviceErrorEvent should not be null.", LastUsbDeviceErrorEvent);

        /* Verify that we can get the last valid interface number */
        usbExceptionThrown = false;
        configurationNumber = 2;
        expectedInterfaceNumber = 2;
        expectedAlternateSetting = 1;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;
        try
        {

            standardRequest.setConfiguration(configurationNumber);
            try
            {
                /*Wait for setConfiguration Data event before continuing */
                Thread.sleep(250);
            }
            catch ( InterruptedException e )
            {
                fail("Sleep was interrupted.");
            }

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
        verifyGetInterfaceIrp(expectedInterfaceNumber);     

        // Release the interface, so it may be claimed by other threads
        try
        {
            usbDevice.getUsbConfiguration((byte)configurationNumber).getUsbInterface((byte)expectedInterfaceNumber).release();
        }
        catch ( UsbException uE )
        {
            fail("Got exception releasing Interface. Exception message:  " + uE.getMessage());                  
        }

    }

    private void verifyGetInterfaceIrp (short expectedwIndex)
    {
        //IRP values expected in get interface Irp

        byte expectedbmRequestType = (UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE +
                                      UsbConst.REQUESTTYPE_DIRECTION_IN);
        byte expectedbRequest = UsbConst.REQUEST_GET_INTERFACE;
        short expectedwValue = 0;

        VerifyIrpMethods.verifyRequestTest(LastUsbDeviceDataEvent.getUsbControlIrp(),
                                           expectedbmRequestType,
                                           expectedbRequest,
                                           expectedwValue,
                                           expectedwIndex);
    }

    private void verifySetInterfaceIrp (short expectedwValue, short expectedwIndex)
    {
        //IRP values expected in set interface Irp

        byte expectedbmRequestType = UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE;
        byte expectedbRequest = UsbConst.REQUEST_SET_INTERFACE;

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