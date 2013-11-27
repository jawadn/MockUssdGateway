/*
 * Response sender takes response objects from queue and sends it to client
 * 
 */
package com.crovate.starscriber.ussdbroker.service.core.workers;


import com.crovate.starscriber.ussdbroker.abstractgateway.UssdRequest;
import com.crovate.starscriber.ussdbroker.abstractgateway.UssdResponse;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.MessageType;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchDialogPool;
import com.crovate.starscriber.ussdbroker.service.timeout.RecordService;
import com.crovate.starscriber.ussdbroker.service.timeout.RecordServiceImpl;
import com.crovate.starscriber.ussdbroker.util.transformers.ProtobufUssdTransformer;
import com.crovate.starscriber.ussdbroker.util.wrappers.ProcessQueues;
import com.google.protobuf.generated.ProtoBuffResponse.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jawad
 */
public class ResponseSender implements Runnable{

    private static RecordService recordService = RecordServiceImpl.getRecServLocal();
    
    private static final LinkedBlockingQueue<UssdResponse> responseQueue = ProcessQueues.getResponseQueue();
    
    private static final Logger logger = LoggerFactory.getLogger(ResponseSender.class.getName());

    public ResponseSender() {
        
      
    }
    
    public void run(){
        
        Socket socket;
        
        while(!Thread.currentThread().isInterrupted()){
            try {
                UssdResponse ussdResponse = responseQueue.poll(3000, TimeUnit.MILLISECONDS);
                if(ussdResponse != null){
                    UssdRequest ussdRequest =  (UssdRequest)recordService.retrieveRecord(ussdResponse.getDialog().intValue());
                    if(ussdRequest != null){
                        logger.info("Processing Ussd response having dialog id:{0}",ussdResponse.getDialog());

                        
                        socket = ussdRequest.getClientSocket();
                        OutputStream output = socket.getOutputStream();
                        ProtobufUssdTransformer ussdBroker = new ProtobufUssdTransformer();
                        Response response = ussdBroker.toUssdBrokerResponse(ussdResponse);

                        response.writeDelimitedTo(output);


                        if(ussdResponse.getMessageType().equals(MessageType.END) ||
                                ussdResponse.getMessageType().equals(MessageType.ABORT)||
                                ussdResponse.getMessageType().equals(MessageType.ERROR)||
                                ussdResponse.getMessageType().equals(MessageType.EXIT) ||
                                recordService.isUssdRequestTimedOut(ussdResponse.getDialog().intValue())){

                           recordService.removeSessionRecord(ussdRequest.getSessionId());
                           recordService.removeRecord(ussdResponse.getDialog().intValue());
                           InSwitchDialogPool.getInstance().returnDialogToPool(ussdResponse.getDialog().intValue());

                        }
                }
              }
            } catch (IOException ex) {
                logger.error("Exception in ResponseSender Thread", ex);
            }catch (Exception ex) {
                logger.error("Exception in ResponseSender Thread", ex);
            }
        
          
        }
    }
    
    
   
  
    
}
