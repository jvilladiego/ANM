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
@NamedQueries({ @NamedQuery(name = "SgdPais.findAll", query = "select o from SgdPais o order by o.nombre") })
@Table(name = "SGD_PAIS")
public class SgdPais implements Serializable {
    private static final long serialVersionUID = -7998610658629989657L;
    @Id
    @Column(nullable = false)
    private int id;
    private String iso;
    @Column(length = 80)
    private String nombre;

    public SgdPais() {
    }

    public SgdPais(int id, String iso, String nombre) {
        this.id = id;
        this.iso = iso;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
