/*
 * Simple Wrapper for throwing errors
 */
package Utils;

/**
 *
 * @author Aaron
 */
public class Throw 
{
    public static void err(String _msg)
    {
        System.err.println(_msg);
        System.exit(1);
    }
}
