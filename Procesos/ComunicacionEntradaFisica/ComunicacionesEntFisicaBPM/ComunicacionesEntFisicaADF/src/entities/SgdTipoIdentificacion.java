package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "SgdTipoIdentificacion.findAll", query = "select o from SgdTipoIdentificacion o") })
@Table(name = "SGD_TIPO_IDENTIFICACION")
public class SgdTipoIdentificacion implements Serializable {
    private static final long serialVersionUID = 8162641684854594117L;
    @Id
    @Column(nullable = false, length = 10)
    private String codigo;
    @Column(nullable = false, length = 200)
    private String nombre;

    public SgdTipoIdentificacion() {
    }

    public SgdTipoIdentificacion(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
