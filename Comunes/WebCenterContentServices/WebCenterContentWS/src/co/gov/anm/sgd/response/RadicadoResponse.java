package co.gov.anm.sgd.response;


public class RadicadoResponse extends WCCResponse {
    
    private String noRadicado;
    
    public RadicadoResponse () {
        super();
    }
    
    public RadicadoResponse (String errorCode) {
        super(errorCode);
    }

    public void setNoRadicado(String noRadicado) {
        this.noRadicado = noRadicado;
    }

    public String getNoRadicado() {
        return noRadicado;
    }
}
