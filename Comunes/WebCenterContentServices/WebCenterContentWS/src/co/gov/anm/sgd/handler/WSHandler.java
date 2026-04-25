package co.gov.anm.sgd.handler;

import java.io.ByteArrayOutputStream;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

public class WSHandler implements SOAPHandler<SOAPMessageContext> {
    
    public static final Logger logger = Logger.getLogger(WSHandler.class);

    public Set<QName> getHeaders() {
            return Collections.emptySet();
    }

    public boolean handleMessage(SOAPMessageContext messageContext) {
            Boolean outboundProperty = (Boolean) messageContext
                            .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

            if (outboundProperty.booleanValue()) {
                logger.debug("Direction=outbound (handleMessage)");
                try {
                    logger.debug("messageContext: "+messageContext.getMessage().getSOAPBody().getTextContent());
                } catch (Exception e) {
                    logger.debug("Error obteniendo SOA Body:" + e);
                }
                    
                SOAPMessage msg = ((SOAPMessageContext) messageContext).getMessage();
                dumpSOAPMessage(msg);
            } else {
                logger.debug("Direction=inbound (handleMessage)");
                try {
                    logger.debug("messageContext: "+messageContext.getMessage().getSOAPBody().getTextContent());
                } catch (Exception e) {
                    logger.debug("Error obteniendo SOA Body:" + e);
                }
                    
                SOAPMessage msg = messageContext.getMessage();
                dumpSOAPMessage(msg);

            }
            SOAPMessage soapMsg = messageContext.getMessage();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                    soapMsg.writeTo(out);
                    logger.debug("Mensaje SOAP:" + out.toString());
            } catch (Exception e) {
                    logger.debug("Error obteniendo mensaje SOAP:" + e);
            }
            return true;
    }

    public boolean handleFault(SOAPMessageContext messageContext)
                    throws java.lang.RuntimeException {
            
            boolean outbound = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    
            if (outbound) {
                    logger.debug("Direction=outbound (handleFault)");
            } else {
                    logger.debug("Direction=inbound (handleFault)");
            }
    
            if (!outbound) {        
                    try {
                            SOAPMessage msg = ((SOAPMessageContext) messageContext).getMessage();
    
                            dumpSOAPMessage(msg);
    
                            if (messageContext.getMessage().getSOAPBody().getFault() != null) {
                                    String detailName = null;
    
                                    try {
                                            detailName = messageContext.getMessage().getSOAPBody().
                                                    getFault().getDetail().getFirstChild().getLocalName();
    
                                            logger.debug("detailName=" + detailName);
                                    } catch (Exception e) {
                                            logger.debug("Error obteniendo mensaje SOAP:" + e);
                                    }
                            }       
                    } catch (Exception e) {
                            logger.debug("Error obteniendo mensaje SOAP:" + e);
                    }
            }       
            return true;
    }
    
    /**
    * Dump SOAP Message to console
    *
    * @param msg
    */
    private void dumpSOAPMessage(SOAPMessage msg) {
            if (msg == null) {
                logger.debug("SOAP Message is null");
                return;
            }
            logger.debug("--------------------");
            logger.debug("DUMP OF SOAP MESSAGE");
            logger.debug("--------------------");
    
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                msg.writeTo(baos);
                logger.debug(msg.getSOAPHeader().getTextContent());
                logger.debug(baos.toString(getMessageEncoding(msg)));
                // show included values 
                String values = msg.getSOAPBody().getTextContent();
                logger.debug("Included values:" + values);
                logger.debug("--------------------");
            } catch (Exception e) {
                    logger.error("Error: " + e.getMessage(), e);
            }
    }
    
    /**
    * Returns the message encoding (e.g. utf-8)
    *
    * @param msg
    * @return
    * @throws javax.xml.soap.SOAPException
    */
    private String getMessageEncoding(SOAPMessage msg) throws SOAPException {
            String encoding = "utf-8";
    
            if (msg.getProperty(SOAPMessage.CHARACTER_SET_ENCODING) != null) {
                    encoding = msg.getProperty(SOAPMessage.CHARACTER_SET_ENCODING).toString();
            }
            return encoding;
    }

    public void close(MessageContext messageContext) {
    }
}
