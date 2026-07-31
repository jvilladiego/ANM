package model;

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
@NamedQueries({ @NamedQuery(name = "SgdAnexoComunciacion.findAll", 
                            query = "select o from SgdAnexoComunciacion o"),
                @NamedQuery(name = "SgdAnexoComunciacion.findByNumRadicado", 
                            query = "select o from SgdAnexoComunciacion o where o.numeroRadicado = :param")
                })
@Table(name = "SGD_ANEXO_COMUNICACION")
public class SgdAnexoComunciacion implements Serializable {
    private static final long serialVersionUID = -5947468404256711855L;
    
    @Id
    @Column(name = "ID_ANEXO_COMUNICACION", nullable = false)
    private BigDecimal idAnexoComunicacion;
    @Column(nullable = false, length = 200)
    private String descripcion;
    @Column(name = "ENLACE_CONTENT", length = 200)
    private String enlaceContent;
    @Column(name = "RUTA_LOCAL_CARGUE", length = 500)
    private String rutaLocalCargue;
    @Column(name = "ID_COMUNICACION")
    private Long sgdComunicacion;
    @Column(name = "CANTIDAD")
    private Long cantidad;
    @Column(name = "NUMERO_RADICADO")
    private String numeroRadicado;



    public SgdAnexoComunciacion() {
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

    public Long getSgdComunicacion() {
        return sgdComunicacion;
    }

    public void setSgdComunicacion(Long sgdComunicacion) {
        this.sgdComunicacion = sgdComunicacion;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setNumeroRadicado(String numeroRadicado) {
        this.numeroRadicado = numeroRadicado;
    }

    public String getNumeroRadicado() {
        return numeroRadicado;
    }
}
