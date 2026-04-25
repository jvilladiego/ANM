package co.gov.anm.sgd.transfer;


public class ValidacionExpediente {

    private String numeroPlaca;
    private Credenciales credenciales;

    public void setNumeroPlaca(String numeroPlaca) {
        this.numeroPlaca = numeroPlaca;
    }

    public String getNumeroPlaca() {
        return numeroPlaca;
    }

    public void setCredenciales(Credenciales credenciales) {
        this.credenciales = credenciales;
    }

    public Credenciales getCredenciales() {
        return credenciales;
    }
}
