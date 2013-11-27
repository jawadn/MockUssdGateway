/*
 * Delegates timeout notifications to client and gateway respectively,
 * by putting into respective queues, runs on frequent interval to sync with
 * timeout notification
 * 
 */
package com.crovate.starscriber.ussdbroker.service.timeout;


import com.crovate.starscriber.ussdbroker.abstractgateway.UssdRequest;
import com.crovate.starscriber.ussdbroker.abstractgateway.UssdResponse;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.MessageType;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.Status;
import com.crovate.starscriber.ussdbroker.service.Service;
import com.crovate.starscriber.ussdbroker.util.TimeoutHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kaunain
 */

public class TimeoutNotificationServiceImpl implements Service,Runnable
{

    private boolean terminated=false;
    private Long sleepInterval=12000l;
    
    private static Logger logger = LoggerFactory.getLogger(TimeoutNotificationServiceImpl.class);
    
    private static final TimeoutNotificationServiceImpl notificationService=new TimeoutNotificationServiceImpl();
    
    private RecordService recordService=RecordServiceImpl.getRecServLocal();
    private BlockingQueue requestQueue;
    private BlockingQueue responseQueue;
    private List<Integer> beginRequests=new ArrayList<Integer>();
    private List<Integer> continueRequests=new ArrayList<Integer>();
    private int conRequests=0;
    private int begRequests=0;
    public TimeoutNotificationServiceImpl()
    {
    
    
    }
    
    public boolean isTerminated() {
        return terminated;
    }

    public void setTerminated(boolean terminated) {
        this.terminated = terminated;
    }

    public Long getSleepInterval() {
        return sleepInterval;
    }

    public void setSleepInterval(Long sleepInterval) {
        this.sleepInterval = sleepInterval;
    }

    public BlockingQueue getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(BlockingQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public BlockingQueue getResponseQueue() {
        return responseQueue;
    }

    public void setResponseQueue(BlockingQueue responseQueue) {
        this.responseQueue = responseQueue;
    }

    public static TimeoutNotificationServiceImpl getNotificationService() {
        return notificationService;
    }

   
    //generalized timeout method updates records for reference
    
    
    public void processBeginRequests(List<Integer> beginRequests)
    {
        
        if(beginRequests!=null && !beginRequests.isEmpty())
        {
    
        Iterator<Integer> iter=beginRequests.iterator();
        while(iter.hasNext())
        {
            try {
                
                int id=iter.next();
                TimeoutHelper helper=new TimeoutHelper();
                UssdResponse response = helper.createTimeoutResponse(id,"", MessageType.ABORT,true);
                responseQueue.put(response);
                begRequests++;
                
            } catch (InterruptedException ex) {
                logger.error("Exception in processBeginRequests()", ex);
                }
            }
        }
    }
    
    public void processContinueEnd(List<Integer> continueRequests)
    {
        
        if(continueRequests!=null && !continueRequests.isEmpty())
        {
    
            Iterator<Integer> iter=continueRequests.iterator();
            while(iter.hasNext())
            {
                try {

                    int id=iter.next();

                    TimeoutHelper helper=new TimeoutHelper();
                    UssdRequest request=helper.createTimeoutRequest(id, MessageType.ABORT);
                    requestQueue.put(request);
                    conRequests++;


                    
                    UssdResponse response = helper.createTimeoutResponse(id,"", MessageType.ABORT,true);
                    responseQueue.put(response);


                } catch (InterruptedException ex) {
                    logger.error("Exception in processContinueEnd", ex);
                    }
                }
        }
    }
    
    public void identifyRequests(List<Object> timedOutRequests)
    {
        
        if(timedOutRequests!=null && !timedOutRequests.isEmpty())
        {
        
            Iterator<Object> iter=timedOutRequests.iterator();
            while(iter.hasNext())
            {
                
                UssdRequest ussdrequest=(UssdRequest)iter.next();
                Status status = ussdrequest.getStatus();
                int requestId = ussdrequest.getDialogId();
                if(status == Status.BEGIN && ussdrequest.isIsTimedOut())
                {
                    
                    beginRequests.add(requestId);
                
                }
                if(status == Status.CONTINUE || status == Status.END && (ussdrequest.isIsTimedOut()))
                {
                        
                        continueRequests.add(requestId);
                
                }
            }
        
        }
    
    }
    
    @Override
    public void run()
    {
    
        while(!terminated)
        {
           logger.info("starting notification service...");
           
            
            try {
                identifyRequests(recordService.getTimedOutRequests());
                processBeginRequests(beginRequests);
                processContinueEnd(continueRequests);
                logger.info("Continue Requests: "+conRequests + "|| Begin Requests :"+ begRequests);

                logger.info("notification service Sleeping for :" + sleepInterval/1000 +"seconds");
                Thread.sleep(sleepInterval);

            } catch (InterruptedException e) {
                           
                logger.error("Exception occured in notification service" + e);

            }
            
        }
        
                 
    
    }
    
    
}

    
    

