/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.util;

import java.net.SocketAddress;

/**
 *
 * @author jawad
 */
public class StringUtil {
     
    public static String convertStringToHex(String str){
	 
          char[] chars = str.toCharArray();
	
          StringBuilder hex = new StringBuilder();
	  for(int i = 0; i < chars.length; i++){			
	    hex.append(String.format("%02x", (int)chars[i]));
	  }
	
          return hex.toString();
    }
	 

    
    public static String convertHexToString(String hex){

        
        StringBuilder sb = new StringBuilder();
	StringBuilder temp = new StringBuilder();
	 
	 //49204c6f7665204a617661 split into two characters 49, 20, 4c...
	  for( int i=0; i<hex.length()-1; i+=2 ){
	      //grab the hex in pairs
	      String output = hex.substring(i, (i + 2));
	      //convert hex to decimal
	      int decimal = Integer.parseInt(output, 16);
	      //convert the decimal to character
	      sb.append((char)decimal);
	      temp.append(decimal);
	  }
	 
		  return sb.toString();
	  }
    
    public static String getReadableAddress(SocketAddress address) 
    {
        if (address == null) 
        {
            throw new IllegalArgumentException("cannot be null");
        }
        
        return address.toString().split(":")[0].replace("/", "");
    }
    
}
