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
@NamedQueries({ @NamedQuery(name = "AnmTipodtalseguntramite.findAll",
                            query = "select o from AnmTipodtalseguntramite o"),
                @NamedQuery(name = "AnmTipodtalseguntramite.tiposDocumentalesTramites", 
                            query = "select o from AnmTipodtalseguntramite o where o.idtramite=:p_id_tramite")
                })
@Table(name = "ANM_TIPODTALSEGUNTRAMITE")
public class AnmTipodtalseguntramite implements Serializable {
    private static final long serialVersionUID = 7431122060355427558L;
    @Id
    @Column(name = "ID_TIPODTALSEGUNTRAMITE", nullable = false)
    private BigDecimal idTipodtalseguntramite;
    private BigDecimal idtipodtal;
    private BigDecimal idtramite;
    @Column(length = 120)
    private String nombretipodocumental;

    public AnmTipodtalseguntramite() {
    }

    public AnmTipodtalseguntramite(BigDecimal idTipodtalseguntramite, BigDecimal idtipodtal, BigDecimal idtramite,
                                   String nombretipodocumental) {
        this.idTipodtalseguntramite = idTipodtalseguntramite;
        this.idtipodtal = idtipodtal;
        this.idtramite = idtramite;
        this.nombretipodocumental = nombretipodocumental;
    }

    public BigDecimal getIdTipodtalseguntramite() {
        return idTipodtalseguntramite;
    }

    public void setIdTipodtalseguntramite(BigDecimal idTipodtalseguntramite) {
        this.idTipodtalseguntramite = idTipodtalseguntramite;
    }

    public BigDecimal getIdtipodtal() {
        return idtipodtal;
    }

    public void setIdtipodtal(BigDecimal idtipodtal) {
        this.idtipodtal = idtipodtal;
    }

    public BigDecimal getIdtramite() {
        return idtramite;
    }

    public void setIdtramite(BigDecimal idtramite) {
        this.idtramite = idtramite;
    }

    public String getNombretipodocumental() {
        return nombretipodocumental;
    }

    public void setNombretipodocumental(String nombretipodocumental) {
        this.nombretipodocumental = nombretipodocumental;
    }
}