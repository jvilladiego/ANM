package co.gov.anm.sgd.response;

import java.util.List;

public class AnexosRadicadoResponse {
   
    public AnexosRadicadoResponse () {
        super();
    }
    
    private String codigoEstado = null;
    private String mensajeRsp = null;
    private List<AnexoRadicado> anexosList;

    public void setCodigoEstado(String codigoEstado) {
        this.codigoEstado = codigoEstado;
    }

    public String getCodigoEstado() {
        return codigoEstado;
    }

    public void setMensajeRsp(String mensajeRsp) {
        this.mensajeRsp = mensajeRsp;
    }

    public String getMensajeRsp() {
        return mensajeRsp;
    }

    public void setAnexosList(List<AnexoRadicado> anexosList) {
        this.anexosList = anexosList;
    }

    public List<AnexoRadicado> getAnexosList() {
        return anexosList;
    }
}
