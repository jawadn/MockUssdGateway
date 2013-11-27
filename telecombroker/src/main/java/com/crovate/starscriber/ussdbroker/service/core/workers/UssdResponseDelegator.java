/*
 * Thread which takes request from gateway via Gatewaymessagetranciever
 * and puts to response queue
 */
package com.crovate.starscriber.ussdbroker.service.core.workers;


import com.crovate.starscriber.ussdbroker.service.core.GatewayClient;
import com.crovate.starscriber.ussdbroker.abstractgateway.GatewayMessageTranciever;
import com.crovate.starscriber.ussdbroker.abstractgateway.UssdResponse;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.MessageType;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdMessageTranciever;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdResponse;
import com.crovate.starscriber.ussdbroker.service.timeout.RecordService;
import com.crovate.starscriber.ussdbroker.service.timeout.RecordServiceImpl;
import com.crovate.starscriber.ussdbroker.util.wrappers.ProcessQueues;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jawad
 */
public class UssdResponseDelegator implements Callable<Boolean>{

    private Socket socket;
    
    private static final LinkedBlockingQueue<UssdResponse> responseQueue = ProcessQueues.getResponseQueue();
    
    private static final Logger logger =  Logger.getLogger(UssdResponseDelegator.class.getName());
    
    private static final RecordService recordService = RecordServiceImpl.getRecServLocal();
    
    
    public UssdResponseDelegator(Socket socket) {
        this.socket = socket;
    
       
    }
    
    public Boolean call() throws IOException {

        UssdResponse ussdResponse = null;
        InSwitchUssdResponse inSwitchResponse;
      
        try {
             //Socket ussdClientSocket = new Socket(InSwitchUssdGateWayConstants.INSWITCH_GATEWAY_IP, InSwitchUssdGateWayConstants.INSWITCH_PORT);
             GatewayMessageTranciever ussdGateway = new InSwitchUssdMessageTranciever();
       
            while(true){
                 
                ussdResponse = ussdGateway.getResponse(socket.getInputStream());
                inSwitchResponse = (InSwitchUssdResponse) ussdResponse;
                
                if(inSwitchResponse != null)
                {
                /*checking if ussd gateway sends a ping message. Then reply "pong" to ussd gateway */
                if(inSwitchResponse.getMessageType() != null && inSwitchResponse.getMessageType().equals(MessageType.PING)){
                    logger.log(Level.INFO, "Ping message recieved from ussd gateway in thread {0}", Thread.currentThread().getName());
                    synchronized(socket.getOutputStream()){
                        ussdGateway.pingGateWay(socket.getOutputStream(),MessageType.PONG);
                    }
                    

                }else if( recordService.hasUssdSession(inSwitchResponse.getDialog().intValue()) && !recordService.isUssdRequestTimedOut(inSwitchResponse.getDialog().intValue()))
                {
                
                   logger.log(Level.INFO, "Response recieved at server of dialog id: {0}", inSwitchResponse.getDialog().intValue());
                    try{
                        responseQueue.put(inSwitchResponse);
                        logger.log(Level.INFO,"Response is added in the result queue. Queue size is {0}" , responseQueue.size());
                    } catch (InterruptedException ex) {
                        logger.log(Level.SEVERE, "Response could not be added in the result queue. Operation is interuppted", ex);
                    }
                
                }
                
                }

            }
        } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
        } catch (Exception ex) { 
                 logger.log(Level.SEVERE, null, ex);
                 
        }finally{
            
           if(socket != null){
                logger.info("Closing the socket in UssdResponseReceiver");
                socket.shutdownInput();
                socket.shutdownOutput();
            }
               GatewayClient.setNumberOfRunningThreads(GatewayClient.getNumberOfRunningThreads()- 1);
        }
        return false;
      
     }       
    
}
