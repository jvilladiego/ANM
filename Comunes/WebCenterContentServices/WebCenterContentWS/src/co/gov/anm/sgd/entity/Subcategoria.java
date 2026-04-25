package co.gov.anm.sgd.entity;

import java.io.Serializable;

import java.math.BigDecimal;

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
@NamedQueries({ @NamedQuery(name = "Subcategoria.findAll", query = "select o from Subcategoria o"),
                @NamedQuery(name = "Subcategoria.findById", query = "select o from Subcategoria o where o.idSubCategoria = :idSub")})
@Table(name = "SGD_SUB_CATEGORIA")
public class Subcategoria implements Serializable {
    private static final long serialVersionUID = -8265838829598070699L;
    
    @Id
    @Column(name = "ID_SUB_CATEGORIA", nullable = false)
    private BigDecimal idSubCategoria;
    
    @Column(name = "VICEPRESIDENCIA", length = 100)
    private String vicepresidencia;
    
    @Column(name = "CODIGO_DEPENDENCIA", length = 100)
    private String codigoDepedencia;
    
    @Column(name = "DEPENDENCIA", length = 200)
    private String depedencia;
    
    @Column(name = "SUBCATEGORIA", length = 200)
    private String subCategoria;
    
    @Column(name = "VIGENTE")
    private Integer vigente;
    
    @Column(name = "USUARIO_CREACION", length = 50)
    private String usuarioCreacion;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION")
    private Date fechaCreacion;
    
    @Column(name = "IP_CREACION", length = 20)
    private String ipCreacion;
    
    @Column(name = "USUARIO_MODIFICACION", length = 50)
    private String usuarioModificacion;
        
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_MODIFICACION")
    private Date fechaModificacion;
    
    @Column(name = "IP_MODIFICACION", length = 20)
    private String ipModificacion;
    
    @OneToMany(mappedBy = "subcategoria")
    private Collection<SgdComunicacion> comunicaciones;
    
    public Subcategoria() {
    }

    public BigDecimal getIdSubCategoria() {
        return idSubCategoria;
    }

    public void setIdSubCategoria(BigDecimal idSubCategoria) {
        this.idSubCategoria = idSubCategoria;
    }

    public String getVicepresidencia() {
        return vicepresidencia;
    }

    public void setVicepresidencia(String vicepresidencia) {
        this.vicepresidencia = vicepresidencia;
    }

    public String getCodigoDepedencia() {
        return codigoDepedencia;
    }

    public void setCodigoDepedencia(String codigoDepedencia) {
        this.codigoDepedencia = codigoDepedencia;
    }

    public String getDepedencia() {
        return depedencia;
    }

    public void setDepedencia(String depedencia) {
        this.depedencia = depedencia;
    }

    public String getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(String subCategoria) {
        this.subCategoria = subCategoria;
    }

    public Integer getVigente() {
        return vigente;
    }

    public void setVigente(Integer vigente) {
        this.vigente = vigente;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getIpCreacion() {
        return ipCreacion;
    }

    public void setIpCreacion(String ipCreacion) {
        this.ipCreacion = ipCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getIpModificacion() {
        return ipModificacion;
    }

    public void setIpModificacion(String ipModificacion) {
        this.ipModificacion = ipModificacion;
    }

    public Collection<SgdComunicacion> getComunicaciones() {
        return comunicaciones;
    }

    public void setComunicaciones(Collection<SgdComunicacion> comunicaciones) {
        this.comunicaciones = comunicaciones;
    }
}
