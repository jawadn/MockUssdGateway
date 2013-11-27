package com.crovate.launchpad;

import com.crovate.launchpad.initializer.BootStrap;

/**
 * Main method class for housing main method separate from any business class
 *
 */
public class Launcher 
{
    
    public static void main(String args[])
    {
    
        BootStrap.bootUssdBroker(args);
    
    }
    
    
}
