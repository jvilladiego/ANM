package co.gov.anm.comunicaciones.model.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "TipoDocumental.findAll", query = "select o from TipoDocumental o") })
@Table(name = "ANM_TIPODOCUMENTAL_TB")
public class TipoDocumental implements Serializable {
    private static final long serialVersionUID = 8180253957555038842L;
    @Id
    @Column(nullable = false)
    private BigDecimal idtipodocumental;
    @Column(length = 120)
    private String tipodocumental;

    public TipoDocumental() {
    }

    public TipoDocumental(BigDecimal idtipodocumental, String tipodocumental) {
        this.idtipodocumental = idtipodocumental;
        this.tipodocumental = tipodocumental;
    }

    public BigDecimal getIdtipodocumental() {
        return idtipodocumental;
    }

    public void setIdtipodocumental(BigDecimal idtipodocumental) {
        this.idtipodocumental = idtipodocumental;
    }

    public String getTipodocumental() {
        return tipodocumental;
    }

    public void setTipodocumental(String tipodocumental) {
        this.tipodocumental = tipodocumental;
    }
}
