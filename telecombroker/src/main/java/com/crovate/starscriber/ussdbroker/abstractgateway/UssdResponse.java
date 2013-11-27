/*
 * Abstract class used as a base class for gateway specific response
 * 
 */
package com.crovate.starscriber.ussdbroker.abstractgateway;

import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.MessageType;



/**
 *
 * @author jawad
 */
public abstract class UssdResponse {
    
    private boolean timeout;
    private Integer stackId; 
    private Integer userId;

    public boolean isTimeout() {
        return timeout;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }
    
    public abstract Long getDialog();
    
    public abstract MessageType getMessageType();

    public Integer getStackId() {
        return stackId;
    }

    public void setStackId(Integer stackId) {
        this.stackId = stackId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    
    
}
