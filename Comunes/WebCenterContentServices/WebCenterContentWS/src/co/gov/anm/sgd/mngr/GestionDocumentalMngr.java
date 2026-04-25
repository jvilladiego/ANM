package co.gov.anm.sgd.mngr;

import co.gov.anm.sgd.bean.ComunicacionesLocal3;
import co.gov.anm.sgd.bean.WCContentLocal3;
import co.gov.anm.sgd.entity.UnidadAdministrativa;
import co.gov.anm.sgd.exception.WSException;
import co.gov.anm.sgd.integration.location.ServiceLocator;
import co.gov.anm.sgd.response.BrowseResponse;
import co.gov.anm.sgd.response.DocumentDetailResponse;
import co.gov.anm.sgd.response.FileResponse;
import co.gov.anm.sgd.response.FolderResponse;
import co.gov.anm.sgd.response.SearchResponse;
import co.gov.anm.sgd.response.WCCResponse;
import co.gov.anm.sgd.transfer.CheckinDocument;
import co.gov.anm.sgd.transfer.ComunicacionRequest;
import co.gov.anm.sgd.transfer.Credenciales;
import co.gov.anm.sgd.transfer.FileTransfer;
import co.gov.anm.sgd.transfer.FolderComDTO;
import co.gov.anm.sgd.transfer.TipificacionAnexosRequest;
import co.gov.anm.sgd.util.Constants;
import static co.gov.anm.sgd.util.Constants.ERWCC00;
import static co.gov.anm.sgd.util.Constants.ERWCC03;
import static co.gov.anm.sgd.util.Constants.ERWCC21;
import static co.gov.anm.sgd.util.Constants.ERWCC22;
import static co.gov.anm.sgd.util.Constants.EXPEDIENTE;
import static co.gov.anm.sgd.util.Constants.OK;
import static co.gov.anm.sgd.util.Constants.PATH_CONS_COM;
import static co.gov.anm.sgd.util.Constants.PATH_PQRS;
import static co.gov.anm.sgd.util.Constants.URL_IDC;
import static co.gov.anm.sgd.util.Constants.WCC_BPM_PASSWORD;
import static co.gov.anm.sgd.util.Constants.WCC_BPM_USER;
import static co.gov.anm.sgd.util.Constants.anmReferencia;
import static co.gov.anm.sgd.util.Constants.anmTipoDocumental;
import static co.gov.anm.sgd.util.Constants.anmTramite;
import static co.gov.anm.sgd.util.Constants.anmUnidadAdministrativa;
import static co.gov.anm.sgd.util.Constants.dID;
import static co.gov.anm.sgd.util.Constants.docAccount;
import static co.gov.anm.sgd.util.Constants.docAuthor;
import static co.gov.anm.sgd.util.Constants.docName;
import static co.gov.anm.sgd.util.Constants.docTitle;
import static co.gov.anm.sgd.util.Constants.docType;
import static co.gov.anm.sgd.util.Constants.fileGUID;
import static co.gov.anm.sgd.util.Constants.fileType;
import static co.gov.anm.sgd.util.Constants.folderGUID;
import static co.gov.anm.sgd.util.Constants.folderPath;
import static co.gov.anm.sgd.util.Constants.nombreExpediente;
import static co.gov.anm.sgd.util.Constants.parentGUID;
import static co.gov.anm.sgd.util.Constants.parentPath;
import static co.gov.anm.sgd.util.Constants.path;
import static co.gov.anm.sgd.util.Constants.primaryFile;
import static co.gov.anm.sgd.util.Constants.securityGroup;
import static co.gov.anm.sgd.util.Constants.tipoRecursoInformacion;
import co.gov.anm.sgd.util.PropertiesLoader;
import co.gov.anm.sgd.util.Utilities;

import genericsoap.GenericSoapPortType;

import java.io.File;
import java.io.FileNotFoundException;

import java.math.BigDecimal;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class GestionDocumentalMngr {
    
    private static final Logger logger = Logger.getLogger(GestionDocumentalMngr.class);
    
    private Credenciales credenciales;
    
    public GestionDocumentalMngr () {
        this.credenciales = new Credenciales();
        this.credenciales.setUsuario(PropertiesLoader.getProperty(WCC_BPM_USER));
        this.credenciales.setContrasena(PropertiesLoader.getProperty(WCC_BPM_PASSWORD));
    }

    @SuppressWarnings("oracle.jdeveloper.java.nested-assignment")
    public WCCResponse cargarDocumentoGestionDocumental (CheckinDocument checkin, WCContentLocal3 wcContentEjb, 
                                                         ComunicacionesLocal3 comEJB) throws WSException, Exception {
        File file = null;
        FolderMngr folderMngr = null;
        DocumentMngr documentMngr = null;
        WCCResponse wccResponse = null;
        List<UnidadAdministrativa> unidAdminList = null;
        FolderComDTO folderComDTO = new FolderComDTO();
        FolderComDTO expMinData = null;
        Map<String, String> metadataList = null;
        HashMap<String, String> metadataLink = null;
        String urlWCC = null;
        boolean existeLinkTaxonomia = false;
         
        try {             
            GenericSoapPortType port = ServiceLocator.getInstance().getGenericUCMPort(this.credenciales);
            folderMngr = new FolderMngr(port);
            logger.debug("arma metadatos del consecutivo de comunicaciones");
            folderComDTO = createFolderCom("CONSECUTIVO", PropertiesLoader.getProperty(PATH_CONS_COM), folderMngr);
            unidAdminList = wcContentEjb.getUnidadAdministrativaFindByCod(new BigDecimal(checkin.getUnidadAdministrativa()));
            
            logger.debug("checkin.getTipoDocumental(): "+checkin.getTipoDocumental());
            logger.info("checkin.getTipoDocumental(): "+checkin.getTipoDocumental());
            metadataList = new HashMap<String, String>();                            
            metadataList.put(securityGroup, "Public");                               
            metadataList.put(docAccount, checkin.getDocAccount());                               
            metadataList.put(docType, "Document");
            metadataList.put(docAuthor, checkin.getCredenciales().getUsuario());
            metadataList.put(docTitle, checkin.getDocTitle());
            metadataList.put(primaryFile, checkin.getPrimaryFile());
            metadataList.put(anmTramite, checkin.getTramite());
            metadataList.put(anmUnidadAdministrativa, unidAdminList.get(0).getIdUnidadadministrativa().toString());
            metadataList.put(anmTipoDocumental, checkin.getTipoDocumental());
            metadataList.put(anmReferencia, checkin.getNroRadicado());
            metadataList.put(folderPath, folderComDTO.getFolderPath());   
            metadataList.put(folderGUID, folderComDTO.getFolderGuid());
            metadataList.put(tipoRecursoInformacion, "2");
            
            if (checkin.getNroPlaca() != null) {
                metadataList.put(nombreExpediente, checkin.getNroPlaca());
            }
                
            file = new File(checkin.getPrimaryFile());
            documentMngr = new DocumentMngr(port);   
                
            FileTransfer fileTransfer = new FileTransfer();
            fileTransfer.setContentType(Files.probeContentType(Paths.get(file.getPath())));
            fileTransfer.setFile(file);
            fileTransfer.setFilename(file.getName());
            fileTransfer.setCredenciales(this.credenciales);
            
            wccResponse = documentMngr.checkinNew(metadataList, fileTransfer);   
            logger.debug("hace checkin en carpeta de comunicaciones");
                
            if (!OK.equals(wccResponse.getStatusCode())) {
                logger.debug("error realizando checkin en comunicaciones: " + wccResponse.getStatusMessage());
                wccResponse.cleanMetaData();
                return wccResponse;
            } 
                
            logger.debug("Metadatos del documento chequeado:");
            for ( Map.Entry ent:wccResponse.getMetaDataFields().entrySet() ){
                logger.debug(ent.getKey()+": "+ent.getValue());
            }
                
            logger.debug("creo link en carpeta de comunicaciones");
                
            metadataLink = new HashMap<String, String>();  
            metadataLink.put(fileType, "soft");
            metadataLink.put(docName, wccResponse.getMetaDataFields().get(docName));
            
            if (checkin.isPQRS()) {
                logger.debug("hace checkin en carpeta de PQRS");
                folderComDTO = createFolderCom("PQRS", PropertiesLoader.getProperty(PATH_PQRS), folderMngr);
                
                metadataLink.put(parentGUID, folderComDTO.getFolderGuid());
                wccResponse = folderMngr.createFileLinkInFolder(metadataLink);  
                
                if (!OK.equals(wccResponse.getStatusCode())) {
                    wccResponse.cleanMetaData();
                    return wccResponse;
                }     
            }
                
            //Crear link en la carpeta Expediente Minero si aplica, de lo contrario lo crea en otra parte
            //de la taxonomia
            if (checkin.isExpMinero() && (expMinData = obtenerIdExpMinero(checkin.getNroPlaca(), folderMngr)) != null) {     
                logger.debug("crea link en carpeta de Expediente Minero");
                existeLinkTaxonomia = true;
                metadataLink.put(parentGUID, expMinData.getFolderGuid());             
            } else if (checkin.getFolderGUID() != null && !checkin.getFolderGUID().trim().equals("")) {
                logger.debug("crea link en otra parte de la taxonomia");
                existeLinkTaxonomia = true;
                metadataLink.put(parentGUID, checkin.getFolderGUID());
            }
            
            if (existeLinkTaxonomia) {
                wccResponse = folderMngr.createFileLinkInFolder(metadataLink); 
                if (!OK.equals(wccResponse.getStatusCode())) {
                    wccResponse.cleanMetaData();
                    return wccResponse;
                }        
            }
            
            if (checkin.isDocumentoPrincipal() && (checkin.getFolderGUID() != null && !checkin.getFolderGUID().trim().equals(""))) {   
                logger.debug("es documento principal, actualiza registro en BD");
                actualizarSeriesBD(checkin.getNroRadicado(), wccResponse.getMetaDataFields().get(parentPath).split("/"), 
                                   checkin.getCredenciales().getUsuario(), comEJB);
            }
            
            logger.debug("consulta informaci�n del documento");
            DocumentDetailResponse detailResponse = documentMngr.docInfoByName(wccResponse.getMetaDataFields().get(docName));                    
            
            if (OK.equals(detailResponse.getStatusCode())) {
                logger.debug("Respuesta servicio de busqueda OK");
                urlWCC = PropertiesLoader.getProperty(URL_IDC) + "?" + "IdcService=GET_FILE" + "&" +
                    dID + "=" + detailResponse.getMetaDataFields().get(dID) + "&" + 
                    docName + "=" + detailResponse.getMetaDataFields().get(docName);
                
                wccResponse.setStatusMessage(urlWCC);
            } else {
                logger.debug("Servicio Search NO encontro resultados");
                detailResponse.cleanMetaData();
                return detailResponse;
            }
            
            wccResponse.cleanMetaData(); 
            logger.debug("respuesta checkin: " + wccResponse.getStatusCode() + ": " + wccResponse.getStatusMessage());
            return wccResponse;
            
        } catch (WSException e) {
            logger.debug(checkin.getNroRadicado() + "--> Excepcion del servicio web: " + e.getMessage(), e);
            return new WCCResponse(e.getCode());
        } catch (FileNotFoundException e) {
            logger.debug(checkin.getNroRadicado() + "--> Archivo no encontrado: " + e.getMessage(), e);
            return new WCCResponse(ERWCC22);    
        } catch (Exception e) {
            logger.error(checkin.getNroRadicado() + "--> Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            return new WCCResponse(ERWCC00);
        }
    }
    
    
    
    
    public WCCResponse checkinplantilla (CheckinDocument checkin) throws WSException, Exception {
        File file = null;
        FolderMngr folderMngr = null;
        DocumentMngr documentMngr = null;
        WCCResponse wccResponse = null;
        BrowseResponse browseResponse = null;
        Map<String, String> metadataList = null;
        Map<String, String> metadataBrowse = null;
        String folderPathPlantillas = null;
        String folderGUIDPlantiilas = null;
        String urlWCC = null;
        
        try {            
            GenericSoapPortType port = ServiceLocator.getInstance().getGenericUCMPort(this.credenciales);
            
            folderPathPlantillas = PropertiesLoader.getProperty(Constants.PATH_PLANTILLAS);
            
            //Busca el id de la carpeta de plantillas
            metadataBrowse = new HashMap<String, String>();
            metadataBrowse.put(path, folderPathPlantillas);
           
            folderMngr = new FolderMngr(port);
            browseResponse = folderMngr.browse(metadataBrowse);
           
            if (OK.equals(browseResponse.getStatusCode())) {    
                folderGUIDPlantiilas = browseResponse.getMetaDataFields().get(folderGUID);
            } else {
                browseResponse.cleanMetaData();
                return browseResponse;
            }
            
            metadataList = new HashMap<String, String>();                            
            metadataList.put(securityGroup, "Public");                               
            metadataList.put(docAccount, checkin.getDocAccount());                               
            metadataList.put(docType, "Document");
            metadataList.put(docAuthor, checkin.getCredenciales().getUsuario());
            metadataList.put(docTitle, checkin.getDocTitle());
            metadataList.put(primaryFile, checkin.getPrimaryFile());
            metadataList.put(anmUnidadAdministrativa, "30");
            metadataList.put(folderPath, folderPathPlantillas);   
            metadataList.put(folderGUID, folderGUIDPlantiilas);
            metadataList.put(tipoRecursoInformacion, "2");
            
           file = new File(checkin.getPrimaryFile());
               
           documentMngr = new DocumentMngr(port);   
               
           FileTransfer fileTransfer = new FileTransfer();
           fileTransfer.setContentType(Files.probeContentType(Paths.get(file.getPath())));
           fileTransfer.setFile(file);
           fileTransfer.setFilename(file.getName());
           fileTransfer.setCredenciales(this.credenciales);
           
           logger.debug("hace checkin en carpeta de plantillas");
                           
           wccResponse = documentMngr.checkinNew(metadataList, fileTransfer);   
               
           if (!OK.equals(wccResponse.getStatusCode())) {
               logger.debug("error realizando checkin en comunicaciones: " + wccResponse.getStatusMessage());
           } else {
               browseResponse = folderMngr.browse(metadataBrowse);
               
               if (OK.equals(browseResponse.getStatusCode())) {    
                   if (browseResponse.getFileList() != null) {
                       for (FileResponse fileResponse : browseResponse.getFileList()) {
                           
                           if (fileResponse.getDocName().equals(wccResponse.getMetaDataFields().get(docName))) {
                               logger.debug("encontr� archivo");
                               
                               urlWCC = PropertiesLoader.getProperty(URL_IDC) + "?" + "IdcService=GET_FILE" + "&" +
                                   dID + "=" + fileResponse.getDID() + "&" + docName + "=" + fileResponse.getDocName();
                               
                               wccResponse.setStatusMessage(urlWCC);
                           }
                       }   
                   }
               } else {
                   browseResponse.cleanMetaData();
                   return browseResponse;
               }
           }
            wccResponse.cleanMetaData();
            return wccResponse;
        } catch (WSException e) {
            return new WCCResponse(e.getCode());
        } catch (FileNotFoundException e) {
            return new WCCResponse(ERWCC22);    
        } catch (Exception e) {
            logger.error("Error en la ejecucion del servicio: " + e.getMessage(), e);            
            return new WCCResponse(ERWCC00);
        }           
    }
    
    
    

    @SuppressWarnings("oracle.jdeveloper.java.nested-assignment")
    public WCCResponse tipificarAnexosComunicacion (TipificacionAnexosRequest request, WCContentLocal3 wcContentEjb, ComunicacionesLocal3 comEJB) throws Exception {
        String searchCriteria = null;
        DocumentMngr documentMngr = null;
        FolderMngr folderMngr = null;
        SearchResponse searchResponse = null;
        FolderComDTO folderComDTO = null;
        FolderComDTO folderPQRSDTO = null;
        FolderComDTO expMinData = null;
        List<UnidadAdministrativa> unidAdminList = null;
        WCCResponse wccResponse = null;
        Map<String, String> metadataEdit = null;
        HashMap<String, String> metadataLink = null;
        String urlWCC = null;
        int docNumber = 0;
        boolean linkOtrasSeries = false;
        
        try {
            GenericSoapPortType port = ServiceLocator.getInstance().getGenericUCMPort(this.credenciales);
            folderMngr = new FolderMngr(port);
           
            //obtiene datos de la carpeta de comunicaciones
            folderComDTO = createFolderCom("CONSECUTIVO", PropertiesLoader.getProperty(PATH_CONS_COM), folderMngr);
            if (request.isPQRS()) {
                //obtiene datos de la carpeta de PQRS
                folderPQRSDTO = createFolderCom("PQRS", PropertiesLoader.getProperty(PATH_PQRS), folderMngr);
            }
           
            searchCriteria = "xANM_Referencia<matches>" + "`" + request.getNroRadicado() + "`<and>dDocType<matches>" +
                             "`Comunicacion`";
            documentMngr = new DocumentMngr(port);
            searchResponse = documentMngr.getSearchResults(searchCriteria); 
            folderMngr = new FolderMngr(port);
            
            logger.debug("tipificarAnexosComunicacion -> searchResponse status: "+searchResponse.getStatusCode());
                if(searchResponse.getResultSetList()!=null){
                    logger.debug("tipificarAnexosComunicacion -> searchResponse result size: "+searchResponse.getResultSetList().size());
                }else{
                    logger.debug("tipificarAnexosComunicacion -> searchResponse resultset is null");
                }
            if (OK.equals(searchResponse.getStatusCode()) && searchResponse.getResultSetList() != null) {
                unidAdminList = wcContentEjb.getUnidadAdministrativaFindByCod(new BigDecimal(request.getIdDependenciaOrigen()));
                
                logger.debug("documentos para tipificar: " + searchResponse.getResultSetList().size());
                
                for (Map<String, String> resultSet : searchResponse.getResultSetList()) {    
                    logger.debug("tipificarAnexosComunicacion -> Tipifica documento: " + resultSet.get("dOriginalName"));
                    logger.debug("tipificarAnexosComunicacion -> docName: " + resultSet.get(docName));
                    imprimirMapa(resultSet);
                    
                    //crea link primero en comunicaciones
                    metadataLink = new HashMap<String, String>();                      
                    metadataLink.put(parentGUID, folderComDTO.getFolderGuid());
                    metadataLink.put(fileType, "owner");
                    metadataLink.put(docName, resultSet.get(docName));
                    
                    wccResponse = folderMngr.createFileLinkInFolder(metadataLink);     
                    logger.debug("tipificarAnexosComunicacion ->  createFileLinkInFolder status/message: " + wccResponse.getStatusCode()+" / "+wccResponse.getStatusMessage());
                    
                    if (!OK.equals(wccResponse.getStatusCode())) {
                        wccResponse.cleanMetaData();
                        return wccResponse;
                    }  
                    
                    logger.debug("actualiza metadatos en comunicaciones");
                    metadataEdit = new HashMap<String, String>();      
                    metadataEdit.put(fileGUID, wccResponse.getMetaDataFields().get(fileGUID));
                    metadataEdit.put(folderPath, folderComDTO.getFolderPath());   
                    metadataEdit.put(docType, "Document");
                    metadataEdit.put(docTitle, request.getNroRadicado() + "_" + request.getNombreDoc() + "_" + ++docNumber);
                    metadataEdit.put(anmTipoDocumental, request.getTipoDocumental());
                    metadataEdit.put(anmTramite, request.getTramite());                    
                    metadataEdit.put(anmUnidadAdministrativa, unidAdminList.get(0).getIdUnidadadministrativa().toString());
                    metadataEdit.put(docAuthor, request.getCredenciales().getUsuario());
                    metadataEdit.put(tipoRecursoInformacion, "2");
                    
                    if (request.getNroPlaca() != null) {
                        metadataEdit.put(nombreExpediente, request.getNroPlaca());                        
                    }
                    
                    wccResponse = documentMngr.edit(metadataEdit);
                    
                    if (!OK.equals(wccResponse.getStatusCode())) {
                        wccResponse.cleanMetaData();
                        return wccResponse;
                    }   
                    
                    metadataLink = new HashMap<String, String>();                         
                    metadataLink.put(docName, resultSet.get(docName));
                    metadataLink.put(fileType, "soft");
                    
                    //crea link en la carpeta PQRS
                    if (request.isPQRS()) {
                        logger.debug("crea link en carpeta de PQRS");
                        
                        metadataLink.put(parentGUID, folderPQRSDTO.getFolderGuid());  
                        
                        wccResponse = folderMngr.createFileLinkInFolder(metadataLink);  
                        
                        if (!OK.equals(wccResponse.getStatusCode())) {
                            wccResponse.cleanMetaData();
                            
                            return wccResponse;
                        }     
                    }
                    
                    //crea link del anexo en expediente minero u otra serie
                    if (request.isExpedienteMinero() && (expMinData = obtenerIdExpMinero(request.getNroPlaca(), folderMngr)) != null) {
                        logger.debug("crea link en expediente minero");
                        
                        linkOtrasSeries = true;
                        metadataLink.put(parentGUID, expMinData.getFolderGuid());    
                    } else if (request.getFolderGUID() != null && !request.getFolderGUID().trim().equals("")) {
                        logger.debug("crea link en otra serie");
                        
                        linkOtrasSeries = true;
                        metadataLink.put(parentGUID, request.getFolderGUID());    
                    }
                    
                    if (linkOtrasSeries) {
                        logger.debug("crea link en otras series");
                        wccResponse = folderMngr.createFileLinkInFolder(metadataLink);   
                        
                        if (!OK.equals(wccResponse.getStatusCode())) {
                            wccResponse.cleanMetaData();
                            return wccResponse;
                        }  
                    }
                }
                
                if (request.isDocPpal() && (request.getFolderGUID() != null && !request.getFolderGUID().trim().equals(""))) {
                    logger.debug("actualiza serie y subserie: " + wccResponse.getMetaDataFields().get(parentPath));
                    actualizarSeriesBD(request.getNroRadicado(), wccResponse.getMetaDataFields().get(parentPath).split("/"), 
                                               request.getCredenciales().getUsuario(), comEJB);                                      
                }
                
                //generar link con el ultimo documento tipificado
                logger.debug("consulta informaci�n del documento");
                DocumentDetailResponse detailResponse = documentMngr.docInfoByName(wccResponse.getMetaDataFields().get(docName));                    
                
                if (OK.equals(detailResponse.getStatusCode())) {
                    logger.debug("Respuesta servicio de busqueda OK");
                    urlWCC = PropertiesLoader.getProperty(URL_IDC) + "?" + "IdcService=GET_FILE" + "&" +
                        dID + "=" + detailResponse.getMetaDataFields().get(dID) + "&" + 
                        docName + "=" + detailResponse.getMetaDataFields().get(docName);
                    
                    wccResponse.setStatusMessage(urlWCC);
                } else {
                    logger.debug("Servicio Search NO encontro resultados");
                    detailResponse.cleanMetaData();
                    return detailResponse;
                }
            } else {
                return new WCCResponse(ERWCC21);
            }
           
            wccResponse.cleanMetaData();
            return wccResponse;
        } catch (WSException e) {
            logger.debug(request.getNroRadicado() + "--> Error del servicio web: " + e.getMessage(), e);
            return new WCCResponse(e.getCode());
        } catch (Exception e) {
            logger.error(request.getNroRadicado() + "--> Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            return new WCCResponse(ERWCC00);
        }
    }
        
    private void imprimirMapa(Map<String, String> map){
        logger.debug("imprimirMapa ini: " + map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            logger.debug("[Key] : " + entry.getKey() + " [Value] : " + entry.getValue());
        }
        logger.debug("imprimirMapa fin");
    }
    
    
    
    private FolderComDTO createFolderCom (String folderName, String folderBasePath, FolderMngr folderMngr) throws Exception {
        boolean isFolderCom = false;
        String folderNameCom = null;
        String folderConPath = null;
        String folderConID = null;
        Map<String, String> metadataBrowse = null;
        BrowseResponse browseResponse = null;
        FolderComDTO folderDTO = null;
        WCCResponse wccResponse = null;
        
        folderNameCom = folderName + " " + Utilities.getDate("YYYY");
        
        //Busca la carpeta del consecutivo del a�o en curso
        metadataBrowse = new HashMap<String, String>();
        metadataBrowse.put(path, folderBasePath);
        
        browseResponse = folderMngr.browse(metadataBrowse);
        
        if (OK.equals(browseResponse.getStatusCode())) {    
            if (browseResponse.getFolderList() != null) {
                for (FolderResponse folder : browseResponse.getFolderList()) {
                    
                    if (folder.getName().equals(folderNameCom)) {
                        logger.debug("Encontr� carpeta");
                        
                        folderConPath = PropertiesLoader.getProperty(PATH_CONS_COM) + "/" + folder.getName();
                        folderConID = folder.getId();
                        
                        isFolderCom = true;   
                        
                        break;
                    }
                }   
            }
            
            if (!isFolderCom) {
                //Crea la carpeta correspondiente al consecutivo del a�o
                logger.debug("No se encuentra la carpeta " + folderNameCom + ". Se crear� la carpeta");
                
                wccResponse = folderMngr.create(browseResponse.getMetaDataFields().get(folderGUID), folderNameCom);
                
                if (!OK.equals(wccResponse.getStatusCode())) {
                    wccResponse.cleanMetaData();
                    
                    return (FolderComDTO) wccResponse;
                }     
                folderConID = wccResponse.getMetaDataFields().get(folderGUID);   
                folderConPath = PropertiesLoader.getProperty(PATH_CONS_COM) + "/" + folderNameCom;
            }     
            
            folderDTO = new FolderComDTO();
            folderDTO.setFolderGuid(folderConID);
            folderDTO.setFolderPath(folderConPath);
            
            return folderDTO;
        } else {
            browseResponse.cleanMetaData();
            
            return (FolderComDTO) ((WCCResponse) browseResponse);
        }
    }
    
    
    
    private FolderComDTO obtenerIdExpMinero (String nroPlaca, FolderMngr folderMngr) throws WSException, Exception {
        FolderComDTO folderComDTO = new FolderComDTO();
        SearchResponse searchResponse = null;
        BrowseResponse browseResponse = null;
        Map<String, String> metadataOptional = null;
        Map<String, String> metadataBrowse = null;
        
        //Busca el GUID del expediente         
        String searchCriteria = "fFolderName<matches>" + "`" + nroPlaca + "`";
        
        searchResponse = folderMngr.search(searchCriteria);
        
        if (OK.equals(searchResponse.getStatusCode())) {
            
            if (searchResponse.getResultSetList() != null) {
                logger.debug("encuentra expediente minero");
                
                metadataOptional = Utilities.validateRecordTaxonomy(searchResponse.getResultSetList(), EXPEDIENTE);
                
                //Valida que el expediente sea un titulo minero
                if (metadataOptional == null) {
                    throw new WSException(ERWCC03, PropertiesLoader.getErrorProperty(ERWCC03));
                } else {
                    //busca la carpeta principal del expediente minero
                    metadataBrowse = new HashMap<String, String>();
                    metadataBrowse.put(path, metadataOptional.get(path));
                    
                    browseResponse = folderMngr.browse(metadataBrowse);
                    
                    if (OK.equals(browseResponse.getStatusCode())) {    
                        if (browseResponse.getFolderList() != null) {
                            for (FolderResponse folder : browseResponse.getFolderList()) {
                                
                                if (folder.getName().equals(Utilities.getCuadernosTituloMinero().get(0))) {  
                                    folderComDTO.setFolderGuid(folder.getId());
                                    
                                    break;
                                }
                            }   
                        }
                    }
                }
            } else {
                logger.error(PropertiesLoader.getErrorProperty(ERWCC03));
                return null;
            }
        } else {
            throw new WSException(searchResponse.getStatusCode(), searchResponse.getStatusMessage());
        }
        return folderComDTO;
    }
    
    private void actualizarSeriesBD (String nroRadicado, String[] pathFolders, String usuarioAct, ComunicacionesLocal3 comEJB) {
        String[] serieArray = null;
        String[] subSerieArray = null;
        ComunicacionesMngr comunicacionesMngr = null;
        
        ComunicacionRequest comunicacion = new ComunicacionRequest();
        
        try {
            comunicacion.setNroRadicado(nroRadicado);
            
            for (String folder : pathFolders) {
                if (folder.startsWith("SD")) {
                    serieArray = folder.split("-");
                    
                    comunicacion.setCodigoSerie(serieArray[0]);        
                    comunicacion.setNombreSerie(serieArray[1]);
                }
                if (folder.startsWith("SS")) {
                    subSerieArray = folder.split("-");
                    
                    comunicacion.setCodigoSubSerie(subSerieArray[0]);
                    comunicacion.setNombreSubSerie(subSerieArray[1]);
                }
            }
            
            comunicacion.setFechaModificacion(Calendar.getInstance());
            comunicacion.setUsuarioActualizacion(usuarioAct);
            
            comunicacionesMngr = new ComunicacionesMngr();
            comunicacionesMngr.actualizarComunicacion(comunicacion, comEJB);
        } catch (Exception e) {
            logger.error("error actualizando registros: " + e.getMessage(), e);
        }
    }    
}
