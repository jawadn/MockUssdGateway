/*
 * Abstract class with static variables 
 * providing default parameters for booting ussd broker
 * 
 */
package com.crovate.launchpad.initializer;

/**
 *
 * @author kaunain
 */
public abstract class BootStrapRuntime {
    
    private static String defaultIp="127.0.0.1";
    
    private static  int defaultServerPort = 6789;

    public static String getDefaultIp() {
        return defaultIp;
    }

    public static void setDefaultIp(String defaultIp) {
        BootStrapRuntime.defaultIp = defaultIp;
    }

    public static int getDefaultServerPort() {
        return defaultServerPort;
    }

    public static void setDefaultServerPort(int defaultServerPort) {
        BootStrapRuntime.defaultServerPort = defaultServerPort;
    }

  
    
    
    
    
}
