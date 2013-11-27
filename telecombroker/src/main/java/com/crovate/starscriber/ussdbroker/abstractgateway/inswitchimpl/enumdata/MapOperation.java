/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata;

/**
 *
 * @author jawad
 */
public enum MapOperation {
    
    OPEN("open"),
    ACCEPT("accept"),
    CLOSE("close");

    private MapOperation(String value) {
        this.value = value;
    }
    
    private String value;

    public String getValue() {
        return value;
    }
    
    
    
    
}
