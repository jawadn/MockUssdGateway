/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata;

/**
 *
 * @author jawad
 */
public enum ComponentOP {
    
    USSD_REQUEST("ussd_request"),
    PROCESS_USSD_REQUEST("process_ussd_request"),
    UNSTRUCTURED_SS_NOTIFY("unstructured_ss_notify");

    private ComponentOP(String value) {
        this.value = value;
    }
    
    
    private String value;

    public String getValue() {
        return value;
    }
    
    
    public static ComponentOP findByValue(String value){
        
        for(ComponentOP op:values()){
            if(op.getValue().equals(value)){
                return op;
            }
        }
        
        return null;
    }
}
