package javax.usb.tck;
 
import junit.framework.*;

/**
 * This class executes the following tests of the Javax.usb TCK Test Suite:
 * <ul>
 * <li> Signature Test
 * </ul>
 */
public class SIGNATURE extends TestCase
{
    public static Test suite()
    {
        //Create test suite
        TestSuite testSuite = new TestSuite();

        //Add tests to the test suite.
        //Each method beginning with testXXX() in the specified class will
        //be called as a separate test.
        testSuite.addTestSuite(SignatureTest.class);
        //The SynchFrame does not work with the Cypress Board                                    @P1D3
        return testSuite;
    }

    public static void main(String[] argv)
    {
        //use the textual Test Runner to call the test suite
        System.exit(junit.textui.TestRunner.run(suite()).wasSuccessful() ? 0 : 1);
    }
}
