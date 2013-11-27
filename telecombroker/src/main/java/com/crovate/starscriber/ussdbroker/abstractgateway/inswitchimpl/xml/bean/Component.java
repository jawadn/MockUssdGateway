/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author jawad
 */
public class Component {
    
    
    private String type;
    
    
    private int id;
    
    
    private String errorType;
    
   
    private String op;
    
   
    private List<ComponentParam> params;
    
   
    private ErrorCode errorCode;

    @XmlAttribute(name="type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlAttribute(name="id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlAttribute(name="error_type")
    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }
    
    
    @XmlAttribute(name="op")
    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    @XmlElement(name="param")
    public List<ComponentParam> getParams() {
        return params;
    }

    public void setParams(List<ComponentParam> params) {
        this.params = params;
    }
    
    @XmlElement(name="error_code") 
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

  
    
}

