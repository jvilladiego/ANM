package co.gov.anm.comunicaciones.model.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "EntidadProductora.findAll", query = "select o from EntidadProductora o") })
@Table(name = "ANM_ENTIDADPRODUCTORA")
@IdClass(EntidadProductoraPK.class)
public class EntidadProductora implements Serializable {
    private static final long serialVersionUID = 7013778466190841544L;
    @Id
    @Column(nullable = false, length = 70)
    private String entidadproductora;
    @Id
    @Column(nullable = false)
    private BigDecimal identidadproductora;

    public EntidadProductora() {
    }

    public EntidadProductora(String entidadproductora, BigDecimal identidadproductora) {
        this.entidadproductora = entidadproductora;
        this.identidadproductora = identidadproductora;
    }

    public String getEntidadproductora() {
        return entidadproductora;
    }

    public void setEntidadproductora(String entidadproductora) {
        this.entidadproductora = entidadproductora;
    }

    public BigDecimal getIdentidadproductora() {
        return identidadproductora;
    }

    public void setIdentidadproductora(BigDecimal identidadproductora) {
        this.identidadproductora = identidadproductora;
    }
}
