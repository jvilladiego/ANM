package co.gov.anm.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "SgdInteresadoInt.findAll", query = "select o from SgdInteresadoInt o") })
@Table(name = "SGD_INTERESADO_INT")
public class SgdInteresadoInt implements Serializable {
    private static final long serialVersionUID = -1202768821936711853L;
    @Column(nullable = false, length = 200)
    private String email;
    @Column(name = "ID_COMUNICACION", nullable = false)
    private BigDecimal idComunicacion;
    @Id
    @Column(name = "ID_INTERESADO_INT", nullable = false)
    private BigDecimal idInteresadoInt;
    @Column(nullable = false, length = 200)
    private String nombre;
    @Column(nullable = false, length = 200)
    private String usuario;

    public SgdInteresadoInt() {
    }

    public SgdInteresadoInt(String email, BigDecimal idComunicacion, BigDecimal idInteresadoInt, String nombre,
                            String usuario) {
        this.email = email;
        this.idComunicacion = idComunicacion;
        this.idInteresadoInt = idInteresadoInt;
        this.nombre = nombre;
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getIdComunicacion() {
        return idComunicacion;
    }

    public void setIdComunicacion(BigDecimal idComunicacion) {
        this.idComunicacion = idComunicacion;
    }

    public BigDecimal getIdInteresadoInt() {
        return idInteresadoInt;
    }

    public void setIdInteresadoInt(BigDecimal idInteresadoInt) {
        this.idInteresadoInt = idInteresadoInt;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
