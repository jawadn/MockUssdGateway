/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jawad
 */
@XmlRootElement
public class UserInfoMap {
    
   
    private List<UserInfoParam> params;
    
    private String operation;

    @XmlElement(name="param")
    public List<UserInfoParam> getParams() {
        return params;
    }

    public void setParams(List<UserInfoParam> params) {
        this.params = params;
    }

     @XmlAttribute
     public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    
    
}
