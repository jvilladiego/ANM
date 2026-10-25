package co.gov.anm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "SgdTipoIdentificacion.findAll", query = "select o from SgdTipoIdentificacion o order by o.codigo") })
@Table(name = "SGD_TIPO_IDENTIFICACION")
public class SgdTipoIdentificacion implements Serializable {
    private static final long serialVersionUID = 833693247378245750L;
    @Id
    @Column(nullable = false, length = 20)
    private String codigo;
    @Id
    @Column(name = "ID_TIPO_IDENTIFICACION", nullable = false)
    private int idTipoIdentificacion;
    @Column(nullable = false, length = 200)
    private String nombre;

    public SgdTipoIdentificacion() {
    }

    public SgdTipoIdentificacion(String codigo, int idTipoIdentificacion, String nombre) {
        this.codigo = codigo;
        this.idTipoIdentificacion = idTipoIdentificacion;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getIdTipoIdentificacion() {
        return idTipoIdentificacion;
    }

    public void setIdTipoIdentificacion(int idTipoIdentificacion) {
        this.idTipoIdentificacion = idTipoIdentificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
