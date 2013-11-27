/*
 * 
 * Manages the gatewayclient restarts gatewayclient on thread death
 * 
 */
package com.crovate.starscriber.ussdbroker.service.core;



import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author jawad
 */
public class GatewayClientManager implements Runnable{
    
  
    
   
    
    private static final Logger logger =  LoggerFactory.getLogger(GatewayClientManager.class.getName());
        
    private final String gatewayIP;
    

    public GatewayClientManager( String gatewayIP) {
        
        this.gatewayIP = gatewayIP;
    }
    
      
    public void run() {
        
        
        Future<?> ussdClientFuture = null;
        ExecutorService gatewayClientExecutor = Executors.newFixedThreadPool(1);
        
        //Keep checing the status of TcpServer, if it is down, try to reconnect
        while (!Thread.currentThread().isInterrupted()) 
        {
                ussdClientFuture = gatewayClientExecutor.submit(new GatewayClient( gatewayIP));
              
                try {
                    ussdClientFuture.get();
                    logger.error("Ussd Broker disconnected from gateway");
                } catch (CancellationException ce) {
                    // may need to restart if not interrupted. let it go through.
                    logger.error("Ussd Broker disconnected from gateway", ce);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException ee) {
                    // may need to restart if not interrupted. let it go through.
                }
        }
        /* 
         * Because we are performing non-standard (non-interrupt) cancellation,
         * we need to call the `cancel(...)` of the FutureTask directly. 
         * `shutdownNow()` simply interrupts the thread which won't work for us.
         */
        if(ussdClientFuture != null)
        {
            ussdClientFuture.cancel(false);
        }
        /*
         * Now shutdown the executor and any remaining threads
         */
        gatewayClientExecutor.shutdownNow();
        
          /*
         * Wait for the executor to shut itself down.
         */
        boolean closed = false;
        try {
            closed = gatewayClientExecutor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
                    
                logger.error(ignored.getMessage());
        
        /** Ignored because exiting **/ }
        
        if (closed) {
            logger.info("GatewayClientManager executor was sucessfully shutdown");
        } else {
            logger.error("GatewayClientManager executor could not be shutdown");
        }
        
        logger.info("GatewayClientManager Manager exits.");
        
    }
    
   
 
    
}
