package co.gov.anm.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "SgdSubCategoria.findAll", query = "select o from SgdSubCategoria o") })
@Table(name = "SGD_SUB_CATEGORIA")
public class SgdSubCategoria implements Serializable {
    private static final long serialVersionUID = -1663692238628014877L;
    
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
    
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "SGD_CATEGORIA_TIPO", 
        joinColumns = { @JoinColumn(name = "ID_SUB_CATEGORIA") }, 
        inverseJoinColumns = { @JoinColumn(name = "ID_TIPO_SOLICITUD") }
    )
    private Collection<SgdTipoSolicitud> tipos;
  
    
    public SgdSubCategoria() {
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

    public Collection<SgdTipoSolicitud> getTipos() {
        return tipos;
    }

    public void setTipos(Collection<SgdTipoSolicitud> tipos) {
        this.tipos = tipos;
    }
}