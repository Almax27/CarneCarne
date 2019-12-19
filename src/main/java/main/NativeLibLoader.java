/**
 * abstracted implementation of code supplied by a forum post somewhere lol
 */

package main;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;
import javax.swing.JOptionPane;

/**
 * @author aaron
 */
public class NativeLibLoader {

    //member variables
    String curDir, sysProp;
    Set<String> OSNames; //using set to ensure no duplicates
    List<String> libPaths; //in same order as OSNames

    //Ctor for NativeLibLoader
    public NativeLibLoader(String sysProperty)
    {
        sysProp = sysProperty;
        curDir = findCurrentDirectory();
        OSNames = new LinkedHashSet(); //maintains insertion order
        libPaths = new ArrayList();
    }
    //Adds OS and it's lib directory relative to the .jar
    public boolean addOS(String os, String relDir)
    {
        boolean result = true;
        result = result & OSNames.add(os);
        result = result & libPaths.add(relDir);
        return result;
    }
    //Sets native lib path relative to OS type
    public void setupPath()
    {
        String osName = System.getProperty("os.name");
        for(int i = 0; i < OSNames.size(); i++)
        {
            if(osName.startsWith((String)OSNames.toArray()[i]))
            {
                //supported OS, set lib path
                String libDir = curDir + libPaths.get(i);
                String oldDir = System.setProperty(sysProp, libDir);
                //URL temp = Thread.currentThread().getContextClassLoader().getResource(libPaths.get(i));
                //String oldDir = System.setProperty(sysProp, temp.toString());
                return;
            }
        }
        //No supported os found
        JOptionPane.showMessageDialog(null, "Unsuported OS: " + osName + ". Exiting...","Error",JOptionPane.ERROR_MESSAGE);
        System.exit(-1);
    }
    //initialise default OS' and lib paths
    public boolean init()
    {
        //setup linux, windows and mac paths
        boolean result = true;
        result = result & addOS("Linux","natives-linux");
        result = result & addOS("Windows","natives-win32");
        result = result & addOS("Mac OS X","natives-mac");
        return result;
    }
    //helper function to find the current directory of the .jar
    private String findCurrentDirectory()
    {
        String output = "";
            output = System.getProperty("user.dir");
            output = NativeLibLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        return output;
    }
}

