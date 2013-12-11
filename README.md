Introduction

MockUssdgateway is a dummy gateway to mock the functionality of Inswitch Ussdgateway. 
Mock gateway communicates with ussd-broker in xml format.

    Oracle JDK: 1.7.* or later
    CentOS 6.3


How To Run?

Mockgateway is run by invoking the following command:

java -jar mockgateway.jar 



FAQ & Notes

How the mock-gateway connects with ussd-broker?

    The mock-gateway use sockets to connect with ussd-broker.


On which port mock gateway accepts connections?
    
    Mock gateway listens on port 4322.




