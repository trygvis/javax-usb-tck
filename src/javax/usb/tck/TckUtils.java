package javax.usb.tck;

import javax.usb.*;
import javax.usb.util.*;
import java.io.*;

public class TckUtils {
    public static void closeSilently(InputStream inputStream) {
        try {
            if (inputStream == null) {
                return;
            }
            inputStream.close();
        } catch (IOException ignore) {
        }
    }

    public static String prefixDeviceId(UsbDevice usbDevice, String s) {
        return
            UsbUtil.toHexString(usbDevice.getUsbDeviceDescriptor().idVendor()) + ":" +
            UsbUtil.toHexString(usbDevice.getUsbDeviceDescriptor().idProduct()) + ": "
                + s;
    }
}
