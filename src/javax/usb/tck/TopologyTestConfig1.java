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
 * Topology Test Configuration 1 - Tests Topology Hardware Configuration 1
 * <p>
 * This test verifies that the API can traverse a hierarchical device topology
 * and that it properly reveals the correct pre-configured topology as defined
 * by the  test hardware setup.  For the UsbDevice methods (and UsbConfig,
 * etc. down to the UsbEndpoint) only the Cypress development board is
 * used, other devices are present only to verify the accuracy of the topology
 * tree.
 * @author Joshua Lowry
 */

public class TopologyTestConfig1 extends TestCase implements TopologyTests
{

    public TopologyTestConfig1(String name)
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
     * Tests Hardware Configuration 1 from the Javax.usb TCK
     * specification.
     * @throws Exception
     */
    public void testTopologyConfigOne() throws Exception {
        UsbServices usbUsbServices = null;
        UsbHub usbVirtualRootUsbHub = null;
        UsbDevice usbProgramableDevice = null;
        UsbDeviceDescriptor usbProgramableDevDescriptor;
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

            usbCurrentDevice = (UsbDevice) usbListUsbDevices.get(i);
            assertTrue("All devices connected to the virtual root UsbHub should be UsbHubs!",
                       usbCurrentDevice.isUsbHub());

            usbCurrentHub = (UsbHub) usbListUsbDevices.get(i);
            usbNextList = usbCurrentHub.getAttachedUsbDevices();

            if ( usbNextList.size() > 0 )
            {
                numHubsWithDevices++;
                assertEquals("There is more than one device connected to the root hub!",
                             (int) 1, usbNextList.size());
                usbProgramableDevice = (UsbDevice) usbNextList.get(0);
                assertFalse("The connected device should not be a UsbHub!",
                            usbProgramableDevice.isUsbHub());
            }
        }

        assertEquals("Only one root UsbHub should have a device connected in this configuration!",
                     (int) 1, numHubsWithDevices);

        usbProgramableDevDescriptor = usbProgramableDevice.getUsbDeviceDescriptor();

        assertEquals("The Vendor ID does't match the programable device Vendor ID",
                     programableVendorID, usbProgramableDevDescriptor.idVendor());       
        assertEquals("The Product ID doesn't match the programable device Product ID",
                     programableProductID, usbProgramableDevDescriptor.idProduct());
        assertEquals("The programable device should be full speed!", 
                     UsbConst.DEVICE_SPEED_FULL, usbProgramableDevice.getSpeed());
    }
}