package co.gov.anm.sgd.model;

import java.io.Serializable;

public class GrafoFirmaDigital implements Serializable{
    @SuppressWarnings("compatibility:-6582898867112456695")
    private static final long serialVersionUID = 1L;
    private String nombreUsuario;
    private String cargoUsuario;
    private String rutaGrafo;
    

    public GrafoFirmaDigital() {
        super();
    }

    public GrafoFirmaDigital(String nombreUsuario, String cargoUsuario, String rutaGrafo) {
        this.nombreUsuario = nombreUsuario;
        this.cargoUsuario = cargoUsuario;
        this.rutaGrafo = rutaGrafo;
    }


    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setCargoUsuario(String cargoUsuario) {
        this.cargoUsuario = cargoUsuario;
    }

    public String getCargoUsuario() {
        return cargoUsuario;
    }

    public void setRutaGrafo(String rutaGrafo) {
        this.rutaGrafo = rutaGrafo;
    }

    public String getRutaGrafo() {
        return rutaGrafo;
    }
    
}
