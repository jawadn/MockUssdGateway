/*
 * Pojo extention using UssdRequest as base class, Inswitch specifc
 * 
 */
package com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl;

import com.crovate.starscriber.ussdbroker.abstractgateway.UssdRequest;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.MessageType;



/**
 *
 * @author jawad
 */
public class InSwitchUssdRequest extends UssdRequest{
    
    private MessageType messageType;

    // for bind request and add_destination request

    private int tcapAddressId;   // for add_destination request only
    private String addressIndicator;
    private String pointCode;
    private int subsystemNumber;
    private int natureOfAddress;
    private int translationType;
    private int numberingPlan;
    private int encodingScheme;
    private String digits;

    // for ussd_request
    private int userId;
     /* each ussd transaction is identified by a dialog id. If a request is send to ussd gateway (i.e transaction started from ussd application side)
    dialog id will be between 0 and 16383. If a transaction is started from mobile side the dialog id will be between between 16384 and 32768*/
    private int dialogId;     
  
    private String userInfObjectIdentifier;
    private String userInfoMapOperation;
    
    private String contextValue; 
    
    private String componentType;
    private int componentId;
    private String componentOp;
    private String dataCodingScheme;
    private String ussdString;

    // for process_ussd_request and ussd_notify request
    private String originationNumber;
    private String originationNai;
    private String originationNpi;
            
    private String destinationNumber;
    private String destinationNai;
    private String destinationNpi;

    // for ussd_notify
    private String destinationId;
    private String msIsdnNumber;
    private String msIsdnNai;
    private String msIsdnNpi;
    
    private String abortValue;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }


    public int getTcapAddressId() {
        return tcapAddressId;
    }

    public void setTcapAddressId(int tcapAddressId) {
        this.tcapAddressId = tcapAddressId;
    }

   
    public String getAddressIndicator() {
        return addressIndicator;
    }

    public void setAddressIndicator(String addressIndicator) {
        this.addressIndicator = addressIndicator;
    }

    public String getPointCode() {
        return pointCode;
    }

    public void setPointCode(String pointCode) {
        this.pointCode = pointCode;
    }

    public int getSubsystemNumber() {
        return subsystemNumber;
    }

    public void setSubsystemNumber(int subsystemNumber) {
        this.subsystemNumber = subsystemNumber;
    }

   

    public int getNatureOfAddress() {
        return natureOfAddress;
    }

    public void setNatureOfAddress(int natureOfAddress) {
        this.natureOfAddress = natureOfAddress;
    }

    public int getTranslationType() {
        return translationType;
    }

    public void setTranslationType(int translationType) {
        this.translationType = translationType;
    }

    public int getNumberingPlan() {
        return numberingPlan;
    }

    public void setNumberingPlan(int numberingPlan) {
        this.numberingPlan = numberingPlan;
    }

    
    public int getEncodingScheme() {
        return encodingScheme;
    }

    public void setEncodingScheme(int encodingScheme) {
        this.encodingScheme = encodingScheme;
    }

    public String getDigits() {
        return digits;
    }

    public void setDigits(String digits) {
        this.digits = digits;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

   
    @Override
    public int getDialogId() {
        return dialogId;
    }

  
    @Override
    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }

    public String getUserInfObjectIdentifier() {
        return userInfObjectIdentifier;
    }

    public void setUserInfObjectIdentifier(String userInfObjectIdentifier) {
        this.userInfObjectIdentifier = userInfObjectIdentifier;
    }

    
    public String getUserInfoMapOperation() {
        return userInfoMapOperation;
    }

    public void setUserInfoMapOperation(String userInfoMapOperation) {
        this.userInfoMapOperation = userInfoMapOperation;
    }

    public String getContextValue() {
        return contextValue;
    }

    public void setContextValue(String contextValue) {
        this.contextValue = contextValue;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

   
    @Override
    public int getComponentId() {
        return componentId;
    }

    
    @Override
    public void setComponentId(int componentId) {
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

    public String getOriginationNumber() {
        return originationNumber;
    }

    public void setOriginationNumber(String originationNumber) {
        this.originationNumber = originationNumber;
    }

    public String getOriginationNai() {
        return originationNai;
    }

    public void setOriginationNai(String originationNai) {
        this.originationNai = originationNai;
    }

    public String getOriginationNpi() {
        return originationNpi;
    }

    public void setOriginationNpi(String originationNpi) {
        this.originationNpi = originationNpi;
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    public String getDestinationNai() {
        return destinationNai;
    }

    public void setDestinationNai(String destinationNai) {
        this.destinationNai = destinationNai;
    }

    public String getDestinationNpi() {
        return destinationNpi;
    }

    public void setDestinationNpi(String destinationNpi) {
        this.destinationNpi = destinationNpi;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }
    
    public String getMsIsdnNumber() {
        return msIsdnNumber;
    }

    public void setMsIsdnNumber(String msIsdnNumber) {
        this.msIsdnNumber = msIsdnNumber;
    }

    public String getMsIsdnNai() {
        return msIsdnNai;
    }

    public void setMsIsdnNai(String msIsdnNai) {
        this.msIsdnNai = msIsdnNai;
    }

    public String getMsIsdnNpi() {
        return msIsdnNpi;
    }

    public void setMsIsdnNpi(String msIsdnNpi) {
        this.msIsdnNpi = msIsdnNpi;
    }

    public String getAbortValue() {
        return abortValue;
    }

    public void setAbortValue(String abortValue) {
        this.abortValue = abortValue;
    }

   
    
    
}
