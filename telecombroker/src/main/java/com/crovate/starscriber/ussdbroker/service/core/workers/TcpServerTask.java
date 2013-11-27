/**
 * Thread for handling a SINGLE TCP Client. It receives Requests from clients via
 * TCP/IP and puts those Requests into the Request Queue.
 * Also checks Tcp heartbeat in another thread and cancels operation if heartbeat misses
 * defined threshold 
 * @author umansoor
 */

package com.crovate.starscriber.ussdbroker.service.core.workers;


import com.crovate.starscriber.ussdbroker.abstractgateway.GatewayMessageTranciever;
import com.crovate.starscriber.ussdbroker.abstractgateway.UssdRequest;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdMessageTranciever;
import com.crovate.starscriber.ussdbroker.service.timeout.RecordService;
import com.crovate.starscriber.ussdbroker.service.timeout.RecordServiceImpl;
import com.crovate.starscriber.ussdbroker.util.HeartBeat;
import com.crovate.starscriber.ussdbroker.util.transformers.ProtobufUssdTransformer;
import com.crovate.starscriber.ussdbroker.util.wrappers.ProcessQueues;
import com.crovate.starscriber.ussdbroker.util.wrappers.ProtobufResponseWrapper;
import com.google.protobuf.generated.ProtoBuffRequest.Request;
import com.google.protobuf.generated.ProtoBuffRequest.Request.RequestType;
import com.google.protobuf.generated.ProtoBuffResponse.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author umansoor
 */

public class TcpServerTask implements Callable<Boolean> 
{
    private final LinkedBlockingQueue<UssdRequest> requestQueue=ProcessQueues.getRequestQueue();
    
    private static RecordService recordService=RecordServiceImpl.getRecServLocal();
            
    private final Socket socket;
    /**
     * This is the time interval in milliseconds this class will submit a
     * heartbeat request *
     */
    private final long heartbeatSendIntervalInMillis = 5000;
    /**
     * Number of missed heartbeat responses after which to take action *
     */
    private final int missedHeartbeatThreshold = 3;
    private static Logger logger = LoggerFactory.getLogger(TcpServerTask.class.getSimpleName());
  
    private Request request = null; 
    private long lastHeartbeatSentInMillis = 0;
   
    /**
     * Constructor
     *
     * @param queue
     * @param SMGClient
     */
    public TcpServerTask( Socket SMGClient) 
    {
        
        this.socket = SMGClient;
        
    }

    /**
     * Keep taking events from the Centralized Queue and send them to the
     * client. Upon exception, the task simply cancels itself.
     *
     * @return not used
     * @throws IOException
     */
    @Override
    public Boolean call() throws IOException 
    {
        final AtomicInteger heartbeatsWithoutAcknowledgement = new AtomicInteger(0);
        final HeartBeat heartbeatUtil = new HeartBeat();
        
        final InputStream input = socket.getInputStream();
         // the actuall call() body
        OutputStream out = socket.getOutputStream();
   
        
        /**
         * Thread for reading input from the client and 
         * if its the hearbeat response then reset the counter 
         * otherwise add the request in the centralized queue for processing.
         */
        Thread requestReaderThread = new Thread(new Runnable() 
        {
    
           @Override
            public void run() 
            {
                try 
                {
                    GatewayMessageTranciever ussdGateway = new InSwitchUssdMessageTranciever();
                    ProtobufUssdTransformer ussdBroker = new ProtobufUssdTransformer();
                    UssdRequest ussdRequest = null;
                    
                   while ((request = Request.parseDelimitedFrom(input)) != null) {
         
                        String data = request.getMessage(); // This can be heartbeat data to test heartbeat;
                        if (heartbeatUtil.isHeartbeatResponse(data)){
                               // logger.info("HearBeat response"+data);
                                // Reset the counter
                                heartbeatsWithoutAcknowledgement.set(0);

                         }else{
                            try {
                               
                               if(request.getType().equals(RequestType.BEGIN) && recordService.hasSession(request.getId())){
                                   logger.error("Invalid request. Session id is duplicate");
                                   Response response =  ProtobufResponseWrapper.buildResponse(request.getId(), "Invalid Request. Session id is duplicate", true,false);
                                    synchronized(socket.getOutputStream()){
                                        response.writeDelimitedTo(socket.getOutputStream());
                                    }
                                   
                               }else{
                                    ussdRequest = ussdBroker.createUssdRequest(request,ussdGateway);
                                    
                                    //ClientConnection clientConnection = clientConnectionMap.get(ussdRequest.getDialog().intValue());
                                    //clientConnection.setClientSocket(socket);
                                    
                                    ussdRequest.setClientSocket(socket);
                                    ussdRequest.setSessionStartTime(System.currentTimeMillis());
                                try{
                                    recordService.addRecord(ussdRequest.getDialogId(), ussdRequest);
                                    requestQueue.put(ussdRequest);
                                   
                                    logger.info("Request added in the queue. Queue size is" + requestQueue.size());
                                    }catch (InterruptedException ex) {
                                        logger.error("Request not added in the queue.", ex);
                                    }
                                }
                            } catch (Exception ex) {
                                
                                logger.error("Invalid request",ex);
                                Response response =  ProtobufResponseWrapper.buildResponse(request.getId(), "Invalid Request", true,false);
                                synchronized(socket.getOutputStream()){
                                    response.writeDelimitedTo(socket.getOutputStream());
                                }
                            }
                            
                        }
        
                   }
                
                } catch (IOException ie) {
                    logger.error("{}", ie.getMessage());
                } 
            }
        }, "Request reader thread for client" + socket.getRemoteSocketAddress());
       
        
        requestReaderThread.start();
        
        /* This main thread will continue to send heartbeat request to client at 
         * regular intervals.
         */

        while (!Thread.currentThread().isInterrupted()) 
        {
            if (System.currentTimeMillis() - lastHeartbeatSentInMillis >= heartbeatSendIntervalInMillis) 
            {
                try {
                    //heartbeatUtil.getHeartbeatRequest().writeDelimitedTo(out);
                      
                      Response response =  ProtobufResponseWrapper.buildResponse("",heartbeatUtil.getHeartbeatRequest().getMessage(),false,false);
                       synchronized(out){
                         response.writeDelimitedTo(out);
                       }
                } catch (IOException ioe) {
                    logger.error("sending heartbeat: {}", ioe.getMessage());
                    break;
                }catch (Exception e) {
                    logger.error("sending heartbeat: {}", e);
                    break;
                               
                } finally {
                    lastHeartbeatSentInMillis = System.currentTimeMillis();
                    heartbeatsWithoutAcknowledgement.set(heartbeatsWithoutAcknowledgement.incrementAndGet()); //count++ for the heartbeat sending
                }
            }
            
            /**
             * If PTD cannot receive any heartbeat response from the PTD client
             * after sending 3 requests, PTD will consider PTD client is timeout
             * and close the connection.
             */
                if (heartbeatsWithoutAcknowledgement.intValue() > missedHeartbeatThreshold) 
                {
                    logger.error("client heartbeat timeout");
                   
                    break;
                }
                
           
           
          } 

      if (socket != null) 
        {
            logger.info("trying to close the socket");
            socket.close();
            
            requestReaderThread.interrupt();
            
        }

        logger.info("exit complete");
        return true;
    }
}
