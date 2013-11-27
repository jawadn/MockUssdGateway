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
@XmlRootElement
public class UserInfo {
    
   
    private ObjectIdentifier objectIdentifier;
    
   
    private UserInfoMap userInfoMap;
 
    @XmlElement(name = "object_identifier")
    public ObjectIdentifier getObjectIdentifier() {
        return objectIdentifier;
    }

    public void setObjectIdentifier(ObjectIdentifier objectIdentifier) {
        this.objectIdentifier = objectIdentifier;
    }

     @XmlElement(name = "map")
    public UserInfoMap getUserInfoMap() {
        return userInfoMap;
    }

    public void setUserInfoMap(UserInfoMap userInfoMap) {
        this.userInfoMap = userInfoMap;
    }
    
    
    
}
