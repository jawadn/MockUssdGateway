/*
 * Thread which takes request from request queue and sends to gateway via 
 * Gatewaymessagetranciever
 * 
 */
package com.crovate.starscriber.ussdbroker.service.core.workers;


import com.crovate.starscriber.ussdbroker.service.core.GatewayClient;
import com.crovate.starscriber.ussdbroker.abstractgateway.GatewayMessageTranciever;
import com.crovate.starscriber.ussdbroker.abstractgateway.UssdRequest;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchDialogPool;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdMessageTranciever;
import com.crovate.starscriber.ussdbroker.service.timeout.RecordService;
import com.crovate.starscriber.ussdbroker.service.timeout.RecordServiceImpl;
import com.crovate.starscriber.ussdbroker.util.wrappers.ProcessQueues;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author jawad
 */
public class UssdRequestDelegator implements Callable<Boolean>{

    private static final LinkedBlockingQueue<UssdRequest> centralizedQueue=ProcessQueues.getRequestQueue();
    
    private static RecordService recordService=RecordServiceImpl.getRecServLocal();
    
    private Socket socket;
    
    private static final Logger logger =  LoggerFactory.getLogger(UssdRequestDelegator.class.getName());
    

    public UssdRequestDelegator(Socket socket) {
     
        this.socket = socket;
        
        
    }

    public Boolean call() throws IOException {
        
        UssdRequest request = null;
        
        
        try {
            
            GatewayMessageTranciever messageTranciever = new InSwitchUssdMessageTranciever();
      
            while(true){
//                               
                /*
                 * Using queue.poll with timeout of 3 seconds so the thread won't get block for
                 * indefinete period of time.
                 * 
                 */
                request = centralizedQueue.poll(3000,TimeUnit.MILLISECONDS);
                
               
               
                if(request != null){
                    
                    //Must execute if request not timed out and 
                    
                    if( recordService.hasUssdSession(request.getDialogId()) && !recordService.isUssdRequestTimedOut(request.getDialogId()))
                    {

                        messageTranciever.sendRequest(request,socket.getOutputStream());
                        
                        UssdRequest ussdRequest =(UssdRequest) recordService.retrieveRecord(request.getDialogId());
                        if(ussdRequest != null && ussdRequest.isClose() ){
                            logger.info("Session cleaning for dialog id: ", request.getDialogId());
                            recordService.removeSessionRecord(ussdRequest.getSessionId());
                            recordService.removeRecord(request.getDialogId());
                            InSwitchDialogPool.getInstance().returnDialogToPool(request.getDialogId());

                        }
                     }
                  }
               }
           } catch (IOException ex) { 
                logger.error("Exception in UssdRequestSubmitter", ex);
                 
        } catch (Exception ex) { 
                logger.error("Exception in UssdRequestSubmitter", ex);
                 
        }finally{
              
                 if(socket != null){
                    logger.info("Closing the socket in UssdRequestSubmitter");
                    socket.shutdownInput();
                    socket.shutdownOutput();
                 }
                GatewayClient.setNumberOfRunningThreads(GatewayClient.getNumberOfRunningThreads()- 1);
        }
        
        return false;
     
    }
                
}