/*
 * Wrapper class for queues used within the application
 * 
 */
package com.crovate.starscriber.ussdbroker.util.wrappers;

import com.crovate.starscriber.ussdbroker.abstractgateway.UssdRequest;
import com.crovate.starscriber.ussdbroker.abstractgateway.UssdResponse;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Kaunain
 */
public class ProcessQueues {
    
    private static final LinkedBlockingQueue<UssdRequest> requestQueue = new LinkedBlockingQueue<UssdRequest>();
    private static final LinkedBlockingQueue<UssdResponse> responseQueue = new LinkedBlockingQueue<UssdResponse>();

    public static LinkedBlockingQueue<UssdRequest> getRequestQueue() {
        return requestQueue;
    }

    public static LinkedBlockingQueue<UssdResponse> getResponseQueue() {
        return responseQueue;
    }

    
    
 
    
    
    
}
