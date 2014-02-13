/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.ussdgateway.mock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jawad
 */
public class MockInswitchUssdGateway {
    
   private final static Logger logger = LoggerFactory.getLogger(MockInswitchUssdGateway.class); 
   private static int PORT = 4322;

      
   public static void main(String argv[]) throws Exception
      {
       
          //ServerSocket socket = new ServerSocket(PORT);
          ServerSocket socket = new ServerSocket();
          SocketAddress address = new InetSocketAddress(PORT);
          socket.setReceiveBufferSize(1024*1024);
          socket.bind(address);
                    
          Socket connectionSocket = null;
          
          final AtomicInteger packetRecieved = new AtomicInteger(0);
        
          ExecutorService executors = Executors.newFixedThreadPool(10);
          
          logger.info("Mock ussd gateway starts running on port {}", PORT);
          
          while(true)
            {
               try{ 
                    connectionSocket = socket.accept();
                    logger.info("Gateway client connected at port:{}",connectionSocket.getLocalPort());
                    
                    connectionSocket.setReceiveBufferSize(1024*1024);
                    connectionSocket.setSendBufferSize(1024*1024);
                    connectionSocket.setTcpNoDelay(true);
                
                    MockInswitchGatewayClientHandler mockClientHandler = new MockInswitchGatewayClientHandler(connectionSocket,packetRecieved);
                    if(argv.length > 0 ){
                        mockClientHandler.setEnableRandomizer(Boolean.valueOf(argv[0]));
                    }
                    executors.submit(mockClientHandler);
               }catch (IOException e) {
                    logger.error("Could not listen on port:{}" , PORT, e);
                    System.exit(-1);
                }
               
//               Thread thread = new Thread(new ServerConnection(connectionSocket));
//               thread.start();
            
              

            }
      } 
   
    
}
