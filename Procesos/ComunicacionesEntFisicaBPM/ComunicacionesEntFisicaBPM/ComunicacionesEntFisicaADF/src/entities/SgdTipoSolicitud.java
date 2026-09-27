package entities;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "SgdTipoSolicitud.findAll", query = "select o from SgdTipoSolicitud o"),
                @NamedQuery(name = "SgdTipoSolicitud.findById", 
                            query = "select o from SgdTipoSolicitud o where o.idTipoSolicitud = :param")
                })
@Table(name = "SGD_TIPO_SOLICITUD")
public class SgdTipoSolicitud implements Serializable {
    private static final long serialVersionUID = 8321885069622756149L;

    @Id
    @Column(name = "ID_TIPO_SOLICITUD")
    private Integer idTipoSolicitud;
    @Column
    private String nombre;
    @Column(name = "TIEMPO_RESPUESTA")
    private Integer tiempoRespuesta;
    @Column
    private Integer vigente;
    @Column(name = "USUARIO_CREACION")
    private String usuarioCreacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION")
    private Date fechaCreacion;
    @Column(name = "IP_CREACION")
    private String ipCreacion;
    @Column(name = "USUARIO_MODIFICACION")
    private String usuarioModificacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_MODIFICACION")
    private Date fechaModificacion;
    @Column(name = "IP_MODIFICACION")
    private String ipModificacion;


    public SgdTipoSolicitud() {
    }

    public void setIdTipoSolicitud(Integer idTipoSolicitud) {
        this.idTipoSolicitud = idTipoSolicitud;
    }

    public Integer getIdTipoSolicitud() {
        return idTipoSolicitud;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setVigente(Integer vigente) {
        this.vigente = vigente;
    }

    public Integer getVigente() {
        return vigente;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setIpCreacion(String ipCreacion) {
        this.ipCreacion = ipCreacion;
    }

    public String getIpCreacion() {
        return ipCreacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setIpModificacion(String ipModificacion) {
        this.ipModificacion = ipModificacion;
    }

    public String getIpModificacion() {
        return ipModificacion;
    }

    public void setTiempoRespuesta(Integer tiempoRespuesta) {
        this.tiempoRespuesta = tiempoRespuesta;
    }

    public Integer getTiempoRespuesta() {
        return tiempoRespuesta;
    }
}
