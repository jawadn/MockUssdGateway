/*
 * Wrapper built around protobuf requests
 * 
 */
package com.crovate.starscriber.ussdbroker.util.wrappers;

import com.google.protobuf.generated.ProtoBuffRequest;
import com.google.protobuf.generated.ProtoBuffRequest.Request;
import com.google.protobuf.generated.ProtoBuffRequest.Request.RequestType;







/**
 *
 * @author jawad
 */
public class ProtobufRequestWrapper {
    
    public static Request buildRequest(String id,String cp,String op,Integer timeout,String message,RequestType type){
       
       ProtoBuffRequest.Request.Builder request =  ProtoBuffRequest.Request.newBuilder();
       
       request.setId(id);
       request.setCp(cp);
       request.setOp(op);
       request.setTimeout(timeout);
       request.setMessage(message);
       request.setType(type);
       
       return request.build();
       
    }
}
