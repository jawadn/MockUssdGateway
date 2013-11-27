/*
 * Abstract class for ussing as a base class for gateway specific request
 * 
 */
package com.crovate.starscriber.ussdbroker.abstractgateway;


import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.Status;
import java.net.Socket;

/**
 *
 * @author jawad
 */
public abstract class UssdRequest{
    
    private String sessionId;
    private Socket clientSocket;
    private long sessionStartTime;
    private int componentId;
    private boolean close;
    private String requestUUID;
    private String req;
    private Long timeOutMillis;
    private Long initMarker;
    private boolean isTimedOut;
    private Status status;
    private int dialogId; 

    public int getDialogId() {
        return dialogId;
    }

    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }
    
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public long getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(long sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public int getComponentId() {
        return componentId;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public String getRequestUUID() {
        return requestUUID;
    }

    public void setRequestUUID(String requestUUID) {
        this.requestUUID = requestUUID;
    }

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }

    public Long getTimeOutMillis() {
        return timeOutMillis;
    }

    public void setTimeOutMillis(Long timeOutMillis) {
        this.timeOutMillis = timeOutMillis;
    }

    public Long getInitMarker() {
        return initMarker;
    }

    public void setInitMarker(Long initMarker) {
        this.initMarker = initMarker;
    }

    public boolean isIsTimedOut() {
        return isTimedOut;
    }

    public void setIsTimedOut(boolean isTimedOut) {
        this.isTimedOut = isTimedOut;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    
    
    
    
    
    
}
