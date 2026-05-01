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
@NamedQueries({ @NamedQuery(name = "UnidadAdministrativa.findAll", query = "select o from UnidadAdministrativa o"), 
                @NamedQuery(name = "UnidadAdministrativa.findByCod", query = "select o from UnidadAdministrativa o where o.codUnidadadministrativa=:p_codUnidadadministrativa") 
                })
@Table(name = "ANM_UNIDADADMINISTRATIVA_TB")
public class UnidadAdministrativa implements Serializable {
    private static final long serialVersionUID = 6566746512343174036L;
    @Column(name = "COD_UNIDADADMINISTRATIVA")
    private BigDecimal codUnidadadministrativa;
    @Id
    @Column(name = "ID_UNIDADADMINISTRATIVA", nullable = false)
    private BigDecimal idUnidadadministrativa;
    @Column(length = 100)
    private String nombreunidadadministrativa;

    public UnidadAdministrativa() {
    }

    public UnidadAdministrativa(BigDecimal codUnidadadministrativa, BigDecimal idUnidadadministrativa,
                                String nombreunidadadministrativa) {
        this.codUnidadadministrativa = codUnidadadministrativa;
        this.idUnidadadministrativa = idUnidadadministrativa;
        this.nombreunidadadministrativa = nombreunidadadministrativa;
    }

    public BigDecimal getCodUnidadadministrativa() {
        return codUnidadadministrativa;
    }

    public void setCodUnidadadministrativa(BigDecimal codUnidadadministrativa) {
        this.codUnidadadministrativa = codUnidadadministrativa;
    }

    public BigDecimal getIdUnidadadministrativa() {
        return idUnidadadministrativa;
    }

    public void setIdUnidadadministrativa(BigDecimal idUnidadadministrativa) {
        this.idUnidadadministrativa = idUnidadadministrativa;
    }

    public String getNombreunidadadministrativa() {
        return nombreunidadadministrativa;
    }

    public void setNombreunidadadministrativa(String nombreunidadadministrativa) {
        this.nombreunidadadministrativa = nombreunidadadministrativa;
    }
}

