package co.gov.anm.entity;

import java.io.Serializable;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "SgdTipoSolicitud.findAll", query = "select o from SgdTipoSolicitud o where o.vigente = 1"),
                @NamedQuery(name = "SgdTipoSolicitud.getTipoSolicitudById",
                            query = "select o from SgdTipoSolicitud o where o.idTipoSolicitud = :idTipoSolicitud")
                
                })
@Table(name = "SGD_TIPO_SOLICITUD")
public class SgdTipoSolicitud implements Serializable {
    @SuppressWarnings("compatibility:1963783150923207280")
    private static final long serialVersionUID = 1L;
    
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
    @Column
    private Integer vigente;
    
    @ManyToMany(mappedBy = "tipos")
    private Collection<SgdSubCategoria> categorias;
    

    public SgdTipoSolicitud() {
    }

    public SgdTipoSolicitud(Date fechaCreacion, Date fechaModificacion, Long idTipoSolicitud, String ipCreacion,
                            String ipModificacion, String nombre, Integer tiempoRespuesta, String usuarioCreacion,
                            String usuarioModificacion, Integer vigente) {
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.idTipoSolicitud = idTipoSolicitud;
        this.ipCreacion = ipCreacion;
        this.ipModificacion = ipModificacion;
        this.nombre = nombre;
        this.tiempoRespuesta = tiempoRespuesta;
        this.usuarioCreacion = usuarioCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.vigente = vigente;
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

    public void setCategorias(Collection<SgdSubCategoria> categorias) {
        this.categorias = categorias;
    }

    public Collection<SgdSubCategoria> getCategorias() {
        return categorias;
    }
}
