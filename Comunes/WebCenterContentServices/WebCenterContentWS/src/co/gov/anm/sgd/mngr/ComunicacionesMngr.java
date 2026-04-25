package co.gov.anm.sgd.mngr;

import co.gov.anm.sgd.bean.ComunicacionesLocal3;
import co.gov.anm.sgd.entity.SgdAnexoComunicacion;
import co.gov.anm.sgd.entity.SgdComunicacion;
import co.gov.anm.sgd.entity.SgdInteresado;
import co.gov.anm.sgd.entity.SgdUsuario;
import co.gov.anm.sgd.entity.SgdUsuarioRol;
import co.gov.anm.sgd.entity.Subcategoria;
import co.gov.anm.sgd.entity.TipoSolicitante;
import co.gov.anm.sgd.entity.TipoSolicitud;
import co.gov.anm.sgd.exception.WSException;
import co.gov.anm.sgd.integration.location.ServiceLocator;
import co.gov.anm.sgd.model.GrafoFirmaDigital;
import co.gov.anm.sgd.response.AnexoRadicado;
import co.gov.anm.sgd.response.AnexosRadicadoResponse;
import co.gov.anm.sgd.response.FechaVencimientoResponse;
import co.gov.anm.sgd.response.Rol;
import co.gov.anm.sgd.response.SearchResponse;
import co.gov.anm.sgd.response.Usuario;
import co.gov.anm.sgd.response.UsuarioComunicacion;
import co.gov.anm.sgd.response.UsuarioResponse;
import co.gov.anm.sgd.response.WCCResponse;
import co.gov.anm.sgd.transfer.AnexoComunicacionRequest;
import co.gov.anm.sgd.transfer.ComunicacionODT;
import co.gov.anm.sgd.transfer.ComunicacionRequest;
import co.gov.anm.sgd.transfer.Credenciales;
import co.gov.anm.sgd.transfer.DependenciaRol;
import co.gov.anm.sgd.transfer.GeneracionODTRequest;
import co.gov.anm.sgd.transfer.InteresadoRequest;
import co.gov.anm.sgd.transfer.ProcesoComunicacionRequest;
import co.gov.anm.sgd.transfer.ProcesoComunicacionResponse;
import co.gov.anm.sgd.transfer.RespuestaGeneradorDTO;
import co.gov.anm.sgd.util.CamposODTProperties;
import static co.gov.anm.sgd.util.Constants.ERWCC21;
import static co.gov.anm.sgd.util.Constants.OK;
import static co.gov.anm.sgd.util.Constants.URL_IDC;
import static co.gov.anm.sgd.util.Constants.WCC_BPM_PASSWORD;
import static co.gov.anm.sgd.util.Constants.WCC_BPM_USER;
import static co.gov.anm.sgd.util.Constants.dID;
import static co.gov.anm.sgd.util.Constants.docName;
import static co.gov.anm.sgd.util.Constants.docTitle;
import co.gov.anm.sgd.util.PDFConverter;
import co.gov.anm.sgd.util.PropertiesLoader;
import co.gov.anm.sgd.util.TemplateMerger;
import co.gov.anm.sgd.util.Utilities;

import genericsoap.GenericSoapPortType;

import java.io.File;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import org.odftoolkit.simple.TextDocument;

/**
 * Clase que implementa los servicios expuestos en la clase <code>Comunicacion</code>
 */
public class ComunicacionesMngr {

    private static final Logger logger = Logger.getLogger(ComunicacionesMngr.class);

    private Credenciales credenciales;

    public ComunicacionesMngr() {
        this.credenciales = new Credenciales();
        this.credenciales.setUsuario(PropertiesLoader.getProperty(WCC_BPM_USER));
        this.credenciales.setContrasena(PropertiesLoader.getProperty(WCC_BPM_PASSWORD));
        //logger.info("WCC_BPM_USER : "+this.credenciales.getUsuario());
        //logger.info("WCC_BPM_PASSWORD : "+this.credenciales.getContrasena());
    }

    public UsuarioComunicacion consultarUsuario(String idUsuario, ComunicacionesLocal3 ejb) throws Exception {
        UsuarioComunicacion response = new UsuarioComunicacion();
        List<Rol> rolList = null;

        SgdUsuario usuario = ejb.getUsuarioById(idUsuario);

        if (usuario != null) {
            response.setStatusCode(OK);
            response.setStatusMessage("Success");
            response.setCodigoDependencia(usuario.getCodigoDependencia());
            response.setEmail(usuario.getEmail());
            response.setIdUsuario(usuario.getIdUsuario());
            response.setNombreUsuario(usuario.getNombreUsuario());

            if (usuario.getSgdUsuarioRolList() != null && usuario.getSgdUsuarioRolList().size() > 0) {
                rolList = new ArrayList<Rol>();
                for (SgdUsuarioRol usuarioRol : usuario.getSgdUsuarioRolList()) {
                    Rol rol = new Rol();
                    rol.setIdRol(usuarioRol.getSgdRol().getIdRol());
                    rol.setNombreRol(usuarioRol.getSgdRol().getNombreRol());

                    rolList.add(rol);
                }
                response.setRolList(rolList);
            }
        } else {
            response.setStatusCode("-1");
            response.setStatusMessage("Usuario no existe");
        }
        return response;
    }

    public UsuarioResponse buscarUsuariosPorDependenciaRol(DependenciaRol dependenciaRol,
                                                           ComunicacionesLocal3 ejb) throws Exception {
        UsuarioResponse response = new UsuarioResponse();
        List<Usuario> usuariosList = null;
        Object[] usuarios = null;

        List consulta = ejb.findUsuarioByDepRol(dependenciaRol.getIdRol(), dependenciaRol.getCodigoDependencia());

        if (consulta != null && consulta.size() > 0) {
            Iterator it = consulta.iterator();

            usuariosList = new ArrayList<Usuario>();

            usuarios = consulta.toArray();

            logger.debug("usuarios consultados: " + usuarios.length);
            while (it.hasNext()) {
                Object[] usuario = (Object[]) it.next();

                Usuario usuarioResponse = new Usuario();
                usuarioResponse.setCodigoDependencia(((BigDecimal) usuario[2]).longValue());
                usuarioResponse.setEmail((String) usuario[3]);
                usuarioResponse.setIdUsuario((String) usuario[0]);
                usuarioResponse.setNombreUsuario((String) usuario[1]);

                usuariosList.add(usuarioResponse);
            }
            response.setUsuarios(usuariosList);
            response.setStatusCode(OK);
            response.setStatusMessage("Success");
        } else {
            response.setStatusCode("-1");
            response.setStatusMessage("No se encontraron usuarios");
        }

        return response;
    }

    public WCCResponse crearProcesoComunicacion(ProcesoComunicacionRequest request,
                                                ComunicacionesLocal3 ejb) throws Exception {
        List<SgdAnexoComunicacion> anexosList = null;
        List<SgdInteresado> interesadosList = null;

        logger.debug("crear comunicacion con radicado: " + request.getComunicacion().getNroRadicado());

        SgdComunicacion sgdComunicacion = new SgdComunicacion();
        sgdComunicacion.setAccesoNegado(request.getComunicacion().getAccesoNegado());
        sgdComunicacion.setAsunto(Utilities.truncateStringValue(request.getComunicacion().getAsunto(), 500));
        sgdComunicacion.setCodSerie(request.getComunicacion().getCodigoSerie());
        sgdComunicacion.setCodSubserie(request.getComunicacion().getCodigoSubSerie());
        sgdComunicacion.setComentarios(Utilities.truncateStringValue(request.getComunicacion().getComentarios(), 200));
        sgdComunicacion.setContentFldComunicaciones(request.getComunicacion().getContentFLdComunicaciones());
        sgdComunicacion.setContentFldOtro(request.getComunicacion().getContentFldOtro());
        sgdComunicacion.setContentFldTitulo(request.getComunicacion().getContentFldTitulo());
        sgdComunicacion.setDiasParaRespuesta(request.getComunicacion().getDiasParaRespuesta());
        sgdComunicacion.setEsPqrs(request.getComunicacion().getEsPQRS());
        sgdComunicacion.setEsTitulo(request.getComunicacion().getEsTitulo());
        sgdComunicacion.setEstadoComunicacion(request.getComunicacion().getEstadoComunicacion());
        sgdComunicacion.setFechaCreacion(request.getComunicacion()
                                                .getFechaCreacion()
                                                .getTime());
        sgdComunicacion.setFechaVencimiento(request.getComunicacion().getFechaVencimiento() != null ?
                                            request.getComunicacion()
                                                                                                             .getFechaVencimiento()
                                                                                                             .getTime() :
                                                                                         null);
        sgdComunicacion.setGrupoSeguridad(request.getComunicacion().getGrupoSeguridad());
        sgdComunicacion.setIdDependenciaDestino(request.getComunicacion().getIdDependenciaDestino());
        sgdComunicacion.setIdTipoDocumentalTramite(request.getComunicacion().getIdTipoDocumentalTramite());
        sgdComunicacion.setIdTramite(request.getComunicacion().getIdTramite());
        sgdComunicacion.setIdUnidadProductora(request.getComunicacion().getIdUnidadProductora());
        sgdComunicacion.setIdentifReservada(request.getComunicacion().getIdReservada());
        sgdComunicacion.setNombreSerie(request.getComunicacion().getNombreSerie());
        sgdComunicacion.setNombreSubserie(request.getComunicacion().getNombreSubSerie());
        sgdComunicacion.setNombreTpDoc(request.getComunicacion().getNombreTpDoc());
        sgdComunicacion.setNroAnexos(request.getComunicacion().getNroAnexos());
        sgdComunicacion.setNroFolios(Utilities.truncateIntegerValue(request.getComunicacion().getNroFolios(), 4));
        sgdComunicacion.setNroPlaca(Utilities.truncateStringValue(request.getComunicacion().getNroPlaca(), 100));
        sgdComunicacion.setNroRadicado(request.getComunicacion().getNroRadicado());
        sgdComunicacion.setNroRadicadoRelacionado(Utilities.truncateStringValue(request.getComunicacion()
                                                                                .getNroRadicadoRelacionado(), 100));
        sgdComunicacion.setReferencia(Utilities.truncateStringValue(request.getComunicacion().getReferencia(), 200));
        sgdComunicacion.setRequiereRespuesta(request.getComunicacion().getRequiereRespuesta());
        sgdComunicacion.setTipoComunicacion(request.getComunicacion().getTipoComunicacion());
        sgdComunicacion.setTramite(request.getComunicacion().getTramite());
        sgdComunicacion.setTrasladoOtraEntidad(request.getComunicacion().getTrasladoOtraEntidad());
        sgdComunicacion.setUsuarioCreacion(request.getComunicacion().getUsuarioCreacion());
        sgdComunicacion.setInstanceNumber(request.getComunicacion().getIdComunicacion() != null ? request.getComunicacion()
                                                                                                         .getIdComunicacion()
                                                                                                         .longValue() :
                                          null);

        if (request.getComunicacion().getIdTipoSolicitud() != null &&
            request.getComunicacion().getIdTipoSolicitud() > 0) {
            TipoSolicitud tipo = ejb.getTipoSolicitudById(request.getComunicacion().getIdTipoSolicitud());
            sgdComunicacion.setTipoSolicitud(tipo);
        }

        if (request.getComunicacion().getIdSubcategoria() != null) {
            Subcategoria subcategoria =
                ejb.getSubcategoriaById(new BigDecimal(request.getComunicacion().getIdSubcategoria()));
            sgdComunicacion.setSubcategoria(subcategoria);
        }

        sgdComunicacion.setMedioResuesta(request.getComunicacion().getMedioRespuesta());

        if (request.getAnexosList() != null) {
            anexosList = new ArrayList<SgdAnexoComunicacion>();

            logger.debug("anexos a registrar: " + request.getAnexosList().size());

            for (AnexoComunicacionRequest anexo : request.getAnexosList()) {
                SgdAnexoComunicacion sgdAnexo = new SgdAnexoComunicacion();
                sgdAnexo.setCantidad(anexo.getCantidad());
                sgdAnexo.setDescripcion(Utilities.truncateStringValue(anexo.getDescripcion(), 200));
                sgdAnexo.setEnlaceContent(anexo.getEnlaceContent());
                logger.debug("JVenegas->RutaWCC (Crear): " + anexo.getEnlaceContent());
                sgdAnexo.setNumeroRadicado(anexo.getNroRadicado());
                sgdAnexo.setRutaLocalCargue(anexo.getRutaLocalCargue());
                sgdAnexo.setSgdComunicacion1(sgdComunicacion);

                anexosList.add(sgdAnexo);
            }
            sgdComunicacion.setSgdAnexoComunicacionList(anexosList);
        }

        if (request.getInteresadosList() != null) {
            interesadosList = new ArrayList<SgdInteresado>();

            logger.debug("interesados a registrar: " + request.getInteresadosList().size());

            for (InteresadoRequest interesado : request.getInteresadosList()) {
                SgdInteresado sgdInteresado = new SgdInteresado();
                sgdInteresado.setCelular(Utilities.truncateStringValue(interesado.getCelular(), 20));
                sgdInteresado.setCiudad(interesado.getCiudad());
                sgdInteresado.setDireccion(Utilities.truncateStringValue(interesado.getDireccion(), 200));
                sgdInteresado.setEmail(Utilities.truncateStringValue(interesado.getEmail(), 200));
                sgdInteresado.setIdDepartamento(interesado.getIdDepartamento() != null ?
                                                new BigDecimal(interesado.getIdDepartamento()) : null);
                sgdInteresado.setIdEtnia(interesado.getIdEtnia() != null ? new BigDecimal(interesado.getIdEtnia()) :
                                         null);
                sgdInteresado.setIdMunicipio(interesado.getIdMunicipio() != null ?
                                             new BigDecimal(interesado.getIdMunicipio()) : null);
                sgdInteresado.setIdPais(interesado.getIdPais() != null ? new BigDecimal(interesado.getIdPais()) : null);
                sgdInteresado.setIdPoblacionVulnerable(interesado.getIdPoblacionVulnerable() != null ?
                                                       new BigDecimal(interesado.getIdPoblacionVulnerable()) : null);
                sgdInteresado.setIdRangoEdad(interesado.getIdRangoEdad() != null ?
                                             new BigDecimal(interesado.getIdRangoEdad()) : null);
                sgdInteresado.setNumeroIdentificacion(Utilities.truncateStringValue(interesado.getNroIdentificacion(),
                                                                                    20));
                sgdInteresado.setOcupacion(Utilities.truncateStringValue(interesado.getOcupacion(), 100));
                sgdInteresado.setPrimerApellido(Utilities.truncateStringValue(interesado.getPrimerApellido(), 100));
                sgdInteresado.setPrimerNombre(Utilities.truncateStringValue(interesado.getPrimerNombre(), 100));
                sgdInteresado.setSegundoApellido(Utilities.truncateStringValue(interesado.getSegundoApellido(), 100));
                sgdInteresado.setSegundoNombre(Utilities.truncateStringValue(interesado.getSegundoNombre(), 100));
                sgdInteresado.setSgdComunicacion(sgdComunicacion);
                sgdInteresado.setTelefono(Utilities.truncateStringValue(interesado.getTelefono(), 20));
                sgdInteresado.setTipoIdentificacion(interesado.getTipoIdentificacion());

                if (interesado.getTipoSolicitante() != null) {

                    TipoSolicitante tipoSolicitante = ejb.getTipoSolicitanteById(interesado.getTipoSolicitante());
                    sgdInteresado.setTipoSolicitante(tipoSolicitante);
                }

                interesadosList.add(sgdInteresado);
            }
            sgdComunicacion.setSgdInteresadoList(interesadosList);
        }

        logger.debug("llama al servicio a traves del ejb");

        ejb.insertComunicacion(sgdComunicacion);

        logger.debug("comunicacion");

        WCCResponse response = new WCCResponse();
        response.setStatusCode(OK);
        response.setStatusMessage("Success");

        return response;
    }

    public WCCResponse crearComunicacion(ComunicacionRequest request, ComunicacionesLocal3 ejb) throws Exception {
        logger.debug("crear comunicacion con radicado: " + request.getNroRadicado());

        SgdComunicacion sgdComunicacion = new SgdComunicacion();
        sgdComunicacion.setAccesoNegado(request.getAccesoNegado());
        sgdComunicacion.setAsunto(Utilities.truncateStringValue(request.getAsunto(), 500));
        sgdComunicacion.setCodSerie(request.getCodigoSerie());
        sgdComunicacion.setCodSubserie(request.getCodigoSubSerie());
        sgdComunicacion.setComentarios(Utilities.truncateStringValue(request.getComentarios(), 200));
        sgdComunicacion.setContentFldComunicaciones(request.getContentFLdComunicaciones());
        sgdComunicacion.setContentFldOtro(request.getContentFldOtro());
        sgdComunicacion.setContentFldTitulo(request.getContentFldTitulo());
        sgdComunicacion.setDiasParaRespuesta(request.getDiasParaRespuesta());
        sgdComunicacion.setEsPqrs(request.getEsPQRS());
        sgdComunicacion.setEsTitulo(request.getEsTitulo());
        sgdComunicacion.setEstadoComunicacion(request.getEstadoComunicacion());
        sgdComunicacion.setFechaCreacion(request.getFechaCreacion().getTime());
        sgdComunicacion.setFechaVencimiento(request.getFechaVencimiento() != null ?
                                            request.getFechaVencimiento().getTime() : null);
        sgdComunicacion.setGrupoSeguridad(request.getGrupoSeguridad());
        sgdComunicacion.setIdDependenciaDestino(request.getIdDependenciaDestino());
        sgdComunicacion.setIdTipoDocumentalTramite(request.getIdTipoDocumentalTramite());
        sgdComunicacion.setIdTramite(request.getIdTramite());
        sgdComunicacion.setIdUnidadProductora(request.getIdUnidadProductora());
        sgdComunicacion.setIdentifReservada(request.getIdReservada());
        sgdComunicacion.setNombreSerie(request.getNombreSerie());
        sgdComunicacion.setNombreSubserie(request.getNombreSubSerie());
        sgdComunicacion.setNombreTpDoc(request.getNombreTpDoc());
        sgdComunicacion.setNroAnexos(request.getNroAnexos());
        sgdComunicacion.setNroFolios(Utilities.truncateIntegerValue(request.getNroFolios(), 4));
        sgdComunicacion.setNroPlaca(Utilities.truncateStringValue(request.getNroPlaca(), 100));
        sgdComunicacion.setNroRadicado(request.getNroRadicado());
        sgdComunicacion.setNroRadicadoRelacionado(Utilities.truncateStringValue(request.getNroRadicadoRelacionado(),
                                                                                100));
        sgdComunicacion.setReferencia(Utilities.truncateStringValue(request.getReferencia(), 200));
        sgdComunicacion.setRequiereRespuesta(request.getRequiereRespuesta());
        sgdComunicacion.setTipoComunicacion(request.getTipoComunicacion());
        sgdComunicacion.setTramite(request.getTramite());
        sgdComunicacion.setTrasladoOtraEntidad(request.getTrasladoOtraEntidad());
        sgdComunicacion.setUsuarioCreacion(request.getUsuarioCreacion());
        sgdComunicacion.setInstanceNumber(request.getIdComunicacion() != null ?
                                          request.getIdComunicacion().longValue() : null);

        ejb.insertComunicacion(sgdComunicacion);

        WCCResponse response = new WCCResponse();
        response.setStatusCode(OK);
        response.setStatusMessage("Success");

        return response;
    }

    public WCCResponse actualizarProcesoComunicacion(ProcesoComunicacionRequest request,
                                                     ComunicacionesLocal3 ejb) throws Exception {
        logger.debug("consulta comunicacion con nro radicado " + request.getComunicacion().getNroRadicado());
        List<SgdComunicacion> comunicacionConsultaList =
            ejb.getSgdComunicacionByNroRadicado(request.getComunicacion().getNroRadicado());
        SgdComunicacion comunicacion = null;
        List<SgdAnexoComunicacion> sgdAnexos = null;
        List<SgdInteresado> interesadosConsulta = null;
        List<SgdInteresado> interesadosAct = null;

        if (comunicacionConsultaList == null || comunicacionConsultaList.size() == 0) {
            throw new Exception("No existe comunicacion para actualizar: " +
                                request.getComunicacion().getNroRadicado());
        } else {
            logger.debug("comunicaciones consultadas: " + comunicacionConsultaList.size());
            comunicacion = comunicacionConsultaList.get(0);
        }

        if (request.getComunicacion().getAccesoNegado() != null) {
            comunicacion.setAccesoNegado(request.getComunicacion().getAccesoNegado());
        }
        if (request.getComunicacion().getAsunto() != null) {
            comunicacion.setAsunto(Utilities.truncateStringValue(request.getComunicacion().getAsunto(), 500));
        }
        if (request.getComunicacion().getCodigoSerie() != null) {
            comunicacion.setCodSerie(request.getComunicacion().getCodigoSerie());
        }
        if (request.getComunicacion().getCodigoSubSerie() != null) {
            comunicacion.setCodSubserie(request.getComunicacion().getCodigoSubSerie());
        }
        if (request.getComunicacion().getComentarios() != null) {
            comunicacion.setComentarios(Utilities.truncateStringValue(request.getComunicacion().getComentarios(), 200));
        }
        if (request.getComunicacion().getContentFLdComunicaciones() != null) {
            comunicacion.setContentFldComunicaciones(request.getComunicacion().getContentFLdComunicaciones());
        }
        if (request.getComunicacion().getContentFldOtro() != null) {
            comunicacion.setContentFldOtro(request.getComunicacion().getContentFldOtro());
        }
        if (request.getComunicacion().getContentFldTitulo() != null) {
            comunicacion.setContentFldTitulo(request.getComunicacion().getContentFldTitulo());
        }
        if (request.getComunicacion().getDiasParaRespuesta() != null) {
            comunicacion.setDiasParaRespuesta(request.getComunicacion().getDiasParaRespuesta());
        }
        if (request.getComunicacion().getEsPQRS() != null) {
            comunicacion.setEsPqrs(request.getComunicacion().getEsPQRS());
        }
        if (request.getComunicacion().getEsTitulo() != null) {
            comunicacion.setEsTitulo(request.getComunicacion().getEsTitulo());
        }
        if (request.getComunicacion().getEstadoComunicacion() != null) {
            comunicacion.setEstadoComunicacion(request.getComunicacion().getEstadoComunicacion());
        }
        if (request.getComunicacion().getFechaActualizacion() != null) {
            comunicacion.setFechaActualizacion(request.getComunicacion()
                                                      .getFechaActualizacion()
                                                      .getTime());
        }
        if (request.getComunicacion().getFechaCreacion() != null) {
            comunicacion.setFechaCreacion(request.getComunicacion()
                                                 .getFechaCreacion()
                                                 .getTime());
        }
        if (request.getComunicacion().getFechaVencimiento() != null) {
            comunicacion.setFechaVencimiento(request.getComunicacion()
                                                    .getFechaVencimiento()
                                                    .getTime());
        }
        if (request.getComunicacion().getGrupoSeguridad() != null) {
            comunicacion.setGrupoSeguridad(request.getComunicacion().getGrupoSeguridad());
        }
        if (request.getComunicacion().getIdDependenciaDestino() != null) {
            comunicacion.setIdDependenciaDestino(request.getComunicacion().getIdDependenciaDestino());
        }
        if (request.getComunicacion().getIdReservada() != null) {
            comunicacion.setIdentifReservada(request.getComunicacion().getIdReservada());
        }
        if (request.getComunicacion().getIdTipoDocumentalTramite() != null) {
            comunicacion.setIdTipoDocumentalTramite(request.getComunicacion().getIdTipoDocumentalTramite());
        }
        if (request.getComunicacion().getIdTramite() != null) {
            comunicacion.setIdTramite(request.getComunicacion().getIdTramite());
        }
        if (request.getComunicacion().getIdUnidadProductora() != null) {
            comunicacion.setIdUnidadProductora(request.getComunicacion().getIdUnidadProductora());
        }
        if (request.getComunicacion().getNombreSerie() != null) {
            comunicacion.setNombreSerie(request.getComunicacion().getNombreSerie());
        }
        if (request.getComunicacion().getNombreSubSerie() != null) {
            comunicacion.setNombreSubserie(request.getComunicacion().getNombreSubSerie());
        }
        if (request.getComunicacion().getNombreTpDoc() != null) {
            comunicacion.setNombreTpDoc(request.getComunicacion().getNombreTpDoc());
        }
        if (request.getComunicacion().getNroAnexos() != null) {
            comunicacion.setNroAnexos(request.getComunicacion().getNroAnexos());
        }
        if (request.getComunicacion().getNroFolios() != null) {
            comunicacion.setNroFolios(Utilities.truncateIntegerValue(request.getComunicacion().getNroFolios(), 4));
        }
        if (request.getComunicacion().getNroPlaca() != null) {
            comunicacion.setNroPlaca(Utilities.truncateStringValue(request.getComunicacion().getNroPlaca(), 100));
        }
        if (request.getComunicacion().getNroRadicado() != null) {
            comunicacion.setNroRadicado(request.getComunicacion().getNroRadicado());
        }
        if (request.getComunicacion().getNroRadicadoRelacionado() != null) {
            comunicacion.setNroRadicadoRelacionado(Utilities.truncateStringValue(request.getComunicacion()
                                                                                 .getNroRadicadoRelacionado(), 100));
        }
        if (request.getComunicacion().getReferencia() != null) {
            comunicacion.setReferencia(Utilities.truncateStringValue(request.getComunicacion().getReferencia(), 200));
        }
        if (request.getComunicacion().getRequiereRespuesta() != null && !request.getComunicacion()
                                                                                .getRequiereRespuesta()
                                                                                .equals("")) {
            comunicacion.setRequiereRespuesta(request.getComunicacion().getRequiereRespuesta());
        } else {
            logger.debug("requiere respuesta es nulo o vacio: " + request.getComunicacion().getRequiereRespuesta());
        }
        if (request.getComunicacion().getTipoComunicacion() != null) {
            comunicacion.setTipoComunicacion(request.getComunicacion().getTipoComunicacion());
        }
        if (request.getComunicacion().getTramite() != null) {
            comunicacion.setTramite(request.getComunicacion().getTramite());
        }
        if (request.getComunicacion().getTrasladoOtraEntidad() != null) {
            comunicacion.setTrasladoOtraEntidad(request.getComunicacion().getTrasladoOtraEntidad());
        }
        if (request.getComunicacion().getUsuarioActualizacion() != null) {
            comunicacion.setUsuarioActualizacion(request.getComunicacion().getUsuarioActualizacion());
        }
        if (request.getComunicacion().getUsuarioCreacion() != null) {
            comunicacion.setUsuarioCreacion(request.getComunicacion().getUsuarioCreacion());
        }
        if (request.getComunicacion().getIdComunicacion() != null) {
            comunicacion.setInstanceNumber(request.getComunicacion()
                                                  .getIdComunicacion()
                                                  .longValue());
        }

        if (request.getComunicacion().getAppName() != null && request.getComunicacion()
                                                                     .getAppName()
                                                                     .trim() != "") {
            comunicacion.setAppName(request.getComunicacion().getAppName());
        }
               
        if (request.getAnexosList() != null) {
            logger.debug("elimina anexos con id comunicacion " + comunicacion.getIdComunicacion());
            int regsDeleted = ejb.deleteSgdAnexoByIdComunicacion(comunicacion.getIdComunicacion());
            logger.debug("anexos eliminados: " + regsDeleted);

            sgdAnexos = new ArrayList<SgdAnexoComunicacion>();
            
            for (AnexoComunicacionRequest anexoRequest : request.getAnexosList()) {
                SgdAnexoComunicacion anexo = new SgdAnexoComunicacion();

                if (anexoRequest.getCantidad() != null) {
                    anexo.setCantidad(anexoRequest.getCantidad());
                }
                if (anexoRequest.getDescripcion() != null) {
                    anexo.setDescripcion(Utilities.truncateStringValue(anexoRequest.getDescripcion(), 200));
                } else {
                    anexo.setDescripcion(" ");
                }
                if (anexoRequest.getEnlaceContent() != null) {
                    anexo.setEnlaceContent(anexoRequest.getEnlaceContent());
                }
                if (anexoRequest.getNroRadicado() != null) {
                    anexo.setNumeroRadicado(anexoRequest.getNroRadicado());
                }
                if (anexoRequest.getRutaLocalCargue() != null) {
                    anexo.setRutaLocalCargue(anexoRequest.getRutaLocalCargue());
                }

                anexo.setSgdComunicacion1(comunicacion);

                sgdAnexos.add(anexo);
            }
            comunicacion.setSgdAnexoComunicacionList(sgdAnexos);
        } else {
            logger.debug("JVenegas-> Sin Anexos.");
        }

        if (request.getInteresadosList() != null && request.getInteresadosList().size() > 0) {
            logger.debug("consulta interesados por id comunicacion " + comunicacion.getIdComunicacion());
            interesadosConsulta = comunicacion.getSgdInteresadoList();
            interesadosAct = new ArrayList<SgdInteresado>();

            logger.debug("interesados consultados: " + interesadosConsulta.size());

            if (interesadosConsulta != null && interesadosConsulta.size() > 0) {
                for (SgdInteresado interesado : interesadosConsulta) {
                    logger.debug("interesados recibidos para consultar: " + request.getInteresadosList().size());
                    for (InteresadoRequest interesadoRequest : request.getInteresadosList()) {
                        if (((interesado.getTipoIdentificacion() != null &&
                              interesado.getTipoIdentificacion().equals(interesadoRequest.getTipoIdentificacion())) &&
                             (interesado.getNumeroIdentificacion() != null &&
                              interesado.getNumeroIdentificacion().equals(interesadoRequest.getNroIdentificacion()))) ||
                            (interesado.getEmail() != null &&
                             interesado.getEmail().equals(interesadoRequest.getEmail()))) {

                            logger.debug("encuentra interesado");

                            if (interesadoRequest.getCiudad() != null) {
                                interesado.setCiudad(interesadoRequest.getCiudad());
                            }
                            if (interesadoRequest.getDireccion() != null) {
                                interesado.setDireccion(Utilities.truncateStringValue(interesadoRequest.getDireccion(),
                                                                                      200));
                            }
                            if (interesadoRequest.getCelular() != null) {
                                interesado.setCelular(Utilities.truncateStringValue(interesadoRequest.getCelular(),
                                                                                    20));
                            }
                            if (interesadoRequest.getIdDepartamento() != null) {
                                interesado.setIdDepartamento(new BigDecimal(interesadoRequest.getIdDepartamento()));
                            }
                            if (interesadoRequest.getIdEtnia() != null) {
                                interesado.setIdEtnia(new BigDecimal(interesadoRequest.getIdEtnia()));
                            }
                            if (interesadoRequest.getIdMunicipio() != null) {
                                interesado.setIdMunicipio(new BigDecimal(interesadoRequest.getIdMunicipio()));
                            }
                            if (interesadoRequest.getIdPais() != null) {
                                interesado.setIdPais(new BigDecimal(interesadoRequest.getIdPais()));
                            }
                            if (interesadoRequest.getIdPoblacionVulnerable() != null) {
                                interesado.setIdPoblacionVulnerable(new BigDecimal(interesadoRequest.getIdPoblacionVulnerable()));
                            }
                            if (interesadoRequest.getIdRangoEdad() != null) {
                                interesado.setIdRangoEdad(new BigDecimal(interesadoRequest.getIdRangoEdad()));
                            }
                            if (interesadoRequest.getOcupacion() != null) {
                                interesado.setOcupacion(Utilities.truncateStringValue(interesadoRequest.getOcupacion(),
                                                                                      100));
                            }
                            if (interesadoRequest.getPrimerApellido() != null) {
                                interesado.setPrimerApellido(Utilities.truncateStringValue(interesadoRequest.getPrimerApellido(),
                                                                                           100));
                            }
                            if (interesadoRequest.getPrimerNombre() != null) {
                                interesado.setPrimerNombre(Utilities.truncateStringValue(interesadoRequest.getPrimerNombre(),
                                                                                         100));
                            }
                            if (interesadoRequest.getSegundoApellido() != null) {
                                interesado.setSegundoApellido(Utilities.truncateStringValue(interesadoRequest.getSegundoApellido(),
                                                                                            100));
                            }
                            if (interesadoRequest.getSegundoNombre() != null) {
                                interesado.setSegundoNombre(Utilities.truncateStringValue(interesadoRequest.getSegundoNombre(),
                                                                                          100));
                            }
                            if (interesadoRequest.getTelefono() != null) {
                                interesado.setTelefono(Utilities.truncateStringValue(interesadoRequest.getTelefono(),
                                                                                     20));
                            }
                            interesadosAct.add(interesado);
                        } else {
                            SgdInteresado interesadoInsert = new SgdInteresado();
                            interesadoInsert.setCelular(Utilities.truncateStringValue(interesadoRequest.getCelular(),
                                                                                      20));
                            interesadoInsert.setCiudad(interesadoRequest.getCiudad());
                            interesadoInsert.setDireccion(Utilities.truncateStringValue(interesadoRequest.getDireccion(),
                                                                                        200));
                            interesadoInsert.setEmail(Utilities.truncateStringValue(interesadoRequest.getEmail(), 200));
                            interesadoInsert.setIdDepartamento(interesadoRequest.getIdDepartamento() != null ?
                                                               new BigDecimal(interesadoRequest.getIdDepartamento()) :
                                                               null);
                            interesadoInsert.setIdEtnia(interesadoRequest.getIdEtnia() != null ?
                                                        new BigDecimal(interesadoRequest.getIdEtnia()) : null);
                            interesadoInsert.setIdMunicipio(interesadoRequest.getIdMunicipio() != null ?
                                                            new BigDecimal(interesadoRequest.getIdMunicipio()) : null);
                            interesadoInsert.setIdPais(interesadoRequest.getIdPais() != null ?
                                                       new BigDecimal(interesadoRequest.getIdPais().intValue()) : null);
                            interesadoInsert.setIdPoblacionVulnerable(interesadoRequest.getIdPoblacionVulnerable() !=
                                                                      null ?
                                                                      new BigDecimal(interesadoRequest.getIdPoblacionVulnerable()
                                                                                     .intValue()) : null);
                            interesadoInsert.setIdRangoEdad(interesadoRequest.getIdRangoEdad() != null ?
                                                            new BigDecimal(interesadoRequest.getIdRangoEdad()
                                                                           .intValue()) : null);
                            interesadoInsert.setNumeroIdentificacion(Utilities.truncateStringValue(interesadoRequest.getNroIdentificacion(),
                                                                                                   20));
                            interesadoInsert.setOcupacion(Utilities.truncateStringValue(interesadoRequest.getOcupacion(),
                                                                                        100));
                            interesadoInsert.setPrimerApellido(Utilities.truncateStringValue(interesadoRequest.getPrimerApellido(),
                                                                                             100));
                            interesadoInsert.setPrimerNombre(Utilities.truncateStringValue(interesadoRequest.getPrimerNombre(),
                                                                                           100));
                            interesadoInsert.setSegundoApellido(Utilities.truncateStringValue(interesadoRequest.getSegundoApellido(),
                                                                                              100));
                            interesadoInsert.setSegundoNombre(Utilities.truncateStringValue(interesadoRequest.getSegundoApellido(),
                                                                                            100));
                            interesadoInsert.setTelefono(Utilities.truncateStringValue(interesadoRequest.getTelefono(),
                                                                                       20));
                            interesadoInsert.setTipoIdentificacion(interesadoRequest.getTipoIdentificacion());
                            interesadoInsert.setSgdComunicacion(comunicacion);

                            interesadosAct.add(interesadoInsert);
                        }
                    }
                }
            } else {

                for (InteresadoRequest interesadoRequest : request.getInteresadosList()) {
                    logger.debug("JVenegas->Recorrido For: " + request.getInteresadosList().size());                   
                    SgdInteresado interesadoInsert = new SgdInteresado();
                    interesadoInsert.setCelular(Utilities.truncateStringValue(interesadoRequest.getCelular(), 20));
                    interesadoInsert.setCiudad(interesadoRequest.getCiudad());
                    interesadoInsert.setDireccion(Utilities.truncateStringValue(interesadoRequest.getDireccion(), 200));
                    interesadoInsert.setEmail(Utilities.truncateStringValue(interesadoRequest.getEmail(), 200));
                    interesadoInsert.setIdDepartamento(interesadoRequest.getIdDepartamento() != null ?
                                                       new BigDecimal(interesadoRequest.getIdDepartamento()) : null);
                    interesadoInsert.setIdEtnia(interesadoRequest.getIdEtnia() != null ?
                                                new BigDecimal(interesadoRequest.getIdEtnia()) : null);
                    interesadoInsert.setIdMunicipio(interesadoRequest.getIdMunicipio() != null ?
                                                    new BigDecimal(interesadoRequest.getIdMunicipio()) : null);
                    interesadoInsert.setIdPais(interesadoRequest.getIdPais() != null ?
                                               new BigDecimal(interesadoRequest.getIdPais().intValue()) : null);
                    interesadoInsert.setIdPoblacionVulnerable(interesadoRequest.getIdPoblacionVulnerable() != null ?
                                                              new BigDecimal(interesadoRequest.getIdPoblacionVulnerable()
                                                                             .intValue()) : null);
                    interesadoInsert.setIdRangoEdad(interesadoRequest.getIdRangoEdad() != null ?
                                                    new BigDecimal(interesadoRequest.getIdRangoEdad().intValue()) :
                                                    null);
                    interesadoInsert.setNumeroIdentificacion(Utilities.truncateStringValue(interesadoRequest.getNroIdentificacion(),
                                                                                           20));
                    interesadoInsert.setOcupacion(Utilities.truncateStringValue(interesadoRequest.getOcupacion(), 100));
                    interesadoInsert.setPrimerApellido(Utilities.truncateStringValue(interesadoRequest.getPrimerApellido(),
                                                                                     100));
                    interesadoInsert.setPrimerNombre(Utilities.truncateStringValue(interesadoRequest.getPrimerNombre(),
                                                                                   100));
                    interesadoInsert.setSegundoApellido(Utilities.truncateStringValue(interesadoRequest.getSegundoApellido(),
                                                                                      100));
                    interesadoInsert.setSegundoNombre(Utilities.truncateStringValue(interesadoRequest.getSegundoNombre(),
                                                                                    100));
                    interesadoInsert.setTelefono(Utilities.truncateStringValue(interesadoRequest.getTelefono(), 20));
                    interesadoInsert.setTipoIdentificacion(interesadoRequest.getTipoIdentificacion());
                    interesadoInsert.setSgdComunicacion(comunicacion);

                    interesadosAct.add(interesadoInsert);
                }
            }

            logger.debug("interesados a actualizar: " + interesadosAct.size());
            comunicacion.setSgdInteresadoList(interesadosAct);
        } else {
            logger.debug("JVenegas->no hay interesados a actualizar ni registrar");
        }

        ejb.updateComunicacion(comunicacion);

        WCCResponse response = new WCCResponse();
        response.setStatusCode(OK);
        response.setStatusMessage("Success");

        return response;
    }

    public WCCResponse actualizarComunicacion(ComunicacionRequest request, ComunicacionesLocal3 ejb) throws Exception {
        logger.debug("busca comunicacion con radicado: " + request.getNroRadicado());
        List<SgdComunicacion> comunicacionConsultaList = ejb.getSgdComunicacionByNroRadicado(request.getNroRadicado());
        SgdComunicacion comunicacion = null;

        if (comunicacionConsultaList == null || comunicacionConsultaList.size() == 0) {
            throw new Exception("No existe comunicacion para actualizar: " + request.getNroRadicado());
        } else {
            logger.debug("comunicaciones consultadas: " + comunicacionConsultaList.size());
            comunicacion = comunicacionConsultaList.get(0);
        }

        if (request.getAccesoNegado() != null) {
            comunicacion.setAccesoNegado(request.getAccesoNegado());
        }
        if (request.getAsunto() != null) {
            comunicacion.setAsunto(Utilities.truncateStringValue(request.getAsunto(), 500));
        }
        if (request.getCodigoSerie() != null) {
            comunicacion.setCodSerie(request.getCodigoSerie());
        }
        if (request.getCodigoSubSerie() != null) {
            comunicacion.setCodSubserie(request.getCodigoSubSerie());
        }
        if (request.getComentarios() != null) {
            comunicacion.setComentarios(Utilities.truncateStringValue(request.getComentarios(), 200));
        }
        if (request.getContentFLdComunicaciones() != null) {
            comunicacion.setContentFldComunicaciones(request.getContentFLdComunicaciones());
        }
        if (request.getContentFldOtro() != null) {
            comunicacion.setContentFldOtro(request.getContentFldOtro());
        }
        if (request.getContentFldTitulo() != null) {
            comunicacion.setContentFldTitulo(request.getContentFldTitulo());
        }
        if (request.getDiasParaRespuesta() != null) {
            comunicacion.setDiasParaRespuesta(request.getDiasParaRespuesta());
        }
        if (request.getEsPQRS() != null) {
            comunicacion.setEsPqrs(request.getEsPQRS());
        }
        if (request.getEsTitulo() != null) {
            comunicacion.setEsTitulo(request.getEsTitulo());
        }
        if (request.getEstadoComunicacion() != null) {
            comunicacion.setEstadoComunicacion(request.getEstadoComunicacion());
        }
        logger.debug("request Fecha Actualizacion: " + request.getFechaActualizacion());
        if (request.getFechaActualizacion() != null) {
            comunicacion.setFechaActualizacion(request.getFechaActualizacion().getTime());
        } else {
            comunicacion.setFechaActualizacion(new java.util.Date());
        }
        logger.debug("comunicacion fecha Actualizacion: " + comunicacion.getFechaActualizacion());
        if (request.getFechaCreacion() != null) {
            comunicacion.setFechaCreacion(request.getFechaCreacion().getTime());
        }
        if (request.getFechaVencimiento() != null) {
            comunicacion.setFechaVencimiento(request.getFechaVencimiento().getTime());
        }
        if (request.getGrupoSeguridad() != null) {
            comunicacion.setGrupoSeguridad(request.getGrupoSeguridad());
        }
        if (request.getIdDependenciaDestino() != null) {
            comunicacion.setIdDependenciaDestino(request.getIdDependenciaDestino());
        }
        if (request.getIdReservada() != null) {
            comunicacion.setIdentifReservada(request.getIdReservada());
        }
        if (request.getIdTipoDocumentalTramite() != null) {
            comunicacion.setIdTipoDocumentalTramite(request.getIdTipoDocumentalTramite());
        }
        if (request.getIdTramite() != null) {
            comunicacion.setIdTramite(request.getIdTramite());
        }
        if (request.getIdUnidadProductora() != null) {
            comunicacion.setIdUnidadProductora(request.getIdUnidadProductora());
        }
        if (request.getNombreSerie() != null) {
            comunicacion.setNombreSerie(request.getNombreSerie());
        }
        if (request.getNombreSubSerie() != null) {
            comunicacion.setNombreSubserie(request.getNombreSubSerie());
        }
        if (request.getNombreTpDoc() != null) {
            comunicacion.setNombreTpDoc(request.getNombreTpDoc());
        }
        if (request.getNroAnexos() != null) {
            comunicacion.setNroAnexos(request.getNroAnexos());
        }
        if (request.getNroFolios() != null) {
            comunicacion.setNroFolios(Utilities.truncateIntegerValue(request.getNroFolios(), 4));
        }
        if (request.getNroPlaca() != null) {
            comunicacion.setNroPlaca(Utilities.truncateStringValue(request.getNroPlaca(), 100));
        }
        if (request.getNroRadicado() != null) {
            comunicacion.setNroRadicado(request.getNroRadicado());
        }
        if (request.getNroRadicadoRelacionado() != null) {
            comunicacion.setNroRadicadoRelacionado(Utilities.truncateStringValue(request.getNroRadicadoRelacionado(),
                                                                                 100));
        }
        if (request.getReferencia() != null) {
            comunicacion.setReferencia(Utilities.truncateStringValue(request.getReferencia(), 200));
        }
        if (request.getRequiereRespuesta() != null && !request.getRequiereRespuesta().equals("")) {
            comunicacion.setRequiereRespuesta(request.getRequiereRespuesta());
        } else {
            logger.info("requiere respuesta es nulo o vacio: " + request.getRequiereRespuesta());
        }
        if (request.getTipoComunicacion() != null) {
            comunicacion.setTipoComunicacion(request.getTipoComunicacion());
        }
        if (request.getTramite() != null) {
            comunicacion.setTramite(request.getTramite());
        }
        if (request.getTrasladoOtraEntidad() != null) {
            comunicacion.setTrasladoOtraEntidad(request.getTrasladoOtraEntidad());
        }
        if (request.getUsuarioActualizacion() != null) {
            comunicacion.setUsuarioActualizacion(request.getUsuarioActualizacion());
        }
        if (request.getUsuarioCreacion() != null) {
            comunicacion.setUsuarioCreacion(request.getUsuarioCreacion());
        }
        if (request.getIdComunicacion() != null) {
            comunicacion.setInstanceNumber(request.getIdComunicacion().longValue());
        }

        if (request.getIdTipoSolicitud() != null && request.getIdTipoSolicitud() > 0) {
            TipoSolicitud tipo = ejb.getTipoSolicitudById(request.getIdTipoSolicitud());
            comunicacion.setTipoSolicitud(tipo);
        }

        ejb.updateComunicacion(comunicacion);

        WCCResponse response = new WCCResponse();
        response.setStatusCode(OK);
        response.setStatusMessage("Success");

        return response;
    }

    public ProcesoComunicacionResponse consultarComunicacion(String nroRadicado,
                                                             ComunicacionesLocal3 ejb) throws Exception {
        ProcesoComunicacionResponse response = new ProcesoComunicacionResponse();

        logger.debug("consulta comunicacion con nro radicado " + nroRadicado);
        List<SgdComunicacion> comunicacionConsultaList = ejb.getSgdComunicacionByNroRadicado(nroRadicado);
        SgdComunicacion comunicacion = null;

        if (comunicacionConsultaList == null || comunicacionConsultaList.size() == 0) {
            throw new Exception("No existe comunicacion para actualizar: " + nroRadicado);
        } else {
            logger.debug("comunicaciones consultadas: " + comunicacionConsultaList.size());
            comunicacion = comunicacionConsultaList.get(0);
        }

        if (comunicacion != null) {
            logger.debug("encuentra comunicacion");

            ComunicacionRequest comunicacionResponse = new ComunicacionRequest();
            comunicacionResponse.setAccesoNegado(comunicacion.getAccesoNegado());
            comunicacionResponse.setAsunto(comunicacion.getAsunto());
            comunicacionResponse.setCodigoSerie(comunicacion.getCodSerie());
            comunicacionResponse.setCodigoSubSerie(comunicacion.getCodSubserie());
            comunicacionResponse.setComentarios(comunicacion.getComentarios());
            comunicacionResponse.setContentFLdComunicaciones(comunicacion.getContentFldComunicaciones());
            comunicacionResponse.setContentFldOtro(comunicacion.getContentFldOtro());
            comunicacionResponse.setContentFldTitulo(comunicacion.getContentFldTitulo());
            comunicacionResponse.setDiasParaRespuesta(comunicacion.getDiasParaRespuesta());
            comunicacionResponse.setEsPQRS(comunicacion.getEsPqrs());
            comunicacionResponse.setEsTitulo(comunicacion.getEsTitulo());
            comunicacionResponse.setEstadoComunicacion(comunicacion.getEstadoComunicacion());
            comunicacionResponse.setFechaCreacion(comunicacion.getFechaCreacion() != null ?
                                                  Utilities.date2Calendar(comunicacion.getFechaCreacion()) : null);
            comunicacionResponse.setFechaModificacion(comunicacion.getFechaActualizacion() != null ?
                                                      Utilities.date2Calendar(comunicacion.getFechaActualizacion()) :
                                                      null);
            comunicacionResponse.setFechaVencimiento(comunicacion.getFechaVencimiento() != null ?
                                                     Utilities.date2Calendar(comunicacion.getFechaVencimiento()) :
                                                     null);
            comunicacionResponse.setGrupoSeguridad(comunicacion.getGrupoSeguridad());
            comunicacionResponse.setIdComunicacion(comunicacion.getIdComunicacion().intValue());
            comunicacionResponse.setIdDependenciaDestino(comunicacion.getIdDependenciaDestino().intValue());
            comunicacionResponse.setIdReservada(comunicacion.getIdentifReservada());
            comunicacionResponse.setIdTipoDocumentalTramite(comunicacion.getIdTipoDocumentalTramite());
            comunicacionResponse.setIdTramite(comunicacion.getIdTramite());
            comunicacionResponse.setIdUnidadProductora(comunicacion.getIdUnidadProductora());
            comunicacionResponse.setNombreSerie(comunicacion.getNombreSerie());
            comunicacionResponse.setNombreSubSerie(comunicacion.getNombreSubserie());
            comunicacionResponse.setNombreTpDoc(comunicacion.getNombreTpDoc());
            comunicacionResponse.setNroAnexos(comunicacion.getNroAnexos());
            comunicacionResponse.setNroFolios(comunicacion.getNroFolios());
            comunicacionResponse.setNroPlaca(comunicacion.getNroPlaca());
            comunicacionResponse.setNroRadicado(comunicacion.getNroRadicado());
            comunicacionResponse.setNroRadicadoRelacionado(comunicacion.getNroRadicadoRelacionado());
            comunicacionResponse.setReferencia(comunicacion.getReferencia());
            comunicacionResponse.setRequiereRespuesta(comunicacion.getRequiereRespuesta());
            comunicacionResponse.setTipoComunicacion(comunicacion.getTipoComunicacion());
            comunicacionResponse.setTramite(comunicacion.getTramite());
            comunicacionResponse.setTrasladoOtraEntidad(comunicacion.getTrasladoOtraEntidad());
            comunicacionResponse.setUsuarioActualizacion(comunicacion.getUsuarioActualizacion());
            comunicacionResponse.setUsuarioCreacion(comunicacion.getUsuarioCreacion());

            response.setComunicacion(comunicacionResponse);

            if (comunicacion.getSgdAnexoComunicacionList() != null &&
                comunicacion.getSgdAnexoComunicacionList().size() > 0) {
                logger.debug("anexos encontrados: " + comunicacion.getSgdAnexoComunicacionList().size());
                List<AnexoComunicacionRequest> anexosList = new ArrayList<AnexoComunicacionRequest>();

                for (SgdAnexoComunicacion anexoBD : comunicacion.getSgdAnexoComunicacionList()) {
                    AnexoComunicacionRequest anexo = new AnexoComunicacionRequest();
                    anexo.setCantidad(anexoBD.getCantidad());
                    anexo.setDescripcion(anexoBD.getDescripcion());
                    anexo.setEnlaceContent(anexoBD.getEnlaceContent());
                    anexo.setIdAnexo(anexoBD.getIdAnexoComunicacion().intValue());
                    anexo.setIdComunicacion(comunicacion.getIdComunicacion().intValue());
                    anexo.setNroRadicado(anexoBD.getNumeroRadicado());
                    anexo.setRutaLocalCargue(anexoBD.getRutaLocalCargue());

                    anexosList.add(anexo);
                }
                response.setAnexosList(anexosList);
            }
            if (comunicacion.getSgdInteresadoList() != null && comunicacion.getSgdInteresadoList().size() > 0) {
                logger.debug("interesados consultados: " + comunicacion.getSgdInteresadoList().size());
                List<InteresadoRequest> interesadosList = new ArrayList<InteresadoRequest>();

                for (SgdInteresado interesadoBD : comunicacion.getSgdInteresadoList()) {
                    InteresadoRequest interesado = new InteresadoRequest();
                    interesado.setCelular(interesadoBD.getCelular());
                    interesado.setCiudad(interesadoBD.getCiudad());
                    interesado.setDireccion(interesadoBD.getDireccion());
                    interesado.setEmail(interesadoBD.getEmail());
                    interesado.setIdComunicacion(comunicacion.getIdComunicacion().intValue());
                    interesado.setIdDepartamento(interesadoBD.getIdDepartamento() != null ?
                                                 interesadoBD.getIdDepartamento().intValue() : null);
                    interesado.setIdEtnia(interesadoBD.getIdEtnia() != null ? interesadoBD.getIdEtnia().intValue() :
                                          null);
                    interesado.setIdInteresado(interesadoBD.getIdInteresado().intValue());
                    interesado.setIdMunicipio(interesadoBD.getIdMunicipio() != null ?
                                              interesadoBD.getIdMunicipio().intValue() : null);
                    interesado.setIdPais(interesadoBD.getIdPais() != null ? interesadoBD.getIdPais().intValue() : null);
                    interesado.setIdPoblacionVulnerable(interesadoBD.getIdPoblacionVulnerable() != null ?
                                                        interesadoBD.getIdPoblacionVulnerable().intValue() : null);
                    interesado.setIdRangoEdad(interesadoBD.getIdRangoEdad() != null ?
                                              interesadoBD.getIdRangoEdad().intValue() : null);
                    interesado.setNroIdentificacion(interesadoBD.getNumeroIdentificacion());
                    interesado.setOcupacion(interesadoBD.getOcupacion());
                    interesado.setPrimerApellido(interesadoBD.getPrimerApellido());
                    interesado.setPrimerNombre(interesadoBD.getPrimerNombre());
                    interesado.setSegundoApellido(interesadoBD.getSegundoApellido());
                    interesado.setSegundoNombre(interesadoBD.getSegundoNombre());
                    interesado.setSegundoNombre(interesadoBD.getSegundoNombre());
                    interesado.setTelefono(interesadoBD.getTelefono());
                    interesado.setTipoIdentificacion(interesadoBD.getTipoIdentificacion());

                    interesadosList.add(interesado);
                }
                response.setInteresadosList(interesadosList);
            }
            response.setCodigoRespuesta("0");
        } else {
            response.setCodigoRespuesta("ERWCC00");
            response.setMensajeRespuesta("No se encuentra la comunicacion");
        }
        return response;
    }


    public FechaVencimientoResponse calcularFechaVencimiento(java.util.Date fechaRadicacion, Integer diasRespuesta,
                                                             ComunicacionesLocal3 comunicacionesEjb) throws Exception {
        logger.debug("Fecha de radicacion: " + Utilities.date2String(fechaRadicacion, "YYYYMMDD"));
        logger.debug("Dias de respuesta: " + diasRespuesta);

        FechaVencimientoResponse response = new FechaVencimientoResponse();
        response.setCodigo("0");
        response.setFechaVencimiento(comunicacionesEjb.getFechaVencimiento(diasRespuesta, fechaRadicacion));

        logger.debug("Fecha de vencimiento: " + Utilities.date2String(response.getFechaVencimiento(), "YYYYMMDD"));

        return response;
    }


    public AnexosRadicadoResponse consultarAnexosRadicado(String nroRadicado) throws WSException, Exception {
        
        logger.debug("Inicio consultarAnexosRadicado"+nroRadicado);
        String searchCriteria;
        DocumentMngr documentMngr;
        SearchResponse searchResponse;
        AnexosRadicadoResponse response = new AnexosRadicadoResponse();
        List<AnexoRadicado> anexoList = new ArrayList<AnexoRadicado>();
        String url;

        GenericSoapPortType port = ServiceLocator.getInstance().getGenericUCMPort(credenciales);
        documentMngr = new DocumentMngr(port);

        // Primer intento: radicado + dDocType = Comunicacion
        searchCriteria = "xANM_Referencia<matches>`" + nroRadicado + "`<and>dDocType<matches>`Comunicacion`";
        searchResponse = documentMngr.getSearchResults(searchCriteria);

        // Si no encuentra, segundo intento: solo por radicado
        if (!(OK.equals(searchResponse.getStatusCode()) &&
              searchResponse.getResultSetList() != null &&
              !searchResponse.getResultSetList().isEmpty())) {

            logger.debug("No se encontraron anexos por radicado + dDocType=Comunicacion. Se intenta solo por radicado.");

            searchCriteria = "xANM_Referencia<matches>`" + nroRadicado + "`";
            searchResponse = documentMngr.getSearchResults(searchCriteria);
        }

        if (OK.equals(searchResponse.getStatusCode()) &&
            searchResponse.getResultSetList() != null &&
            !searchResponse.getResultSetList().isEmpty()) {

            logger.debug("anexos consultados: " + searchResponse.getResultSetList().size());

            for (Map<String, String> resultSet : searchResponse.getResultSetList()) {
                AnexoRadicado anexo = new AnexoRadicado();

                url = PropertiesLoader.getProperty(URL_IDC)
                        + "?IdcService=GET_FILE"
                        + "&" + dID + "=" + resultSet.get(dID)
                        + "&" + docName + "=" + resultSet.get(docName);

                anexo.setNombre(resultSet.get(docTitle));
                anexo.setUrl(url);
                anexoList.add(anexo);

                logger.debug("Anexo: " + anexo.getNombre());
            }

            response.setCodigoEstado("0");
            response.setMensajeRsp("Success");
        } else {
            response.setCodigoEstado(ERWCC21);
            response.setMensajeRsp(PropertiesLoader.getErrorProperty(ERWCC21));
        }

        response.setAnexosList(anexoList);
        logger.debug("Fin consultarAnexosRadicado"+nroRadicado);
        return response;
    }

    public String generarNroRadicado(String tipoComunicacion, String codDependencia,
                                     ComunicacionesLocal3 comunicacionesEjb) {
        return comunicacionesEjb.getNumeroRadicado(codDependencia, tipoComunicacion);
    }


    public RespuestaGeneradorDTO generarBarCode(GeneracionODTRequest request,
                                                ComunicacionesLocal3 comunicacionesEjb) throws Exception {
        logger.debug("INICIO generarBarCode");
        String rutaArchivo = request.getRutaArchivo();
        String rutaCodigoBarras = null;
        String path = null;
        String mimeType = null;
        Object documentObject = null;
        TemplateMerger merger = new TemplateMerger();
        ComunicacionODT comunicacion = request.getComunicacion();
        RespuestaGeneradorDTO respuesta = new RespuestaGeneradorDTO();

        try {
            logger.debug("carga documento: " + rutaArchivo);
            path = merger.obtenerRutaArchivos(rutaArchivo);
            mimeType = FilenameUtils.getExtension(rutaArchivo);
            File file = new File(rutaArchivo);
            mimeType = new MimetypesFileTypeMap().getContentType(file);
            logger.debug("mimeType de la plantilla: " + mimeType);
            if (mimeType.equals("application/vnd.oasis.opendocument.text")) {
                documentObject = TextDocument.loadDocument(rutaArchivo);
                //} else if (mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                //documentObject = new XWPFDocument(new java.io.FileInputStream(rutaArchivo));
            } else {
                throw new Exception("formato de plantilla invalida: " + mimeType);
            }
            logger.debug("NroRadicado: " + comunicacion.getNroRadicado());
            /**
             * ###################################################################
             * Se obtienen los datos de la dependencia y jefe que estampa su grafo
             *
             */
            DependenciaRol depRol = new DependenciaRol();
            depRol.setCodigoDependencia(consultarUsuario(request.getComunicacion().getElaboro(), comunicacionesEjb)
                                        .getCodigoDependencia());
            depRol.setIdRol(2L);
            UsuarioResponse usuario = buscarUsuariosPorDependenciaRol(depRol, comunicacionesEjb);
            /**
             * ###################################################################
             */
            if (comunicacion.getNroRadicado() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getNroRadicado(),
                                         PropertiesLoader.getProperty(CamposODTProperties.NRO_RADICADO));
                logger.debug("path: " + path);
                rutaCodigoBarras = merger.generarCodigoBarras(comunicacion.getNroRadicado(), path);

                logger.debug("rutaCodigoBarras: " + rutaCodigoBarras);
                merger.buscarYReemplazarImagen(documentObject, rutaCodigoBarras,
                                               PropertiesLoader.getProperty(CamposODTProperties.NRO_RADICADO_IMG),
                                               mimeType);
                /**
                 * ###################################################
                 * Se obtiene la ruta del grafo y se estampa si existe
                 */
                Usuario jefe = null;
                if (usuario != null && !usuario.getUsuarios().isEmpty()) {
                    jefe = usuario.getUsuarios().get(0);
                } else {
                    throw new Exception("No se ha encontrado el jefe del usuario creador");
                }
                
                GrafoFirmaDigital firmaDigital;
                
                if ("jefe".equals(jefe.getIdUsuario())) {
                    /**
                     * Si es el usuario de pruebas "Jefe" se usa el usuario de pruebas del
                     * servicio de firma digital
                     */
                    firmaDigital = merger.generarGrafo("55305495", path);
                } else {
                    /**
                     * Para los demas usuarios se intenta obtener el usuario desde el
                     * servicio de firma digital
                     */
                    firmaDigital = merger.generarGrafo(usuario.getUsuarios()
                                                              .get(0)
                                                              .getIdUsuario(), path);
                }
                logger.debug("Usuario jefe :"+jefe.getIdUsuario()+" "+jefe.getEmail());
                

                if (firmaDigital != null) {
                    /**
                     * Si se obtiene la firma se estampa en el documento
                     */
                    merger.buscarYReemplazarImagen(documentObject, firmaDigital.getRutaGrafo(),
                                                   PropertiesLoader.getProperty(CamposODTProperties.GRAFO_FIRMA_DIGITAL),
                                                   mimeType);
                    merger.buscarYReemplazar(documentObject, mimeType, firmaDigital.getNombreUsuario(),
                                             PropertiesLoader.getProperty(CamposODTProperties.NOMBRE_FIRMA_DIGITAL));
                    merger.buscarYReemplazar(documentObject, mimeType, firmaDigital.getCargoUsuario(),
                                             PropertiesLoader.getProperty(CamposODTProperties.CARGO_FIRMA_DIGITAL));
                    logger.debug("rutaGrafo: " + firmaDigital.getRutaGrafo());
                } else {
                    /**
                     * Si no se obtiene, se estampa unicamente el nombre del jefe en el documento
                     */
                    merger.buscarYReemplazar(documentObject, mimeType, "",
                                             PropertiesLoader.getProperty(CamposODTProperties.GRAFO_FIRMA_DIGITAL));
                    merger.buscarYReemplazar(documentObject, mimeType, jefe.getNombreUsuario(),
                                             PropertiesLoader.getProperty(CamposODTProperties.NOMBRE_FIRMA_DIGITAL));
                    merger.buscarYReemplazar(documentObject, mimeType, "",
                                             PropertiesLoader.getProperty(CamposODTProperties.CARGO_FIRMA_DIGITAL));
                }
            
                /**
                 * ##################################################
                 */
            }
            if (comunicacion.getAsunto() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getAsunto(),
                                         PropertiesLoader.getProperty(CamposODTProperties.ASUNTO));
            }
            if (comunicacion.getNroFolios() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getNroFolios(),
                                         PropertiesLoader.getProperty(CamposODTProperties.NRO_FOLIOS));
            }
            if (comunicacion.getReferencia() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getReferencia(),
                                         PropertiesLoader.getProperty(CamposODTProperties.REFERENCIA));
            }
            if (comunicacion.getEsTituloMinero() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getEsTituloMinero(),
                                         PropertiesLoader.getProperty(CamposODTProperties.TITULO_MINERO));
            }
            if (comunicacion.getNroPlaca() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getNroPlaca(),
                                         PropertiesLoader.getProperty(CamposODTProperties.NRO_PLACA));
            }
            if (comunicacion.getNroRadicadoRelacionado() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getNroRadicadoRelacionado(),
                                         PropertiesLoader.getProperty(CamposODTProperties.NRO_RADICADO_RELACIONADO));
            }
            if (comunicacion.getRequiereRespuesta() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getRequiereRespuesta(),
                                         PropertiesLoader.getProperty(CamposODTProperties.REQUIERE_RESPUESTA));
            }
            if (comunicacion.getNroAnexos() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getNroAnexos(),
                                         PropertiesLoader.getProperty(CamposODTProperties.NRO_ANEXOS));
            }
            if (comunicacion.getFechaRadicado() != null) {
                logger.debug("Fecha radicado: " + comunicacion.getFechaRadicado());
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getFechaRadicado(),
                                         PropertiesLoader.getProperty(CamposODTProperties.FECHA_RADICADO));
            }
            if (comunicacion.getDependenciaDestino() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getDependenciaDestino(),
                                         PropertiesLoader.getProperty(CamposODTProperties.DEPENDENCIA_DESTINO));
            }
            if (comunicacion.getDestinatarioInterno() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getDestinatarioInterno(),
                                         PropertiesLoader.getProperty(CamposODTProperties.DESTINATARIO_INTERNO));
            }
            if (comunicacion.getTipoComunicacion() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getTipoComunicacion(),
                                         PropertiesLoader.getProperty(CamposODTProperties.TIPO_COMUNICACION));
            }
            if (comunicacion.getTiempoRespuesta() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getTiempoRespuesta(),
                                         PropertiesLoader.getProperty(CamposODTProperties.TIEMPO_RESPUESTA));
            }
            if (comunicacion.getFechaCreacion() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getFechaCreacion(),
                                         PropertiesLoader.getProperty(CamposODTProperties.FECHA_CREACION));
            }
            if (comunicacion.getMedioEnvio() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getMedioEnvio(),
                                         PropertiesLoader.getProperty(CamposODTProperties.MEDIO_ENVIO));
            }
            if (comunicacion.getDependenciaOrigen() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getDependenciaOrigen(),
                                         PropertiesLoader.getProperty(CamposODTProperties.DEPENDENCIA_ORIGEN));
            }
            if (comunicacion.getContenido() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getContenido(),
                                         PropertiesLoader.getProperty(CamposODTProperties.CONTENIDO));
            }
            if (comunicacion.getElaboro() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getElaboro(),
                                         PropertiesLoader.getProperty(CamposODTProperties.ELABORO));
            }
            if (comunicacion.getTipoPQRS() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getTipoPQRS(),
                                         PropertiesLoader.getProperty(CamposODTProperties.TIPO_PQRS));
            }
            if (comunicacion.getTipoIdentificacionInteresado() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getTipoIdentificacionInteresado(),
                                         PropertiesLoader.getProperty(CamposODTProperties.TIPO_ID_INTERESADO));
            }
            if (comunicacion.getNumIdentificacionInteresado() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getNumIdentificacionInteresado(),
                                         PropertiesLoader.getProperty(CamposODTProperties.NUM_ID_INTERESADO));
            }
            if (comunicacion.getNomCompletoInteresado() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getNomCompletoInteresado(),
                                         PropertiesLoader.getProperty(CamposODTProperties.NOM_COMPLETO_INTERESADO));
            }
            if (comunicacion.getEmailInteresado() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getEmailInteresado(),
                                         PropertiesLoader.getProperty(CamposODTProperties.EMAIL_INTERESADO));
            }
            if (comunicacion.getEmailInteresado() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getEmailInteresado(),
                                         PropertiesLoader.getProperty(CamposODTProperties.EMAIL_INTERESADO));
            }
            if (comunicacion.getTelefonoInteresado() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getTelefonoInteresado(),
                                         PropertiesLoader.getProperty(CamposODTProperties.TELEFONO_INTERESADO));
            }
            if (comunicacion.getCelularInteresado() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getCelularInteresado(),
                                         PropertiesLoader.getProperty(CamposODTProperties.CELULAR_INTERESADO));
            }
            if (comunicacion.getDireccionInteresado() != null) {
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getDireccionInteresado(),
                                         PropertiesLoader.getProperty(CamposODTProperties.DIRECCION_INTERESADO));
            }
            if (comunicacion.getPaisInteresado() != null) {
                logger.debug("pais: " + comunicacion.getPaisInteresado());
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getPaisInteresado(),
                                         PropertiesLoader.getProperty(CamposODTProperties.PAIS_INTERESADO));
            }
            if (comunicacion.getDepartamentoInteresado() != null) {
                logger.debug("departamento: " + comunicacion.getDepartamentoInteresado());
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getDepartamentoInteresado(),
                                         PropertiesLoader.getProperty(CamposODTProperties.DEPTO_INTERESADO));
            }
            if (comunicacion.getMunicipioInteresado() != null) {
                logger.debug("municipio: " + comunicacion.getMunicipioInteresado());
                merger.buscarYReemplazar(documentObject, mimeType, comunicacion.getMunicipioInteresado(),
                                         PropertiesLoader.getProperty(CamposODTProperties.MUNICIPIO_INTERESADO));
            }

            /*documentoOdt.save(path + "/" +
                              merger.obtenerNombreArchivo(rutaArchivo, request.getComunicacion().getNroRadicado(), false));*/

            merger.saveDoc(documentObject,
                           path + "/" +
                           merger.obtenerNombreArchivo(rutaArchivo, request.getComunicacion().getNroRadicado(), false),
                           mimeType);

            if (request.isPdf()) {
                PDFConverter.getInstance()
                    .convertirAPdf(path + "/" +
                                   merger.obtenerNombreArchivo(rutaArchivo, request.getComunicacion().getNroRadicado(),
                                                               false),
                                   path + "/" +
                                   merger.obtenerNombreArchivo(rutaArchivo, request.getComunicacion().getNroRadicado(),
                                                               true));
            }

            //if (rutaCodigoBarras != null) {
            //  new File(rutaCodigoBarras).delete();
            //}

            respuesta = new RespuestaGeneradorDTO();
            respuesta.setRespuesta("SUCCESS");

            if (request.isPdf()) {
                respuesta.setRutaArchivoFinal(path + "/" +
                                              merger.obtenerNombreArchivo(rutaArchivo,
                                                                          request.getComunicacion().getNroRadicado(),
                                                                          true));
            } else {
                respuesta.setRutaArchivoFinal(path + "/" +
                                              merger.obtenerNombreArchivo(rutaArchivo,
                                                                          request.getComunicacion().getNroRadicado(),
                                                                          false));
            }
            logger.debug("FIN generarBarCode");
            return respuesta;
        } catch (Exception e) {
            //logger.error("Exception generarBarcode",e);
            throw new Exception("Error creando archivo de salida", e);
        }

    }
}
