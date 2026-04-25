package co.gov.anm.sgd.integration.listener;

import co.gov.anm.sgd.util.PDFConverter;
import co.gov.anm.sgd.util.PropertiesLoader;
import co.gov.anm.sgd.util.Utilities;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * La clase <code>WCCContextListener</code> inicializa los par&aacute;metros
 * b&aacute;sicos de la aplicaci&oacute;n.
 *
 * @author Jainer Eduardo Quiceno Rojas [jaine.quiceno@oracle.com]
 */
public class WCCContextListener implements ServletContextListener {
    
    public static final Logger logger = Logger.getLogger(WCCContextListener.class);
    
    public WCCContextListener() {
        super();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String propPath = null;
        String errorPath = null;
        String log4jPath = null;
        
        //carga archivos de propiedades
        try {
            logger.info("Listener starting");
            
            javax.servlet.ServletContext serveletContext = servletContextEvent.getServletContext();
                   
            propPath = serveletContext.getInitParameter("PROP_PATH");
            errorPath = serveletContext.getInitParameter("ERROR_PATH");
            log4jPath = serveletContext.getInitParameter("LOG4J_PATH");
            
            logger.info("Configuring properties files");
            PropertyConfigurator.configure(log4jPath);
            
            PropertiesLoader.load(propPath, errorPath);
            
            //carga propiedades
            try {
                Utilities.loadAppProperties();
            } catch (Exception e) {
                logger.error("Error Inesperado cargando propiedades " + e.getMessage(),e);  
            }
            PDFConverter.getInstance();
        } catch (Exception e) {
            logger.error("Error Inesperado cargando archivos de propiedades " + e.getMessage(),e);  
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try{
            PDFConverter.getInstance().removeInstance();    
        }catch(Exception e){
            logger.error(e);
        }
        
    }
}
