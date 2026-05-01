package co.gov.anm.comunicaciones.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "Tramite.findAll", 
                                query = "select o from Tramite o order by o.tramite") })
@Table(name = "ANM_TRAMITE_TB")
public class Tramite implements Serializable {
    private static final long serialVersionUID = -3387083038976152797L;
    @Id
    @Column(nullable = false)
    private BigDecimal idtramite;
    @Column(length = 100)
    private String tramite;

    public Tramite() {
    }

    public Tramite(BigDecimal idtramite, String tramite) {
        this.idtramite = idtramite;
        this.tramite = tramite;
    }

    public BigDecimal getIdtramite() {
        return idtramite;
    }

    public void setIdtramite(BigDecimal idtramite) {
        this.idtramite = idtramite;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }
}
