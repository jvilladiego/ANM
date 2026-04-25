package co.gov.anm.sgd.transfer;

import java.util.Calendar;

public class ComunicacionRequest {
    
    private Integer idComunicacion;
    private String nroRadicado;
    private String asunto;
    private Integer nroFolios;
    private String referencia;
    private String esTitulo;
    private String nroPlaca;
    private String nroRadicadoRelacionado;
    private String requiereRespuesta;
    private Integer nroAnexos;
    private Integer idUnidadProductora;
    private Integer idDependenciaDestino;
    private Calendar fechaCreacion;
    private Calendar fechaModificacion;
    private String usuarioCreacion;
    private String usuarioActualizacion;
    private String contentFLdComunicaciones;
    private String contentFldTitulo;
    private String contentFldOtro;
    private String grupoSeguridad;
    private Integer idTramite;
    private String tramite;
    private Long idTipoDocumentalTramite;
    private String estadoComunicacion;
    private String esPQRS;
    private String trasladoOtraEntidad;
    private String idReservada;
    private String comentarios;
    private Integer diasParaRespuesta;
    private Calendar fechaVencimiento;
    private String codigoSerie;
    private String codigoSubSerie;
    private String nombreSerie;
    private String nombreSubSerie;
    private String nombreTpDoc;
    private String accesoNegado;
    private String tipoComunicacion;
    private Long idTipoSolicitud;
    private Long idSubcategoria;
    private String medioRespuesta;
    private String appName;

    public void setTipoComunicacion(String tipoComunicacion) {
        this.tipoComunicacion = tipoComunicacion;
    }

    public String getTipoComunicacion() {
        return tipoComunicacion;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setDiasParaRespuesta(Integer diasParaRespuesta) {
        this.diasParaRespuesta = diasParaRespuesta;
    }

    public Integer getDiasParaRespuesta() {
        return diasParaRespuesta;
    }

    public void setFechaVencimiento(Calendar fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Calendar getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setCodigoSerie(String codigoSerie) {
        this.codigoSerie = codigoSerie;
    }

    public String getCodigoSerie() {
        return codigoSerie;
    }

    public void setCodigoSubSerie(String codigoSubSerie) {
        this.codigoSubSerie = codigoSubSerie;
    }

    public String getCodigoSubSerie() {
        return codigoSubSerie;
    }

    public void setNombreSerie(String nombreSerie) {
        this.nombreSerie = nombreSerie;
    }

    public String getNombreSerie() {
        return nombreSerie;
    }

    public void setNombreSubSerie(String nombreSubSerie) {
        this.nombreSubSerie = nombreSubSerie;
    }

    public String getNombreSubSerie() {
        return nombreSubSerie;
    }

    public void setNombreTpDoc(String nombreTpDoc) {
        this.nombreTpDoc = nombreTpDoc;
    }

    public String getNombreTpDoc() {
        return nombreTpDoc;
    }

    public void setAccesoNegado(String accesoNegado) {
        this.accesoNegado = accesoNegado;
    }

    public String getAccesoNegado() {
        return accesoNegado;
    }

    public void setEsPQRS(String esPQRS) {
        this.esPQRS = esPQRS;
    }

    public String getEsPQRS() {
        return esPQRS;
    }

    public void setTrasladoOtraEntidad(String trasladoOtraEntidad) {
        this.trasladoOtraEntidad = trasladoOtraEntidad;
    }

    public String getTrasladoOtraEntidad() {
        return trasladoOtraEntidad;
    }

    public void setIdReservada(String idReservada) {
        this.idReservada = idReservada;
    }

    public String getIdReservada() {
        return idReservada;
    }

    public void setIdComunicacion(Integer idComunicacion) {
        this.idComunicacion = idComunicacion;
    }

    public Integer getIdComunicacion() {
        return idComunicacion;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public String getTramite() {
        return tramite;
    }

    public void setIdTipoDocumentalTramite(Long idTipoDocumentalTramite) {
        this.idTipoDocumentalTramite = idTipoDocumentalTramite;
    }

    public Long getIdTipoDocumentalTramite() {
        return idTipoDocumentalTramite;
    }

    public void setEstadoComunicacion(String estadoComunicacion) {
        this.estadoComunicacion = estadoComunicacion;
    }

    public String getEstadoComunicacion() {
        return estadoComunicacion;
    }

    public void setNroRadicado(String nroRadicado) {
        this.nroRadicado = nroRadicado;
    }

    public String getNroRadicado() {
        return nroRadicado;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setNroFolios(Integer nroFolios) {
        this.nroFolios = nroFolios;
    }

    public Integer getNroFolios() {
        return nroFolios;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setEsTitulo(String esTitulo) {
        this.esTitulo = esTitulo;
    }

    public String getEsTitulo() {
        return esTitulo;
    }

    public void setNroPlaca(String nroPlaca) {
        this.nroPlaca = nroPlaca;
    }

    public String getNroPlaca() {
        return nroPlaca;
    }

    public void setNroRadicadoRelacionado(String nroRadicadoRelacionado) {
        this.nroRadicadoRelacionado = nroRadicadoRelacionado;
    }

    public String getNroRadicadoRelacionado() {
        return nroRadicadoRelacionado;
    }

    public void setRequiereRespuesta(String requiereRespuesta) {
        this.requiereRespuesta = requiereRespuesta;
    }

    public String getRequiereRespuesta() {
        return requiereRespuesta;
    }

    public void setNroAnexos(Integer nroAnexos) {
        this.nroAnexos = nroAnexos;
    }

    public Integer getNroAnexos() {
        return nroAnexos;
    }

    public void setIdUnidadProductora(Integer idUnidadProductora) {
        this.idUnidadProductora = idUnidadProductora;
    }

    public Integer getIdUnidadProductora() {
        return idUnidadProductora;
    }

    public void setIdDependenciaDestino(Integer idDependenciaDestino) {
        this.idDependenciaDestino = idDependenciaDestino;
    }

    public Integer getIdDependenciaDestino() {
        return idDependenciaDestino;
    }

    public void setFechaCreacion(Calendar fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Calendar getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaModificacion(Calendar fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Calendar getFechaActualizacion() {
        return fechaModificacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioActualizacion(String usuarioActualizacion) {
        this.usuarioActualizacion = usuarioActualizacion;
    }

    public String getUsuarioActualizacion() {
        return usuarioActualizacion;
    }

    public void setContentFLdComunicaciones(String contentFLdComunicaciones) {
        this.contentFLdComunicaciones = contentFLdComunicaciones;
    }

    public String getContentFLdComunicaciones() {
        return contentFLdComunicaciones;
    }

    public void setContentFldTitulo(String contentFldTitulo) {
        this.contentFldTitulo = contentFldTitulo;
    }

    public String getContentFldTitulo() {
        return contentFldTitulo;
    }

    public void setContentFldOtro(String contentFldOtro) {
        this.contentFldOtro = contentFldOtro;
    }

    public String getContentFldOtro() {
        return contentFldOtro;
    }

    public void setGrupoSeguridad(String grupoSeguridad) {
        this.grupoSeguridad = grupoSeguridad;
    }

    public String getGrupoSeguridad() {
        return grupoSeguridad;
    }

    public void setIdTramite(Integer idTramite) {
        this.idTramite = idTramite;
    }

    public Integer getIdTramite() {
        return idTramite;
    }

    public Long getIdTipoSolicitud() {
        return idTipoSolicitud;
    }

    public void setIdTipoSolicitud(Long idTipoSolicitud) {
        this.idTipoSolicitud = idTipoSolicitud;
    }

    public Long getIdSubcategoria() {
        return idSubcategoria;
    }

    public void setIdSubcategoria(Long idSubcategoria) {
        this.idSubcategoria = idSubcategoria;
    }

    public String getMedioRespuesta() {
        return medioRespuesta;
    }

    public void setMedioRespuesta(String medioRespuesta) {
        this.medioRespuesta = medioRespuesta;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

}
