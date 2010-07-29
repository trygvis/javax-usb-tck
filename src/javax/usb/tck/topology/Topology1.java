package javax.usb.tck.topology;

import javax.usb.*;

// Use this to generate an example XML file.
public class Topology1 {
    public static final TopologyTckDescriptor descriptor;

    static {
        descriptor = new TopologyTckDescriptor();
        descriptor.apiVersion = Version.getApiVersion();
        descriptor.usbVersion = Version.getUsbVersion();

        descriptor.usbHub = new UsbHubTckDescriptor();
        descriptor.usbHub.rootUsbHub = true;
    }

    public static void main(String[] args) {
        descriptor.writeTo(System.out);
    }
}
