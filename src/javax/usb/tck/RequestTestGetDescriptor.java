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

import java.io.*;

import javax.usb.*;
import javax.usb.event.*;
import javax.usb.util.*;

import junit.framework.TestCase;

/**
 * Request Test - Get Descriptor tests
 * 
 * This test creates a Request object and verifies that the GetDescriptor 
 * method sends messages to the Default Control Pipe
 * 
 * @author Bob Rossi
 * 
 * The topology.b6 image must be programmed into the programmable device EEPROM
 * for the StandardRequestTest.
 *
 */

public class RequestTestGetDescriptor extends TestCase
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

    public void testGetDescriptorStatic()
    {
        /* The method being tested is:
         * public static int getDescriptor(UsbDevice usbDevice, byte type, 
         *                                                byte index, short langid, byte[] data)
         *                             throws usbException
         */     


        /***********************************************************************
         *	Do a getDescriptor for the device - minimum desc length
         ***********************************************************************/
        byte requestType;
        int retdDescriptorLength;
        byte expectedDescLength;
        byte descType;
        byte descIndex;
        byte descLength;
        short langid;
        byte[] descriptorData;
        boolean usbExceptionThrown = false;

        requestType = UsbConst.ENDPOINT_DIRECTION_OUT;
        /* set type to 1 for device descriptor */
        descType = UsbConst.DESCRIPTOR_TYPE_DEVICE;

        descIndex = 0;
        /* set length for minimum size descriptor */
        descLength = UsbConst.DESCRIPTOR_MIN_LENGTH;
        /* langid is 0 for a device descriptor */
        langid = 0;
        /* will hold value of the actual descriptor length */
        retdDescriptorLength = 0;
        /* size of byte[] will be descLength, but the actual descriptor will be the size
         * of a standard device descriptor */
        expectedDescLength = UsbConst.DESCRIPTOR_MIN_LENGTH_DEVICE;
        descriptorData = null;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;


        descriptorData = setDescriptorData(descLength, descType);       

        try
        {

            retdDescriptorLength = StandardRequest.getDescriptor(usbDevice, descType,
                                                                 descIndex, langid,
                                                                 descriptorData);
            try
            {
                /*
                * Wait for device event before leaving getDescriptor routine
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
            fail("Got exception getting the Descriptor.  Exception message: " + uE.getMessage());
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyDevDescriptorData(descLength, expectedDescLength, descType, descriptorData);  
        verifyGetDescriptorIrp(descType, descIndex, langid, retdDescriptorLength);



        /***********************************************************************
         *	Do a getDescriptor for the device - standard device desc length
         ***********************************************************************/   

        requestType = UsbConst.REQUESTTYPE_DIRECTION_IN;
        /* set type to 1 for device descriptor */
        descType = UsbConst.DESCRIPTOR_TYPE_DEVICE;
        descIndex = 0;
        /* set length for standard device descriptor */
        descLength = UsbConst.DESCRIPTOR_MIN_LENGTH_DEVICE;
        expectedDescLength = descLength;
        /* langid is 0 for a device descriptor */
        langid = 0;
        retdDescriptorLength = 0;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        descriptorData = setDescriptorData(descLength, descType);       

        try
        {

            retdDescriptorLength = StandardRequest.getDescriptor(usbDevice, descType,
                                                                 descIndex, langid,
                                                                 descriptorData);
            try
            {
                /*
                * Wait for device event before leaving getDescriptor routine
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
            fail("Got exception getting the Descriptor.  Exception message: " + uE.getMessage());
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyDevDescriptorData(descLength, expectedDescLength, descType, descriptorData);  
        verifyGetDescriptorIrp(descType, descIndex, langid, retdDescriptorLength);


        /***********************************************************************
         *	Do a getDescriptor for a configuration - minimum desc length
         ***********************************************************************/   


        requestType = UsbConst.REQUESTTYPE_DIRECTION_IN;
        /* set type to 2 for configuration descriptor */
        descType = UsbConst.DESCRIPTOR_TYPE_CONFIGURATION;
        /* get configuration #1 */
        descIndex = 1;
        /* set length for minimum size descriptor */
        descLength = UsbConst.DESCRIPTOR_MIN_LENGTH;

        /* size of byte[] will be descLength, but the actual descriptor will be the size
         * of a standard configuration descriptor */
        expectedDescLength = UsbConst.DESCRIPTOR_MIN_LENGTH_CONFIGURATION;
        /* langid is 0 for a configuration descriptor */
        langid = 0;
        retdDescriptorLength = 0;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        descriptorData = setDescriptorData(descLength, descType);


        try
        {

            retdDescriptorLength = StandardRequest.getDescriptor(usbDevice, descType,
                                                                 descIndex, langid,
                                                                 descriptorData);
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
            fail("Got exception getting the Descriptor.  Exception message: " + uE.getMessage());
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        


        verifyConfigDescriptorData(descLength, expectedDescLength, descType, descriptorData, descIndex);    
        verifyGetDescriptorIrp(descType, descIndex, langid, retdDescriptorLength);


        /***********************************************************************
         *	Do a getDescriptor for a configuration - length of the descriptor
         ***********************************************************************/   

        /* Use the same descriptor that was used for minimum length
         * Don't need to get the descriptor again */


        /* set length for standard configuration descriptor */
        descLength = UsbConst.DESCRIPTOR_MIN_LENGTH_CONFIGURATION;
        expectedDescLength = descLength;

        descriptorData = setDescriptorData(descLength, descType);

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;         

        try
        {

            retdDescriptorLength = StandardRequest.getDescriptor(usbDevice, descType,
                                                                 descIndex, langid,
                                                                 descriptorData);

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
            fail("Got exception getting the Descriptor.  Exception message: " + uE.getMessage());
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        


        verifyConfigDescriptorData(descLength, expectedDescLength, descType, descriptorData, descIndex);    
        verifyGetDescriptorIrp(descType, descIndex, langid, retdDescriptorLength);


        /*************************************************************************
         *	Do a getDescriptor for a string - min length for string descriptor
         *************************************************************************/ 

        requestType = UsbConst.REQUESTTYPE_DIRECTION_IN;
        /* set type to 3 for string descriptor */
        descType = UsbConst.DESCRIPTOR_TYPE_STRING;
        /* get string #1 */
        descIndex = 1;
        /* set length for minimum size descriptor */
        descLength = UsbConst.DESCRIPTOR_MIN_LENGTH;

        /* size of byte[] will be descLength, but the actual descriptor will be the size
         * of the string descriptor, which is 26 for this string */
        expectedDescLength = 26;
        /* langid is 1033 for a string descriptor */
        langid = 1033;
        retdDescriptorLength = 0;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        descriptorData = setDescriptorData(descLength, descType);

        try
        {

            retdDescriptorLength = StandardRequest.getDescriptor(usbDevice, descType,
                                                                 descIndex, langid,
                                                                 descriptorData);

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
            fail("Got exception getting the Descriptor.  Exception message: " + uE.getMessage());
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyGetDescriptorIrp(descType, descIndex, langid, retdDescriptorLength);  
        verifyStringDescriptorData(descLength, expectedDescLength, descType, descriptorData, descIndex);    


        /*************************************************************************
        *	Do a getDescriptor for the string - length = size of string descriptor
        *************************************************************************/  

        /* set length to size of actual string descriptor, as returned in last
         * getDescriptor() call */
        descLength = descriptorData[0];

        /* size of byte[] will be descLength, but the actual descriptor will be the size
         * of the string descriptor which is 26 for this string */
        expectedDescLength = 26;
        langid = 1033;

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        descriptorData = setDescriptorData(descLength, descType);

        try
        {

            retdDescriptorLength = StandardRequest.getDescriptor(usbDevice, descType,
                                                                 descIndex, langid,
                                                                 descriptorData);                                                                                                                
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
            fail("Got exception getting the Descriptor.  Exception message: " + uE.getMessage());
        }

        try
        {
            /*Wait for getDescriptor event before continuing */
            Thread.sleep(250);
        }
        catch ( InterruptedException e )
        {
            fail("Sleep was interrupted.");
        }
        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);

        verifyGetDescriptorIrp(descType, descIndex, langid, retdDescriptorLength);  
        verifyStringDescriptorData(descLength, expectedDescLength, descType, descriptorData, descIndex);    

    }


    public void testGetDescriptorNonStatic()
    {
        /* The method being tested is:
         * public static int getDescriptor(byte type, byte index, 
         *                                                 short langid, byte[] data)
         *                             throws usbException
         */     

        StandardRequest standardRequest = new StandardRequest(usbDevice);

        /***********************************************************************
         *	Do a getDescriptor for the device - minimum desc length
         ***********************************************************************/
        byte requestType;
        int retdDescriptorLength;
        byte expectedDescLength;
        byte descType;
        byte descIndex;
        byte descLength;
        short langid;
        byte[] descriptorData;
        boolean usbExceptionThrown = false;

        requestType = UsbConst.REQUESTTYPE_DIRECTION_IN;
        /* set type to 1 for device descriptor */
        descType = UsbConst.DESCRIPTOR_TYPE_DEVICE;
        descIndex = 0;
        /* set length for minimum size descriptor */
        descLength = UsbConst.DESCRIPTOR_MIN_LENGTH;
        /* langid is 0 for a device descriptor */
        langid = 0;
        retdDescriptorLength = 0;
        /* size of byte[] will be descLength, but the actual descriptor will be the size
         * of a standard device descriptor */
        expectedDescLength = UsbConst.DESCRIPTOR_MIN_LENGTH_DEVICE;
        descriptorData = null;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        descriptorData = setDescriptorData(descLength, descType);       

        try
        {

            retdDescriptorLength = standardRequest.getDescriptor(descType,
                                                                 descIndex, langid,
                                                                 descriptorData);
            try
            {
                /*
                * Wait for device event before leaving getDescriptor routine
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
            fail("Got exception getting the Descriptor.  Exception message: " + uE.getMessage());
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyDevDescriptorData(descLength, expectedDescLength, descType, descriptorData);  
        verifyGetDescriptorIrp(descType, descIndex, langid, retdDescriptorLength);

        /***********************************************************************
         *	Do a getDescriptor for the device - standard device desc length
         ***********************************************************************/   

        requestType = UsbConst.REQUESTTYPE_DIRECTION_IN;
        /* set type to 1 for device descriptor */
        descType = UsbConst.DESCRIPTOR_TYPE_DEVICE;
        descIndex = 0;
        /* set length for standard device descriptor */
        descLength = UsbConst.DESCRIPTOR_MIN_LENGTH_DEVICE;
        expectedDescLength = descLength;
        /* langid is 0 for a device descriptor */
        langid = 0;
        retdDescriptorLength = 0;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        descriptorData = setDescriptorData(descLength, descType);       

        try
        {

            retdDescriptorLength = standardRequest.getDescriptor(descType,
                                                                 descIndex, langid,
                                                                 descriptorData);
            try
            {
                /*
                * Wait for device event before leaving getDescriptor routine
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
            fail("Got exception getting the Descriptor.  Exception message: " + uE.getMessage());
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyDevDescriptorData(descLength, expectedDescLength, descType, descriptorData);  
        verifyGetDescriptorIrp(descType, descIndex, langid, retdDescriptorLength);

        /***********************************************************************
         *	Do a getDescriptor for a configuration - minimum desc length
         ***********************************************************************/   

        requestType = UsbConst.REQUESTTYPE_DIRECTION_IN;
        /* set type to 2 for configuration descriptor */
        descType = UsbConst.DESCRIPTOR_TYPE_CONFIGURATION;
        /* get configuration #1 */
        descIndex = 1;
        /* set length for minimum size descriptor */
        descLength = UsbConst.DESCRIPTOR_MIN_LENGTH;

        /* size of byte[] will be descLength, but the actual descriptor will be the size
         * of a standard configuration descriptor */
        expectedDescLength = UsbConst.DESCRIPTOR_MIN_LENGTH_CONFIGURATION;
        /* langid is 0 for a configuration descriptor */
        langid = 0;
        retdDescriptorLength = 0;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        descriptorData = setDescriptorData(descLength, descType);

        try
        {

            retdDescriptorLength = standardRequest.getDescriptor(descType,
                                                                 descIndex, langid,
                                                                 descriptorData);
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
            fail("Got exception getting the Descriptor.  Exception message: " + uE.getMessage());
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyConfigDescriptorData(descLength, expectedDescLength, descType, descriptorData, descIndex);    
        verifyGetDescriptorIrp(descType, descIndex, langid, retdDescriptorLength);

        /***********************************************************************
         *	Do a getDescriptor for a configuration - length of the descriptor
         ***********************************************************************/   

        /* Use the same descriptor that was used for minimum length
         * Don't need to get the descriptor again */

        /* set length for standard configuration descriptor */
        descLength = UsbConst.DESCRIPTOR_MIN_LENGTH_CONFIGURATION;
        expectedDescLength = descLength;

        descriptorData = setDescriptorData(descLength, descType);

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;         

        try
        {

            retdDescriptorLength = standardRequest.getDescriptor(descType,
                                                                 descIndex, langid,
                                                                 descriptorData);

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
            fail("Got exception getting the Descriptor.  Exception message: " + uE.getMessage());
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyConfigDescriptorData(descLength, expectedDescLength, descType, descriptorData, descIndex);    
        verifyGetDescriptorIrp(descType, descIndex, langid, retdDescriptorLength);

        /*************************************************************************
         *	Do a getDescriptor for a string - min length for string descriptor
         *************************************************************************/ 

        requestType = UsbConst.REQUESTTYPE_DIRECTION_IN;
        /* set type to 3 for string descriptor */
        descType = UsbConst.DESCRIPTOR_TYPE_STRING;
        /* get string #1 */
        descIndex = 1;
        /* set length for minimum size descriptor */
        descLength = UsbConst.DESCRIPTOR_MIN_LENGTH;

        /* size of byte[] will be descLength, but the actual descriptor will be the size
         * of the string descriptor, which is 26 for this string */
        expectedDescLength = 26;
        /* langid is 0 for a string descriptor */
        langid = 1033;
        retdDescriptorLength = 0;
        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        descriptorData = setDescriptorData(descLength, descType);

        try
        {

            retdDescriptorLength = standardRequest.getDescriptor(descType,
                                                                 descIndex, langid,
                                                                 descriptorData);

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
            fail("Got exception getting the Descriptor.  Exception message: " + uE.getMessage());
        }

        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);        

        verifyGetDescriptorIrp(descType, descIndex, langid, retdDescriptorLength);  
        verifyStringDescriptorData(descLength, expectedDescLength, descType, descriptorData, descIndex);    

        /*************************************************************************
        *	Do a getDescriptor for the string - length = size of string descriptor
        *************************************************************************/  

        /* set length to size of actual string descriptor, as returned in last
         * getDescriptor() call */
        descLength = descriptorData[0];

        /* size of byte[] will be descLength, but the actual descriptor will be the size
         * of the string descriptor which is 26 for this string */
        expectedDescLength = 26;
        langid = 1033;

        LastUsbDeviceDataEvent = null;
        LastUsbDeviceErrorEvent = null;

        descriptorData = setDescriptorData(descLength, descType);

        try
        {

            retdDescriptorLength = standardRequest.getDescriptor(descType,
                                                                 descIndex, langid,
                                                                 descriptorData);                                                                                                                
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
            fail("Got exception getting the Descriptor.  Exception message: " + uE.getMessage());
        }

        try
        {
            /*Wait for getDescriptor event before continuing */
            Thread.sleep(250);
        }
        catch ( InterruptedException e )
        {
            fail("Sleep was interrupted.");
        }
        /* Verify proper listener event received */
        assertNotNull("DeviceDataEvent should not be null.", LastUsbDeviceDataEvent);
        assertNull("DeviceErrorEvent should be null.", LastUsbDeviceErrorEvent);

        verifyGetDescriptorIrp(descType, descIndex, langid, retdDescriptorLength);  
        verifyStringDescriptorData(descLength, expectedDescLength, descType, descriptorData, descIndex);    

    }


    private byte[] setDescriptorData(byte bLength, byte bDescriptorType)
    {
        /* loads the requested descriptor data into the byte[] */

        byte[] descriptorData;

        descriptorData = new byte[bLength];

        descriptorData[0] = bLength;
        descriptorData[1] = bDescriptorType;

        return descriptorData;                  
    }   

    private void verifyDevDescriptorData(byte bLength, byte standardDescLength, 
                                         byte bDescriptorType,
                                         byte[] descriptorData)
    {
        short bcdUSB;
        byte bDeviceClass;
        byte bDeviceSubClass;
        byte bDeviceProtocol;
        byte bMaxPacketSize0;
        short idVendor;
        short idProduct;
        short bcdDevice;
        byte iManufacturer;
        byte iProduct;
        byte iSerialNumber;
        byte bNumConfigurations;        

        assertEquals("The Device Descriptor length doesn't match the expected value.  Expected value = " 
                     + standardDescLength + " Actual value = " + usbDevice.getUsbDeviceDescriptor().bLength(),
                     standardDescLength, usbDevice.getUsbDeviceDescriptor().bLength() );
        assertEquals("The Descriptor Type doesn't match the expected value.  Expected value = " 
                     + bDescriptorType + " Actual value = " + usbDevice.getUsbDeviceDescriptor().bDescriptorType(),
                     bDescriptorType, usbDevice.getUsbDeviceDescriptor().bDescriptorType() ); 

        if ( bLength > 2 )
        {
            bcdUSB = UsbUtil.toShort(descriptorData[3], descriptorData[2]);
            bDeviceClass = descriptorData[4];
            bDeviceSubClass = descriptorData[5];
            bDeviceProtocol = descriptorData[6];
            bMaxPacketSize0 = descriptorData[7];
            idVendor = UsbUtil.toShort(descriptorData[9], descriptorData[8]);
            idProduct = UsbUtil.toShort(descriptorData[11], descriptorData[10]);
            bcdDevice = UsbUtil.toShort(descriptorData[13], descriptorData[12]);
            iManufacturer = descriptorData[14];
            iProduct = descriptorData[15];
            iSerialNumber = descriptorData[16];
            bNumConfigurations = descriptorData[17];            

            assertEquals("The USB spec version doesn't match the expected value.  Expected value = " 
                         + bcdUSB + " Actual value = " + usbDevice.getUsbDeviceDescriptor().bcdUSB(),
                         bcdUSB, usbDevice.getUsbDeviceDescriptor().bcdUSB() );
            assertEquals("The Device Class value doesn't match the expected value.  Expected value = " 
                         + bDeviceClass + " Actual value = " + usbDevice.getUsbDeviceDescriptor().bDeviceClass(),
                         bDeviceClass, usbDevice.getUsbDeviceDescriptor().bDeviceClass() );
            assertEquals("The Device SubClass value doesn't match the expected value.  Expected value = " 
                         + bDeviceSubClass + " Actual value = " + usbDevice.getUsbDeviceDescriptor().bDeviceSubClass(),
                         bDeviceSubClass, usbDevice.getUsbDeviceDescriptor().bDeviceSubClass() );
            assertEquals("The Device Protocol value doesn't match the expected value.  Expected value = " 
                         + bDeviceProtocol + " Actual value = " + usbDevice.getUsbDeviceDescriptor().bDeviceProtocol(),
                         bDeviceProtocol, usbDevice.getUsbDeviceDescriptor().bDeviceProtocol() );
            assertEquals("The Max Packet Size doesn't match the expected value.  Expected value = " 
                         + bMaxPacketSize0 + " Actual value = " + usbDevice.getUsbDeviceDescriptor().bMaxPacketSize0(),
                         bMaxPacketSize0, usbDevice.getUsbDeviceDescriptor().bMaxPacketSize0() );
            assertEquals("The Vendor ID doesn't match the expected value.  Expected value = " 
                         + idVendor + " Actual value = " + usbDevice.getUsbDeviceDescriptor().idVendor(),
                         idVendor, usbDevice.getUsbDeviceDescriptor().idVendor() );
            assertEquals("The Product ID doesn't match the expected value.  Expected value = " 
                         + idProduct + " Actual value = " + usbDevice.getUsbDeviceDescriptor().idProduct(),
                         idProduct, usbDevice.getUsbDeviceDescriptor().idProduct() );
            assertEquals("The Device release number doesn't match the expected value.  Expected value = " 
                         + bcdDevice + " Actual value = " + usbDevice.getUsbDeviceDescriptor().bcdDevice(),
                         bcdDevice, usbDevice.getUsbDeviceDescriptor().bcdDevice() );
            assertEquals("The Manufacturer string doesn't match the expected value.  Expected value = " 
                         + iManufacturer + " Actual value = " + usbDevice.getUsbDeviceDescriptor().iManufacturer(),
                         iManufacturer, usbDevice.getUsbDeviceDescriptor().iManufacturer() );
            assertEquals("The Product string doesn't match the expected value.  Expected value = " 
                         + iProduct + " Actual value = " + usbDevice.getUsbDeviceDescriptor().iProduct(),
                         iProduct, usbDevice.getUsbDeviceDescriptor().iProduct() );
            assertEquals("The Serial Number string doesn't match the expected value.  Expected value = " 
                         + iSerialNumber + " Actual value = " + usbDevice.getUsbDeviceDescriptor().iSerialNumber(),
                         iSerialNumber, usbDevice.getUsbDeviceDescriptor().iSerialNumber() );
            assertEquals("The number of Configurations doesn't match the expected value.  Expected value = " 
                         + bNumConfigurations + " Actual value = " + usbDevice.getUsbDeviceDescriptor().bNumConfigurations(),
                         bNumConfigurations, usbDevice.getUsbDeviceDescriptor().bNumConfigurations() );
        }
    }


    private void verifyConfigDescriptorData(byte bLength, byte standardDescLength, 
                                            byte bDescriptorType,
                                            byte[] descriptorData,
                                            byte configNumber)
    {
        short wTotalLength;
        byte bNumInterfaces;
        byte bConfigurationValue;
        byte iConfiguration;
        byte bmAttributes;
        byte bMaxPower;     
        UsbConfigurationDescriptor configDescriptor;

        configDescriptor = usbDevice.getUsbConfiguration(configNumber).getUsbConfigurationDescriptor();

        assertEquals("The Configuration Descriptor length doesn't match the expected value.  Expected value = " 
                     + standardDescLength + " Actual value = " + configDescriptor.bLength(),
                     standardDescLength, configDescriptor.bLength() );
        assertEquals("The Descriptor Type doesn't match the expected value.  Expected value = " 
                     + bDescriptorType + " Actual value = " + configDescriptor.bDescriptorType(),
                     bDescriptorType, configDescriptor.bDescriptorType() );   

        if ( bLength > 2 )
        {
            wTotalLength = 316;
            bNumInterfaces = 2;
            bConfigurationValue = 1;
            iConfiguration = 4;
            bmAttributes = -96;
            bMaxPower = 50;

            assertEquals("The Total Length doesn't match the expected value.  Expected value = "
                         + wTotalLength + " Actual value = " + configDescriptor.wTotalLength(),
                         wTotalLength, configDescriptor.wTotalLength()); 
            assertEquals("The Number of interfaces doesn't match the expected value.  Expected value = "
                         + bNumInterfaces + " Actual value = " + configDescriptor.bNumInterfaces(),
                         bNumInterfaces, configDescriptor.bNumInterfaces());     
            assertEquals("The Configuration Value doesn't match the expected value.  Expected value = "
                         + bConfigurationValue + " Actual value = " + configDescriptor.bConfigurationValue(),
                         bConfigurationValue, configDescriptor.bConfigurationValue());
            assertEquals("The Configuration index doesn't match the expected value.  Expected value = "
                         + iConfiguration + " Actual value = " + configDescriptor.iConfiguration(),
                         iConfiguration, configDescriptor.iConfiguration());
            assertEquals("The Attributes value doesn't match the expected value.  Expected value = "
                         + bmAttributes + " Actual value = " + configDescriptor.bmAttributes(),
                         bmAttributes, configDescriptor.bmAttributes());
            assertEquals("The MaxPower doesn't match the expected value.  Expected value = "
                         + bMaxPower + " Actual value = " + configDescriptor.bMaxPower(),
                         bMaxPower, configDescriptor.bMaxPower());
        }
    }

    private void verifyStringDescriptorData(byte bLength, byte standardDescLength, 
                                            byte bDescriptorType,
                                            byte[] descriptorData,
                                            byte stringNumber)
    {
        UsbStringDescriptor stringDescriptor;
        String expectedString;
        String actualString;

        expectedString = "Manufacturer";

        try
        {

            stringDescriptor = usbDevice.getUsbStringDescriptor(stringNumber);
            actualString = stringDescriptor.getString();

            assertEquals("The String Descriptor length doesn't match the expected value.  Expected value = " 
                         + standardDescLength + " Actual value = " + stringDescriptor.bLength(),
                         standardDescLength, stringDescriptor.bLength() );
            assertEquals("The Descriptor Type doesn't match the expected value.  Expected value = " 
                         + bDescriptorType + " Actual value = " + stringDescriptor.bDescriptorType(),
                         bDescriptorType, stringDescriptor.bDescriptorType() );           

            if ( bLength > 2 )
            {
                assertEquals("The string data does not match the expected string data.",
                             expectedString, actualString);                      
            }

        }
        catch ( UsbException uE )
        {
            fail ("Unexpected exception getting the string descriptor. " + uE.getMessage());
        }
        catch ( UnsupportedEncodingException uEE )
        {
            fail ("Unsupported encoding exception occurred." + uEE.getMessage());
        }
    }


    private void verifyGetDescriptorIrp (byte descType,
                                         byte descIndex,
                                         short expectedIndex,
                                         int expectedLength)
    {
        //IRP values expected in get feature Irp

        byte expectedbRequest = UsbConst.REQUEST_GET_DESCRIPTOR;
        short expectedValue;
        byte expectedbmRequestType = UsbConst.REQUESTTYPE_RECIPIENT_DEVICE |                  // @P1C
                                     UsbConst.REQUESTTYPE_TYPE_STANDARD |                     // @P1A
                                     UsbConst.REQUESTTYPE_DIRECTION_IN;                       // @P1A

        expectedValue = UsbUtil.toShort(descType, descIndex);

        VerifyIrpMethods.verifyRequestTestData(LastUsbDeviceDataEvent.getUsbControlIrp(),
                                               expectedbmRequestType,
                                               expectedbRequest,
                                               expectedValue,
                                               expectedIndex,
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