package co.gov.anm.sgd.entity;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "SgdUsuarioRol.findAll", query = "select o from SgdUsuarioRol o"),
                @NamedQuery(name = "SgdUsuarioRol.findByRol", query = "select o from SgdUsuarioRol o where o.sgdRol = :idRol")
            })
@Table(name = "SGD_USUARIO_ROL")
public class SgdUsuarioRol implements Serializable {
    private static final long serialVersionUID = 3887378630878573693L;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ACTUALIZACION")
    private Date fechaActualizacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION", nullable = false)
    private Date fechaCreacion;
    @Id
    @Column(name = "ID_USUARIO_ROL", nullable = false)
    private Long idUsuarioRol;
    @Column(name = "USUARIO_ACTUALIZACION", length = 100)
    private String usuarioActualizacion;
    @Column(name = "USUARIO_CREACION", nullable = false, length = 100)
    private String usuarioCreacion;
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private SgdUsuario sgdUsuario;
    @ManyToOne
    @JoinColumn(name = "ID_ROL")
    private SgdRol sgdRol;

    public SgdUsuarioRol() {
    }

    public SgdUsuarioRol(Date fechaActualizacion, Date fechaCreacion, SgdRol sgdRol, SgdUsuario sgdUsuario,
                         Long idUsuarioRol, String usuarioActualizacion, String usuarioCreacion) {
        this.fechaActualizacion = fechaActualizacion;
        this.fechaCreacion = fechaCreacion;
        this.sgdRol = sgdRol;
        this.sgdUsuario = sgdUsuario;
        this.idUsuarioRol = idUsuarioRol;
        this.usuarioActualizacion = usuarioActualizacion;
        this.usuarioCreacion = usuarioCreacion;
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


    public Long getIdUsuarioRol() {
        return idUsuarioRol;
    }

    public void setIdUsuarioRol(Long idUsuarioRol) {
        this.idUsuarioRol = idUsuarioRol;
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

    public SgdUsuario getSgdUsuario() {
        return sgdUsuario;
    }

    public void setSgdUsuario(SgdUsuario sgdUsuario) {
        this.sgdUsuario = sgdUsuario;
    }

    public SgdRol getSgdRol() {
        return sgdRol;
    }

    public void setSgdRol(SgdRol sgdRol) {
        this.sgdRol = sgdRol;
    }
}
