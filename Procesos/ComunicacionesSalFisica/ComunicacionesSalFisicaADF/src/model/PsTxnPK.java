package model;

import java.io.Serializable;

import java.math.BigDecimal;

public class PsTxnPK implements Serializable {
    private Long collid;
    private BigDecimal id;

    public PsTxnPK() {
    }

    public PsTxnPK(Long collid, BigDecimal id) {
        this.collid = collid;
        this.id = id;
    }

    public boolean equals(Object other) {
        if (other instanceof PsTxnPK) {
            final PsTxnPK otherPsTxnPK = (PsTxnPK) other;
            final boolean areEqual = (otherPsTxnPK.collid.equals(collid) && otherPsTxnPK.id.equals(id));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public Long getCollid() {
        return collid;
    }

    public void setCollid(Long collid) {
        this.collid = collid;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }
}
