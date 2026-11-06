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
@NamedQueries({ @NamedQuery(name = "SgdOcupacion.findAll", query = "select o from SgdOcupacion o") })
@Table(name = "SGD_OCUPACION")
public class SgdOcupacion implements Serializable {
    private static final long serialVersionUID = 1780424491775719152L;
    
    @Id
    @Column(name="ID_OCUPACION", nullable = false)
    private BigDecimal idOcupacion;
    
    @Column(nullable = false, length = 100)
    private String nombre;


    public SgdOcupacion() {
    }

    public BigDecimal getIdOcupacion() {
        return idOcupacion;
    }

    public void setIdOcupacion(BigDecimal idOcupacion) {
        this.idOcupacion = idOcupacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

