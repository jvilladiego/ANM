package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "SgdPais.findAll", 
                            query = "select o from SgdPais o") })
@Table(name = "SGD_PAIS")
public class SgdPais implements Serializable {
    private static final long serialVersionUID = -3253978113588298999L;

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;
    @Column(name = "ISO", length = 2)
    private String iso;
    @Column(name = "NOMBRE", length = 80)
    private String nombre;
    
    public SgdPais() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getIso() {
        return iso;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
