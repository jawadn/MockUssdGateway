/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean;

import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.AbortReason;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.AbortType;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.Component;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.Context;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.Destination;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.Dialog;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.Error;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.Link;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.Reason;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.Route;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.Stack;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.Status;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.TCAPAddress;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.Trunk;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.User;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.UserInfo;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jawad
 */
@XmlRootElement
public class Message {
    
   
    private String type;
    
    private TCAPAddress tcapAddress;
    
    private User user;

    private Dialog dialog;
    
    private Stack stack;
            
    private UserInfo userInfo;

    private Destination destination;
    
    private Component component;
     
    private Context context;
    
    private AbortType abortType;
    
    private AbortReason abortReason;
    
    private Error error;
    
    private Trunk trunk;
    
    private Status status;
    
    private Link link;
   
    private Route route;
    
    private Reason reason;
      
    @XmlAttribute
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        
    }

    @XmlElement(name="tcapaddress")
    public TCAPAddress getTcapAddress() {
        return tcapAddress;
    }

    public void setTcapAddress(TCAPAddress tcapAddress) {
        this.tcapAddress = tcapAddress;
    }

    @XmlElement(name="user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

        
    @XmlElement(name="dialog")
    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    @XmlElement(name="stack")
    public Stack getStack() {
        return stack;
    }

    public void setStack(Stack stack) {
        this.stack = stack;
    }

    @XmlElement(name="user_info")
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

     @XmlElement(name="destination")
     public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    @XmlElement(name="component")
    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
    
    @XmlElement(name="context")
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    @XmlElement(name="abort_type")
    public AbortType getAbortType() {
        return abortType;
    }

    public void setAbortType(AbortType abortType) {
        this.abortType = abortType;
    }

    @XmlElement(name="abort_reason")
    public AbortReason getAbortReason() {
        return abortReason;
    }

    public void setAbortReason(AbortReason abortReason) {
        this.abortReason = abortReason;
    }

    @XmlElement(name="error")
    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @XmlElement(name="trunk")
    public Trunk getTrunk() {
        return trunk;
    }

    public void setTrunk(Trunk trunk) {
        this.trunk = trunk;
    }

    @XmlElement(name="status")
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @XmlElement(name="link")
    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    @XmlElement(name="route")
    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    @XmlElement(name="reason")
    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public Message(String type, TCAPAddress tcapAddress, User user, Dialog dialog, Stack stack, UserInfo userInfo, Destination destination, Component component, Context context, AbortType abortType, AbortReason abortReason, Error error, Trunk trunk, Status status, Link link, Route route, Reason reason) {
       
        this.type = type;
        this.tcapAddress = tcapAddress;
        this.user = user;
        this.dialog = dialog;
        this.stack = stack;
        this.userInfo = userInfo;
        this.destination = destination;
        this.component = component;
        this.context = context;
        this.abortType = abortType;
        this.abortReason = abortReason;
        this.error = error;
        this.trunk = trunk;
        this.status = status;
        this.link = link;
        this.route = route;
        this.reason = reason;
    }

    public Message() {
    }
    
    public Message(String type){
        this.type = type;
    }


    
    
    
}
