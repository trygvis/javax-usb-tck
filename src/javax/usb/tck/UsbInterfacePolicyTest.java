package javax.usb.tck;

/**
 * Copyright (c) 2004, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import junit.framework.TestCase;
import java.util.*;

import javax.usb.*;

/**
 * Usb Interface Policy Test
 * <p>
 * Goal: This test will use a new implementation of the UsbInterfacePolicy
 *               Interface to test the that the UsbInterfacePolicy is being used correctly.
 *
 * @author Dale Heeks
 */
public class UsbInterfacePolicyTest extends TestCase
{

    public UsbInterfacePolicyTest(String name)
    {

        super(name);
    }

    protected void setUp() throws Exception {
        usbDevice = programmableDevice.getProgrammableDevice();
        assertTrue( "Find Programmable board Failed! Got a null instance", usbDevice != null );
        assertTrue("The usbDevice is not confuigured", usbDevice.isConfigured());
        usbConfiguration = usbDevice.getUsbConfiguration((byte)1);
        policy1 = new InterfacePolicyImp();
    }

    protected void tearDown() throws Exception {

    }


    /*ForceClaim policy test cases */
    /**
     * Test forceClaim policy with a false return
     * <p>
     * This test case will set the ForceClaim policy
     * to false and then call claim to ensure that the
     * ForceClaim policy method is invoked inside
     * the UsbInterfacePolicyImp class
     *<p>
     * <strong>NOTE:</strong> This will not check if the claim isn't forced
     * because some implemetations
     * will not be able to do a force claim and some will,
     *
     */

    public void testIfaceForceClaimFalse()
    {


        policy1.setForceClaimPolicy(false);

        assertFalse("Force Claim tag isn't reset, this is probably a TCK defect",
                    policy1.getForceClaimTag() );

        assertTrue("Can't claim Interface, this is probably a defect with your implementation",
                   claimIface(usbConfiguration.getUsbInterface((byte)0), policy1));

        assertTrue("Force claim tag not set, this probably means the forceClaim method was not called by the implementation",
                   policy1.getForceClaimTag() );

        //reset the claim tag
        policy1.setForceClaimTag(false);

        releaseIface(usbConfiguration.getUsbInterface((byte)0), null);

    }

    /**
     * Test forceClaim policy with a true return
     * <p>
     * This test case will set the ForceClaim policy
     * to true and then call claim to ensure that the
     * ForceClaim policy method is invoked inside
     * the UsbInterfacePolicyImp class
     *<p>
     * <strong>NOTE:</strong> This will not check if the claim is forced
     * because some implemetations
     * will not be able to do a force claim and some will.
     *
     */
    public void testIfaceForceClaimTrue()
    {

        policy1.setForceClaimPolicy(true);

        assertFalse("Force Claim tag isn't reset, this is probably a TCK defect",
                    policy1.getForceClaimTag() );

        assertTrue("Can't claim Interface, this is probably a defect with your implementation",
                   claimIface(usbConfiguration.getUsbInterface((byte)0), policy1));

        assertTrue("Force claim tag not set, this probably means the force claim policy isn't being used",
                   policy1.getForceClaimTag() );

        //Reset the force claim tag
        policy1.setForceClaimTag(false);

        releaseIface(usbConfiguration.getUsbInterface((byte)0), null);

    }


    /* Open pipe using interface policy test cases */

    /**
     * Test Open policy with a true return and a null key
     * <p>
     * This test case will set the open policy to true and
     * then will claim an interface with that policy and try
     * to open the first pipe it can find.
     * <p>
     * This test case will succeed if the open returns successfully
     */

    public void testIfaceOpenPolicyTrueNoKey()
    {


        policy1.setOpenPolicy(true);


        assertTrue("Couldn't claim interface",
                   claimIface(usbConfiguration.getUsbInterface((byte)0), policy1));
        UsbPipe e1 = getUsbPipe(usbConfiguration.getUsbInterface((byte)0));
        assertEquals("Couldn't open pipe", opened, openPipe(e1, null));

        //clean up
        closePipe(e1);
        releaseIface(usbConfiguration.getUsbInterface((byte)0), null);
    }

    /**
     * Test Open policy with a false return and a null key
     * <p>
     * This test case will set the open policy to false and
     * then will claim an interface with that policy and try
     * to open the first pipe it can find.
     * <p>
     * This test case will succeed if the open returns a policyDenied exception
     */
    public void testIfaceOpenPolicyFalseNoKey()
    {

        policy1.setOpenPolicy(false);

        assertTrue("Couldn't claim interface",
                   claimIface(usbConfiguration.getUsbInterface((byte)0), policy1));
        UsbPipe e1 = getUsbPipe(usbConfiguration.getUsbInterface((byte)0));
        assertEquals("Could open pipe, but policy shouldn't have allowed it",
                     policyDenied, openPipe(e1, null));

        //clean up
        closePipe(e1);
        releaseIface(usbConfiguration.getUsbInterface((byte)0), null);

    }

    /**
     * Test Open policy with key1, a true open policy
     * <p>
     * This test case will claim an interface and try
     * to open the first pipe it can find with key1.
     * <p>
     * key1 will set the open policy to true
     * key2 will set the open policy to false
     * <p>
     * This test case will succeed if the open returns successfully
     */
    public void testIfaceOpenPolicyKey1()
    {

        assertTrue("Couldn't claim interface",
                   claimIface(usbConfiguration.getUsbInterface((byte)0), policy1));
        UsbPipe e1 = getUsbPipe(usbConfiguration.getUsbInterface((byte)0));
        assertEquals("Couldn't open pipe with key1, should of been able to",
                     opened, openPipe(e1, key1));

        //clean up
        closePipe(e1);
        releaseIface(usbConfiguration.getUsbInterface((byte)0), null);

    }

    /**
     * Test Open policy with key2, a false open policy
     * <p>
     * This test case will claim an interface and try
     * to open the first pipe it can find with key2.
     * <p>
     * key1 will set the open policy to true
     * key2 will set the open policy to false
     * <p>
     * This test case will succeed if the open returns a policyDenied exception
     */
    public void testIfaceOpenPolicyFalseKey()
    {

        assertTrue("Couldn't claim interface",
                   claimIface(usbConfiguration.getUsbInterface((byte)0), policy1));
        UsbPipe e1 = getUsbPipe(usbConfiguration.getUsbInterface((byte)0));
        assertEquals("Could open pipe with key2 but policy should not allow this",
                     policyDenied, openPipe(e1, key2));

        //clean up
        closePipe(e1);
        releaseIface(usbConfiguration.getUsbInterface((byte)0), null);

    }


    /*Release policy test cases*/

    /**
     * Test release policy with a true return and a null key
     * <p>
     * This test case will attempt to release an interface that has an open
     * policy of true.
     * <p>
     * This test case will succeed if the release returns successfully
     */
    public void testIfaceReleaseTrueNoKeyClaimed()
    {

        policy1.setReleasePolicy(true);

        assertTrue("Couldn't claim interface",
                   claimIface(usbConfiguration.getUsbInterface((byte)0), policy1));
        assertEquals("Can't release device, policy should allow this", released,
                     releaseIface(usbConfiguration.getUsbInterface((byte)0), null));

    }

    /**
     * Test release policy with a false return and a null key
     * <p>
     * This test case will attempt to release an interface that has an open
     * policy of false.
     * <p>
     * This test case will succeed if the release returns a policyDenied exception
     */
    public void testIfaceReleaseFalseNoKeyClaimed()
    {

        policy1.setReleasePolicy(false);

        assertTrue("Couldn't claim interface",
                   claimIface(usbConfiguration.getUsbInterface((byte)0), policy1));
        assertEquals("Interface shouldn't release per the policy, but it did", policyDenied,
                     releaseIface(usbConfiguration.getUsbInterface((byte)0), null));

        policy1.setReleasePolicy(true);
        releaseIface(usbConfiguration.getUsbInterface((byte)0), null);

    }

    /**
     * Test release policy with key1, a true release policy
     * <p>
     * This test case will attempt to release an interface
     * with key1
     * <p>
     * key1 will set the release policy to true
     * key2 will set the release policy to false
     * <p>
     * This test case will succeed if the release returns successfully
     */
    public void testIfaceReleaseKey1Claimed()
    {

        assertTrue("Couldn't claim interface",
                   claimIface(usbConfiguration.getUsbInterface((byte)0), policy1));
        assertEquals("Can't release device with key1 but should be able to", released,
                     releaseIface(usbConfiguration.getUsbInterface((byte)0), key1));
    }

    /**
     * Test release policy with key2, a false release policy
     * <p>
     * This test case will attempt to release an interface
     * with key2
     * <p>
     * key1 will set the release policy to true
     * key2 will set the release policy to false
     * <p>
     * This test case will succeed if the release returns a policyDenied exception
     */
    public void testIfaceReleaseKey2Claimed()
    {

        assertTrue("Couldn't claim interface",
                   claimIface(usbConfiguration.getUsbInterface((byte)0), policy1));
        assertEquals("Able to release device with key2, this shouldn't be allowed", policyDenied,
                     releaseIface(usbConfiguration.getUsbInterface((byte)0), key2));

        policy1.setReleasePolicy(true);
        releaseIface(usbConfiguration.getUsbInterface((byte)0), null);

    }

    /**
     * Test a release of an interface that isn't claimed
     * <p>
     * The releasePolicy is set to true
     */
    public void testIfaceReleaseFalseNoKeyNotClaimed()
    {

        policy1.setReleasePolicy(true);
        assertEquals("Never claimed, shouldn't release", notClaimed,
                     releaseIface(usbConfiguration.getUsbInterface((byte)0), null));

    }

    /**
     * Test a release of an interface that isn't claimed
     * <p>
     * The releasePolicy is set to false
     */
    public void testIfaceReleaseKey1NotClaimed()
    {

        assertEquals("Never claimed, shouldn't release", notClaimed,
                     releaseIface(usbConfiguration.getUsbInterface((byte)0), key1));

    }


    //This method will take an interface and policy and call the appropriate
    //claim method, this method should only be used within this test
    private boolean claimIface(UsbInterface iface,
                               UsbInterfacePolicy ifacePolicy)
    {
        try
        {
            if ( ifacePolicy == null )
                iface.claim();
            else
                iface.claim(ifacePolicy);
        }
        catch ( UsbClaimException uce )
        {
            return false;
        }
        catch ( UsbNotActiveException unae )
        {
            fail("UsbNotActiveException: " + unae);
        }
        catch ( UsbException ue )
        {
            fail("UsbException: " + ue);
        }
        return true;
    }


    //This method takes a interface and key and make the appropriate
    //interface method call
    private int releaseIface(UsbInterface iface,
                             Object key)
    {
        try
        {
            if ( key == null )
                iface.release();
            else
                iface.release(key);
        }
        catch ( UsbClaimException uce )
        {
            return notClaimed;
        }
        catch ( UsbPolicyDenied uce )
        {
            return policyDenied;
        }
        catch ( UsbNotActiveException unae )
        {
            fail("UsbNotActiveException: " + unae);
        }
        catch ( UsbException ue )
        {
            fail("UsbException: " + ue);
        }
        return released;
    }

    //Method that takes an interface and returns it's first pipe
    private UsbPipe getUsbPipe(UsbInterface iface)
    {
        List endpoints = iface.getUsbEndpoints();
        UsbEndpoint e1 = (UsbEndpoint)endpoints.get(0);
        return e1.getUsbPipe();
    }

    //Method that will call the appropriate open method given
    //a pipe and a key
    private int openPipe(UsbPipe pipe, Object key)
    {
        try
        {
            if ( key == null )
                pipe.open();
            else
                pipe.open(key);
        }
        catch ( UsbPolicyDenied uce )
        {
            return policyDenied;
        }
        catch ( UsbNotActiveException unae )
        {
            fail("UsbNotActiveException: " + unae);
        }
        catch ( UsbNotClaimedException unae )
        {
            fail("UsbNotActiveException: " + unae);
        }
        catch ( UsbException ue )
        {
            fail("UsbException: " + ue);
        }

        return opened;
    }

    //method to close a pipe, this is for test case clean up
    private boolean closePipe(UsbPipe pipe)
    {
        try
        {
            pipe.close();
        }
        catch ( UsbException ue )
        {
            fail("UsbException: " + ue);
        }
        catch ( UsbNotActiveException unae )
        {
            fail("UsbNotOpenedException: " + unae);
        }
        return true;
    }


    private FindProgrammableDevice programmableDevice = FindProgrammableDevice.getInstance();
    private InterfacePolicyImp policy1 = null;
    private UsbConfiguration usbConfiguration = null;
    private UsbDevice usbDevice = null;
    private UsbInterface iface = null;
    private UsbEndpoint Endpoint;
    private static final boolean debug = true;
    private static final int released = 1111;
    private static final int opened = 2222;
    private static final int policyDenied = 3333;
    private static final int notClaimed = 4444;
    private static final String key1 = "key1";
    private static final String key2 = "key2";



    /*
     *************************************************************************
     ********************Test Interface Policy Implemetation class************
     *************************************************************************/
    /**
     * Copyright (c) 2004, International Business Machines Corporation.
     * All Rights Reserved.
     *
     * This software is provided and licensed under the terms and conditions
     * of the Common Public License:
     * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
     */

    /**
     * Name of class -- Usb Interface Policy test implementation
     * <p>
     * This class is an implementation of the UsbInterfacePolicy, this
     * should only be used for the Javax.usb tck.
     *
     * @author Dale Heeks
     */

    private class InterfacePolicyImp implements UsbInterfacePolicy
    {


        /**Constuctor
         *
         * <p>
         * Default constructor, sets everything in this policy to true,
         * which is also the same as the default policy
         *
         *
         */
        public InterfacePolicyImp ()
        {
            releasePolicy = true;
            openPolicy = true;
            forceClaimPolicy = true;
            forceClaimTag = false;
        }


        /**Constuctor that will set the policy dynamically.
         *
         * @param releaseIn The release policy
         * @param openIn The open policy
         * @param forceClaimIn The force claim policy
         *
         */
        public InterfacePolicyImp (boolean releaseIn, boolean openIn, boolean forceClaimIn)
        {
            releasePolicy = releaseIn;
            openPolicy = openIn;
            forceClaimPolicy = forceClaimIn;
            forceClaimTag = false;
        }

        /**Method that should be called from a release using
         * the non default interface policy
         *
         *@param arg0 The current UsbInterface that is being released
         *@param arg1 A key that will categorize this release call
         *
         *@return boolean Whether to allow an interface to be releaed
         */
        public boolean release(UsbInterface arg0, Object arg1)
        {

            String key = (String)arg1;

            if ( key == null )
                return releasePolicy;

            if ( key.compareTo("key1")==0 )
                return true;

            if ( key.compareTo("key2")==0 )
                return false;

            return releasePolicy;
        }

        /**Method that should be called from an open using
         * the non default interface policy
         *
         *@param arg0 The current UsbPipe that is trying to be opened
         *@param arg1 A key that will categorize this open call
         *
         *@return boolean Whether to allow a pipe to be opened
         */
        public boolean open(UsbPipe arg0, Object arg1)
        {

            String key = (String)arg1;

            if ( key == null )
                return openPolicy;

            if ( key.compareTo("key1")==0 )
                return true;

            if ( key.compareTo("key2")==0 )
                return false;


            return openPolicy;
        }

        /**Method that should be called from a claim using
         * the non default interface policy
         *
         *@param arg0 The current UsbInterface that is being released
         *
         *@return boolean Whether to allow an interface to attempt
         *a force claim
         */
        public boolean forceClaim(UsbInterface arg0)
        {
            forceClaimTag = true;
            return forceClaimPolicy;
        }

        /**Allows a user to dynamically set the release policy
         *
         * @param releaseIn The new release policy
         */
        public void setReleasePolicy(boolean releaseIn)
        {
            releasePolicy = releaseIn;
        }

        /**Allows a user to dynamically set the open policy
         *
         * @param openIn The new open policy
         */
        public void setOpenPolicy(boolean openIn)
        {
            openPolicy = openIn;
        }

        /**Allows a user to dynamically set the force claim policy
         *
         * @param forceClaimIn The new force claim policy
         */
        public void setForceClaimPolicy(boolean forceClaimIn)
        {
            forceClaimPolicy = forceClaimIn;
        }

        /**Allows a user to dynamically set the force claim tag
         *
         * <p>
         * The force claim tag checks to see if the force claim policy has been called
         *
         * @param forceClaimTagIn The new force claim tag
         */
        public void setForceClaimTag(boolean forceClaimTagIn)
        {
            forceClaimTag = forceClaimTagIn;
        }

        /**Allows a user to check the force claim tag
         *
         * <p>
         * The force claim tag checks to see if the force claim policy has been called
         *
         * @return boolean The force claim tag
         */
        public boolean getForceClaimTag()
        {
            return forceClaimTag;
        }

        private boolean releasePolicy;
        private boolean openPolicy;
        private boolean forceClaimPolicy;
        private boolean forceClaimTag;



    }
    /*************************************************************************/
}
