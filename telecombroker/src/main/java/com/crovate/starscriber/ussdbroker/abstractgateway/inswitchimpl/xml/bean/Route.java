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
public class Route {
   
   
    private int id;
 
    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
