/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jawad
 */
@XmlRootElement(name="abort_reason")
public class AbortReason {
    
   
    private String v;
    
    @XmlAttribute
    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

  
}
