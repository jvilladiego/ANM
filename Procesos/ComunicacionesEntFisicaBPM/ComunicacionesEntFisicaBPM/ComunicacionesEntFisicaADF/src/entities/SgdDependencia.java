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
@NamedQueries({ @NamedQuery(name = "SgdDependencia.findAll", query = "select o from SgdDependencia o") })
@Table(name = "SGD_DEPENDENCIA")
public class SgdDependencia implements Serializable {
    private static final long serialVersionUID = -5931196717966820716L;
    @Column(length = 20)
    private String codigo;
    @Column(length = 500)
    private String descripcion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION")
    private Date fechaCreacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_MODIFICACION")
    private Date fechaModificacion;
    @Id
    @Column(name = "ID_SGD_DEPENDENCIA", nullable = false)
    private Long idSgdDependencia;
    @Column(name = "IP_CREACION", length = 20)
    private String ipCreacion;
    @Column(name = "IP_MODIFICACION", length = 20)
    private String ipModificacion;
    @Column(length = 50)
    private String nombre;
    @Column(name = "USUARIO_CREACION", length = 50)
    private String usuarioCreacion;
    @Column(name = "USUARIO_MODIFICACION", length = 50)
    private String usuarioModificacion;
    private Integer vigente;

    public SgdDependencia() {
    }

    public SgdDependencia(String codigo, String descripcion, Date fechaCreacion, Date fechaModificacion,
                          Long idSgdDependencia, String ipCreacion, String ipModificacion, String nombre,
                          String usuarioCreacion, String usuarioModificacion, Integer vigente) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.idSgdDependencia = idSgdDependencia;
        this.ipCreacion = ipCreacion;
        this.ipModificacion = ipModificacion;
        this.nombre = nombre;
        this.usuarioCreacion = usuarioCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.vigente = vigente;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public Long getIdSgdDependencia() {
        return idSgdDependencia;
    }

    public void setIdSgdDependencia(Long idSgdDependencia) {
        this.idSgdDependencia = idSgdDependencia;
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
}
