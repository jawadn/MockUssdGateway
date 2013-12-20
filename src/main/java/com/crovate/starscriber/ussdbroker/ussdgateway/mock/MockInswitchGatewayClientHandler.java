/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.ussdgateway.mock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 *
 * @author jawad
 */
public class MockInswitchGatewayClientHandler implements Runnable{
    
    private Socket clientSocket;
    OutputStream output ;
    InputStream input ;
    PrintWriter writer;
    
    private boolean enableRandomizer;
    
    private final static Logger logger = LoggerFactory.getLogger(MockInswitchGatewayClientHandler.class);
    
    private final long pingSendIntervalInMillis = 30000;
    private long lastPingSentInMillis = 0;
    
    static int packetRecieved = 0;

    public boolean isEnableRandomizer() {
        return enableRandomizer;
    }

    public void setEnableRandomizer(boolean enableRandomizer) {
        this.enableRandomizer = enableRandomizer;
    }

  
    public enum Type {
        BIND,PING, PONG, BEGIN, CONTINUE, END, ABORT, UNRECOGNIZED 
    }

    public MockInswitchGatewayClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            output = clientSocket.getOutputStream();
            input = clientSocket.getInputStream(); 
            writer = new PrintWriter(output);
            
          
            XPath xpath = XPathFactory.newInstance().newXPath();
            
            Thread pingThread = new Thread(new Runnable() {

                public void run() {
                     while(true){
                
                        
                         try {
                             if (System.currentTimeMillis() - lastPingSentInMillis >= pingSendIntervalInMillis){
                                String response = getResponse(Type.PONG, null,0,null,0);
                                synchronized(clientSocket.getOutputStream()){
                                    sendMsg(response);
                                }
                                lastPingSentInMillis = System.currentTimeMillis(); 
                               }
                         } catch (Exception ex) {
                             logger.error("", ex);
                         }

                  } 
                }
            },"Ping thread");
            
            pingThread.start();
            
            while(true){
                
                byte[] header = new byte[4];
		input.read(header);
		        								
                int length = getLength(header);
                          	
		byte[] body = new byte[length];
                input.read(body);
		String requestXml = new String(body, "UTF-8");
                if(!requestXml.isEmpty()){
                    Type type = getMsgType(requestXml);

                    logger.debug("Request recieved of type:" + type.name());
                    if(!type.equals(Type.PONG) && !type.equals(Type.END) && !type.equals(Type.ABORT)){

                        String dialogId = (String) xpath.evaluate("/upms/msg/dialog/@id", new InputSource(new StringReader(requestXml)), XPathConstants.STRING);
                        String componentId = (String) xpath.evaluate("/upms/msg/component/@id", new InputSource(new StringReader(requestXml)), XPathConstants.STRING);
                        String componentType = (String) xpath.evaluate("/upms/msg/component/@op", new InputSource(new StringReader(requestXml)), XPathConstants.STRING);
                        
                        int selectedOption = 0;
                        
                        if(isEnableRandomizer()){
                            
                            if(type.equals(Type.BEGIN) || (type.equals(Type.CONTINUE) && !componentType.equals("unstructured_ss_notify"))){
                                String menuInHex = (String) xpath.evaluate("/upms/msg/component/param[@name='ussd_string']/@v", new InputSource(new StringReader(requestXml)), XPathConstants.STRING);

                                String menu = StringUtil.convertHexToString(menuInHex);

                                selectedOption = selectRandonOption(menu);
                            }
                            
                        }else{
                            selectedOption = getRandomInt(1,3);
                        }
                        int compId = 0;
                        if(componentId != null && !componentId.isEmpty() && !componentId.equals("")){
                            compId = Integer.valueOf(componentId);
                        }
                        String response = getResponse(type,dialogId,compId,componentType,selectedOption);
                      
                         synchronized(clientSocket.getOutputStream()){
                            sendMsg(response);
                         }
                            logger.debug("Response send for dialog id: " + dialogId + " and component id: " + componentId);
                    }
                }  
                
            } 
        } catch (IOException ex) {
                logger.error("", ex);
        }catch (Exception ex) {
                logger.error("", ex);
                System.out.println("Exception occured in ServerConnection run() method");
          } 
        
    }
                  
    
    private int getLength(byte[] b){
        
        int i =	(b[3])      << 24 |
		(b[2]&0xff) << 16 |
		(b[1]&0xff) <<  8 |
		(b[0]&0xff);
		
		return i;
    }                  
    
    
    private Type getMsgType(String str){
        
       try{
            if(!str.isEmpty()){
                
                XPath xpath = XPathFactory.newInstance().newXPath();
                InputSource source = new InputSource(new StringReader(str));

                String type = (String) xpath.evaluate("/upms/msg/@type", source, XPathConstants.STRING);
                logger.debug(String.format("Msg Type: %s", type));

                if (type.equals("bind")){
                    return MockInswitchGatewayClientHandler.Type.BIND;
                } else if (type.equals("ping")){
                    return MockInswitchGatewayClientHandler.Type.PING;
                } else if (type.equals("pong")){
                    return MockInswitchGatewayClientHandler.Type.PONG;
                } else if  (type.equals("begin")){
                     packetRecieved++;
                    logger.debug("**** Number of packet Recieved*****" + packetRecieved);
                    return MockInswitchGatewayClientHandler.Type.BEGIN;
                } else if  (type.equals("continue")){
                     packetRecieved++;
                    logger.debug("**** Number of packet Recieved*****" + packetRecieved);
                    return MockInswitchGatewayClientHandler.Type.CONTINUE;
                } else if  (type.equals("end")){
                     packetRecieved++;
                    logger.debug("**** Number of packet Recieved*****" + packetRecieved);
                    return MockInswitchGatewayClientHandler.Type.END;
                 } else if  (type.equals("abort")){
                    return MockInswitchGatewayClientHandler.Type.ABORT;
                }
            }
	
            return MockInswitchGatewayClientHandler.Type.UNRECOGNIZED;
		
	} catch (Exception e) {
             logger.error("Error occured", e);
	     return MockInswitchGatewayClientHandler.Type.UNRECOGNIZED;
	}
    
    }
    
    
     private String getResponse(Type type, String dialogId, int compId, String componentType, int selectedMenuOption){
         
         String response = "";
         
         try{          
           
           Integer randomSelectedOption = selectedMenuOption;
           
           String selectedOptionHex = null; 
           
           switch(type){
                
                case BIND:
                    response = "<upms>" +
                                "<msg type=\"bind_confirm\">" +
                                "<stack id=\"0\"/>" +
                                "<user id=\"1\"/>" +
                                "</msg>" +
                                "</upms>";
                    break;
                case PING:
                    response = "<upms>" +
                                "<msg type=\"pong\"/>" +
                                "</upms>";
                    break;
                
                case PONG:
                    response = "<upms>" +
                                "<msg type=\"ping\"/>" +
                                "</upms>";
                    break;
                case BEGIN:
                   //randomSelectedOption = getRandomInt(1,2);
                   // Thread.sleep(30000);
                    selectedOptionHex = StringUtil.convertStringToHex(String.valueOf(randomSelectedOption));
                    
                    logger.debug("-----Option Selected----" + randomSelectedOption + "-------for dialogId------"+dialogId);
          
                    response = "<upms>" +
                                "<msg type=\"continue\">" +
                                "<user id=\"1\"/>" +
                                "<dialog id=\""+ dialogId+ "\"/>" +
                                "<user_info>" +
                             //   "<object_identifier v=\"04000001010101\"/>" +
                                "<map operation=\"accept\"/>" +
                                "</user_info>" +
                              //  "<context v=\"04000001001302\"/>" +
                                "<component type=\"return_result_l\" id=\""+compId+ "\" op=\"ussd_request\">" +
                                "<param name=\"data_coding_scheme\" v=\"15\"/>" +
                                "<param name=\"ussd_string\" v='"+selectedOptionHex+"'/>" +
                                "</component>" +
                                "</msg>" +
                                "</upms>";
                    break;
                    case CONTINUE:
                        //Thread.sleep(30000);
                        if(componentType.equals("unstructured_ss_notify")){
                         
                            response = "<upms>" +
                                "<msg type=\"continue\">" +
                                "<user id=\"1\"/>" +
                                "<dialog id=\""+ dialogId+ "\"/>" +
//                                "<user_info>" +
//                                "<object_identifier v=\"04000001010101\"/>" +
//                                "</user_info>" +
//                                "<context v=\"04000001001302\"/>" +
                                "<component type=\"return_result_l\" id=\""+compId+"\"/>" +
//                                "<param name=\"data_coding_scheme\" v=\"15\"/>" +
//                                "<param name=\"ussd_string\" v=\"31\"/>" +
//                                "</component>" +
                                "</msg>" +
                                "</upms>";
                        }else{
                           // randomSelectedOption = getRandomInt(1,3);
                            selectedOptionHex = StringUtil.convertStringToHex(String.valueOf(randomSelectedOption));
                            logger.debug("-----Option Selected----" + randomSelectedOption + "-------for dialogId------"+dialogId);
                           
                            response = "<upms>" +
                                "<msg type=\"continue\">" +
                                "<user id=\"1\"/>" +
                                "<dialog id=\""+ dialogId+ "\"/>" +
//                                "<user_info>" +
//                                "<object_identifier v=\"04000001010101\"/>" +
//                                "</user_info>" +
//                                "<context v=\"04000001001302\"/>" +
                                "<component type=\"return_result_l\" id='"+compId+"' op=\"ussd_request\">" +
                                "<param name=\"data_coding_scheme\" v=\"15\"/>" +
                                "<param name=\"ussd_string\" v='"+selectedOptionHex+"'/>" +
                                "</component>" +
                                "</msg>" +
                                "</upms>";
                            
                        }
                       break;
                        
                     case END:
                        response = "";
                        break;
                    default:
            }
         }catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("",e);
          }
            
            return response;
        }
   
    private void sendMsg(String xml){
        try {
		byte[] c = new byte[4];
		//System.out.println(String.format("Sending: %d bytes", xml.length()));		

		int value = xml.length();
		c[0] = (byte) (value & 0xFF);
		c[1] = (byte) ((value >> 8) & 0xFF);
		c[2] = (byte) ((value >> 16) & 0xFF);
		c[3] = (byte) ((value >> 24) & 0xFF);		
		
//		System.out.println(String.format("Sending: %d:%d:%d:%d", (int)c[0], (int)c[1], (int)c[2], (int)c[3]));
		
		// Send packet length
		output.write(c, 0, 4);
				
		writer.write(xml);
		writer.flush();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
    
    public static int getRandomInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
        
    }
    
     public static int selectRandonOption(String menu){
        
      //  String regex = "(?m)^[\\d]";
        
        String regex = "(?m)(^[^\\s]+)"; // regex for matching the first character before space
        
        Matcher matcher = Pattern.compile(regex).matcher(menu);
        
        List<Integer> optionList = new ArrayList<Integer>();
        
        System.out.println("Options in menu:");
        
        while(matcher.find()){
        String option = matcher.group(0).replaceAll("\\D", "");
            if(option != null && !option.isEmpty()){
                System.out.println("------"+option+"----");
                optionList.add(Integer.valueOf(option));
            }
        }
        
        
        Random rand = new Random();

        int index = rand.nextInt(optionList.size());
        int optionSelected = optionList.get(index);
        
        return optionSelected; 
    
    }

}
