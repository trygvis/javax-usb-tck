/**
 * Copyright (c) 2004, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */
package javax.usb.tck;
 
import junit.framework.Assert;
 
import java.util.*;
 
import javax.usb.*;
import javax.usb.util.*;
 
/**
 * IOMethods
 * <p>
 * Helper functions for IO tests
 *
 * @author Leslie Blair
 */
public class IOMethods
{
    protected static void claimInterface(UsbInterface usbInterface)
    {
        Assert.assertFalse("Interface already claimed.", usbInterface.isClaimed());
        try
        {
            usbInterface.claim();
            Assert.assertTrue("usbInterface.isClaimed() returns false after interface is claimed.", usbInterface.isClaimed());
        }
        catch ( Exception e )
        {
            Assert.fail("Exception claiming interface.  " + e.toString());
        }
 
    };
    protected static void releaseInterface(UsbInterface usbInterface)
    {
        Assert.assertTrue("Attempting to release an interface that is not claimed.", usbInterface.isClaimed());
        try
        {
            usbInterface.release();
            Assert.assertFalse("usbInterface.isClaimed() returns true after interface is released.", usbInterface.isClaimed());
        }
        catch ( Exception e )
        {
            Assert.fail("Exception releasing interface.  " + e.toString());
        }
 
    };
 
    protected static void openPipe(UsbPipe usbPipe)
    {
        Assert.assertTrue("Can't open pipe if it is not active.",usbPipe.isActive());
 
        Assert.assertFalse("Pipe already claimed.", usbPipe.isOpen());
        try
        {
            usbPipe.open();
            Assert.assertTrue("usbPipe.isOpen() returns false after pipe is opened.", usbPipe.isOpen());
        }
        catch ( Exception e )
        {
            Assert.fail("Exception opening pipe.  " + e.toString());
        }
 
    };
    protected static void closePipe(UsbPipe usbPipe)
    {
        Assert.assertTrue("Trying to close a pipe that is not open.", usbPipe.isOpen());
        try
        {
            usbPipe.close();
            Assert.assertFalse("usbPipe.isOpen() returns true after pipe is closed.", usbPipe.isOpen());
        }
        catch ( Exception e )
        {
            Assert.fail("Exception closing pipe.  " + e.toString());
        }
 
    };
 
 
    protected static void createListofAllAvailablePipesOfSpecifiedEndpointType(UsbDevice usbDevice, byte endpointType, List usbPipeList)
    {
        //The following are the expected configuration, interface, and alternate setting location of the following types of pipes
        if ( (endpointType == UsbConst.ENDPOINT_TYPE_INTERRUPT) || (endpointType == UsbConst.ENDPOINT_TYPE_BULK) )
        {
            byte configurationNumber = 1;
            byte interfaceNumber = 0;
            byte alternateSetting = 1;
            createListofAllAvailablePipesOfSpecifiedEndpointType(usbDevice, endpointType, configurationNumber,
                                                                 interfaceNumber, alternateSetting, usbPipeList);
        }
        else if ( endpointType == UsbConst.ENDPOINT_TYPE_ISOCHRONOUS )
        {
            byte configurationNumber = 1;
            byte interfaceNumber = 0;
            byte alternateSetting = 0;
            createListofAllAvailablePipesOfSpecifiedEndpointType(usbDevice, endpointType, configurationNumber,
                                                                 interfaceNumber, alternateSetting, usbPipeList);
        }
 
    };
 
    protected static void createListofAllAvailablePipesOfSpecifiedEndpointType(UsbDevice usbDevice, byte endpointType, byte configurationNumber,
                                                                               byte interfaceNumber, byte alternateSetting, List usbPipeList )
    {
        //set desired interface
        selectAlternateSetting(usbDevice, configurationNumber, interfaceNumber, alternateSetting);
 
 
 
        UsbConfiguration usbConfiguration = usbDevice.getUsbConfiguration(configurationNumber);
        UsbInterface usbInterface = usbConfiguration.getUsbInterface(interfaceNumber);
        claimInterface(usbInterface);
 
        List endpointList = new ArrayList();
        List endpointListFromImplementation = null;
        endpointListFromImplementation = usbInterface.getUsbEndpoints();
        for ( int i = 0; i< endpointListFromImplementation.size(); i++ )
        {
            endpointList.add(endpointListFromImplementation.get(i));
        }
 
        //get rid of all the endpoints that are not of the desired type
        int numInList = 0;
        while ( numInList != endpointList.size() )
        {
            if ( (((UsbEndpoint)endpointList.get(numInList)).getType()) != endpointType )
            {
                //it's not the right endpoint type, so remove it from list
                endpointList.remove(numInList);
            }
            else
            {
                //point to next entry in list
                numInList++;
            }
        }
 
 
        //get pipes for all the endpoints
        for ( int i = 0; i < endpointList.size(); i++ )
        {
            usbPipeList.add(((UsbEndpoint)endpointList.get(i)).getUsbPipe());
        }
    };
 
    /**
     *  select alternate setting for the programmable board (AS0 or AS1)
     */
    protected static void selectAlternateSetting(UsbDevice usbDevice,
                                                 byte requiredUsbConfigurationNumber,
                                                 byte requiredInterfaceNumber,
                                                 byte desiredAlternateSetting)
    {
        try
        {
            if ( usbDevice.isConfigured() )
            {
                if ( debug )
                    System.out.println("Active ConfigurationNumber: " + usbDevice.getActiveUsbConfigurationNumber() );
                //if not desired configuration then set configuraion
                if ( usbDevice.getActiveUsbConfigurationNumber() != requiredUsbConfigurationNumber )
                {
                    StandardRequest.setConfiguration(usbDevice, (short)requiredUsbConfigurationNumber);
                    Assert.assertTrue ("Just configured device, but usbDevice.isConfigured() returns false.", usbDevice.isConfigured());
                }
 
 
                //get UsbConfiguraion
                UsbConfiguration usbConfiguration = usbDevice.getActiveUsbConfiguration();
 
                //getUsbInterface
                UsbInterface usbInterface = usbConfiguration.getUsbInterface(requiredInterfaceNumber);
                if ( debug )
                    System.out.println("Select Alternate Setting 1");
                //set desired alternate setting
                StandardRequest.setInterface(usbDevice,requiredInterfaceNumber, desiredAlternateSetting);
            }
        }
        catch ( Exception e )
        {
            Assert.fail("Error setting alternate setting.  " + e.toString());
        }
 
    };
 
    protected static void releaseListOfPipes(List pipeList )
    {
        //all pipes in list are expected to be on the same interface
        UsbInterface usbInterface = ((UsbPipe)pipeList.get(0)).getUsbEndpoint().getUsbInterface();
        while ( pipeList.size() != 0 )
        {
            //make sure all pipes are closed (needed for error conditions)
            if ( ((UsbPipe) pipeList.get(0)).isOpen() == true )
            {
                closePipe((UsbPipe) pipeList.get(0));
            }
            pipeList.remove(0);
        }
        releaseInterface(usbInterface);
    };
 
    protected static void findINandOUTPipesForTest(List pipeList, int endpointSize, int[] pipeIndexes,
                                                   int inPipeArrayIndex, int outPipeArrayIndex)
    {
        int i;
        int j;
 
        int inPipeListIndex = 0;
        int outPipeListIndex = 0;
 
        Assert.assertFalse("There are no pipes in list.",(0 == pipeList.size()));
 
        boolean pipeIndexesFound = false;
 
        for ( i = 0; i< pipeList.size(); i++ )
        {
 
            UsbPipe usbPipe1 = (UsbPipe) pipeList.get(i);
            if ( usbPipe1.getUsbEndpoint().getUsbEndpointDescriptor().wMaxPacketSize() == endpointSize )
            {
                //it's the correct endpoint size, now get a matching pair
                //For a pair to match, they must be an IN/OUT endpoint pair and their maxPacketSizes must be the same
                byte endpointAddress1 = usbPipe1.getUsbEndpoint().getUsbEndpointDescriptor().bEndpointAddress();
                byte endpointAddress2;
                if ( usbPipe1.getUsbEndpoint().getDirection() == UsbConst.ENDPOINT_DIRECTION_IN )
                {
                    endpointAddress2 = (byte)(endpointAddress1 & ~UsbConst.ENDPOINT_DIRECTION_MASK);
                    inPipeListIndex = i;
                }
                else
                {
                    endpointAddress2 = (byte) (endpointAddress1 | UsbConst.ENDPOINT_DIRECTION_MASK);
                    outPipeListIndex = i;
                }
 
                //go through list looking for address 2
                for ( j = i+1; j< pipeList.size(); j++ )
                {
                    UsbPipe usbPipe2 = (UsbPipe) pipeList.get(j);
                    if ( (usbPipe2.getUsbEndpoint().getUsbEndpointDescriptor().bEndpointAddress() == endpointAddress2)
                         && (usbPipe2.getUsbEndpoint().getUsbEndpointDescriptor().wMaxPacketSize() == endpointSize)
                       )
                    {
                        //a pair of endpoints has been found
                        if ( usbPipe2.getUsbEndpoint().getDirection() == UsbConst.ENDPOINT_DIRECTION_IN )
                        {
                            inPipeListIndex = j;
                        }
                        else
                        {
                            outPipeListIndex = j;
                        }
                        pipeIndexesFound = true;
                        break; //break out of inner loop as a pair has been found
                    }
 
                }
            }
 
            if ( pipeIndexesFound )
            {
                pipeIndexes[inPipeArrayIndex] = inPipeListIndex;
                pipeIndexes[outPipeArrayIndex] = outPipeListIndex;
                break;
            }
 
        }
 
    };
    protected static void verifyThePipes(UsbPipe inPipe, UsbPipe outPipe, int endpointSize)
    {
        Assert.assertNotNull("IN pipe should not be null.", inPipe);
        Assert.assertNotNull("OUT pipe should not be null.",outPipe);
        Assert.assertTrue("These pipes are not from matchine endpoint pairs.",
                          ((inPipe.getUsbEndpoint().getUsbEndpointDescriptor().bEndpointAddress()) - UsbConst.ENDPOINT_DIRECTION_MASK)
                          == (outPipe.getUsbEndpoint().getUsbEndpointDescriptor().bEndpointAddress()));
        Assert.assertEquals("The IN pipe does not have the correct maxPacketSize.",
                            endpointSize,inPipe.getUsbEndpoint().getUsbEndpointDescriptor().wMaxPacketSize());
        Assert.assertEquals("The OUT pipe does not have the correct maxPacketSize.",
                            endpointSize,outPipe.getUsbEndpoint().getUsbEndpointDescriptor().wMaxPacketSize());
 
    };
 
    /**
     * printDebug method will print the specified string if "debug" is true.
     * Useful function for debugging
     * @param infoString
     */
    protected static void printDebug(String infoString)
    {
        if ( debug )
        {
            System.out.println(infoString);
        }
    }
    private static boolean debug = false;
    //private static boolean debug = true;
}
