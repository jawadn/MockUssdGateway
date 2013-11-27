/*
 * Wrapper built around protobuf response
 * 
 */
package com.crovate.starscriber.ussdbroker.util.wrappers;

import com.google.protobuf.generated.ProtoBuffResponse;





/**
 *
 * @author jawad
 */
public class ProtobufResponseWrapper {
    
    public static ProtoBuffResponse.Response buildResponse(String id,String responseString ,boolean error, boolean timeout){
      
        ProtoBuffResponse.Response.Builder response =  ProtoBuffResponse.Response.newBuilder();
       
        response.setId(id);
        response.setResponse(responseString);
        response.setError(error);
        response.setTimeout(timeout);
        return response.build();
    
    }
}
