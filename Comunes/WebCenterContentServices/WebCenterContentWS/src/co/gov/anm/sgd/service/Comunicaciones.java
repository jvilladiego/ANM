package co.gov.anm.sgd.service;

import co.gov.anm.sgd.bean.ComunicacionesLocal3;
import co.gov.anm.sgd.mngr.ComunicacionesMngr;
import co.gov.anm.sgd.response.AnexosRadicadoResponse;
import co.gov.anm.sgd.response.FechaVencimientoResponse;
import co.gov.anm.sgd.response.RadicadoResponse;
import co.gov.anm.sgd.response.UsuarioComunicacion;
import co.gov.anm.sgd.response.UsuarioResponse;
import co.gov.anm.sgd.response.WCCResponse;
import co.gov.anm.sgd.transfer.ComunicacionRequest;
import co.gov.anm.sgd.transfer.DependenciaRol;
import co.gov.anm.sgd.transfer.FechaVencimientoRequest;
import co.gov.anm.sgd.transfer.GeneracionODTRequest;
import co.gov.anm.sgd.transfer.ProcesoComunicacionRequest;
import co.gov.anm.sgd.transfer.ProcesoComunicacionResponse;
import co.gov.anm.sgd.transfer.RespuestaGeneradorDTO;
import co.gov.anm.sgd.transfer.SolicitudRadicado;
import static co.gov.anm.sgd.util.Constants.OK;
import static co.gov.anm.sgd.util.Constants.PATH_VIRTUAL_DIRECTORY;
import static co.gov.anm.sgd.util.Constants.PATH_VIRTUAL_DIRECTORY_HTTP;
import co.gov.anm.sgd.util.PropertiesLoader;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.Resource;

import javax.ejb.EJB;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;

@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService(name = "Comunicaciones", serviceName = "Comunicaciones", targetNamespace = "http://service.sgd.anm.gov.co",
            portName = "ComunicacionesPort", wsdlLocation = "WEB-INF/wsdl/Comunicaciones.wsdl")
public class Comunicaciones {
    
    private static Logger logger = Logger.getLogger(Comunicaciones.class);
    
    @Resource
    private WebServiceContext context;
    
    @EJB
    ComunicacionesLocal3 comunicacionesEjb;
    
    @WebResult(name = "NoRadicado", partName = "noRadicado", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public RadicadoResponse generarNumeroRadicado (@WebParam(name = "SolicitudRadicado", partName = "solicitudRadicado", targetNamespace = "http://transfer.sgd.anm.gov.co") 
                                                          SolicitudRadicado solicitud) throws Exception {
        RadicadoResponse radicado = null;        
        ComunicacionesMngr comunicacionesMngr = null;
        
        
            logger.debug("tipo comunicacion: " + solicitud.getTipoComunicacion());
            logger.debug("codigo dependencia: " + solicitud.getCodDependencia());
            
            if ( solicitud.getTipoComunicacion()==null||solicitud.getTipoComunicacion().trim().equals("") ){
                throw new Exception("Comunicacion.generarNumeroRadicado() -> Tipo Comunicacion Nulo o Vacio");
            }
            
            if ( solicitud.getCodDependencia()==null||solicitud.getCodDependencia().trim().equals("") ){
                throw new Exception("Comunicacion.generarNumeroRadicado() -> Codigo Dependencia Nulo o Vacio");
            }
            
            comunicacionesMngr = new ComunicacionesMngr();
            
            radicado = new RadicadoResponse();
            radicado.setNoRadicado(comunicacionesMngr.generarNroRadicado(solicitud.getTipoComunicacion(), 
                                                    solicitud.getCodDependencia(), comunicacionesEjb));
            radicado.setStatusCode(OK);
            radicado.setStatusMessage("Success");
            
            comunicacionesMngr = new ComunicacionesMngr();
            
            logger.debug("numero de radicado generado: " + radicado.getNoRadicado());
            
            return radicado;
        
    }
    
    @WebResult(name = "RespuestaCreacion", partName = "respuestaCreacion", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public WCCResponse crearComunicacion (@WebParam(name = "CreacionComunicacion", partName = "creacionComunicacion", targetNamespace = "http://transfer.sgd.anm.gov.co") 
                                                          ComunicacionRequest comunicacion) throws Exception {
        logger.debug("Inicio servicio crearComunicacion");
        long starTime = System.currentTimeMillis();
        
        ComunicacionesMngr comunicacionesMngr = null;        
        try {            
            comunicacionesMngr = new ComunicacionesMngr();
            return comunicacionesMngr.crearComunicacion(comunicacion, comunicacionesEjb);
        } catch (Exception e) {
            logger.error("instance number: "+comunicacion.getIdComunicacion()+"nro radicado: "+comunicacion.getNroRadicado() + "--> Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            throw e;
        } finally {
            logger.debug("Fin servicio crearComunicacion en " + (System.currentTimeMillis() - starTime) + " ms");
        }
    }
    
    @WebResult(name = "RespuestaRegistro", partName = "respuestaRegistro", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public WCCResponse registrarComunicacion (@WebParam(name = "RegistroComunicacion", partName = "RegistroComunicacion", targetNamespace = "http://transfer.sgd.anm.gov.co") 
                                                ProcesoComunicacionRequest request) throws Exception {
        logger.debug("Inicio servicio registrarComunicacion");
        long starTime = System.currentTimeMillis();
        
        ComunicacionesMngr mngr = new ComunicacionesMngr();
        WCCResponse response = new WCCResponse();
        try {
            mngr.crearProcesoComunicacion(request, comunicacionesEjb);
            
            response.setStatusCode("0");
        } catch (Exception e) {
            logger.error("instance number: "+request.getComunicacion().getIdComunicacion()+"nro radicado: "+
                         request.getComunicacion().getNroRadicado() + "--> Error en la ejecucion del servicio: " + 
                         e.getMessage(), e);
            
            /*response.setStatusCode(ERWCC00);
            response.setStatusMessage(e.getMessage());*/
            throw e;
        }  finally {
            logger.debug("Fin servicio registrarComunicacion en " + (System.currentTimeMillis() - starTime) + " ms");
        }
        return response;
    }
    
    @WebResult(name = "RespuestaActualizacionProc", partName = "respuestaActualizacionProc", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public WCCResponse actualizarProcesoComunicacion (@WebParam(name = "ActualizacionProceso", partName = "actualizacionProceso", targetNamespace = "http://transfer.sgd.anm.gov.co")
                                                      ProcesoComunicacionRequest comunicacion) throws Exception{
        logger.debug("Inicio servicio actualizarProcesoComunicacion");
        long starTime = System.currentTimeMillis();
        
        ComunicacionesMngr comunicacionesMngr = new ComunicacionesMngr();
                
        try {
           return comunicacionesMngr.actualizarProcesoComunicacion(comunicacion, comunicacionesEjb);
        } catch (Exception e) {
            logger.error(comunicacion.getComunicacion().getNroRadicado() + " --> Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            //return new WCCResponse(ERWCC00, e.getMessage());
            throw e;
        } finally {
            logger.debug("Fin servicio actualizarProcesoComunicacion en " + (System.currentTimeMillis() - starTime) + " ms");
        }
    }
    
    @WebResult(name = "RespuestaActualizacion", partName = "respuestaActualizacion", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public WCCResponse actualizarComunicacion (@WebParam(name = "ActualizacionComunicacion", partName = "actualizacionComunicacion", targetNamespace = "http://transfer.sgd.anm.gov.co") 
                                                          ComunicacionRequest comunicacion) throws Exception {
        logger.debug("Inicio servicio actualizarComunicacion");
        long starTime = System.currentTimeMillis();
        ComunicacionesMngr comunicacionesMngr = new ComunicacionesMngr();
        
        try {
            logger.debug("numero de radicado: " + comunicacion.getNroRadicado());
            
            return comunicacionesMngr.actualizarComunicacion(comunicacion, comunicacionesEjb);
        } catch (Exception e) {
            logger.error(comunicacion.getNroRadicado() + "--> Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            //return new WCCResponse(ERWCC00);
            throw e;
        } finally {
            logger.debug("Fin servicio actualizarComunicacion en " + (System.currentTimeMillis() - starTime) + " ms");
        }
    }
    
    @WebResult(name = "ComunicacionResponse", partName = "comunicacionResponse", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public ProcesoComunicacionResponse consultarComunicacion (@WebParam(name = "NroRadicadoComunicacion", partName = "nroRadicadoComunicacion", targetNamespace = "http://transfer.sgd.anm.gov.co")
                                                       String nroRadicado) throws Exception {
        
        logger.debug("Inicio servicio consultarComunicacion");
        long starTime = System.currentTimeMillis();
        
        ComunicacionesMngr comunicacionesMngr = null;
        ProcesoComunicacionResponse response = new ProcesoComunicacionResponse();
        
        try {
            logger.debug("numero de radicado: " + nroRadicado);
            
            if ( nroRadicado==null || nroRadicado.trim().equals("") ){
                throw new Exception("Comunicaciones.consultarComunicacion() -> nroRadicado recibido nulo o vacio");
            }
             
            comunicacionesMngr = new ComunicacionesMngr();            
            response = comunicacionesMngr.consultarComunicacion(nroRadicado, comunicacionesEjb);            
        } catch (Exception e) {
            logger.error(nroRadicado + "--> Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            /*response.setCodigoRespuesta(ERWCC00);
            response.setMensajeRespuesta(e.getMessage());*/
            throw e;
        } finally {
            logger.debug("Fin servicio consultarComunicacion en " + (System.currentTimeMillis() - starTime) + " ms");
        }
        return response;
    }
    
    @WebResult(name = "UsuarioComunicacion", partName = "usuarioComunicacion", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public UsuarioComunicacion consultarUsuario (@WebParam(name = "IdUsuario", partName = "idUsuario", targetNamespace = "http://transfer.sgd.anm.gov.co")
                                                  String idUsuario) throws Exception {        
        logger.debug("Inicio servicio consultarUsuario");
        long starTime = System.currentTimeMillis();
        
        ComunicacionesMngr mngr = null;
        
        try {
            logger.debug("Busca roles por id de usuario: " + idUsuario);
            
            if ( idUsuario==null || idUsuario.trim().equals("") ){
                throw new Exception("Comunicaciones.consultarUsuario() -> idUsuario recibido nulo o vacio");
            }
            
            mngr = new ComunicacionesMngr();
            return mngr.consultarUsuario(idUsuario, comunicacionesEjb);
        } catch (Exception e) {
            logger.error(idUsuario + "--> Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            //return new UsuarioComunicacion(ERWCC00);
            throw e;
        } finally {
            logger.debug("Fin servicio consultarUsuario en " + (System.currentTimeMillis() - starTime) + " ms");
        }
    }
    
    @WebResult(name = "UsuariosRol", partName = "usuariosRol", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public UsuarioResponse buscarUsuariosPorDependenciaRol (@WebParam(name = "dependenciaRol", partName = "DependenciaRol", targetNamespace = "http://transfer.sgd.anm.gov.co") 
                                                            DependenciaRol dependenciaRol) throws Exception{        
        logger.debug("Inicio servicio buscarUsuariosPorDependenciaRol");
        long starTime = System.currentTimeMillis();
        
        ComunicacionesMngr mngr = new ComunicacionesMngr();
        
        try {
            if ( dependenciaRol==null || dependenciaRol.getCodigoDependencia()==null ){
                throw new Exception("Comunicaciones.buscarUsuariosPorDependenciaRol() -> dependencia recibido nulo o vacio");
            }
            
            if ( dependenciaRol.getIdRol()==null ){
                throw new Exception("Comunicaciones.buscarUsuariosPorDependenciaRol() -> rol recibido nulo o vacio");
            }
        
            logger.debug("Busca usuarios por rol: " + dependenciaRol.getIdRol() + " y dependencia: " + dependenciaRol.getCodigoDependencia());
            
            return mngr.buscarUsuariosPorDependenciaRol(dependenciaRol, comunicacionesEjb);
        } catch (Exception e) {
            logger.error("Dep: " + dependenciaRol.getCodigoDependencia() + ", Rol: " + dependenciaRol.getIdRol() + " -->Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            //return new UsuarioResponse(ERWCC00);
            throw e;
        } finally {
            logger.debug("Fin servicio buscarUsuariosPorDependenciaRol en " + (System.currentTimeMillis() - starTime) + " ms");
        }
    }
    
    @WebResult(name = "AnexosRadicadoResponse", partName = "anexosRadicadoResponse", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public AnexosRadicadoResponse consultarAnexosRadicado (@WebParam(name = "NroRadicadoAnexo", partName = "nroRadicadoAnexo", targetNamespace = "http://transfer.sgd.anm.gov.co")
                                                           String nroRadicado) throws Exception {
        logger.debug("Inicio servicio consultarAnexosRadicado");
        long starTime = System.currentTimeMillis();
        
        AnexosRadicadoResponse response = new AnexosRadicadoResponse();
        ComunicacionesMngr comunicacionesMngr = new ComunicacionesMngr();
        
        try {
            if ( nroRadicado==null || nroRadicado.trim().equals("") ){
                throw new Exception("Comunicaciones.consultarAnexosRadicado() -> nroRadicado recibido nulo o vacio");
            }
            
            return comunicacionesMngr.consultarAnexosRadicado(nroRadicado);
        /*} catch (WSException e) {
            logger.error(nroRadicado + "--> Error en el servicio web: " + e.getMessage(), e);
            response.setCodigoEstado(e.getCode());
            response.setMensajeRsp(PropertiesLoader.getProperty(e.getCode()));*/
        } catch (Exception e) {
            logger.error(nroRadicado + "--> Error en la ejecucion del servicio: " + e.getMessage(), e);
            /*response.setCodigoEstado(ERWCC00);
            response.setMensajeRsp(e.getMessage());*/
            throw e;
        } finally {
            logger.debug("Fin servicio consultarAnexosRadicado en " + (System.currentTimeMillis() - starTime) + " ms");
        }
        //return response;
    }
    
    @WebResult(name = "GeneracionODTResponse", partName = "generacionODTResponse", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public RespuestaGeneradorDTO generarBarCode(@WebParam(name = "GeneracionODTRequest", partName = "generacionODTRequest", targetNamespace = "http://transfer.sgd.anm.gov.co")
                                                    GeneracionODTRequest request) throws Exception{
        logger.debug("Inicio servicio generarArchivoODT");
        long starTime = System.currentTimeMillis();
        
        String virtualDirectoryHttp = PropertiesLoader.getProperty(PATH_VIRTUAL_DIRECTORY_HTTP);
        String virtualDirectory = PropertiesLoader.getProperty(PATH_VIRTUAL_DIRECTORY);
        
        ComunicacionesMngr comunicacionesMngr = new ComunicacionesMngr();
        RespuestaGeneradorDTO response = new RespuestaGeneradorDTO();
        
        if ( request==null || request.getRutaArchivo()==null || request.getRutaArchivo().trim().equals("") ){
            throw new Exception("Comunicaciones.generarBarCode() -> nroRadicadono se recibe archivo para realizar el merge");
        }
        
        try {
            logger.debug("Previo invocacion de generacion de documento PDF");
            response = comunicacionesMngr.generarBarCode(request, comunicacionesEjb);
            logger.debug("Posterior invocacion de generacion de documento PDF");
            if ( response.getRutaArchivoFinal()!=null ){
                StringBuilder urlFile = new StringBuilder(virtualDirectoryHttp);                                
                String fileName = response.getRutaArchivoFinal().substring(response.getRutaArchivoFinal().lastIndexOf(File.separator));
                urlFile.append(fileName);
                
                //Copia el archivo al virtual directory       
                Path src = Paths.get(response.getRutaArchivoFinal().toString());
                Path dest = Paths.get(virtualDirectory+fileName);
                Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
                logger.debug("1. urlFinalArchivo: "+urlFile);
                response.setRutaArchivoFinal(urlFile.toString());   
           }
        } catch (Exception e) {
            logger.error(request.getComunicacion().getNroRadicado() + "--> Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            //response.setRespuesta(ERWCC00);
            throw e;
        } finally {
            logger.debug("Fin servicio generarArchivoODT en " + (System.currentTimeMillis() - starTime) + " ms");
        }
        return response;
    }      
    
    @WebResult(name = "FechaVencimientoResponse", partName = "fechaVencimientoResponse", targetNamespace = "http://response.sgd.anm.gov.co")
    @WebMethod
    public FechaVencimientoResponse calcularFechaVencimiento (@WebParam(name = "FechaVencimientoRequest", partName = "fechaVencimientoRequest", targetNamespace = "http://transfer.sgd.anm.gov.co")
                                            FechaVencimientoRequest request) throws Exception {
        logger.debug("Inicio servicio calcularFechaVencimiento");
        long starTime = System.currentTimeMillis();
        
        ComunicacionesMngr comunicacionesMngr = new ComunicacionesMngr();
        FechaVencimientoResponse response = new FechaVencimientoResponse();
        
        try {  
            if ( request==null || request.getDiasRespuesta()==null  ){
                throw new Exception("Comunicaciones.calcularFechaVencimiento() -> diasRespuesta no recibido o nulo");
            }
            if ( request==null || request.getFechaRadicacion()==null  ){
                throw new Exception("Comunicaciones.calcularFechaVencimiento() -> fechaRadicacion no recibido o nulo");
            }
            response = comunicacionesMngr.calcularFechaVencimiento(request.getFechaRadicacion(), 
                                                                request.getDiasRespuesta(), comunicacionesEjb);
        } catch (Exception e) {
            logger.error("Error en la ejecucion del servicio: " + e.getMessage(), e);
            
            /*response.setCodigo(ERWCC00);
            response.setMensaje(e.getMessage());*/
            throw e;
        } finally {
            logger.debug("Fin servicio calcularFechaVencimiento en " + (System.currentTimeMillis() - starTime) + " ms");
        }
        return response;
    }
}
