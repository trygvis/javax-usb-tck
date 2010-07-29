package javax.usb.tck.topology;

import static javax.usb.tck.TckUtils.prefixDeviceId;
import static junit.framework.Assert.*;

import javax.usb.*;
import java.util.*;

public class UsbHubTckDescriptor extends UsbDeviceTckDescriptor {
    boolean rootUsbHub;

    List<UsbDeviceTckDescriptor> expectedDevices;

    public void doAsserts(UsbDevice usbDevice) {
        usbHub = true;

        super.doAsserts(usbDevice);

        UsbHub usbHub = (UsbHub) usbDevice;

        assertEquals(prefixDeviceId(usbDevice, "rootUsbHub"), rootUsbHub, usbHub.isRootUsbHub());

        if(expectedDevices == null) {
            // No checks configured
            return;
        }

        List<UsbDevice> actualDevices = usbHub.getAttachedUsbDevices();

        boolean requireAllDevicesConfigured = true;

        Iterator<UsbDeviceTckDescriptor> iterator = expectedDevices.iterator();

        while (iterator.hasNext()) {
            UsbDeviceTckDescriptor descriptor = iterator.next();

            if(descriptor instanceof SkipDevicesTckDescriptor) {
                if(!requireAllDevicesConfigured) {
                    fail(prefixDeviceId(usbDevice, "<skipDevices> used twice in a list"));
                }
                requireAllDevicesConfigured = false;
                iterator.remove();
            }
        }

        if(requireAllDevicesConfigured) {
            // This also implies a one to one relationship between the list and the returned expectedDevices. You better
            // hope your implementation's list is consistent between runs.
            assertEquals(prefixDeviceId(usbDevice, "List of devices"), expectedDevices.size(), actualDevices.size());

            for (int i = 0; i < expectedDevices.size(); i++) {
                expectedDevices.get(i).doAsserts(actualDevices.get(i));
            }
        }
        else {
            for (int i = 0; i < expectedDevices.size(); i++) {
                UsbDeviceTckDescriptor expectedDevice = expectedDevices.get(i);

                // for now locate only through idVendor + idProduct

                if(!expectedDevice.hasIdVendorAndIdProduct()) {
                    fail(prefixDeviceId(usbHub, "Device #" + (i + 1) + " has to specify idVendor+idPrefix."));
                }

                UsbDevice device = null;
                for (UsbDevice actualDevice : actualDevices) {
                    if(expectedDevice.productMatches(actualDevice)) {
                        device = actualDevice;
                    }
                }

                if(device == null) {
                    fail(prefixDeviceId(usbHub, "Could not find device with id=" + expectedDevice.getId()));
                }

                expectedDevice.doAsserts(device);
            }
        }
    }
}
