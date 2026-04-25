package entities;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "TipoSolicitante.findAll", query = "select o from TipoSolicitante o") })
@Table(name = "SGD_TIPO_SOLICITANTE")
public class TipoSolicitante implements Serializable {
    private static final long serialVersionUID = -6470148654110955297L;
    
    @Id
    @Column(name="ID_TIPO_SOLICITANTE", nullable = false)
    private BigDecimal idTipoSolicitante;
    
    @Column(nullable = false, length = 100)
    private String nombre;


    public TipoSolicitante() {
    }

    public BigDecimal getIdTipoSolicitante() {
        return idTipoSolicitante;
    }

    public void setIdTipoSolicitante(BigDecimal idTipoSolicitante) {
        this.idTipoSolicitante = idTipoSolicitante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}


