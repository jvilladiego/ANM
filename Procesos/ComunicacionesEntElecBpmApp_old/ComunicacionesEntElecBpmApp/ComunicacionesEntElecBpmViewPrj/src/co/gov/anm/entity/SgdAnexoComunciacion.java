package co.gov.anm.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "SgdAnexoComunciacion.findAll", query = "select o from SgdAnexoComunciacion o") })
@Table(name = "SGD_ANEXO_COMUNCIACION")
public class SgdAnexoComunciacion implements Serializable {
    private static final long serialVersionUID = 1609977072416580412L;
    @Column(nullable = false, length = 200)
    private String descripcion;
    @Column(name = "ENLACE_CONTENT", length = 200)
    private String enlaceContent;
    @Id
    @Column(name = "ID_ANEXO_COMUNICACION", nullable = false)
    private BigDecimal idAnexoComunicacion;
    @Column(name = "ID_COMUNICACION", nullable = false)
    private BigDecimal idComunicacion;
    @Column(name = "RUTA_LOCAL_CARGUE", length = 500)
    private String rutaLocalCargue;

    public SgdAnexoComunciacion() {
    }

    public SgdAnexoComunciacion(String descripcion, String enlaceContent, BigDecimal idAnexoComunicacion,
                                BigDecimal idComunicacion, String rutaLocalCargue) {
        this.descripcion = descripcion;
        this.enlaceContent = enlaceContent;
        this.idAnexoComunicacion = idAnexoComunicacion;
        this.idComunicacion = idComunicacion;
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

    public BigDecimal getIdComunicacion() {
        return idComunicacion;
    }

    public void setIdComunicacion(BigDecimal idComunicacion) {
        this.idComunicacion = idComunicacion;
    }

    public String getRutaLocalCargue() {
        return rutaLocalCargue;
    }

    public void setRutaLocalCargue(String rutaLocalCargue) {
        this.rutaLocalCargue = rutaLocalCargue;
    }
}
