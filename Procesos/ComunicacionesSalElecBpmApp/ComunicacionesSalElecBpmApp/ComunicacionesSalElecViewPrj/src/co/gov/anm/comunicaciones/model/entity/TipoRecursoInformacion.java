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
@NamedQueries({ @NamedQuery(name = "TipoRecursoInformacion.findAll",
                            query = "select o from TipoRecursoInformacion o") })
@Table(name = "ANM_TIPORECURSOINFORMACION_TB")
public class TipoRecursoInformacion implements Serializable {
    private static final long serialVersionUID = -8525025474168986861L;
    @Id
    @Column(nullable = false)
    private BigDecimal idtiporecurinformacion;
    @Column(length = 30)
    private String tiporecursoinformacion;

    public TipoRecursoInformacion() {
    }

    public TipoRecursoInformacion(BigDecimal idtiporecurinformacion, String tiporecursoinformacion) {
        this.idtiporecurinformacion = idtiporecurinformacion;
        this.tiporecursoinformacion = tiporecursoinformacion;
    }

    public BigDecimal getIdtiporecurinformacion() {
        return idtiporecurinformacion;
    }

    public void setIdtiporecurinformacion(BigDecimal idtiporecurinformacion) {
        this.idtiporecurinformacion = idtiporecurinformacion;
    }

    public String getTiporecursoinformacion() {
        return tiporecursoinformacion;
    }

    public void setTiporecursoinformacion(String tiporecursoinformacion) {
        this.tiporecursoinformacion = tiporecursoinformacion;
    }
}
