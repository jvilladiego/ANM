package entities;

import java.io.Serializable;

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
@NamedQueries({ @NamedQuery(name = "SgdRol.findAll", query = "select o from SgdRol o") })
@Table(name = "SGD_ROL")
public class SgdRol implements Serializable {
    private static final long serialVersionUID = -81937092952542823L;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ACTUALIZACION")
    private Date fechaActualizacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION", nullable = false)
    private Date fechaCreacion;
    @Id
    @Column(name = "ID_ROL", nullable = false)
    private Long idRol;
    @Column(name = "NOMBRE_ROL", length = 100)
    private String nombreRol;
    @Column(name = "USUARIO_ACTUALIZACION", length = 100)
    private String usuarioActualizacion;
    @Column(name = "USUARIO_CREACION", nullable = false, length = 100)
    private String usuarioCreacion;
    @OneToMany(mappedBy = "sgdRol", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<SgdUsuarioRol> sgdUsuarioRolList1;

    public SgdRol() {
    }

    public SgdRol(Date fechaActualizacion, Date fechaCreacion, Long idRol, String nombreRol,
                  String usuarioActualizacion, String usuarioCreacion) {
        this.fechaActualizacion = fechaActualizacion;
        this.fechaCreacion = fechaCreacion;
        this.idRol = idRol;
        this.nombreRol = nombreRol;
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

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
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

    public List<SgdUsuarioRol> getSgdUsuarioRolList1() {
        return sgdUsuarioRolList1;
    }

    public void setSgdUsuarioRolList1(List<SgdUsuarioRol> sgdUsuarioRolList1) {
        this.sgdUsuarioRolList1 = sgdUsuarioRolList1;
    }

    public SgdUsuarioRol addSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol) {
        getSgdUsuarioRolList1().add(sgdUsuarioRol);
        sgdUsuarioRol.setSgdRol(this);
        return sgdUsuarioRol;
    }

    public SgdUsuarioRol removeSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol) {
        getSgdUsuarioRolList1().remove(sgdUsuarioRol);
        sgdUsuarioRol.setSgdRol(null);
        return sgdUsuarioRol;
    }
}
