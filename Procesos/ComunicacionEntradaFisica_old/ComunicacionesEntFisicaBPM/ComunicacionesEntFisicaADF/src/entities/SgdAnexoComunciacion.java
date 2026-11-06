package entities;

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

/**
 * To create ID generator sequence SGD_ANEXO_COMUNCIACION_SEQ:
 * CREATE SEQUENCE SGD_ANEXO_COMUNCIACION_SEQ INCREMENT BY 50 START WITH 50;
 */
@Entity
@NamedQueries({ @NamedQuery(name = "SgdAnexoComunciacion.findAll", 
                            query = "select o from SgdAnexoComunciacion o"),
                @NamedQuery(name = "SgdAnexoComunciacion.findByIdCom", 
                            query = "select o from SgdAnexoComunciacion o where o.idComunicacion = :param"),
                @NamedQuery(name = "SgdAnexoComunciacion.findByNumRadicado", 
                            query = "select o from SgdAnexoComunciacion o where o.numeroRadicado = :param")
                })
@Table(name = "SGD_ANEXO_COMUNICACION")
@SequenceGenerator(name = "SGD_ANEXO_COMUNCIACION_SEQ", sequenceName = "SGD_ANEXO_COMUNCIACION_SEQ",
                   allocationSize = 1, initialValue = 1)
public class SgdAnexoComunciacion implements Serializable {
    private static final long serialVersionUID = -1087678465431898155L;
    
    @Id
    @Column(name = "ID_ANEXO_COMUNICACION", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SGD_ANEXO_COMUNCIACION_SEQ")
    private Long idAnexoComunicacion;
    @Column(nullable = false, length = 200)
    private String descripcion;
    @Column(name = "ENLACE_CONTENT", length = 200)
    private String enlaceContent;
    @Column(name = "RUTA_LOCAL_CARGUE", length = 500)
    private String rutaLocalCargue;
    @Column(name = "ID_COMUNICACION")
    private Long idComunicacion;
    @Column(name = "CANTIDAD")
    private Integer cantidad;
    @Column(name = "NUMERO_RADICADO")
    private String numeroRadicado;
    

    public SgdAnexoComunciacion() {
    }

    public SgdAnexoComunciacion(String descripcion, String enlaceContent, Long idAnexoComunicacion,
                                Long idComunicacion, String rutaLocalCargue) {
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

    public Long getIdAnexoComunicacion() {
        return idAnexoComunicacion;
    }

    public void setIdAnexoComunicacion(Long idAnexoComunicacion) {
        this.idAnexoComunicacion = idAnexoComunicacion;
    }


    public String getRutaLocalCargue() {
        return rutaLocalCargue;
    }

    public void setRutaLocalCargue(String rutaLocalCargue) {
        this.rutaLocalCargue = rutaLocalCargue;
    }

    public Long getIdComunicacion() {
        return idComunicacion;
    }

    public void setIdComunicacion(Long idComunicacion) {
        this.idComunicacion = idComunicacion;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setNumeroRadicado(String numeroRadicado) {
        this.numeroRadicado = numeroRadicado;
    }

    public String getNumeroRadicado() {
        return numeroRadicado;
    }
}
