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
@XmlRootElement
public class ComponentParam {
    
   
    private String name;
    
  
    private String number;
    
 
    private String nai;
    
  
    private String npi;
    
  
    private String v;

    @XmlAttribute(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
  
    @XmlAttribute(name="number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
  
    @XmlAttribute(name="nai")
    public String getNai() {
        return nai;
    }

    public void setNai(String nai) {
        this.nai = nai;
    }
  
    @XmlAttribute(name="npi")
    public String getNpi() {
        return npi;
    }

    public void setNpi(String npi) {
        this.npi = npi;
    }

    @XmlAttribute(name="v")
    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }
    
    
    
}

