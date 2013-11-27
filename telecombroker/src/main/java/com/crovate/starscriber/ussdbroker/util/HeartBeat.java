/**
 * Utility functions for handling heartbeat between TCP Server (Consumer part)
 * and clients.
 * 
 * @author umansoor
 */


package com.crovate.starscriber.ussdbroker.util;

import com.crovate.starscriber.ussdbroker.util.wrappers.ProtobufRequestWrapper;
import com.google.protobuf.generated.ProtoBuffRequest;
import com.google.protobuf.generated.ProtoBuffRequest.Request.RequestType;

/**
 * 
 * @author umansoor
 */
public class HeartBeat
{
    /** The heartbeat request event in Protocol Buffer serialized format - This 
     * is synonymous to [CHECK]. However, we cannot send [CHECK] as bytes since
     * the TCP stream is handled by Google Protocol Buffer and it expects 
     * everything to be in that format. */
    private static ProtoBuffRequest.Request heartbeatEvents_REQUEST = null;
    
    /** The Heartbeat acknowledgement from the client */
    private final static String RESPONSE = "[LINE-OK]\r\n";
    
    /**
     * Pre-defined Heartbeat request event in Proto Buffer format. 
     */
    static {
        //RequestHandler requestHandler = new ProtobufRequestWrapper();
        heartbeatEvents_REQUEST = ProtobufRequestWrapper.buildRequest("", "", "", 0, RESPONSE,  RequestType.HEART_BEAT); //"This could be protocol Buffer some standard flag";
        
    }

    /**
     * Tells with certainty if the packet is heartbeat response or not.
     * 
     * @param packet
     * @return true - The packet is heartbeat response
     *         false - The packet is not heartbeat response
     */
    public boolean isHeartbeatResponse(String packet) 
    {
        if (packet.trim().equals(RESPONSE.trim())) 
        {
            return true;
        } else {
            return false;
        }
    }
            
    /**
     * 
     * @return Mobilerequest format heartbeat event.
     */
    public ProtoBuffRequest.Request getHeartbeatRequest() 
    {   
        return heartbeatEvents_REQUEST;
    } 
}
