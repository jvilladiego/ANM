package co.gov.anm.sgd.service;

import co.gov.anm.sgd.bean.ComunicacionesLocal3;
import co.gov.anm.sgd.bean.WCContentLocal3;
import co.gov.anm.sgd.entity.UnidadAdministrativa;
import co.gov.anm.sgd.exception.WSException;
import co.gov.anm.sgd.integration.location.ServiceLocator;
import co.gov.anm.sgd.mngr.DocumentMngr;
import co.gov.anm.sgd.mngr.FolderMngr;
import co.gov.anm.sgd.mngr.GestionDocumentalMngr;
import co.gov.anm.sgd.response.BrowseResponse;
import co.gov.anm.sgd.response.DocumentDetailResponse;
import co.gov.anm.sgd.response.DocumentResponse;
import co.gov.anm.sgd.response.FileResponse;
import co.gov.anm.sgd.response.FolderResponse;
import co.gov.anm.sgd.response.SearchResponse;
import co.gov.anm.sgd.response.WCCResponse;
import co.gov.anm.sgd.transfer.CheckinDocument;
import co.gov.anm.sgd.transfer.Credenciales;
import co.gov.anm.sgd.transfer.FileTransfer;
import co.gov.anm.sgd.transfer.Solicitud;
import co.gov.anm.sgd.transfer.TipificacionAnexosRequest;
import co.gov.anm.sgd.util.Constants;
import static co.gov.anm.sgd.util.Constants.ERWCC00;
import static co.gov.anm.sgd.util.Constants.ERWCC01;
import static co.gov.anm.sgd.util.Constants.ERWCC03;
import static co.gov.anm.sgd.util.Constants.ERWCC05;
import static co.gov.anm.sgd.util.Constants.ERWCC07;
import static co.gov.anm.sgd.util.Constants.ERWCC15;
import static co.gov.anm.sgd.util.Constants.EXPEDIENTE;
import static co.gov.anm.sgd.util.Constants.OK;
import static co.gov.anm.sgd.util.Constants.PATH_CONS_COM;
import static co.gov.anm.sgd.util.Constants.PATH_PQRS;
import static co.gov.anm.sgd.util.Constants.TITULOS;
import static co.gov.anm.sgd.util.Constants.URL_IDC;
import static co.gov.anm.sgd.util.Constants.WCC_BPM_PASSWORD;
import static co.gov.anm.sgd.util.Constants.WCC_BPM_USER;
import static co.gov.anm.sgd.util.Constants.WCC_CMC_PASS;
import static co.gov.anm.sgd.util.Constants.WCC_CMC_USER;
import static co.gov.anm.sgd.util.Constants.anmReferencia;
import static co.gov.anm.sgd.util.Constants.anmTipoDocumental;
import static co.gov.anm.sgd.util.Constants.anmTramite;
import static co.gov.anm.sgd.util.Constants.anmUnidadAdministrativa;
import static co.gov.anm.sgd.util.Constants.comments;
import static co.gov.anm.sgd.util.Constants.dID;
import static co.gov.anm.sgd.util.Constants.docAccount;
import static co.gov.anm.sgd.util.Constants.docAuthor;
import static co.gov.anm.sgd.util.Constants.docName;
import static co.gov.anm.sgd.util.Constants.docTitle;
import static co.gov.anm.sgd.util.Constants.docType;
import static co.gov.anm.sgd.util.Constants.folderGUID;
import static co.gov.anm.sgd.util.Constants.folderName;
import static co.gov.anm.sgd.util.Constants.folderPath;
import static co.gov.anm.sgd.util.Constants.path;
import static co.gov.anm.sgd.util.Constants.primaryFile;
import static co.gov.anm.sgd.util.Constants.securityGroup;
import static co.gov.anm.sgd.util.Constants.tipoRecursoInformacion;
import co.gov.anm.sgd.util.PropertiesLoader;
import co.gov.anm.sgd.util.Utilities;

import genericsoap.GenericSoapPortType;

import java.io.File;

import java.math.BigDecimal;

import java.net.URLEncoder;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.text.MessageFormat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.log4j.Logger;


@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService(name = "Document", serviceName = "Document", targetNamespace = "http://service.sgd.anm.gov.co",
            portName = "DocumentPort", wsdlLocation = "WEB-INF/wsdl/Document.wsdl")
public class Document {
    
    private static Logger logger = Logger.getLogger(Document.class);
    
    private GenericSoapPortType port = null;
    
    @EJB
    private WCContentLocal3 wcContentEjb;
    
    @EJB
    ComunicacionesLocal3 comunicacionesEjb;    

    @WebResult(name = "RespuestaUCM", partName = "respuestaUCM", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public WCCResponse cargarDocumentoAExpedienteMinero (@WebParam(name = "CargueDocumento", partName = "cargueDocumento", targetNamespace = "http://transfer.sgd.anm.gov.co") 
                                                          Solicitud solicitud) throws Exception {
        logger.debug("Inicio servicio cargarDocumentoAExpedienteMinero");
        long starTime = System.currentTimeMillis();
        
        String searchCriteria = null;
        Credenciales credenciales = null;
        FolderMngr folderMngr = null;
        DocumentMngr documentMngr = null;
        SearchResponse searchResponse = null;
        WCCResponse wccResponse = null;
        Map<String, String> metadataList = null;
        Map<String, String> metadataSearch = null;
        boolean existeCuaderno = false;
        String urlWCC = null;
        
        //try {            
            credenciales = new Credenciales();
            credenciales.setUsuario(PropertiesLoader.getProperty(WCC_CMC_USER));
            credenciales.setContrasena(PropertiesLoader.getProperty(WCC_CMC_PASS));
            
            port = ServiceLocator.getInstance().getGenericUCMPort(credenciales);
            
            metadataList = solicitud.getMetadatosList();
            
            Utilities.validateParams(metadataList, Utilities.getParameters(Constants.PARAM_CARGAR_DOC));
            
            //Busca el GUID del expediente         
            searchCriteria = folderName + "<matches>" + "`" + metadataList.get(anmReferencia) + "`";
            
            folderMngr = new FolderMngr(port);
            searchResponse = folderMngr.search(searchCriteria);
            
            if (OK.equals(searchResponse.getStatusCode())) {
                logger.debug("encuentra coincidencias");
                
                if (searchResponse.getResultSetList() != null) {
                    metadataSearch = Utilities.validateRecordTaxonomy(searchResponse.getResultSetList(), TITULOS);
                    
                    //Valida que el expediente sea un titulo minero
                    if (metadataSearch == null) {
                        return new WCCResponse(ERWCC03);
                    }
                } else {
                    return new WCCResponse(ERWCC03);
                }
                
                logger.debug("Busca los cuadernos del expediente");
                
                //Lista los cuadernos del expediente
                searchCriteria = "fParentGUID<matches>" + "`" + metadataSearch.get(folderGUID) + "`";
                
                searchResponse = folderMngr.search(searchCriteria);
                
                if (OK.equals(searchResponse.getStatusCode())) {
                    logger.debug("Valida el cuaderno enviado");                    
                    
                    for (Map<String, String> resultSet : searchResponse.getResultSetList()) {
                        logger.debug("cuaderno: " + resultSet.get(folderName));
                        if (resultSet.get(folderName).equals(metadataList.get(folderName).toUpperCase())) {                
                            
                            metadataList.put(folderPath, resultSet.get(path));    
                            metadataList.put(folderGUID, resultSet.get(folderGUID));  
                            metadataList.put(tipoRecursoInformacion, "3");
                            
                            logger.debug("Encontro cuaderno");
                            existeCuaderno = true;
                            break;                                  
                        }
                    }                        
                    
                    if (existeCuaderno) {
                        documentMngr = new DocumentMngr(port);                                           
                        
                        wccResponse = documentMngr.checkinNew(metadataList, null);   
                        
                        logger.debug("consulta informacion del documento");
                        DocumentDetailResponse detailResponse = documentMngr.docInfoByName(wccResponse.getMetaDataFields().get(docName));                    
                        
                        if (OK.equals(detailResponse.getStatusCode())) {
                            logger.debug("Respuesta servicio de busqueda OK");
                            urlWCC = PropertiesLoader.getProperty(URL_IDC) + "?" + "IdcService=GET_FILE" + "&" +
                                dID + "=" + detailResponse.getMetaDataFields().get(dID) + "&" + 
                                docName + "=" + detailResponse.getMetaDataFields().get(docName);
                            
                            wccResponse.setStatusMessage(urlWCC);
                        } else {
                            logger.debug("Servicio Search NO encontro resultados");
                            wccResponse.cleanMetaData();
                            logger.debug("Fin servicio cargarDocumentoAExpedienteMinero en " + (System.currentTimeMillis() - starTime) + " ms");
                            
                            return wccResponse;
                        }
                        wccResponse.cleanMetaData();
                        logger.debug("*Fin servicio cargarDocumentoAExpedienteMinero en " + (System.currentTimeMillis() - starTime) + " ms");
                        
                        return wccResponse;
                    } else {
                        logger.debug("**Fin servicio cargarDocumentoAExpedienteMinero en " + (System.currentTimeMillis() - starTime) + " ms");
                        
                       return new WCCResponse(ERWCC07, MessageFormat.format(PropertiesLoader.getErrorProperty(ERWCC07), 
                                                                           metadataList.get(folderName)));
                    }
                } else {
                    searchResponse.cleanMetaData();
                    logger.debug("***Fin servicio cargarDocumentoAExpedienteMinero en " + (System.currentTimeMillis() - starTime) + " ms");
                    
                    return searchResponse;    
                }
            } else {
                searchResponse.cleanMetaData();
                logger.debug("****Fin servicio cargarDocumentoAExpedienteMinero en " + (System.currentTimeMillis() - starTime) + " ms");
                
                return searchResponse; 
            }   
        /*} catch (WSException e) {
            return new BrowseResponse(e.getCode());
        } catch (Exception e) {
            logger.error("Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            return new WCCResponse(ERWCC00);
        } finally {
            logger.debug("Fin servicio cargarDocumentoAExpedienteMinero en " + (System.currentTimeMillis() - starTime) + " ms");
        }*/
    }
    
    @WebResult(name = "BrowseResponse", partName = "browseResponse", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public BrowseResponse consultarContenidoExpedienteMinero (@WebParam(name = "NavegacionCarpeta", partName = "navegacionCarpeta", targetNamespace = "http://transfer.sgd.anm.gov.co") 
                                                          Solicitud solicitud) throws WSException {
        String searchCriteria = null;
        Credenciales credenciales = null;
        FolderMngr folderMngr = null;
        SearchResponse searchResponse = null;
        BrowseResponse browseResponse = null;
        Map<String, String> metadataList = null;
        Map<String, String> metadataSearch = null;
        
        try {
            credenciales = new Credenciales();
            credenciales.setUsuario(PropertiesLoader.getProperty(WCC_CMC_USER));
            credenciales.setContrasena(PropertiesLoader.getProperty(WCC_CMC_PASS));
            
            port = ServiceLocator.getInstance().getGenericUCMPort(credenciales);
            
            folderMngr = new FolderMngr(port);
            
            metadataList = solicitud.getMetadatosList();
            
            if (metadataList.containsKey(anmReferencia)) {
                //Busca el GUID del expediente enviado         
                searchCriteria = folderName + "<matches>" + "`" + solicitud.getMetadatosList().get(anmReferencia) + "`";                                            
                
                searchResponse = folderMngr.search(searchCriteria);
                
                if (OK.equals(searchResponse.getStatusCode())) {
                    //Valida que el expediente exista como solicitud o como t�tulo
                    
                    if (searchResponse.getResultSetList() != null) {
                        metadataSearch = Utilities.validateRecordTaxonomy(searchResponse.getResultSetList(), TITULOS);
                        
                        if (metadataSearch == null) {
                            return new BrowseResponse(ERWCC05);
                        } else {
                            metadataList.put(folderGUID, metadataSearch.get(folderGUID));
                        }
                    } else {
                        return new BrowseResponse(ERWCC05);
                    }
                } else {
                    return new BrowseResponse(searchResponse.getStatusCode(), searchResponse.getStatusMessage());
                }
            } else if (!metadataList.containsKey(folderGUID)) {
                return new BrowseResponse(ERWCC01);
            }           
            
            browseResponse = folderMngr.browse(metadataList); 
            
            browseResponse.cleanMetaData();
            return browseResponse;  
        } catch (WSException e) {
            return new BrowseResponse(e.getCode());
        } catch (Exception e) {
            logger.error("Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            return new BrowseResponse(ERWCC00);
        }       
    }
    
    @WebResult(name = "DetalleDocumento", partName = "detalleDocumento", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public DocumentDetailResponse verDetalleDocumento (@WebParam(name = "SolicitudDetalle", partName = "solicitudDetalle", targetNamespace = "http://transfer.sgd.anm.gov.co") 
                                                          Solicitud solicitud) throws WSException {
        
        DocumentMngr documentMngr = null;
        Credenciales credenciales = null;
        
        try {
            credenciales = new Credenciales();
            credenciales.setUsuario(PropertiesLoader.getProperty(WCC_CMC_USER));
            credenciales.setContrasena(PropertiesLoader.getProperty(WCC_CMC_PASS));
            
            port = ServiceLocator.getInstance().getGenericUCMPort(credenciales);
            
            documentMngr = new DocumentMngr(port);
            
            return documentMngr.docInfoByName(solicitud.getMetadatosList().get(docName));
        } catch (WSException e) {
            return new DocumentDetailResponse(e.getCode());
        } catch (Exception e) {
            logger.error("Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            return new DocumentDetailResponse(ERWCC00);
        }
    }
    
    @WebResult(name = "RespuestaUCM", partName = "respuestaUCM", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public WCCResponse consultarDocumentoExpedienteMinero (@WebParam(name = "DescargaDocumento", partName = "descargaDocumento", targetNamespace = "http://transfer.sgd.anm.gov.co") 
                                                          Solicitud solicitud) throws WSException {
        Credenciales credenciales = null;
        DocumentMngr documentMngr = null;
        Map<String, String> metadataList = null;
        DocumentResponse documentResponse = null;
        
        try {
            credenciales = new Credenciales();
            credenciales.setUsuario(PropertiesLoader.getProperty(WCC_CMC_USER));
            credenciales.setContrasena(PropertiesLoader.getProperty(WCC_CMC_PASS));
            
            port = ServiceLocator.getInstance().getGenericUCMPort(credenciales);
            
            metadataList = solicitud.getMetadatosList();
            
            Utilities.validateParams(metadataList, Utilities.getParameters(Constants.PARAM_DESCARGAR_DOC));
            
            documentMngr = new DocumentMngr(port);
                
            documentResponse = documentMngr.getFile(solicitud.getCredenciales(), metadataList.get("dDocName"), metadataList.get("dID"), metadataList.get("destination"));
            
            return documentResponse;
        } catch (WSException e) {
            return new WCCResponse(e.getCode());
        } catch (Exception e) {
            logger.error("Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            return new WCCResponse(ERWCC00);
        }
    }
    
    @WebResult(name = "RespuestaUCM", partName = "respuestaUCM", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public WCCResponse cargarDocumentoAComunicacion (@WebParam(name = "CargueComunicacion", partName = "cargueComunicacion", targetNamespace = "http://transfer.sgd.anm.gov.co")
                                                    CheckinDocument checkin) throws Exception {
        
        logger.debug("Inicio servicio cargarDocumentoAComunicacion");
        long starTime = System.currentTimeMillis();
        
        Credenciales credenciales = null;
        File file = null;
        FolderMngr folderMngr = null;
        DocumentMngr documentMngr = null;
        WCCResponse wccResponse = null;
        BrowseResponse browseResponse = null;
        Map<String, String> metadataList = null;
        Map<String, String> metadataBrowse = null;
        String folderNameCom = null;
        String folderConPath = null;
        String folderConID = null;
        boolean isFolderCom = false;
        
        //try {   
            credenciales = new Credenciales();
            credenciales.setUsuario(PropertiesLoader.getProperty(WCC_BPM_USER));
            credenciales.setContrasena(PropertiesLoader.getProperty(WCC_BPM_PASSWORD));
            
            port = ServiceLocator.getInstance().getGenericUCMPort(credenciales);
            
            folderNameCom = "CONSECUTIVO " + Utilities.getDate("YYYY");
            
            //Busca la carpeta del consecutivo del a�o en curso
            metadataBrowse = new HashMap<String, String>();
            metadataBrowse.put(path, PropertiesLoader.getProperty(PATH_CONS_COM));
            
            folderMngr = new FolderMngr(port);
            
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
                        return wccResponse;
                    }     
                    folderConID = wccResponse.getMetaDataFields().get(folderGUID);   
                }                                                                
                 
                metadataList = new HashMap<String, String>();                            
                metadataList.put(securityGroup, "Public");                               
                metadataList.put(docAccount, checkin.getDocAccount());                               
                metadataList.put(docType, "Document");
                metadataList.put(docAuthor, checkin.getCredenciales().getUsuario());
                metadataList.put(docTitle, checkin.getDocTitle());
                metadataList.put(primaryFile, checkin.getPrimaryFile());
                metadataList.put(anmTramite, checkin.getTramite());
                metadataList.put(anmUnidadAdministrativa, checkin.getUnidadAdministrativa());
                metadataList.put(anmTipoDocumental, checkin.getTipoDocumental());
                metadataList.put(anmReferencia, checkin.getNroPlaca());
                metadataList.put(folderPath, folderConPath);   
                metadataList.put(folderGUID, folderConID);
                metadataList.put(tipoRecursoInformacion, "2");
                
                file = new File(checkin.getPrimaryFile());
                
                documentMngr = new DocumentMngr(port);   
                
                FileTransfer fileTransfer = new FileTransfer();
                fileTransfer.setContentType(Files.probeContentType(Paths.get(file.getPath())));
                fileTransfer.setFile(file);
                fileTransfer.setFilename(file.getName());
                fileTransfer.setCredenciales(checkin.getCredenciales());
                            
                wccResponse = documentMngr.checkinNew(metadataList, fileTransfer);   
                
                wccResponse.cleanMetaData();
                return wccResponse;
            } else {      
                browseResponse.cleanMetaData();
                return browseResponse;
            }
        /*} catch (WSException e) {
            return new WCCResponse(e.getCode());
        } catch (FileNotFoundException e) {
            return new WCCResponse(ERWCC22);    
        } catch (Exception e) {
            logger.error("Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            return new WCCResponse(ERWCC00);
        } finally {
            logger.debug("Fin servicio cargarDocumentoAComunicacion en " + (System.currentTimeMillis() - starTime) + " ms");
        }*/
    }
    
    @WebResult(name = "RespuestaUCM", partName = "respuestaUCM", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public WCCResponse cargarDocumentoAPQRS (@WebParam(name = "CarguePQRS", partName = "carguePQRS", targetNamespace = "http://transfer.sgd.anm.gov.co") 
                                                          CheckinDocument checkin) throws Exception {
        
        FolderMngr folderMngr = null;
        DocumentMngr documentMngr = null;
        WCCResponse wccResponse = null;
        BrowseResponse browseResponse = null;
        Map<String, String> metadataList = null;
        Map<String, String> metadataBrowse = null;
        String folderNamePQRS = null;
        String folderPQRSPath = null;
        String folderPQRSID = null;
        boolean isFolderPQRS = false;
        
        //try {
            port = ServiceLocator.getInstance().getGenericUCMPort(checkin.getCredenciales());
            
            folderNamePQRS = "PQRS " + Utilities.getDate("YYYY");
            
            //Busca la carpeta del consecutivo del a�o en curso
            metadataBrowse = new HashMap<String, String>();
            metadataBrowse.put(path, PropertiesLoader.getProperty(PATH_PQRS));
            
            folderMngr = new FolderMngr(port);
            
            browseResponse = folderMngr.browse(metadataBrowse);
            
            if (OK.equals(browseResponse.getStatusCode())) {   
                if (browseResponse.getFileList() != null) {
                    if (browseResponse.getFolderList() != null) {
                        for (FolderResponse folder : browseResponse.getFolderList()) {
                            
                            if (folder.getName().equals(folderNamePQRS)) {
                                logger.debug("Encontr� carpeta");
                                
                                folderPQRSPath = PropertiesLoader.getProperty(PATH_PQRS) + "/" + folder.getName();
                                folderPQRSID = folder.getId();
                                
                                isFolderPQRS = true;   
                                
                                break;
                            }
                        }
                    }
                }
                
                if (isFolderPQRS) {
                    //Lista los documentos de la carpeta del a�o en curso
                    metadataBrowse.put(path, folderPQRSPath);
                    
                    browseResponse = folderMngr.browse(metadataBrowse);
                    
                    if (OK.equals(browseResponse.getStatusCode())) {
                        if (browseResponse.getFileList() != null) {
                            for (FileResponse file : browseResponse.getFileList()) {
                                
                                if (file.getName().equals(checkin.getDocTitle())) {
                                    logger.debug("Encontr� comunicaci�n");
                                    
                                    return new WCCResponse(ERWCC15);                        
                                }
                            }   
                        }
                        logger.debug("No encontr� PQRS");
                    }
                } else {
                    //Crea la carpeta correspondiente al consecutivo del a�o
                    logger.debug("No se encuentra la carpeta " + folderNamePQRS + ". Se crear� la carpeta");
                    
                    wccResponse = folderMngr.create(browseResponse.getMetaDataFields().get(folderGUID), folderNamePQRS);
                    
                    if (!OK.equals(wccResponse.getStatusCode())) {
                        return wccResponse;
                    }     
                    folderPQRSID = wccResponse.getMetaDataFields().get(folderGUID);
                }                                                                
                
                metadataList = new HashMap<String, String>();                            
                metadataList.put(securityGroup, "Public");                               
                metadataList.put(docAccount, checkin.getDocAccount());                               
                metadataList.put(docType, "Document");
                metadataList.put(docTitle, checkin.getDocTitle());
                metadataList.put(primaryFile, checkin.getPrimaryFile());
                metadataList.put(anmTramite, checkin.getTramite());
                metadataList.put(anmUnidadAdministrativa, checkin.getUnidadAdministrativa());
                metadataList.put(anmTipoDocumental, checkin.getTipoDocumental());
                metadataList.put(anmReferencia, checkin.getNroPlaca());   
                metadataList.put(folderPath, folderPQRSPath);   
                metadataList.put(folderGUID, folderPQRSID);
                metadataList.put(tipoRecursoInformacion, "2");
                
                documentMngr = new DocumentMngr(port);                                           
                            
                return documentMngr.checkinNew(metadataList, null);   
            } else {                
                return browseResponse;
            }
        /*} catch (WSException e) {
            return new WCCResponse(e.getCode());
        } catch (Exception e) {
            logger.error("Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            return new WCCResponse(ERWCC00);
        }*/
    }
    
    @WebResult(name = "RespuestaUCM", partName = "respuestaUCM", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public WCCResponse cargarDocumentoAExpMinero (@WebParam(name = "CheckinDocument", partName = "checkinDocument", targetNamespace = "http://transfer.sgd.anm.gov.co") 
                                                          CheckinDocument checkin) throws Exception { 
        
        logger.debug("Inicio servicio cargarDocumentoAExpMinero");
        long starTime = System.currentTimeMillis();
        
        String searchCriteria = null;
        String folderIdRequest = null;
        Credenciales credenciales = null;
        File file = null;
        FolderMngr folderMngr = null;
        DocumentMngr documentMngr = null;
        SearchResponse searchResponse = null;
        WCCResponse wccResponse = null;
        BrowseResponse browseResponse = null;
        Map<String, String> metadataList = null;
        Map<String, String> metadataSearch = null;
        Map<String, String> metadataBrowse = null;
        
        //try {            
        credenciales = new Credenciales();
        credenciales.setUsuario(PropertiesLoader.getProperty(WCC_BPM_USER));
        credenciales.setContrasena(PropertiesLoader.getProperty(WCC_BPM_PASSWORD));
        logger.debug("credenciales usuario: "+credenciales.getUsuario());
        
        port = ServiceLocator.getInstance().getGenericUCMPort(credenciales);
        
        List<UnidadAdministrativa> unidAdminList = wcContentEjb.getUnidadAdministrativaFindByCod(new BigDecimal(checkin.getUnidadAdministrativa()));
        logger.debug("unidAdminList size: "+unidAdminList.size());
            
        //Busca el GUID del expediente         
        searchCriteria = "fFolderName<matches>" + "`" + checkin.getNroPlaca() + "`";
        
        folderMngr = new FolderMngr(port);
        searchResponse = folderMngr.search(searchCriteria);
        
        if (OK.equals(searchResponse.getStatusCode())) {
            
            if (searchResponse.getResultSetList() != null) {
                logger.debug("encuentra coincidencias");
                
                metadataSearch = Utilities.validateRecordTaxonomy(searchResponse.getResultSetList(), EXPEDIENTE);
                
                //Valida que el expediente sea un titulo minero
                if (metadataSearch == null) {
                    return new WCCResponse(ERWCC03);
                }
            } else {
                return new WCCResponse(ERWCC03);
            }
            
            metadataBrowse = new HashMap<String, String>();
            metadataBrowse.put(path, metadataSearch.get(path));
            
            browseResponse = folderMngr.browse(metadataBrowse);
            logger.debug("browseResponse folderList: "+browseResponse.getFolderList());
            
            if (browseResponse.getFolderList() != null) {
                for (FolderResponse folder : browseResponse.getFolderList()) {
                        
                    //valida que no se encuentre en la subserie destino                            
                    if (Utilities.getCuadernosTituloMinero().get(0).equals(folder.getName())) {
                        folderIdRequest = folder.getId();
                    }
                }
            } 
            logger.debug("folderIdRequest: "+folderIdRequest);
            
            documentMngr = new DocumentMngr(port);      
                    
            metadataList = new HashMap<String, String>();                        
            metadataList.put(securityGroup, "Public");   
            metadataList.put(docAccount, checkin.getDocAccount());   
            metadataList.put(docType, "Document");
            metadataList.put(docTitle, checkin.getDocTitle());
            /******************************************
             * Fecha: 28Feb2024
             * Descripcion: Se actualiza el usuario que realiza el checkin para que no se asigne desde
             * el flujo BPM sino desde el valor de WCC_USER en archivo de properties
             * Autor: adrimol
             * ******************************************/
            //metadataList.put(docAuthor, checkin.getCredenciales().getUsuario());
            metadataList.put(docAuthor, credenciales.getUsuario());
            logger.debug("metadataList docAuthor: "+metadataList.get(docAuthor));
            /******************************************/
            metadataList.put(primaryFile, checkin.getPrimaryFile());
            metadataList.put(anmTramite, checkin.getTramite());
            metadataList.put(anmUnidadAdministrativa, unidAdminList.get(0).getIdUnidadadministrativa().toString());
            metadataList.put(anmReferencia, checkin.getNroPlaca());
            metadataList.put(anmTipoDocumental, checkin.getTipoDocumental());
            metadataList.put(folderGUID, folderIdRequest);  
            metadataList.put(comments, metadataSearch.get(comments));  
            metadataList.put(tipoRecursoInformacion, "2");
            
            file = new File(checkin.getPrimaryFile());
            
            documentMngr = new DocumentMngr(port);   
            
            FileTransfer fileTransfer = new FileTransfer();
            fileTransfer.setContentType(Files.probeContentType(Paths.get(file.getPath())));
            fileTransfer.setFile(file);
            fileTransfer.setFilename(file.getName());
            /******************************************
             * Fecha: 28Feb2024
             * Descripcion: Se actualiza el usuario que realiza el checkin para que no se asigne desde
             * el flujo BPM sino desde el valor de WCC_USER en archivo de properties
             * Autor: adrimol
             * ******************************************/
            //fileTransfer.setCredenciales(checkin.getCredenciales());
            fileTransfer.setCredenciales(credenciales);
            /******************************************/
            
            wccResponse = documentMngr.checkinNew(metadataList, fileTransfer);    
            
            /******************************************
             * Fecha: 07Marzo2024
             * Descripcion: Se consulta la metadata del documento cargado en WCC, para armar la url y
             * retornarla como parte del status message de la respuesta.
             * Autor: adrimol
             * ******************************************/
            wccResponse = documentMngr.agregarUrlDocumentoEnResponse(wccResponse);
            /******************************************/
            
            wccResponse.cleanMetaData();
            logger.debug("Fin servicio cargarDocumentoAExpMinero en " + (System.currentTimeMillis() - starTime) + " ms");
            
            return wccResponse;
        } else {
            searchResponse.cleanMetaData();
            logger.debug("Fin servicio cargarDocumentoAExpMinero - Status: "+searchResponse.getStatusCode());
            
            return searchResponse; 
        }   
        /*} catch (WSException e) {
            return new BrowseResponse(e.getCode());
        } catch (Exception e) {
            logger.error("Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            return new BrowseResponse(ERWCC00);
        } finally {
            logger.debug("Fin servicio cargarDocumentoAExpMinero en " + (System.currentTimeMillis() - starTime) + " ms");
        }*/
    }
    
    
    @WebResult(name = "RespuestaUCM", partName = "respuestaUCM", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public WCCResponse cargarDocumentoATaxonomia (@WebParam(name = "CheckinTaxonomia", partName = "checkinTaxonomia", targetNamespace = "http://transfer.sgd.anm.gov.co") 
                                                     CheckinDocument checkin) throws Exception {    
        
        logger.debug("Inicio servicio cargarDocumentoATaxonomia");
        DocumentMngr documentMngr = null;
        WCCResponse wccResponse = null;
        Map<String, String> metadataList = null;
        Credenciales credenciales = null;
        List<UnidadAdministrativa> unidAdminList = wcContentEjb.getUnidadAdministrativaFindByCod(new BigDecimal(checkin.getUnidadAdministrativa()));
        logger.debug("unidAdminList size: "+unidAdminList.size());
        //try {    
        credenciales = new Credenciales();
        credenciales.setUsuario(PropertiesLoader.getProperty(WCC_BPM_USER));
        credenciales.setContrasena(PropertiesLoader.getProperty(WCC_BPM_PASSWORD));
        
        port = ServiceLocator.getInstance().getGenericUCMPort(credenciales);
        
        documentMngr = new DocumentMngr(port);      
                
        metadataList = new HashMap<String, String>();  
        metadataList.put(securityGroup, "Public");                               
        metadataList.put(docAccount, checkin.getDocAccount());                               
        metadataList.put(docType, "Document");
        metadataList.put(docTitle, checkin.getDocTitle());
        metadataList.put(primaryFile, checkin.getPrimaryFile());
        metadataList.put(anmTramite, checkin.getTramite());
        metadataList.put(anmUnidadAdministrativa, unidAdminList.get(0).getIdUnidadadministrativa().toString());
        metadataList.put(anmReferencia, checkin.getNroPlaca());
        metadataList.put(anmTipoDocumental, checkin.getTipoDocumental());
        metadataList.put(folderGUID, checkin.getFolderGUID());   
        metadataList.put(tipoRecursoInformacion, "2");
                
        wccResponse = documentMngr.checkinNew(metadataList, null);    
        
        /******************************************
         * Fecha: 07Marzo2024
         * Descripcion: Se consulta la metadata del documento cargado en WCC, para armar la url y
         * retornarla como parte del status message de la respuesta.
         * Autor: adrimol
         * ******************************************/
        wccResponse = documentMngr.agregarUrlDocumentoEnResponse(wccResponse);
        /******************************************/
        
        wccResponse.cleanMetaData();
            
        logger.debug("Fin servicio cargarDocumentoATaxonomia");
        
        return wccResponse;
        /*} catch (WSException e) {
            return new BrowseResponse(e.getCode());
        } catch (Exception e) {
            logger.error("Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            return new BrowseResponse(ERWCC00);
        }*/
    }   
    
    @WebResult(name = "RespuestaUCM", partName = "respuestaUCM", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public WCCResponse tipificarAnexosComunicacion (@WebParam(name = "TipificacionAnexos", partName = "tipificacionAnexos", targetNamespace = "http://transfer.sgd.anm.gov.co") 
                                                    TipificacionAnexosRequest request) throws Exception {        
        logger.debug("Inicio servicio tipificarAnexosComunicacion");
        long starTime = System.currentTimeMillis();
        
        GestionDocumentalMngr gcMngr = null;        
        try {            
            gcMngr = new GestionDocumentalMngr();
            
            return gcMngr.tipificarAnexosComunicacion(request, wcContentEjb, comunicacionesEjb);        
        /*} catch (WSException e) {
            logger.error(request.getNroRadicado() + "--> Error en la ejecucion del servicio web: " + e.getMessage(), e);
            return new BrowseResponse(e.getCode());*/
        } catch (Exception e) {
            logger.error(request.getNroRadicado() + "--> Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            //return new BrowseResponse(ERWCC00);
            throw e;
        } finally {            
            logger.debug("Fin servicio tipificarAnexosComunicacion en " + (System.currentTimeMillis() - starTime) + " ms");
        }
    }
    
    @WebResult(name = "RespuestaUCM", partName = "respuestaUCM", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public WCCResponse checkinGestionDocumental (@WebParam(name = "CheckinGestionDocumental", partName = "checkinGestionDocumental", targetNamespace = "http://transfer.sgd.anm.gov.co") 
                                                     CheckinDocument checkin) throws Exception {
        logger.debug("Inicio servicio checkinGestionDocumental");
        long starTime = System.currentTimeMillis();
        logger.debug("Entro a servicio checkinGestionDocumental");
        GestionDocumentalMngr gcMngr = null;        
        try {            
            gcMngr = new GestionDocumentalMngr();
            
            return gcMngr.cargarDocumentoGestionDocumental(checkin, wcContentEjb, comunicacionesEjb);            
        /*} catch (WSException e) {
            logger.error(checkin.getNroRadicado() + "--> Error en la ejecucion del servicio web: " + e.getMessage(), e);
            
            return new WCCResponse(e.getCode());*/
        } catch (Exception e) {
            logger.error(checkin.getNroRadicado() + "--> Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            //return new WCCResponse(ERWCC00, e.getMessage());
            throw e;
        } finally {            
            logger.debug("Fin servicio checkinGestionDocumental en " + (System.currentTimeMillis() - starTime) + " ms");
        } 
    }
    
    @WebResult(name = "RespuestaUCM", partName = "respuestaUCM", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public WCCResponse checkinPlantilla (@WebParam(name = "CheckinPlantilla", partName = "checkinPlantilla", targetNamespace = "http://transfer.sgd.anm.gov.co") 
                                                     CheckinDocument checkin) throws WSException {
        logger.debug("Inicio servicio checkinPlantilla");
        long starTime = System.currentTimeMillis();
        GestionDocumentalMngr mngr = new GestionDocumentalMngr();
        try {
            return mngr.checkinplantilla(checkin);
        } catch (WSException e) {
            return new WCCResponse(e.getCode());
        } catch (Exception e) {
            logger.error("Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            return new WCCResponse(ERWCC00, e.getMessage());
        } finally {
            logger.debug("Fin servicio checkinPlantilla en " + (System.currentTimeMillis() - starTime) + " ms");
        }
    }
}
