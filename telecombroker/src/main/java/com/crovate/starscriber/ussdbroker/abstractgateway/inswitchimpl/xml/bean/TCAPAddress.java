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
 public class TCAPAddress {

    private Integer id;   

    private String addressIndicator;

    private String pointCode;

    private int subsystemNumber;

    private int natureOfAddress;

    private int translationType;

    private int numberingPlan;

    private int encodingScheme;

    private String digits;

    @XmlAttribute(name="id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlAttribute(name="addressIndicator")
    public String getAddressIndicator() {
        return addressIndicator;
    }

    public void setAddressIndicator(String addressIndicator) {
        this.addressIndicator = addressIndicator;
    }

    @XmlAttribute(name="pointCode")
    public String getPointCode() {
        return pointCode;
    }

    public void setPointCode(String pointCode) {
        this.pointCode = pointCode;
    }

    @XmlAttribute(name="subsystemNumber")
    public int getSubsystemNumber() {
        return subsystemNumber;
    }

    public void setSubsystemNumber(int subsystemNumber) {
        this.subsystemNumber = subsystemNumber;
    }

    @XmlAttribute(name="natureOfAddress")
    public int getNatureOfAddress() {
        return natureOfAddress;
    }

    public void setNatureOfAddress(int natureOfAddress) {
        this.natureOfAddress = natureOfAddress;
    }
    
    @XmlAttribute(name="translationType")
    public int getTranslationType() {
        return translationType;
    }

    public void setTranslationType(int translationType) {
        this.translationType = translationType;
    }
 
    @XmlAttribute(name="numberingPlan")
    public int getNumberingPlan() {
        return numberingPlan;
    }

    public void setNumberingPlan(int numberingPlan) {
        this.numberingPlan = numberingPlan;
    }

    @XmlAttribute(name="encodingScheme")
    public int getEncodingScheme() {
        return encodingScheme;
    }

    public void setEncodingScheme(int encodingScheme) {
        this.encodingScheme = encodingScheme;
    }

    @XmlAttribute(name="digits")
    public String getDigits() {
        return digits;
    }

    public void setDigits(String digits) {
        this.digits = digits;
    }

    public TCAPAddress(int id, String addressIndicator, String pointCode, int subsystemNumber, int natureOfAddress, int translationType, int numberingPlan, int encodingScheme, String digits) {
        this.id = id;
        this.addressIndicator = addressIndicator;
        this.pointCode = pointCode;
        this.subsystemNumber = subsystemNumber;
        this.natureOfAddress = natureOfAddress;
        this.translationType = translationType;
        this.numberingPlan = numberingPlan;
        this.encodingScheme = encodingScheme;
        this.digits = digits;
    }

    public TCAPAddress() {
    }
 
 

    
}
