package co.gov.anm.sgd.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "TipoSolicitante.findAll", query = "select o from TipoSolicitante o"),
                @NamedQuery(name = "TipoSolicitante.findById", query = "select o from TipoSolicitante o where o.idTipoSolicitante = :idTipo")})
@Table(name = "SGD_TIPO_SOLICITANTE")
public class TipoSolicitante implements Serializable {
    private static final long serialVersionUID = -8309050008302843876L;

    @Id
    @Column(name="ID_TIPO_SOLICITANTE", nullable = false)
    private Long idTipoSolicitante;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @OneToMany(mappedBy = "tipoSolicitante", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<SgdInteresado> interesadoList;


    public TipoSolicitante() {
    }

    public Long getIdTipoSolicitante() {
        return idTipoSolicitante;
    }

    public void setIdTipoSolicitante(Long idTipoSolicitante) {
        this.idTipoSolicitante = idTipoSolicitante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public List<SgdInteresado> getInteresadoList() {
        return interesadoList;
    }

    public void setInteresadoList(List<SgdInteresado> interesadoList) {
        this.interesadoList = interesadoList;
    }
}
