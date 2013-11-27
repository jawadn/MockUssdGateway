/*
 * Constructs/Transforms UssdRequest to protobuf object and vise versa
 * 
 * 
 */
package com.crovate.starscriber.ussdbroker.util.transformers;


import com.crovate.starscriber.ussdbroker.abstractgateway.GatewayMessageTranciever;
import com.crovate.starscriber.ussdbroker.abstractgateway.UssdRequest;
import com.crovate.starscriber.ussdbroker.abstractgateway.UssdResponse;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.ComponentOP;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.ComponentType;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.MapOperation;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.MessageType;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.Status;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchDialogPool;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdGateWayConstants;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdMessageTranciever;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdRequest;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdResponse;
import com.crovate.starscriber.ussdbroker.service.timeout.RecordService;
import com.crovate.starscriber.ussdbroker.service.timeout.RecordServiceImpl;
import com.crovate.starscriber.ussdbroker.util.StringUtil;
import com.crovate.starscriber.ussdbroker.util.wrappers.ProtobufResponseWrapper;
import com.google.protobuf.generated.ProtoBuffRequest.Request;
import com.google.protobuf.generated.ProtoBuffResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jawad
 */
public class ProtobufUssdTransformer {
    
    
    private Long defaultTimeouta=10000l;
   
    private String abortValue = "1";
    private final static Logger logger = Logger.getLogger(ProtobufUssdTransformer.class.getName());
     
    private final InSwitchDialogPool dialogPool = InSwitchDialogPool.getInstance();
    
    private static RecordService recordService=RecordServiceImpl.getRecServLocal();
    
    public UssdRequest createUssdRequest(Request request, GatewayMessageTranciever ussdGateway) throws Exception{
         
        Long currentTimemillis=System.currentTimeMillis();
        InSwitchUssdMessageTranciever inSwitchUssdGateway = null;
        
        InSwitchUssdRequest inSwitchRequest = new InSwitchUssdRequest();
        
        String sessionId = request.getId();
        Integer dialogId = null;
                
        UssdRequest ussdRequestOld = null;
        
        try {
            inSwitchUssdGateway = (InSwitchUssdMessageTranciever) ussdGateway;
           // inSwitchRequest = inSwitchUssdGateway.toUssdRequest(request);
            inSwitchRequest.setUserId(InSwitchUssdMessageTranciever.getUserId());
          
            inSwitchRequest.setUssdString(StringUtil.convertStringToHex(request.getMessage()).toUpperCase());
            
           switch (request.getType()){
               
               case BEGIN:
                   //sessionId = getUniqueSessionId();
                   dialogId = dialogPool.getDialog();
                   //sessionDialogMap.put(sessionId, dialogId);
                   recordService.addSessionRecord(sessionId, dialogId);
                   System.out.println("----Dialog id---- "+ dialogId +" ********* is assigned to session id ****** "+sessionId);
                   inSwitchRequest.setMessageType(MessageType.BEGIN);
                   inSwitchRequest.setDialogId(dialogId);
                   inSwitchRequest.setDestinationId(String.valueOf(1));
                   inSwitchRequest.setDestinationNumber(request.getCp());
                   inSwitchRequest.setDestinationNai(InSwitchUssdGateWayConstants.NAI);
                   inSwitchRequest.setDestinationNpi(InSwitchUssdGateWayConstants.NPI);
                   
                   inSwitchRequest.setOriginationNumber(InSwitchUssdGateWayConstants.ORIGINATION_NUMBER);
                   inSwitchRequest.setOriginationNai(InSwitchUssdGateWayConstants.NAI);
                   inSwitchRequest.setOriginationNpi(InSwitchUssdGateWayConstants.NPI);
                   
                   inSwitchRequest.setUserInfoMapOperation(MapOperation.OPEN.getValue());
                   inSwitchRequest.setUserInfObjectIdentifier(InSwitchUssdGateWayConstants.OBJECT_IDENTIFIER);
                   inSwitchRequest.setContextValue(InSwitchUssdGateWayConstants.CONTEXT);
                   inSwitchRequest.setComponentType(ComponentType.INVOKE.getValue());
                   inSwitchRequest.setComponentOp(ComponentOP.USSD_REQUEST.getValue());
                   inSwitchRequest.setDataCodingScheme(InSwitchUssdGateWayConstants.DATA_CODING_SCHEME);
                   inSwitchRequest.setMsIsdnNumber(request.getCp());
                   inSwitchRequest.setMsIsdnNai(InSwitchUssdGateWayConstants.NAI);
                   inSwitchRequest.setMsIsdnNpi(InSwitchUssdGateWayConstants.NPI);
                   inSwitchRequest.setInitMarker(currentTimemillis);
                   inSwitchRequest.setTimeOutMillis(defaultTimeouta);
                   inSwitchRequest.setStatus(Status.BEGIN);
//                   clientConnection = new ClientConnection();
//                   clientConnection.setSessionId(sessionId);
//                   clientConnection.setComponentId(0);
                  
                   inSwitchRequest.setSessionId(sessionId);
                   inSwitchRequest.setComponentId(0);
                   recordService.addRecord(dialogId, inSwitchRequest);
                   break;
            
               case CONTINUE:
                   if((dialogId = recordService.retrieveSessionRecord(request.getId())) == null){
                        throw new Exception("Invalid request");
                   }
                   ussdRequestOld = (UssdRequest)recordService.retrieveRecord(dialogId);
                   inSwitchRequest.setDialogId(dialogId);
                   inSwitchRequest.setMessageType(MessageType.CONTINUE);
                   inSwitchRequest.setComponentId(ussdRequestOld.getComponentId() + 1);
                   inSwitchRequest.setComponentType(ComponentType.INVOKE.getValue());
                   inSwitchRequest.setComponentOp(ComponentOP.USSD_REQUEST.getValue());
                   inSwitchRequest.setDataCodingScheme(InSwitchUssdGateWayConstants.DATA_CODING_SCHEME);
                   inSwitchRequest.setSessionId(sessionId);
                   inSwitchRequest.setInitMarker(currentTimemillis);
                   inSwitchRequest.setTimeOutMillis(defaultTimeouta);
                   inSwitchRequest.setStatus(Status.CONTINUE);
                   //clientConnection.setComponentId(clientConnection.getComponentId()+1);
                   break;
               
               case END:
                   if((dialogId = recordService.retrieveSessionRecord(request.getId())) == null){
                        throw new Exception("Invalid request");
                   }
                   ussdRequestOld = (UssdRequest)recordService.retrieveRecord(dialogId);
                   inSwitchRequest.setDialogId(dialogId);
                   inSwitchRequest.setMessageType(MessageType.CONTINUE);
                   inSwitchRequest.setUserInfoMapOperation(MapOperation.CLOSE.getValue());
                   inSwitchRequest.setUserInfObjectIdentifier(InSwitchUssdGateWayConstants.OBJECT_IDENTIFIER);
                   inSwitchRequest.setComponentId(ussdRequestOld.getComponentId() + 1);
                   inSwitchRequest.setComponentType(ComponentType.INVOKE.getValue());
                   inSwitchRequest.setComponentOp(ComponentOP.UNSTRUCTURED_SS_NOTIFY.getValue());
                   inSwitchRequest.setDataCodingScheme(InSwitchUssdGateWayConstants.DATA_CODING_SCHEME);
                   inSwitchRequest.setSessionId(sessionId);
                   inSwitchRequest.setInitMarker(currentTimemillis);
                   inSwitchRequest.setTimeOutMillis(defaultTimeouta);
                   inSwitchRequest.setStatus(Status.END);
                   //clientConnection.setComponentId(clientConnection.getComponentId()+1);
                   recordService.addRecord(dialogId, inSwitchRequest);
                   
                   break;
                   
               case CLOSE:
                    if((dialogId = recordService.retrieveSessionRecord(request.getId())) == null){
                        throw new Exception("Invalid request");
                    }
                    ussdRequestOld = (UssdRequest)recordService.retrieveRecord(dialogId);
                    inSwitchRequest.setMessageType(MessageType.END);
                    inSwitchRequest.setSessionId(sessionId);
                    inSwitchRequest.setDialogId(dialogId);
                    inSwitchRequest.setUserId(InSwitchUssdMessageTranciever.getUserId());
                    inSwitchRequest.setClose(true);
                    inSwitchRequest.setInitMarker(currentTimemillis);
                    inSwitchRequest.setTimeOutMillis(defaultTimeouta);
                    inSwitchRequest.setStatus(Status.END);
                    recordService.addRecord(dialogId, inSwitchRequest);
               default:
                   
           }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE,null,e);
        }
          
          return  inSwitchRequest;
      }
     
    public ProtoBuffResponse.Response toUssdBrokerResponse(UssdResponse ussdResponse){
        
        ProtoBuffResponse.Response response = null;  
        
        try {
                InSwitchUssdResponse inSwitchResponse = (InSwitchUssdResponse) ussdResponse;
                
                UssdRequest ussdRequest = (UssdRequest)recordService.retrieveRecord(ussdResponse.getDialog().intValue());
        
                boolean isError = false;
              
                
                String message = "";
                
                if(inSwitchResponse.getUssdString() != null){
                    message = StringUtil.convertHexToString(inSwitchResponse.getUssdString());
                }
                
                switch (ussdResponse.getMessageType()){
                    
                    case ABORT:
                        message = "Request cannot be completed. User Aborted";
                        isError = true;
                        break;
                        
                    case ERROR:
                        message = "Request cannot be completed. Error occured";
                        isError = true;
                        break;
                        
                    case END:
                        message = "Request cannot be completed. Request Ended";
                        isError = true;
                        break;
                        
                        
                }
                
                response =  ProtobufResponseWrapper.buildResponse(ussdRequest.getSessionId(), message , isError,ussdResponse.isTimeout());
             } catch (Exception e) {
           
                 logger.log(Level.SEVERE,null,e);
        }
          
          return response;
    }

    
}
