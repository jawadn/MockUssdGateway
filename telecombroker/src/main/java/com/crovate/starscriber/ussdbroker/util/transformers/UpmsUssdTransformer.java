/*
 * Transforms/Constructs UssdRequest to UPMS XML object, and vice versa.
 * 
 */
package com.crovate.starscriber.ussdbroker.util.transformers;


import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.ComponentOP;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.MessageType;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdGateWayConstants;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdRequest;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.InSwitchUssdResponse;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.AbortType;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.Component;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.ComponentParam;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.Context;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.Destination;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.Dialog;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.Message;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.ObjectIdentifier;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.TCAPAddress;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.UPMS;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.User;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.UserInfo;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.UserInfoMap;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.UserInfoParam;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jawad
 */
public class UpmsUssdTransformer {
    
    public UPMS buildUpms(InSwitchUssdRequest request){
       
        UPMS upms = new UPMS();
        upms.setMessage(buildMessage(request));
        return upms;
    }
    
    public Message buildMessage(InSwitchUssdRequest request){
    
            Message message = new Message(request.getMessageType().getValue());
            
           
            switch(MessageType.findByValue(request.getMessageType().getValue())){
                
                case BIND:
                      message.setTcapAddress(buildTCAPAddress(request));
                      break;
                case ADD_DESTINATION:
                      message.setTcapAddress(buildTCAPAddress(request));
                case BEGIN:
                      message.setUser(buildUser(request));
                      message.setDialog(buildDialog(request));
                      message.setDestination(buildDestination(request));
                      message.setContext(buildContext(request));
                      message.setUserInfo(buildUserInfo(request));
                      message.setComponent(buildComponent(request));
                      break;
                case CONTINUE:
                      message.setUser(buildUser(request));
                      message.setDialog(buildDialog(request));
                      if(request.getComponentOp().equals(ComponentOP.UNSTRUCTURED_SS_NOTIFY.getValue())){
                            message.setUserInfo(buildUserInfo(request));
                      }
                      message.setComponent(buildComponent(request));
                     
                      
                    break;
                case END:
                      message.setUser(buildUser(request));
                      message.setDialog(buildDialog(request));
                    break;
                case ABORT:
                      message.setUser(buildUser(request));
                      message.setDialog(buildDialog(request));
                      message.setAbortType(buildAbort(request));
                    break;
                default:
                    
            }
            
            return message;
    }

      
    public TCAPAddress buildTCAPAddress(InSwitchUssdRequest request){
         
            TCAPAddress tcapAddress  = new TCAPAddress();
            if (MessageType.findByValue(request.getMessageType().getValue()).equals(MessageType.ADD_DESTINATION)){
                tcapAddress.setId(request.getTcapAddressId());
            }else{
                tcapAddress.setId(null);
            }    
            tcapAddress.setAddressIndicator(InSwitchUssdGateWayConstants.ADDRESS_INDICATOR);
            tcapAddress.setDigits(InSwitchUssdGateWayConstants.DIGITS);
            tcapAddress.setEncodingScheme(InSwitchUssdGateWayConstants.ENCODING_SCHEME);
            tcapAddress.setNatureOfAddress(InSwitchUssdGateWayConstants.NATURE_OF_ADDRESS);
            tcapAddress.setNumberingPlan(InSwitchUssdGateWayConstants.NUMBERING_PLAN);
            tcapAddress.setPointCode(InSwitchUssdGateWayConstants.POINT_CODE);
            tcapAddress.setSubsystemNumber(InSwitchUssdGateWayConstants.SUBSYSTEM_NUMBER);
            tcapAddress.setTranslationType(InSwitchUssdGateWayConstants.TRANSLATION_TYPE);
           // message.setTcapAddress(tcapAddress);
            return tcapAddress;
    
    }
    
    public Dialog buildDialog(InSwitchUssdRequest request) {
        Dialog dialog = new Dialog();
        dialog.setId(request.getDialogId());
        return dialog;
    }
    
    public UserInfo buildUserInfo(InSwitchUssdRequest request) {
        
        UserInfo userInfo = new UserInfo();
        
        ObjectIdentifier objectIdentifier = new ObjectIdentifier();
        objectIdentifier.setV(request.getUserInfObjectIdentifier());
        userInfo.setObjectIdentifier(objectIdentifier);
        
        UserInfoMap userInfoMap = new UserInfoMap();
        userInfoMap.setOperation(request.getUserInfoMapOperation());
        if(ComponentOP.findByValue(request.getComponentOp()).equals(ComponentOP.USSD_REQUEST)){
            List<UserInfoParam> userInfoParamList = new ArrayList<UserInfoParam>();

            UserInfoParam originationParam = new UserInfoParam();
            originationParam.setName("origination");
            originationParam.setNai(request.getOriginationNai());
            originationParam.setNpi(request.getOriginationNpi());
            originationParam.setNumber(request.getOriginationNumber());

            UserInfoParam destinationParam = new UserInfoParam();
            destinationParam.setName("destination");
            destinationParam.setNai(request.getDestinationNai());
            destinationParam.setNpi(request.getDestinationNpi());
            destinationParam.setNumber(request.getDestinationNumber());

            userInfoParamList.add(originationParam);
            userInfoParamList.add(destinationParam);

            userInfoMap.setParams(userInfoParamList);
        }
        userInfo.setUserInfoMap(userInfoMap);
        
        return userInfo;
    }

    public Component buildComponent(InSwitchUssdRequest request) {
     
        Component component = new Component();
        
        component.setId(request.getComponentId());
        component.setOp(request.getComponentOp());
        component.setType( request.getComponentType());
        
        List<ComponentParam> paramList = new ArrayList<ComponentParam>();
        
        ComponentParam dataCodingParam = new ComponentParam();
        dataCodingParam.setName("data_coding_scheme");
        dataCodingParam.setV(request.getDataCodingScheme());
        paramList.add(dataCodingParam);
       
        ComponentParam ussdStringParam = new ComponentParam();
        ussdStringParam.setName("ussd_string");
        ussdStringParam.setV(request.getUssdString());
        paramList.add(ussdStringParam);
       
        if(request.getMessageType().equals(MessageType.BEGIN)){
            ComponentParam msIsdnParam = new ComponentParam();
            msIsdnParam.setName("msisdn");
            msIsdnParam.setNumber(request.getMsIsdnNumber());
            msIsdnParam.setNai(request.getMsIsdnNai());
            msIsdnParam.setNpi(request.getMsIsdnNpi());
            paramList.add(msIsdnParam);
        }
        component.setParams(paramList);
        
        return component;
    }
    
    
   

    public Context buildContext(InSwitchUssdRequest request) {
        Context context = new Context();
        context.setV(request.getContextValue());
        return context;
    }

    public User buildUser(InSwitchUssdRequest request) {
        User user = new User();
        user.setId(request.getUserId());
        return user;
    }

    public Destination buildDestination(InSwitchUssdRequest request) {
        
        Destination destination = new Destination();
        if(request.getMessageType().equals(MessageType.BEGIN)){
            destination.setAddressIndicator(InSwitchUssdGateWayConstants.BEGIN_ADDRESS_INDICATOR);
            destination.setPointCode(InSwitchUssdGateWayConstants.BEGIN_POINT_CODE);
        }else{
            destination.setAddressIndicator(InSwitchUssdGateWayConstants.ADDRESS_INDICATOR);
            destination.setPointCode(InSwitchUssdGateWayConstants.POINT_CODE);
        }
        
        destination.setSubsystemNumber(InSwitchUssdGateWayConstants.SUBSYSTEM_NUMBER);
        destination.setNatureOfAddress(InSwitchUssdGateWayConstants.NATURE_OF_ADDRESS);
        destination.setTranslationType(InSwitchUssdGateWayConstants.TRANSLATION_TYPE);
        destination.setNumberingPlan(InSwitchUssdGateWayConstants.NUMBERING_PLAN);
        destination.setEncodingScheme(InSwitchUssdGateWayConstants.ENCODING_SCHEME);
        destination.setDigits(request.getDestinationNumber());
        
        return destination;
    }
    
    public AbortType buildAbort(InSwitchUssdRequest request){
        
        AbortType abortType = new AbortType();
        abortType.setV(request.getAbortValue());
        
        return abortType;
    }
    
     public InSwitchUssdResponse toUssdResponse(UPMS upms){
       
       InSwitchUssdResponse ussdResponse = new InSwitchUssdResponse();
       
       Message message = upms.getMessage();
       
       if(message != null){
            
          
          ussdResponse.setMessageType(MessageType.findByValue(message.getType()));
          ussdResponse.setUserId((message.getUser() != null) ? message.getUser().getId() : null);
          ussdResponse.setDestinationId((message.getDestination()!= null) ? message.getDestination().getId() : null);
          ussdResponse.setStackId((message.getStack()!= null) ? message.getStack().getId() : null);

          Component component = upms.getMessage().getComponent();
              
          if(component != null){
                 ussdResponse.setComponentId(component.getId());
                 
                 if(component.getOp() != null){
                    ussdResponse.setComponentOp(component.getOp());
                 }
                 ussdResponse.setComponentType(component.getType());
              
                if(component.getParams() != null){
                    for(ComponentParam param: component.getParams()){
                        if(param.getName().equals("ussd_string")){
                             ussdResponse.setUssdString(param.getV());

                        }
                    }
                }
              }
           
           ussdResponse.setContext((message.getContext()!= null) ? message.getContext().getV() : null);
           ussdResponse.setDialogId((message.getDialog()!= null) ? (int)(long)message.getDialog().getId() : null);

           if(message.getUserInfo() != null){
            
               ussdResponse.setUserInfObjectIdentifier((message.getUserInfo().getObjectIdentifier()!= null) ? message.getUserInfo().getObjectIdentifier().getV() : null);
               ussdResponse.setUserInfoMapOperation((message.getUserInfo().getUserInfoMap() != null) ? message.getUserInfo().getUserInfoMap().getOperation() : null);
           }
           
       }
       return ussdResponse;
       
   }
   
}
