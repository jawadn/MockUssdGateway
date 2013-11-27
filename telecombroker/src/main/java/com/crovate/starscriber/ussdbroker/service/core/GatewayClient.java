/*
 * Bind with gateway and initates worker threads (request delegator and response delegator)
 * to executor, monitors the worker threads and resubmit on thread death
 * 
 */
package com.crovate.starscriber.ussdbroker.service.core;




import com.crovate.starscriber.ussdbroker.abstractgateway.GatewayMessageTranciever;
import com.crovate.starscriber.ussdbroker.abstractgateway.UssdResponse;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.MessageType;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdGateWayConstants;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdMessageTranciever;
import com.crovate.starscriber.ussdbroker.service.core.workers.UssdRequestDelegator;
import com.crovate.starscriber.ussdbroker.service.core.workers.UssdResponseDelegator;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jawad
 */
public class GatewayClient implements Callable{
    
    
    
    private static final Logger logger =  LoggerFactory.getLogger(GatewayClient.class.getName());
    
   
    //
    private static int numberOfRunningThreads = 0;
    //
    private final String gatewayIP;
    
 
    
    public GatewayClient( String ipAddress) {
     
        this.gatewayIP = ipAddress;
    }
    
    @Override
    public Boolean call() throws IOException{
        
         int maxNumberOfThreads = 10;
         long lastPingSentInMillis = 0;
         long pingSendIntervalInMillis = 600000000;
        
        Socket ussdClientSocket = null;
        
        ExecutorService  requestSubmitExecutor = Executors.newFixedThreadPool(maxNumberOfThreads);
        ExecutorService  responseRecieveExecutor = Executors.newFixedThreadPool(maxNumberOfThreads);
   
        try {
            
           GatewayMessageTranciever messageTranciever = new InSwitchUssdMessageTranciever();
            
            ussdClientSocket = new Socket(gatewayIP, InSwitchUssdGateWayConstants.INSWITCH_PORT);
        
            
            messageTranciever.bindToGateway(ussdClientSocket.getOutputStream());
            
           
            for (int i = 0; i < maxNumberOfThreads; i++) {
                
                Socket ussdSocket = new Socket(gatewayIP, InSwitchUssdGateWayConstants.INSWITCH_PORT);
               
                requestSubmitExecutor.submit(new UssdRequestDelegator(ussdSocket ));
                responseRecieveExecutor.submit(new UssdResponseDelegator(ussdSocket ));
                numberOfRunningThreads++;
                    
            }
            
            UssdResponse inSwitchResponse;
                      
            while (!Thread.currentThread().isInterrupted()) {
                
                /*
                 * The current thread will keep on sending ping request to gateway at regular intervals.
                 * It also reply to gateway in case recieves a ping request from it.
                 */
                 try {
                    
                       inSwitchResponse = messageTranciever.getResponse(ussdClientSocket.getInputStream());

                         /*checking if ussd gateway sends a ping message. Then reply "pong" to ussd gateway */
                        if(inSwitchResponse.getMessageType().equals(MessageType.PING)){
                            logger.debug("Ping message recieved from ussd gateway in thread {0}", Thread.currentThread().getName());
                             messageTranciever.pingGateWay(ussdClientSocket.getOutputStream(),MessageType.PONG);
                             lastPingSentInMillis = System.currentTimeMillis();

                        }else if(inSwitchResponse.getMessageType().equals(MessageType.BIND_CONFIRM)){

                            InSwitchUssdMessageTranciever.setUserId(inSwitchResponse.getUserId());
                            InSwitchUssdMessageTranciever.setStackId(inSwitchResponse.getStackId());

                        }

                        if (System.currentTimeMillis() - lastPingSentInMillis >= pingSendIntervalInMillis){

                               messageTranciever.pingGateWay(ussdClientSocket.getOutputStream(),MessageType.PING);

                               lastPingSentInMillis = System.currentTimeMillis(); 

                               inSwitchResponse = messageTranciever.getResponse(ussdClientSocket.getInputStream());

                               if(!inSwitchResponse.getMessageType().equals(MessageType.PONG)){
                                     logger.info("Ussd gateway is not responding");
                               }
                           }
                        
                        
                        /* If at anytime thread dies or socket connection breaks 
                         * then a new socket will be added in the executor*/
                        if(numberOfRunningThreads < maxNumberOfThreads){

                           Socket ussdSocket = new Socket(gatewayIP, InSwitchUssdGateWayConstants.INSWITCH_PORT);

                           requestSubmitExecutor.submit(new UssdRequestDelegator(ussdSocket ));
                           
                           responseRecieveExecutor.submit(new UssdResponseDelegator(ussdSocket ));
                           
                           numberOfRunningThreads++;
                       }
                        
                        
                
                 } catch (SocketException e) {
                     logger.error("SocketException in UssdClient", e);
                     break;
                 }

             }
         }catch(Exception e){
                logger.error("Exception in UssdClient", e);
          }
        
        
        if (ussdClientSocket != null) 
        {
            try {
                logger.info("Broker has lost the connection with gateway.");
                ussdClientSocket.close();
                
                requestSubmitExecutor.shutdown();
                responseRecieveExecutor.shutdown();
            } catch (IOException ex) {
                logger.error("", ex);
            }
            
        }

        logger.info("UssdClient exits");
        
        return true;
       
    }

    public static int getNumberOfRunningThreads() {
        return numberOfRunningThreads;
    }

    public static void setNumberOfRunningThreads(int numberOfRunningThreads) {
        GatewayClient.numberOfRunningThreads = numberOfRunningThreads;
    }

  
   
   
}
