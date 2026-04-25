package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "AnmUnidadAdministrativaTb.findAll",
                            query = "select o from AnmUnidadAdministrativaTb o order by o.codUnidadAdministrativa"),
                @NamedQuery(name = "AnmUnidadAdministrativaTb.findByCodigo",
                            query = "select o from AnmUnidadAdministrativaTb o where o.codUnidadAdministrativa = :param")
                })
@Table(name = "ANM_UNIDADADMINISTRATIVA_TB")
public class AnmUnidadAdministrativaTb implements Serializable {
    
    private static final long serialVersionUID = -802530459142211226L;
    
    @Id
    @Column(name = "ID_UNIDADADMINISTRATIVA", nullable = false)
    private Integer idUnidadadministrativa;
    @Column(name = "COD_UNIDADADMINISTRATIVA")
    private Integer codUnidadAdministrativa;
    private String nombreUnidadadministrativa;
    @Column(name = "COD_CMC")
    private Integer codCmc;

    public AnmUnidadAdministrativaTb() {
    }

    public Integer getIdUnidadadministrativa() {
        return idUnidadadministrativa;
    }

    public void setIdUnidadadministrativa(Integer idUnidadadministrativa) {
        this.idUnidadadministrativa = idUnidadadministrativa;
    }

    public Integer getCodUnidadAdministrativa() {
        return codUnidadAdministrativa;
    }

    public void setCodUnidadAdministrativa(Integer codUnidadAdministrativa) {
        this.codUnidadAdministrativa = codUnidadAdministrativa;
    }

    public String getNombreUnidadadministrativa() {
        return nombreUnidadadministrativa;
    }

    public void setNombreUnidadadministrativa(String nombreUnidadadministrativa) {
        this.nombreUnidadadministrativa = nombreUnidadadministrativa;
    }

    public void setCodCmc(Integer codCmc) {
        this.codCmc = codCmc;
    }

    public Integer getCodCmc() {
        return codCmc;
    }
}
