package co.gov.anm.sgd.transfer;

import java.math.BigDecimal;


public class AnexoComunicacionRequest {
    
    private Integer idAnexo;
    private String descripcion;
    private String enlaceContent;
    private Integer idComunicacion;
    private String rutaLocalCargue;
    private BigDecimal cantidad;
    private String nroRadicado;


    public void setIdAnexo(Integer idAnexo) {
        this.idAnexo = idAnexo;
    }

    public Integer getIdAnexo() {
        return idAnexo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setEnlaceContent(String enlaceContent) {
        this.enlaceContent = enlaceContent;
    }

    public String getEnlaceContent() {
        return enlaceContent;
    }

    public void setIdComunicacion(Integer idComunicacion) {
        this.idComunicacion = idComunicacion;
    }

    public Integer getIdComunicacion() {
        return idComunicacion;
    }

    public void setRutaLocalCargue(String rutaLocalCargue) {
        this.rutaLocalCargue = rutaLocalCargue;
    }

    public String getRutaLocalCargue() {
        return rutaLocalCargue;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setNroRadicado(String nroRadicado) {
        this.nroRadicado = nroRadicado;
    }

    public String getNroRadicado() {
        return nroRadicado;
    }
    
    @Override
    public String toString(){
        return "\n"+this.getClass().getName()+":\n" +
        "idAnexo : "+idAnexo+":\n" +
         "descripcion : '"+descripcion+"'\n" +
         "enlaceContent : '"+enlaceContent+"'\n" +
         "idComunicacion : '"+idComunicacion+"'\n" +
         "rutaLocalCargue : '"+rutaLocalCargue+"'\n" +
         "cantidad : '"+cantidad+"\n" +
         "nroRadicado : '"+nroRadicado+"'\n";
    }
    
}
