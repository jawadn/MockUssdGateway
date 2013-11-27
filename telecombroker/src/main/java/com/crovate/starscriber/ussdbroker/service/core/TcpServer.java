/**
 * <ul>
 * <li>Starts a TCP Server on the specified port capable of accepting multiple clients.</li>
 * <li>Each connected client gets its own {@link com.starscriber.smg3.ptd.consumer.TcpServerTask} thread after connecting.</li>
 * </ul>
 * 
 * @author umansoor
 */

package com.crovate.starscriber.ussdbroker.service.core;




import com.crovate.starscriber.ussdbroker.service.core.workers.TcpServerTask;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.crovate.starscriber.ussdbroker.util.IdentifiableCallable;
import com.crovate.starscriber.ussdbroker.util.StringUtil;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author umansoor
 */

public class TcpServer implements IdentifiableCallable<Boolean>
{
 
    private final int port;
    private ServerSocket serverSocket;
    private volatile boolean cancelled = false;
    /**
     * signal to the caller once <code>TcpServerTask</code> is running
     */
    private final CountDownLatch tcpServerStartSignal;
    private final int MAX_CLIENTS = 10;
    private static Logger logger = LoggerFactory.getLogger(TcpServer.class.getSimpleName());
  
    /**
     * Constructor
     * 
     * @param p PTD port
     * @param q queue
     * @param l countdownlatch
     */
    public TcpServer(int p, CountDownLatch l)
    {
        port = p;
        
        tcpServerStartSignal = l;
        
    }  
    
    /**
     * Not using this at the moment.
     * 
     * @return id 
     */
    @Override
    public synchronized byte getId()
    {
        return 0;
    }
    
    @Override
    public RunnableFuture<Boolean> newTask()
    {
        return new FutureTask<Boolean>(this) {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                TcpServer.this.cancelTask();
                return super.cancel(mayInterruptIfRunning);
            }
        };
    }
    
    /**
     * Non-standard cancellation to get around blocking nature of Socket.accept()
     */
    @Override
    public synchronized void cancelTask() 
    {
        cancelled = true;

        if(serverSocket != null){
            try {
                logger.debug("Cancellation requested. Closing socket...");
                serverSocket.close();
            } catch (IOException ioe) {
                logger.error("closing socket {}", ioe);
            }
        }

    }
 
    /**
     * Start TCP Server and wait for clients to connect.
     * 
     * @return true - TCP Server has been safely shutdown.
     *         false - unable to start TCP server
     * @throws Exception
     */
    @Override
    public Boolean call() throws IOException
    {
        try {
             serverSocket = new ServerSocket(port);
             logger.info("Server is running on {}, waiting for clients...", port);

             tcpServerStartSignal.countDown(); // Signal that TCP server is running

            ExecutorService clientSocketExecutor = Executors.newFixedThreadPool(MAX_CLIENTS);

            while (!cancelled) {
                try {
                    Socket client = serverSocket.accept();
                    logger.info("A client is accepted: {}", StringUtil.getReadableAddress(client.getRemoteSocketAddress()));
                    clientSocketExecutor.submit(new TcpServerTask(client));


                } catch (SocketException se) {
                    logger.error("Socket exception: {} ", se);
                } catch (IOException ioe) {
                    logger.error("IOException: {} ", ioe);
                } 
            }

            /** Consume the interruption status of the thread if it was requested. */
            if (Thread.currentThread().isInterrupted()) {
                Thread.interrupted(); //Swallow the interruption
            }

            clientSocketExecutor.shutdownNow();

            boolean closed = false;
            try {
                closed = clientSocketExecutor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) { /** Ignored because exiting **/ }

            if (closed) {
                logger.info("TcpServerTasks executor was sucessfully shutdown");
            } else {
                logger.error("TcpServerTasks executor could not be shutdown");
            }

            logger.info("TcpServer is over");
        
         } catch (Exception e) {
            logger.error("unable to start tcp server on {}. Exception {}", port, e.toString());
            return false;
        }
        return true;
    }
}
