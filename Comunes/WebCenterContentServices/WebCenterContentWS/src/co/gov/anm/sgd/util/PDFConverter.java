package co.gov.anm.sgd.util;

import co.gov.anm.sgd.integration.listener.WCCContextListener;

import org.apache.log4j.Logger;
import java.io.File;
import static co.gov.anm.sgd.util.Constants.*;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

/**
 *
 *
 * Se implementa el patron singleton para uso del API JOD para generacion
 * de PDFs a partir de documentos en word o odt.
 *
 * Se inicia como un proceso unico en la JVM para reutilizarse cada vez que se invoca
 *
 * @author jairo.gutierrez@oracle.com
 */
public class PDFConverter {
    
    public static final Logger logger = Logger.getLogger(PDFConverter.class);
    private OfficeDocumentConverter converter = null;
    private OfficeManager officeManager = null;
    
    private static PDFConverter INSTANCE = null;
    
    private PDFConverter() {
        
    }
    
    public static PDFConverter getInstance ( ) throws Exception{
        if ( INSTANCE==null ){
            logger.debug("Inicializando el servicio de conversion de PDFs");
            String libreOfficePath = PropertiesLoader.getProperty(LIBRE_OFFICE_PATH);        
            String portsList = PropertiesLoader.getProperty(JOD_PORT_NUMBERS);        
            Long queueTimeout = getLongProperty(JOD_TASK_QUEUE_TIMEOUT);        
            Long execTimeout = getLongProperty(JOD_TASK_EXEC_TIMEOUT);        
            Integer taskPerProcess = getIntegerProperty(JOD_TASK_PER_PROCESS);        
            Long retryTimeout = getLongProperty(JOD_RETRY_TIMEOUT);        
            
            String[] portsArray = portsList.split(";");
            int ports[] = new int[portsArray.length];
            try{
                for ( int i=0;i<portsArray.length;i++ ){
                    ports[i] = Integer.valueOf(portsArray[i]);
                }
            }catch ( Exception e ){
                logger.error("No se definio correctamente la propiedad: "+JOD_PORT_NUMBERS,e);
                throw e;
            }
            INSTANCE = new PDFConverter();
            INSTANCE.officeManager = new DefaultOfficeManagerConfiguration()
                                            .setOfficeHome(new File(libreOfficePath))
                                            .setPortNumbers(ports) 
                                            .setTaskQueueTimeout(queueTimeout) 
                                            .setTaskExecutionTimeout(execTimeout) 
                                            .setMaxTasksPerProcess(taskPerProcess) 
                                            .setRetryTimeout(retryTimeout) 
                                            .buildOfficeManager();
            
            INSTANCE.officeManager.start();
            logger.debug("Inicializando el servicio de conversion de PDFs completa");            
            INSTANCE.converter = new OfficeDocumentConverter(INSTANCE.officeManager);
        }
        return INSTANCE;
    }
    
    public void removeInstance ( ){
        logger.debug("Deteniendo el servicio de conversion de PDFs");            
        INSTANCE.officeManager.stop();
    }
    
    private static Long getLongProperty ( String prop ) throws Exception{
        String propValue = PropertiesLoader.getProperty(prop); 
        Long result = null;
        try{
            result = Long.valueOf(propValue);
        }catch ( Exception e ){
            logger.error("La propiedad "+prop+" no tiene un valor numerico valido");
            throw e;
        }
        return result;        
    }
    
    
    private static Integer getIntegerProperty ( String prop ) throws Exception{
        String propValue = PropertiesLoader.getProperty(prop); 
        Integer result = null;
        try{
            result = Integer.valueOf(propValue);
        }catch ( Exception e ){
            logger.error("La propiedad "+prop+" no tiene un valor numerico entero valido");
            throw e;
        }
        return result;        
    }
    
    public void convertirAPdf ( String sourceFile, String resultFile ) throws Exception{
        logger.debug("Inicia conversion de: "+sourceFile);
        converter.convert(new File(sourceFile), new File(resultFile));
        logger.debug("Finaliza conversion de: "+sourceFile+" a: "+resultFile);
    }
    
    
}
