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
 * FLAG REASON   RELEASE  DATE   WHO      DESCRIPTION
 * ---- -------- -------- ------ -------- ------------------------------------
 * 0000 nnnnnnn           yymmdd          Initial Development
 * $P1           tck.rel1 041222 raulortz Change API Version supported
 */

/**
 * Topology Tests - Contains constants used for all Topology Tests
 * <p>
 * This interface contains the constants used by TopologyTestConfig1,
 * TopologyTestConfig2 and TopologyTestConfig3.  There are no methods to
 * implement in this interface.
 * @author Joshua Lowry
 */

abstract interface TopologyTests
{

    //**************************************************************************
    // Device Descriptor Constants of type byte for the programable device

    public static final byte programableLength = (byte) 0x12;
    public static final byte programableDeviceClass = (byte) 0xFF;
    public static final byte programableDeviceSubClass = (byte) 0xFF;
    public static final byte programableDeviceProtocol = (byte) 0xFF;
    public static final byte programableMaxPacketSize = (byte) 0x40;
    public static final byte programableManufacturerIndex = (byte) 0x01;
    public static final byte programableProductIndex = (byte) 0x02;
    public static final byte programableSerialNumIndex = (byte) 0x03;
    public static final byte programableNumConfigurations = (byte) 0x02;

    //**************************************************************************
    // Configuration Descriptor Constants of type byte for the programable
    // device

    public static final byte programableNumInterfaces[] = {(byte) 0x02, (byte) 0x03};
    public static final byte programableConfigValue[] = {(byte) 0x01, (byte) 0x02};
    public static final byte programableConfigIndex[] = {(byte) 0x04, (byte) 0x05};

    //**************************************************************************
    // Interface Descriptor Constants of type byte for the programable device

    public static final byte programableNumSettings[][] = {{(byte) 0x02, (byte) 0x01}, 
        {(byte) 0x01, (byte) 0x01, (byte) 0x03}};
    public static final byte programableNumEndpoints[][][] = {{{(byte) 0x1E,
                (byte) 0x0A},{(byte) 0x00}},{{(byte) 0x00}, {(byte) 0x01},
            {(byte) 0x02, (byte) 0x03, (byte) 0x04}}};
    public static final byte programableInterfaceIndex[][][] = {{{(byte) 0x06,
                (byte) 0x07},{(byte) 0x00}},{{(byte) 0x08}, {(byte) 0x09}, 
            {(byte) 0x0A, (byte) 0x0B, (byte) 0x0C}}};

    //**************************************************************************
    // Endpoint Descriptor Constants of type byte for the programable device

    public static final byte programableEndpointAddr[][][][] = {{{{(byte) 0x81,
                    (byte) 0x01, (byte) 0x82, (byte) 0x02, (byte) 0x83, (byte) 0x03,
                    (byte) 0x84, (byte) 0x04, (byte) 0x85, (byte) 0x05, (byte) 0x86,
                    (byte) 0x06, (byte) 0x87, (byte) 0x07, (byte) 0x88, (byte) 0x08,
                    (byte) 0x89, (byte) 0x09, (byte) 0x8A, (byte) 0x0A, (byte) 0x8B,
                    (byte) 0x0B, (byte) 0x8C, (byte) 0x0C, (byte) 0x8D, (byte) 0x0D,
                    (byte) 0x8E, (byte) 0x0E, (byte) 0x8F, (byte) 0x0F},{(byte) 0x81,
                    (byte) 0x01, (byte) 0x82, (byte) 0x02, (byte) 0x83, (byte) 0x03,
                    (byte) 0x84, (byte) 0x04, (byte) 0x88, (byte) 0x08}}},{{{}},
            {{(byte) 0x83}},{{(byte) 0x85, (byte) 0x05},{(byte) 0x88,
                    (byte) 0x08, (byte) 0x89},{(byte) 0x81, (byte) 0x83, (byte) 0x85,
                    (byte) 0x87}}}};
    public static final byte programableEndpointType[][][][] = {{{{(byte) 0x02,
                    (byte) 0x02, (byte) 0x03, (byte) 0x03, (byte) 0x02, (byte) 0x02,
                    (byte) 0x02, (byte) 0x02, (byte) 0x03, (byte) 0x03, (byte) 0x03,
                    (byte) 0x03, (byte) 0x03, (byte) 0x03, (byte) 0x01, (byte) 0x01,
                    (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
                    (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
                    (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01},{(byte) 0x03,
                    (byte) 0x03, (byte) 0x03, (byte) 0x03, (byte) 0x02, (byte) 0x02,
                    (byte) 0x02, (byte) 0x02, (byte) 0x01, (byte) 0x01}}},{{{}},
            {{(byte) 0x02}},{{(byte) 0x03, (byte) 0x03},{(byte) 0x01,
                    (byte) 0x01, (byte) 0x01},{(byte) 0x02, (byte) 0x02, (byte) 0x02,
                    (byte) 0x02}}}};
    public static final byte programableEndpointInter[][][][] = {{{{(byte) 0x00,
                    (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0x00,
                    (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                    (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x01, (byte) 0x01,
                    (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
                    (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
                    (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01},{(byte) 0xFF,
                    (byte) 0x0FF, (byte) 0xFF, (byte) 0x0FF, (byte) 0x00, (byte) 0x00,
                    (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x01}}},{{{}},
            {{(byte) 0x00}},{{(byte) 0xFF, (byte) 0xFF},{(byte) 0x01,
                    (byte) 0x01, (byte) 0x01},{(byte) 0x00, (byte) 0x00, (byte) 0x00,
                    (byte) 0x00}}}};

    //**************************************************************************
    // Device Descriptor Constants of type short for the programable device

    public static final short programableBcdUsb = (short) 0x0110;
    public static final short programableBcdDevice = (short) 0x0100;
    public static final short programableVendorID  = (short) 0x0547;
    public static final short programableProductID = (short) 0xFF01;

    //**************************************************************************
    // Endpoint Descriptor Constants of type short for the programable device

    public static final short programableEndpointMPS[][][][] = 
    {{{{(short) 0x0040, (short) 0x0040, (short) 0x0008, (short) 0x0008,
                    (short) 0x0040, (short) 0x0040, (short) 0x0008, (short) 0x0008,
                    (short) 0x0010, (short) 0x0010, (short) 0x0020, (short) 0x0020,
                    (short) 0x0040, (short) 0x0040, (short) 0x0010, (short) 0x0010,
                    (short) 0x0010, (short) 0x0010, (short) 0x0020, (short) 0x0020,
                    (short) 0x0020, (short) 0x0020, (short) 0x0040, (short) 0x0040, 
                    (short) 0x0040, (short) 0x0040, (short) 0x0080, (short) 0x0080,
                    (short) 0x0040, (short) 0x0040},{(short) 0x0040, (short) 0x0040,
                    (short) 0x0008, (short) 0x0008, (short) 0x0040, (short) 0x0040,
                    (short) 0x0008, (short) 0x0008, (short) 0x03FF, (short) 0x03FF}}},
        {{{}},{{(short) 0x0040}},{{(short) 0x0010, (short) 0x0010},
                {(short) 0x0010, (short) 0x0010, (short) 0x0010},
                {(short) 0x0040, (short) 0x0040, (short) 0x0010, (short) 0x0040}}}};

    //**************************************************************************
    // String Descriptor Constants for the programable device

    public static final String APIVersion = "1.0.0";                                         // @P1C
    public static final String USBVersion = "1.1";
    public static final String programableStrings[] = {"Manufacturer", 
        "JSR80 TCK Device 1",   "SN123456", "Config 1", "Config 2",
        "C1 I0 AS0", "C1 I0 AS1", "C2 I0 AS0", "C2 I1 AS0", "C2 I2 AS0",
        "C2 I2 AS1", "C2 I2 AS2",
        "\u30DE\u30F3\u30A5\u0032\u0020\u0049\u0032\u0020\u0041\u0053\u0032"};
    public static final String programableInterfaceStrings[][][] = {{{"C1 I0 AS0", "C1 I0 AS1"}, {null}},
        {{"C2 I0 AS0"},{"C2 I1 AS0"},{"C2 I2 AS0","C2 I2 AS1","C2 I2 AS2"}}};
}
