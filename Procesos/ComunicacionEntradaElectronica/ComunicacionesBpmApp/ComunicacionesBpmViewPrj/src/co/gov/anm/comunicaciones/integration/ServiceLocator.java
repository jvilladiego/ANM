package co.gov.anm.comunicaciones.integration;

import co.gov.anm.sgd.service.ExpedienteMinero;
import co.gov.anm.sgd.service.ExpedienteMineroPortBindingQSService;

import java.net.URL;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;


public class ServiceLocator {
    
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
    
    public ExpedienteMinero getExpMineroPort (String urlWSDL) {
        ExpedienteMinero port = null;
        
        try {
            URL url = new URL(urlWSDL);
            
            Service service = new ExpedienteMineroPortBindingQSService(url, new QName("http://service.sgd.anm.gov.co", "ExpedienteMineroPortBindingQSService"));
            
            port = service.getPort(ExpedienteMinero.class);
            
            Map<String, Object> requestContext = ((BindingProvider)port).getRequestContext();
            
            //Define tiempos de conexión y ejecución
            //requestContext.put(com.sun.xml.ws.client.BindingProviderProperties.CONNECT_TIMEOUT, 3000);
            //srequestContext.put(com.sun.xml.ws.client.BindingProviderProperties.REQUEST_TIMEOUT, 5000);            
            
            System.setProperty("javax.net.debug", "all");
            System.setProperty("xml.debug.verify", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return port;
    }
}
