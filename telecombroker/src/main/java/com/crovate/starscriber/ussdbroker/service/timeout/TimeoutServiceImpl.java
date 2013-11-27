/*
 * Runs the actual timeout service, timming out all requests having timeout
 * markers lapsing the current system time
 * 
 */
package com.crovate.starscriber.ussdbroker.service.timeout;



import com.crovate.starscriber.ussdbroker.abstractgateway.UssdRequest;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdRequest;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Administrator
 */
public class TimeoutServiceImpl implements TimeoutService 
{

    private boolean terminated = false;
    private Long sleepInterval = 10000l;
    
    private static Logger logger = LoggerFactory.getLogger(TimeoutServiceImpl.class);

    
    private static final TimeoutServiceImpl timeOutService = new TimeoutServiceImpl();
    private RecordService recordService = RecordServiceImpl.getRecServLocal();
  
    int timeOutCount = 0;
    
    public TimeoutServiceImpl()
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

    public static TimeoutServiceImpl getTimeOutService() {
        return timeOutService;
    }

   
    //generalized timeout method updates records for reference
    
    
    @Override
    public void doTimeOut(List<Object> timeOutRequestList, Map<Integer, Boolean> ussdTimeOutMap)
    {   
        
        boolean timeoutswitch=true;
        
        if(timeOutRequestList!=null && timeOutRequestList.size()>0)
        {
            Iterator<Object> iter=timeOutRequestList.iterator();
            while(iter.hasNext())
            {
                
                UssdRequest request=(InSwitchUssdRequest)iter.next();
                request.setIsTimedOut(true);
                //request.setStatus(Status.SESSION_TIMEDOUT);
                
                if(ussdTimeOutMap!=null && !ussdTimeOutMap.isEmpty())
                {
                    timeOutCount++;
                    ussdTimeOutMap.put(request.getDialogId(), timeoutswitch);
                
                }
            }
       
        }
        
    
    }
    
       
    
    @Override
    public void run()
    {
        while(!terminated)
        {
            try {
                
                logger.info("Timeout service Sleeping for :" + sleepInterval/1000 +"seconds");
                Thread.sleep(sleepInterval);
             } catch (InterruptedException e) {
                   logger.error("Exception occured in Timeout service " + e);
              }
            
               logger.info("Starting TimeOut Service...");
               doTimeOut(recordService.getTimedOutRequests(),
               RecordServiceImpl.getUssdTimeOutMap());
               logger.info("Requests Timed Out: "+ timeOutCount);
 
            
        }
        
                 
    
    }
    
    
}
