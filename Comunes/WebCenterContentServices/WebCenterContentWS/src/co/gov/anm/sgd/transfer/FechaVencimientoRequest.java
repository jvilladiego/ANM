package co.gov.anm.sgd.transfer;

import java.util.Date;

public class FechaVencimientoRequest {
    
    private Integer diasRespuesta;
    private Date fechaRadicacion;


    public void setDiasRespuesta(Integer diasRespuesta) {
        this.diasRespuesta = diasRespuesta;
    }

    public Integer getDiasRespuesta() {
        return diasRespuesta;
    }

    public void setFechaRadicacion(Date fechaRadicacion) {
        this.fechaRadicacion = fechaRadicacion;
    }

    public Date getFechaRadicacion() {
        return fechaRadicacion;
    }
}
