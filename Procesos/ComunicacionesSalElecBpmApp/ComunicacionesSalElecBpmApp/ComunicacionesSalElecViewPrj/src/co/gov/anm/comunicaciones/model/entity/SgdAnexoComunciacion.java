package co.gov.anm.comunicaciones.model.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "SgdAnexoComunciacion.findAll", query = "select o from SgdAnexoComunciacion o") })
@Table(name = "SGD_ANEXO_COMUNCIACION")
public class SgdAnexoComunciacion implements Serializable {
    private static final long serialVersionUID = 2759766678082769692L;
    @Column(nullable = false, length = 200)
    private String descripcion;
    @Column(name = "ENLACE_CONTENT", length = 200)
    private String enlaceContent;
    @Id
    @Column(name = "ID_ANEXO_COMUNICACION", nullable = false)
    private BigDecimal idAnexoComunicacion;
    @Column(name = "RUTA_LOCAL_CARGUE", length = 500)
    private String rutaLocalCargue;
    @ManyToOne
    @JoinColumn(name = "ID_COMUNICACION")
    private SgdComunicacion sgdComunicacion;

    public SgdAnexoComunciacion() {
    }

    public SgdAnexoComunciacion(String descripcion, String enlaceContent, BigDecimal idAnexoComunicacion,
                                SgdComunicacion sgdComunicacion, String rutaLocalCargue) {
        this.descripcion = descripcion;
        this.enlaceContent = enlaceContent;
        this.idAnexoComunicacion = idAnexoComunicacion;
        this.sgdComunicacion = sgdComunicacion;
        this.rutaLocalCargue = rutaLocalCargue;
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


    public String getRutaLocalCargue() {
        return rutaLocalCargue;
    }

    public void setRutaLocalCargue(String rutaLocalCargue) {
        this.rutaLocalCargue = rutaLocalCargue;
    }

    public SgdComunicacion getSgdComunicacion() {
        return sgdComunicacion;
    }

    public void setSgdComunicacion(SgdComunicacion sgdComunicacion) {
        this.sgdComunicacion = sgdComunicacion;
    }
}
