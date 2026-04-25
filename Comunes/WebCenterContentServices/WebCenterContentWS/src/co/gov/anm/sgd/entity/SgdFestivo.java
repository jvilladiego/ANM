package co.gov.anm.sgd.entity;

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
@NamedQueries({ @NamedQuery(name = "SgdFestivo.findAll", query = "select o from SgdFestivo o") })
@Table(name = "SGD_FESTIVO")
public class SgdFestivo implements Serializable {
    private static final long serialVersionUID = 1141933254489408348L;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ACTUALIZACION")
    private Date fechaActualizacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION", nullable = false)
    private Date fechaCreacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_FESTIVO", nullable = false)
    private Date fechaFestivo;
    @Id
    @Column(name = "ID_FESTIVO", nullable = false)
    private Long idFestivo;
    @Column(name = "USUARIO_ACTUALIZACION", length = 100)
    private String usuarioActualizacion;
    @Column(name = "USUARIO_CREACION", nullable = false, length = 100)
    private String usuarioCreacion;

    public SgdFestivo() {
    }

    public SgdFestivo(Date fechaActualizacion, Date fechaCreacion, Date fechaFestivo, Long idFestivo,
                      String usuarioActualizacion, String usuarioCreacion) {
        this.fechaActualizacion = fechaActualizacion;
        this.fechaCreacion = fechaCreacion;
        this.fechaFestivo = fechaFestivo;
        this.idFestivo = idFestivo;
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

    public Date getFechaFestivo() {
        return fechaFestivo;
    }

    public void setFechaFestivo(Date fechaFestivo) {
        this.fechaFestivo = fechaFestivo;
    }

    public Long getIdFestivo() {
        return idFestivo;
    }

    public void setIdFestivo(Long idFestivo) {
        this.idFestivo = idFestivo;
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
