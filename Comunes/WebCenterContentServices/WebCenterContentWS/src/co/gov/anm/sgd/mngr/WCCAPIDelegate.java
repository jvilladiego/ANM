package co.gov.anm.sgd.mngr;

import co.gov.anm.sgd.exception.WSException;
import co.gov.anm.sgd.transfer.FileTransfer;
import static co.gov.anm.sgd.util.Constants.ERWCC13;
import static co.gov.anm.sgd.util.Constants.ERWCC14;
import co.gov.anm.sgd.util.PropertiesLoader;

import com.oracle.ucm.Field;
import com.oracle.ucm.Generic;
import com.oracle.ucm.Service;

import com.sun.xml.ws.client.ClientTransportException;

import genericsoap.GenericSoapPortType;

import java.net.SocketTimeoutException;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class WCCAPIDelegate {
    
    private static final Logger logger  = Logger.getLogger(WCCAPIDelegate.class);
    
    protected GenericSoapPortType port = null;
    protected Generic request = null;
    
    public WCCAPIDelegate (GenericSoapPortType port) {
        request = new Generic();
        request.setWebKey("cs");
        
        this.port = port;
    }  
 
    /**
     * Ejecuta cualquier servicio del API de idc por medio del servicio web Gen&eacute;rico.
     * @param idcService
     * @param metadataMap
     * @return
     */
    public Generic executeWS (String idcService, Map<String, String> metadataMap,
                             FileTransfer fileTransfer) throws WSException {        
        Service svcRequest = new Service();
        Service.Document doc = new Service.Document();
        List<Field> fields = doc.getField();
        
        try {
            request.setService(svcRequest);

            svcRequest.setIdcService(idcService);
            svcRequest.setDocument(doc);
            
            for (Map.Entry<String, String> metadata : metadataMap.entrySet()) {
                Field field = new Field();
                field.setName(metadata.getKey());
                field.setValue(metadata.getValue());

                fields.add(field);
            }
            
            /*if (fileTransfer != null) {
                //create file container object from document object  
                List <com.oracle.ucm.File> f = doc.getFile();
                
                //create file object, not file object is generated from WSDL and not a stadard java.io.File object
                com.oracle.ucm.File pfile = new com.oracle.ucm.File();
                
                pfile.setHref(metadataMap.get(docTitle));
                pfile.setContents(new DataHandler(new ByteArrayDataSource(fileTransfer.getInputStream(), fileTransfer.getContentType())));
                
                //add file object to file container
                f.add(pfile);
            }*/
            
            return port.genericSoapOperation(request);
        } catch (Exception e) {
            logger.error("Error en la invocacion del servicio web: " + e.getMessage(), e);
            
            if (e.getCause() instanceof SocketTimeoutException ||
                e.getCause() instanceof ClientTransportException) {
                
                throw new WSException(ERWCC13, PropertiesLoader.getErrorProperty(ERWCC13));               
            }
            
            throw new WSException(ERWCC14, PropertiesLoader.getErrorProperty(ERWCC14));                              
        }
    }
}
