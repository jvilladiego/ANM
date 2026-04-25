package co.gov.anm.sgd.transfer;

import java.util.List;

public class ProcesoComunicacionRequest {
    
    private ComunicacionRequest comunicacion;
    private List<AnexoComunicacionRequest> anexosList;
    private List<InteresadoRequest> interesadosList;


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
