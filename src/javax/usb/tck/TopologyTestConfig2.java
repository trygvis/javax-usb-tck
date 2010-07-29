package javax.usb.tck;

/**
 * Copyright (c) 2004, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.*;

import junit.framework.*;

import javax.usb.*;

/**
 * Topology Test Configuration 2 - Tests Topology Hardware Configuration 2
 * <p>
 * This test verifies that the API can traverse a hierarchical device topology
 * and that it properly reveals the correct pre-configured topology as defined
 * by the  test hardware setup.  For the UsbDevice methods (and UsbConfig,
 * etc. down to the UsbEndpoint) only the Cypress development board is
 * used, other devices are present only to verify the accuracy of the topology
 * tree.
 * @author Joshua Lowry
 */

public class TopologyTestConfig2 extends TestCase implements TopologyTests
{

    public TopologyTestConfig2(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
    }

    protected void tearDown() throws Exception
    {
    }

    /**
     * Tests Hardware Configuration 2 from the Javax.usb TCK
     * specification.
     * @throws Exception
     */
    public void testTopologyConfigTwo() throws Exception {
        UsbServices usbUsbServices = null;
        UsbHub usbVirtualRootUsbHub = null;
        UsbHub usb1stLayerHub = null;
        UsbDevice usbProgramableDevice = null;
        UsbDevice usbLowSpeedDevice = null;
        UsbDeviceDescriptor usbProgramableDevDescriptor = null;
        Properties usbUsbProperties;
        List usbListUsbDevices;

        assertEquals("The API version should be set to " + APIVersion,
                     APIVersion, Version.getApiVersion());
        assertEquals("The USB version should be set to " + USBVersion,
                     USBVersion, Version.getUsbVersion());

        try
        {
            usbUsbServices = UsbHostManager.getUsbServices();
        }
        catch ( UsbException uE )
        {
            fail("An error occurred when attempting to create the UsbServices implementation");
        }
        catch ( SecurityException sE )
        {
            fail("The caller doesn't have security access to the UsbServices implementation");
        }

        try
        {
            usbUsbProperties = UsbHostManager.getProperties();
        }
        catch ( UsbException uE )
        {
            fail("An error occurred when attempting to load the properties file");          
        }
        catch ( SecurityException sE )
        {
            fail("The caller doesn't have security access to the properties file");
        }

        assertNotNull("A minimum version returned by UsbServices getApiVersion is required",
                      usbUsbServices.getApiVersion());
        assertNotNull("Some version returned by UsbServices getImpVersion is required",
                      usbUsbServices.getImpVersion());
        assertNotNull("Some description returned by UsbServices getImpDescription is required",
                      usbUsbServices.getImpDescription());

        try
        {
            usbVirtualRootUsbHub = usbUsbServices.getRootUsbHub();
        }
        catch ( UsbException uE )
        {
            fail("An error occurred when accessing javax.usb");         
        }
        catch ( SecurityException sE )
        {
            fail("The current client is not configured to access javax.usb");
        }

        assertTrue("This device should be the virtual root UsbHub!",
                   usbVirtualRootUsbHub.isRootUsbHub());
        assertTrue("The virtual root UsbHub should be a UsbHub!",
                   usbVirtualRootUsbHub.isUsbHub());
        assertTrue("There should be at least two root UsbHubs attached to the virtual root UsbHub!",
                   usbVirtualRootUsbHub.getNumberOfPorts() >= 2);

        usbListUsbDevices = usbVirtualRootUsbHub.getAttachedUsbDevices();

        assertTrue("There should be more than one virtual root UsbHub!",
                   usbListUsbDevices.size() >= 2);
        assertEquals("The # of root UsbHubs should equal then # of ports on the vitual root UsbHub!",
                     usbVirtualRootUsbHub.getNumberOfPorts(),
                     usbListUsbDevices.size());

        int numHubsWithDevices = 0;

        for ( int i = 0; i < usbListUsbDevices.size(); i++ )
        {
            UsbDevice usbCurrentDevice;
            UsbHub usbCurrentHub;
            List usbNextList;

            usbCurrentDevice = (UsbDevice)usbListUsbDevices.get(i);
            assertTrue("All devices connected to the virtual root UsbHub should be UsbHubs!",
                       usbCurrentDevice.isUsbHub());

            usbCurrentHub = (UsbHub)usbListUsbDevices.get(i);
            usbNextList = usbCurrentHub.getAttachedUsbDevices();

            if ( usbNextList.size() > 0 )
            {
                UsbDevice usbNextDevice;

                numHubsWithDevices++;
                assertEquals("There is more than one device connected to the root hub!",
                             (int)1, usbNextList.size());
                usbNextDevice = (UsbDevice) usbNextList.get(0);
                assertTrue("The connected device should be a UsbHub!",
                           usbNextDevice.isUsbHub());
                usb1stLayerHub = (UsbHub) usbNextList.get(0);
            }
        }

        assertEquals("Only one root UsbHub should have a device connected in this configuration!",
                     (int) 1, numHubsWithDevices);

        usbListUsbDevices = usb1stLayerHub.getAttachedUsbDevices();

        assertEquals("Only two devices should be attached to the UsbHub!",
                     (int) 2, usbListUsbDevices.size());

        for ( int i = 0; i < usbListUsbDevices.size(); i++ )
        {
            UsbDevice usbCurrentDevice;
            UsbDeviceDescriptor usbCurrentDevDescriptor;

            usbCurrentDevice = (UsbDevice) usbListUsbDevices.get(i);
            assertFalse("The connected device should not be a UsbHub!",
                        usbCurrentDevice.isUsbHub());
            usbCurrentDevDescriptor = usbCurrentDevice.getUsbDeviceDescriptor();
            if ( usbCurrentDevDescriptor.idVendor() == programableVendorID &&
                 usbCurrentDevDescriptor.idProduct() == programableProductID )
            {
                usbProgramableDevice = usbCurrentDevice;
                usbProgramableDevDescriptor = usbCurrentDevDescriptor;  
            }
            else
            {
                usbLowSpeedDevice = usbCurrentDevice;
            }
        }

        assertEquals("The Low Speed device should be low speed!",
                     UsbConst.DEVICE_SPEED_LOW, usbLowSpeedDevice.getSpeed());

        assertEquals("The Vendor ID does't match the programable device Vendor ID",
                     programableVendorID, usbProgramableDevDescriptor.idVendor());       
        assertEquals("The Product ID doesn't match the programable device Product ID",
                     programableProductID, usbProgramableDevDescriptor.idProduct());
        assertEquals("The programable device should be full speed!", 
                     UsbConst.DEVICE_SPEED_FULL, usbProgramableDevice.getSpeed());
    }
}
