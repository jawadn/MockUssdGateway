/*
 * Transport messages to and from ussdgateway
 * 
 */
package com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl;


import com.crovate.starscriber.ussdbroker.abstractgateway.GatewayMessageTranciever;
import com.crovate.starscriber.ussdbroker.abstractgateway.UssdRequest;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.enumdata.MessageType;
import com.crovate.starscriber.ussdbroker.abstractgateway.inswitchimpl.xml.bean.UPMS;
import com.crovate.starscriber.ussdbroker.util.transformers.UpmsUssdTransformer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jawad
 */
public class InSwitchUssdMessageTranciever implements GatewayMessageTranciever{
    
    //essential id specific to application connected to inswitch, assigns a new id to every
    //application connected to gateway
    private static Integer userId;
    private static Integer stackId;
    
   
    private static final Logger logger =  LoggerFactory.getLogger(InSwitchUssdMessageTranciever.class);
   
       
   
    public void sendRequest(UssdRequest ussdRequest, OutputStream output){
    
        try {
         
            InSwitchUssdRequest request = (InSwitchUssdRequest) ussdRequest;
            UpmsUssdTransformer upmsBuilder = new UpmsUssdTransformer();
            
            UPMS upms=upmsBuilder.buildUpms(request);

         
            JAXBContext jaxbContext;

            jaxbContext = JAXBContext.newInstance(UPMS.class);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            
         
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            
            
           
            
            StringWriter stringWriter = new StringWriter();
            jaxbMarshaller.marshal(upms, stringWriter);
            String xmlString = stringWriter.toString();
            if(upms.getMessage().getComponent() != null){
              logger.info("Sending request to gateway with dialog id {} and component id {}",
                                        upms.getMessage().getDialog().getId(),
                                        upms.getMessage().getComponent().getId());
            }
            
            
            sendMsg(xmlString, output);

         } catch (JAXBException ex) {
          
             logger.error("", ex);
      } catch (Exception ex) {
          
             logger.error("", ex);
      }  
                
    }
    
    public InSwitchUssdResponse getResponse(InputStream inputStream) {
        
          InSwitchUssdResponse inSwitchResponse = null;
          try {

            
            JAXBContext jaxbContext = JAXBContext.newInstance(UPMS.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
           
            byte[] header = new byte[4];
            inputStream.read(header);
		        	
            int length = getLength(header);
           
            byte[] body = new byte[length];
            inputStream.read(body);
            String xml = new String(body, "UTF-8");
            logger.debug("Response recieved from gateway {0}" , xml);
                                
            StringReader reader  = new StringReader(xml);
           
            UPMS upms = (UPMS) jaxbUnmarshaller.unmarshal(reader);
            inSwitchResponse = new UpmsUssdTransformer().toUssdResponse(upms);
            
            
          } catch (JAXBException e) {
            
                logger.error("", e);
          } catch (IOException ex) {
                 logger.error("", ex);
        } catch (Exception ex) {
                 logger.error("", ex);
        }
          
           return inSwitchResponse;
           
        
   }
     
 
    
       
   public void bindToGateway(OutputStream output){

        InSwitchUssdRequest inSwitchRequest = new InSwitchUssdRequest();
        inSwitchRequest.setMessageType(MessageType.BIND);

        sendRequest(inSwitchRequest,output);
   
        
    }
    
    public void pingGateWay(OutputStream output , MessageType messageType){
        
        InSwitchUssdRequest inSwitchRequest = new InSwitchUssdRequest();
        inSwitchRequest.setMessageType(messageType);
        sendRequest(inSwitchRequest,output);
    }

    
    
    public static Integer getUserId() {
        return userId;
    }

    public static void setUserId(Integer userId) {
        InSwitchUssdMessageTranciever.userId = userId;
    }

    public static Integer getStackId() {
        return stackId;
    }

    public static void setStackId(Integer stackId) {
        InSwitchUssdMessageTranciever.stackId = stackId;
    }

     
  
      
      private void sendMsg(String xml,OutputStream output){
        
        PrintWriter writer = null;
        
        try {
		byte[] c = new byte[4];
		
		int value = xml.length();
		c[0] = (byte) (value & 0xFF);
		c[1] = (byte) ((value >> 8) & 0xFF);
		c[2] = (byte) ((value >> 16) & 0xFF);
		c[3] = (byte) ((value >> 24) & 0xFF);		
		
		// Send packet length
		output.write(c, 0, 4);
		writer = new PrintWriter(output);
                writer.write(xml);
		logger.debug("Sending xml to gateway {}",xml);
		writer.flush();
                
		} catch (Exception e) {
			
			logger.error("Exception in sendMsg()",e);
		}	
	}
      
        private int getLength(byte[] b){
        
          int i = (b[3]) << 24 |
		(b[2]&0xff) << 16 |
		(b[1]&0xff) <<  8 |
		(b[0]&0xff);
		
		return i;
        }     

    
}
