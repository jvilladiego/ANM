package entities;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "SgdComunicacion.findAll", query = "select o from SgdComunicacion o") })
@Table(name = "SGD_COMUNICACION")
public class SgdComunicacion implements Serializable {
    private static final long serialVersionUID = 7847382076920295177L;
    @Column(nullable = false, length = 200)
    private String asunto;
    @Column(name = "CONTENT_FLD_COMUNICACIONES", length = 200)
    private String contentFldComunicaciones;
    @Column(name = "CONTENT_FLD_OTRO", length = 200)
    private String contentFldOtro;
    @Column(name = "CONTENT_FLD_TITULO", length = 200)
    private String contentFldTitulo;
    @Column(name = "ESTADO_COMUNICACION", length = 100)
    private String estadoComunicacion;
    @Column(name = "ES_TITULO", nullable = false, length = 1)
    private String esTitulo;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ACTUALIZACION")
    private Date fechaActualizacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION", nullable = false)
    private Date fechaCreacion;
    @Column(name = "GRUPO_SEGURIDAD", length = 100)
    private String grupoSeguridad;
    @Id
    @Column(name = "ID_COMUNICACION", nullable = false)
    private BigDecimal idComunicacion;
    @Column(name = "ID_DEPENDENCIA_DESTINO")
    private Integer idDependenciaDestino;
    @Column(name = "ID_TIPO_DOCUMENTAL_TRAMITE")
    private Long idTipoDocumentalTramite;
    @Column(name = "ID_TRAMITE")
    private Integer idTramite;
    @Column(name = "ID_UNIDAD_PRODUCTORA", nullable = false)
    private Integer idUnidadProductora;
    @Column(name = "NRO_ANEXOS", nullable = false)
    private Integer nroAnexos;
    @Column(name = "NRO_FOLIOS", nullable = false)
    private Integer nroFolios;
    @Column(name = "NRO_PLACA", length = 100)
    private String nroPlaca;
    @Column(name = "NRO_RADICADO", length = 100)
    private String nroRadicado;
    @Column(name = "NRO_RADICADO_RELACIONADO", length = 100)
    private String nroRadicadoRelacionado;
    @Column(length = 200)
    private String referencia;
    @Column(name = "REQUIERE_RESPUESTA", nullable = false, length = 1)
    private String requiereRespuesta;
    @Column(length = 100)
    private String tramite;
    @Column(name = "USUARIO_ACTUALIZACION", length = 100)
    private String usuarioActualizacion;
    @Column(name = "USUARIO_CREACION", nullable = false, length = 100)
    private String usuarioCreacion;
   
   
   
    public SgdComunicacion() {
    }

    public SgdComunicacion(String asunto, String contentFldComunicaciones, String contentFldOtro,
                           String contentFldTitulo, String esTitulo, String estadoComunicacion, Date fechaActualizacion,
                           Date fechaCreacion, String grupoSeguridad, BigDecimal idComunicacion,
                           Integer idDependenciaDestino, Long idTipoDocumentalTramite, Integer idTramite,
                           Integer idUnidadProductora, Integer nroAnexos, Integer nroFolios, String nroPlaca,
                           String nroRadicado, String nroRadicadoRelacionado, String referencia,
                           String requiereRespuesta, String tramite, String usuarioActualizacion,
                           String usuarioCreacion) {
        this.asunto = asunto;
        this.contentFldComunicaciones = contentFldComunicaciones;
        this.contentFldOtro = contentFldOtro;
        this.contentFldTitulo = contentFldTitulo;
        this.esTitulo = esTitulo;
        this.estadoComunicacion = estadoComunicacion;
        this.fechaActualizacion = fechaActualizacion;
        this.fechaCreacion = fechaCreacion;
        this.grupoSeguridad = grupoSeguridad;
        this.idComunicacion = idComunicacion;
        this.idDependenciaDestino = idDependenciaDestino;
        this.idTipoDocumentalTramite = idTipoDocumentalTramite;
        this.idTramite = idTramite;
        this.idUnidadProductora = idUnidadProductora;
        this.nroAnexos = nroAnexos;
        this.nroFolios = nroFolios;
        this.nroPlaca = nroPlaca;
        this.nroRadicado = nroRadicado;
        this.nroRadicadoRelacionado = nroRadicadoRelacionado;
        this.referencia = referencia;
        this.requiereRespuesta = requiereRespuesta;
        this.tramite = tramite;
        this.usuarioActualizacion = usuarioActualizacion;
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContentFldComunicaciones() {
        return contentFldComunicaciones;
    }

    public void setContentFldComunicaciones(String contentFldComunicaciones) {
        this.contentFldComunicaciones = contentFldComunicaciones;
    }

    public String getContentFldOtro() {
        return contentFldOtro;
    }

    public void setContentFldOtro(String contentFldOtro) {
        this.contentFldOtro = contentFldOtro;
    }

    public String getContentFldTitulo() {
        return contentFldTitulo;
    }

    public void setContentFldTitulo(String contentFldTitulo) {
        this.contentFldTitulo = contentFldTitulo;
    }

    public String getEstadoComunicacion() {
        return estadoComunicacion;
    }

    public void setEstadoComunicacion(String estadoComunicacion) {
        this.estadoComunicacion = estadoComunicacion;
    }

    public String getEsTitulo() {
        return esTitulo;
    }

    public void setEsTitulo(String esTitulo) {
        this.esTitulo = esTitulo;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getGrupoSeguridad() {
        return grupoSeguridad;
    }

    public void setGrupoSeguridad(String grupoSeguridad) {
        this.grupoSeguridad = grupoSeguridad;
    }

    public BigDecimal getIdComunicacion() {
        return idComunicacion;
    }

    public void setIdComunicacion(BigDecimal idComunicacion) {
        this.idComunicacion = idComunicacion;
    }

    public Integer getIdDependenciaDestino() {
        return idDependenciaDestino;
    }

    public void setIdDependenciaDestino(Integer idDependenciaDestino) {
        this.idDependenciaDestino = idDependenciaDestino;
    }

    public Long getIdTipoDocumentalTramite() {
        return idTipoDocumentalTramite;
    }

    public void setIdTipoDocumentalTramite(Long idTipoDocumentalTramite) {
        this.idTipoDocumentalTramite = idTipoDocumentalTramite;
    }

    public Integer getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(Integer idTramite) {
        this.idTramite = idTramite;
    }

    public Integer getIdUnidadProductora() {
        return idUnidadProductora;
    }

    public void setIdUnidadProductora(Integer idUnidadProductora) {
        this.idUnidadProductora = idUnidadProductora;
    }

    public Integer getNroAnexos() {
        return nroAnexos;
    }

    public void setNroAnexos(Integer nroAnexos) {
        this.nroAnexos = nroAnexos;
    }

    public Integer getNroFolios() {
        return nroFolios;
    }

    public void setNroFolios(Integer nroFolios) {
        this.nroFolios = nroFolios;
    }

    public String getNroPlaca() {
        return nroPlaca;
    }

    public void setNroPlaca(String nroPlaca) {
        this.nroPlaca = nroPlaca;
    }

    public String getNroRadicado() {
        return nroRadicado;
    }

    public void setNroRadicado(String nroRadicado) {
        this.nroRadicado = nroRadicado;
    }

    public String getNroRadicadoRelacionado() {
        return nroRadicadoRelacionado;
    }

    public void setNroRadicadoRelacionado(String nroRadicadoRelacionado) {
        this.nroRadicadoRelacionado = nroRadicadoRelacionado;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getRequiereRespuesta() {
        return requiereRespuesta;
    }

    public void setRequiereRespuesta(String requiereRespuesta) {
        this.requiereRespuesta = requiereRespuesta;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public String getUsuarioActualizacion() {
        return usuarioActualizacion;
    }

    public void setUsuarioActualizacion(String usuarioActualizacion) {
        this.usuarioActualizacion = usuarioActualizacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

}
