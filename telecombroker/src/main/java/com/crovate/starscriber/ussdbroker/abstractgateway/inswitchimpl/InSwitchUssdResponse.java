/*
 * Pojo using UssdResponse as base class, Inswitch specific
 * r.
 */
package com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl;

import com.crovate.starscriber.ussdbroker.abstractgateway.UssdResponse;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.MessageType;



/**
 *
 * @author jawad
 */
public class InSwitchUssdResponse extends UssdResponse{
    
    private MessageType messageType;
    private Integer stackId; // Stack Id assigned to application in bind operation and return in response after bind operation is done on ussd gateway
    private Integer userId; // user Id assigned to application in bind operation and return in response after bind operation is done on ussd gateway. Unique per application
    private Integer destinationId; // return in response after add_destination operation
    private Integer dialogId;      // each ussd transaction is identified by a dialog id. If a request is send to ussd gateway (i.e transaction started from ussd application side)
                               //dialog id will be between 0 and 16383. If a transaction is started from mobile side the dialog id will be between between 16384 and 32768

    private String userInfoMapOperation;
    private String userInfObjectIdentifier;
    
    private String context;
    
    private String componentType;
    private Integer componentId;
    private String componentOp;
    private String dataCodingScheme;
    private String ussdString;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

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

    public Integer getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Integer destinationId) {
        this.destinationId = destinationId;
    }

    public Integer getDialogId() {
        return dialogId;
    }

    public void setDialogId(Integer dialogId) {
        this.dialogId = dialogId;
    }

    public String getUserInfoMapOperation() {
        return userInfoMapOperation;
    }

    public void setUserInfoMapOperation(String userInfoMapOperation) {
        this.userInfoMapOperation = userInfoMapOperation;
    }

    public String getUserInfObjectIdentifier() {
        return userInfObjectIdentifier;
    }

    public void setUserInfObjectIdentifier(String userInfObjectIdentifier) {
        this.userInfObjectIdentifier = userInfObjectIdentifier;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public Integer getComponentId() {
        return componentId;
    }

    public void setComponentId(Integer componentId) {
        this.componentId = componentId;
    }

    public String getComponentOp() {
        return componentOp;
    }

    public void setComponentOp(String componentOp) {
        this.componentOp = componentOp;
    }

    public String getDataCodingScheme() {
        return dataCodingScheme;
    }

    public void setDataCodingScheme(String dataCodingScheme) {
        this.dataCodingScheme = dataCodingScheme;
    }

    public String getUssdString() {
        return ussdString;
    }

    public void setUssdString(String ussdString) {
        this.ussdString = ussdString;
    }
    
    public Long getDialog(){
       return new Long(dialogId);
   }
   
    
}
