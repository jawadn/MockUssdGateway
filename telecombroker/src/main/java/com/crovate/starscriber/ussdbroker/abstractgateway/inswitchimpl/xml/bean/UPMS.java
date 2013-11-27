/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jawad
 */

@XmlRootElement(name = "upms")
public class UPMS {
    
    private Message message;

    @XmlElement(name = "msg")
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

   
   
}
