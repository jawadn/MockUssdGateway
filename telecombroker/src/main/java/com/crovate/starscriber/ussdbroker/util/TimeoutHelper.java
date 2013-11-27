/*
 * Timeout helper class houses basic method for checking individual request
 * 
 */
package com.crovate.starscriber.ussdbroker.util;


import com.crovate.starscriber.ussdbroker.abstractgateway.UssdRequest;
import com.crovate.starscriber.ussdbroker.abstractgateway.UssdResponse;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdMessageTranciever;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdRequest;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdResponse;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author kaunain
 */
public class TimeoutHelper 
{

    private static Long defacto_timeout=10000l;
    
    private static Logger logger = LoggerFactory.getLogger(TimeoutHelper.class);

    public static void setDefacto_timeout(Long defacto_timeout) {
        TimeoutHelper.defacto_timeout = defacto_timeout;
    }
    
    public static Long getDefacto_timeout() {
        return defacto_timeout;
    }
    
    public boolean isRequestTimedout(UssdRequest request)
    {
        
        Long currentTime=System.currentTimeMillis();
        boolean flag=false;
        
        if(request!=null)
        {
            
            Long initiatedTime = request.getInitMarker();
            Long timeOut = request.getTimeOutMillis();
            Long currentMarker = (long)currentTime-initiatedTime;
            if(timeOut == 0)
                timeOut=defacto_timeout;
            
            
            if(currentMarker >= timeOut)
            {     
                 flag=true;
                 
                 return flag;
                
            }    
            
        
        }else{
            logger.info("request is null so will not be considered for timeout");
        }
        
        return flag;
        
    
    }
    
    
    public UssdResponse createTimeoutResponse(int dialogId, String message, MessageType messageType,boolean isTimeout){
    
        InSwitchUssdResponse ussdResponse = new InSwitchUssdResponse();
        ussdResponse.setDialogId(dialogId);
        ussdResponse.setMessageType(messageType);
        ussdResponse.setTimeout(isTimeout);
        return ussdResponse;
    }
    
    public UssdRequest createTimeoutRequest(Integer dialogId, MessageType messageType)
    {
         String abortValue = "1";
        InSwitchUssdRequest timeoutRequest = new InSwitchUssdRequest();
          timeoutRequest.setMessageType(MessageType.ABORT);
                    timeoutRequest.setDialogId(dialogId);
                    timeoutRequest.setUserId(InSwitchUssdMessageTranciever.getUserId());
                    timeoutRequest.setAbortValue(abortValue);
                    timeoutRequest.setClose(true);
    
     return timeoutRequest;
    }
    
    
    
}
