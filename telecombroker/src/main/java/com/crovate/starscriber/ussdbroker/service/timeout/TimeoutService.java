
package com.crovate.starscriber.ussdbroker.service.timeout;

import com.crovate.starscriber.ussdbroker.service.Service;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public interface TimeoutService extends Service, Runnable 
{

    
    public void doTimeOut(List<Object> timeOutRequestList, Map<Integer, Boolean> ussdTimeOutMap);
    
    
}
