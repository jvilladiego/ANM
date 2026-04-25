package co.gov.anm.sgd.integration.location;

import co.gov.anm.sgd.exception.WSException;
import co.gov.anm.sgd.handler.WSHandlerResolver;
import co.gov.anm.sgd.transfer.Credenciales;
import co.gov.anm.sgd.util.Constants;
import static co.gov.anm.sgd.util.Constants.ERWCC08;
import static co.gov.anm.sgd.util.Constants.ERWCC09;
import co.gov.anm.sgd.util.PropertiesLoader;

import genericsoap.GenericSoapPortType;
import genericsoap.GenericSoapService;

import java.net.ConnectException;
import java.net.URL;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

import org.apache.log4j.Logger;

import weblogic.wsee.jws.jaxws.owsm.SecurityPoliciesFeature;

public class ServiceLocator {

    private static final Logger logger = Logger.getLogger(ServiceLocator.class);

    private static ServiceLocator instance;


    /**
     * M&eacute;todo que implementa el patr&oacute;n <i>Singleton</i> para
     * compartir una instancia de la clase <code>ServiceLocator</code>.
     *
     * @return
     */
    static public ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }

        return instance;
    }

    public GenericSoapPortType getGenericUCMPort (Credenciales credenciales) throws WSException {
        GenericSoapPortType port = null;
            
        try {
            URL url = new URL(PropertiesLoader.getProperty(Constants.URL_WSDL_GENERIC));
            Service service = new GenericSoapService(url, new QName("urn:GenericSoap", "GenericSoapService"));
                
            service.setHandlerResolver(new WSHandlerResolver());  
                
            //Configure the policy that has been applied to the Web Service
            /*SecurityPoliciesFeature securityFeatures = new SecurityPoliciesFeature(new String[] 
                {"oracle/wss11_username_token_with_message_protection_client_policy"});*/
            
            SecurityPoliciesFeature securityFeatures = new SecurityPoliciesFeature(new String[] 
                {"oracle/wss_username_token_client_policy"});
                
            //instantiate a GenericSoapPortType object with security policies attached.
            port = service.getPort(GenericSoapPortType.class, securityFeatures);
                
            //System.setProperty("oracle.security.jps.config", PropertiesLoader.getProperty(Constants.JPS_PATH));
            Map<String, Object> requestContext = ((BindingProvider)port).getRequestContext();
                
            requestContext.put(BindingProvider.USERNAME_PROPERTY, credenciales.getUsuario());
            requestContext.put(BindingProvider.PASSWORD_PROPERTY, credenciales.getContrasena());
                
            //Define tiempos de conexión y ejecución
            requestContext.put(com.sun.xml.ws.client.BindingProviderProperties.CONNECT_TIMEOUT, 
                Integer.parseInt(PropertiesLoader.getProperty(Constants.WCC_CONN_TIMEOUT)));
            requestContext.put(com.sun.xml.ws.client.BindingProviderProperties.REQUEST_TIMEOUT, 
                Integer.parseInt(PropertiesLoader.getProperty(Constants.WCC_READ_TIMEOUT)));
                
            /*requestContext.put(ClientConstants.WSSEC_KEYSTORE_LOCATION, PropertiesLoader.getProperty(Constants.KEYSTORE_PATH));
            requestContext.put(ClientConstants.WSSEC_KEYSTORE_PASSWORD, PropertiesLoader.getProperty(Constants.KEYSTORE_PASSWORD));
            requestContext.put(ClientConstants.WSSEC_KEYSTORE_TYPE, "JKS");
            requestContext.put(ClientConstants.WSSEC_SIG_KEY_ALIAS, PropertiesLoader.getProperty(Constants.KEY_ALIAS));
            requestContext.put(ClientConstants.WSSEC_SIG_KEY_PASSWORD, PropertiesLoader.getProperty(Constants.KEY_PASSWORD));*/
                
            System.setProperty("javax.net.debug", "all");
            System.setProperty("xml.debug.verify", "true");
        } catch (WebServiceException e) {
            logger.error("Error conectandose a webcenter content: " + e, e);           
                    
            if (e.getCause() instanceof ConnectException) {
                throw new WSException(ERWCC09, e);               
            }
            throw new WSException(ERWCC08, e);                  
        } catch (Exception e) {
            logger.error("Error creando el puerto del ws: " + e, e);                 
                
            throw new WSException(ERWCC08, e);
        }
        return port;
    }
}
