package co.gov.anm.sgd.util;

import java.io.UnsupportedEncodingException;

import java.text.MessageFormat;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class Mail {

    private static final String username    = PropertiesLoader.getProperty(Constants.MAIL_ACCOUNT_USERNAME);
    private static final String password    = PropertiesLoader.getProperty(Constants.MAIL_ACCOUNT_PASSWORD);
    private static final String auth        = PropertiesLoader.getProperty(Constants.MAIL_SMTP_AUTH);
    private static final String tls         = PropertiesLoader.getProperty(Constants.MAIL_SMTP_TLS_ENABLE);
    private static final String host        = PropertiesLoader.getProperty(Constants.MAIL_SMTP_HOST);
    private static final String port        = PropertiesLoader.getProperty(Constants.MAIL_SMTP_PORT);
    
    public static String formatText(String text, String... args){
        return MessageFormat.format(text, args); 
        }

    public static void enviarCorreo(String correo, String mensaje, String asunto) throws AddressException,
                                                                                         javax.mail.MessagingException {
        
        System.out.println("auth : "+auth+" tls : "+tls+" host : "+host+" port : "+port);
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", tls);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(username, "Agencia Nacional de Minería"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correo.trim()));
        
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(mensaje, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        
        message.setContent(multipart, "text/html; charset=UTF-8");
        message.setSubject(asunto);
        
        Transport.send(message);
    }

}
