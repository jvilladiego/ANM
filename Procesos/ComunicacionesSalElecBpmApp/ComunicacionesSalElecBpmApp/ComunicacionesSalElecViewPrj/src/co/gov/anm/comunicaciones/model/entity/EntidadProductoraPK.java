package co.gov.anm.comunicaciones.model.entity;

import java.io.Serializable;

import java.math.BigDecimal;

public class EntidadProductoraPK implements Serializable {
    private String entidadproductora;
    private BigDecimal identidadproductora;

    public EntidadProductoraPK() {
    }

    public EntidadProductoraPK(String entidadproductora, BigDecimal identidadproductora) {
        this.entidadproductora = entidadproductora;
        this.identidadproductora = identidadproductora;
    }

    public boolean equals(Object other) {
        if (other instanceof EntidadProductoraPK) {
            final EntidadProductoraPK otherEntidadProductoraPK = (EntidadProductoraPK) other;
            final boolean areEqual =
                (otherEntidadProductoraPK.entidadproductora.equals(entidadproductora) &&
                 otherEntidadProductoraPK.identidadproductora.equals(identidadproductora));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
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
