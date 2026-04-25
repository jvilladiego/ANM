package co.gov.anm.sgd.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "SgdAnexoComunicacion.findAll", query = "select o from SgdAnexoComunicacion o"),
                @NamedQuery(name = "SgdAnexoComunicacion.findByIdComunicacion", query = "select o from SgdAnexoComunicacion o where o.sgdComunicacion1 = :idComunicacion"),
                @NamedQuery(name = "SgdAnexoComunicacion.deleteByIdComunicacion", query = "delete from SgdAnexoComunicacion o where o.sgdComunicacion1 = :idComunicacion")
             })
@Table(name = "SGD_ANEXO_COMUNICACION")
public class SgdAnexoComunicacion implements Serializable {
    private static final long serialVersionUID = -1142779894349742207L;
    private BigDecimal cantidad;
    @Column(nullable = false, length = 200)
    private String descripcion;
    @Column(name = "ENLACE_CONTENT", length = 200)
    private String enlaceContent;
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SGD_ANEXO_COMUNCIACION_SEQ")
    @SequenceGenerator(name="SGD_ANEXO_COMUNCIACION_SEQ", sequenceName="SGD_ANEXO_COMUNCIACION_SEQ", allocationSize=1)
    @Id
    @Column(name = "ID_ANEXO_COMUNICACION", nullable = false)
    private BigDecimal idAnexoComunicacion;
    @Column(name = "NUMERO_RADICADO", length = 20)
    private String numeroRadicado;
    @Column(name = "RUTA_LOCAL_CARGUE", length = 500)
    private String rutaLocalCargue;
    @ManyToOne
    @JoinColumn(name = "ID_COMUNICACION")
    private SgdComunicacion sgdComunicacion1;

    public SgdAnexoComunicacion() {
    }

    public SgdAnexoComunicacion(BigDecimal cantidad, String descripcion, String enlaceContent,
                                BigDecimal idAnexoComunicacion, SgdComunicacion sgdComunicacion1, String numeroRadicado,
                                String rutaLocalCargue) {
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.enlaceContent = enlaceContent;
        this.idAnexoComunicacion = idAnexoComunicacion;
        this.sgdComunicacion1 = sgdComunicacion1;
        this.numeroRadicado = numeroRadicado;
        this.rutaLocalCargue = rutaLocalCargue;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEnlaceContent() {
        return enlaceContent;
    }

    public void setEnlaceContent(String enlaceContent) {
        this.enlaceContent = enlaceContent;
    }

    public BigDecimal getIdAnexoComunicacion() {
        return idAnexoComunicacion;
    }

    public void setIdAnexoComunicacion(BigDecimal idAnexoComunicacion) {
        this.idAnexoComunicacion = idAnexoComunicacion;
    }


    public String getNumeroRadicado() {
        return numeroRadicado;
    }

    public void setNumeroRadicado(String numeroRadicado) {
        this.numeroRadicado = numeroRadicado;
    }

    public String getRutaLocalCargue() {
        return rutaLocalCargue;
    }

    public void setRutaLocalCargue(String rutaLocalCargue) {
        this.rutaLocalCargue = rutaLocalCargue;
    }

    public SgdComunicacion getSgdComunicacion1() {
        return sgdComunicacion1;
    }

    public void setSgdComunicacion1(SgdComunicacion sgdComunicacion1) {
        this.sgdComunicacion1 = sgdComunicacion1;
    }
}
