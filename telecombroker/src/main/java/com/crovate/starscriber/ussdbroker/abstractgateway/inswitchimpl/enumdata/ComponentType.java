/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata;

/**
 *
 * @author jawad
 */
public enum ComponentType {
    
    INVOKE("invoke"),
    RETURN_RESULT("return_result"),
    RETURN_RESULT_L("return_result_l"),
    RETURN_ERROR("return_error"),
    REJECT("reject");


    private ComponentType(String value) {
        this.value = value;
    }
    
    private String value;

    public String getValue() {
        return value;
    }
    
    
    
}
