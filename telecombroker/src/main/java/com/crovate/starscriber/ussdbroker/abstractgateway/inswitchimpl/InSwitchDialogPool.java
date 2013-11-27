/*
 * 
 * Helper class for managing Inswitch dialog pool
 */
package com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jawad
 */
public class InSwitchDialogPool {
    
  private final static int limit = 16384;
  
  //Singleton
  private static final InSwitchDialogPool dialogPool = new InSwitchDialogPool();
  private final BlockingQueue<Integer> dialogQueue ;
    
    public static InSwitchDialogPool getInstance(){
    
        return dialogPool;
    }

    private InSwitchDialogPool() {
        
        dialogQueue = new ArrayBlockingQueue<Integer>(limit);
        for (int i = 1; i <= limit; i++) {
            dialogQueue.add(i);
        }
    }
    
    public Integer getDialog(){
        
        Integer tempInteger = null;
         
        try {
             tempInteger = dialogQueue.take();
            
          
        }catch (InterruptedException ex) {
                Logger.getLogger(InSwitchDialogPool.class.getName()).log(Level.SEVERE, null, ex);
         
        }  
        Logger.getLogger(InSwitchDialogPool.class.getName()).log(Level.INFO,"Get dialog Id from pool: {0}" , tempInteger);
        return tempInteger;
    }
    
    public void returnDialogToPool(Integer dialog){
        if(!isDialogExist(dialog)){
            dialogQueue.offer(dialog);
            Logger.getLogger(InSwitchDialogPool.class.getName()).log(Level.INFO,"Dialog Id {0} return to pool" , dialog);
        }
    }
    
  
    
    public boolean isDialogExist(Integer dialogId){
        return dialogQueue.contains(dialogId);
    }
    
    public BlockingQueue<Integer> getAllDialogs(){
        return dialogQueue;
    }
    
}
