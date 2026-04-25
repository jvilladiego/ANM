package co.gov.anm.sgd.service;


import co.gov.anm.sgd.exception.WSException;
import co.gov.anm.sgd.integration.location.ServiceLocator;
import co.gov.anm.sgd.mngr.DocumentMngr;
import co.gov.anm.sgd.mngr.ExpedienteMineroMngr;
import co.gov.anm.sgd.mngr.FolderMngr;
import co.gov.anm.sgd.response.BrowseResponse;
import co.gov.anm.sgd.response.FolderResponse;
import co.gov.anm.sgd.response.SearchResponse;
import co.gov.anm.sgd.response.WCCResponse;
import co.gov.anm.sgd.transfer.Credenciales;
import co.gov.anm.sgd.transfer.ModificacionExpediente;
import co.gov.anm.sgd.transfer.Solicitud;
import co.gov.anm.sgd.util.PropertiesLoader;
import co.gov.anm.sgd.util.Utilities;
import genericsoap.GenericSoapPortType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import org.apache.log4j.Logger;

@WebService(name = "ExpedienteMinero", targetNamespace = "http://service.sgd.anm.gov.co", serviceName = "ExpedienteMinero", portName = "ExpedienteMineroPort", wsdlLocation = "WEB-INF/wsdl/ExpedienteMinero.wsdl")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class ExpedienteMinero {
  private static Logger logger = Logger.getLogger(co.gov.anm.sgd.service.ExpedienteMinero.class);
  
  private GenericSoapPortType port = null;
  
  @WebResult(name = "RespuestaUCM", partName = "respuestaUCM", targetNamespace = "http://response.sgd.anm.gov.co")
  @WebMethod
  public WCCResponse crearExpedienteSolicitudMinera(@WebParam(name = "CreacionSolicitudMinera", partName = "solicitudMinera", targetNamespace = "http://transfer.sgd.anm.gov.co") Solicitud solicitud) throws WSException {
    logger.debug("inicia servicio crearExpedienteSolicitudMinera");
    String searchCriteria = null;
    FolderMngr folderMngr = null;
    DocumentMngr documentMngr = null;
    SearchResponse searchResponse = null;
    WCCResponse wccResponse = null;
    Credenciales credenciales = null;
    Map<String, String> metadataList = null;
    String subSerieId = null;
    String recordFolderId = null;
    long time = System.currentTimeMillis();
    try {
      credenciales = new Credenciales();
      credenciales.setUsuario(PropertiesLoader.getProperty("WCC_CMC_USER"));
      credenciales.setContrasena(PropertiesLoader.getProperty("WCC_CMC_PASS"));
      this.port = ServiceLocator.getInstance().getGenericUCMPort(credenciales);
      metadataList = solicitud.getMetadatosList();
      logger.debug("valida campos de entrada");
      Utilities.validateParams(metadataList, Utilities.getParameters("PARAM_CREAR_SOLICITUD"));
      Utilities.validateRequestOrigin(metadataList.get(metadataList.get("origenSolicitud")));
      searchCriteria = "fFolderName<matches>`" + (String)metadataList.get("fFolderName") + "`";
      folderMngr = new FolderMngr(this.port);
      searchResponse = folderMngr.search(searchCriteria);
      logger.debug("busca folder de entrada");
      if ("0".equals(searchResponse.getStatusCode())) {
        logger.debug("Encuentra subserie de la solicitud minera");
        if (searchResponse.getResultSetList() != null) {
          if (Utilities.validateRecordTaxonomy(searchResponse.getResultSetList(), 2) == null)
            return new WCCResponse("ERWCC18"); 
          subSerieId = (String)((Map)searchResponse.getResultSetList().get(0)).get("fFolderGUID");
        } 
      } 
      searchCriteria = "fFolderName<matches>`" + (String)metadataList.get("xANM_Referencia") + "`";
      logger.debug("busca placa");
      searchResponse = folderMngr.search(searchCriteria);
      if ("0".equals(searchResponse.getStatusCode())) {
        if (searchResponse.getResultSetList() != null)
          if (Utilities.validateRecordTaxonomy(searchResponse.getResultSetList(), 2) != null)
            return new WCCResponse("ERWCC10");  
        wccResponse = folderMngr.create(subSerieId, metadataList.get("xANM_Referencia"));
        recordFolderId = (String)wccResponse.getMetaDataFields().get("fFolderGUID");
        if ("0".equals(wccResponse.getStatusCode())) {
          logger.debug("Expediente " + (String)metadataList.get("xANM_Referencia") + " creado");
          wccResponse = folderMngr.create(recordFolderId, Utilities.getCuadernosTituloMinero().get(0));
          if ("0".equals(wccResponse.getStatusCode())) {
            logger.debug("Cuaderno principal creado");
            if ("R".equals(metadataList.get("origenSolicitud"))) {
              metadataList.put("fFolderPath", (String)wccResponse.getMetaDataFields().get("fParentPath") + "/" + 
                  (String)Utilities.getCuadernosTituloMinero().get(0));
              metadataList.put("fFolderName", (String)wccResponse.getMetaDataFields().get("fFolderName"));
              metadataList.put("fFolderGUID", (String)wccResponse.getMetaDataFields().get("fFolderGUID"));
              documentMngr = new DocumentMngr(this.port);
              metadataList.put("xComments", (String)wccResponse.getMetaDataFields().get("xComments"));
              metadataList.put("xANM_TipoRecursoInformacion", "3");
              wccResponse = documentMngr.checkinNew(metadataList, null);
            } else if ("X".equals(metadataList.get("origenSolicitud"))) {
              logger.debug("Solicitud extemporcreada");
            } 
          } 
          if (!"0".equals(wccResponse.getStatusCode()))
            folderMngr.delete(recordFolderId); 
        } 
      } else {
        searchResponse.cleanMetaData();
        return (WCCResponse)searchResponse;
      } 
      wccResponse.cleanMetaData();
      return wccResponse;
    } catch (WSException e) {
      return new WCCResponse(e.getCode());
    } catch (Exception e) {
      logger.error("Error en la ejecucion del servicio: " + e.getMessage(), e);
      return new WCCResponse("ERWCC00");
    } finally {
      logger.debug("fin servicio crearExpedienteSolicitudMinera en " + (System.currentTimeMillis() - time) + "ms");
    } 
  }
  
  @WebResult(name = "RespuestaUCM", partName = "respuestaUCM", targetNamespace = "http://response.sgd.anm.gov.co")
  @WebMethod
  public WCCResponse promoverExpedienteSolicitudATitulo(@WebParam(name = "CreacionTitulo", partName = "creacionTitulo", targetNamespace = "http://transfer.sgd.anm.gov.co") Solicitud solicitud) throws WSException {
    FolderMngr folderMngr = null;
    Map<String, String> metadataList = null;
    WCCResponse respuestaUCM = null;
    SearchResponse searchResponse = null;
    Credenciales credenciales = null;
    String searchCriteria = null;
    String destination = null;
    String item = null;
    try {
      credenciales = new Credenciales();
      credenciales.setUsuario(PropertiesLoader.getProperty("WCC_CMC_USER"));
      credenciales.setContrasena(PropertiesLoader.getProperty("WCC_CMC_PASS"));
      this.port = ServiceLocator.getInstance().getGenericUCMPort(credenciales);
      metadataList = solicitud.getMetadatosList();
      Utilities.validateParams(metadataList, Utilities.getParameters("PARAM_CREAR_TITULO"));
      searchCriteria = "fFolderName<matches>`" + (String)metadataList.get("fFolderName") + "`";
      folderMngr = new FolderMngr(this.port);
      searchResponse = folderMngr.search(searchCriteria);
      if ("0".equals(searchResponse.getStatusCode())) {
        logger.debug("encuentra resultados de la subserie");
        if (searchResponse.getResultSetList() != null && 
          Utilities.validateRecordTaxonomy(searchResponse.getResultSetList(), 1) == null)
          return new WCCResponse("ERWCC11"); 
        destination = (String)((Map)searchResponse.getResultSetList().get(0)).get("fFolderGUID");
        logger.debug("busca numero de placa");
        searchCriteria = "fFolderName<matches>`" + (String)metadataList.get("xANM_Referencia") + "`";
        searchResponse = folderMngr.search(searchCriteria);
        if ("0".equals(searchResponse.getStatusCode())) {
          if (searchResponse.getResultSetList() != null) {
            if (Utilities.validateRecordTaxonomy(searchResponse.getResultSetList(), 2) == null)
              return new WCCResponse("ERWCC04"); 
            if (Utilities.validateRecordTaxonomy(searchResponse.getResultSetList(), 1) != null)
              return new WCCResponse("ERWCC11"); 
          } else {
            return new WCCResponse("ERWCC04");
          } 
          item = (String)((Map)searchResponse.getResultSetList().get(0)).get("fFolderGUID");
          respuestaUCM = folderMngr.move(destination, item);
          respuestaUCM.cleanMetaData();
          if ("0".equals(respuestaUCM.getStatusCode())) {
            List<String> recordBooksList = null;
            if (((String)metadataList.get("fFolderName")).equals(PropertiesLoader.getProperty("TITULO_PIN"))) {
              recordBooksList = Utilities.getCuadernosTituloPIN();
            } else {
              recordBooksList = Utilities.getCuadernosTituloMinero();
            } 
            destination = item;
            for (String bookName : recordBooksList) {
              if (!"CD01-PRINCIPAL".equals(bookName))
                folderMngr.create(destination, bookName); 
            } 
            return respuestaUCM;
          } 
          return respuestaUCM;
        } 
        return new WCCResponse("ERWCC12");
      } 
      searchResponse.cleanMetaData();
      return (WCCResponse)searchResponse;
    } catch (WSException e) {
      return new WCCResponse(e.getCode());
    } catch (Exception e) {
      logger.error("Error en la ejecucion del servicio: " + e.getMessage(), e);
      return new WCCResponse("ERWCC00");
    } 
  }
  
  @WebResult(name = "RespuestaUCM", partName = "respuestaUCM", targetNamespace = "http://response.sgd.anm.gov.co")
  @WebMethod
  public WCCResponse rechazarExpedienteSolicitudMinera(@WebParam(name = "RechazoSolicitud", partName = "rechazoSolicitud", targetNamespace = "http://transfer.sgd.anm.gov.co") Solicitud solicitud) throws WSException {
    FolderMngr folderMngr = null;
    Map<String, String> metadataList = null;
    Map<String, String> metadataSearch = null;
    WCCResponse wccResponse = null;
    Credenciales credenciales = null;
    SearchResponse searchResponse = null;
    String searchCriteria = null;
    try {
      credenciales = new Credenciales();
      credenciales.setUsuario(PropertiesLoader.getProperty("WCC_CMC_USER"));
      credenciales.setContrasena(PropertiesLoader.getProperty("WCC_CMC_PASS"));
      this.port = ServiceLocator.getInstance().getGenericUCMPort(credenciales);
      metadataList = solicitud.getMetadatosList();
      Utilities.validateParams(metadataList, Utilities.getParameters("PARAM_RECHAZAR_SOLICITUD"));
      searchCriteria = "fFolderName<matches>`" + (String)metadataList.get("xANM_Referencia") + "`";
      folderMngr = new FolderMngr(this.port);
      searchResponse = folderMngr.search(searchCriteria);
      if ("0".equals(searchResponse.getStatusCode())) {
        if (searchResponse.getResultSetList() != null) {
          metadataSearch = Utilities.validateRecordTaxonomy(searchResponse.getResultSetList(), 2);
          if (metadataSearch == null)
            return new WCCResponse("ERWCC04"); 
        } else {
          return new WCCResponse("ERWCC04");
        } 
        metadataList.put("fFolderGUID", metadataSearch.get("fFolderGUID"));
        metadataList.put("xANM_EstadoArchivo", "2");
        wccResponse = folderMngr.edit(metadataList);
        wccResponse.cleanMetaData();
        return wccResponse;
      } 
      searchResponse.cleanMetaData();
      return (WCCResponse)searchResponse;
    } catch (WSException e) {
      return new WCCResponse(e.getCode());
    } catch (Exception e) {
      logger.error("Error en la ejecucion del servicio: " + e.getMessage(), e);
      return new WCCResponse("ERWCC00");
    } 
  }
  
  @WebResult(name = "RespuestaUCM", partName = "respuestaUCM", targetNamespace = "http://response.sgd.anm.gov.co")
  @WebMethod
  public WCCResponse modificarModalidadExpedienteMinero(@WebParam(name = "ModificacionExpediente", partName = "modificacionExpediente", targetNamespace = "http://transfer.sgd.anm.gov.co") ModificacionExpediente modificacion) throws WSException {
    FolderMngr folderMngr = null;
    Map<String, String> metadataList = null;
    Map<String, String> metadataBrowse = null;
    List<Map<String, String>> resultSetRecord = null;
    SearchResponse searchResponse = null;
    BrowseResponse browseResponse = null;
    WCCResponse wccResponse = null;
    Credenciales credenciales = null;
    String searchCriteria = null;
    String recordId = null;
    try {
      credenciales = new Credenciales();
      credenciales.setUsuario(PropertiesLoader.getProperty("WCC_CMC_USER"));
      credenciales.setContrasena(PropertiesLoader.getProperty("WCC_CMC_PASS"));
      this.port = ServiceLocator.getInstance().getGenericUCMPort(credenciales);
      if (((modificacion.getNumeroPlaca() == null || modificacion.getDestino().trim() == "") && modificacion
        .getDestino() == null) || modificacion.getDestino().trim() == "")
        throw new WSException("ERWCC16", PropertiesLoader.getErrorProperty("ERWCC16")); 
      searchCriteria = "fFolderName<matches>`" + modificacion.getNumeroPlaca() + "`";
      folderMngr = new FolderMngr(this.port);
      searchResponse = folderMngr.search(searchCriteria);
      if ("0".equals(searchResponse.getStatusCode())) {
        resultSetRecord = searchResponse.getResultSetList();
        if (resultSetRecord != null) {
          if (2 == modificacion.getTipoModificacion().intValue()) {
            metadataList = Utilities.validateRecordTaxonomy(resultSetRecord, 2);
            if (metadataList == null)
              return new WCCResponse("ERWCC04"); 
            recordId = metadataList.get("fFolderGUID");
          } else if (1 == modificacion.getTipoModificacion().intValue()) {
            metadataList = Utilities.validateRecordTaxonomy(resultSetRecord, 1);
            if (metadataList == null)
              return new WCCResponse("ERWCC05"); 
            recordId = metadataList.get("fFolderGUID");
          } 
        } else {
          return new WCCResponse("ERWCC03", PropertiesLoader.getErrorProperty("ERWCC03"));
        } 
        metadataBrowse = new HashMap<>();
        metadataBrowse.put("path", (2 == modificacion.getTipoModificacion().intValue()) ? (
            PropertiesLoader.getProperty("PATH_SOLICITUDES_MINERAS") + "/" + modificacion.getDestino()) : (
            PropertiesLoader.getProperty("PATH_TITULOS_MINEROS") + "/" + modificacion.getDestino()));
        browseResponse = folderMngr.browse(metadataBrowse);
        if ("0".equals(browseResponse.getStatusCode())) {
          if (browseResponse.getFolderList() != null)
            for (FolderResponse folder : browseResponse.getFolderList()) {
              if (modificacion.getNumeroPlaca().equals(folder.getName())) {
                logger.debug("Encontrexpediente en subserie destino");
                return new WCCResponse("ERWCC17");
              } 
            }  
          wccResponse = folderMngr.move((String)browseResponse.getMetaDataFields().get("fFolderGUID"), recordId);
          wccResponse.cleanMetaData();
          return wccResponse;
        } 
        if ("-16".equals(browseResponse.getStatusCode()))
          return new WCCResponse("ERWCC18"); 
        browseResponse.cleanMetaData();
        return (WCCResponse)browseResponse;
      } 
      searchResponse.cleanMetaData();
      return (WCCResponse)searchResponse;
    } catch (WSException e) {
      return new WCCResponse(e.getCode());
    } catch (Exception e) {
      logger.error("Error en la ejecucion del servicio: " + e.getMessage(), e);
      return new WCCResponse("ERWCC00");
    } 
  }
  
  @WebResult(name = "RespuestaUCM", partName = "respuestaUCM", targetNamespace = "http://response.sgd.anm.gov.co")
  @WebMethod
  public WCCResponse validarExpedienteMinero(@WebParam(name = "NroPlacaValidacion", partName = "nroPlacaValidacion", targetNamespace = "http://transfer.sgd.anm.gov.co") String nroPlaca) throws Exception {
    logger.debug("Inicio servicio validarExpedienteMinero");
    long starTime = System.currentTimeMillis();
    WCCResponse response = null;
    ExpedienteMineroMngr expMineroMngr = null;
    try {
      response = new WCCResponse();
      expMineroMngr = new ExpedienteMineroMngr();
      return expMineroMngr.validarExpedienteMinero(nroPlaca);
    } catch (Exception e) {
      logger.error("Error en la ejecucion del servicio: " + e.getMessage(), e);
      throw e;
    } finally {
      logger.debug("Fin servicio validarExpedienteMinero en " + (System.currentTimeMillis() - starTime) + " ms");
    } 
  }
}
