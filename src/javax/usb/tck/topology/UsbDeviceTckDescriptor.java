package javax.usb.tck.topology;

import static javax.usb.tck.TckUtils.*;
import static junit.framework.Assert.*;

import javax.usb.*;
import javax.usb.util.*;

public class UsbDeviceTckDescriptor {
    Boolean usbHub;
    String idVendorString;
    String idProductString;
    String speed;

    transient short idVendor;
    transient short idProduct;

    @SuppressWarnings({"UnusedDeclaration"})
    private Object readResolve() {
        if (idVendorString != null) {
            idVendor = (short) Integer.decode(idVendorString).intValue();
        }
        if (idProductString != null) {
            idProduct = (short) Integer.decode(idProductString).intValue();
        }

        System.out.println("idVendorString = " + idVendorString);
        System.out.println("idProductString = " + idProductString);
        System.out.println("speed = " + speed);

        return this;
    }

    public boolean hasIdVendorAndIdProduct() {
        return idVendor != 0 && idProduct != 0;
    }

    public String getId() {
        return idVendor + ":" + idProduct;
    }

    public boolean productMatches(UsbDevice actualDevice) {
        return
            actualDevice.getUsbDeviceDescriptor().idVendor() == idVendor &&
                actualDevice.getUsbDeviceDescriptor().idProduct() == idProduct;
    }

    public void doAsserts(UsbDevice usbDevice) {
        if (usbHub != null) {
            assertEquals(prefixDeviceId(usbDevice, "usbHub"), usbHub.booleanValue(), usbDevice.isUsbHub());
        }

        if (idVendorString != null) {
            assertEquals(prefixDeviceId(usbDevice, "idVendor"), idVendor, usbDevice.getUsbDeviceDescriptor().idVendor());
        }

        if (idProductString != null) {
            assertEquals(prefixDeviceId(usbDevice, "idProduct"), idProduct, usbDevice.getUsbDeviceDescriptor().idProduct());
        }

        System.out.println("speed = " + speed);
        if (speed != null) {
            assertEquals(prefixDeviceId(usbDevice, "speed"), speed, UsbUtil.getSpeedString(usbDevice.getSpeed()));
        }
    }
}
