/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.service.timeout;



import com.crovate.starscriber.ussdbroker.abstractgateway.UssdRequest;
import com.crovate.starscriber.ussdbroker.util.TimeoutHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author kaunain
 */
public class RecordServiceImpl implements RecordService
{
    
   
    private List<Object> timeOutList;

    //To Keep records of references and their keys
    private static volatile Map<Integer, UssdRequest> requestRegisterMap= new ConcurrentHashMap<Integer, UssdRequest>();
    private static final ConcurrentHashMap<String, Integer> sessionDialogMap = new ConcurrentHashMap<String, Integer>();
    private static volatile Map<Integer, Boolean> ussdTimeOutMap= new ConcurrentHashMap<Integer, Boolean>();
    private static TimeoutHelper timeout=new TimeoutHelper();
    
    private static Logger logger = LoggerFactory.getLogger(RecordServiceImpl.class);
    
    private static final RecordServiceImpl recServLocal=new RecordServiceImpl();
    
    private RecordServiceImpl()
    {
        
        timeOutList = new ArrayList<Object>();
    }
    
    public static Map<Integer, UssdRequest> getCmap() {
        
        return requestRegisterMap;
    }
    
    public List<Object> getTimeOutList() {
        
        return timeOutList;
    }
    
    public void setTimeOutList(ArrayList<Object> timeOutList) {
        
        this.timeOutList = timeOutList;
    }
    
    public static Map<Integer, UssdRequest> getRequestRegisterMap() {
        return requestRegisterMap;
    }
    
    
    public static Map<Integer, Boolean> getUssdTimeOutMap() {
        return ussdTimeOutMap;
    }
    
    @Override
    public void addRecord(Integer dialogid, Object obj)
    {
        
        UssdRequest request=(UssdRequest)obj;
        
        requestRegisterMap.put(dialogid, request);
        ussdTimeOutMap.put(dialogid, Boolean.FALSE);
        
        
    }
    
    
    @Override
    public Integer retrieveSessionRecord(String key)
    {
        Integer dialogid = sessionDialogMap.get(key);
        
        return dialogid;
    }
    
    
    public void addSessionRecord(String key, Integer id) {
        
        sessionDialogMap.put(key,id);
    }
    
    
    public void removeSessionRecord(String sessionId)
    {
        if(sessionDialogMap.contains(sessionId))
            sessionDialogMap.remove(sessionId);
        
    }
    
    
    
    @Override
    public Object retrieveRecord(Integer key)
    {
        Object obj=requestRegisterMap.get(key);
        return obj;
    }
    
    
    
    @Override
    public Object removeRecord(Integer key)
    {
        UssdRequest obj=null;
        if(requestRegisterMap.containsKey(key))
        {
            obj=(UssdRequest)requestRegisterMap.remove(key);
            //clientTimeOutMap.remove(key);
            ussdTimeOutMap.remove(key);
            logger.info("Record unregistered with the following key :"+ key);
        }
        
        return obj;
        
    }
    
    public boolean hasUssdSession(Integer key)
    {
        boolean flag = ussdTimeOutMap.containsKey(key);
        return flag;
    }
    
    @Override
    public boolean hasSession(String key)
    {
        
        boolean flag = sessionDialogMap.contains(key);
        return flag;
    }
    
    
    @Override
    public List<Object> getTimedOutRequests()
    {
        
        if(!requestRegisterMap.isEmpty())
        {
            
            Iterator<Integer> iter=requestRegisterMap.keySet().iterator();
            while(iter.hasNext())
            {
                Integer currentKey=iter.next();
                UssdRequest currRequest=(UssdRequest)requestRegisterMap.get(currentKey);
                boolean responseStatus=ussdTimeOutMap.get(currentKey);
                
                if(!responseStatus)
                {
                    if(!currRequest.isIsTimedOut())
                    {
                        if(timeout.isRequestTimedout(currRequest))
                        {
                            
                            timeOutList.add(currRequest);
                            
                        }
                    }
                }
            }
        }
        
        
        return timeOutList;
    }
    
    
    
    public void updateRequestStatus(Integer key, boolean isRespReceived)
    {
        
        if(key!=null)
        {
            
            if(ussdTimeOutMap.containsKey(key))
            {
                
                ussdTimeOutMap.put(key, isRespReceived);
                
            }
            
        }
        
    }
    
    public static RecordServiceImpl getRecServLocal()
    {
        return recServLocal;
        
        
    }
    
    
    @Override
    public boolean isUssdRequestTimedOut(Integer dialogId)
    {
        
        boolean ussdTimedOut=ussdTimeOutMap.get(dialogId);
        return ussdTimedOut;
        
    }
    
    public static ConcurrentHashMap<String, Integer> getSessionDialogMap() {
        return sessionDialogMap;
    }
    
    
    
    
}
