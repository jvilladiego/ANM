package co.gov.anm.sgd.mngr;

import co.gov.anm.sgd.exception.WSException;
import co.gov.anm.sgd.response.DocumentDetailResponse;
import co.gov.anm.sgd.response.DocumentResponse;
import co.gov.anm.sgd.response.SearchResponse;
import co.gov.anm.sgd.response.WCCResponse;
import co.gov.anm.sgd.transfer.Credenciales;
import co.gov.anm.sgd.transfer.FileTransfer;
import co.gov.anm.sgd.util.Constants;
import static co.gov.anm.sgd.util.Constants.ERWCC00;
import static co.gov.anm.sgd.util.Constants.ERWCC08;
import static co.gov.anm.sgd.util.Constants.ERWCC20;
import static co.gov.anm.sgd.util.Constants.OK;
import static co.gov.anm.sgd.util.Constants.SVR_CHECK_NEW;
import static co.gov.anm.sgd.util.Constants.SVR_DOC_INFO_BY_NAME;
import static co.gov.anm.sgd.util.Constants.SVR_FLD_COPY;
import static co.gov.anm.sgd.util.Constants.SVR_FLD_EDIT_FILE;
import static co.gov.anm.sgd.util.Constants.SVR_GET_FILE;
import static co.gov.anm.sgd.util.Constants.SVR_GET_SEARCH_RESULTS;
import static co.gov.anm.sgd.util.Constants.URL_IDC;
import static co.gov.anm.sgd.util.Constants.anmEstadoArchivo;
import static co.gov.anm.sgd.util.Constants.anmReferencia;
import static co.gov.anm.sgd.util.Constants.anmTramite;
import static co.gov.anm.sgd.util.Constants.anmUnidadAdministrativa;
import static co.gov.anm.sgd.util.Constants.dID;
import static co.gov.anm.sgd.util.Constants.docAccount;
import static co.gov.anm.sgd.util.Constants.docName;
import static co.gov.anm.sgd.util.Constants.fileGUID;
import static co.gov.anm.sgd.util.Constants.fileName;
import static co.gov.anm.sgd.util.Constants.folderGUID;
import static co.gov.anm.sgd.util.Constants.format;
import static co.gov.anm.sgd.util.Constants.parentGUID;
import static co.gov.anm.sgd.util.Constants.queryText;
import static co.gov.anm.sgd.util.Constants.revLabel;
import static co.gov.anm.sgd.util.Constants.securityGroup;
import static co.gov.anm.sgd.util.Constants.xANM_Idiomas;
import co.gov.anm.sgd.util.PropertiesLoader;

import com.oracle.ucm.Field;
import com.oracle.ucm.Generic;
import com.oracle.ucm.ResultSet;
import com.oracle.ucm.Row;
import com.oracle.ucm.Service;

import genericsoap.GenericSoapPortType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcClientManager;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.common.http.utils.RIDCHttpConstants;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.TransferFile;
import oracle.stellent.ridc.protocol.ServiceResponse;

import org.apache.log4j.Logger;

public class DocumentMngr  {

    public static final Logger logger = Logger.getLogger(DocumentMngr.class);

    private GenericSoapPortType port;
    
    private IdcClient idcClient;
    private IdcContext idcContext;

    public DocumentMngr(GenericSoapPortType port) {
        this.port = port;
    }
    
    private void createIdcConnection (Credenciales credenciales) throws WSException {
        try {
            IdcClientManager idcClientManager = new IdcClientManager();
            idcClient = idcClientManager.createClient(PropertiesLoader.getProperty(Constants.URL_IDC2));
            //idcClient.getConfig().setProperty("http.library", RIDCHttpConstants.HttpLibrary.apache4.name());
            
            //idcContext = new IdcContext(credenciales.getUsuario(), credenciales.getContrasena());
            idcContext = new IdcContext(credenciales.getUsuario());
        } catch (IdcClientException e) {
            throw new WSException(ERWCC08, e);
        }
    }

    public WCCResponse checkinNew(Map<String, String> metadataMap, FileTransfer fileTransfer) throws FileNotFoundException, Exception {
        logger.debug("Inicio servicio " + SVR_CHECK_NEW);

        Generic wsResponse = null;
        WCCResponse wccResponse = null;
        FolderMngr folderMngr = null;
        
        WCCAPIDelegate delegate = new WCCAPIDelegate(port);
        
        metadataMap.put(revLabel, "1");
        metadataMap.put(anmEstadoArchivo, "1");
        metadataMap.put(xANM_Idiomas, "1");
        
        logger.debug("fileTransfer: " + fileTransfer);
        if (fileTransfer != null) {                           
            createIdcConnection(fileTransfer.getCredenciales());
            
            DataBinder binderRequest = idcClient.createBinder();
            binderRequest.putLocal("IdcService", SVR_CHECK_NEW);
            binderRequest.putLocal(format, fileTransfer.getContentType());
            
            for (Map.Entry<String, String> metadata : metadataMap.entrySet()) {
                logger.debug("metadato: " + metadata.getKey());
                logger.debug("valor: " + metadata.getValue());
                binderRequest.putLocal(metadata.getKey(), metadata.getValue());
            }
            binderRequest.addFile("primaryFile", new TransferFile(fileTransfer.getFile()));
            
            ServiceResponse response = idcClient.sendRequest(idcContext, binderRequest);
            logger.debug("idcClient response1: "+response.getResponseAsString());
            
            DataBinder dataBinderRes = response.getResponseAsBinder();
            
            String docNameDocument = dataBinderRes.getLocal(docName);
            String folderGUIDRequest = dataBinderRes.getLocal(folderGUID);
            
            logger.debug("docNameDocument: "+docNameDocument);
            logger.debug("folderGUIDRequest: "+folderGUIDRequest);           
            
            DataBinder binderRequest2 = idcClient.createBinder();
            binderRequest2.putLocal("IdcService", "FLD_CREATE_FILE");
            binderRequest2.putLocal(parentGUID, folderGUIDRequest);         
            binderRequest2.putLocal(docName, docNameDocument);
            response = idcClient.sendRequest(idcContext, binderRequest2);
            logger.debug("idcClient response2: "+response.getResponseAsString());
            
            wccResponse = new WCCResponse(response.getResponseAsBinder());
            logger.debug("Fin servicio " + SVR_CHECK_NEW);
            
            return wccResponse;
        }
        
        wsResponse = delegate.executeWS(SVR_CHECK_NEW, metadataMap, fileTransfer);
        Service.Document rspdoc = wsResponse.getService().getDocument();
        wccResponse = new WCCResponse(rspdoc.getField());
        logger.debug("wccResponse StatusCode: " + wccResponse.getStatusCode());

        if (OK.equals(wccResponse.getStatusCode())) {
            HashMap<String, String> metadataLink = new HashMap<String, String>();

            metadataLink.put(parentGUID, metadataMap.get(folderGUID));

            for (Field field : rspdoc.getField()) {

                if (docName.equals(field.getName())) {
                    metadataLink.put(docName, field.getValue());
                }
            }
            folderMngr = new FolderMngr(port);

            return folderMngr.createFileLinkInFolder(metadataLink);
        }
        logger.debug("Fin servicio " + SVR_CHECK_NEW);
        
        return wccResponse;
    }
    
    public WCCResponse edit (Map<String, String> metadataMap) throws Exception {
        logger.debug("inicia servicio " + SVR_FLD_EDIT_FILE);
        
        WCCAPIDelegate delegate = new WCCAPIDelegate(port);
        
        Generic wsResponse = delegate.executeWS(SVR_FLD_EDIT_FILE, metadataMap, null);

        return new WCCResponse(wsResponse.getService().getDocument().getField());
    }

    public DocumentResponse getFile(Credenciales credenciales, String docNameRequest, String dID, String destination) throws Exception {
        logger.debug("inicia servicio " + SVR_GET_FILE);

        DocumentResponse docResponse = null;
        InputStream is = null;
        OutputStream os = null;
        
        DataBinder binderRequest = null;

        try {            
            createIdcConnection(credenciales);
        
            binderRequest = idcClient.createBinder();
            binderRequest.putLocal("IdcService", SVR_GET_FILE);
            binderRequest.putLocal("dID", dID);
            binderRequest.putLocal(docName, docNameRequest);
            
            ServiceResponse response = idcClient.sendRequest(idcContext, binderRequest);
            
            docResponse = new DocumentResponse();
            docResponse.setStatusCode("0");
            docResponse.setStatusMessage("Success");
            
            logger.debug("servicio invocado");
                                
            os = new FileOutputStream(new File(destination));
            
            is = response.getResponseStream();
                
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = is.read(bytes, 0, bytes.length)) != -1) {
                os.write(bytes, 0, read);
            }
            
            os.flush();
            
            return docResponse;   
        } catch (FileNotFoundException e) {
            logger.error("Error escribiendo el documento destino", e);
            
            throw new WSException(ERWCC20, PropertiesLoader.getErrorProperty(ERWCC20), e);
        } catch (IOException e) {
            logger.error("Error escribiendo el documento destino", e);
            
            throw new WSException(ERWCC00, PropertiesLoader.getErrorProperty(ERWCC00), e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                logger.error("Error en la ejecuci�n del servicio", e);
                
                throw new WSException(ERWCC00, PropertiesLoader.getErrorProperty(ERWCC00), e);
            }
        }
    }
    
    public SearchResponse getSearchResults (String criteria, String engine) throws Exception {
        logger.debug("getSearchResults -> inicia servicio " + SVR_GET_SEARCH_RESULTS);
        logger.debug("getSearchResults -> criteria: "+criteria);
        logger.debug("getSearchResults -> engine: "+engine);
        
        Generic wsResponse = null;
        SearchResponse searchResponse = null;
        Map<String, String> metadataRequest = new HashMap<String, String>();
        Map<String, String> metadataList = null;    
        
        metadataRequest.put(queryText, criteria);
        
        if (engine!=null){
            metadataRequest.put("SearchEngineName", engine);
        }
        
        WCCAPIDelegate delegate = new WCCAPIDelegate(port);
        
        wsResponse = delegate.executeWS(SVR_GET_SEARCH_RESULTS, metadataRequest, null);

        Service.Document rspdoc = wsResponse.getService().getDocument();

        searchResponse = new SearchResponse(rspdoc.getField());

        //Get Results Sets from document
        List<ResultSet> resultSetList = rspdoc.getResultSet();
        logger.debug("getSearchResults -> resultSetList size: "+resultSetList.size());

        for (ResultSet rset : resultSetList) {
            logger.debug("getSearchResults -> rset.getName(): "+rset.getName());
            //solo procesa el tag search_results
            if ("searchresults".equals(rset.getName().toLowerCase()) ) {
                List<Map<String, String>> resultSetRspList = new ArrayList<Map<String, String>>();

                for (Row row : rset.getRow()) {
                    List<Field> rscols = row.getField();

                    metadataList = new HashMap<String, String>();

                    for (Field field : rscols) {
                        metadataList.put(field.getName(), field.getValue());
                    }
                    searchResponse.setMetadataList(metadataList);

                    resultSetRspList.add(metadataList);
                }
                searchResponse.setResultSetList(resultSetRspList);
            } 
        } 
        if(searchResponse.getResultSetList()!=null){
            logger.debug("getSearchResults -> searchResponse result size: "+searchResponse.getResultSetList().size());
        }else{
            logger.debug("getSearchResults -> searchResponse resultset is null");
        }
        
        return searchResponse;
    }
    
    public SearchResponse getSearchResults (String criteria) throws Exception {
        return getSearchResults(criteria, null);
    }
    
    public WCCResponse copyItem (Map<String, String> metadataMap) throws Exception {
        logger.debug("inicia servicio " + SVR_FLD_COPY);
        
        WCCAPIDelegate delegate = new WCCAPIDelegate(port);
        
        Generic wsResponse = delegate.executeWS(SVR_FLD_COPY, metadataMap, null);

        return new WCCResponse(wsResponse.getService().getDocument().getField());
    }
    
    public DocumentDetailResponse docInfoByName (String docNameRequest) throws Exception {
        logger.debug("inicia servicio " + SVR_DOC_INFO_BY_NAME);
        
        Generic wsResponse = null;
        DocumentDetailResponse detailResponse = null;
        Map<String, String> metadataRequest = new HashMap<String, String>();
        
        metadataRequest.put(docName, docNameRequest);
        
        WCCAPIDelegate delegate = new WCCAPIDelegate(port);
        
        wsResponse = delegate.executeWS(SVR_DOC_INFO_BY_NAME, metadataRequest, null);

        Service.Document rspdoc = wsResponse.getService().getDocument();

        detailResponse = new DocumentDetailResponse(rspdoc.getField());
        
        //Get Results Sets from document
        List<ResultSet> resultSetList = rspdoc.getResultSet();

        for (ResultSet rset : resultSetList) {
            if ("doc_info".equals(rset.getName().toLowerCase())) {

                for (Row row : rset.getRow()) {

                    for (Field field : row.getField()) {
                        
                        if (field.getName().equals(anmReferencia)) {
                            detailResponse.setNumeroPlaca(field.getValue());    
                        } else if (field.getName().equals(anmUnidadAdministrativa)) {
                            detailResponse.setUnidadAdministrativa(field.getValue());
                        } else if (field.getName().equals(anmEstadoArchivo)) {
                            detailResponse.setEstadoArchivo(field.getValue());
                        } else if (field.getName().equals(anmTramite)) {
                            detailResponse.setTramite(field.getValue());
                        }                        
                    }
                }
            } else if ("fileinfo".equals(rset.getName().toLowerCase())) {
                for (Row row : rset.getRow()) {

                    for (Field field : row.getField()) {
                        
                        if (field.getName().equals(fileName)) {
                            detailResponse.setNombreDocumento(field.getValue());
                        } else if (field.getName().equals(fileGUID)) {
                            detailResponse.setIdDocumento(field.getValue());
                        } else if (field.getName().equals("dPublishedRevisionID")) {
                            detailResponse.setVersionPublicada(field.getValue());
                        } else if (field.getName().equals("fCreator")) {
                            detailResponse.setCreador(field.getValue());
                        } else if (field.getName().equals("fLastModifier")) {
                            detailResponse.setUltimoModificador(field.getValue());                   
                        } else if (field.getName().equals("fCreateDate")) {
                            detailResponse.setFechaCreacioon(field.getValue());
                        } else if (field.getName().equals("fLastModifiedDate")) {
                            detailResponse.setUltimaFechaModificacion(field.getValue());
                        } else if (field.getName().equals(securityGroup)) {
                            detailResponse.setGrupoSeguridad(field.getValue());
                        } else if (field.getName().equals(docAccount)) {
                            detailResponse.setCuenta(field.getValue());
                        }
                    }
                }
            }
        } 
        
        return detailResponse;
    }
    
    /******************************************
     * Fecha: 07Marzo2024
     * Descripcion: Se consulta la metadata del documento cargado en WCC, para armar la url y
     * retornarla como parte del status message de la respuesta.
     * Autor: adrimol
     * ******************************************/
    public WCCResponse agregarUrlDocumentoEnResponse(WCCResponse wccResponse) throws Exception {
        logger.debug("Inicio agregarUrlDocumentoEnResponse");
        DocumentDetailResponse detailResponse = this.docInfoByName(wccResponse.getMetaDataFields().get(docName));                    
        
        if (OK.equals(detailResponse.getStatusCode())) {
            logger.debug("Respuesta servicio de busqueda info Documento OK");
            String urlWCC = PropertiesLoader.getProperty(URL_IDC) + "?" + "IdcService=GET_FILE" + "&" +
                dID + "=" + detailResponse.getMetaDataFields().get(dID) + "&" + 
                docName + "=" + detailResponse.getMetaDataFields().get(docName);
            
            wccResponse.setStatusMessage(urlWCC);
        } else {
            logger.debug("Servicio Search NO encontro resultados");
        }
        logger.debug("Fin agregarUrlDocumentoEnResponse");
        
        return wccResponse;
    }
    /******************************************/
} 
