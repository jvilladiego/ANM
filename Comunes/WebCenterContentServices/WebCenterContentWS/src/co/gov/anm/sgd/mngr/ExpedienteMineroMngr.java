package co.gov.anm.sgd.mngr;

import co.gov.anm.sgd.exception.WSException;
import co.gov.anm.sgd.integration.location.ServiceLocator;
import co.gov.anm.sgd.response.SearchResponse;
import co.gov.anm.sgd.response.WCCResponse;
import co.gov.anm.sgd.transfer.Credenciales;
import static co.gov.anm.sgd.util.Constants.ERWCC03;
import static co.gov.anm.sgd.util.Constants.EXPEDIENTE;
import static co.gov.anm.sgd.util.Constants.OK;
import static co.gov.anm.sgd.util.Constants.WCC_BPM_PASSWORD;
import static co.gov.anm.sgd.util.Constants.WCC_BPM_USER;
import co.gov.anm.sgd.util.PropertiesLoader;
import co.gov.anm.sgd.util.Utilities;

import genericsoap.GenericSoapPortType;

import java.util.Map;

import org.apache.log4j.Logger;


public class ExpedienteMineroMngr {
    
    private static final Logger logger = Logger.getLogger(ExpedienteMineroMngr.class);
    
    private Credenciales credenciales;
    
    public ExpedienteMineroMngr() {
        this.credenciales = new Credenciales();
        this.credenciales.setUsuario(PropertiesLoader.getProperty(WCC_BPM_USER));
        this.credenciales.setContrasena(PropertiesLoader.getProperty(WCC_BPM_PASSWORD));
    }
    
    public WCCResponse validarExpedienteMinero (String nroPlaca) throws WSException, Exception {
        String searchCriteria = null;
        FolderMngr folderMngr = null;
        SearchResponse searchResponse = null;
        WCCResponse response = new WCCResponse();
        Map<String, String> metadataSearch = null;
        
        GenericSoapPortType port = ServiceLocator.getInstance().getGenericUCMPort(credenciales);
        
        searchCriteria = "fFolderName<matches>" + "`" + nroPlaca + "`";
        
        folderMngr = new FolderMngr(port);
        searchResponse = folderMngr.search(searchCriteria);
        
        if (OK.equals(searchResponse.getStatusCode())) {
            
            if (searchResponse.getResultSetList() != null) {
                logger.debug("encuentra coincidencias");
                
                metadataSearch = Utilities.validateRecordTaxonomy(searchResponse.getResultSetList(), EXPEDIENTE);
                
                //Valida que el expediente sea un titulo minero
                if (metadataSearch == null) {
                    response.setStatusCode(ERWCC03);
                    response.setStatusMessage(PropertiesLoader.getProperty(ERWCC03));
                } else {
                    response.setStatusCode(OK);
                    response.setStatusMessage("Success");
                }
            } else {
                response.setStatusCode(ERWCC03);
                response.setStatusMessage(PropertiesLoader.getProperty(ERWCC03));
            }
        }
        
        return response;
    }
}
