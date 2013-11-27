/*
 * Interface for sending and recieving messages to and from gateway
 * for using as a base for gateway specific implementations
 * 
 */
package com.crovate.starscriber.ussdbroker.abstractgateway;


import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.MessageType;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author jawad
 */
public interface GatewayMessageTranciever {
    
    public void pingGateWay(OutputStream output,MessageType type);
    
   public void sendRequest(UssdRequest request,OutputStream output);
    
    public UssdResponse getResponse(InputStream inputStream);
    
public void bindToGateway(OutputStream output);
    
}
