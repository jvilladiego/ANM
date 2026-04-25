package co.gov.anm.sgd.transfer;


public class ModificacionExpediente {
    
    private String numeroPlaca;
    private String destino;
    private Integer tipoModificacion;
    private Credenciales credenciales;

    public void setTipoModificacion(Integer tipoModificacion) {
        this.tipoModificacion = tipoModificacion;
    }

    public Integer getTipoModificacion() {
        return tipoModificacion;
    }

    public void setNumeroPlaca(String numeroPlaca) {
        this.numeroPlaca = numeroPlaca;
    }

    public String getNumeroPlaca() {
        return numeroPlaca;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDestino() {
        return destino;
    }

    public void setCredenciales(Credenciales credenciales) {
        this.credenciales = credenciales;
    }

    public Credenciales getCredenciales() {
        return credenciales;
    }
}
