/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.service.timeout;

import com.crovate.starscriber.ussdbroker.service.Service;
import java.util.List;

/**
 *
 * @author kaunain
 */
public interface RecordService extends Service
{
    
    public Object retrieveRecord(Integer key);
    public Integer retrieveSessionRecord(String key);
    public List<Object> getTimedOutRequests();
    public void addRecord(Integer key,Object obj);
    public void addSessionRecord(String key, Integer id);
    public Object removeRecord(Integer key);
    public void removeSessionRecord(String sessionId);
    public boolean hasUssdSession(Integer key);
    public boolean hasSession(String key);
    public boolean isUssdRequestTimedOut(Integer dialogId);
    public List<Object> getTimeOutList();


}
