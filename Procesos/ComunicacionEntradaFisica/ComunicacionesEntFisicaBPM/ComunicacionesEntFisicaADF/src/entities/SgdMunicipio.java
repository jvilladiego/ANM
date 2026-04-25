package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.TemporalType;
import javax.persistence.Temporal;

@Entity
@NamedQueries({ @NamedQuery(name = "SgdMunicipio.findAll", 
                            query = "select o from SgdMunicipio o"),
                @NamedQuery(name = "SgdMunicipio.findByDepartamento", 
                            query = "select o from SgdMunicipio o where o.idDepartamento = :param order by o.nombre"),
                @NamedQuery(name = "SgdMunicipio.findById", 
                            query = "select o from SgdMunicipio o where o.idMunicipio = :param") 
                })
@Table(name = "SGD_MUNICIPIO")
public class SgdMunicipio implements Serializable {
    private static final long serialVersionUID = 3200068486778071970L;

    @Id
    @Column(name = "ID_MUNICIPIO", nullable = false)
    private Long idMunicipio;
    @Column(name = "ID_DEPARTAMENTO", length = 2)
    private Long idDepartamento;
    @Column(name = "NOMBRE", length = 50)
    private String nombre;
    @Column(name = "CODIGO", length = 6)
    private String codigo;
    @Column(name = "VIGENTE")
    private Integer vigente;
    @Column(name = "USUARIO_CREACION", length = 50)
    private String usuarioCreacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION", nullable = false)
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
    
    public SgdMunicipio() {
    }

    public void setIdMunicipio(Long idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public Long getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdDepartamento(Long idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public Long getIdDepartamento() {
        return idDepartamento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
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
}
