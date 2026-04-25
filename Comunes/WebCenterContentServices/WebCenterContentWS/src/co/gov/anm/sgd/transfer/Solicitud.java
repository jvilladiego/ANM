package co.gov.anm.sgd.transfer;

import java.io.Serializable;

import java.util.HashMap;

public class Solicitud implements Serializable {
    @SuppressWarnings("compatibility:8740397008867865781")
    private static final long serialVersionUID = 1L;

    private String sistemaOrigen;
    private Credenciales credenciales;
    private HashMap<String, String> metadatosList = new HashMap<String, String>();

    public void setCredenciales(Credenciales credenciales) {
        this.credenciales = credenciales;
    }

    public Credenciales getCredenciales() {
        return credenciales;
    }

    public void setMetadatosList(HashMap<String, String> metadatosList) {
        this.metadatosList = metadatosList;
    }

    public HashMap<String, String> getMetadatosList() {
        return metadatosList;
    }

    public void setSistemaOrigen(String sistemaOrigen) {
        this.sistemaOrigen = sistemaOrigen;
    }

    public String getSistemaOrigen() {
        return sistemaOrigen;
    }
}
