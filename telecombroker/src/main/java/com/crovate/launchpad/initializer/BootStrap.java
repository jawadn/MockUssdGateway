/*
 * BootStrap class for booting ussd broker, initializes all threads
 * essential to the application
 * 
 */
package com.crovate.launchpad.initializer;

import com.crovate.starscriber.ussdbroker.service.core.GatewayClientManager;
import com.crovate.starscriber.ussdbroker.service.core.TcpServerManager;
import com.crovate.starscriber.ussdbroker.service.core.workers.ResponseSender;
import com.crovate.starscriber.ussdbroker.service.timeout.TimeoutNotificationServiceImpl;
import com.crovate.starscriber.ussdbroker.service.timeout.TimeoutServiceImpl;
import com.crovate.starscriber.ussdbroker.util.wrappers.ProcessQueues;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Kaunain
 */
public class BootStrap {
    
    

    private final static Logger logger = LoggerFactory.getLogger(BootStrap.class.getName());
    
    
     
    public static void bootUssdBroker(String params[])
    {
      
       //Obtain centralized process queues
       LinkedBlockingQueue requestQueue=ProcessQueues.getRequestQueue();
       LinkedBlockingQueue responseQueue=ProcessQueues.getResponseQueue();
       
       
       TimeoutNotificationServiceImpl notificationService=TimeoutNotificationServiceImpl.getNotificationService();
       notificationService.setRequestQueue(requestQueue);
       notificationService.setResponseQueue(responseQueue);
       
       //Countdownlatch for catching server startup failure
       CountDownLatch tcpServerStartSignal = new CountDownLatch(1);
       boolean started;
       
       
      
       
       try {
            
           
           //Tcpserverthread to start ussdbroker server and listen to connections
           Thread tcpServerThread = new Thread(new TcpServerManager( BootStrapRuntime.getDefaultServerPort(), tcpServerStartSignal));
           tcpServerThread.start();
            
            
           //set flag upon server start success or failure
            started= tcpServerStartSignal.await(5, TimeUnit.SECONDS); //wait for at most 5 sec to let TCPServer run
            
                    if (!started)
                    {
                        logger.error("Tcp server failed to start");
                        tcpServerThread.interrupt();
                        System.exit(0);
                    }
            
            else{
                
                
                if(params.length != 0){
                    BootStrapRuntime.setDefaultIp(params[0]);
                }
                
            
            //Initlize ussd client thread for communicating with gateway
            Thread ussdClientThread = new Thread(new GatewayClientManager( BootStrapRuntime.getDefaultIp()));
            ussdClientThread.start();

            //initilize response thread for sending responses to client
            Thread clientResponse = new Thread(new ResponseSender());
            clientResponse.start();

            //initialize timeout thread for timing out requests
            Thread timeoutThread=new Thread(TimeoutServiceImpl.getTimeOutService());
            timeoutThread.start();

            
            //initialize timeout notification thread for sending notifications
            Thread notificationThread=new Thread(notificationService);
            notificationThread.start();
                
            }
        } 
            
        catch (Exception e)
        {
            logger.error("Exception in ServerMain", e);
        }
    }
    
}
