/**
 * It starts a TCP Server, and is responsible for its life-cycle. This class
 * is thread safe and supports cancellation by interruption.
 * 
 * @author umansoor
 */



package com.crovate.starscriber.ussdbroker.service.core;



import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.crovate.starscriber.ussdbroker.util.CustomFutureReturningExecutor;

public class TcpServerManager implements Runnable {
    
    private final int port;
    
    private final CountDownLatch tcpServerStartSignal;
   

    private static final Logger logger = LoggerFactory.getLogger(TcpServerManager.class.getSimpleName());
    
    /**
     * Constructor
     * 
     * @param q
     * @param p PTD port
     * @param l signal to the caller once <code>TcpServerTask</code> is running
     */
    public TcpServerManager( int p, CountDownLatch l)
    {        
        
        port = p;
        tcpServerStartSignal =  l; 
        
    }
    
    @Override
    public void run() 
    {
        Future<?> tcpServerFuture = null;
        ExecutorService tcpServerExecutor = new CustomFutureReturningExecutor(1,1, Long.MAX_VALUE, TimeUnit.MILLISECONDS,
                                                        new LinkedBlockingQueue<Runnable>());
        
        //Keep checing the status of TcpServer, if it is down, try to reconnect
        while (!Thread.currentThread().isInterrupted()) 
        {
                tcpServerFuture = tcpServerExecutor.submit
                        (new TcpServer(port, tcpServerStartSignal));
              
                try {
                    tcpServerFuture.get();
                    logger.error("tcp server disconnected");
                } catch (CancellationException ce) {
                    // may need to restart if not interrupted. let it go through.
                    logger.error("tcp server", ce);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException ee) {
                    // may need to restart if not interrupted. let it go through.
                     logger.error("tcp server", ee);
                }
        }
         // Get here upon exiting while loop on interruption...
        
        /* 
         * Reset the interrupted status of the thread as we need to call blocking
         * method below. */
        if (Thread.currentThread().isInterrupted()) {
            Thread.interrupted(); //Swallow the interruption
        }
       
        /* 
         * Because we are performing non-standard (non-interrupt) cancellation,
         * we need to call the `cancel(...)` of the FutureTask directly. 
         * `shutdownNow()` simply interrupts the thread which won't work for us.
         */
        if(tcpServerFuture != null)
        {
            tcpServerFuture.cancel(false);
        }
        /*
         * Now shutdown the executor and any remaining threads
         */
        tcpServerExecutor.shutdownNow();
        
        /*
         * Wait for the executor to shut itself down.
         */
        boolean closed = false;
        try {
            closed = tcpServerExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) { 
        
        /** Ignored because exiting **/ 
         logger.error(ignored.getMessage());
        }
        
        if (closed) {
            logger.info("tcpServerExecutor executor was sucessfully shutdown");
        } else {
            logger.error("tcpServerExecutor executor could not be shutdown");
        }
        
        logger.info("UssdBroker Server Manager exits.");
    }
    
    /**
     * 
     * @return string
     */
    @Override 
    public String toString() {
        return ("UssdBroker Server Manager. Using Port " + port);
    }
}
