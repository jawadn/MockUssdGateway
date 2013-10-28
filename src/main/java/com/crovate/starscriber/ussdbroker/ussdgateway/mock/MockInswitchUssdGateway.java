/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.ussdgateway.mock;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jawad
 */
public class MockInswitchUssdGateway {
    
   private final static Logger logger = Logger.getLogger(MockInswitchUssdGateway.class.getName()); 
   private static int PORT = 4322;

      
   public static void main(String argv[]) throws Exception
      {
       
          ServerSocket socket = new ServerSocket(PORT);
          Socket connectionSocket = null;
        
          ExecutorService executors = Executors.newFixedThreadPool(10);
          
          System.out.println("Mock ussd gateway starts running on port" + PORT);
          
          while(true)
            {
               try{ 
                    connectionSocket = socket.accept();
                    logger.log(Level.INFO,"Gateway client connected at port:{0}",connectionSocket.getLocalPort());
               }catch (IOException e) {
                    logger.log(Level.SEVERE, "Could not listen on port:" + PORT, e);
                    System.exit(-1);
                }
               
//               Thread thread = new Thread(new ServerConnection(connectionSocket));
//               thread.start();
            
              executors.execute(new MockInswitchGatewayClientHandler(connectionSocket));

            }
      } 
   
    
}
