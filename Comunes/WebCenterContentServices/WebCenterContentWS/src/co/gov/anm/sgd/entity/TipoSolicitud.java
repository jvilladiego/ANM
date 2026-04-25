package co.gov.anm.sgd.entity;

import java.io.Serializable;

import java.util.Collection;
import java.util.Date;

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
@NamedQueries({ @NamedQuery(name = "TipoSolicitud.findAll", query = "select o from TipoSolicitud o"),
                @NamedQuery(name = "TipoSolicitud.findById", query = "select o from TipoSolicitud o where o.idTipoSolicitud = :idTipo")})
@Table(name = "SGD_TIPO_SOLICITUD")
public class TipoSolicitud implements Serializable {
    private static final long serialVersionUID = 7740331401624734829L;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION")
    private Date fechaCreacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_MODIFICACION")
    private Date fechaModificacion;
    @Id
    @Column(name = "ID_TIPO_SOLICITUD", nullable = false)
    private Long idTipoSolicitud;
    @Column(name = "IP_CREACION", length = 20)
    private String ipCreacion;
    @Column(name = "IP_MODIFICACION", length = 20)
    private String ipModificacion;
    @Column(length = 50)
    private String nombre;
    @Column(name = "TIEMPO_RESPUESTA")
    private Integer tiempoRespuesta;
    @Column(name = "USUARIO_CREACION", length = 50)
    private String usuarioCreacion;
    @Column(name = "USUARIO_MODIFICACION", length = 50)
    private String usuarioModificacion;
    @Column(name = "DIAS_ALERTA", length = 50)
    private String diasAlerta;
    @Column(name = "PLANTILLA_ALERTA", length = 50)
    private String plantillaAlerta;
    private Integer vigente;
    @OneToMany(mappedBy = "tipoSolicitud")
    private Collection<SgdComunicacion> comunicaciones;
    
    public TipoSolicitud() {
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Long getIdTipoSolicitud() {
        return idTipoSolicitud;
    }

    public void setIdTipoSolicitud(Long idTipoSolicitud) {
        this.idTipoSolicitud = idTipoSolicitud;
    }

    public String getIpCreacion() {
        return ipCreacion;
    }

    public void setIpCreacion(String ipCreacion) {
        this.ipCreacion = ipCreacion;
    }

    public String getIpModificacion() {
        return ipModificacion;
    }

    public void setIpModificacion(String ipModificacion) {
        this.ipModificacion = ipModificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getTiempoRespuesta() {
        return tiempoRespuesta;
    }

    public void setTiempoRespuesta(Integer tiempoRespuesta) {
        this.tiempoRespuesta = tiempoRespuesta;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Integer getVigente() {
        return vigente;
    }

    public void setVigente(Integer vigente) {
        this.vigente = vigente;
    }
    
    public String getDiasAlerta() {
        return diasAlerta;
    }

    public void setDiasAlerta(String diasAlerta) {
        this.diasAlerta = diasAlerta;
    }
    
    public String getPlantillaAlerta() {
        return plantillaAlerta;
    }

    public void setPlantillaAlerta(String plantillaAlerta) {
        this.plantillaAlerta = plantillaAlerta;
    }

    public Collection<SgdComunicacion> getComunicaciones() {
        return comunicaciones;
    }

    public void setComunicaciones(Collection<SgdComunicacion> comunicaciones) {
        this.comunicaciones = comunicaciones;
    }
}
