package javax.usb.tck.topology;

import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.converters.reflection.*;
import com.thoughtworks.xstream.io.xml.*;
import static junit.framework.Assert.*;

import javax.usb.*;
import javax.usb.tck.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class TopologyTckDescriptor {

    String apiVersion;
    String usbVersion;

    List<EntryTckDescriptor> usbProperties = new ArrayList<EntryTckDescriptor>();
    UsbHubTckDescriptor usbHub;

    private static final XStream xstream = new XStream(new PureJavaReflectionProvider(),
        new XppDomDriver()) {{
        alias("entry", EntryTckDescriptor.class);

        alias("topology", TopologyTckDescriptor.class);
        aliasAttribute(TopologyTckDescriptor.class, "apiVersion", "apiVersion");
        aliasAttribute(TopologyTckDescriptor.class, "usbVersion", "usbVersion");

        alias("skipDevices", SkipDevicesTckDescriptor.class);

        alias("usbDevice", UsbDeviceTckDescriptor.class);
        aliasAttribute(UsbDeviceTckDescriptor.class, "usbHub", "usbHub");
        aliasAttribute(UsbDeviceTckDescriptor.class, "idVendorString", "idVendor");
        aliasAttribute(UsbDeviceTckDescriptor.class, "idProductString", "idProduct");

        alias("usbHub", UsbHubTckDescriptor.class);
        aliasAttribute(UsbHubTckDescriptor.class, "rootUsbHub", "rootUsbHub");
        aliasAttribute(UsbHubTckDescriptor.class, "expectedDevices", "devices");
    }};

    public void writeTo(OutputStream outputStream) {
        xstream.toXML(this, outputStream);
    }

    public static TopologyTckDescriptor fromXml(URL url) throws IOException {
        if (url == null) {
            throw new IOException("Could not find resource");
        }

        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            TopologyTckDescriptor descriptor = (TopologyTckDescriptor) xstream.fromXML(inputStream);
            return descriptor;
        } finally {
            TckUtils.closeSilently(inputStream);
        }
    }

    public void doAsserts(UsbServices usbUsbServices) throws Exception {
        Properties properties = new Properties();
        properties.putAll(UsbHostManager.getProperties());

        for (EntryTckDescriptor usbProperty : usbProperties) {
            if (usbProperty.value == null) {
                fail("Missing required usb property: " + usbProperty.key);
            } else {
                assertEquals("Illegal value for property: " + usbProperty.key,
                    properties.get(usbProperty.key),
                    usbProperty.value);
            }
        }

        if (usbHub != null) {
            usbHub.doAsserts(usbUsbServices.getRootUsbHub());
        }
    }
}
