package javax.usb.tck;

/**
 * Copyright (c) 2004, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.io.*;
import java.util.*;

import junit.framework.*;

import javax.usb.*;
import javax.usb.util.*;

/**
 * Topology Test Configuration 3 - Tests Topology Hardware Configuration 3
 * <p>
 * This test verifies that the API can traverse a hierarchical device topology
 * and that it properly reveals the correct pre-configured topology as defined
 * by the  test hardware setup.  For the UsbDevice methods (and UsbConfig,
 * etc. down to the UsbEndpoint) only the Cypress development board is
 * used, other devices are present only to verify the accuracy of the topology
 * tree.
 * @author Joshua Lowry
 */

public class TopologyTestConfig3 extends TestCase implements TopologyTests
{

    public TopologyTestConfig3(String name)
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
     * Tests Hardware Configuration 3 from the Javax.usb TCK
     * specification.
     * @throws Exception
     */
    public void testTopologyConfigThree() throws Exception {
        StandardRequest usbProgramableRequests;
        StandardRequest usbNonDfltCtrlPipeRequests;
        UsbServices usbUsbServices = null;
        UsbHub usbVirtualRootUsbHub = null;
        UsbHub usbRootHubOne = null;
        UsbHub usbRootHubOneHub = null;
        UsbHub usbRootHubTwoHub = null;
        UsbPort usbProgramableParentPort;
        UsbDevice usbProgramableDevice = null;
        UsbDevice usbLowSpeedDevice = null;
        UsbDevice usbNonDfltCtrlPipeDevice;
        UsbDeviceDescriptor usbProgramableDevDescriptor = null;
        Properties usbUsbProperties;
        List usbListUsbDevices;
        List usbListUsbConfigs;
        boolean bFoundNonDfltCtrlPipe;

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
            UsbDevice usbNextDevice1;
            UsbDevice usbNextDevice2;
            UsbHub usbCurrentHub;
            List usbNextList;

            usbCurrentDevice = (UsbDevice) usbListUsbDevices.get(i);
            assertTrue("All devices connected to the virtual root UsbHub should be UsbHubs!",
                       usbCurrentDevice.isUsbHub());

            usbCurrentHub = (UsbHub) usbListUsbDevices.get(i);
            usbNextList = usbCurrentHub.getAttachedUsbDevices();

            switch ( usbNextList.size() )
            {
            case 0: 
                break;
            case 1:
                numHubsWithDevices++;
                usbNextDevice1 = (UsbDevice) usbNextList.get(0);
                assertTrue("The only device attached to this root UsbHub should be a UsbHub!",
                           usbNextDevice1.isUsbHub());
                usbRootHubTwoHub = (UsbHub) usbNextList.get(0);
                assertFalse("This hub should not be a root UsbHub!",
                            usbRootHubTwoHub.isRootUsbHub());
                break;
            case 2:
                numHubsWithDevices++;
                usbRootHubOne = usbCurrentHub;
                usbNextDevice1 = (UsbDevice) usbNextList.get(0);
                usbNextDevice2 = (UsbDevice) usbNextList.get(1);
                assertTrue("The devices attached to this root UsbHub should not both be UsbHubs or UsbDevices!",
                           usbNextDevice1.isUsbHub() != usbNextDevice2.isUsbHub());
                if ( usbNextDevice1.isUsbHub() )
                {
                    usbProgramableDevice = usbNextDevice2;
                    usbRootHubOneHub = (UsbHub) usbNextList.get(0);
                }
                else
                {
                    usbProgramableDevice = usbNextDevice1;
                    usbRootHubOneHub = (UsbHub) usbNextList.get(1);
                }
                assertFalse("This hub should not be a root UsbHub!",
                            usbRootHubOneHub.isRootUsbHub());
                usbProgramableParentPort = usbCurrentHub.getUsbPort(usbProgramableDevice.getParentUsbPort().getPortNumber());
                break;
            default:
                fail("There are too many devices attached to this root UsbHub!");
                break;
            }
        }

        assertEquals("Only two root UsbHub should have devices connected in this configuration!",
                     (int) 2, numHubsWithDevices);

        usbListUsbDevices = usbRootHubOneHub.getAttachedUsbDevices();

        for ( int i = 0; i < 3; i++ )
        {
            UsbDevice usbCurrentDevice;
            UsbHub usbCurrentHub;

            assertEquals("Only one device should be attached to this UsbHub!",
                         (int) 1, usbListUsbDevices.size());
            usbCurrentDevice = (UsbDevice) usbListUsbDevices.get(0);
            assertTrue("Only a hub should be attached to this UsbHub!",
                       usbCurrentDevice.isUsbHub());
            usbCurrentHub = (UsbHub) usbListUsbDevices.get(0);
            assertFalse("This hub should not be a root UsbHub!",
                        usbCurrentHub.isRootUsbHub());
            if ( i == 2 )
            {
                List usbUsbPorts;
                UsbHub usbAttachedHub;
                UsbDevice usbAttachedHubDevice;
                UsbPort usbUsbPort3;

                assertEquals("This hub should have exactly four ports!", (byte) 4,
                             usbCurrentHub.getNumberOfPorts());
                usbUsbPorts = usbCurrentHub.getUsbPorts();
                assertEquals("The getNumberOfPorts should return a list of four ports!",
                             (int) 4, usbUsbPorts.size());
                usbUsbPort3 = usbCurrentHub.getUsbPort((byte) 3);
                assertEquals("This UsbPort should be port #3!", (byte) 3,
                             usbUsbPort3.getPortNumber());
                assertTrue("Port 3 of this UsbHub should have an attached device!",
                           usbUsbPort3.isUsbDeviceAttached());
                usbLowSpeedDevice = usbUsbPort3.getUsbDevice();
                assertNotNull("There getUsbDevcie method should not have returned null for UsbPort #3!",
                              usbLowSpeedDevice);
                usbAttachedHub = usbUsbPort3.getUsbHub();
                usbAttachedHubDevice = usbAttachedHub.getUsbPort((byte) 3).getUsbDevice();
                assertFalse("The UsbHub UsbPort #3 is attached to should not have a downstream hub!",
                            usbAttachedHubDevice.isUsbHub());

                for ( byte j = 0; j < 4; j++ )
                {
                    UsbPort usbCurrentPort;

                    usbCurrentPort = (UsbPort) usbUsbPorts.get(j);
                    assertEquals("The UsbPort # doesn't match the expected port #!",
                                 (byte) (j+1), usbCurrentPort.getPortNumber());
                    usbAttachedHub = usbCurrentPort.getUsbHub();
                    usbAttachedHubDevice = usbAttachedHub.getUsbPort((byte) 3).getUsbDevice();
                    assertFalse("The UsbHub this UsbPort is attached to should not have a downstream hub!",
                                usbAttachedHubDevice.isUsbHub());

                    if ( j+1 != 3 )
                    {
                        assertFalse("There should not be any devices attached to this port!",
                                    usbCurrentPort.isUsbDeviceAttached());
                        assertNull("The getUsbDevice method should have returned null!",
                                   usbCurrentPort.getUsbDevice());
                    }
                    else
                    {
                        assertTrue("There should have been a device attached to port 3!",
                                   usbCurrentPort.isUsbDeviceAttached());
                        assertNotNull("The getUsbDevice method should not have returned null!",
                                      usbCurrentPort.getUsbDevice());
                    }
                }
            }
            usbListUsbDevices = usbCurrentHub.getAttachedUsbDevices();
        }

        usbListUsbDevices = usbRootHubTwoHub.getAttachedUsbDevices();
        assertEquals("Only one device should be attached to this UsbHub!",
                     (int) 1, usbListUsbDevices.size());
        usbNonDfltCtrlPipeDevice = (UsbDevice) usbListUsbDevices.get(0);
        assertFalse("Only a device should be attached to this UsbHub!",
                    usbNonDfltCtrlPipeDevice.isUsbHub());
        usbNonDfltCtrlPipeRequests = new StandardRequest(usbNonDfltCtrlPipeDevice);

        usbListUsbConfigs = usbNonDfltCtrlPipeDevice.getUsbConfigurations();
        bFoundNonDfltCtrlPipe = false;
        for ( int i = 0; i < usbListUsbConfigs.size(); i++ )
        {
            UsbConfiguration usbCurrentConfig;
            UsbConfigurationDescriptor usbCurrentConfigDescriptor;
            List usbInterfacesList;

            usbCurrentConfig = (UsbConfiguration) usbListUsbConfigs.get(i);
            usbCurrentConfigDescriptor = usbCurrentConfig.getUsbConfigurationDescriptor();
            if ( !usbCurrentConfig.isActive() )
                usbNonDfltCtrlPipeRequests.setConfiguration(usbCurrentConfigDescriptor.bConfigurationValue());
            usbInterfacesList = usbCurrentConfig.getUsbInterfaces();
            for ( int j = 0; j < usbInterfacesList.size(); j++ )
            {
                UsbInterface usbCurrentInterface;
                List usbSettings;

                usbCurrentInterface = (UsbInterface) usbInterfacesList.get(j);
                usbSettings = usbCurrentInterface.getSettings();
                for ( int k = 0; k < usbCurrentInterface.getNumSettings(); k++ )
                {
                    List usbEndpointsList;

                    usbCurrentInterface = usbCurrentInterface.getSetting((byte) k);
                    assertTrue("The UsbInteface getSettings method contains Setting #" + k + " for Config #" + i + " Interface #" + j,
                               usbSettings.contains((Object) usbCurrentInterface));
                    usbEndpointsList = usbCurrentInterface.getUsbEndpoints();
                    for ( int l = 0; l < usbEndpointsList.size(); l++ )
                    {
                        UsbEndpoint CurrentEndpoint;
                        UsbEndpointDescriptor CurrentEndpointDescriptor;

                        CurrentEndpoint = (UsbEndpoint) usbEndpointsList.get(l);
                        CurrentEndpointDescriptor = CurrentEndpoint.getUsbEndpointDescriptor();
                        if ( CurrentEndpointDescriptor.bEndpointAddress() != 0 &&
                             CurrentEndpoint.getType() == UsbConst.ENDPOINT_TYPE_CONTROL )
                        {
                            bFoundNonDfltCtrlPipe = true;
                            break;
                        }
                    }
                    if ( bFoundNonDfltCtrlPipe )
                        break;
                }
                if ( bFoundNonDfltCtrlPipe )
                    break;
            }
            if ( bFoundNonDfltCtrlPipe )
                break;
        }

        assertTrue("The non-default control pipe device doesn't have a non-default control pipe!",
                   bFoundNonDfltCtrlPipe);

        assertEquals("The low speed device is not low speed!",
                     UsbConst.DEVICE_SPEED_LOW, usbLowSpeedDevice.getSpeed());

        /*
         * Assert that the programable device's parent port leads back to the programable device
         * as well as its parent Hub
         */
        usbProgramableParentPort = usbProgramableDevice.getParentUsbPort();
        assertTrue("The programable device's parent port should have an attached device!",
                   usbProgramableParentPort.isUsbDeviceAttached());
        assertNotNull("The programable device's parent port getUsbDevice method shouln't return null!",
                      usbProgramableParentPort.getUsbDevice());
        assertFalse("The programable device's parent port getUsbDevice should not be a UsbHub!",
                    usbProgramableParentPort.getUsbDevice().isUsbHub());
        assertEquals("The programable device's parent port getUsbDevice should have the programable device VendorID!",
                     programableVendorID,
                     usbProgramableParentPort.getUsbDevice().getUsbDeviceDescriptor().idVendor());
        assertEquals("The programable device's parent port getUsbDevice should have the programable device ProductID!",
                     programableProductID,
                     usbProgramableParentPort.getUsbDevice().getUsbDeviceDescriptor().idProduct());
        assertTrue("The programable device's parent port # should get the root hub to an attached device!",
                   usbRootHubOne.getUsbPort(usbProgramableParentPort.getPortNumber()).isUsbDeviceAttached());
        assertNotNull("The programable device's parent port # should let the root hub get an attached device!",
                      usbRootHubOne.getUsbPort(usbProgramableParentPort.getPortNumber()).getUsbDevice());
        assertEquals("The programable device's parent port # should get the root hub to the devices VendorID!",
                     programableVendorID,
                     usbRootHubOne.getUsbPort(usbProgramableParentPort.getPortNumber()).getUsbDevice().getUsbDeviceDescriptor().idVendor());
        assertEquals("The programable device's parent port # should get the root hub to the devices ProductID!",
                     programableProductID,
                     usbRootHubOne.getUsbPort(usbProgramableParentPort.getPortNumber()).getUsbDevice().getUsbDeviceDescriptor().idProduct());

        /*
         * Assert that the UsbDevice methods match with the programable device
         */     
        assertFalse("The programable device should not be a UsbHub",
                    usbProgramableDevice.isUsbHub());
        try
        {
            assertEquals("The Manufacturer string doesn't match the programable device Manufacturer string",
                         programableStrings[0], usbProgramableDevice.getManufacturerString());
            assertEquals("The Product string doesn't match the programable device Product string",
                         programableStrings[1], usbProgramableDevice.getProductString());
            assertEquals("The Serial # string doesn't match the programable device Serial # string",
                         programableStrings[2], usbProgramableDevice.getSerialNumberString());
        }
        catch ( UnsupportedEncodingException uEE )
        {
            fail ("The String encoding is not supported!");
        }
        assertEquals("The device speed doesn't match the programable device device speed",
                     UsbConst.DEVICE_SPEED_FULL, usbProgramableDevice.getSpeed());
        assertTrue("The active configuration should be active!",
                   usbProgramableDevice.getActiveUsbConfiguration().isActive());
        assertTrue("The active configuration # should lead to the active configuration!",
                   usbProgramableDevice.getUsbConfiguration(usbProgramableDevice.getActiveUsbConfigurationNumber()).isActive());
        for ( byte i = 1; i < (programableNumConfigurations + 1); i++ )
        {
            assertTrue("Configuration " + i + "should exist on the programable device!",
                       usbProgramableDevice.containsUsbConfiguration(i));
        }
        assertTrue("The programable device should be configured",
                   usbProgramableDevice.isConfigured());

        /*
         * Assert that the UsbStringDescriptor methodsreturn all the correct srings
         */
        for ( int i = 0; i < programableStrings.length; i++ )
        {
            UsbStringDescriptor usbCurrentStringDesc=null;
            String bytesToString;

            try
            {
                usbCurrentStringDesc = usbProgramableDevice.getUsbStringDescriptor((byte) (i+1));
            }
            catch ( UsbException uE )
            {
                fail("Error occured while getting the UsbStringDescriptor!");
            }

            bytesToString = new String(usbCurrentStringDesc.bString(), "UTF-16LE");
            assertEquals("The byte string at index " + (i+1) + " doesn't match the programable device's at that index",
                         programableStrings[i], bytesToString);
            try
            {
                assertEquals("The string at index " + (i+1) + "doesn't match the programable device's at that index",
                             programableStrings[i], usbCurrentStringDesc.getString());
                assertEquals("The string at index " + (i+1) + "doesn't match the programable device's at that index",
                             programableStrings[i], usbProgramableDevice.getString((byte) (i+1)));
            }
            catch ( UnsupportedEncodingException uEE )
            {
                fail ("The String encoding is not supported!");
            }
        }

        usbProgramableDevDescriptor = usbProgramableDevice.getUsbDeviceDescriptor();
        /*
         * Assert that the UsbDeviceDescriptor methods match with the programable device
         */
        assertEquals("The Device Descriptor Length doesn't match the programable device Length",
                     programableLength, usbProgramableDevDescriptor.bLength());
        assertEquals("The Device Descriptor Type doesn't match the programable device Descriptor Type",
                     UsbConst.DESCRIPTOR_TYPE_DEVICE, usbProgramableDevDescriptor.bDescriptorType());
        assertEquals("The USB spec version doesn't match the programable device USB spec version",
                     programableBcdUsb, usbProgramableDevDescriptor.bcdUSB());
        assertEquals("The Device Class doesn't match the programable device Device Class",
                     programableDeviceClass, usbProgramableDevDescriptor.bDeviceClass());
        assertEquals("The Device Subclass doesn't match the programable device Device Subclass",
                     programableDeviceSubClass, usbProgramableDevDescriptor.bDeviceSubClass());
        assertEquals("The Device Protocol doesn't match the programable device Device Protocol",
                     programableDeviceProtocol, usbProgramableDevDescriptor.bDeviceProtocol());
        assertEquals("The Max Packet Size 0 doesn't match the programable device Max Packet Size 0",
                     programableMaxPacketSize, usbProgramableDevDescriptor.bMaxPacketSize0());
        assertEquals("The Vendor ID does't match the programable device Vendor ID",
                     programableVendorID, usbProgramableDevDescriptor.idVendor());       
        assertEquals("The Product ID doesn't match the programable device Product ID",
                     programableProductID, usbProgramableDevDescriptor.idProduct());
        assertEquals("The Device Release doen't match the programable device Device Release",
                     programableBcdDevice, usbProgramableDevDescriptor.bcdDevice());
        assertEquals("The Manufacturer Index doesn't match the programable device Manufacturer Index",
                     programableManufacturerIndex, usbProgramableDevDescriptor.iManufacturer());
        assertEquals("The Product Index doesn't match the programable device Product Index",
                     programableProductIndex, usbProgramableDevDescriptor.iProduct());
        assertEquals("The Serial # Index doesn't match the programable device Serial # Index",
                     programableSerialNumIndex, usbProgramableDevDescriptor.iSerialNumber());
        assertEquals("The # Configurations doesn't match the programable device # Configurations",
                     programableNumConfigurations, usbProgramableDevDescriptor.bNumConfigurations());
        assertFalse("Configuration #" + (programableNumConfigurations+2) + " should not exist",
                    usbProgramableDevice.containsUsbConfiguration((byte) (programableNumConfigurations + 2)));
        assertNull("The UsbDevice.getUsbConfiguration(" + (programableNumConfigurations+2) + "should be Null",
                   usbProgramableDevice.getUsbConfiguration((byte) (programableNumConfigurations + 2)));

        /*
         * Assert that the Configurations and ConfigurationDescriptors match the
         * expected values from the programmable device
         */
        usbProgramableRequests = new StandardRequest(usbProgramableDevice);
        usbListUsbConfigs = usbProgramableDevice.getUsbConfigurations();
        assertEquals("The # Configs from getUsbConfigurations doesn't match the programable device",
                     programableNumConfigurations, usbListUsbConfigs.size());
        for ( int i = 0; i < usbListUsbConfigs.size(); i++ )
        {
            UsbConfiguration usbCurrentConfig = null;
            UsbConfigurationDescriptor usbCurrentConfigDescriptor;
            List usbListUsbInterfaces;

            for ( byte j = 0; j < usbListUsbConfigs.size(); j++ )
            {
                usbCurrentConfig = (UsbConfiguration) usbListUsbConfigs.get(j);
                if ( usbCurrentConfig.getUsbConfigurationDescriptor().bConfigurationValue() == (byte) (i+1) )
                    break;
            }
            usbCurrentConfigDescriptor = usbCurrentConfig.getUsbConfigurationDescriptor();
            if ( !usbCurrentConfig.isActive() )
            {
                UsbInterface usbInactiveInterface;

                usbInactiveInterface = usbCurrentConfig.getUsbInterface((byte) 0);
                assertNotNull("An interface should have been returned by the inactive UsbConfiguration.getUsbInterface",
                              usbInactiveInterface);
                assertFalse("The interface received from the inactive configuration should not be active",
                            usbInactiveInterface.isActive());
                try
                {
                    usbInactiveInterface.getActiveSettingNumber();
                    fail("A UsbNotActiveException did not occur when calling getActiveSettingNumber on an inactive configuration");
                }
                catch ( UsbNotActiveException uNAE )
                {
                    assertNotNull("A UsbNotActiveException should not be null",
                                  uNAE);
                }
                try
                {
                    usbInactiveInterface.getActiveSetting();
                    fail("A UsbNotActiveException did not occur when calling getActiveSetting on an inactive configuration");
                }
                catch ( UsbNotActiveException uNAE )
                {
                    assertNotNull("A UsbNotActiveException should not be null",
                                  uNAE);
                }
                try
                {
                    usbProgramableRequests.setConfiguration(usbCurrentConfigDescriptor.bConfigurationValue());
                }
                catch ( UsbException uE )
                {
                    fail("Unable to set the UsbConfiguration");
                }
            }

            assertTrue("Config #" + (i+1) + " should now be the active configuration",
                       usbCurrentConfig.isActive());
            assertEquals("The active configuration # should now be " + (i+1),
                         (byte) (i+1), usbProgramableDevice.getActiveUsbConfigurationNumber());
            assertEquals("The active configuration should be the current config",
                         usbCurrentConfigDescriptor.bConfigurationValue(),
                         usbProgramableDevice.getActiveUsbConfiguration().getUsbConfigurationDescriptor().bConfigurationValue());

            try
            {
                assertEquals("The Configuration String doesn't match the programable device string for Config #" +(i+1),
                             programableStrings[i+3], usbCurrentConfig.getConfigurationString());
            }
            catch ( UnsupportedEncodingException uEE )
            {
                fail ("The String encoding is not supported!");
            }
            assertEquals("The Configuration UsbDevice method doesn't lead back to the programable device",
                         programableVendorID,
                         usbCurrentConfig.getUsbDevice().getUsbDeviceDescriptor().idVendor());
            assertEquals("The Configuration UsbDevice method doesn't lead back to the programable device",
                         programableProductID,
                         usbCurrentConfig.getUsbDevice().getUsbDeviceDescriptor().idProduct());
            assertTrue("Config #" + (i+1) + " should be the Active Configuration!",
                       usbCurrentConfig.isActive());
            assertEquals("The Config length field doesn't match with programable device Config #" + (i+1),
                         (byte) 0x09, usbCurrentConfigDescriptor.bLength());
            assertEquals("The Config descriptor type field is not set to Configuration",
                         UsbConst.DESCRIPTOR_TYPE_CONFIGURATION,
                         usbCurrentConfigDescriptor.bDescriptorType());
            assertEquals("The Config value doesn't match with the programable device Config #" + (i+1),
                         programableConfigValue[i], usbCurrentConfigDescriptor.bConfigurationValue());
            assertEquals("The # Interfaces doesn't match with the programable device Config #" + (i+1),
                         programableNumInterfaces[i], usbCurrentConfigDescriptor.bNumInterfaces());
            assertEquals("The Config index doesn't match with the programable device Config #" + (i+1),
                         programableConfigIndex[i], usbCurrentConfigDescriptor.iConfiguration());
            assertEquals("The Config attributes doesn't match with the programable device Config #" + (i+1),
                         ((byte) 0x80 | UsbConst.CONFIGURATION_REMOTE_WAKEUP),
                         usbCurrentConfigDescriptor.bmAttributes());
            assertEquals("The Config MaxPower doesn't match with the programable device Config #" + (i+1),
                         (byte) 0x32, usbCurrentConfigDescriptor.bMaxPower());
            assertFalse("Interface #" + (programableNumInterfaces[i]+1) + " should not exist for Config #" + (i+1),
                        usbCurrentConfig.containsUsbInterface((byte) (programableNumInterfaces[i]+1)));
            assertNull("The getUsbInterface(" + (programableNumInterfaces[i]+1) + ") should be Null for Config #" + (i+1),
                       usbCurrentConfig.getUsbInterface((byte) (programableNumInterfaces[i]+1)));

            /*
             * Assert that the Interfaces and InterfaceDescriptors match the
             * expected values from the programmable device for each
             * Configuration
             */
            usbListUsbInterfaces = usbCurrentConfig.getUsbInterfaces();
            for ( int j = 0; j < usbListUsbInterfaces.size(); j++ )
            {
                UsbInterface usbCurrentInterface = null;

                for ( int k = 0; k < usbListUsbInterfaces.size(); k++ )
                {
                    usbCurrentInterface = (UsbInterface) usbListUsbInterfaces.get(k);
                    if ( usbCurrentInterface.getUsbInterfaceDescriptor().bInterfaceNumber() == j )
                        break;
                }

                assertTrue("Config #" + (i+1) + " should contain this interface!",
                           usbCurrentConfig.containsUsbInterface((byte) j));
                try
                {
                    assertEquals("The UsbInterface from getUsbInterfaces should be the same as getUsbInterface",
                                 usbCurrentInterface.getInterfaceString(),
                                 usbCurrentConfig.getUsbInterface((byte) j).getInterfaceString());
                    assertEquals("The UsbConfiguration from getUsbConfiguration should be the same as the current configuration",
                                 usbCurrentConfig.getConfigurationString(),
                                 usbCurrentInterface.getUsbConfiguration().getConfigurationString());
                }
                catch ( UnsupportedEncodingException uEE )
                {
                    fail ("The String encoding is not supported!");
                }
                assertEquals("The number of settings for Config #" + (i+1) + " Interface #" +j+ " should match the programable device",
                             programableNumSettings[i][j], usbCurrentInterface.getNumSettings());
                assertFalse("Setting #" + (programableNumSettings[i][j]+1) + " should not exist for Config #" + (i+1) + " Interface #" +j,
                            usbCurrentInterface.containsSetting((byte) (programableNumSettings[i][j]+1)));
                assertNull("The getSetting(" + (programableNumSettings[i][j]+1) + ") should be Null for Config #" + (i+1) + " Interface #" +j,
                           usbCurrentInterface.getSetting((byte) (programableNumSettings[i][j]+1)));
                try
                {
                    assertEquals("The Default Active Setting # for Config #" + (i+1) + " Interface #" +j+ "should be 0",
                                 (byte) 0x00, usbCurrentInterface.getActiveSettingNumber());
                    assertEquals("The Default Active Setting is the setting received by getUsbInterfaces",
                                 usbCurrentInterface.getInterfaceString(),
                                 usbCurrentInterface.getActiveSetting().getInterfaceString());
                }
                catch ( UsbNotActiveException uNAE )
                {
                    fail("Config #" + (i+1) + " Interface #" + j +" is not active!");
                }
                catch ( UnsupportedEncodingException uEE )
                {
                    fail ("The String encoding is not supported!");
                }
                assertEquals("The current Interface # for Config #" + (i+1) + " should  be " +j,
                             (byte) j, usbCurrentInterface.getUsbInterfaceDescriptor().bInterfaceNumber());
                assertEquals("The Setting # for Config #" + (i+1) + " Interface #" +j+ "should be 0",
                             (byte) 0x00, usbCurrentInterface.getUsbInterfaceDescriptor().bAlternateSetting());

                /*
                 * Assert that the Alternate Settings of the Interfaces and 
                 * InterfaceDescriptors match the expected values from 
                 * the programmable device for each Configuration and
                 * Interface
                 */
                for ( int k = 0; k < usbCurrentInterface.getNumSettings(); k++ )
                {
                    UsbInterfaceDescriptor usbCurrentInterfaceDescriptor;           
                    List usbListUsbEndpoints;

                    assertTrue("Config #" + (i+1) + " Interface #" +j+ " should contain AlternateSetting #" +k,
                               usbCurrentInterface.containsSetting((byte) k));

                    usbCurrentInterface = usbCurrentInterface.getSetting((byte) k);
                    usbCurrentInterfaceDescriptor = usbCurrentInterface.getUsbInterfaceDescriptor();

                    try
                    {
                        assertEquals("Config #" + (i+1) + " Interface #" +j+ " AlternateSetting #" +k+ " InterfaceString doesn't match",
                                     programableInterfaceStrings[i][j][k],
                                     usbCurrentInterface.getInterfaceString());

                    }
                    catch ( UnsupportedEncodingException uEE )
                    {
                        fail ("The String encoding is not supported!");
                    }
                    if ( k == 0 )
                        assertTrue("Config #" + (i+1) + " Interface #" +j+ " AlternateSetting #" +k+ " should be active",
                                   usbCurrentInterface.isActive());
                    else
                        assertFalse("Config #" + (i+1) + " Interface #" +j+ " AlternateSetting #" +k+ " should not be active",
                                    usbCurrentInterface.isActive());
                    assertEquals("The current Interface # for Config #" + (i+1) + " should  not change",
                                 (byte) j, usbCurrentInterfaceDescriptor.bInterfaceNumber());
                    assertEquals("The current setting # for Config #" + (i+1) + " Interface #" +j+ " should be " + k,
                                 (byte) k, usbCurrentInterface.getUsbInterfaceDescriptor().bAlternateSetting());
                    assertEquals("The # endpoints for Config #" + (i+1) + " Interface #" +j+ " Alternate Setting #" + k + "doesn't match",
                                 programableNumEndpoints[i][j][k], usbCurrentInterfaceDescriptor.bNumEndpoints());
                    assertEquals("The Interface Class for Config #" + (i+1) + " Interface #" +j+ " Alternate Setting #" + k + "doesn't match",
                                 (byte) 0xFF, usbCurrentInterfaceDescriptor.bInterfaceClass());
                    assertEquals("The Interface SubClass for Config #" + (i+1) + " Interface #" +j+ " Alternate Setting #" + k + "doesn't match",
                                 (byte) 0x00, usbCurrentInterfaceDescriptor.bInterfaceSubClass());
                    assertEquals("The Interface Protocol for Config #" + (i+1) + " Interface #" +j+ " Alternate Setting #" + k + "doesn't match",
                                 (byte) 0x00, usbCurrentInterfaceDescriptor.bInterfaceProtocol());
                    assertEquals("The current interface index for Config #" + (i+1) + " Interface #" +j+ " Alternate Setting #" + k + "doesn't match",
                                 programableInterfaceIndex[i][j][k], usbCurrentInterfaceDescriptor.iInterface());
                    assertFalse("UsbEndpoint 0x7F should not exist for Config #" + (i+1) + " Interface #" +j+ " Alternate Setting #" + k,
                                usbCurrentInterface.containsUsbEndpoint((byte) 0x7F));
                    assertNull("UsbEndpoint 0x7F should be Null for Config #" + (i+1) + " Interface #" +j+ " Alternate Setting #" + k,
                               usbCurrentInterface.getUsbEndpoint((byte) 0x7F));

                    /*
                     * Assert that the Endpoints and Pipes of each Interfaces' settings
                     * match the expected values from the programmable device for
                     * each Configuration, Interface and Alternate Setting
                     */
                    usbListUsbEndpoints = usbCurrentInterface.getUsbEndpoints();
                    for ( int l = 0; l < usbListUsbEndpoints.size(); l++ )
                    {
                        UsbEndpoint usbCurrentEndpoint;
                        UsbEndpointDescriptor usbCurrentEndpointDescriptor;
                        UsbPipe usbCurrentPipe;

                        usbCurrentEndpoint = (UsbEndpoint) usbListUsbEndpoints.get(l);
                        usbCurrentEndpointDescriptor = usbCurrentEndpoint.getUsbEndpointDescriptor();
                        usbCurrentPipe = usbCurrentEndpoint.getUsbPipe();

                        assertEquals("The Endpoint Address isn't " + programableEndpointAddr[i][j][k][l] + " for Config #" + (i+1) + " Interface #" +j+ " AlternateSetting #" +k,
                                     programableEndpointAddr[i][j][k][l], usbCurrentEndpointDescriptor.bEndpointAddress());
                        assertTrue("Config #" + (i+1) + " Interface #" +j+ " AlternateSetting #" +k+ " Should contain Endpoint Address " + programableEndpointAddr[i][j][k][l],
                                   usbCurrentInterface.containsUsbEndpoint(programableEndpointAddr[i][j][k][l]));
                        assertEquals("The UsbEndpoint address from getUsbEndpoints should be the same as getUsbEndpoint",
                                     usbCurrentEndpointDescriptor.bEndpointAddress(),
                                     usbCurrentInterface.getUsbEndpoint(programableEndpointAddr[i][j][k][l]).getUsbEndpointDescriptor().bEndpointAddress());
                        assertEquals("The UsbEndpoint type from getUsbEndpoints should be the same as getUsbEndpoint",
                                     usbCurrentEndpointDescriptor.bDescriptorType(),
                                     usbCurrentInterface.getUsbEndpoint(programableEndpointAddr[i][j][k][l]).getUsbEndpointDescriptor().bDescriptorType());
                        assertEquals("The UsbEndpoint address from getUsbEndpoints should be the same as getUsbEndpoint",
                                     usbCurrentEndpointDescriptor.bEndpointAddress(),
                                     usbCurrentPipe.getUsbEndpoint().getUsbEndpointDescriptor().bEndpointAddress());
                        assertEquals("The UsbEndpoint type from getUsbEndpoints should be the same as getUsbEndpoint",
                                     usbCurrentEndpointDescriptor.bDescriptorType(),
                                     usbCurrentPipe.getUsbEndpoint().getUsbEndpointDescriptor().bDescriptorType());
                        try
                        {
                            assertEquals("The UsbConfiguration from getUsbInterface should be the same as the current interface",
                                         usbCurrentInterface.getInterfaceString(),
                                         usbCurrentEndpoint.getUsbInterface().getInterfaceString());
                        }
                        catch ( UnsupportedEncodingException uEE )
                        {
                            fail ("The String encoding is not supported!");
                        }
                        byte address = programableEndpointAddr[i][j][k][l];
                        byte direction = (byte) (address / (byte) 0x70);
                        if ( programableEndpointAddr[i][j][k][l] / (byte) 0x70 == -1 )
                            assertEquals("The Endpoint Direction isn't correct for Config #" + (i+1) + " Interface #" +j+ " AlternateSetting #" +k+ " Endpoint Address " + programableEndpointAddr[i][j][k][l],
                                         UsbConst.ENDPOINT_DIRECTION_IN,
                                         usbCurrentEndpoint.getDirection());
                        else
                            assertEquals("The Endpoint Direction isn't correct for Config #" + (i+1) + " Interface #" +j+ " AlternateSetting #" +k+ " Endpoint Address " + programableEndpointAddr[i][j][k][l],
                                         UsbConst.ENDPOINT_DIRECTION_OUT,
                                         usbCurrentEndpoint.getDirection());
                        assertEquals("The Endpoint Type isn't correct for Config #" + (i+1) + " Interface #" +j+ " AlternateSetting #" +k+ " Endpoint Address " + programableEndpointAddr[i][j][k][l],
                                     programableEndpointType[i][j][k][l], usbCurrentEndpoint.getType());
                        assertEquals("The Endpoint Attribute isn't correct for Config #" + (i+1) + " Interface #" +j+ " AlternateSetting #" +k+ " Endpoint Address " + programableEndpointAddr[i][j][k][l],
                                     programableEndpointType[i][j][k][l], ((byte) 0x03 & usbCurrentEndpointDescriptor.bmAttributes()));
                        assertEquals("The Endpoint Max Packet Size isn't correct for Config #" + (i+1) + " Interface #" +j+ " AlternateSetting #" +k+ " Endpoint Address " + programableEndpointAddr[i][j][k][l],
                                     programableEndpointMPS[i][j][k][l], usbCurrentEndpointDescriptor.wMaxPacketSize());
                        assertEquals("The Endpoint Interval isn't correct for Config #" + (i+1) + " Interface #" +j+ " AlternateSetting #" +k+ " Endpoint Address " + programableEndpointAddr[i][j][k][l],
                                     programableEndpointInter[i][j][k][l], usbCurrentEndpointDescriptor.bInterval());
                        assertEquals("The Endpoint Descriptor type isn't correct for Config #" + (i+1) + " Interface #" +j+ " AlternateSetting #" +k+ " Endpoint Address " + programableEndpointAddr[i][j][k][l],
                                     (byte) 0x05, usbCurrentEndpointDescriptor.bDescriptorType());
                        assertEquals("The Endpoint length isn't correct for Config #" + (i+1) + " Interface #" +j+ " AlternateSetting #" +k+ " Endpoint Address " + programableEndpointAddr[i][j][k][l],
                                     (byte) 0x07, usbCurrentEndpointDescriptor.bLength());
                    }
                }
            }
        }
    }
}