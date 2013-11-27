/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata;

/**
 *
 * @author jawad
 */
public enum MessageType {
    
    PING("ping"),
    PONG("pong"),
    BIND("bind"),
    BIND_CONFIRM("bind_confirm"),
    BIND_FAILURE("bind_failure"),
    ADD_DESTINATION("add_destination"),
    ADD_DESTINATION_CONFIRM("add_destination_confirm"),
    ADD_DESTINATION_FAILURE("add_destination_failure"),
    BEGIN("begin"),
    CONTINUE("continue"),
    END("end"),
    ABORT("abort"),
    ERROR("error"),
    EXEC_ERROR("exec_error"),
    EXIT("exit");
    
    
    
    private MessageType(String value){
        this.value = value;
    }
    
    private String value;

    public String getValue() {
        return value;
    }
    
    public static MessageType findByValue(String value){
            
        
        for (MessageType messageType: values()) {
            if(messageType.getValue().equals(value))
               return messageType; 
        }
        
        return null;
    }
    
    
}
