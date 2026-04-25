package co.gov.anm.sgd.transfer;

import java.util.List;

public class ProcesoComunicacionResponse {
    
    private String codigoRespuesta;
    private String mensajeRespuesta;
    
    private ComunicacionRequest comunicacion;
    private List<AnexoComunicacionRequest> anexosList;
    private List<InteresadoRequest> interesadosList;

    public void setCodigoRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setMensajeRespuesta(String mensajeRespuesta) {
        this.mensajeRespuesta = mensajeRespuesta;
    }

    public String getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    public void setComunicacion(ComunicacionRequest comunicacion) {
        this.comunicacion = comunicacion;
    }

    public ComunicacionRequest getComunicacion() {
        return comunicacion;
    }

    public void setAnexosList(List<AnexoComunicacionRequest> anexosList) {
        this.anexosList = anexosList;
    }

    public List<AnexoComunicacionRequest> getAnexosList() {
        return anexosList;
    }

    public void setInteresadosList(List<InteresadoRequest> interesadosList) {
        this.interesadosList = interesadosList;
    }

    public List<InteresadoRequest> getInteresadosList() {
        return interesadosList;
    }
}
